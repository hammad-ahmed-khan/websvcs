package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.ItemUnavailabilityStatus;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.CfsSmsEmailStatusInfo;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.utils.ProjectUtils;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException;

public class ResponseProcessing {
	public static final Logger log = ProjectUtils.getLog();

	public CustomerOrderResponse generateResponse(CustomerOrder input, String serviceStatus, String errorCode, BigDecimal omsCustOrdNo, ItemUnavailabilityStatus itemUnavailabilityStatus)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		log.info("omsCustOrdNo " + omsCustOrdNo + "inside response processing");
		log.info("omsCustOrdNo " + omsCustOrdNo + "Service status" + serviceStatus);
		log.info("omsCustOrdNo " + omsCustOrdNo + "errorCode=" + errorCode);
		if (omsCustOrdNo == null) {
			log.info("checking errorcode contains INVALID BILLCITY ");
			if (errorCode.contains("INVALID BILLCITY")) {
				String errorValue[] = errorCode.split(",");
				omsCustOrdNo = new BigDecimal(errorValue[1]);
				errorCode = errorValue[0];
				log.info("yes errorcode contains INVALID BILLCITY for omsCustOrdNo " + omsCustOrdNo + " and code is errorCode from spliting the string is" + errorCode);
			}
		}
		String subCustomerOrderNo = "1";
		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setApplicationId(input.getApplicationId());
		Map<BigDecimal, ArrayList<SfsConfirmationPojo>> sfsConfirmationPojomap = null;
		ArrayList<SfsConfirmationPojo> SfsConfirmationPojoList = null;
		if (input.getOrderType().equals("B2B")) {
			if (input.getCustomerSubOrderNo() != null) {
				response.setCustomerSubOrderNo(input.getCustomerSubOrderNo());
				subCustomerOrderNo = input.getCustomerSubOrderNo();
			}
		} else {
			response.setCustomerSubOrderNo("1");
		}
		String extCustOrdNo = input.getCustomerOrderNo().trim() + subCustomerOrderNo;
		if (subCustomerOrderNo.equals("1")) {
			extCustOrdNo = input.getCustomerOrderNo();
		} else if (subCustomerOrderNo.trim().length() < 3) {
			if (subCustomerOrderNo.trim().length() == 2) {
				extCustOrdNo = input.getCustomerOrderNo().trim() + "0" + subCustomerOrderNo.trim();
			} else {
				extCustOrdNo = input.getCustomerOrderNo().trim() + "00" + subCustomerOrderNo.trim();
			}
		}
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
		if (serviceStatus.equals("VALIDATE_ERROR")) {
			if (omsCustOrdNo != null) {
				response.setOmsCustomerOrderNo(omsCustOrdNo.longValue());
			}
			response.setResponseMessage("FAILED");
			response.setMessageStatus("F");
			try {
				if (null != errorCode) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Paring the error:" + errorCode);
					OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errorCode);
					response.setMessageCode(theErrorObj.getOmsErrorCode());
					response.setMessageDesc(theErrorObj.getOmsErrLangDesc());
				} else {
					response.setMessageDesc("");
				}
				response.setMessageStatus("F");
				log.info("omsCustOrdNo " + omsCustOrdNo + "setting the status to F in OmsCustOrdHead");
				if (omsCustOrdNo != null) {
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					log.info("omsCustOrdNo " + omsCustOrdNo + "updated to F");
					// updateRequestResponseDetails(response);
				}
			} catch (Exception e) {
				log.error("No data exist in oms_error_codes for error_code=" + errorCode);
			}
		} else if (serviceStatus.equals("UNAVL_COMB_ID")) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "--------------------------------");
			log.info("omsCustOrdNo " + omsCustOrdNo + "Error code " + errorCode);
			response.setOmsCustomerOrderNo(Long.valueOf(omsCustOrdNo.longValue()));
			response.setResponseMessage("FAILED");
			response.setMessageStatus("F");
			try {
				OmsErrorCodes omsError = OMSUtilCommons.getOmsErrorCodesObject(errorCode, input.getCustomerLang());
				response.setMessageCode(omsError.getOmsErrorCode());
				response.setMessageDesc(omsError.getOmsErrLangDesc());
			} catch (Exception e) {
				log.warn("No data exist in oms_error_codes for error_code = " + errorCode, e);
				response.setMessageCode("UNAVL_COMB_ID");
				response.setMessageDesc("Combination id unvavailable");
			}
			// updateRequestResponseDetails(response);
			if (itemUnavailabilityStatus != null) {
				List<ItemAvailability> unavailableItems = itemUnavailabilityStatus.getUnavailableInvtemsList();
				List<CustomerOrderResponseItems> responseItemList = response.getCustomerOrderResponseItems();
				for (ItemAvailability item : unavailableItems) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Unavailable item =" + item.getItem());
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					customerOrderResponseItems.setItem(item.getItem());
					customerOrderResponseItems.setLineNo(item.getLineNo().longValue());
					customerOrderResponseItems.setOrderQtySuom(item.getOrderQty());
					customerOrderResponseItems.setFulfillQtySuom(BigDecimal.ZERO);
					customerOrderResponseItems.setAvailableQty(item.getAvailableQty());
					customerOrderResponseItems.setStatus("UNAVL_COMB_ID");
					log.info("omsCustOrdNo " + omsCustOrdNo + "item.getErrorMessage()" + item.getErrorMessage());
					customerOrderResponseItems.setStatusMessage(item.getErrorMessage());
					responseItemList.add(customerOrderResponseItems);
				}
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "setting the status to F in OmsCustOrdHead");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			log.info("omsCustOrdNo " + omsCustOrdNo + "updated to F");
		} else if (serviceStatus.equals("INV_UNAVILABLE")) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "--------------------------------");
			log.info("omsCustOrdNo " + omsCustOrdNo + "Error code " + errorCode);
			response.setOmsCustomerOrderNo(Long.valueOf(omsCustOrdNo.longValue()));
			response.setResponseMessage("FAILED");
			response.setMessageStatus("F");
			try {
				OmsErrorCodes omsError = OMSUtilCommons.getOmsErrorCodesObject(errorCode, input.getCustomerLang());
				response.setMessageCode(omsError.getOmsErrorCode());
				response.setMessageDesc(omsError.getOmsErrLangDesc());
			} catch (Exception e) {
				log.error("No data exist in oms_error_codes for error_code=" + errorCode);
			}
			// updateRequestResponseDetails(response);
			if (itemUnavailabilityStatus != null) {
				List<ItemAvailability> unavailableItems = itemUnavailabilityStatus.getUnavailableInvtemsList();
				List<CustomerOrderResponseItems> responseItemList = response.getCustomerOrderResponseItems();
				for (ItemAvailability item : unavailableItems) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Unavailable item =" + item.getItem());
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					customerOrderResponseItems.setItem(item.getItem());
					customerOrderResponseItems.setLineNo(item.getLineNo().longValue());
					customerOrderResponseItems.setOrderQtySuom(item.getOrderQty());
					customerOrderResponseItems.setFulfillQtySuom(BigDecimal.ZERO);
					customerOrderResponseItems.setAvailableQty(item.getAvailableQty());
					customerOrderResponseItems.setStatus("UNAVAILABLE");
					log.info("omsCustOrdNo " + omsCustOrdNo + "item.getErrorMessage()" + item.getErrorMessage());
					customerOrderResponseItems.setStatusMessage(item.getErrorMessage());
					if (item.getErrorMessage().equals("UNAVL_COMB_ID")) {
						response.setMessageCode("UNAVL_COMB_ID");
						response.setMessageDesc("Combination Id is Unavailable");
					} else {
						response.setMessageCode("UNAVL_INV");
						response.setMessageDesc("Unable to locate sufficient inventory");
					}
					responseItemList.add(customerOrderResponseItems);
				}
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "setting the status to F in OmsCustOrdHead");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			log.info("omsCustOrdNo " + omsCustOrdNo + "updated to F");
			// updateRequestResponseDetails(response);
		} else if (serviceStatus.equals("EXT_SYS_ERROR")) {
			response.setOmsCustomerOrderNo(Long.valueOf(omsCustOrdNo.longValue()));
			if (null != errorCode && !errorCode.equals("SYSTEM_ERROR")) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Paring the error:" + errorCode);
				try {
					OmsErrorCodes omsError = OMSUtilCommons.getOmsErrorCodesObject(errorCode, input.getCustomerLang());
					response.setMessageCode(omsError.getOmsErrorCode());
					response.setMessageDesc(omsError.getOmsErrLangDesc());
				} catch (Exception e) {
					response.setMessageCode(errorCode);
					response.setMessageDesc("Unable to process order.");
				}
				response.setResponseMessage("FAILED");
				response.setMessageStatus("E");
			} else {
				response.setResponseMessage("FAILED");
				response.setMessageStatus("E");
				response.setMessageCode("SYSTEM_ERROR");
				response.setMessageDesc(errorCode);
				// updateRequestResponseDetails(response);
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "setting the status to F in OmsCustOrdHead");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			log.info("omsCustOrdNo " + omsCustOrdNo + "updated to F");
		} else if (serviceStatus.equals("SUCCESS")) {
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("S");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			log.info("checking the delivery type " + omsCustOrdHead.getDeliveryType());
			response.setResponseMessage("SUCCESS");
			response.setMessageStatus("S");
			response.setMessageCode("SUCCESS");
			response.setMessageDesc("Order created successfully.");
			log.info("omsCustOrdNo " + omsCustOrdNo + "------");
			response.setOmsCustomerOrderNo(Long.valueOf(omsCustOrdNo.longValue()));
			SfsConfirmationPojoList = new ArrayList<SfsConfirmationPojo>();
			if (input.getOrderCreateReserveInd().equals("C")) {
				List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
				Map<BigDecimal, ArrayList<OmsCoFulfillDetail>> responseMap = new HashMap<BigDecimal, ArrayList<OmsCoFulfillDetail>>();
				Map<BigDecimal, ArrayList<OmsCoFulfillDetail>> backOrderResponseMap = new HashMap<BigDecimal, ArrayList<OmsCoFulfillDetail>>();
				sfsConfirmationPojomap = new HashMap<BigDecimal, ArrayList<SfsConfirmationPojo>>();
				for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
					if (responseMap.get(omsCoFulfillDetail.getLineNo()) == null || (responseMap.get(omsCoFulfillDetail.getLineNo())).size() == 0) {
						ArrayList<OmsCoFulfillDetail> tempList = new ArrayList<OmsCoFulfillDetail>();
						tempList.add(omsCoFulfillDetail);
						log.info("omsCustOrdNo " + omsCustOrdNo + "Creating map for response" + omsCoFulfillDetail.getLineNo() + omsCoFulfillDetail.getItem());
						responseMap.put(omsCoFulfillDetail.getLineNo(), tempList);
						log.info("omsCustOrdNo " + omsCustOrdNo + "checking TSF number(" + omsCoFulfillDetail.getTsfNo()
								+ ") is null and sourcelocatiotype is ST for the first iteration of omsCoFulfillDetail ");
						log.info("checking source location type " + omsCoFulfillDetail.getSourceLocType());
						log.info("checking order Requestor Id " + input.getOrderRequestorId());
						if (input.getOrderRequestorId() != 19008L && omsCoFulfillDetail.getTsfNo() == null && omsCoFulfillDetail.getSourceLocType().equals("ST")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "condition is true and source location is " + omsCoFulfillDetail.getSourceLocType());
							SfsConfirmationPojo obj = new SfsConfirmationPojo();
							obj.setOmscustOrderNo(omsCoFulfillDetail.getOmsCustOrdNo().toString());
							obj.setStoreNo(omsCoFulfillDetail.getSourceLoc());
							SfsConfirmationPojoList.add(obj);
						}
						continue;
					}
					ArrayList<OmsCoFulfillDetail> existingList = responseMap.get(omsCoFulfillDetail.getLineNo());
					existingList.add(omsCoFulfillDetail);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Creating map for response" + omsCoFulfillDetail.getLineNo() + omsCoFulfillDetail.getItem());
					responseMap.put(omsCoFulfillDetail.getLineNo(), existingList);
					log.info("omsCustOrdNo " + omsCustOrdNo + "checking TSF number is null and sourcelocatiotype is ST for the next iteration of omsCoFulfillDetail ");
					log.info("omsCustOrdNo " + omsCustOrdNo + "checking source location type " + omsCoFulfillDetail.getSourceLocType());
					if (input.getOrderRequestorId() != 19008L && omsCoFulfillDetail.getTsfNo() == null && omsCoFulfillDetail.getSourceLocType().equals("ST")) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "condition is true and source location is " + omsCoFulfillDetail.getSourceLocType());
						SfsConfirmationPojo obj = new SfsConfirmationPojo();
						obj.setOmscustOrderNo(omsCoFulfillDetail.getOmsCustOrdNo().toString());
						obj.setStoreNo(omsCoFulfillDetail.getSourceLoc());
						SfsConfirmationPojoList.add(obj);
					}
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "adding to the list");
				log.info("omsCustOrdNo " + omsCustOrdNo + "getting the cutsomer value" + input.getCustomerOrderNo());
				sfsConfirmationPojomap.put(omsCustOrdNo, SfsConfirmationPojoList);
				log.info("omsCustOrdNo " + omsCustOrdNo + "added to the SFS list");
				List<OmsBackOrderDtl> omsBackOrderDtlList1 = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
				log.info("omsCustOrdNo " + omsCustOrdNo + "omsBackOrderDtlList1" + omsBackOrderDtlList1.size());
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList1) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside ---");
					OmsCoFulfillDetail omsCoFulfillDetailTemp = new OmsCoFulfillDetail();
					omsCoFulfillDetailTemp.setItem(omsBackOrderDtl.getItem());
					omsCoFulfillDetailTemp.setLineNo(omsBackOrderDtl.getLineNo());
					omsCoFulfillDetailTemp.setFulfillConfQty(omsBackOrderDtl.getSourceQty());
					omsCoFulfillDetailTemp.setSourceLoc(omsBackOrderDtl.getSourceLoc());
					omsCoFulfillDetailTemp.setSourceLocType(omsBackOrderDtl.getSourceLocType());
					omsCoFulfillDetailTemp.setFulfillLoc(omsBackOrderDtl.getFulfillLoc());
					omsCoFulfillDetailTemp.setFulfillLocType(omsBackOrderDtl.getFulfillLocType());
					omsCoFulfillDetailTemp.setFulfillReqQty(omsBackOrderDtl.getSourceQty());
					if (backOrderResponseMap.get(omsCoFulfillDetailTemp.getLineNo()) == null || (backOrderResponseMap.get(omsCoFulfillDetailTemp.getLineNo())).size() == 0) {
						ArrayList<OmsCoFulfillDetail> tempList = new ArrayList<OmsCoFulfillDetail>();
						tempList.add(omsCoFulfillDetailTemp);
						log.info("omsCustOrdNo " + omsCustOrdNo + "Creating map for response" + omsCoFulfillDetailTemp.getLineNo() + "Item" + omsCoFulfillDetailTemp.getItem());
						backOrderResponseMap.put(omsCoFulfillDetailTemp.getLineNo(), tempList);
						continue;
					}
					ArrayList<OmsCoFulfillDetail> existingList = backOrderResponseMap.get(omsCoFulfillDetailTemp.getLineNo());
					existingList.add(omsCoFulfillDetailTemp);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Creating map for response" + omsCoFulfillDetailTemp.getLineNo() + "Item" + omsCoFulfillDetailTemp.getItem());
					backOrderResponseMap.put(omsCoFulfillDetailTemp.getLineNo(), existingList);
				}
				sfsConfirmationPojomap.put(omsCustOrdNo, SfsConfirmationPojoList);
				List<CustomerOrderResponseItems> responseItemLists = response.getCustomerOrderResponseItems();
				for (CustomerOrderItems coItem : input.getCustomerOrderItems()) {
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					List<CustomerOrderResponseItemFulfillment> fulfilList = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
				}
				for (BigDecimal key : responseMap.keySet()) {
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					List<CustomerOrderResponseItemFulfillment> fulfilList = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
					log.info("omsCustOrdNo " + omsCustOrdNo + "key=" + key);
					ArrayList<OmsCoFulfillDetail> list = responseMap.get(key);
					ArrayList<OmsCoFulfillDetail> backOrderList = backOrderResponseMap.get(key);
					log.info("omsCustOrdNo " + omsCustOrdNo + "list" + list.size());
					customerOrderResponseItems.setLineNo(key.longValue());
					customerOrderResponseItems.setStatus("AVAILABLE");
					customerOrderResponseItems.setStatusMessage("SUCCESS");
					BigDecimal orderQty = BigDecimal.ZERO;
					BigDecimal fulfilQty = BigDecimal.ZERO;
					for (OmsCoFulfillDetail omsCoFulfillDetail : list) {
						customerOrderResponseItems.setItem(omsCoFulfillDetail.getItem());
						orderQty = omsCoFulfillDetail.getFulfillReqQty().add(orderQty);
						fulfilQty = omsCoFulfillDetail.getFulfillConfQty().add(fulfilQty);
						log.info("omsCustOrdNo " + omsCustOrdNo + "orderQty" + orderQty + "fulfilQty=" + fulfilQty);
						customerOrderResponseItems.setOrderQtySuom(orderQty);
						customerOrderResponseItems.setFulfillQtySuom(fulfilQty);
						CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
						item.setFulfillOrderNo(Long.valueOf(omsCoFulfillDetail.getFulfillOrderNo().longValue()));
						item.setItem(omsCoFulfillDetail.getItem());
						item.setOrderQtySuom(omsCoFulfillDetail.getFulfillReqQty());
						item.setFulfillLoc(Long.valueOf(omsCoFulfillDetail.getFulfillLoc().longValue()));
						item.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
						item.setFulfillQtySuom(omsCoFulfillDetail.getFulfillConfQty());
						item.setSourceLoc(Long.valueOf(omsCoFulfillDetail.getSourceLoc().longValue()));
						item.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
						if (omsCoFulfillDetail.getSourceLocType().equals("WH") && omsCoFulfillDetail.getFulfillLocType().equals("W"))
							item.setTsfNo(Long.valueOf(omsCoFulfillDetail.getTsfNo().longValue()));
						try {
							BigDecimal poNo = ((Ordcust) session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0)).getOrderNo();
							if (poNo != null)
								item.setPoNo(Long.valueOf(poNo.longValue()));
							BigDecimal tsfN0 = ((Ordcust) session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0)).getTsfNo();
							if (tsfN0 != null)
								item.setTsfNo(Long.valueOf(((Ordcust) session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(),
										omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc()).get(0)).getTsfNo().longValue()));
						} catch (Exception e) {
						}
						fulfilList.add(item);
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "adding customerOrderResponseItems");
					responseItemLists.add(customerOrderResponseItems);
				}
				for (BigDecimal key : backOrderResponseMap.keySet()) {
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					List<CustomerOrderResponseItemFulfillment> fulfilList = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
					log.info("omsCustOrdNo " + omsCustOrdNo + "key=" + key);
					ArrayList<OmsCoFulfillDetail> list = backOrderResponseMap.get(key);
					ArrayList<OmsCoFulfillDetail> backOrderList = backOrderResponseMap.get(key);
					log.info("omsCustOrdNo " + omsCustOrdNo + "list" + list.size());
					customerOrderResponseItems.setLineNo(key.longValue());
					customerOrderResponseItems.setStatus("AVAILABLE");
					customerOrderResponseItems.setStatusMessage("SUCCESS");
					BigDecimal orderQty = BigDecimal.ZERO;
					BigDecimal fulfilQty = BigDecimal.ZERO;
					for (OmsCoFulfillDetail omsCoFulfillDetail : backOrderList) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "inside backorde" + "r");
						customerOrderResponseItems.setItem(omsCoFulfillDetail.getItem());
						orderQty = omsCoFulfillDetail.getFulfillReqQty().add(orderQty);
						fulfilQty = omsCoFulfillDetail.getFulfillConfQty().add(fulfilQty);
						log.info("omsCustOrdNo " + omsCustOrdNo + "orderQty" + orderQty + "fulfilQty=" + fulfilQty);
						customerOrderResponseItems.setOrderQtySuom(orderQty);
						customerOrderResponseItems.setFulfillQtySuom(fulfilQty);
						CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
						item.setItem(omsCoFulfillDetail.getItem());
						item.setOrderQtySuom(omsCoFulfillDetail.getFulfillReqQty());
						item.setFulfillLoc(Long.valueOf(omsCoFulfillDetail.getFulfillLoc().longValue()));
						item.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
						item.setFulfillQtySuom(omsCoFulfillDetail.getFulfillConfQty());
						item.setSourceLoc(Long.valueOf(omsCoFulfillDetail.getSourceLoc().longValue()));
						item.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
						try {
							BigDecimal poNo = ((Ordcust) session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0)).getOrderNo();
							if (poNo != null)
								item.setPoNo(Long.valueOf(poNo.longValue()));
							BigDecimal tsfN0 = ((Ordcust) session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0)).getTsfNo();
							if (tsfN0 != null)
								item.setTsfNo(Long.valueOf(((Ordcust) session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(),
										omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc()).get(0)).getTsfNo().longValue()));
						} catch (Exception e) {
						}
						fulfilList.add(item);
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "adding customerOrderResponseItems");
					responseItemLists.add(customerOrderResponseItems);
				}
			} else if (input.getOrderCreateReserveInd().equals("R")) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside reserve");
				List<CustomerOrderResponseItems> responseItemList = response.getCustomerOrderResponseItems();
				for (CustomerOrderItems coItem : input.getCustomerOrderItems()) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "generating response");
					CustomerOrderResponseItems customerOrderResponseItems = new CustomerOrderResponseItems();
					customerOrderResponseItems.setItem(coItem.getItem());
					customerOrderResponseItems.setLineNo(coItem.getLineNo());
					customerOrderResponseItems.setOrderQtySuom(coItem.getOrderQtySuom());
					List<CustomerOrderResponseItemFulfillment> fulfillmentlist = customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
					List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, coItem.getItem(), new BigDecimal(coItem.getLineNo()));
					List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrdNo, coItem.getItem(), new BigDecimal(coItem.getLineNo()));
					if (input.getOrderCreateReserveInd().equals("R")) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "inside reserve response");
						for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList) {
							CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
							item.setItem(coItem.getItem());
							item.setOrderQtySuom(coItem.getOrderQtySuom());
							BigDecimal loc = omsCustOrdReserve.getRmsResvLoc();
							if (loc != null || loc.longValue() >= 0L)
								item.setRMSResvLoc(Long.valueOf(loc.longValue()));
							String locType = omsCustOrdReserve.getRmsResvLocType();
							if (locType != null || !locType.isEmpty())
								item.setRMSResvLocType(locType);
							BigDecimal resvQty = omsCustOrdReserve.getRmsResvQty();
							if (resvQty != null || resvQty.longValue() >= 0L)
								item.setRMSResvQty(Long.valueOf(resvQty.longValue()));
							fulfillmentlist.add(item);
						}
						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
							CustomerOrderResponseItemFulfillment item = new CustomerOrderResponseItemFulfillment();
							item.setItem(coItem.getItem());
							item.setOrderQtySuom(coItem.getOrderQtySuom());
							item.setSourceLoc(Long.valueOf(omsBackOrderDtl.getSourceLoc().longValue()));
							item.setSourceLocType(omsBackOrderDtl.getSourceLocType());
							item.setFulfillLoc(Long.valueOf(omsBackOrderDtl.getFulfillLoc().longValue()));
							item.setFulfillLocType(omsBackOrderDtl.getFulfillLocType());
							item.setFulfillQtySuom(omsBackOrderDtl.getSourceQty());
							BigDecimal resvQty = omsBackOrderDtl.getSourceQty();
							if (resvQty != null || resvQty.longValue() >= 0L)
								item.setRMSResvQty(Long.valueOf(resvQty.longValue()));
							fulfillmentlist.add(item);
						}
					}
					response.setResponseMessage("SUCCESSFUL");
					responseItemList.add(customerOrderResponseItems);
				}
			}
		}
		if (!response.getMessageStatus().equals("S") && omsCustOrdNo != null)
			rollbackOfBackOrder(omsCustOrdNo);
		OmsCustOrdHead omscustOrdHead = null;
		log.info("Checking record exists or not into oms_cust_ord_head for any duplicate order id");
		try {
			omscustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		} catch (Exception e) {
			log.info("Record  doesn't exists or not into oms_cust_ord_head for any ");
		}
		if (null != omscustOrdHead) {
			if ("C".equals(omscustOrdHead.getDeliveryType()) && "S".equals(omscustOrdHead.getOrdPaymentStatus()) && "C".equals(omscustOrdHead.getOrderCreateReserveInd())
					&& "S".equals(omscustOrdHead.getStatus()))
				insertintocfssmsemailinfotable(omscustOrdHead.getPickLoc().intValue(), omscustOrdHead.getCustOrderNo());
			if ("S".equals(omscustOrdHead.getDeliveryType()) && "S".equals(omscustOrdHead.getOrdPaymentStatus()) && "C".equals(omscustOrdHead.getOrderCreateReserveInd())
					&& "S".equals(omscustOrdHead.getStatus())) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside SFS confirmation method");
				if (!sfsConfirmationPojomap.isEmpty() && sfsConfirmationPojomap != null)
					try {
						log.info("omsCustOrdNo " + omsCustOrdNo + "checking SFS list is null or not");
						ArrayList<SfsConfirmationPojo> list = sfsConfirmationPojomap.get(omscustOrdHead.getOmsCustOrdNo());
						if (list.size() > 0)
							for (SfsConfirmationPojo pojo : list) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "inside SFS list");
								log.info("omsCustOrdNo " + omsCustOrdNo + "inside SFS list and store number is " + pojo.getStoreNo());
								insertintocfssmsemailinfotable(pojo.getStoreNo().intValue(), omscustOrdHead.getCustOrderNo());
							}
					} catch (Exception e) {
						log.info("Exception in SFS menthod" + e.getMessage());
					}
			}
		}
		log.info("Before returning  the response");
		return response;
	}

	public void rollbackOfBackOrder(BigDecimal omsCustOrdNo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		if ("F".equals(omsCustOrdHead.getStatus())) {
			List<OmsBackOrderDtl> omsBackOrderDtls = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
			for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtls) {
				BigDecimal channelId = null;
				if (omsBackOrderDtl.getSourceQty().intValue() != 0 || "N".equals(omsBackOrderDtl.getBackorderStatus())) {
					omsBackOrderDtl.setSourceQty(new BigDecimal(0));
					omsBackOrderDtl.setBackorderStatus("S");
					session.mergeOmsBackOrderDtl(omsBackOrderDtl);
					BigDecimal physicalWH = BigDecimal.ZERO;
					if (omsBackOrderDtl.getSourceLocType().toString().equalsIgnoreCase("WH")) {
						List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
						for (Object[] result : tempWhObject) {
							physicalWH = new BigDecimal(result[0].toString());
							channelId = new BigDecimal(result[1].toString());
							log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
						}
						oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsBackOrderDtl.getSourceQty().negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
								channelId);
						continue;
					}
					channelId = BigDecimal.ZERO;
					oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsBackOrderDtl.getSourceQty().negate(), omsBackOrderDtl.getSourceLoc().longValue(),
							omsBackOrderDtl.getSourceLocType(), "EA", channelId);
				}
			}
		}
	}

	private void insertintocfssmsemailinfotable(int storeNo, String customerOrderNo) throws SOAPException {
		log.info("insertintocfssmsemailinfotable methods Begin  store no is " + storeNo + "Customer Order No is " + customerOrderNo);
		ResponseProcessing responseProcessing = new ResponseProcessing();
		CfsSmsEmailStatusInfo cfsSmsEmailStatusInfoObj = new CfsSmsEmailStatusInfo();
		log.info(customerOrderNo + " 1.setting the store no" + storeNo);
		String newStore = storeNo + "";
		cfsSmsEmailStatusInfoObj.setStoreNo(new BigDecimal(newStore));
		log.info(customerOrderNo + "2.setting the custoemr order no " + customerOrderNo);
		cfsSmsEmailStatusInfoObj.setCustOrderNo(customerOrderNo);
		log.info(customerOrderNo + "setting the timestamp");
		cfsSmsEmailStatusInfoObj.setCreateDatetime(new Timestamp((new Date()).getTime()));
		cfsSmsEmailStatusInfoObj.setSmsStatusCode("0");
		cfsSmsEmailStatusInfoObj.setEmailStatusCode("0");
		cfsSmsEmailStatusInfoObj.setOrderPickStatus("0");
		cfsSmsEmailStatusInfoObj.setEmailProcessInd("N");
		cfsSmsEmailStatusInfoObj.setSmsProcessInd("N");
		log.info(customerOrderNo + "getting ID value" + cfsSmsEmailStatusInfoObj.getId());
		log.info(customerOrderNo + "calling persists  methods to save the record into DataBase" + cfsSmsEmailStatusInfoObj.hashCode());
		try {
			responseProcessing.insertCfsSmsEmailInfo(cfsSmsEmailStatusInfoObj);
		} catch (Exception e) {
			log.info(" Failed to inserting into cfsSmsEmailStatusInfo table : " + e.getMessage());
		}
		log.info("insert into cfssmsemailinfotable methods Ends");
	}

	private void insertCfsSmsEmailInfo(CfsSmsEmailStatusInfo cfsBean) throws Exception {
		log.info(cfsBean.getCustOrderNo() + " ***insert into CFS_SMS_EMAIL_STATUS_INFO ***");
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "insert into CFS_SMS_EMAIL_STATUS_INFO(ID, CUST_ORDER_NO, STORE_NO, ORDER_PICK_STATUS, CREATE_DATETIME, LAST_UPDATE_DATETIME, EMAIL_STATUS_CODE, SMS_STATUS_CODE, EMAIL_PROCESS_IND, SMS_PROCESS_IND) values(CFS_SMS_EMAIL_STATUS_INFO_SEQ.nextval,?,?,?,systimestamp,systimestamp,?,?,?,?)";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			statement = conn.prepareStatement(query);
			statement.setString(1, cfsBean.getCustOrderNo());
			statement.setBigDecimal(2, cfsBean.getStoreNo());
			statement.setString(3, cfsBean.getOrderPickStatus());
			statement.setString(4, cfsBean.getEmailStatusCode());
			statement.setString(5, cfsBean.getSmsStatusCode());
			statement.setString(6, cfsBean.getEmailProcessInd());
			statement.setString(7, cfsBean.getSmsProcessInd());
			statement.executeUpdate();
			log.info(cfsBean.getCustOrderNo() + "Successfully inserted into CFS_SMS_EMAIL_STATUS_INFO..");
		} catch (Exception e) {
			log.info(cfsBean.getCustOrderNo() + " Failed to inserting into cfsSmsEmailStatusInfo table : " + e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, statement, rs);
			} catch (Exception e) {
				log.info(cfsBean.getCustOrderNo() + "Exception while closing the finally block while inserting in CFS_SMS_EMAIL_STATUS_INFO " + e.getMessage());
			}
		}
	}

