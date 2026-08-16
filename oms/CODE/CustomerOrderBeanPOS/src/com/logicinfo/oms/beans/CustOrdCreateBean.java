package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.extra.bds.bean.Booking;
import com.extra.bds.bean.CustomerDetail;
import com.extra.bds.bean.Delivery;
import com.extra.bds.bean.DeliveryAddress;
import com.extra.bds.bean.Item;
import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsOrposContact;
import com.logicinfo.oms.ejb.OmsOrposCustOrdFul;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsOrposCustomer;
import com.logicinfo.oms.ejb.OmsOrposDiscntLine;
import com.logicinfo.oms.ejb.OmsOrposEmail;
import com.logicinfo.oms.ejb.OmsOrposGeoaddr;
import com.logicinfo.oms.ejb.OmsOrposLocale;
import com.logicinfo.oms.ejb.OmsOrposMasterAudit;
import com.logicinfo.oms.ejb.OmsOrposPayment;
import com.logicinfo.oms.ejb.OmsOrposPhone;
import com.logicinfo.oms.ejb.OmsOrposPromoLine;
import com.logicinfo.oms.ejb.OmsOrposTaxLine;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Emails;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Phones;
import com.oracle.retail.integration.base.bo.customerdesc.v1.AddrBook;
import com.oracle.retail.integration.base.bo.customerdesc.v1.AddrBookEntry;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerDesc;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerGrpIdLst;
import com.oracle.retail.integration.base.bo.customerdesc.v1.PrimAddrType;
import com.oracle.retail.integration.base.bo.custorddelcoldesc.v1.CustOrdDelColDesc;
import com.oracle.retail.integration.base.bo.custorddeldesc.v1.CustOrdDelDesc;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custordfulcoldesc.v1.CustOrdFulColDesc;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.CustOrdFulDesc;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.DeliveryDestDtl;
import com.oracle.retail.integration.base.bo.custorditmcoldesc.v1.CustOrdItmColDesc;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.CustOrdItmDesc;
import com.oracle.retail.integration.base.bo.discntlinecoldesc.v1.DiscntLineColDesc;
import com.oracle.retail.integration.base.bo.discntlinedesc.v1.DiscntLineDesc;
import com.oracle.retail.integration.base.bo.emaildesc.v1.EmailDesc;
import com.oracle.retail.integration.base.bo.geoaddrdesc.v1.GeoAddrDesc;
import com.oracle.retail.integration.base.bo.localedesc.v1.LocaleDesc;
import com.oracle.retail.integration.base.bo.paymentcoldesc.v1.PaymentColDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CouponTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CreditDebitTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.GiftCardTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.GiftCertTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentType;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PurchaseOrdTender;
import com.oracle.retail.integration.base.bo.phonedesc.v1.PhoneDesc;
import com.oracle.retail.integration.base.bo.promolinedesc.v1.PromoLineDesc;
import com.oracle.retail.integration.base.bo.taxlinecoldesc.v1.TaxLineColDesc;
import com.oracle.retail.integration.base.bo.taxlinedesc.v1.TaxLineDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

public class CustOrdCreateBean {

	private static IBDSClient bdsClient;
	
	public CustOrdCreateBean() {
		super();
	}

