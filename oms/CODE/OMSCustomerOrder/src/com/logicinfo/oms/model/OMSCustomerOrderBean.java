package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.ItemUnavailabilityStatus;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.logicinfo.oms.util.OmsStatusUpdateForReturnPickupCancellation;
import com.logicinfo.oms.utils.ProjectUtils;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

public class OMSCustomerOrderBean {
	public final static Logger log = ProjectUtils.getLog();
	BigDecimal custOrdHeadSeqNo = null;
	SourceLocIdentify omsSourceLocIdentify = new SourceLocIdentify();
	SourceLocationIdentifier sourceLocationIdentifier = new SourceLocationIdentifier();

	public OMSCustomerOrderBean() {
		super();
	}

	public void validateInput(CustomerOrder input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "*** Start validation ***");
		if (input.getOrderType().equals("B2B")) {
			if (input.getCustomerSubOrderNo() == null) {
				String languageCode = input.getCustomerLang();
				Boolean flag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.SubCustomerReqid, input.getCustomerLang());
				if (Boolean.FALSE == flag) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.SubCustomerReqid, languageCode, new String[] {});
				log.error(errString);
				throw new SOAPException(errString);
			}
		}
		if (input.getDeliveryType().equals("S") || input.getDeliveryType().equals("SS") || input.getDeliveryType().equals("SC")) {
			if (input.getCustomerOrderAddress() == null) {
				// In case for ship to customer ,address is required.
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CUST_ADD_REQ"));
				String languageCode = input.getCustomerLang();
				Boolean flag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.customerAddressReqid, languageCode);
				if (Boolean.FALSE == flag) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.customerAddressReqid, languageCode, new String[] {});
				log.error(errString);
				throw new SOAPException(errString);
			}
		}
		if (input.getDeliveryType().equals("C") || input.getDeliveryType().equals("CC")) {
			if (input.getPickLoc() == null) {
				// In case for customer pick up ,pick_loc is required.
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault("PICK_LOC_REQ"));
				String languageCode = input.getCustomerLang();
				Boolean flag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.pickUpLocationReqid, languageCode);
				if (Boolean.FALSE == flag) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.pickUpLocationReqid, languageCode, new String[] {});
				log.error(errString);
				throw new SOAPException(errString);
			}
		}
		for (CustomerOrderTenders tender : input.getCustomerOrderTenders()) {
			List<String> codeList = session.getCodeDetailFindCode("TENT");
			int found = 0;
			for (String code : codeList) {
				if (code.equals(tender.getTenderType()) == true) {
					found = 1;
					break;
				}
			}
			if (found == 0) {
				String languageCode = input.getCustomerLang();
				Boolean flag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.invalidTenderCode, languageCode);
				if (Boolean.FALSE == flag) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.invalidTenderCode, languageCode, new String[] { tender.getTenderType() });
				log.error(errString);
				throw new SOAPException(errString);
			}
			if (tender.getTenderType().equals("CHECK") || tender.getTenderType().equals("GCARD") || tender.getTenderType().equals("VOUCH")) {
				if (tender.getTenderRefId() == null) {
					// If tender type='CHECK/GIFTCARD/VOUCHER' then tender_ref_id is required.
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("TEND_REF_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean flag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.tenderRefernceReqid, languageCode);
					if (Boolean.FALSE == flag) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.tenderRefernceReqid, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
			} else if (tender.getTenderType().equals("CCARD") || tender.getTenderType().equals("DCARD")) {
				if (tender.getCcNo() == null) {
					// cc_no is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_NO_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean recordExistFlag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.creditCardNoReqId, languageCode);
					if (recordExistFlag == Boolean.FALSE) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.creditCardNoReqId, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcAuthNo() == null) {
					// cc_auth_no is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_AUTH_NO"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorCoderecordExistFlag = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CreditCardAuthorizartionNo, languageCode);
					if (Boolean.FALSE == omsErrorCoderecordExistFlag) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CreditCardAuthorizartionNo, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcAuthSrc() == null) {
					// cc_auth_src is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_AUTH_SRC"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.creditCardAuthorizationSourceNo, languageCode);
					if (Boolean.FALSE == omsErrorRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.creditCardAuthorizationSourceNo, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcCardholderVerf() == null) {
					// cc_cardholder_verf is required field in case of tender_type ='CCARD' or
					// 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_HOLDER_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.creditCardHolderRequired, languageCode);
					if (Boolean.FALSE == omsErrorRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.creditCardHolderRequired, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcEntryMode() == null) {
					// cc_entry_mode is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_ENTRY_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CreditCardEntryReqId, languageCode);
					if (Boolean.FALSE == omsErrorCodesRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CreditCardEntryReqId, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcExpDate() == null) {
					// cc_exp_date is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_EXP_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CreditCardExpiryDateReqId, languageCode);
					if (Boolean.FALSE == omsErrorCodesRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CreditCardExpiryDateReqId, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcSpecCond() == null) {
					// cc_spec_cond is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_SPEC_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CreditCardSpecReqid, languageCode);
					if (Boolean.FALSE == omsErrorCodesRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CreditCardSpecReqid, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
				if (tender.getCcTermId() == null) {
					// cc_term_id is required field in case of tender_type ='CCARD' or 'DCARD'
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault("CC_TERM_REQ"));
					String languageCode = input.getCustomerLang();
					Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CreditCardTermsReqId, languageCode);
					if (Boolean.FALSE == omsErrorCodesRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CreditCardTermsReqId, languageCode, new String[] {});
					log.error(errString);
					throw new SOAPException(errString);
				}
			}
		}
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "*** validation succeesfully completed ***");
	}

	public ItemUnavailabilityStatus findSourceLocation(CustomerOrder input)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		ItemUnavailabilityStatus itemUnavailabilityStatus = sourceLocationIdentifier.OrderPickUpModule(input, custOrdHeadSeqNo);
		return itemUnavailabilityStatus;
	}

	public void persistData(CustomerOrder input) throws SOAPException {
		OMSPersistence omsPersistence = new OMSPersistence();
		custOrdHeadSeqNo = omsPersistence.omsPersist(input);
	}

	public void checkCreateOrReserveOrder(CustomerOrder input) throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException, BusinessException {
		log.info("inside checkCreateOrReserveOrder");
		// SourceLocIdentify omsSourceLocIdentify = new SourceLocIdentify();
		if (sourceLocationIdentifier.getMap() != null && sourceLocationIdentifier.getMap().size() > 0 && sourceLocationIdentifier.getMap().keySet() != null) {
			if (input.getOrderCreateReserveInd().equals("R")) {
				RmsPackage rmsPackageCall = new RmsPackage();
				log.info("omscustordNo" + custOrdHeadSeqNo + " sourceLocationIdentifier.getMap().size() " + sourceLocationIdentifier.getMap().size());
				log.info("omscustordNo" + custOrdHeadSeqNo + " Map KeySet " + sourceLocationIdentifier.getMap().keySet());
				rmsPackageCall.persistOmsCustOrdReserve(custOrdHeadSeqNo, sourceLocationIdentifier.getMap());
				log.info("Calling RMSPackage Call : " + input.getCustomerId());
				rmsPackageCall.rmsPackageCall(sourceLocationIdentifier.getMap());
				log.info("End of calling RMSPackage call: " + input.getCustomerId());
				InterfacePersistence interfacePersistence = new InterfacePersistence();
				interfacePersistence.adjustInventoryByItemLocation(sourceLocationIdentifier.getMap(), input.getCustomerOrderNo());
			}
			// Step 5 : Call RMS and SIM webservice in case of NON SADAD Payment
			else {
				NonSADADPayment nonSADADPayment = new NonSADADPayment();
				log.info("omscustordNo" + custOrdHeadSeqNo + "sourceLocationIdentifier.getMap().size() " + sourceLocationIdentifier.getMap().size());
				nonSADADPayment.processNonSadad(sourceLocationIdentifier.getMap(), custOrdHeadSeqNo, input);
			}
		}
	}

	public void splitTender(CustomerOrder input) throws SOAPException {
		for (CustomerOrderTenders tender : input.getCustomerOrderTenders()) {
			if (tender.getTenderType().equals("VOUCH")) {
				TenderSplit tenderSplit = new TenderSplit();
				tenderSplit.tenderSplit(custOrdHeadSeqNo);
				break;
			}
		}
	}

	public void persistRTLog(CustomerOrder input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		if (input.getPayInStoreInd().equals("N") && input.getOrderCreateReserveInd().equals("C")) {
			for (CustomerOrderItems customerOrderItems : input.getCustomerOrderItems()) {
				OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
				omsRtlogPublishLog.setLocation(new BigDecimal(input.getOrderRequestorId()));
				omsRtlogPublishLog.setItem(customerOrderItems.getItem());
				omsRtlogPublishLog.setLineNo(new BigDecimal(customerOrderItems.getLineNo()));
				omsRtlogPublishLog.setPublishedInd("N");
				omsRtlogPublishLog.setOmsCustOrdNo(custOrdHeadSeqNo);
				omsRtlogPublishLog.setTranType("ORI");
				omsRtlogPublishLog.setQty(customerOrderItems.getOrderQtySuom());
				omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
				Date d1 = new Date();
				log.info("omsCustOrdNo" + custOrdHeadSeqNo + "while persiting into omsRtlogPublishLog " + d1);
				session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
				Date d2 = new Date();
				log.info("omsCustOrdNo" + custOrdHeadSeqNo + "after persiting into omsRtlogPublishLog " + (d2.getTime() - d1.getTime()) + " in milliseconds");
			}
		}
	}

	public void notifySiebel(CustomerOrder input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		if (!input.getApplicationId().equals("SIEBEL_CRM")) {
			InterfacePersistence interfacePersistance = new InterfacePersistence();
			try {
				if (session.getOmsSystemParametersFindIndValue("CALL_SIEBEL", "OMS_SYSTEM_OPTION").equals("Y")) {
					log.info("Calling siebel");
					// interfacePersistance.callSeibelWebservice(input, custOrdHeadSeqNo);
					OmsOrderStatusUpdateHeader omsOrderStatusHeaderObj = interfacePersistance.getOmsOrderStatusUpdateHeader(input, custOrdHeadSeqNo);
					OmsStatusUpdateForReturnPickupCancellation omsStatusUpdate = new OmsStatusUpdateForReturnPickupCancellation();
					omsStatusUpdate.callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(omsOrderStatusHeaderObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void bookingODDSlot(CustomerOrder input) {
		try {
			oddPackageCall(input, custOrdHeadSeqNo);

		} catch (Exception e) {
			log.error("FAILED CALLING ODD PACKAGE order number - " + input.getCustomerOrderNo(), e);
		}

	}

	public static Connection createConnection() {
		log.info("***Start createConnection***");
		Connection connection = null;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("jdbc/oms");
			connection = ds.getConnection();
			log.info("connected to db");
		} catch (Exception e) {
			log.info("unable to connect to database");
		}
		return connection;

	}

	private void oddPackageCall(CustomerOrder input, BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("CALLING ON DEMAND DELIVERY PACKAGE");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection con = null;
		CallableStatement pstmt = null;
		BigDecimal sourceLoc = null;
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
		OmsCoFulfillDetail omsCoFulfillDetail = omsCoFulfillDetailList.get(0);
		sourceLoc = omsCoFulfillDetail.getSourceLoc();

		String slot = input.getConsumerDeliverySlot();
		String trimmedSlot = slot.trim();
		String window = trimmedSlot.substring(0, 2);
		log.info("WINDOW: " + window + " source loc - " + sourceLoc);
		try {
			con = createConnection();
			pstmt = con.prepareCall("{call XXHDB_CORE_PKG.reserve_booking_small(?,?,?,?,?,?,?,?)}");
			pstmt.setInt(1, omsCustOrdNo.intValue());
			pstmt.setString(2, input.customerOrderNo);
			pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			pstmt.setString(4, window);
			pstmt.setInt(5, sourceLoc.intValue());
			pstmt.setString(6, input.getCustomerPhoneNo());
			pstmt.registerOutParameter(7, Types.VARCHAR);
			pstmt.registerOutParameter(8, Types.VARCHAR);

			pstmt.executeUpdate();
			String status = pstmt.getString(7);
			String msg = pstmt.getString(8);
			log.info("result is -: status-" + status + " msg-" + msg);
			if ("S".equals(status)) {
				log.info("ODD Package Call is SUCCESS");
			} else {
				log.info("ODD Package Call is FAILED" + msg);
			}
		} catch (Exception e) {
			log.error("Error while calling package for oms cust order number " + omsCustOrdNo, e);
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.warn(e);
			}
			try {
				con.close();
			} catch (SQLException e) {
				log.warn(e);
			}
		}
	}

	public CustomerOrderResponse generateResponse(CustomerOrder input, String serviceStatus, String errorCode, ItemUnavailabilityStatus itemUnavailabilityStatus)
			throws SOAPException, com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("inside bean class generateResponse");
		ResponseProcessing responseProcessing = new ResponseProcessing();
		return responseProcessing.generateResponse(input, serviceStatus, errorCode, custOrdHeadSeqNo, itemUnavailabilityStatus);
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
		log.info("Starting rollback");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("fulfullDetailMap map size=" + fulfullDetailMap.size());
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		if (fulfullDetailMap.size() >= 1) {
			for (BigDecimal key : fulfullDetailMap.keySet()) {
				ArrayList<OmsTempCoFo> list = fulfullDetailMap.get(key);
				log.info("List size" + list.size());
				log.info("Processing app=" + list.get(0).getProcessingApp());
				if (list.get(0).getProcessingApp().equals("RMS") && list.size() > 1) {
					if (list.get(0).getSourceLocId().compareTo(list.get(0).getFulfillLocId()) == 0) {
						interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
						log.info("callSIMCancelllationWS successful");
					}
					interfacePersistence.callRMSCancellationWebservice(list.get(0).getOmsCustOrdNo(), list);
					log.info("callRMSCancellationWebservice successful");
				} else {
					interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
					log.info("callSIMCancelllationWS successful");
				}
				log.info(">>>");
				/*
				 * for(OmsTempCoFo temp:omsTempCoFoList) {
				 * if(temp.getProcessingApp().equals("RMS")) {
				 * if(temp.getSourceLocId().compareTo(temp.getFulfillLocId())==0) {
				 * interfacePersistence.callSIMCancelllationWS(temp.getOmsCustOrdNo(), temp); }
				 * 
				 * interfacePersistence.callRMSCancellationWebservice(temp.getOmsCustOrdNo(),
				 * temp); } }
				 */
			}
		}
	}

	public CustomerOrderResponse createResponse(CustomerOrder input, BigDecimal custOrdHeadSeqNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(custOrdHeadSeqNo);
		omsCustOrdHead.setStatus("S");
		session.mergeOmsCustOrdHead(omsCustOrdHead);
		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setApplicationId(input.getApplicationId());
		response.setOmsCustomerOrderNo(custOrdHeadSeqNo.longValue());
		response.setCustomerSubOrderNo(input.getCustomerSubOrderNo());
		response.setEntityId(input.getEntityId());
		response.setCustomerOrderNo(input.getCustomerOrderNo());
		response.setRequestDatetimestamp(input.getRequestDatetimestamp());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		response.setResponseDatetimestamp(now);
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(custOrdHeadSeqNo);
		Map<String, ArrayList<OmsCoFulfillDetail>> responseMap = new HashMap<String, ArrayList<OmsCoFulfillDetail>>();
		for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
			if (responseMap.get(omsCoFulfillDetail.getItem()) == null || responseMap.get(omsCoFulfillDetail.getItem()).size() == 0) {
				log.info("Creating map with new key+" + omsCoFulfillDetail.getItem());
				ArrayList<OmsCoFulfillDetail> tempList = new ArrayList<OmsCoFulfillDetail>();
				tempList.add(omsCoFulfillDetail);
				responseMap.put(omsCoFulfillDetail.getItem(), tempList);
			} else {
				log.info("Adding temp record with existing key=" + omsCoFulfillDetail.getItem());
				ArrayList<OmsCoFulfillDetail> existingList = responseMap.get(omsCoFulfillDetail.getItem());
				existingList.add(omsCoFulfillDetail);
				responseMap.put(omsCoFulfillDetail.getItem(), existingList);
			}
		}
		List<CustomerOrderResponseItems> responseItemLists = response.getCustomerOrderResponseItems();
		for (String key : responseMap.keySet()) {
			log.info("Creating fulfilmap key=" + key);
			ArrayList<OmsCoFulfillDetail> list = responseMap.get(key);
			CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
			List<CustomerOrderResponseItemFulfillment> fulfilList = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
			customerOrderResponseItems.setItem(key);
			customerOrderResponseItems.setStatus("AVAILABLE");
			customerOrderResponseItems.setStatusMessage("SUCCESS");
			BigDecimal orderQty = BigDecimal.ZERO;
			BigDecimal fulfilQty = BigDecimal.ZERO;
			for (OmsCoFulfillDetail omsCoFulfillDetail : list) {
				orderQty = omsCoFulfillDetail.getFulfillReqQty().add(orderQty);
				fulfilQty = omsCoFulfillDetail.getFulfillConfQty().add(fulfilQty);
				customerOrderResponseItems.setOrderQtySuom(orderQty);
				customerOrderResponseItems.setFulfillQtySuom(fulfilQty);
				CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
				log.info("reponse of item=" + omsCoFulfillDetail.getItem());
				item.setItem(omsCoFulfillDetail.getItem());
				item.setOrderQtySuom(omsCoFulfillDetail.getFulfillReqQty());
				item.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
				item.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
				item.setFulfillQtySuom(omsCoFulfillDetail.getFulfillConfQty());
				item.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
				item.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
				BigDecimal poNo = session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(), omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(),
						omsCoFulfillDetail.getFulfillLoc()).get(0).getOrderNo();
				if (poNo != null) {
					item.setPoNo(poNo.longValue());
				}
				BigDecimal tsfN0 = session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(), omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(),
						omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo();
				if (tsfN0 != null) {
					item.setTsfNo(session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(), omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(),
							omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo().longValue());
				}
				fulfilList.add(item);
			}
			responseItemLists.add(customerOrderResponseItems);
		}
		if (input.getOrderCreateReserveInd().equals("R")) {
			List<CustomerOrderResponseItems> responseItemList = response.getCustomerOrderResponseItems();
			for (CustomerOrderItems coItem : input.getCustomerOrderItems()) {
				log.info("generating reposnse");
				CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
				customerOrderResponseItems.setItem(coItem.getItem());
				customerOrderResponseItems.setOrderQtySuom(coItem.getOrderQtySuom());
				List<CustomerOrderResponseItemFulfillment> fulfillmentlist = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
				CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
				item.setItem(coItem.getItem());
				item.setOrderQtySuom(coItem.getOrderQtySuom());
				if (coItem.getBackOrderInd().equals("N")) {
					if (input.getOrderCreateReserveInd().equals("R")) {
						log.info("inside reserve response");
						BigDecimal loc = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(custOrdHeadSeqNo, coItem.getItem(), new BigDecimal(coItem.getLineNo())).get(0).getRmsResvLoc();
						if (loc != null || loc.longValue() >= 0) {
							log.info("inside loc if");
							item.setRMSResvLoc(loc.longValue());
						}
						String locType = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(custOrdHeadSeqNo, coItem.getItem(), new BigDecimal(coItem.getLineNo())).get(0).getRmsResvLocType();
						if (locType != null || locType.isEmpty() == false) {
							item.setRMSResvLocType(locType);
						}
						BigDecimal resvQty = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(custOrdHeadSeqNo, coItem.getItem(), new BigDecimal(coItem.getLineNo())).get(0).getRmsResvQty();
						if (resvQty != null || resvQty.longValue() >= 0) {
							item.setRMSResvQty(resvQty.longValue());
						}
						fulfillmentlist.add(item);
					}
				} else {
					item.setBackorderQty(coItem.getOrderQtySuom().longValue());
					fulfillmentlist.add(item);
				}
				response.setResponseMessage("SUCCESSFUL");
				responseItemList.add(customerOrderResponseItems);
			}
		}
		return response;
	}

	public CustomerOrderResponse createResponseForFailedItems(CustomerOrder input, BigDecimal custOrdHeadSeqNo, List<ItemAvailability> itemList) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setApplicationId(input.getApplicationId());
		// response.setCustomerId(input.getCustomerId());
		response.setOmsCustomerOrderNo(custOrdHeadSeqNo.longValue());
		response.setCustomerSubOrderNo(input.getCustomerSubOrderNo());
		response.setEntityId(input.getEntityId());
		// response.setComments(input.getComments());
		response.setCustomerOrderNo(input.getCustomerOrderNo());
		response.setRequestDatetimestamp(input.getRequestDatetimestamp());
		response.setResponseMessage("FAILED");
		response.setMessageStatus("F");
		response.setMessageDesc("Unable to process request");
		response.setResponseMessage("FAILED");
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		response.setResponseDatetimestamp(now);
		List<CustomerOrderResponseItems> responseItemList = response.getCustomerOrderResponseItems();
		for (ItemAvailability item : itemList) {
			CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
			customerOrderResponseItems.setItem(item.getItem());
			customerOrderResponseItems.setOrderQtySuom(item.getOrderQty());
			customerOrderResponseItems.setFulfillQtySuom(item.getAvailableQty());
			customerOrderResponseItems.setStatus("UNAVAILABLE");
			customerOrderResponseItems.setStatusMessage(item.getErrorMessage());
			responseItemList.add(customerOrderResponseItems);
		}
		return response;
	}

	public CustomerOrderResponse createResponseForInputValidation(CustomerOrder input, String errorCode) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setApplicationId(input.getApplicationId());
		// response.setCustomerId(input.getCustomerId());
		// response.setOmsCustomerOrderNo(custOrdHeadSeqNo.longValue());
		if (input.getOrderType().equals("B2B")) {
			response.setCustomerSubOrderNo(input.getCustomerSubOrderNo());
		} else {
			response.setCustomerSubOrderNo("1");
		}
		response.setEntityId(input.getEntityId());
		// response.setComments(input.getComments());
		response.setCustomerOrderNo(input.getCustomerOrderNo());
		response.setRequestDatetimestamp(input.getRequestDatetimestamp());
		// Below codes are changed to omsErrorRecord based on input customer language
		// session.getOmsErrorCodesFindByErrorCode(errorCode, input.getCustomerLang());
		// removed to fix 3209 Bug
		OmsErrorCodes omsError = OMSUtilCommons.getOmsErrorCodesObject(errorCode, input.getCustomerLang());
		response.setResponseMessage("FAILED");
		response.setMessageStatus("F");
		response.setMessageCode(omsError.getOmsErrorCode());
		response.setMessageDesc(omsError.getOmsErrLangDesc());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		response.setResponseDatetimestamp(now);
		return response;
	}

	public OmsErrorCodes getErrorMessage(String errorCode, String langCode) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsErrorCodes omsErrorCode = session.getOmsErrorCodesFindByErrorCode(errorCode, langCode);
		return omsErrorCode;
	}
}