//Persist Failed orders in header and item level table
	/*
	 * private void updateRequestResponseDetails(CustomerOrderResponse
	 * customerOrderResponse) {
	 * log.info("FAILED ORDERS Calling updateRequestResponseDetails method");
	 * Connection connection = null; PreparedStatement prepStatement = null;
	 * PreparedStatement prepStatementItems = null; ResultSet rs = null; int
	 * candidateId = 0; Long l = new Long(10); int i = l.intValue(); String check =
	 * "RequestResponse"; byte b[] = check.getBytes(); GregorianCalendar
	 * gregorianCalendar = new GregorianCalendar(); DatatypeFactory datatypeFactory
	 * = null; try { datatypeFactory = DatatypeFactory.newInstance(); } catch
	 * (DatatypeConfigurationException e) { log.warn(e.toString()); // throw new //
	 * SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString())); }
	 * XMLGregorianCalendar now =
	 * datatypeFactory.newXMLGregorianCalendar(gregorianCalendar); try {
	 * log.info("connecting to OMS Schema"); log.info("OMSConstants.DS_OMS_STRING "
	 * + OMSConstants.DS_OMS_STRING); connection =
	 * OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
	 * log.info("connection of Connection : " + connection); String insert =
	 * "INSERT INTO oms_order_create_response_head(cust_order_no,oms_cust_ord_no,order_message_status,order_message_code,order_message_desc) VALUES (?, ?, ?, ?, ?)"
	 * ; prepStatement = connection.prepareStatement(insert);
	 * prepStatement.setString(1, customerOrderResponse.getCustomerOrderNo());
	 * prepStatement.setLong(2, customerOrderResponse.getOmsCustomerOrderNo());
	 * prepStatement.setString(3, customerOrderResponse.getMessageStatus());
	 * prepStatement.setString(4, customerOrderResponse.getMessageCode());
	 * prepStatement.setString(5, customerOrderResponse.getMessageDesc()); if
	 * (customerOrderResponse.getCustomerOrderResponseItems() != null) { for
	 * (CustomerOrderResponseItems items :
	 * customerOrderResponse.getCustomerOrderResponseItems()) { String insertItems =
	 * "INSERT INTO oms_order_create_response_item(oms_cust_ord_no,line_no,item,item_status,item_status_message) VALUES (?, ?, ?, ?, ?)"
	 * ; prepStatementItems = connection.prepareStatement(insertItems);
	 * prepStatementItems.setLong(1, customerOrderResponse.getOmsCustomerOrderNo());
	 * prepStatementItems.setLong(2, items.getLineNo());
	 * prepStatementItems.setString(3, items.getItem());
	 * prepStatementItems.setString(4, items.getStatus());
	 * prepStatementItems.setString(5, items.getStatusMessage()); } } int
	 * rowAffected = prepStatement.executeUpdate(); int rowAffectedItems =
	 * prepStatementItems.executeUpdate(); if (rowAffected == 1) { // get candidate
	 * id rs = prepStatement.getGeneratedKeys(); if (rs.next()) candidateId =
	 * rs.getInt(1); } if (rowAffectedItems == 1) { // get candidate id rs =
	 * prepStatementItems.getGeneratedKeys(); if (rs.next()) candidateId =
	 * rs.getInt(1); } log.info("FAILED ORDERS Insert Query " +
	 * customerOrderResponse.getMessageStatus() + "," +
	 * customerOrderResponse.getMessageCode() + "," +
	 * customerOrderResponse.getMessageDesc()); } catch (Exception e) {
	 * log.info("Exception e " + e.getMessage()); } finally { try {
	 * prepStatement.close(); prepStatementItems.close(); rs.close();
	 * connection.close(); } catch (Exception e) { log.info(e.getMessage()); } } }
	 */// End of method
}
