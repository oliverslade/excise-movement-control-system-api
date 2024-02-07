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

  "createFromXml" should {
    "create scope from XML" in {

      val result = createScopeFromXml(newMessageWith818And802, "NewMessagesDataResponse")

      val expectedUnfoldedScope = List(
        ("ns", "http://www.govtalk.gov.uk/taxation/InternationalTrade/Excise/NewMessagesData/3"),
        ("urn", "urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"),
        ("urn1", "urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.01"),
        ("urn2", "urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE802:V3.01")
      )

      unfoldNS(result).toSet mustBe expectedUnfoldedScope.toSet
    }

    "return an empty" when {
      "not match found for label" in {
        val result = createScopeFromXml(newMessageWith818And802, "test")

        result mustBe TopScope
      }

      "No scope found" in {
        val xmlStr =
          """<body>
            | <message>test</message>
            |</body>
            |""".stripMargin

        val xml = scala.xml.XML.loadString(xmlStr)

        val result = createScopeFromXml(xml, "body")

        result mustBe TopScope
      }
    }
  }

  def unfoldNS(ns: NamespaceBinding): List[(String, String)] = ns match {
    case TopScope => Nil
    case _ => (ns.prefix, ns.uri) :: unfoldNS(ns.parent)
  }
}
