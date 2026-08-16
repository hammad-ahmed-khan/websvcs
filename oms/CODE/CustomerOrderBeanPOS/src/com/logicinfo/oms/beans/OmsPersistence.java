package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoCancelHead;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdItemDisc;
import com.logicinfo.oms.ejb.OmsCustOrdLog;
import com.logicinfo.oms.ejb.OmsCustOrdLogItem;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsOrposCustOrdFul;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsOrposPayment;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.CustOrdItmDesc;
import com.oracle.retail.integration.base.bo.custorditmpkvo.v1.CustOrdItmPkVo;
import com.oracle.retail.integration.base.bo.discntlinedesc.v1.DiscntLineDesc;
import com.oracle.retail.integration.base.bo.geoaddrdesc.v1.GeoAddrDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentType;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

public class OmsPersistence {
	public OmsPersistence() {
		super();
	}

	BigDecimal omsCustOrdNo;
	private final static Logger log = Logger.getLogger(com.logicinfo.oms.beans.OmsPersistence.class.getName());

	public BigDecimal omsPersist(CustOrderDesc custOrderDesc, BigDecimal omsOrposCustOrderId) throws SOAPException {
		log.info("inside omsPersist");
		omsCustOrdNo = persistOmsCustOrdHead(custOrderDesc, omsOrposCustOrderId);
		// persistOmsCustOrdItem(custOrderDesc);
		checkNsaveOmsCustOrdAddress(custOrderDesc, omsOrposCustOrderId);
		checkNsaveOmsCustOrdTender(custOrderDesc, omsOrposCustOrderId);
		return omsCustOrdNo;
	}

	public BigDecimal persistOmsCustOrdHead(CustOrderDesc input, BigDecimal omsOrposCustOrderId) throws SOAPException {
		log.info("persistOmsCustOrdHead");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = new OmsCustOrdHead();
		omsCustOrdHead.setEntityId("eXtra");
		omsCustOrdHead.setApplicationId("ORPOS");
		log.info("After app id");
		// OmsOrposCustomer omsOrposCustomer1 =
		// session.getOmsOrposCustomerFindByOmsOrposCustOrdId(omsOrposCustOrderId);
		// OmsOrposContact omsOrposContact =
		// session.getOmsOrposContactFindByCustomerId(omsOrposCustOrderId,
		// omsOrposCustomer1.getCustomerId());
		log.info("input.getInitiateLocId() " + input.getInitiateLocId());
		omsCustOrdHead.setOrderRequestorId(new BigDecimal(input.getInitiateLocId()));
		log.info("input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getPhones().getPhoneDesc().get(0).getPhoneNumber() "
				+ input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getPhones().getPhoneDesc().get(0).getPhoneNumber());
		omsCustOrdHead.setCustPhoneNo(input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getPhones().getPhoneDesc().get(0).getPhoneNumber());
		log.info("input.getLocaleDesc().getLang() " + input.getLocaleDesc().getLang());
		omsCustOrdHead.setCustomerLang(input.getLocaleDesc().getLang());
		omsCustOrdHead.setOrderCreateReserveInd("C");
		if (input.getGrandTotal().longValue() - (input.getPaidAmount().longValue()) > 0) {
			omsCustOrdHead.setOrdPaymentStatus("P");
		} else {
			omsCustOrdHead.setOrdPaymentStatus("S");
		}
		omsCustOrdHead.setCustOrderNo((input.getCustomerOrderId()));
		log.info("input.getCustomerDesc().getCustomerId() is null or not");
		log.info("input.getCustomerDesc().getCustomerId() " + input.getCustomerDesc());
		if (input.getCustomerDesc() == null) {
			log.info("inside getCustomerId");
			omsCustOrdHead.setCustId("1");
		} else {
			omsCustOrdHead.setCustId(input.getCustomerDesc().getCustomerId());
		}
		omsCustOrdHead.setSubCustOrderNo("1");
		log.info("After sub customer order ");
		// OmsOrposCustomer omsOrposCustomer =
		// session.getOmsOrposCustomerFindByOmsOrposCustOrdId(omsOrposCustOrderId);
		// omsCustOrdHead.setCustId(omsOrposCustomer.getCustomerId());
		// omsCustOrdHead.setCustOrderType("STOREORDER");
		omsCustOrdHead.setCustOrderType("STR");
		log.info("Order type is STR");
		List<OmsOrposCustOrdFul> omsOrposCustOrdFulList = session.getOmsOrposCustOrdFulFindByOmsOrposCustOrdId(omsOrposCustOrderId);

		omsCustOrdHead.setDeliveryType(omsOrposCustOrdFulList.get(0).getDeliveryType());
		BigDecimal pickLoc = new BigDecimal(input.getPickLoc());
		if (pickLoc != null) {
			omsCustOrdHead.setPickLoc(pickLoc);
			log.info("pick loc id is" + omsCustOrdHead.getPickLoc());
		}
		Long delvAuthCodeVal = input.getDelvAuthCode();
		if (delvAuthCodeVal != null && delvAuthCodeVal > 0L) {
			BigDecimal delvAuthCode = BigDecimal.valueOf(delvAuthCodeVal);
			omsCustOrdHead.setDeliveryAuthCode(delvAuthCode);
		}
		log.info("first name" + input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getFirstName());
		omsCustOrdHead.setCustFirstName(input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getFirstName());
		omsCustOrdHead.setCustLastName(input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc().getLastName());
		omsCustOrdHead.setPayInStore("N");
		if (input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getConsumerDeliveryDate() != null) {
			omsCustOrdHead.setConsumerDlyTime(new Timestamp(input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getConsumerDeliveryDate().toGregorianCalendar().getTimeInMillis()));
		} else {

			omsCustOrdHead.setConsumerDlyTime(new Timestamp(System.currentTimeMillis()));
		}
		omsCustOrdHead.setCreateDatetime(new Timestamp(new Date().getTime()));
		omsCustOrdHead.setCustomerLang("1");
		omsCustOrdHead.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
		omsCustOrdHead.setOrderCategory(input.getOrderCategory());
		log.info("setting intially omsCustOrdHead status to A");
		omsCustOrdHead.setStatus("A"); // Active
		log.info("before persisting");
		if (input.getAddrVerifiedInd() != null) {
			omsCustOrdHead.setAddrVerifiedInd(input.getAddrVerifiedInd());
		} else {
			omsCustOrdHead.setAddrVerifiedInd("N");
		}
		// omsCustOrdHead.setCartNumber("N");
		omsCustOrdHead.setShipToStore(input.getShipToStore());
		omsCustOrdHead = session.persistOmsCustOrdHead(omsCustOrdHead);
		List<BigDecimal> custOrdHeadSeq = session.getOmsCustOrdHeadFindDuplicate("ORPOS", input.getCustomerOrderId(), "A");
		if (custOrdHeadSeq.size() == 0) {
			log.warn("No record found for OMS_CUST_ORD_NO , unable to insert in OmsCustOrdItem table");
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Database_Exception"));
		} else {
			omsCustOrdNo = custOrdHeadSeq.get(0);
			log.info("**OmsCustomerOrderNo=" + omsCustOrdNo + "******");
			// ProjectUtils.setCustOrdHeadSeqNo(omsCustOrdNo);
		}
		omsCustOrdNo = omsCustOrdHead.getOmsCustOrdNo();
		log.info("returning omsCustOrdNo " + omsCustOrdNo);
		return omsCustOrdNo;
	}

