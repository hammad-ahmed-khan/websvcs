package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItmRtn;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.ejb.OmsSystemParameters;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlActCodeDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlRsnCodeDesc;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsPortType;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsService;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

public class RMAGenerationBean {
	public RMAGenerationBean() {
		super();
	}

	BigDecimal rma;
	Properties props;
	BigDecimal omsCustOrdNo;
	int maxNoOfRetry = 2;
	private final static Logger log = Logger.getLogger(RMAGenerationBean.class.getName());

	private static final String SELECT_JOOD_ORD_TRAN_SQL = "SELECT (SELECT MAX(JM.MEMBERSHIP_ID) FROM XX_JOOD_MEMBERSHIP JM WHERE J.MOBILE_NO = JM.MOBILE_NO) MEMBERSHIP_ID, H.SOURCE, I.ITEM, I.IINE_NO, "
			+ "I.QTY, I.UNIT_RETAIL, I.UNIT_DISCOUNT_AMOUNT, I.JOOD_DISCOUNT, NVL(I.EARNED_CB,0) EARNED_CB, COALESCE(CASE WHEN UPPER(H.SOURCE) = 'E-COMMERCE' THEN "
			+ "NVL(R.REDEEMED_CB, 0) END, NVL(I.REDEEMED_CB, 0)) AS REDEEMED_CB, NVL(I.USED_CB, 0) USED_CB, I.TOTAL_SELLING_RETAIL, H.JOOD_PROGRAM FROM XX_JOOD_TRANSACTIONS_HEAD H "
			+ "JOIN XX_JOOD_TRANSACTIONS_DTL I ON H.TRAN_SEQ_NO = I.TRAN_SEQ_NO LEFT JOIN XX_JOOD_TRANSACTIONS_HEAD R ON R.ORDER_NUMBER = H.ORDER_NUMBER AND  "
			+ "UPPER(R.TRAN_TYPE) = 'REDEEM' JOIN XX_JOOD_MEMBERSHIP J ON H.MEMBERSHIP_ID = J.MEMBERSHIP_ID WHERE H.ORDER_NUMBER = ? AND I.ITEM IN (items) "
			+ "AND I.IINE_NO IN (lineNos) AND UPPER(H.TRAN_TYPE) IN ('SALE','SALEANDREDEEM')";

	String language = "1";

