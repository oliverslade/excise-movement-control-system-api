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

package uk.gov.hmrc.excisemovementcontrolsystemapi.data

object SchedulingTestData {

  lazy val ie704 =
    <IE704
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
      xmlns:ns1="http://www.govtalk.gov.uk/taxation/InternationalTrade/Excise/ie704uk/3">
      <ns1:Header>
        <urn:MessageSender>token</urn:MessageSender>
        <urn:MessageRecipient>token</urn:MessageRecipient>
        <urn:DateOfPreparation>2008-09-29</urn:DateOfPreparation>
        <urn:TimeOfPreparation>00:18:33</urn:TimeOfPreparation>
        <urn:MessageIdentifier>messageId-4</urn:MessageIdentifier>
        <urn:CorrelationIdentifier>token</urn:CorrelationIdentifier>
      </ns1:Header>
      <ns1:Body>
        <ns1:GenericRefusalMessage>
          <ns1:Attributes>
            <ns1:AdministrativeReferenceCode>tokentokentokentokent</ns1:AdministrativeReferenceCode>
            <ns1:SequenceNumber>to</ns1:SequenceNumber>
            <ns1:LocalReferenceNumber>token</ns1:LocalReferenceNumber>
          </ns1:Attributes>
          <ns1:FunctionalError>
            <ns1:ErrorType>4518</ns1:ErrorType>
            <ns1:ErrorReason>token</ns1:ErrorReason>
            <ns1:ErrorLocation>token</ns1:ErrorLocation>
            <ns1:OriginalAttributeValue>token</ns1:OriginalAttributeValue>
          </ns1:FunctionalError>
        </ns1:GenericRefusalMessage>
      </ns1:Body>
    </IE704>

