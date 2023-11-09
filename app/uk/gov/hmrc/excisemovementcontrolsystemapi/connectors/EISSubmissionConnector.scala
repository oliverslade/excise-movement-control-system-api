/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.excisemovementcontrolsystemapi.connectors

import play.api.Logging
import play.api.http.Status.{BAD_REQUEST, NOT_FOUND, SERVICE_UNAVAILABLE}
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, InternalServerError, NotFound, ServiceUnavailable}
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.{EisHttpClient, EisHttpResponse, ResponseHandler}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.EmcsUtils
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auth.ValidatedXmlRequest
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis._
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages._
import uk.gov.hmrc.http.HeaderCarrier

import java.nio.charset.StandardCharsets
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EISSubmissionConnector @Inject()(
  eisHttpClient: EisHttpClient,
  emcsUtils: EmcsUtils,
  appConfig: AppConfig,
)(implicit ec: ExecutionContext) extends ResponseHandler with Logging {

  def submitMessage(request: ValidatedXmlRequest[_])(implicit hc: HeaderCarrier): Future[Either[Result, EISSubmissionResponse]] = {

    //todo: add retry
    val encodedMessage = emcsUtils.createEncoder.encodeToString(request.body.toString.getBytes(StandardCharsets.UTF_8))
    val messageType = request.parsedRequest.ieMessage.messageType
    val ern = getSingleErnFromMessage(request.parsedRequest.ieMessage, request.validErns)

    eisHttpClient.post(
      appConfig.emcsReceiverMessageUrl,
      ern,
      messageType,
      encodedMessage,
      "emcs.submission.connector.timer"
    ).map{ response =>
      val result = extractIfSuccessful[EISSubmissionResponse](response)
      result match {
        case Right(eisResponse) => Right(eisResponse)
        case Left(eisHttpResponse) => Left(handleErrorResponse(eisHttpResponse, messageType))
      }
    }
  }

  private def handleErrorResponse(
    response: EisHttpResponse,
    messageType: String
  ): Result = {

    logger.warn(EISErrorMessage(response.createdDateTime, response.ern, response.body, response.correlationId, messageType))

    val messageAsJson = response.json
    response.status match {
      case BAD_REQUEST => BadRequest(messageAsJson)
      case NOT_FOUND => NotFound(messageAsJson)
      case SERVICE_UNAVAILABLE => ServiceUnavailable(messageAsJson)
      case _ => InternalServerError(messageAsJson)
    }
  }

  /*
    The illegal state exception for IE801 and IE801 message should never happen here,
    because these should have been caught previously during the validation.
  */
  private def getSingleErnFromMessage(message: IEMessage, validErns: Set[String]) = {
    message match {
      case x: IE801Message => matchErn(x.consignorId, x.consigneeId, validErns, x.messageType)
      case x: IE815Message => x.consignorId
      case x: IE818Message => x.consigneeId.getOrElse(throw new IllegalStateException(s"[EISSubmissionConnector] - ern not supplied for message: ${x.messageType}"))
      case x: IE837Message => matchErn(x.consignorId, x.consigneeId, validErns, x.messageType)
      case _ => throw new RuntimeException(s"[EISSubmissionConnector] - Unsupported Message Type: ${message.messageType}")
    }
  }

  private def matchErn(
                        consignorId: Option[String],
                        consigneeId: Option[String],
                        erns: Set[String],
                        messageType: String
                      ): String = {
    val messageErn: Set[String] = Set(consignorId, consigneeId).flatten
    val availableErn = erns.intersect(messageErn)

    if (availableErn.nonEmpty) availableErn.head
    else throw new IllegalStateException(s"[EISSubmissionConnector] - ern not supplied for message: $messageType")
  }
}
