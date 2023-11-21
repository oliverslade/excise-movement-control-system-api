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

import generated.{IE881Type, MessagesOption}
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.MockitoSugar.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes

class IE881MessageSpec extends PlaySpec with BeforeAndAfterEach {

  private val message = mock[DataRecord[MessagesOption]]
  private val scalaXbMessageType = mock[IE881Type](RETURNS_DEEP_STUBS)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(message, scalaXbMessageType)

    when(message.key).thenReturn(Some("IE881"))
    when(message.namespace).thenReturn(Some("IE881"))
    when(message.as[Any]).thenReturn(scalaXbMessageType)
  }

  "messageType" should {
    "return IE881 type" in {
      IE881Message(message).messageType mustBe MessageTypes.IE881.value
    }
  }
}
