/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.excisemovementcontrolsystemapi.services

import cats.data.OptionT
import cats.syntax.all._
import org.apache.pekko.Done
import play.api.{Configuration, Logging}
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.{MessageConnector, TraderMovementConnector}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages._
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.model.{Message, Movement}
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.{BoxIdRepository, ErnRetrievalRepository, MovementRepository}
import uk.gov.hmrc.excisemovementcontrolsystemapi.services.MessageService.UpdateOutcome
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.{DateTimeService, EmcsUtils}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.mongo.lock.{LockService, MongoLockRepository}

import java.time.Instant
import javax.inject.{Inject, Singleton}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class MessageService @Inject() (
  configuration: Configuration,
  movementRepository: MovementRepository,
  ernRetrievalRepository: ErnRetrievalRepository,
  boxIdRepository: BoxIdRepository,
  messageConnector: MessageConnector,
  traderMovementConnector: TraderMovementConnector,
  dateTimeService: DateTimeService,
  correlationIdService: CorrelationIdService,
  emcsUtils: EmcsUtils,
  auditService: AuditService,
  mongoLockRepository: MongoLockRepository
)(implicit executionContext: ExecutionContext)
    extends Logging {

  private val throttleCutoff: FiniteDuration =
    configuration.get[FiniteDuration]("microservice.services.eis.throttle-cutoff")

  private val migrateLastUpdatedCutoff: Instant =
    Instant.parse(configuration.get[String]("migrateLastUpdatedCutoff"))

  def updateAllMessages(erns: Set[String])(implicit hc: HeaderCarrier): Future[Done] =
    erns.toSeq
      .traverse { ern =>
        ernRetrievalRepository.getLastRetrieved(ern).flatMap { lastRetrieved =>
          updateMessages(ern, lastRetrieved).recover { case NonFatal(error) =>
            logger.warn(s"[MessageService]: Failed to update messages", error)
            Done
          }
        }
      }
      .as(Done)

  def updateMessages(ern: String, lastRetrieved: Option[Instant])(implicit hc: HeaderCarrier): Future[UpdateOutcome] = {
    val lockService = LockService(mongoLockRepository, ern, throttleCutoff)
    lockService
      .withLock {
        val now = dateTimeService.timestamp()
        // Temporary migration code to update movement's lastUpdated
        if (shouldMigrateLastUpdated(lastRetrieved)) {
          movementRepository.migrateLastUpdated(ern).recover { case NonFatal(error) =>
            logger.warn(s"[MessageService]: Failed to migrate lastUpdated", error)
            Done
          }
        }
        if (shouldProcessNewMessages(lastRetrieved, now)) {
          for {
            boxIds <- getBoxIds(ern)
            _      <- processNewMessages(ern, boxIds)
            _      <- ernRetrievalRepository.setLastRetrieved(ern, now)
          } yield UpdateOutcome.Updated
        } else {
          Future.successful(UpdateOutcome.NotUpdatedThrottled)
        }
      }
      .map(_.getOrElse(UpdateOutcome.Locked))
  }

  private def shouldMigrateLastUpdated(maybeLastRetrieved: Option[Instant]): Boolean =
    //noinspection MapGetOrElseBoolean
    maybeLastRetrieved.map(_.isBefore(migrateLastUpdatedCutoff)).getOrElse(false)

  private def shouldProcessNewMessages(maybeLastRetrieved: Option[Instant], now: Instant): Boolean = {
    val cutoffTime = now.minus(throttleCutoff.length, throttleCutoff.unit.toChronoUnit)
    //noinspection MapGetOrElseBoolean
    maybeLastRetrieved.map(_.isBefore(cutoffTime)).getOrElse(true)
  }

  private def getBoxIds(ern: String): Future[Set[String]] =
    boxIdRepository.getBoxIds(ern)

  private def processNewMessages(ern: String, boxIds: Set[String])(implicit hc: HeaderCarrier): Future[Done] = {
    logger.info(s"[MessageService]: Processing new messages")
    for {
      response <- messageConnector.getNewMessages(ern)
      _        <- updateMovements(ern, response.messages, boxIds)
      _        <- acknowledgeAndContinue(response, ern, boxIds)
    } yield Done
  }

  private def acknowledgeAndContinue(response: GetMessagesResponse, ern: String, boxIds: Set[String])(implicit
    hc: HeaderCarrier
  ): Future[Done] =
    if (response.messageCount == 0) {
      Future.successful(Done)
    } else {
      messageConnector.acknowledgeMessages(ern).flatMap { _ =>
        if (response.messageCount > response.messages.size) {
          processNewMessages(ern, boxIds)
        } else {
          Future.successful(Done)
        }
      }
    }

  private def updateMovements(ern: String, messages: Seq[IEMessage], boxIds: Set[String])(implicit
    hc: HeaderCarrier
  ): Future[Done] = {
    logger.info(s"[MessageService]: Updating movements")
    if (messages.nonEmpty) {
      movementRepository
        .getAllBy(ern)
        .flatMap { movements =>
          messages
            .foldLeft(Future.successful(Seq.empty[Movement])) { (accumulated, message) =>
              for {
                accumulatedMovements <- accumulated
                updatedMovements     <- updateOrCreateMovements(ern, movements, accumulatedMovements, message, boxIds)
              } yield (updatedMovements ++ accumulatedMovements)
                .distinctBy { movement =>
                  (movement.localReferenceNumber, movement.consignorId, movement.administrativeReferenceCode)
                }
            }
            .flatMap {
              _.traverse(movementRepository.save)
            }
        }
        .as(Done)
    } else {
      Future.successful(Done)
    }
  }

  private def updateOrCreateMovements(
    ern: String,
    movements: Seq[Movement],
    updatedMovements: Seq[Movement],
    message: IEMessage,
    boxIds: Set[String]
  )(implicit hc: HeaderCarrier): Future[Seq[Movement]] =
    findMovementsForMessage(movements, updatedMovements, message).flatMap { matchedMovements =>
      if (matchedMovements.nonEmpty) {
        Future.successful {
          matchedMovements.map { movement =>
            updateMovement(ern, movement, message, boxIds)
          }
        }
      } else {
        createMovement(ern, message, boxIds)
          .map(movement => (movement +: updatedMovements.map(Some(_))).flatten)
      }
    }

  private def updateMovement(recipient: String, movement: Movement, message: IEMessage, boxIds: Set[String]): Movement =
    if (
      movement.messages.exists(m =>
        m.messageId == message.messageIdentifier
          && m.recipient.equalsIgnoreCase(recipient)
      )
    ) {
      movement
    } else {
      val timestamp = dateTimeService.timestamp()
      movement.copy(
        messages = getUpdatedMessages(recipient, movement, message, boxIds, timestamp),
        administrativeReferenceCode = getArc(movement, message),
        consigneeId = getConsignee(movement, message),
        lastUpdated = timestamp
      )
    }

  private def getUpdatedMessages(
    recipient: String,
    movement: Movement,
    message: IEMessage,
    boxIds: Set[String],
    timestamp: Instant
  ): Seq[Message] =
    movement.messages :+ convertMessage(recipient, message, boxIds, timestamp)

  private def findMovementsForMessage(
    movements: Seq[Movement],
    updatedMovements: Seq[Movement],
    message: IEMessage
  ): Future[Seq[Movement]] =
    (findByArc(updatedMovements, message) orElse
      findByLrn(updatedMovements, message) orElse
      findByArc(movements, message) orElse
      findByLrn(movements, message) orElse
      findByArcInMessage(message)).getOrElse(Seq.empty)

  private def getConsignee(movement: Movement, message: IEMessage): Option[String] =
    message match {
      case ie801: IE801Message => ie801.consigneeId
      case ie813: IE813Message => ie813.consigneeId orElse movement.consigneeId
      case _                   => movement.consigneeId
    }

  private def getArc(movement: Movement, message: IEMessage): Option[String] =
    message match {
      case ie801: IE801Message =>
        movement.administrativeReferenceCode orElse ie801.administrativeReferenceCode.flatten.headOption
      case _                   => movement.administrativeReferenceCode
    }

  private def createMovement(ern: String, message: IEMessage, boxIds: Set[String])(implicit
    hc: HeaderCarrier
  ): Future[Option[Movement]] =
    message match {
      case ie704: IE704Message if ie704.localReferenceNumber.isDefined =>
        Future.successful(createMovementFromIE704(ern, ie704, boxIds))
      case ie801: IE801Message                                         => Future.successful(Some(createMovementFromIE801(ern, ie801, boxIds)))
      case _                                                           => createMovementFromTraderMovement(ern, message, boxIds)
    }

  private def createMovementFromTraderMovement(ern: String, message: IEMessage, boxIds: Set[String])(implicit
    hc: HeaderCarrier
  ): Future[Option[Movement]] =
    message.administrativeReferenceCode.flatten.headOption
      .map { arc =>
        val traderMovementMessages = traderMovementConnector.getMovementMessages(ern, arc)
        val movement               =
          traderMovementMessages.map((messages: Seq[IEMessage]) =>
            buildMovementFromTraderMovement(messages, boxIds, message, ern)
          )
        movement
      }
      .getOrElse {
        // Auditing here because we only want to audit on the message we've picked up rather than the messages from `getMovementMessages`
        Future.successful(auditMessageForNoMovement(message))
      }

  private def buildMovementFromTraderMovement(
    messages: Seq[IEMessage],
    boxIds: Set[String],
    originatingMessage: IEMessage,
    originatingErn: String
  )(implicit
    hc: HeaderCarrier
  ): Option[Movement] =
    messages.find(_.isInstanceOf[IE801Message]).fold(auditMessageForNoMovement(originatingMessage)) {
      case ie801: IE801Message =>
        val timestamp     = dateTimeService.timestamp()
        // For a new movement from trader-movement call, add the IE801 for the consignor and consignee
        // also add the originating message all at once
        val messagesToAdd = Seq(
          Some(convertMessage(ie801.consignorId, ie801, boxIds, timestamp)),
          ie801.consigneeId.map(consignee => convertMessage(consignee, ie801, boxIds, timestamp)),
          Some(convertMessage(originatingErn, originatingMessage, boxIds, timestamp))
        ).flatten
        Some(
          Movement(
            correlationIdService.generateCorrelationId(),
            None,
            ie801.localReferenceNumber,
            ie801.consignorId,
            ie801.consigneeId,
            administrativeReferenceCode = ie801.administrativeReferenceCode.head,
            timestamp,
            messages = messagesToAdd
          )
        )
    }

  private def auditMessageForNoMovement(message: IEMessage)(implicit
    hc: HeaderCarrier
  ): Option[Movement] = {
    val errorMessage =
      s"An ${message.messageType} message has been retrieved with no movement, unable to create movement"
    auditService.auditMessage(message, errorMessage)
    logger.error(errorMessage)
    None
  }

  private def createMovementFromIE704(consignor: String, message: IE704Message, boxIds: Set[String])(implicit
    hc: HeaderCarrier
  ): Option[Movement] = {
    val timestamp = dateTimeService.timestamp()
    message.localReferenceNumber.fold[Option[Movement]](auditMessageForNoMovement(message))(lrn =>
      Some(
        Movement(
          correlationIdService.generateCorrelationId(),
          None,
          lrn,
          consignor,
          None,
          administrativeReferenceCode = message.administrativeReferenceCode.head,
          timestamp,
          messages = Seq(convertMessage(consignor, message, boxIds, timestamp))
        )
      )
    )
  }

  private def createMovementFromIE801(recipient: String, message: IE801Message, boxIds: Set[String]): Movement = {
    val timestamp = dateTimeService.timestamp()
    Movement(
      correlationIdService.generateCorrelationId(),
      None,
      message.localReferenceNumber,
      message.consignorId,
      message.consigneeId,
      administrativeReferenceCode = message.administrativeReferenceCode.head,
      timestamp,
      messages = Seq(convertMessage(recipient, message, boxIds, timestamp))
    )
  }

  private def findByArc(movements: Seq[Movement], message: IEMessage): OptionT[Future, Seq[Movement]] = {

    val matchedMovements = message.administrativeReferenceCode.flatten.flatMap { arc =>
      movements.find(_.administrativeReferenceCode.contains(arc))
    }

    if (matchedMovements.isEmpty) OptionT.none else OptionT.pure(matchedMovements)
  }

  private def findByArcInMessage(message: IEMessage): OptionT[Future, Seq[Movement]] =
    message.administrativeReferenceCode.flatten.traverse { arc =>
      OptionT(movementRepository.getByArc(arc))
    }

  private def findByLrn(movements: Seq[Movement], message: IEMessage): OptionT[Future, Seq[Movement]] =
    OptionT.fromOption(
      movements
        .find(movement => message.lrnEquals(movement.localReferenceNumber))
        .map(Seq(_))
    )

  private def convertMessage(recipient: String, input: IEMessage, boxIds: Set[String], timestamp: Instant): Message =
    Message(
      encodedMessage = emcsUtils.encode(input.toXml.toString),
      messageType = input.messageType,
      messageId = input.messageIdentifier,
      recipient = recipient,
      boxesToNotify = boxIds,
      createdOn = timestamp
    )
}

object MessageService {
  sealed trait UpdateOutcome

  object UpdateOutcome {
    case object Updated extends UpdateOutcome
    case object Locked extends UpdateOutcome
    case object NotUpdatedThrottled extends UpdateOutcome
  }
}
