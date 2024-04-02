/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.excisemovementcontrolsystemapi.models

import org.mockito.MockitoSugar.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.IE801Message
import uk.gov.hmrc.excisemovementcontrolsystemapi.repository.model.Message

import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64

class MovementSpec extends PlaySpec {

  "Message" should {
    "create a message object from xml" in {
      val xml = <urn1:IE801><urn1:Header><urn:Name>Jon</urn:Name></urn1:Header></urn1:IE801>
      val xmlWithoutPrefix = <IE801><Header><Name>Jon</Name></Header></IE801>
      val encodedXml = Base64.getEncoder.encodeToString(xml.toString().getBytes(StandardCharsets.UTF_8))
      val timestamp = Instant.now
      val message = mock[IE801Message]
      when(message.toXml).thenReturn(xml)
      when(message.messageType).thenReturn("IE801")
      when(message.messageIdentifier).thenReturn("123")


      Message(message, timestamp) mustBe Message(xmlWithoutPrefix.hashCode(), encodedXml, "IE801", "123", timestamp)
    }
  }
}
