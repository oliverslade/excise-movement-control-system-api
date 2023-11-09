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

package uk.gov.hmrc.excisemovementcontrolsystemapi.connector.util

import akka.Done
import com.codahale.metrics.Timer
import com.kenshoo.play.metrics.Metrics
import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.Mockito.{RETURNS_DEEP_STUBS, times}
import org.mockito.MockitoSugar.{reset, verify, when}
import org.scalatest.{BeforeAndAfterEach, EitherValues}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import play.api.http.{ContentTypes, HeaderNames}
import play.api.http.Status.{NOT_FOUND, OK}
import play.api.libs.concurrent.Futures
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.NotFound
import play.api.test.Helpers.{await, defaultAwaitTimeout, status}
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.{EisHttpClient, EisHttpResponse}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.EmcsUtils
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis.EISRequest
import uk.gov.hmrc.http.{GatewayTimeoutException, HeaderCarrier, HttpClient, HttpResponse}

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class EisHttpClientSpec
  extends PlaySpec
    with EitherValues
    with BeforeAndAfterEach{

  protected implicit val hc: HeaderCarrier = HeaderCarrier()
  protected implicit val ec: ExecutionContext = ExecutionContext.global

  private val httpClient = mock[HttpClient]
  private val emcsUtils = mock[EmcsUtils]
  private val appConfig = mock[AppConfig]
  private val metrics = mock[Metrics](RETURNS_DEEP_STUBS)
  private val futures = mock[Futures]
  private val eisHttpClient = new EisHttpClient(httpClient, appConfig, emcsUtils, metrics, futures)
  private val dateTime = LocalDateTime.of(2023,5,6, 1, 2, 3)
  private val timerContext = mock[Timer.Context]
  private val correlationId = UUID.fromString("00000000-0000-0001-0000-000000000001").toString
  private val expectedEisRequest = EISRequest(correlationId, dateTime.toString, "any-message-type", "APIP", "user1", "encodedMessage")


  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(httpClient, emcsUtils, appConfig, metrics, timerContext)

    when(httpClient.GET[Any](any, any, any)(any, any, any))
      .thenReturn(Future.successful(HttpResponse(200, "message body")))
    when(httpClient.POST[Any,Any](any, any, any)(any, any, any, any))
      .thenReturn(Future.successful(HttpResponse(200, "message body")))
    when(emcsUtils.generateCorrelationId)
      .thenReturn(correlationId)
    when(emcsUtils.getCurrentDateTimeString).thenReturn(dateTime.toString)
    when(appConfig.systemApplication).thenReturn("system.application")
    when(metrics.defaultRegistry.timer(any).time()) thenReturn timerContext
    when(futures.delay(any)).thenReturn(Future.successful(Done))
  }

  "get" should {
    "should sent a HttpClient.get request with success" in {
      val result = getRequest

      result mustBe EisHttpResponse(200, "message body", "ern", correlationId, dateTime.toString)
      verifyHttpGetCall()
    }

    "start and stop the metric" in {
      await(eisHttpClient.get("/url", "value", Seq("exciseregistrationnumber" -> "value"), "timerName"))

      verify(metrics.defaultRegistry).timer(eqTo("timerName"))
      verify(metrics.defaultRegistry.timer(eqTo("timerName"))).time()
      verify(timerContext).stop()
    }

    "retry to send the request 3 times" when {
      "an exception occur continuously" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(Future.failed(new GatewayTimeoutException(("error message"))))

        the[Exception] thrownBy getRequest  must have message "error message"

        verifyHttpGetCall(3)
      }

      "always return a not 2xx status code" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(Future.successful(HttpResponse(404, "Not found")))

        val result = getRequest

        result mustBe EisHttpResponse(NOT_FOUND, "Not found", "ern", correlationId, dateTime.toString )
        verifyHttpGetCall(3)
      }
    }

    "stop retrying" when {
      "successful after an exception" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(
            Future.failed(new GatewayTimeoutException("error message")),
            Future.successful(HttpResponse(200, "success"))
          )

        getRequest

        verifyHttpGetCall(2)
      }

      "return a 2xx after a error" in {
        when(httpClient.GET[Any](any, any, any)(any, any, any))
          .thenReturn(
            Future.successful(HttpResponse(404, "Not found")),
            Future.successful(HttpResponse(200, "success"))
          )

        val result = getRequest

        result mustBe EisHttpResponse(200, "success", "ern", correlationId, dateTime.toString)
        verifyHttpGetCall(2)
      }
    }
  }

  "post" should {
    "sent a successful post request" in {
      postRequest.status mustBe OK

      withClue("should send the right parameters") {
        verifyHttpPostCall()
      }
    }

    "start and stop the metric" in {
      postRequest

      verify(metrics.defaultRegistry).timer(eqTo("post-timer"))
      verify(metrics.defaultRegistry.timer(eqTo("post-timer"))).time()
      verify(timerContext).stop()
    }

    "retry to send the request 3 times" when {
      "an exception occur continuously" in {
        when(httpClient.POST[Any, Any](any, any, any)(any, any, any, any))
          .thenReturn(Future.failed(new GatewayTimeoutException(("error message"))))

        the[Exception] thrownBy postRequest must have message "error message"

        verifyHttpPostCall(3)
      }

      "always return a not 2xx status code" in {
        when(httpClient.POST[Any, Any](any, any, any)(any, any, any, any))
          .thenReturn(Future.successful(HttpResponse(404, "Not found")))

        val result = postRequest

        result.status mustBe NOT_FOUND
        result.body mustBe "Not found"
        verifyHttpPostCall(3)
      }
    }

    "stop retrying" when {
      "successful after an exception" in {
        when(httpClient.POST[Any, Any](any, any, any)(any, any, any, any))
          .thenReturn(
            Future.failed(new GatewayTimeoutException("error message")),
            Future.successful(HttpResponse(200, "success"))
          )

        postRequest

        verifyHttpPostCall(2)
      }

      "return a 2xx after a error" in {
        when(httpClient.POST[Any, Any](any, any, any)(any, any, any, any))
          .thenReturn(
            Future.successful(HttpResponse(404, "Not found")),
            Future.successful(HttpResponse(200, "success"))
          )

        val result = postRequest

        result mustBe EisHttpResponse(200, "success", "ern", correlationId, dateTime.toString)
        verifyHttpPostCall(2)
      }
    }
  }

  private def verifyHttpPostCall(occurrences: Int = 1) = {
    verify(httpClient, times(occurrences)).POST(
      eqTo("/url"),
      eqTo(expectedEisRequest),
      eqTo(expectedSubmissionHeader)
    )(any, any, any, any)
  }

  private def verifyHttpGetCall(occurrences: Int = 1) = {
    verify(httpClient, times(occurrences)).GET(
      eqTo("/url"),
      eqTo(Seq("exciseregistrationnumber" -> "ern")),
      eqTo(expectedConsumptionHeader)
    )(any, any, any)
  }

  def expectedConsumptionHeader: Seq[(String, String)] = {
    Seq(
      "x-forwarded-host" -> "system.application",
      "x-correlation-id" -> "00000000-0000-0001-0000-000000000001",
      "source" -> "APIP",
      "dateTime" -> dateTime.toString
    )
  }

  def expectedSubmissionHeader: Seq[(String, String)] = {
    Seq(HeaderNames.ACCEPT -> ContentTypes.JSON,
      HeaderNames.CONTENT_TYPE -> ContentTypes.JSON,
      "dateTime" -> dateTime.toString,
      "x-correlation-id" -> "00000000-0000-0001-0000-000000000001",
      "x-forwarded-host" -> "",
      "source" -> "APIP",

    )
  }

  private def postRequest = {
    await(eisHttpClient.post(
      "/url",
      "ern",
      "any-message-type",
      "encodedMessage",
      "post-timer"))
  }

  private def getRequest: EisHttpResponse ={
    await(eisHttpClient.get(
      "/url",
      "ern",
      Seq("exciseregistrationnumber" -> "ern"),
      "timerName"
      )
    )
  }
}

