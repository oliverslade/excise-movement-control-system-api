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

package uk.gov.hmrc.excisemovementcontrolsystemapi.factories

import generated.MessagesOption
import org.mockito.MockitoSugar.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.Ie801XmlMessage.IE801
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.TestXml
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages._

class IEMessageFactorySpec
  extends PlaySpec
    with TestXml
    with BeforeAndAfterEach {

  private val message = mock[DataRecord[MessagesOption]]
  private val sut = IEMessageFactory()

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(message)

    when(message.value).thenReturn(mock[MessagesOption])
  }

  "createIEMessage" should {

    "return throw an error" when {
      "cannot handle message type" in {
        when(message.key).thenReturn(Some("Anything"))
        intercept[RuntimeException] {
          sut.createIEMessage(message)
        }.getMessage mustBe s"[IEMessageFactory] - Could not create Message object. Unsupported message: Anything"
      }

      "messageType is empty" in {
        when(message.key).thenReturn(None)
        intercept[RuntimeException] {
          sut.createIEMessage(message)
        }.getMessage mustBe "[IEMessageFactory] - Could not create Message object. Message type is empty"
      }
    }

    "return an instance of IE704Message" in {
      when(message.key).thenReturn(Some("IE704"))
      sut.createIEMessage(message).isInstanceOf[IE704Message] mustBe true
    }

    "return an instance of IE801Message" in {
      when(message.key).thenReturn(Some("IE801"))
      sut.createIEMessage(message).isInstanceOf[IE801Message] mustBe true
    }

    "return an instance of IE802Message" in {
      when(message.key).thenReturn(Some("IE802"))
      sut.createIEMessage(message).isInstanceOf[IE802Message] mustBe true
    }

    "return an instance of IE803Message" in {
      when(message.key).thenReturn(Some("IE803"))
      sut.createIEMessage(message).isInstanceOf[IE803Message] mustBe true
    }

    "return an instance of IE807Message" in {
      when(message.key).thenReturn(Some("IE807"))
      sut.createIEMessage(message).isInstanceOf[IE807Message] mustBe true
    }

    "return an instance of IE810Message" in {
      when(message.key).thenReturn(Some("IE810"))
      sut.createIEMessage(message).isInstanceOf[IE810Message] mustBe true
    }

    "return an instance of IE813Message" in {
      when(message.key).thenReturn(Some("IE813"))
      sut.createIEMessage(message).isInstanceOf[IE813Message] mustBe true
    }

    "return an instance of IE818Message" in {
      when(message.key).thenReturn(Some("IE818"))
      sut.createIEMessage(message).isInstanceOf[IE818Message] mustBe true
    }

    "return an instance of IE819Message" in {
      when(message.key).thenReturn(Some("IE819"))
      sut.createIEMessage(message).isInstanceOf[IE819Message] mustBe true
    }

    "return an instance of IE829Message" in {
      when(message.key).thenReturn(Some("IE829"))
      sut.createIEMessage(message).isInstanceOf[IE829Message] mustBe true
    }

    "return an instance of IE837Message" in {
      when(message.key).thenReturn(Some("IE837"))
      sut.createIEMessage(message).isInstanceOf[IE837Message] mustBe true
    }

    "return an instance of IE839Message" in {
      when(message.key).thenReturn(Some("IE839"))
      sut.createIEMessage(message).isInstanceOf[IE839Message] mustBe true
    }

    "return an instance of IE840Message" in {
      when(message.key).thenReturn(Some("IE840"))
      sut.createIEMessage(message).isInstanceOf[IE840Message] mustBe true
    }

    "return an instance of IE871Message" in {
      when(message.key).thenReturn(Some("IE871"))
      sut.createIEMessage(message).isInstanceOf[IE871Message] mustBe true
    }

    "return an instance of IE880Message" in {
      when(message.key).thenReturn(Some("IE880"))
      sut.createIEMessage(message).isInstanceOf[IE880Message] mustBe true
    }

    "return an instance of IE881Message" in {
      when(message.key).thenReturn(Some("IE881"))
      sut.createIEMessage(message).isInstanceOf[IE881Message] mustBe true
    }

    "return an instance of IE905Message" in {
      when(message.key).thenReturn(Some("IE905"))
      sut.createIEMessage(message).isInstanceOf[IE905Message] mustBe true
    }

  }

  "createFromType" should {
    "return throw an error" when {
      "cannot handle message type" in {

        the[Exception] thrownBy {
          sut.createFromXml("Anything", IE801)
        } must have message s"[IEMessageFactory] - Could not create Message object. Unsupported message: Anything"
      }
    }

    "return an instance of IE801Message" in {
      val result = sut.createFromXml("IE801", IE801).asInstanceOf[IE801Message]
      result.consignorId mustBe Some("tokentokentok")
      result.consigneeId mustBe Some("token")
      result.administrativeReferenceCode mustBe Some("tokentokentokentokent")
      result.localReferenceNumber mustBe Some("token")
    }

    "return an instance of IE802Message" in {
      sut.createFromXml("IE802", IE802).isInstanceOf[IE802Message] mustBe true
    }

    "return an instance of IE803Message" in {
      sut.createFromXml("IE803", IE803).asInstanceOf[IE803Message]
    }

    "return an instance of IE807Message" in {
      sut.createFromXml("IE807", IE807).asInstanceOf[IE807Message]
    }

    "return an instance of IE810Message" in {

      val result = sut.createFromXml("IE810", IE810).asInstanceOf[IE810Message]
      result.consignorId mustBe None
      result.consigneeId mustBe None
      result.administrativeReferenceCode mustBe Some("23GB00000000000377161")
      result.localReferenceNumber mustBe None
    }

    "return an instance of IE813Message" in {
      val result = sut.createFromXml("IE813", IE813).asInstanceOf[IE813Message]
      result.consignorId mustBe None
      result.consigneeId mustBe None
      result.administrativeReferenceCode mustBe Some("23GB00000000000378126")
    }

    "return an instance of IE815Message" in {
      val result = sut.createFromXml("IE815", IE815).asInstanceOf[IE815Message]
      result.consignorId mustBe "GBWK002281023"
      result.consigneeId mustBe Some("GBWKQOZ8OVLYR")
      result.administrativeReferenceCode mustBe None
      result.localReferenceNumber mustBe "LRNQA20230909022221"
    }

    "return an instance of IE818Message" in {
      val result = sut.createFromXml("IE818", IE818).asInstanceOf[IE818Message]
      result.consignorId mustBe None
      result.consigneeId mustBe Some("GBWK002281023")
      result.administrativeReferenceCode mustBe Some("23GB00000000000378553")
    }

    "return an instance of IE819Message" in {
      val result = sut.createFromXml("IE819", IE819).asInstanceOf[IE819Message]
      result.consignorId mustBe None
      result.consigneeId mustBe Some("GBWK002281023")
      result.administrativeReferenceCode mustBe Some("23GB00000000000378574")
    }

    "return an instance of IE829Message" in {
      sut.createFromXml("IE829", IE829).isInstanceOf[IE829Message] mustBe true
    }

    "return an instance of IE837Message with Consignor" in {
      val result = sut.createFromXml("IE837", IE837WithConsignor).asInstanceOf[IE837Message]
      result.consignorId mustBe Some("GBWK240176600")
      result.consigneeId mustBe None
      result.administrativeReferenceCode mustBe Some("16GB00000000000192223")
    }

    "return an instance of IE837Message with Consignee" in {
      val result = sut.createFromXml("IE837", IE837WithConsignee).asInstanceOf[IE837Message]
      result.consignorId mustBe None
      result.consigneeId mustBe Some("GBWK240176600")
      result.administrativeReferenceCode mustBe Some("16GB00000000000192223")
    }

    "return an instance of IE839Message with Consignee" in {
      val result = sut.createFromXml("IE839", IE839).asInstanceOf[IE839Message]
      result.consignorId mustBe None
      result.consigneeId mustBe Some("GBWK002281010")
      result.administrativeReferenceCode mustBe Some("91CFRXRWG7MKAFUCDJPA6")
    }

    "return an instance of IE840Message with Consignee" in {
      sut.createFromXml("IE840", IE840).isInstanceOf[IE840Message] mustBe true
    }

    "return an instance of IE871Message" in {
      val result = sut.createFromXml("IE871", IE871).asInstanceOf[IE871Message]
      result.consignorId mustBe Some("GBWK240176600")
      result.consigneeId mustBe None
      result.administrativeReferenceCode mustBe Some("23GB00000000000377768")
    }

    "return an instance of IE880Message" in {
      sut.createFromXml("IE880", IE880).isInstanceOf[IE880Message] mustBe true
    }

    "return an instance of IE881Message with Consignee" in {
      sut.createFromXml("IE881", IE881).isInstanceOf[IE881Message] mustBe true
    }

    "return an instance of IE905Message with Consignee" in {
      sut.createFromXml("IE905", IE905).isInstanceOf[IE905Message] mustBe true
    }
  }
}