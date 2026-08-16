package com.extra.einvoicing.ebs;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.etsi.uri._01903.v1_3.CertIDListType;
import org.etsi.uri._01903.v1_3.CertIDType;
import org.etsi.uri._01903.v1_3.DigestAlgAndValueType;
import org.etsi.uri._01903.v1_3.QualifyingPropertiesType;
import org.etsi.uri._01903.v1_3.SignedPropertiesType;
import org.etsi.uri._01903.v1_3.SignedSignaturePropertiesType;
import org.w3._2000._09.xmldsig.CanonicalizationMethodType;
import org.w3._2000._09.xmldsig.DigestMethodType;
import org.w3._2000._09.xmldsig.KeyInfoType;
import org.w3._2000._09.xmldsig.ObjectType;
import org.w3._2000._09.xmldsig.ReferenceType;
import org.w3._2000._09.xmldsig.SignatureMethodType;
import org.w3._2000._09.xmldsig.SignatureType;
import org.w3._2000._09.xmldsig.SignatureValueType;
import org.w3._2000._09.xmldsig.SignedInfoType;
import org.w3._2000._09.xmldsig.TransformType;
import org.w3._2000._09.xmldsig.TransformsType;
import org.w3._2000._09.xmldsig.X509DataType;
import org.w3._2000._09.xmldsig.X509IssuerSerialType;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DeliveryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyTaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentMeansType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ActualDeliveryDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BuildingNumberType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompanyIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CountrySubentityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EmbeddedDocumentBinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineCountNumericType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentMeansCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PlotIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PostalZoneType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ProfileIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UUIDType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionURIType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.commonsignaturecomponents_2.UBLDocumentSignaturesType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;
import oasis.names.specification.ubl.schema.xsd.signatureaggregatecomponents_2.SignatureInformationType;
import oasis.names.specification.ubl.schema.xsd.signaturebasiccomponents_2.ReferencedSignatureIDType;

/**
 * @author aibrahim
 *
 */
