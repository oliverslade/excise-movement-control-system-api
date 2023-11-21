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

import scala.xml.Elem

trait TestXml {

  lazy val IE802 = <urn:IE802
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE802:V3.01"
      xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
      xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
      xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>CSMISE.EC</urn1:MessageSender>
      <urn1:MessageRecipient>CSMISE.EC</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2008-09-29</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>00:18:33</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>98ad594a-78a0-4bbf-b524-33e927fe844f</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>07760ea5-0b47-4acb-9c53-516c70a6f0b6</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:ReminderMessageForExciseMovement>
        <urn:Attributes>
          <urn:DateAndTimeOfIssuanceOfReminder>2006-08-19T18:27:14</urn:DateAndTimeOfIssuanceOfReminder>
          <!--Optional:-->
          <urn:ReminderInformation language="to">token</urn:ReminderInformation>
          <urn:LimitDateAndTime>2009-05-16T13:42:28</urn:LimitDateAndTime>
          <urn:ReminderMessageType>2</urn:ReminderMessageType>
        </urn:Attributes>
        <urn:ExciseMovement>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>10</urn:SequenceNumber>
        </urn:ExciseMovement>
      </urn:ReminderMessageForExciseMovement>
    </urn:Body>
  </urn:IE802>

  lazy val IE803 = <urn:IE803
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE803:V3.01"
      xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
      xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
      xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>CSMISE.EC</urn1:MessageSender>
      <urn1:MessageRecipient>CSMISE.EC</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2008-09-29</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>00:18:33</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>a489157d-cd69-4370-a315-5fddee61ee0c</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>a3df9a67-3735-4983-a3a1-3987d34ca1f5</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:NotificationOfDivertedEADESAD>
        <urn:ExciseNotification>
          <urn:NotificationType>1</urn:NotificationType>
          <urn:NotificationDateAndTime>2018-11-01T05:36:46</urn:NotificationDateAndTime>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>1</urn:SequenceNumber>
        </urn:ExciseNotification>
        <!--0 to 9 repetitions:-->
        <urn:DownstreamArc>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
        </urn:DownstreamArc>
      </urn:NotificationOfDivertedEADESAD>
    </urn:Body>
  </urn:IE803>

  lazy val IE807 = <urn:IE807
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE807:V3.01"
      xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
      xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
      xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2008-09-29</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>00:18:33</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>1ef3e0d6-1201-4c27-9b0d-305c5f8b2230</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>fbf2d42a-19b0-4d7e-b255-94b6b31ebac5</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:InterruptionOfMovement>
        <urn:Attributes>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <!--Optional:-->
          <urn:ComplementaryInformation language="to">token</urn:ComplementaryInformation>
          <urn:DateAndTimeOfIssuance>2006-08-19T18:27:14</urn:DateAndTimeOfIssuance>
          <urn:ReasonForInterruptionCode>12</urn:ReasonForInterruptionCode>
          <urn:ReferenceNumberOfExciseOffice>ABdvRT13</urn:ReferenceNumberOfExciseOffice>
          <!--Optional:-->
          <urn:ExciseOfficerIdentification>token</urn:ExciseOfficerIdentification>
        </urn:Attributes>
        <!--0 to 9 repetitions:-->
        <urn:ReferenceControlReport>
          <urn:ControlReportReference>AFavAV24a5RV1aD2</urn:ControlReportReference>
        </urn:ReferenceControlReport>
        <!--0 to 9 repetitions:-->
        <urn:ReferenceEventReport>
          <urn:EventReportNumber>AFavAV24a5RV1aD2</urn:EventReportNumber>
        </urn:ReferenceEventReport>
      </urn:InterruptionOfMovement>
    </urn:Body>
  </urn:IE807>

  lazy val IE810: Elem = <urn:IE810 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE810:V3.01"
                                    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
                                    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
                                    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
                                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2023-06-13</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>10:16:58</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>GB100000000302249</urn1:MessageIdentifier>
      <urn1:CorrelationIdentifier>49ec29186e2c471eb1fb2e98313bd1ce</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:CancellationOfEAD>
        <urn:Attributes>
          <urn:DateAndTimeOfValidationOfCancellation>2023-06-13T10:17:05</urn:DateAndTimeOfValidationOfCancellation>
        </urn:Attributes>
        <urn:ExciseMovementEad>
        <urn:AdministrativeReferenceCode>23GB00000000000377161</urn:AdministrativeReferenceCode>
      </urn:ExciseMovementEad>
        <urn:Cancellation>
        <urn:CancellationReasonCode>3</urn:CancellationReasonCode>
      </urn:Cancellation>
      </urn:CancellationOfEAD>
    </urn:Body>
  </urn:IE810>


