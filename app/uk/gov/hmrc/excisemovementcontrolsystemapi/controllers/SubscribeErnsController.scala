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

package uk.gov.hmrc.excisemovementcontrolsystemapi.controllers

import cats.implicits.toFunctorOps
import play.api.Logging
import play.api.libs.json.{JsValue, Json, OFormat}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.excisemovementcontrolsystemapi.controllers.SubscribeErnsController.{SubscribeErnRequest, UnsubscribeErnRequest}
import uk.gov.hmrc.excisemovementcontrolsystemapi.controllers.actions.AuthAction
import uk.gov.hmrc.excisemovementcontrolsystemapi.services.NotificationsService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SubscribeErnsController @Inject() (
  authAction: AuthAction,
  cc: ControllerComponents,
  notificationsService: NotificationsService
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def subscribeErn(ernID: String): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[SubscribeErnRequest] { subscribeRequest =>
      notificationsService
        .subscribeErns(subscribeRequest.clientId, Seq(ernID))
        .as(Ok)
    }
  }

  def unsubscribeErn(ernID: String): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[UnsubscribeErnRequest] { subscribeRequest =>
      notificationsService
        .unsubscribeErns(subscribeRequest.clientId, Seq(ernID))
        .as(Ok)
    }
  }

}

object SubscribeErnsController {

  final case class SubscribeErnRequest(clientId: String, ern: String)

  object SubscribeErnRequest {
    implicit lazy val format: OFormat[SubscribeErnRequest] = Json.format
  }

  final case class UnsubscribeErnRequest(clientId: String, ern: String)

  object UnsubscribeErnRequest {
    implicit lazy val format: OFormat[UnsubscribeErnRequest] = Json.format
  }
}
