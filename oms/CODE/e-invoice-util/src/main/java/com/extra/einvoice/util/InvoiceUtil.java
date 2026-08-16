package com.extra.einvoice.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentMeansType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BuildingNumberType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EmbeddedDocumentBinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InstructionNoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineCountNumericType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentMeansCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PostalZoneType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ProfileIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UUIDType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

public class InvoiceUtil {

	private static final ProfileIDType PROFILE_ID_TYPE;

	private static JAXBContext jaxbContext;

	static {
		try {
		} catch (Exception w) {
		}
		PROFILE_ID_TYPE = new ProfileIDType();
		PROFILE_ID_TYPE.setValue("reporting:1.0");
	}

	public static String serializeToXml(InvoiceType invoice) throws Exception {
		// rountInvoiceAmount(invoice);
		JAXBElement<InvoiceType> elem = new ObjectFactory().createInvoice(invoice);
		Marshaller marshaller;
		marshaller = getJaxbContext().createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(elem, stringWriter);
		return stringWriter.toString();
	}

	private static void addICVDocumentRef(InvoiceType invoice) {

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();

		IDType idType = new IDType();
		idType.setValue("ICV");
		documentReferenceType.setID(idType);

		UUIDType uuidType = new UUIDType();
		documentReferenceType.setUUID(uuidType);

		invoice.getAdditionalDocumentReference().add(documentReferenceType);
	}

	private static void addPIHDocumentRef(InvoiceType invoice) throws Exception {
		DocumentReferenceType documentReferenceType = new DocumentReferenceType();

		IDType idType = new IDType();
		idType.setValue("PIH");
		documentReferenceType.setID(idType);

		AttachmentType attachmentType = new AttachmentType();

		EmbeddedDocumentBinaryObjectType embeddedDocumentBinaryObjectType = new EmbeddedDocumentBinaryObjectType();
		embeddedDocumentBinaryObjectType.setMimeCode("text/plain");

		attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocumentBinaryObjectType);
		documentReferenceType.setAttachment(attachmentType);