  lazy val IE813: Elem = <urn:IE813 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE813:V3.01"
                                    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
                                    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
                                    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
                                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2023-08-15</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>11:54:25</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>GB100000000302715</urn1:MessageIdentifier>
      <urn1:CorrelationIdentifier>PORTAL906384fb126d43e787a802683c03b44c</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:ChangeOfDestination>
        <urn:Attributes>
          <urn:DateAndTimeOfValidationOfChangeOfDestination>2023-08-15T11:54:32</urn:DateAndTimeOfValidationOfChangeOfDestination>
        </urn:Attributes>
        <urn:UpdateEadEsad>
          <urn:AdministrativeReferenceCode>23GB00000000000378126</urn:AdministrativeReferenceCode>
          <urn:JourneyTime>D02</urn:JourneyTime>
          <urn:ChangedTransportArrangement>1</urn:ChangedTransportArrangement>
          <urn:SequenceNumber>3</urn:SequenceNumber>
          <urn:InvoiceNumber>5678</urn:InvoiceNumber>
          <urn:TransportModeCode>4</urn:TransportModeCode>
        </urn:UpdateEadEsad>
        <urn:DestinationChanged>
          <urn:DestinationTypeCode>1</urn:DestinationTypeCode>
          <urn:NewConsigneeTrader language="en">
            <urn:Traderid>GBWK240176600</urn:Traderid>
            <urn:TraderName>pqr</urn:TraderName>
            <urn:StreetName>Tattenhoe Park</urn:StreetName>
            <urn:StreetNumber>18 Priestly</urn:StreetNumber>
            <urn:Postcode>MK4 4NW</urn:Postcode>
            <urn:City>Milton Keynes</urn:City>
          </urn:NewConsigneeTrader>
          <urn:DeliveryPlaceTrader language="en">
            <urn:Traderid>GB00240176601</urn:Traderid>
            <urn:TraderName>lmn</urn:TraderName>
            <urn:StreetName>Tattenhoe Park</urn:StreetName>
            <urn:StreetNumber>18 Priestl</urn:StreetNumber>
            <urn:Postcode>MK4 4NW</urn:Postcode>
            <urn:City>Milton Keynes</urn:City>
          </urn:DeliveryPlaceTrader>
          <urn:MovementGuarantee>
            <urn:GuarantorTypeCode>1</urn:GuarantorTypeCode>
          </urn:MovementGuarantee>
        </urn:DestinationChanged>
        <urn:NewTransporterTrader language="en">
          <urn:TraderName>pqr</urn:TraderName>
          <urn:StreetName>Tattenhoe Park</urn:StreetName>
          <urn:StreetNumber>18 Priestly</urn:StreetNumber>
          <urn:Postcode>MK4 4NW</urn:Postcode>
          <urn:City>Milton Keynes</urn:City>
        </urn:NewTransporterTrader>
        <urn:TransportDetails>
          <urn:TransportUnitCode>1</urn:TransportUnitCode>
          <urn:IdentityOfTransportUnits>1</urn:IdentityOfTransportUnits>
        </urn:TransportDetails>
      </urn:ChangeOfDestination>
    </urn:Body>
  </urn:IE813>

  lazy val IE815: Elem = IE815Template("GBWK002281023")
  lazy val IE815WithNoCosignor: Elem = IE815Template("")

  private def IE815Template(consignor: String): Elem = <urn:IE815 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE815:V3.01"
                                    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
                                    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
                                    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
                                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2023-09-09</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>03:22:47</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>6de1b822562c43fb9220d236e487c920</urn1:MessageIdentifier>
      <urn1:CorrelationIdentifier>PORTAL6de1b822562c43fb9220d236e487c920</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:SubmittedDraftOfEADESAD>
        <urn:Attributes>
          <urn:SubmissionMessageType>1</urn:SubmissionMessageType>
        </urn:Attributes>
        <urn:ConsigneeTrader language="en">
          <urn:Traderid>GBWKQOZ8OVLYR</urn:Traderid>
          <urn:TraderName>WFlgUjfC</urn:TraderName>
          <urn:StreetName>xoL0NsNyDi</urn:StreetName>
          <urn:StreetNumber>67</urn:StreetNumber>
          <urn:Postcode>A1 1AA</urn:Postcode>
          <urn:City>l8WSaHS9</urn:City>
        </urn:ConsigneeTrader>
        <urn:ConsignorTrader language="en">
          <urn:TraderExciseNumber>{consignor}</urn:TraderExciseNumber>
          <urn:TraderName>DIAGEO PLC</urn:TraderName>
          <urn:StreetName>msfvZUL1Oe</urn:StreetName>
          <urn:StreetNumber>25</urn:StreetNumber>
          <urn:Postcode>A1 1AA</urn:Postcode>
          <urn:City>QDHwPa61</urn:City>
        </urn:ConsignorTrader>
        <urn:PlaceOfDispatchTrader language="en">
          <urn:ReferenceOfTaxWarehouse>GB00DO459DMNX</urn:ReferenceOfTaxWarehouse>
          <urn:TraderName>2z0waekA</urn:TraderName>
          <urn:StreetName>MhO1XtDIVr</urn:StreetName>
          <urn:StreetNumber>25</urn:StreetNumber>
          <urn:Postcode>A1 1AA</urn:Postcode>
          <urn:City>zPCc6skm</urn:City>
        </urn:PlaceOfDispatchTrader>
        <urn:DeliveryPlaceTrader language="en">
          <urn:Traderid>GB00AIP67RAO3</urn:Traderid>
          <urn:TraderName>BJpWdv2N</urn:TraderName>
          <urn:StreetName>C24vvUqCw6</urn:StreetName>
          <urn:StreetNumber>43</urn:StreetNumber>
          <urn:Postcode>A1 1AA</urn:Postcode>
          <urn:City>A9ZlElxP</urn:City>
        </urn:DeliveryPlaceTrader>
        <urn:CompetentAuthorityDispatchOffice>
          <urn:ReferenceNumber>GB004098</urn:ReferenceNumber>
        </urn:CompetentAuthorityDispatchOffice>
        <urn:FirstTransporterTrader language="en">
          <urn:VatNumber>123798354</urn:VatNumber>
          <urn:TraderName>Mr Delivery place trader 4</urn:TraderName>
          <urn:StreetName>Delplace Avenue</urn:StreetName>
          <urn:StreetNumber>05</urn:StreetNumber>
          <urn:Postcode>FR5 4RN</urn:Postcode>
          <urn:City>Delville</urn:City>
        </urn:FirstTransporterTrader>
        <urn:DocumentCertificate>
          <urn:DocumentType>9</urn:DocumentType>
          <urn:DocumentReference>DPdQsYktZEJEESpc7b32Ig0U6B34XmHmfZU</urn:DocumentReference>
        </urn:DocumentCertificate>
        <urn:HeaderEadEsad>
          <urn:DestinationTypeCode>1</urn:DestinationTypeCode>
          <urn:JourneyTime>D07</urn:JourneyTime>
          <urn:TransportArrangement>1</urn:TransportArrangement>
        </urn:HeaderEadEsad>
        <urn:TransportMode>
          <urn:TransportModeCode>3</urn:TransportModeCode>
        </urn:TransportMode>
        <urn:MovementGuarantee>
          <urn:GuarantorTypeCode>1</urn:GuarantorTypeCode>
        </urn:MovementGuarantee>
        <urn:BodyEadEsad>
          <urn:BodyRecordUniqueReference>1</urn:BodyRecordUniqueReference>
          <urn:ExciseProductCode>B000</urn:ExciseProductCode>
          <urn:CnCode>22030001</urn:CnCode>
          <urn:Quantity>2000</urn:Quantity>
          <urn:GrossMass>20000</urn:GrossMass>
          <urn:NetMass>19999</urn:NetMass>
          <urn:AlcoholicStrengthByVolumeInPercentage>0.5</urn:AlcoholicStrengthByVolumeInPercentage>
          <urn:FiscalMarkUsedFlag>0</urn:FiscalMarkUsedFlag>
          <urn:Package>
            <urn:KindOfPackages>BA</urn:KindOfPackages>
            <urn:NumberOfPackages>2</urn:NumberOfPackages>
          </urn:Package>
        </urn:BodyEadEsad>
        <urn:EadEsadDraft>
          <urn:LocalReferenceNumber>LRNQA20230909022221</urn:LocalReferenceNumber>
          <urn:InvoiceNumber>Test123</urn:InvoiceNumber>
          <urn:InvoiceDate>2023-09-09</urn:InvoiceDate>
          <urn:OriginTypeCode>1</urn:OriginTypeCode>
          <urn:DateOfDispatch>2023-09-09</urn:DateOfDispatch>
          <urn:TimeOfDispatch>12:00:00</urn:TimeOfDispatch>
        </urn:EadEsadDraft>
        <urn:TransportDetails>
          <urn:TransportUnitCode>1</urn:TransportUnitCode>
          <urn:IdentityOfTransportUnits>100</urn:IdentityOfTransportUnits>
        </urn:TransportDetails>
      </urn:SubmittedDraftOfEADESAD>
    </urn:Body>
  </urn:IE815>

