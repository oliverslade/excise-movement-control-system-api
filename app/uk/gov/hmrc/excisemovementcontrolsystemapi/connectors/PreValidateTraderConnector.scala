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

import com.codahale.metrics.MetricRegistry
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.InternalServerError
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.PreValidateTraderHttpReader
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.EisErrorResponsePresentation
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis._
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.preValidateTrader.request.PreValidateTraderRequest
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.preValidateTrader.response.PreValidateTraderEISResponse
import uk.gov.hmrc.excisemovementcontrolsystemapi.services.CorrelationIdService
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.DateTimeService
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.DateTimeService.DateTimeFormat
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, StringContextOps}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PreValidateTraderConnector @Inject() (
  httpClient: HttpClientV2,
  correlationIdService: CorrelationIdService,
  appConfig: AppConfig,
  metrics: MetricRegistry,
  dateTimeService: DateTimeService
)(implicit ec: ExecutionContext)
    extends EISSubmissionHeaders
    with Logging {

  def submitMessage(request: PreValidateTraderRequest, ern: String)(implicit
    hc: HeaderCarrier
  ): Future[Either[Result, PreValidateTraderEISResponse]] = {

    logger.info("[PreValidateTraderConnector]: Submitting PreValidateTrader message")

    val timer = metrics.timer("emcs.prevalidatetrader.connector.timer").time()

    val correlationId   = correlationIdService.generateCorrelationId()
    val timestamp       = dateTimeService.timestamp()
    val createdDateTime = timestamp.asStringInMilliseconds

    implicit val reader: HttpReads[Either[Result, PreValidateTraderEISResponse]] = PreValidateTraderHttpReader(
      correlationId = correlationId,
      ern = ern,
      createDateTime = createdDateTime,
      dateTimeService = dateTimeService
    )
    httpClient
      .post(url"${appConfig.preValidateTraderUrl}")
      .setHeader(build(correlationId, createdDateTime, appConfig.preValidateTraderBearerToken): _*)
      .withBody(Json.toJson(request))
      .execute[Either[Result, PreValidateTraderEISResponse]]
      .andThen { case _ => timer.stop() }
      .recover { case ex: Throwable =>
        logger.warn(EISErrorMessage(createdDateTime, ex.getMessage, correlationId, "PreValidateTrader"), ex)

        val error = EisErrorResponsePresentation(
          timestamp,
          "Internal Server Error",
          "Unexpected error occurred while processing PreValidateTrader request",
          correlationId
        )

        Left(InternalServerError(Json.toJson(error)))
      }

  }

}