		invoice.getAdditionalDocumentReference().add(documentReferenceType);
	}

	private static void addCustomerParty(InvoiceType invoice, ResultSet rs) throws Exception {
		CustomerPartyType customerPartyType = new CustomerPartyType();
		PartyType partyType = new PartyType();

		addCustomerIdentification(partyType, rs);
		addCustomerPostalAddress(partyType, rs);
		addCustomerLegalEntity(partyType, rs);

		customerPartyType.setParty(partyType);
		invoice.setAccountingCustomerParty(customerPartyType);
	}

	private static void addCustomerIdentification(PartyType partyType, ResultSet rs) throws Exception {
		String custVAT = rs.getString("CUST_VAT_NO");
		if (custVAT != null && !custVAT.trim().equals("")) {
			PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
			IDType idType = new IDType();
			idType.setSchemeID("TIN");
			idType.setValue(custVAT);
			partyIdentificationType.setID(idType);
			partyType.getPartyIdentification().add(partyIdentificationType);
		}
	}

	private static void addCustomerPostalAddress(PartyType partyType, ResultSet rs) throws Exception {
		AddressType addressType = new AddressType();

		StreetNameType streetNameType = new StreetNameType();
		streetNameType.setValue(rs.getString("CUST_ADDRESS1"));
		addressType.setStreetName(streetNameType);

		BuildingNumberType buildingNumberType = new BuildingNumberType();
		buildingNumberType.setValue("..");
		addressType.setBuildingNumber(buildingNumberType);

		CitySubdivisionNameType citySubdivisionNameType = new CitySubdivisionNameType();
		citySubdivisionNameType.setValue(rs.getString("STATE"));
		addressType.setCitySubdivisionName(citySubdivisionNameType);

		CityNameType cityNameType = new CityNameType();
		cityNameType.setValue(rs.getString("CITY"));
		addressType.setCityName(cityNameType);

		PostalZoneType postalZoneType = new PostalZoneType();
		postalZoneType.setValue(rs.getString("POSTAL_CODE"));
		addressType.setPostalZone(postalZoneType);

		CountryType countryType = new CountryType();
		IdentificationCodeType identificationCodeType = new IdentificationCodeType();
		identificationCodeType.setValue(rs.getString("COUNTRY"));
		countryType.setIdentificationCode(identificationCodeType);
		addressType.setCountry(countryType);

		partyType.setPostalAddress(addressType);
	}

	private static void addCustomerLegalEntity(PartyType partyType, ResultSet rs) throws Exception {

		PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
		RegistrationNameType registrationNameType = new RegistrationNameType();
		registrationNameType.setValue(rs.getString("CUST_NAME"));
		partyLegalEntityType.setRegistrationName(registrationNameType);

		partyType.getPartyLegalEntity().add(partyLegalEntityType);
	}

	private static JAXBContext getJaxbContext() throws JAXBException {
		if (jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(InvoiceType.class);
		}
		return jaxbContext;
	}

	public static <T> T deserializeToType(byte[] clearedInvoice, Class<T> clazz) throws Exception {
		Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
		return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(clearedInvoice)), clazz).getValue();
	}



	private static void addB2CCustomerParty(InvoiceType invoice, ResultSet rs) throws Exception {
		CustomerPartyType customerPartyType = new CustomerPartyType();
		PartyType partyType = new PartyType();

		addCustomerLegalEntity(partyType, rs);

		customerPartyType.setParty(partyType);
		invoice.setAccountingCustomerParty(customerPartyType);
	}

	private static void addPartyIdentification(PartyType partyType, String crn) {
		PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
		IDType partyIDIdType = new IDType();
		partyIDIdType.setSchemeID("CRN");
		partyIDIdType.setValue(crn);
		partyIdentificationType.setID(partyIDIdType);
		
		partyType.getPartyIdentification().add(partyIdentificationType);
	}

	public static InvoiceType createEComInvoice(ResultSet rs) throws Exception {
		InvoiceType invoice = new InvoiceType();
		invoice.setProfileID(PROFILE_ID_TYPE);

		IDType idType = new IDType();
		idType.setValue(rs.getString("ORDER_NO"));
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue(UUID.randomUUID().toString());
		invoice.setUUID(uuidType);
	
		Date orderDateTime = rs.getTimestamp("ORDER_DATE");
		XMLGregorianCalendar gregFmt = null;
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(orderDateTime));
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format(orderDateTime));
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		String invType = rs.getString("ORDER_TYPE");
		invoiceTypeCodeType.setName("0200000"); /* Simplified Invoice */
		
		PaymentMeansType paymentMeansType = new PaymentMeansType();
		PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
		paymentMeansCodeType.setValue("1");
		paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
		invoice.getPaymentMeans().add(paymentMeansType);

		if ("SALE".equals(invType)) {
			invoiceTypeCodeType.setValue("388");
		} else {
			invoiceTypeCodeType.setValue("381");
			BillingReferenceType billingReferenceType = new BillingReferenceType();

			DocumentReferenceType documentReferenceType = new DocumentReferenceType();
			IDType invOrgReg = new IDType();
			invOrgReg.setValue(rs.getString("ORIGINAL_ORDER"));
			documentReferenceType.setID(invOrgReg);
			billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
			invoice.getBillingReference().add(billingReferenceType);

			
			InstructionNoteType instructionNoteType = new InstructionNoteType();
			instructionNoteType.setValue("REASON");
			paymentMeansType.getInstructionNote().add(instructionNoteType);
		}
		invoice.setInvoiceTypeCode(invoiceTypeCodeType);

		String currencyCode = "SAR";
		DocumentCurrencyCodeType documentCurrencyCodeType = new DocumentCurrencyCodeType();
		documentCurrencyCodeType.setValue(currencyCode);
		invoice.setDocumentCurrencyCode(documentCurrencyCodeType);

		TaxCurrencyCodeType taxCurrencyCodeType = new TaxCurrencyCodeType();
		taxCurrencyCodeType.setValue(currencyCode);
		invoice.setTaxCurrencyCode(taxCurrencyCodeType);

		LineCountNumericType lineCountNumericType = new LineCountNumericType();
		lineCountNumericType.setValue(BigDecimal.ONE);
		invoice.setLineCountNumeric(lineCountNumericType);

		addICVDocumentRef(invoice);

		addPIHDocumentRef(invoice);

		addPartyIdentification(invoice.getAccountingSupplierParty().getParty(), "344233");

		addB2CCustomerParty(invoice, rs);
		
		return invoice;
	}

}
