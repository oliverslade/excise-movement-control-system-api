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
import play.api.libs.json.Json
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType.ManualClosure
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.MessageTypeFormats.GeneratedJsonWriters

import scala.xml.NodeSeq

case class IE881Message(
                         private val obj: IE881Type,
                         private val key: Option[String],
                         private val namespace: Option[String],
                         auditType: AuditType
                       ) extends IEMessage with GeneratedJsonWriters{

  override def consigneeId: Option[String] = None

  override def administrativeReferenceCode: Seq[Option[String]] =
    Seq(Some(obj.Body.ManualClosureResponse.AttributesValue.AdministrativeReferenceCode))

  override def messageType: String = MessageTypes.IE881.value

  override def toXml: NodeSeq = {
    scalaxb.toXML[IE881Type](obj, namespace, key, generated.defaultScope)
  }
  override def toJson = Json.toJson(obj)

  override def lrnEquals(lrn: String): Boolean = false

  override def messageIdentifier: String = obj.Header.MessageIdentifier
}

object IE881Message {
  def apply(message: DataRecord[MessagesOption]): IE881Message = {
    IE881Message(message.as[IE881Type], message.key, message.namespace, ManualClosure)
  }

  def createFromXml(xml: NodeSeq): IE881Message = {
    val ie881: IE881Type = scalaxb.fromXML[IE881Type](xml)
    IE881Message(ie881, Some(ie881.productPrefix), None, ManualClosure)
  }
}