	public boolean checkDuplicate(CustomerOrderRMA input) throws SOAPException {
		log.info("checkDuplicate started");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		boolean b = false;
		List<BigDecimal> list = null;
		try {
			list = session.getOmsRmaReqFindRmaId(omsCustOrdNo, (input.getRmaRequestId()));
		} catch (Exception e) {
			log.error(" error in getOmsRmaReqFindRmaId" + e);
		}
		if (list.size() != 0) {
			log.error("Record already exist for customer order no:" + input.getCustomerOrderNo() + "for rma request id:" + input.getRmaRequestId());
			// String errString = OMSUtilCommons.formErrorDescription("RMA_EXIST", "1", new
			// String[] {input.getCustomerOrderNo(),input.getRmaRequestId()});
			String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
			Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Rma_Already_Exist, languageCode);
			if (Boolean.FALSE == omsErrorCodesRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Rma_Already_Exist, languageCode, new String[] { input.getCustomerOrderNo(), input.getRmaRequestId() });
			log.error(errString);
			throw new SOAPException(errString);
		} else {
			log.info(" No record already exist for customer order no:" + input.getCustomerOrderNo() + "for rma request id:" + input.getRmaRequestId());
			b = true;
		}
		log.info("checkDuplicate completed");
		return b;
	}

	public String validate(CustomerOrderRMA input) throws SOAPException {
		log.info("Validation started");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> warehouses = null;
		String subCustomerOrderNo = "1";
		if (input.getSubCustomerOrderNo() != null) {
			subCustomerOrderNo = input.getSubCustomerOrderNo();
		}
		try {
			// List<BigDecimal> findPhyWH = session.getVWhFindPhyWH(new
			// BigDecimal(input.getPhysicalWh()));
			warehouses = session.getVWhFindPhyWH(new BigDecimal(input.getPhysicalWh()));
		} catch (Exception e) {
			log.error("physical wh " + e.getCause());
		}
		if (warehouses.size() == 0) {
			// Physical warehouse does not exist.
			// String errString = OMSUtilCommons.formErrorDescription("PHY_WH_UNAVAILABLE",
			// "1", new String[] { String.valueOf(input.getPhysicalWh())});
			String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());

			Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.phyiscal_WareHouse_Unavailable, languageCode);
			if (Boolean.FALSE == omsErrorCodesRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.phyiscal_WareHouse_Unavailable, languageCode, new String[] { String.valueOf(input.getPhysicalWh()) });
			log.error(errString);
			throw new SOAPException(errString);
		}
		List<BigDecimal> omsCustordnoList = session.getOmsCustOrdHeadFindByExternalCustOrdNo(input.getCustomerOrderNo(), subCustomerOrderNo, input.getApplicationId());
		if (omsCustordnoList.size() <= 0) {
			// "customer order no does not exist in cust_ord_head table.
			// String errString = OMSUtilCommons.formErrorDescription("INVALID_CO_INPUT",
			// "1", new String[]
			// {input.getCustomerOrderNo(),subCustomerOrderNo,input.getApplicationId()});
			String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
			Boolean omsErrorCodeRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Customer_Order_Exist, languageCode);
			if (Boolean.FALSE == omsErrorCodeRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Customer_Order_Exist, languageCode,
					new String[] { input.getCustomerOrderNo(), subCustomerOrderNo, input.getApplicationId() });
			log.error(errString);
			throw new SOAPException(errString);
		}
		omsCustOrdNo = omsCustordnoList.get(0);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		language = omsCustOrdHead.getCustomerLang();

		if (input.getReturnDate().toGregorianCalendar().getTime().compareTo(new Date()) <= 0) {
			log.error("invalid return date. Return date should be greater than today's date");
			// String errString =
			// OMSUtilCommons.formErrorDescription("INVALID_RMA_RTN_DATE", "1", new
			// String[]{ input.getReturnDate().toString()});
			String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
			Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Invalid_Rma_ReturnDate, languageCode);
			if (Boolean.FALSE == omsErrorRecordExist) {
				languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Invalid_Rma_ReturnDate, languageCode, new String[] { input.getReturnDate().toString() });
			log.error(errString);
			throw new SOAPException(errString);
			// throw new SOAPException("INVALID_RMA_RTN_DATE");
		}

		BigDecimal omsOrposCustOrdNo = null;
		try {
			omsOrposCustOrdNo = session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(input.getCustomerOrderNo());

		} catch (Exception e) {

		}
		log.info("<------  Processin RMA Items---------->");
		List<CustomerOrderRMAItem> itemList = input.getCustomerOrderRMAItem();
		OmsCustOrdItem item = null;
		for (CustomerOrderRMAItem items : itemList) {
			// checking for shipping charge Item
//			log.info("Checking whether the item is a shipping charge item");
//			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
//			log.info("shipingChargeDept " + shipingChargeDept);
//			log.info("items.getItem() " + items.getItem());
//			BigDecimal itemDept = session.getItemMasterFindDept(items.getItem());
//			log.info("itemDept " + itemDept);
//			log.info("<-------------------------------------------------------------->");
//			if (shipingChargeDept.equals(itemDept.toString()) == true) {
//				log.info("contains a shipping charge Item");
//				throw new SOAPException("Requested Item " + items.getItem() + " Contains a shipping charge Item cannot create RMA");
//			}
			try {
				// changed the line number code for looping through CustomerOrderRMAItem
				log.info("Finding item for the given customer Order---->" + omsCustOrdNo + "<---->");
				item = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, items.getItem(), new BigDecimal(items.getLineNo()));
			} catch (Exception e) {
				// "Invalid item cannot process rma of item whih is not in customer order."
				// String errString = OMSUtilCommons.formErrorDescription("INVALID_RMA_ITEM",
				// "1", new String[] {item.getItem()});
				log.info("<--- Inside catch Block ------>" + e);
				String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
				log.info("<---- Language Code--->" + languageCode + "<--->");
				Boolean omsErrorCodesRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Invalid_Rma_item, languageCode);
				log.info("<----Checking Validation Conditon --------->");
				if (Boolean.FALSE == omsErrorCodesRecordExist) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Invalid_Rma_item, languageCode, new String[] { items.getItem() });
				log.error(errString);
				throw new SOAPException(errString);
			}
			// changed the query for finding the qty delivered based on the
			// omsCustOrdNo,item and lineNo.
			int deliverdQty = session.getOmsCustOrdItemFindDeilverdQuantity(omsCustOrdNo, items.getItem(), new BigDecimal(items.getLineNo())).intValue();
			log.info("deliverdQty=" + deliverdQty);
			if (items.getReturnQtySuom().intValue() == 0 || items.getReturnQtySuom().intValue() > deliverdQty) {
				log.info("INVALID_RTN_QTY");
				// Invalid return quantity for item @@value1.You can return item which is
				// deliverd.
				// String errString = OMSUtilCommons.formErrorDescription("INVALID_RTN_QTY",
				// "1", new String[] { item.getItem()});
				String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
				Boolean omsErrorCodeRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Invalid_Return_qty, languageCode);
				if (Boolean.FALSE == omsErrorCodeRecordExist) {
					languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
				}
				String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Invalid_Return_qty, languageCode, new String[] { item.getItem() });
				log.error(errString);
				throw new SOAPException(errString);
			}
