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

package uk.gov.hmrc.excisemovementcontrolsystemapi.services

import generated.MessagesOption
import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.MockitoSugar.{reset, times, verify, when}
import org.mockito.captor.ArgCaptor
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.NewMessagesXml
import uk.gov.hmrc.excisemovementcontrolsystemapi.factories.IEMessageFactory
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.IEMessage
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.EmcsUtils

import java.nio.charset.StandardCharsets
import java.util.Base64
import scala.xml.{NamespaceBinding, NodeSeq, TopScope}

class NewMessageParserServiceSpec
  extends PlaySpec
    with NewMessagesXml
    with BeforeAndAfterEach {

  private val messageFactory = mock[IEMessageFactory]
  private val parser = new NewMessageParserService(messageFactory, new EmcsUtils())

  override def beforeEach(): Unit = {
    super.beforeEach()

    reset(messageFactory)
  }

  "countOfMessagesAvailable" should {
    "return the number of messages" in {
      val xml = encodeXml(newMessageWith2IE801sXml)
      parser.countOfMessagesAvailable(xml) mustBe 2
    }
  }


  "extractMessages" should {
    "extract all messages" in {

      val message1 = mock[IEMessage]
      val message2 = mock[IEMessage]
      when(messageFactory.createIEMessage(any, any)).thenReturn(message1, message2)

      val encodeGetNewMessage = encodeXml(newMessageWith818And802)
      parser.extractMessages(encodeGetNewMessage) mustBe Seq(message1, message2)

      val captor = ArgCaptor[DataRecord[MessagesOption]]
      verify(messageFactory, times(2)).createIEMessage(captor.capture, any)

      val messages = captor.values
      messages(0).key mustBe Some("IE818")
      messages(1).key mustBe Some("IE802")
    }

    "should use a TopScope for creating a message" in {
      when(messageFactory.createIEMessage(any, any))
        .thenReturn(mock[IEMessage], mock[IEMessage])

      val encodeGetNewMessage = encodeXml(newMessageWith818And802)
      val parser = partialNewMessageParserService
      parser.extractMessages(encodeGetNewMessage)

      verify(messageFactory, times(2)).createIEMessage(any, eqTo(TopScope))
    }

    "handle an empty list of messages" in {
      val encodeGetNewMessage = encodeXml(emptyNewMessageDataXml)
      parser.extractMessages(encodeGetNewMessage) mustBe Seq.empty
    }
  }

  private def partialNewMessageParserService: NewMessageParserService = {
    class PartialNewMessageParserService(factory: IEMessageFactory,
                                         emcsUtils: EmcsUtils) extends NewMessageParserService(factory, emcsUtils) {

      override def getScopeFromXml(xml: NodeSeq, key: Option[String]) : NamespaceBinding =
        super.getScopeFromXml(xml, None)
    }

    new PartialNewMessageParserService(messageFactory, new EmcsUtils())
  }

  private def encodeXml(xml: NodeSeq) = {
    Base64.getEncoder.encodeToString(xml.toString.getBytes(StandardCharsets.UTF_8))
  }
}

