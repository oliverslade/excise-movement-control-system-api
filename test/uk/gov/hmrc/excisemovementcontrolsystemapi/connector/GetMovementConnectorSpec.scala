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

package uk.gov.hmrc.excisemovementcontrolsystemapi.connector

import akka.Done
import com.codahale.metrics.Timer
import com.kenshoo.play.metrics.Metrics
import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.MockitoSugar.{reset, verify, when}
import org.scalatest.{BeforeAndAfterEach, EitherValues}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.Results.InternalServerError
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.{EisHttpClient, GetMovementConnector}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.EmcsUtils
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis.EISConsumptionResponse
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.concurrent.Futures

import scala.reflect.runtime.universe.typeOf

class GetMovementConnectorSpec extends PlaySpec
  with BeforeAndAfterEach
  with EitherValues {

  protected implicit val ec: ExecutionContext = ExecutionContext.global
  protected implicit val hc: HeaderCarrier = HeaderCarrier()

  private val httpClient = mock[HttpClient]
  private val appConfig = mock[AppConfig]
  private val metrics = mock[Metrics](RETURNS_DEEP_STUBS)
  private val emcsUtil = mock[EmcsUtils]
  private val dateTime = LocalDateTime.of(2023, 2, 3, 5, 6, 7)
  private val response  = EISConsumptionResponse(
    dateTime,
    "ern",
    "message"
  )

  private val timerContext = mock[Timer.Context]
  private val futures = mock[Futures]

  private def createConnector = {
    val eisHttpClient = new EisHttpClient(httpClient, appConfig, emcsUtil, metrics, futures)
    new GetMovementConnector(eisHttpClient, appConfig, emcsUtil, metrics)
  }

  override def beforeEach(): Unit = {
    super.beforeEach()

    reset(httpClient, appConfig, emcsUtil, metrics, timerContext)

    when(httpClient.GET[Any](any,any,any)(any,any,any))
      .thenReturn(Future.successful(HttpResponse(200, Json.toJson(response).toString())))
    when(appConfig.traderMovementUrl).thenReturn("/trader-movement-url")
    when(appConfig.systemApplication).thenReturn("uk.gov.hmrc.cdio.cbit.emcs.core.api")
    when(emcsUtil.generateCorrelationId).thenReturn("1234")
    when(emcsUtil.getCurrentDateTimeString).thenReturn(dateTime.toString)
    when(metrics.defaultRegistry.timer(any).time()).thenReturn(timerContext)
    when(futures.delay(any)).thenReturn(Future.successful(Done))
  }

  "get" should {
    "return a response" in {
      val result = await(createConnector.get("ern", "arc"))

      result mustBe Right(response)
    }

    "pass the right parameters to the HttpClient" in {
      await(createConnector.get("ern", "arc"))

      verify(httpClient).GET(
        eqTo("/trader-movement-url"),
        eqTo(Seq("exciseregistrationnumber" -> "ern", "arc" -> "arc")),
        eqTo(expectedHeader)
      )(any,any,any)
    }

    "return an error "when {
      "cannot parse Json" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(Future.successful(HttpResponse(200, "error message")))

        val result = await(createConnector.get("ern", "arc"))

        result.left.value mustBe InternalServerError(s"Response body could not be read as type ${typeOf[EISConsumptionResponse]}")
      }

      "EIS return an error" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(Future.successful(HttpResponse(404, "error message")))

        val result = await(createConnector.get("ern", "arc"))

        result.left.value mustBe InternalServerError("error message")
      }
    }
  }

  private def expectedHeader: Seq[(String, String)] = {
    Seq(
      "x-forwarded-host" -> "uk.gov.hmrc.cdio.cbit.emcs.core.api",
      "x-correlation-id" -> "1234",
      "source" -> "APIP",
      "dateTime" -> dateTime.toString
    )
  }
}
