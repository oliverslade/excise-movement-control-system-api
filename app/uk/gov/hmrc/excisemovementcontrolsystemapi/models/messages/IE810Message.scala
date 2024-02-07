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

import generated.{IE810Type, MessagesOption}
import play.api.libs.json.{JsValue, Json}
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType.CancelMovement
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.MessageTypeFormats.GeneratedJsonWriters

import scala.xml.{NamespaceBinding,  TopScope}

case class IE810Message(
                         private val obj: IE810Type,
                         private val key: Option[String],
                         private val scope: NamespaceBinding,
                         auditType: AuditType
                       ) extends IEMessage with GeneratedJsonWriters {
  def localReferenceNumber: Option[String] = None

  def consignorId: Option[String] = None

  override def consigneeId: Option[String] = None

  override def administrativeReferenceCode: Seq[Option[String]] =
    Seq(Some(obj.Body.CancellationOfEAD.ExciseMovementEad.AdministrativeReferenceCode))

  override def messageType: String = MessageTypes.IE810.value

  override def toXml: NodeSeq = {
    scalaxb.toXML[IE810Type](obj, key.getOrElse(messageType), scope)
  }

  override def toJson: JsValue = Json.toJson(obj)

  override def lrnEquals(lrn: String): Boolean = false

  override def messageIdentifier: String = obj.Header.MessageIdentifier

  override def toString: String = s"Message type: $messageType, message identifier: $messageIdentifier, ARC: $administrativeReferenceCode"

}

object IE810Message {
  def apply(message: DataRecord[MessagesOption], scope: NamespaceBinding): IE810Message = {
    IE810Message(message.as[IE810Type], message.key, scope, CancelMovement)
  }

  def createFromXml(xml: NodeSeq): IE810Message = {
    val ie810: IE810Type = scalaxb.fromXML[IE810Type](xml)
    IE810Message(ie810, Some(ie810.productPrefix), TopScope, CancelMovement)
  }
}