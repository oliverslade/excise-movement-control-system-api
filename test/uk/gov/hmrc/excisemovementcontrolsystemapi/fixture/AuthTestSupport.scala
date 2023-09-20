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

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.MockitoSugar.when
import org.scalatestplus.mockito.MockitoSugar.mock
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.{affinityGroup, authorisedEnrolments, credentials, internalId}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, ~}
import uk.gov.hmrc.auth.core.syntax.retrieved.authSyntaxForRetrieved
import uk.gov.hmrc.auth.core.{AffinityGroup, AuthConnector, Enrolment, Enrolments}

import scala.concurrent.Future

trait AuthTestSupport {

  lazy val authConnector = mock[AuthConnector]
  val authFetch = authorisedEnrolments and affinityGroup and credentials and internalId
  val enrolment = Enrolment("HMRC-EMCS-ORG")

  def withAuthorizedTrader(identifier: String): Unit = {
    val retrieval = Enrolments(Set(enrolment.withIdentifier("ExciseNumber", identifier))) and
      Some(AffinityGroup.Organisation) and
      Some(Credentials("testProviderId", "testProviderType")) and
      Some("123")

    withAuthorization(retrieval)
  }

  def withUnAuthorizedERN: Unit = {
    val retrieval = Enrolments(Set(enrolment)) and
      Some(AffinityGroup.Organisation) and
      Some(Credentials("testProviderId", "testProviderType")) and
      Some("123")

    withAuthorization(retrieval)
  }

  def withAuthorization(
    retrieval: Enrolments ~ Option[AffinityGroup] ~ Option[Credentials] ~ Option[String]
  ): Unit = {

    when(authConnector.authorise(ArgumentMatchers.argThat((p: Predicate) => true), eqTo(authFetch))(any,any))
      .thenReturn(Future.successful(retrieval))
  }

  def withUnauthorizedTrader(error: Throwable): Unit =
    when(authConnector.authorise(any, any)(any, any)).thenReturn(Future.failed(error))

}