	public Map<String, String> persistOmsCustOrdItem(CustOrderDesc input, Map<String, CustOrdItmDesc> groupedItemMap) throws SOAPException {
		log.info("persistOmsCustOrdItem");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Map<String, String> itemPreOrdIndMap = new HashMap<String, String>();
		List<BigDecimal> custOrdHeadSeq = session.getOmsCustOrdHeadFindDuplicate("ORPOS", input.getCustomerOrderId(), "A");
		if (custOrdHeadSeq.size() == 0) {
			log.info("No record found for OMS_CUST_ORD_NO , unable to insert in OmsCustOrdItem table");
		} else {
			omsCustOrdNo = custOrdHeadSeq.get(0);
			log.info("omsCustOrdNo " + omsCustOrdNo);
			log.info("***OmsCustOrdItem table persist starts***");
			OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			String shipClassifcation = null;
			String ship = null;
			for (String groupItemKey : groupedItemMap.keySet()) {
				log.info("size of items ---" + groupedItemMap.keySet().size());
				CustOrdItmDesc custOrdItmDesc = groupedItemMap.get(groupItemKey);
				if (custOrdItmDesc.getShippingChargeFlag().value().equals("Y") == false) {
					OmsCustOrdItem omsCustOrdItem = new OmsCustOrdItem();
					omsCustOrdItem.setOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdItem.setItem(custOrdItmDesc.getItemId());
					omsCustOrdItem.setLineLinkNo(custOrdItmDesc.getLinkLineNo());
					log.info("LineNo=" + custOrdItmDesc.getCapturedLineItemNo() + "Item=" + custOrdItmDesc.getItemId());
					log.info("ship " + ship);
					try {
						String wallBracketItem = findWallBracketSku(custOrdItmDesc.getItemId());
						if (wallBracketItem != null && wallBracketItem.equals(custOrdItmDesc.getItemId()) && !wallBracketItem.isEmpty()) {
							log.info("Inside wall Bracket Item ship classification -- item = " + wallBracketItem);
							for (String itemKey : groupedItemMap.keySet()) {
								CustOrdItmDesc item = groupedItemMap.get(itemKey);
								if (findWallBracketSku(item.getItemId()) == null) {
									shipClassifcation = getOmsShipClassification(item.getItemId(), input.getInitiateLocId());
									log.info("wall Bracket Item ship classification-- " + shipClassifcation);
									break;
								}
							}
							if (shipClassifcation == null) {
								shipClassifcation = getOmsShipClassification(custOrdItmDesc.getItemId(), input.getInitiateLocId());
								log.info("shipClassifcation " + shipClassifcation);
							}
							log.info("shipClassifcation " + shipClassifcation);
						} else {
							shipClassifcation = getOmsShipClassification(custOrdItmDesc.getItemId(), input.getInitiateLocId());
							log.info("Normal shipClassifcation " + shipClassifcation);
						}

						String clzPreOrdArr[] = shipClassifcation.split("-");
						log.info("Normal shipClassifcation " + shipClassifcation);
						shipClassifcation = clzPreOrdArr[0];
						if (clzPreOrdArr.length > 1) {
							itemPreOrdIndMap.put(custOrdItmDesc.getItemId(), clzPreOrdArr[1]);
						}
						log.info("Final shipClassifcation = " + shipClassifcation + ", clzPreOrdArr[0] = " + clzPreOrdArr[0] + ", clzPreOrdArr[1] = " + clzPreOrdArr[1]);
						if (shipClassifcation == null || shipClassifcation.isEmpty()) {
							input.setOrderDesc("OMS_ORPOS_ERROR_113"); // Ship classification is null
							omsCutOrdHead.setStatus("F");
							session.mergeOmsCustOrdHead(omsCutOrdHead);
							OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(input.getCustomerOrderId(), "A");
							log.info("retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
							omsOrposCustOrderHead.setStatus("F");
							session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
							session.mergeOmsCustOrdHead(omsCutOrdHead);
						} else {
							if ("Y".equals(input.getExpressDelvInd())) {
								if ("SMALL".equals(shipClassifcation)) {
									omsCustOrdItem.setShipClassification("EXPDELVSMALL");
								} else {
									omsCustOrdItem.setShipClassification("EXPDELVBIG");
								}
							} else {
								omsCustOrdItem.setShipClassification(shipClassifcation);
							}
						}
					} catch (Exception e) {
						input.setOrderDesc("OMS_ORPOS_ERROR_113"); // Ship classification is null
						omsCutOrdHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCutOrdHead);
						OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(input.getCustomerOrderId(), "A");
						log.info("retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
						omsOrposCustOrderHead.setStatus("F");
						session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
						session.mergeOmsCustOrdHead(omsCutOrdHead);
					}
					omsCustOrdItem.setOrigUnitRetail(custOrdItmDesc.getUnitRegularPrice());
					omsCustOrdItem.setLineNo(new BigDecimal(custOrdItmDesc.getCapturedLineItemNo()));
					omsCustOrdItem.setBackorderInd("N");
					omsCustOrdItem.setJoodItem(custOrdItmDesc.getJoodItem());
					log.info("QtyReturned " + 0);
					omsCustOrdItem.setQtyReturned(BigDecimal.ZERO);
					// need clarification on link_line_no
					/*
					 * if (customerOrderItems.getLinkLineNo() != null) {
					 * omsCustOrdItem.setLineLinkNo(new
					 * BigDecimal(customerOrderItems.getLinkLineNo())); }
					 */
					// To be fetched from rms table
					// omsCustOrdItem.setShipClassification(customerOrderItems.getShippingClassification());
					log.info("before setting sustitude ind");
					// if(customerOrderItems.getSubstitutionInd().isEmpty()==false ||
					// customerOrderItems.getSubstitutionInd()!=null )
					omsCustOrdItem.setSubstituteAllowInd("N");
					// clarification required
					/*
					 * if(customerOrderItems.getFutInvAvlDate()!=null) {
					 * omsCustOrdItem.setBackorderDlyDate(new
					 * Timestamp(customerOrderItems.getFutInvAvlDate().toGregorianCalendar().
					 * getTimeInMillis())); }
					 */
					omsCustOrdItem.setQtyOrderedSuom(custOrdItmDesc.getQuantity());
					// Check whether mandatory or not ,otherwise need to query item_master fro
					// finding standard_uom by item
					omsCustOrdItem.setStandardUom("EA");
					omsCustOrdItem.setTransactionUom("EA");
					omsCustOrdItem.setQtyCancelled(custOrdItmDesc.getCancelledQuantity());
					omsCustOrdItem.setCumQtyDelivered(custOrdItmDesc.getCompletedQuantity());
					omsCustOrdItem.setUnitRetail(custOrdItmDesc.getUnitSellPrice());
					if (custOrdItmDesc.getCurrencyCode() != null) {
						omsCustOrdItem.setRetailCurr(custOrdItmDesc.getCurrencyCode());
					}

					// expected delivery date

					if (input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getConsumerDeliveryDate() != null) {
						omsCustOrdItem
								.setExpectedDeliveryDate(new Timestamp(input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getConsumerDeliveryDate().toGregorianCalendar().getTimeInMillis()));
					}

					omsCustOrdItem.setComments("ITEM orded from POS");
					omsCustOrdItem.setCreateDatetime(new Timestamp(new Date().getTime()));
					omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
					omsCustOrdItem.setStatus("N"); // New
					// Start: Vat Amount @tsultana
					if (custOrdItmDesc.getInclusiveTaxTotal() != null) {
						log.info("Vat Amount: " + custOrdItmDesc.getInclusiveTaxTotal());
						log.info("Quantity is " + custOrdItmDesc.getQuantity());
						log.info("Line No " + omsCustOrdItem.getLineNo() + "Quanity in item table " + omsCustOrdItem.getQtyOrderedSuom());
						BigDecimal unitVatAmount = (custOrdItmDesc.getInclusiveTaxTotal().divide(custOrdItmDesc.getQuantity(), 2, RoundingMode.HALF_EVEN));
						// unitVatAmount=unitVatAmount.setScale(2, RoundingMode.HALF_EVEN);
						log.info("line No " + custOrdItmDesc.getLineItemNo() + "unitVatAmount is " + unitVatAmount);
						omsCustOrdItem.setUnitVatAmount(unitVatAmount);
					} else {
						log.info("Vat Amount: " + custOrdItmDesc.getInclusiveTaxTotal());
						omsCustOrdItem.setUnitVatAmount(BigDecimal.ZERO);
					}
					omsCustOrdItem.setApplyServiceInd("N");
					if (custOrdItmDesc.getDelvEffort() != null) {
						omsCustOrdItem.setDelvEffort(custOrdItmDesc.getDelvEffort());
					}
					try {
						session.persistOmsCustOrdItem(omsCustOrdItem);
						log.info("persisting into OmsCustOrdItemDisc");
						persistIntoOmsCustOrdItemDisc(omsCustOrdNo, custOrdItmDesc);
					} catch (Exception e) {
						log.info(e);
						omsCutOrdHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCutOrdHead);
						OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(input.getCustomerOrderId(), "A");
						log.info("retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
						omsOrposCustOrderHead.setStatus("F");
						session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
						session.mergeOmsCustOrdHead(omsCutOrdHead);
						// throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("You are
						// trying to order same item multiple times.Please order same item only
						// once."));
					}
				}
			}
		}
		return itemPreOrdIndMap;
	}

	public void checkNsaveOmsCustOrdTender(CustOrderDesc input, BigDecimal omsOrposCustOrderId) throws SOAPException {
		log.info("***Start-checkNsaveOmsCustOrdTender***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// check it should return list
		TenderTypeClassifier tenderTypeClassifier = new TenderTypeClassifier();
		String tgroup = null;
		// storing the value in BigDecimal variable
		BigDecimal tenderTypeId = null;
		List<OmsOrposPayment> omsOrposPaymentList = session.getOmsOrposPaymentFindByOmsOrposCustOrdId(omsOrposCustOrderId);
		log.info(" omsOrposPaymentList.size() " + omsOrposPaymentList.size());
		List<PaymentDesc> paymentDesc = input.getPaymentColDesc().getPaymentDesc();
		log.info("paymentDesc.size() " + paymentDesc.size());
		OmsCustOrdTender omsCustOrdTender = new OmsCustOrdTender();
		// int i = 0;
		for (PaymentDesc paymentDescLoop : paymentDesc) {
			log.info("inside the loop for payment in saving tender");
			tenderTypeId = tenderTypeClassifier.getTenderTypeIdBasedOnParameterValueForCreateOrder(paymentDescLoop, input);
			log.info("tender type id is======+++++++++++===========" + tenderTypeId);
			omsCustOrdTender.setTenderTypeId(tenderTypeId);
			log.info("calling getPosTenderTypeHeadFindByTenderTypeId ");
			tgroup = session.getPosTenderTypeHeadFindByTenderTypeId(tenderTypeId);
			log.info("tgroup " + tgroup);
			omsCustOrdTender.setTenderTypeGroup(tgroup);
			omsCustOrdTender.setTenderAmt(paymentDescLoop.getAmount());
			log.info("seq No :" + paymentDescLoop.getSeqNo());
			omsCustOrdTender.setTenderSeqNo(new BigDecimal(paymentDescLoop.getSeqNo()));
			omsCustOrdTender.setPaymentStatusInd("S");
			omsCustOrdTender.setOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdTender.setCreateDatetime(new Timestamp(new Date().getTime()));
			if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CREDIT"))) {
				log.info("inside CREDIT");
				// omsCustOrdTender.setTenderTypeGroup("CREDIT");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("DEBIT"))) {
				log.info("inside DEBIT");
				// omsCustOrdTender.setTenderTypeGroup("DEBIT");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CASH"))) {
				log.info("inside CASH");
				// omsCustOrdTender.setTenderTypeGroup("CASH");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CHECK"))) {
				log.info("inside CHECK");
				// omsCustOrdTender.setTenderTypeGroup("CHECK");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("PURCHASEORDER"))) {
				log.info("inside PURCHASEORDER");
				// omsCustOrdTender.setTenderTypeGroup("PURCHASEORDER");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("STORECREDIT"))) {
				log.info("inside STORECREDIT");
				// omsCustOrdTender.setTenderTypeGroup("STORECREDIT");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("GIFTCARD"))) {
				log.info("inside GIFTCARD");
				if ("1111".equals(paymentDescLoop.getGiftCardTender().getAuthorizationCode()) || "5555".equals(paymentDescLoop.getGiftCardTender().getAuthorizationCode())) {
					omsCustOrdTender.setCcAuthNo(paymentDescLoop.getGiftCardTender().getAuthorizationCode());
					log.info("AuthorizationCode -" + paymentDescLoop.getGiftCardTender().getAuthorizationCode());
				}
				// omsCustOrdTender.setTenderTypeGroup("GIFTCARD");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("COUPON"))) {
				log.info("inside COUPON");
				// omsCustOrdTender.setTenderTypeGroup("COUPON");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("MONEYORDER"))) {
				log.info("inside MONEYORDER");
				// omsCustOrdTender.setTenderTypeGroup("MONEYORDER");
			} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("GIFTCERT"))) {
				log.info("inside GIFTCERT");
				// omsCustOrdTender.setTenderTypeGroup("GIFTCERT");
			}
			if (paymentDescLoop.getPaymentType().equals(PaymentType.CREDIT)) {
				// if (paymentDescLoop.getCreditDebitTender().getA != null)
				// {
				omsCustOrdTender.setCcAuthSrc("E");
				// }
				if (paymentDescLoop.getCreditDebitTender().getAuthorizationCode() != null) {
					omsCustOrdTender.setCcAuthNo(paymentDescLoop.getCreditDebitTender().getAuthorizationCode());
				}
				if (paymentDescLoop.getCreditDebitTender().getMaskedAccountNumber() != null) {
					omsCustOrdTender.setCcNo(paymentDescLoop.getCreditDebitTender().getMaskedAccountNumber());
				}
				if (paymentDescLoop.getCreditDebitTender().getCardToken() != null) {
					omsCustOrdTender.setTenderRefId(paymentDescLoop.getCreditDebitTender().getCardToken());
				}
				omsCustOrdTender.setCcEntryMode("MSR");
				omsCustOrdTender.setCcSpecCond("E");
				omsCustOrdTender.setCcCardholderVerf("P");
			}
			session.persistOmsCustOrdTender(omsCustOrdTender);
			log.info("----> OmsCustOrdTender table persist success <----");
			// code was here
			// try {
			// session.persistOmsCustOrdTender(omsCustOrdTender);
			// log.info("OmsCustOrdTender table persist success");
			// } catch (Exception e) {
			// if ( (null ==paymentDesc) || (( null!=paymentDesc)&& paymentDesc.size() ==
			// 0)) {
			// OmsCustOrdTender omsCustOrdTenderforZeroTender = new OmsCustOrdTender();
			// omsCustOrdTenderforZeroTender.setTenderTypeId(new BigDecimal(1000));
			// omsCustOrdTenderforZeroTender.setTenderTypeGroup("CASH");
			// omsCustOrdTenderforZeroTender.setTenderAmt(BigDecimal.ZERO);
			// omsCustOrdTenderforZeroTender.setTenderSeqNo(new BigDecimal(0));
			// omsCustOrdTenderforZeroTender.setPaymentStatusInd("S");
			// omsCustOrdTenderforZeroTender.setOmsCustOrdNo(omsCustOrdNo);
			// omsCustOrdTenderforZeroTender.setCreateDatetime(new Timestamp(new
			// Date().getTime()));
			// session.persistOmsCustOrdTender(omsCustOrdTenderforZeroTender);
			// }
			// }
			// }
			// }
		}
	}

	public void checkNsaveOmsCustOrdAddress(CustOrderDesc input, BigDecimal omsOrposCustOrderId) throws SOAPException {
		log.info("***Start-checkNsaveOmsCustOrdAddress***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		GeoAddrDesc geoAddrDesc = input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc();
		ContactDesc contactDesc = input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getContactDesc();
		// code fix for 2784 bug
		String intitateCountryCode = input.getInitiateCountryCode();
		log.info("<---Intitated Country Code---" + intitateCountryCode + "<--->");
		String stateCode = input.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getStateCode();
		log.info("<----State Code is----->" + stateCode + "<---->");
		log.info("<-----Calling Oms Reference Data function-------> ");
		String deliveryCity = session.getOmsReferenceDataFindRefValue("STATE_CITY_LINK", intitateCountryCode, stateCode);
		log.info("<---Delivery City is----->" + deliveryCity + "<------>");
		OmsCustOrdAddress omsCustOrdAddress = new OmsCustOrdAddress();
		omsCustOrdAddress.setOmsCustOrdNo(omsCustOrdNo);
		// if(input.getCustomerDesc().getCustomerId()==null)
		// {
		omsCustOrdAddress.setCustId("1");
		// }
		// else {
		// omsCustOrdAddress.setCustId(input.getCustomerDesc().getCustomerId());
		// }
		if (contactDesc.getFirstName() != null) {
			omsCustOrdAddress.setDeliverFirstName(contactDesc.getFirstName());
			omsCustOrdAddress.setBillFirstName(contactDesc.getFirstName());
		}
		if (contactDesc.getLastName() != null) {
			omsCustOrdAddress.setDeliverLastName(contactDesc.getLastName());
			omsCustOrdAddress.setBillLastName(contactDesc.getLastName());
		}
		if (geoAddrDesc != null) {
			if (geoAddrDesc.getAddress1() != null) {
				omsCustOrdAddress.setDeliverAdd1(geoAddrDesc.getAddress1());
				omsCustOrdAddress.setBillAdd1(geoAddrDesc.getAddress1());
			}
			if (geoAddrDesc.getCity() != null) {
				omsCustOrdAddress.setDeliverAdd2(geoAddrDesc.getCity());
				omsCustOrdAddress.setBillAdd2(geoAddrDesc.getArea());
			} else if (geoAddrDesc.getAddress2() != null) {
				omsCustOrdAddress.setDeliverAdd2(geoAddrDesc.getAddress2());
				omsCustOrdAddress.setBillAdd2(geoAddrDesc.getAddress2());
			}
			if (geoAddrDesc.getAddress3() != null) {
				omsCustOrdAddress.setDeliverAdd3(geoAddrDesc.getAddress3());
				omsCustOrdAddress.setBillAdd3(geoAddrDesc.getAddress3());
			}
			if (geoAddrDesc.getLatitude() != null) {
				omsCustOrdAddress.setLatitude(geoAddrDesc.getLatitude());
			}
			if (geoAddrDesc.getLongitude() != null) {
				omsCustOrdAddress.setLongitude(geoAddrDesc.getLongitude());
			}
			if (geoAddrDesc.getDelvShortAddr() != null) {
				omsCustOrdAddress.setDelvShortAddr(geoAddrDesc.getDelvShortAddr());
			}
			if (geoAddrDesc.getCity() != null) {
				omsCustOrdAddress.setDeliverCity(deliveryCity);
				omsCustOrdAddress.setBillCity(deliveryCity);
			}
			// checke whether state code or name
			if (geoAddrDesc.getStateCode() != null) {
				omsCustOrdAddress.setDeliverState(geoAddrDesc.getStateCode());
				omsCustOrdAddress.setBillState(geoAddrDesc.getStateCode());
			}
			if (geoAddrDesc.getCountryCode() != null) {
				omsCustOrdAddress.setDeliverCountry(geoAddrDesc.getCountryCode());
				omsCustOrdAddress.setBillCountry(geoAddrDesc.getCountryCode());
			}
			if (geoAddrDesc.getPostalCode() != null) {
				omsCustOrdAddress.setDeliverPost(geoAddrDesc.getPostalCode());
				omsCustOrdAddress.setBillPost(geoAddrDesc.getPostalCode());
			}
			if (contactDesc.getPhones().getPhoneDesc().get(0).getPhoneNumber() != null) {
				omsCustOrdAddress.setDeliverPhoneNo(contactDesc.getPhones().getPhoneDesc().get(0).getPhoneNumber());
			}

			/*
			 * if (input.getCustomerOrderAddress().getDeliverPhoneNo() != null) {
			 * omsCustOrdAddress.setDeliverPhoneNo(input.getCustomerOrderAddress().
			 * getDeliverPhoneNo()); } if
			 * (input.getCustomerOrderAddress().getBillFirstName() != null) {
			 * omsCustOrdAddress.setBillFirstName(input.getCustomerOrderAddress().
			 * getBillFirstName()); } if (input.getCustomerOrderAddress().getBillLastName()
			 * != null) { omsCustOrdAddress.setBillLastName(input.getCustomerOrderAddress().
			 * getBillLastName()); } if (input.getCustomerOrderAddress().getBillAddress1()
			 * != null) {
			 * omsCustOrdAddress.setBillAdd1(input.getCustomerOrderAddress().getBillAddress1
			 * ()); } if (input.getCustomerOrderAddress().getBillAddress2() != null) {
			 * omsCustOrdAddress.setBillAdd2(input.getCustomerOrderAddress().getBillAddress2
			 * ()); } if (input.getCustomerOrderAddress().getBillAddress3() != null) {
			 * omsCustOrdAddress.setBillAdd3(input.getCustomerOrderAddress().getBillAddress3
			 * ()); } if (input.getCustomerOrderAddress().getBillCity() != null) {
			 * omsCustOrdAddress.setBillCity(input.getCustomerOrderAddress().getBillCity());
			 * } if (input.getCustomerOrderAddress().getBillState() != null) {
			 * omsCustOrdAddress.setBillState(input.getCustomerOrderAddress().getBillState()
			 * ); } if (input.getCustomerOrderAddress().getBillCountry() != null) {
			 * omsCustOrdAddress.setBillCountry(input.getCustomerOrderAddress().
			 * getBillCountry()); } if (input.getCustomerOrderAddress().getBillPostal() !=
			 * null) {
			 * omsCustOrdAddress.setBillPost(input.getCustomerOrderAddress().getBillPostal()
			 * ); }
			 */
		}
		omsCustOrdAddress.setCreateDatetime(new Timestamp(new Date().getTime()));
		try {
			session.persistOmsCustOrdAddress(omsCustOrdAddress);
			log.info("OmsCustOrdAddress table persist success");
		} catch (Exception e) {
			OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCutOrdHead.setStatus("F");
			OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(input.getCustomerOrderId(), "A");
			log.info("retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
			omsOrposCustOrderHead.setStatus("F");
			session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
		}
	}

	public OmsCoCancelItem persistOmsCoCancelItem(String item, BigDecimal omsCancelId, BigDecimal omsCustOrdNo, CustOrdItmPkVo custOrdItmPkVo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("*****Start persistOmsCoCancelItem for cancellation*****");
		// CustOrdItmPkColVo custOrdItmPkColVo=custOrderPicVo.getCustOrdItmPkColVo();
		OmsCoCancelItem omsCoCancelItem = new OmsCoCancelItem();
		try {
			omsCoCancelItem.setItem(item);
			omsCoCancelItem.setComments("POS Cancellation");
			omsCoCancelItem.setCancelReqQty(custOrdItmPkVo.getCancelledQuantity());
			omsCoCancelItem.setLineNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
			omsCoCancelItem.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsCoCancelItem.setCancelConfQty(BigDecimal.ZERO);
			log.info("cancel id " + omsCancelId);
			omsCoCancelItem.setOmsCancelId(omsCancelId); // generated by sequence
			session.persistOmsCoCancelItem(omsCoCancelItem);
			log.info("Successfully persisted into OmsCoCancelItem for cancellation");
		} catch (Exception e) {
			log.error("Failed in persistOmsCoCancelItem. error=" + e);
		}
		return omsCoCancelItem;
	}

	public void persistCustOrdLogItem(CustOrdItmPkVo itemlist, String item, long fulilmentOrdNo, BigDecimal logSeqNo, String customerOrderNo) throws SOAPException {
		log.info("Inside OmsCustOrdLogItem method for cancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("logSeqNo " + logSeqNo);
		log.info("item " + item);
		log.info("fulilmentOrdNo " + fulilmentOrdNo);
		OmsCustOrdLogItem omsCustOrdLogItem = new OmsCustOrdLogItem();
		// OmsCustOrdItem
		// omsCustOrdItem=session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo,
		// item.getLineItemNo());
		omsCustOrdLogItem.setItem(item);
		omsCustOrdLogItem.setQty(itemlist.getCancelledQuantity());
		omsCustOrdLogItem.setLineNo(new BigDecimal(itemlist.getLineItemNo()));
		omsCustOrdLogItem.setCreateDatetime(new Timestamp(new Date().getTime()));
		omsCustOrdLogItem.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));
		omsCustOrdLogItem.setLogSeqNo(logSeqNo);
		session.persistOmsCustOrdLogItem(omsCustOrdLogItem);
		log.info("Successfully persisted into OmsCustOrdLogItem for Cancellation");
	}

//Persisting into OmsRtlogPublishLog for cancellation
	public void persistOmsRtlogPublishLog(String item, BigDecimal omsCancelId, BigDecimal omsCustOrderNo, CustOrderPicVo input) throws SOAPException {
		log.info("Inside persistOmsRtlogPublishLog method for cancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
		BigDecimal fulfillLoc = null;
		BigDecimal fulfillOrderNo = null;
		BigDecimal lineItemNo = null;
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrderNo);
		for (OmsCustOrdItem omsCustOrdItemLoop : omsCustOrdItemList) {
			log.info("----------Inside omsCustOrdItemLoop---------");
			omsRtlogPublishLog.setItem(item);
			lineItemNo = omsCustOrdItemLoop.getLineNo();
			omsRtlogPublishLog.setLineNo(lineItemNo);
			omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrderNo);
			omsRtlogPublishLog.setPublishedInd("N"); // need to confirm its value
			omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsRtlogPublishLog.setTranType("ORC");
			omsRtlogPublishLog.setQty(omsCustOrdItemLoop.getQtyCancelled()); // need to confirm
			omsRtlogPublishLog.setOmsCancelId(omsCancelId);
			for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
				log.info("Inside omsCoFulfillDetail loop");
				fulfillLoc = omsCoFulfillDetail.getFulfillLoc();
				fulfillOrderNo = omsCoFulfillDetail.getFulfillOrderNo();
			}
			omsRtlogPublishLog.setFulfillOrderNo(fulfillOrderNo);
			omsRtlogPublishLog.setLocation(fulfillLoc);
			omsRtlogPublishLog.setErrorMessage(null);
			omsRtlogPublishLog.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
			log.info("Successfully persisted into OmsRtlogPublishLog table for cancellation");
		}
	}

	public void persistOmsCoCancelHead(CustOrderPicVo input, BigDecimal omsCustOrdNo, String customerOrderNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("**persistOmsCoCancelHead start for cancellation**");
		try {
			// String custOrderNo=input.getCustomerOrderId();
			String custOrderNo = customerOrderNo;
			log.info("custOrderNo " + custOrderNo);
			BigDecimal fulfillLoc = null;
			BigDecimal omsCustOrderNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdNo " + omsCustOrderNo);
			List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
			List<OmsBackOrderDtl> omsBackOrderDtlList = null;
			// BigDecimal
			// omsOrposCustOrderId=session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
			// BigDecimal
			// custOrderPicVoSeq=session.getMaxOmsOrposCustOrderPickUp(input.getCustomerOrderId());
			BigDecimal custOrderPicVoSeq = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
			OmsCoCancelHead omsCoCancelHead = new OmsCoCancelHead();
			try {
				omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrderNo);
				if (omsCoFulfillDetailList.size() > 0) {
					omsCoCancelHead.setEntityId("eXtra");
					omsCoCancelHead.setApplicationId(omsCustOrdHead.getApplicationId());
					// omsCoCancelHead.setCustOrdNo(input.getCustomerOrderId());
					omsCoCancelHead.setCustOrdNo(customerOrderNo);
					omsCoCancelHead.setSubCustOrdNo("1");
					for (OmsCoFulfillDetail omsCoFulfillDetailsLoop : omsCoFulfillDetailList) {
						fulfillLoc = omsCoFulfillDetailsLoop.getFulfillLoc();
					}
					log.info("fulfillLoc" + fulfillLoc);
					omsCoCancelHead.setCancelReqstLocId(fulfillLoc);
					omsCoCancelHead.setCancelReqId(custOrderPicVoSeq);
					omsCoCancelHead.setRefundOption("ORPOS");
					omsCoCancelHead.setRefundCompltInd("Y");
					omsCoCancelHead.setRefindAmount(input.getCancelledAmount());
					omsCoCancelHead.setComments("POS Cancellation");
					omsCoCancelHead.setCanReqDatetime(new Timestamp(new Date().getTime()));
					omsCoCancelHead.setCreateDatetime(new Timestamp(new Date().getTime()));
					omsCoCancelHead.setStatus("N");
					omsCoCancelHead.setLastUpdateDatetime(null);
					session.persistOmsCoCancelHead(omsCoCancelHead);
					log.info("Successfully persisted data into OmsCoCancelHead table for cancellation");
				} else {
					log.info("fetching record from omsBackOrderDtl");
					omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
					log.info("omsBackOrderDtlList.size()  " + omsBackOrderDtlList.size());
					omsCoCancelHead.setEntityId("eXtra");
					omsCoCancelHead.setApplicationId(omsCustOrdHead.getApplicationId());
					// omsCoCancelHead.setCustOrdNo(input.getCustomerOrderId());
					omsCoCancelHead.setCustOrdNo(customerOrderNo);
					omsCoCancelHead.setSubCustOrdNo("1");
					for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
						fulfillLoc = omsBackOrderDtl.getFulfillLoc();
					}
					log.info("fulfillLoc " + fulfillLoc);
					omsCoCancelHead.setCancelReqstLocId(fulfillLoc);
					omsCoCancelHead.setCancelReqId(custOrderPicVoSeq);
					omsCoCancelHead.setRefundOption("ORPOS");
					omsCoCancelHead.setRefundCompltInd("Y");
					omsCoCancelHead.setRefindAmount(input.getCancelledAmount());
					omsCoCancelHead.setComments(null);
					omsCoCancelHead.setCanReqDatetime(new Timestamp(new Date().getTime()));
					omsCoCancelHead.setCreateDatetime(new Timestamp(new Date().getTime()));
					omsCoCancelHead.setStatus("N");
					omsCoCancelHead.setLastUpdateDatetime(null);
					session.persistOmsCoCancelHead(omsCoCancelHead);
					log.info("Successfully persisted data into OmsCoCancelHead table for cancellation");
				}
			} catch (Exception e) {
			}
		} catch (Exception e) {
		}
	}

