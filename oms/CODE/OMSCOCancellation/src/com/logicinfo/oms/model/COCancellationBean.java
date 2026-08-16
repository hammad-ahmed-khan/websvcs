
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OMSUtilJdbc;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.beans.OpenDeliveryBean;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoCancelHead;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFoCancel;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRevsPick;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.ejb.Tsfdetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.logicinfo.oms.util.OmsStatusUpdateForReturnPickupCancellation;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordItm;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfItm;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;

public class COCancellationBean {
	public COCancellationBean() {
		super();
	}

	static BigDecimal su_qty = BigDecimal.valueOf(0);
	long L_cum_cancel_qty;
	long L_CO_ITEM_CANCEL_QTY;
	long L_fo_open_qty;
	long fulilmentOrdNo;
	BigDecimal omsCancelId;
	BigDecimal omsCustOrderNo;
	Properties props;
	TreeMap<String, BigDecimal> backorderTreeMap = new TreeMap<String, BigDecimal>();
	Map<BigDecimal, BigDecimal> nonInventoryItemMap = new HashMap<BigDecimal, BigDecimal>();
	int retryForRMSCancelFulfilOrdColRef = 0;
	int retryCreateReversePick = 0;
	int retryConfirmReversePick = 0;
	int retryCancelFulfillmentOrderDetail1 = 0;
	int retryCancelFulfillmentOrderDetail2 = 0;
	int retryCancelFulfillmentOrderDetail3 = 0;
	int countforCusordLogPersistance = 0;
	Boolean processcancellationcall = false;

	String extCustOrdNo = "";
	String shipingChargeDept;
	CustomerOrderCancellationResponse response;
	List<CustomerOrderCancellationItems> shippingChargeItemList = null;
	private final static Logger log = Logger.getLogger(com.logicinfo.oms.model.OMSCOCancellationWebServiceImpl.class.getName());
	List<ErrorListResponse> list = new ArrayList<ErrorListResponse>();