  lazy val IE818: Elem = <urn:IE818 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE818:V3.01"
                                    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
                                    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
                                    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
                                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2023-08-30</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>13:53:53.425279</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>GB100000000302814</urn1:MessageIdentifier>
      <urn1:CorrelationIdentifier>73e87e9b-1145-4dbf-8ee3-807ac103ba62</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:AcceptedOrRejectedReportOfReceiptExport>
        <urn:Attributes>
          <urn:DateAndTimeOfValidationOfReportOfReceiptExport>2023-08-30T14:53:56</urn:DateAndTimeOfValidationOfReportOfReceiptExport>
        </urn:Attributes>
        <urn:ConsigneeTrader language="en">
          <urn:Traderid>GBWK002281023</urn:Traderid>
          <urn:TraderName>Meredith Ent</urn:TraderName>
          <urn:StreetName>Romanus Crescent</urn:StreetName>
          <urn:StreetNumber>38</urn:StreetNumber>
          <urn:Postcode>SE24 5GY</urn:Postcode>
          <urn:City>London</urn:City>
        </urn:ConsigneeTrader>
        <urn:ExciseMovement>
          <urn:AdministrativeReferenceCode>23GB00000000000378553</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>1</urn:SequenceNumber>
        </urn:ExciseMovement>
        <urn:DeliveryPlaceTrader language="en">
          <urn:Traderid>GB00002281023</urn:Traderid>
          <urn:TraderName>Meredith Ent</urn:TraderName>
          <urn:StreetName>Romanus Crescent</urn:StreetName>
          <urn:StreetNumber>38</urn:StreetNumber>
          <urn:Postcode>SE24 5GY</urn:Postcode>
          <urn:City>London</urn:City>
        </urn:DeliveryPlaceTrader>
        <urn:DestinationOffice>
          <urn:ReferenceNumber>GB004098</urn:ReferenceNumber>
        </urn:DestinationOffice>
        <urn:ReportOfReceiptExport>
          <urn:DateOfArrivalOfExciseProducts>2023-08-30</urn:DateOfArrivalOfExciseProducts>
          <urn:GlobalConclusionOfReceipt>3</urn:GlobalConclusionOfReceipt>
        </urn:ReportOfReceiptExport>
        <urn:BodyReportOfReceiptExport>
          <urn:BodyRecordUniqueReference>1</urn:BodyRecordUniqueReference>
          <urn:ExciseProductCode>B000</urn:ExciseProductCode>
          <urn:UnsatisfactoryReason>
            <urn:UnsatisfactoryReasonCode>2</urn:UnsatisfactoryReasonCode>
            <urn:ComplementaryInformation language="en">All is good :)</urn:ComplementaryInformation>
          </urn:UnsatisfactoryReason>
        </urn:BodyReportOfReceiptExport>
      </urn:AcceptedOrRejectedReportOfReceiptExport>
    </urn:Body>
  </urn:IE818>

  lazy val IE819: Elem =
    <urn:IE819 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE819:V3.01"
               xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
               xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
               xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <urn:Header>
        <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
        <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
        <urn1:DateOfPreparation>2023-08-31</urn1:DateOfPreparation>
        <urn1:TimeOfPreparation>11:32:45</urn1:TimeOfPreparation>
        <urn1:MessageIdentifier>GB100000000302820</urn1:MessageIdentifier>
        <urn1:CorrelationIdentifier>PORTAL1a027311ecef42ef90e40d7201b4f5a7</urn1:CorrelationIdentifier>
      </urn:Header>
      <urn:Body>
        <urn:AlertOrRejectionOfEADESAD>
          <urn:Attributes>
            <urn:DateAndTimeOfValidationOfAlertRejection>2023-08-31T11:32:47</urn:DateAndTimeOfValidationOfAlertRejection>
          </urn:Attributes>
          <urn:ConsigneeTrader language="en">
            <urn:Traderid>GBWK002281023</urn:Traderid>
            <urn:TraderName>Roms PLC</urn:TraderName>
            <urn:StreetName>Bellhouston Road</urn:StreetName>
            <urn:StreetNumber>420</urn:StreetNumber>
            <urn:Postcode>G41 5BS</urn:Postcode>
            <urn:City>Glasgow</urn:City>
          </urn:ConsigneeTrader>
          <urn:ExciseMovement>
            <urn:AdministrativeReferenceCode>23GB00000000000378574</urn:AdministrativeReferenceCode>
            <urn:SequenceNumber>1</urn:SequenceNumber>
          </urn:ExciseMovement>
          <urn:DestinationOffice>
            <urn:ReferenceNumber>GB004098</urn:ReferenceNumber>
          </urn:DestinationOffice>
          <urn:AlertOrRejection>
            <urn:DateOfAlertOrRejection>2023-08-31</urn:DateOfAlertOrRejection>
            <urn:EadEsadRejectedFlag>1</urn:EadEsadRejectedFlag>
          </urn:AlertOrRejection>
          <urn:AlertOrRejectionOfEadEsadReason>
            <urn:AlertOrRejectionOfMovementReasonCode>2</urn:AlertOrRejectionOfMovementReasonCode>
            <urn:ComplementaryInformation language="en">test</urn:ComplementaryInformation>
          </urn:AlertOrRejectionOfEadEsadReason>
        </urn:AlertOrRejectionOfEADESAD>
      </urn:Body>
    </urn:IE819>

  lazy val IE829: Elem = <urn:IE829
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE829:V3.01"
      xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
      xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
      xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2009-06-10</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>23:33:23</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>329a9edb-4a30-4a62-9f41-7450e8bc9c4c</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>35e81e00-0658-4469-8d57-fd219a27cc2e</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:NotificationOfAcceptedExport>
        <urn:Attributes>
          <urn:DateAndTimeOfIssuance>2016-10-25T04:28:36</urn:DateAndTimeOfIssuance>
        </urn:Attributes>
        <!--Optional:-->
        <urn:ConsigneeTrader language="to">
          <!--Optional:-->
          <urn:Traderid>GBWK002671021</urn:Traderid>
          <urn:TraderName>token</urn:TraderName>
          <urn:StreetName>token</urn:StreetName>
          <!--Optional:-->
          <urn:StreetNumber>token</urn:StreetNumber>
          <urn:Postcode>token</urn:Postcode>
          <urn:City>token</urn:City>
          <!--Optional:-->
          <urn:EoriNumber>token</urn:EoriNumber>
        </urn:ConsigneeTrader>
        <!--1 to 99 repetitions:-->
        <urn:ExciseMovementEad>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>4</urn:SequenceNumber>
        </urn:ExciseMovementEad>
        <!--Optional:-->
        <urn:ExportPlaceCustomsOffice>
          <urn:ReferenceNumber>GB054045</urn:ReferenceNumber>
        </urn:ExportPlaceCustomsOffice>
        <urn:ExportAcceptance>
          <urn:ReferenceNumberOfSenderCustomsOffice>GB004323</urn:ReferenceNumberOfSenderCustomsOffice>
          <!--Optional:-->
          <urn:IdentificationOfSenderCustomsOfficer>token</urn:IdentificationOfSenderCustomsOfficer>
          <urn:DateOfAcceptance>2014-01-07</urn:DateOfAcceptance>
          <urn:DocumentReferenceNumber>token</urn:DocumentReferenceNumber>
        </urn:ExportAcceptance>
      </urn:NotificationOfAcceptedExport>
    </urn:Body>
  </urn:IE829>

  lazy val IE837WithConsignor: Elem =
    <urn:IE837 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE837:V3.01"
               xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
               xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
               xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <urn:Header>
        <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
        <urn1:MessageRecipient>NDEA.EU</urn1:MessageRecipient>
        <urn1:DateOfPreparation>2023-08-10</urn1:DateOfPreparation>
        <urn1:TimeOfPreparation>09:56:40.695540</urn1:TimeOfPreparation>
        <urn1:MessageIdentifier>GB100000000302681</urn1:MessageIdentifier>
        <urn1:CorrelationIdentifier>a2f65a81-c297-4117-bea5-556129529463</urn1:CorrelationIdentifier>
      </urn:Header>
      <urn:Body>
        <urn:ExplanationOnDelayForDelivery>
          <urn:Attributes>
            <urn:SubmitterIdentification>GBWK240176600</urn:SubmitterIdentification>
            <urn:SubmitterType>1</urn:SubmitterType>
            <urn:ExplanationCode>6</urn:ExplanationCode>
            <urn:ComplementaryInformation language="en">Accident on M5</urn:ComplementaryInformation>
            <urn:MessageRole>1</urn:MessageRole>
            <urn:DateAndTimeOfValidationOfExplanationOnDelay>2023-08-10T10:56:42</urn:DateAndTimeOfValidationOfExplanationOnDelay>
          </urn:Attributes>
          <urn:ExciseMovement>
            <urn:AdministrativeReferenceCode>16GB00000000000192223</urn:AdministrativeReferenceCode>
            <urn:SequenceNumber>2</urn:SequenceNumber>
          </urn:ExciseMovement>
        </urn:ExplanationOnDelayForDelivery>
      </urn:Body>
    </urn:IE837>

  lazy val IE837WithConsignee: Elem =
    <urn:IE837 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE837:V3.01"
               xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
               xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
               xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <urn:Header>
        <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
        <urn1:MessageRecipient>NDEA.EU</urn1:MessageRecipient>
        <urn1:DateOfPreparation>2023-08-10</urn1:DateOfPreparation>
        <urn1:TimeOfPreparation>09:56:40.695540</urn1:TimeOfPreparation>
        <urn1:MessageIdentifier>GB100000000302681</urn1:MessageIdentifier>
        <urn1:CorrelationIdentifier>a2f65a81-c297-4117-bea5-556129529463</urn1:CorrelationIdentifier>
      </urn:Header>
      <urn:Body>
        <urn:ExplanationOnDelayForDelivery>
          <urn:Attributes>
            <urn:SubmitterIdentification>GBWK240176600</urn:SubmitterIdentification>
            <urn:SubmitterType>2</urn:SubmitterType>
            <urn:ExplanationCode>6</urn:ExplanationCode>
            <urn:ComplementaryInformation language="en">Accident on M5</urn:ComplementaryInformation>
            <urn:MessageRole>1</urn:MessageRole>
            <urn:DateAndTimeOfValidationOfExplanationOnDelay>2023-08-10T10:56:42</urn:DateAndTimeOfValidationOfExplanationOnDelay>
          </urn:Attributes>
          <urn:ExciseMovement>
            <urn:AdministrativeReferenceCode>16GB00000000000192223</urn:AdministrativeReferenceCode>
            <urn:SequenceNumber>2</urn:SequenceNumber>
          </urn:ExciseMovement>
        </urn:ExplanationOnDelayForDelivery>
      </urn:Body>
    </urn:IE837>

  lazy val IE839: Elem = <urn:IE839
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE839:V3.01"
    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <urn:Header>
        <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
        <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
        <urn1:DateOfPreparation>2011-06-29</urn1:DateOfPreparation>
        <urn1:TimeOfPreparation>20:24:23</urn1:TimeOfPreparation>
        <urn1:MessageIdentifier>9b60495c-e967-4d8c-8df0-e60a7a33cd80</urn1:MessageIdentifier>
        <!--Optional:-->
        <urn1:CorrelationIdentifier>0a2eb5f2-53ce-41e1-add8-2e957ee079f4</urn1:CorrelationIdentifier>
      </urn:Header>
      <urn:Body>
        <urn:RefusalByCustoms>
          <urn:Attributes>
            <urn:DateAndTimeOfIssuance>2012-11-05T00:24:55</urn:DateAndTimeOfIssuance>
          </urn:Attributes>
          <!--Optional:-->
          <urn:ConsigneeTrader language="to">
            <!--Optional:-->
            <urn:Traderid>GBWK002281010</urn:Traderid>
            <urn:TraderName>token</urn:TraderName>
            <urn:StreetName>token</urn:StreetName>
            <!--Optional:-->
            <urn:StreetNumber>token</urn:StreetNumber>
            <urn:Postcode>token</urn:Postcode>
            <urn:City>token</urn:City>
            <!--Optional:-->
            <urn:EoriNumber>token</urn:EoriNumber>
          </urn:ConsigneeTrader>
          <!--Optional:-->
          <urn:ExportPlaceCustomsOffice>
            <urn:ReferenceNumber>GB004045</urn:ReferenceNumber>
          </urn:ExportPlaceCustomsOffice>
          <!--Optional:-->
          <urn:ExportCrossCheckingDiagnoses>
            <!--Optional:-->
            <urn:LocalReferenceNumber>token</urn:LocalReferenceNumber>
            <!--Optional:-->
            <urn:DocumentReferenceNumber>token</urn:DocumentReferenceNumber>
            <!--1 or more repetitions:-->
            <urn:Diagnosis>
              <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
              <urn:BodyRecordUniqueReference>234</urn:BodyRecordUniqueReference>
              <urn:DiagnosisCode>3</urn:DiagnosisCode>
            </urn:Diagnosis>
          </urn:ExportCrossCheckingDiagnoses>
          <urn:Rejection>
            <urn:RejectionDateAndTime>2003-03-28T17:17:22</urn:RejectionDateAndTime>
            <urn:RejectionReasonCode>2</urn:RejectionReasonCode>
          </urn:Rejection>
          <!--0 to 99 repetitions:-->
          <urn:CEadVal>
            <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
            <urn:SequenceNumber>3</urn:SequenceNumber>
          </urn:CEadVal>
          <!--Optional:-->
          <urn:NEadSub>
            <urn:LocalReferenceNumber>token</urn:LocalReferenceNumber>
          </urn:NEadSub>
        </urn:RefusalByCustoms>
      </urn:Body>
    </urn:IE839>

  lazy val IE840 = <urn:IE840
  xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE840:V3.01"
  xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
  xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
  xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2017-02-08</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>03:01:47</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>d56198c9-7f17-4598-a0a1-b9c20ec06798</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>4a768bff-e8b0-4689-a2b3-8a6a0c1e0805</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:EventReportEnvelope>
        <urn:Attributes>
          <urn:EventReportMessageType>3</urn:EventReportMessageType>
          <!--Optional:-->
          <urn:DateAndTimeOfValidationOfEventReport>2009-12-15T07:25:57</urn:DateAndTimeOfValidationOfEventReport>
        </urn:Attributes>
        <urn:HeaderEventReport>
          <!--Optional:-->
          <urn:EventReportNumber>AFavAV24a5RV1aD2</urn:EventReportNumber>
          <!--Optional:-->
          <urn:MsOfSubmissionEventReportReference>token</urn:MsOfSubmissionEventReportReference>
          <urn:ReferenceNumberOfExciseOffice>ABdvRT13</urn:ReferenceNumberOfExciseOffice>
          <urn:MemberStateOfEvent>DH</urn:MemberStateOfEvent>
        </urn:HeaderEventReport>
        <!--Optional:-->
        <urn:ExciseMovement>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>13</urn:SequenceNumber>
        </urn:ExciseMovement>
        <!--Optional:-->
        <urn:OtherAccompanyingDocument>
          <urn:OtherAccompanyingDocumentType>2</urn:OtherAccompanyingDocumentType>
          <!--Optional:-->
          <urn:ShortDescriptionOfOtherAccompanyingDocument language="to">token</urn:ShortDescriptionOfOtherAccompanyingDocument>
          <urn:OtherAccompanyingDocumentNumber>token</urn:OtherAccompanyingDocumentNumber>
          <urn:OtherAccompanyingDocumentDate>2006-12-04</urn:OtherAccompanyingDocumentDate>
          <!--Optional:-->
          <urn:ImageOfOtherAccompanyingDocument>YWx0b3M=</urn:ImageOfOtherAccompanyingDocument>
          <urn:MemberStateOfDispatch>to</urn:MemberStateOfDispatch>
          <urn:MemberStateOfDestination>to</urn:MemberStateOfDestination>
          <!--0 to 9 repetitions:-->
          <urn:PersonInvolvedInMovementTrader language="to">
            <!--Optional:-->
            <urn:TraderExciseNumber>GBWK0033815At</urn:TraderExciseNumber>
            <!--Optional:-->
            <urn:Traderid>token</urn:Traderid>
            <!--Optional:-->
            <urn:TraderName>token</urn:TraderName>
            <!--Optional:-->
            <urn:TraderPersonType>2</urn:TraderPersonType>
            <!--Optional:-->
            <urn:MemberStateCode>AD</urn:MemberStateCode>
            <!--Optional:-->
            <urn:StreetName>token</urn:StreetName>
            <!--Optional:-->
            <urn:StreetNumber>token</urn:StreetNumber>
            <!--Optional:-->
            <urn:Postcode>token</urn:Postcode>
            <!--Optional:-->
            <urn:City>token</urn:City>
            <!--Optional:-->
            <urn:PhoneNumber>token</urn:PhoneNumber>
            <!--Optional:-->
            <urn:FaxNumber>token</urn:FaxNumber>
            <!--Optional:-->
            <urn:EmailAddress>token</urn:EmailAddress>
          </urn:PersonInvolvedInMovementTrader>
          <!--Zero or more repetitions:-->
          <urn:GoodsItem>
            <!--Optional:-->
            <urn:DescriptionOfTheGoods>token</urn:DescriptionOfTheGoods>
            <!--Optional:-->
            <urn:CnCode>15367567</urn:CnCode>
            <!--Optional:-->
            <urn:CommercialDescriptionOfTheGoods>token</urn:CommercialDescriptionOfTheGoods>
            <!--Optional:-->
            <urn:AdditionalCode>token</urn:AdditionalCode>
            <!--Optional:-->
            <urn:Quantity>1000.0</urn:Quantity>
            <!--Optional:-->
            <urn:UnitOfMeasureCode>12</urn:UnitOfMeasureCode>
            <!--Optional:-->
            <urn:GrossMass>1000.0</urn:GrossMass>
            <!--Optional:-->
            <urn:NetMass>1000.0</urn:NetMass>
          </urn:GoodsItem>
          <!--Optional:-->
          <urn:MeansOfTransport>
            <urn:TraderName>token</urn:TraderName>
            <urn:StreetName>token</urn:StreetName>
            <!--Optional:-->
            <urn:StreetNumber>token</urn:StreetNumber>
            <urn:TransporterCountry>to</urn:TransporterCountry>
            <urn:Postcode>token</urn:Postcode>
            <urn:City>token</urn:City>
            <urn:TransportModeCode>12</urn:TransportModeCode>
            <!--Optional:-->
            <urn:AcoComplementaryInformation language="to">token</urn:AcoComplementaryInformation>
            <urn:Registration>token</urn:Registration>
            <urn:CountryOfRegistration>to</urn:CountryOfRegistration>
          </urn:MeansOfTransport>
        </urn:OtherAccompanyingDocument>
        <urn:EventReport>
          <urn:DateOfEvent>2002-01-29</urn:DateOfEvent>
          <urn:PlaceOfEvent language="to">token</urn:PlaceOfEvent>
          <!--Optional:-->
          <urn:ExciseOfficerIdentification>token</urn:ExciseOfficerIdentification>
          <urn:SubmittingPerson>token</urn:SubmittingPerson>
          <urn:SubmittingPersonCode>12</urn:SubmittingPersonCode>
          <!--Optional:-->
          <urn:SubmittingPersonComplement language="to">token</urn:SubmittingPersonComplement>
          <!--Optional:-->
          <urn:ChangedTransportArrangement>4</urn:ChangedTransportArrangement>
          <!--Optional:-->
          <urn:Comments language="to">token</urn:Comments>
        </urn:EventReport>
        <!--0 to 9 repetitions:-->
        <urn:EvidenceOfEvent>
          <!--Optional:-->
          <urn:IssuingAuthority language="to">token</urn:IssuingAuthority>
          <urn:EvidenceTypeCode>1</urn:EvidenceTypeCode>
          <urn:ReferenceOfEvidence language="to">token</urn:ReferenceOfEvidence>
          <!--Optional:-->
          <urn:ImageOfEvidence>ZXQ=</urn:ImageOfEvidence>
          <!--Optional:-->
          <urn:EvidenceTypeComplement language="to">token</urn:EvidenceTypeComplement>
        </urn:EvidenceOfEvent>
        <!--Optional:-->
        <urn:NewTransportArrangerTrader language="to">
          <!--Optional:-->
          <urn:VatNumber>token</urn:VatNumber>
          <urn:TraderName>token</urn:TraderName>
          <urn:StreetName>token</urn:StreetName>
          <!--Optional:-->
          <urn:StreetNumber>token</urn:StreetNumber>
          <urn:Postcode>token</urn:Postcode>
          <urn:City>token</urn:City>
        </urn:NewTransportArrangerTrader>
        <!--Optional:-->
        <urn:NewTransporterTrader language="to">
          <!--Optional:-->
          <urn:VatNumber>token</urn:VatNumber>
          <urn:TraderName>token</urn:TraderName>
          <urn:StreetName>token</urn:StreetName>
          <!--Optional:-->
          <urn:StreetNumber>token</urn:StreetNumber>
          <urn:Postcode>token</urn:Postcode>
          <urn:City>token</urn:City>
        </urn:NewTransporterTrader>
        <!--0 to 99 repetitions:-->
        <urn:TransportDetails>
          <urn:TransportUnitCode>12</urn:TransportUnitCode>
          <!--Optional:-->
          <urn:IdentityOfTransportUnits>token</urn:IdentityOfTransportUnits>
          <!--Optional:-->
          <urn:CommercialSealIdentification>token</urn:CommercialSealIdentification>
          <!--Optional:-->
          <urn:ComplementaryInformation language="to">token</urn:ComplementaryInformation>
          <!--Optional:-->
          <urn:SealInformation language="to">token</urn:SealInformation>
        </urn:TransportDetails>
        <!--0 to 99 repetitions:-->
        <urn:BodyEventReport>
          <urn:EventTypeCode>12</urn:EventTypeCode>
          <!--Optional:-->
          <urn:AssociatedInformation language="to">token</urn:AssociatedInformation>
          <!--Optional:-->
          <urn:BodyRecordUniqueReference>34</urn:BodyRecordUniqueReference>
          <!--Optional:-->
          <urn:DescriptionOfTheGoods>token</urn:DescriptionOfTheGoods>
          <!--Optional:-->
          <urn:CnCode>15367534</urn:CnCode>
          <!--Optional:-->
          <urn:AdditionalCode>token</urn:AdditionalCode>
          <!--Optional:-->
          <urn:IndicatorOfShortageOrExcess>E</urn:IndicatorOfShortageOrExcess>
          <!--Optional:-->
          <urn:ObservedShortageOrExcess>1000.0</urn:ObservedShortageOrExcess>
        </urn:BodyEventReport>
      </urn:EventReportEnvelope>
    </urn:Body>
  </urn:IE840>

  lazy val IE871: Elem =
    <urn:IE871 xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE871:V3.01"
               xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
               xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
               xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <urn:Header>
        <urn1:MessageSender>NDEA.GB</urn1:MessageSender>
        <urn1:MessageRecipient>NDEA.GB</urn1:MessageRecipient>
        <urn1:DateOfPreparation>2023-08-15</urn1:DateOfPreparation>
        <urn1:TimeOfPreparation>09:57:17</urn1:TimeOfPreparation>
        <urn1:MessageIdentifier>GB100000000302708</urn1:MessageIdentifier>
        <urn1:CorrelationIdentifier>PORTAL56f290f317b947c79ee93b806800351b</urn1:CorrelationIdentifier>
      </urn:Header> <urn:Body>
      <urn:ExplanationOnReasonForShortage>
        <urn:Attributes>
          <urn:SubmitterType>1</urn:SubmitterType>
          <urn:DateAndTimeOfValidationOfExplanationOnShortage>2023-08-15T09:57:19</urn:DateAndTimeOfValidationOfExplanationOnShortage>
        </urn:Attributes>
        <urn:ExciseMovement>
          <urn:AdministrativeReferenceCode>23GB00000000000377768</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>1</urn:SequenceNumber>
        </urn:ExciseMovement>
        <urn:ConsignorTrader language="en">
          <urn:TraderExciseNumber>GBWK240176600</urn:TraderExciseNumber>
          <urn:TraderName>CHARLES HASWELL AND PARTNERS LTD</urn:TraderName>
          <urn:StreetName>1</urn:StreetName>
          <urn:Postcode>AA11AA</urn:Postcode>
          <urn:City>1</urn:City>
        </urn:ConsignorTrader>
        <urn:Analysis>
          <urn:DateOfAnalysis>2023-08-15</urn:DateOfAnalysis>
          <urn:GlobalExplanation language="en">Courier drank the wine</urn:GlobalExplanation>
        </urn:Analysis>
      </urn:ExplanationOnReasonForShortage>
    </urn:Body>
    </urn:IE871>

  lazy val IE880: Elem = <urn:IE880
    xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE880:V3.01"
    xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
    xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2008-09-29</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>00:18:33</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>91CFRXRWG7MKAFUCDJPA6</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>91CFRXRWG7MKAFUCDJPA6</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:ManualClosureRequest>
        <urn:Attributes>
          <urn:AdministrativeReferenceCode>tokentokentokentokent</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>to</urn:SequenceNumber>
          <urn:ManualClosureRequestReasonCode>t</urn:ManualClosureRequestReasonCode>
          <!--Optional:-->
          <urn:ManualClosureRequestReasonCodeComplement language="to">token</urn:ManualClosureRequestReasonCodeComplement>
        </urn:Attributes>
        <!--0 to 9 repetitions:-->
        <urn:SupportingDocuments>
          <!--Optional:-->
          <urn:SupportingDocumentDescription language="to">token</urn:SupportingDocumentDescription>
          <!--Optional:-->
          <urn:ReferenceOfSupportingDocument language="to">token</urn:ReferenceOfSupportingDocument>
          <!--Optional:-->
          <urn:ImageOfDocument>dmVudG9z</urn:ImageOfDocument>
          <!--Optional:-->
          <urn:SupportingDocumentType>toke</urn:SupportingDocumentType>
        </urn:SupportingDocuments>
        <!--Zero or more repetitions:-->
        <urn:BodyManualClosure>
          <urn:BodyRecordUniqueReference>tok</urn:BodyRecordUniqueReference>
          <!--Optional:-->
          <urn:IndicatorOfShortageOrExcess>S</urn:IndicatorOfShortageOrExcess>
          <!--Optional:-->
          <urn:ObservedShortageOrExcess>1000.0</urn:ObservedShortageOrExcess>
          <!--Optional:-->
          <urn:ExciseProductCode>toke</urn:ExciseProductCode>
          <!--Optional:-->
          <urn:RefusedQuantity>1000.0</urn:RefusedQuantity>
          <!--Optional:-->
          <urn:ComplementaryInformation language="to">token</urn:ComplementaryInformation>
        </urn:BodyManualClosure>
      </urn:ManualClosureRequest>
    </urn:Body>
  </urn:IE880>

  lazy val IE881: Elem = <urn:IE881
  xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE881:V3.01"
  xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
  xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
  xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2000-01-03</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>03:03:53</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>8c7e47b1-a294-4a12-a00b-f9b1d3bb4af6</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>2815389c-a16c-47e3-8ad9-18bfa76703a5</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:ManualClosureResponse>
        <urn:Attributes>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>13</urn:SequenceNumber>
          <!--Optional:-->
          <urn:DateOfArrivalOfExciseProducts>2015-12-11</urn:DateOfArrivalOfExciseProducts>
          <!--Optional:-->
          <urn:GlobalConclusionOfReceipt>2</urn:GlobalConclusionOfReceipt>
          <!--Optional:-->
          <urn:ComplementaryInformation language="to">token</urn:ComplementaryInformation>
          <urn:ManualClosureRequestReasonCode>1</urn:ManualClosureRequestReasonCode>
          <!--Optional:-->
          <urn:ManualClosureRequestReasonCodeComplement language="to">token</urn:ManualClosureRequestReasonCodeComplement>
          <urn:ManualClosureRequestAccepted>1</urn:ManualClosureRequestAccepted>
          <!--Optional:-->
          <urn:ManualClosureRejectionReasonCode>2</urn:ManualClosureRejectionReasonCode>
          <!--Optional:-->
          <urn:ManualClosureRejectionComplement language="to">token</urn:ManualClosureRejectionComplement>
        </urn:Attributes>
        <!--0 to 9 repetitions:-->
        <urn:SupportingDocuments>
          <!--Optional:-->
          <urn:SupportingDocumentDescription language="to">token</urn:SupportingDocumentDescription>
          <!--Optional:-->
          <urn:ReferenceOfSupportingDocument language="to">token</urn:ReferenceOfSupportingDocument>
          <!--Optional:-->
          <urn:ImageOfDocument>dGFsaWE=</urn:ImageOfDocument>
          <!--Optional:-->
          <urn:SupportingDocumentType>toke</urn:SupportingDocumentType>
        </urn:SupportingDocuments>
        <!--Zero or more repetitions:-->
        <urn:BodyManualClosure>
          <urn:BodyRecordUniqueReference>67</urn:BodyRecordUniqueReference>
          <!--Optional:-->
          <urn:IndicatorOfShortageOrExcess>E</urn:IndicatorOfShortageOrExcess>
          <!--Optional:-->
          <urn:ObservedShortageOrExcess>1000.0</urn:ObservedShortageOrExcess>
          <!--Optional:-->
          <urn:ExciseProductCode>toke</urn:ExciseProductCode>
          <!--Optional:-->
          <urn:RefusedQuantity>1000.0</urn:RefusedQuantity>
          <!--Optional:-->
          <urn:ComplementaryInformation language="to">token</urn:ComplementaryInformation>
        </urn:BodyManualClosure>
      </urn:ManualClosureResponse>
    </urn:Body>
  </urn:IE881>

  lazy val IE905: Elem = <urn:IE905
      xmlns:urn="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:IE905:V3.01"
      xmlns:urn1="urn:publicid:-:EC:DGTAXUD:EMCS:PHASE4:TMS:V3.01"
      xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
      xmlns="http://www.hmrc.gov.uk/ChRIS/Service/Control"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <urn:Header>
      <urn1:MessageSender>NDEA.XI</urn1:MessageSender>
      <urn1:MessageRecipient>NDEA.XI</urn1:MessageRecipient>
      <urn1:DateOfPreparation>2002-01-15</urn1:DateOfPreparation>
      <urn1:TimeOfPreparation>23:28:52</urn1:TimeOfPreparation>
      <urn1:MessageIdentifier>1375e6ce-f037-49cb-a7ca-3d21c7c6f435</urn1:MessageIdentifier>
      <!--Optional:-->
      <urn1:CorrelationIdentifier>8ef3c27e-edef-48f9-8788-a53149211841</urn1:CorrelationIdentifier>
    </urn:Header>
    <urn:Body>
      <urn:StatusResponse>
        <urn:Attributes>
          <urn:AdministrativeReferenceCode>91CFRXRWG7MKAFUCDJPA6</urn:AdministrativeReferenceCode>
          <urn:SequenceNumber>4</urn:SequenceNumber>
          <urn:Status>X03</urn:Status>
          <urn:LastReceivedMessageType>IE905</urn:LastReceivedMessageType>
        </urn:Attributes>
      </urn:StatusResponse>
    </urn:Body>
  </urn:IE905>

}