//            //checking for shipping charge Item
//            log.info("Checking whether the item is a shipping charge item");
//            String shipingChargeDept= session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
//            log.info("shipingChargeDept "+shipingChargeDept);
//            log.info("items.getItem() "+items.getItem());
//            BigDecimal itemDept= session.getItemMasterFindDept(items.getItem());
//            log.info("itemDept "+itemDept);
//            if(shipingChargeDept.equals(itemDept.toString())==true) 
//            {
//                 log.info("contains a shipping charge Item");
//                 throw new SOAPException("Contains a shipping charge Item"); 
//            }
//        
			BigDecimal returnQty = BigDecimal.ZERO;
			if (omsOrposCustOrdNo != null) {
				log.info("Returned from ORPOS");
				List<OmsOrposCustOrdItmRtn> omsOrposCustOrdItmRtnList = session.getOmsOrposCustOrdItmRtnFindByOmsOrposCustOrdIdAndLineItemNo(omsOrposCustOrdNo, new BigDecimal(items.getLineNo()));

				for (OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn : omsOrposCustOrdItmRtnList) {
					returnQty = returnQty.add(omsOrposCustOrdItmRtn.getReturnedQuantity());

				}
				/*
				 * try { OmsOrposCustOrdItm omsOrposCustOrdItm=
				 * session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(
				 * omsOrposCustOrdNo, items.getItem(), new BigDecimal(items.getLineNo()));
				 * returnQty=returnQty.add(omsOrposCustOrdItm.getReturnedQuantity());
				 * }catch(Exception e) {
				 * 
				 * }
				 */

				log.info("returnQty=" + returnQty);
				if (returnQty.intValue() >= deliverdQty) {
					log.info("ITEM_ALDY_RETUNED");
					// Invalid return quantity for item @@value1.You can return item which is
					// deliverd.
					// String errString = OMSUtilCommons.formErrorDescription("ITEM_ALDY_RETUNED",
					// "1", new String[] { item.getItem()});
					String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
					Boolean omsErrorRecordExist = OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.Item_already_Returned, languageCode);
					if (Boolean.FALSE == omsErrorRecordExist) {
						languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
					}
					String errString = OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.Item_already_Returned, languageCode, new String[] { item.getItem() });
					log.error(errString);
					throw new SOAPException(errString);
				}

			}

		}

		log.info("Validation completed");
		return omsCustOrdHead.getOrderRequestorId().toString();
	}

	public void saveRMA(CustomerOrderRMA input) throws SOAPException {
		log.info("saveRMA started");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsRmaReq omsRmaReq = new OmsRmaReq();
		omsRmaReq.setCustOrderNo(input.getCustomerOrderNo());
		if (input.getSubCustomerOrderNo() != null) {
			omsRmaReq.setSubCustOrderNo(input.getSubCustomerOrderNo());
		} else {
			omsRmaReq.setSubCustOrderNo("1");
		}
		omsRmaReq.setOmsCustOrdNo(omsCustOrdNo);
		omsRmaReq.setRmaReqId(input.getRmaRequestId());
		omsRmaReq.setReturnLocId(new BigDecimal(input.getPhysicalWh()));
		omsRmaReq.setRestockAmount(BigDecimal.ZERO);
		omsRmaReq.setRestockPercentage(BigDecimal.ZERO);
		omsRmaReq.setOrigRefundAmt(input.getRefundAmount());
		omsRmaReq.setComments(input.getComments());
		omsRmaReq.setCreateDatetime(new Timestamp(new Date().getTime()));
		omsRmaReq.setStatus("N");
		omsRmaReq.setReturnDateTime(new Timestamp(input.getReturnDate().toGregorianCalendar().getTimeInMillis()));
		if (input.getRefundPreference().equals("CLEARING")) {
			omsRmaReq.setRefundCompltInd("Y");
		} else {
			omsRmaReq.setRefundCompltInd("N");
		}
		omsRmaReq.setRefundOption(input.getRefundPreference());
		omsRmaReq.setRefundAmount(input.getRefundAmount());
		omsRmaReq.setReturnStatus("N");// N=not completed
		omsRmaReq.setReasonCode(input.getReasonCode());
		omsRmaReq.setReason(input.getReason());
		session.persistOmsRmaReq(omsRmaReq);
	}

	public void saveRMAItems(CustomerOrderRMA input) throws SOAPException {
		log.info("saveRMAItems started");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<BigDecimal> rmaId = session.getOmsRmaReqFindRmaId(omsCustOrdNo, (input.getRmaRequestId()));
		OmsRmaReqItem omsRmaReqItem = new OmsRmaReqItem();
		rma = rmaId.get(0);
		List<CustomerOrderRMAItem> itemList = input.getCustomerOrderRMAItem();

		// int i=1;
		for (CustomerOrderRMAItem items : itemList) {
			// changed the code for line number. changed the query for finding the
			// deliveredQty based on omsCustOrdNo,item and lineNo.

			/*
			 * if (deliverdQty.compareTo(items.getReturnQtySuom()) == 1) { //For
			 * item @@value1 ,omly @@value2 is delieverd.Please enter valid return quantity.
			 * log.
			 * warn("Return quantity should be less than or equal to delieverd quantity.");
			 * //throw new
			 * SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty(
			 * "INVALID_RMA_RTN_QTY"))); String errString =
			 * OMSUtilCommons.formErrorDescription("INVALID_RMA_RTN_QTY", "1", new String[]
			 * {}); log.error(errString); throw new SOAPException(errString); }
			 */
			omsRmaReqItem.setItem(items.getItem());
			omsRmaReqItem.setReceivedQty(BigDecimal.ZERO);
			// changed the code for lineNo, the value comes from the input XSD.
			omsRmaReqItem.setLineNo(new BigDecimal(items.getLineNo()));
			omsRmaReqItem.setOrigRmaQty(items.getReturnQtySuom());
			omsRmaReqItem.setItemComments(items.getItemComments());
			omsRmaReqItem.setRmaQty(items.getReturnQtySuom());
			omsRmaReqItem.setRmaId(rmaId.get(0));
			omsRmaReqItem.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsRmaReqItem.setStatus("A");
			session.persistOmsRmaReqItem(omsRmaReqItem);
			// i++;
		}
	}

	/**
	 * @param input
	 * @throws SOAPException
	 * @throws IllegalArgumentWSFaultException
	 * @throws IllegalStateWSFaultException
	 * @throws ValidationWSFaultException
	 */
	public void callRWMSWebService(CustomerOrderRMA input, String reqStoreId) throws SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		log.info("callRWMSWebService started");
		PendingReturnsService pendingReturnsService = new PendingReturnsService();
		PendingReturnsPortType pendingReturnsPortType = pendingReturnsService.getPendingReturnsPort();
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String reasonCode = "";
		String reason_description = "";
		String actionCode = "";
		String inventoryInd = null;
		List<OmsSystemParameters> sysParams = session.getOmsSystemParametersFindByParameterId("RMA_REQ");

		for (OmsSystemParameters parameter : sysParams) {
			if (parameter.getParameterName().equals("REASON_CODE")) {
				reasonCode = parameter.getParameterValue();
			}
			if (parameter.getParameterName().equals("REASON_DESCRIPTION")) {
				reason_description = parameter.getParameterValue();
			}
			if (parameter.getParameterName().equals("ACTION_CODE")) {
				actionCode = parameter.getParameterValue();
			}
		}
		PendRtrnDesc pendRtrnDesc = new PendRtrnDesc();
		pendRtrnDesc.setCustOrderNbr(input.getCustomerOrderNo());
		pendRtrnDesc.setPhysicalWh(input.getPhysicalWh());
		pendRtrnDesc.setRmaNbr(rma.toString());
		pendRtrnDesc.setExpectedReceipt(input.getReturnDate());
		pendRtrnDesc.setSpecialInstructions("OMS Generated RMA");
		List<PendRtrnDtlDesc> pendRtrnDtlDescList = pendRtrnDesc.getPendRtrnDtlDesc();

		List<CustomerOrderRMAItem> itemList = input.getCustomerOrderRMAItem();
		try {
			inventoryInd = getInventoryInd(itemList.get(0).getItem());
			log.info("Inventory Indicator -- " + inventoryInd);
		} catch (Exception e) {
			log.error("Error fetching inventory indicator: " + e.getMessage(), e);
		}
		List<OmsRmaReqItem> omsRmaReqItemList = session.getOmsRmaReqItemFindByRmaId(rma);
		for (CustomerOrderRMAItem items : itemList) {
			if (!"N".equals(inventoryInd)) {
				PendRtrnDtlDesc pendRtrnDtlDesc = new PendRtrnDtlDesc();
				pendRtrnDtlDesc.setItemId(items.getItem());
				// changed the code for line number. The values for line number comes from the
				// input XSD
				// pendRtrnDtlDesc.setLineItemNbr(items.getLineNo());
				pendRtrnDtlDesc.setLineItemNbr(1);
				// revertring back the lone no to hard coded 1 for defect 2779
				pendRtrnDtlDesc.setExpectedUnitQty(items.getReturnQtySuom());
				PendRtrnDtlRsnCodeDesc pendRtrnDtlRsnCodeDesc = new PendRtrnDtlRsnCodeDesc();
				pendRtrnDtlRsnCodeDesc.setReasonCode(reasonCode);
				pendRtrnDtlRsnCodeDesc.setDescription(reason_description);
				pendRtrnDtlDesc.getPendRtrnDtlRsnCodeDesc().add(pendRtrnDtlRsnCodeDesc);
				PendRtrnDtlActCodeDesc pendRtrnDtlActCodeDesc = new PendRtrnDtlActCodeDesc();
				pendRtrnDtlActCodeDesc.setActionCode(actionCode);
				// pendRtrnDtlActCodeDesc.setUnitQty(value);
				pendRtrnDtlActCodeDesc.setComments(items.getItemComments());
				pendRtrnDtlDesc.getPendRtrnDtlActCodeDesc().add(pendRtrnDtlActCodeDesc);
				pendRtrnDtlDescList.add(pendRtrnDtlDesc);
			}
		}
		InvocationSuccess invocationsuccess;

		try {
			if (!"N".equals(inventoryInd)) {
				invocationsuccess = pendingReturnsPortType.pendReturnDtlCreate(pendRtrnDesc);
			}
//			log.info("invocationsuccess.getSuccessMessage() " + invocationsuccess.getSuccessMessage());
			log.info("omsCustOrdNo " + omsCustOrdNo);
			log.info("input.getRmaRequestId() " + input.getRmaRequestId());
			OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByOmsCustOrdNoAndRmaId(omsCustOrdNo, input.getRmaRequestId());
			omsRmaReq.setStatus("S");
			session.mergeOmsRmaReq(omsRmaReq);
			for (OmsRmaReqItem rmaItem : omsRmaReqItemList) {

				OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, rmaItem.getItem(), (rmaItem.getLineNo()));
				log.info("line-no" + omsCustOrdItem.getLineNo() + "item" + omsCustOrdItem.getItem());
				rmaItem.setStatus("S");
				session.mergeOmsRmaReqItem(rmaItem);
				log.info("checking whether the omsRmaReq.getStatus().equals(\"S\")  " + omsRmaReq.getStatus());
				if (omsRmaReq.getStatus().equals("S")) {
					log.info("inside status is equal to S");
					if (omsCustOrdItem.getQtyReturned() != null) {
						log.info("omsCustOrdItem.getQtyReturned() is not null " + omsCustOrdItem.getQtyReturned());
						omsCustOrdItem.setQtyReturned(rmaItem.getRmaQty().add(omsCustOrdItem.getQtyReturned()));
						omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
						session.mergeOmsCustOrdItem(omsCustOrdItem);
					} else {
						log.info("omsCustOrdItem.getQtyReturned() is null");
						omsCustOrdItem.setQtyReturned(rmaItem.getRmaQty());
						omsCustOrdItem.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
						session.mergeOmsCustOrdItem(omsCustOrdItem);
					}
					log.info("persisted the return qty into OmsCustOrdItem");

				}
			}
			saveJoodTranscation(input, reqStoreId);
		} catch (IllegalArgumentWSFaultException e) {
			log.error(e);
			if (maxNoOfRetry > 0) {
				maxNoOfRetry--;
				callRWMSWebService(input, reqStoreId);
			} else {
				OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByOmsCustOrdNoAndRmaId(omsCustOrdNo, input.getRmaRequestId());
				omsRmaReq.setStatus("F");
				session.mergeOmsRmaReq(omsRmaReq);
				throw e;
			}
		} catch (IllegalStateWSFaultException e) {
			log.error(e);
			if (maxNoOfRetry > 0) {
				maxNoOfRetry--;
				callRWMSWebService(input, reqStoreId);
			} else {
				OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByOmsCustOrdNoAndRmaId(omsCustOrdNo, input.getRmaRequestId());
				omsRmaReq.setStatus("F");
				session.mergeOmsRmaReq(omsRmaReq);
				throw e;
			}
		} catch (ValidationWSFaultException e) {
			log.error(e);
			if (maxNoOfRetry > 0) {
				maxNoOfRetry--;
				callRWMSWebService(input, reqStoreId);
			} else {
				OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByOmsCustOrdNoAndRmaId(omsCustOrdNo, input.getRmaRequestId());
				omsRmaReq.setStatus("F");
				session.mergeOmsRmaReq(omsRmaReq);
				throw e;
			}
		}
	}