public class ReGenerateXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		InvoiceType invoice = new InvoiceType();
		JAXBElement<InvoiceType> elem = new ObjectFactory().createInvoice(invoice);

		createSimplifiedInvoice(invoice);

		// UBLE
		UBLExtensionsType extensionsType = new UBLExtensionsType();
		UBLExtensionType extensionType = new UBLExtensionType();

		ExtensionURIType extensionURIType = new ExtensionURIType();
		extensionURIType.setValue("urn:oasis:names:specification:ubl:dsig:enveloped:xades");
		extensionType.setExtensionURI(extensionURIType);

		ExtensionContentType contentType = new ExtensionContentType();
		contentType.setAny(new oasis.names.specification.ubl.schema.xsd.commonsignaturecomponents_2.ObjectFactory().createUBLDocumentSignatures(createUBLDocumentSignature()));
		extensionType.setExtensionContent(contentType);

		extensionsType.getUBLExtension().add(extensionType);
		invoice.setUBLExtensions(extensionsType);

		JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceType.class, UBLDocumentSignaturesType.class, QualifyingPropertiesType.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(elem, stringWriter);
		String xml = stringWriter.toString();
		System.out.print(xml);

		Files.writeString(Paths.get("D:\\Projects\\eXtra\\oms\\oms\\CODE\\XsdToJava\\src\\test\\resources\\files\\simple-regen.xml"), xml);
	}

	private static void createSimplifiedInvoice(InvoiceType invoice) throws Exception {
		ProfileIDType profileIDType = new ProfileIDType();
		profileIDType.setValue("reporting:1.0");
		invoice.setProfileID(profileIDType);

		IDType idType = new IDType();
		idType.setValue("100");
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue("522ae3e1-9e9e-48b3-99ee-11a97912d036");
		invoice.setUUID(uuidType);

		XMLGregorianCalendar gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-10-06");
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar("12:21:49");
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		invoiceTypeCodeType.setName("0200000");
		invoiceTypeCodeType.setValue("388");
		invoice.setInvoiceTypeCode(invoiceTypeCodeType);

		DocumentCurrencyCodeType documentCurrencyCodeType = new DocumentCurrencyCodeType();
		documentCurrencyCodeType.setValue("SAR");
		invoice.setDocumentCurrencyCode(documentCurrencyCodeType);

		TaxCurrencyCodeType taxCurrencyCodeType = new TaxCurrencyCodeType();
		taxCurrencyCodeType.setValue("SAR");
		invoice.setTaxCurrencyCode(taxCurrencyCodeType);

		LineCountNumericType lineCountNumericType = new LineCountNumericType();
		lineCountNumericType.setValue(BigDecimal.ONE);
		invoice.setLineCountNumeric(lineCountNumericType);

		addICVDocumentRef(invoice);

		addPIHDocumentRef(invoice);

		addSuplierParty(invoice);

		// addCustomerParty(invoice);

		DeliveryType deliveryType = new DeliveryType();
		ActualDeliveryDateType actualDeliveryDateType = new ActualDeliveryDateType();
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		actualDeliveryDateType.setValue(gregFmt);
		deliveryType.setActualDeliveryDate(actualDeliveryDateType);
		invoice.getDelivery().add(deliveryType);

		PaymentMeansType paymentMeansType = new PaymentMeansType();
		PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
		paymentMeansCodeType.setValue("42");
		paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
		invoice.getPaymentMeans().add(paymentMeansType);

		addTaxTotalWithSubTotal(invoice);

		addTaxTotal(invoice);

		addLegalMonetryTotal(invoice);

		addInvoiceLineItem(invoice);
	}

	protected static void addBillingReference(InvoiceType invoice) {

		BillingReferenceType billingReferenceType = new BillingReferenceType();

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();
		IDType idType = new IDType();
		idType.setValue("Invoice Number: 348; Invoice Issue Date: 2022-11-04");
		documentReferenceType.setID(idType);
		billingReferenceType.setInvoiceDocumentReference(documentReferenceType);

		invoice.getBillingReference().add(billingReferenceType);
	}

	private static void addInvoiceLineItem(InvoiceType invoice) {

		InvoiceLineType invoiceLineType = new InvoiceLineType();

		IDType idType = new IDType();
		idType.setValue(BigDecimal.ONE.toString());
		invoiceLineType.setID(idType);

		InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
		invoicedQuantityType.setUnitCode("EA");
		invoicedQuantityType.setValue(BigDecimal.ONE);
		invoiceLineType.setInvoicedQuantity(invoicedQuantityType);

		LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
		lineExtensionAmountType.setCurrencyID("SAR");
		lineExtensionAmountType.setValue(BigDecimal.valueOf(100));
		invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

		TaxTotalType taxTotalType = new TaxTotalType();

		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID("SAR");
		taxAmountType.setValue(BigDecimal.valueOf(15));
		taxTotalType.setTaxAmount(taxAmountType);

		RoundingAmountType roundingAmountType = new RoundingAmountType();
		roundingAmountType.setCurrencyID("SAR");
		roundingAmountType.setValue(BigDecimal.valueOf(115));
		taxTotalType.setRoundingAmount(roundingAmountType);

		invoiceLineType.getTaxTotal().add(taxTotalType);

		ItemType itemType = new ItemType();

		NameType nameType = new NameType();
		nameType.setValue("iPhone 13X");
		itemType.setName(nameType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();

		IDType classIdType = new IDType();
		classIdType.setValue("S");
		taxCategoryType.setID(classIdType);

		PercentType percentType = new PercentType();
		percentType.setValue(BigDecimal.valueOf(15));
		taxCategoryType.setPercent(percentType);

		TaxSchemeType taxSchemeType = new TaxSchemeType();
		idType = new IDType();
		idType.setValue("VAT");
		taxSchemeType.setID(idType);
		taxCategoryType.setTaxScheme(taxSchemeType);

		itemType.getClassifiedTaxCategory().add(taxCategoryType);

		invoiceLineType.setItem(itemType);

		PriceType priceType = new PriceType();
		PriceAmountType priceAmountType = new PriceAmountType();
		priceAmountType.setCurrencyID("SAR");
		priceAmountType.setValue(BigDecimal.valueOf(100));
		priceType.setPriceAmount(priceAmountType);
		invoiceLineType.setPrice(priceType);

		invoice.getInvoiceLine().add(invoiceLineType);
	}

	private static void addLegalMonetryTotal(InvoiceType invoice) {

		MonetaryTotalType monetaryTotalType = new MonetaryTotalType();

		LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
		lineExtensionAmountType.setCurrencyID("SAR");
		lineExtensionAmountType.setValue(BigDecimal.valueOf(100));
		monetaryTotalType.setLineExtensionAmount(lineExtensionAmountType);

		TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType();
		taxExclusiveAmountType.setCurrencyID("SAR");
		taxExclusiveAmountType.setValue(BigDecimal.valueOf(100));
		monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountType);

		TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
		taxInclusiveAmountType.setCurrencyID("SAR");
		taxInclusiveAmountType.setValue(BigDecimal.valueOf(115));
		monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountType);

		PayableAmountType payableAmountType = new PayableAmountType();
		payableAmountType.setCurrencyID("SAR");
		payableAmountType.setValue(BigDecimal.valueOf(115));
		monetaryTotalType.setPayableAmount(payableAmountType);

		invoice.setLegalMonetaryTotal(monetaryTotalType);
	}

	private static void addTaxTotal(InvoiceType invoice) {
		TaxTotalType taxTotalType = new TaxTotalType();

		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID("SAR");
		taxAmountType.setValue(BigDecimal.valueOf(115));
		taxTotalType.setTaxAmount(taxAmountType);

		invoice.getTaxTotal().add(taxTotalType);
	}

	private static void addTaxTotalWithSubTotal(InvoiceType invoice) {
		TaxTotalType taxTotalType = new TaxTotalType();

		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID("SAR");
		taxAmountType.setValue(BigDecimal.valueOf(15));
		taxTotalType.setTaxAmount(taxAmountType);

		TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
		taxSubtotalType.setTaxAmount(taxAmountType);

		TaxableAmountType taxableAmountType = new TaxableAmountType();
		taxableAmountType.setCurrencyID("SAR");
		taxableAmountType.setValue(BigDecimal.valueOf(100));
		taxSubtotalType.setTaxableAmount(taxableAmountType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();

		IDType idType = new IDType();
		idType.setValue("S");
		taxCategoryType.setID(idType);

		PercentType percentType = new PercentType();
		percentType.setValue(BigDecimal.valueOf(15));
		taxCategoryType.setPercent(percentType);

		TaxSchemeType taxSchemeType = new TaxSchemeType();
		idType = new IDType();
		idType.setValue("VAT");
		taxSchemeType.setID(idType);
		taxCategoryType.setTaxScheme(taxSchemeType);

		taxSubtotalType.setTaxCategory(taxCategoryType);

		taxTotalType.getTaxSubtotal().add(taxSubtotalType);

		invoice.getTaxTotal().add(taxTotalType);
	}

	private static void addSuplierParty(InvoiceType invoice) {
		SupplierPartyType supplierPartyType = new SupplierPartyType();

		PartyType partyType = new PartyType();

		addSuplierPartyIdentification(partyType);

		addSuplierPostalAddress(partyType);

		supplierPartyType.setParty(partyType);
		invoice.setAccountingSupplierParty(supplierPartyType);
	}

	private static void addSuplierPostalAddress(PartyType partyType) {
		AddressType addressType = new AddressType();

		StreetNameType streetNameType = new StreetNameType();
		streetNameType.setValue("King Abdulaziz Road");
		addressType.setStreetName(streetNameType);

		BuildingNumberType buildingNumberType = new BuildingNumberType();
		buildingNumberType.setValue("8228");
		addressType.setBuildingNumber(buildingNumberType);

		PlotIdentificationType plotIdentificationType = new PlotIdentificationType();
		plotIdentificationType.setValue("2121");
		addressType.setPlotIdentification(plotIdentificationType);

		CitySubdivisionNameType citySubdivisionNameType = new CitySubdivisionNameType();
		citySubdivisionNameType.setValue("Al Amal");
		addressType.setCitySubdivisionName(citySubdivisionNameType);

		CityNameType cityNameType = new CityNameType();
		cityNameType.setValue("Riyadh");
		addressType.setCityName(cityNameType);

		PostalZoneType postalZoneType = new PostalZoneType();
		postalZoneType.setValue("12643");
		addressType.setPostalZone(postalZoneType);

		CountrySubentityType countrySubentityType = new CountrySubentityType();
		countrySubentityType.setValue("Riyadh Region");

		CountryType countryType = new CountryType();
		IdentificationCodeType identificationCodeType = new IdentificationCodeType();
		identificationCodeType.setValue("SA");
		countryType.setIdentificationCode(identificationCodeType);
		addressType.setCountry(countryType);

		partyType.setPostalAddress(addressType);

		PartyTaxSchemeType partyTaxSchemeType = new PartyTaxSchemeType();

		CompanyIDType companyIDType = new CompanyIDType();
		companyIDType.setValue("310175397400003");
		partyTaxSchemeType.setCompanyID(companyIDType);

		TaxSchemeType taxSchemeType = new TaxSchemeType();

		IDType idType = new IDType();
		idType.setValue("VAT");
		taxSchemeType.setID(idType);
		partyTaxSchemeType.setTaxScheme(taxSchemeType);

		PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
		RegistrationNameType registrationNameType = new RegistrationNameType();
		registrationNameType.setValue("United");
		partyLegalEntityType.setRegistrationName(registrationNameType);

		partyType.getPartyLegalEntity().add(partyLegalEntityType);

		partyType.getPartyTaxScheme().add(partyTaxSchemeType);
	}

	private static void addSuplierPartyIdentification(PartyType partyType) {
		PartyIdentificationType partyIdentificationType = new PartyIdentificationType();

		IDType idType = new IDType();
		idType.setSchemeID("MLS");
		idType.setValue("123457890");

		partyIdentificationType.setID(idType);
		partyType.getPartyIdentification().add(partyIdentificationType);
	}

	private static void addPIHDocumentRef(InvoiceType invoice) throws Exception {
		DocumentReferenceType documentReferenceType = new DocumentReferenceType();

		IDType idType = new IDType();
		idType.setValue("PIH");
		documentReferenceType.setID(idType);

		AttachmentType attachmentType = new AttachmentType();

		EmbeddedDocumentBinaryObjectType embeddedDocumentBinaryObjectType = new EmbeddedDocumentBinaryObjectType();
		embeddedDocumentBinaryObjectType.setMimeCode("text/plain");
		byte[] bytes = Base64.getDecoder().decode("NWZlY2ViNjZmZmM4NmYzOGQ5NTI3ODZjNmQ2OTZjNzljMmRiYzIzOWRkNGU5MWI0NjcyOWQ3M2EyN2ZiNTdlOQ==");
		embeddedDocumentBinaryObjectType.setValue(bytes);

		attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocumentBinaryObjectType);
		documentReferenceType.setAttachment(attachmentType);

		invoice.getAdditionalDocumentReference().add(documentReferenceType);
	}

	private static void addICVDocumentRef(InvoiceType invoice) {

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();

		IDType idType = new IDType();
		idType.setValue("ICV");
		documentReferenceType.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue("2");
		documentReferenceType.setUUID(uuidType);

		invoice.getAdditionalDocumentReference().add(documentReferenceType);
	}

	private static QualifyingPropertiesType createQualifyingProperty() throws Exception {
		QualifyingPropertiesType qualifyingPropertiesType = new QualifyingPropertiesType();

		qualifyingPropertiesType.setTarget("signature");

		SignedPropertiesType signedPropertiesType = new SignedPropertiesType();
		signedPropertiesType.setId("xadesSignedProperties");

		SignedSignaturePropertiesType signedSignaturePropertiesType = new SignedSignaturePropertiesType();

		signedSignaturePropertiesType.setSigningTime(null);

		CertIDListType certIDListType = new CertIDListType();

		CertIDType certIDType = new CertIDType();

		DigestAlgAndValueType digestAlgAndValueType = new DigestAlgAndValueType();

		DigestMethodType digestMethodType = new DigestMethodType();
		digestMethodType.setAlgorithm("http://www.w3.org/2001/04/xmlenc#sha256");
		digestAlgAndValueType.setDigestMethod(digestMethodType);

		digestAlgAndValueType.setDigestValue(Base64.getDecoder().decode("YTJkM2JhYTcwZTBhZTAxOGYwODMyNzY3NTdkZDM3YzhjY2IxOTIyZDZhM2RlZGJiMGY0NDUzZWJhYWI4MDhmYg=="));

		certIDType.setCertDigest(digestAlgAndValueType);

		X509IssuerSerialType x509IssuerSerialType = new X509IssuerSerialType();
		x509IssuerSerialType.setX509IssuerName("CN=TSZEINVOICE-SubCA-1, DC=extgazt, DC=gov, DC=local");
		x509IssuerSerialType.setX509SerialNumber(new BigInteger("2475382886904809774818644480820936050208702411"));
		certIDType.setIssuerSerial(x509IssuerSerialType);

		certIDListType.getCert().add(certIDType);

		signedSignaturePropertiesType.setSigningCertificate(certIDListType);
		
		
		XMLGregorianCalendar gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-10-06T12:21:53Z");
		signedSignaturePropertiesType.setSigningTime(gregFmt);

		signedPropertiesType.setSignedSignatureProperties(signedSignaturePropertiesType);
		qualifyingPropertiesType.setSignedProperties(signedPropertiesType);

		return qualifyingPropertiesType;
	}

	private static void createUBLExtensions(InvoiceType invoice) {
		UBLExtensionsType extensionsType = new UBLExtensionsType();

		UBLExtensionType extensionType = new UBLExtensionType();

		ExtensionURIType extensionURIType = new ExtensionURIType();
		extensionURIType.setValue("urn:oasis:names:specification:ubl:dsig:enveloped:xades");
		extensionType.setExtensionURI(extensionURIType);

		ExtensionContentType contentType = new ExtensionContentType();

		extensionType.setExtensionContent(contentType);

		extensionsType.getUBLExtension().add(extensionType);
		invoice.setUBLExtensions(extensionsType);
	}

	private static UBLDocumentSignaturesType createUBLDocumentSignature() throws Exception {
		UBLDocumentSignaturesType documentSignaturesType = new UBLDocumentSignaturesType();

		SignatureInformationType signatureInformationType = new SignatureInformationType();

		IDType idType = new IDType();
		idType.setValue("urn:oasis:names:specification:ubl:signature:1");
		signatureInformationType.setID(idType);

		ReferencedSignatureIDType referencedSignatureIDType = new ReferencedSignatureIDType();
		referencedSignatureIDType.setValue("urn:oasis:names:specification:ubl:signature:Invoice");
		signatureInformationType.setReferencedSignatureID(referencedSignatureIDType);

		SignatureType signatureType = new SignatureType();
		signatureType.setId("signature");

		SignedInfoType signedInfoType = new SignedInfoType();

		CanonicalizationMethodType canonicalizationMethodType = new CanonicalizationMethodType();
		canonicalizationMethodType.setAlgorithm("http://www.w3.org/2006/12/xml-c14n11");
		signedInfoType.setCanonicalizationMethod(canonicalizationMethodType);

		SignatureMethodType signatureMethodType = new SignatureMethodType();
		signatureMethodType.setAlgorithm("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256");
		signedInfoType.setSignatureMethod(signatureMethodType);

		ReferenceType referenceType = new ReferenceType();

		referenceType.setId("invoiceSignedData");

		referenceType.setURI("");

		referenceType.setTransforms(new TransformsType());

		TransformType transformType = new TransformType();
		transformType.setAlgorithm("http://www.w3.org/TR/1999/REC-xpath-19991116");
		transformType.getContent().add(new org.w3._2000._09.xmldsig.ObjectFactory().createTransformTypeXPath("not(//ancestor-or-self::ext:UBLExtensions)"));
		referenceType.getTransforms().getTransform().add(transformType);

		transformType = new TransformType();
		transformType.setAlgorithm("http://www.w3.org/TR/1999/REC-xpath-19991116");
		transformType.getContent().add(new org.w3._2000._09.xmldsig.ObjectFactory().createTransformTypeXPath("not(//ancestor-or-self::cac:Signature)"));
		referenceType.getTransforms().getTransform().add(transformType);

		transformType = new TransformType();
		transformType.setAlgorithm("http://www.w3.org/TR/1999/REC-xpath-19991116");
		transformType.getContent().add(new org.w3._2000._09.xmldsig.ObjectFactory().createTransformTypeXPath("not(//ancestor-or-self::cac:AdditionalDocumentReference[cbc:ID='QR'])"));
		referenceType.getTransforms().getTransform().add(transformType);

		transformType = new TransformType();
		transformType.setAlgorithm("http://www.w3.org/2006/12/xml-c14n11");
		referenceType.getTransforms().getTransform().add(transformType);

		DigestMethodType digestMethodType = new DigestMethodType();
		digestMethodType.setAlgorithm("http://www.w3.org/2001/04/xmlenc#sha256");
		referenceType.setDigestMethod(digestMethodType);

		referenceType.setDigestValue(Base64.getDecoder().decode("XMAMg3BbWK50RgCgFXieD35XVi6qywusiSRXscrFedk="));

		signedInfoType.getReference().add(referenceType);

		referenceType = new ReferenceType();
		referenceType.setType("http://www.w3.org/2000/09/xmldsig#SignatureProperties");
		referenceType.setURI("#xadesSignedProperties");
		referenceType.setDigestMethod(digestMethodType);
		referenceType.setDigestValue(Base64.getDecoder().decode("MTI1M2Y0NDBiZDRjNThiMmE1YTA4ODkxMDU5YmM2NmE4OTdjMjgyOTYwNTEyNjA2ODkzNDkzMmY4MDg3MTViMQ=="));

		signedInfoType.getReference().add(referenceType);

		signatureType.setSignedInfo(signedInfoType);

		SignatureValueType signatureValueType = new SignatureValueType();
		signatureValueType.setValue(Base64.getDecoder().decode("MEYCIQDfzcgpI9ZoHHwLd+ANWY1DUUxIwFsIe+j+V4DvXoqGnwIhALQ1A5j13MOUhX6sCvfEFsLtV7AkFCBL6+YclPTapmt9"));
		signatureType.setSignatureValue(signatureValueType);

		KeyInfoType keyInfoType = new KeyInfoType();

		X509DataType x509DataType = new X509DataType();

		x509DataType.getX509IssuerSerialOrX509SKIOrX509SubjectName().add(new org.w3._2000._09.xmldsig.ObjectFactory().createX509DataTypeX509Certificate(Base64.getDecoder().decode("MIID6TCCA5CgAwIBAgITbwAAf8tem6jngr16DwABAAB/yzAKBggqhkjOPQQDAjBjMRUwEwYKCZImiZPyLGQBGRYFbG9jYWwxEzARBgoJkiaJk/IsZAEZFgNnb3YxFzAVBgoJkiaJk/IsZAEZFgdleHRnYXp0MRwwGgYDVQQDExNUU1pFSU5WT0lDRS1TdWJDQS0xMB4XDTIyMDkxNDEzMjYwNFoXDTI0MDkxMzEzMjYwNFowTjELMAkGA1UEBhMCU0ExEzARBgNVBAoTCjMxMTExMTExMTExDDAKBgNVBAsTA1RTVDEcMBoGA1UEAxMTVFNULTMxMTExMTExMTEwMTExMzBWMBAGByqGSM49AgEGBSuBBAAKA0IABGGDDKDmhWAITDv7LXqLX2cmr6+qddUkpcLCvWs5rC2O29W/hS4ajAK4Qdnahym6MaijX75Cg3j4aao7ouYXJ9GjggI5MIICNTCBmgYDVR0RBIGSMIGPpIGMMIGJMTswOQYDVQQEDDIxLVRTVHwyLVRTVHwzLWE4NjZiMTQyLWFjOWMtNDI0MS1iZjhlLTdmNzg3YTI2MmNlMjEfMB0GCgmSJomT8ixkAQEMDzMxMTExMTExMTEwMTExMzENMAsGA1UEDAwEMTEwMDEMMAoGA1UEGgwDVFNUMQwwCgYDVQQPDANUU1QwHQYDVR0OBBYEFDuWYlOzWpFN3no1WtyNktQdrA8JMB8GA1UdIwQYMBaAFHZgjPsGoKxnVzWdz5qspyuZNbUvME4GA1UdHwRHMEUwQ6BBoD+GPWh0dHA6Ly90c3RjcmwuemF0Y2EuZ292LnNhL0NlcnRFbnJvbGwvVFNaRUlOVk9JQ0UtU3ViQ0EtMS5jcmwwga0GCCsGAQUFBwEBBIGgMIGdMG4GCCsGAQUFBzABhmJodHRwOi8vdHN0Y3JsLnphdGNhLmdvdi5zYS9DZXJ0RW5yb2xsL1RTWkVpbnZvaWNlU0NBMS5leHRnYXp0Lmdvdi5sb2NhbF9UU1pFSU5WT0lDRS1TdWJDQS0xKDEpLmNydDArBggrBgEFBQcwAYYfaHR0cDovL3RzdGNybC56YXRjYS5nb3Yuc2Evb2NzcDAOBgNVHQ8BAf8EBAMCB4AwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMDMCcGCSsGAQQBgjcVCgQaMBgwCgYIKwYBBQUHAwIwCgYIKwYBBQUHAwMwCgYIKoZIzj0EAwIDRwAwRAIgOgjNPJW017lsIijmVQVkP7GzFO2KQKd9GHaukLgIWFsCIFJF9uwKhTMxDjWbN+1awsnFI7RLBRxA/6hZ+F1wtaqU")));

		keyInfoType.getContent().add(new org.w3._2000._09.xmldsig.ObjectFactory().createX509Data(x509DataType));
		signatureType.setKeyInfo(keyInfoType);

		ObjectType objectType = new ObjectType();
		objectType.getContent().add(new org.etsi.uri._01903.v1_3.ObjectFactory().createQualifyingProperties(createQualifyingProperty()));
		
		
		signatureType.getObject().add(objectType);

		signatureInformationType.setSignature(signatureType);

		documentSignaturesType.getSignatureInformation().add(signatureInformationType);

		return documentSignaturesType;
	}
}
