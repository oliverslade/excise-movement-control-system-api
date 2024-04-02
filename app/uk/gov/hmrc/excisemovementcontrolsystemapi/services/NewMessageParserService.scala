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

import generated.NewMessagesDataResponse
import uk.gov.hmrc.excisemovementcontrolsystemapi.factories.IEMessageFactory
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.IEMessage
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.{EmcsUtils, XmlScope}

import javax.inject.Inject
import scala.xml.{NamespaceBinding, NodeSeq, TopScope}

class NewMessageParserService @Inject()
(
  factory: IEMessageFactory,
  emcsUtils: EmcsUtils
) extends XmlScope {


  def countOfMessagesAvailable(encodedMessage: String): Long = {
    val newMessage = scala.xml.XML.loadString(emcsUtils.decode(encodedMessage))
    (newMessage \ "CountOfMessagesAvailable").text.toLong
  }

  def extractMessages(encodedMessage: String): Seq[IEMessage] = {
    val decodedMessage: String = emcsUtils.decode(encodedMessage)
    val xml = scala.xml.XML.loadString(decodedMessage)
    val newMessageDataResponse = scalaxb.fromXML[NewMessagesDataResponse](xml)

    for {
      m <- newMessageDataResponse.Messages.messagesoption
      scope = getScopeFromXml(xml, m.key)
      message = factory.createIEMessage(m, scope)
    } yield message
  }

  protected def getScopeFromXml(xml: NodeSeq, key: Option[String]) : NamespaceBinding = {
    key match {
      case Some(k) => createScopeFromXml(xml, k)
      case _ => TopScope
    }
  }
}