//	private void saveJoodTranscation(CustomerOrderRMA input, String reqStoreId) {
//		Connection connection = null;
//		PreparedStatement statement = null;
//		try {
//			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
//			StringBuilder items = new StringBuilder();
//			StringBuilder lineNos = new StringBuilder();
//			Map<String, CustomerOrderRMAItem> itemMap = new HashMap<>();
//			for (CustomerOrderRMAItem item : input.getCustomerOrderRMAItem()) {
//				if (items.length() > 0) {
//					items.append(", ");
//					lineNos.append(", ");
//				}
//				items.append("'").append(item.getItem()).append("'");
//				lineNos.append(item.getLineNo());
//				itemMap.put(item.getItem() + "~" + item.getLineNo(), item);
//			}
//			
//			PreparedStatement selectStmt = connection.prepareStatement(SELECT_JOOD_ORD_TRAN_SQL
//					.replace("ITEM_IN_P", items.toString()).replace("LINE_IN_P", lineNos.toString()));
//			selectStmt.setString(1, input.getCustomerOrderNo());
//			selectStmt.setString(2, "SALE");
//			
//			ResultSet selectRs = selectStmt.executeQuery();
//			List<Object[]> list = new ArrayList<>();
//			Long memberShipId = null;
//			BigDecimal retail = new BigDecimal(0);
//			BigDecimal discount = new BigDecimal(0);
//			BigDecimal joodDis = new BigDecimal(0);
//			BigDecimal selling = new BigDecimal(0);
//			while (selectRs.next()) {
//				Long line = selectRs.getLong("IINE_NO");
//				String item = selectRs.getString("ITEM");
//				CustomerOrderRMAItem rmaItem = itemMap.get(item + "~" + line);
//				Object[] objArr = new Object[7];
//				memberShipId = selectRs.getLong("MEMBERSHIP_ID");
//				BigDecimal unitRetail = selectRs.getBigDecimal("UNIT_RETAIL");
//				BigDecimal dicAmt = selectRs.getBigDecimal("UNIT_DISCOUNT_AMOUNT");
//				BigDecimal joodDsnt = selectRs.getBigDecimal("JOOD_DISCOUNT");
//				BigDecimal sellingAmt = selectRs.getBigDecimal("TOTAL_SELLING_RETAIL");
//				objArr[0] = line;
//				objArr[1] = item;
//				objArr[2] = rmaItem.getReturnQtySuom();
//				objArr[3] = unitRetail;
//				objArr[4] = dicAmt;
//				objArr[5] = joodDsnt;
//				objArr[6] = sellingAmt;
//				retail = retail.add((unitRetail != null ? unitRetail : BigDecimal.ZERO).multiply(rmaItem.getReturnQtySuom()));
//				discount = discount.add((dicAmt != null ? dicAmt : BigDecimal.ZERO).multiply(rmaItem.getReturnQtySuom()));
//				joodDis = joodDis.add((joodDsnt != null ? joodDsnt : BigDecimal.ZERO).multiply(rmaItem.getReturnQtySuom()));
//				selling = selling.add((sellingAmt != null ? sellingAmt : BigDecimal.ZERO).multiply(rmaItem.getReturnQtySuom()));
//				list.add(objArr);
//			}
//			try {
//				selectRs.close();
//			} catch (Exception e) {
//				// Ignore Exception
//			}
//			if (!list.isEmpty()) {
//				CallableStatement stmt = connection.prepareCall("{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
//				stmt.setLong(1, memberShipId);
//				stmt.setString(2, input.getApplicationId());
//				stmt.setString(3, "RETURN");
//				stmt.setTimestamp(4, new Timestamp(input.getReturnDate().toGregorianCalendar().getTimeInMillis()));
//	
//				stmt.setString(5, input.getCustomerOrderNo());
//				stmt.setString(6, input.getCustomerOrderNo());
//				stmt.setString(7, input.getCustomerOrderNo());
//				stmt.setBigDecimal(8, retail);
//				stmt.setBigDecimal(9, discount);
//				stmt.setBigDecimal(10, joodDis);
//				stmt.setBigDecimal(11, selling);
//	
//				stmt.setArray(12, ((oracle.jdbc.OracleConnection) connection).createOracleArray("XX_JOOD_TRANDTL_TBL", list.toArray()));
//	
//				stmt.registerOutParameter(13, Types.VARCHAR);
//				stmt.registerOutParameter(14, Types.VARCHAR);
//				stmt.registerOutParameter(15, Types.VARCHAR);
//				stmt.registerOutParameter(16, Types.VARCHAR);
//				stmt.execute();
//				String status = stmt.getString(13);
//				log.info("Status for JOOD RMA transaction for jood order: " + input.getCustomerOrderNo() + " is: " + status + ", message: " + stmt.getString(14));
//			} else {
//				log.info("No Jood item for order " + input.getCustomerOrderNo());
//			}
//		} catch (Exception e) {
//			log.error("Error while saving JOOD transaction for order" + input.getCustomerOrderNo(), e);
//		}
//		finally {
//			OMSUtil.closeDBConnection(connection, statement, null);
//		}
//	}

	private void saveJoodTranscation(CustomerOrderRMA input, String reqStoreId) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder items = new StringBuilder();
			StringBuilder lineNos = new StringBuilder();
			Map<String, CustomerOrderRMAItem> itemMap = new HashMap<>();
			for (CustomerOrderRMAItem item : input.getCustomerOrderRMAItem()) {
				if (items.length() > 0) {
					items.append(", ");
					lineNos.append(", ");
				}
				items.append("'").append(item.getItem()).append("'");
				lineNos.append(item.getLineNo());
				itemMap.put(item.getItem() + "~" + item.getLineNo(), item);
			}

			PreparedStatement selectStmt = connection.prepareStatement(SELECT_JOOD_ORD_TRAN_SQL.replace("items", items.toString()).replace("lineNos", lineNos.toString()));
			selectStmt.setString(1, input.getCustomerOrderNo());

			ResultSet selectRs = selectStmt.executeQuery();
			List<Object[]> list = new ArrayList<>();
			Long memberShipId = null;
			BigDecimal retail = BigDecimal.ZERO;
			BigDecimal discount = BigDecimal.ZERO;
			BigDecimal joodDis = BigDecimal.ZERO;
			BigDecimal selling = BigDecimal.ZERO;
			BigDecimal totalRewardsCb = BigDecimal.ZERO;
			BigDecimal totalRedeemedCb = BigDecimal.ZERO;
			BigDecimal joodProgram = BigDecimal.ZERO;

			while (selectRs.next()) {
				Long line = selectRs.getLong("IINE_NO");
				String item = selectRs.getString("ITEM");
				CustomerOrderRMAItem rmaItem = itemMap.get(item + "~" + line);

				Object[] objArr = new Object[10];
				memberShipId = selectRs.getLong("MEMBERSHIP_ID");

				BigDecimal unitRetail = selectRs.getBigDecimal("UNIT_RETAIL") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("UNIT_RETAIL");
				BigDecimal dicAmt = selectRs.getBigDecimal("UNIT_DISCOUNT_AMOUNT") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("UNIT_DISCOUNT_AMOUNT");
				BigDecimal joodDsnt = selectRs.getBigDecimal("JOOD_DISCOUNT") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("JOOD_DISCOUNT");
				BigDecimal sellingAmt = selectRs.getBigDecimal("TOTAL_SELLING_RETAIL") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("TOTAL_SELLING_RETAIL");
				BigDecimal earnedCb = selectRs.getBigDecimal("EARNED_CB") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("EARNED_CB");
				BigDecimal usedCb = selectRs.getBigDecimal("USED_CB") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("USED_CB");
				BigDecimal redeemedCb = selectRs.getBigDecimal("REDEEMED_CB") == null ? BigDecimal.ZERO : selectRs.getBigDecimal("REDEEMED_CB");
				joodProgram = selectRs.getBigDecimal("JOOD_PROGRAM");

				BigDecimal unitRewardsCb = earnedCb.subtract(usedCb);
				objArr[0] = line;
				objArr[1] = item;
				objArr[2] = rmaItem.getReturnQtySuom();
				objArr[3] = unitRetail;
				objArr[4] = dicAmt;
				objArr[5] = joodDsnt;
				objArr[6] = sellingAmt;
				objArr[7] = unitRewardsCb;
				objArr[8] = redeemedCb;
				objArr[9] = "Y";

				retail = retail.add(unitRetail.multiply(rmaItem.getReturnQtySuom()));
				discount = discount.add(dicAmt.multiply(rmaItem.getReturnQtySuom()));
				joodDis = joodDis.add(joodDsnt.multiply(rmaItem.getReturnQtySuom()));
				selling = selling.add(sellingAmt.multiply(rmaItem.getReturnQtySuom()));
				totalRewardsCb = totalRewardsCb.add(unitRewardsCb.multiply(rmaItem.getReturnQtySuom()));
				totalRedeemedCb = totalRedeemedCb.add(redeemedCb != null ? redeemedCb : BigDecimal.ZERO);

				list.add(objArr);
			}
			try {
				selectRs.close();
			} catch (Exception e) {
			}

			if (!list.isEmpty()) {
				CallableStatement stmt = connection.prepareCall("{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

				stmt.setLong(1, memberShipId);
				stmt.setString(2, input.getApplicationId());
				stmt.setString(3, "RETURN");
				stmt.setTimestamp(4, new Timestamp(input.getReturnDate().toGregorianCalendar().getTimeInMillis()));

				stmt.setString(5, input.getCustomerOrderNo());
				stmt.setString(6, input.getCustomerOrderNo());
				stmt.setString(7, input.getCustomerOrderNo());
				stmt.setBigDecimal(8, retail);
				stmt.setBigDecimal(9, discount);
				stmt.setBigDecimal(10, joodDis);
				stmt.setBigDecimal(11, selling);
				stmt.setBigDecimal(12, totalRewardsCb);
				stmt.setBigDecimal(13, totalRedeemedCb);

				stmt.setArray(14, ((oracle.jdbc.OracleConnection) connection).createOracleArray("XX_JOOD_TRANDTL_TBL", list.toArray()));

				stmt.setString(15, "N");
				stmt.setBigDecimal(16, joodProgram);

				stmt.registerOutParameter(17, Types.VARCHAR);
				stmt.registerOutParameter(18, Types.VARCHAR);
				stmt.registerOutParameter(19, Types.VARCHAR);
				stmt.registerOutParameter(20, Types.NUMERIC);
				stmt.registerOutParameter(21, Types.VARCHAR);

				stmt.execute();

				String status = stmt.getString(15);
				log.info("Status for JOOD RMA transaction for order: " + input.getCustomerOrderNo() + " is: " + status + ", message: " + stmt.getString(16));
			} else {
				log.info("No Jood item for order " + input.getCustomerOrderNo());
			}
		} catch (Exception e) {
			log.error("Error while saving JOOD transaction for order " + input.getCustomerOrderNo(), e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, null);
		}
	}

	public CustomerOrderRMAResponse createResponse(CustomerOrderRMA input, String serviceStatus, String errMessage) {
		log.info("Inside createResponse with service status =" + serviceStatus + "ErrorMsg" + errMessage);
		CustomerOrderRMAResponse response = new CustomerOrderRMAResponse();
		response.setEntityId(input.getEntityId());
		response.setApplicationId(input.getApplicationId());
		response.setCustomerOrderNo(input.getCustomerOrderNo());
		response.setComments(input.getComments());
		response.setEntityId(response.getEntityId());
		response.setRmaRequestId(input.getRmaRequestId());
		response.setRequestDatetimestamp(input.getRequestDatetimestamp());
		response.setRequestDatetimestamp(input.getRequestDatetimestamp());

		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {

		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		response.setResponseDatetimestamp(now);
		if (serviceStatus.equals("VALIDATE_ERROR")) {
			log.info("inside validate eroor");
			// OmsErrorCodes omsError = session.getOmsErrorCodesFindByErrorCode(errorCode,
			// omsCustOrdHead.getCustomerLang());
			response.setResponseMessage("FAILED");
			if (null != errMessage) {
				log.info("inside errMeagage:" + errMessage);
				log.info("Paring the error");
				OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
				// theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
				response.setMessageDesc(theErrorObj.getOmsErrLangDesc());
			} else {
				// theResponse.setMessageDesc("");
				// theResponse.setMessageCode("");
				response.setMessageDesc("");
			}
			response.setMessageStatus("F");
			// response.setMessageCode(omsError.getOmsErrorCode());
		}

		else if (serviceStatus.equals("EXT_SYS_ERROR")) {
			if (null != errMessage) {
				OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
				// theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
				// theResponse.setMessageCode(theErrorObj.getOmsErrorCode());
				response.setMessageDesc(theErrorObj.getOmsErrLangDesc());
			} else {
				// theResponse.setMessageDesc("");
				// theResponse.setMessageCode("");
				response.setMessageDesc("");
			}
			response.setResponseMessage("FAILED");
			response.setMessageStatus("E");
			response.setMessageDesc("SYSTEM_ERROR");
		} else if (serviceStatus.equals("SUCCESS")) {
			response.setRMANo(rma.longValue());
			response.setResponseMessage("SUCCESS");
			response.setMessageStatus("S");
			response.setMessageDesc("RMA generated successfully.");
		}
		return response;
	}

	public String getInventoryInd(String item) throws Exception {
		log.info("Entering getInventoryInd method");
		String inventoryInd = null;

		String query = "SELECT inventory_ind FROM item_master WHERE item = ?";
		log.info("Executing query: " + query);
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			// Establish DB connection
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);

			// Execute query
			rs = preparedStatement.executeQuery();

			// Process result
			if (rs.next()) {
				inventoryInd = rs.getString("inventory_ind");
				log.info("Fetched inventory_ind: " + inventoryInd);
			} else {
				log.warn("No record found for item: " + item);
			}
		} catch (Exception e) {
			log.error("Exception occurred while fetching inventory_ind: " + e.getMessage(), e);
			throw e; // Re-throw the exception to notify the caller
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception closeEx) {
				log.error("Exception occurred while closing DB connection: " + closeEx.getMessage(), closeEx);
			}
		}

		log.info("Returning inventoryInd: " + (inventoryInd != null ? inventoryInd : "null"));
		return inventoryInd;
	}

}
