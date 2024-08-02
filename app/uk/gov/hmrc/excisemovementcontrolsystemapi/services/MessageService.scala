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

import cats.syntax.all._
import org.apache.pekko.Done
import play.api.{Configuration, Logging}
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.{MessageConnector, TraderMovementConnector}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages._
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.model.{Message, Movement}
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.{BoxIdRepository, ErnRetrievalRepository, MovementRepository}
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

  def updateAllMessages(erns: Set[String])(implicit hc: HeaderCarrier): Future[Done] =
    erns.toSeq
      .traverse { ern =>
        updateMessages(ern).recover { case NonFatal(error) =>
          logger.warn(s"[MessageService]: Failed to update messages for ERN: $ern", error)
          Done
        }
      }
      .as(Done)

  def updateMessages(ern: String)(implicit hc: HeaderCarrier): Future[Done] = {
    val lockService = LockService(mongoLockRepository, ern, throttleCutoff)
    lockService
      .withLock {
        val now = dateTimeService.timestamp()
        ernRetrievalRepository.getLastRetrieved(ern).flatMap { maybeLastRetrieved =>
          if (shouldProcessNewMessages(maybeLastRetrieved)) {
            for {
              boxIds <- getBoxIds(ern)
              _      <- processNewMessages(ern, boxIds)
              _      <- ernRetrievalRepository.setLastRetrieved(ern, now)
            } yield Done
          } else {
            Future.successful(Done)
          }
        }
      }
      .as(Done)
  }

  // TODO, temporarily exposed as a public method to call in Movements Controller and see what we get back from EMCS in QA
  def getTraderMovementMessages(ern: String, arc: String)(implicit
    hc: HeaderCarrier
  ): Future[Seq[IEMessage]] =
    traderMovementConnector.getMovementMessages(ern, arc).recover { case NonFatal(error) =>
      logger.warn(s"[MessageService]: Failed to call trader-movement for: $ern, $arc", error)
      Seq.empty
    }

  private def shouldProcessNewMessages(maybeLastRetrieved: Option[Instant]): Boolean = {
    val cutoffTime = dateTimeService.timestamp().minus(throttleCutoff.length, throttleCutoff.unit.toChronoUnit)
    //noinspection MapGetOrElseBoolean
    maybeLastRetrieved.map(_.isBefore(cutoffTime)).getOrElse(true)
  }

  private def getBoxIds(ern: String): Future[Set[String]] =
    boxIdRepository.getBoxIds(ern)

  private def processNewMessages(ern: String, boxIds: Set[String])(implicit hc: HeaderCarrier): Future[Done] = {
    logger.info(s"[MessageService]: Processing new messages for ern: $ern")
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
    logger.info(s"[MessageService]: Updating movements for ern: $ern")
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
  )(implicit hc: HeaderCarrier): Future[Seq[Movement]] = {
    val matchedMovements: Seq[Movement] = findMovementsForMessage(movements, updatedMovements, message)
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
      movement.copy(
        messages = getUpdatedMessages(recipient, movement, message, boxIds),
        administrativeReferenceCode = getArc(movement, message),
        consigneeId = getConsignee(movement, message)
      )
    }

  private def getUpdatedMessages(
    recipient: String,
    movement: Movement,
    message: IEMessage,
    boxIds: Set[String]
  ): Seq[Message] =
    movement.messages :+ convertMessage(recipient, message, boxIds)

  private def findMovementsForMessage(
    movements: Seq[Movement],
    updatedMovements: Seq[Movement],
    message: IEMessage
  ): Seq[Movement] = {
    findByArc(updatedMovements, message) orElse
      findByLrn(updatedMovements, message) orElse
      findByArc(movements, message) orElse
      findByLrn(movements, message)
  }.getOrElse(Seq.empty)

  private def getConsignee(movement: Movement, message: IEMessage): Option[String] =
    message match {
      case ie801: IE801Message => movement.consigneeId orElse ie801.consigneeId
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
    // TODO, we might have to loop over ARCs here for 829
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
        val movement = createMovementFromIE801(ie801.consignorId, ie801, boxIds)
        Some(updateMovement(originatingErn, movement, originatingMessage, boxIds))
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
  ): Option[Movement] =
    message.localReferenceNumber.fold[Option[Movement]](auditMessageForNoMovement(message))(lrn =>
      Some(
        Movement(
          correlationIdService.generateCorrelationId(),
          None,
          lrn,
          consignor,
          None,
          administrativeReferenceCode = message.administrativeReferenceCode.head, // TODO remove .head
          dateTimeService.timestamp(),
          messages = Seq(convertMessage(consignor, message, boxIds))
        )
      )
    )

  private def createMovementFromIE801(recipient: String, message: IE801Message, boxIds: Set[String]): Movement =
    Movement(
      correlationIdService.generateCorrelationId(),
      None,
      message.localReferenceNumber,
      message.consignorId,
      message.consigneeId,
      administrativeReferenceCode = message.administrativeReferenceCode.head, // TODO remove .head
      dateTimeService.timestamp(),
      messages = Seq(convertMessage(recipient, message, boxIds))
    )

  private def findByArc(movements: Seq[Movement], message: IEMessage): Option[Seq[Movement]] = {

    val matchedMovements = message.administrativeReferenceCode.flatten.flatMap { arc =>
      movements.find(_.administrativeReferenceCode.contains(arc))
    }

    if (matchedMovements.isEmpty) None else Some(matchedMovements)
  }

  private def findByLrn(movements: Seq[Movement], message: IEMessage): Option[Seq[Movement]] =
    movements
      .find(movement => message.lrnEquals(movement.localReferenceNumber))
      .map(Seq(_))

  private def convertMessage(recipient: String, input: IEMessage, boxIds: Set[String]): Message =
    Message(
      encodedMessage = emcsUtils.encode(input.toXml.toString),
      messageType = input.messageType,
      messageId = input.messageIdentifier,
      recipient = recipient,
      boxesToNotify = boxIds,
      createdOn = dateTimeService.timestamp()
    )
}
