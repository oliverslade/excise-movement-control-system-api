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

package uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages

import generated.{ExciseMovementEadTypeType, IE829Type, MessagesOption}
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.MockitoSugar.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.NewMessagesXml

class IE829MessageSpec extends PlaySpec
  with NewMessagesXml
  with BeforeAndAfterEach {

  private val message = mock[DataRecord[MessagesOption]]
  private val scalaXbMessageType = mock[IE829Type](RETURNS_DEEP_STUBS)

  override def beforeEach(): Unit = {
    super.beforeEach()

    reset(message, scalaXbMessageType)
    when(message.key).thenReturn(Some("IE829"))
    when(message.namespace).thenReturn(Some("IE8299"))
    when(message.as[Any]).thenReturn(scalaXbMessageType)
  }

  "consigneeId" should {
    "return the consigneeID" in {
      when(scalaXbMessageType.Body.NotificationOfAcceptedExport.ConsigneeTrader.get.Traderid)
        .thenReturn(Some("123"))
      IE829Message(message).consigneeId mustBe Some("123")
    }

    "not return a consigneeId" when {
      "consigneeTrader is None" in {
        when(scalaXbMessageType.Body.NotificationOfAcceptedExport.ConsigneeTrader).thenReturn(None)
        IE829Message(message).consigneeId mustBe None
      }

      "there is no TraderId" in {
        when(scalaXbMessageType.Body.NotificationOfAcceptedExport.ConsigneeTrader.get.Traderid)
          .thenReturn(None)
        IE829Message(message).consigneeId mustBe None
      }
    }
  }

  "administrativeReferenceCode" should {
    "match the ARC with the movement " in {

      when(scalaXbMessageType.Body.NotificationOfAcceptedExport.ExciseMovementEad)
      .thenReturn(Seq(
        ExciseMovementEadTypeType("1234", "1"),
        ExciseMovementEadTypeType("456", "2")
      ))

      IE829Message(message).administrativeReferenceCode mustBe Some("1234")

    }

    "return no ARC when no ARC" in {
      when(scalaXbMessageType.Body.NotificationOfAcceptedExport.ExciseMovementEad)
        .thenReturn(Seq.empty)
      IE829Message(message).administrativeReferenceCode mustBe None
    }
  }
}
