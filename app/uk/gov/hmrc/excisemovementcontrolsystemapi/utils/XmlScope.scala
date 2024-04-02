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

package uk.gov.hmrc.excisemovementcontrolsystemapi.utils

import scala.xml.{NamespaceBinding, NodeSeq, TopScope}

trait XmlScope {

  /*
  Create a scope from the XML.
   */
  def createScopeFromXml(xml: NodeSeq, messageType: String): NamespaceBinding = {

    val unfoldedScope = extractScope(xml, messageType)
    val scope = scalaxb.toScope(unfoldedScope: _*)

    if(scope.equals(TopScope)) generated.defaultScope
    else scope
  }

  /*
  Extract the scope from the XML message. This go through the XML and collect
  any prefix (e.g urn if <urn:IE801>) and return an unfolded scope for all
  the prefix collected
   */
  private def extractScope(xml: NodeSeq, messageType: String): Seq[(Option[String], String)] = {
    (xml \\ messageType \\ "_").map { o =>
        if (o.scope.equals(TopScope)) (None, "")
        else (Some(o.prefix), o.namespace)
      }
      .distinctBy(identity)
      .filterNot(_._1.isEmpty)
  }
}

