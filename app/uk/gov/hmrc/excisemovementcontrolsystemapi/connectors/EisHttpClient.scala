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

import com.kenshoo.play.metrics.Metrics
import play.api.Logging
import play.api.http.Status
import play.api.mvc.Results.InternalServerError
import sttp.model.QueryParams
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.EisHttpClient.{retryAttempts, retryDelayInMillisecond}
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.ResponseHandler
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.{EmcsUtils, MessageTypes}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis.{EISConsumptionHeader, EISConsumptionResponse, EISErrorMessage}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}
import play.api.libs.concurrent.Futures

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}


case class EisHttpResponse(status: Int, body: String, correlationId: String, createdDateTime: String)

object EisHttpResponse {
  def fromHttpResponse(
    correlationId: String,
    createdDateTime: String
  ) (httpResponse: HttpResponse): EisHttpResponse = {
    EisHttpResponse(httpResponse.status, httpResponse.body, correlationId, createdDateTime)
  }
}
class EisHttpClient @Inject()
(
  httpClient: HttpClient,
  override val appConfig: AppConfig,
  emcsUtils: EmcsUtils,
  metrics: Metrics,
  futures: Futures
  )(implicit val ec: ExecutionContext) extends EISConsumptionHeader with Logging {



  def get(
    url:String,
    queryParams: Seq[(String, String)],
    timerName: String
  )(implicit hc: HeaderCarrier): Future[EisHttpResponse] = {

    val timer = metrics.defaultRegistry.timer(timerName).time()
    val correlationId = emcsUtils.generateCorrelationId
    val dateTime = emcsUtils.getCurrentDateTimeString

    val httpGetFn = () => httpClient.GET(
      url,
      queryParams,
      build(correlationId, dateTime)
    ).map {
      EisHttpResponse.fromHttpResponse(correlationId, dateTime)
    }

    retry(retryAttempts, httpGetFn, url)
      .andThen{ case _ => timer.stop() }

  }

  def retry(times: Int, function: () => Future[EisHttpResponse], url: String): Future[EisHttpResponse] = {
    function().transformWith { t: Try[EisHttpResponse] =>
      t match {
        case Failure(f) => f match {
          case exception if times > 1 =>
            logger.warn(s"EMCS_API_RETRY retrying: url $url exception $exception")
            futures
              .delay(((retryAttempts + 1 - times) * retryDelayInMillisecond) milliseconds)
              .flatMap { _ => retry(times - 1, function, url) }

          case exception =>
            logger.warn(s"EMCS_API_RETRY gave up: url $url exception $exception")
            Future.failed(exception)
        }

        case Success(r) => r match {
          case response if Status.isSuccessful(response.status) =>
            if (times != retryAttempts)
              logger.warn(s"EMCS_API_RETRY successful: url $url correlation-id ${response.correlationId}")
            Future.successful(response)

          case response if times > 1 =>
            logger.warn(s"EMCS_API_RETRY retrying: url $url status ${response.status} correlation-id ${response.correlationId}")
            futures
              .delay(((retryAttempts + 1 - times) * retryDelayInMillisecond) milliseconds)
              .flatMap { _ => retry(times - 1, function, url) }

          case response =>
            logger.warn(s"EMCS_API_RETRY gave up: url $url status ${response.status} correlation-id ${response.correlationId}")
            Future.successful(response)
        }
      }
    }
  }
}

object EisHttpClient {
  val retryDelayInMillisecond = 2000
  val retryAttempts = 3
}
