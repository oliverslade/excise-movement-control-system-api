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

import generated.{IE717Type, MessagesOption}
import play.api.libs.json.{JsValue, Json}
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auditing.AuditType.{ControlResponse, Refused}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.MessageTypeFormats.GeneratedJsonWriters

import scala.xml.NodeSeq

case class IE717Message
(
  private val obj: IE717Type,
  private val key: Option[String],
  private val namespace: Option[String],
  auditType: AuditType
) extends IEMessage with GeneratedJsonWriters {
  override def consigneeId: Option[String] = None

  override def administrativeReferenceCode: Seq[Option[String]] =
    Seq(
      obj.Body.ControlReportEnvelope.ExciseMovement map {
        _.AdministrativeReferenceCode
      }
    )

  override def messageType: String = MessageTypes.IE717.value

  override def messageIdentifier: String = obj.Header.MessageIdentifier

  override def toXml: NodeSeq =
    scalaxb.toXML[IE717Type](obj, namespace, key, generated.defaultScope)

  override def toJson: JsValue = Json.toJson(obj)

  override def lrnEquals(lrn: String): Boolean = false
}

object IE717Message {
  def apply(message: DataRecord[MessagesOption]): IE717Message = {
    IE717Message(message.as[IE717Type], message.key, message.namespace, ControlResponse)
  }

  def createFromXml(xml: NodeSeq): IE717Message = {
    val ie717: IE717Type = scalaxb.fromXML[IE717Type](xml)
    IE717Message(ie717, Some(ie717.productPrefix), None, ControlResponse)
  }
}
