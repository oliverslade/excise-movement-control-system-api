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

package uk.gov.hmrc.excisemovementcontrolsystemapi.writes.testObjects

import play.api.libs.json.{JsValue, Json}

object IE717TestMessageType extends TestMessageType {

  override def json1: JsValue = Json.parse("{\"Header\":{\"MessageSender\":\"NDEA.XI\",\"MessageRecipient\":\"NDEA.AT\",\"DateOfPreparation\":\"2023-06-26\",\"TimeOfPreparation\":\"23:18:33\",\"MessageIdentifier\":\"XI0003213\",\"CorrelationIdentifier\":\"bda98048-2d38-41ca-97b4-0bcd5ac564be\"},\"Body\":{\"ControlReportEnvelope\":{\"AttributesValue\":{\"ControlReportMessageType\":\"1\",\"DateAndTimeOfValidationOfControlReport\":\"2023-06-26T23:20:46\"},\"HeaderControlReport\":{\"ControlReportReference\":\"XI2023XYT6TGJUL3\",\"ControlOffice\":{\"ReferenceNumberOfControlOffice\":\"GB202332\",\"MemberStateCode\":\"GB\",\"ControlOfficeName\":\"Dover Port\",\"StreetName\":\"Ship Way\",\"StreetNumber\":\"23\",\"Postcode\":\"CT17 1XZ\",\"City\":\"Dover\",\"PhoneNumber\":\"0123459839383\",\"EmailAddress\":\"example@example.org\",\"attributes\":{\"@language\":\"en\"}}},\"ExciseMovement\":{\"AdministrativeReferenceCode\":\"23XI00000000000000331\",\"SequenceNumber\":\"1\"},\"OtherAccompanyingDocument\":{\"OtherAccompanyingDocumentType\":\"0\",\"ShortDescriptionOfOtherAccompanyingDocument\":{\"value\":\"Customs documents\",\"attributes\":{\"@language\":\"en\"}},\"OtherAccompanyingDocumentNumber\":\"XI7478484ABC\",\"OtherAccompanyingDocumentDate\":\"2023-06-26\",\"MemberStateOfDispatch\":\"XI\",\"MemberStateOfDestination\":\"AT\",\"PersonInvolvedInMovementTrader\":[{\"TraderExciseNumber\":\"XIWK000467015\",\"TraderName\":\"Clarkys Eagles\",\"TraderPersonType\":\"1\",\"StreetName\":\"Happy Street\",\"Postcode\":\"ZZ1 1BG\",\"City\":\"The City\",\"attributes\":{\"@language\":\"en\"}},{\"TraderExciseNumber\":\"GBWK000602056\",\"TraderName\":\"LUCY'S GIN STORE\",\"TraderPersonType\":\"2\",\"StreetName\":\"The Road\",\"Postcode\":\"MC234\",\"City\":\"Happy Town\",\"attributes\":{\"@language\":\"en\"}}],\"GoodsItem\":[{\"DescriptionOfTheGoods\":\"Gin\",\"CnCode\":\"27101231\",\"CommercialDescriptionOfTheGoods\":\"Gin\",\"Quantity\":100,\"GrossMass\":100,\"NetMass\":90}]},\"ControlReport\":{\"DateOfControl\":\"2023-06-26\",\"PlaceOfControl\":{\"value\":\"Dover Port\",\"attributes\":{\"@language\":\"en\"}},\"ControlType\":\"1\",\"ReasonForControl\":\"1\",\"ComplementaryOriginReference\":{\"value\":\"XIfh4833383l2023\",\"attributes\":{\"@language\":\"en\"}},\"ControlOfficerIdentity\":{\"value\":\"Steve\",\"attributes\":{\"@language\":\"en\"}},\"GlobalControlConclusion\":\"3\",\"ControlAtArrivalRequired\":\"1\",\"Flag\":\"0\",\"Comments\":{\"value\":\"Goods are bad. Need to seize them :(\",\"attributes\":{\"@language\":\"en\"}},\"PerformedControlAction\":[{\"PerformedControlAction\":\"9\",\"OtherControlAction\":{\"value\":\"token\",\"attributes\":{\"@language\":\"to\"}}},{\"PerformedControlAction\":\"3\",\"OtherControlAction\":{\"value\":\"token\",\"attributes\":{\"@language\":\"to\"}}},{\"PerformedControlAction\":\"0\",\"OtherControlAction\":{\"value\":\"Tested it a bit\",\"attributes\":{\"@language\":\"en\"}}}],\"SupportingEvidence\":[{\"IssuingAuthority\":{\"value\":\"Border Agency\",\"attributes\":{\"@language\":\"en\"}},\"EvidenceTypeCode\":\"01\",\"ReferenceOfEvidence\":{\"value\":\"Over here\",\"attributes\":{\"@language\":\"en\"}},\"ImageOfEvidence\":[98,101,108,108,97]}],\"UnsatisfactoryReason\":[{\"UnsatisfactoryReasonCode\":\"2\",\"ComplementaryInformation\":{\"value\":\"Not good\",\"attributes\":{\"@language\":\"en\"}}}],\"TransportDetails\":[{\"TransportUnitCode\":\"1\",\"IdentityOfTransportUnits\":\"Train\",\"CommercialSealIdentification\":\"AZnrj4jff\",\"SealInformation\":{\"value\":\"Piece of paper\",\"attributes\":{\"@language\":\"en\"}},\"ComplementaryInformation\":{\"value\":\"Pretty nice seal!\",\"attributes\":{\"@language\":\"en\"}}}],\"BodyControlReport\":[{\"BodyRecordUniqueReference\":\"101\",\"DescriptionOfTheGoods\":\"Gin\",\"CnCode\":\"84866332\",\"AdditionalCode\":\"NH22\",\"Comments\":{\"value\":\"Had to investigate this shipment\",\"attributes\":{\"@language\":\"en\"}},\"UnsatisfactoryReason\":[{\"UnsatisfactoryReasonCode\":\"1\",\"ComplementaryInformation\":{\"value\":\"Not good\",\"attributes\":{\"@language\":\"en\"}}}]}]}}}}")

}
