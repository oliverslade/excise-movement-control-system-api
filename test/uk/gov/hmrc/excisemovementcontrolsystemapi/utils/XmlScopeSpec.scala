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

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.NewMessagesXml

import scala.xml.{NamespaceBinding, TopScope}

class XmlScopeSpec extends PlaySpec with NewMessagesXml with XmlScope{

  "createFromXml1" should {
    "create scope from XML message" in {

      val result = createScopeFromXml(newMessageWith818And802, "IE818")

      val expectedUnfoldedScope = List(
        ("urn", "urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"),
        ("urn1", "urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.13")
      )

      unfoldNS(result).toSet mustBe expectedUnfoldedScope.toSet
    }

    "return a default scope if cannot create scope" in {
      val result = createScopeFromXml(newMessageWith818And802, "IE201")

      result mustBe generated.defaultScope
    }
  }

  def unfoldNS(ns: NamespaceBinding): List[(String, String)] = ns match {
    case TopScope => Nil
    case _ => (ns.prefix, ns.uri) :: unfoldNS(ns.parent)
  }
}
