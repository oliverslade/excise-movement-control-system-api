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

import generated.{IE839Type, MessagesOption}
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.MessageTypes

import scala.xml.NodeSeq

case class IE839Message (
                          private val obj: IE839Type,
                          private val key: Option[String],
                          private val namespace: Option[String]
                        ) extends IEMessage {

  def localReferenceNumber: Option[String] = {
    // todo: The xml could contains 2 LRN. Find out which one we need to retrieve
    for {
      rejectionAtImport <- obj.Body.RefusalByCustoms.NEadSub
      lrn = rejectionAtImport.LocalReferenceNumber
    } yield lrn
  }

  def consignorId: Option[String] = None

  override def consigneeId: Option[String] =
    for {
      consignee <- obj.Body.RefusalByCustoms.ConsigneeTrader
      traderId <- consignee.Traderid
    } yield traderId

  override def administrativeReferenceCode: Option[String] = {
    // todo: The xml contains 2 ARC number. We should check which ARC number we need to take or
    // if these ARC may be the same all the time. The ARC number may be missing depending if this
    // is a rejection at export or rejection at import. If so how can we determine the two factors
    // (export and import)?
    for {
      // todo: CEadVal is a list of exported groupd data. here we take the first one.
      // find out the above todo and which one take from the list. Or do we need them all?
      rejectionAtExport <- obj.Body.RefusalByCustoms.CEadVal.headOption
      arc = rejectionAtExport.AdministrativeReferenceCode
    } yield arc
  }

  override def messageType: String = MessageTypes.IE839.value

  override def toXml: NodeSeq = {
    scalaxb.toXML[IE839Type](obj, namespace, key, generated.defaultScope)
  }

  override def lrnEquals(lrn: String): Boolean =
    localReferenceNumber.contains(lrn)
}

object IE839Message {
  def apply(message: DataRecord[MessagesOption]): IE839Message = {
    IE839Message(message.as[IE839Type], message.key, message.namespace)
  }

  def createFromXml(xml: NodeSeq): IE839Message = {
    val ie839: IE839Type = scalaxb.fromXML[IE839Type](xml)
    IE839Message(ie839, Some(ie839.productPrefix), None)
  }
}