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
import play.api.mvc.Result
import play.api.mvc.Results.InternalServerError
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.{EisHttpClient, ResponseHandler}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis.{EISConsumptionResponse, EISErrorMessage}
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GetMovementConnector @Inject()
(
  eishttpClient: EisHttpClient,
  appConfig: AppConfig,
)(implicit val ec: ExecutionContext) extends ResponseHandler with Logging {

  def get(ern: String, arc: String)(implicit hc: HeaderCarrier): Future[Either[Result, EISConsumptionResponse]] = {

   eishttpClient.get(
     appConfig.traderMovementUrl,
     ern,
     Seq("exciseregistrationnumber" -> ern, "arc" -> arc),
     "emcs.getmovements.timer"
    ).map { response =>

      extractIfSuccessful[EISConsumptionResponse](response) match {
        case Right(eisResponse) => Right(eisResponse)
        case Left(response) =>
          logger.warn(EISErrorMessage(response.createdDateTime, ern, response.body, response.correlationId, MessageTypes.IE_MOVEMENT_FOR_TRADER.value))
          Left(InternalServerError(response.body))
      }
    }
  }
}
