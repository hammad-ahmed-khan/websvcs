package com.extra.einvoicing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;

import com.extra.einvoicing.config.ApplicationProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ItemIdentificationType;
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
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AllowanceChargeReasonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BuildingNumberType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ChargeIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CitySubdivisionNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompanyIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EmbeddedDocumentBinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InstructionNoteType;
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
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RegistrationNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UUIDType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

/**
 * @author aibrahim
 *
 */
public class InvoiceUtil {

	private static ApplicationProperty APPLICATION_PROPERTY;

	private static JAXBContext jaxbContext;

	static {
		try {
			APPLICATION_PROPERTY = ApplicationSpringContext.getBean(ApplicationProperty.class);
		} catch (Exception w) {
		}

	}

	public static InvoiceType createEBSInvoice(ResultSet rs) throws Exception {
		InvoiceType invoice = new InvoiceType();
		invoice.setProfileID(InvoiceConstant.getProfileType());

		IDType idType = new IDType();
		idType.setValue(rs.getString("INVOICE_NUM"));
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue(UUID.randomUUID().toString());
		invoice.setUUID(uuidType);

		XMLGregorianCalendar gregFmt = null;
		Date invoiceDate = rs.getDate("INVOICE_DATE");
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(invoiceDate));
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format(invoiceDate));
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		String invType = rs.getString("INVOICE_TYPE_LOOKUP_CODE");
		invoiceTypeCodeType.setName("0100000"); /* Standard Tax Invoice */
		String type = rs.getString("TYPE");
		if ((("AR".equals(type) || "PA".equals(type)) && "STANDARD".equals(invType)) || invType.equals("DEBIT")) {
			invoiceTypeCodeType.setValue("388");
		} else {
			invoiceTypeCodeType.setValue("381");

			BillingReferenceType billingReferenceType = new BillingReferenceType();

			DocumentReferenceType documentReferenceType = new DocumentReferenceType();
			IDType invOrgReg = new IDType();
			invOrgReg.setValue(rs.getString("ORIGINAL_INVOICE"));
			documentReferenceType.setID(invOrgReg);
			billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
			invoice.getBillingReference().add(billingReferenceType);

			PaymentMeansType paymentMeansType = new PaymentMeansType();
			InstructionNoteType instructionNoteType = new InstructionNoteType();
			instructionNoteType.setValue("Customer canncelled the items");
			paymentMeansType.getInstructionNote().add(instructionNoteType);

			PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
			paymentMeansCodeType.setValue("1");
			paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
			invoice.getPaymentMeans().add(paymentMeansType);
		}
		invoice.setInvoiceTypeCode(invoiceTypeCodeType);

		String currencyCode = rs.getString("INVOICE_CURRENCY_CODE");
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

		addSuplierParty(invoice, rs);

		addPartyIdentification(invoice.getAccountingSupplierParty().getParty(), APPLICATION_PROPERTY.getCommercialRegMap().get(rs.getString("OU")));

		addCustomerParty(invoice, rs);

		return invoice;
	}

	public static void createEBSLineItem(InvoiceType invoice, ResultSet rs) throws Exception {

		InvoiceLineType invoiceLineType = new InvoiceLineType();

		String currencyCode = rs.getString("INVOICE_CURRENCY_CODE");
		BigDecimal qty = rs.getBigDecimal("QTY");
		BigDecimal unitPrice = rs.getBigDecimal("UNIT_PRICE");
		if (unitPrice.signum() < 0) {
			unitPrice = unitPrice.negate();
		}
		BigDecimal taxAmt = rs.getBigDecimal("TAX_AMOUNT");
		if (taxAmt == null) {
			taxAmt = BigDecimal.ZERO;
		} else if (taxAmt.signum() < 0) {
			taxAmt = taxAmt.negate();
		}
		BigDecimal taxRate = rs.getBigDecimal("TAX_RATE");
		BigDecimal taxExcluAmt = rs.getBigDecimal("TOTAL_EXCL_VAT");
		if (taxExcluAmt == null) {
			taxExcluAmt = BigDecimal.ZERO;
		} else if (taxExcluAmt.signum() < 0) {
			taxExcluAmt = taxExcluAmt.negate();
		}

		IDType idType = new IDType();
		idType.setValue(rs.getString("INVOICE_LINE_NUMBER"));
		invoiceLineType.setID(idType);

		InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
		invoicedQuantityType.setUnitCode("EA");
		invoicedQuantityType.setValue(qty);
		invoiceLineType.setInvoicedQuantity(invoicedQuantityType);

		LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
		lineExtensionAmountType.setCurrencyID(currencyCode);
		lineExtensionAmountType.setValue(qty.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP));
		invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

		TaxTotalType taxTotalType = new TaxTotalType();

		BigDecimal roundedTaxAmt = taxAmt.setScale(2, RoundingMode.HALF_UP);
		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID(currencyCode);
		taxAmountType.setValue(roundedTaxAmt);
		taxTotalType.setTaxAmount(taxAmountType);

		RoundingAmountType roundingAmountType = new RoundingAmountType();
		roundingAmountType.setCurrencyID(currencyCode);
		roundingAmountType.setValue(lineExtensionAmountType.getValue().add(taxAmountType.getValue()));
		taxTotalType.setRoundingAmount(roundingAmountType);

		invoiceLineType.getTaxTotal().add(taxTotalType);

		ItemType itemType = new ItemType();

		NameType nameType = new NameType();
		nameType.setValue(rs.getString("ITEM_DESC"));
		itemType.setName(nameType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();

		IDType classIdType = new IDType();
		classIdType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
		taxCategoryType.setID(classIdType);

		PercentType percentType = new PercentType();
		percentType.setValue(taxRate != null ? taxRate : BigDecimal.ZERO);
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
		priceAmountType.setCurrencyID(currencyCode);
		priceAmountType.setValue(unitPrice.setScale(2, RoundingMode.HALF_UP));
		priceType.setPriceAmount(priceAmountType);
		invoiceLineType.setPrice(priceType);

		invoice.getInvoiceLine().add(invoiceLineType);

		MonetaryTotalType monetaryTotalType = invoice.getLegalMonetaryTotal();
		if (monetaryTotalType == null) {
			monetaryTotalType = new MonetaryTotalType();
			invoice.setLegalMonetaryTotal(monetaryTotalType);

			LineExtensionAmountType mainLineExtensionAmountType = new LineExtensionAmountType();
			mainLineExtensionAmountType.setCurrencyID(currencyCode);
			mainLineExtensionAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setLineExtensionAmount(mainLineExtensionAmountType);

			TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType();
			taxExclusiveAmountType.setCurrencyID(currencyCode);
			taxExclusiveAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountType);

			TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
			taxInclusiveAmountType.setCurrencyID(currencyCode);
			taxInclusiveAmountType.setValue(roundingAmountType.getValue());
			monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountType);

			PayableAmountType payableAmountType = new PayableAmountType();
			payableAmountType.setCurrencyID(currencyCode);
			payableAmountType.setValue(taxInclusiveAmountType.getValue());
			monetaryTotalType.setPayableAmount(payableAmountType);

			/* Tax Total */
			TaxTotalType mainTaxTotalType = new TaxTotalType();

			TaxAmountType mainTaxAmountType = new TaxAmountType();
			mainTaxAmountType.setCurrencyID(currencyCode);
			mainTaxAmountType.setValue(taxAmountType.getValue());
			mainTaxTotalType.setTaxAmount(mainTaxAmountType);

			invoice.getTaxTotal().add(mainTaxTotalType);

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = new TaxTotalType();

			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			subTaxTotalType.setTaxAmount(taxAmountType);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			taxSubtotalType.setTaxAmount(taxAmountType);

			TaxableAmountType taxableAmountType = new TaxableAmountType();
			taxableAmountType.setCurrencyID(currencyCode);
			taxableAmountType.setValue(lineExtensionAmountType.getValue());
			taxSubtotalType.setTaxableAmount(taxableAmountType);

			taxCategoryType = new TaxCategoryType();

			idType = new IDType();
			idType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
			taxCategoryType.setID(idType);

			percentType = new PercentType();
			percentType.setValue(taxRate != null ? taxRate : BigDecimal.ZERO);
			taxCategoryType.setPercent(percentType);

			taxSchemeType = new TaxSchemeType();
			idType = new IDType();
			idType.setValue("VAT");
			taxSchemeType.setID(idType);
			taxCategoryType.setTaxScheme(taxSchemeType);

			taxSubtotalType.setTaxCategory(taxCategoryType);

			subTaxTotalType.getTaxSubtotal().add(taxSubtotalType);

			invoice.getTaxTotal().add(subTaxTotalType);
		} else {
			monetaryTotalType.getLineExtensionAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getLineExtensionAmount().getValue()));
			monetaryTotalType.getTaxExclusiveAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getTaxExclusiveAmount().getValue()));
			monetaryTotalType.getTaxInclusiveAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getTaxInclusiveAmount().getValue()));
			monetaryTotalType.getPayableAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getPayableAmount().getValue()));

			/* Tax Total */
			TaxTotalType mainTaxTotalType = invoice.getTaxTotal().get(0);
			mainTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(mainTaxTotalType.getTaxAmount().getValue()));

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = invoice.getTaxTotal().get(1);
			subTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(subTaxTotalType.getTaxAmount().getValue()));
			TaxSubtotalType subTotalAmountType = subTaxTotalType.getTaxSubtotal().get(0);
			subTotalAmountType.getTaxableAmount().setValue(lineExtensionAmountType.getValue().add(subTotalAmountType.getTaxableAmount().getValue()));
			subTotalAmountType.getTaxAmount().setValue(taxAmountType.getValue().add(subTotalAmountType.getTaxAmount().getValue()));

			if (subTotalAmountType.getTaxAmount().getValue().intValue() > 0 && (taxRate != null && taxRate.intValue() > 0)) {
				subTotalAmountType.getTaxCategory().getID().setValue("S");
				subTotalAmountType.getTaxCategory().getPercent().setValue(taxRate);
			}
		}
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

	private static SupplierPartyType addSuplierParty() throws Exception {
		SupplierPartyType supplierPartyType = new SupplierPartyType();
		PartyType partyType = new PartyType();

		addSuplierPostalAddress(partyType, null);
		supplierPartyType.setParty(partyType);
		return supplierPartyType;
	}

	private static void addSuplierParty(InvoiceType invoice, ResultSet rs) throws Exception {
		SupplierPartyType supplierPartyType = new SupplierPartyType();
		PartyType partyType = new PartyType();

		addSuplierPostalAddress(partyType, rs);
		supplierPartyType.setParty(partyType);

		invoice.setAccountingSupplierParty(supplierPartyType);
	}

	private static void addSuplierPostalAddress(PartyType partyType, ResultSet rs) throws Exception {
		AddressType addressType = new AddressType();

		StreetNameType streetNameType = new StreetNameType();
		streetNameType.setValue(APPLICATION_PROPERTY.getStreetName());
		addressType.setStreetName(streetNameType);

		BuildingNumberType buildingNumberType = new BuildingNumberType();
		buildingNumberType.setValue(APPLICATION_PROPERTY.getBuildingName());
		addressType.setBuildingNumber(buildingNumberType);

		PlotIdentificationType plotIdentificationType = new PlotIdentificationType();
		plotIdentificationType.setValue(APPLICATION_PROPERTY.getAddtionalStreetNumber());
		addressType.setPlotIdentification(plotIdentificationType);

		CitySubdivisionNameType citySubdivisionNameType = new CitySubdivisionNameType();
		citySubdivisionNameType.setValue(APPLICATION_PROPERTY.getCityDivision());
		addressType.setCitySubdivisionName(citySubdivisionNameType);

		CityNameType cityNameType = new CityNameType();
		cityNameType.setValue(APPLICATION_PROPERTY.getCity());
		addressType.setCityName(cityNameType);

		PostalZoneType postalZoneType = new PostalZoneType();
		postalZoneType.setValue(APPLICATION_PROPERTY.getPostalCode());
		addressType.setPostalZone(postalZoneType);

		CountryType countryType = new CountryType();
		IdentificationCodeType identificationCodeType = new IdentificationCodeType();
		identificationCodeType.setValue(APPLICATION_PROPERTY.getCountry());
		countryType.setIdentificationCode(identificationCodeType);
		addressType.setCountry(countryType);

		partyType.setPostalAddress(addressType);

		PartyTaxSchemeType partyTaxSchemeType = new PartyTaxSchemeType();

		CompanyIDType companyIDType = new CompanyIDType();
		companyIDType.setValue(APPLICATION_PROPERTY.getVatNo());
		partyTaxSchemeType.setCompanyID(companyIDType);

		TaxSchemeType taxSchemeType = new TaxSchemeType();

		IDType idType = new IDType();
		idType.setValue("VAT");
		taxSchemeType.setID(idType);
		partyTaxSchemeType.setTaxScheme(taxSchemeType);

		PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
		RegistrationNameType registrationNameType = new RegistrationNameType();
		registrationNameType.setValue(rs != null ? rs.getString("COMPANY_NAME") : APPLICATION_PROPERTY.getRegisterName());
		partyLegalEntityType.setRegistrationName(registrationNameType);

		partyType.getPartyLegalEntity().add(partyLegalEntityType);

		partyType.getPartyTaxScheme().add(partyTaxSchemeType);
	}

	private static void addCustomerParty(InvoiceType invoice, ResultSet rs) throws Exception {
		CustomerPartyType customerPartyType = new CustomerPartyType();
		PartyType partyType = new PartyType();

		addCustomerPostalAddress(partyType, rs);
		addCustomerLegalEntity(partyType, rs);

		customerPartyType.setParty(partyType);
		invoice.setAccountingCustomerParty(customerPartyType);
	}

	private static void addCustomerPostalAddress(PartyType partyType, ResultSet rs) throws Exception {
		AddressType addressType = new AddressType();

		StreetNameType streetNameType = new StreetNameType();
		streetNameType.setValue(rs.getString("TO_ADDRESS_STREET_NAME"));
		addressType.setStreetName(streetNameType);

		BuildingNumberType buildingNumberType = new BuildingNumberType();
		buildingNumberType.setValue("..");
		addressType.setBuildingNumber(buildingNumberType);

		CitySubdivisionNameType citySubdivisionNameType = new CitySubdivisionNameType();
		citySubdivisionNameType.setValue(rs.getString("TO_ADDRESS_CITY"));
		addressType.setCitySubdivisionName(citySubdivisionNameType);

		CityNameType cityNameType = new CityNameType();
		cityNameType.setValue(rs.getString("TO_ADDRESS_CITY"));
		addressType.setCityName(cityNameType);

		PostalZoneType postalZoneType = new PostalZoneType();
		postalZoneType.setValue("12345");
		addressType.setPostalZone(postalZoneType);

		CountryType countryType = new CountryType();
		IdentificationCodeType identificationCodeType = new IdentificationCodeType();
		identificationCodeType.setValue(rs.getString("TO_ADDRESS_COUNTRY"));
		countryType.setIdentificationCode(identificationCodeType);
		addressType.setCountry(countryType);

		partyType.setPostalAddress(addressType);
	}

	private static void addCustomerLegalEntity(PartyType partyType, ResultSet rs) throws Exception {

		PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
		RegistrationNameType registrationNameType = new RegistrationNameType();
		registrationNameType.setValue(rs.getString("VENDOR_NAME"));
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

	public static String generateQRImage(String qrCode) {
		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(new String(qrCode.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), BarcodeFormat.QR_CODE, 200, 200);
			MatrixToImageWriter.writeToStream(matrix, "png", stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(stream.toByteArray());
	}

	public static InvoiceType createPOSInvoice(ResultSet rs) throws Exception {
		InvoiceType invoice = new InvoiceType();
		invoice.setProfileID(InvoiceConstant.getProfileType());

		IDType idType = new IDType();
		idType.setValue(rs.getString("RECEIPT_NUM"));
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue(UUID.randomUUID().toString());
		invoice.setUUID(uuidType);

		XMLGregorianCalendar gregFmt = null;
		Date businessDate = rs.getDate("BUSINESS_DATE");
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(businessDate));
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format(businessDate));
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		String invType = rs.getString("TRAN_TYPE");
		invoiceTypeCodeType.setName("0100000"); /* Standard Tax Invoice */
		if (invType.equals("SALE")) {
			invoiceTypeCodeType.setValue("388");
		} else {
			invoiceTypeCodeType.setValue("381");
			BillingReferenceType billingReferenceType = new BillingReferenceType();

			DocumentReferenceType documentReferenceType = new DocumentReferenceType();
			IDType invOrgReg = new IDType();
			String origInvNo = rs.getString("ORG_INVOICE_NO");
			if (StringUtils.isBlank(origInvNo)) {
				throw new IllegalArgumentException("Missing original invoice number for the invoice number " + rs.getString("RECEIPT_NUM"));
			}
			invOrgReg.setValue(origInvNo);
			documentReferenceType.setID(invOrgReg);
			billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
			invoice.getBillingReference().add(billingReferenceType);

			PaymentMeansType paymentMeansType = new PaymentMeansType();
			InstructionNoteType instructionNoteType = new InstructionNoteType();
			instructionNoteType.setValue(rs.getString("REASON"));
			paymentMeansType.getInstructionNote().add(instructionNoteType);

			PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
			paymentMeansCodeType.setValue("1");
			paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
			invoice.getPaymentMeans().add(paymentMeansType);
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

		addSuplierParty(invoice, null);
		
		addPartyIdentification(invoice.getAccountingSupplierParty().getParty(), rs.getString("CRN"));

		addCustomerParty(invoice, rs);

		return invoice;
	}

	public static void createPOSLineItem(InvoiceType invoice, ResultSet rs, BigDecimal op) throws Exception {

		String itemSeq = rs.getString("ITEM_SEQ_NO");
		InvoiceLineType invoiceLineType = null;

		BigDecimal discQty = rs.getBigDecimal("DISC_QTY");
		String currencyCode = "SAR";
		Optional<InvoiceLineType> optLineItem = null;
		AllowanceChargeType allowanceChargeType = null;
		if (discQty != null) {
			optLineItem = invoice.getInvoiceLine().stream().filter(i -> i.getID().getValue().equals(itemSeq)).findFirst();
			allowanceChargeType = new AllowanceChargeType();

			ChargeIndicatorType chargeIndicatorType = new ChargeIndicatorType();
			chargeIndicatorType.setValue(false);
			allowanceChargeType.setChargeIndicator(chargeIndicatorType);

			AllowanceChargeReasonType allowanceChargeReasonType = new AllowanceChargeReasonType();
			allowanceChargeReasonType.setValue(rs.getString("DISC_TYPE"));
			allowanceChargeType.getAllowanceChargeReason().add(allowanceChargeReasonType);

			AmountType amountType = new AmountType();
			amountType.setCurrencyID(currencyCode);
			amountType.setValue(discQty.multiply(op).multiply(rs.getBigDecimal("UNIT_DISCOUNT_AMT")).setScale(2, RoundingMode.HALF_UP));
			allowanceChargeType.setAmount(amountType);
		}

		if (discQty == null || optLineItem.isEmpty()) {
			invoiceLineType = new InvoiceLineType();
			BigDecimal qty = rs.getBigDecimal("QTY").multiply(op);
			BigDecimal unitPrice = rs.getBigDecimal("UNIT_RETAIL");
			BigDecimal taxRate = rs.getBigDecimal("IGTAX_RATE");
			BigDecimal taxExcluAmt = unitPrice.multiply(qty).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(100).add(taxRate), 10, RoundingMode.HALF_UP);
			BigDecimal taxAmt = taxExcluAmt.multiply(taxRate).divide(BigDecimal.valueOf(100)).setScale(10, RoundingMode.HALF_UP);

			IDType idType = new IDType();
			idType.setValue(itemSeq);
			invoiceLineType.setID(idType);

			InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
			invoicedQuantityType.setUnitCode("EA");
			invoicedQuantityType.setValue(qty);
			invoiceLineType.setInvoicedQuantity(invoicedQuantityType);

			LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
			lineExtensionAmountType.setCurrencyID(currencyCode);
			lineExtensionAmountType.setValue(taxExcluAmt.setScale(2, RoundingMode.HALF_UP));
			invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

			TaxTotalType taxTotalType = new TaxTotalType();

			BigDecimal roundedTaxAmt = taxAmt.setScale(2, RoundingMode.HALF_UP);
			TaxAmountType taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			taxTotalType.setTaxAmount(taxAmountType);

			RoundingAmountType roundingAmountType = new RoundingAmountType();
			roundingAmountType.setCurrencyID(currencyCode);
			roundingAmountType.setValue(lineExtensionAmountType.getValue().add(taxAmountType.getValue()));
			taxTotalType.setRoundingAmount(roundingAmountType);

			invoiceLineType.getTaxTotal().add(taxTotalType);

			ItemType itemType = new ItemType();

			NameType nameType = new NameType();
			nameType.setValue(rs.getString("ITEM_DSC"));
			itemType.setName(nameType);
			
			ItemIdentificationType skuIdentificationType = new ItemIdentificationType();
			IDType skuIdType = new IDType();
			skuIdType.setValue(rs.getString("ITEM"));
			skuIdentificationType.setID(skuIdType);
			itemType.setSellersItemIdentification(skuIdentificationType);

			TaxCategoryType taxCategoryType = new TaxCategoryType();

			IDType classIdType = new IDType();
			classIdType.setValue("S");
			taxCategoryType.setID(classIdType);

			PercentType percentType = new PercentType();
			percentType.setValue(taxRate);
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
			priceAmountType.setCurrencyID(currencyCode);
			priceAmountType.setValue(unitPrice.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(100).add(taxRate), 13, RoundingMode.HALF_UP));
			priceType.setPriceAmount(priceAmountType);
			priceType.getAllowanceCharge().add(allowanceChargeType);

			invoiceLineType.setPrice(priceType);

			invoice.getInvoiceLine().add(invoiceLineType);

			MonetaryTotalType monetaryTotalType = invoice.getLegalMonetaryTotal();
			if (monetaryTotalType == null) {
				monetaryTotalType = new MonetaryTotalType();
				invoice.setLegalMonetaryTotal(monetaryTotalType);

				LineExtensionAmountType mainLineExtensionAmountType = new LineExtensionAmountType();
				mainLineExtensionAmountType.setCurrencyID(currencyCode);
				mainLineExtensionAmountType.setValue(lineExtensionAmountType.getValue());
				monetaryTotalType.setLineExtensionAmount(mainLineExtensionAmountType);

				TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType();
				taxExclusiveAmountType.setCurrencyID(currencyCode);
				taxExclusiveAmountType.setValue(lineExtensionAmountType.getValue());
				monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountType);

				TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
				taxInclusiveAmountType.setCurrencyID(currencyCode);
				taxInclusiveAmountType.setValue(roundingAmountType.getValue());
				monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountType);

				PayableAmountType payableAmountType = new PayableAmountType();
				payableAmountType.setCurrencyID(currencyCode);
				payableAmountType.setValue(roundingAmountType.getValue());
				monetaryTotalType.setPayableAmount(payableAmountType);

				/* Tax Total */
				TaxTotalType mainTaxTotalType = new TaxTotalType();

				TaxAmountType mainTaxAmountType = new TaxAmountType();
				mainTaxAmountType.setCurrencyID(currencyCode);
				mainTaxAmountType.setValue(taxAmountType.getValue());
				mainTaxTotalType.setTaxAmount(mainTaxAmountType);

				invoice.getTaxTotal().add(mainTaxTotalType);

				/* Tax Sub Total */
				TaxTotalType subTaxTotalType = new TaxTotalType();

				taxAmountType = new TaxAmountType();
				taxAmountType.setCurrencyID(currencyCode);
				taxAmountType.setValue(roundedTaxAmt);
				subTaxTotalType.setTaxAmount(taxAmountType);

				TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
				taxAmountType = new TaxAmountType();
				taxAmountType.setCurrencyID(currencyCode);
				taxAmountType.setValue(roundedTaxAmt);
				taxSubtotalType.setTaxAmount(taxAmountType);

				TaxableAmountType taxableAmountType = new TaxableAmountType();
				taxableAmountType.setCurrencyID(currencyCode);
				taxableAmountType.setValue(taxRate.intValue() == 0 ? BigDecimal.ZERO : lineExtensionAmountType.getValue());
				taxSubtotalType.setTaxableAmount(taxableAmountType);

				taxCategoryType = new TaxCategoryType();

				idType = new IDType();
				idType.setValue("S");
				taxCategoryType.setID(idType);

				percentType = new PercentType();
				percentType.setValue(taxRate);
				taxCategoryType.setPercent(percentType);

				taxSchemeType = new TaxSchemeType();
				idType = new IDType();
				idType.setValue("VAT");
				taxSchemeType.setID(idType);
				taxCategoryType.setTaxScheme(taxSchemeType);

				taxSubtotalType.setTaxCategory(taxCategoryType);

				subTaxTotalType.getTaxSubtotal().add(taxSubtotalType);

				invoice.getTaxTotal().add(subTaxTotalType);
			} else {
				monetaryTotalType.getLineExtensionAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getLineExtensionAmount().getValue()));
				monetaryTotalType.getTaxExclusiveAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getTaxExclusiveAmount().getValue()));
				monetaryTotalType.getTaxInclusiveAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getTaxInclusiveAmount().getValue()));
				monetaryTotalType.getPayableAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getPayableAmount().getValue()));

				/* Tax Total */
				TaxTotalType mainTaxTotalType = invoice.getTaxTotal().get(0);
				mainTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(mainTaxTotalType.getTaxAmount().getValue()));

				/* Tax Sub Total */
				TaxTotalType subTaxTotalType = invoice.getTaxTotal().get(1);
				subTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(subTaxTotalType.getTaxAmount().getValue()));
				TaxSubtotalType subTotalAmountType = subTaxTotalType.getTaxSubtotal().get(0);
				subTotalAmountType.getTaxableAmount().setValue((taxRate.intValue() == 0 ? BigDecimal.ZERO : lineExtensionAmountType.getValue()).add(subTotalAmountType.getTaxableAmount().getValue()));
				subTotalAmountType.getTaxAmount().setValue(taxAmountType.getValue().add(subTotalAmountType.getTaxAmount().getValue()));
			}
		} else {
			optLineItem.get().getPrice().getAllowanceCharge().add(allowanceChargeType);
		}
	}

	public static InvoiceType createB2CInvoice(ResultSet rs) throws Exception {

		InvoiceType invoice = new InvoiceType();
		invoice.setProfileID(InvoiceConstant.getProfileType());

		IDType idType = new IDType();
		idType.setValue(rs.getString("UNIQUE_INV_ID"));
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue(UUID.randomUUID().toString());
		invoice.setUUID(uuidType);

		XMLGregorianCalendar gregFmt = null;
		Date currentDate = rs.getTimestamp("CREATE_DATETIME");
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(currentDate));
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format(currentDate));
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		invoiceTypeCodeType.setName("0200000"); /* Simplified Tax Invoice */
		invoiceTypeCodeType.setValue("381");
		invoice.setInvoiceTypeCode(invoiceTypeCodeType);

		BillingReferenceType billingReferenceType = new BillingReferenceType();

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();
		IDType invOrgReg = new IDType();
		invOrgReg.setValue(rs.getString("ORIGINAL_INVOICE_NO"));
		documentReferenceType.setID(invOrgReg);
		billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
		invoice.getBillingReference().add(billingReferenceType);

		PaymentMeansType paymentMeansType = new PaymentMeansType();
		InstructionNoteType instructionNoteType = new InstructionNoteType();
		instructionNoteType.setValue("R".equals(rs.getString("TYPE")) ? "Customer returned the items" : "Customer canncelled the items");
		paymentMeansType.getInstructionNote().add(instructionNoteType);

		PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
		paymentMeansCodeType.setValue("1");
		paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
		invoice.getPaymentMeans().add(paymentMeansType);

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

		invoice.setAccountingSupplierParty(addSuplierParty());

		addPartyIdentification(invoice.getAccountingSupplierParty().getParty(), rs.getString("CRN"));

		addB2CCustomerParty(invoice, rs);
		return invoice;

	}

	public static void createB2CLineItem(InvoiceType invoice, ResultSet rs) throws Exception {
		InvoiceLineType invoiceLineType = new InvoiceLineType();

		String currencyCode = "SAR";
		BigDecimal qty = rs.getBigDecimal("QTY");
		BigDecimal unitPrice = rs.getBigDecimal("UNIT_RETAIL");
		BigDecimal taxAmt = rs.getBigDecimal("UNIT_VAT_AMOUNT");
		BigDecimal taxRate = rs.getBigDecimal("TAXRATE");
		
		BigDecimal taxIncAmt = unitPrice;
		BigDecimal discount = rs.getBigDecimal("TOTAL_DISOUNT").setScale(2, RoundingMode.HALF_UP);
		if (discount != null && BigDecimal.ZERO.compareTo(discount) != 0) {
			taxIncAmt = taxIncAmt.subtract(discount);
			discount = discount.multiply(qty);
		}

		BigDecimal pmpQty = null;
		BigDecimal pmpTax = null;
		BigDecimal pmpDiscount = rs.getBigDecimal("TOTAL_PMP_AMOUNT");
		if (pmpDiscount != null && BigDecimal.ZERO.compareTo(pmpDiscount) != 0) {
			pmpQty = rs.getBigDecimal("PMP_QTY");
			pmpTax = rs.getBigDecimal("PMP_TAX_TOTAL");
			taxIncAmt = taxIncAmt.subtract(pmpDiscount);
			taxAmt = taxAmt.subtract(pmpTax);
		}
		
		BigDecimal restockAmount = rs.getBigDecimal("RESTOCK_AMOUNT");
		if (restockAmount != null && BigDecimal.ZERO.compareTo(restockAmount) != 0) {
			taxIncAmt = taxIncAmt.subtract(restockAmount);
			taxAmt = taxAmt.subtract(restockAmount.subtract(restockAmount.multiply(BigDecimal.valueOf(100)).divide(taxRate.add(BigDecimal.valueOf(100)), 2, RoundingMode.HALF_UP)));
		}
		
		BigDecimal taxExcluAmt = taxIncAmt.subtract(taxAmt);

		IDType idType = new IDType();
		idType.setValue(rs.getString("LINE_NO"));
		invoiceLineType.setID(idType);

		InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
		invoicedQuantityType.setUnitCode("EA");
		invoicedQuantityType.setValue(qty.setScale(2, RoundingMode.HALF_UP));
		invoiceLineType.setInvoicedQuantity(invoicedQuantityType);

		LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
		lineExtensionAmountType.setCurrencyID(currencyCode);
		lineExtensionAmountType.setValue(qty.multiply(taxExcluAmt).setScale(2, RoundingMode.HALF_UP));
		invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

		TaxTotalType taxTotalType = new TaxTotalType();

		BigDecimal roundedTaxAmt = taxAmt.multiply(qty).setScale(2, RoundingMode.HALF_UP);
		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID(currencyCode);
		taxAmountType.setValue(roundedTaxAmt);
		taxTotalType.setTaxAmount(taxAmountType);

		RoundingAmountType roundingAmountType = new RoundingAmountType();
		roundingAmountType.setCurrencyID(currencyCode);
		roundingAmountType.setValue(lineExtensionAmountType.getValue().add(taxAmountType.getValue()).setScale(2, RoundingMode.HALF_UP));
		taxTotalType.setRoundingAmount(roundingAmountType);

		invoiceLineType.getTaxTotal().add(taxTotalType);

		ItemType itemType = new ItemType();

		NameType nameType = new NameType();
		nameType.setValue(rs.getString("ITEM_DSC"));
		itemType.setName(nameType);
		
		ItemIdentificationType skuIdentificationType = new ItemIdentificationType();
		IDType skuIdType = new IDType();
		skuIdType.setValue(rs.getString("ITEM"));
		skuIdentificationType.setID(skuIdType);
		itemType.setSellersItemIdentification(skuIdentificationType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();

		IDType classIdType = new IDType();
		classIdType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
		taxCategoryType.setID(classIdType);

		PercentType percentType = new PercentType();
		percentType.setValue(taxRate);
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
		priceAmountType.setCurrencyID(currencyCode);
		priceAmountType.setValue(taxExcluAmt.setScale(2, RoundingMode.HALF_UP));
		priceType.setPriceAmount(priceAmountType);

		if (discount != null && BigDecimal.ZERO.compareTo(discount) != 0) {
			AllowanceChargeType allowanceChargeType = new AllowanceChargeType();
			ChargeIndicatorType chargeIndicatorType = new ChargeIndicatorType();
			chargeIndicatorType.setValue(false);
			allowanceChargeType.setChargeIndicator(chargeIndicatorType);

			AllowanceChargeReasonType allowanceChargeReasonType = new AllowanceChargeReasonType();
			allowanceChargeReasonType.setValue("discount");
			allowanceChargeType.getAllowanceChargeReason().add(allowanceChargeReasonType);

			AmountType amountType = new AmountType();
			amountType.setValue(discount.setScale(2, RoundingMode.HALF_UP));
			amountType.setCurrencyID(currencyCode);
			allowanceChargeType.setAmount(amountType);
			priceType.getAllowanceCharge().add(allowanceChargeType);
		}

		if (pmpDiscount != null && BigDecimal.ZERO.compareTo(pmpDiscount) != 0) {
			AllowanceChargeType allowanceChargeType = new AllowanceChargeType();
			ChargeIndicatorType chargeIndicatorType = new ChargeIndicatorType();
			chargeIndicatorType.setValue(false);
			allowanceChargeType.setChargeIndicator(chargeIndicatorType);

			AllowanceChargeReasonType allowanceChargeReasonType = new AllowanceChargeReasonType();
			allowanceChargeReasonType.setValue("PMP");
			allowanceChargeType.getAllowanceChargeReason().add(allowanceChargeReasonType);

			AmountType amountType = new AmountType();
			amountType.setValue(pmpDiscount.multiply(pmpQty).setScale(2, RoundingMode.HALF_UP));
			amountType.setCurrencyID(currencyCode);
			allowanceChargeType.setAmount(amountType);

			priceType.getAllowanceCharge().add(allowanceChargeType);
		}

		invoiceLineType.setPrice(priceType);

		invoice.getInvoiceLine().add(invoiceLineType);

		MonetaryTotalType monetaryTotalType = invoice.getLegalMonetaryTotal();
		if (monetaryTotalType == null) {
			monetaryTotalType = new MonetaryTotalType();
			invoice.setLegalMonetaryTotal(monetaryTotalType);

			LineExtensionAmountType mainLineExtensionAmountType = new LineExtensionAmountType();
			mainLineExtensionAmountType.setCurrencyID(currencyCode);
			mainLineExtensionAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setLineExtensionAmount(mainLineExtensionAmountType);

			TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType();
			taxExclusiveAmountType.setCurrencyID(currencyCode);
			taxExclusiveAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountType);

			TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
			taxInclusiveAmountType.setCurrencyID(currencyCode);
			taxInclusiveAmountType.setValue(roundingAmountType.getValue());
			monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountType);

			PayableAmountType payableAmountType = new PayableAmountType();
			payableAmountType.setCurrencyID(currencyCode);
			payableAmountType.setValue(roundingAmountType.getValue());
			monetaryTotalType.setPayableAmount(payableAmountType);

			/* Tax Total */
			TaxTotalType mainTaxTotalType = new TaxTotalType();

			TaxAmountType mainTaxAmountType = new TaxAmountType();
			mainTaxAmountType.setCurrencyID(currencyCode);
			mainTaxAmountType.setValue(taxAmountType.getValue());
			mainTaxTotalType.setTaxAmount(mainTaxAmountType);

			invoice.getTaxTotal().add(mainTaxTotalType);

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = new TaxTotalType();

			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			subTaxTotalType.setTaxAmount(taxAmountType);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			taxSubtotalType.setTaxAmount(taxAmountType);

			TaxableAmountType taxableAmountType = new TaxableAmountType();
			taxableAmountType.setCurrencyID(currencyCode);
			taxableAmountType.setValue(lineExtensionAmountType.getValue());
			taxSubtotalType.setTaxableAmount(taxableAmountType);

			taxCategoryType = new TaxCategoryType();

			idType = new IDType();
			idType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
			taxCategoryType.setID(idType);

			percentType = new PercentType();
			percentType.setValue(taxRate);
			taxCategoryType.setPercent(percentType);

			taxSchemeType = new TaxSchemeType();
			idType = new IDType();
			idType.setValue("VAT");
			taxSchemeType.setID(idType);
			taxCategoryType.setTaxScheme(taxSchemeType);

			taxSubtotalType.setTaxCategory(taxCategoryType);

			subTaxTotalType.getTaxSubtotal().add(taxSubtotalType);

			invoice.getTaxTotal().add(subTaxTotalType);
		} else {
			monetaryTotalType.getLineExtensionAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getLineExtensionAmount().getValue()));
			monetaryTotalType.getTaxExclusiveAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getTaxExclusiveAmount().getValue()));
			monetaryTotalType.getTaxInclusiveAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getTaxInclusiveAmount().getValue()));
			monetaryTotalType.getPayableAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getPayableAmount().getValue()));

			/* Tax Total */
			TaxTotalType mainTaxTotalType = invoice.getTaxTotal().get(0);
			mainTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(mainTaxTotalType.getTaxAmount().getValue()));

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = invoice.getTaxTotal().get(1);
			subTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(subTaxTotalType.getTaxAmount().getValue()));
			TaxSubtotalType subTotalAmountType = subTaxTotalType.getTaxSubtotal().get(0);
			subTotalAmountType.getTaxableAmount().setValue(lineExtensionAmountType.getValue().add(subTotalAmountType.getTaxableAmount().getValue()));
			subTotalAmountType.getTaxAmount().setValue(taxAmountType.getValue().add(subTotalAmountType.getTaxAmount().getValue()));
			
			if (subTotalAmountType.getTaxAmount().getValue().intValue() > 0 && (taxRate != null && taxRate.intValue() > 0)) {
				subTotalAmountType.getTaxCategory().getID().setValue("S");
				subTotalAmountType.getTaxCategory().getPercent().setValue(taxRate);
			}
		}
	}

	public static InvoiceType createSeibelInvoice(ResultSet rs) throws Exception {
		InvoiceType invoice = new InvoiceType();
		invoice.setProfileID(InvoiceConstant.getProfileType());

		IDType idType = new IDType();
		idType.setValue(rs.getString("UNIQUE_INVOICE_NO"));
		invoice.setID(idType);

		UUIDType uuidType = new UUIDType();
		uuidType.setValue(UUID.randomUUID().toString());
		invoice.setUUID(uuidType);

		XMLGregorianCalendar gregFmt = null;
		Date businessDate = rs.getTimestamp("INVOICE_CREATION_DATE");
		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(businessDate));
		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(gregFmt);
		invoice.setIssueDate(issueDateType);

		gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format(businessDate));
		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(gregFmt);
		invoice.setIssueTime(issueTimeType);

		InvoiceTypeCodeType invoiceTypeCodeType = new InvoiceTypeCodeType();
		String invType = rs.getString("TRAN_TYPE");
		invoiceTypeCodeType.setName("0200000"); /* Simplified Tax Invoice */
		if (!invType.equals("RETURN")) {
			invoiceTypeCodeType.setValue("388");
		} else {
			invoiceTypeCodeType.setValue("381");
			BillingReferenceType billingReferenceType = new BillingReferenceType();

			DocumentReferenceType documentReferenceType = new DocumentReferenceType();
			IDType invOrgReg = new IDType();
			invOrgReg.setValue(rs.getString("ORIGINAL_INVOICE_NO"));
			documentReferenceType.setID(invOrgReg);
			billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
			invoice.getBillingReference().add(billingReferenceType);

			PaymentMeansType paymentMeansType = new PaymentMeansType();
			InstructionNoteType instructionNoteType = new InstructionNoteType();
			instructionNoteType.setValue("Customer canncelled the items");
			paymentMeansType.getInstructionNote().add(instructionNoteType);

			PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
			paymentMeansCodeType.setValue("1");
			paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
			invoice.getPaymentMeans().add(paymentMeansType);
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

		invoice.setAccountingSupplierParty(addSuplierParty());
		
		addPartyIdentification(invoice.getAccountingSupplierParty().getParty(), rs.getString("CRN"));

		addB2CCustomerParty(invoice, rs);

		return invoice;
	}

	public static void createSeibelLineItem(InvoiceType invoice, ResultSet rs, BigDecimal op) throws Exception {

		String itemSeq = rs.getString("INVOICE_LINE_NO");
		InvoiceLineType invoiceLineType = null;

		String currencyCode = "SAR";
		AllowanceChargeType allowanceChargeType = null;

		invoiceLineType = new InvoiceLineType();
		BigDecimal qty = rs.getBigDecimal("QTY").multiply(op);
		BigDecimal taxRate = rs.getBigDecimal("VAT_PERCENTAGE").setScale(2, RoundingMode.HALF_UP);
		BigDecimal taxExcluAmt = rs.getBigDecimal("PRICE_EXCL_VAT").setScale(2, RoundingMode.HALF_UP);

		IDType idType = new IDType();
		idType.setValue(itemSeq);
		invoiceLineType.setID(idType);

		InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
		invoicedQuantityType.setUnitCode("EA");
		invoicedQuantityType.setValue(qty);
		invoiceLineType.setInvoicedQuantity(invoicedQuantityType);

		LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
		lineExtensionAmountType.setCurrencyID(currencyCode);
		lineExtensionAmountType.setValue(rs.getBigDecimal("TOTAL_LINE_EXCL_VAT").multiply(op).setScale(2, RoundingMode.HALF_UP));
		invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

		TaxTotalType taxTotalType = new TaxTotalType();

		BigDecimal roundedTaxAmt = rs.getBigDecimal("VAT_AMT").multiply(op).setScale(2, RoundingMode.HALF_UP);
		TaxAmountType taxAmountType = new TaxAmountType();
		taxAmountType.setCurrencyID(currencyCode);
		taxAmountType.setValue(roundedTaxAmt);
		taxTotalType.setTaxAmount(taxAmountType);

		RoundingAmountType roundingAmountType = new RoundingAmountType();
		roundingAmountType.setCurrencyID(currencyCode);
		roundingAmountType.setValue(rs.getBigDecimal("TOTAL_LINE").multiply(op).setScale(2, RoundingMode.HALF_UP));
		taxTotalType.setRoundingAmount(roundingAmountType);

		invoiceLineType.getTaxTotal().add(taxTotalType);

		ItemType itemType = new ItemType();

		NameType nameType = new NameType();
		nameType.setValue(rs.getString("ITEM"));
		itemType.setName(nameType);

		ItemIdentificationType skuIdentificationType = new ItemIdentificationType();
		IDType skuIdType = new IDType();
		skuIdType.setValue(rs.getString("SKU"));
		skuIdentificationType.setID(skuIdType);
		itemType.setSellersItemIdentification(skuIdentificationType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();

		IDType classIdType = new IDType();
		classIdType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
		taxCategoryType.setID(classIdType);

		PercentType percentType = new PercentType();
		percentType.setValue(taxRate);
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
		priceAmountType.setCurrencyID(currencyCode);
		priceAmountType.setValue(taxExcluAmt);
		priceType.setPriceAmount(priceAmountType);
		priceType.getAllowanceCharge().add(allowanceChargeType);

		invoiceLineType.setPrice(priceType);

		invoice.getInvoiceLine().add(invoiceLineType);

		MonetaryTotalType monetaryTotalType = invoice.getLegalMonetaryTotal();
		if (monetaryTotalType == null) {
			monetaryTotalType = new MonetaryTotalType();
			invoice.setLegalMonetaryTotal(monetaryTotalType);

			LineExtensionAmountType mainLineExtensionAmountType = new LineExtensionAmountType();
			mainLineExtensionAmountType.setCurrencyID(currencyCode);
			mainLineExtensionAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setLineExtensionAmount(mainLineExtensionAmountType);

			TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType();
			taxExclusiveAmountType.setCurrencyID(currencyCode);
			taxExclusiveAmountType.setValue(lineExtensionAmountType.getValue());
			monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountType);

			TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
			taxInclusiveAmountType.setCurrencyID(currencyCode);
			taxInclusiveAmountType.setValue(roundingAmountType.getValue());
			monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountType);

			PayableAmountType payableAmountType = new PayableAmountType();
			payableAmountType.setCurrencyID(currencyCode);
			payableAmountType.setValue(roundingAmountType.getValue());
			monetaryTotalType.setPayableAmount(payableAmountType);

			/* Tax Total */
			TaxTotalType mainTaxTotalType = new TaxTotalType();

			TaxAmountType mainTaxAmountType = new TaxAmountType();
			mainTaxAmountType.setCurrencyID(currencyCode);
			mainTaxAmountType.setValue(taxAmountType.getValue());
			mainTaxTotalType.setTaxAmount(mainTaxAmountType);

			invoice.getTaxTotal().add(mainTaxTotalType);

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = new TaxTotalType();

			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			subTaxTotalType.setTaxAmount(taxAmountType);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			taxAmountType = new TaxAmountType();
			taxAmountType.setCurrencyID(currencyCode);
			taxAmountType.setValue(roundedTaxAmt);
			taxSubtotalType.setTaxAmount(taxAmountType);

			TaxableAmountType taxableAmountType = new TaxableAmountType();
			taxableAmountType.setCurrencyID(currencyCode);
			taxableAmountType.setValue(taxRate.intValue() == 0 ? BigDecimal.ZERO : lineExtensionAmountType.getValue());
			taxSubtotalType.setTaxableAmount(taxableAmountType);

			taxCategoryType = new TaxCategoryType();

			idType = new IDType();
			idType.setValue(taxRate != null && taxRate.intValue() > 0 ? "S" : "Z");
			taxCategoryType.setID(idType);

			percentType = new PercentType();
			percentType.setValue(taxRate);
			taxCategoryType.setPercent(percentType);

			taxSchemeType = new TaxSchemeType();
			idType = new IDType();
			idType.setValue("VAT");
			taxSchemeType.setID(idType);
			taxCategoryType.setTaxScheme(taxSchemeType);

			taxSubtotalType.setTaxCategory(taxCategoryType);

			subTaxTotalType.getTaxSubtotal().add(taxSubtotalType);

			invoice.getTaxTotal().add(subTaxTotalType);
		} else {
			monetaryTotalType.getLineExtensionAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getLineExtensionAmount().getValue()));
			monetaryTotalType.getTaxExclusiveAmount().setValue(lineExtensionAmountType.getValue().add(monetaryTotalType.getTaxExclusiveAmount().getValue()));
			monetaryTotalType.getTaxInclusiveAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getTaxInclusiveAmount().getValue()));
			monetaryTotalType.getPayableAmount().setValue(roundingAmountType.getValue().add(monetaryTotalType.getPayableAmount().getValue()));

			/* Tax Total */
			TaxTotalType mainTaxTotalType = invoice.getTaxTotal().get(0);
			mainTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(mainTaxTotalType.getTaxAmount().getValue()));

			/* Tax Sub Total */
			TaxTotalType subTaxTotalType = invoice.getTaxTotal().get(1);
			subTaxTotalType.getTaxAmount().setValue(taxAmountType.getValue().add(subTaxTotalType.getTaxAmount().getValue()));
			TaxSubtotalType subTotalAmountType = subTaxTotalType.getTaxSubtotal().get(0);
			subTotalAmountType.getTaxableAmount().setValue((taxRate.intValue() == 0 ? BigDecimal.ZERO : lineExtensionAmountType.getValue()).add(subTotalAmountType.getTaxableAmount().getValue()));
			subTotalAmountType.getTaxAmount().setValue(taxAmountType.getValue().add(subTotalAmountType.getTaxAmount().getValue()));

			if (subTotalAmountType.getTaxAmount().getValue().intValue() > 0 && (taxRate != null && taxRate.intValue() > 0)) {
				subTotalAmountType.getTaxCategory().getID().setValue("S");
				subTotalAmountType.getTaxCategory().getPercent().setValue(taxRate);
			}
		}
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

	public static String getHashFromXml(InvoiceType invoiceType) {
		byte[] qr = invoiceType.getAdditionalDocumentReference().stream().filter(a -> a.getID().getValue().equals("QR")).findFirst().get().getAttachment().getEmbeddedDocumentBinaryObject().getValue();
		return new BerTlvParser().parse(qr).find(new BerTag(6)).getTextValue();
	}
}
