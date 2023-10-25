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

package uk.gov.hmrc.excisemovementcontrolsystemapi.controllers

import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.MockitoSugar.{reset, verify, when}
import org.scalatest.matchers.must.Matchers
import org.scalatest.{BeforeAndAfterEach, EitherValues}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, OK}
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.Results.InternalServerError
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, contentAsJson, defaultAwaitTimeout, status, stubControllerComponents}
import uk.gov.hmrc.excisemovementcontrolsystemapi.connectors.ShowNewMessagesConnector
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.TestXml
import uk.gov.hmrc.excisemovementcontrolsystemapi.fixture.{FakeAuthentication, FakeValidateConsignorAction, FakeXmlParsers}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.ShowNewMessageResponse
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.model.Message
import uk.gov.hmrc.excisemovementcontrolsystemapi.services.MovementService
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.MessageFilter

import java.time.{Instant, LocalDateTime}
import scala.concurrent.{ExecutionContext, Future}

class GetMessagesControllerSpec extends PlaySpec
  with FakeAuthentication
  with FakeXmlParsers
  with FakeValidateConsignorAction
  with TestXml
  with EitherValues
  with BeforeAndAfterEach
  with Matchers {

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
  private val movementService = mock[MovementService]
  private val cc = stubControllerComponents()
  private val showNewMessagesConnector = mock[ShowNewMessagesConnector]
  private val messageFilter = mock[MessageFilter]
  private val lrn = "LRN1234"
  private val timeStamp = Instant.now
  private val newMessage = ShowNewMessageResponse(
    LocalDateTime.of(2023, 5, 5, 6, 6, 2),
    ern,
    "message")

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(showNewMessagesConnector, movementService,messageFilter)

  when(movementService.getMatchingERN(any, any))
    .thenReturn(Future.successful(Some(ern)))

    when(messageFilter.filter(any, any)).thenReturn(Seq(Message("message","IE801", timeStamp)))
  }

  "getMessagesForMovement" should {
    "return 200" in {

      when(showNewMessagesConnector.get(any)(any))
        .thenReturn(Future.successful(Right(newMessage)))

      val result = createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest())

      status(result) mustBe OK
      contentAsJson(result) mustBe Json.toJson(Seq(Message("message","IE801", timeStamp)))
    }

    "get all the new messages" in {
        when(showNewMessagesConnector.get(any)(any))
          .thenReturn(Future.successful(Right(newMessage)))

        await(createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest()))

        verify(showNewMessagesConnector).get(eqTo(ern))(any)
      }

    "filter messages by lrn" in {
      when(showNewMessagesConnector.get(any)(any))
        .thenReturn(Future.successful(Right(newMessage)))

      await(createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest()))

      verify(messageFilter).filter(eqTo(newMessage), eqTo(lrn))
    }

    //todo: remove these test ig changes approved
//    "get all the new messages" when {
//      "matching the consignorId" in {
//        when(showNewMessagesConnector.get(any)(any))
//          .thenReturn(Future.successful(Right(newMessage)))
//
//        await(createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest()))
//
//        verify(showNewMessagesConnector).get(eqTo(ern))(any)
//      }
//
//      "matching the consigneeId" in {
//        when(movementService.getMovementByLRNAndERNIn(any, any))
//          .thenReturn(Future.successful(Some(Movement("LRN1234", "234", Some(ern)))))
//
//        when(showNewMessagesConnector.get(any)(any))
//          .thenReturn(Future.successful(Right(newMessage))          )
//
//        await(createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest()))
//
//        verify(showNewMessagesConnector).get(eqTo(ern))(any)
//      }
//
//    }

    "return a bad request when no movement exists for LRN/ERNs combination" in {
      when(movementService.getMatchingERN(any, any)).thenReturn(Future.successful(None))

      val result = createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest())

      status(result) mustBe BAD_REQUEST
    }

    "return 500 when 500 error from eis" in {

      when(showNewMessagesConnector.get(any)(any))
        .thenReturn(Future.successful(Left(InternalServerError( "error :("))))

      val result = createWithSuccessfulAuth.getMessagesForMovement(lrn)(createRequest())

      status(result) mustBe INTERNAL_SERVER_ERROR
    }
  }

  private def createWithSuccessfulAuth =
    new GetMessagesController(
      FakeSuccessAuthentication,
      showNewMessagesConnector,
      movementService,
      messageFilter,
      cc
    )

  private def createRequest(): FakeRequest[AnyContent] = {
    FakeRequest("GET", "/foo")
  }
}