  lazy val ie801 =
  <IE801
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE801:V3.13">
    <urn1:Header>
      <urn:MessageSender>token</urn:MessageSender>
      <urn:MessageRecipient>token</urn:MessageRecipient>
      <urn:DateOfPreparation>2018-11-01</urn:DateOfPreparation>
      <urn:TimeOfPreparation>02:02:49+01:00</urn:TimeOfPreparation>
      <urn:MessageIdentifier>messageId-1</urn:MessageIdentifier>
      <urn:CorrelationIdentifier>token</urn:CorrelationIdentifier>
    </urn1:Header>
    <urn1:Body>
      <urn1:EADESADContainer>
        <urn1:ConsigneeTrader language="to">
          <urn1:Traderid>token</urn1:Traderid>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
          <urn1:EoriNumber>token</urn1:EoriNumber>
        </urn1:ConsigneeTrader>
        <urn1:ExciseMovement>
          <urn1:AdministrativeReferenceCode>tokentokentokentokent</urn1:AdministrativeReferenceCode>
          <urn1:DateAndTimeOfValidationOfEadEsad>2002-11-05T08:01:03</urn1:DateAndTimeOfValidationOfEadEsad>
        </urn1:ExciseMovement>
        <urn1:ConsignorTrader language="to">
          <urn1:TraderExciseNumber>tokentokentok</urn1:TraderExciseNumber>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:ConsignorTrader>
        <urn1:PlaceOfDispatchTrader language="to">
          <urn1:ReferenceOfTaxWarehouse>tokentokentok</urn1:ReferenceOfTaxWarehouse>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:PlaceOfDispatchTrader>
        <urn1:DispatchImportOffice>
          <urn1:ReferenceNumber>tokentok</urn1:ReferenceNumber>
        </urn1:DispatchImportOffice>
        <urn1:ComplementConsigneeTrader>
          <urn1:MemberStateCode>to</urn1:MemberStateCode>
          <urn1:SerialNumberOfCertificateOfExemption>token</urn1:SerialNumberOfCertificateOfExemption>
        </urn1:ComplementConsigneeTrader>
        <urn1:DeliveryPlaceTrader language="to">
          <urn1:Traderid>token</urn1:Traderid>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:DeliveryPlaceTrader>
        <urn1:DeliveryPlaceCustomsOffice>
          <urn1:ReferenceNumber>tokentok</urn1:ReferenceNumber>
        </urn1:DeliveryPlaceCustomsOffice>
        <urn1:CompetentAuthorityDispatchOffice>
          <urn1:ReferenceNumber>tokentok</urn1:ReferenceNumber>
        </urn1:CompetentAuthorityDispatchOffice>
        <urn1:TransportArrangerTrader language="to">
          <urn1:VatNumber>token</urn1:VatNumber>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:TransportArrangerTrader>
        <urn1:FirstTransporterTrader language="to">
          <urn1:VatNumber>token</urn1:VatNumber>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:FirstTransporterTrader>
        <urn1:DocumentCertificate>
          <urn1:DocumentType>toke</urn1:DocumentType>
          <urn1:DocumentReference>token</urn1:DocumentReference>
          <urn1:DocumentDescription language="to">token</urn1:DocumentDescription>
          <urn1:ReferenceOfDocument language="to">token</urn1:ReferenceOfDocument>
        </urn1:DocumentCertificate>
        <urn1:EadEsad>
          <urn1:LocalReferenceNumber>token</urn1:LocalReferenceNumber>
          <urn1:InvoiceNumber>token</urn1:InvoiceNumber>
          <urn1:InvoiceDate>2002-06-24+01:00</urn1:InvoiceDate>
          <urn1:OriginTypeCode>3</urn1:OriginTypeCode>
          <urn1:DateOfDispatch>2012-01-07</urn1:DateOfDispatch>
          <urn1:TimeOfDispatch>09:44:59</urn1:TimeOfDispatch>
          <urn1:UpstreamArc>tokentokentokentokent</urn1:UpstreamArc>
          <urn1:ImportSad>
            <urn1:ImportSadNumber>token</urn1:ImportSadNumber>
          </urn1:ImportSad>
        </urn1:EadEsad>
        <urn1:HeaderEadEsad>
          <urn1:SequenceNumber>to</urn1:SequenceNumber>
          <urn1:DateAndTimeOfUpdateValidation>2013-06-17T18:14:58</urn1:DateAndTimeOfUpdateValidation>
          <urn1:DestinationTypeCode>10</urn1:DestinationTypeCode>
          <urn1:JourneyTime>tok</urn1:JourneyTime>
          <urn1:TransportArrangement>4</urn1:TransportArrangement>
        </urn1:HeaderEadEsad>
        <urn1:TransportMode>
          <urn1:TransportModeCode>to</urn1:TransportModeCode>
          <urn1:ComplementaryInformation language="to">token</urn1:ComplementaryInformation>
        </urn1:TransportMode>
        <urn1:MovementGuarantee>
          <urn1:GuarantorTypeCode>12</urn1:GuarantorTypeCode>
          <urn1:GuarantorTrader language="to">
            <urn1:TraderExciseNumber>tokentokentok</urn1:TraderExciseNumber>
            <urn1:TraderName>token</urn1:TraderName>
            <urn1:StreetName>token</urn1:StreetName>
            <urn1:StreetNumber>token</urn1:StreetNumber>
            <urn1:City>token</urn1:City>
            <urn1:Postcode>token</urn1:Postcode>
            <urn1:VatNumber>token</urn1:VatNumber>
          </urn1:GuarantorTrader>
        </urn1:MovementGuarantee>
        <urn1:BodyEadEsad>
          <urn1:BodyRecordUniqueReference>tok</urn1:BodyRecordUniqueReference>
          <urn1:ExciseProductCode>toke</urn1:ExciseProductCode>
          <urn1:CnCode>tokentok</urn1:CnCode>
          <urn1:Quantity>1000.00000000000</urn1:Quantity>
          <urn1:GrossMass>1000.000000000000</urn1:GrossMass>
          <urn1:NetMass>1000.000000000000</urn1:NetMass>
          <urn1:AlcoholicStrengthByVolumeInPercentage>1000.00</urn1:AlcoholicStrengthByVolumeInPercentage>
          <urn1:DegreePlato>1000.00</urn1:DegreePlato>
          <urn1:FiscalMark language="to">token</urn1:FiscalMark>
          <urn1:FiscalMarkUsedFlag>1</urn1:FiscalMarkUsedFlag>
          <urn1:DesignationOfOrigin language="to">token</urn1:DesignationOfOrigin>
          <urn1:SizeOfProducer>token</urn1:SizeOfProducer>
          <urn1:Density>1000.00</urn1:Density>
          <urn1:CommercialDescription language="to">token</urn1:CommercialDescription>
          <urn1:BrandNameOfProducts language="to">token</urn1:BrandNameOfProducts>
          <urn1:MaturationPeriodOrAgeOfProducts>token</urn1:MaturationPeriodOrAgeOfProducts>
          <urn1:Package>
            <urn1:KindOfPackages>to</urn1:KindOfPackages>
            <urn1:NumberOfPackages>token</urn1:NumberOfPackages>
            <urn1:ShippingMarks>token</urn1:ShippingMarks>
            <urn1:CommercialSealIdentification>token</urn1:CommercialSealIdentification>
            <urn1:SealInformation language="to">token</urn1:SealInformation>
          </urn1:Package>
          <urn1:WineProduct>
            <urn1:WineProductCategory>2</urn1:WineProductCategory>
            <urn1:WineGrowingZoneCode>to</urn1:WineGrowingZoneCode>
            <urn1:ThirdCountryOfOrigin>to</urn1:ThirdCountryOfOrigin>
            <urn1:OtherInformation language="to">token</urn1:OtherInformation>
            <urn1:WineOperation>
              <urn1:WineOperationCode>to</urn1:WineOperationCode>
            </urn1:WineOperation>
          </urn1:WineProduct>
        </urn1:BodyEadEsad>
        <urn1:TransportDetails>
          <urn1:TransportUnitCode>to</urn1:TransportUnitCode>
          <urn1:IdentityOfTransportUnits>token</urn1:IdentityOfTransportUnits>
          <urn1:CommercialSealIdentification>token</urn1:CommercialSealIdentification>
          <urn1:ComplementaryInformation language="to">token</urn1:ComplementaryInformation>
          <urn1:SealInformation language="to">token</urn1:SealInformation>
        </urn1:TransportDetails>
      </urn1:EADESADContainer>
    </urn1:Body>
  </IE801>