	public void validateCancelReqId(CustomerOrderCancellation input) throws SOAPException {
		log.info("Inside validateCancelReqId ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<OmsCoCancelHead> omsCoCancelHead = null;
		log.info("Checking whether cancellation id is already exist");
		omsCoCancelHead = session.getOmsCoCancelHeadFindByCustOrdNo(input.getCustOrderNo());
		if (omsCoCancelHead != null && omsCoCancelHead.size() > 0) {
			if (omsCoCancelHead.get(0).getCancelReqId().toString().equals(input.getCancellationId())) {
				log.info("Id already exist");
				// throw new
				// SOAPException(OMSUtilCommons.formErrorDescription("CANCEL_ID_EXIST", "1",
				String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
				Boolean OmsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.CancelledIdExist, languageCode);
				if (Boolean.FALSE == OmsErrorCodesRecordExist) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.CancelledIdExist, languageCode, new String[] { input.getCancellationId() }));
			}
		}
	}

	public List<ErrorListResponse> validateQty(CustomerOrderCancellation input) throws SOAPException {
		log.info("inside validateQty");

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrderNo = null;
		ArrayList<String> errorCode = new ArrayList<String>();
		if (input.getSubCustOrderNo() != null) {
			omsCustOrderNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(input.getCustOrderNo(), input.getSubCustOrderNo());
		} else {
			omsCustOrderNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(input.getCustOrderNo(), "1");
		}
		log.info("omsCustOrderNum " + omsCustOrderNo);

		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
		log.info("Oms cust orde item list size=" + omsCustOrdItemList.size());
		List<CustomerOrderCancellationItems> cancellList = input.getCancellationItems();
		log.info("cancellList " + cancellList.size());
		List<String> cancelledList = new ArrayList<String>();
		List<String> deliveredList = new ArrayList<String>();
		List<String> qtyUnvailableList = new ArrayList<String>();
		List<String> deliveredandCancelledList = new ArrayList<String>();
		BigDecimal pendingQty = null;
		// String errorCode=null;
		String desc = null;
		for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
			pendingQty = omsCustOrdItem.getQtyOrderedSuom().subtract((omsCustOrdItem.getCumQtyDelivered().add(omsCustOrdItem.getQtyCancelled())));
			log.info("Pending Quantity to cancel " + pendingQty);
			ErrorListResponse errorListResponse = new ErrorListResponse();
			for (CustomerOrderCancellationItems customerOrderCancellation : cancellList) {

				if (customerOrderCancellation.getItem().equals(omsCustOrdItem.getItem()) && customerOrderCancellation.getLineNo() == omsCustOrdItem.getLineNo().longValue()
						&& omsCustOrdItem.getQtyOrderedSuom().intValue() == omsCustOrdItem.getCumQtyDelivered().intValue()) {
					log.info("============deliveredList================");
					// session.getOmsErrorCodesFindByonlyErrorCode("ITEM_ALRDY_DLVD"); code removed
					// to support error message
					// in multi language
					String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
					OmsErrorCodes omsErrorCodesObject = OMSUtilCommons.getOmsErrorCodesObject(OmsErrorCodesConstant.itemAlredayDelieverd, languageCode);
					desc = omsErrorCodesObject.getOmsErrLangDesc();
					deliveredList.add(omsCustOrdItem.getItem());
					errorCode.add(OmsErrorCodesConstant.itemAlredayDelieverd);
					errorListResponse.setLineNo(customerOrderCancellation.getLineNo());
					errorListResponse.setItem(customerOrderCancellation.getItem());
					errorListResponse.setCancelQtySuom(customerOrderCancellation.getCancelQtySuom());
					errorListResponse.setMessageCode(OmsErrorCodesConstant.itemAlredayDelieverd);
					errorListResponse.setMessageDesc(desc);
					list.add(errorListResponse);

				} else if (customerOrderCancellation.getItem().equals(omsCustOrdItem.getItem()) && customerOrderCancellation.getLineNo() == omsCustOrdItem.getLineNo().longValue()
						&& omsCustOrdItem.getQtyOrderedSuom().intValue() == omsCustOrdItem.getQtyCancelled().intValue()) {
					log.info("============cancelledList================");

					cancelledList.add(omsCustOrdItem.getItem());
					errorCode.add(OmsErrorCodesConstant.itemAlreadyCancelled);
					String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
					OmsErrorCodes omsErrorCodesObject = OMSUtilCommons.getOmsErrorCodesObject(OmsErrorCodesConstant.itemAlreadyCancelled, languageCode);
					// session.getOmsErrorCodesFindByonlyErrorCode("ITEM_ALRDY_CLD"); removed for
					// 3209 Bug
					desc = omsErrorCodesObject.getOmsErrLangDesc();
					errorListResponse.setLineNo(customerOrderCancellation.getLineNo());
					errorListResponse.setItem(customerOrderCancellation.getItem());
					errorListResponse.setCancelQtySuom(customerOrderCancellation.getCancelQtySuom());
					errorListResponse.setMessageCode(OmsErrorCodesConstant.itemAlreadyCancelled);
					errorListResponse.setMessageDesc(desc);
					list.add(errorListResponse);
				}

				else if (customerOrderCancellation.getItem().equals(omsCustOrdItem.getItem()) && customerOrderCancellation.getLineNo() == omsCustOrdItem.getLineNo().longValue()
						&& omsCustOrdItem.getQtyOrderedSuom().intValue() == (omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue())) {
					log.info("============deliveredandCancelledList================");
					deliveredandCancelledList.add((omsCustOrdItem.getItem()));
					errorCode.add(OmsErrorCodesConstant.requestedQtyOfanItemForCancellationNotAvailable);
					String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
					OmsErrorCodes omsErrorCodesObject = OMSUtilCommons.getOmsErrorCodesObject(OmsErrorCodesConstant.requestedQtyOfanItemForCancellationNotAvailable, languageCode);
					// session.getOmsErrorCodesFindByonlyErrorCode("ITEM_ALRDY_DLVD_CLD"); removed
					// to fix 3209 Bug..
					desc = omsErrorCodesObject.getOmsErrLangDesc();
					deliveredList.add(omsCustOrdItem.getItem());
					errorListResponse.setLineNo(customerOrderCancellation.getLineNo());
					errorListResponse.setItem(customerOrderCancellation.getItem());
					errorListResponse.setCancelQtySuom(customerOrderCancellation.getCancelQtySuom());
					errorListResponse.setMessageCode(OmsErrorCodesConstant.requestedQtyOfanItemForCancellationNotAvailable);
					errorListResponse.setMessageDesc(desc);
					list.add(errorListResponse);
				} else if (customerOrderCancellation.getItem().equals(omsCustOrdItem.getItem()) && customerOrderCancellation.getLineNo() == omsCustOrdItem.getLineNo().longValue()
						&& customerOrderCancellation.getCancelQtySuom().intValue() > pendingQty.intValue()) {
					log.info("============qtyUnvailableList================");
					qtyUnvailableList.add(omsCustOrdItem.getItem());
					errorCode.add(OmsErrorCodesConstant.itemAvailableForCancellation);
					// session.getOmsErrorCodesFindByonlyErrorCode("ITEM_QTY_UNAVB"); removed to fix
					// 3209 Bug
					String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
					OmsErrorCodes omsErrorCodesObject = OMSUtilCommons.getOmsErrorCodesObject(OmsErrorCodesConstant.itemAvailableForCancellation, languageCode);
					desc = omsErrorCodesObject.getOmsErrLangDesc();
					deliveredList.add(omsCustOrdItem.getItem());
					errorListResponse.setLineNo(customerOrderCancellation.getLineNo());
					errorListResponse.setItem(customerOrderCancellation.getItem());
					errorListResponse.setCancelQtySuom(customerOrderCancellation.getCancelQtySuom());
					errorListResponse.setMessageCode(OmsErrorCodesConstant.itemAvailableForCancellation);
					errorListResponse.setMessageDesc(pendingQty + " " + desc);
					list.add(errorListResponse);
				}

			}
		}

		return list;
	}

	public void validateInput(CustomerOrderCancellation input) throws SOAPException {
		log.info("Inside validateInput method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside validate");
		getOmsCustOrdNo(input);
		log.info("Requested Cancellation Id " + input.getCancellationId());
		OmsCustOrdHead omsCustOrdHead = null;

		try {
			omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);
		} catch (Exception e) {
			log.error("Unable to find record in getOmsCustOrdHeadFindByOmsCustOrdNo");
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("No record found in oms_cust_ord_Head "));
		}
		if (omsCustOrdHead.getCustOrderType().equals("B2B")) {
			if (input.getSubCustOrderNo() == null) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Sub_customer_ord_no is required in case of B2B orders"));
			}
		}
		if (omsCustOrdHead.getSubCustOrderNo().equals("1")) {
			extCustOrdNo = omsCustOrdHead.getCustOrderNo();
		} else {
			extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
			if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
				if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
					extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "-0" + omsCustOrdHead.getSubCustOrderNo().trim();
				} else {
					extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "-00" + omsCustOrdHead.getSubCustOrderNo().trim();
				}
			}
		}
		log.info("omsCustOrderNo " + omsCustOrderNo);
		// List<OmsCustOrdItem> omsCustOrdItemList =
		// session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
		// log.info("omsCustOrdItemList size "+omsCustOrdItemList.size());
		// List<CustomerOrderCancellationItems>
		// cancellList=input.getCancellationItems();
		// List<String> errorItemList=new ArrayList<String>();
		// List<String> arraylist = new ArrayList();
		// int deliveredItems=0;
		// int inputitemsSize=cancellList.size();
		// log.info("omsCustOrdItemList.size "+omsCustOrdItemList.size());
		// log.info("inputitemsSize "+inputitemsSize);
		//
		// for(OmsCustOrdItem omsCustOrdItem:omsCustOrdItemList )
		// {
		// log.info("Inside omsCustOrdItem Loop");
		// for(CustomerOrderCancellationItems customerOrderCancellation:cancellList)
		// {
		// log.info("Inside CustomerOrderCancellationItems Loop");
		// if(customerOrderCancellation.getItem().equals(omsCustOrdItem.getItem()) &&
		// customerOrderCancellation.getLineNo()==omsCustOrdItem.getLineNo().longValue())
		// {
		// log.info("item and line no are equal");
		// log.info("Ordered Qty "+omsCustOrdItem.getQtyOrderedSuom().intValue());
		// log.info("intial Delievered Qty
		// "+omsCustOrdItem.getCumQtyDelivered().intValue());
		// log.info("intial Cancelled Qty
		// "+omsCustOrdItem.getQtyCancelled().intValue());
		// log.info("===============Requested Qty=============================");
		// log.info("Requested Qty
		// "+customerOrderCancellation.getCancelQtySuom().intValue());
		// log.info("============================delivered and cancelled
		// qty===================================");
		// BigDecimal
		// deliveredQqty=omsCustOrdItem.getQtyOrderedSuom().subtract(omsCustOrdItem.getCumQtyDelivered());
		// //log.info("deliveredQqty "+deliveredQqty);
		// BigDecimal
		// cancelledQty=omsCustOrdItem.getQtyOrderedSuom().subtract(omsCustOrdItem.getQtyCancelled());
		// //log.info("cancelledQty "+cancelledQty);
		// BigDecimal delivQtyCancelQty=deliveredQqty.add(cancelledQty);
		// log.info("Delivered and cancelled Qty "+delivQtyCancelQty);

		// if(customerOrderCancellation.getCancelQtySuom().intValue()>omsCustOrdItem.getQtyOrderedSuom().intValue())
		// {
		// log.info("Requested quantity is greater than the order quantity");
		// throw new SOAPException(OMSUtilCommons.formErrorDescription("ITEM_QTY_GTR",
		// "1", new String[]
		// {omsCustOrdItem.getItem(),omsCustOrdItem.getQtyOrderedSuom().toString(),customerOrderCancellation.getCancelQtySuom().toString()}));
		//
		// }
		//

		log.info("Completed validateInput method");
	}

	public void getOmsCustOrdNo(CustomerOrderCancellation input) throws SOAPException {
		log.info("inside getOmsCustOrdNo");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			if (input.getSubCustOrderNo() == null) {
				omsCustOrderNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(input.getCustOrderNo(), "1");
			} else {
				omsCustOrderNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(input.getCustOrderNo(), input.getSubCustOrderNo());
			}

		} catch (Exception e) {
			String errString = OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_NOT_EXISTS, "1", new String[] { "oms_cust_ord_head" });
			log.error(errString);
			throw new SOAPException(errString);
		}
		log.info("omsCustOrderNo" + omsCustOrderNo);
	}

	public void checkItemExistence(CustomerOrderCancellation input) throws SOAPException {
		log.info("inside checkItemExistence");

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		response = new CustomerOrderCancellationResponse();
		for (CustomerOrderCancellationItems item : input.getCancellationItems()) {
			try {
				// changed the code for lineNo
				session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
			} catch (Exception e) {
				log.error("Invalid item, valid record not found in oms_cust_ord_item for the given item :" + item.getItem() + "and OmsCustOrdNumber :" + omsCustOrderNo + ",rejecting the whole request"
						+ item.getLineNo() + "--Line no");
				/*
				 * throw new SOAPFaultException(OMSUtil.getInstance().
				 * newSoapFault("Invalid item, valid record not found in oms_cust_ord_item for the given item :"
				 * + item.getItem() + "and OmsCustOrdNumber :" + omsCustOrderNo +
				 * ",rejecting the whole request"));
				 */

				String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrderNo);
				Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cancel_Invalid_Item, languageCode);
				if (Boolean.FALSE == omsErrorRecordExist) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cancel_Invalid_Item, languageCode, new String[] { item.getItem(), omsCustOrderNo.toString() });
				log.error(errString);
				throw new SOAPException(errString);
			}
		}
		log.info("Completed checkItemExistence method");
	}

	public void saveCOCancellationDetails(CustomerOrderCancellation input) throws SOAPException {
		log.info("Inside saveCoCancellationDetails method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> omsCancelIdList = null;
		try {
			if (input.getSubCustOrderNo() == null) {
				omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), "1", new BigDecimal(input.getCancellationId()));

			} else {

				omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), input.getSubCustOrderNo(), new BigDecimal(input.getCancellationId()));
			}

		} catch (Exception e) {
			String errString = OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_NOT_EXISTS, "1", new String[] { "combination of application id,cust_ord_number and cancellation_id" });
			log.error(errString);
			throw new SOAPException(errString);
		}
		if (omsCancelIdList.size() > 0) {
			log.info("Failure ..Duplicate entries found,combination of application id,oms_cust_ord_number and cancellation_id already exist in OMS_CO_CANCEL_HEAD");
			String errString = OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_EXISTS, "1", new String[] { "combination of application id,oms_cust_ord_number and cancellation_id" });
			log.error(errString);
			throw new SOAPException(errString);
		} else {
			OmsPersistence omsPersistence = new OmsPersistence();
			omsPersistence.persistOmsCoCancelHead(input, omsCustOrderNo);
		}
		log.info("Completed saveCoCancellationDetails method");
	}

	public List<ErrorListResponse> checkLinkedItem(CustomerOrderCancellation input) throws SOAPException {
		log.info("-->inside checkLinkedItem");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Map<BigDecimal, BigDecimal> inputmap = new HashMap<BigDecimal, BigDecimal>();
		Map<BigDecimal, String> inputItem = new HashMap<BigDecimal, String>();
		Map<BigDecimal, String> itemTab = new HashMap<BigDecimal, String>();
		String desc = null;

		List<OmsCustOrdItem> omsCustOrdItemList = null;
		ArrayList<String> errorCode = new ArrayList<String>();
		String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustOrderNo());
		log.info("<-----------Returned language Code " + languageCode + "<------->");
		OmsErrorCodes omsErrorCodesObject = OMSUtilCommons.getOmsErrorCodesObject(OmsErrorCodesConstant.linkedItem, languageCode);
		// session.getOmsErrorCodesFindByonlyErrorCode("LINKED_ITEM"); code removed to
		// fix 3209 Bug
		desc = omsErrorCodesObject.getOmsErrLangDesc();
		log.info("<----Description error message" + desc);
		OMSUtilCommons omsutilCommons = new OMSUtilCommons();
		OmsCustOrdItem omsCustOrdItem = null;
		for (CustomerOrderCancellationItems item : input.getCancellationItems()) {
			inputmap.put(new BigDecimal(item.getLineNo()), item.getCancelQtySuom());
		}

		for (CustomerOrderCancellationItems item : input.getCancellationItems()) {
			inputItem.put(new BigDecimal(item.getLineNo()), item.getItem());
			log.info("omscustordno: " + omsCustOrderNo);
			log.info("item: " + item.getItem());
			log.info("line no: " + new BigDecimal(item.getLineNo()));
			omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));

			if (omsCustOrdItem.getLineLinkNo() != null) {
				BigDecimal availabeQtytoCancel = omsCustOrdItem.getQtyOrderedSuom().subtract(omsCustOrdItem.getCumQtyDelivered().add(omsCustOrdItem.getQtyCancelled()));
				log.info("availabeQtytoCancel " + availabeQtytoCancel);
				BigDecimal retail_amt = omsutilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrderNo, omsCustOrdItem.getLineNo(), omsCustOrdItem.getUnitRetail());
				log.info("retail_amt " + retail_amt);

				if (item.getCancelQtySuom().intValue() != availabeQtytoCancel.intValue()) {
					log.info("removing lineNo " + item.getLineNo());
					if (retail_amt.intValue() > 0) {
						inputItem.remove(new BigDecimal(item.getLineNo()));
						log.info("<--------------- Child ItemTab ------------- " + itemTab.keySet() + "---------------");
						log.info("<-------------child input map----------" + inputItem.keySet() + "------------------------>");
					}

				}
			}
			try {
				log.info("<----------------Inside try block--------------------->");
				omsCustOrdItemList = session.getOmsCustOrdItemFindByLinkLineNo(omsCustOrderNo, omsCustOrdItem.getLineNo());

				if (omsCustOrdItemList.size() > 0) {
					for (OmsCustOrdItem ordItem : omsCustOrdItemList) {
						BigDecimal retail_amt = omsutilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrderNo, omsCustOrdItem.getLineNo(), omsCustOrdItem.getUnitRetail());
						log.info("retail_amt " + retail_amt);
						ErrorListResponse errorListResponse = new ErrorListResponse();
						log.info("ordItem.getQtyOrderedSuom() " + ordItem.getQtyOrderedSuom());
						log.info("ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled() " + (ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled())));
						if (ordItem.getQtyOrderedSuom().intValue() != (ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled()).intValue())) {
							log.info("==============================11111111111111111111111111111111111====================");
							log.info("===========item.getLineNo()=============== " + item.getLineNo());
							log.info("==========ordItem.getLineNo()================ " + ordItem.getLineNo());
							itemTab.put(ordItem.getLineNo(), ordItem.getItem()); // b
							itemTab.put(ordItem.getLineLinkNo(), ordItem.getItem()); // a
							log.info("<---------------ItemTab ------------- " + itemTab.keySet() + "---------------");
							errorCode.add(OmsErrorCodesConstant.linkedItem);
							log.info("item.getLineNo() " + item.getLineNo());
							log.info("item.getItem() " + item.getItem());
							log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
							errorListResponse.setLineNo(item.getLineNo());
							errorListResponse.setItem(item.getItem());
							errorListResponse.setCancelQtySuom(item.getCancelQtySuom());
							errorListResponse.setMessageCode(OmsErrorCodesConstant.linkedItem);
							errorListResponse.setMessageDesc(desc);
							list.add(errorListResponse);
						} else {
							log.info("==============================22222222222222222222222222222222====================");

							log.info("==========ordItem.getLineLinkNo()================ " + ordItem.getLineLinkNo());
							itemTab.put(ordItem.getLineLinkNo(), ordItem.getItem());
							log.info("<--------------- ItemTab ------------- " + itemTab.keySet() + "---------------");
							log.info("<-------------input map----------" + inputmap.keySet() + "------------------------>");
							errorCode.add(OmsErrorCodesConstant.linkedItem);
							log.info("item.getLineNo() " + item.getLineNo());
							log.info("item.getItem() " + item.getItem());
							log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
							errorListResponse.setLineNo(item.getLineNo());
							errorListResponse.setItem(item.getItem());
							errorListResponse.setCancelQtySuom(item.getCancelQtySuom());
							errorListResponse.setMessageCode(OmsErrorCodesConstant.linkedItem);
							errorListResponse.setMessageDesc(desc);
							list.add(errorListResponse);
						}
					}
				} else {
					log.info("==============================33333333333333333333333333333333333333333333====================");
					log.info("No records exist for link lineNo " + omsCustOrdItem.getLineLinkNo());
					log.info("==========omsCustOrdItem.getLineLinkNo()================ " + omsCustOrdItem.getLineLinkNo());
					log.info("omsCustOrdItem.getUnitRetail() " + omsCustOrdItem.getUnitRetail());
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					BigDecimal retail_amt = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrderNo, omsCustOrdItem.getLineNo(), omsCustOrdItem.getUnitRetail());
					log.info("retail_amt " + retail_amt);

					if (omsCustOrdItem.getLineLinkNo() != null && inputItem.containsKey(omsCustOrdItem.getLineLinkNo()) && retail_amt.intValue() > 0) {
						ErrorListResponse errorListResponse = new ErrorListResponse();
						itemTab.put(omsCustOrdItem.getLineLinkNo(), omsCustOrdItem.getItem());
						log.info("<---------------ItemTab ------------- " + itemTab.keySet() + "---------------");
						log.info("<-------------input map----------" + inputmap.keySet() + "------------------------>");
						errorCode.add(OmsErrorCodesConstant.linkedItem);
						log.info("item.getLineNo() " + item.getLineNo());
						log.info("item.getItem() " + item.getItem());
						log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
						errorListResponse.setLineNo(item.getLineNo());
						errorListResponse.setItem(item.getItem());
						errorListResponse.setCancelQtySuom(item.getCancelQtySuom());
						errorListResponse.setMessageCode(OmsErrorCodesConstant.linkedItem);
						errorListResponse.setMessageDesc(desc);
						list.add(errorListResponse);
					} else if (retail_amt.intValue() > 0 && !inputItem.containsKey(omsCustOrdItem.getLineLinkNo())) {
						log.info("Unit Retail is greater than Zero should be cancelled with main item");
						ErrorListResponse errorListResponse = new ErrorListResponse();
						if (omsCustOrdItem.getLineLinkNo() != null) {
							itemTab.put(omsCustOrdItem.getLineLinkNo(), omsCustOrdItem.getItem());
							log.info("<--------------- ItemTab ------------- " + itemTab.keySet() + "---------------");
							log.info("<------------- input map----------" + inputmap.keySet() + "------------------------>");
						}
						errorCode.add(OmsErrorCodesConstant.linkedItem);
						log.info("item.getLineNo() " + item.getLineNo());
						log.info("item.getItem() " + item.getItem());
						log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
						errorListResponse.setLineNo(item.getLineNo());
						errorListResponse.setItem(item.getItem());
						errorListResponse.setCancelQtySuom(item.getCancelQtySuom());
						errorListResponse.setMessageCode(OmsErrorCodesConstant.linkedItem);
						errorListResponse.setMessageDesc(desc);
						list.add(errorListResponse);
					} else if (retail_amt.intValue() == 0 && inputmap.keySet().contains(omsCustOrdItem.getLineLinkNo())) {
						BigDecimal mainItemInputQty = inputmap.get(omsCustOrdItem.getLineLinkNo());
						BigDecimal mainItemAvailableQuantity = BigDecimal.ZERO;
						log.info("mainItemInputQty -------------> " + mainItemInputQty);

						List<OmsCustOrdItem> omsCustOrdItem1 = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrderNo, omsCustOrdItem.getLineLinkNo());
						mainItemAvailableQuantity = omsCustOrdItem1.get(0).getQtyOrderedSuom().subtract(omsCustOrdItem1.get(0).getCumQtyDelivered().add(omsCustOrdItem1.get(0).getQtyCancelled()));
						log.info("mainItemAvailableQuantity-------------------->" + mainItemAvailableQuantity);
						BigDecimal childItemAvailableQty = omsCustOrdItem.getQtyOrderedSuom().subtract(omsCustOrdItem.getCumQtyDelivered().add(omsCustOrdItem.getQtyCancelled()));
						log.info("-----childItemAvailableQty : " + childItemAvailableQty);
						if (inputmap.keySet().contains(omsCustOrdItem.getLineNo())) {
							log.info("---------------------^^^^^^^^^^^^^^^^^^^^^----------------------");
							BigDecimal childItemQty = inputmap.get(omsCustOrdItem.getLineNo());
							if (childItemQty.intValue() != childItemAvailableQty.intValue() && mainItemAvailableQuantity.intValue() == mainItemInputQty.intValue()) {
								log.info("*******************IF LOOP**********************");
								inputItem.remove(omsCustOrdItem.getLineNo());
								log.info("<--------------- Child ItemTab ------------- " + itemTab.keySet() + "---------------");
								log.info("<-------------child input map----------" + inputmap.keySet() + "------------------------>");
								break;
							}

						}

					}
				}

			} catch (Exception e) {
				log.info("No records exist in OmsCustOrdItem for link line No " + omsCustOrdItem.getLineNo() + e.getMessage());
			}

		}

		log.info("itemTab " + itemTab.keySet());
		log.info("input " + inputItem.keySet());
		log.info("list.size() " + list.size());
		boolean flag = false;
		if (itemTab != null && itemTab.size() > 0) {
			for (BigDecimal k : itemTab.keySet()) {
				log.info("checking whether itemTab.keyset contains key from inputItem " + k);
				flag = false;
				if (inputItem.containsKey(k)) {
					flag = true;
					log.info("inputItem contains key " + k);
					log.info("flag " + flag);

				} else {
					log.info("key " + k + "doesnot contain in the inputItem.keySet() " + inputItem.keySet());
					flag = false;
					break;
				}
			}
			log.info("flag value after existing the loop " + flag);

			if (flag) {
				log.info("Allow to cancel");
				list.clear();

				log.info("list.size()" + list.size());
				log.info("Reqest contains both the items");
			}

		} else {
			log.info("Allow to cancel");
			list.clear();
			log.info("list.size()" + list.size());
		}
		return list;
	}

	public boolean saveCustOrdLog(CustomerOrderCancellation input) throws SOAPException {
		log.info("Inside saveCustOrdLog method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> omsCancelIdList = null;
		if (input.getSubCustOrderNo() != null) {
			omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), input.getSubCustOrderNo(), new BigDecimal(input.getCancellationId()));
		} else {
			omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), "1", new BigDecimal(input.getCancellationId()));
		}
		if (omsCancelIdList.isEmpty()) {
			log.error("No record found for omsCancelId in Oms_Cancel_head table , unable to insert in OmsCustOrdLog table");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("CancelId_notFound")));
			throw new SOAPException("-->No record found for omsCancelId in Oms_Cancel_head table , unable to insert in OmsCustOrdLog table");
		}
		omsCancelId = omsCancelIdList.get(0);
		if (session.getOmsCustOrdLogFindOmsCancelId(omsCancelId).size() > 0) {
			log.error("-->records already exsist in OmsCustlog , unable to insert in OmsCustOrdLog table");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("OmsCustOrdLog_Exist")));
			throw new SOAPException("records already exsist in OmsCustlog , unable to insert in OmsCustOrdLog table");
		} else {
			OmsPersistence omsPersistence = new OmsPersistence();
			omsPersistence.persistOmsCustOrdLog(omsCancelId, omsCustOrderNo, "CA");
			log.info("Saved data in oms_cust_ord_log table first time.");
		}
		return true;

	}

	public void checkCreateOrReseveCancellation(CustomerOrderCancellation input)
			throws SOAPException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,

			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("checkCreateOrReserveCancellation method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> omsCancelIdList = null;
		if (input.getSubCustOrderNo() != null) {
			omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), input.getSubCustOrderNo(), new BigDecimal(input.getCancellationId()));
		} else {
			omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(input.getCustOrderNo(), "1", new BigDecimal(input.getCancellationId()));
		}
		if (omsCancelIdList.isEmpty()) {
			log.error("No record found for omsCancelId in Oms_Cancel_head table , unable to insert in OmsCustOrdLog table");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("CancelId_notFound")));
			throw new SOAPException("-->No record found for omsCancelId in Oms_Cancel_head table , unable to insert in OmsCustOrdLog table");
		}
		omsCancelId = omsCancelIdList.get(0);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);
		OmsPersistence omsPersistence = new OmsPersistence();
		OmsCoCancelItem omsCoCancelItem = null;
		for (CustomerOrderCancellationItems customerOrderCancellationItems : input.getCancellationItems()) {
			omsCoCancelItem = omsPersistence.persistOmsCoCancelItem(customerOrderCancellationItems, omsCancelId, omsCustOrderNo);
			log.info("persisted into oms_co_cancel item");
		}
		if (omsCustOrdHead.getOrderCreateReserveInd().equals("R")) {
			List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrderNo);

			if (omsCoFulfillDetailList.size() == 0) {

				for (CustomerOrderCancellationItems customerOrderCancellationItems : input.getCancellationItems()) {
					List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrderNo, customerOrderCancellationItems.getItem(),
							new BigDecimal(customerOrderCancellationItems.getLineNo()));
					int openCancellledQty = customerOrderCancellationItems.getCancelQtySuom().intValue();
					for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList) {
						if (openCancellledQty > 0 && omsCustOrdReserve.getRmsResvQty().intValue() > 0) {
							OmsCoFoCancel omsCoFoCancel = new OmsCoFoCancel();
							omsCoFoCancel.setItem(omsCustOrdReserve.getItem());
							if (omsCustOrdReserve.getRmsResvQty().intValue() >= customerOrderCancellationItems.getCancelQtySuom().intValue()) {
								omsCoFoCancel.setFoCancelledOty((customerOrderCancellationItems.getCancelQtySuom()));
							} else {

								omsCoFoCancel.setFoCancelledOty(omsCustOrdReserve.getRmsResvQty());
							}
							omsCoFoCancel.setFulfillOrderNo(omsCustOrdReserve.getFulfillOrderNo());
							omsCoFoCancel.setLineNo((omsCustOrdReserve.getLineNo()));
							omsCoFoCancel.setOmsCancelId(omsCancelId);
							omsCoFoCancel.setCreateDatetime(new Timestamp(new Date().getTime()));
							session.persistOmsCoFoCancel(omsCoFoCancel);
							BigDecimal qtyToCancel = BigDecimal.ZERO;
							if (omsCustOrdReserve.getRmsResvQty().intValue() >= openCancellledQty) {
								// omsCustOrdReserve.setRmsResvQty(omsCustOrdReserve.getRmsResvQty().subtract(customerOrderCancellationItems.getCancelQtySuom()));
								// omsCustOrdReserve.setQty(new BigDecimal(openCancellledQty));
								if (omsCustOrdReserve.getRmsResvQty().subtract(new BigDecimal(openCancellledQty)).intValue() < 0) {
									omsCustOrdReserve.setQty(BigDecimal.ZERO);
									omsCustOrdReserve.setRmsResvQty(BigDecimal.ZERO);
									qtyToCancel = BigDecimal.ZERO;
								} else {
									omsCustOrdReserve.setQty(omsCustOrdReserve.getQty().subtract(new BigDecimal(openCancellledQty)));
									omsCustOrdReserve.setRmsResvQty(omsCustOrdReserve.getRmsResvQty().subtract(new BigDecimal(openCancellledQty)));
									qtyToCancel = new BigDecimal(openCancellledQty);
								}

								session.mergeOmsCustOrdReserve(omsCustOrdReserve);
								log.info("openCancellledQty=" + openCancellledQty);
								RMSPackage rmsPackage = new RMSPackage();
								rmsPackage.rollbackRmsPackageCall(input, customerOrderCancellationItems, omsCustOrderNo, omsCustOrdReserve, qtyToCancel);
								InterfacePersistence interfacePersistence = new InterfacePersistence();
								interfacePersistence.adjustInventoryByItemLocation(input, customerOrderCancellationItems, omsCustOrderNo, qtyToCancel, omsCustOrdReserve);
								log.info("Cancelling line no=" + omsCustOrdReserve.getLineNo() + " location=" + omsCustOrdReserve.getRmsResvLoc() + " cancel qty=" + omsCustOrdReserve.getRmsResvQty());
								openCancellledQty = 0;
								log.info("calling break stmt");
								break;
							} else {
								RMSPackage rmsPackage = new RMSPackage();
								rmsPackage.rollbackRmsPackageCall(input, customerOrderCancellationItems, omsCustOrderNo, omsCustOrdReserve, omsCustOrdReserve.getRmsResvQty());
								InterfacePersistence interfacePersistence = new InterfacePersistence();
								interfacePersistence.adjustInventoryByItemLocation(input, customerOrderCancellationItems, omsCustOrderNo, omsCustOrdReserve.getRmsResvQty(), omsCustOrdReserve);
								openCancellledQty = openCancellledQty - omsCustOrdReserve.getRmsResvQty().intValue();
								omsCustOrdReserve.setRmsResvQty(BigDecimal.ZERO);

								omsCustOrdReserve.setQty(BigDecimal.ZERO);
								session.mergeOmsCustOrdReserve(omsCustOrdReserve);
								log.info("Cancelling line no=" + omsCustOrdReserve.getLineNo() + " location=" + omsCustOrdReserve.getRmsResvLoc() + " cancel qty=" + omsCustOrdReserve.getRmsResvQty());

							}
						}
					}

					if (openCancellledQty > 0) {
						if (omsBackOrderDtlList.size() != 0) {
							// issue no 25
							if (omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
								// processCancellation(input);
								processcancellationcall = true;
							} else {
								List<OmsBackOrderDtl> omsBackOrderDtlItemList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo, customerOrderCancellationItems.getItem(),
										new BigDecimal(customerOrderCancellationItems.getLineNo()));
								log.info("omsBackOrderDtlList " + omsBackOrderDtlList.size());
								OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
								for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlItemList) {
									if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
										BigDecimal channelId = BigDecimal.ZERO;
										BigDecimal physicalWH = null;
										BigDecimal qtyToCancel = BigDecimal.ZERO;
										if (omsBackOrderDtl.getSourceQty().intValue() >= openCancellledQty) {
											qtyToCancel = new BigDecimal(openCancellledQty);
										} else {
											qtyToCancel = omsBackOrderDtl.getSourceQty();
										}
										log.info("Cancelling line no=" + omsBackOrderDtl.getLineNo() + " location=" + omsBackOrderDtl.getSourceLoc() + " cancel qty=" + omsBackOrderDtl.getSourceQty());

										openCancellledQty = openCancellledQty - qtyToCancel.intValue();
										if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
											List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
											for (Object[] result : tempWhObject) {
												physicalWH = new BigDecimal(result[0].toString());
												channelId = new BigDecimal(result[1].toString());
											}

											oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), qtyToCancel.negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
													channelId);

										} else {
											channelId = BigDecimal.ZERO;
											oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), qtyToCancel.negate(), omsBackOrderDtl.getSourceLoc().longValue(),
													omsBackOrderDtl.getSourceLocType(), "EA", channelId);

										}

										Long lineNoItem = customerOrderCancellationItems.getLineNo();
										String item1 = customerOrderCancellationItems.getItem();
										log.info("=========Seibel Data for Backorder==========");
										String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType()
												+ "," + omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
										backorderTreeMap.put(completeKey, qtyToCancel);
										log.info("customerOrderCancellationItems.getCancelQtySuom()).negate()" + customerOrderCancellationItems.getCancelQtySuom().negate());
										omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(qtyToCancel));
										if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
											omsBackOrderDtl.setBackorderStatus("S");
										}
										session.mergeOmsBackOrderDtl(omsBackOrderDtl);

									}
								}

								/*
								 * OmsCustOrdItem omsCustOrdItem
								 * =session.getOmsCustOrdItemFindByItem(omsCustOrderNo,
								 * customerOrderCancellationItems.getItem(),new
								 * BigDecimal(customerOrderCancellationItems.getLineNo()));
								 * log.info("customerOrderCancellationItems.getCancelQtySuom() "
								 * +customerOrderCancellationItems.getCancelQtySuom());
								 * log.info("omsCustOrdItem.getQtyCancelled() "+omsCustOrdItem.getQtyCancelled()
								 * ); omsCustOrdItem.setQtyCancelled(customerOrderCancellationItems.
								 * getCancelQtySuom().add(omsCustOrdItem.getQtyCancelled()));
								 * session.mergeOmsCustOrdItem(omsCustOrdItem);
								 * log.info("omsCustOrdItem.getQtyCancelled()==== "+omsCustOrdItem.
								 * getQtyCancelled());
								 */
							}
						}
					}

					if (!omsCustOrdHead.getOrdPaymentStatus().equals("S") && omsCustOrdHead.getOrderCreateReserveInd().equals("R")) {
						log.info("updating in omsCustOrdItem");
						OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, customerOrderCancellationItems.getItem(),
								new BigDecimal(customerOrderCancellationItems.getLineNo()));
						log.info("customerOrderCancellationItems.getCancelQtySuom() " + customerOrderCancellationItems.getCancelQtySuom());
						log.info("omsCustOrdItem.getQtyCancelled() " + omsCustOrdItem.getQtyCancelled());
						omsCustOrdItem.setQtyCancelled(customerOrderCancellationItems.getCancelQtySuom().add(omsCustOrdItem.getQtyCancelled()));
						// Change for Last update time
						omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
						session.mergeOmsCustOrdItem(omsCustOrdItem);
						log.info("omsCustOrdItem.getQtyCancelled()==== " + omsCustOrdItem.getQtyCancelled());
					}
					// OmsCoCancelItem
					// omsCoCancelItem1=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId,
					// new BigDecimal( customerOrderCancellationItems.getLineNo()));
					log.info("updating in oms_co_cancel item " + customerOrderCancellationItems.getCancelQtySuom());
					omsCoCancelItem.setCancelConfQty(customerOrderCancellationItems.getCancelQtySuom());
					log.info("updating oms_co_cancel_item");
					session.mergeOmsCoCancelItem(omsCoCancelItem);

				}
				if (processcancellationcall == true) {
					processCancellation(input);
					processcancellationcall = false;
				}
			} else if (omsBackOrderDtlList.size() != 0) {
				log.info("Pure back order with reserve indicator R");
				// issue no 25
				if (omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
					// Issue identified during mega sale
					// Added validation for cancellation of order with respect to
					// Backorder(Homedelivery Scenario)
					log.info("Added validation code for pure backorder, home delivery scenario");
					checkOpenDelivery(input);

					// Commented the processCancellation(input) since the checkopendelivery itself
					// is inturn calling processcancellation method

					// processCancellation(input);
				} else {
					for (CustomerOrderCancellationItems customerOrderCancellationItems : input.getCancellationItems()) {
						List<OmsBackOrderDtl> omsBackOrderDtlItemList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo, customerOrderCancellationItems.getItem(),
								new BigDecimal(customerOrderCancellationItems.getLineNo()));
						log.info("omsBackOrderDtlList " + omsBackOrderDtlList.size());
						OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
						int openCancellledQty = customerOrderCancellationItems.getCancelQtySuom().intValue();
						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlItemList) {
							if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
								BigDecimal channelId = BigDecimal.ZERO;
								BigDecimal physicalWH = null;
								BigDecimal qtyToCancel = BigDecimal.ZERO;
								if (omsBackOrderDtl.getSourceQty().intValue() >= openCancellledQty) {
									qtyToCancel = new BigDecimal(openCancellledQty);
								} else {
									qtyToCancel = omsBackOrderDtl.getSourceQty();
								}
								openCancellledQty = openCancellledQty - qtyToCancel.intValue();
								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
									for (Object[] result : tempWhObject) {
										physicalWH = new BigDecimal(result[0].toString());
										channelId = new BigDecimal(result[1].toString());
									}
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (qtyToCancel).negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA", channelId);

								} else {
									channelId = BigDecimal.ZERO;
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (qtyToCancel).negate(), omsBackOrderDtl.getSourceLoc().longValue(), omsBackOrderDtl.getSourceLocType(),
											"EA", channelId);

								}

								Long lineNoItem = customerOrderCancellationItems.getLineNo();
								String item1 = customerOrderCancellationItems.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, qtyToCancel);
								log.info("customerOrderCancellationItems.getCancelQtySuom()).negate()" + customerOrderCancellationItems.getCancelQtySuom().negate());
								omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract((customerOrderCancellationItems.getCancelQtySuom())));
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);

							}
						}

					}
				}
			}

			else {
				checkOpenDelivery(input);
			}

		} else {
			checkOpenDelivery(input);
		}
		log.info("Exited the checkCreacheckOpenDeliveryteOrReserveCancellation method");
	}

	public void checkOpenDelivery(CustomerOrderCancellation input) throws SOAPException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("--> inside checkOpenDelivery for oms_cust_ord_no=" + omsCustOrderNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);

		boolean isCancellable = false;
		// oct-26 -- mani changed for ST to S reservation and WH to S pickup changes
		boolean isReservation = true;
		for (CustomerOrderCancellationItems inputItem : input.getCancellationItems()) {
			log.info("inside for of item, omsCustOrderNo=" + omsCustOrderNo + "inputItem.getLineNo()" + inputItem.getLineNo() + "inputItem.getItem()" + inputItem.getItem());
			List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByItem(omsCustOrderNo, new BigDecimal(inputItem.getLineNo()), inputItem.getItem());
			log.info("omsCoFulfillDetailList size" + omsCoFulfillDetailList.size());
			int handeOverToCourierQty = 0;
			int openQty = 0;
			int backOrderQuantity = 0;
			int count = 0;

			for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
				isReservation = true;
				log.info("checking open delivery,inside fulfill order loop---" + omsCustOrdHead.getDeliveryType() + "----" + omsCoFulfillDetail.getSourceLocType());
				// mani changes for open qty old changes
				// openQty =
				// openQty + omsCoFulfillDetail.getFulfillReqQty().intValue() -
				// (omsCoFulfillDetail.getFulfillDeliverQty().intValue() +
				// omsCoFulfillDetail.getFulfillCancelQty().intValue());
				openQty = openQty + omsCoFulfillDetail.getFulfillReqQty().intValue() - omsCoFulfillDetail.getFulfillCancelQty().intValue();

				try {
					// OmsBackOrderDtl omsBackOrderDtl =
					// session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(omsCustOrderNo,
					// omsCoFulfillDetail.getLineNo(),omsCoFulfillDetail.getSourceLoc());
					// issue in which

					if (count == 0) {
						List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo, inputItem.getItem(), new BigDecimal(inputItem.getLineNo()));
						if (omsBackOrderDtlList.size() != 0) {
							for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {

								// BigDecimal
								// currentBOQty=omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty());
								backOrderQuantity = backOrderQuantity + omsBackOrderDtl.getSourceQty().intValue() - omsBackOrderDtl.getFulfillQty().intValue();
							}
							count++;
						}
					}

				} catch (Exception e) {
					log.info("Exception while fetching the values from backorder table " + e.getMessage());
				}
				if (omsCustOrdHead.getDeliveryType().equals("S") && omsCoFulfillDetail.getSourceLocType().equals("WH") == false
						&& omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
					log.info("$$$$$$$$$$$$$$$$$$$inside ST BOL Check$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
					InterfacePersistence interfacePersistence = new InterfacePersistence();
					String result = interfacePersistence.pickQtyCheckMod(omsCoFulfillDetail, input.getCustOrderNo(), inputItem.getCancelQtySuom());

					if (result == "PICKED") {
						String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrderNo);
						Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress, languageCode);
						if (Boolean.FALSE == omsErrorCodesRecordExist) {
							languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
						}
						throw new SOAPException(
								OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress, languageCode, new String[] { inputItem.getItem().toString() }));

					} else {
						OpenDeliveryBean openDeliveryBean = omsUtilCommmons.findAvailableQtyForCancellation(omsCoFulfillDetail, input.getCustOrderNo());
						if (openDeliveryBean.getItem() != null) {

							handeOverToCourierQty = handeOverToCourierQty + Math.abs((openDeliveryBean.getQuantity() - omsCoFulfillDetail.getFulfillDeliverQty().intValue()));
							log.info("handeOverToCourierQty=" + handeOverToCourierQty);
						} else {
							isCancellable = true;
						}
					}
				} else if (omsCustOrdHead.getDeliveryType().equals("S") && omsCoFulfillDetail.getSourceLocType().equals("WH")) {
					// ship to customer from ware house
					log.info("omsCustOrdHead.getDeliveryType().equals(\"S\") &&\n" + "                       omsCoFulfillDetail.getSourceLoc().equals(\"WH\") == false");
					// List<Ordcust> ordCustList =
					// session.getOrdcustFindByFulfilOrdNo(input.getCustOrderNo().toString(),
					// omsCoFulfillDetail.getFulfillOrderNo().toString());
					log.info("extCustOrdNo " + extCustOrdNo);
					log.info(" omsCoFulfillDetail.getFulfillOrderNo().toString() " + omsCoFulfillDetail.getFulfillOrderNo().toString());
					List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(),
							omsCoFulfillDetail.getFulfillLoc());
					if (ordCustList.get(0).getTsfNo() != null && ordCustList.get(0).getTsfNo().intValue() != 0) {
						log.info("Transfer No" + ordCustList.get(0).getTsfNo());
						log.info("Item " + omsCoFulfillDetail.getItem().toString());
						BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
						// mani changes for pick logic old changes
						// OMSUtilJdbc omsUtilJdbc = new OMSUtilJdbc();
						InterfacePersistence interfacePersistence = new InterfacePersistence();

						try {
							totalSelectedDistroQty = interfacePersistence.getselectedandDistroQty(omsCoFulfillDetail.getItem(), ordCustList.get(0).getTsfNo());
							log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);

						} catch (Exception e) {
							log.info("Exception from getselectedandDistroQty " + e.getMessage());
							totalSelectedDistroQty = BigDecimal.ZERO;
						}
						log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);

						if (totalSelectedDistroQty.intValue() > 0) {
							// handeOverToCourierQty=handeOverToCourierQty+selectedQty.intValue();
							handeOverToCourierQty = handeOverToCourierQty + totalSelectedDistroQty.intValue();
							log.info("handeOverToCourierQty " + handeOverToCourierQty);
							// throw new
							// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("CANT_CANCEL")));
							// throw new SOAPException(OMSUtilCommons.formErrorDescription("CANT_CANCEL",
							// "1",
							// new String[] {omsCoFulfillDetail.getItem().toString() }));
						} else {
							log.info("No transfer can cancel order");
							// cancel the order
							isCancellable = true;
							// --- processCancellation(input);
						}
					}
					// oct-26 -- mani changed for ST to S reservation and WH to S pickup changes
					if (!omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc()) && omsCoFulfillDetail.getTsfNo() != null && omsCoFulfillDetail.getSourceLocType().equals("WH")
							&& omsCoFulfillDetail.getFulfillLocType().equals("S")) {
						isReservation = false;
					}

				}

				// else if(omsCustOrdHead.getDeliveryType().equals("S")) {
				// log.info("SIM Pick qty validation for SFS");
				// // -- changes
				// // old changes=isCancellable = true;
				// // log.info("Cancel order");
				// // CR-Number:
				// InterfacePersistence interfacePersistence = new InterfacePersistence();
				// String result =
				// interfacePersistence.pickQtyCheckMod(omsCoFulfillDetail,
				// input.getCustOrderNo(), inputItem.getCancelQtySuom());
				//
				// if (result == "PICKED") {
				// String languageCode =
				// OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrderNo);
				// Boolean omsErrorCodesRecordExist =
				// OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress,
				// languageCode);
				// if (Boolean.FALSE == omsErrorCodesRecordExist) {
				// languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				// }
				// throw new
				// SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress,
				// languageCode,
				// new String[] { inputItem.getItem().toString() }));
				//
				// } else {
				// log.info("inside else : isCancellable true");
				// isCancellable = true;
				// }
				//
				//
				// //--- processCancellation(input);
				// }
				else {
					// oct-26 -- mani changed for ST to S reservation and WH to S pickup changes
					if (!omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc()) && omsCoFulfillDetail.getTsfNo() != null && omsCoFulfillDetail.getSourceLocType().equals("ST")
							&& omsCoFulfillDetail.getFulfillLocType().equals("S")) {
						isReservation = false;
					}

					isCancellable = true;

				}

			} // for end
			log.info("handeOverToCourierQty=" + handeOverToCourierQty + "openQty=" + openQty);
			if (omsCoFulfillDetailList.size() != 0) {
				int a = (openQty + backOrderQuantity) - handeOverToCourierQty;
				log.info("*" + a);

				// oct-26 -- mani changed for ST to S reservation and WH to S pickup changes
				if (inputItem.getCancelQtySuom().intValue() > (openQty + backOrderQuantity) - handeOverToCourierQty && isReservation) {
					isCancellable = false;
					// throw new SOAPException(OMSUtilCommons.formErrorDescription("CANT_CANCEL",
					// "1",
					String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrderNo);
					Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress, languageCode);
					if (Boolean.FALSE == omsErrorCodesRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}

					throw new SOAPException(
							OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cannot_Cancel_Delievry_In_Progress, languageCode, new String[] { inputItem.getItem().toString() }));
				} else {
					isCancellable = true;
				}

			} else {
				// cancellation of back order
				isCancellable = true;

			}
		}
		if (isCancellable == true)
			processCancellation(input);
	}

	public void processCancellation(CustomerOrderCancellation input) throws SOAPException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException {
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		log.info("inside method saveCOCancellationItems");
		// shifted the call to saveCustOrdLog method instead of impl class as even if
		// failed in checking open delivery record getting inserted
		if (countforCusordLogPersistance == 0) {
			saveCustOrdLog(input);
			countforCusordLogPersistance++;
		}
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);

		BigDecimal logSeqNo = session.getOmsCustOrdLogFindlogSeqNo(omsCancelId);
		List<CustomerOrderCancellationItems> itemList = input.getCancellationItems();
		OmsCoCancelItem omsCoCancelItem = new OmsCoCancelItem();
		OmsCustOrdItem omsCustOrdItem;
		OmsCoFoCancel omsCoFoCancel = new OmsCoFoCancel();
		for (CustomerOrderCancellationItems item : itemList) {
			OmsPersistence omsPersistence = new OmsPersistence();
			omsCoCancelItem = session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId, new BigDecimal(item.getLineNo()));
			List<OmsCoFulfillDetail> fulfillDetailList = null;
			log.info("oms_cust_ord_no=" + extCustOrdNo + "item=" + item.getItem());
			L_CO_ITEM_CANCEL_QTY = item.getCancelQtySuom().longValue();
			L_cum_cancel_qty = 0;
			long backOrderOpenQty = 0;
			boolean isBOFound = false;

			try {
				// changed the code for line no
				// changed the query for fetching the OmsFulfillDetails by adding lineNo
				// filtration
				fulfillDetailList = session.getOmsCoFulfillDetailFindFulFillDetails(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
				fulfillDetailList = shuffleFulfillOrderNo(fulfillDetailList);
				log.info("Size of fulfil detail list :" + fulfillDetailList.size());
			} catch (Exception e) {
				log.error("-->no records found in fulfil_detail table");
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("NoRecord_FulfilDetail")));
				throw new SOAPException("no records found in fulfil_detail table");
			}
			if (fulfillDetailList.size() == 0 && L_cum_cancel_qty == 0) {
				log.info("Condition: Size of fulfil detail list is 0. and L_cum_cancel_qty == 0 ");
				log.info("omsCustOrderNo=" + omsCustOrderNo + "item=" + item.getItem() + "line no=" + item.getLineNo());
				List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
				log.info("omsBackOrderDtlList " + omsBackOrderDtlList.size());
				BigDecimal requestedCancelQty = BigDecimal.ZERO;
				int i = 0;
				if (omsBackOrderDtlList.size() != 0) {
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {

						// issue 14
						BigDecimal currentBOQty = omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty());
						if (i == 0) {
							requestedCancelQty = item.getCancelQtySuom();
							i++;
						}
						if (omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() != 0 && requestedCancelQty.intValue() != 0) {
							BigDecimal channelId = BigDecimal.ZERO;
							BigDecimal physicalWH = null;
							if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
								List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
								for (Object[] result : tempWhObject) {
									physicalWH = new BigDecimal(result[0].toString());
									channelId = new BigDecimal(result[1].toString());
								}
							} else {
								channelId = BigDecimal.ZERO;
							}

							if (currentBOQty.intValue() >= requestedCancelQty.intValue()) {

								Long lineNoItem = item.getLineNo();
								String item1 = item.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, requestedCancelQty);
								omsBackOrderDtl.setSourceQty(currentBOQty.subtract(requestedCancelQty));
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);

								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									log.info("====================calling WH========================");
									log.info("calling RMSBackOrderWS for WH for qty " + requestedCancelQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling WH========================");
									log.info("calling RMSBackOrderWS for WH for qty " + requestedCancelQty.negate());
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), requestedCancelQty.negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
											channelId);
								}

								else // ST
								{
									log.info("====================calling ST========================");
									log.info("calling RMSBackOrderWS for ST for qty " + requestedCancelQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("omsBackOrderDtl.getFulfillLoc() " + omsBackOrderDtl.getFulfillLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling ST========================");
									log.info("calling RMSBackOrderWS for ST for qty " + requestedCancelQty.negate());
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), requestedCancelQty.negate(), omsBackOrderDtl.getSourceLoc().longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);
								}
								break;
							} else {
								Long lineNoItem = item.getLineNo();
								String item1 = item.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, currentBOQty);
								omsBackOrderDtl.setSourceQty(BigDecimal.ZERO);
								requestedCancelQty = requestedCancelQty.subtract(currentBOQty);
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									log.info("====================calling WH========================");
									log.info("calling RMSBackOrderWS for WH for qty " + currentBOQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling WH========================");
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), currentBOQty.negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA", channelId);
								} else // ST
								{
									log.info("====================calling ST========================");
									log.info("calling RMSBackOrderWS for ST for qty " + currentBOQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("omsBackOrderDtl.getFulfillLoc() " + omsBackOrderDtl.getFulfillLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling ST========================");
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), currentBOQty.negate(), omsBackOrderDtl.getSourceLoc().longValue(), omsBackOrderDtl.getSourceLocType(),
											"EA", channelId);
								}

							}
							if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
								omsBackOrderDtl.setBackorderStatus("S");
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							}

						}

						// issue 14 end
						if (omsBackOrderDtl.getSourceQty().intValue() >= item.getCancelQtySuom().intValue()) {
							log.info("omsBackOrderDtl.getSourceQty().intValue()>=L_fo_open_qty");

							BigDecimal channelId = BigDecimal.ZERO;
							BigDecimal physicalWH = null;
							if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
								List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
								for (Object[] result : tempWhObject) {
									physicalWH = new BigDecimal(result[0].toString());
									channelId = new BigDecimal(result[1].toString());
								}
								oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (item.getCancelQtySuom()).negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
										channelId);
							} else {
								channelId = BigDecimal.ZERO;
								oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (item.getCancelQtySuom()).negate(), omsBackOrderDtl.getSourceLoc().longValue(),
										omsBackOrderDtl.getSourceLocType(), "EA", channelId);

							}
							log.info("omsBackOrderDtl.getSourceQty() " + omsBackOrderDtl.getSourceQty());
							log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
							log.info("omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()) " + omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()));

							Long lineNoItem = item.getLineNo();
							String item1 = item.getItem();

							log.info("=========Seibel Data for Backorder==========");
							String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
									+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
							backorderTreeMap.put(completeKey, item.getCancelQtySuom());

							omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()));
							log.info("omsBackOrderDtl.getSourceQty()" + omsBackOrderDtl.getSourceQty());
							if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
								omsBackOrderDtl.setBackorderStatus("S");
							}
							session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							// L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
						}
					}
					OmsCustOrdItem omsCustOrdItem1 = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
					omsCustOrdItem1.setQtyCancelled(omsCustOrdItem1.getQtyCancelled().add(item.getCancelQtySuom()));
					// Change for Last update time
					omsCustOrdItem1.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
					session.mergeOmsCustOrdItem(omsCustOrdItem1);
					// OmsCoCancelItem
					// omsCoCancelItem2=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId,
					// new BigDecimal(item.getLineNo()));
					omsCoCancelItem.setCancelConfQty(item.getCancelQtySuom());
					log.info("updating oms_co_cancel_item");
					session.mergeOmsCoCancelItem(omsCoCancelItem);
				} else {
					// updating oms_co_cancel_item
					// OmsCoCancelItem
					// omsCoCancelItem3=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId,
					// new BigDecimal(item.getLineNo()));
					omsCoCancelItem.setCancelConfQty(BigDecimal.ZERO);
					log.info("updating oms_co_cancel_item");
					session.mergeOmsCoCancelItem(omsCoCancelItem);
				}
			}
			// finished
			else if (fulfillDetailList.size() == 0 && L_cum_cancel_qty != 0) {
				List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
				OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
					if (omsBackOrderDtl.getFulfillQty().intValue() >= L_fo_open_qty) {
						log.info("--" + new BigDecimal(L_cum_cancel_qty).negate());
						log.info("omsBackOrderDtl.getSourceQty().intValue()>=L_fo_open_qty");
						BigDecimal channelId = BigDecimal.ZERO;
						BigDecimal physicalWH = null;
						if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
							for (Object[] result : tempWhObject) {
								physicalWH = new BigDecimal(result[0].toString());
								channelId = new BigDecimal(result[1].toString());
							}
							oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (item.getCancelQtySuom()).negate(), physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
									channelId);
						} else {
							channelId = BigDecimal.ZERO;
							oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (item.getCancelQtySuom()).negate(), omsBackOrderDtl.getSourceLoc().longValue(),
									omsBackOrderDtl.getSourceLocType(), "EA", channelId);

						}

						Long lineNoItem = item.getLineNo();
						String item1 = item.getItem();

						log.info("=========Seibel Data for Backorder==========");
						String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
								+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();

						backorderTreeMap.put(completeKey, item.getCancelQtySuom());
						log.info("Value of key for Seibel: " + completeKey);
						log.info("Source Qty value while sending to Seibel : " + omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()));
						omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()));
						if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
							omsBackOrderDtl.setBackorderStatus("S");
						}
						session.mergeOmsBackOrderDtl(omsBackOrderDtl);
						// L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
					}
				}
				log.info("Condition: Size of fulfil detail list is 0. and L_cum_cancel_qty !=0 ");
				// OmsCoCancelItem
				// omsCoCancelItem4=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId,
				// new BigDecimal(item.getLineNo()));
				omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
				log.info("updating oms_co_cancel_item");
				session.mergeOmsCoCancelItem(omsCoCancelItem);
				try {
					// changed the code for lineNo
					omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
				} catch (Exception e) {
					log.error("no records found in oms_cust_ord_item");
					throw new SOAPException("no records found in oms_cust_ord_item table");
				}
				log.info("old value in oms_cust_ord_item" + omsCustOrdItem.getQtyCancelled());
				omsCustOrdItem.setQtyCancelled((item.getCancelQtySuom().add(omsCustOrdItem.getQtyCancelled())));
				// Change for Last update time
				omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
				log.info("new value in oms_cust_ord_item" + omsCustOrdItem.getQtyCancelled());
				log.info("updating oms_cust_ord_item");
				session.mergeOmsCustOrdItem(omsCustOrdItem); // updating oms_cust_ord_item
				log.info("Successfully updated OmsCustOrdItem table for size--0 and l_cum_cancel_qty!=0 loop");
				continue; // fetch new record (check for new logic)
			} else {
				log.info("fulfil detail loop going to start ");
				int fulfillDetailList_count = 0;
				int whQty = 0;
				for (OmsCoFulfillDetail fulfilDetail : fulfillDetailList) {
					fulfillDetailList_count++;
					log.info("fulfil lopp start-------");
					log.info("item=" + fulfilDetail.getItem() + "line_no=" + fulfilDetail.getLineNo());
					log.info("Fulfil ord no=" + fulfilDetail.getFulfillOrderNo());
					fulilmentOrdNo = fulfilDetail.getFulfillOrderNo().longValue();
					try {
						log.info("fulfilDetail.getFulfillConfQty() " + fulfilDetail.getFulfillConfQty());
						log.info("fulfilDetail.getFulfillDeliverQty()" + fulfilDetail.getFulfillDeliverQty());
						log.info("fulfilDetail.getFulfillCancelQty() " + fulfilDetail.getFulfillCancelQty());
						log.info("omsCoCancelItem.getCancelReqQty() " + omsCoCancelItem.getCancelReqQty());
						log.info("omsCoCancelItem.getCancelConfQty()" + omsCoCancelItem.getCancelConfQty());
						L_fo_open_qty = Math.min((fulfilDetail.getFulfillConfQty().longValue() - fulfilDetail.getFulfillDeliverQty().longValue() - fulfilDetail.getFulfillCancelQty().longValue()),
								(omsCoCancelItem.getCancelReqQty().longValue() - omsCoCancelItem.getCancelConfQty().longValue()));

					} catch (Exception e) {
						log.error("failing in finding min qty" + e);
					}
					log.info("Minimum quantity is L_fo_open_qty=" + L_fo_open_qty);
					omsCoFoCancel.setItem(item.getItem());

					log.info("--------Item is set----" + item.getItem());
					// omsCoFoCancel.setFoCancelledOty(new BigDecimal(L_fo_open_qty));
					omsCoFoCancel.setFoCancelledOty(BigDecimal.ZERO);
					log.info("-----Cancelled Qty----" + L_fo_open_qty);
					omsCoFoCancel.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));
					log.info("---Fulfill order no---" + fulilmentOrdNo);
					omsCoFoCancel.setLineNo(new BigDecimal(item.getLineNo()));
					log.info("-----line number is------" + item.getLineNo());
					omsCoFoCancel.setOmsCancelId(omsCancelId);
					log.info("-----omsCancelId is set-------");
					omsCoFoCancel.setCreateDatetime(new Timestamp(new Date().getTime()));
					session.persistOmsCoFoCancel(omsCoFoCancel);
					log.info("persisted in oms_co_fo_cancel");
					log.info("Source loc type is:" + fulfilDetail.getSourceLocType());
					// OmsCustOrdItem omsCustOrdItem;
					try {
						log.info("Inside the try block");
						// changed the code for lineNo
						omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
					} catch (Exception e) {
						log.info("no records found in oms_cust_ord_item");
						// throw new
						// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("NoRecord_OmsCustOrdItem")));
						throw new SOAPException("no records found in oms_cust_ord_item table with omsCustOrdNo and item=" + item.getItem());
					}

					List<OmsBackOrderDtl> omsBackOrderDtlList = null;
					try {

						omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNo(omsCustOrderNo, fulfilDetail.getLineNo());
						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
							if (omsBackOrderDtl.getSourceQty().intValue() > 0) {
								isBOFound = true;
								backOrderOpenQty = Math.min((omsBackOrderDtl.getSourceQty().longValue() - omsBackOrderDtl.getFulfillQty().longValue()),
										(omsCoCancelItem.getCancelReqQty().longValue() - omsCoCancelItem.getCancelConfQty().longValue()));
							}
						}
						log.info("isBOFound" + isBOFound + "backOrderOpenQty=" + backOrderOpenQty);
					} catch (Exception e) {
						log.info("No back order created for order");
					}
					if (fulfilDetail.getSourceLocType() != null && fulfilDetail.getSourceLocType().equals("SU")) {
						// cancelling PO
						log.info("source loc=SU");
						// List<OmsBackOrderDtl> omsBackOrderDtlList=
						// session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrderNo,
						// item.getItem(),new BigDecimal( item.getLineNo()));

						interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));

						// updating oms_co_fo_cancel table
						omsCoFoCancel.setWsResponse("C");
						session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
						log.info("old cancel Quantity" + fulfilDetail.getFulfillCancelQty());
						fulfilDetail.setFulfillCancelQty(new BigDecimal(L_fo_open_qty).add(fulfilDetail.getFulfillCancelQty()));
						omsCoFoCancel.setFoCancelledOty(new BigDecimal(L_fo_open_qty));

						su_qty = BigDecimal.valueOf(su_qty.intValue() + fulfilDetail.getFulfillCancelQty().intValue());

						log.info("new cancel Quantity" + fulfilDetail.getFulfillCancelQty());
						// changed the code for lineNo
						// fulfilDetail.setLineNo(new BigDecimal(item.getLineNo()));
						session.mergeOmsCoFulfillDetail(fulfilDetail);
						session.mergeOmsCoFoCancel(omsCoFoCancel);
						L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "   L_fo_open_qty=" + L_fo_open_qty);
						omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
						session.mergeOmsCoCancelItem(omsCoCancelItem);
						if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
							log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty=" + L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);

							// issue -if only one record in fulfilement and BO then not cancelling the BO
							if (fulfillDetailList.size() == fulfillDetailList_count) {
								if (isBOFound == true) {
									log.info("Cancelling BO");
									OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();

									log.info("L_cum_cancel_qty=" + L_cum_cancel_qty);
									boolean backOrderCompleted = false;
									for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
										backOrderOpenQty = Math.min((omsBackOrderDtl.getSourceQty().longValue() - omsBackOrderDtl.getFulfillQty().longValue()),
												(omsCoCancelItem.getCancelReqQty().longValue() - omsCoCancelItem.getCancelConfQty().longValue()));
										log.info("backOrderOpenQty=" + backOrderOpenQty);
										if (omsBackOrderDtl != null && omsBackOrderDtl.getSourceQty().intValue() >= backOrderOpenQty
												&& omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
											BigDecimal channelId = BigDecimal.ZERO;
											BigDecimal physicalWH = null;
											if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
												log.info("Source loc in back order is WH " + omsBackOrderDtl.getSourceLoc());
												/*
												 * List<Object[]> tempWhObject=
												 * session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc()); BigDecimal
												 * physicalWH=BigDecimal.ZERO; BigDecimal channelId=BigDecimal.ZERO;
												 * for(Object[] result:tempWhObject) { log.info("-----"); physicalWH= new
												 * BigDecimal(result[0].toString()); omsBackOrderDtl.setSourceLoc(physicalWH);
												 * channelId=new BigDecimal(result[1].toString());
												 * log.info("WH="+result[0]+"channel id="+result[1]); }
												 */
												log.info("Calling BO cancellation WS");

												List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
												for (Object[] result : tempWhObject) {
													physicalWH = new BigDecimal(result[0].toString());
													channelId = new BigDecimal(result[1].toString());
												}
												oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (new BigDecimal(backOrderOpenQty).negate()), physicalWH.longValue(),
														omsBackOrderDtl.getSourceLocType(), "EA", channelId);

											} else {
												channelId = BigDecimal.ZERO;
												oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (new BigDecimal(backOrderOpenQty).negate()), omsBackOrderDtl.getSourceLoc().longValue(),
														omsBackOrderDtl.getSourceLocType(), "EA", channelId);

											}

											Long lineNoItem = item.getLineNo();
											String item1 = item.getItem();

											log.info("=========Seibel Data for Backorder==========");
											String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + ","
													+ omsBackOrderDtl.getSourceLocType() + "," + omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
											backorderTreeMap.put(completeKey, new BigDecimal(backOrderOpenQty));

											omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(new BigDecimal(backOrderOpenQty)));
											if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
												omsBackOrderDtl.setBackorderStatus("S");
											}
											session.mergeOmsBackOrderDtl(omsBackOrderDtl);
										}

										L_cum_cancel_qty = backOrderOpenQty + L_cum_cancel_qty;

										if (L_fo_open_qty > backOrderOpenQty)
											L_fo_open_qty = L_fo_open_qty - backOrderOpenQty;
										else if (omsCoCancelItem.getCancelReqQty().intValue() > backOrderOpenQty)
											L_fo_open_qty = omsCoCancelItem.getCancelReqQty().intValue() - backOrderOpenQty;
										else
											L_fo_open_qty = backOrderOpenQty - omsCoCancelItem.getCancelReqQty().intValue();

										log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "   L_fo_open_qty=" + backOrderOpenQty);
										omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
										session.mergeOmsCoCancelItem(omsCoCancelItem);

										if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
											log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty=" + L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);
											// continue; //Fetching another FO
										} else {
											backOrderCompleted = true;
											break;
										}
									}
									if (backOrderCompleted == true) {
										break;
									}

								}
							}

							continue;
						} else {
							break;
						}

					}
					if (isBOFound == true) {
						log.info("Cancelling BO");
						OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();

						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty);
						boolean backOrderCompleted = false;
						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
							backOrderOpenQty = Math.min((omsBackOrderDtl.getSourceQty().longValue() - omsBackOrderDtl.getFulfillQty().longValue()),
									(omsCoCancelItem.getCancelReqQty().longValue() - omsCoCancelItem.getCancelConfQty().longValue()));
							log.info("backOrderOpenQty=" + backOrderOpenQty);
							if (omsBackOrderDtl != null && omsBackOrderDtl.getSourceQty().intValue() >= backOrderOpenQty
									&& omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
								BigDecimal channelId = BigDecimal.ZERO;
								BigDecimal physicalWH = null;
								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									log.info("Source loc in back order is WH " + omsBackOrderDtl.getSourceLoc());
									/*
									 * List<Object[]> tempWhObject=
									 * session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc()); BigDecimal
									 * physicalWH=BigDecimal.ZERO; BigDecimal channelId=BigDecimal.ZERO;
									 * for(Object[] result:tempWhObject) { log.info("-----"); physicalWH= new
									 * BigDecimal(result[0].toString()); omsBackOrderDtl.setSourceLoc(physicalWH);
									 * channelId=new BigDecimal(result[1].toString());
									 * log.info("WH="+result[0]+"channel id="+result[1]); }
									 */
									log.info("Calling BO cancellation WS");

									List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
									for (Object[] result : tempWhObject) {
										physicalWH = new BigDecimal(result[0].toString());
										channelId = new BigDecimal(result[1].toString());
									}
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (new BigDecimal(backOrderOpenQty).negate()), physicalWH.longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);

								} else {
									channelId = BigDecimal.ZERO;
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), (new BigDecimal(backOrderOpenQty).negate()), omsBackOrderDtl.getSourceLoc().longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);

								}

								Long lineNoItem = item.getLineNo();
								String item1 = item.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = lineNoItem.toString() + "," + item1.toString() + "," + omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + "," + omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, new BigDecimal(backOrderOpenQty));

								omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(new BigDecimal(backOrderOpenQty)));
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0 || omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							}
							log.info("L_cum_cancel_qty after assigning " + L_cum_cancel_qty);
							L_cum_cancel_qty = backOrderOpenQty + L_cum_cancel_qty;
							log.info("L_cum_cancel_qty after assigning " + L_cum_cancel_qty);
							// issue no 13
							if (L_fo_open_qty > backOrderOpenQty) {

								// L_fo_open_qty = L_fo_open_qty - backOrderOpenQty;
								L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
								log.info("L_fo_open_qty =" + L_fo_open_qty);
							} else if (omsCoCancelItem.getCancelReqQty().intValue() > (su_qty.intValue() + backOrderOpenQty))
								L_fo_open_qty = omsCoCancelItem.getCancelReqQty().intValue() - (su_qty.intValue() + backOrderOpenQty);
							else
								L_fo_open_qty = (su_qty.intValue() + backOrderOpenQty) - omsCoCancelItem.getCancelReqQty().intValue();
							// end issue no 13
							// L_fo_open_qty = L_fo_open_qty - backOrderOpenQty;
							L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
							log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "L_fo_open_qty=" + L_fo_open_qty + "backOrderOpenQty= " + backOrderOpenQty + "L_CO_ITEM_CANCEL_QTY ="
									+ L_CO_ITEM_CANCEL_QTY);
							omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
							session.mergeOmsCoCancelItem(omsCoCancelItem);

							if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
								log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty=" + L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);
								// continue; //Fetching another FO
							} else {
								backOrderCompleted = true;
								break;
							}
						}
						if (backOrderCompleted == true) {
							break;
						}

					}
					if (fulfilDetail.getSourceLocType() != null && (fulfilDetail.getSourceLocType().equals("ST") || fulfilDetail.getSourceLocType().equals("WH"))
							&& fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) != 0) {
						// cancelling transfer
						log.info("source loc=ST or WH");
						log.info("fulilmentOrdNo" + fulilmentOrdNo);
						try {
							if (omsCustOrdHead.getDeliveryType().equals("C") && fulfilDetail.getSourceLocType().equals("WH")) {
								List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
										fulfilDetail.getFulfillLoc());
								if (ordCustList.get(0).getTsfNo() != null && ordCustList.get(0).getTsfNo().intValue() != 0) {
									log.info("Transfer No" + ordCustList.get(0).getTsfNo());
									log.info("Item " + fulfilDetail.getItem().toString());
									OMSUtilJdbc omsUtilJdbc = new OMSUtilJdbc();
									BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
									try {
										totalSelectedDistroQty = omsUtilJdbc.getselectedandDistroQty(fulfilDetail.getItem().toString(), ordCustList.get(0).getTsfNo());
										log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);

									} catch (Exception e) {
										log.info("Exception from getselectedandDistroQty " + e.getMessage());
										totalSelectedDistroQty = BigDecimal.ZERO;
									}
									log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);
									if (totalSelectedDistroQty.intValue() == 0) {
										log.info("");
										whQty = fulfilDetail.getFulfillConfQty().intValue() - (fulfilDetail.getFulfillDeliverQty().intValue() + fulfilDetail.getFulfillCancelQty().intValue());
										if (L_fo_open_qty > whQty) {
											log.info("Cancelling WH transfer");
											interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
											L_fo_open_qty = L_fo_open_qty - whQty;
										} else {
											interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
											// L_fo_open_qty=0;
										}

									} else {
										// for production issue for which cancellation not allowed for order fulfilled
										// from WH and partial qty waved and trying to cancel remaining
										whQty = fulfilDetail.getFulfillConfQty().intValue() - (fulfilDetail.getFulfillDeliverQty().intValue() + fulfilDetail.getFulfillCancelQty().intValue())
												- totalSelectedDistroQty.intValue();

										// Added the condition for whqty>0, for mega sale issue.
										if (whQty > 0) {
											if (L_fo_open_qty > whQty) {
												log.info("Cancelling WH transfer");
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
												L_fo_open_qty = L_fo_open_qty - whQty;
											} else {
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));

											}
										}
									} /// end of else block
								}
							} else {
								// Calling RMS cancelFulfilOrdColRef
								if (fulfilDetail.getSourceLocType().equals("WH")) {
									List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
											fulfilDetail.getFulfillLoc());
									if (ordCustList.get(0).getTsfNo() != null && ordCustList.get(0).getTsfNo().intValue() != 0) {
										log.info("Transfer No" + ordCustList.get(0).getTsfNo());
										log.info("Item " + fulfilDetail.getItem().toString());
										OMSUtilJdbc omsUtilJdbc = new OMSUtilJdbc();
										BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
										try {
											totalSelectedDistroQty = omsUtilJdbc.getselectedandDistroQty(fulfilDetail.getItem().toString(), ordCustList.get(0).getTsfNo());
											log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);

										} catch (Exception e) {
											log.info("Exception from getselectedandDistroQty " + e.getMessage());
											totalSelectedDistroQty = BigDecimal.ZERO;
										}
										log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);
										if (totalSelectedDistroQty.intValue() != 0) {
											// for production issue for which cancellation not allowed for order fulfilled
											// from WH and partial qty waved and trying to cancel remaining
											whQty = fulfilDetail.getFulfillConfQty().intValue() - (fulfilDetail.getFulfillDeliverQty().intValue() + fulfilDetail.getFulfillCancelQty().intValue())
													- totalSelectedDistroQty.intValue();
											if (L_fo_open_qty > whQty) {
												log.info("Cancelling WH transfer");
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
												L_fo_open_qty = L_fo_open_qty - whQty;
											} else {
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
												// L_fo_open_qty=0;
											}

											// continue;
										} else {
											whQty = fulfilDetail.getFulfillConfQty().intValue() - (fulfilDetail.getFulfillDeliverQty().intValue() + fulfilDetail.getFulfillCancelQty().intValue());
											log.info("qty to cancel in WH " + whQty);
											if (L_fo_open_qty > whQty) {
												log.info("Cancelling WH transfer");
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
												L_fo_open_qty = L_fo_open_qty - whQty;
											} else {
												interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
												// L_fo_open_qty=0;
											}

										}
									} else {

										interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
									}
								} else {
									// store to store transfer
									OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
									List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
											fulfilDetail.getFulfillLoc());
									log.info("calling SIMLoopUpTransferHeader ");
									log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
									log.info("ordCustList.get(0).getTsfNo() " + ordCustList.get(0).getTsfNo());

									StsTsfHdrColDesc stsTsfHdrColDesc = oMSUtilCommons.callSimLookupTransferHeader(fulfilDetail.getSourceLoc().longValue(), ordCustList.get(0).getTsfNo().longValue());
									log.info("calling SimReadTransferDetail");
									log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
									log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() " + stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
									StsTsfDesc stsTsfDesc = oMSUtilCommons.callSimReadTransferDetail(fulfilDetail.getSourceLoc().longValue(),
											stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
									List<StsTsfItm> stsTsfItmList = stsTsfDesc.getStsTsfItm();
									for (StsTsfItm stsTsfItm : stsTsfItmList) {
										if (stsTsfItm.getItemId().equals(fulfilDetail.getItem())) {
											log.info("stsTsfItm.getItemId() " + stsTsfItm.getItemId());
											log.info("fulfilDetail.getItem() " + fulfilDetail.getItem());
											log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus() " + stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus());
											if (stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus().value().equals("PENDING")) {
												log.info("Transfer is in pending status");
												log.info("transfer id=" + stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() + "line id=" + stsTsfItm.getLineId() + "Requested qty="
														+ stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)));
												log.info("stsTsfItm.getRequestedQuantity() " + stsTsfItm.getRequestedQuantity());
												log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
												log.info("L_fo_open_qty " + L_fo_open_qty);
												if (stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)).intValue() > 0) {
													log.info("calling SIMSaveTransferRequest when stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)).intValue()>0 ");
													oMSUtilCommons.callSimSaveTransferRequest(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId(), stsTsfItm.getLineId(), stsTsfItm.getItemId(),
															stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)), stsTsfDesc.getSendingStoreId(), stsTsfDesc.getReceivingStoreId());
													log.info("calling RMS when status is pending");
													interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
												} else if (stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)).intValue() <= 0) {
													// added for internal bug pending transfer cancellation
													if (stsTsfItmList.size() > 1) {
														oMSUtilCommons.cancelPendingTransfer(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId(), stsTsfItm.getLineId(), stsTsfItm.getItemId(),
																stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)), stsTsfDesc.getSendingStoreId(),
																stsTsfDesc.getReceivingStoreId());
													}
													if (stsTsfItmList.size() == 1) {
														long transferId = oMSUtilCommons.cancelApprovedTransferRequestForStore2Store(
																new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()), fulfilDetail.getFulfillLoc());
														log.info("cancelled transfer Id  from SIM " + transferId);
														log.info("Tsf_No " + ordCustList.get(0).getTsfNo());
														log.info("fulfillLoc " + fulfilDetail.getFulfillLoc());

													}

													log.info("calling RMS when status is pending");
													interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, stsTsfItm.getRequestedQuantity().intValue(), omsCustOrdItem,
															(extCustOrdNo));

													log.info("calling persistintoOmsSimCancelTsfDetail method when the status is pending");
													log.info("omsCancelId " + omsCancelId);
													log.info("omsCustOrderNo " + omsCustOrderNo);
													log.info("fulfilDetail.getFulfillOrderNo() " + fulfilDetail.getFulfillOrderNo());
													log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() " + stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
													log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
													log.info("item.getItem() " + item.getItem());
													log.info("item.getLineNo() " + item.getLineNo());
													log.info("stsTsfItm.getRequestedQuantity() " + stsTsfItm.getRequestedQuantity());
													oMSUtilCommons.persistintoOmsSimCancelTsfDetail(omsCancelId, omsCustOrderNo, fulfilDetail.getFulfillOrderNo(),
															new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()), fulfilDetail.getSourceLoc(), item.getItem(),
															new BigDecimal(item.getLineNo()), stsTsfItm.getRequestedQuantity());

												}
												// Added for pending transfers
												oMSUtilCommons.detectQuantityfromUnapprovedTransfers(omsCustOrderNo, ordCustList.get(0).getTsfNo(), fulfilDetail.getItem(), fulfilDetail.getSourceLoc(),
														new BigDecimal(L_fo_open_qty));

											} else {
												// Added for pending transfers to delete when it is approved and not deleted
												ArrayList<BigDecimal> transferList = new ArrayList<BigDecimal>();
												transferList.add(ordCustList.get(0).getTsfNo());
												oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList, omsCustOrderNo);
												log.info("Transfer is in approved status");
												log.info("stsTsfItm.getRequestedQuantity() " + stsTsfItm.getRequestedQuantity());
												log.info("item.getCancelQtySuom() " + item.getCancelQtySuom());
												log.info("L_fo_open_qty " + L_fo_open_qty);
												// if(stsTsfItm.getRequestedQuantity().subtract(item.getCancelQtySuom()).intValue()<=0)
												// {
												log.info("calling  persistintoOmsSimCancelTsfDetail method when the status is non-pending");
												log.info("omsCancelId " + omsCancelId);
												log.info("omsCustOrderNo " + omsCustOrderNo);
												log.info("fulfilDetail.getFulfillOrderNo() " + fulfilDetail.getFulfillOrderNo());
												log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() " + stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
												log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
												log.info("item.getItem() " + item.getItem());
												log.info("item.getLineNo() " + item.getLineNo());
												log.info("Math.min(stsTsfItm.getRequestedQuantity().intValue(),new BigDecimal(L_fo_open_qty).intValue()) "
														+ new BigDecimal(Math.min(stsTsfItm.getRequestedQuantity().intValue(), new BigDecimal(L_fo_open_qty).intValue())));
												oMSUtilCommons.persistintoOmsSimCancelTsfDetail(omsCancelId, omsCustOrderNo, fulfilDetail.getFulfillOrderNo(),
														new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()), fulfilDetail.getSourceLoc(), item.getItem(),
														new BigDecimal(item.getLineNo()),
														new BigDecimal(Math.min(stsTsfItm.getRequestedQuantity().intValue(), new BigDecimal(L_fo_open_qty).intValue())));

												// }

												log.info("transfer Id " + new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()));
												log.info("store id " + fulfilDetail.getSourceLoc());
												log.info("L_fo_open_qty " + L_fo_open_qty);
												log.info("fulfilDetail.getFulfillOrderNo() " + fulfilDetail.getFulfillOrderNo());
												log.info("fulfilDetail.getLineNo() " + fulfilDetail.getLineNo());
												log.info("fulfilDetail.getItem() " + fulfilDetail.getItem());
												log.info("fulfilDetail.getFulfillConfQty() " + fulfilDetail.getFulfillConfQty());
												log.info("fulfilDetail.getFulfillCancelQty() " + fulfilDetail.getFulfillCancelQty());
												log.info("fulfilDetail.getFulfillDeliverQty() " + fulfilDetail.getFulfillDeliverQty());
												log.info("fulfilDetail.getFulfillConfQty().subtract(fulfilDetail.getFulfillCancelQty().add(fulfilDetail.getFulfillDeliverQty())).intValue() "
														+ fulfilDetail.getFulfillConfQty().subtract(fulfilDetail.getFulfillCancelQty().add(fulfilDetail.getFulfillDeliverQty())).intValue());
												// if(L_fo_open_qty==fulfilDetail.getFulfillConfQty().subtract(fulfilDetail.getFulfillCancelQty().add(fulfilDetail.getFulfillDeliverQty())).intValue())
												// {
												boolean flag = false;
												try {
													log.info("===========calling cancellingSingleTransferForMultipleItems========");
													log.info("extCustOrdNo " + extCustOrdNo);
													log.info("L_fo_open_qty " + L_fo_open_qty);
													log.info(" omsCustOrderNo " + omsCustOrderNo);
													log.info("ordCustList.get(0).getTsfNo() " + ordCustList.get(0).getTsfNo());
													log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
													log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
													flag = oMSUtilCommons.cancellingSingleTransferForMultipleItems(extCustOrdNo, new BigDecimal(L_fo_open_qty), omsCustOrderNo,
															ordCustList.get(0).getTsfNo(), fulfilDetail.getSourceLoc(), fulfilDetail.getFulfillLoc());
													log.info("flag " + flag);
												} catch (Exception e) {
													log.info("Exception calling cancellingSingleTransferForMultipleItems " + e.getMessage());
												}
												if (flag) {
													log.info("calling RMS and SIM when status is approved and cancelling the quantity completely");
													log.info(" calling cancelApprovedTransferRequestForStore2Store ");
													long transferId = oMSUtilCommons.cancelApprovedTransferRequestForStore2Store(
															new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()), fulfilDetail.getSourceLoc());
													log.info("cancelled transfer Id  from SIM " + transferId);
													log.info("Tsf_No " + ordCustList.get(0).getTsfNo());

													List<Tsfdetail> tsfDetailList = session.getTsfDetailTransferQty(ordCustList.get(0).getTsfNo());
													log.info("tsfDetailList.size() " + tsfDetailList.size());
													for (Tsfdetail tsfdetail : tsfDetailList) {
														BigDecimal cancelQty = tsfdetail.getTsfQty();
														log.info("cancelQty " + cancelQty);
														log.info("calling RMS for line No " + fulfilDetail.getLineNo());
														log.info("Item " + tsfdetail.getItem());
														log.info("SrcLoc " + fulfilDetail.getSourceLoc());
														log.info("fulFillLoc " + fulfilDetail.getFulfillLoc());

														interfacePersistence.callRMSCancelFulfilOrdColRefforApprovedTransfers(input, fulfilDetail, cancelQty, tsfdetail.getItem(), omsCustOrdItem,
																(extCustOrdNo));
														log.info("called RMS when status is approved and cancelling the quantity completely");
													}

												}
												// }
											}

										}
									}

								}
							}

						} catch (Exception e) {
							if (retryForRMSCancelFulfilOrdColRef < 2) {
								retryForRMSCancelFulfilOrdColRef++;
								interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));

							}
						}
						Long FulfillReqQty = Math.min((fulfilDetail.getFulfillConfQty().longValue() - fulfilDetail.getFulfillDeliverQty().longValue() - fulfilDetail.getFulfillCancelQty().longValue()),
								(omsCoCancelItem.getCancelReqQty().longValue() - omsCoCancelItem.getCancelConfQty().longValue()));
						if (FulfillReqQty <= L_fo_open_qty) {
							fulfilDetail.setFulfillCancelQty(new BigDecimal(FulfillReqQty).add(fulfilDetail.getFulfillCancelQty()));
							session.mergeOmsCoFulfillDetail(fulfilDetail);
							omsCoFoCancel.setFoCancelledOty(new BigDecimal(FulfillReqQty));
							session.mergeOmsCoFoCancel(omsCoFoCancel);
						} else {
							if (fulfilDetail.getSourceLocType().equals("WH")) {
								fulfilDetail.setFulfillCancelQty(new BigDecimal(whQty));
								session.mergeOmsCoFulfillDetail(fulfilDetail);
								omsCoFoCancel.setFoCancelledOty(new BigDecimal(whQty));
								session.mergeOmsCoFoCancel(omsCoFoCancel);
							}
						}
						L_cum_cancel_qty = FulfillReqQty + L_cum_cancel_qty;
						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "   L_fo_open_qty=" + L_fo_open_qty);
						omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
						log.info(" mergin this in omscoCanelItem L_cum_cancel_qty" + L_cum_cancel_qty);
						session.mergeOmsCoCancelItem(omsCoCancelItem);
						// updating temp table
						omsCoFoCancel.setWsResponse("C");
						session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
						if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
							log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty=" + L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);
							continue; // Fetching another FO
						} else {
							break;
						}
					} // else if (fulfilDetail.getSourceLocType() == null ||
						// fulfilDetail.getSourceLocType() == "" ||
						// fulfilDetail.getSourceLocType().isEmpty()) {
					if (fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) == 0) {
						boolean simvalue = interfacePersistence.callSIMPingMethod();
						boolean rmsvalue = interfacePersistence.callRMSPingMethod();
						boolean simRevvalue = interfacePersistence.callSIMRevPingMethod();

						log.info("simvalue " + simvalue);
						log.info("rmsvalue " + rmsvalue);
						log.info("simRevvalue " + simRevvalue);
						if (simvalue == false || rmsvalue == false || simRevvalue == false) {
							// throw new
							// SOAPException(OMSUtilCommons.formErrorDescription("SIM/RMS_IS_DOWN", "1",
							String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrderNo);
							Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.applicationDown_Sim_RMS, languageCode);
							if (Boolean.FALSE == omsErrorRecordExist) {
								languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
							}
							throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.applicationDown_Sim_RMS, languageCode, new String[] {}));
						}

						if (simvalue == true && rmsvalue == true && simRevvalue == true) {
							// cancel reservation
							log.info("inside reservation condition");
							// Step 1: Call SIM webservice
							// StoreFulfillmentOrderService/lookupFulfillmentOrderHeaders.
							StrFordHdrColDesc strFordHdrColDesc = interfacePersistence.callSIMLookupFulfillmentOrderHeaders(fulfilDetail, fulilmentOrdNo, extCustOrdNo);
							log.info("size is " + strFordHdrColDesc.getStrFordHdrDesc().size());
							// Step 2 : Call SIM webservice
							// StoreFulfillmentOrderService/readFulfillmentOrderDetails.
							StrFordDesc strFordDesc = interfacePersistence.callSIMReadFulfillmentOrderDetail(strFordHdrColDesc);
							long pickedQty = 0;
							long delievedQty = 0;
							long orderQty = 0;
							long lineId = 0;
							long canceldQty = 0;
							long reserved_qty = 0;
							long actual_reserved_qty = 0;
							long to_be_picekd;
							long reverseQty = 0;
							// code for multiple items
							for (StrFordItm strFordItm : strFordDesc.getStrFordItm()) {
								if (strFordItm.getItemId().equals(item.getItem())) {
									pickedQty = strFordItm.getPickedQty().longValue();
									delievedQty = strFordItm.getDeliveredQty().longValue();
									orderQty = strFordItm.getOrderQty().longValue();
									lineId = strFordItm.getLineId();
									canceldQty = strFordItm.getCanceledQty().longValue();
									reserved_qty = strFordItm.getReservedQty().longValue();
									break;
								}
							}
							// code for 2766
							// check the open delivery, if exist cancel it then do reverse pick
							OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
							FodHdrColDesc fodHdrColDesc = omsUtilCommons.isDeliveryCreated(strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
							log.info("Delivery exist if size >0 otherwise doesn't exist and size is " + fodHdrColDesc.getFodHdrDesc().size());
							if (fodHdrColDesc.getFodHdrDesc().size() > 0) {

								omsUtilCommons.cancelDelivery(fodHdrColDesc);
							}
							BigDecimal qtyToBeCanceledinSIM = BigDecimal.ZERO;
							log.info("cancel qty=" + item.getCancelQtySuom());
							if (pickedQty == delievedQty) {
								log.info("pickedQty == delievedQty");
								actual_reserved_qty = reserved_qty;
								// if (item.getCancelQtySuom().longValue() <= actual_reserved_qty)
								if (L_fo_open_qty < actual_reserved_qty) {
									// qtyToBeCanceledinSIM = item.getCancelQtySuom();
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									log.info("item.getCancelQtySuom().longValue() <= actual_reserved_qty");
									// fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());

								}
								// else if (item.getCancelQtySuom().longValue() > actual_reserved_qty)
								else if (L_fo_open_qty >= actual_reserved_qty) {
									log.info("item.getCancelQtySuom().longValue() > actual_reserved_qty");
									qtyToBeCanceledinSIM = new BigDecimal(actual_reserved_qty);
									// fulfilOrdDtlRef.setCancelQtySuom(new BigDecimal(actual_reserved_qty));
								}
								try {
									// Step 3: Call SIM CancelFulfillmentOrderDetail
									log.info("qtyToBeCanceledinSIM" + qtyToBeCanceledinSIM + "actual_reserved_qty" + actual_reserved_qty);
									interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty, qtyToBeCanceledinSIM,
											extCustOrdNo);
								} catch (Exception e) {
									if (retryCancelFulfillmentOrderDetail1 < 2) {
										retryCancelFulfillmentOrderDetail1++;
										interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty, qtyToBeCanceledinSIM,
												(extCustOrdNo));
									}
								}
							} else if (pickedQty > delievedQty) {
								log.info("pickedQty > delievedQty");
								log.info("Condition 2");
								to_be_picekd = pickedQty - delievedQty;
								actual_reserved_qty = reserved_qty - to_be_picekd;

								// if (item.getCancelQtySuom().longValue() <= actual_reserved_qty)
								if (L_fo_open_qty <= actual_reserved_qty) {
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									// Step 4 :calling cancelFulfillmentOrderDetail
									log.info("calling cancelFulfillmentOrderDetail when pickedQty>delievedQty and Cancel_qty<=actual_reserved_qty");
									try {
										log.info("Order no=" + extCustOrdNo + "Calling SIM cancellation for fulilmentOrdNo=" + fulilmentOrdNo + "qtyToBeCanceledinSIM=" + qtyToBeCanceledinSIM);

										interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty, qtyToBeCanceledinSIM,
												(extCustOrdNo));
									} catch (Exception e) {
										if (retryCancelFulfillmentOrderDetail2 < 2) {
											retryCancelFulfillmentOrderDetail2++;
											interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
													qtyToBeCanceledinSIM, (extCustOrdNo));
										} else {
											throw new SOAPException("Error in calling sim webservice cancelFulfillmentOrderDetail");
										}
									}
								} else if (L_fo_open_qty > actual_reserved_qty) {
									log.info("actual_reserved_qty=" + actual_reserved_qty);
									log.info(" when pickedQty>delievedQty and Cancel_qty>actual_reserved_qty");
									if (L_fo_open_qty == reserved_qty) {
										log.info("item.getCancelQtySuom().longValue() == reserved_qty.....reverseQty=" + reverseQty);
										reverseQty = item.getCancelQtySuom().longValue() - actual_reserved_qty;
									} else if (L_fo_open_qty < reserved_qty) {
										if (to_be_picekd > L_fo_open_qty - actual_reserved_qty) {
											log.info("to_be_picekd > item.getCancelQtySuom().longValue() - actual_reserved_qty.....reverseQty=" + reverseQty);
											reverseQty = L_fo_open_qty - actual_reserved_qty;
										}
									} else if (L_fo_open_qty > reserved_qty) {
										if (to_be_picekd < L_fo_open_qty - actual_reserved_qty) {
											log.info("to_be_picekd < item.getCancelQtySuom().longValue().....reverseQty=" + reverseQty);
											reverseQty = to_be_picekd;
										}
									}

									// calling createReversePick
									log.info("calling createReversePick");
									ForpRef forpRef;
									try {
										// Step 5: Call SIM CreateReversePick
										forpRef = interfacePersistence.callSIMCreateReversePick(lineId, reverseQty, strFordHdrColDesc);
									} catch (Exception e) {
										if (retryCreateReversePick < 2) {
											retryCreateReversePick++;
											log.info("Retrying reverse pick");
											forpRef = interfacePersistence.callSIMCreateReversePick(lineId, reverseQty, strFordHdrColDesc);
										} else {
											throw new SOAPException("Error in calling sim webservice createReversePick");
										}
									}
									// calling confirmReversePick
									log.info("calling confirmReversePick");
									InvocationSuccess invocationSuccess;
									// Step 6: Call SIM ConfirmReversePick
									try {
										invocationSuccess = interfacePersistence.callSIMConfirmReversePick(forpRef);
									} catch (Exception e) {
										if (retryConfirmReversePick < 2) {
											log.info("retrying confirm reversePick");
											invocationSuccess = interfacePersistence.callSIMConfirmReversePick(forpRef);
										} else {
											throw new SOAPException("Error in calling sim webservice confirmReversePick");
										}
									}
									// calling cancelFulfillmentOrderDetail
									log.info("calling cancelFulfillmentOrderDetail");
									log.info("--------");
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									// Step 7: Call Sim CancelFulfillmentOrderDetail
									try {
										log.info("Order no=" + extCustOrdNo + "Calling SIM cancellation for fulilmentOrdNo=" + fulilmentOrdNo + "qtyToBeCanceledinSIM=" + qtyToBeCanceledinSIM);

										interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty, qtyToBeCanceledinSIM,
												(extCustOrdNo));
										log.info("Calling SIM success");
									} catch (Exception e) {
										log.info("callSIMCancelFulfillmentOrderDetail failed" + e);
										if (retryCancelFulfillmentOrderDetail3 < 2) {
											retryCancelFulfillmentOrderDetail3++;
											interfacePersistence.callSIMCancelFulfillmentOrderDetail(input, fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
													qtyToBeCanceledinSIM, (extCustOrdNo));
										} else {
											throw new SOAPException("Error in calling sim webservice cancelFulfillmentOrderDetail");
										}
									}
									// Saving data in oms_revs_pick table in case of reverse pick
									OmsRevsPick omsRevsPick = new OmsRevsPick();
									omsRevsPick.setOmsCustOrdNo(omsCustOrderNo);
									omsRevsPick.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));
									omsRevsPick.setCreateDatetime(new Timestamp(new Date().getTime()));
									omsRevsPick.setItem(item.getItem());
									omsRevsPick.setPuQty(BigDecimal.ZERO);
									omsRevsPick.setLoc(fulfilDetail.getSourceLoc());
									omsRevsPick.setRevPicQty(new BigDecimal(reverseQty));
									session.persistOmsRevsPick(omsRevsPick);

								}

							}
							log.info("actual_reserved_qty in SIM " + actual_reserved_qty);
							log.info("qtyToBeCanceledinSIM in SIM " + qtyToBeCanceledinSIM);
							log.info("L_fo_open_qty " + L_fo_open_qty);
							log.info("L_cum_cancel_qty " + L_cum_cancel_qty);
							log.info("L_CO_ITEM_CANCEL_QTY " + L_CO_ITEM_CANCEL_QTY);

							if (L_fo_open_qty > qtyToBeCanceledinSIM.longValue()) {
								interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, qtyToBeCanceledinSIM.longValue(), omsCustOrdItem, (extCustOrdNo));
								L_fo_open_qty = L_fo_open_qty - qtyToBeCanceledinSIM.longValue();
								log.info("L_fo_open_qty =========" + qtyToBeCanceledinSIM.longValue());
								fulfilDetail.setFulfillCancelQty(new BigDecimal(qtyToBeCanceledinSIM.longValue()).add(fulfilDetail.getFulfillCancelQty()));
								session.mergeOmsCoFulfillDetail(fulfilDetail);
								log.info("Merged omsCoFulfillDetail");
								omsCoFoCancel.setFoCancelledOty(new BigDecimal(qtyToBeCanceledinSIM.longValue()));
								session.mergeOmsCoFoCancel(omsCoFoCancel);
								L_cum_cancel_qty = L_cum_cancel_qty + qtyToBeCanceledinSIM.longValue();
								log.info("L_cum_cancel_qty ==in if==========" + L_cum_cancel_qty);
							} else {
								interfacePersistence.callRMSCancelFulfilOrdColRef(input, fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
								fulfilDetail.setFulfillCancelQty(new BigDecimal(L_fo_open_qty).add(fulfilDetail.getFulfillCancelQty()));
								session.mergeOmsCoFulfillDetail(fulfilDetail);
								log.info("Merged omsCoFulfillDetail");
								omsCoFoCancel.setFoCancelledOty(new BigDecimal(L_fo_open_qty));
								session.mergeOmsCoFoCancel(omsCoFoCancel);
								L_cum_cancel_qty = L_cum_cancel_qty + L_fo_open_qty;
								log.info("L_cum_cancel_qty ==in else==========" + L_cum_cancel_qty);
							}
							log.info("L_cum_cancel_qty " + L_cum_cancel_qty);
							log.info("L_fo_open_qty " + L_fo_open_qty);
							log.info("L_CO_ITEM_CANCEL_QTY " + L_CO_ITEM_CANCEL_QTY);
							log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + " L_fo_open_qty= " + L_fo_open_qty + " L_CO_ITEM_CANCEL_QTY " + L_CO_ITEM_CANCEL_QTY);
							// updating oms_co_fo_cancel table
							L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
							omsCoFoCancel.setWsResponse("C");
							session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
							omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
							session.mergeOmsCoCancelItem(omsCoCancelItem);
							// calling RMS webservice cancelFulfilOrdColRef

							log.info("RMS call success");
							if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {

								continue; // Fetching another FO
							} else if (L_cum_cancel_qty == L_CO_ITEM_CANCEL_QTY) {
								break;
							}
						}
					} // end of ping method
				}
				log.info("Persisting into custOrdLogItem for item =" + item + "  and fulilmentOrdNo" + fulilmentOrdNo);
				omsPersistence.persistCustOrdLogItem(item, fulilmentOrdNo, logSeqNo);
				// code change for lineNo
				omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrderNo, item.getItem(), new BigDecimal(item.getLineNo()));
				log.info("omsCoCancelItem.getCancelConfQty()" + omsCoCancelItem.getCancelConfQty());
				log.info("omsCustOrdItem.getQtyCancelled()" + omsCustOrdItem.getQtyCancelled());
				omsCustOrdItem.setQtyCancelled(omsCoCancelItem.getCancelConfQty().add(omsCustOrdItem.getQtyCancelled()));
				omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
				session.mergeOmsCustOrdItem(omsCustOrdItem);
				log.info("merged OmsCustOrdLogItem");

			}
			if (input.getRefundPreference().equals("CLEARING") && omsCustOrdHead.getPayInStore().equals("N") && (omsCustOrdHead.getOrdPaymentStatus().equals("S"))) {
				omsPersistence.persistOmsRtlogPublishLog(item, omsCancelId, omsCustOrderNo, input);
			}
			log.info("persisted data in OmsRtlogPublishLog");
			// saveCustCancelTender(input);
			CustomerOrderCancelResponseItems responseItem = new CustomerOrderCancelResponseItems();
			// setting reponse
			responseItem.setItem(item.getItem());
			responseItem.setCancelQtySuom(omsCoCancelItem.getCancelConfQty());
			response.getCustomerOrderCancelResponseItems().add(responseItem);
		}

		log.info("omsCustOrderNo " + omsCustOrderNo);
		log.info("omsCancelId " + omsCancelId);
		List<OmsCoCancelItem> omsCoCancelItem1 = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		for (OmsCoCancelItem omsCoCancelItem2 : omsCoCancelItem1) {
			update_ShippingChargeOrNonInvertoryItems(omsCoCancelItem2, omsCancelId, omsCustOrderNo);
		}

	}

	/**
	 * @param input
	 * @throws SOAPException
	 */
	public void checkItemStatus(CustomerOrderCancellation input) throws SOAPException {
		log.info("inside method checkItemStatus");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<OmsCustOrdItem> omsCustOrdItem = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrderNo);
		int i = 0;
		int count = 0;
		for (OmsCustOrdItem list : omsCustOrdItem) {
			log.info("list.getQtyCancelled()=" + list.getQtyCancelled() + "and list.getQtyOrderedSuom() " + list.getQtyOrderedSuom());
			// mani changes old changes if (list.getQtyCancelled().longValue() !=
			// list.getQtyOrderedSuom().longValue())
			if (list.getCumQtyDelivered().longValue() + list.getQtyCancelled().longValue() != list.getQtyOrderedSuom().longValue()) {
				// mani changes
				count++;
				log.info("quant doesnot match i=" + i);
				i = 1;
				break;
			}
			if (i == 0) {
				log.info("All items cancelled" + i);
				// mani changes
				count++;
				OmsPersistence omsPersistence = new OmsPersistence();
				omsPersistence.persistOmsCustOrdLog(omsCancelId, omsCustOrderNo, "CL");
				log.info("persisted data in OmsCustOrdLog with event id =CL");
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);
				log.info("omsCustOrdHead.getStatus() " + omsCustOrdHead.getStatus());
				if (omsCustOrdHead.getStatus().equals("S")) {
					omsCustOrdHead.setCloseDatetime(new Timestamp(new Date().getTime()));
					List<OmsCustOrdTender> omsCustOrdTenderList = session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrderNo);
					if (omsCustOrdHead.getOrdPaymentStatus().equals("P")) {
						for (OmsCustOrdTender omsCustOrdTender : omsCustOrdTenderList) {
							omsCustOrdTender.setPaymentStatusInd("R");
							session.mergeOmsCustOrdTender(omsCustOrdTender);
						}
						omsCustOrdHead.setOrdPaymentStatus("R");
					}
					omsCustOrdHead.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
					// mani changes -- new changes if(count == omsCustOrdItem.size()){
					if (count == omsCustOrdItem.size()) {
						session.mergeOmsCustOrdHead(omsCustOrdHead);
					}
				}
			}
		}
	}

	public CustomerOrderCancellationResponse createResponse(CustomerOrderCancellation input) {
		// response=new CustomerOrderCancellationResponse();

		response.setCancellationId(input.getCancellationId());

		// response.setOmsCustOrdNumber(omsCustOrderNo.longValue());

		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.error("***Response Date_DatatypeConfigurationException" + e);
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		response.setResponseDatetimestamp(now);
		return response;
	}

	public CustomerOrderCancellationResponse generateResponse(CustomerOrderCancellation input, String serviceStatus, String errorCode) throws SOAPException {
		ResponseProcessing responseProcessing = new ResponseProcessing();
		return responseProcessing.generateResponse(input, serviceStatus, errorCode, omsCustOrderNo);
	}

	public CustomerOrderCancellationResponse generateErrorResponseList(CustomerOrderCancellation input, String serviceStatus, List<ErrorListResponse> errorCode) throws SOAPException {
		ResponseProcessing responseProcessing = new ResponseProcessing();
		return responseProcessing.generateErrorResponse(input, serviceStatus, errorCode, omsCustOrderNo);
	}

	public void processRTLog(CustomerOrderCancellation input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// code is not used
		if (input.getRefundPreference().equals("CLEARING")) {
			OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
			session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
		}
	}

	public void notifySiebel(CustomerOrderCancellation input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		if (!input.getApplicationId().equals("SIEBEL_CRM")) {
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);
			InterfacePersistence interfacePersistance = new InterfacePersistence();
			log.info("Calling siebel");
			try {
				// interfacePersistance.callSeibelWebservice(input, omsCustOrdHead.getCustId(),
				// omsCancelId, backorderTreeMap);
				OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderObj = interfacePersistance.getOmsOrderStatusUpdateforCancellation(input, omsCustOrdHead.getCustId(), omsCancelId,
						backorderTreeMap);
				OmsStatusUpdateForReturnPickupCancellation omsStatusUpdateForReturnPickupCancellationWsCall = new OmsStatusUpdateForReturnPickupCancellation();
				omsStatusUpdateForReturnPickupCancellationWsCall.callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(omsOrderStatusUpdateHeaderObj);

			} catch (Exception e) {
				log.error("Error while calling sim");
			}
		}
	}

	public void updateConfirmQty(CustomerOrderCancellation input) throws SOAPException {
		log.info("inside updateConfirmQty ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCancelId " + omsCancelId);
		List<OmsCoCancelItem> omsCoCancelItemList = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		for (OmsCoCancelItem omsCoCancelItem : omsCoCancelItemList) {
			if (omsCoCancelItem.getCancelConfQty().intValue() == 0) {
				log.info("omsCoCancelItem lineno : " + omsCoCancelItem.getLineNo());
				log.info("omsCoCancelItem item : " + omsCoCancelItem.getItem());
				omsCoCancelItem.setCancelConfQty(omsCoCancelItem.getCancelReqQty());
				session.mergeOmsCoCancelItem(omsCoCancelItem);
				log.info("updated confirmQty");
			}
		}
	}

	public void update_ShippingChargeOrNonInvertoryItems(OmsCoCancelItem omsCoCancelItem, BigDecimal omsCancelId, BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("omsCancelId" + omsCancelId + "=============inside update_ShippingChargeOrNonInvertoryItems==========");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsCoCancelItem.getItem(), omsCoCancelItem.getLineNo());
		log.info("omsCancelId" + omsCancelId + "line No " + omsCoCancelItem.getLineNo());
		log.info("omsCancelId" + omsCancelId + "Item " + omsCoCancelItem.getItem());
		BigDecimal itemDept = session.getItemMasterFindDept(omsCoCancelItem.getItem());
		String inventoryIndn = session.getItemMasterFindInventoryInd(omsCoCancelItem.getItem(), itemDept);
		shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
		if (omsCustOrdItem.getLineNo().intValue() == omsCoCancelItem.getLineNo().intValue() && omsCustOrdItem.getItem().equals(omsCoCancelItem.getItem())) {
			if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
				omsCoCancelItem.setCancelConfQty(omsCoCancelItem.getCancelReqQty());
				session.mergeOmsCoCancelItem(omsCoCancelItem);
				log.info("omsCancelId" + omsCancelId + "omsCustOrdItem.getQtyCancelled() " + omsCustOrdItem.getQtyCancelled());
				log.info("omsCancelId" + omsCancelId + "omsCustOrdItem.getCancelReqQty() " + omsCoCancelItem.getCancelReqQty());

				BigDecimal cancelQty = BigDecimal.ZERO;
				if (omsCustOrdItem.getQtyCancelled() == null) {
					omsCustOrdItem.setQtyCancelled(omsCoCancelItem.getCancelReqQty());
				} else {
					cancelQty = omsCustOrdItem.getQtyCancelled().add(omsCoCancelItem.getCancelReqQty());
					log.info("omsCancelId" + omsCancelId + "cancelQty " + cancelQty);
					omsCustOrdItem.setQtyCancelled(cancelQty);
				}
				omsCustOrdItem = session.mergeOmsCustOrdItem(omsCustOrdItem);
				log.info("updated the cancelQty for lineNo " + omsCustOrdItem.getLineNo() + "qty is " + omsCustOrdItem.getQtyCancelled());

			}
		}

	}

	// validation for shippingcharge

	public OmsCustOrdHead cancellationforShippingCharge(CustomerOrderCancellation input) throws SOAPException {
		checkNonInventoryItem(input);
		return checkShippingChargeItem(input);
	} // end of for cancellation items

	public List<OmsCoFulfillDetail> shuffleFulfillOrderNo(List<OmsCoFulfillDetail> fulfillDetailList) {

		log.info("prcessing method shuffleFulfillOrderNo");
		List<OmsCoFulfillDetail> newFulfillDetailList = new ArrayList<OmsCoFulfillDetail>();
		// OmsCoFulfillDetail poFulfillDetail = null;
		List<OmsCoFulfillDetail> poFulfillDetail = new ArrayList<OmsCoFulfillDetail>();
		for (OmsCoFulfillDetail omsCoFulfillDetail : fulfillDetailList) {
			if (omsCoFulfillDetail.getSourceLocType().equals("SU") == true) {
				poFulfillDetail.add(omsCoFulfillDetail);
				// poFulfillDetail = omsCoFulfillDetail;

				// break;
			}

		}
		// log.info("Adding PO to the first of the list with fulfill order no=" +
		// poFulfillDetail.getFulfillOrderNo());
		newFulfillDetailList.addAll(poFulfillDetail);
		for (OmsCoFulfillDetail omsCoFulfillDetail : fulfillDetailList) {
			if (omsCoFulfillDetail.getSourceLocType().equals("SU") == false) {
				log.info("Adding fulfilment detail with fufill order no=" + omsCoFulfillDetail.getFulfillOrderNo());
				newFulfillDetailList.add(omsCoFulfillDetail);
			}
		}
		return newFulfillDetailList;
	}

	public void checkNonInventoryItem(CustomerOrderCancellation input) throws SOAPException {
		log.info("Check only for non-inventory items");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(input.getCustOrderNo(), "S");
		log.info("omsCustOrdHead.getOmsCustOrdNo() " + omsCustOrdHead.getOmsCustOrdNo());

		Map<BigDecimal, BigDecimal> inventoryItemMap = new HashMap<BigDecimal, BigDecimal>();
		String shippingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
		String inventoryIndicator = "N";
		BigDecimal itemDept = null;
		boolean cancellationNonIventoryCheck = false;
		List<CustomerOrderCancellationItems> cancellList = input.getCancellationItems();
		for (CustomerOrderCancellationItems customerOrderCancellation : cancellList) {
			itemDept = session.getItemMasterFindDept(customerOrderCancellation.getItem());
			inventoryIndicator = session.getItemMasterFindInventoryInd(customerOrderCancellation.getItem(), itemDept);
			log.info("inventoryIndicator " + inventoryIndicator);
			if (inventoryIndicator.equals("N") && !itemDept.toString().equals(shippingChargeDept.toString())) {
				List<OmsCustOrdItem> omsCustOrdItem = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), new BigDecimal(customerOrderCancellation.getLineNo()));
				log.info("is link no present in the input request " + cancellList.contains(omsCustOrdItem.get(0).getLineLinkNo()));
				if (omsCustOrdItem.get(0).getLineLinkNo() == null) {
					nonInventoryItemMap.put(new BigDecimal(customerOrderCancellation.getLineNo()), customerOrderCancellation.getCancelQtySuom());
				}

			} else if (!itemDept.toString().equals(shippingChargeDept.toString())) {
				inventoryItemMap.put(new BigDecimal(customerOrderCancellation.getLineNo()), customerOrderCancellation.getCancelQtySuom());
			}
		}
		log.info("nonInventoryItemMap.size() " + nonInventoryItemMap.size());
		log.info("inventoryItemMap.size() " + inventoryItemMap.size());
		if (inventoryItemMap == null || inventoryItemMap.size() == 0) {
			List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			log.info("omsCustOrdItemList.size() " + omsCustOrdItemList.size());
			for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
				cancellationNonIventoryCheck = false;
				itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
				inventoryIndicator = session.getItemMasterFindInventoryInd(omsCustOrdItem.getItem(), itemDept);
				log.info("Main Item inventoryIndicator " + inventoryIndicator);
				log.info("shippingChargeDept " + shippingChargeDept);
				log.info("itemDept " + itemDept);
				if (inventoryIndicator.equals("Y") && !itemDept.toString().equals(shippingChargeDept.toString())) {
					log.info("omsCustOrdItem.getQtyOrderedSuom() " + omsCustOrdItem.getQtyOrderedSuom());
					log.info("omsCustOrdItem.getCumQtyDelivered() " + omsCustOrdItem.getCumQtyDelivered());
					log.info("omsCustOrdItem.getQtyCancelled() " + omsCustOrdItem.getQtyCancelled());
					if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue())) {
						log.info("Still items are there to cancel and its not present in the cancellation input");
						cancellationNonIventoryCheck = true;
						break;
					}
				}
			}
		} else if (nonInventoryItemMap.size() > 0) {
			for (BigDecimal lineNo : inventoryItemMap.keySet()) {
				cancellationNonIventoryCheck = false;
				BigDecimal cancelQty = inventoryItemMap.get(lineNo);
				List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), lineNo);
				if (cancelQty.intValue() + (omsCustOrdItemList.get(0).getCumQtyDelivered().intValue() + omsCustOrdItemList.get(0).getQtyCancelled().intValue()) != omsCustOrdItemList.get(0)
						.getQtyOrderedSuom().intValue()) {
					log.info("Main item qty is less in input so cannot cancel the shipping charge item");
					cancellationNonIventoryCheck = true;
					break;
				}
			}

			if (cancellationNonIventoryCheck == false) {
				List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
				for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
					cancellationNonIventoryCheck = false;
					itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
					log.info("omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
					log.info("shippingChargeDept " + shippingChargeDept);
					log.info("itemDept " + itemDept);
					inventoryIndicator = session.getItemMasterFindInventoryInd(omsCustOrdItem.getItem(), itemDept);
					if (!inventoryItemMap.containsKey(omsCustOrdItem.getLineNo()) && itemDept.toString().equals(shippingChargeDept.toString()) == false && inventoryIndicator.equals("Y")) {
						if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue())) {
							log.info("its failing");
							log.info("Still items are there to cancel and its not present in the cancellation input");
							cancellationNonIventoryCheck = true;
							break;
						}

					}
				}
			}
		}

		if (cancellationNonIventoryCheck == false) {
			log.info("Allow to cancel the non-inventory item since all the main items are cancelled");
		} else {
			String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdHead.getOmsCustOrdNo());
			Boolean omsErrorCodeRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cannot_Cancel_ShippingCharge_item, languageCode);
			if (Boolean.FALSE == omsErrorCodeRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cannot_Cancel_ShippingCharge_item, languageCode, new String[] {}));
		}
	}

	public OmsCustOrdHead checkShippingChargeItem(CustomerOrderCancellation input) throws SOAPException {
		log.info("Check only for ShippingCharge items");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(input.getCustOrderNo(), "S");
		log.info("omsCustOrdHead.getOmsCustOrdNo() " + omsCustOrdHead.getOmsCustOrdNo());
		Map<BigDecimal, BigDecimal> shippingChargeItemMap = new HashMap<BigDecimal, BigDecimal>();
		Map<BigDecimal, BigDecimal> mainItemMap = new HashMap<BigDecimal, BigDecimal>();
		Map<BigDecimal, BigDecimal> ItemMap = new HashMap<BigDecimal, BigDecimal>();

		String shippingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
		String inventoryIndicator = null;
		BigDecimal itemDept = null;
		boolean shippingChargeItemcheck = false;
		List<CustomerOrderCancellationItems> cancellList = input.getCancellationItems();
		for (CustomerOrderCancellationItems customerOrderCancellation : cancellList) {
			itemDept = session.getItemMasterFindDept(customerOrderCancellation.getItem());
			inventoryIndicator = session.getItemMasterFindInventoryInd(customerOrderCancellation.getItem(), itemDept);
			log.info("inventoryIndicator " + inventoryIndicator);
			if (itemDept.toString().equals(shippingChargeDept.toString()) == true) {
				shippingChargeItemMap.put(new BigDecimal(customerOrderCancellation.getLineNo()), customerOrderCancellation.getCancelQtySuom());
			} else {
				mainItemMap.put(new BigDecimal(customerOrderCancellation.getLineNo()), customerOrderCancellation.getCancelQtySuom());
			}
		}
		if (mainItemMap == null || mainItemMap.size() == 0) {
			List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
				shippingChargeItemcheck = false;
				itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
				log.info("shippingChargeDept " + shippingChargeDept);
				log.info("itemDept " + itemDept);
				inventoryIndicator = session.getItemMasterFindInventoryInd(omsCustOrdItem.getItem(), itemDept);

				if (!itemDept.toString().equals(shippingChargeDept.toString()) == true) {
					log.info("omsCustOrdItem.getQtyOrderedSuom() " + omsCustOrdItem.getQtyOrderedSuom());
					log.info("omsCustOrdItem.getCumQtyDelivered() " + omsCustOrdItem.getCumQtyDelivered());
					log.info("omsCustOrdItem.getQtyCancelled() " + omsCustOrdItem.getQtyCancelled());
					if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue())) {
						log.info("Still items are there to cancel and its not present in the cancellation input");
						shippingChargeItemcheck = true;
						break;
					}
				}

			}
		} else {
			if (shippingChargeItemMap.size() > 0) {
				ItemMap.putAll(shippingChargeItemMap);
				ItemMap.putAll(mainItemMap);
				log.info("ItemMap.keySet()=====" + ItemMap.keySet());
				for (BigDecimal lineNo : mainItemMap.keySet()) {
					shippingChargeItemcheck = false;
					BigDecimal cancelQty = mainItemMap.get(lineNo);
					List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), lineNo);
					if (cancelQty.intValue() + (omsCustOrdItemList.get(0).getCumQtyDelivered().intValue() + omsCustOrdItemList.get(0).getQtyCancelled().intValue()) != omsCustOrdItemList.get(0)
							.getQtyOrderedSuom().intValue()) {
						log.info("Main item qty is less in input so cannot cancel the shipping charge item");
						shippingChargeItemcheck = true;
						break;
					}
				}
				if (shippingChargeItemcheck == false) {
					List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
					for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
						shippingChargeItemcheck = false;
						if (!ItemMap.containsKey(omsCustOrdItem.getLineNo())) {
							if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue())) {
								log.info("Still items are there to cancel and its not present in the cancellation input");
								shippingChargeItemcheck = true;
								break;
							}
						}
					}
				}
			}

		}
		log.info("shippingChargeItemcheck " + shippingChargeItemcheck);
		if (shippingChargeItemcheck == false) {
			log.info("Allow to cancel the non-inventory item since all the main items are cancelled");
		} else {
			String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdHead.getOmsCustOrdNo());
			Boolean omsErrorCodeRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Cannot_Cancel_ShippingCharge_item, languageCode);
			if (Boolean.FALSE == omsErrorCodeRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Cannot_Cancel_ShippingCharge_item, languageCode, new String[] {}));
		}
		return omsCustOrdHead;
	}

	public void saveEInvoicingReq(CustomerOrderCancellation input) {
		Connection connection = null;
		PreparedStatement statement = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			statement = connection.prepareStatement("INSERT INTO XX_EINV_CAN_RET_OMS(UNIQUE_INV_ID, CUST_ORDER_NO, TRAN_TYPE, REFUND_OPTION, INSERT_DATE, PROCESS_FLAG, CAN_RET_ID, STORE_ID) VALUES(?, ?, 'ORC', ?, SYSDATE, 'N', ?, ?)");
			statement.setString(1, dateFormat.format(input.getCancellationDate().toGregorianCalendar().getTime()) + input.getCancellationRequestorId() + omsCancelId);
			statement.setString(2, input.getCustOrderNo());
			statement.setString(3, input.getRefundPreference());
			statement.setBigDecimal(4, omsCancelId);
			statement.setString(5, input.getCancellationRequestorId());
			statement.executeUpdate();
			try {
				statement.close();
			} catch (Exception e) {
				// EAT Exception
			}
			statement = connection.prepareStatement("UPDATE OMS_CO_CANCEL_HEAD SET EIN_PROCESS_FLAG = ? WHERE OMS_CANCEL_ID = ?");
			statement.setString(1, "Y");
			statement.setBigDecimal(2, omsCancelId);
			statement.executeUpdate();
		} catch (Exception e) {
			log.error("Error while insert request into XX_EINV_CAN_RET_OMS", e);
		}
		finally {
			OMSUtil.closeDBConnection(connection, statement, null);
		}
	}
} // end of class