	private final static Logger log = Logger.getLogger(CustOrdCreateBean.class.getName());
	BigDecimal omsOrposCustOrderId;
	BigDecimal contactSeq;
	BigDecimal omsCustOrdNo = null;
//SourceLocIdentify omsSourceLocIdentify = new SourceLocIdentify();
	PosSourceLocationIndentifier posSourceLocationIndentifier = new PosSourceLocationIndentifier();
	Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;

//Create separate method based on tables
	public BigDecimal saveCreatCustOrd(CustOrderDesc custOrderDesc) throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info(" *******************Begin of saveCreatCustOrd*************************************");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("********Creation of OMSUtil Session EJB :  " + session);
		log.info("omsCustOrdNo " + omsCustOrdNo + "Create cust ord started");
		OmsOrposCustOrderHead omsOrposCustOrd = null;
		OmsOrposCustOrderHead omsOrposCustOrderHead = new OmsOrposCustOrderHead();
		omsOrposCustOrderHead.setCustomerOrderId(custOrderDesc.getCustomerOrderId());
		omsOrposCustOrderHead.setExternalRefId(checkNullValueForString(custOrderDesc.getExternalRefId()));
		omsOrposCustOrderHead.setCurrencyCode(checkNullValueForString(custOrderDesc.getCurrencyCode()));
		// Locale Desc
		omsOrposCustOrderHead.setOrderDesc(checkNullValueForString(custOrderDesc.getOrderDesc()));
		omsOrposCustOrderHead.setOrderStatus(checkNullValueForString(custOrderDesc.getOrderStatus().value()));
		omsOrposCustOrderHead.setInitiateLocType(checkNullValueForString(custOrderDesc.getInitiateLocType().value()));
		omsOrposCustOrderHead.setInitiateLocId(checkNullValueForNumber(new BigDecimal(custOrderDesc.getInitiateLocId())));
		omsOrposCustOrderHead.setGrandTotal(checkNullValueForNumber(custOrderDesc.getGrandTotal()));
		omsOrposCustOrderHead.setSubTotal(checkNullValueForNumber(custOrderDesc.getSubTotal()));
		omsOrposCustOrderHead.setTaxTotal(checkNullValueForNumber(custOrderDesc.getTaxTotal()));
		omsOrposCustOrderHead.setInclusiveTaxTotal(checkNullValueForNumber(custOrderDesc.getInclusiveTaxTotal()));
		omsOrposCustOrderHead.setShippingChargeTotal(checkNullValueForNumber(custOrderDesc.getShippingChargeTotal()));
		omsOrposCustOrderHead.setDiscountTotal(checkNullValueForNumber(custOrderDesc.getDiscountTotal()));
		omsOrposCustOrderHead.setCompletedAmount(checkNullValueForNumber(custOrderDesc.getCompletedAmount()));
		omsOrposCustOrderHead.setCancelledAmount(checkNullValueForNumber(custOrderDesc.getCancelledAmount()));
		omsOrposCustOrderHead.setReturnedDiscountAmount(checkNullValueForNumber(custOrderDesc.getReturnedDiscountAmount()));
		omsOrposCustOrderHead.setCompletedTaxAmount(checkNullValueForNumber(custOrderDesc.getCompletedTaxAmount()));
		omsOrposCustOrderHead.setCancelledTaxAmount(checkNullValueForNumber(custOrderDesc.getCancelledTaxAmount()));
		omsOrposCustOrderHead.setReturnedTaxAmount(checkNullValueForNumber(custOrderDesc.getReturnedTaxAmount()));
		omsOrposCustOrderHead.setCompletedInclusiveTaxAmount(checkNullValueForNumber(custOrderDesc.getCompletedInclusiveTaxAmount()));
		omsOrposCustOrderHead.setCancelledInclusiveTaxAmount(checkNullValueForNumber(custOrderDesc.getCancelledInclusiveTaxAmount()));
		omsOrposCustOrderHead.setReturnedInclusiveTaxAmount(checkNullValueForNumber(custOrderDesc.getReturnedInclusiveTaxAmount()));
		omsOrposCustOrderHead.setPaidAmount(checkNullValueForNumber(custOrderDesc.getPaidAmount()));
		omsOrposCustOrderHead.setRoundingAdjustment(custOrderDesc.getRoundingAdjustment());
		omsOrposCustOrderHead.setInitiateCountryCode(checkNullValueForString((custOrderDesc.getInitiateCountryCode())));
		omsOrposCustOrderHead.setGiftReceiptAssigned(checkNullValueForString((custOrderDesc.getGiftReceiptAssigned().value())));
		omsOrposCustOrderHead.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
		omsOrposCustOrderHead.setUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
		log.info("setting status intially to A");
		omsOrposCustOrderHead.setStatus("A");
		log.info("Persisting into persistOmsOrposCustOrderHead table");
		try {
			session.persistOmsOrposCustOrderHead(omsOrposCustOrderHead);
		} catch (Exception e) {
			log.info("Exception occured while persisting : " + e.getMessage());
		}
		log.info("Successfully persisted in omsOrposCustOrderHead");
		log.info("custOrderDesc.getCustomerOrderId()" + custOrderDesc.getCustomerOrderId());
		try {
			omsOrposCustOrd = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(custOrderDesc.getCustomerOrderId(), "A");
		} catch (Exception e) {
			log.info(" Error occured while find the customer id" + e.getMessage());
		}
		omsOrposCustOrderId = omsOrposCustOrd.getOmsOrposCustOrderId();
		log.info("omsOrposCustOrderId " + omsOrposCustOrd.getOmsOrposCustOrderId());
		log.info("custOrderDesc.getLocaleDesc()" + custOrderDesc.getLocaleDesc());
		LocaleDesc localeDesc = custOrderDesc.getLocaleDesc();
		try {
			saveLocaleDesc(localeDesc, "CustOrderDesc");
		} catch (Exception e) {
			log.info(" Error Occured" + e.getMessage());
		}
		// Customer Desc
		// Optional
		if (custOrderDesc.getCustomerDesc() != null) {
			log.info("inside customer desc");
			CustomerDesc customerDesc = custOrderDesc.getCustomerDesc();
			saveCustomerDesc(customerDesc, "CustOrderDesc");
			if (customerDesc.getContactDesc() != null) {
				ContactDesc contactDesc = customerDesc.getContactDesc();
				saveContactDesc(contactDesc, "CUSTOMER", customerDesc.getCustomerId());
				log.info("calling getOmsOrposContactFindByCustomerId  omsOrposCustOrderId " + omsOrposCustOrderId + " and CustomerId " + customerDesc.getCustomerId());
				OmsOrposContact omsOrposContact = session.getOmsOrposContactFindByOmsOrposCustOrderIdandCustomerId(omsOrposCustOrderId, new BigDecimal(customerDesc.getCustomerId()));
				log.info("fetched the values from getOmsOrposContactFindByCustomerId");
				// Optional
				if (contactDesc.getPhones() != null) {
					Phones phones = contactDesc.getPhones();
					savePhones(phones, omsOrposContact.getContactSeq());
				}
				// Optional
				if (contactDesc.getEmails() != null) {
					Emails emails = contactDesc.getEmails();
					saveEmails(emails, omsOrposContact.getContactSeq());
				}
			}
			// Optional
			if (customerDesc.getAddrBook() != null) {
				AddrBook addrBook = customerDesc.getAddrBook();
				saveAddrBook(addrBook);
			}
			// Optional
			if (customerDesc.getCustomerGrpIdLst() != null) {
				CustomerGrpIdLst customerGrpIdLst = customerDesc.getCustomerGrpIdLst();
				saveCustomerGrpIdLst(customerGrpIdLst);
			} // Optional
			if (customerDesc.getLocaleDesc() != null) {
				LocaleDesc localeDesc1 = customerDesc.getLocaleDesc();
				saveLocaleDesc(localeDesc1, "AddrBook");
			}
		}
		// Item Desc
		// Optional
		log.info("Item Collection of customer Order.");
		CustOrdItmColDesc custOrdItmColDesc = custOrderDesc.getCustOrdItmColDesc();
		log.info(" Presting Item collection data");
		try {
			saveCustOrdItmColDesc(custOrdItmColDesc);
		} catch (Exception e) {
			log.info(" Error Occured " + e.getMessage());
		}
		// Fulfilment Desc
		log.info(" persitting Fulfilment Desc ");
		CustOrdFulColDesc custOrdFulColDesc = custOrderDesc.getCustOrdFulColDesc();
		saveCustOrdFulColDesc(custOrdFulColDesc);
		// Delete desc
		// CustOrdDelColDesc custOrdDelColDesc= custOrderDesc.getCustOrdDelColDesc();
		// saveCustOrdDelColDesc(custOrdDelColDesc);
		// Payment desc
		log.info(" calling payment collection description");
		PaymentColDesc paymentColDesc = custOrderDesc.getPaymentColDesc();
		savePaymentColDesc(custOrderDesc, paymentColDesc);
		OmsPersistence omsPersistence = new OmsPersistence();
		omsCustOrdNo = omsPersistence.omsPersist(custOrderDesc, omsOrposCustOrderId);
		log.info("omsCustOrdNo" + omsCustOrdNo + "after persisting and returning to custOrdBean Class ");
		try {
			log.info("********calling  find Source Location method*************");
			findSourceLocation(custOrderDesc, omsOrposCustOrderId);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Processing complete");
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside try");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			OmsOrposCustOrderHead omsOrposCustOrderHead1 = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			log.info("omsCustOrdNo " + omsCustOrdNo + "status " + omsOrposCustOrderHead1.getStatus());
			log.info("omsCustOrdNo " + omsCustOrdNo + "status " + omsOrposCustOrderHead1.getStatus());
			omsOrposCustOrderHead1.setOrderStatus("FILLED");
			if (omsOrposCustOrderHead1.getStatus().equals("F")) {
				omsCustOrdHead.setStatus("F");
			} else {
				omsCustOrdHead.setStatus("S");
			}
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead1);
		} catch (Exception e) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside catch block for processing complete");
			OmsOrposCustOrderHead omsOrposCustOrderHead1 = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposCustOrderHead1.setOrderStatus("CANCELED");
			omsOrposCustOrderHead1.setStatus("F");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead1);
		}
		return omsOrposCustOrderId;
	} // end of method saveCreatCustOrd

	public void saveLocaleDesc(LocaleDesc localeDesc, String headerNode) throws SOAPException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside locale omsOrposCustOrderId=" + omsOrposCustOrderId);
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposLocale omsOrposLocale = new OmsOrposLocale();
		omsOrposLocale.setOmsOrposCustOrderId(omsOrposCustOrderId);
		omsOrposLocale.setLang(localeDesc.getLang());
		omsOrposLocale.setCountry(checkNullValueForString(localeDesc.getCountry()));
		session.persistOmsOrposLocale(omsOrposLocale);
		log.info("Successfully persisted in omsOrposLocale");
	}

	public void saveCustomerDesc(CustomerDesc customerDesc, String headerNode) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside saveCustomerDesc");
		log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
		OmsOrposCustomer omsOrposCustomer = new OmsOrposCustomer();
		log.info("omsCustOrdNo " + omsCustOrdNo + "saving customer" + omsOrposCustOrderId);
		omsOrposCustomer.setOmsOrposCustOrderId(omsOrposCustOrderId);
		log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
		log.info("customerDesc.getCustomerId() " + customerDesc.getCustomerId());
		omsOrposCustomer.setCustomerId(customerDesc.getCustomerId());
		log.info("customerDesc.getCustomerType() " + customerDesc.getCustomerType().value());
		omsOrposCustomer.setCustomerType(customerDesc.getCustomerType().value());
		log.info("customerDesc.getContactByEmail().value() " + customerDesc.getContactByEmail().value());
		omsOrposCustomer.setContactByEmail(customerDesc.getContactByEmail().value());
		omsOrposCustomer.setContactByMail(customerDesc.getContactByMail().value());
		omsOrposCustomer.setContactByPhone(customerDesc.getContactByPhone().value());

		session.persistOmsOrposCustomer(omsOrposCustomer);
		log.info("Successfully persisted in omsOrposCustomer");
	}

	public void saveContactDesc(ContactDesc contactDesc, String headerNode, String headerSeq) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposContact omsOrposContact = new OmsOrposContact();
		if ("CUSTOMER".equals(headerNode)) {
			omsOrposContact.setCustomerId(new BigDecimal(headerSeq)); // changet the columns data type in db
		} else if ("FULFILLMENT".equals(headerNode)) {
			omsOrposContact.setCustOrdFulSeqNo(new BigDecimal(headerSeq));
		} else if ("GEOADDR".equals(headerNode)) {
			omsOrposContact.setPaymentSeqNo(new BigDecimal(headerSeq));
		}
		omsOrposContact.setOmsOrposCustOrderId(omsOrposCustOrderId);
		omsOrposContact.setFirstName(checkNullValueForString(contactDesc.getFirstName()));
		omsOrposContact.setPhoneticFirst(checkNullValueForString(contactDesc.getPhoneticFirst()));
		omsOrposContact.setLastName(checkNullValueForString(contactDesc.getLastName()));
		omsOrposContact.setMiddleName(checkNullValueForString(contactDesc.getMiddleName()));
		omsOrposContact.setPreferredName(checkNullValueForString(contactDesc.getPreferredName()));
		omsOrposContact.setNamePrefix(checkNullValueForString(contactDesc.getNamePrefix()));
		omsOrposContact.setNameSuffix(checkNullValueForString(contactDesc.getNameSuffix()));
		omsOrposContact.setCompanyName(checkNullValueForString(contactDesc.getCompanyName()));
		session.persistOmsOrposContact(omsOrposContact);
		log.info("Successfully persisted in omsOrposContact");

	}

	public void savePhones(Phones phones, BigDecimal headerSeq) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<PhoneDesc> phoneDescList = phones.getPhoneDesc();
		for (PhoneDesc phoneDesc : phoneDescList) {
			// Change th FK constraint in dB FK_CD_OMS_ORPOS_PHONE
			// Chnage th PK ,add seq and make it as PK
			OmsOrposPhone omsOrposPhone = new OmsOrposPhone();
			// omsOrposPhone.setHeaderNode(headerNode);
			omsOrposPhone.setContactSeq(headerSeq);
			omsOrposPhone.setOmsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposPhone.setPhoneId(checkNullValueForNumber(phoneDesc.getPhoneId()));
			omsOrposPhone.setPhoneNumber(phoneDesc.getPhoneNumber());
			omsOrposPhone.setPhoneType(checkNullValueForString(phoneDesc.getPhoneType().value()));
			omsOrposPhone.setPhoneExtension(checkNullValueForString(phoneDesc.getPhoneExtension()));
			omsOrposPhone.setPrimaryPhoneInd(phoneDesc.getPrimaryPhoneInd());
			session.persistOmsOrposPhone(omsOrposPhone);
		}
		log.info("Successfully persisted in omsOrposPhone");
	}

	public void saveEmails(Emails emails, BigDecimal headerSeq) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<EmailDesc> emailDescList = emails.getEmailDesc();
		for (EmailDesc emailDesc : emailDescList) {
			OmsOrposEmail omsOrposEmail = new OmsOrposEmail();
			omsOrposEmail.setContactSeq(headerSeq);
			// omsOrposEmail.setHeaderNode(headerNode);
			omsOrposEmail.setOmsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposEmail.setEmailId(checkNullValueForNumber(emailDesc.getEmailId()));
			omsOrposEmail.setEmailAddress(checkNullValueForString(emailDesc.getEmailAddress()));
			omsOrposEmail.setEmailType(checkNullValueForString(emailDesc.getEmailType().value()));
			omsOrposEmail.setPrimaryEmailInd(checkNullValueForString(emailDesc.getPrimaryEmailInd()));
			session.persistOmsOrposEmail(omsOrposEmail);
		}
		log.info("Successfully persisted in omsOrposEmail");
	}

	public void saveAddrBook(AddrBook addrBook) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<AddrBookEntry> addrBookEntryLsit = addrBook.getAddrBookEntry();
		for (AddrBookEntry addrBookEntry : addrBookEntryLsit) {
			OmsOrposGeoaddr omsOrposGeoaddr = new OmsOrposGeoaddr();
			omsOrposGeoaddr.setOmsOrposCustOrderId(omsOrposCustOrderId);
			// omsOrposGeoaddr.setAddrId(addrBookEntry.getAddrId());
			// need to check the xml
			GeoAddrDesc geoAddrDesc = addrBookEntry.getGeoAddrDesc();
			// add from xml
			// Optional
			ContactDesc contactDesc = addrBookEntry.getContactDesc();
//			 saveContactDesc(contactDesc,"GEOADDR",geoAddrDesc.get);
			PrimAddrType primaryAddrInd = addrBookEntry.getPrimaryAddrInd();
			// need to add
		}
		log.info("Successfully persisted in AddrBook");
	}

	public void saveCustomerGrpIdLst(CustomerGrpIdLst customerGrpIdLst) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		for (BigDecimal customerGroupId : customerGrpIdLst.getCustomerGroupId()) {
		}
	}

	public void saveCustOrdItmColDesc(CustOrdItmColDesc custOrdItmColDesc) throws SOAPException {
		log.info("************** Begin of saveCustOrdItmColDesc method**************** ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<CustOrdItmDesc> custOrdItmDescList = custOrdItmColDesc.getCustOrdItmDesc();
		for (CustOrdItmDesc custOrdItmDesc : custOrdItmDescList) {
			OmsOrposCustOrdItm omsOrposCustOrdItm = new OmsOrposCustOrdItm();
			omsOrposCustOrdItm.setLineItemNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
			omsOrposCustOrdItm.setCapturedLineItemNo(new BigDecimal(custOrdItmDesc.getCapturedLineItemNo()));
			omsOrposCustOrdItm.setOmsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposCustOrdItm.setItemId(custOrdItmDesc.getItemId());
			omsOrposCustOrdItm.setItemUpc(custOrdItmDesc.getItemUpc());
			omsOrposCustOrdItm.setItemDescription(custOrdItmDesc.getItemDescription());
			omsOrposCustOrdItm.setQuantity(custOrdItmDesc.getQuantity());
			omsOrposCustOrdItm.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity());
			omsOrposCustOrdItm.setCompletedQuantity(custOrdItmDesc.getCompletedQuantity());
			omsOrposCustOrdItm.setCancelledQuantity(custOrdItmDesc.getCancelledQuantity());
			omsOrposCustOrdItm.setReturnedQuantity(custOrdItmDesc.getReturnedQuantity());
			omsOrposCustOrdItm.setUnitOfMeasure(checkNullValueForString(custOrdItmDesc.getUnitOfMeasure()));
			omsOrposCustOrdItm.setCurrencyCode(custOrdItmDesc.getCurrencyCode());
			omsOrposCustOrdItm.setItemTotal(checkNullValueForNumber(custOrdItmDesc.getItemTotal()));
			omsOrposCustOrdItm.setUnitSellPrice(custOrdItmDesc.getUnitSellPrice());
			omsOrposCustOrdItm.setUnitRegularPrice(custOrdItmDesc.getUnitRegularPrice());
			omsOrposCustOrdItm.setCompletedAmount(custOrdItmDesc.getCompletedAmount());
			omsOrposCustOrdItm.setCancelledAmount(custOrdItmDesc.getCancelledAmount());
			omsOrposCustOrdItm.setReturnedAmount(custOrdItmDesc.getReturnedAmount());
			omsOrposCustOrdItm.setPaidAmount(custOrdItmDesc.getPaidAmount());
			omsOrposCustOrdItm.setDiscountTotal(checkNullValueForNumber(custOrdItmDesc.getDiscountTotal()));
			omsOrposCustOrdItm.setCompletedDiscountAmount(checkNullValueForNumber(custOrdItmDesc.getCompletedDiscountAmount()));
			omsOrposCustOrdItm.setCancelledDiscountAmount(checkNullValueForNumber(custOrdItmDesc.getCancelledDiscountAmount()));
			omsOrposCustOrdItm.setReturnedDiscountAmount(checkNullValueForNumber(custOrdItmDesc.getCancelledDiscountAmount()));
			omsOrposCustOrdItm.setDepartmentId(checkNullValueForString(custOrdItmDesc.getDepartmentId()));
			omsOrposCustOrdItm.setRestrictiveAge(custOrdItmDesc.getRestrictiveAge());
			omsOrposCustOrdItm.setTaxGroupId(custOrdItmDesc.getTaxGroupId());
			omsOrposCustOrdItm.setTaxableFlag(custOrdItmDesc.getTaxableFlag().value());
			omsOrposCustOrdItm.setTaxTotal(custOrdItmDesc.getTaxTotal());
			omsOrposCustOrdItm.setWeight(custOrdItmDesc.getWeight());
			omsOrposCustOrdItm.setGiftReceiptedItemFlag(custOrdItmDesc.getGiftReceiptedItemFlag().value());
			omsOrposCustOrdItm.setShippingChargeFlag(custOrdItmDesc.getShippingChargeFlag().value());
			omsOrposCustOrdItm.setFulfillmentSeqNo(new BigDecimal(custOrdItmDesc.getFulfillmentSeqNo()));
			omsOrposCustOrdItm.setDiscountableFlag(custOrdItmDesc.getDiscountableFlag().value());
			omsOrposCustOrdItm.setDamageDiscountableFlag(custOrdItmDesc.getDamageDiscountableFlag().value());
			omsOrposCustOrdItm.setEmployeeDiscountableFlag(custOrdItmDesc.getEmployeeDiscountableFlag().value());
			omsOrposCustOrdItm.setRestockingFeeFlag(custOrdItmDesc.getRestockingFeeFlag().value());
			omsOrposCustOrdItm.setReturnEligibleFlag(custOrdItmDesc.getReturnEligibleFlag().value());
			omsOrposCustOrdItm.setSerializedItemFlag(custOrdItmDesc.getSerializedItemFlag().value());
			omsOrposCustOrdItm.setValidateSerialNumberFlag(custOrdItmDesc.getValidateSerialNumberFlag().value());
			omsOrposCustOrdItm.setAllowNewSerialNumberFlag(custOrdItmDesc.getAllowNewSerialNumberFlag().value());
			omsOrposCustOrdItm.setSizeRequiredFlag(custOrdItmDesc.getSizeRequiredFlag().value());
			omsOrposCustOrdItm.setGiftReceiptedItemFlag(custOrdItmDesc.getGiftReceiptedItemFlag().value());
			omsOrposCustOrdItm.setShippingChargeFlag(custOrdItmDesc.getShippingChargeFlag().value());
			omsOrposCustOrdItm.setItemType(custOrdItmDesc.getItemType().value());
			// omsOrposCustOrdItm.setMerchandiseHierarchyGroupId(custOrdItmDesc.getMerchandiseHierarchyGroupId());
			log.info("--------------Changed Merch Group Id Length to 0 to 90");
			String merchandiseHierachry = custOrdItmDesc.getMerchandiseHierarchyGroupId();
			if (merchandiseHierachry.length() >= 100)
				merchandiseHierachry = merchandiseHierachry.substring(0, 90);
			omsOrposCustOrdItm.setMerchandiseHierarchyGroupId(merchandiseHierachry);
			log.info("Merch group Id : " + custOrdItmDesc.getMerchandiseHierarchyGroupId());
			// need to confirm whether tax will come or not
			/* Start tax details @tsultana */
			omsOrposCustOrdItm.setCompletedTaxAmount(BigDecimal.ZERO);
			omsOrposCustOrdItm.setCancelledTaxAmount(BigDecimal.ZERO);
			omsOrposCustOrdItm.setReturnedTaxAmount(BigDecimal.ZERO);
			omsOrposCustOrdItm.setInclusiveTaxTotal(checkNullValueForNumber(custOrdItmDesc.getInclusiveTaxTotal()));
			omsOrposCustOrdItm.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
			omsOrposCustOrdItm.setCancelledInclusiveTaxAmount(BigDecimal.ZERO);
			omsOrposCustOrdItm.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);
			/* End tax details @tsultana */
			session.persistOmsOrposCustOrdItm(omsOrposCustOrdItm);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Successfully persisted in omsOrposCustOrdItm");
			if (custOrdItmDesc.getPromoLineDesc() != null) {
				PromoLineDesc promoLineDesc = custOrdItmDesc.getPromoLineDesc();
				OmsOrposPromoLine omsOrposPromoLine = new OmsOrposPromoLine();
				omsOrposPromoLine.setItemId(custOrdItmDesc.getItemId());
				omsOrposPromoLine.setCurrencyCode(promoLineDesc.getCurrencyCode());
				omsOrposPromoLine.setUnitDiscountAmount(promoLineDesc.getUnitDiscountAmount());
				omsOrposPromoLine.setPromotionComponentDetailId(promoLineDesc.getPromotionComponentDetailId());
				omsOrposPromoLine.setPromotionComponentId(promoLineDesc.getPromotionComponentId());
				omsOrposPromoLine.setPromotionId(promoLineDesc.getPromotionId());
				omsOrposPromoLine.setOmsOrposCustOrderId(omsOrposCustOrderId);
				omsOrposPromoLine.setCapturedLineItemNo(new BigDecimal(custOrdItmDesc.getCapturedLineItemNo()));
				session.persistOmsOrposPromoLine(omsOrposPromoLine);
			}
			if (custOrdItmDesc.getDiscntLineColDesc() != null) {
				log.info("Inside if statement of custOrdItmDesc.getDiscntLineColDesc()");
				DiscntLineColDesc discntLineColDesc = custOrdItmDesc.getDiscntLineColDesc();
				if (discntLineColDesc.getDiscntLineDesc() != null) {
					log.info("Inside if statement of discntLineColDesc.getDiscntLineDesc()" + discntLineColDesc.getCollectionSize());
					for (DiscntLineDesc discntLineDesc : discntLineColDesc.getDiscntLineDesc()) {
						OmsOrposDiscntLine omsOrposDiscntLine = new OmsOrposDiscntLine();
						omsOrposDiscntLine.setCapturedLineItemNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
						omsOrposDiscntLine.setOmsOrposCustOrderId(checkNullValueForNumber(omsOrposCustOrderId));
						omsOrposDiscntLine.setOmsCustOrdNo(BigDecimal.ZERO);
						omsOrposDiscntLine.setItemId(checkNullValueForString(custOrdItmDesc.getItemId()));
						omsOrposDiscntLine.setLineNo(new BigDecimal(discntLineDesc.getLineNo()));
						omsOrposDiscntLine.setAccountingMethod("DISCOUNT");
						omsOrposDiscntLine.setAdvancedPricingRuleFlag("N");
						log.info("Advanced Pricing Rule Flag : " + discntLineDesc.getAdvancedPricingRuleFlag().value());
						// omsOrposDiscntLine.setAssignmentBasis("OTHR");
						// log.info("Assignment Basis : " +discntLineDesc.getAssignmentBasis().value());
						// Changing the Assignment basis based on the POS input instead of hardcoding
						omsOrposDiscntLine.setAssignmentBasis(discntLineDesc.getAssignmentBasis().value());
						log.info("Assignment Basis : " + discntLineDesc.getAssignmentBasis().value());
						omsOrposDiscntLine.setDamageDiscountFlag("N");
						log.info("Damage Discount Flag : " + discntLineDesc.getDamageDiscountFlag().value());
						omsOrposDiscntLine.setCurrencyCode(checkNullValueForString(discntLineDesc.getCurrencyCode()));
						log.info("Currency Code : " + discntLineDesc.getCurrencyCode());
						omsOrposDiscntLine.setDiscountAmount(checkNullValueForNumber(discntLineDesc.getDiscountAmount()));
						log.info("Discount Amount : " + discntLineDesc.getDiscountAmount());
						omsOrposDiscntLine.setCompletedDiscountAmount(BigDecimal.ZERO);
						log.info("Completed Discount Amount : " + discntLineDesc.getCompletedDiscountAmount());
						omsOrposDiscntLine.setCancelledDiscountAmount(BigDecimal.ZERO);
						log.info("Cancelled Discount Amount : " + discntLineDesc.getCancelledDiscountAmount());
						omsOrposDiscntLine.setReturnedDiscountAmount(BigDecimal.ZERO);
						log.info("Returned Discount Amount : " + discntLineDesc.getReturnedDiscountAmount());
						omsOrposDiscntLine.setUnitDiscountAmount(checkNullValueForNumber(discntLineDesc.getUnitDiscountAmount()));
						log.info("Unit Discount Amount : " + discntLineDesc.getUnitDiscountAmount());
						omsOrposDiscntLine.setDiscountEmployeeId(checkNullValueForString(discntLineDesc.getDiscountEmployeeId()));
						omsOrposDiscntLine.setDiscountMethod("AMT");
						omsOrposDiscntLine.setDiscountRate(BigDecimal.ZERO);
						log.info("Discount Rate : " + discntLineDesc.getDiscountRate());
						// Added for Simple Promo
						if (discntLineDesc.getDiscountRuleId() != null) {
							log.info("discntLineDesc.getDiscountRuleId() : " + discntLineDesc.getDiscountRuleId());
							omsOrposDiscntLine.setDiscountRuleId(discntLineDesc.getDiscountRuleId());
						}
						// omsOrposDiscntLine.setDiscountRuleId("AAA");
						// log.info("Discount Rule Id : " +discntLineDesc.getDiscountRuleId());
						omsOrposDiscntLine.setDiscountScope("ITM");
						log.info("Discount Scope : " + discntLineDesc.getDiscountScope().value());
						omsOrposDiscntLine.setIncludedInBestdealFlag("N");
						log.info("Included In Best deal Flag : " + discntLineDesc.getIncludedInBestdealFlag().value());
						omsOrposDiscntLine.setDiscountReasonCode(checkNullValueForString(discntLineDesc.getDiscountReasonCode()));
						log.info("Discount Reason Code : " + discntLineDesc.getDiscountReasonCode());
						omsOrposDiscntLine.setStoreCouponId(checkNullValueForString(discntLineDesc.getStoreCouponId()));
						log.info("Store Coupon Id : " + discntLineDesc.getStoreCouponId());
						omsOrposDiscntLine.setPromotionComponentDetailId(BigDecimal.ZERO);
						log.info("Promotion Component Detail Id : " + discntLineDesc.getPromotionComponentDetailId());
						omsOrposDiscntLine.setPromotionComponentId(checkNullValueForNumber(discntLineDesc.getPromotionComponentId()));
						log.info("Promotion Component Id : " + discntLineDesc.getPromotionComponentId());
						omsOrposDiscntLine.setPromotionId(checkNullValueForNumber(discntLineDesc.getPromotionId()));
						log.info("Promotion Id : " + discntLineDesc.getPromotionId());
						session.persistOmsOrposDiscntLine(omsOrposDiscntLine);
					}
				}
			}
			/* Start Tax Enablement @tsultana */
			if (custOrdItmDesc.getTaxLineColDesc() != null) {
				log.info("Inside if statement of custOrdItmDesc.getTaxLineColDesc()");
				TaxLineColDesc taxLineColDesc = custOrdItmDesc.getTaxLineColDesc();
				if (taxLineColDesc.getTaxLineDesc() != null) {
					log.info("Inside if statement of taxLineColDesc.getTaxLineDesc()" + taxLineColDesc.getCollectionSize());
					for (TaxLineDesc taxLineDesc : taxLineColDesc.getTaxLineDesc()) {
						log.info("Tax details exists for the item");
						OmsOrposTaxLine omsOrposTaxLine = new OmsOrposTaxLine();
						log.info("Capture Line Item No " + new BigDecimal(custOrdItmDesc.getLineItemNo()));
						omsOrposTaxLine.setCapturedLineItemNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
						log.info("omsOrposCustOrderId" + checkNullValueForNumber(omsOrposCustOrderId));
						omsOrposTaxLine.setOmsOrposCustOrderId(checkNullValueForNumber(omsOrposCustOrderId));
						log.info("Item Id " + checkNullValueForString(custOrdItmDesc.getItemId()));
						omsOrposTaxLine.setItemId(checkNullValueForString(custOrdItmDesc.getItemId()));
						log.info("Tax Line No  " + checkNullValueForString(custOrdItmDesc.getItemId()));
						omsOrposTaxLine.setLineNo(new BigDecimal(taxLineDesc.getLineNo()));
						log.info("Tax Authority Id " + checkNullValueForString(custOrdItmDesc.getItemId()));
						omsOrposTaxLine.setTaxAuthorityId(checkNullValueForNumber(taxLineDesc.getTaxAuthorityId()));
						if (taxLineDesc.getTaxGroupId() != null) {
							log.info("Tax Group Id " + taxLineDesc.getTaxGroupId());
							omsOrposTaxLine.setTaxGroupId(taxLineDesc.getTaxGroupId());
						}
						if (taxLineDesc.getTaxTypeCode() != null) {
							log.info("Tax Type Code " + taxLineDesc.getTaxTypeCode());
							omsOrposTaxLine.setTaxTypeCode(taxLineDesc.getTaxTypeCode());
						}
						if (taxLineDesc.getTaxHolidayFlag() != null) {
							log.info("Tax Holiday Flag " + taxLineDesc.getTaxHolidayFlag().value());
							omsOrposTaxLine.setTaxHolidayFlag(taxLineDesc.getTaxHolidayFlag().value());
						}
						log.info("Currency Code : " + taxLineDesc.getCurrencyCode());
						omsOrposTaxLine.setCurrencyCode(taxLineDesc.getCurrencyCode());
						log.info("Taxable Amount " + checkNullValueForNumber(taxLineDesc.getTaxableAmount()));
						omsOrposTaxLine.setTaxableAmount(checkNullValueForNumber(taxLineDesc.getTaxableAmount()));
						log.info("Tax Amount : " + checkNullValueForNumber(taxLineDesc.getTaxAmount()));
						omsOrposTaxLine.setTaxAmount(checkNullValueForNumber(taxLineDesc.getTaxAmount()));
						omsOrposTaxLine.setCancelledTaxAmount(BigDecimal.ZERO);
						omsOrposTaxLine.setCompletedTaxAmount(BigDecimal.ZERO);
						omsOrposTaxLine.setReturnedTaxAmount(BigDecimal.ZERO);
						if (taxLineDesc.getInclusiveTaxFlag() != null) {
							log.info("Inclusive Tax Flag " + taxLineDesc.getInclusiveTaxFlag().value());
							omsOrposTaxLine.setInclusiveTaxFlag(taxLineDesc.getInclusiveTaxFlag().value());
						}
						if (taxLineDesc.getTaxMode() != null) {
							log.info("Tax Mode " + taxLineDesc.getTaxMode().value());
							omsOrposTaxLine.setTaxMode(taxLineDesc.getTaxMode().value());
						}
						if (taxLineDesc.getTaxModReasonCode() != null) {
							log.info("Tax Mode Reason Code " + taxLineDesc.getTaxModReasonCode());
							omsOrposTaxLine.setTaxModReasonCode(taxLineDesc.getTaxModReasonCode());
						}
						if (taxLineDesc.getTaxModScope() != null) {
							log.info("Tax Mode Scope " + taxLineDesc.getTaxModScope().value());
							omsOrposTaxLine.setTaxModScope(taxLineDesc.getTaxModScope().value());
						}
						if (taxLineDesc.getTaxRate() != null) {
							log.info("Tax Rate " + taxLineDesc.getTaxRate());
							omsOrposTaxLine.setTaxRate(taxLineDesc.getTaxRate());
						} else {
							log.info("Tax Rate is null");
							omsOrposTaxLine.setTaxRate(BigDecimal.ZERO);
						}
						if (taxLineDesc.getTaxRuleName() != null) {
							log.info("Tax Rule Name " + taxLineDesc.getTaxRuleName());
							omsOrposTaxLine.setTaxRuleName(taxLineDesc.getTaxRuleName());
						}
						if (taxLineDesc.getTaxAuthorityName() != null) {
							log.info("Tax Authority Name " + taxLineDesc.getTaxAuthorityName());
							omsOrposTaxLine.setTaxAuthorityName(taxLineDesc.getTaxAuthorityName());
						}
						session.persistOmsOrposTaxLine(omsOrposTaxLine);
					}
				}
			}
			/* End Tax Enablement @tsultana */
		}
	}

	public void saveCustOrdFulColDesc(CustOrdFulColDesc custOrdFulColDesc) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		for (CustOrdFulDesc custOrdFulDesc : custOrdFulColDesc.getCustOrdFulDesc()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Persisting in fulfill orde");
			OmsOrposCustOrdFul omsOrposCustOrdFul = new OmsOrposCustOrdFul();
			omsOrposCustOrdFul.setOmsOrposCustOrderId(omsOrposCustOrderId);
			// add seq no
			omsOrposCustOrdFul.setCustOrdFulSeqNo(checkNullValueForNumber(new BigDecimal(custOrdFulDesc.getSeqNo())));
			// omsOrposCustOrdFul.setFulfillOrderId(checkNullValueForNumber(new
			// BigDecimal(custOrdFulDesc.getFulfillOrderId())));
			if (custOrdFulDesc.getFulfillLocId() != null) {
				omsOrposCustOrdFul.setFulfillLocId(checkNullValueForNumber(new BigDecimal(custOrdFulDesc.getFulfillLocId())));
			}
			if (custOrdFulDesc.getFulfillLocType() != null) {
				omsOrposCustOrdFul.setFulfillLocType(custOrdFulDesc.getFulfillLocType().value());
			}
			omsOrposCustOrdFul.setDeliveryType(custOrdFulDesc.getDeliveryType().value());
			if (custOrdFulDesc.getPartialDeliveryInd() != null) {
				omsOrposCustOrdFul.setPartialDeliveryInd(checkNullValueForString(custOrdFulDesc.getPartialDeliveryInd().value()));
			}
			if (custOrdFulDesc.getShipToFulfillLocFlag() != null) {
				omsOrposCustOrdFul.setShipToFulfillLocFlag(checkNullValueForString(custOrdFulDesc.getShipToFulfillLocFlag().value()));
			}
			omsOrposCustOrdFul.setCarrierCode(checkNullValueForString(custOrdFulDesc.getCarrierCode()));
			omsOrposCustOrdFul.setCarrierServiceCode(checkNullValueForString(custOrdFulDesc.getCarrierServiceCode()));
			if (custOrdFulDesc.getConsumerDeliveryDate() != null) {
				omsOrposCustOrdFul.setConsumerDeliveryDate(new Timestamp(custOrdFulDesc.getConsumerDeliveryDate().toGregorianCalendar().getTimeInMillis()));
			}
			omsOrposCustOrdFul.setComments(checkNullValueForString(custOrdFulDesc.getComments()));
			session.persistOmsOrposCustOrdFul(omsOrposCustOrdFul);
			DeliveryDestDtl deliveryDestDt = custOrdFulDesc.getDeliveryDestDtl();
			if (deliveryDestDt.getContactDesc() != null) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside delivery detail ,contact");
				ContactDesc contactDesc = deliveryDestDt.getContactDesc();
				saveContactDesc(contactDesc, "FULFILLMENT", String.valueOf(custOrdFulDesc.getSeqNo()));
				OmsOrposContact omsOrposContact = session.getOmsOrposContactFindByFulSeqNo(omsOrposCustOrderId, new BigDecimal(custOrdFulDesc.getSeqNo()));
				if (contactDesc.getPhones() != null) {
					Phones phones = contactDesc.getPhones();
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsOrposContact.getContactSeq()" + omsOrposContact.getContactSeq());
					savePhones(phones, omsOrposContact.getContactSeq());
				}
				if (contactDesc.getEmails() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside email");
					Emails emails = contactDesc.getEmails();
					saveEmails(emails, omsOrposContact.getContactSeq());
				}
				if (deliveryDestDt.getGeoAddrDesc() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside geo address");
					GeoAddrDesc geoAddrDesc = deliveryDestDt.getGeoAddrDesc();
					OmsOrposGeoaddr omsOrposGeoaddr = new OmsOrposGeoaddr();
					omsOrposGeoaddr.setAddressAlias(geoAddrDesc.getAddressAlias());
					omsOrposGeoaddr.setOmsOrposCustOrderId(omsOrposCustOrderId);
					omsOrposGeoaddr.setAddress1(geoAddrDesc.getAddress1());
					omsOrposGeoaddr.setCity(geoAddrDesc.getCity());
					omsOrposGeoaddr.setStateCode(geoAddrDesc.getStateCode());
					omsOrposGeoaddr.setCountryCode(geoAddrDesc.getCountryCode());
					omsOrposGeoaddr.setPostalCode(geoAddrDesc.getPostalCode());
					omsOrposGeoaddr.setCustOrdFulSeqNo(new BigDecimal(custOrdFulDesc.getSeqNo()));
					session.persistOmsOrposGeoaddr(omsOrposGeoaddr);
					/*
					 * BillingDestDtl billingDestDtl= custOrdFulDesc.getBillingDestDtl();
					 * ContactDesc contactDesc2= billingDestDtl.getContactDesc(); Phones
					 * phones2=contactDesc.getPhones(); List<PhoneDesc> phoneDescList2=
					 * phones2.getPhoneDesc(); for(PhoneDesc phoneDesc:phoneDescList2) {
					 * 
					 * } Emails emails2= contactDesc.getEmails(); List<EmailDesc> emailDescList2=
					 * emails2.getEmailDesc(); for (EmailDesc emailDesc:emailDescList2) {
					 * 
					 * } GeoAddrDesc geoAddrDesc= billingDestDtl.getGeoAddrDesc();
					 */
				}
			}
		}
	}

	public void savePaymentColDesc(CustOrderDesc cusOrderDesc, PaymentColDesc paymentColDesc) throws SOAPException {
		log.info("Started persisting in payment");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// paymentColDesc = cusOrderDesc.getPaymentColDesc();
		List<PaymentDesc> paymentDescList = paymentColDesc.getPaymentDesc();
		log.info("paymentDescList.size() " + paymentDescList.size());
		for (PaymentDesc paymentDesc : paymentDescList) {
			OmsOrposPayment omsOrposPayment = new OmsOrposPayment();
			omsOrposPayment.setOmsOrposCustOrderId(omsOrposCustOrderId);
			log.info("seq No " + paymentDesc.getSeqNo());
			omsOrposPayment.setPaymentSeqNo(new BigDecimal(paymentDesc.getSeqNo()));
			omsOrposPayment.setPaymentType(paymentDesc.getPaymentType().value());
			omsOrposPayment.setCurrencyCode(paymentDesc.getCurrencyCode());
			omsOrposPayment.setAlternateAmount(paymentDesc.getAlternateAmount());
			omsOrposPayment.setAlternateCurrencyCode(paymentDesc.getAlternateCurrencyCode());
			omsOrposPayment.setAmount(paymentDesc.getAmount());
			log.info("The Payment Type entered is: ---" + paymentDesc.getPaymentType());
			// ==============================================CREDIT=======================================================
			if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("CREDIT"))) {
				if (paymentDesc.getCreditDebitTender() != null) {
					log.info("Credit card payment");
					CreditDebitTender creditDebitTender = paymentDesc.getCreditDebitTender();
					omsOrposPayment.setMaskedAccountNumber(checkNullValueForString(creditDebitTender.getMaskedAccountNumber()));
					log.info("masked acct no set");
					omsOrposPayment.setCardToken(checkNullValueForString(creditDebitTender.getCardToken()));
					log.info("Card token set");
					log.info("fetching cardType ");
					if (creditDebitTender.getCardType() != null) {
						omsOrposPayment.setCardType(checkNullValueForString(creditDebitTender.getCardType().value()));
					}
					log.info("creditDebitTender.getAuthorizationCode() " + creditDebitTender.getAuthorizationCode());
					omsOrposPayment.setAuthorizationCode(checkNullValueForString(creditDebitTender.getAuthorizationCode()));
					if (creditDebitTender.getAuthorizationDatetime() != null) {
						omsOrposPayment.setAuthorizationDatetime(new Timestamp(creditDebitTender.getAuthorizationDatetime().toGregorianCalendar().getTimeInMillis()));
					}
					if (creditDebitTender.getAuthorizationMethod().value() != null) {
						log.info("creditDebitTender.getAuthorizationMethod().value() " + creditDebitTender.getAuthorizationMethod().value());
						omsOrposPayment.setAuthorizationMethod(checkNullValueForString(creditDebitTender.getAuthorizationMethod().value()));
					}
					log.info("creditDebitTender.getSettlementData() " + creditDebitTender.getSettlementData());
					omsOrposPayment.setSettlementData(checkNullValueForString(creditDebitTender.getSettlementData()));
				}
			} else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("DEBIT"))) {
				if (paymentDesc.getCreditDebitTender() != null) {
					log.info("Credit card payment");
					CreditDebitTender creditDebitTender = paymentDesc.getCreditDebitTender();
					omsOrposPayment.setMaskedAccountNumber(checkNullValueForString(creditDebitTender.getMaskedAccountNumber()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "masked acct no set");
					omsOrposPayment.setCardToken(checkNullValueForString(creditDebitTender.getCardToken()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Card token set");
					log.info("checking for creditDebitTender.getCardType() ");
					if (creditDebitTender.getCardType() != null) {
						omsOrposPayment.setCardType(checkNullValueForString(creditDebitTender.getCardType().value()));
					}
					omsOrposPayment.setAuthorizationCode(checkNullValueForString(creditDebitTender.getAuthorizationCode()));
					if (creditDebitTender.getAuthorizationDatetime() != null) {
						omsOrposPayment.setAuthorizationDatetime(new Timestamp(creditDebitTender.getAuthorizationDatetime().toGregorianCalendar().getTimeInMillis()));
					}
					omsOrposPayment.setAuthorizationMethod(checkNullValueForString(creditDebitTender.getAuthorizationMethod().value()));
					omsOrposPayment.setSettlementData(checkNullValueForString(creditDebitTender.getSettlementData()));
				}
			} else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("CHECK"))) {
				if (paymentDesc.getCheckTender() != null) {
					omsOrposPayment.setBankId(paymentDesc.getCheckTender().getBankId());
					omsOrposPayment.setAccountNumber(paymentDesc.getCheckTender().getAccountNumber());
					omsOrposPayment.setMicrNumber(paymentDesc.getCheckTender().getMicrNumber());
					omsOrposPayment.setCheckNumber(paymentDesc.getCheckTender().getCheckNumber());
					omsOrposPayment.setAuthorizationCode(paymentDesc.getCheckTender().getAuthorizationCode());
				}
			}
			// ========================================PURCHASEORDER======================================
			else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("PURCHASEORDER"))) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside PURCHASEORDER");
				PurchaseOrdTender purchaseOrdTender = new PurchaseOrdTender();
				if (paymentDesc.getPurchaseOrdTender() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "paymentDesc.getPurchaseOrdTender().getAgentName() " + paymentDesc.getPurchaseOrdTender().getAgentName());
					omsOrposPayment.setAgentName(paymentDesc.getPurchaseOrdTender().getAgentName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "paymentDesc.getPurchaseOrdTender().getPurchaseOrderNumber() " + paymentDesc.getPurchaseOrdTender().getPurchaseOrderNumber());
					omsOrposPayment.setPurchaseOrderNumber(paymentDesc.getPurchaseOrdTender().getPurchaseOrderNumber());
				}
			}
			// ===================================================STORECREDIT================================================
			else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("STORECREDIT"))) {
				if (paymentDesc.getStoreCreditTender() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside store credit tender");
					if (paymentDesc.getStoreCreditTender().getCertificateType() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "setting value for certificate type " + paymentDesc.getStoreCreditTender().getCertificateType().value());
						omsOrposPayment.setCertificateType(paymentDesc.getStoreCreditTender().getCertificateType().value());
					}
					if (paymentDesc.getStoreCreditTender().getFirstName() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "setting first name as " + paymentDesc.getStoreCreditTender().getFirstName());
						omsOrposPayment.setFirstName(checkNullValueForString(paymentDesc.getStoreCreditTender().getFirstName()));
					}
					if (paymentDesc.getStoreCreditTender().getLastName() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "setting last name as " + paymentDesc.getStoreCreditTender().getLastName());
						omsOrposPayment.setLastName(paymentDesc.getStoreCreditTender().getLastName());
					}
					if (paymentDesc.getStoreCreditTender().getPersonalIdType() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "setting personal Id " + paymentDesc.getStoreCreditTender().getPersonalIdType());
						omsOrposPayment.setPersonalIdType(paymentDesc.getStoreCreditTender().getPersonalIdType());
					}
					if (paymentDesc.getStoreCreditTender().getState() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "State " + paymentDesc.getStoreCreditTender().getState());
						omsOrposPayment.setState(paymentDesc.getStoreCreditTender().getState().value());
					}
					if (paymentDesc.getStoreCreditTender().getStoreCreditId() != null) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "StoreCreditId " + paymentDesc.getStoreCreditTender().getStoreCreditId());
						omsOrposPayment.setStoreCreditId(paymentDesc.getStoreCreditTender().getStoreCreditId());
					}
				}
			} else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("GIFTCARD"))) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside gift card tender if condition");
				if (paymentDesc.getGiftCardTender() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Entered the if loop for GiftCardTender");
					GiftCardTender giftCardTender = paymentDesc.getGiftCardTender();
					omsOrposPayment.setCardNumber(checkNullValueForString(giftCardTender.getCardNumber()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "card number is set");
					omsOrposPayment.setGifcardAuthorizationCode(checkNullValueForString(giftCardTender.getAuthorizationCode()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Gift Card Authoization code");
					omsOrposPayment.setGifcardAuthorizationDatetime(new Timestamp(new java.util.Date().getTime()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Authorization date time set");
					if (giftCardTender.getAuthorizationMethod() != null) {
						omsOrposPayment.setGifcardAuthorizationMethod(checkNullValueForString(giftCardTender.getAuthorizationMethod().value()));
					}
					// omsOrposPayment.setGifcardAuthorizationMethod(checkNullValueForString(giftCardTender.getAuthorizationMethod().value()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Gift card Authorization method");
					if (giftCardTender.getCreditFlag() != null) {
						omsOrposPayment.setCreditFlag(checkNullValueForString(giftCardTender.getCreditFlag().value()));
					}
					// omsOrposPayment.setCreditFlag(checkNullValueForString(giftCardTender.getCreditFlag().value()));
					log.info("Credit flag is set");
					if (giftCardTender.getEntryMethod() != null) {
						omsOrposPayment.setGifcardEntryMethod(checkNullValueForString(giftCardTender.getEntryMethod().value()));
					}
					// omsOrposPayment.setGifcardEntryMethod(checkNullValueForString(giftCardTender.getEntryMethod().value()));
					log.info("Gift Card Entry method is set");
					omsOrposPayment.setOriginalBalance(checkNullValueForNumber(giftCardTender.getOriginalBalance()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Original balance is set");
					omsOrposPayment.setRemainingBalance(checkNullValueForNumber(giftCardTender.getRemainingBalance()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Remaining balance is set");
					omsOrposPayment.setGifcardsettlementData(checkNullValueForString(giftCardTender.getSettlementData()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Exited the if loop for GiftCardTender");
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "After gift card tender if condition");
			} else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("COUPON"))) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside the coupon tender if condition");
				if (paymentDesc.getCouponTender() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Entered the if loop for CouponTender");
					CouponTender couponTender = paymentDesc.getCouponTender();
					if (couponTender.getCouponType() != null) {
						omsOrposPayment.setCouponType(checkNullValueForString(couponTender.getCouponType().value()));
						log.info("omsCustOrdNo " + omsCustOrdNo + "Coupon type is set");
					}
					if (couponTender.getEntryMethod() != null) {
						omsOrposPayment.setCoupontenderEntryMethod(checkNullValueForString(couponTender.getEntryMethod().value()));
						log.info("omsCustOrdNo " + omsCustOrdNo + "Coupon tender entry method is set");
					}
					omsOrposPayment.setCouponNumber(checkNullValueForString(couponTender.getCouponNumber()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "Exited the if loop for CouponTender");
				}
			} else if (PaymentType.MALLCERT.equals(paymentDesc.getPaymentType())) {
				GiftCertTender giftCertTender = paymentDesc.getGiftCertTender();
				if (giftCertTender != null) {
					if (giftCertTender.getCertificateType() != null) {
						omsOrposPayment.setCertificateType(giftCertTender.getCertificateType().toString());
					}
					if (giftCertTender.getIssueLocationType() != null) {
						omsOrposPayment.setIssueLocationType(giftCertTender.getIssueLocationType().toString());
					}
					if (giftCertTender.getIssueLocationId() != null) {
						omsOrposPayment.setIssueLocationId(BigDecimal.valueOf(giftCertTender.getIssueLocationId()));
					}
					omsOrposPayment.setSerialNumber(giftCertTender.getSerialNumber());
				}
			} else if (PaymentType.TRAVCHECK.equals(paymentDesc.getPaymentType())) {
				if (paymentDesc.getTravelCheckTender() != null) {
					omsOrposPayment.setCheckCount(paymentDesc.getTravelCheckTender().getCheckCount());
				}
			}
			session.persistOmsOrposPayment(omsOrposPayment);
		}
	}

	public void saveCustOrdDelColDesc(CustOrdDelColDesc custOrdDelColDesc) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<CustOrdDelDesc> custOrdDelDescList = custOrdDelColDesc.getCustOrdDelDesc();
		for (CustOrdDelDesc custOrdDelDesc : custOrdDelDescList) {
		}
	}

	public String checkNullValueForString(String value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public BigDecimal checkNullValueForNumber(BigDecimal value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public void findSourceLocation(CustOrderDesc custOrderDesc, BigDecimal omsOrposCustOrderId)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("****************** Begin of findSourceLocation method *****************");
		long SOH = 0L;
		String fulFillLocType = "S";
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal nextLoc = BigDecimal.ONE;
		BigDecimal sourceLocId = BigDecimal.ZERO;
		BigDecimal fulfillLocId = BigDecimal.ZERO;
		// List<CustOrdFulDesc>
		// custOrdFulDesc=custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc();
		if (custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId() != null) {
			nextLoc = new BigDecimal(custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "nextLoc------" + nextLoc);
			sourceLocId = new BigDecimal(custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId----" + sourceLocId);
			fulfillLocId = new BigDecimal(custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId------" + fulfillLocId);
		}
		try {
			log.info("omsCustOrdNo " + omsCustOrdNo + "----Inside the try block of findSourceLocation method---");
			// SourceLocIdentify sourceLocIdentify=new SourceLocIdentify();
			log.info("omsCustOrdNo " + omsCustOrdNo + "----created the object for SourceLocIdentify----");
			fulfillDetailMap = posSourceLocationIndentifier.processSplitOrders(omsCustOrdNo, custOrderDesc, omsOrposCustOrderId);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Successfully called the processSplitOrders method");
			if (fulfillDetailMap != null && fulfillDetailMap.size() > 0) {
				log.info(" FulFill Detail Map Set is" + fulfillDetailMap.keySet() + "------------->");
				log.info("omsCustOrdNo " + omsCustOrdNo + "calling checkCreateOrReserveOrder ");
				checkCreateOrReserveOrder(omsCustOrdNo, custOrderDesc, omsOrposCustOrderId);
				log.info("omsCustOrdNo " + omsCustOrdNo + "---Sucessfully called the checkCreateOrReserveOrder method---");
			}
		} catch (Exception e) {
			log.info("********************** Error occured " + e.getMessage() + "***********************");
			if (fulfillDetailMap != null && fulfillDetailMap.size() > 0) {
				log.info(" FullFill ment Detail map size is " + fulfillDetailMap.size() + " Key Set is" + fulfillDetailMap.keySet());
				OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
				log.info("omsCustOrdNo " + omsCustOrdNo + "retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
				omsOrposCustOrderHead.setStatus("F");
				session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				omsCustOrdHead.setStatus("F");
				session.mergeOmsCustOrdHead(omsCustOrdHead);
				log.error("omsCustOrdNo " + omsCustOrdNo + "Error in checkCreateOrReserveOrder" + e);
			}
		}
	}

	public void checkCreateOrReserveOrder(BigDecimal omsCustOrdNo, CustOrderDesc custOrderDesc, BigDecimal omsOrposCustOrderId)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("******************** Begin checkCreateOrReserveOrder method **************************");
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside checkCreateOrReserveOrder");
		// Step 5 : Call RMS and SIM webservice in case of NON SADAD Payment
		OMSOrderFulfillment omsOrderFulfillment = new OMSOrderFulfillment();
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsSourceLocIdentify map----" + posSourceLocationIndentifier.getMap(omsCustOrdNo));
		log.info("omsCustOrdNo " + omsCustOrdNo);
		log.info("omsCustOrdNo " + omsCustOrdNo + "CustOrderDesc=====" + custOrderDesc);
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsOrposCustOrderId=====" + omsOrposCustOrderId);
		log.info("<----------------*****Calling of processNonSadad method *****------------------------------------------------->");
		try {
			fulfillDetailMap = omsOrderFulfillment.processNonSadad(posSourceLocationIndentifier.getMap(omsCustOrdNo), omsCustOrdNo, custOrderDesc, omsOrposCustOrderId);
		} catch (Exception e) {
			log.info(" Error Occured When processNonSadad method executed." + e.getMessage());
			log.info(" Map Size is " + fulfillDetailMap.size() + ".........." + fulfillDetailMap.keySet());
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "after calling processNonSadad");
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillDetailMap.size() " + fulfillDetailMap.size());
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillDetailMap.keySet() " + fulfillDetailMap.keySet());
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String siebelStatus = session.getOmsSystemParametersFindIndValue("CALL_SIEBEL", "OMS_SYSTEM_OPTION");
		log.info("omsCustOrdNo " + omsCustOrdNo + "siebelStatus " + siebelStatus);
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		if (siebelStatus.equals("Y")) {
			interfacePersistence.callSeibelWebservice(custOrderDesc, omsCustOrdNo);
		}
		log.info("*********************End of checkCreateOrReserveOrder Method *****************************");
	}

	public String checkOrderStatus(BigDecimal omsOrposCustOrderId, CustOrderDesc custOrderDesc)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalStateWSFaultException, IllegalStateWSFaultException, IllegalStateWSFaultException, IllegalArgumentWSFaultException,
			IllegalArgumentWSFaultException, ValidationWSFaultException, ValidationWSFaultException, ValidationWSFaultException, ValidationWSFaultException {
		log.info(" *** Begin of checkOrderStatus Method **********");
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside checkOrderStatus");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String orderStatus = null;
		try {
			OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsOrposCustOrderHead.getOmsOrposCustOrderId() " + omsOrposCustOrderHead.getOmsOrposCustOrderId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
			log.info("omsCustOrdNo " + omsCustOrdNo + "OmsOrposCustOrderId " + omsOrposCustOrderHead.getOmsOrposCustOrderId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo Number " + omsCustOrdHead.getOmsCustOrdNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdHead.getStatus() " + omsCustOrdHead.getStatus());
			List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsBackOrderDtlList.size() " + omsBackOrderDtlList.size());
			if (omsCustOrdHead.getStatus().equals("S")) {
				for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside inside OmsCustOrdItem loop ");
					if (omsCustOrdItem.getQtyOrderedSuom() == omsCustOrdItem.getQtyCancelled() || omsCustOrdItem.getQtyCancelled().intValue() != 0) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "setting ORPOS Status in if");
						omsCustOrdHead.setStatus("F");
						omsOrposCustOrderHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCustOrdHead);
						session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
						orderStatus = "CANCELED";
						break;
					}
				}
			}
			if (omsCustOrdHead.getStatus().equals("S")) {
				omsOrposCustOrderHead.setOrderStatus("FILLED");
				omsOrposCustOrderHead.setStatus("S");
				session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
				orderStatus = "FILLED";
			} else {

				orderStatus = "CANCELED";
			}
			try {
				log.info("omsCustOrdNo " + omsCustOrdNo + "checking OmsCoFulfillDetail");
				List<OmsCoFulfillDetail> omsCoFulfillDetail = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "omsCoFulfillDetail " + omsCoFulfillDetail.size());
				if (omsCoFulfillDetail.size() == 0 && omsBackOrderDtlList.size() == 0) {
					omsCustOrdHead.setStatus("F");
					omsOrposCustOrderHead.setOrderStatus("CANCELED");
					omsOrposCustOrderHead.setStatus("F");
					orderStatus = "x";
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					log.info("omsCustOrdNo " + omsCustOrdNo + "updating OmsOrposCustOrderHead");
					session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
					log.info("omsCustOrdNo " + omsCustOrdNo + "updated OmsOrposCustOrderHead");
				} else {
					for (OmsCoFulfillDetail omsCoFulfillDetailLoop : omsCoFulfillDetail) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "omsCoFulfillDetailLoop.getOmsCustOrdNo() " + omsCoFulfillDetailLoop.getOmsCustOrdNo());
						log.info("omsCustOrdNo " + omsCustOrdNo + "omsCoFulfillDetailLoop.getFulfillStatus() " + omsCoFulfillDetailLoop.getFulfillStatus());
						if (omsCoFulfillDetailLoop.getFulfillStatus().equals("F")) {
							omsCustOrdHead.setStatus("F");
							omsOrposCustOrderHead.setOrderStatus("CANCELED");
							omsOrposCustOrderHead.setStatus("F");
							orderStatus = "CANCELED";
							session.mergeOmsCustOrdHead(omsCustOrdHead);
							log.info("omsCustOrdNo " + omsCustOrdNo + "updating OmsOrposCustOrderHead");
							session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
							log.info("omsCustOrdNo " + omsCustOrdNo + "updated OmsOrposCustOrderHead");
							break;
						}
					}
				}
			} catch (Exception e) {
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "orderStatus " + orderStatus);
			return orderStatus;
		} catch (Exception e) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside catch block");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposCustOrderHead.setStatus("F");
			omsCustOrdHead.setStatus("F");
			log.info("omsCustOrdNo " + omsCustOrdNo + "updating omsCustOrdHead");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			log.info("omsCustOrdNo " + omsCustOrdNo + "updating OmsOrposCustOrderHead");
			session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
			orderStatus = "CANCELED";
		}
		return orderStatus;
	}

	public String checkCustomerOrderStatus(CustOrderDesc custOrderDesc) throws SOAPException {
		String status = null;
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside checkCustomerOrderStatus method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposCustOrderHead omsOrposCustOrderHead = null;
		try {
			omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(custOrderDesc.getCustomerOrderId(), "S");
			status = "S";
		} catch (Exception e) {
			log.info("no record exist for Customer Order No" + custOrderDesc.getCustomerOrderId() + "with status S");
			status = "F";
			// custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_101");
		}
		log.info("return status " + status);
		return status;
	}

	public void callRollBackMethod(String customerOrderNo) throws EntityAlreadyExistsWSFaultException, IllegalStateWSFaultException, IllegalStateWSFaultException, IllegalStateWSFaultException,
			IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, ValidationWSFaultException, ValidationWSFaultException, ValidationWSFaultException, ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException, SOAPException {
		log.info("******* Begin of callRollBackMethod ********* ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		log.info("omsCustOrdNo" + omsCustOrdNo + " inside callRollBackMethod");
		if (fulfillDetailMap != null && fulfillDetailMap.size() > 0) {
			try {
				// BigDecimal count=new
				// BigDecimal(session.getOrdCustCountofCustomerOrderNo(customerOrderNo).longValue());
				// log.info("omsCustOrdNo"+omsCustOrdNo+"count "+count);
				log.info("omsCustOrdNo" + omsCustOrdNo + "fulfillDetailMap.size() " + fulfillDetailMap.size());
				// rollback(fulfillDetailMap);
				log.info("Calling rollback method from OMSUtilCommons....");
				oMSUtilCommons.rollback(customerOrderNo);
			} catch (Exception e) {
				log.info("No Record exist in RMS or SIM");
			}
			/*
			 * try { List<OmsBackOrderDtl> omsBackOrderDtlList=
			 * session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"omsBackOrderDtlList "
			 * +omsBackOrderDtlList.size()); if(omsBackOrderDtlList.size()!=0) {
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"calling BO WS for cancelling");
			 * for(OmsBackOrderDtl omsBackOrderDtl:omsBackOrderDtlList) {
			 * 
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"omsBackOrderDtl.getItem() "
			 * +omsBackOrderDtl.getItem());
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"omsBackOrderDtl.getSourceQty() "
			 * +omsBackOrderDtl.getSourceQty().negate());
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"omsBackOrderDtl.getSourceLoc() "
			 * +omsBackOrderDtl.getSourceLoc());
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"omsBackOrderDtl.getSourceLocType() "
			 * +omsBackOrderDtl.getSourceLocType()); log.info(
			 * "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			 * String locType=omsBackOrderDtl.getSourceLocType();
			 * log.info("++++++++++locType++++++++++++ "+locType);
			 * log.info("intializing channelId and physicalWH to zero ");
			 * 
			 * BigDecimal channelId=BigDecimal.ZERO; BigDecimal physicalWH=BigDecimal.ZERO;
			 * log.info("checking for omsBackOrderDtl.getSourceLocType() if condition : ");
			 * if(omsBackOrderDtl.getSourceLocType().toString().equalsIgnoreCase("WH")) {
			 * 
			 * log.info("omsCustOrdNo"+omsCustOrdNo+"inside WH equal condition");
			 * 
			 * List<Object[]> tempWhObject=
			 * session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc()); for(Object[]
			 * result:tempWhObject) { physicalWH= new BigDecimal(result[0].toString());
			 * channelId=new BigDecimal(result[1].toString());
			 * log.info("omsCustOrdNo "+omsCustOrdNo
			 * +"WH="+result[0]+"channel id="+result[1]); }
			 * log.info("omsCustOrdNo "+omsCustOrdNo +"physicalWH "+physicalWH);
			 * log.info("omsCustOrdNo "+omsCustOrdNo +"channelId "+channelId);
			 * log.info("omsCustOrdNo"+
			 * omsCustOrdNo+" calling RMSBackorderWS for omsBackOrderDtl.getSourceLocType(): "
			 * +omsBackOrderDtl.getSourceLocType());
			 * oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),(omsBackOrderDtl.
			 * getSourceQty().negate()),physicalWH.longValue(),omsBackOrderDtl.
			 * getSourceLocType(),"EA", channelId);
			 * 
			 * } else { channelId=BigDecimal.ZERO; log.info("omsCustOrdNo "+omsCustOrdNo
			 * +"rollbacking  BO for ST");
			 * oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
			 * (omsBackOrderDtl.getSourceQty().negate()),
			 * omsBackOrderDtl.getSourceLoc().longValue(),
			 * omsBackOrderDtl.getSourceLocType(),"EA", channelId);
			 * 
			 * }
			 * 
			 * } } } catch(Exception e) {
			 * 
			 * }
			 */
		}
		/*
		 * else { log.info("omsCustOrdNo"+omsCustOrdNo+
		 * "inside else block for fulfillDetailMap.size()==0/* ");
		 */
		try {
			List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdNo" + omsCustOrdNo + "omsBackOrderDtlList " + omsBackOrderDtlList.size());
			if (omsBackOrderDtlList.size() != 0) {
				log.info("omsCustOrdNo" + omsCustOrdNo + "calling BO WS for cancelling");
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
					BigDecimal channelId = null;
					log.info("omsCustOrdNo" + omsCustOrdNo + "omsBackOrderDtl.getItem() " + omsBackOrderDtl.getItem());
					log.info("omsCustOrdNo" + omsCustOrdNo + "omsBackOrderDtl.getSourceQty() " + omsBackOrderDtl.getSourceQty().negate());
					log.info("omsCustOrdNo" + omsCustOrdNo + "omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
					log.info("omsCustOrdNo" + omsCustOrdNo + "omsBackOrderDtl.getSourceLocType() " + omsBackOrderDtl.getSourceLocType());
					log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					String locType = omsBackOrderDtl.getSourceLocType();
					log.info("++++++++++locType++++++++++++ " + locType);
					log.info("intializing channelId and physicalWH to zero ");
					BigDecimal physicalWH = BigDecimal.ZERO;
					log.info("checking for omsBackOrderDtl.getSourceLocType() if condition : ");
					if (omsBackOrderDtl.getSourceLocType().toString().equalsIgnoreCase("WH")) {
						log.info("omsCustOrdNo" + omsCustOrdNo + "inside WH equal condition");
						List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
						for (Object[] result : tempWhObject) {
							physicalWH = new BigDecimal(result[0].toString());
							channelId = new BigDecimal(result[1].toString());
							log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
						}
						log.info("omsCustOrdNo " + omsCustOrdNo + "physicalWH " + physicalWH);
						log.info("omsCustOrdNo " + omsCustOrdNo + "channelId " + channelId);
						log.info("omsCustOrdNo" + omsCustOrdNo + " calling RMSBackorderWS for omsBackOrderDtl.getSourceLocType(): " + omsBackOrderDtl.getSourceLocType());
						oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (omsBackOrderDtl.getSourceQty().negate()), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
								channelId);
					} else {
						channelId = BigDecimal.ZERO;
						log.info("omsCustOrdNo " + omsCustOrdNo + "rollbacking  BO for ST");
						oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (omsBackOrderDtl.getSourceQty().negate()), omsBackOrderDtl.getSourceLoc().longValue(),
								omsBackOrderDtl.getSourceLocType(), "EA", channelId);
					}
					// oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
					// (omsBackOrderDtl.getSourceQty().negate()),
					// omsBackOrderDtl.getSourceLoc().longValue(),
					// omsBackOrderDtl.getSourceLocType(),"EA", channelId);
				}
			}
		} catch (Exception e) {
		}
	}

	public void rollback(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfullDetailMap)
			throws EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			SOAPException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "Starting rollback");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfullDetailMap map size=" + fulfullDetailMap.size());
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("omsCustOrdHead.getCustOrderNo() " + omsCustOrdHead.getCustOrderNo());
		// Ordcust
		// ordCust=session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(),
		// fulfillOrderNo)
		if (fulfullDetailMap.size() >= 1) {
			for (BigDecimal key : fulfullDetailMap.keySet()) {
				log.info("key " + key);
				ArrayList<OmsTempCoFo> list = fulfullDetailMap.get(key);
				log.info("omsCustOrdNo " + omsCustOrdNo + "List size" + list.size());
				log.info("omsCustOrdNo " + omsCustOrdNo + "Processing app=" + list.get(0).getProcessingApp());
				if ("CARRERA".equals(list.get(0).getProcessingApp())) {
					interfacePersistence.callCarreraCancellation(list.get(0));
				} else if (list.get(0).getProcessingApp().equals("RMS") && list.size() >= 1) {
					if (list.get(0).getSourceLocId().compareTo(list.get(0).getFulfillLocId()) == 0) {
						log.info("inside srcloc ==fullfillloc");
						interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
						log.info("omsCustOrdNo " + omsCustOrdNo + "callSIMCancelllationWS successful");
					}
					interfacePersistence.callRMSCancellationWebservice(list.get(0).getOmsCustOrdNo(), list);
					log.info("omsCustOrdNo " + omsCustOrdNo + "callRMSCancellationWebservice successful");
				} else {
					interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
					log.info("omsCustOrdNo " + omsCustOrdNo + "callSIMCancelllationWS successful");
				}
			}
		}
	}

	public String changeStatus(CustOrderDesc custOrderDesc) throws SOAPException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside changeStatus method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposCustOrderHead omsOrposCustOrderHead = null;
		try {
			log.info("omsCustOrdNo " + omsCustOrdNo);
			log.info("omsCustOrdNo " + omsCustOrdNo + "calling getOmsOrposCustOrderHeadFindByomsOrposCustOrderId where omsOrposCustOrderId is" + omsOrposCustOrderId);
			omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			log.info("omsCustOrdNo " + omsCustOrdNo + "retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
			omsOrposCustOrderHead.setStatus("F");
			session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
			log.info("Calling OMSCustomerOrderHead " + omsOrposCustOrderHead.getCustomerOrderId());
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(omsOrposCustOrderHead.getCustomerOrderId(), "A");
			log.info("omsCustOrdNo " + omsCustOrdHead.getOmsCustOrdNo() + "retrived status value from omsCustOrdHead " + omsCustOrdHead.getStatus());
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
		} catch (Exception e) {
			log.info("Exception while updating the status");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(omsOrposCustOrderHead.getCustomerOrderId(), "S");
			log.info("omsCustOrdNo " + omsCustOrdNo + "retrived status value from omsCustOrdHead " + omsCustOrdHead.getStatus());
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
		}
		return "CANCELED";
	}

	public void reduceBOSrcQty() throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside reduceBOSrcQty");
		log.info("omsCustOrdNo " + omsCustOrdNo);
		log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
		try {
			List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
			if (omsBackOrderDtlList.size() > 0) {
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
					omsBackOrderDtl.setSourceQty(BigDecimal.ZERO);
					session.mergeOmsBackOrderDtl(omsBackOrderDtl);
				}
			}
		} catch (Exception e) {
		}
	}

	public void reconcilationCreateOrder(CustOrderDesc custOrderDesc1) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// OMSUtilJdbc omsUtilJdbc=new OMSUtilJdbc();
		String customerOrderId = custOrderDesc1.getCustomerOrderId();
		log.info("customerOrderId " + customerOrderId);
		// omsUtilJdbc.persistintoOmsOrposMasterAudit(customerOrderId,omsCustOrdNo,"-1",new
		// BigDecimal(-1),new BigDecimal(-1),BigDecimal.ZERO, BigDecimal.ZERO,
		// BigDecimal.ZERO,new BigDecimal(-1),"CR","N");
		try {
			log.info("Entered order creation method for omsOrposMasterAudit");
			OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();
			log.info("order Id " + custOrderDesc1.getCustomerOrderId());
			omsOrposMasterAudit.setOrderId(custOrderDesc1.getCustomerOrderId());
			String defaultTransNo = "-1";
			if (custOrderDesc1.getOrposTransactionNumber() != null) {
				defaultTransNo = custOrderDesc1.getOrposTransactionNumber();
			}
			omsOrposMasterAudit.setOrposTransactionNumber(defaultTransNo);
			log.info("omsCustOrdNo " + omsCustOrdNo);
			omsOrposMasterAudit.setOmsCustOrderNo(omsCustOrdNo);
			omsOrposMasterAudit.setLineItemNo(new BigDecimal(-1));
			omsOrposMasterAudit.setCancelReqId(new BigDecimal(-1));
			omsOrposMasterAudit.setRmaReqId(new BigDecimal(-1));
			omsOrposMasterAudit.setEventId("CR");
			omsOrposMasterAudit.setStatus("N");
			omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
			omsOrposMasterAudit.setOrposInvoiceNumber(custOrderDesc1.getOrposInvoiceNumber());
			log.info("persisting into omsOrposMasterAudit table");
			session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Exiting order creation method after persisting into omsOrposMasterAudit");
		} catch (Exception e) {
			log.info("omsCustOrdNo" + omsCustOrdNo + " reconcilation catch block " + e.getMessage());
		}
	}

	public void persistRTLogTable(CustOrderDesc custOrderDesc) throws SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("omsCustOrdNo " + omsCustOrdNo + " calling persistRTLog ");
		OmsPersistence omsPersistence = new OmsPersistence();
		omsPersistence.persistRTLog(custOrderDesc, omsCustOrdNo);
		log.info("omsCustOrdNo " + omsCustOrdNo + " successfully persited into OmsRtlogPublishLog table");
	}

	private static IBDSClient getBDSClient() throws Exception {
		if (bdsClient == null) {
			synchronized (CustOrdCreateBean.class) {
				if (bdsClient == null) {
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					bdsClient = Feign.builder().encoder(new GsonEncoder(gson)).decoder(new GsonDecoder(gson)).target(IBDSClient.class, OMSUtilCommons.getWebServiceURL("RSB_SERVER_URL"));
				}
			}
		}
		return bdsClient;
	}

	public void bookSlotForOrder(CustOrderDesc custOrderDesc) throws Exception {
		try {
			Request request = new Request();
			request.setOrderNo(custOrderDesc.getCustomerOrderId());
			request.setOrderType("POS_CO");
			request.setCountry(custOrderDesc.getInitiateCountryCode());
			request.setReserveOnly(true);

			CustomerDetail customerDetail = new CustomerDetail();
			CustomerDesc customerDesc = custOrderDesc.getCustomerDesc();
			if (customerDesc != null) {
				customerDetail.setCustomerId(customerDesc.getCustomerId());
			}
			DeliveryDestDtl deliveryDestDtl = custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl();
			customerDetail.setFirstName(deliveryDestDtl.getContactDesc().getFirstName());
			customerDetail.setLastName(deliveryDestDtl.getContactDesc().getLastName());
			customerDetail.setPhoneNo(deliveryDestDtl.getContactDesc().getPhones().getPhoneDesc().get(0).getPhoneNumber());
			request.setCustomerDetails(customerDetail);
			
			Delivery delivery = new Delivery();
			delivery.setDeliveryId(1);
			delivery.setDeliveryType("S");
			DeliveryAddress deliveryAddress = new DeliveryAddress();
			deliveryAddress.setFirstName(deliveryDestDtl.getContactDesc().getFirstName());
			deliveryAddress.setLastName(deliveryDestDtl.getContactDesc().getLastName());
			deliveryAddress.setPhoneNo(deliveryDestDtl.getContactDesc().getPhones().getPhoneDesc().get(0).getPhoneNumber());
			deliveryAddress.setCountry(deliveryDestDtl.getGeoAddrDesc().getCountryCode());
			
			deliveryAddress.setCity(deliveryDestDtl.getGeoAddrDesc().getCity());
			deliveryAddress.setAddress1(deliveryDestDtl.getGeoAddrDesc().getAddress1());
			deliveryAddress.setAddress2(deliveryDestDtl.getGeoAddrDesc().getAddress2());
			deliveryAddress.setAddress3(deliveryDestDtl.getGeoAddrDesc().getAddress3());
			deliveryAddress.setArea(deliveryDestDtl.getGeoAddrDesc().getArea());
			delivery.setDeliveryAddress(deliveryAddress);
			
			Booking booking = new Booking();
			booking.setGroupId(999);
			booking.setReservationId(Math.abs(new Random().nextInt()));
			booking.setDate(custOrderDesc.getBookingDate().toGregorianCalendar().getTime());
			booking.setWindowCode(custOrderDesc.getBookingWindow());
			booking.setSlots(1);
			booking.setRegionID(1);
			booking.setRegionName("");
			booking.setItems(new ArrayList<Item>());

			for(CustOrdItmDesc custOrdItmDesc : custOrderDesc.getCustOrdItmColDesc().getCustOrdItmDesc()) {
				Item item = new Item();
				item.setLineNo(custOrdItmDesc.getLineItemNo());
				item.setItemCode(custOrdItmDesc.getItemId());
				item.setOrderQty(custOrdItmDesc.getQuantity().intValue());
				item.setUnitRetailPrice(custOrdItmDesc.getUnitSellPrice().toPlainString());
				item.setRetailCurrency(custOrdItmDesc.getCurrencyCode());
				booking.getItems().add(item);
			}

			delivery.setBookings(Collections.singletonList(booking));
			request.setDeliveries(Collections.singletonList(delivery));

			List<Response> res = getBDSClient().bookSlot(request);
			custOrderDesc.setBookingStatus(res.get(0).getStatus().isSuccess() ? "SUCCEESS" : "FAILED");
		} catch (Exception e) {
			log.warn("Error while booking the slot for the order" + custOrderDesc.getCustomerOrderId(), e);
			custOrderDesc.setBookingStatus("FAILED");
		}
	}
}