  lazy val ie818 =
    <IE818
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.13">
    <urn1:Header>
      <urn:MessageSender>token</urn:MessageSender>
      <urn:MessageRecipient>token</urn:MessageRecipient>
      <urn:DateOfPreparation>2008-09-29</urn:DateOfPreparation>
      <urn:TimeOfPreparation>00:18:33</urn:TimeOfPreparation>
      <urn:MessageIdentifier>messageId-2</urn:MessageIdentifier>
      <urn:CorrelationIdentifier>token</urn:CorrelationIdentifier>
    </urn1:Header>
    <urn1:Body>
      <urn1:AcceptedOrRejectedReportOfReceiptExport>
        <urn1:Attributes>
          <urn1:DateAndTimeOfValidationOfReportOfReceiptExport>2006-08-19T18:27:14+01:00</urn1:DateAndTimeOfValidationOfReportOfReceiptExport>
        </urn1:Attributes>
        <urn1:ConsigneeTrader language="to">
          <urn1:Traderid>token</urn1:Traderid>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
          <urn1:EoriNumber>token</urn1:EoriNumber>
        </urn1:ConsigneeTrader>
        <urn1:ExciseMovement>
          <urn1:AdministrativeReferenceCode>tokentokentokentokent</urn1:AdministrativeReferenceCode>
          <urn1:SequenceNumber>to</urn1:SequenceNumber>
        </urn1:ExciseMovement>
        <urn1:DeliveryPlaceTrader language="to">
          <urn1:Traderid>token</urn1:Traderid>
          <urn1:TraderName>token</urn1:TraderName>
          <urn1:StreetName>token</urn1:StreetName>
          <urn1:StreetNumber>token</urn1:StreetNumber>
          <urn1:Postcode>token</urn1:Postcode>
          <urn1:City>token</urn1:City>
        </urn1:DeliveryPlaceTrader>
        <urn1:DestinationOffice>
          <urn1:ReferenceNumber>tokentok</urn1:ReferenceNumber>
        </urn1:DestinationOffice>
        <urn1:ReportOfReceiptExport>
          <urn1:DateOfArrivalOfExciseProducts>2009-05-16</urn1:DateOfArrivalOfExciseProducts>
          <urn1:GlobalConclusionOfReceipt>3</urn1:GlobalConclusionOfReceipt>
          <urn1:ComplementaryInformation language="to">token</urn1:ComplementaryInformation>
        </urn1:ReportOfReceiptExport>
        <urn1:BodyReportOfReceiptExport>
          <urn1:BodyRecordUniqueReference>tok</urn1:BodyRecordUniqueReference>
          <urn1:IndicatorOfShortageOrExcess>E</urn1:IndicatorOfShortageOrExcess>
          <urn1:ObservedShortageOrExcess>1000.00000000000</urn1:ObservedShortageOrExcess>
          <urn1:ExciseProductCode>toke</urn1:ExciseProductCode>
          <urn1:RefusedQuantity>1000.00000000000</urn1:RefusedQuantity>
          <urn1:UnsatisfactoryReason>
            <urn1:UnsatisfactoryReasonCode>to</urn1:UnsatisfactoryReasonCode>
            <urn1:ComplementaryInformation language="to">token</urn1:ComplementaryInformation>
          </urn1:UnsatisfactoryReason>
        </urn1:BodyReportOfReceiptExport>
      </urn1:AcceptedOrRejectedReportOfReceiptExport>
    </urn1:Body>
  </IE818>

  lazy val ie802 =
    <IE802
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.13"
    xmlns:urn2="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE802:V3.13">
    <urn2:Header>
      <urn:MessageSender>token</urn:MessageSender>
      <urn:MessageRecipient>token</urn:MessageRecipient>
      <urn:DateOfPreparation>2015-08-24</urn:DateOfPreparation>
      <urn:TimeOfPreparation>23:07:00+01:00</urn:TimeOfPreparation>
      <urn:MessageIdentifier>messageId-3</urn:MessageIdentifier>
      <urn:CorrelationIdentifier>token</urn:CorrelationIdentifier>
    </urn2:Header>
    <urn2:Body>
      <urn2:ReminderMessageForExciseMovement>
        <urn2:Attributes>
          <urn2:DateAndTimeOfIssuanceOfReminder>2000-04-21T01:36:55+01:00</urn2:DateAndTimeOfIssuanceOfReminder>
          <urn2:ReminderInformation language="to">token</urn2:ReminderInformation>
          <urn2:LimitDateAndTime>2017-04-19T15:38:57+01:00</urn2:LimitDateAndTime>
          <urn2:ReminderMessageType>1</urn2:ReminderMessageType>
        </urn2:Attributes>
        <urn2:ExciseMovement>
          <urn2:AdministrativeReferenceCode>tokentokentokentokent</urn2:AdministrativeReferenceCode>
          <urn2:SequenceNumber>to</urn2:SequenceNumber>
        </urn2:ExciseMovement>
      </urn2:ReminderMessageForExciseMovement>
    </urn2:Body>
  </IE802>
}