//Persisting into OmsCustOrdLog for Cancellation
	public void persistOmsCustOrdLog(BigDecimal omsCustOrderNo, BigDecimal omsCancelId, String eventId, CustOrderPicVo custOrderPicVo, String customerOrderNo) throws SOAPException {
		log.info("Inside persistOmsCustOrdLog mehtod for cancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// String custOrderNo=custOrderPicVo.getCustomerOrderId();
		String custOrderNo = customerOrderNo;
		omsCustOrderNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		try {
			OmsCustOrdLog omsCustOrdLog = new OmsCustOrdLog();
			omsCustOrdLog.setOmsCustOrdNo(omsCustOrderNo);
			omsCustOrdLog.setOmsDlvConfId(null);
			omsCustOrdLog.setEventId(eventId);
			omsCustOrdLog.setEventComments("Item Cancelled");
			omsCustOrdLog.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsCustOrdLog.setOmsCancelId(omsCancelId);
			session.persistOmsCustOrdLog(omsCustOrdLog);
			log.info("Successfully persisted into OmsCustOrdLog table");
		} catch (Exception e) {
			log.info("Exception in omsCustOrdLog ");
		}
	}

	public void persistRTLog(CustOrderDesc input, BigDecimal omsCustOrdNo) throws SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
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
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCustOrdNo " + omsCustOrdNo);
		List<OmsCustOrdItem> omscustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdNo);
		log.info("omscustOrdItemList.size " + omscustOrdItemList.size());
		for (OmsCustOrdItem omsCustOrdItem : omscustOrdItemList) {
			OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
			log.info("omsCustOrdNo " + omsCustOrdNo + " input.getInitiateLocId() " + input.getInitiateLocId());
			omsRtlogPublishLog.setLocation(new BigDecimal(input.getInitiateLocId()));
			log.info("omsCustOrdNo " + omsCustOrdNo + " omsCustOrdItem.getItem() " + omsCustOrdItem.getItem());
			omsRtlogPublishLog.setItem(omsCustOrdItem.getItem());
			log.info("omsCustOrdNo " + omsCustOrdNo + " omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
			omsRtlogPublishLog.setLineNo(omsCustOrdItem.getLineNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + " PublishedInd  " + "N");
			omsRtlogPublishLog.setPublishedInd("N");
			log.info("omsCustOrdNo " + omsCustOrdNo);
			omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdNo " + omsCustOrdNo + " setted ORI");
			omsRtlogPublishLog.setTranType("ORI");
			log.info("omsCustOrdNo " + omsCustOrdNo + " omsCustOrdItem.getQtyOrderedSuom() " + omsCustOrdItem.getQtyOrderedSuom());
			omsRtlogPublishLog.setQty(omsCustOrdItem.getQtyOrderedSuom());
			omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
			try {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Persisted records into RTLOG");
				session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
			} catch (Exception e) {
				log.info("omsCustOrdNo " + omsCustOrdNo + " Exception while persistOmsRtlogPublishLog ");
			}
		}
	}

	public void persistIntoOmsCustOrdItemDisc(BigDecimal omsCustOrdNo, CustOrdItmDesc custOrdItmDesc) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("Inside persistIntoOmsCustOrdItemDisc method");
		if (custOrdItmDesc.getDiscntLineColDesc() != null) {
			log.info("discntColSize " + custOrdItmDesc.getDiscntLineColDesc().getCollectionSize());
			for (DiscntLineDesc discntLineDesc : custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc()) {
				log.info("Inside DiscntLineDesc loop");
				OmsCustOrdItemDisc omsCustOrdItemDisc = new OmsCustOrdItemDisc();
				omsCustOrdItemDisc.setOmsCustOrdNo(omsCustOrdNo);
				log.info("Oms Customer Order Number : " + omsCustOrdNo);
				omsCustOrdItemDisc.setLineNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
				log.info("Line Number : " + custOrdItmDesc.getLineItemNo());
				omsCustOrdItemDisc.setDiscLineNo(new BigDecimal(discntLineDesc.getLineNo()));
				log.info("Discount Line Number : " + discntLineDesc.getLineNo());
				if (discntLineDesc.getDiscountReasonCode() != null) {
					omsCustOrdItemDisc.setRmsPromoType(discntLineDesc.getDiscountReasonCode());
					log.info("RMS Promo Type :" + discntLineDesc.getDiscountReasonCode());
				}
				if (discntLineDesc.getPromotionId() != null) {
					omsCustOrdItemDisc.setDiscRefNo(discntLineDesc.getPromotionId());
					log.info("Discount Reference Number : " + discntLineDesc.getPromotionId());
				}
				if (discntLineDesc.getDiscountEmployeeId() != null) {
					omsCustOrdItemDisc.setEmployeeId(discntLineDesc.getDiscountEmployeeId());
					log.info("Employee Id : " + discntLineDesc.getDiscountEmployeeId());
				}
				if (discntLineDesc.getStoreCouponId() != null) {
					omsCustOrdItemDisc.setDiscountType(discntLineDesc.getStoreCouponId());
					log.info("Discount Type : " + discntLineDesc.getStoreCouponId());
				}
				if (discntLineDesc.getUnitDiscountAmount() != null) {
					omsCustOrdItemDisc.setUnitDiscountAmount(discntLineDesc.getUnitDiscountAmount());
					log.info("Unit Discount Amount : " + discntLineDesc.getUnitDiscountAmount());
				}
				if (discntLineDesc.getPromotionComponentId() != null) {
					omsCustOrdItemDisc.setPromoCompId(discntLineDesc.getPromotionComponentId());
					log.info("Promotion Component Id : " + discntLineDesc.getPromotionComponentId());
				}
				// Added for Simple Promo
				if (discntLineDesc.getDiscountRuleId() != null) {
					omsCustOrdItemDisc.setSimplePromoInd(discntLineDesc.getDiscountRuleId());
					log.info("Discount Rule Id : " + discntLineDesc.getDiscountRuleId());
				}
				omsCustOrdItemDisc.setCreateDatetime(new Timestamp(new Date().getTime()));
				session.persistOmsCustOrdItemDisc(omsCustOrdItemDisc);
			}
		}
	}

	private String getOmsShipClassification(String item, Long requestorId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String shipClassification = null;
		String query = " SELECT GET_CLASSIFICATION_BO (?, ?) SHIP FROM DUAL ";
		log.info("query shipClassification " + query.replace("?", item) + " : " + requestorId);
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item);
			pstmt.setLong(2, requestorId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("ship") != null) {
					shipClassification = rs.getString("ship");
					log.info("getOmsShipClassification " + shipClassification);
//					shipClassification = shipClassification.split("-")[0];
				}
			}
		} catch (Exception e) {
			log.warn("Exception while estabilishing the connection for shipClassification ", e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				pstmt.close();
			} catch (Exception e) {
			}
			try {
				con.close();
			} catch (Exception e) {
			}
		}
		return shipClassification;
	}

	private String findWallBracketSku(String item) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		String query = "SELECT ITEM FROM XX_WALL_BRACKET_SKU WHERE ITEM = ?";
		log.info("Query for WALL_BRACKET: " + query);

		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String fetchedItem = rs.getString("ITEM");
				if (fetchedItem != null && !fetchedItem.trim().isEmpty()) {
					result = fetchedItem;
				}
			}
			log.info("Result from WALL_BRACKET query: " + result);

		} catch (Exception e) {
			log.error("Exception while establishing the connection for shipClassification: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				log.warn("Error while closing result set: " + e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while closing statement: " + e.getMessage());
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection: " + e.getMessage());
			}
		}

		return result;
	}
}
