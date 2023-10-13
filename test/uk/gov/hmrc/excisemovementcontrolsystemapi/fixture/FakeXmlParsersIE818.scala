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

package uk.gov.hmrc.excisemovementcontrolsystemapi.fixture

import generated.{IE815Type, IE818Type}
import play.api.mvc.Result
import play.api.mvc.Results.BadRequest
import uk.gov.hmrc.excisemovementcontrolsystemapi.controllers.actions.{ParseIE815XmlAction, ParseIE818XmlAction}
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.auth.{EnrolmentRequest, ParsedXmlRequest, ParsedXmlRequestIE818}

import scala.concurrent.{ExecutionContext, Future}

trait FakeXmlParsersIE818 {
  case class FakeSuccessIE818XMLParser(ieMessage: IE818Type) extends ParseIE818XmlAction {
    override def refine[A](request: EnrolmentRequest[A]): Future[Either[Result, ParsedXmlRequestIE818[A]]] = {
      Future.successful(Right(ParsedXmlRequestIE818(EnrolmentRequest(request, Set.empty, "123"),null, Set.empty, "123")))
    }

    override protected def executionContext: ExecutionContext = ExecutionContext.Implicits.global
  }

  object FakeFailureIE818XMLParser extends ParseIE818XmlAction {
    override def refine[A](request: EnrolmentRequest[A]): Future[Either[Result, ParsedXmlRequestIE818[A]]] = {
      Future.successful(Left(BadRequest("Invalid xml supplied")))
    }

    override protected def executionContext: ExecutionContext = ExecutionContext.Implicits.global
  }

}
