package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdItemDisc;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.utils.ProjectUtils;

public class OMSPersistence {
	public final static Logger log = ProjectUtils.getLog();
	BigDecimal omsCustOrdNo = null;
	boolean addressStateis999 = false;

	public OMSPersistence() {
		super();
	}

	public void checkNsaveOmsCustOrdHead(CustomerOrder input) throws SOAPException {
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "***Starting persisting in OmsCustOrdHead***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String subCustOrdNo = "";
		if (input.getCustomerSubOrderNo() == null) {
			subCustOrdNo = "1";
		} else {
			subCustOrdNo = input.getCustomerSubOrderNo();
		}
		log.info("subCustOrdNo=" + subCustOrdNo);
		if (session.getOmsCustOrdHeadFindByStatus(input.getApplicationId(), input.getCustomerOrderNo(), subCustOrdNo).size() > 0) {
			String errString = "Error while getting order Status";
			log.info("Duplicate entries found.Please try using new customer_order_no.");
			String languageCode = input.getCustomerLang();
			Boolean recordOmsErrorCodeExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.duplicateCustomerOrderNo, languageCode);
			if (Boolean.FALSE == recordOmsErrorCodeExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			String status = getOmsOrderStatus(input.getCustomerOrderNo());
			if ("S".equals(status)) {
				errString = OMSUtilCommons.formErrorDescription("Dup_Cust_Ord_Success", languageCode, new String[] {});
			} else if ("A".equals(status)) {
				errString = OMSUtilCommons.formErrorDescription("Dup_Cust_Ord_Inprogress", languageCode, new String[] {});
			}
			// String errString =
			// OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.duplicateCustomerOrderNo,
			// languageCode, new String[] { });
			log.error(errString);
			throw new SOAPException(errString);
		} else {
			OmsCustOrdHead omsCustOrdHead = new OmsCustOrdHead();
			// omsCustOrdHead.setCartNumber(input.getCartNumber());
			omsCustOrdHead.setSubCustOrderNo(subCustOrdNo);
			omsCustOrdHead.setEntityId(input.getEntityId());
			omsCustOrdHead.setApplicationId(input.getApplicationId());
			if (input.getComments() != null) {
				omsCustOrdHead.setComments(input.getComments());
			}
			// omsCustOrdHead.setContractFlag(input.getContractFlag());
			omsCustOrdHead.setOrderRequestorId(new BigDecimal(input.getOrderRequestorId()));
			omsCustOrdHead.setCustPhoneNo(input.getCustomerPhoneNo());
			omsCustOrdHead.setCustomerLang(input.getCustomerLang());
			omsCustOrdHead.setCustFirstName(input.getFirstName());
			omsCustOrdHead.setCustLastName(input.getLastName());
			omsCustOrdHead.setOrderCreateReserveInd(input.getOrderCreateReserveInd());
			if (input.getOrderCreateReserveInd().equals("R")) {
				omsCustOrdHead.setOrdPaymentStatus("P");
			} else {
				omsCustOrdHead.setOrdPaymentStatus("S");
			}
			omsCustOrdHead.setCustOrderNo((input.getCustomerOrderNo()));
			omsCustOrdHead.setCustId(input.getCustomerId());
			omsCustOrdHead.setCustOrderType(input.getOrderType());
			omsCustOrdHead.setDeliveryType(input.getDeliveryType());
			if (input.getPickLoc() != null) {
				omsCustOrdHead.setPickLoc(new BigDecimal(input.getPickLoc()));
			}
			omsCustOrdHead.setPayInStore(input.getPayInStoreInd());
			omsCustOrdHead.setConsumerDlyTime(new Timestamp(input.getConsumerDeliveryDate().toGregorianCalendar().getTimeInMillis()));
			omsCustOrdHead.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsCustOrdHead.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			omsCustOrdHead.setStatus("A"); // Active
			omsCustOrdHead.setSource(input.getSource());
			omsCustOrdHead.setConsumerDeliverySlot(input.getConsumerDeliverySlot());
			omsCustOrdHead.setDeliveryMode(input.getDeliveryModeType());
			omsCustOrdHead.setOrderCategory(input.getOrderCategory());
			if (input.getDeliveryAuthCode() != null) {
				omsCustOrdHead.setDeliveryAuthCode(new BigDecimal(input.getDeliveryAuthCode()));
			}
			if (input.getAddrVerifiedInd() != null) {
				omsCustOrdHead.setAddrVerifiedInd(input.getAddrVerifiedInd());
			} else {
				omsCustOrdHead.setAddrVerifiedInd("N");
			}
			if (input.getOmsReference() != null) {
				omsCustOrdHead.setOmsReference(input.getOmsReference());
			}
			if (input.getOmsMedium() != null) {
				omsCustOrdHead.setOmsMedium(input.getOmsMedium());
			}
			if (input.getOrdType() != null) {
				omsCustOrdHead.setOrdType(input.getOrdType());
			}
			if (input.getLockerId() != null) {
				omsCustOrdHead.setLockerId(input.getLockerId());
			}
			Date d1 = new Date();
			session.persistOmsCustOrdHead(omsCustOrdHead);
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "OmsCustOrdHead table persist success");
			Date d2 = new Date();
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in persisting the data in OmsCustOrdHead" + (d2.getTime() - d1.getTime()) + "milli seconds");
			if (input.cartNumber != null) {
				updateCartNumber(input.getCartNumber(), input.getCustomerOrderNo());
			}

		}
	}

	public void checkNsaveOmsCustOrdItem(CustomerOrder input) throws SOAPException {
		log.info("---------Begin  of checkNsaveOmsCustOrdItem method----------------------------");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> custOrdHeadSeq = session.getOmsCustOrdHeadFindDuplicate(input.getApplicationId(), input.getCustomerOrderNo(), "A");
		if (custOrdHeadSeq.size() == 0) {
			log.warn("No record found for OMS_CUST_ORD_NO , unable to insert in OmsCustOrdItem table");
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Database_Exception"));
		} else {
			omsCustOrdNo = custOrdHeadSeq.get(0);
			log.info("**OmsCustomerOrderNo=" + omsCustOrdNo + "******");
			for (CustomerOrderItems customerOrderItems : input.getCustomerOrderItems()) {
				OmsCustOrdItem omsCustOrdItem = new OmsCustOrdItem();
				log.info("=========customerOrderItems=============== " + customerOrderItems);
				omsCustOrdItem.setOmsCustOrdNo(omsCustOrdNo);
				omsCustOrdItem.setItem(customerOrderItems.getItem());
				omsCustOrdItem.setLineNo(new BigDecimal(customerOrderItems.getLineNo()));
				log.info("===========customerOrderItems.getLineNo()========== " + customerOrderItems.getLineNo());
				if (customerOrderItems.getLinkLineNo() != null) {
					log.info("customerOrderItems.getLinkLineNo() " + customerOrderItems.getLinkLineNo());
					omsCustOrdItem.setLineLinkNo(new BigDecimal(customerOrderItems.getLinkLineNo()));
				}
				log.info("Printing Ship Classification");
				omsCustOrdItem.setShipClassification(customerOrderItems.getShippingClassification());
				log.info("Ship Classification " + customerOrderItems.getShippingClassification());
				log.info("Substitute Indicator");
				if (customerOrderItems.getSubstitutionInd() == null) {
					omsCustOrdItem.setSubstituteAllowInd("N");
				} else {
					omsCustOrdItem.setSubstituteAllowInd(customerOrderItems.getSubstitutionInd());
				}
				log.info("customerOrderItems.getSubstitutionInd()" + customerOrderItems.getSubstitutionInd());
				log.info("Backorder Ind");
				omsCustOrdItem.setBackorderInd(customerOrderItems.getBackOrderInd());
				log.info("Backorder Ind: " + customerOrderItems.getBackOrderInd());
				omsCustOrdItem.setQtyOrderedSuom(customerOrderItems.getOrderQtySuom());
				String standardUom = "";
				log.info("Standard UOM");
				if (customerOrderItems.getStandardUom() != null) {
					omsCustOrdItem.setStandardUom(customerOrderItems.getStandardUom());
				} else {
					try {
						standardUom = session.getItemMasterFindStandardUom(customerOrderItems.getItem());
					} catch (Exception e1) {
						log.info("DataBase Exception while checking Standard UOM in Item Master Tables :" + e1);
						OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
						omsCutOrdHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCutOrdHead);
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Failed in persiting into oms_cust_ord_item table."));
					}
					omsCustOrdItem.setStandardUom(standardUom);
				}
				if (customerOrderItems.getTransactionUom() != null) {
					omsCustOrdItem.setTransactionUom(customerOrderItems.getTransactionUom());
				} else {
					omsCustOrdItem.setTransactionUom(standardUom);
				}
				log.info("Standard UOM : " + customerOrderItems.getStandardUom());
				log.info("Transaction UOM : " + customerOrderItems.getTransactionUom());
				omsCustOrdItem.setQtyCancelled(BigDecimal.ZERO);
				omsCustOrdItem.setCumQtyDelivered(BigDecimal.ZERO);
				omsCustOrdItem.setQtyReturned(BigDecimal.ZERO);
				omsCustOrdItem.setUnitRetail(customerOrderItems.getUnitRetail());

				// Start: Vat Amount @tsultana
				log.info("VAT Amount");
				log.info("Vat Amount: " + customerOrderItems.getUnitVatAmount());
				if (customerOrderItems.getUnitVatAmount() != null) {
					omsCustOrdItem.setUnitVatAmount(customerOrderItems.getUnitVatAmount());
				} else {
					omsCustOrdItem.setUnitVatAmount(BigDecimal.ZERO);
				}
				log.info("VAT Amount : " + omsCustOrdItem.getUnitVatAmount());
				// End: Vat Amount @tsultana

				if (customerOrderItems.getRetailCurrency() != null) {
					omsCustOrdItem.setRetailCurr(customerOrderItems.getRetailCurrency());
				}
				log.info("Item Comments ");
				if (customerOrderItems.getItemComments() != null) {
					omsCustOrdItem.setComments(customerOrderItems.getItemComments());
				}
				log.info("Item Comments : " + customerOrderItems.getItemComments());
				if (customerOrderItems.getOrigUnitRetail() != null) {
					omsCustOrdItem.setOrigUnitRetail(customerOrderItems.getOrigUnitRetail());
				}
				omsCustOrdItem.setCreateDatetime(new Timestamp(new Date().getTime()));
				omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
				omsCustOrdItem.setStatus("N"); // New
				// Code added for expected delivery date
				if (customerOrderItems.getExpectedDeliveryDateTime() != null) {
					omsCustOrdItem.setExpectedDeliveryDate(new Timestamp(customerOrderItems.getExpectedDeliveryDateTime().toGregorianCalendar().getTimeInMillis()));
					log.info("customerOrderItems.getExpectedDeliveryDate() : " + customerOrderItems.getExpectedDeliveryDateTime());
				}
				if (customerOrderItems.getJoodItem() != null) {
					omsCustOrdItem.setJoodItem(customerOrderItems.getJoodItem());
					log.info("JoodItem : " + customerOrderItems.getJoodItem());
				}
				if (customerOrderItems.getMarketplaceInd() != null) {
					omsCustOrdItem.setMarketPlaceInd(customerOrderItems.getMarketplaceInd());
					log.info("MarketplaceInd : " + customerOrderItems.getMarketplaceInd());
				} else {
					omsCustOrdItem.setMarketPlaceInd("N");
				}
				if (customerOrderItems.getApplyServiceInd() != null) {
					omsCustOrdItem.setApplyServiceInd(customerOrderItems.getApplyServiceInd());
					log.info("ApplyServiceInd : " + customerOrderItems.getApplyServiceInd());
				} else {
					omsCustOrdItem.setApplyServiceInd("N");
				}
				if (customerOrderItems.getOriginalTranOrderNo() != null) {
					omsCustOrdItem.setOriginalTranOrderNo(customerOrderItems.getOriginalTranOrderNo());
					log.info("OriginalTranOrderNo : " + customerOrderItems.getOriginalTranOrderNo());
				}
				Long TranLineNo = customerOrderItems.getOriginalTranLineNo();
				if (TranLineNo != null) {
					omsCustOrdItem.setOriginalTranLineNo(TranLineNo);
					log.info("OriginalTranLineNo : " + TranLineNo);
				}
				if (customerOrderItems.getServiceType() != null) {
					omsCustOrdItem.setServiceType(customerOrderItems.getServiceType());
					log.info("ServiceType : " + customerOrderItems.getServiceType());
				}
				if (customerOrderItems.getOrdSrId() != null) {
					omsCustOrdItem.setOrdSrId(customerOrderItems.getOrdSrId());
					log.info("OrdSrId : " + customerOrderItems.getOrdSrId());
				}
				if (customerOrderItems.getDelvEffort() != null) {
					omsCustOrdItem.setDelvEffort(customerOrderItems.getDelvEffort());
					log.info("delvEffort : " + customerOrderItems.getDelvEffort());
				}
				if (customerOrderItems.getRsaItem() != null) {
					omsCustOrdItem.setRsaItem(customerOrderItems.getRsaItem());
					log.info("customerOrderItems.getRsaItem() : " + customerOrderItems.getRsaItem());
				}
				try {
					Date d1 = new Date();
					session.persistOmsCustOrdItem(omsCustOrdItem);
					persistintoOmsOrdItemDesc(customerOrderItems);
					// mani added new code RSA Details insert
					if (customerOrderItems.getRsaItem() != null && customerOrderItems.getRsaItem().equalsIgnoreCase("true")) {
						insertRSADetails(customerOrderItems, omsCustOrdNo, input);
					}

					Date d2 = new Date();
					log.info("omsCustOrdNo " + omsCustOrdNo + "Time consumed in persisting the data in OmsCustOrdItem" + (d2.getTime() - d1.getTime()) + "milli seconds");
				} catch (Exception e) {
					log.info(e);
					OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCutOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCutOrdHead);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Failed in persiting into oms_cust_ord_item table."));
				}
			}
			log.info("OmsCustOrdItem table persist success");
		}
		log.info("------------------------End of checkNsaveOmsCustOrdItem method -----------------------");
	}

	public void checkNsaveOmsCustOrdAddress(CustomerOrder input) throws SOAPException {
		log.info("----------------Begin of checkNsaveOmsCustOrdAddress method--------------");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		if (input.getCustomerOrderAddress() != null) {
			OmsCustOrdAddress omsCustOrdAddress = new OmsCustOrdAddress();
			omsCustOrdAddress.setOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdAddress.setCustId(input.getCustomerId());
			if (input.getCustomerOrderAddress().getDeliverFirstName() != null) {
				omsCustOrdAddress.setDeliverFirstName(input.getCustomerOrderAddress().getDeliverFirstName());
			}
			if (input.getCustomerOrderAddress().getDeliverLastName() != null) {
				omsCustOrdAddress.setDeliverLastName(input.getCustomerOrderAddress().getDeliverLastName());
			}
			if (input.getCustomerOrderAddress().getDeliverAddress1() != null) {
				omsCustOrdAddress.setDeliverAdd1(input.getCustomerOrderAddress().getDeliverAddress1());
			}
			if (input.getCustomerOrderAddress().getDeliverAddress2() != null) {
				omsCustOrdAddress.setDeliverAdd2(input.getCustomerOrderAddress().getDeliverAddress2());
			}
			if (input.getCustomerOrderAddress().getDeliverAddress3() != null) {
				omsCustOrdAddress.setDeliverAdd3(input.getCustomerOrderAddress().getDeliverAddress3());
			}
			if (input.getCustomerOrderAddress().getDelvShortAddr() != null) {
				omsCustOrdAddress.setDelvShortAddr(input.getCustomerOrderAddress().getDelvShortAddr());
			}
			if (input.getCustomerOrderAddress().getDeliver_zone() != null) {
				omsCustOrdAddress.setZone(input.getCustomerOrderAddress().getDeliver_zone());
			}
			if (input.getCustomerOrderAddress().getDeliverCity() != null) {
				omsCustOrdAddress.setDeliverCity(input.getCustomerOrderAddress().getDeliverCity());
			}
			if (input.getCustomerOrderAddress().getDeliverState() != null) {
				omsCustOrdAddress.setDeliverState(input.getCustomerOrderAddress().getDeliverState());
			}
			if (input.getCustomerOrderAddress().getDeliverCountry() != null) {
				omsCustOrdAddress.setDeliverCountry(input.getCustomerOrderAddress().getDeliverCountry());
			}
			if (input.getCustomerOrderAddress().getDeliverPostal() != null) {
				omsCustOrdAddress.setDeliverPost(input.getCustomerOrderAddress().getDeliverPostal());
			}
			if (input.getCustomerOrderAddress().getDeliverPhoneNo() != null) {
				omsCustOrdAddress.setDeliverPhoneNo(input.getCustomerOrderAddress().getDeliverPhoneNo());
			}
			if (input.getCustomerOrderAddress().getLatitude() != null) {
				omsCustOrdAddress.setLatitude(input.getCustomerOrderAddress().getLatitude());
			}
			if (input.getCustomerOrderAddress().getLongitude() != null) {
				omsCustOrdAddress.setLongitude(input.getCustomerOrderAddress().getLongitude());
			}
			if (input.getCustomerOrderAddress().getBillFirstName() != null) {
				omsCustOrdAddress.setBillFirstName(input.getCustomerOrderAddress().getBillFirstName());
			}
			if (input.getCustomerOrderAddress().getBillLastName() != null) {
				omsCustOrdAddress.setBillLastName(input.getCustomerOrderAddress().getBillLastName());
			}
			if (input.getCustomerOrderAddress().getBillAddress1() != null) {
				omsCustOrdAddress.setBillAdd1(input.getCustomerOrderAddress().getBillAddress1());
			}
			if (input.getCustomerOrderAddress().getBillAddress2() != null) {
				omsCustOrdAddress.setBillAdd2(input.getCustomerOrderAddress().getBillAddress2());
			}
			if (input.getCustomerOrderAddress().getBillAddress3() != null) {
				omsCustOrdAddress.setBillAdd3(input.getCustomerOrderAddress().getBillAddress3());
			}
			if (input.getCustomerOrderAddress().getBillCity() != null) {
				omsCustOrdAddress.setBillCity(input.getCustomerOrderAddress().getBillCity());
			}
			if (input.getCustomerOrderAddress().getBillState() != null) {
				omsCustOrdAddress.setBillState(input.getCustomerOrderAddress().getBillState());
			}
			if (input.getCustomerOrderAddress().getBillCountry() != null) {
				omsCustOrdAddress.setBillCountry(input.getCustomerOrderAddress().getBillCountry());
			}
			if (input.getCustomerOrderAddress().getBillPostal() != null) {
				omsCustOrdAddress.setBillPost(input.getCustomerOrderAddress().getBillPostal());
			}
			if (input.getCustomerOrderAddress().getCompanyName() != null) {
				omsCustOrdAddress.setCompanyName(input.getCustomerOrderAddress().getCompanyName());
			}
			if (input.getCustomerOrderAddress().getVatRegNumber() != null) {
				omsCustOrdAddress.setVatRegNumber(input.getCustomerOrderAddress().getVatRegNumber());
			}
			if (input.getCustomerOrderAddress().getCrNumber() != null) {
				omsCustOrdAddress.setCrNumber(input.getCustomerOrderAddress().getCrNumber());
			}
			omsCustOrdAddress.setCreateDatetime(new Timestamp(new Date().getTime()));
			try {
				Date d1 = new Date();
				session.persistOmsCustOrdAddress(omsCustOrdAddress);
				Date d2 = new Date();
				log.info("omsCustOrdNo " + omsCustOrdNo + "Time consumed in persisting the data in OmsCustOrdAddress" + (d2.getTime() - d1.getTime()) + "milli seconds");
				log.info("OmsCustOrdAddress table persist success");

			} catch (Exception e) {
				OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				omsCutOrdHead.setStatus("F");
				session.mergeOmsCustOrdHead(omsCutOrdHead);
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Failed in persiting into oms_cust_ord_address table."));
			}
			if (omsCustOrdAddress.getBillState().equals("999")) {
				addressStateis999 = true;
			}
		}
		log.info("----------------End of checkNsaveOmsCustOrdAddress method--------------");
	}

	public void checkNsaveOmsCustOrdTender(CustomerOrder input) throws SOAPException {
		log.info("----------------Begin of checkNsaveOmsCustOrdTender method ------------");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCutOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		OmsCustOrdTender omsCustOrdTender = new OmsCustOrdTender();
		int i = 0;
		for (CustomerOrderTenders customerOrderTenders : input.getCustomerOrderTenders()) {
			omsCustOrdTender.setTenderTypeId(new BigDecimal(customerOrderTenders.getTenderTypeId()));
			omsCustOrdTender.setTenderTypeGroup(customerOrderTenders.getTenderType());
			omsCustOrdTender.setTenderAmt(customerOrderTenders.getTenderAmount());
			if (customerOrderTenders.getCcAuthNo() != null) {
				omsCustOrdTender.setCcAuthNo(customerOrderTenders.getCcAuthNo());
			}

			if (customerOrderTenders.getCcRefId() != null) {
				omsCustOrdTender.setCcRefId(customerOrderTenders.getCcRefId());
			}
			if (customerOrderTenders.getVoucherNo() != null) {
				omsCustOrdTender.setVoucherNo(customerOrderTenders.getVoucherNo());
			}
			if (customerOrderTenders.getTenderSubAcc() != null) {
				omsCustOrdTender.setTenderSubAcc(customerOrderTenders.getTenderSubAcc());
			}
			if (customerOrderTenders.getCcAuthSrc() != null) {
				omsCustOrdTender.setCcAuthSrc(customerOrderTenders.getCcAuthSrc());
			}
			if (customerOrderTenders.getCcCardholderVerf() != null) {
				omsCustOrdTender.setCcCardholderVerf(customerOrderTenders.getCcCardholderVerf());
			}
			if (customerOrderTenders.getCcEntryMode() != null) {
				omsCustOrdTender.setCcEntryMode(customerOrderTenders.getCcEntryMode());
			}
			if (customerOrderTenders.getCcExpDate() != null) {
				omsCustOrdTender.setCcExpDate(new Timestamp(customerOrderTenders.getCcExpDate().toGregorianCalendar().getTimeInMillis()));
			}
			if (customerOrderTenders.getCcNo() != null) {
				omsCustOrdTender.setCcNo(customerOrderTenders.getCcNo());
			}
			if (customerOrderTenders.getCcSpecCond() != null) {
				omsCustOrdTender.setCcSpecCond(customerOrderTenders.getCcSpecCond());
			}
			if (customerOrderTenders.getCcTermId() != null) {
				omsCustOrdTender.setCcTermId(customerOrderTenders.getCcTermId());
			}
			if (customerOrderTenders.getTenderRefId() != null) {
				omsCustOrdTender.setTenderRefId((customerOrderTenders.getTenderRefId().toString()));
			}
			omsCustOrdTender.setPaymentStatusInd(customerOrderTenders.getPymtStatusInd());
			omsCustOrdTender.setOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdTender.setTenderSeqNo(new BigDecimal(++i));
			omsCustOrdTender.setCreateDatetime(new Timestamp(new Date().getTime()));
			try {
				Date d1 = new Date();
				session.persistOmsCustOrdTender(omsCustOrdTender);
				Date d2 = new Date();
				log.info("omsCustOrdNo " + omsCustOrdNo + "Time consumed in persisting the data in omsCustOrdTender" + (d2.getTime() - d1.getTime()) + "milli seconds");
			} catch (Exception e) {
				omsCutOrdHead.setStatus("F");
				session.mergeOmsCustOrdHead(omsCutOrdHead);
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Failed in persiting into oms_cust_ord_tender table."));
			}
		}
		log.info("----------------End of checkNsaveOmsCustOrdTender method ------------");
		if (addressStateis999) {
			omsCutOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCutOrdHead);
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("INVALID BILLCITY," + omsCustOrdNo));
		}
	}

	public Boolean persistOmsTempCoFo(BigDecimal omscustomerOrderNo, String item, BigDecimal orderQty, BigDecimal sourceLocId, String sourceLocType, String fulfillLocationType,
			BigDecimal fulfillLocId) throws SOAPException, SOAPException {
		System.out.println("***Start-persistOmsTempCoFo***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsTempCoFo omsTempCoFo = new OmsTempCoFo();
		omsTempCoFo.setOmsCustOrdNo(omscustomerOrderNo);
		omsTempCoFo.setFulfillOrderNo(null); // unable insert null
		omsTempCoFo.setItem(item);
		omsTempCoFo.setOrderQty(orderQty);
		omsTempCoFo.setFoConfQty(BigDecimal.ZERO);
		omsTempCoFo.setSourceLocationType(sourceLocType);
		omsTempCoFo.setSourceLocId(sourceLocId);
		omsTempCoFo.setFulfillLocationType(fulfillLocationType);
		omsTempCoFo.setFulfillLocId(fulfillLocId);
		omsTempCoFo.setRmsResponseCode("");
		omsTempCoFo.setRmsErrorMsg("");
		omsTempCoFo.setCreateDatetime(new Timestamp(new Date().getTime()));
		omsTempCoFo.setStatus("NEW");
		session.persistOmsTempCoFo(omsTempCoFo);
		System.out.println("OmsTempCoFo persist success");
		return true;
	}

	public BigDecimal omsPersist(CustomerOrder input) throws SOAPException {
		checkNsaveOmsCustOrdHead(input);
		checkNsaveOmsCustOrdItem(input);
		checkNsaveOmsCustOrdAddress(input);
		checkNsaveOmsCustOrdTender(input);
		return omsCustOrdNo;
	}

	private void persistintoOmsOrdItemDesc(CustomerOrderItems customerOrderItem) throws SOAPException {
		log.info("omsCustOrdNo" + omsCustOrdNo + "persistintoOmsOrdItemDesc function Call Begin");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdItemDisc omsCustomerOrderItemDiscount = null;
		// fetching all discount associated to an item
		List<CustOrdItemDisc> customerOrderItemDiscounts = customerOrderItem.getCustOrdItemDisc();
		log.info("Getting the List of discount associated to item into customerOrderItemDiscounts Object");
		// looping/ iterating discounts one by one and inserting record into
		// OMS_CUST_ORD_ITEM_DISC table
		for (CustOrdItemDisc cusOrdItemDiscount : customerOrderItemDiscounts) {
			omsCustomerOrderItemDiscount = new OmsCustOrdItemDisc();
			log.info("Setting the value of customerOrderItemDiscount CustomerOrderNo value" + omsCustOrdNo);
			omsCustomerOrderItemDiscount.setOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdNo" + omsCustOrdNo + "setting the value of Line No Of customerOrderItemDiscount" + customerOrderItem.getLineNo());
			omsCustomerOrderItemDiscount.setLineNo(new BigDecimal(customerOrderItem.getLineNo()));
			log.info("omsCustOrdNo" + omsCustOrdNo + "setting the value of Discount Line No Of customerOrderItemDiscount" + cusOrdItemDiscount.getDiscLineNo());
			omsCustomerOrderItemDiscount.setDiscLineNo(new BigDecimal(cusOrdItemDiscount.getDiscLineNo()));
			log.info("omsCustOrdNo" + omsCustOrdNo + " setting the value of Promotion Type Of customerOrderItemDiscount" + cusOrdItemDiscount.getRmsPromoType());
			omsCustomerOrderItemDiscount.setRmsPromoType(cusOrdItemDiscount.getRmsPromoType());
			log.info("omsCustOrdNo" + omsCustOrdNo + "setting the value of Discount Amount Of customerOrderItemDiscount " + cusOrdItemDiscount.getUnitDiscountAmount());
			omsCustomerOrderItemDiscount.setUnitDiscountAmount(cusOrdItemDiscount.getUnitDiscountAmount());
			if (null != cusOrdItemDiscount.getDiscountType()) {
				log.info("omsCustOrdNo" + omsCustOrdNo + " setting the value of Discount type Of customerOrderItemDiscount" + cusOrdItemDiscount.getDiscountType());
				omsCustomerOrderItemDiscount.setDiscountType(cusOrdItemDiscount.getDiscountType());
			}
			if (null != cusOrdItemDiscount.getEmployeeId()) {
				log.info("omsCustOrdNo" + omsCustOrdNo + " setting the value of Employee No of customerOrderItemDiscount " + cusOrdItemDiscount.getEmployeeId());
				omsCustomerOrderItemDiscount.setEmployeeId(cusOrdItemDiscount.getEmployeeId());
			}
			log.info("Converting the value of Discount Reference No into Big Decimal" + cusOrdItemDiscount.getDiscRefNo());
			if (null != cusOrdItemDiscount.getDiscRefNo()) {
				BigDecimal discRefNo = new BigDecimal(cusOrdItemDiscount.getDiscRefNo());
				if (discRefNo != null) {
					log.info("omsCustOrdNo" + omsCustOrdNo + "Setting the value of Discount Reference No of customerOrderItemDiscount" + cusOrdItemDiscount.getDiscRefNo());
					omsCustomerOrderItemDiscount.setDiscRefNo(discRefNo);
				}
			}
			if (null != cusOrdItemDiscount.getPromoCompId()) {
				log.info(" Converting promotion ComponentId value into BigDeciaml" + cusOrdItemDiscount.getPromoCompId());
				BigDecimal promoCompId = new BigDecimal(cusOrdItemDiscount.getPromoCompId());
				if (null != promoCompId) {
					log.info("omsCustOrdNo" + omsCustOrdNo + "Setting the value Of Promotion Component Id of customerOrderItemDiscount" + cusOrdItemDiscount.getPromoCompId());
					omsCustomerOrderItemDiscount.setPromoCompId(promoCompId);
				}
			}
			if (null != cusOrdItemDiscount.getSimplePromoInd()) {
				log.info("omsCustOrdNo" + omsCustOrdNo + "Setting the value Of Promotion Component Id of customerOrderItemDiscount" + cusOrdItemDiscount.getSimplePromoInd());
				omsCustomerOrderItemDiscount.setSimplePromoInd(cusOrdItemDiscount.getSimplePromoInd());
			}
			omsCustomerOrderItemDiscount.setCreateDatetime(new Timestamp(new java.util.Date().getTime()));
			try {
				log.info("trying Persist into OmsCustOrdItemDisc table");
				session.persistOmsCustOrdItemDisc(omsCustomerOrderItemDiscount);
				log.info("OmsCustOrdItemDisc table persist success");
			} catch (Exception e) {
				log.info(e);
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Failed in persiting into oms_cust_ord_item_disc table."));
			}
		}
	}

	private void updateCartNumber(String cartNumber, String custOrderNo) {
		log.info("CART NUMBER UPDATION Calling updateCartNumber method");
		Connection connection = null;
		PreparedStatement prepStatement = null;
		String query = null;
		ResultSet rs = null;
		int candidateId = 0;
		Long l = new Long(10);
		int i = l.intValue();
		// String carrierCity = null;
		// query = "select CARRIER_CITY from xx_vxref_carrier_city where EXTRA_CITY= ?
		// and CARRIER_CODE = ? ";
		try {
			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("connection of Delivery City : " + connection);
			String update = "update oms_cust_ord_head set CART_NUMBER=? where cust_order_no=?";
			prepStatement = connection.prepareStatement(update);
			prepStatement.setString(1, cartNumber);
			prepStatement.setString(2, custOrderNo);
			int rowAffected = prepStatement.executeUpdate();
			if (rowAffected == 1) {
				// get candidate id
				rs = prepStatement.getGeneratedKeys();
				if (rs.next())
					candidateId = rs.getInt(1);
			}
			log.info("UPDATE QUERY CART NUMBER" + cartNumber + "," + custOrderNo);
		} catch (Exception e) {
			log.info("Exception e " + e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(connection, prepStatement, rs);
				prepStatement.close();
				rs.close();
				connection.close();
			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}
	} // end of method updateCartNumber

	private String getOmsOrderStatus(String custOrderNo) {
		log.info("get Oms Order Status");
		Connection connection = null;
		PreparedStatement prepStatement = null;
		String status = "";
		ResultSet rs = null;
		try {
			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("connection of Delivery City : " + connection);
			String getQuery = "select status from oms_cust_ord_head where CUST_ORDER_NO = ? and (status='S' OR status='A')";
			prepStatement = connection.prepareStatement(getQuery);
			prepStatement.setString(1, custOrderNo);
			rs = prepStatement.executeQuery();
			if (rs.next()) {
				status = rs.getString("status");
			}
		} catch (Exception e) {
			log.info("Exception e " + e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(connection, prepStatement, rs);
				prepStatement.close();
				rs.close();
				connection.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return status;
	}

	private void insertRSADetails(CustomerOrderItems customerOrderItems, BigDecimal omCustOrdNo, CustomerOrder input) throws Exception {

		log.info(input.getCustomerOrderNo() + " ***insert into OMS_CUST_ORD_RSA ***");
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "insert into OMS_CUST_ORD_RSA(CUST_ORDER_NO, OMS_CUST_ORD_NO, RSA_BRAND, RSA_YEAR, RSA_NUMBER, ITEM, LINE_NO) values(?,?,?,?,?,?,?)";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			statement = conn.prepareStatement(query);
			statement.setString(1, input.getCustomerOrderNo());
			statement.setBigDecimal(2, omCustOrdNo);
			statement.setString(3, customerOrderItems.getRSADetails().get(0).getRsaBrand());
			statement.setLong(4, customerOrderItems.getRSADetails().get(0).getRsaYear());
			statement.setString(5, customerOrderItems.getRSADetails().get(0).getRsaNumber());
			statement.setString(6, customerOrderItems.getItem());
			statement.setLong(7, customerOrderItems.getLineNo());

			statement.executeUpdate();
			log.info(input.getCustomerOrderNo() + "Successfully inserted into OMS_CUST_ORD_RSA..");
		} catch (Exception e) {
			log.info(input.getCustomerOrderNo() + " Failed to inserting into OMS_CUST_ORD_RSA table : " + e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(conn,statement,rs);
				statement.close();
				conn.close();
			} catch (Exception e) {
				log.info(input.getCustomerOrderNo() + "Exception while closing the finally block while inserting in OMS_CUST_ORD_RSA " + e.getMessage());
			}
		}
	}

}
