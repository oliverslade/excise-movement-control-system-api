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

package uk.gov.hmrc.excisemovementcontrolsystemapi.services

import generated.MessagesOption
import org.mockito.ArgumentMatchersSugar.{any, eqTo}
import org.mockito.MockitoSugar.{reset, times, verify, when}
import org.mockito.captor.ArgCaptor
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import scalaxb.DataRecord
import uk.gov.hmrc.excisemovementcontrolsystemapi.data.NewMessagesXml
import uk.gov.hmrc.excisemovementcontrolsystemapi.factories.IEMessageFactory
import uk.gov.hmrc.excisemovementcontrolsystemapi.models.messages.IEMessage
import uk.gov.hmrc.excisemovementcontrolsystemapi.utils.EmcsUtils

import java.nio.charset.StandardCharsets
import java.util.Base64
import scala.xml.{NamespaceBinding, NodeSeq, TopScope}

class NewMessageParserServiceSpec
  extends PlaySpec
    with NewMessagesXml
    with BeforeAndAfterEach {

  private val messageFactory = mock[IEMessageFactory]
  private val parser = new NewMessageParserService(messageFactory, new EmcsUtils())

  override def beforeEach(): Unit = {
    super.beforeEach()

    reset(messageFactory)
  }

  "countOfMessagesAvailable" should {
    "return the number of messages" in {
      val xml = encodeXml(newMessageWith2IE801sXml)
      parser.countOfMessagesAvailable(xml) mustBe 2
    }
  }


  "extractMessages" should {
    "extract all messages" in {

      val message1 = mock[IEMessage]
      val message2 = mock[IEMessage]
      when(messageFactory.createIEMessage(any, any)).thenReturn(message1, message2)

      val encodeGetNewMessage = encodeXml(newMessageWith818And802)
      parser.extractMessages(encodeGetNewMessage) mustBe Seq(message1, message2)

      val captor = ArgCaptor[DataRecord[MessagesOption]]
      verify(messageFactory, times(2)).createIEMessage(captor.capture, any)

      val messages = captor.values
      messages(0).key mustBe Some("IE818")
      messages(1).key mustBe Some("IE802")
    }

    "should use a TopScope for creating a message" in {
      when(messageFactory.createIEMessage(any, any))
        .thenReturn(mock[IEMessage], mock[IEMessage])

      val encodeGetNewMessage = encodeXml(newMessageWith818And802)
      val parser = partialNewMessageParserService
      parser.extractMessages(encodeGetNewMessage)

      verify(messageFactory, times(2)).createIEMessage(any, eqTo(TopScope))
    }

    "handle an empty list of messages" in {
      val encodeGetNewMessage = encodeXml(emptyNewMessageDataXml)
      parser.extractMessages(encodeGetNewMessage) mustBe Seq.empty
    }
  }

  private def partialNewMessageParserService: NewMessageParserService = {
    class PartialNewMessageParserService(factory: IEMessageFactory,
                                         emcsUtils: EmcsUtils) extends NewMessageParserService(factory, emcsUtils) {

      override def getScopeFromXml(xml: NodeSeq, key: Option[String]) : NamespaceBinding =
        super.getScopeFromXml(xml, None)
    }

    new PartialNewMessageParserService(messageFactory, new EmcsUtils())
  }

  private def encodeXml(xml: NodeSeq) = {
    Base64.getEncoder.encodeToString(xml.toString.getBytes(StandardCharsets.UTF_8))
  }

  private def xml = {
    <ns:NewMessagesDataResponse
    xmlns:ns="http://www.govtalk.gov.uk/taxation/InternationalTrade/Excise/NewMessagesData/3"
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
    xmlns:ns1="http://www.govtalk.gov.uk/taxation/InternationalTrade/Excise/ie704uk/3"
    xmlns:tms="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13"
    xmlns:urn2="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13"
    xmlns:urn3="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.13"
    xmlns:urn4="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13"
    xmlns:urn5="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.13"
    xmlns:urn6="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE871:V3.13"
    xmlns:urn7="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13"
    xmlns:urn8="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE810:V3.13"
    xmlns:urn9="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13">
      <ns:Messages>
        <ns1:IE704>
          <ns1:Header>
            <urn:MessageSender>NDEA.XI</urn:MessageSender>
            <urn:MessageRecipient>NDEA.XI</urn:MessageRecipient>
            <urn:DateOfPreparation>2008-09-29</urn:DateOfPreparation>
            <urn:TimeOfPreparation>00:18:33</urn:TimeOfPreparation>
            <urn:MessageIdentifier>XI000001</urn:MessageIdentifier>
            <urn:CorrelationIdentifier>9b8effe4-adca-4431-bfc2-d65bb5f1e15d</urn:CorrelationIdentifier>
          </ns1:Header>
          <ns1:Body>
            <ns1:GenericRefusalMessage>
              <ns1:Attributes>
                <ns1:LocalReferenceNumber>lrnie8158976912</ns1:LocalReferenceNumber>
              </ns1:Attributes>
              <ns1:FunctionalError>
                <ns1:ErrorType>4401</ns1:ErrorType>
                <ns1:ErrorReason>token</ns1:ErrorReason>
                <ns1:ErrorLocation>token</ns1:ErrorLocation>
                <ns1:OriginalAttributeValue>token</ns1:OriginalAttributeValue>
              </ns1:FunctionalError>
            </ns1:GenericRefusalMessage>
          </ns1:Body>
        </ns1:IE704>
        <urn1:IE801>
          <urn1:Header>
            <tms:MessageSender>NDEA.XI</tms:MessageSender>
            <tms:MessageRecipient>NDEA.AT</tms:MessageRecipient>
            <tms:DateOfPreparation>2023-06-22</tms:DateOfPreparation>
            <tms:TimeOfPreparation>12:37:08.755</tms:TimeOfPreparation>
            <tms:MessageIdentifier>XI000002</tms:MessageIdentifier>
          </urn1:Header>
          <urn1:Body>
            <urn1:EADESADContainer>
              <urn1:ConsigneeTrader language="en">
                <urn1:Traderid>AT00000602078</urn1:Traderid>
                <urn1:TraderName>AFOR KALE LTD</urn1:TraderName>
                <urn1:StreetName>The Street</urn1:StreetName>
                <urn1:Postcode>AT123</urn1:Postcode>
                <urn1:City>The City</urn1:City>
              </urn1:ConsigneeTrader>
              <urn1:ExciseMovement>
                <urn1:AdministrativeReferenceCode>23XI00000000000000013</urn1:AdministrativeReferenceCode>
                <urn1:DateAndTimeOfValidationOfEadEsad>2023-06-22T11:37:10.345739396</urn1:DateAndTimeOfValidationOfEadEsad>
              </urn1:ExciseMovement>
              <urn1:ConsignorTrader language="en">
                <urn1:TraderExciseNumber>GBWKQOZ8OVLYR</urn1:TraderExciseNumber>
                <urn1:TraderName>Clarkys Eagles</urn1:TraderName>
                <urn1:StreetName>Happy Street</urn1:StreetName>
                <urn1:Postcode>BT1 1BG</urn1:Postcode>
                <urn1:City>The City</urn1:City>
              </urn1:ConsignorTrader>
              <urn1:PlaceOfDispatchTrader language="en">
                <urn1:ReferenceOfTaxWarehouse>XI00000467014</urn1:ReferenceOfTaxWarehouse>
              </urn1:PlaceOfDispatchTrader>
              <urn1:DeliveryPlaceTrader language="en">
                <urn1:Traderid>AT00000602078</urn1:Traderid>
                <urn1:TraderName>METEST BOND STTSTGE</urn1:TraderName>
                <urn1:StreetName>WHITETEST ROAD METEST CITY ESTATE</urn1:StreetName>
                <urn1:Postcode>BN2 4KX</urn1:Postcode>
                <urn1:City>STTEST,KENT</urn1:City>
              </urn1:DeliveryPlaceTrader>
              <urn1:CompetentAuthorityDispatchOffice>
                <urn1:ReferenceNumber>GB004098</urn1:ReferenceNumber>
              </urn1:CompetentAuthorityDispatchOffice>
              <urn1:EadEsad>
                <urn1:LocalReferenceNumber>lrnie8156540856</urn1:LocalReferenceNumber>
                <urn1:InvoiceNumber>INVOICE001</urn1:InvoiceNumber>
                <urn1:InvoiceDate>2018-04-04</urn1:InvoiceDate>
                <urn1:OriginTypeCode>1</urn1:OriginTypeCode>
                <urn1:DateOfDispatch>2021-12-02</urn1:DateOfDispatch>
                <urn1:TimeOfDispatch>22:37:00</urn1:TimeOfDispatch>
              </urn1:EadEsad>
              <urn1:HeaderEadEsad>
                <urn1:SequenceNumber>1</urn1:SequenceNumber>
                <urn1:DateAndTimeOfUpdateValidation>2023-06-22T11:37:10.345801029</urn1:DateAndTimeOfUpdateValidation>
                <urn1:DestinationTypeCode>1</urn1:DestinationTypeCode>
                <urn1:JourneyTime>D01</urn1:JourneyTime>
                <urn1:TransportArrangement>1</urn1:TransportArrangement>
              </urn1:HeaderEadEsad>
              <urn1:TransportMode>
                <urn1:TransportModeCode>1</urn1:TransportModeCode>
              </urn1:TransportMode>
              <urn1:MovementGuarantee>
                <urn1:GuarantorTypeCode>1</urn1:GuarantorTypeCode>
              </urn1:MovementGuarantee>
              <urn1:BodyEadEsad>
                <urn1:BodyRecordUniqueReference>1</urn1:BodyRecordUniqueReference>
                <urn1:ExciseProductCode>E410</urn1:ExciseProductCode>
                <urn1:CnCode>27101231</urn1:CnCode>
                <urn1:Quantity>100.000</urn1:Quantity>
                <urn1:GrossMass>100.00</urn1:GrossMass>
                <urn1:NetMass>90.00</urn1:NetMass>
                <urn1:Density>10.00</urn1:Density>
                <urn1:Package>
                  <urn1:KindOfPackages>BH</urn1:KindOfPackages>
                  <urn1:NumberOfPackages>2</urn1:NumberOfPackages>
                  <urn1:ShippingMarks>Subhasis Swain1</urn1:ShippingMarks>
                </urn1:Package>
                <urn1:Package>
                  <urn1:KindOfPackages>BH</urn1:KindOfPackages>
                  <urn1:NumberOfPackages>2</urn1:NumberOfPackages>
                  <urn1:ShippingMarks>Subhasis Swain 2</urn1:ShippingMarks>
                </urn1:Package>
              </urn1:BodyEadEsad>
              <urn1:TransportDetails>
                <urn1:TransportUnitCode>1</urn1:TransportUnitCode>
                <urn1:IdentityOfTransportUnits>Transformers robots in disguise</urn1:IdentityOfTransportUnits>
              </urn1:TransportDetails>
              <urn1:TransportDetails>
                <urn1:TransportUnitCode>2</urn1:TransportUnitCode>
                <urn1:IdentityOfTransportUnits>MACHINES</urn1:IdentityOfTransportUnits>
              </urn1:TransportDetails>
              <urn1:TransportDetails>
                <urn1:TransportUnitCode>3</urn1:TransportUnitCode>
                <urn1:IdentityOfTransportUnits>MORE MACHINES</urn1:IdentityOfTransportUnits>
              </urn1:TransportDetails>
            </urn1:EADESADContainer>
          </urn1:Body>
        </urn1:IE801>
        <urn2:IE801>
          <urn2:Header>
            <tms:MessageSender>NDEA.XI</tms:MessageSender>
            <tms:MessageRecipient>NDEA.AT</tms:MessageRecipient>
            <tms:DateOfPreparation>2023-06-22</tms:DateOfPreparation>
            <tms:TimeOfPreparation>12:37:08.755</tms:TimeOfPreparation>
            <tms:MessageIdentifier>XI000003</tms:MessageIdentifier>
          </urn2:Header>
          <urn2:Body>
            <urn2:EADESADContainer>
              <urn2:ConsigneeTrader language="en">
                <urn2:Traderid>AT00000602080</urn2:Traderid>
                <urn2:TraderName>AFOR KALE LTD</urn2:TraderName>
                <urn2:StreetName>The Street</urn2:StreetName>
                <urn2:Postcode>AT123</urn2:Postcode>
                <urn2:City>The City</urn2:City>
              </urn2:ConsigneeTrader>
              <urn2:ExciseMovement>
                <urn2:AdministrativeReferenceCode>23XI00000000000000014</urn2:AdministrativeReferenceCode>
                <urn2:DateAndTimeOfValidationOfEadEsad>2023-06-22T11:37:10.345739396</urn2:DateAndTimeOfValidationOfEadEsad>
              </urn2:ExciseMovement>
              <urn2:ConsignorTrader language="en">
                <urn2:TraderExciseNumber>GBWKQOZ8OVLYR</urn2:TraderExciseNumber>
                <urn2:TraderName>Clarkys Eagles</urn2:TraderName>
                <urn2:StreetName>Happy Street</urn2:StreetName>
                <urn2:Postcode>BT1 1BG</urn2:Postcode>
                <urn2:City>The City</urn2:City>
              </urn2:ConsignorTrader>
              <urn2:PlaceOfDispatchTrader language="en">
                <urn2:ReferenceOfTaxWarehouse>XI00000467014</urn2:ReferenceOfTaxWarehouse>
              </urn2:PlaceOfDispatchTrader>
              <urn2:DeliveryPlaceTrader language="en">
                <urn2:Traderid>AT00000602078</urn2:Traderid>
                <urn2:TraderName>METEST BOND STTSTGE</urn2:TraderName>
                <urn2:StreetName>WHITETEST ROAD METEST CITY ESTATE</urn2:StreetName>
                <urn2:Postcode>BN2 4KX</urn2:Postcode>
                <urn2:City>STTEST,KENT</urn2:City>
              </urn2:DeliveryPlaceTrader>
              <urn2:CompetentAuthorityDispatchOffice>
                <urn2:ReferenceNumber>GB004098</urn2:ReferenceNumber>
              </urn2:CompetentAuthorityDispatchOffice>
              <urn2:EadEsad>
                <urn2:LocalReferenceNumber>lrnie8155973812</urn2:LocalReferenceNumber>
                <urn2:InvoiceNumber>INVOICE001</urn2:InvoiceNumber>
                <urn2:InvoiceDate>2018-04-04</urn2:InvoiceDate>
                <urn2:OriginTypeCode>1</urn2:OriginTypeCode>
                <urn2:DateOfDispatch>2021-12-02</urn2:DateOfDispatch>
                <urn2:TimeOfDispatch>22:37:00</urn2:TimeOfDispatch>
              </urn2:EadEsad>
              <urn2:HeaderEadEsad>
                <urn2:SequenceNumber>1</urn2:SequenceNumber>
                <urn2:DateAndTimeOfUpdateValidation>2023-06-22T11:37:10.345801029</urn2:DateAndTimeOfUpdateValidation>
                <urn2:DestinationTypeCode>1</urn2:DestinationTypeCode>
                <urn2:JourneyTime>D01</urn2:JourneyTime>
                <urn2:TransportArrangement>1</urn2:TransportArrangement>
              </urn2:HeaderEadEsad>
              <urn2:TransportMode>
                <urn2:TransportModeCode>1</urn2:TransportModeCode>
              </urn2:TransportMode>
              <urn2:MovementGuarantee>
                <urn2:GuarantorTypeCode>1</urn2:GuarantorTypeCode>
              </urn2:MovementGuarantee>
              <urn2:BodyEadEsad>
                <urn2:BodyRecordUniqueReference>1</urn2:BodyRecordUniqueReference>
                <urn2:ExciseProductCode>E410</urn2:ExciseProductCode>
                <urn2:CnCode>27101231</urn2:CnCode>
                <urn2:Quantity>100.000</urn2:Quantity>
                <urn2:GrossMass>100.00</urn2:GrossMass>
                <urn2:NetMass>90.00</urn2:NetMass>
                <urn2:Density>10.00</urn2:Density>
                <urn2:Package>
                  <urn2:KindOfPackages>BH</urn2:KindOfPackages>
                  <urn2:NumberOfPackages>2</urn2:NumberOfPackages>
                  <urn2:ShippingMarks>Subhasis Swain1</urn2:ShippingMarks>
                </urn2:Package>
                <urn2:Package>
                  <urn2:KindOfPackages>BH</urn2:KindOfPackages>
                  <urn2:NumberOfPackages>2</urn2:NumberOfPackages>
                  <urn2:ShippingMarks>Subhasis Swain 2</urn2:ShippingMarks>
                </urn2:Package>
              </urn2:BodyEadEsad>
              <urn2:TransportDetails>
                <urn2:TransportUnitCode>1</urn2:TransportUnitCode>
                <urn2:IdentityOfTransportUnits>Transformers robots in disguise</urn2:IdentityOfTransportUnits>
              </urn2:TransportDetails>
              <urn2:TransportDetails>
                <urn2:TransportUnitCode>2</urn2:TransportUnitCode>
                <urn2:IdentityOfTransportUnits>MACHINES</urn2:IdentityOfTransportUnits>
              </urn2:TransportDetails>
              <urn2:TransportDetails>
                <urn2:TransportUnitCode>3</urn2:TransportUnitCode>
                <urn2:IdentityOfTransportUnits>MORE MACHINES</urn2:IdentityOfTransportUnits>
              </urn2:TransportDetails>
            </urn2:EADESADContainer>
          </urn2:Body>
        </urn2:IE801>
        <urn3:IE818>
          <urn3:Header>
            <urn:MessageSender>NDEA.XI</urn:MessageSender>
            <urn:MessageRecipient>NDEA.XI</urn:MessageRecipient>
            <urn:DateOfPreparation>2006-08-04</urn:DateOfPreparation>
            <urn:TimeOfPreparation>09:43:40</urn:TimeOfPreparation>
            <urn:MessageIdentifier>XI0000015</urn:MessageIdentifier>
            <urn:CorrelationIdentifier>8a4127de-904f-46ca-a779-70238f21c4bd</urn:CorrelationIdentifier>
          </urn3:Header>
          <urn3:Body>
            <urn3:AcceptedOrRejectedReportOfReceiptExport>
              <urn3:Attributes>
                <urn3:DateAndTimeOfValidationOfReportOfReceiptExport>2001-01-03T11:25:01</urn3:DateAndTimeOfValidationOfReportOfReceiptExport>
              </urn3:Attributes>
              <urn3:ConsigneeTrader language="to">
                <urn3:Traderid>GBWK002901025</urn3:Traderid>
                <urn3:TraderName>token</urn3:TraderName>
                <urn3:StreetName>token</urn3:StreetName>
                <urn3:StreetNumber>token</urn3:StreetNumber>
                <urn3:Postcode>token</urn3:Postcode>
                <urn3:City>token</urn3:City>
                <urn3:EoriNumber>token</urn3:EoriNumber>
              </urn3:ConsigneeTrader>
              <urn3:ExciseMovement>
                <urn3:AdministrativeReferenceCode>23XI00000000000000014</urn3:AdministrativeReferenceCode>
                <urn3:SequenceNumber>1</urn3:SequenceNumber>
              </urn3:ExciseMovement>
              <urn3:DeliveryPlaceTrader language="to">
                <urn3:Traderid>token</urn3:Traderid>
                <urn3:TraderName>token</urn3:TraderName>
                <urn3:StreetName>token</urn3:StreetName>
                <urn3:StreetNumber>token</urn3:StreetNumber>
                <urn3:Postcode>token</urn3:Postcode>
                <urn3:City>token</urn3:City>
              </urn3:DeliveryPlaceTrader>
              <urn3:DestinationOffice>
                <urn3:ReferenceNumber>GB005045</urn3:ReferenceNumber>
              </urn3:DestinationOffice>
              <urn3:ReportOfReceiptExport>
                <urn3:DateOfArrivalOfExciseProducts>2014-01-10</urn3:DateOfArrivalOfExciseProducts>
                <urn3:GlobalConclusionOfReceipt>22</urn3:GlobalConclusionOfReceipt>
                <urn3:ComplementaryInformation language="to">token</urn3:ComplementaryInformation>
              </urn3:ReportOfReceiptExport>
              <urn3:BodyReportOfReceiptExport>
                <urn3:BodyRecordUniqueReference>123</urn3:BodyRecordUniqueReference>
                <urn3:IndicatorOfShortageOrExcess>S</urn3:IndicatorOfShortageOrExcess>
                <urn3:ObservedShortageOrExcess>1000.0</urn3:ObservedShortageOrExcess>
                <urn3:ExciseProductCode>toke</urn3:ExciseProductCode>
                <urn3:RefusedQuantity>1000.0</urn3:RefusedQuantity>
                <urn3:UnsatisfactoryReason>
                  <urn3:UnsatisfactoryReasonCode>12</urn3:UnsatisfactoryReasonCode>
                  <urn3:ComplementaryInformation language="to">token</urn3:ComplementaryInformation>
                </urn3:UnsatisfactoryReason>
              </urn3:BodyReportOfReceiptExport>
            </urn3:AcceptedOrRejectedReportOfReceiptExport>
          </urn3:Body>
        </urn3:IE818>
        <urn4:IE801>
          <urn4:Header>
            <tms:MessageSender>NDEA.XI</tms:MessageSender>
            <tms:MessageRecipient>NDEA.AT</tms:MessageRecipient>
            <tms:DateOfPreparation>2023-06-22</tms:DateOfPreparation>
            <tms:TimeOfPreparation>12:37:08.755</tms:TimeOfPreparation>
            <tms:MessageIdentifier>XI000005</tms:MessageIdentifier>
          </urn4:Header>
          <urn4:Body>
            <urn4:EADESADContainer>
              <urn4:ConsigneeTrader language="en">
                <urn4:Traderid>AT00000602082</urn4:Traderid>
                <urn4:TraderName>AFOR KALE LTD</urn4:TraderName>
                <urn4:StreetName>The Street</urn4:StreetName>
                <urn4:Postcode>AT123</urn4:Postcode>
                <urn4:City>The City</urn4:City>
              </urn4:ConsigneeTrader>
              <urn4:ExciseMovement>
                <urn4:AdministrativeReferenceCode>23XI00000000000000016</urn4:AdministrativeReferenceCode>
                <urn4:DateAndTimeOfValidationOfEadEsad>2023-06-22T11:37:10.345739396</urn4:DateAndTimeOfValidationOfEadEsad>
              </urn4:ExciseMovement>
              <urn4:ConsignorTrader language="en">
                <urn4:TraderExciseNumber>GBWKQOZ8OVLYR</urn4:TraderExciseNumber>
                <urn4:TraderName>Clarkys Eagles</urn4:TraderName>
                <urn4:StreetName>Happy Street</urn4:StreetName>
                <urn4:Postcode>BT1 1BG</urn4:Postcode>
                <urn4:City>The City</urn4:City>
              </urn4:ConsignorTrader>
              <urn4:PlaceOfDispatchTrader language="en">
                <urn4:ReferenceOfTaxWarehouse>XI00000467014</urn4:ReferenceOfTaxWarehouse>
              </urn4:PlaceOfDispatchTrader>
              <urn4:DeliveryPlaceTrader language="en">
                <urn4:Traderid>AT00000602078</urn4:Traderid>
                <urn4:TraderName>METEST BOND STTSTGE</urn4:TraderName>
                <urn4:StreetName>WHITETEST ROAD METEST CITY ESTATE</urn4:StreetName>
                <urn4:Postcode>BN2 4KX</urn4:Postcode>
                <urn4:City>STTEST,KENT</urn4:City>
              </urn4:DeliveryPlaceTrader>
              <urn4:CompetentAuthorityDispatchOffice>
                <urn4:ReferenceNumber>GB004098</urn4:ReferenceNumber>
              </urn4:CompetentAuthorityDispatchOffice>
              <urn4:EadEsad>
                <urn4:LocalReferenceNumber>lrnie8151695045</urn4:LocalReferenceNumber>
                <urn4:InvoiceNumber>INVOICE001</urn4:InvoiceNumber>
                <urn4:InvoiceDate>2018-04-04</urn4:InvoiceDate>
                <urn4:OriginTypeCode>1</urn4:OriginTypeCode>
                <urn4:DateOfDispatch>2021-12-02</urn4:DateOfDispatch>
                <urn4:TimeOfDispatch>22:37:00</urn4:TimeOfDispatch>
              </urn4:EadEsad>
              <urn4:HeaderEadEsad>
                <urn4:SequenceNumber>1</urn4:SequenceNumber>
                <urn4:DateAndTimeOfUpdateValidation>2023-06-22T11:37:10.345801029</urn4:DateAndTimeOfUpdateValidation>
                <urn4:DestinationTypeCode>1</urn4:DestinationTypeCode>
                <urn4:JourneyTime>D01</urn4:JourneyTime>
                <urn4:TransportArrangement>1</urn4:TransportArrangement>
              </urn4:HeaderEadEsad>
              <urn4:TransportMode>
                <urn4:TransportModeCode>1</urn4:TransportModeCode>
              </urn4:TransportMode>
              <urn4:MovementGuarantee>
                <urn4:GuarantorTypeCode>1</urn4:GuarantorTypeCode>
              </urn4:MovementGuarantee>
              <urn4:BodyEadEsad>
                <urn4:BodyRecordUniqueReference>1</urn4:BodyRecordUniqueReference>
                <urn4:ExciseProductCode>E410</urn4:ExciseProductCode>
                <urn4:CnCode>27101231</urn4:CnCode>
                <urn4:Quantity>100.000</urn4:Quantity>
                <urn4:GrossMass>100.00</urn4:GrossMass>
                <urn4:NetMass>90.00</urn4:NetMass>
                <urn4:Density>10.00</urn4:Density>
                <urn4:Package>
                  <urn4:KindOfPackages>BH</urn4:KindOfPackages>
                  <urn4:NumberOfPackages>2</urn4:NumberOfPackages>
                  <urn4:ShippingMarks>Subhasis Swain1</urn4:ShippingMarks>
                </urn4:Package>
                <urn4:Package>
                  <urn4:KindOfPackages>BH</urn4:KindOfPackages>
                  <urn4:NumberOfPackages>2</urn4:NumberOfPackages>
                  <urn4:ShippingMarks>Subhasis Swain 2</urn4:ShippingMarks>
                </urn4:Package>
              </urn4:BodyEadEsad>
              <urn4:TransportDetails>
                <urn4:TransportUnitCode>1</urn4:TransportUnitCode>
                <urn4:IdentityOfTransportUnits>Transformers robots in disguise</urn4:IdentityOfTransportUnits>
              </urn4:TransportDetails>
              <urn4:TransportDetails>
                <urn4:TransportUnitCode>2</urn4:TransportUnitCode>
                <urn4:IdentityOfTransportUnits>MACHINES</urn4:IdentityOfTransportUnits>
              </urn4:TransportDetails>
              <urn4:TransportDetails>
                <urn4:TransportUnitCode>3</urn4:TransportUnitCode>
                <urn4:IdentityOfTransportUnits>MORE MACHINES</urn4:IdentityOfTransportUnits>
              </urn4:TransportDetails>
            </urn4:EADESADContainer>
          </urn4:Body>
        </urn4:IE801>
        <urn5:IE818>
          <urn5:Header>
            <urn:MessageSender>NDEA.XI</urn:MessageSender>
            <urn:MessageRecipient>NDEA.XI</urn:MessageRecipient>
            <urn:DateOfPreparation>2006-08-04</urn:DateOfPreparation>
            <urn:TimeOfPreparation>09:43:40</urn:TimeOfPreparation>
            <urn:MessageIdentifier>XI0000016</urn:MessageIdentifier>
            <urn:CorrelationIdentifier>8a4127de904f46caa779702381c4bd</urn:CorrelationIdentifier>
          </urn5:Header>
          <urn5:Body>
            <urn5:AcceptedOrRejectedReportOfReceiptExport>
              <urn5:Attributes>
                <urn5:DateAndTimeOfValidationOfReportOfReceiptExport>2001-01-03T11:25:01</urn5:DateAndTimeOfValidationOfReportOfReceiptExport>
              </urn5:Attributes>
              <urn5:ConsigneeTrader language="to">
                <urn5:Traderid>GBWK002901025</urn5:Traderid>
                <urn5:TraderName>token</urn5:TraderName>
                <urn5:StreetName>token</urn5:StreetName>
                <urn5:StreetNumber>token</urn5:StreetNumber>
                <urn5:Postcode>token</urn5:Postcode>
                <urn5:City>token</urn5:City>
                <urn5:EoriNumber>token</urn5:EoriNumber>
              </urn5:ConsigneeTrader>
              <urn5:ExciseMovement>
                <urn5:AdministrativeReferenceCode>23XI00000000000000016</urn5:AdministrativeReferenceCode>
                <urn5:SequenceNumber>1</urn5:SequenceNumber>
              </urn5:ExciseMovement>
              <urn5:DeliveryPlaceTrader language="to">
                <urn5:Traderid>token</urn5:Traderid>
                <urn5:TraderName>token</urn5:TraderName>
                <urn5:StreetName>token</urn5:StreetName>
                <urn5:StreetNumber>token</urn5:StreetNumber>
                <urn5:Postcode>token</urn5:Postcode>
                <urn5:City>token</urn5:City>
              </urn5:DeliveryPlaceTrader>
              <urn5:DestinationOffice>
                <urn5:ReferenceNumber>GB005045</urn5:ReferenceNumber>
              </urn5:DestinationOffice>
              <urn5:ReportOfReceiptExport>
                <urn3:DateOfArrivalOfExciseProducts>2014-01-10</urn3:DateOfArrivalOfExciseProducts>
                <urn3:GlobalConclusionOfReceipt>22</urn3:GlobalConclusionOfReceipt>
                <urn3:ComplementaryInformation language="to">token</urn3:ComplementaryInformation>
              </urn5:ReportOfReceiptExport>
              <urn5:BodyReportOfReceiptExport>
                <urn5:BodyRecordUniqueReference>123</urn5:BodyRecordUniqueReference>
                <urn5:IndicatorOfShortageOrExcess>S</urn5:IndicatorOfShortageOrExcess>
                <urn5:ObservedShortageOrExcess>1000.0</urn5:ObservedShortageOrExcess>
                <urn5:ExciseProductCode>toke</urn5:ExciseProductCode>
                <urn5:RefusedQuantity>1000.0</urn5:RefusedQuantity>
                <urn5:UnsatisfactoryReason>
                  <urn5:UnsatisfactoryReasonCode>12</urn5:UnsatisfactoryReasonCode>
                  <urn5:ComplementaryInformation language="to">token</urn5:ComplementaryInformation>
                </urn5:UnsatisfactoryReason>
              </urn5:BodyReportOfReceiptExport>
            </urn5:AcceptedOrRejectedReportOfReceiptExport>
          </urn5:Body>
        </urn5:IE818>
        <urn6:IE871>
          <urn6:Header>
            <urn:MessageSender>NDEA.XI</urn:MessageSender>
            <urn:MessageRecipient>NDEA.XI</urn:MessageRecipient>
            <urn:DateOfPreparation>2015-08-06</urn:DateOfPreparation>
            <urn:TimeOfPreparation>03:44:00</urn:TimeOfPreparation>
            <urn:MessageIdentifier>XI0000017</urn:MessageIdentifier>
            <urn:CorrelationIdentifier>054f764f07d24664b0d2351dfd19d09d</urn:CorrelationIdentifier>
          </urn6:Header>
          <urn6:Body>
            <urn6:ExplanationOnReasonForShortage>
              <urn6:Attributes>
                <urn6:SubmitterType>2</urn6:SubmitterType>
                <urn6:DateAndTimeOfValidationOfExplanationOnShortage>2006-12-27T09:49:58</urn6:DateAndTimeOfValidationOfExplanationOnShortage>
              </urn6:Attributes>
              <urn6:ConsigneeTrader language="to">
                <urn6:Traderid>GBWK002281034</urn6:Traderid>
                <urn6:TraderName>token</urn6:TraderName>
                <urn6:StreetName>token</urn6:StreetName>
                <urn6:StreetNumber>token</urn6:StreetNumber>
                <urn6:Postcode>token</urn6:Postcode>
                <urn6:City>token</urn6:City>
                <urn6:EoriNumber>token</urn6:EoriNumber>
              </urn6:ConsigneeTrader>
              <urn6:ExciseMovement>
                <urn6:AdministrativeReferenceCode>23XI00000000000000016</urn6:AdministrativeReferenceCode>
                <urn6:SequenceNumber>12</urn6:SequenceNumber>
              </urn6:ExciseMovement>
              <urn6:ConsignorTrader language="to">
                <urn6:TraderExciseNumber>GBWKQOZ8OVLYR</urn6:TraderExciseNumber>
                <urn6:TraderName>token</urn6:TraderName>
                <urn6:StreetName>token</urn6:StreetName>
                <urn6:StreetNumber>token</urn6:StreetNumber>
                <urn6:Postcode>token</urn6:Postcode>
                <urn6:City>token</urn6:City>
              </urn6:ConsignorTrader>
              <urn6:Analysis>
                <urn6:DateOfAnalysis>2002-02-01</urn6:DateOfAnalysis>
                <urn6:GlobalExplanation language="to">token</urn6:GlobalExplanation>
              </urn6:Analysis>
              <urn6:BodyAnalysis>
                <urn6:ExciseProductCode>toke</urn6:ExciseProductCode>
                <urn6:BodyRecordUniqueReference>45</urn6:BodyRecordUniqueReference>
                <urn6:Explanation language="to">token</urn6:Explanation>
                <urn6:ActualQuantity>1000.0</urn6:ActualQuantity>
              </urn6:BodyAnalysis>
            </urn6:ExplanationOnReasonForShortage>
          </urn6:Body>
        </urn6:IE871>
        <urn7:IE801>
          <urn7:Header>
            <tms:MessageSender>NDEA.XI</tms:MessageSender>
            <tms:MessageRecipient>NDEA.AT</tms:MessageRecipient>
            <tms:DateOfPreparation>2023-06-22</tms:DateOfPreparation>
            <tms:TimeOfPreparation>12:37:08.755</tms:TimeOfPreparation>
            <tms:MessageIdentifier>XI000006</tms:MessageIdentifier>
          </urn7:Header>
          <urn7:Body>
            <urn7:EADESADContainer>
              <urn7:ConsigneeTrader language="en">
                <urn7:Traderid>AT00000602082</urn7:Traderid>
                <urn7:TraderName>AFOR KALE LTD</urn7:TraderName>
                <urn7:StreetName>The Street</urn7:StreetName>
                <urn7:Postcode>AT123</urn7:Postcode>
                <urn7:City>The City</urn7:City>
              </urn7:ConsigneeTrader>
              <urn7:ExciseMovement>
                <urn7:AdministrativeReferenceCode>23XI00000000000000017</urn7:AdministrativeReferenceCode>
                <urn7:DateAndTimeOfValidationOfEadEsad>2023-06-22T11:37:10.345739396</urn7:DateAndTimeOfValidationOfEadEsad>
              </urn7:ExciseMovement>
              <urn7:ConsignorTrader language="en">
                <urn7:TraderExciseNumber>GBWKQOZ8OVLYR</urn7:TraderExciseNumber>
                <urn7:TraderName>Clarkys Eagles</urn7:TraderName>
                <urn7:StreetName>Happy Street</urn7:StreetName>
                <urn7:Postcode>BT1 1BG</urn7:Postcode>
                <urn7:City>The City</urn7:City>
              </urn7:ConsignorTrader>
              <urn7:PlaceOfDispatchTrader language="en">
                <urn7:ReferenceOfTaxWarehouse>XI00000467014</urn7:ReferenceOfTaxWarehouse>
              </urn7:PlaceOfDispatchTrader>
              <urn7:DeliveryPlaceTrader language="en">
                <urn7:Traderid>AT00000602078</urn7:Traderid>
                <urn7:TraderName>METEST BOND STTSTGE</urn7:TraderName>
                <urn7:StreetName>WHITETEST ROAD METEST CITY ESTATE</urn7:StreetName>
                <urn7:Postcode>BN2 4KX</urn7:Postcode>
                <urn7:City>STTEST,KENT</urn7:City>
              </urn7:DeliveryPlaceTrader>
              <urn7:CompetentAuthorityDispatchOffice>
                <urn7:ReferenceNumber>GB004098</urn7:ReferenceNumber>
              </urn7:CompetentAuthorityDispatchOffice>
              <urn7:EadEsad>
                <urn7:LocalReferenceNumber>lrnie8155743934</urn7:LocalReferenceNumber>
                <urn7:InvoiceNumber>INVOICE001</urn7:InvoiceNumber>
                <urn7:InvoiceDate>2018-04-04</urn7:InvoiceDate>
                <urn7:OriginTypeCode>1</urn7:OriginTypeCode>
                <urn7:DateOfDispatch>2021-12-02</urn7:DateOfDispatch>
                <urn7:TimeOfDispatch>22:37:00</urn7:TimeOfDispatch>
              </urn7:EadEsad>
              <urn7:HeaderEadEsad>
                <urn7:SequenceNumber>1</urn7:SequenceNumber>
                <urn7:DateAndTimeOfUpdateValidation>2023-06-22T11:37:10.345801029</urn7:DateAndTimeOfUpdateValidation>
                <urn7:DestinationTypeCode>1</urn7:DestinationTypeCode>
                <urn7:JourneyTime>D01</urn7:JourneyTime>
                <urn7:TransportArrangement>1</urn7:TransportArrangement>
              </urn7:HeaderEadEsad>
              <urn7:TransportMode>
                <urn7:TransportModeCode>1</urn7:TransportModeCode>
              </urn7:TransportMode>
              <urn7:MovementGuarantee>
                <urn7:GuarantorTypeCode>1</urn7:GuarantorTypeCode>
              </urn7:MovementGuarantee>
              <urn7:BodyEadEsad>
                <urn7:BodyRecordUniqueReference>1</urn7:BodyRecordUniqueReference>
                <urn7:ExciseProductCode>E410</urn7:ExciseProductCode>
                <urn7:CnCode>27101231</urn7:CnCode>
                <urn7:Quantity>100.000</urn7:Quantity>
                <urn7:GrossMass>100.00</urn7:GrossMass>
                <urn7:NetMass>90.00</urn7:NetMass>
                <urn7:Density>10.00</urn7:Density>
                <urn7:Package>
                  <urn7:KindOfPackages>BH</urn7:KindOfPackages>
                  <urn7:NumberOfPackages>2</urn7:NumberOfPackages>
                  <urn7:ShippingMarks>Subhasis Swain1</urn7:ShippingMarks>
                </urn7:Package>
                <urn7:Package>
                  <urn7:KindOfPackages>BH</urn7:KindOfPackages>
                  <urn7:NumberOfPackages>2</urn7:NumberOfPackages>
                  <urn7:ShippingMarks>Subhasis Swain 2</urn7:ShippingMarks>
                </urn7:Package>
              </urn7:BodyEadEsad>
              <urn7:TransportDetails>
                <urn7:TransportUnitCode>1</urn7:TransportUnitCode>
                <urn7:IdentityOfTransportUnits>Transformers robots in disguise</urn7:IdentityOfTransportUnits>
              </urn7:TransportDetails>
              <urn7:TransportDetails>
                <urn7:TransportUnitCode>2</urn7:TransportUnitCode>
                <urn7:IdentityOfTransportUnits>MACHINES</urn7:IdentityOfTransportUnits>
              </urn7:TransportDetails>
              <urn7:TransportDetails>
                <urn7:TransportUnitCode>3</urn7:TransportUnitCode>
                <urn7:IdentityOfTransportUnits>MORE MACHINES</urn7:IdentityOfTransportUnits>
              </urn7:TransportDetails>
            </urn7:EADESADContainer>
          </urn7:Body>
        </urn7:IE801>
        <urn8:IE810>
          <urn8:Header>
            <urn:MessageSender>NDEA.XI</urn:MessageSender>
            <urn:MessageRecipient>NDEA.XI</urn:MessageRecipient>
            <urn:DateOfPreparation>2008-09-29</urn:DateOfPreparation>
            <urn:TimeOfPreparation>00:18:33</urn:TimeOfPreparation>
            <urn:MessageIdentifier>XI0000018</urn:MessageIdentifier>
            <urn:CorrelationIdentifier>a4dd7694ccef4055abb1dfef7ff06f49</urn:CorrelationIdentifier>
          </urn8:Header>
          <urn8:Body>
            <urn8:CancellationOfEAD>
              <urn8:Attributes>
                <urn8:DateAndTimeOfValidationOfCancellation>2006-08-19T18:27:14</urn8:DateAndTimeOfValidationOfCancellation>
              </urn8:Attributes>
              <urn8:ExciseMovementEad>
                <urn8:AdministrativeReferenceCode>23XI00000000000000017</urn8:AdministrativeReferenceCode>
              </urn8:ExciseMovementEad>
              <urn8:Cancellation>
                <urn8:CancellationReasonCode>1</urn8:CancellationReasonCode>
                <urn8:ComplementaryInformation language="to">token</urn8:ComplementaryInformation>
              </urn8:Cancellation>
            </urn8:CancellationOfEAD>
          </urn8:Body>
        </urn8:IE810>
        <urn9:IE801>
          <urn9:Header>
            <tms:MessageSender>NDEA.XI</tms:MessageSender>
            <tms:MessageRecipient>NDEA.AT</tms:MessageRecipient>
            <tms:DateOfPreparation>2023-06-22</tms:DateOfPreparation>
            <tms:TimeOfPreparation>12:37:08.755</tms:TimeOfPreparation>
            <tms:MessageIdentifier>XI000003</tms:MessageIdentifier>
          </urn9:Header>
          <urn9:Body>
            <urn9:EADESADContainer>
              <urn9:ConsigneeTrader language="en">
                <urn9:Traderid>AT00000602078</urn9:Traderid>
                <urn9:TraderName>AFOR KALE LTD</urn9:TraderName>
                <urn9:StreetName>The Street</urn9:StreetName>
                <urn9:Postcode>AT123</urn9:Postcode>
                <urn9:City>The City</urn9:City>
              </urn9:ConsigneeTrader>
              <urn9:ExciseMovement>
                <urn9:AdministrativeReferenceCode>23XI00000000000000021</urn9:AdministrativeReferenceCode>
                <urn9:DateAndTimeOfValidationOfEadEsad>2023-06-22T11:37:10.345739396</urn9:DateAndTimeOfValidationOfEadEsad>
              </urn9:ExciseMovement>
              <urn9:ConsignorTrader language="en">
                <urn9:TraderExciseNumber>GBWKQOZ8OVLYR</urn9:TraderExciseNumber>
                <urn9:TraderName>Clarkys Eagles</urn9:TraderName>
                <urn9:StreetName>Happy Street</urn9:StreetName>
                <urn9:Postcode>BT1 1BG</urn9:Postcode>
                <urn9:City>The City</urn9:City>
              </urn9:ConsignorTrader>
              <urn9:PlaceOfDispatchTrader language="en">
                <urn9:ReferenceOfTaxWarehouse>XI00000467014</urn9:ReferenceOfTaxWarehouse>
              </urn9:PlaceOfDispatchTrader>
              <urn9:DeliveryPlaceTrader language="en">
                <urn9:Traderid>AT00000602078</urn9:Traderid>
                <urn9:TraderName>METEST BOND STTSTGE</urn9:TraderName>
                <urn9:StreetName>WHITETEST ROAD METEST CITY ESTATE</urn9:StreetName>
                <urn9:Postcode>BN2 4KX</urn9:Postcode>
                <urn9:City>STTEST,KENT</urn9:City>
              </urn9:DeliveryPlaceTrader>
              <urn9:CompetentAuthorityDispatchOffice>
                <urn9:ReferenceNumber>GB004098</urn9:ReferenceNumber>
              </urn9:CompetentAuthorityDispatchOffice>
              <urn9:EadEsad>
                <urn9:LocalReferenceNumber>lrnie8154968571</urn9:LocalReferenceNumber>
                <urn9:InvoiceNumber>INVOICE001</urn9:InvoiceNumber>
                <urn9:InvoiceDate>2018-04-04</urn9:InvoiceDate>
                <urn9:OriginTypeCode>1</urn9:OriginTypeCode>
                <urn9:DateOfDispatch>2021-12-02</urn9:DateOfDispatch>
                <urn9:TimeOfDispatch>22:37:00</urn9:TimeOfDispatch>
              </urn9:EadEsad>
              <urn9:HeaderEadEsad>
                <urn9:SequenceNumber>1</urn9:SequenceNumber>
                <urn9:DateAndTimeOfUpdateValidation>2023-06-22T11:37:10.345801029</urn9:DateAndTimeOfUpdateValidation>
                <urn9:DestinationTypeCode>1</urn9:DestinationTypeCode>
                <urn9:JourneyTime>D01</urn9:JourneyTime>
                <urn9:TransportArrangement>1</urn9:TransportArrangement>
              </urn9:HeaderEadEsad>
              <urn9:TransportMode>
                <urn9:TransportModeCode>1</urn9:TransportModeCode>
              </urn9:TransportMode>
              <urn9:MovementGuarantee>
                <urn9:GuarantorTypeCode>1</urn9:GuarantorTypeCode>
              </urn9:MovementGuarantee>
              <urn9:BodyEadEsad>
                <urn9:BodyRecordUniqueReference>1</urn9:BodyRecordUniqueReference>
                <urn9:ExciseProductCode>E410</urn9:ExciseProductCode>
                <urn9:CnCode>27101231</urn9:CnCode>
                <urn9:Quantity>100.000</urn9:Quantity>
                <urn9:GrossMass>100.00</urn9:GrossMass>
                <urn9:NetMass>90.00</urn9:NetMass>
                <urn9:Density>10.00</urn9:Density>
                <urn9:Package>
                  <urn9:KindOfPackages>BH</urn9:KindOfPackages>
                  <urn9:NumberOfPackages>2</urn9:NumberOfPackages>
                  <urn9:ShippingMarks>Subhasis Swain1</urn9:ShippingMarks>
                </urn9:Package>
                <urn9:Package>
                  <urn9:KindOfPackages>BH</urn9:KindOfPackages>
                  <urn9:NumberOfPackages>2</urn9:NumberOfPackages>
                  <urn9:ShippingMarks>Subhasis Swain 2</urn9:ShippingMarks>
                </urn9:Package>
              </urn9:BodyEadEsad>
              <urn9:TransportDetails>
                <urn9:TransportUnitCode>1</urn9:TransportUnitCode>
                <urn9:IdentityOfTransportUnits>Transformers robots in disguise</urn9:IdentityOfTransportUnits>
              </urn9:TransportDetails>
              <urn9:TransportDetails>
                <urn9:TransportUnitCode>2</urn9:TransportUnitCode>
                <urn9:IdentityOfTransportUnits>MACHINES</urn9:IdentityOfTransportUnits>
              </urn9:TransportDetails>
              <urn9:TransportDetails>
                <urn9:TransportUnitCode>3</urn9:TransportUnitCode>
                <urn9:IdentityOfTransportUnits>MORE MACHINES</urn9:IdentityOfTransportUnits>
              </urn9:TransportDetails>
            </urn9:EADESADContainer>
          </urn9:Body>
        </urn9:IE801>
      </ns:Messages>
      <ns:CountOfMessagesAvailable>39</ns:CountOfMessagesAvailable>
    </ns:NewMessagesDataResponse>
  }
}

