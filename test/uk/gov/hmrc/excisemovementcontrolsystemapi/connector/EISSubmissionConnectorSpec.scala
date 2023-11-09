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
import play.api.http.{ContentTypes, HeaderNames}
import play.api.libs.concurrent.Futures
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, InternalServerError, NotFound, ServiceUnavailable}
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.excisemovementcontrolsystemapi.config.AppConfig
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.EISSubmissionConnector
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.util.EisHttpClient
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.EmcsUtils
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auth.{EnrolmentRequest, ParsedXmlRequest, ValidatedXmlRequest}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.eis.{EISErrorResponse, EISRequest, EISSubmissionResponse}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.Base64
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.NodeSeq

class EISSubmissionConnectorSpec extends PlaySpec with BeforeAndAfterEach with EitherValues {

  protected implicit val hc: HeaderCarrier = HeaderCarrier()
  protected implicit val ec: ExecutionContext = ExecutionContext.global

  private val mockHttpClient = mock[HttpClient]
  private val emcsUtils = mock[EmcsUtils]
  private val appConfig = mock[AppConfig]

  private val metrics = mock[Metrics](RETURNS_DEEP_STUBS)

  private val emcsCorrelationId = "1234566"
  private val xml = "<IE815></IE815>"
  private val encoder = Base64.getEncoder
  private val timerContext = mock[Timer.Context]
  private val ie815Message = mock[IE815Message]
  private val dateTime = LocalDateTime.of(2023,5,5,13,34,45)
  private val expectedEisResponse = Json.toJson(EISErrorResponse(dateTime, "message", "debugMessage", "emcsCorrelationId"))

  private def createConnector = {
    val futures = mock[Futures]
    when(futures.delay(any)).thenReturn(Future.successful(Done))

    val eisHttpClient = new EisHttpClient(mockHttpClient, appConfig, emcsUtils, metrics, futures)
    new EISSubmissionConnector(eisHttpClient, emcsUtils, appConfig)
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockHttpClient, appConfig, metrics, timerContext)

    when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
      .thenReturn(Future.successful(HttpResponse(200, Json.toJson(EISSubmissionResponse("ok", "Success", emcsCorrelationId)).toString())))
    when(emcsUtils.getCurrentDateTimeString).thenReturn("2023-09-17T09:32:50.345")
    when(emcsUtils.generateCorrelationId).thenReturn(emcsCorrelationId)
    when(appConfig.emcsReceiverMessageUrl).thenReturn("/eis/path")
    when(metrics.defaultRegistry.timer(any).time()) thenReturn timerContext
    when(emcsUtils.createEncoder).thenReturn(encoder)
    when(ie815Message.messageType).thenReturn("IE815")
    when(ie815Message.consignorId).thenReturn("123")

  }

  "post" should {
    "return successful EISResponse" in {
      val result: Either[Result, EISSubmissionResponse] = await(submitExciseMovementForIE815)

      result mustBe Right(EISSubmissionResponse("ok", "Success", emcsCorrelationId))
    }

    "get URL from appConfig" in {
      submitExciseMovementWithParams(xml, ie815Message, Set("123"), Set("123"))

      verify(appConfig).emcsReceiverMessageUrl
    }

    "send a request with the right parameters" in {
      val encodeMessage = encoder.encodeToString(xml.getBytes(StandardCharsets.UTF_8))
      val eisRequest = EISRequest(emcsCorrelationId, "2023-09-17T09:32:50.345", "IE815", "APIP", "user1", encodeMessage)

      submitExciseMovementWithParams(xml, ie815Message, Set("123"), Set("123"))

      verify(mockHttpClient).POST(
        eqTo("/eis/path"),
        eqTo(eisRequest),
        eqTo(expectedHeader)
      )(any, any, any, any)
    }


    "throw an error if unsupported message" in {
      class NonSupportedMessage extends IEMessage {
        override def consigneeId: Option[String] = None
        override def administrativeReferenceCode: Option[String] = None
        override def messageType: String = "any-type"
        override def toXml: NodeSeq = NodeSeq.Empty
        override def getErns: Set[String] = Set.empty
        override def lrnEquals(lrn: String): Boolean = false
      }

      the[RuntimeException] thrownBy
        submitExciseMovementWithParams(xml, new NonSupportedMessage(), Set("123"), Set("456")) must
        have message "[EISSubmissionConnector] - Unsupported Message Type: any-type"
    }


    "return Bad request error" in {
      when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
        .thenReturn(Future.successful(HttpResponse(400, expectedEisResponse.toString())))

      val result = await(submitExciseMovementForIE815)

      result.left.value mustBe BadRequest(expectedEisResponse)
    }

    "return 500 if post request fail" in {
      when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
        .thenReturn(Future.failed(new RuntimeException("error")))

      the [RuntimeException] thrownBy await(submitExciseMovementForIE815) must have message "error"
    }

    "return Not found error" in {
      when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
        .thenReturn(Future.successful(HttpResponse(404, expectedEisResponse.toString())))

      val result = await(submitExciseMovementForIE815)

      result.left.value mustBe NotFound(expectedEisResponse)
    }

    "return service unavailable error" in {
      when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
        .thenReturn(Future.successful(HttpResponse(503, expectedEisResponse.toString())))

      val result = await(submitExciseMovementForIE815)

      result.left.value mustBe ServiceUnavailable(expectedEisResponse)
    }

    "return Internal service error error" in {
      when(mockHttpClient.POST[Any, Any](any, any, any)(any, any, any, any))
        .thenReturn(Future.successful(HttpResponse(500, expectedEisResponse.toString())))

      val result = await(submitExciseMovementForIE815)

      result.left.value mustBe InternalServerError(expectedEisResponse)
    }
  }

  private def submitExciseMovementForIE815: Future[Either[Result, EISSubmissionResponse]] = {
    submitExciseMovementWithParams(xml, ie815Message, Set("123"), Set("123"))
  }

  private def submitExciseMovementWithParams(
    xml: String,
    message: IEMessage,
    enrolledErns: Set[String],
    validatedErns: Set[String]
  ): Future[Either[Result, EISSubmissionResponse]] = {
    createConnector.submitMessage(ValidatedXmlRequest(ParsedXmlRequest(
      EnrolmentRequest(FakeRequest().withBody(xml), enrolledErns, "124"),
      message,
      enrolledErns,
      "124"
    ), validatedErns
    ))
  }

  private def expectedHeader =
    Seq(HeaderNames.ACCEPT -> ContentTypes.JSON,
      HeaderNames.CONTENT_TYPE -> ContentTypes.JSON,
      "dateTime" -> "2023-09-17T09:32:50.345",
      "x-correlation-id" -> "1234566",
      "x-forwarded-host" -> "",
      "source" -> "APIP"
    )
}
