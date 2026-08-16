package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoCancelHead;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFoCancel;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustCancelTender;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdLog;
import com.logicinfo.oms.ejb.OmsCustOrdLogItem;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItmPickup;
import com.logicinfo.oms.ejb.OmsOrposCustOrderPickup;
import com.logicinfo.oms.ejb.OmsOrposDiscntLinePickup;
import com.logicinfo.oms.ejb.OmsOrposEmail;
import com.logicinfo.oms.ejb.OmsOrposMasterAudit;
import com.logicinfo.oms.ejb.OmsOrposPayment;
import com.logicinfo.oms.ejb.OmsOrposPhone;
import com.logicinfo.oms.ejb.OmsOrposRefundTender;
import com.logicinfo.oms.ejb.OmsOrposTaxLinePickup;
import com.logicinfo.oms.ejb.OmsRevsPick;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.ejb.Tsfdetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Emails;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Phones;
import com.oracle.retail.integration.base.bo.custordcancelitemdetails.v1.CustOrdCancelItemDetails;
import com.oracle.retail.integration.base.bo.custordcancelitemdetailscol.v1.CustOrdCancelItemDetailsCol;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;
import com.oracle.retail.integration.base.bo.custorditmpkcolvo.v1.CustOrdItmPkColVo;
import com.oracle.retail.integration.base.bo.custorditmpkvo.v1.CustOrdItmPkVo;
import com.oracle.retail.integration.base.bo.discntlinepkcolvo.v1.DiscntLinePkColVo;
import com.oracle.retail.integration.base.bo.discntlinepkvo.v1.DiscntLinePkVo;
import com.oracle.retail.integration.base.bo.emaildesc.v1.EmailDesc;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.paymentcoldesc.v1.PaymentColDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CouponTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.GiftCardTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentType;
import com.oracle.retail.integration.base.bo.phonedesc.v1.PhoneDesc;
import com.oracle.retail.integration.base.bo.pickupcustomerorderitemdetailsref.v1.PickupCustomerOrderItemDetailsRef;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordItm;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfItm;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;
import com.oracle.retail.integration.base.bo.taxlinepkcolvo.v1.TaxLinePkColVo;
import com.oracle.retail.integration.base.bo.taxlinepkvo.v1.TaxLinePkVo;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;

public class PickCustOrdItemBean {
	public PickCustOrdItemBean() {
		super();
	}

	static BigDecimal su_qty = BigDecimal.valueOf(0);
	public String status = "S";
	private final static Logger log = Logger.getLogger(PickCustOrdItemBean.class.getName());
	BigDecimal omsCustOrderNo;
	BigDecimal omsOrposCustOrdId = null;
	BigDecimal custOrderPicVoSeq = null;
	long L_cum_cancel_qty;
	long L_CO_ITEM_CANCEL_QTY;
	long L_fo_open_qty;
	long fulilmentOrdNo;
	BigDecimal omsCancelId;
	String extCustOrdNo = "";
	BigDecimal dummycancellationid = BigDecimal.ZERO;
	Map<BigDecimal, BigDecimal> nonInventoryItemMap = new HashMap<BigDecimal, BigDecimal>();

	int retryForRMSCancelFulfilOrdColRef = 0;
	int retryCreateReversePick = 0;
	int retryConfirmReversePick = 0;
	int retryCancelFulfillmentOrderDetail1 = 0;
	int retryCancelFulfillmentOrderDetail2 = 0;
	int retryCancelFulfillmentOrderDetail3 = 0;
	List<CustOrdItmPkVo> pickUpItemsList = new ArrayList<CustOrdItmPkVo>();
	List<CustOrdItmPkVo> cancellItemList = new ArrayList<CustOrdItmPkVo>();
	List<BigDecimal> dummycancelidList = new ArrayList<BigDecimal>();
	List<BigDecimal> rmaCancelList = new ArrayList<BigDecimal>();
	List<RMARefundData> rmaRefundData = new ArrayList<RMARefundData>();
	List<DummyCancelData> dummyCancelData = new ArrayList<DummyCancelData>();
	Map<String, BigDecimal> siebelDataforPickUp = new HashMap<String, BigDecimal>();
	boolean dummyCancelFlag = false;
	boolean rmaFlag = false;
	boolean globalRmaFalg = false;
	boolean rmaandcancel = false;
	boolean negetiveQty = false;
	String error = null;
	String customerOrderNo = "";
	public String transactionNumber = "";
	TreeMap<String, BigDecimal> backorderTreeMap = new TreeMap<String, BigDecimal>();

	public String checkforOrderIdandTransactionNo(String customerOrderNo, String transactionNumber)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("===========inside checkforOrderIdandTransactionNo===============");
		List<OmsOrposMasterAudit> omsOrposMasterAudit = null;
		try {
			log.info("customerOrderNo " + customerOrderNo);
			log.info("transactionNumber " + transactionNumber);
			omsOrposMasterAudit = session.getOmsOrposMasterAuditfindByOrderIdandOrposTransactionNumber(customerOrderNo,
					transactionNumber);

			if (omsOrposMasterAudit != null && omsOrposMasterAudit.size() > 0) {
				log.info("omsOrposMasterAudit.size()" + omsOrposMasterAudit.size());
				status = "F";
				error = "ERROR_115";
				log.info("setting status to " + status);
			} else {

				status = "S";
				error = "success";
			}
		} catch (Exception e) {
			if (omsOrposMasterAudit == null && omsOrposMasterAudit.size() == 0) {
				log.info("No record exist in the table in the OmsOrposMasterAudit Table : ");
				status = "S";
				log.info("Inside catch block setting status to " + status);
			}
		}
		log.info("returning status in checkforOrderIdandTransactionNo " + status);
		return status;
	}

	public void getCustomerOrderandTransactionNo(CustOrderPicVo custOrderPicVo) throws SOAPException {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		if (custOrderPicVo.getCustomerOrderId().contains("-")) {
			log.info("insisde if block input customerOrderNo contains dellimeter");
			String posCustOrdNo[] = oMSUtilCommons.splitCustomerOrder(custOrderPicVo.getCustomerOrderId());
			customerOrderNo = posCustOrdNo[0];
			transactionNumber = posCustOrdNo[1];
			log.info("customerOrderNo " + customerOrderNo);
			log.info("transactionNumber " + transactionNumber);
			// Adding code for

			status = checkforOrderIdandTransactionNo(customerOrderNo, transactionNumber);
			log.info("status " + status);

		} else {
			log.info("inside else block input customerOrderNo doesnot contain delimeter");
			customerOrderNo = custOrderPicVo.getCustomerOrderId();

		}

	}

	public void cancelShippingChargeItem(String customerOrderNo, CustOrdItmPkVo custOrdItmPkVo, BigDecimal omsCancelId)
			throws SOAPException {
		log.info("inside cancelShippingChargeItem method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("custOrdItmPkVo.getLineItemNo() " + custOrdItmPkVo.getLineItemNo());
		log.info("omsCancelId " + omsCancelId);
		List<OmsCoCancelItem> omsCoCancelItem1 = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		for (OmsCoCancelItem omsCoCancelItem2 : omsCoCancelItem1) {
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT",
					"OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(omsCoCancelItem2.getItem());
			String inventoryIndn = session.getItemMasterFindInventoryInd(omsCoCancelItem2.getItem(), itemDept);
			if (custOrdItmPkVo.getLineItemNo() == omsCoCancelItem2.getLineNo().intValue()) {
				if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
					log.info("request contains a shipping charge item or non-inventory item");
					OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(
							omsCustOrdHead.getOmsCustOrdNo(), omsCoCancelItem2.getItem(), omsCoCancelItem2.getLineNo());
					BigDecimal summation = omsCustOrdItem.getQtyCancelled().add(omsCoCancelItem2.getCancelReqQty());
					log.info("omsCancelId" + omsCancelId + "summation " + summation);
					omsCustOrdItem.setQtyCancelled(summation);
					try {
						session.mergeOmsCustOrdItem(omsCustOrdItem);
					} catch (Exception e) {
						log.info("omsCancelId" + omsCancelId + "Exception " + e.getMessage());
					}
					omsCoCancelItem2.setCancelConfQty(omsCoCancelItem2.getCancelReqQty());
					session.mergeOmsCoCancelItem(omsCoCancelItem2);
				}
			}
		}
	}

	public boolean rmaAndcancellation_Validation(String customerOrderNo, CustOrdItmPkVo custOrdItmPkVo,
			BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("inside rmaAndcancellation_Validation method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<OmsRmaReq> omsRmaReqList = session.getOmsRmaReqFindByOmscustOrdNo(omsCustOrdNo);
		for (OmsRmaReq omsRmaReq1 : omsRmaReqList) {
			log.info("omsRmaReq1.getRmaId() " + omsRmaReq1.getRmaId());
			OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByRmaId(omsRmaReq1.getRmaId());
			List<OmsRmaReqItem> omaRmaItem = session.getOmsRmaReqItemFindByRmaId(omsRmaReq.getRmaId());
			if (omaRmaItem.get(0).getLineNo().intValue() == custOrdItmPkVo.getLineItemNo()) {
				if (omsRmaReq.getStatus().equals("S")) {
					if (omsRmaReq.getRefundCompltInd().equals("N")) {
						rmaandcancel = true;
					}
				}
			}

		}
		List<OmsCoCancelHead> omsCoCancelHeadList = session.getOmsCoCancelHeadFindByCustOrdNo(customerOrderNo);
		for (OmsCoCancelHead omsCoCancelHead : omsCoCancelHeadList) {
			List<OmsCoCancelItem> omsCoCancelItemList = session
					.getOmsCoCancelItemFindByOmsCancelId(omsCoCancelHead.getOmsCancelId());
			for (OmsCoCancelItem omsCoCancelItem : omsCoCancelItemList) {
				if (omsCoCancelItem.getLineNo().intValue() == custOrdItmPkVo.getLineItemNo()) {
					if (omsCoCancelHead.getRefundCompltInd().equals("N")) {
						rmaandcancel = true;
					}
				}
			}

		}
		log.info("rmaandcancel " + rmaandcancel);
		return rmaandcancel;
	}

	public String checkValidation(String customerOrderNo, CustOrdItmPkVo custOrdItmPkVo, CustOrderPicVo custOrderPicVo)
			throws SOAPException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, ValidationWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			IllegalStateWSFaultException, IllegalStateWSFaultException, IllegalArgumentWSFaultException,
			ValidationWSFaultException, ValidationWSFaultException

	{
		log.info("inside checkValidation method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
		log.info("CustomerOrder no:" + customerOrderNo);
		OmsCustOrdHead omscustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("after omscustOrdHead obj creat");
		List<OmsCustOrdItem> omsCustOrdItem = null;
		omsCustOrdItem = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omscustOrdHead.getOmsCustOrdNo(),
				new BigDecimal(custOrdItmPkVo.getLineItemNo()));
		OmsCustOrdItem omsCustOrdItem1 = session.getOmsCustOrdItemFindByItem(omscustOrdHead.getOmsCustOrdNo(),
				omsCustOrdItem.get(0).getItem(), new BigDecimal(custOrdItmPkVo.getLineItemNo()));
		OmsCoFulfillDetail omsCoFulfillDetail = null;
		int picked_Qty = 0;
		boolean checkRmaandCancelvalue = false;
		try {
			log.info("getting picked_Qty " + picked_Qty);
			picked_Qty = omsUtilCommmons.checkOpenDeliveryForQuantity_PickedandDevelired(customerOrderNo,
					omscustOrdHead.getOmsCustOrdNo(), omsCustOrdItem1);
			// omsCoFulfillDetail=session.getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(omscustOrdHead.getOmsCustOrdNo(),
			// new BigDecimal(custOrdItmPkVo.getLineItemNo()));
			picked_Qty = picked_Qty - omsCustOrdItem1.getCumQtyDelivered().intValue();
			log.info("After getting picked_Qty " + picked_Qty);

		} catch (Exception e) {
			log.info("e " + e);
			picked_Qty = 0;
		}
		log.info("=====for line No " + custOrdItmPkVo.getLineItemNo() + "=============");
		log.info("custOrdItmPkVo.getCancelledQuantity() " + custOrdItmPkVo.getCancelledQuantity());
		log.info("custOrdItmPkVo.getCompletedQuantity() " + custOrdItmPkVo.getCompletedQuantity());
		log.info("omsCustOrdItem.get(0).getCumQtyDelivered() " + omsCustOrdItem.get(0).getCumQtyDelivered());
		log.info("omsCustOrdItem.get(0).getQtyCancelled() " + omsCustOrdItem.get(0).getQtyCancelled());
		log.info("omsCustOrdItem.get(0).getQtyOrderedSuom()" + omsCustOrdItem.get(0).getQtyOrderedSuom());

		if (omsCustOrdItem.get(0).getCumQtyDelivered()
				.add(omsCustOrdItem.get(0).getQtyCancelled()
						.add(custOrdItmPkVo.getCompletedQuantity().add(custOrdItmPkVo.getCancelledQuantity())))
				.intValue() <= omsCustOrdItem.get(0).getQtyOrderedSuom().intValue() || negetiveQty == true) {
			if (negetiveQty == true) {
				checkRmaandCancelvalue = rmaAndcancellation_Validation(customerOrderNo, custOrdItmPkVo,
						omscustOrdHead.getOmsCustOrdNo());
				if (checkRmaandCancelvalue == false) {
					status = "F";
					error = "ERROR_201"; // Fraud attempt
				} else {
					log.info("else 1");
					status = "S";
					error = "success";
				}
			} else if (custOrdItmPkVo.getCompletedQuantity().intValue() > picked_Qty) {
				if (picked_Qty < 0) {
					status = "S";
				} else {
					status = "F";
					error = "ERROR_201"; // Fraud attempt
				}
			}

			else {
				log.info("else 2");
				status = "S";
				error = "success";

			}
		} else {

			status = "F";
			error = "ERROR_201"; // Fraud attempt

		}

		log.info("returning Status " + status);
		log.info("Error " + error);

		return status;

	}

	// Main method which checks whether the item coming in is for pickup or cancel
	// operation

	public PickupCustomerOrderItemDetailsRef findPickUpOrCancel(CustOrderPicVo custOrderPicVo)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			SOAPException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException,
			Exception {
		log.info("Inside findPickUporCancel method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = new PickupCustomerOrderItemDetailsRef();
		log.info("status " + status);
		log.info("Error " + error);

		if (status.equals("S")) {
			CustOrdItmPkColVo custOrdItmPkColVo = custOrderPicVo.getCustOrdItmPkColVo();
			BigDecimal omsCancelId = null;
			log.info("CustOrdItmPkVo initialized");
			log.info("customerOrderNo " + customerOrderNo);
			BigDecimal omscustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
			List<CustOrdItmPkVo> custOrdItmPkVoList = custOrdItmPkColVo.getCustOrdItmPkVo();
			log.info("CustOrdItmPkVo list initialized");
			for (CustOrdItmPkVo custOrdItmPkVo : custOrdItmPkVoList) {
				log.info("CustOrdItmPkVo loop starts");
				if (custOrdItmPkVo.getCompletedQuantity().intValue() != 0
						&& custOrdItmPkVo.getCancelledQuantity().intValue() == 0) {
					// Call all the pickUp methods
					log.info("Inside pickUp condition loop");
					status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
					if (status.equals("F")) {
						status = error;
						log.info("status ====" + status);
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
						break;

					}
					pickUpItemsList.add(custOrdItmPkVo);
				} else if (custOrdItmPkVo.getCancelledQuantity().intValue() != 0
						&& custOrdItmPkVo.getCompletedQuantity().intValue() == 0) {
					// call the cancellation methods
					log.info("Inside cancellation condition loop");
					log.info("before calling filterForDummyCancellation " + custOrdItmPkVo.getCancelledQuantity());
					log.info("calling filterForDummyCancellation method =============1");
					if (custOrdItmPkVo.getCancelledQuantity().intValue() < 0) {
						negetiveQty = true;
						log.info("custOrdItmPkVo.getCancelledQuantity() " + custOrdItmPkVo.getCancelledQuantity());
						log.info("custOrdItmPkVo.getCancelledQuantity().negate() "
								+ custOrdItmPkVo.getCancelledQuantity().negate());
						custOrdItmPkVo.setCancelledQuantity(custOrdItmPkVo.getCancelledQuantity().negate());
						status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
						log.info("returned status is " + status);
						if (status.equals("F")) {
							status = error;
							log.info("status ====" + status);
							pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
							break;
						}
						custOrdItmPkVo = filterForDummyCancellation(custOrdItmPkVo, customerOrderNo, custOrderPicVo);
						log.info("after calling filterForDummyCancellation " + custOrdItmPkVo.getCancelledQuantity());
						negetiveQty = false;
						if (status.equals("F")) {
							break;
						}

					} else {
						status = "F";
						error = "No cancellation on pickup.";
						break;
					}

					status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
					log.info("returned status is " + status);
					if (status.equals("F")) {
						status = error;
						log.info("status ====" + status);
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
						break;
					}

					if (custOrdItmPkVo.getCancelledQuantity().intValue() != 0) {
						log.info("cancelled list is greater than zero");
						cancellItemList.add(custOrdItmPkVo);
						log.info("cancelList =========++++--------------------" + cancellItemList.size());
						log.info("====================================");
						log.info("Status====================" + status);
					}
				} else if (custOrdItmPkVo.getCompletedQuantity().intValue() != 0
						&& custOrdItmPkVo.getCancelledQuantity().intValue() != 0) {
					// call the pickup x` cancellation process simultaneously
					log.info("Inside the double scenario case condition loop");
					status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
					pickUpItemsList.add(custOrdItmPkVo);
					log.info("before calling filterForDummyCancellation " + custOrdItmPkVo.getCancelledQuantity());
					if (custOrdItmPkVo.getCancelledQuantity().intValue() < 0) {
						negetiveQty = true;
						custOrdItmPkVo.setCancelledQuantity(custOrdItmPkVo.getCancelledQuantity().negate());
						status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
						log.info("returned status is " + status);
						if (status.equals("F")) {
							status = error;
							log.info("status ====" + status);
							pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
							break;
						}
						log.info("calling filterForDummyCancellation method =============2");
						custOrdItmPkVo = filterForDummyCancellation(custOrdItmPkVo, customerOrderNo, custOrderPicVo);
						log.info("before calling filterForDummyCancellation " + custOrdItmPkVo.getCancelledQuantity());
						negetiveQty = false;
					} else {
						status = "F";
						error = "No cancellation on pickup.";
						break;
					}
					status = checkValidation(customerOrderNo, custOrdItmPkVo, custOrderPicVo);
					log.info("returned status is " + status);
					if (status.equals("F")) {
						status = error;
						log.info("status ====" + status);
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
						break;
					}
					if (custOrdItmPkVo.getCancelledQuantity().intValue() != 0) {
						cancellItemList.add(custOrdItmPkVo);
					}
				}

			}

			// list dummy
			log.info("status is S proceeding further");
			if (status.equalsIgnoreCase("S")) {

				log.info("cancellItemList.size() " + cancellItemList.size());
				log.info("inside 1st if   ");
				try {
					log.info("inside try");
					if (rmaCancelList.size() > 0) {
						log.info("inside arrayList for omsRmaReq");
						for (BigDecimal rmaid : rmaCancelList) {
							log.info("rmaid " + rmaid);
							OmsRmaReq omsRmaReq = session.getOmsRmaReqFindByRmaId(rmaid);
							if (omsRmaReq.getStatus().equals("S")) {
								if (omsRmaReq.getRefundCompltInd().equals("N")) {
									log.info("its N changing to Y");
									omsRmaReq.setRefundCompltInd("Y");
									session.mergeOmsRmaReq(omsRmaReq);
								} else {
									log.info("already Y in OmsRmaReq");
								}
							}
						}
					}

					if (dummycancelidList.size() > 0) {
						log.info("inside arrayList for omscocancelHead");
						for (BigDecimal omscancelId : dummycancelidList) {
							log.info("omscancelId " + omscancelId);
							OmsCoCancelHead omsCoCancelHead = session
									.getOmsCoCancelHeadFindCustOrderNoByomsCancelId(omscancelId);
							if (omsCoCancelHead.getRefundCompltInd().equals("N")) {
								log.info("its N changing to Y");
								omsCoCancelHead.setRefundCompltInd("Y");
								session.mergeOmsCoCancelHead(omsCoCancelHead);
							} else {
								log.info("already Y in OmsCoCancelHead");
								// status="F";
								// error="ERROR_201";
							}
						}
					}
				} catch (Exception e) {
					log.info("no records found in omscocancel head");
				}

				log.info("Before starting core logic++++++++++++++++++++++++++++++++");

				if (pickUpItemsList.size() != 0 && cancellItemList.size() != 0 && dummycancelidList.size() == 0) {
					log.info("inside 2nd if   ");
					log.info("Inside pick and cancel item list if loop");
					// processes the cancellation request for items
					saveCOCancellationDetails(custOrderPicVo);
					saveCustOrdLog(custOrderPicVo);
					status = checkLinkedItem(custOrderPicVo, omscustOrdNo);
					if (status.equalsIgnoreCase("S")) {
						status = cancelNonInventoryShippingChargeItems(custOrderPicVo);
					}
					if (status.equalsIgnoreCase("S")) {
						status = checkOpenDelivery(custOrderPicVo, omscustOrdNo);
					}
					if (status.equalsIgnoreCase("S")) {
						status = processCancellation(cancellItemList, custOrderPicVo);
						/*
						 * if(status.equalsIgnoreCase("S")) { //updateConfirmQty(custOrderPicVo); }
						 */
					}

					if (status.equalsIgnoreCase("S")) {
						// Calling update methods for Pickup
						updateOmsCustOrdItem(customerOrderNo, pickUpItemsList);
						log.info("after checking the qty validation status is " + status);
						updateOmsCoFulfillDetail(customerOrderNo, pickUpItemsList);
						// updateOmsCustOrdHead(customerOrderId);

						// Calling persistence methods for Pickup
						persistOmsCustOrdLogPickup(customerOrderNo);
						persistCustOrdLogItemPickup(customerOrderNo, pickUpItemsList);
						// persistOmsRtlogPublishLogPickup(customerOrderId);

						// Calling methods for persistence for Cancellation

						// Call the persistence for CustOrdLog table

						// Checks what is the status of an item
						omsCancelId = checkItemStatus(customerOrderNo, custOrderPicVo);

						// Notify Siebel
						notifySiebel(custOrderPicVo, cancellItemList, omsCancelId);

					} else {
						status = error;
						log.info("status ====" + status);
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
					}
				} else if (pickUpItemsList.size() != 0) {
					log.info("inside 1st  else if   ");
					log.info("Inside PickUp Item list if loop");
					// Calling update methods for Pickup
					updateOmsCustOrdItem(customerOrderNo, pickUpItemsList);
					log.info("after checking the qty validation status is " + status);
					updateOmsCoFulfillDetail(customerOrderNo, pickUpItemsList);
					// Calling persistence methods for Pickup
					persistOmsCustOrdLogPickup(customerOrderNo);
					persistCustOrdLogItemPickup(customerOrderNo, pickUpItemsList);
					// persistOmsRtlogPublishLogPickup(customerOrderId);
				} else if (cancellItemList.size() != 0 && dummycancelidList.size() == 0) {
					log.info("inside 2nd  else if   ");
					log.info("Inside cancel item list if loop");
					saveCOCancellationDetails(custOrderPicVo);
					// Call the persistence for CustOrdLog table
					saveCustOrdLog(custOrderPicVo);
					// processes the cancellation request for items
					status = checkLinkedItem(custOrderPicVo, omscustOrdNo);
					if (status.equals("S")) {
						status = cancelNonInventoryShippingChargeItems(custOrderPicVo);
					}

					if (status.equalsIgnoreCase("S")) {
						status = checkOpenDelivery(custOrderPicVo, omscustOrdNo);
					}
					if (status.equalsIgnoreCase("S")) {
						status = processCancellation(cancellItemList, custOrderPicVo);
						/*
						 * if(status.equalsIgnoreCase("S")) { //updateConfirmQty(custOrderPicVo); }
						 */
					}
					log.info("status " + status);
					log.info("error " + error);
					if (status.equalsIgnoreCase("S")) {
						// Calling methods for persistence for Cancellation
						// Checks what is the status of an item
						omsCancelId = checkItemStatus(customerOrderNo, custOrderPicVo);
						// Notify Siebel
						notifySiebel(custOrderPicVo, cancellItemList, omsCancelId);
					} else {
						status = error;
						log.info("status ====" + status);
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
					}
				}
				if (status.equals("S")) {
					persistOmsCustOrdTender(customerOrderNo, custOrderPicVo);
					updateOmsCustOrdHead(customerOrderNo);
					log.info("<-----------Setting the response------->");
					log.info("pickUpItemsList.size() " + pickUpItemsList.size());
					log.info("cancellItemList.size() " + cancellItemList.size());
					if (pickUpItemsList.size() != 0 && cancellItemList.size() != 0) {
						log.info("inside pick and cancel condition for response");
						pickupCustomerOrderItemDetailsRef = createResponseForCancellation(custOrderPicVo);
					} else if (pickUpItemsList.size() != 0) {
						log.info("inside pickup condition for response");
						pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
					} else if (cancellItemList.size() != 0) {
						log.info("inside cancel condition for response");
						pickupCustomerOrderItemDetailsRef = createResponseForCancellation(custOrderPicVo);
					} else {
						log.info("inside default else for dummy & RMA");
						pickupCustomerOrderItemDetailsRef = createResponseForDefaultDummyandRMACancellation(
								custOrderPicVo);
					}
				}
			}

			else if (status.equals("F")) {
				// persistOmsCustOrdTender(customerOrderId, custOrderPicVo);
				status = error;
				log.info("status ====" + status);
				log.info("++++++++++++++++++++++++++++++++");
				pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
			}

			log.info("Before returning response++++++++++++++++++++++++++++++++");
		} else {
			status = error;
			log.info("status ====" + status);
			log.info("++++++++++++++++++++++++++++++++");
			pickupCustomerOrderItemDetailsRef = createResponseForPickUp(custOrderPicVo, status);
		}
		return pickupCustomerOrderItemDetailsRef;

	}

	public int saveCustOrderPicVo(CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("Inside saveCustOrderPicVo method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int collectionSize = 0;
		// omsOrposCustOrdId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderPicVo.getCustomerOrderId());
		// Persisting the values for Head Table of Pick-UP CustOrderPicVo
		OmsOrposCustOrderPickup omsOrposCustOrderPickup = new OmsOrposCustOrderPickup();
		// omsOrposCustOrderPickup.setOmsOrposCustOrderId(omsOrposCustOrdId);
		omsOrposCustOrderPickup.setCustomerOrderId(customerOrderNo);
		omsOrposCustOrderPickup.setCurrencyCode(custOrderPicVo.getCurrencyCode());

		// setting all the stages for Amount
		omsOrposCustOrderPickup.setCompletedAmount(custOrderPicVo.getCompletedAmount());
		omsOrposCustOrderPickup.setCancelledAmount(custOrderPicVo.getCancelledAmount());
		omsOrposCustOrderPickup.setRepricedAmount(custOrderPicVo.getRepricedAmount());
		omsOrposCustOrderPickup.setCompletedNewAmount(custOrderPicVo.getCompletedNewAmount());

		// setting all the stages for DiscountAmount
		omsOrposCustOrderPickup.setCompletedDiscountAmount(custOrderPicVo.getCompletedDiscountAmount());
		omsOrposCustOrderPickup.setCancelledDiscountAmount(custOrderPicVo.getCancelledDiscountAmount());
		omsOrposCustOrderPickup.setRepricedDiscountAmount(custOrderPicVo.getRepricedDiscountAmount());
		omsOrposCustOrderPickup.setCompletedNewDiscountAmount(custOrderPicVo.getCompletedNewDiscountAmount());

		// setting all the stages for TaxAmount
		omsOrposCustOrderPickup.setCompletedTaxAmount(custOrderPicVo.getCompletedTaxAmount());
		omsOrposCustOrderPickup.setCancelledTaxAmount(custOrderPicVo.getCancelledTaxAmount());
		omsOrposCustOrderPickup.setRepricedTaxAmount(custOrderPicVo.getRepricedTaxAmount());
		omsOrposCustOrderPickup.setCompletedNewTaxAmount(custOrderPicVo.getCompletedNewTaxAmount());

		// setting all the stages for IncTaxAmount
		omsOrposCustOrderPickup.setCompletedIncTaxAmount(custOrderPicVo.getCompletedIncTaxAmount());
		omsOrposCustOrderPickup.setCancelledIncTaxAmount(custOrderPicVo.getCancelledIncTaxAmount());
		omsOrposCustOrderPickup.setRepricedIncTaxAmount(custOrderPicVo.getRepricedIncTaxAmount());
		omsOrposCustOrderPickup.setCompletedNewIncTaxAmount(custOrderPicVo.getCompletedNewIncTaxAmount());

		omsOrposCustOrderPickup.setPaidAmount(custOrderPicVo.getPaidAmount());
		omsOrposCustOrderPickup.setRoundingAdjustment(custOrderPicVo.getRoundingAdjustment());
		omsOrposCustOrderPickup.setRefundAmountOffsetBySale(custOrderPicVo.getRefundAmountOffsetBySale());
		omsOrposCustOrderPickup.setUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
		session.persistOmsOrposCustOrderPickup(omsOrposCustOrderPickup);
		log.info("Successfully persisted the data into OmsOrposCustOrderPickup table");
		List<BigDecimal> custOrderPicVoSeq = session.getOmsOrposCustOrderPickupFindCustOrderPicVoSeq(customerOrderNo);

		// child node of CustOrderPicVo
		CustOrdItmPkColVo custOrdItmPkColVo = custOrderPicVo.getCustOrdItmPkColVo();
		collectionSize = saveCustOrdItmPickup(custOrdItmPkColVo, omsOrposCustOrdId, custOrderPicVo);
		return collectionSize;
	} // end of pickOrderHead

	public int saveCustOrdItmPickup(CustOrdItmPkColVo custOrdItmPkColVo, BigDecimal omsOrposCustOrdId,
			CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("Inside CustOrdItmPkup method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int collectionSize = 0;
		BigDecimal maxPickSeqNo = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
		log.info("pick-cancel seqNo=" + maxPickSeqNo);

		List<CustOrdItmPkVo> custOrdItmPkVoList = custOrdItmPkColVo.getCustOrdItmPkVo();
		collectionSize = custOrdItmPkVoList.size();
		// child node of CustOrdItmPkColVo
		for (CustOrdItmPkVo custOrdItmPkVo : custOrdItmPkVoList) {
			OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup = new OmsOrposCustOrdItmPickup();
			// Persisting the values for CUST_ORD_ITM_PICKUP - CustOrdItmPkVo (child of
			// CustOrdItmPkColVo)
			// omsOrposCustOrdItmPickup.setOmsOrposCustOrderId(omsOrposCustOrdId);
			omsOrposCustOrdItmPickup.setCustOrderPicVoSeq(maxPickSeqNo);
			omsOrposCustOrdItmPickup.setLineItemNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
			omsOrposCustOrdItmPickup.setFulfillOrderId(custOrdItmPkVo.getFulfillOrderId());

			// setting all the stages for Quanity
			omsOrposCustOrdItmPickup.setCompletedQuantity(custOrdItmPkVo.getCompletedQuantity());
			omsOrposCustOrdItmPickup.setCancelledQuantity(custOrdItmPkVo.getCancelledQuantity());
			omsOrposCustOrdItmPickup.setCompletedRepricedQuantity(custOrdItmPkVo.getCompletedRepricedQuantity());
			omsOrposCustOrdItmPickup.setUnitOfMeasure(custOrdItmPkVo.getUnitOfMeasure());
			omsOrposCustOrdItmPickup.setCurrencyCode(custOrdItmPkVo.getCurrencyCode());

			// setting all the stages for amount
			omsOrposCustOrdItmPickup.setCompletedAmount(custOrdItmPkVo.getCompletedAmount());
			omsOrposCustOrdItmPickup.setCancelledAmount(custOrdItmPkVo.getCancelledAmount());
			omsOrposCustOrdItmPickup.setRepricedAmount(custOrdItmPkVo.getRepricedAmount());
			omsOrposCustOrdItmPickup.setCompletedNewAmount(custOrdItmPkVo.getCompletedNewAmount());

			// setting all the stages for DiscountAmount
			omsOrposCustOrdItmPickup.setCompletedDiscountAmount(custOrdItmPkVo.getCompletedDiscountAmount());
			omsOrposCustOrdItmPickup.setCancelledDiscountAmount(custOrdItmPkVo.getCancelledDiscountAmount());
			omsOrposCustOrdItmPickup.setRepricedDiscountAmount(custOrdItmPkVo.getRepricedDiscountAmount());
			omsOrposCustOrdItmPickup.setCompletedNewDiscountAmount(custOrdItmPkVo.getCompletedNewDiscountAmount());

			// setting all the stages for TaxAmount
			omsOrposCustOrdItmPickup.setCompletedTaxAmount(custOrdItmPkVo.getCompletedTaxAmount());
			omsOrposCustOrdItmPickup.setCancelledTaxAmount(custOrdItmPkVo.getCancelledTaxAmount());
			omsOrposCustOrdItmPickup.setRepricedTaxAmount(custOrdItmPkVo.getRepricedTaxAmount());
			omsOrposCustOrdItmPickup.setCompletedNewTaxAmount(custOrdItmPkVo.getCompletedNewTaxAmount());

			// setting all the stages for IncTaxAmount
			omsOrposCustOrdItmPickup.setCompletedIncTaxAmount(custOrdItmPkVo.getCompletedIncTaxAmount());
			omsOrposCustOrdItmPickup.setCancelledIncTaxAmount(custOrdItmPkVo.getCancelledIncTaxAmount());
			omsOrposCustOrdItmPickup.setRepricedIncTaxAmount(custOrdItmPkVo.getRepricedIncTaxAmount());
			omsOrposCustOrdItmPickup.setCompletedNewIncTaxAmount(custOrdItmPkVo.getCompletedNewIncTaxAmount());
			omsOrposCustOrdItmPickup.setPaidAmount(custOrdItmPkVo.getPaidAmount());
			omsOrposCustOrdItmPickup.setSerialNumber(custOrdItmPkVo.getSerialNumber());
			session.persistOmsOrposCustOrdItmPickup(omsOrposCustOrdItmPickup);
			log.info("Data successfully persisted in OmsOrposCustOrdItmPickup");

			if (custOrdItmPkVo.getDiscntLinePkColVo() != null) {
				// child node of CustOrdItmPkVo
				DiscntLinePkColVo discntLinePkColVo = custOrdItmPkVo.getDiscntLinePkColVo();
				// child node of DiscntLinePkColVo
				if (discntLinePkColVo != null) {
					for (DiscntLinePkVo discntLinePkVo : discntLinePkColVo.getDiscntLinePkVo()) {
						OmsOrposDiscntLinePickup omsOrposDiscntLinePickup = new OmsOrposDiscntLinePickup();
						// omsOrposDiscntLinePickup.setOmsOrposCustOrderId(omsOrposCustOrdId);
						omsOrposDiscntLinePickup.setCustOrderPicVoSeq(maxPickSeqNo);
						omsOrposDiscntLinePickup.setLineItemNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
						omsOrposDiscntLinePickup.setLineNo(new BigDecimal(discntLinePkVo.getLineNo()));
						omsOrposDiscntLinePickup.setCurrencyCode(discntLinePkVo.getCurrencyCode());
						omsOrposDiscntLinePickup
								.setCompletedDiscountAmount(discntLinePkVo.getCompletedDiscountAmount());
						omsOrposDiscntLinePickup
								.setCancelledDiscountAmount(discntLinePkVo.getCancelledDiscountAmount());
						omsOrposDiscntLinePickup.setRepricedDiscountAmount(discntLinePkVo.getRepricedDiscountAmount());
						session.persistOmsOrposDiscntLinePickup(omsOrposDiscntLinePickup);
						log.info("Data successfully persisted in OmsOrposDiscntLinePickup");
					}
				}
			}

			if (custOrdItmPkVo.getTaxLinePkColVo() != null) {
				// child node of CustOrdItmPkVo
				TaxLinePkColVo taxLinePkColVo = custOrdItmPkVo.getTaxLinePkColVo();
				// child node of TaxLinePkColVo
				if (taxLinePkColVo != null) {
					for (TaxLinePkVo taxLinePkVo : taxLinePkColVo.getTaxLinePkVo()) {
						OmsOrposTaxLinePickup omsOrposTaxLinePickup = new OmsOrposTaxLinePickup();
						// omsOrposTaxLinePickup.setOmsOrposCustOrderId(omsOrposCustOrdId);
						omsOrposTaxLinePickup.setCustOrderPicVoSeq(maxPickSeqNo);
						omsOrposTaxLinePickup.setLineItemNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
						omsOrposTaxLinePickup.setLineNo(new BigDecimal(taxLinePkVo.getLineNo()));
						omsOrposTaxLinePickup.setCurrencyCode(taxLinePkVo.getCurrencyCode());
						omsOrposTaxLinePickup.setCompletedTaxAmount(taxLinePkVo.getCompletedTaxAmount());
						omsOrposTaxLinePickup.setCancelledTaxAmount(taxLinePkVo.getCancelledTaxAmount());
						omsOrposTaxLinePickup.setRepricedTaxAmount(taxLinePkVo.getRepricedTaxAmount());
						omsOrposTaxLinePickup.setInclusiveTaxFlag(taxLinePkVo.getInclusiveTaxFlag().value());
						/* Start Tax enablement tsultana */
						session.persistOmsOrposTaxLinePickup(omsOrposTaxLinePickup);
						log.info("Data successfully persisted in OmsOrposTaxLinePickup");
						/* End Tax enablement tsultana */
					}
				}
			}

			// CustOrdDelDesc custOrdDelDesc= custOrderPicVo.getCustOrdDelDesc();
			// persisting into CustOrdDel table
			// saveCustOrdDelDesc(custOrdDelDesc);

			PaymentColDesc paymentColDesc = custOrderPicVo.getPaymentColDesc();
			// persisting into Payment table
			// savePaymentColDesc(paymentColDesc,omsOrposCustOrdId);

		}
		return collectionSize;
	}

	// Persisting into PaymentColDesc table
	/*
	 * public void savePaymentColDesc(PaymentColDesc paymentColDesc,BigDecimal
	 * omsOrposCustOrdId) throws SOAPException {
	 * log.info("Started persisting in payment"); OMSUtilSessionEJB session =
	 * OMSUtil.doLookup(); CustOrderPicVo custOrderPicVo=new CustOrderPicVo();
	 * //omsOrposCustOrdId=
	 * session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderPicVo.
	 * getCustomerOrderId()); List<PaymentDesc>
	 * paymentDescList=paymentColDesc.getPaymentDesc(); for (PaymentDesc
	 * paymentDesc:paymentDescList) { BigDecimal paymentSeqNo=new
	 * BigDecimal(paymentDesc.getSeqNo()); OmsOrposPayment omsOrposPayment=new
	 * OmsOrposPayment(); omsOrposPayment.setOmsOrposCustOrderId(omsOrposCustOrdId);
	 * omsOrposPayment.setPaymentSeqNo(new BigDecimal(paymentDesc.getSeqNo()));
	 * omsOrposPayment.setPaymentType(paymentDesc.getPaymentType().value());
	 * omsOrposPayment.setCurrencyCode(paymentDesc.getCurrencyCode());
	 * omsOrposPayment.setAmount(paymentDesc.getAmount());
	 * omsOrposPayment.setAlternateCurrencyCode(paymentDesc.getAlternateCurrencyCode
	 * ()); omsOrposPayment.setAmount(paymentDesc.getAmount()); if(
	 * paymentDesc.getCreditDebitTender()!=null) {
	 * log.info("Entered the if loop for CreditDebitTender");
	 * log.info("Credit card payment"); CreditDebitTender creditDebitTender=
	 * paymentDesc.getCreditDebitTender();
	 * omsOrposPayment.setMaskedAccountNumber(checkNullValueForString(
	 * creditDebitTender.getMaskedAccountNumber())); log.info("masked acct no set");
	 * omsOrposPayment.setCardToken(checkNullValueForString(creditDebitTender.
	 * getCardToken())); log.info("Card token set");
	 * omsOrposPayment.setCardType(checkNullValueForString(creditDebitTender.
	 * getCardType().value())); log.info("Card Type is set");
	 * omsOrposPayment.setEntryMethod(checkNullValueForString(creditDebitTender.
	 * getEntryMethod().value()));
	 * omsOrposPayment.setAuthorizationCode(checkNullValueForString(
	 * creditDebitTender.getAuthorizationCode()));
	 * log.info("Authorization Code is set");
	 * if(creditDebitTender.getAuthorizationDatetime()!=null) {
	 * omsOrposPayment.setAuthorizationDatetime(new
	 * Timestamp(creditDebitTender.getAuthorizationDatetime().toGregorianCalendar().
	 * getTimeInMillis())); }
	 * omsOrposPayment.setAuthorizationMethod(checkNullValueForString(
	 * creditDebitTender.getAuthorizationMethod().value()));
	 * omsOrposPayment.setPersonalIdCountry(checkNullValueForString(
	 * creditDebitTender.getPersonalIdCountry()));
	 * omsOrposPayment.setPersonalIdState(checkNullValueForString(creditDebitTender.
	 * getPersonalIdState())); omsOrposPayment.setPersonalIdExpirationDate(new
	 * Timestamp(new java.util.Date().getTime()));
	 * omsOrposPayment.setPrepaidBalance(creditDebitTender.getPrepaidBalance());
	 * omsOrposPayment.setAccountApr(checkNullValueForString(creditDebitTender.
	 * getAccountApr()));
	 * omsOrposPayment.setAccountAprType(checkNullValueForString(creditDebitTender.
	 * getAccountAprType()));
	 * omsOrposPayment.setPromotionApr(checkNullValueForString(creditDebitTender.
	 * getPromotionApr()));
	 * omsOrposPayment.setPromotionAprType(checkNullValueForString(creditDebitTender
	 * .getPromotionAprType()));
	 * omsOrposPayment.setPromotionDescription(checkNullValueForString(
	 * creditDebitTender.getPromotionDescription()));
	 * omsOrposPayment.setPromotionDuration(checkNullValueForString(
	 * creditDebitTender.getPromotionDuration()));
	 * omsOrposPayment.setSettlementData(checkNullValueForString(creditDebitTender.
	 * getSettlementData()));
	 * omsOrposPayment.setSignatureData(checkNullValueForString(creditDebitTender.
	 * getSignatureData().toString()));
	 * omsOrposPayment.setAdditionalSecurityInfo(checkNullValueForString(
	 * creditDebitTender.getAdditionalSecurityInfo()));
	 * log.info("setting of values till AdditionalSecurityInfo is done"); }
	 * 
	 * log.info("Entering the if loop for CheckTender");
	 * if(paymentDesc.getCheckTender()!=null) {
	 * log.info("Entered the CheckTender if loop"); CheckTender checkTender=
	 * paymentDesc.getCheckTender();
	 * omsOrposPayment.setBankId(checkNullValueForString(checkTender.getBankId()));
	 * omsOrposPayment.setAccountNumber(checkNullValueForString(checkTender.
	 * getAccountNumber()));
	 * omsOrposPayment.setMicrNumber(checkNullValueForString(checkTender.
	 * getMicrNumber()));
	 * omsOrposPayment.setCheckNumber(checkNullValueForString(checkTender.
	 * getCheckNumber()));
	 * omsOrposPayment.setAuthorizationCode(checkNullValueForString(checkTender.
	 * getAuthorizationCode()));
	 * omsOrposPayment.setAuthorizationMethod(checkNullValueForString(checkTender.
	 * getAuthorizationMethod().value()));
	 * omsOrposPayment.setPersonalIdNumber(checkNullValueForString(checkTender.
	 * getPersonalIdNumber()));
	 * omsOrposPayment.setPersonalIdIssuer(checkNullValueForString(checkTender.
	 * getPersonalIdIssuer()));
	 * omsOrposPayment.setPersonalIdIssuerCoCode(checkNullValueForString(checkTender
	 * .getPersonalIdIssuerCoCode()));
	 * omsOrposPayment.setPersonalIdIssuerStateCode(checkNullValueForString(
	 * checkTender.getPersonalIdIssuerStateCode()));
	 * omsOrposPayment.setPersonalIdSwipedFlag(checkNullValueForString(checkTender.
	 * getPersonalIdSwipedFlag().value()));
	 * omsOrposPayment.setCustomerPhoneNumber(checkNullValueForString(checkTender.
	 * getCustomerPhoneNumber()));
	 * omsOrposPayment.setChecktenderEntryMethod(checkNullValueForString(checkTender
	 * .getEntryMethod().value()));
	 * omsOrposPayment.setEcheckConversionCode(checkNullValueForString(checkTender.
	 * getEcheckConversionCode().value()));
	 * log.info("Exited the if loop for CheckTender"); }
	 * 
	 * //Persisting into Payment - CouponTender
	 * log.info("Entering the if loop for CouponTender");
	 * if(paymentDesc.getCouponTender()!=null) {
	 * log.info("Entered the if loop for CouponTender"); CouponTender
	 * couponTender=paymentDesc.getCouponTender();
	 * omsOrposPayment.setCouponType(checkNullValueForString(couponTender.
	 * getCouponType().value()));
	 * omsOrposPayment.setCoupontenderEntryMethod(checkNullValueForString(
	 * couponTender.getEntryMethod().value()));
	 * omsOrposPayment.setCouponNumber(checkNullValueForString(couponTender.
	 * getCouponNumber())); log.info("Exited the if loop for CouponTender"); }
	 * 
	 * //Persisting into Payment - GiftCardTender
	 * if(paymentDesc.getGiftCardTender()!=null) {
	 * log.info("Entered the if loop for GiftCardTender"); GiftCardTender
	 * giftCardTender=paymentDesc.getGiftCardTender();
	 * omsOrposPayment.setCardNumber(checkNullValueForString(giftCardTender.
	 * getCardNumber()));
	 * omsOrposPayment.setGifcardAuthorizationCode(checkNullValueForString(
	 * giftCardTender.getAuthorizationCode()));
	 * omsOrposPayment.setGifcardAuthorizationDatetime(new Timestamp(new
	 * java.util.Date().getTime()));
	 * omsOrposPayment.setGifcardAuthorizationMethod(checkNullValueForString(
	 * giftCardTender.getAuthorizationMethod().value()));
	 * omsOrposPayment.setCreditFlag(checkNullValueForString(giftCardTender.
	 * getCreditFlag().value()));
	 * omsOrposPayment.setGifcardEntryMethod(checkNullValueForString(giftCardTender.
	 * getEntryMethod().value()));
	 * omsOrposPayment.setOriginalBalance(giftCardTender.getOriginalBalance());
	 * omsOrposPayment.setRemainingBalance(giftCardTender.getRemainingBalance());
	 * omsOrposPayment.setGifcardsettlementData(checkNullValueForString(
	 * giftCardTender.getSettlementData()));
	 * log.info("Exited the if loop for GiftCardTender"); }
	 * 
	 * //Persisting into Payment- GiftCertTender
	 * if(paymentDesc.getGiftCertTender()!=null) {
	 * log.info("Entered the if loop for GiftCertTender"); GiftCertTender
	 * giftCertTender=paymentDesc.getGiftCertTender();
	 * omsOrposPayment.setCertificateType(checkNullValueForString(giftCertTender.
	 * getCertificateType().value()));
	 * omsOrposPayment.setIssueLocationType(checkNullValueForString(giftCertTender.
	 * getIssueLocationType().value())); omsOrposPayment.setIssueLocationId(new
	 * BigDecimal(giftCertTender.getIssueLocationId()));
	 * omsOrposPayment.setSerialNumber(checkNullValueForString(giftCertTender.
	 * getSerialNumber())); log.info("Exited the if loop for GiftCertTender"); }
	 * 
	 * //Persisting into Payment Mail Check Tender
	 * if(paymentDesc.getMailCheckTender()!=null) {
	 * log.info("Entered the if loop for MailCheckTender"); MailCheckTender
	 * mailCheckTender=paymentDesc.getMailCheckTender();
	 * 
	 * mailCheckTender.getContactDesc(); if(mailCheckTender.getContactDesc()!=null)
	 * { log.info("Entered the if loop for ContactDesc"); ContactDesc contactDesc=
	 * mailCheckTender.getContactDesc();
	 * 
	 * saveContactDesc(contactDesc,"PAYMENT",new
	 * BigDecimal(paymentDesc.getSeqNo()),omsOrposCustOrdId);
	 * 
	 * OmsOrposContact omsOrposContact=
	 * session.getOmsOrposContactFindByPaymentSeqNo(paymentSeqNo,
	 * omsOrposCustOrdId);
	 * 
	 * //Optional if(contactDesc.getPhones()!=null) {
	 * log.info("Entered the if loop of getPhones()"); Phones
	 * phones=contactDesc.getPhones();
	 * savePhones(phones,omsOrposContact.getContactSeq(),omsOrposCustOrdId); }
	 * 
	 * //Optional if(contactDesc.getEmails()!=null) {
	 * log.info("Entered the if loop of getEmails()"); Emails emails=
	 * contactDesc.getEmails();
	 * saveEmails(emails,omsOrposContact.getContactSeq(),omsOrposCustOrdId); } }
	 * 
	 * if(mailCheckTender.getGeoAddrDesc()!=null) {
	 * log.info("Entered the if loop of GeoAddrDesc"); GeoAddrDesc
	 * geoAddrDesc=mailCheckTender.getGeoAddrDesc(); OmsOrposGeoaddr
	 * omsOrposGeoaddr=new OmsOrposGeoaddr();
	 * omsOrposGeoaddr.setOmsOrposCustOrderId(omsOrposCustOrdId);
	 * omsOrposGeoaddr.setAddressAlias(geoAddrDesc.getAddressAlias());
	 * omsOrposGeoaddr.setAddress1(geoAddrDesc.getAddress1());
	 * omsOrposGeoaddr.setAddress2(geoAddrDesc.getAddress2());
	 * omsOrposGeoaddr.setAddress3(geoAddrDesc.getAddress3());
	 * omsOrposGeoaddr.setAddress4(geoAddrDesc.getAddress4());
	 * omsOrposGeoaddr.setAddress5(geoAddrDesc.getAddress5());
	 * omsOrposGeoaddr.setCity(geoAddrDesc.getCity());
	 * omsOrposGeoaddr.setCounty(geoAddrDesc.getCounty());
	 * omsOrposGeoaddr.setStateCode(geoAddrDesc.getStateCode());
	 * omsOrposGeoaddr.setCountryCode(geoAddrDesc.getCountryCode());
	 * omsOrposGeoaddr.setCountryName(geoAddrDesc.getCountryName());
	 * omsOrposGeoaddr.setPostalCode(geoAddrDesc.getPostalCode());
	 * omsOrposGeoaddr.setJurisdictionCode(geoAddrDesc.getJurisdictionCode());
	 * session.persistOmsOrposGeoaddr(omsOrposGeoaddr);
	 * log.info("OmsOrposGeoAddr persisted successfully"); }
	 * omsOrposPayment.setPersonalIdType(mailCheckTender.getPersonalIdType());
	 * log.info("Exited the loop for MailCheckTender"); }
	 * 
	 * //Persisting into PurchaseOrdTender
	 * if(paymentDesc.getPurchaseOrdTender()!=null) {
	 * log.info("Entered the if loop for PurchaseOrdTender"); PurchaseOrdTender
	 * purchaseOrdTender=paymentDesc.getPurchaseOrdTender();
	 * omsOrposPayment.setAgentName(purchaseOrdTender.getAgentName());
	 * log.info("Agent Name value is set");
	 * omsOrposPayment.setPurchaseOrderNumber(purchaseOrdTender.
	 * getPurchaseOrderNumber()); log.info("Purchase Order Number value is set");
	 * log.info("Exited the if loop for PurchaseOrdTender"); }
	 * 
	 * //Persisting into StoreCreditTender
	 * if(paymentDesc.getStoreCreditTender()!=null) {
	 * log.info("Entered the if loop for StoreCreditTender"); StoreCreditTender
	 * storeCreditTender=new StoreCreditTender();
	 * log.info("Setting StoreCreditTender object"); CertificateType
	 * certificateType=paymentDesc.getStoreCreditTender().getCertificateType();
	 * String certificate=certificateType.value();
	 * log.info("CertifficateType---"+certificate);
	 * omsOrposPayment.setStrcrtenderCertificateType(checkNullValueForString(
	 * paymentDesc.getStoreCreditTender().getCertificateType().value()));
	 * log.info("StoreTenderCertificateType is set");
	 * omsOrposPayment.setFirstName(checkNullValueForString(storeCreditTender.
	 * getFirstName())); log.info("Setting the first name");
	 * omsOrposPayment.setLastName(checkNullValueForString(storeCreditTender.
	 * getLastName())); log.info("Setting the last name");
	 * omsOrposPayment.setStrcrtenderPersonalIdType(checkNullValueForString(
	 * storeCreditTender.getPersonalIdType()));
	 * log.info("Setting the PersonalIDType");
	 * omsOrposPayment.setState(checkNullValueForString(paymentDesc.
	 * getStoreCreditTender().getState().value())); log.info("Setting the state");
	 * omsOrposPayment.setStoreCreditId(checkNullValueForString(storeCreditTender.
	 * getStoreCreditId())); log.info("Setting the Store Credit ID");
	 * log.info("Exited the if loop for StoreCreditTender"); }
	 * 
	 * //Persisting into TravelCheckTender
	 * if(paymentDesc.getTravelCheckTender()!=null) {
	 * log.info("Entered the if loop for TravelCheckTender"); TravelCheckTender
	 * travelCheckTender=new TravelCheckTender();
	 * log.info("TravelCheck object created");
	 * omsOrposPayment.setCheckCount(travelCheckTender.getCheckCount());
	 * log.info("Check count value is set");
	 * log.info("Exited the if loop for TravelCheckTender"); }
	 * session.persistOmsOrposPayment(omsOrposPayment);
	 * log.info("Successfully in OmsOrposPayment"); } }
	 * 
	 * //Persisting into Contact table public void saveContactDesc(ContactDesc
	 * contactDesc, String headerNode,BigDecimal headerSeq,BigDecimal
	 * omsOrposCustOrdId) throws SOAPException {
	 * log.info("Entered the saveContactDesc method"); OMSUtilSessionEJB session =
	 * OMSUtil.doLookup(); CustOrderPicVo custOrderPicVo=new CustOrderPicVo();
	 * //omsOrposCustOrdId=session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(
	 * custOrderPicVo.getCustomerOrderId()); log.info("headerNode----"+headerNode);
	 * log.info("headerSeq-----"+headerSeq); OmsOrposContact omsOrposContact=new
	 * OmsOrposContact();
	 * 
	 * //Setting the Payment Sequence omsOrposContact.setPaymentSeqNo(headerSeq);
	 * 
	 * omsOrposContact.setOmsOrposCustOrderId(omsOrposCustOrdId);
	 * omsOrposContact.setFirstName(checkNullValueForString(contactDesc.getFirstName
	 * ())); omsOrposContact.setPhoneticFirst(checkNullValueForString(contactDesc.
	 * getPhoneticFirst()));
	 * omsOrposContact.setLastName(checkNullValueForString(contactDesc.getLastName()
	 * )); omsOrposContact.setMiddleName(checkNullValueForString(contactDesc.
	 * getMiddleName()));
	 * omsOrposContact.setPreferredName(checkNullValueForString(contactDesc.
	 * getPreferredName()));
	 * omsOrposContact.setNamePrefix(checkNullValueForString(contactDesc.
	 * getNamePrefix()));
	 * omsOrposContact.setNameSuffix(checkNullValueForString(contactDesc.
	 * getNameSuffix()));
	 * omsOrposContact.setCompanyName(checkNullValueForString(contactDesc.
	 * getCompanyName())); session.persistOmsOrposContact(omsOrposContact);
	 * log.info("Successfully persisted in omsOrposContact"); // OmsOrposContact
	 * omsOrposContact1=session.getOmsOrposContactFindByOmsOrposCustOrderId(
	 * omsOrposCustOrderId); //contactSeq=omsOrposContact1.getContactSeq(); //
	 * log.info("inside save contact contactSeq"+contactSeq); }
	 */

	// Persisting into Phones table

	public void savePhones(Phones phones, BigDecimal headerSeq, BigDecimal omsOrposCustOrdId) throws SOAPException {
		log.info("Entered the savePhones method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderPicVo custOrderPicVo = new CustOrderPicVo();
		// omsOrposCustOrdId=session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderPicVo.getCustomerOrderId());
		List<PhoneDesc> phoneDescList = phones.getPhoneDesc();
		for (PhoneDesc phoneDesc : phoneDescList) {
			log.info("Entered the loop for phoneDesc");
			// Change th FK constraint in dB FK_CD_OMS_ORPOS_PHONE
			// Chnage th PK ,add seq and make it as PK
			OmsOrposPhone omsOrposPhone = new OmsOrposPhone();
			// omsOrposPhone.setHeaderNode(headerNode);
			omsOrposPhone.setContactSeq(headerSeq);
			omsOrposPhone.setOmsOrposCustOrderId(omsOrposCustOrdId);
			omsOrposPhone.setPhoneId(phoneDesc.getPhoneId());
			omsOrposPhone.setPhoneNumber(phoneDesc.getPhoneNumber());
			omsOrposPhone.setPhoneType(checkNullValueForString(phoneDesc.getPhoneType().value()));
			omsOrposPhone.setPhoneExtension(checkNullValueForString(phoneDesc.getPhoneExtension()));
			omsOrposPhone.setPrimaryPhoneInd(phoneDesc.getPrimaryPhoneInd());
			session.persistOmsOrposPhone(omsOrposPhone);
		}
		log.info("Successfully persisted in omsOrposPhone");
	}

	public void saveEmails(Emails emails, BigDecimal headerSeq, BigDecimal omsOrposCustOrdId) throws SOAPException {
		log.info("Entered the saveEmails method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderPicVo custOrderPicVo = new CustOrderPicVo();
		// omsOrposCustOrdId=session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderPicVo.getCustomerOrderId());
		List<EmailDesc> emailDescList = emails.getEmailDesc();
		for (EmailDesc emailDesc : emailDescList) {
			log.info("Entered the loop for EmailDesc");
			OmsOrposEmail omsOrposEmail = new OmsOrposEmail();
			omsOrposEmail.setContactSeq(headerSeq);
			// omsOrposEmail.setHeaderNode(headerNode);
			omsOrposEmail.setOmsOrposCustOrderId(omsOrposCustOrdId);
			omsOrposEmail.setEmailId(emailDesc.getEmailId());
			omsOrposEmail.setEmailAddress(checkNullValueForString(emailDesc.getEmailAddress()));
			omsOrposEmail.setEmailType(checkNullValueForString(emailDesc.getEmailType().value()));
			omsOrposEmail.setPrimaryEmailInd(checkNullValueForString(emailDesc.getPrimaryEmailInd()));
			session.persistOmsOrposEmail(omsOrposEmail);
		}
		log.info("Successfully persisted in omsOrposEmail");
	}

	public boolean isAllItemsCancelled(String custOrderNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrderNo = null;
		int cancel = 0;
		omsCustOrderNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
		for (OmsCustOrdItem omsCustOrdItemLoop : omsCustOrdItemList) {
			if (omsCustOrdItemLoop.getQtyOrderedSuom().equals(omsCustOrdItemLoop.getQtyCancelled())) {

			} else {
				cancel = 1;
			}
		}
		if (cancel == 1) {
			return false;
		} else {
			return true;
		}

	}

	// Method to update the OmsCustOrdHead

	public void updateOmsCustOrdHead(String custOrderNo) throws SOAPException {
		log.info("Inside OmsCustOrdHead method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		System.out.println("============================Inside updateOmsCustOrdHead method========================");
		BigDecimal omsCustOrderNo = null;
		// For B2B orders
		/*
		 * String subOrderNo= custOrderNo.substring(custOrderNo.length() -3);
		 * custOrderNo = custOrderNo.substring(0, custOrderNo.length() -3);
		 * log.info("custOrderNo: "+custOrderNo+" subOrderNo: "+subOrderNo); while
		 * ((subOrderNo.length() > 1) && (subOrderNo.charAt(0) == '0')) { subOrderNo=
		 * subOrderNo.substring(1); }
		 * log.info("After deleting the leading zeros "+subOrderNo); //omsCustOrderNo =
		 * session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); omsCustOrderNo =
		 * session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo,
		 * subOrderNo); log.info("omsCustOrderNo: "+omsCustOrderNo);
		 */
		omsCustOrderNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);

		// omsCustOrderNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);

		BigDecimal unitRetailPrice = null;
		BigDecimal tenderAmount = null;
		BigDecimal sumOfQtyForCloseDatetime = null;
		try {
			List<OmsCustOrdHead> omsCustOrdHeadList = session.getOmsCustOrdHeadFindColumns(custOrderNo);
			for (OmsCustOrdHead omsCustOrdHead : omsCustOrdHeadList) {
				log.info("Inside the OmsCustOrdHead loop " + omsCustOrderNo);
				unitRetailPrice = session.getOmsCustOrdItemSumUnitRetail(omsCustOrderNo);
				tenderAmount = session.getOmsCustOrdTenderSumOfTenderAmt(omsCustOrderNo);
				log.info("tenderAmount before checking for null" + tenderAmount);

				sumOfQtyForCloseDatetime = session.getOmsCustOrdItemFindSumOfQtysForCloseDateTime(omsCustOrderNo);
				log.info("&&&&&&&&  sumOfQtyForCloseDatetime : " + sumOfQtyForCloseDatetime + "&&&&&&&&&");
				omsCustOrdHead.setLastUpdateDatetime(new Timestamp(new java.util.Date().getTime()));
				if (isAllItemsCancelled(custOrderNo)) {
					log.info("&&&&&&&&  sumOfQtyForCloseDatetime : " + sumOfQtyForCloseDatetime + "&&&&&&&&&");
					omsCustOrdHead.setCancelDatetime(new Timestamp(new java.util.Date().getTime()));
				}
				if (sumOfQtyForCloseDatetime.intValue() == 0) {
					log.info("&&&&&&&&  sumOfQtyForCloseDatetime : " + sumOfQtyForCloseDatetime + "&&&&&&&&&");
					omsCustOrdHead.setCloseDatetime(new Timestamp(new java.util.Date().getTime()));
				}
				session.mergeOmsCustOrdHead(omsCustOrdHead);
				log.info("Updated OmsCustOrdHead successfully");
			}

		} catch (Exception e) {
			log.info("Exception occured " + e.getMessage());
		}

	}

	// Method for updating OmsCustOrdItem table from the request that is coming in
	// from xml

	public void updateOmsCustOrdItem(String custOrderNo, List<CustOrdItmPkVo> pickupList) throws SOAPException {
		log.info("Inside OmsCustOrdItem method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal maxPickSeqNo = session.getMaxOmsOrposCustOrderPickUp(custOrderNo);
		BigDecimal omsCustOrdNo;

		// Initializing values for OmsCustOrdItem
		BigDecimal cumQtyDelivered = null;
		BigDecimal qtyCancelled = null;
		BigDecimal lineItemNoItm = null;

		// Initializing values for OmsOrposCustOrdItm
		BigDecimal completedQuantity = null;
		BigDecimal cancelledQuantity = null;
		BigDecimal lineItemNoPickUp = null;

		// For B2B orders
		/*
		 * String subOrderNo= custOrderNo.substring(custOrderNo.length() -3);
		 * custOrderNo = custOrderNo.substring(0, custOrderNo.length() -3);
		 * log.info("custOrderNo: "+custOrderNo+" subOrderNo: "+subOrderNo); while
		 * ((subOrderNo.length() > 1) && (subOrderNo.charAt(0) == '0')) { subOrderNo=
		 * subOrderNo.substring(1); }
		 * log.info("After deleting the leading zeros "+subOrderNo); //omsCustOrderNo =
		 * session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); omsCustOrdNo =
		 * session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo,
		 * subOrderNo); log.info("omsCustOrderNo: "+omsCustOrdNo);
		 */
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);

		// omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdNo);
		for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
			lineItemNoItm = omsCustOrdItem.getLineNo();
			cumQtyDelivered = omsCustOrdItem.getCumQtyDelivered();
			qtyCancelled = omsCustOrdItem.getQtyCancelled();
			if (cumQtyDelivered == null || qtyCancelled == null) {
				cumQtyDelivered = new BigDecimal(0);
				qtyCancelled = new BigDecimal(0);
			}
			for (CustOrdItmPkVo pickUpItems : pickupList) {
				lineItemNoPickUp = new BigDecimal(pickUpItems.getLineItemNo());
				completedQuantity = pickUpItems.getCompletedQuantity();
				cancelledQuantity = pickUpItems.getCancelledQuantity();

				log.info("omsCustOrdItem.getCumQtyDelivered() " + omsCustOrdItem.getCumQtyDelivered());
				log.info("omsCustOrdItem.getQtyCancelled() " + omsCustOrdItem.getQtyCancelled());
				log.info("omsCustOrdItem.getQtyOrderedSuom() " + omsCustOrdItem.getQtyOrderedSuom());
				log.info("completedQuantity " + completedQuantity.intValue());
				log.info("Adding " + (omsCustOrdItem.getCumQtyDelivered().intValue()
						+ omsCustOrdItem.getQtyCancelled().intValue() + completedQuantity.intValue()));

				if (pickUpItems.getLineItemNo() == omsCustOrdItem.getLineNo().intValue()) {
					log.info("inside lineNo equal");
					if ((omsCustOrdItem.getCumQtyDelivered().intValue() + omsCustOrdItem.getQtyCancelled().intValue()
							+ completedQuantity.intValue()) > omsCustOrdItem.getQtyOrderedSuom().intValue()) {
						log.info("inside valiation for optimazation for pickUp");

						status = "F";
						error = "ERROR_201";
						break;
					}
				}

				int lineValue = lineItemNoItm.compareTo(lineItemNoPickUp);

				if (lineValue == 0) {

					log.info("updating the delivered qty for line No " + pickUpItems.getLineItemNo() + "qty to :"
							+ cumQtyDelivered.add(completedQuantity));
					omsCustOrdItem.setCumQtyDelivered(cumQtyDelivered.add(completedQuantity));
					omsCustOrdItem.setDeliveryDateTime(new Timestamp(new Date().getTime()));
					log.info("updating the Cancelled  qty for line No " + pickUpItems.getLineItemNo() + "qty to :"
							+ qtyCancelled.add(cancelledQuantity));
					log.info("updating the Delivery Date and time for line No " + pickUpItems.getLineItemNo() + "Date :"
							+ new Timestamp(new Date().getTime()));
					omsCustOrdItem.setQtyCancelled(qtyCancelled.add(cancelledQuantity));
					session.mergeOmsCustOrdItem(omsCustOrdItem);
					log.info("Updated OmsCustOrdItem successfully");
				}
			}
		}
	}

	// Method for updating OmsCoFulfillDetail

	public void updateOmsCoFulfillDetail(String custOrderNo, List<CustOrdItmPkVo> pickUpList) throws SOAPException {
		log.info("Inside update Co fulfill method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		String item;
		BigDecimal maxPickSeqNo = session.getMaxOmsOrposCustOrderPickUp(custOrderNo);

		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);

		// omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// Initializing the values for OmsCoFulfillDetail
		BigDecimal fulfillDeliverQty = null;
		BigDecimal fulfillCancelQty = null;
		String fulfillOrderId = null;
		String fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item = null;

		// Initializing the values for OmsOrposCustOrdItm
		int completedQuantity = 0;
		BigDecimal cancelledQuantity = null;
		BigDecimal lineItemNoPickUp = null;
		List<OmsCoFulfillDetail> omsCoFulfillDetail = session.getOmsCoFulfillDetailFindColumns(omsCustOrdNo);

		for (CustOrdItmPkVo pickupList : pickUpList) {
			lineItemNoPickUp = new BigDecimal(pickupList.getLineItemNo());
			completedQuantity = pickupList.getCompletedQuantity().intValue();
			cancelledQuantity = pickupList.getCancelledQuantity();
			for (OmsCoFulfillDetail omsCoFulFillDetailLoop : omsCoFulfillDetail) {
				fulfillOrderId = omsCoFulFillDetailLoop.getFulfillOrderNo().toString();
				fulfillDeliverQty = omsCoFulFillDetailLoop.getFulfillDeliverQty();
				fulfillCancelQty = omsCoFulFillDetailLoop.getFulfillCancelQty();

				if (fulfillDeliverQty == null || fulfillCancelQty == null) {
					fulfillDeliverQty = new BigDecimal(0);
					fulfillCancelQty = new BigDecimal(0);
				}

				if (cancelledQuantity == null) {
					// completedQuantity = new BigDecimal(0);
					cancelledQuantity = new BigDecimal(0);
				}

				if (lineItemNoPickUp.equals(omsCoFulFillDetailLoop.getLineNo())
						&& omsCoFulFillDetailLoop.getSourceLoc().equals(omsCoFulFillDetailLoop.getFulfillLoc())) {
					int quantity = getPickandDeliveryFulfilorderNo(custOrderNo, omsCoFulFillDetailLoop.getItem(),
							omsCoFulFillDetailLoop.getFulfillOrderNo().intValue());
					log.info("PickedQty is greater than zer for fulfilOrderNo "
							+ omsCoFulFillDetailLoop.getFulfillOrderNo());
					log.info("quantity " + quantity);
					if (completedQuantity != 0 && (omsCoFulFillDetailLoop.getFulfillConfQty()
							.intValue() != omsCoFulFillDetailLoop.getFulfillDeliverQty().intValue()) && quantity > 0) {
						log.info("completedQuantity " + completedQuantity);
						if (completedQuantity > omsCoFulFillDetailLoop.getFulfillConfQty().intValue()) {
							log.info(
									"inside completedQuantity>omsCoFulFillDetailLoop.getFulfillConfQty() if condition");
							fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item = omsCoFulFillDetailLoop
									.getFulfillOrderNo().toString() + ","
									+ omsCoFulFillDetailLoop.getLineNo().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLoc().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLocType() + ","
									+ omsCoFulFillDetailLoop.getFulfillLoc().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLocType() + ","
									+ omsCoFulFillDetailLoop.getItem();
							log.info("============Siebel data for PickUp=================");
							log.info("completedQuantity " + completedQuantity);
							omsCoFulFillDetailLoop.setFulfillDeliverQty(
									fulfillDeliverQty.add(omsCoFulFillDetailLoop.getFulfillConfQty()));
							completedQuantity = completedQuantity
									- omsCoFulFillDetailLoop.getFulfillDeliverQty().intValue();
							siebelDataforPickUp.put(fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item,
									omsCoFulFillDetailLoop.getFulfillConfQty());
							log.info("fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item "
									+ fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item);
							log.info("After subtracting the Completedqty " + completedQuantity);
							log.info("Updating the delivery qty for in omsCoFulfil "
									+ fulfillDeliverQty.add(omsCoFulFillDetailLoop.getFulfillConfQty()));
							try {
								session.mergeOmsCoFulfillDetail(omsCoFulFillDetailLoop);
							} catch (Exception e) {
								log.info(
										"Exception while updating the delivered Qty inside if(completedQuantity>omsCoFulFillDetailLoop.getFulfillConfQty().intValue()) "
												+ e.getMessage());
							}
						} else {
							log.info("inside else ");
							log.info("============Siebel data for PickUp in else block=================");
							log.info("completedQuantity " + completedQuantity);
							log.info("Updating the delivery qty for in omsCoFulfil inside else  " + completedQuantity);
							omsCoFulFillDetailLoop.setFulfillDeliverQty(omsCoFulFillDetailLoop.getFulfillDeliverQty()
									.add(new BigDecimal(completedQuantity)));
							log.info("setted deliveredqty " + omsCoFulFillDetailLoop.getFulfillDeliverQty());
							fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item = omsCoFulFillDetailLoop
									.getFulfillOrderNo().toString() + ","
									+ omsCoFulFillDetailLoop.getLineNo().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLoc().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLocType() + ","
									+ omsCoFulFillDetailLoop.getFulfillLoc().toString() + ","
									+ omsCoFulFillDetailLoop.getSourceLocType() + ","
									+ omsCoFulFillDetailLoop.getItem();
							siebelDataforPickUp.put(fOrderNo_LineNo_Src_SrcType_FulfilLoc_FulfilType_Item,
									new BigDecimal(completedQuantity));
							completedQuantity = 0;
							try {
								session.mergeOmsCoFulfillDetail(omsCoFulFillDetailLoop);
							} catch (Exception e) {
								log.info("Exception while updating the delivered Qty inside else " + e.getMessage());
							}
							break;
						}
					}
					omsCoFulFillDetailLoop.setFulfillCancelQty(fulfillCancelQty.add(cancelledQuantity));
					session.mergeOmsCoFulfillDetail(omsCoFulFillDetailLoop);
				}
			}
		}
	}

	// Method for persisting into OmsCustOrdLog for PickUp scenario

	public void persistOmsCustOrdLogPickup(String custOrderNo) throws SOAPException {
		log.info("Inside the persistence method of OmsCustOrdLogPickup table");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		// For B2B orders
		/*
		 * String subOrderNo= custOrderNo.substring(custOrderNo.length() -3);
		 * custOrderNo = custOrderNo.substring(0, custOrderNo.length() -3);
		 * log.info("custOrderNo: "+custOrderNo+" subOrderNo: "+subOrderNo); while
		 * ((subOrderNo.length() > 1) && (subOrderNo.charAt(0) == '0')) { subOrderNo=
		 * subOrderNo.substring(1); }
		 * log.info("After deleting the leading zeros "+subOrderNo); //omsCustOrderNo =
		 * session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); omsCustOrdNo =
		 * session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo,
		 * subOrderNo); log.info("omsCustOrderNo: "+omsCustOrdNo);
		 */
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);
		// BigDecimal omsCustOrderNo =
		// session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal maxLogSeqNo=session.getOmsCustOrdLogFindMaxLogSeqNo();
		try {
			OmsCustOrdLog omsCustOrdLog = new OmsCustOrdLog();
			omsCustOrdLog.setOmsCustOrdNo(omsCustOrdNo);
			// omsCustOrdLog.setLogSeqNo(maxLogSeqNo);
			omsCustOrdLog.setOmsDlvConfId(null);
			omsCustOrdLog.setEventId("DL");
			omsCustOrdLog.setEventComments("Item PickUp");
			omsCustOrdLog.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsCustOrdLog.setOmsCancelId(null);
			session.persistOmsCustOrdLog(omsCustOrdLog);
		} catch (Exception e) {
		}
	}

	// Method for persisting data into OmsCustOrdLog during Cancellation scenario
	/*
	 * public void persistOmsCustOrdLogCancel(String custOrderNo) throws
	 * SOAPException { log.info("Inside the OmsCustOrdLogCancel method");
	 * OMSUtilSessionEJB session = OMSUtil.doLookup(); BigDecimal
	 * omsCustOrderNo=session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); //
	 * BigDecimal maxLogSeqNo=session.getOmsCustOrdLogFindMaxLogSeqNo(); try {
	 * OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup=new
	 * OmsOrposCustOrdItmPickup(); OmsCustOrdLog omsCustOrdLog = new
	 * OmsCustOrdLog(); omsCustOrdLog.setOmsCustOrdNo(omsCustOrderNo); //
	 * omsCustOrdLog.setLogSeqNo(maxLogSeqNo); omsCustOrdLog.setOmsDlvConfId(null);
	 * omsCustOrdLog.setEventId("CA");
	 * omsCustOrdLog.setEventComments("Item Cancelled");
	 * omsCustOrdLog.setCreateDatetime(new Timestamp(new Date().getTime()));
	 * omsCustOrdLog.setOmsCancelId(omsOrposCustOrdItmPickup.getCustOrderPicVoSeq())
	 * ; session.persistOmsCustOrdLog(omsCustOrdLog); } catch(Exception e) {
	 * 
	 * } }
	 */

	// Method for persisting data into OmsCustOrdLogItem for PickUp scenario

	public void persistCustOrdLogItemPickup(String custOrderNo, List<CustOrdItmPkVo> pickUpItemsList)
			throws SOAPException {
		log.info("inside persistCustOrdLogItemPickup");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String item = null;
		BigDecimal lineItemNo = null;
		BigDecimal fulfillOrderNo = null;
		BigDecimal omsCustOrdNo;
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);

		// BigDecimal omsCustOrdNo =
		// session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal omsCustOrderNo =
		// session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		BigDecimal maxLogSeqNo = session.getOmsCustOrdLogFindMaxLogSeqNo(omsCustOrdNo);
		OmsCustOrdLogItem omsCustOrdLogItem = new OmsCustOrdLogItem();
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdNo);
		for (OmsCustOrdItem omsCustOrdItemLoop : omsCustOrdItemList) {
			for (OmsCoFulfillDetail omsCoFulfillDetailLoop : omsCoFulfillDetailList) {
				for (CustOrdItmPkVo custOrdItmPkVo : pickUpItemsList) {
					if (custOrdItmPkVo.getLineItemNo() == omsCustOrdItemLoop.getLineNo().intValue()) {
						// log.info("both line No are equal");
						if (omsCoFulfillDetailLoop.getLineNo().intValue() == omsCustOrdItemLoop.getLineNo().intValue()
								&& omsCoFulfillDetailLoop.getItem().equals(omsCustOrdItemLoop.getItem())
								&& omsCoFulfillDetailLoop.getSourceLoc().intValue() == omsCoFulfillDetailLoop
										.getFulfillLoc().intValue()) {
							BigDecimal logItemqty = BigDecimal.ZERO;
							log.info("src loc and fullfill loc are equal ");
							item = omsCustOrdItemLoop.getItem();
							omsCustOrdLogItem.setItem(item);

							lineItemNo = omsCustOrdItemLoop.getLineNo();
							omsCustOrdLogItem.setLineNo(lineItemNo);
							omsCustOrdLogItem.setLogSeqNo(maxLogSeqNo);
							log.info("MaxLogSeqNo-----" + maxLogSeqNo);
							omsCustOrdLogItem.setQty(omsCustOrdItemLoop.getCumQtyDelivered());
							fulfillOrderNo = omsCoFulfillDetailLoop.getFulfillOrderNo();
							log.info(" Line No :" + lineItemNo + " Item :" + item
									+ " omsCoFulfillDetailLoop.getSourceLoc() : "
									+ omsCoFulfillDetailLoop.getSourceLoc()
									+ " omsCoFulfillDetailLoop.getFulfillLoc() : "
									+ omsCoFulfillDetailLoop.getFulfillLoc()
									+ " omsCustOrdItemLoop.getCumQtyDelivered() : "
									+ omsCustOrdItemLoop.getCumQtyDelivered());
							log.info("fulfillOrderNo " + fulfillOrderNo);
							omsCustOrdLogItem.setFulfillOrderNo(fulfillOrderNo);
							omsCustOrdLogItem.setCreateDatetime(new Timestamp(new Date().getTime()));
							try {
								session.persistOmsCustOrdLogItem(omsCustOrdLogItem);
								log.info("Successfully persisted in CustordLogItemPickup");
							} catch (Exception e) {
								logItemqty = session.getOmsCustOrdLogLineItemandLogSeqNo(omsCustOrdItemLoop.getLineNo(),
										omsCustOrdItemLoop.getItem(), maxLogSeqNo);
								omsCustOrdLogItem.setQty(logItemqty.add(omsCustOrdLogItem.getQty()));
								session.mergeOmsCustOrdLogItem(omsCustOrdLogItem);
							}
						}
					}
				}
			}

		}
		// notifySiebelForpickUp(custOrderNo,omsCustOrdItemList,omsCoFulfillDetailList,pickUpItemsList);
		if (pickUpItemsList.size() > 0) {
			notifySiebelForpickUp(custOrderNo, pickUpItemsList);
		}

	}

	public void notifySiebelForpickUp(String custOrderNo, List<CustOrdItmPkVo> pickUpItemsList) throws SOAPException {
		log.info("Inside notifySiebelForpickUp ");
		POSTransactionBean pOSTransactionBean = new POSTransactionBean();
		log.info("calling pickFromPos ");
		PosTransactionMsg posTransactionMessageCallStatus = pOSTransactionBean.pickFromPos(custOrderNo,
				transactionNumber, siebelDataforPickUp);
		log.info("called pickFromPos");

		if (posTransactionMessageCallStatus.getPos_transacation_call() == Boolean.FALSE) {
			// We need to insert into republish data table....
			String xmlMsg = posTransactionMessageCallStatus
					.getPosTranscationXMLMessage(posTransactionMessageCallStatus.getPosTransactionDesc());
			log.info("The XML message is " + xmlMsg);
			log.info(" Inserting record into republish data table");
			posTransactionMessageCallStatus.insertRecordIntoRepublishDataForPos_Transaction(xmlMsg, custOrderNo);

		}

		InterfacePersistence interfacePersistance = new InterfacePersistence();
		log.info("Calling siebel for pickUp " + custOrderNo);
		try {
			// interfacePersistance.callSeibelWebserviceForPickUp(custOrderNo, "1",
			// pickUpItemsList, siebelDataforPickUp);
			OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderpickup = interfacePersistance
					.getOmsOrderStatusUpdateHeaderForPickup(custOrderNo, "1", pickUpItemsList, siebelDataforPickUp);
			
			/*
			 * OmsStatusUpdateForReturnPickupCancellation
			 * omsStatusUpdateForReturnPickupCancellationWSCall = new
			 * OmsStatusUpdateForReturnPickupCancellation();
			 * omsStatusUpdateForReturnPickupCancellationWSCall
			 * .callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(
			 * omsOrderStatusUpdateHeaderpickup);
			 */
			 
			interfacePersistance.saveOmsWSeibelRequest(omsOrderStatusUpdateHeaderpickup, OMSUtil.doLookup());
			

		} catch (Exception e) {
			log.info(" Exception while saving pickUp Siebel request" + e.getMessage());
		}

	}

	// Method for persisting data into OmsCustOrdLogItem when cancellation scenario
	/*
	 * public void persistCustOrdLogItemCancel(String custOrderNo) throws
	 * SOAPException { OMSUtilSessionEJB session = OMSUtil.doLookup(); BigDecimal
	 * omsCustOrderNo=session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); try {
	 * BigDecimal
	 * maxLogSeqNo=session.getOmsCustOrdLogFindMaxLogSeqNo(omsCustOrderNo);
	 * OmsCustOrdItem omsCustOrdItem=new OmsCustOrdItem(); OmsCoFulfillDetail
	 * omsCoFulfillDetail=new OmsCoFulfillDetail(); OmsCustOrdLogItem
	 * omsCustOrdLogItem = new OmsCustOrdLogItem();
	 * omsCustOrdLogItem.setItem(omsCustOrdItem.getItem());
	 * omsCustOrdLogItem.setLogSeqNo(maxLogSeqNo);
	 * omsCustOrdLogItem.setQty(omsCustOrdItem.getQtyCancelled());
	 * omsCustOrdLogItem.setFulfillOrderNo(omsCoFulfillDetail.getFulfillOrderNo());
	 * omsCustOrdLogItem.setCreateDatetime(new Timestamp(new Date().getTime()));
	 * session.persistOmsCustOrdLogItem(omsCustOrdLogItem); } catch(Exception e) {
	 * 
	 * } }
	 */

	// Method for persisting data into OmsRtlogPublishLog for PickUp scenario

	public void persistOmsRtlogPublishLogPickup(String custOrderNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		// For B2B orders
		/*
		 * String subOrderNo= custOrderNo.substring(custOrderNo.length() -3);
		 * custOrderNo = custOrderNo.substring(0, custOrderNo.length() -3);
		 * log.info("custOrderNo: "+custOrderNo+" subOrderNo: "+subOrderNo); while
		 * ((subOrderNo.length() > 1) && (subOrderNo.charAt(0) == '0')) { subOrderNo=
		 * subOrderNo.substring(1); }
		 * log.info("After deleting the leading zeros "+subOrderNo); //omsCustOrderNo =
		 * session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo); omsCustOrdNo =
		 * session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo,
		 * subOrderNo); log.info("omsCustOrderNo: "+omsCustOrdNo);
		 */
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);

		// BigDecimal omsCustOrderNo =
		// session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		String item = null;
		BigDecimal fulfillLoc = null;
		BigDecimal fulfillOrderId = null;
		BigDecimal lineItemNo = null;
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
		// BigDecimal
		// omsRtlogPubSeqNo=session.getOmsRtlogPublishLogFindMaxOmsRtLogPubSeqNo();
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdNo);
		try {
			omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
		} catch (Exception e) {

		}
		OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
		// omsRtlogPublishLog.setOmsRtlogPubSeqNo(omsRtlogPubSeqNo);
		for (OmsCustOrdItem omsCustOrdItemLoop : omsCustOrdItemList) {
			item = omsCustOrdItemLoop.getItem();
			omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrdNo);
			lineItemNo = omsCustOrdItemLoop.getLineNo();
			omsRtlogPublishLog.setLineNo(lineItemNo);
			omsRtlogPublishLog.setItem(item);
			omsRtlogPublishLog.setQty(omsCustOrdItemLoop.getCumQtyDelivered());
			for (OmsCoFulfillDetail omsCofulfillDetailLoop : omsCoFulfillDetailList) {
				fulfillOrderId = omsCofulfillDetailLoop.getFulfillOrderNo();
				fulfillLoc = omsCofulfillDetailLoop.getFulfillLoc();
			}
			omsRtlogPublishLog.setLocation(fulfillLoc);
			omsRtlogPublishLog.setFulfillOrderNo(fulfillOrderId);
			omsRtlogPublishLog.setTranType("ORD");
			omsRtlogPublishLog.setPublishedInd("N"); // need to confirm its value
			omsRtlogPublishLog.setErrorMessage(null);
			omsRtlogPublishLog.setOmsCancelId(null);
			omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
			omsRtlogPublishLog.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
			log.info("Successfully persisted in OmsRtlogPublishLog");
		}

	}

	// Method for persisting into OmsRtlogPublishLog during Cancellation scenario
	/*
	 * public void persistOmsRtlogPublishLogCancel(String custOrderNo) throws
	 * SOAPException { OMSUtilSessionEJB session = OMSUtil.doLookup(); BigDecimal
	 * omsCustOrderNo=session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
	 * //BigDecimal
	 * omsRtlogPubSeqNo=session.getOmsRtlogPublishLogFindMaxOmsRtLogPubSeqNo(); try
	 * { OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
	 * OmsCoFulfillDetail omsCoFulfillDetail=new OmsCoFulfillDetail();
	 * OmsCustOrdItem omsCustOrdItem=new OmsCustOrdItem();
	 * //omsRtlogPublishLog.setOmsRtlogPubSeqNo(omsRtlogPubSeqNo);
	 * omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrderNo);
	 * omsRtlogPublishLog.setFulfillOrderNo(omsCoFulfillDetail.getFulfillOrderNo());
	 * omsRtlogPublishLog.setItem(omsCustOrdItem.getItem());
	 * omsRtlogPublishLog.setQty(omsCustOrdItem.getCumQtyDelivered());
	 * omsRtlogPublishLog.setLocation(omsCoFulfillDetail.getFulfillLoc());
	 * omsRtlogPublishLog.setTranType("ORC");
	 * omsRtlogPublishLog.setPublishedInd("N"); //need to confirm its value
	 * omsRtlogPublishLog.setErrorMessage(null);
	 * omsRtlogPublishLog.setOmsCancelId(null);
	 * omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
	 * omsRtlogPublishLog.setLastUpdateDatetime(new Timestamp(new
	 * Date().getTime())); session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
	 * }catch(Exception e) {
	 * 
	 * } }
	 */

	public OmsCustOrdTender persistOmsCustOrdTender(String custOrderNo, CustOrderPicVo custOrderPicVo)
			throws SOAPException {
		log.info("Inside OmsCustOrdTender method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;

		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(custOrderNo);
		log.info("omsCustOrderNo: " + omsCustOrdNo);
		if (globalRmaFalg == true) {
			persistOmsOrposRefundTenderMethod(custOrderNo, custOrderPicVo, omsCustOrdNo);
		}
		BigDecimal omsOrposCustOrderId = null;
		try {
			// omsOrposCustOrderId=
			// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderPicVo.getCustomerOrderId());
			omsOrposCustOrderId = session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(customerOrderNo);
			log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
		} catch (Exception e) {
			log.info("No records found in OmsOrposCustOrderHead table");
		}
		// BigDecimal omsCustOrderNo =
		// session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal
		// tenderSeqNo=session.getOmsCustOrdTenderFindMaxTenderSeqNo(omsCustOrderNo);
		BigDecimal unitRetail = null;
		BigDecimal tenderAmt = null;
		OmsCustOrdTender omsCustOrderTender = new OmsCustOrdTender();
		OmsOrposPayment omsOrposPayment = new OmsOrposPayment();
		// List<OmsCustOrdTender>
		// omsCustOrderTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrderNo);
		TenderTypeClassifier tenderTypeClassifier = new TenderTypeClassifier();
		if (custOrderPicVo.getPaymentColDesc() != null) {
			log.info("+++++++++inside PaymentColDesc not null condition++++++");
			PaymentColDesc paymentColDesc = custOrderPicVo.getPaymentColDesc();
			List<PaymentDesc> paymentDesc = paymentColDesc.getPaymentDesc();
			BigDecimal tenderTypeId = null;
			BigDecimal paymentSeqNo = null;
			BigDecimal tenderSeqNo = null;
			String tgroup = null;
			for (PaymentDesc paymentDescLoop : paymentDesc) {
				log.info("Getting tenderTypeId for " + paymentDescLoop.getPaymentType());
				tenderTypeId = tenderTypeClassifier.getTenderTypeIdBasedOnParameterValueForPickup(paymentDescLoop,
						custOrderPicVo);
				log.info("tender type id is=============" + tenderTypeId);
				try {
					tenderSeqNo = session.getOmsCustOrdTenderFindMaxTenderSeqNo(omsCustOrdNo);
					log.info("tenderSeqNo " + tenderSeqNo);
				} catch (Exception e) {
					log.info("No records found in OmsCustOrdTender for omsCustOrdNo " + omsCustOrdNo);
				}
				omsCustOrderTender.setOmsCustOrdNo(omsCustOrdNo);
				log.info("OmsCustOrderNo is set");
				// omsCustOrderTender.setTenderSeqNo(tenderSeqNo.add(new BigDecimal(1)));
				if (tenderSeqNo != null) {
					omsCustOrderTender.setTenderSeqNo(tenderSeqNo.add(new BigDecimal(1)));
				} else {
					omsCustOrderTender.setTenderSeqNo(new BigDecimal(0));
				}
				log.info("TenderSeqNo is set");
				omsCustOrderTender.setTenderTypeId(tenderTypeId);
				log.info("tender type id is set");
				tgroup = session.getPosTenderTypeHeadFindByTenderTypeId(tenderTypeId);
				log.info("tgroup " + tgroup);
				omsCustOrderTender.setTenderTypeGroup(tgroup);
				log.info("Tender type group");
				omsCustOrderTender.setTenderAmt(paymentDescLoop.getAmount());
				log.info("Tender amt is set");

				log.info("The Payment Type entered is: ---" + paymentDescLoop.getPaymentType());
				if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CREDIT"))
						|| paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("DEBIT"))) {
					if (paymentDescLoop.getCreditDebitTender() != null) {
						omsCustOrderTender.setCcNo(paymentDescLoop.getCreditDebitTender().getMaskedAccountNumber());
						log.info("CC no is set");
						omsCustOrderTender.setCcAuthNo(paymentDescLoop.getCreditDebitTender().getAuthorizationCode());
						log.info("CC auth no is set");
						omsCustOrderTender.setCcAuthSrc(null);
						log.info("CC auth src is set");
						omsCustOrderTender.setCcCardholderVerf(null);
						log.info("CCCardHolder is set");
						omsCustOrderTender.setCcExpDate(null);
						log.info("CCexpDate is set");
						omsCustOrderTender.setCcEntryMode(null);
						log.info("Cc Entry mode is set");
						omsCustOrderTender.setCcTermId(null);
						log.info("Cc Term id");
						omsCustOrderTender.setCcSpecCond(null);
						log.info("Cc Spec Cond is set");
						omsCustOrderTender.setTenderRefId(paymentDescLoop.getCreditDebitTender().getCardToken());
						log.info("Tender Ref Id");
					}
				}

				if (omsOrposCustOrderId != null) {
					omsOrposPayment.setOmsOrposCustOrderId(omsOrposCustOrderId);
					log.info("setted omsOrposCustOrderId " + omsOrposCustOrderId);
					try {
						paymentSeqNo = session.getOmsOrpospaymentFindMaxSeqNoByomsOrposCustOrderId(omsOrposCustOrderId);
						log.info("paymentSeqNo " + paymentSeqNo);
					} catch (Exception e) {
						log.info("no record found in OmsOrpospayment table for omsOrposCustOrderId "
								+ omsOrposCustOrderId);
					}

					log.info("paymentSeqNo " + paymentSeqNo);
					if (paymentSeqNo != null) {
						omsOrposPayment.setPaymentSeqNo(paymentSeqNo.add(new BigDecimal(1)));
						log.info("setted SeqNo " + paymentSeqNo.add(new BigDecimal(1)));
					} else {
						omsOrposPayment.setPaymentSeqNo(new BigDecimal(0));
						log.info("setted seqno " + 0);
					}
					omsOrposPayment.setPaymentType(paymentDescLoop.getPaymentType().value());
					log.info("PaymentType " + paymentDescLoop.getPaymentType().value());
					omsOrposPayment.setCurrencyCode(paymentDescLoop.getCurrencyCode());
					log.info("paymentDescLoop.getCurrencyCode() " + paymentDescLoop.getCurrencyCode());
					omsOrposPayment.setAlternateAmount(paymentDescLoop.getAlternateAmount());
					log.info("paymentDescLoop.getAlternateAmount " + paymentDescLoop.getAlternateAmount());
					omsOrposPayment.setAlternateCurrencyCode(paymentDescLoop.getAlternateCurrencyCode());
					log.info(
							"paymentDescLoop.getAlternateCurrencyCode() " + paymentDescLoop.getAlternateCurrencyCode());
					omsOrposPayment.setAmount(paymentDescLoop.getAmount());
					log.info("paymentDescLoop.getAmount() " + paymentDescLoop.getAmount());
					if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CHECK"))) {
						// Added Persistence code for credit-debit tender, gift-card and coupounTender
						if (paymentDescLoop.getCheckTender() != null) {

							omsOrposPayment.setBankId(paymentDescLoop.getCheckTender().getBankId());
							omsOrposPayment.setAccountNumber(paymentDescLoop.getCheckTender().getAccountNumber());
							omsOrposPayment.setMicrNumber(paymentDescLoop.getCheckTender().getMicrNumber());
							omsOrposPayment.setCheckNumber(paymentDescLoop.getCheckTender().getCheckNumber());
							omsOrposPayment
									.setAuthorizationCode(paymentDescLoop.getCheckTender().getAuthorizationCode());
						}
					} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("PURCHASEORDER"))) {

						if (paymentDescLoop.getPurchaseOrdTender() != null) {
							omsOrposPayment.setAgentName(paymentDescLoop.getPurchaseOrdTender().getAgentName());
							omsOrposPayment.setPurchaseOrderNumber(
									paymentDescLoop.getPurchaseOrdTender().getPurchaseOrderNumber());
						}
					} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("STORECREDIT"))) {
						if (paymentDescLoop.getStoreCreditTender() != null) {
							log.info("inside store credit tender");
							log.info("getting for StoreCreditTender values");
							// log.info(paymentDescLoop.getStoreCreditTender().getCertificateType().value());
							if (paymentDescLoop.getStoreCreditTender().getCertificateType() != null) {
								log.info("inside CertificateType");
								omsOrposPayment.setCertificateType(checkNullValueForString(
										paymentDescLoop.getStoreCreditTender().getCertificateType().value()));
							}
							if (paymentDescLoop.getStoreCreditTender().getFirstName() != null) {
								log.info("inside FirstName");
								omsOrposPayment.setFirstName(
										checkNullValueForString(paymentDescLoop.getStoreCreditTender().getFirstName()));
							}
							if (paymentDescLoop.getStoreCreditTender().getLastName() != null) {
								log.info("inside LastName");
								omsOrposPayment.setLastName(
										checkNullValueForString(paymentDescLoop.getStoreCreditTender().getLastName()));
							}
							if (paymentDescLoop.getStoreCreditTender().getPersonalIdType() != null) {
								log.info("inside PersonalIdType");
								omsOrposPayment.setPersonalIdType(checkNullValueForString(
										paymentDescLoop.getStoreCreditTender().getPersonalIdType()));
							}
							if (paymentDescLoop.getStoreCreditTender().getState() != null) {
								log.info("inside State");
								omsOrposPayment.setState(checkNullValueForString(
										paymentDescLoop.getStoreCreditTender().getState().value()));
							}
							if (paymentDescLoop.getStoreCreditTender().getStoreCreditId() != null) {
								log.info("inside StoreCreditId");
								log.info("paymentDescLoop.getStoreCreditTender().getStoreCreditId() "
										+ paymentDescLoop.getStoreCreditTender().getStoreCreditId());
								omsOrposPayment
										.setStoreCreditId(paymentDescLoop.getStoreCreditTender().getStoreCreditId());
								log.info("setted StoreCreditId");
							}
						}
					} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("GIFTCARD"))) {
						log.info("inside gift card tender if condition");
						if (paymentDescLoop.getGiftCardTender() != null) {
							log.info("Entered the if loop for GiftCardTender");
							GiftCardTender giftCardTender = paymentDescLoop.getGiftCardTender();
							omsOrposPayment.setCardNumber(checkNullValueForString(giftCardTender.getCardNumber()));

							log.info("********* card number is set " + giftCardTender.getCardNumber()
									+ "*******************");
							omsOrposPayment.setGifcardAuthorizationCode(
									checkNullValueForString(giftCardTender.getAuthorizationCode()));
							log.info("Gift Card Authoization code");
							omsOrposPayment
									.setGifcardAuthorizationDatetime(new Timestamp(new java.util.Date().getTime()));
							log.info("Authorization date time set");
							if (giftCardTender.getAuthorizationMethod() != null) {
								omsOrposPayment.setGifcardAuthorizationMethod(
										checkNullValueForString(giftCardTender.getAuthorizationMethod().value()));
							}
							// omsOrposPayment.setGifcardAuthorizationMethod(checkNullValueForString(giftCardTender.getAuthorizationMethod().value()));
							log.info("Gift card Authorization method");
							if (giftCardTender.getCreditFlag() != null) {
								omsOrposPayment
										.setCreditFlag(checkNullValueForString(giftCardTender.getCreditFlag().value()));
							}
							// omsOrposPayment.setCreditFlag(checkNullValueForString(giftCardTender.getCreditFlag().value()));
							log.info("Credit flag is set");
							if (giftCardTender.getEntryMethod() != null) {
								omsOrposPayment.setGifcardEntryMethod(
										checkNullValueForString(giftCardTender.getEntryMethod().value()));
							}
							// omsOrposPayment.setGifcardEntryMethod(checkNullValueForString(giftCardTender.getEntryMethod().value()));
							log.info("Gift Card Entry method is set");
							omsOrposPayment
									.setOriginalBalance(checkNullValueForNumber(giftCardTender.getOriginalBalance()));
							log.info("Original balance is set");
							omsOrposPayment
									.setRemainingBalance(checkNullValueForNumber(giftCardTender.getRemainingBalance()));
							log.info("Remaining balance is set");
							omsOrposPayment.setGifcardsettlementData(
									checkNullValueForString(giftCardTender.getSettlementData()));
							log.info("Exited the if loop for GiftCardTender");
						}
						log.info("After gift card tender if condition");
					} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("COUPON"))) {
						log.info("inside the coupon tender if condition");
						if (paymentDescLoop.getCouponTender() != null) {
							log.info("Entered the if loop for CouponTender");
							CouponTender couponTender = paymentDescLoop.getCouponTender();
							if (couponTender.getCouponType() != null) {
								omsOrposPayment
										.setCouponType(checkNullValueForString(couponTender.getCouponType().value()));
								log.info("Coupon type is set");
							}
							if (couponTender.getEntryMethod() != null) {
								omsOrposPayment.setCoupontenderEntryMethod(
										checkNullValueForString(couponTender.getEntryMethod().value()));
								log.info("Coupon tender entry method is set");
							}
							omsOrposPayment.setCouponNumber(checkNullValueForString(couponTender.getCouponNumber()));
							log.info("Exited the if loop for CouponTender");
						}
					}
					log.info("persisting into OmsOrposPayment table");
					session.persistOmsOrposPayment(omsOrposPayment);

					log.info("Successfully persisted in OmsOrposPayment table");
				}

				// Added code for Bug 2550
				else {
					if (paymentDescLoop.getPaymentType() != null) {
						log.info("Fetching tender details for e commerce");
						if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("GIFTCARD"))) {
							if (paymentDescLoop.getGiftCardTender() != null) {
								GiftCardTender giftCardTender = paymentDescLoop.getGiftCardTender();
								omsCustOrderTender
										.setCcNo(checkNullValueForString(giftCardTender.getCardNumber()));
								omsCustOrderTender.setCcAuthNo(giftCardTender.getAuthorizationCode());
								log.info("******* ECOMMERCE ***** giftCardTender.getCardNumber() : "
										+ giftCardTender.getCardNumber() + "***************");
							}
						}

						else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CHECK"))) {
							log.info("inside CHECK for Ecommerce ");

							if (paymentDescLoop.getCheckTender() != null) {
								log.info("******* ECOMMERCE ***** checkTender : "
										+ paymentDescLoop.getCheckTender().getCheckNumber());
								omsCustOrderTender.setTenderRefId(paymentDescLoop.getCheckTender().getCheckNumber());
							}
						} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("PURCHASEORDER"))) {
							log.info("inside PURCHASEORDER for Ecommerce");
							if (paymentDescLoop.getPurchaseOrdTender() != null) {
								log.info(
										"******* ECOMMERCE ***** paymentDescLoop.getPurchaseOrdTender().getPurchaseOrderNumber(): "
												+ paymentDescLoop.getPurchaseOrdTender().getPurchaseOrderNumber());
								omsCustOrderTender.setTenderRefId(
										paymentDescLoop.getPurchaseOrdTender().getPurchaseOrderNumber());
							}
						}

						else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("STORECREDIT"))) {
							log.info("inside STORECREDIT for Ecommerce");
							if (paymentDescLoop.getStoreCreditTender() != null) {
								log.info(
										"******* ECOMMERCE ***** paymentDescLoop.getStoreCreditTender().getStoreCreditId(): "
												+ paymentDescLoop.getStoreCreditTender().getStoreCreditId());
								omsCustOrderTender
										.setTenderRefId(paymentDescLoop.getStoreCreditTender().getStoreCreditId());
							}
						} else if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("COUPON"))) {
							log.info("inside COUPON for Ecommerce");
							if (paymentDescLoop.getCouponTender() != null) {
								CouponTender couponTender = paymentDescLoop.getCouponTender();
								log.info("******* ECOMMERCE ***** couponTender.getCouponNumber(): "
										+ couponTender.getCouponNumber());
								omsCustOrderTender
										.setTenderRefId(checkNullValueForString(couponTender.getCouponNumber()));
							}
						}

					}
				}

				omsCustOrderTender.setCreateDatetime(new Timestamp(new Date().getTime()));
				log.info("CreateDatetime is set");
				unitRetail = session.getOmsCustOrdItemSumUnitRetail(omsCustOrdNo);
				log.info("Unit retail is set");
				tenderAmt = session.getOmsCustOrdTenderSumOfTenderAmt(omsCustOrdNo);
				if (tenderAmt == null) {
					tenderAmt = BigDecimal.ZERO;
				}
				if (unitRetail == null) {
					unitRetail = BigDecimal.ZERO;
				}
				log.info("=============tenderAmt=============" + tenderAmt);
				log.info("tenderAmt from query is set");
				BigDecimal valueDiff = new BigDecimal(0);
				if (tenderAmt != null) {
					valueDiff = unitRetail.subtract(tenderAmt);
				} else {
					valueDiff = unitRetail;
				}
				log.info("==========valueDiff========" + valueDiff);
				/*
				 * if (valueDiff.intValue() > 0) //commented for 2533 bug {
				 * omsCustOrderTender.setPaymentStatusInd("P");
				 * log.info("Payment Status Ind is set to P"); } else {
				 */
				omsCustOrderTender.setPaymentStatusInd("S");
				log.info("Payment Status Ind is set to S");
				// }
				session.persistOmsCustOrdTender(omsCustOrderTender);
				log.info("Successfully persisted into OmsCustOrdTender table");
			}
		}
		return omsCustOrderTender;
	}

	// WebServices code starts
	// Method for calling CO cancellation
	// Calling the methods from Siebel, RMS and SIM web services

	/**
	 * @param input
	 * @throws SOAPException
	 */
	public void saveCOCancellationDetails(CustOrderPicVo input) throws SOAPException {
		log.info("Inside saveCOCancellationDetails method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String custOrderNo = customerOrderNo;
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);

		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
		BigDecimal custOrderPicVoSeqNo = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("test point");
		extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
		int subNo = Integer.parseInt(omsCustOrdHead.getSubCustOrderNo().trim());
		log.info("=====================SubNo=============" + subNo);
		if (subNo == 1) {
			log.info("inside subOrderN0");
			extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim();
		} else if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
			if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "0" + omsCustOrdHead.getSubCustOrderNo().trim();
			} else {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "00"
						+ omsCustOrdHead.getSubCustOrderNo().trim();
			}
		}
		log.info("extCustOrdNo=" + extCustOrdNo);
		List<BigDecimal> omsCancelIdList = null;
		try {
			omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(custOrderNo, "1", custOrderPicVoSeqNo);
			log.info("omsCancelId=" + omsCancelIdList.size());
		} catch (Exception e) {
			log.error("Error in getOmsCoCancelHeadFindOmsCancelId" + e);
		}
		if (omsCancelIdList.size() > 0) {
			log.info("Failure ..Duplicate entries found in OMS_CO_CANCEL_HEAD");
		} else {
			log.info("going to persistOmsCoCancelHead");
			OmsPersistence omsPersistence = new OmsPersistence();
			// log.info("persistOmsCoCancelHead omsCustOrdNo "+omsCustOrdNo);
			omsPersistence.persistOmsCoCancelHead(input, omsCustOrdNo, customerOrderNo);
		}
	}

	// Method for saving the CustOrdLog

	public boolean saveCustOrdLog(CustOrderPicVo input) throws SOAPException {
		String custOrderNo = customerOrderNo;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
		BigDecimal custOrderPicVoSeqNo = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
		List<BigDecimal> omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(customerOrderNo, "1",
				custOrderPicVoSeqNo);
		if (omsCancelIdList.isEmpty()) {
			log.error(
					"No record found for omsCancelId in Oms_Cancel_head table , unable to insert in OmsCustOrdLog table");
		}
		omsCancelId = omsCancelIdList.get(0);
		if (session.getOmsCustOrdLogFindOmsCancelId(omsCancelId).size() > 0) {
			log.error("-->records already exsist in OmsCustlog , unable to insert in OmsCustOrdLog table");
			status = "ERROR_205"; // DataBase Error
		} else {
			OmsPersistence omsPersistence = new OmsPersistence();
			omsPersistence.persistOmsCustOrdLog(omsCustOrderNo, omsCancelId, "CA", input, customerOrderNo);

			log.info("Saved data in oms_cust_ord_log table first time.");
		}
		return true;
	}

	// Method for Checking the Item Status - needed

	public BigDecimal checkItemStatus(String custOrderNo, CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("inside method checkItemStatus");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
		BigDecimal custOrderPicVoSeqNo = session.getMaxOmsOrposCustOrderPickUp(custOrderNo);
		List<BigDecimal> omsCancelIdList = null;
		omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(custOrderNo, "1", custOrderPicVoSeqNo);
		log.info("++++++++++++++++++++++++++omsCancelId ++++++++++++++++++++++" + omsCancelId);
		List<OmsCustOrdItem> omsCustOrdItem = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
		int i = 0;
		for (OmsCustOrdItem list : omsCustOrdItem) {
			log.info("list.getQtyCancelled()=" + list.getQtyCancelled() + "and list.getQtyOrderedSuom() "
					+ list.getQtyOrderedSuom());
			if (list.getQtyCancelled().longValue() != list.getQtyOrderedSuom().longValue()) {
				log.info("quant doesnot match i=" + i);
				i = 1;
				break;
			}
			if (i == 0) {
				log.info("All items cancelled" + i);
				OmsPersistence omsPersistence = new OmsPersistence();
				// omsPersistence.persistOmsCustOrdLog(omsCustOrdNo,omsCancelId, "CL",
				// custOrderPicVo);
				omsPersistence.persistOmsCustOrdLog(omsCustOrdNo, omsCancelId, "CL", custOrderPicVo, customerOrderNo);
				log.info("persisted data in OmsCustOrdLog with event id =CL");
			}
		}
		return omsCancelId;
	}

	// Method for saveCoCancellationItems

	public String processCancellation(List<CustOrdItmPkVo> cancelList, CustOrderPicVo custOrderPicVo)
			throws SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException,
			Exception {

		InterfacePersistence interfacePersistence = new InterfacePersistence();
		log.info("inside method processCancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		String custOrdNo = customerOrderNo;
		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrdNo);
		BigDecimal custOrderPicVoSeqNo = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrdNo);
		// OmsCustOrdHead omsCustOrdHead =
		// session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("omsCustOrdNo " + omsCustOrdNo);
		List<BigDecimal> omsCancelIdList = null;
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		omsCustOrderNo = omsCustOrdNo;
		omsCancelIdList = session.getOmsCoCancelHeadFindOmsCancelId(custOrdNo, "1", custOrderPicVoSeqNo);
		BigDecimal omsCancelId = omsCancelIdList.get(0);
		log.info("cancel id" + omsCancelId);
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		BigDecimal logSeqNo = session.getOmsCustOrdLogFindlogSeqNo(omsCancelId);
		OmsCoCancelItem omsCoCancelItem = new OmsCoCancelItem();
		OmsCustOrdItem omsCustOrdItem = null;
		OmsCoFoCancel omsCoFoCancel = new OmsCoFoCancel();
		BigDecimal lineNo = null;
		String item = null;
		for (CustOrdItmPkVo itemList : cancelList) {
			log.info("Inside the cancelItemList loop");
			lineNo = new BigDecimal(itemList.getLineItemNo());
			log.info("line no--------" + lineNo);
			log.info("line no is set");
			List<OmsCustOrdItem> omsCustOrdItemList1 = session
					.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, (lineNo));
			log.info("omsCustOrdItemList1.get(0).getCumQtyDelivered() "
					+ omsCustOrdItemList1.get(0).getCumQtyDelivered());
			log.info("omsCustOrdItemList1.get(0).getQtyCancelled() " + omsCustOrdItemList1.get(0).getQtyCancelled());
			log.info("omsCustOrdItemList1.get(0).getQtyCancelled() " + omsCustOrdItemList1.get(0).getQtyCancelled());
			log.info("itemList.getCancelledQuantity() " + itemList.getCancelledQuantity());
			if (omsCustOrdItemList1.get(0).getCumQtyDelivered().intValue()
					+ omsCustOrdItemList1.get(0).getQtyCancelled().intValue()
					+ itemList.getCancelledQuantity().intValue() > omsCustOrdItemList1.get(0).getQtyOrderedSuom()
							.intValue()) {
				log.info("inside valiation for optimazation for cancellation");
				status = "F";
				error = "ERROR_201";
				break;
			}
			item = omsCustOrdItemList1.get(0).getItem();
			OmsPersistence omsPersistence = new OmsPersistence();
			omsCoCancelItem = omsPersistence.persistOmsCoCancelItem(item, omsCancelId, omsCustOrdNo, itemList); // get
																												// the
																												// item
																												// from
																												// OmsCustOrdItem
																												// using
																												// line_no
			log.info("After persistOmsCoCancelItem stmt");
			List<OmsCoFulfillDetail> fulfillDetailList = null;
			log.info("oms_cust_ord_no=" + omsCustOrdNo + "item=" + item);
			L_CO_ITEM_CANCEL_QTY = itemList.getCancelledQuantity().longValue();
			// Actual cancelled qty
			BigDecimal actual_Qty = new BigDecimal(0);
			BigDecimal dummy_Qty = new BigDecimal(0);
			BigDecimal requsted_CancelQty = new BigDecimal(L_CO_ITEM_CANCEL_QTY);
			dummy_Qty = omsCustOrdItemList1.get(0).getQtyCancelled();
			log.info("dummy Cancelled Qty in OmsCustOrdItem " + dummy_Qty);
			log.info("Requested Cancelled Qty in OmsOrposCustOrdpickUpItem " + requsted_CancelQty);
			actual_Qty = requsted_CancelQty.subtract(dummy_Qty);
			log.info(" actual_Qty " + actual_Qty);
			L_cum_cancel_qty = 0;
			long backOrderOpenQty = 0;
			boolean isBOFound = false;
			try {
				fulfillDetailList = session.getOmsCoFulfillDetailFindFulFillDetails(omsCustOrdNo, item,
						new BigDecimal(itemList.getLineItemNo()));
				fulfillDetailList = shuffleFulfillOrderNo(fulfillDetailList);
				log.info("Size of fulfil detail list :" + fulfillDetailList.size());
			} catch (Exception e) {
				log.error("-->no records found in fulfil_detail table");
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("NoRecord_FulfilDetail")));
				// throw new SOAPException("no records found in fulfil_detail table");
			}
			// BO Cancellation
			if (fulfillDetailList.size() == 0 && L_cum_cancel_qty == 0) {
				log.info("Condition: Size of fulfil detail list is 0. and L_cum_cancel_qty == 0 ");
				log.info("omsCustOrderNo=" + omsCustOrderNo + "item=" + omsCustOrdItemList1.get(0).getItem()
						+ "line no=" + itemList.getLineItemNo());
				List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(
						omsCustOrderNo, omsCustOrdItemList1.get(0).getItem(), new BigDecimal(itemList.getLineItemNo()));
				log.info("omsBackOrderDtlList " + omsBackOrderDtlList.size());
				BigDecimal requestedCancelQty = BigDecimal.ZERO;
				int i = 0;
				if (omsBackOrderDtlList.size() != 0) {
					for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {

						// issue 14
						BigDecimal currentBOQty = omsBackOrderDtl.getSourceQty()
								.subtract(omsBackOrderDtl.getFulfillQty());
						if (i == 0) {
							requestedCancelQty = itemList.getCancelledQuantity();
							i++;
						}
						if (omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() != 0
								&& requestedCancelQty.intValue() != 0) {
							BigDecimal channelId = BigDecimal.ZERO;
							BigDecimal physicalWH = null;
							if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
								List<Object[]> tempWhObject = session
										.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
								for (Object[] result : tempWhObject) {
									physicalWH = new BigDecimal(result[0].toString());
									channelId = new BigDecimal(result[1].toString());
								}
							} else {
								channelId = BigDecimal.ZERO;
							}

							if (currentBOQty.intValue() >= requestedCancelQty.intValue()) {

								int lineNoItem = itemList.getLineItemNo();
								String item1 = omsBackOrderDtl.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = omsBackOrderDtl.getLineNo() + ","
										+ omsBackOrderDtl.getItem().toString() + ","
										+ omsBackOrderDtl.getSourceLoc().toString() + ","
										+ omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + ","
										+ omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, requestedCancelQty);
								omsBackOrderDtl.setSourceQty(currentBOQty.subtract(requestedCancelQty));
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0
										|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
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
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
											requestedCancelQty.negate(), physicalWH.longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);
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
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
											requestedCancelQty.negate(), omsBackOrderDtl.getSourceLoc().longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);
								}
								break;
							} else {
								Long lineNoItem = omsBackOrderDtl.getLineNo().longValue();
								String item1 = omsBackOrderDtl.getItem();

								log.info("=========Seibel Data for Backorder==========");
								String completeKey = omsBackOrderDtl.getLineNo().toString() + "," + item1.toString()
										+ "," + omsBackOrderDtl.getSourceLoc().toString() + ","
										+ omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + ","
										+ omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, currentBOQty);
								omsBackOrderDtl.setSourceQty(BigDecimal.ZERO);
								requestedCancelQty = requestedCancelQty.subtract(currentBOQty);
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0
										|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									log.info("====================calling WH========================");
									log.info("calling RMSBackOrderWS for WH for qty " + currentBOQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling WH========================");
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), currentBOQty.negate(),
											physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(), "EA",
											channelId);
								} else // ST
								{
									log.info("====================calling ST========================");
									log.info("calling RMSBackOrderWS for ST for qty " + currentBOQty.negate());
									log.info("omsBackOrderDtl.getSourceLoc() " + omsBackOrderDtl.getSourceLoc());
									log.info("omsBackOrderDtl.getFulfillLoc() " + omsBackOrderDtl.getFulfillLoc());
									log.info("Item " + omsBackOrderDtl.getItem());
									log.info("====================calling ST========================");
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(), currentBOQty.negate(),
											omsBackOrderDtl.getSourceLoc().longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);
								}

							}
							if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0
									|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
								omsBackOrderDtl.setBackorderStatus("S");
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							}

						}

						// issue 14 end
						if (omsBackOrderDtl.getSourceQty().intValue() >= itemList.getCancelledQuantity().intValue()) {
							log.info("omsBackOrderDtl.getSourceQty().intValue()>=L_fo_open_qty");

							BigDecimal channelId = BigDecimal.ZERO;
							BigDecimal physicalWH = null;
							if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
								List<Object[]> tempWhObject = session
										.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
								for (Object[] result : tempWhObject) {
									physicalWH = new BigDecimal(result[0].toString());
									channelId = new BigDecimal(result[1].toString());
								}
								oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
										(itemList.getCancelledQuantity()).negate(), physicalWH.longValue(),
										omsBackOrderDtl.getSourceLocType(), "EA", channelId);
							} else {
								channelId = BigDecimal.ZERO;
								oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
										(itemList.getCancelledQuantity()).negate(),
										omsBackOrderDtl.getSourceLoc().longValue(), omsBackOrderDtl.getSourceLocType(),
										"EA", channelId);

							}
							log.info("omsBackOrderDtl.getSourceQty() " + omsBackOrderDtl.getSourceQty());
							log.info("item.getCancelQtySuom() " + itemList.getCancelledQuantity());
							log.info("omsBackOrderDtl.getSourceQty().subtract(item.getCancelQtySuom()) "
									+ omsBackOrderDtl.getSourceQty().subtract(itemList.getCancelledQuantity()));

							Long lineNoItem = omsBackOrderDtl.getLineNo().longValue();
							String item1 = omsBackOrderDtl.getItem();

							log.info("=========Seibel Data for Backorder==========");
							String completeKey = lineNoItem.toString() + "," + item1.toString() + ","
									+ omsBackOrderDtl.getSourceLoc().toString() + ","
									+ omsBackOrderDtl.getSourceLocType() + ","
									+ omsBackOrderDtl.getFulfillLoc().toString() + ","
									+ omsBackOrderDtl.getFulfillLocType();
							backorderTreeMap.put(completeKey, itemList.getCancelledQuantity());

							omsBackOrderDtl.setSourceQty(
									omsBackOrderDtl.getSourceQty().subtract(itemList.getCancelledQuantity()));
							log.info("omsBackOrderDtl.getSourceQty()" + omsBackOrderDtl.getSourceQty());
							if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0
									|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
								omsBackOrderDtl.setBackorderStatus("S");
							}
							session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							// L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
						}
					}
					OmsCustOrdItem omsCustOrdItem1 = session.getOmsCustOrdItemFindByItem(omsCustOrderNo,
							omsCustOrdItemList1.get(0).getItem(), new BigDecimal(itemList.getLineItemNo()));
					omsCustOrdItem1
							.setQtyCancelled(omsCustOrdItem1.getQtyCancelled().add(itemList.getCancelledQuantity()));
					session.mergeOmsCustOrdItem(omsCustOrdItem1);
					// OmsCoCancelItem
					// omsCoCancelItem2=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId,
					// new BigDecimal(item.getLineNo()));
					omsCoCancelItem.setCancelConfQty(itemList.getCancelledQuantity());
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
				List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(
						omsCustOrderNo, item, new BigDecimal(itemList.getLineItemNo()));
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
					if (omsBackOrderDtl.getFulfillQty().intValue() >= L_fo_open_qty) {
						log.info("--" + new BigDecimal(L_cum_cancel_qty).negate());
						BigDecimal channelId = BigDecimal.ZERO;
						BigDecimal physicalWH = null;
						if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
							for (Object[] result : tempWhObject) {
								physicalWH = new BigDecimal(result[0].toString());
								channelId = new BigDecimal(result[1].toString());
								log.info(
										"omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + "physicalWH " + physicalWH);
							log.info("omsCustOrdNo " + omsCustOrdNo + "channelId " + channelId);
							oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
									((itemList.getCancelledQuantity()).negate()), physicalWH.longValue(),
									omsBackOrderDtl.getSourceLocType(), "EA", channelId);
						} else {
							channelId = BigDecimal.ZERO;
							oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
									((itemList.getCancelledQuantity()).negate()),
									omsBackOrderDtl.getSourceLoc().longValue(), omsBackOrderDtl.getSourceLocType(),
									"EA", channelId);

						}
						log.info("=========Seibel Data for Backorder==========");
						String completeKey = itemList.getLineItemNo() + "," + item + ","
								+ omsBackOrderDtl.getSourceLoc().toString() + "," + omsBackOrderDtl.getSourceLocType()
								+ "," + omsBackOrderDtl.getFulfillLoc().toString() + ","
								+ omsBackOrderDtl.getFulfillLocType();
						backorderTreeMap.put(completeKey, itemList.getCancelledQuantity());
						omsBackOrderDtl
								.setSourceQty(omsBackOrderDtl.getSourceQty().subtract(itemList.getCancelledQuantity()));
						session.mergeOmsBackOrderDtl(omsBackOrderDtl);
						// L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
					}
				}

				log.info("Condition: Size of fulfil detail list is 0. and L_cum_cancel_qty !=0 ");
				omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
				log.info("updating oms_co_cancel_item");
				session.mergeOmsCoCancelItem(omsCoCancelItem);
				try {
					omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item,
							new BigDecimal(itemList.getLineItemNo()));
				} catch (Exception e) {
					log.error("no records found in oms_cust_ord_item");
					// throw new
					// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("NoRecord_OmsCustOrdItem")));
					// throw new SOAPException("no records found in oms_cust_ord_item table");
				}
				log.info("old value in oms_cust_ord_item" + omsCustOrdItem.getQtyCancelled());
				omsCustOrdItem.setQtyCancelled((itemList.getCancelledQuantity().add(omsCustOrdItem.getQtyCancelled())));
				log.info("new value in oms_cust_ord_item" + omsCustOrdItem.getQtyCancelled());
				log.info("updating oms_cust_ord_item");
				session.mergeOmsCustOrdItem(omsCustOrdItem); // updating oms_cust_ord_item
				continue; // fetch new record (check for new logic)
			} else {
				log.info("fulfil detail loop going to start ");
				int fulfillDetailList_count = 0;
				int whQty = 0;
				for (OmsCoFulfillDetail fulfilDetail : fulfillDetailList) {
					fulfillDetailList_count++;
					log.info("fulfil loop start-------");
					log.info("Fulfil ord no=" + fulfilDetail.getFulfillOrderNo());

					fulilmentOrdNo = fulfilDetail.getFulfillOrderNo().longValue();
					log.info("fulilmentOrdNo " + fulilmentOrdNo);

					try {
						log.info(fulfilDetail.getFulfillConfQty());
						log.info(fulfilDetail.getFulfillDeliverQty());
						log.info(fulfilDetail.getFulfillCancelQty());
						log.info(omsCoCancelItem.getCancelReqQty());
						log.info(omsCoCancelItem.getCancelConfQty());
						L_fo_open_qty = Math.min(
								(fulfilDetail.getFulfillConfQty().longValue()
										- fulfilDetail.getFulfillDeliverQty().longValue()
										- fulfilDetail.getFulfillCancelQty().longValue()),
								(omsCoCancelItem.getCancelReqQty().longValue()
										- omsCoCancelItem.getCancelConfQty().longValue()));

					} catch (Exception e) {
						log.error("failing in finding min qty" + e);
					}
					log.info("Minimum quantity is L_fo_open_qty=" + L_fo_open_qty);
					omsCoFoCancel.setItem(item);
					// changed line number code
					omsCoFoCancel.setLineNo(lineNo);
					// omsCoFoCancel.setFoCancelledOty(new BigDecimal(L_fo_open_qty));
					omsCoFoCancel.setFoCancelledOty(BigDecimal.ZERO);
					omsCoFoCancel.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));
					omsCoFoCancel.setOmsCancelId(omsCancelId);
					omsCoFoCancel.setCreateDatetime(new Timestamp(new Date().getTime()));
					session.persistOmsCoFoCancel(omsCoFoCancel);
					log.info("persisted in oms_co_fo_cancel");
					log.info("Source loc type is:" + fulfilDetail.getSourceLocType());
					// OmsCustOrdItem omsCustOrdItem;
					try {
						omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item,
								new BigDecimal(itemList.getLineItemNo()));
					} catch (Exception e) {
						log.info("no records found in oms_cust_ord_item");
						// throw new
						// SOAPFaultException(OMSUtil.getInstance().newSoapFault(props.getProperty("NoRecord_OmsCustOrdItem")));
						// throw new SOAPException("no records found in oms_cust_ord_item table with
						// omsCustOrdNo and item="+item);
					}
					List<OmsBackOrderDtl> omsBackOrderDtlList = null;
					try {
						omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNo(omsCustOrderNo,
								fulfilDetail.getLineNo());
						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
							log.info("SourceQty=" + omsBackOrderDtl.getSourceQty());
							if (omsBackOrderDtl.getSourceQty().intValue() > 0) {
								isBOFound = true;
								backOrderOpenQty = Math.min(
										(omsBackOrderDtl.getSourceQty().longValue()
												- omsBackOrderDtl.getFulfillQty().longValue()),
										(omsCoCancelItem.getCancelReqQty().longValue()
												- omsCoCancelItem.getCancelConfQty().longValue()));
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
						// item,new BigDecimal( itemList.getLineItemNo()));
						// if(omsBackOrderDtlList.size()==0)
						// {
						interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo, fulfilDetail, item,
								L_fo_open_qty, omsCustOrdItem, extCustOrdNo);

						// updating oms_co_fo_cancel table
						omsCoFoCancel.setWsResponse("C");
						session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
						log.info("old cancel Quantity" + fulfilDetail.getFulfillCancelQty());
						fulfilDetail.setFulfillCancelQty(
								new BigDecimal(L_fo_open_qty).add(fulfilDetail.getFulfillCancelQty()));
						omsCoFoCancel.setFoCancelledOty(new BigDecimal(L_fo_open_qty));
						su_qty = BigDecimal.valueOf(su_qty.intValue() + fulfilDetail.getFulfillCancelQty().intValue());
						log.info("new cancel Quantity" + fulfilDetail.getFulfillCancelQty());
						session.mergeOmsCoFulfillDetail(fulfilDetail);
						session.mergeOmsCoFoCancel(omsCoFoCancel);
						// }

						L_cum_cancel_qty = L_fo_open_qty + L_cum_cancel_qty;
						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "   L_fo_open_qty=" + L_fo_open_qty);
						omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
						session.mergeOmsCoCancelItem(omsCoCancelItem);
						if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
							log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty="
									+ L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);

							// issue -if only one record in fulfilement and BO then not cancelling the BO
							if (fulfillDetailList.size() == fulfillDetailList_count) {
								if (isBOFound == true) {
									log.info("Cancelling BO");

									log.info("L_cum_cancel_qty=" + L_cum_cancel_qty);
									boolean backOrderCompleted = false;
									for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
										backOrderOpenQty = Math.min(
												(omsBackOrderDtl.getSourceQty().longValue()
														- omsBackOrderDtl.getFulfillQty().longValue()),
												(omsCoCancelItem.getCancelReqQty().longValue()
														- omsCoCancelItem.getCancelConfQty().longValue()));
										log.info("backOrderOpenQty=" + backOrderOpenQty);
										if (omsBackOrderDtl != null
												&& omsBackOrderDtl.getSourceQty().intValue() >= backOrderOpenQty
												&& omsBackOrderDtl.getSourceQty()
														.compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
											BigDecimal channelId = BigDecimal.ZERO;
											BigDecimal physicalWH = null;
											if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
												log.info("Source loc in back order is WH "
														+ omsBackOrderDtl.getSourceLoc());
												/*
												 * List<Object[]> tempWhObject=
												 * session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
												 * BigDecimal physicalWH=BigDecimal.ZERO; BigDecimal
												 * channelId=BigDecimal.ZERO; for(Object[] result:tempWhObject) {
												 * log.info("-----"); physicalWH= new BigDecimal(result[0].toString());
												 * omsBackOrderDtl.setSourceLoc(physicalWH); channelId=new
												 * BigDecimal(result[1].toString());
												 * log.info("WH="+result[0]+"channel id="+result[1]); }
												 */
												log.info("Calling BO cancellation WS");

												List<Object[]> tempWhObject = session
														.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
												for (Object[] result : tempWhObject) {
													physicalWH = new BigDecimal(result[0].toString());
													channelId = new BigDecimal(result[1].toString());
												}
												oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
														(new BigDecimal(backOrderOpenQty).negate()),
														physicalWH.longValue(), omsBackOrderDtl.getSourceLocType(),
														"EA", channelId);

											} else {
												channelId = BigDecimal.ZERO;
												oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
														(new BigDecimal(backOrderOpenQty).negate()),
														omsBackOrderDtl.getSourceLoc().longValue(),
														omsBackOrderDtl.getSourceLocType(), "EA", channelId);

											}

											log.info("=========Seibel Data for Backorder==========");
											String completeKey = itemList.getLineItemNo() + "," + item + ","
													+ omsBackOrderDtl.getSourceLoc().toString() + ","
													+ omsBackOrderDtl.getSourceLocType() + ","
													+ omsBackOrderDtl.getFulfillLoc().toString() + ","
													+ omsBackOrderDtl.getFulfillLocType();
											backorderTreeMap.put(completeKey, new BigDecimal(backOrderOpenQty));
											omsBackOrderDtl.setSourceQty(omsBackOrderDtl.getSourceQty()
													.subtract(new BigDecimal(backOrderOpenQty)));
											if (omsBackOrderDtl.getSourceQty()
													.compareTo(omsBackOrderDtl.getFulfillQty()) == 0
													|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
												omsBackOrderDtl.setBackorderStatus("S");
											}
											session.mergeOmsBackOrderDtl(omsBackOrderDtl);
										}

										L_cum_cancel_qty = backOrderOpenQty + L_cum_cancel_qty;

										if (L_fo_open_qty > backOrderOpenQty)
											L_fo_open_qty = L_fo_open_qty - backOrderOpenQty;
										else if (omsCoCancelItem.getCancelReqQty().intValue() > backOrderOpenQty)
											L_fo_open_qty = omsCoCancelItem.getCancelReqQty().intValue()
													- backOrderOpenQty;
										else
											L_fo_open_qty = backOrderOpenQty
													- omsCoCancelItem.getCancelReqQty().intValue();
										log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "L_fo_open_qty="
												+ L_fo_open_qty + "backOrderOpenQty= " + backOrderOpenQty
												+ "L_CO_ITEM_CANCEL_QTY =" + L_CO_ITEM_CANCEL_QTY);
										omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
										session.mergeOmsCoCancelItem(omsCoCancelItem);

										if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
											log.info(
													"inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty="
															+ L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY="
															+ L_CO_ITEM_CANCEL_QTY);
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

							continue; // Fetching another FO
						} else {
							break;
						}

					}

					if (isBOFound == true) {
						log.info("Cancelling BO");

						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty);
						boolean backOrderCompleted = false;

						for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
							backOrderOpenQty = Math.min(
									(omsBackOrderDtl.getSourceQty().longValue()
											- omsBackOrderDtl.getFulfillQty().longValue()),
									(omsCoCancelItem.getCancelReqQty().longValue()
											- omsCoCancelItem.getCancelConfQty().longValue()));
							log.info("backOrderOpenQty=" + backOrderOpenQty);
							if (omsBackOrderDtl != null && omsBackOrderDtl.getSourceQty().intValue() >= backOrderOpenQty
									&& omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) != 0) {
								BigDecimal channelId = BigDecimal.ZERO;
								BigDecimal physicalWH = null;
								if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
									List<Object[]> tempWhObject = session
											.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
									for (Object[] result : tempWhObject) {
										physicalWH = new BigDecimal(result[0].toString());
										channelId = new BigDecimal(result[1].toString());
										log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id="
												+ result[1]);
									}
									log.info("omsCustOrdNo " + omsCustOrdNo + "physicalWH " + physicalWH);
									log.info("omsCustOrdNo " + omsCustOrdNo + "channelId " + channelId);
									log.info("Source loc in back order is WH " + omsBackOrderDtl.getSourceLoc() + "qty="
											+ backOrderOpenQty);
									log.info("Calling BO cancellation WS");
									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
											(new BigDecimal(backOrderOpenQty).negate()), physicalWH.longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);

								} else {
									channelId = BigDecimal.ZERO;
									log.info(
											"Source loc " + omsBackOrderDtl.getSourceLoc() + "qty=" + backOrderOpenQty);

									oMSUtilCommons.callRMSBackorderWS(omsBackOrderDtl.getItem(),
											(new BigDecimal(backOrderOpenQty).negate()),
											omsBackOrderDtl.getSourceLoc().longValue(),
											omsBackOrderDtl.getSourceLocType(), "EA", channelId);

								}
								log.info("=========Seibel Data for Backorder==========");
								String completeKey = itemList.getLineItemNo() + "," + item + ","
										+ omsBackOrderDtl.getSourceLoc().toString() + ","
										+ omsBackOrderDtl.getSourceLocType() + ","
										+ omsBackOrderDtl.getFulfillLoc().toString() + ","
										+ omsBackOrderDtl.getFulfillLocType();
								backorderTreeMap.put(completeKey, new BigDecimal(backOrderOpenQty));
								omsBackOrderDtl.setSourceQty(
										omsBackOrderDtl.getSourceQty().subtract(new BigDecimal(backOrderOpenQty)));
								if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0
										|| omsBackOrderDtl.getSourceQty().intValue() == 0) {
									omsBackOrderDtl.setBackorderStatus("S");
								}
								session.mergeOmsBackOrderDtl(omsBackOrderDtl);
							}
							L_cum_cancel_qty = backOrderOpenQty + L_cum_cancel_qty;
							// issue no 13
							if (L_fo_open_qty > backOrderOpenQty) {

								// L_fo_open_qty = L_fo_open_qty - backOrderOpenQty;
								L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
								log.info("L_fo_open_qty =" + L_fo_open_qty);
							} else if (omsCoCancelItem.getCancelReqQty()
									.intValue() > (su_qty.intValue() + backOrderOpenQty))
								L_fo_open_qty = omsCoCancelItem.getCancelReqQty().intValue()
										- (su_qty.intValue() + backOrderOpenQty);
							else
								L_fo_open_qty = (su_qty.intValue() + backOrderOpenQty)
										- omsCoCancelItem.getCancelReqQty().intValue();
							// end issue no 13
							L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
							log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "L_fo_open_qty=" + L_fo_open_qty
									+ "backOrderOpenQty= " + backOrderOpenQty + "L_CO_ITEM_CANCEL_QTY ="
									+ L_CO_ITEM_CANCEL_QTY);
							omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
							session.mergeOmsCoCancelItem(omsCoCancelItem);
							if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
								log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty="
										+ L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);
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

					if (fulfilDetail.getSourceLocType() != null
							&& (fulfilDetail.getSourceLocType().equals("ST")
									|| fulfilDetail.getSourceLocType().equals("WH"))
							&& fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) != 0) {
						// cancelling transfer
						log.info("source loc=ST or WH");
						log.info("fulilmentOrdNo" + fulilmentOrdNo);
						try {
							if (omsCustOrdHead.getDeliveryType().equals("C")
									&& fulfilDetail.getSourceLocType().equals("WH")) {
								List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,
										fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
										fulfilDetail.getFulfillLoc());
								if (ordCustList.get(0).getTsfNo() != null
										&& ordCustList.get(0).getTsfNo().intValue() != 0) {
									log.info("Transfer No" + ordCustList.get(0).getTsfNo());
									log.info("Item " + fulfilDetail.getItem().toString() + "L_fo_open_qty="
											+ L_fo_open_qty);
									BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
									/*
									 * OMSUtilJdbc omsUtilJdbc=new OMSUtilJdbc(); try {
									 * totalSelectedDistroQty=omsUtilJdbc.getselectedandDistroQty(fulfilDetail.
									 * getItem(), ordCustList.get(0).getTsfNo());
									 * log.info("totalSelectedDistroQty  "+totalSelectedDistroQty);
									 * 
									 * } catch(Exception e) {
									 * log.info("Exception from getselectedandDistroQty "+e.getMessage());
									 * totalSelectedDistroQty=BigDecimal.ZERO; }
									 */
									log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);
									if (totalSelectedDistroQty.intValue() == 0) {
										// Calling RMS cancelFulfilOrdColRef
										whQty = fulfilDetail.getFulfillConfQty().intValue()
												- (fulfilDetail.getFulfillDeliverQty().intValue()
														+ fulfilDetail.getFulfillCancelQty().intValue());
										if (L_fo_open_qty > whQty) {
											log.info("Cancelling WH transfer");
											interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
													fulfilDetail, item, whQty, omsCustOrdItem, extCustOrdNo);
											L_fo_open_qty = L_fo_open_qty - whQty;
										} else {
											interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
													fulfilDetail, item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
											// L_fo_open_qty=0;
										}
									} else {
										// for production issue for which cancellation not allowed for order fulfilled
										// from WH and partial qty waved and trying to cancel remaining
										whQty = fulfilDetail.getFulfillConfQty().intValue()
												- (fulfilDetail.getFulfillDeliverQty().intValue()
														+ fulfilDetail.getFulfillCancelQty().intValue())
												- totalSelectedDistroQty.intValue();
										if (whQty > 0) {
											if (L_fo_open_qty > whQty) {
												log.info("Cancelling WH transfer");
												interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
														fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
												L_fo_open_qty = L_fo_open_qty - whQty;
											} else {
												interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
														fulfilDetail, item, L_fo_open_qty, omsCustOrdItem,
														(extCustOrdNo));
												// L_fo_open_qty=0;
											}
										}
									}
								}
							} else {
								// Calling RMS cancelFulfilOrdColRef
								if (fulfilDetail.getSourceLocType().equals("WH")) {
									List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,
											fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
											fulfilDetail.getFulfillLoc());
									if (ordCustList.get(0).getTsfNo() != null
											&& ordCustList.get(0).getTsfNo().intValue() != 0) {
										log.info("Transfer No" + ordCustList.get(0).getTsfNo());
										log.info("Item " + fulfilDetail.getItem().toString());
										BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
										OMSUtilJdbc omsUtilJdbc = new OMSUtilJdbc();
										try {
											totalSelectedDistroQty = omsUtilJdbc.getselectedandDistroQty(
													fulfilDetail.getItem(), ordCustList.get(0).getTsfNo());
											log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);

										} catch (Exception e) {
											log.info("Exception from getselectedandDistroQty " + e.getMessage());
											totalSelectedDistroQty = BigDecimal.ZERO;
										}
										log.info("totalSelectedDistroQty  " + totalSelectedDistroQty);
										if (totalSelectedDistroQty.intValue() != 0) {
											// for production issue for which cancellation not allowed for order
											// fulfilled from WH and partial qty waved and trying to cancel remaining
											whQty = fulfilDetail.getFulfillConfQty().intValue()
													- (fulfilDetail.getFulfillDeliverQty().intValue()
															+ fulfilDetail.getFulfillCancelQty().intValue())
													- totalSelectedDistroQty.intValue();
											if (whQty > 0) {
												if (L_fo_open_qty > whQty) {
													log.info("Cancelling WH transfer");
													interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
															fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
													L_fo_open_qty = L_fo_open_qty - whQty;
												} else {
													interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
															fulfilDetail, item, L_fo_open_qty, omsCustOrdItem,
															(extCustOrdNo));
													// L_fo_open_qty=0;
												}
											}
											// continue;
										} else {
											whQty = fulfilDetail.getFulfillConfQty().intValue()
													- (fulfilDetail.getFulfillDeliverQty().intValue()
															+ fulfilDetail.getFulfillCancelQty().intValue());
											log.info("qty to cancel in WH " + whQty);
											if (L_fo_open_qty > whQty) {
												interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
														fulfilDetail, item, whQty, omsCustOrdItem, (extCustOrdNo));
												L_fo_open_qty = L_fo_open_qty - whQty;
											} else {
												interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
														fulfilDetail, item, L_fo_open_qty, omsCustOrdItem,
														(extCustOrdNo));
												// L_fo_open_qty=0;
											}

										}
									} else {
										interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo, fulfilDetail,
												item, L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
									}
								} else {
									// store to store transfer
									List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,
											fulfilDetail.getFulfillOrderNo().toString(), fulfilDetail.getSourceLoc(),
											fulfilDetail.getFulfillLoc());

									StsTsfHdrColDesc stsTsfHdrColDesc = oMSUtilCommons.callSimLookupTransferHeader(
											fulfilDetail.getSourceLoc().longValue(),
											ordCustList.get(0).getTsfNo().longValue());
									StsTsfDesc stsTsfDesc = oMSUtilCommons.callSimReadTransferDetail(
											fulfilDetail.getSourceLoc().longValue(),
											stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
									List<StsTsfItm> stsTsfItmList = stsTsfDesc.getStsTsfItm();

									// Change for transfer issue
									for (StsTsfItm stsTsfItm : stsTsfItmList) {
										if (stsTsfItm.getItemId().equals(fulfilDetail.getItem())) {
											log.info("stsTsfItm.getItemId() " + stsTsfItm.getItemId());
											log.info("fulfilDetail.getItem() " + fulfilDetail.getItem());
											log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus() "
													+ stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus());
											if (stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus().value()
													.equals("PENDING")) {
												log.info("Transfer is in pending status");
												log.info("transfer id="
														+ stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()
														+ "line id=" + stsTsfItm.getLineId() + "Requested qty="
														+ stsTsfItm.getRequestedQuantity()
																.subtract(new BigDecimal(L_fo_open_qty)));
												log.info("stsTsfItm.getRequestedQuantity() "
														+ stsTsfItm.getRequestedQuantity());
												log.info("item.getCancelQtySuom() " + itemList.getCancelledQuantity());
												log.info("L_fo_open_qty " + L_fo_open_qty);
												if (stsTsfItm.getRequestedQuantity()
														.subtract(new BigDecimal(L_fo_open_qty)).intValue() > 0) {
													log.info(
															"calling SIMSaveTransferRequest when stsTsfItm.getRequestedQuantity().subtract(new BigDecimal(L_fo_open_qty)).intValue()>0 ");
													oMSUtilCommons.callSimSaveTransferRequest(
															stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId(),
															stsTsfItm.getLineId(), stsTsfItm.getItemId(),
															stsTsfItm.getRequestedQuantity()
																	.subtract(new BigDecimal(L_fo_open_qty)),
															stsTsfDesc.getSendingStoreId(),
															stsTsfDesc.getReceivingStoreId());
													log.info("calling RMS when status is pending");
													interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
															fulfilDetail, item, L_fo_open_qty, omsCustOrdItem,
															(extCustOrdNo));
												} else if (stsTsfItm.getRequestedQuantity()
														.subtract(new BigDecimal(L_fo_open_qty)).intValue() <= 0) {

													// added for internal bug pending transfer cancellation
													if (stsTsfItmList.size() > 1) {
														oMSUtilCommons.cancelPendingTransfer(
																stsTsfHdrColDesc.getStsTsfHdrDesc().get(0)
																		.getTransferId(),
																stsTsfItm.getLineId(), stsTsfItm.getItemId(),
																stsTsfItm.getRequestedQuantity()
																		.subtract(new BigDecimal(L_fo_open_qty)),
																stsTsfDesc.getSendingStoreId(),
																stsTsfDesc.getReceivingStoreId());
													}
													if (stsTsfItmList.size() == 1) {
														long transferId = oMSUtilCommons
																.cancelApprovedTransferRequestForStore2Store(
																		new BigDecimal(
																				stsTsfHdrColDesc.getStsTsfHdrDesc()
																						.get(0).getTransferId()),
																		fulfilDetail.getFulfillLoc());
														log.info("cancelled transfer Id  from SIM " + transferId);
														log.info("Tsf_No " + ordCustList.get(0).getTsfNo());
														log.info("fulfillLoc " + fulfilDetail.getFulfillLoc());

													}

													log.info("calling RMS when status is pending");
													interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo,
															fulfilDetail, item,
															stsTsfItm.getRequestedQuantity().intValue(), omsCustOrdItem,
															(extCustOrdNo));

													log.info(
															"calling  persistintoOmsSimCancelTsfDetail method when the status is pending");
													log.info("omsCancelId " + omsCancelId);
													log.info("omsCustOrderNo " + omsCustOrderNo);
													log.info("fulfilDetail.getFulfillOrderNo() "
															+ fulfilDetail.getFulfillOrderNo());
													log.info(
															"stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() "
																	+ stsTsfHdrColDesc.getStsTsfHdrDesc().get(0)
																			.getTransferId());
													log.info("fulfilDetail.getSourceLoc() "
															+ fulfilDetail.getSourceLoc());
													log.info("item.getItem() " + fulfilDetail.getItem());
													log.info("item.getLineNo() " + fulfilDetail.getLineNo());
													log.info("stsTsfItm.getRequestedQuantity() "
															+ stsTsfItm.getRequestedQuantity());
													oMSUtilCommons.persistintoOmsSimCancelTsfDetail(omsCancelId,
															omsCustOrderNo, fulfilDetail.getFulfillOrderNo(),
															new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0)
																	.getTransferId()),
															fulfilDetail.getSourceLoc(), fulfilDetail.getItem(),
															fulfilDetail.getLineNo(), stsTsfItm.getRequestedQuantity());

												}
												// Added for pending transfers
												oMSUtilCommons.detectQuantityfromUnapprovedTransfers(omsCustOrderNo,
														ordCustList.get(0).getTsfNo(), fulfilDetail.getItem(),
														fulfilDetail.getSourceLoc(), new BigDecimal(L_fo_open_qty));

											} else {
												// Added for pending transfers to delete when it is approved and not
												// deleted
												ArrayList<BigDecimal> transferList = new ArrayList<BigDecimal>();
												transferList.add(ordCustList.get(0).getTsfNo());
												oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList,
														omsCustOrderNo);

												log.info("Transfer is in approved status");
												log.info("stsTsfItm.getRequestedQuantity() "
														+ stsTsfItm.getRequestedQuantity());
												log.info("itemList.getCancelledQuantity() "
														+ itemList.getCancelledQuantity());
												log.info("L_fo_open_qty " + L_fo_open_qty);

												log.info(
														"calling  persistintoOmsSimCancelTsfDetail method when the status is non-pending");
												log.info("omsCancelId " + omsCancelId);
												log.info("omsCustOrderNo " + omsCustOrderNo);
												log.info("fulfilDetail.getFulfillOrderNo() "
														+ fulfilDetail.getFulfillOrderNo());
												log.info("stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId() "
														+ stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId());
												log.info("fulfilDetail.getSourceLoc() " + fulfilDetail.getSourceLoc());
												log.info("item.getItem() " + fulfilDetail.getItem());
												log.info("item.getLineNo() " + fulfilDetail.getLineNo());
												log.info(
														"Math.min(stsTsfItm.getRequestedQuantity().intValue(),new BigDecimal(L_fo_open_qty).intValue()) "
																+ new BigDecimal(Math.min(
																		stsTsfItm.getRequestedQuantity().intValue(),
																		new BigDecimal(L_fo_open_qty).intValue())));
												oMSUtilCommons.persistintoOmsSimCancelTsfDetail(omsCancelId,
														omsCustOrderNo, fulfilDetail.getFulfillOrderNo(),
														new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc().get(0)
																.getTransferId()),
														fulfilDetail.getSourceLoc(), fulfilDetail.getItem(),
														fulfilDetail.getLineNo(),
														new BigDecimal(
																Math.min(stsTsfItm.getRequestedQuantity().intValue(),
																		new BigDecimal(L_fo_open_qty).intValue())));

												log.info("transfer Id " + new BigDecimal(
														stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getTransferId()));
												log.info("store id " + fulfilDetail.getSourceLoc());
												log.info("L_fo_open_qty " + L_fo_open_qty);
												log.info("fulfilDetail.getFulfillOrderNo() "
														+ fulfilDetail.getFulfillOrderNo());
												log.info("fulfilDetail.getLineNo() " + fulfilDetail.getLineNo());
												log.info("fulfilDetail.getItem() " + fulfilDetail.getItem());
												log.info("fulfilDetail.getFulfillConfQty() "
														+ fulfilDetail.getFulfillConfQty());
												log.info("fulfilDetail.getFulfillCancelQty() "
														+ fulfilDetail.getFulfillCancelQty());
												log.info("fulfilDetail.getFulfillDeliverQty() "
														+ fulfilDetail.getFulfillDeliverQty());
												log.info(
														"fulfilDetail.getFulfillConfQty().subtract(fulfilDetail.getFulfillCancelQty().add(fulfilDetail.getFulfillDeliverQty())).intValue() "
																+ fulfilDetail.getFulfillConfQty()
																		.subtract(fulfilDetail.getFulfillCancelQty()
																				.add(fulfilDetail
																						.getFulfillDeliverQty()))
																		.intValue());
												// if(L_fo_open_qty==fulfilDetail.getFulfillConfQty().subtract(fulfilDetail.getFulfillCancelQty().add(fulfilDetail.getFulfillDeliverQty())).intValue())
												// {
												// Added code for 2471 defect
												boolean flag = false;
												try {
													log.info(
															"===========calling cancellingSingleTransferForMultipleItems========");
													log.info("extCustOrdNo " + extCustOrdNo);
													log.info("L_fo_open_qty " + L_fo_open_qty);
													log.info(" omsCustOrderNo " + omsCustOrderNo);
													log.info("ordCustList.get(0).getTsfNo() "
															+ ordCustList.get(0).getTsfNo());
													log.info("fulfilDetail.getSourceLoc() "
															+ fulfilDetail.getSourceLoc());
													log.info("fulfilDetail.getFulfillLoc() "
															+ fulfilDetail.getFulfillLoc());
													flag = oMSUtilCommons.cancellingSingleTransferForMultipleItems(
															extCustOrdNo, new BigDecimal(L_fo_open_qty), omsCustOrderNo,
															ordCustList.get(0).getTsfNo(), fulfilDetail.getSourceLoc(),
															fulfilDetail.getFulfillLoc());
													log.info("flag " + flag);
												} catch (Exception e) {
													log.info(
															"Exception calling cancellingSingleTransferForMultipleItems "
																	+ e.getMessage());
												}
												if (flag) {
													long transferId = oMSUtilCommons
															.cancelApprovedTransferRequestForStore2Store(
																	new BigDecimal(stsTsfHdrColDesc.getStsTsfHdrDesc()
																			.get(0).getTransferId()),
																	fulfilDetail.getSourceLoc());
													log.info("cancelled transfer Id " + transferId);
													log.info(
															"calling RMS when status is approved and cancelling the quantity completely");
													log.info("Tsf_No " + ordCustList.get(0).getTsfNo());
													log.info("omsCustOrdNo " + omsCustOrdNo);
													log.info("extCustOrdNo " + extCustOrdNo);
													List<Tsfdetail> tsfDetailList = session
															.getTsfDetailTransferQty(ordCustList.get(0).getTsfNo());
													log.info("tsfDetailList.size() " + tsfDetailList.size());
													for (Tsfdetail tsfdetail : tsfDetailList) {
														BigDecimal cancelQty = tsfdetail.getTsfQty();
														log.info("cancelQty " + cancelQty);

														interfacePersistence
																.callRMSCancelFulfilOrdColRefforApprovedTransfers(
																		fulfilDetail, cancelQty, tsfdetail.getItem(),
																		omsCustOrdItem, extCustOrdNo);
														log.info(
																"called RMS when status is approved and cancelling the quantity completely");
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
								interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo, fulfilDetail, item,
										L_fo_open_qty, omsCustOrdItem, extCustOrdNo);

							}
						}
						Long FulfillReqQty = Math.min(
								(fulfilDetail.getFulfillConfQty().longValue()
										- fulfilDetail.getFulfillDeliverQty().longValue()
										- fulfilDetail.getFulfillCancelQty().longValue()),
								(omsCoCancelItem.getCancelReqQty().longValue()
										- omsCoCancelItem.getCancelConfQty().longValue()));
						if (FulfillReqQty <= L_fo_open_qty) {
							fulfilDetail.setFulfillCancelQty(
									new BigDecimal(FulfillReqQty).add(fulfilDetail.getFulfillCancelQty()));
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
						log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + "L_fo_open_qty=" + L_fo_open_qty);
						omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
						log.info(" mergin this in omscoCanelItem L_cum_cancel_qty" + L_cum_cancel_qty);
						session.mergeOmsCoCancelItem(omsCoCancelItem);
						// updating temp table
						omsCoFoCancel.setWsResponse("C");
						session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
						if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {
							log.info("inside L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY where L_cum_cancel_qty="
									+ L_cum_cancel_qty + "and L_CO_ITEM_CANCEL_QTY=" + L_CO_ITEM_CANCEL_QTY);
							continue; // Fetching another FO
						} else {
							break;
						}
					} // else if (fulfilDetail.getSourceLocType() == null ||
						// fulfilDetail.getSourceLocType() == "" ||
						// fulfilDetail.getSourceLocType().isEmpty()) {
					else if (fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) == 0) {
						boolean simvalue = interfacePersistence.callSIMPingMethod();
						boolean rmsvalue = interfacePersistence.callRMSPingMethod();
						log.info("simvalue " + simvalue);
						log.info("rmsvalue " + rmsvalue);

						if (simvalue == false || rmsvalue == false) {
							error = "ERROR_206"; // SIM and RMS WS is down
							status = "F";
							break;
						}

						if (simvalue == true && rmsvalue == true ) {
							// cancel reservation
							log.info("inside reservation condition");
							StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
							StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService
									.getStoreFulfillmentOrderPort();
							// Step 1: Call SIM webservice
							// StoreFulfillmentOrderService/lookupFulfillmentOrderHeaders.
							StrFordHdrColDesc strFordHdrColDesc = interfacePersistence
									.callSIMLookupFulfillmentOrderHeaders(fulfilDetail, fulilmentOrdNo, extCustOrdNo);
							log.info("size is " + strFordHdrColDesc.getStrFordHdrDesc().size());
							// Step 2 : Call SIM webservice
							// StoreFulfillmentOrderService/readFulfillmentOrderDetails.
							StrFordDesc strFordDesc = interfacePersistence
									.callSIMReadFulfillmentOrderDetail(strFordHdrColDesc);
							long pickedQty = 0;
							long delievedQty = 0;
							long orderQty = 0;
							long lineId = 0;
							long canceldQty = 0;
							long reserved_qty = 0;
							long actual_reserved_qty = 0;
							long to_be_picekd;
							long reverseQty = 0;
							for (StrFordItm strFordItm : strFordDesc.getStrFordItm()) {
								if (strFordItm.getItemId().equals(item)) {
									pickedQty = strFordItm.getPickedQty().longValue();
									delievedQty = strFordItm.getDeliveredQty().longValue();
									orderQty = strFordItm.getOrderQty().longValue();
									lineId = strFordItm.getLineId();
									canceldQty = strFordItm.getCanceledQty().longValue();
									reserved_qty = strFordItm.getReservedQty().longValue();
									log.info("pickedQty=" + pickedQty + "delievedQty=" + delievedQty + "canceldQty="
											+ canceldQty + "reserved_qty=" + reserved_qty + "L_fo_open_qty="
											+ L_fo_open_qty);
									break;
								}
							}
							// code for 2766
							// check the open delivery, if exist cancel it then do reverse pick
							OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
							FodHdrColDesc fodHdrColDesc = omsUtilCommons.isDeliveryCreated(
									strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
							log.info("Delivery exist if size >0 otherwise doesn't exist and size is "
									+ fodHdrColDesc.getFodHdrDesc().size());
							if (fodHdrColDesc.getFodHdrDesc().size() > 0) {

								omsUtilCommons.cancelDelivery(fodHdrColDesc);
							}
							BigDecimal qtyToBeCanceledinSIM = BigDecimal.ZERO;
							log.info("cancel qty=" + itemList.getCancelledQuantity());
							if (pickedQty == delievedQty) {
								log.info("pickedQty == delievedQty");
								actual_reserved_qty = reserved_qty;
								// if (itemList.getCancelledQuantity().longValue() <= actual_reserved_qty)
								// if (L_fo_open_qty <= actual_reserved_qty)
								if (L_fo_open_qty < actual_reserved_qty) {
									log.info("L_fo_open_qty <= actual_reserved_qty");
									// qtyToBeCanceledinSIM = itemList.getCancelledQuantity();
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									// fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());

								}
								// else if (itemList.getCancelledQuantity().longValue() > actual_reserved_qty)
								// else if (L_fo_open_qty > actual_reserved_qty)
								else if (L_fo_open_qty >= actual_reserved_qty) {
									log.info("L_fo_open_qty > actual_reserved_qty");
									qtyToBeCanceledinSIM = new BigDecimal(actual_reserved_qty);
									// fulfilOrdDtlRef.setCancelQtySuom(new BigDecimal(actual_reserved_qty));
								}
								try {
									log.info("Order no=" + extCustOrdNo + "Calling SIM cancellation for fulilmentOrdNo="
											+ fulilmentOrdNo + "qtyToBeCanceledinSIM=" + qtyToBeCanceledinSIM);
									// Step 3: Call SIM CancelFulfillmentOrderDetail
									interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
											fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
											qtyToBeCanceledinSIM, extCustOrdNo);
								} catch (Exception e) {
									if (retryCancelFulfillmentOrderDetail1 < 2) {
										retryCancelFulfillmentOrderDetail1++;
										interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
												fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
												qtyToBeCanceledinSIM, extCustOrdNo);
									}
								}
							} else if (pickedQty > delievedQty) {

								log.info("Condition 2");
								to_be_picekd = pickedQty - delievedQty;
								actual_reserved_qty = reserved_qty - to_be_picekd;

								if (L_fo_open_qty <= actual_reserved_qty)
								// if (itemList.getCancelledQuantity().longValue() <= actual_reserved_qty)
								{
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									// Step 4 :calling cancelFulfillmentOrderDetail
									log.info(
											"calling cancelFulfillmentOrderDetail when pickedQty>delievedQty and Cancel_qty<=actual_reserved_qty");
									try {
										log.info("Order no=" + extCustOrdNo
												+ "Calling SIM cancellation for fulilmentOrdNo=" + fulilmentOrdNo
												+ "qtyToBeCanceledinSIM=" + qtyToBeCanceledinSIM);

										interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
												fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
												qtyToBeCanceledinSIM, extCustOrdNo);
									} catch (Exception e) {
										if (retryCancelFulfillmentOrderDetail2 < 2) {
											retryCancelFulfillmentOrderDetail2++;
											interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
													fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem,
													actual_reserved_qty, qtyToBeCanceledinSIM, extCustOrdNo);
										} else {
											status = "ERROR_106";
											log.info("Error in calling sim webservice cancelFulfillmentOrderDetail");
										}
									}
								} else if (L_fo_open_qty > actual_reserved_qty) {
									log.info(" when pickedQty>delievedQty and Cancel_qty>actual_reserved_qty");
									if (L_fo_open_qty == reserved_qty) {
										log.info("when cancelled qty is equal to reserved quantity");
										reverseQty = L_fo_open_qty - actual_reserved_qty;
									} else if (L_fo_open_qty < reserved_qty) {
										log.info("when cancelled qty is less than reserved qty");
										if (to_be_picekd > L_fo_open_qty - actual_reserved_qty) {
											log.info("when toBePicked qty is greater than cancelled qty");
											reverseQty = L_fo_open_qty - actual_reserved_qty;
										}
									} else if (L_fo_open_qty > reserved_qty) {
										log.info("when cancelled qty is greater than reserved qty");
										if (to_be_picekd < L_fo_open_qty - actual_reserved_qty) {
											reverseQty = to_be_picekd;
										}
									}
									// calling createReversePick
									log.info("calling createReversePick");
									ForpRef forpRef = null;
									try {
										// Step 5: Call SIM CreateReversePick
										forpRef = interfacePersistence.callSIMCreateReversePick(lineId, reverseQty,
												strFordHdrColDesc);
									} catch (Exception e) {
										if (retryCreateReversePick < 2) {
											retryCreateReversePick++;
											forpRef = interfacePersistence.callSIMCreateReversePick(lineId, reverseQty,
													strFordHdrColDesc);
										} else {
											error = "ERROR_106";
											log.info("Error in calling sim webservice createReversePick");
											status = "F";
											break;
										}
									}
									long reversePickId = forpRef.getReversePickId();
									// calling confirmReversePick
									log.info("calling confirmReversePick");
									InvocationSuccess invocationSuccess;
									// Step 6: Call SIM ConfirmReversePick
									try {
										invocationSuccess = interfacePersistence.callSIMConfirmReversePick(forpRef);
										log.info("Called SIMConfirmReversePick method");
									} catch (Exception e) {
										if (retryConfirmReversePick < 2) {
											invocationSuccess = interfacePersistence.callSIMConfirmReversePick(forpRef);
										} else {
											// throw new SOAPException("Error in calling sim webservice
											// confirmReversePick");
										}
									}
									// calling cancelFulfillmentOrderDetail
									log.info("calling cancelFulfillmentOrderDetail");
									qtyToBeCanceledinSIM = new BigDecimal(L_fo_open_qty);
									// Step 7: Call Sim CancelFulfillmentOrderDetail
									try {
										interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
												fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem, actual_reserved_qty,
												qtyToBeCanceledinSIM, extCustOrdNo);
									} catch (Exception e) {
										if (retryCancelFulfillmentOrderDetail3 < 2) {
											retryCancelFulfillmentOrderDetail3++;
											interfacePersistence.callSIMCancelFulfillmentOrderDetail(custOrderPicVo,
													fulfilDetail, item, fulilmentOrdNo, omsCustOrdItem,
													actual_reserved_qty, qtyToBeCanceledinSIM, extCustOrdNo);
										} else {
											// throw new SOAPException("Error in calling sim webservice
											// cancelFulfillmentOrderDetail");
										}
									}
									// Saving data in oms_revs_pick table in case of reverse pick
									OmsRevsPick omsRevsPick = new OmsRevsPick();
									omsRevsPick.setOmsCustOrdNo(omsCustOrdNo);
									omsRevsPick.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));
									omsRevsPick.setCreateDatetime(new Timestamp(new Date().getTime()));
									omsRevsPick.setItem(item);
									omsRevsPick.setPuQty(BigDecimal.ZERO);
									omsRevsPick.setLoc(fulfilDetail.getSourceLoc());
									omsRevsPick.setRevPicQty(new BigDecimal(reverseQty));
									session.persistOmsRevsPick(omsRevsPick);
									log.info("Successfully persisted into OmsRevPick");
								}

							}
							log.info("actual_reserved_qty in SIM " + actual_reserved_qty);
							log.info("qtyToBeCanceledinSIM in SIM " + qtyToBeCanceledinSIM);
							log.info("L_fo_open_qty " + L_fo_open_qty);
							log.info("L_cum_cancel_qty " + L_cum_cancel_qty);
							log.info("L_CO_ITEM_CANCEL_QTY " + L_CO_ITEM_CANCEL_QTY);

							if (L_fo_open_qty > qtyToBeCanceledinSIM.longValue()) {
								interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo, fulfilDetail, item,
										qtyToBeCanceledinSIM.longValue(), omsCustOrdItem, (extCustOrdNo));
								L_fo_open_qty = L_fo_open_qty - qtyToBeCanceledinSIM.longValue();
								log.info("L_fo_open_qty =========" + L_fo_open_qty);
								fulfilDetail.setFulfillCancelQty(new BigDecimal(qtyToBeCanceledinSIM.longValue())
										.add(fulfilDetail.getFulfillCancelQty()));
								session.mergeOmsCoFulfillDetail(fulfilDetail);
								log.info("Merged omsCoFulfillDetail");
								omsCoFoCancel.setFoCancelledOty(new BigDecimal(qtyToBeCanceledinSIM.longValue()));
								session.mergeOmsCoFoCancel(omsCoFoCancel);
								L_cum_cancel_qty = L_cum_cancel_qty + qtyToBeCanceledinSIM.longValue();
								log.info("L_cum_cancel_qty ==in if==========" + L_cum_cancel_qty);
							} else {
								interfacePersistence.callRMSCancelFulfilOrdColRef(custOrderPicVo, fulfilDetail, item,
										L_fo_open_qty, omsCustOrdItem, (extCustOrdNo));
								fulfilDetail.setFulfillCancelQty(
										new BigDecimal(L_fo_open_qty).add(fulfilDetail.getFulfillCancelQty()));
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
							log.info("L_cum_cancel_qty=" + L_cum_cancel_qty + " L_fo_open_qty= " + L_fo_open_qty
									+ " L_CO_ITEM_CANCEL_QTY " + L_CO_ITEM_CANCEL_QTY);
							// updating oms_co_fo_cancel table
							L_fo_open_qty = L_CO_ITEM_CANCEL_QTY - L_cum_cancel_qty;
							omsCoFoCancel.setWsResponse("C");
							session.mergeOmsCoFoCancel(omsCoFoCancel); // need to check whether to create new object
							omsCoCancelItem.setCancelConfQty(new BigDecimal(L_cum_cancel_qty));
							session.mergeOmsCoCancelItem(omsCoCancelItem);
							// calling RMS webservice cancelFulfilOrdColRef
							if (L_cum_cancel_qty < L_CO_ITEM_CANCEL_QTY) {

								continue; // Fetching another FO
							} else if (L_cum_cancel_qty == L_CO_ITEM_CANCEL_QTY) {
								break;
							}
						}

					} // end of pingmethods
					/*
					 * else { error="ERROR_206";//SIM and RMS WS is down status="F"; break; }
					 */

				} // end od resv
					// omsPersistence.persistCustOrdLogItem(itemList,item,fulilmentOrdNo, logSeqNo,
					// custOrderPicVo);
				omsPersistence.persistCustOrdLogItem(itemList, item, fulilmentOrdNo, logSeqNo, customerOrderNo);

				omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item,
						new BigDecimal(itemList.getLineItemNo()));
				log.info("omsCoCancelItem.getCancelConfQty()" + omsCoCancelItem.getCancelConfQty());
				log.info("omsCustOrdItem.getQtyCancelled()" + omsCustOrdItem.getQtyCancelled());
				omsCustOrdItem
						.setQtyCancelled(omsCoCancelItem.getCancelConfQty().add(omsCustOrdItem.getQtyCancelled()));

				session.mergeOmsCustOrdItem(omsCustOrdItem);
				log.info("merged OmsCustOrdLogItem");
			}
			cancelShippingChargeItem(custOrdNo, itemList, omsCancelId);

		}
		return status;
	}

	public void notifySiebel(CustOrderPicVo input, List<CustOrdItmPkVo> cancellItemList, BigDecimal omsCancelId)
			throws SOAPException {
		log.info("Inside Notify Siebel Method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String custOrderNo = customerOrderNo;
		// BigDecimal omsOrposCustOrderId =
		// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderNo);
		BigDecimal custOrderPicVoSeqNo = session.getMaxOmsOrposCustOrderPickUp(customerOrderNo);
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		// List<OmsCoCancelHead> omsCancelIdList = null;
		// omsCancelIdList =
		// session.getOmsCoCancelHeadFindByCustOrdNo(input.getCustomerOrderId());

		// BigDecimal omsCancelId = omsCancelIdList.get(0);
		// log.info("cancel id" + omsCancelId);

		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("omsCustOrdHead.getApplicationId() " + omsCustOrdHead.getApplicationId());
		InterfacePersistence interfacePersistance = new InterfacePersistence();
		log.info("Calling siebel");
		try {
			// interfacePersistance.callSeibelWebserviceForCancellation(omsCancelId, "1",
			// cancellItemList,backorderTreeMap);
			OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderForCancellation = interfacePersistance
					.getOmsOrderStatusUpdateHeaderForCancellation(omsCancelId, "1", cancellItemList, backorderTreeMap);
			
			/*
			 * OmsStatusUpdateForReturnPickupCancellation
			 * omsStatusUpdateForReturnPickupCancellationWSCall = new
			 * OmsStatusUpdateForReturnPickupCancellation();
			 * omsStatusUpdateForReturnPickupCancellationWSCall
			 * .callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(
			 * omsOrderStatusUpdateHeaderForCancellation);
			 */
			 
			interfacePersistance.saveOmsWSeibelRequest(omsOrderStatusUpdateHeaderForCancellation, session);

		} catch (Exception e) {
			log.info("Exception occured while siebel call " + e.getMessage());
		}
	}

	// Method for checking the null value for String

	public String checkNullValueForString(String value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public BigDecimal getCustomerOrderAlongWithSubOrderNo(String custOrderNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String subOrderId = null;
		String delimiter = "--";
		BigDecimal omsCustOrdNo = null;
		if (custOrderNo.contains("--")) {
			String[] orderId = custOrderNo.split(delimiter);
			// for(int i=0;i< orderId.length;i++){
			custOrderNo = orderId[0];
			subOrderId = orderId[1];
			log.info("custOrderId-->" + custOrderNo);
			log.info("subOrderId-->" + subOrderId);
			while ((subOrderId.length() > 1) && (subOrderId.charAt(0) == '0')) {
				subOrderId = subOrderId.substring(1);
			}
			log.info("After deleting the leading zeros " + subOrderId);
			// }
			try {
				omsCustOrdNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo, subOrderId);
			} catch (Exception e) {
				log.error("Failed in getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo with error:" + e);
				// throw new SOAPException(e);
			}
		} else {
			try {
				omsCustOrdNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderNo, "1");
			} catch (Exception e) {
				log.error("Failed in getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo with error:" + e);
				// throw new SOAPException(e);
			}
		}
		return omsCustOrdNo;
	}

	// Updating the OmsOrposCustOrderHead table with the help of cust_order_id
	/*
	 * public void updateOmsOrposCustOrderHead(String customerOrderId) throws
	 * SOAPException { log.info("Entered the updateOmsOrposCustOrderHead method");
	 * OMSUtilSessionEJB session = OMSUtil.doLookup(); //Initializing values for
	 * CustOrderHead table BigDecimal headPaidAmount=null; BigDecimal
	 * headRoundingAdjustment=null; BigDecimal headCompletedAmount=null; BigDecimal
	 * headCancelledAmount=null; BigDecimal headCompletedDiscountAmount=null;
	 * BigDecimal headCancelledDiscountAmount=null; BigDecimal
	 * headCompletedTaxAmount=null; BigDecimal headCancelledTaxAmount=null;
	 * BigDecimal headCompletedInclusiveTaxAmount=null; BigDecimal
	 * headCancelledInclusiveTaxAmount=null; BigDecimal omsOrposCustOrderId=null;
	 * 
	 * //Initializing values for CustOrderPickup BigDecimal pickUpPaidAmount=null;
	 * BigDecimal pickUpRoundingAdjustment=null; BigDecimal
	 * pickUpCompletedAmount=null; BigDecimal pickUpCancelledAmount=null; BigDecimal
	 * pickUpCompletedDiscountAmount=null; BigDecimal
	 * pickUpCancelledDiscountAmount=null; BigDecimal pickUpCompletedTaxAmount=null;
	 * BigDecimal pickUpCancelledTaxAmount=null; BigDecimal
	 * pickUpCompletedInclusiveTaxAmount=null; BigDecimal
	 * pickUpCancelledInclusiveTaxAmount=null;
	 * 
	 * 
	 * BigDecimal
	 * maxPickSeqNo=session.getMaxOmsOrposCustOrderPickUp(omsOrposCustOrdId);
	 * List<OmsOrposCustOrderHead>
	 * omsOrposCustOrderHeadList=session.getOmsOrposCustOrderHeadfindColumns(
	 * customerOrderId); for(OmsOrposCustOrderHead omsOrposCustOrderHead:
	 * omsOrposCustOrderHeadList) { log.info("Inside CustOrderHead loop");
	 * omsOrposCustOrderId=omsOrposCustOrderHead.getOmsOrposCustOrderId();
	 * 
	 * headPaidAmount=omsOrposCustOrderHead.getPaidAmount();
	 * headRoundingAdjustment=omsOrposCustOrderHead.getRoundingAdjustment();
	 * headCompletedAmount=omsOrposCustOrderHead.getCompletedAmount();
	 * headCancelledAmount=omsOrposCustOrderHead.getCancelledAmount();
	 * headCompletedDiscountAmount=omsOrposCustOrderHead.getCompletedDiscountAmount(
	 * );
	 * headCancelledDiscountAmount=omsOrposCustOrderHead.getCancelledDiscountAmount(
	 * ); headCompletedTaxAmount=omsOrposCustOrderHead.getCompletedTaxAmount();
	 * headCancelledTaxAmount=omsOrposCustOrderHead.getCancelledTaxAmount();
	 * headCompletedInclusiveTaxAmount=omsOrposCustOrderHead.
	 * getCompletedInclusiveTaxAmount();
	 * headCancelledInclusiveTaxAmount=omsOrposCustOrderHead.
	 * getCancelledInclusiveTaxAmount();
	 * 
	 * //Assigning zero to null values in OmsOrposCustOrderHead table
	 * 
	 * if(headPaidAmount==null||headRoundingAdjustment==null||headCompletedAmount==
	 * null||headCancelledAmount==null||headCompletedDiscountAmount==null
	 * ||headCancelledDiscountAmount==null||headCompletedTaxAmount==null||
	 * headCancelledTaxAmount==null||headCompletedInclusiveTaxAmount==null
	 * ||headCancelledInclusiveTaxAmount==null) { headPaidAmount=new BigDecimal(0);
	 * headRoundingAdjustment=new BigDecimal(0); headCompletedAmount=new
	 * BigDecimal(0); headCancelledAmount=new BigDecimal(0);
	 * headCompletedDiscountAmount=new BigDecimal(0);
	 * headCancelledDiscountAmount=new BigDecimal(0); headCompletedTaxAmount=new
	 * BigDecimal(0); headCancelledTaxAmount=new BigDecimal(0);
	 * headCompletedInclusiveTaxAmount=new BigDecimal(0);
	 * headCancelledInclusiveTaxAmount=new BigDecimal(0);
	 * 
	 * } List<OmsOrposCustOrderPickup>
	 * omsOrposCustOrderPickup=session.getOmsOrposCustOrderPickupFindAllColumns(
	 * maxPickSeqNo); for(OmsOrposCustOrderPickup omsOrposCustOrderPick:
	 * omsOrposCustOrderPickup) { log.info("Inside CustOrderPick loop");
	 * pickUpPaidAmount=omsOrposCustOrderPick.getPaidAmount();
	 * pickUpRoundingAdjustment=omsOrposCustOrderPick.getRoundingAdjustment();
	 * pickUpCompletedAmount=omsOrposCustOrderPick.getCompletedAmount();
	 * pickUpCancelledAmount=omsOrposCustOrderPick.getCancelledAmount();
	 * pickUpCompletedDiscountAmount=omsOrposCustOrderPick.
	 * getCompletedDiscountAmount();
	 * pickUpCancelledDiscountAmount=omsOrposCustOrderPick.
	 * getCancelledDiscountAmount();
	 * pickUpCompletedTaxAmount=omsOrposCustOrderPick.getCompletedTaxAmount();
	 * pickUpCancelledTaxAmount=omsOrposCustOrderPick.getCancelledTaxAmount();
	 * pickUpCompletedInclusiveTaxAmount=omsOrposCustOrderPick.
	 * getCompletedIncTaxAmount();
	 * pickUpCancelledInclusiveTaxAmount=omsOrposCustOrderPick.
	 * getCancelledIncTaxAmount();
	 * 
	 * //Assigning zero to all null values in OmsOrposCustOrderPickup table
	 * 
	 * if(pickUpPaidAmount==null||pickUpRoundingAdjustment==null||
	 * pickUpCompletedAmount==null||pickUpCancelledAmount==null||
	 * pickUpCompletedDiscountAmount==null
	 * ||pickUpCancelledDiscountAmount==null||pickUpCompletedTaxAmount==null||
	 * pickUpCancelledTaxAmount==null||pickUpCompletedInclusiveTaxAmount==null
	 * ||pickUpCancelledInclusiveTaxAmount==null) { pickUpPaidAmount=new
	 * BigDecimal(0); pickUpRoundingAdjustment=new BigDecimal(0);
	 * pickUpCompletedAmount=new BigDecimal(0); pickUpCancelledAmount=new
	 * BigDecimal(0); pickUpCompletedDiscountAmount=new BigDecimal(0);
	 * pickUpCancelledDiscountAmount=new BigDecimal(0); pickUpCompletedTaxAmount=new
	 * BigDecimal(0); pickUpCancelledTaxAmount=new BigDecimal(0);
	 * pickUpCompletedInclusiveTaxAmount=new BigDecimal(0);
	 * pickUpCancelledInclusiveTaxAmount=new BigDecimal(0); } }
	 * 
	 * log.info("Updation process starts"); int
	 * valuePaidAmount=headPaidAmount.compareTo(pickUpCompletedAmount);
	 * if(valuePaidAmount==0||valuePaidAmount==1) {
	 * omsOrposCustOrderHead.setPaidAmount(headPaidAmount.add(pickUpPaidAmount)); }
	 * if(valuePaidAmount==-1) {
	 * log.info("Pick Up CompletedAmount is greater than PaidAmount"); }
	 * omsOrposCustOrderHead.setRoundingAdjustment(headRoundingAdjustment.add(
	 * pickUpRoundingAdjustment));
	 * omsOrposCustOrderHead.setCompletedAmount(headCompletedAmount.add(
	 * pickUpCompletedAmount));
	 * omsOrposCustOrderHead.setCancelledAmount(headCancelledAmount.add(
	 * pickUpCancelledAmount));
	 * omsOrposCustOrderHead.setCompletedDiscountAmount(headCompletedDiscountAmount.
	 * add(pickUpCompletedDiscountAmount));
	 * omsOrposCustOrderHead.setCancelledDiscountAmount(headCancelledDiscountAmount.
	 * add(pickUpCancelledDiscountAmount));
	 * omsOrposCustOrderHead.setCompletedTaxAmount(headCompletedTaxAmount.add(
	 * pickUpCompletedTaxAmount));
	 * omsOrposCustOrderHead.setCancelledTaxAmount(headCancelledTaxAmount.add(
	 * pickUpCancelledTaxAmount));
	 * omsOrposCustOrderHead.setCompletedInclusiveTaxAmount(
	 * headCompletedInclusiveTaxAmount.add(pickUpCompletedInclusiveTaxAmount));
	 * omsOrposCustOrderHead.setCancelledInclusiveTaxAmount(
	 * headCancelledInclusiveTaxAmount.add(pickUpCancelledInclusiveTaxAmount));
	 * session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
	 * log.info("Updated OmsOrposCustOrderHead successfully !");
	 * updateOmsOrposCustOrdItm(omsOrposCustOrderId,maxPickSeqNo); } }
	 */

	// Updating the OmsOrposCustOrdItm table with the help of cust_order_id
	/*
	 * public void updateOmsOrposCustOrdItm(BigDecimal
	 * omsOrposCustOrderId,BigDecimal custOrderPicVoSeq) throws SOAPException {
	 * OMSUtilSessionEJB session = OMSUtil.doLookup(); System.out.
	 * println("=======================Inside OmsOrposCustOrdItm method=============================="
	 * );
	 * 
	 * //Initializing values for CustOrderItm table String headItemId=null;
	 * BigDecimal headLineItemNo=new BigDecimal(0); BigDecimal
	 * headCapturedLineItemNo=null; BigDecimal headQuantity=null; BigDecimal
	 * headAvailableQuantity=null; BigDecimal headCompletedQuantity=null; BigDecimal
	 * headCancelledQuantity=null; BigDecimal headCompletedAmount=null; BigDecimal
	 * headCancelledAmount=null; BigDecimal headPaidAmount=null; BigDecimal
	 * headCompletedDiscountAmount=null; BigDecimal
	 * headCancelledDiscountAmount=null; BigDecimal headCompletedTaxAmount=null;
	 * BigDecimal headCancelledTaxAmount=null; BigDecimal
	 * headCompletedInclusiveTaxAmount=null; BigDecimal
	 * headCancelledInclusiveTaxAmount=null;
	 * 
	 * //Initializing values for CustOrderItmPickup BigDecimal
	 * pickUpLineItemNo=null; BigDecimal pickUpAvailableQuantity=null; BigDecimal
	 * pickUpCompletedQuantity=null; BigDecimal pickUpCancelledQuantity=null;
	 * BigDecimal pickUpCompletedAmount=null; BigDecimal pickUpCancelledAmount=null;
	 * BigDecimal pickUpPaidAmount=null; BigDecimal
	 * pickUpCompletedDiscountAmount=null; BigDecimal
	 * pickUpCancelledDiscountAmount=null; BigDecimal pickUpCompletedTaxAmount=null;
	 * BigDecimal pickUpCancelledTaxAmount=null; BigDecimal
	 * pickUpCompletedInclusiveTaxAmount=null; BigDecimal
	 * pickUpCancelledInclusiveTaxAmount=null; BigDecimal
	 * pickUpCompletedRepricedQuantity=null; BigDecimal
	 * pickUpRepricedDiscountAmount=null; BigDecimal pickUpRepricedAmount=null;
	 * BigDecimal pickUpRepricedTaxAmount=null; BigDecimal
	 * pickUpRepricedIncTaxAmount=null;
	 * 
	 * System.out.
	 * println("=======================Inside omsOrposCustOrdItmList /Loop=============================="
	 * ); List<OmsOrposCustOrdItm>
	 * omsOrposCustOrdItmList=session.getOmsOrposCustOrdItmFindColumns(
	 * omsOrposCustOrderId); for(OmsOrposCustOrdItm omsOrposCustOrdItm:
	 * omsOrposCustOrdItmList) { log.info("Inside OmsOrposCustOrdItm loop");
	 * omsOrposCustOrderId=omsOrposCustOrdItm.getOmsOrposCustOrderId();
	 * headItemId=omsOrposCustOrdItm.getItemId();
	 * headLineItemNo=omsOrposCustOrdItm.getLineItemNo();
	 * headCapturedLineItemNo=omsOrposCustOrdItm.getCapturedLineItemNo();
	 * headQuantity=omsOrposCustOrdItm.getQuantity();
	 * headAvailableQuantity=omsOrposCustOrdItm.getAvailableQuantity();
	 * headCompletedQuantity=omsOrposCustOrdItm.getCompletedQuantity();
	 * headCancelledQuantity=omsOrposCustOrdItm.getCancelledQuantity();
	 * headCompletedAmount=omsOrposCustOrdItm.getCompletedAmount();
	 * headCancelledAmount=omsOrposCustOrdItm.getCancelledAmount();
	 * headPaidAmount=omsOrposCustOrdItm.getPaidAmount();
	 * headCompletedDiscountAmount=omsOrposCustOrdItm.getCompletedDiscountAmount();
	 * headCancelledDiscountAmount=omsOrposCustOrdItm.getCancelledDiscountAmount();
	 * headCompletedTaxAmount=omsOrposCustOrdItm.getCompletedTaxAmount();
	 * headCancelledTaxAmount=omsOrposCustOrdItm.getCancelledTaxAmount();
	 * headCompletedInclusiveTaxAmount=omsOrposCustOrdItm.
	 * getCompletedInclusiveTaxAmount();
	 * headCancelledInclusiveTaxAmount=omsOrposCustOrdItm.
	 * getCancelledInclusiveTaxAmount();
	 * 
	 * if(headAvailableQuantity==null||headCompletedQuantity==null||
	 * headCancelledQuantity==null||headCompletedAmount==null||headCancelledAmount==
	 * null ||headPaidAmount==null||headCompletedDiscountAmount==null||
	 * headCancelledDiscountAmount==null||headCompletedTaxAmount==null
	 * ||headCancelledTaxAmount==null||headCompletedInclusiveTaxAmount==null||
	 * headCancelledInclusiveTaxAmount==null) { headAvailableQuantity=new
	 * BigDecimal(0); headCompletedQuantity=new BigDecimal(0);
	 * headCancelledQuantity=new BigDecimal(0); headCompletedAmount=new
	 * BigDecimal(0); headCancelledAmount=new BigDecimal(0); headPaidAmount=new
	 * BigDecimal(0); headCompletedDiscountAmount=new BigDecimal(0);
	 * headCancelledDiscountAmount=new BigDecimal(0); headCompletedTaxAmount=new
	 * BigDecimal(0); headCancelledTaxAmount=new BigDecimal(0);
	 * headCompletedInclusiveTaxAmount=new BigDecimal(0);
	 * headCancelledInclusiveTaxAmount=new BigDecimal(0);
	 * 
	 * }
	 * 
	 * List<OmsOrposCustOrdItmPickup> omsOrposCustOrdItmPickupList=session.
	 * getOmsOrposCustOrdItmPickupFindAllColumns(omsOrposCustOrderId,
	 * custOrderPicVoSeq); for(OmsOrposCustOrdItmPickup
	 * omsOrposCustOrdItmPickup:omsOrposCustOrdItmPickupList) {
	 * log.info("Inside OmsOrposCustOrdItmPickup loop");
	 * pickUpLineItemNo=omsOrposCustOrdItmPickup.getLineItemNo();
	 * pickUpCompletedQuantity=omsOrposCustOrdItmPickup.getCompletedQuantity();
	 * pickUpCancelledQuantity=omsOrposCustOrdItmPickup.getCancelledQuantity();
	 * pickUpCompletedAmount=omsOrposCustOrdItmPickup.getCompletedAmount();
	 * pickUpCancelledAmount=omsOrposCustOrdItmPickup.getCancelledAmount();
	 * pickUpPaidAmount=omsOrposCustOrdItmPickup.getPaidAmount();
	 * pickUpCompletedDiscountAmount=omsOrposCustOrdItmPickup.
	 * getCompletedDiscountAmount();
	 * pickUpCancelledDiscountAmount=omsOrposCustOrdItmPickup.
	 * getCancelledDiscountAmount();
	 * pickUpCompletedTaxAmount=omsOrposCustOrdItmPickup.getCompletedTaxAmount();
	 * pickUpCancelledTaxAmount=omsOrposCustOrdItmPickup.getCancelledTaxAmount();
	 * pickUpCompletedInclusiveTaxAmount=omsOrposCustOrdItmPickup.
	 * getCompletedIncTaxAmount();
	 * pickUpCancelledInclusiveTaxAmount=omsOrposCustOrdItmPickup.
	 * getCancelledIncTaxAmount();
	 * pickUpCompletedRepricedQuantity=omsOrposCustOrdItmPickup.
	 * getCompletedRepricedQuantity();
	 * pickUpRepricedDiscountAmount=omsOrposCustOrdItmPickup.
	 * getRepricedDiscountAmount();
	 * pickUpRepricedAmount=omsOrposCustOrdItmPickup.getRepricedAmount();
	 * pickUpRepricedTaxAmount=omsOrposCustOrdItmPickup.getRepricedTaxAmount();
	 * pickUpRepricedIncTaxAmount=omsOrposCustOrdItmPickup.getRepricedIncTaxAmount()
	 * ; if(pickUpCompletedQuantity==null||pickUpCancelledQuantity==null||
	 * pickUpCompletedAmount==null||pickUpCancelledAmount==null
	 * ||pickUpPaidAmount==null||pickUpCompletedDiscountAmount==null||
	 * pickUpCancelledDiscountAmount==null||pickUpCompletedTaxAmount==null
	 * ||pickUpCancelledTaxAmount==null||pickUpCompletedInclusiveTaxAmount==null||
	 * pickUpCancelledInclusiveTaxAmount==null||pickUpCompletedRepricedQuantity==
	 * null ||pickUpRepricedDiscountAmount==null||pickUpRepricedAmount==null||
	 * pickUpRepricedTaxAmount==null||pickUpRepricedIncTaxAmount==null||
	 * pickUpLineItemNo==null) {
	 * 
	 * pickUpCompletedQuantity=new BigDecimal(0); pickUpCancelledQuantity=new
	 * BigDecimal(0); pickUpCompletedAmount=new BigDecimal(0);
	 * pickUpCancelledAmount=new BigDecimal(0); pickUpPaidAmount=new BigDecimal(0);
	 * pickUpCompletedDiscountAmount=new BigDecimal(0);
	 * pickUpCancelledDiscountAmount=new BigDecimal(0); pickUpCompletedTaxAmount=new
	 * BigDecimal(0); pickUpCancelledTaxAmount=new BigDecimal(0);
	 * pickUpCompletedInclusiveTaxAmount=new BigDecimal(0);
	 * pickUpCancelledInclusiveTaxAmount=new BigDecimal(0);
	 * pickUpCompletedRepricedQuantity=new BigDecimal(0);
	 * pickUpRepricedDiscountAmount=new BigDecimal(0); pickUpRepricedAmount=new
	 * BigDecimal(0); pickUpRepricedTaxAmount=new BigDecimal(0);
	 * pickUpRepricedIncTaxAmount=new BigDecimal(0); pickUpLineItemNo=new
	 * BigDecimal(0); }
	 * 
	 * //BigDecimal
	 * sumComplCancelQty=headCompletedQuantity.add(pickUpCompletedQuantity).add(
	 * headCancelledQuantity).add(pickUpCancelledQuantity).add(
	 * pickUpCompletedRepricedQuantity); // BigDecimal
	 * availableQtyNew=headAvailableQuantity.subtract(sumComplCancelQty);
	 * System.out.
	 * println("==========OmsOrposCustOrderID  from CustOrderHead=============="
	 * +omsOrposCustOrderId); System.out.println("====Item ID====="+headItemId);
	 * System.out.println("======Captured Line Item No=========================="
	 * +headCapturedLineItemNo); System.out.
	 * println("####################Initial Values#############################");
	 * System.out.println("=======Initial Quantity=============================="
	 * +headQuantity);
	 * System.out.println("====headAvailableQuantity====="+headAvailableQuantity);
	 * System.out.println("====headCompletedQuantity====="+headCompletedQuantity);
	 * System.out.println("====headCancelledQuantity====="+headCancelledQuantity);
	 * System.out.println("====headCompletedAmount====="+headCompletedAmount);
	 * System.out.println("====headCancelledAmount====="+headCancelledAmount);
	 * System.out.println("====headPaidAmount====="+headPaidAmount);
	 * System.out.println("====headCompletedDiscountAmount====="+
	 * headCompletedDiscountAmount);
	 * System.out.println("====headCancelledDiscountAmount====="+
	 * headCancelledDiscountAmount);
	 * System.out.println("====headCompletedTaxAmount====="+headCompletedTaxAmount);
	 * System.out.println("====headCancelledTaxAmount====="+headCancelledTaxAmount);
	 * System.out.println("====headCompletedInclusiveTaxAmount====="+
	 * headCompletedInclusiveTaxAmount);
	 * System.out.println("====headCancelledInclusiveTaxAmount====="+
	 * headCancelledInclusiveTaxAmount); System.out.
	 * println("***********************Values after adding***********************************************************"
	 * );
	 * //System.out.println("=========================Available qty================"
	 * +availableQtyNew); System.out.
	 * println("====Adding pickupCompletedQuantity + pickupCompletedRepricedQuantity====="
	 * +headCompletedQuantity.add(pickUpCompletedQuantity).add(
	 * pickUpCompletedRepricedQuantity));
	 * System.out.println("====Adding pickUpCancelledQuantity====="
	 * +headCancelledQuantity.add(pickUpCancelledQuantity)); System.out.
	 * println("====Adding pickUpCompletedAmount + pickUpRepricedAmount====="
	 * +headCompletedAmount.add(pickUpCompletedAmount).add(pickUpRepricedAmount));
	 * System.out.println("====Adding pickUpCancelledAmount====="
	 * +headCancelledAmount.add(pickUpCancelledAmount));
	 * System.out.println("====Adding pickUpPaidAmount====="+headPaidAmount.add(
	 * pickUpPaidAmount)); System.out.
	 * println("====Adding pickUpCompletedDiscountAmount + pickupRepricedDiscountAmount====="
	 * +headCompletedDiscountAmount.add(pickUpCompletedDiscountAmount).add(
	 * pickUpRepricedDiscountAmount));
	 * System.out.println("====Adding pickUpCancelledDiscountAmount====="
	 * +headCancelledDiscountAmount.add(pickUpCancelledDiscountAmount));
	 * 
	 * System.out.
	 * println("====Adding pickUpCompletedTaxAmount + pickupRepricedTaxAmount====="
	 * +headCompletedTaxAmount.add(pickUpCompletedTaxAmount).add(
	 * pickUpRepricedTaxAmount));
	 * System.out.println("====Adding pickUpCancelledTaxAmount====="
	 * +headCancelledTaxAmount.add(pickUpCancelledTaxAmount)); System.out.
	 * println("====Adding pickUpCompletedInclusiveTaxAmount + pickupRepricedIncTaxAmount====="
	 * +headCompletedInclusiveTaxAmount.add(pickUpCompletedInclusiveTaxAmount).add(
	 * pickUpRepricedIncTaxAmount));
	 * System.out.println("====Adding pickUpCancelledInclusiveTaxAmount====="
	 * +headCancelledInclusiveTaxAmount.add(pickUpCancelledInclusiveTaxAmount));
	 * 
	 * //Storing the line_item_no for validation purpose int
	 * valueLineItemNo=headLineItemNo.compareTo(pickUpLineItemNo);
	 * 
	 * //Storing the completed_quantity for validation purpose int
	 * valueCompletedQuantity=headCompletedQuantity.compareTo(
	 * pickUpCompletedQuantity);
	 * 
	 * //write the if loop for original and completed quantity
	 * if(valueLineItemNo==0) {
	 * 
	 * if(valueCompletedQuantity==0||valueCompletedQuantity==1) {
	 * omsOrposCustOrdItm.setCompletedQuantity(headCompletedQuantity.add(
	 * pickUpCompletedQuantity).add(pickUpCompletedRepricedQuantity)); }
	 * if(valueCompletedQuantity==-1) {
	 * log.info("You have entered invalid quantity"); }
	 * omsOrposCustOrdItm.setCancelledQuantity(headCancelledQuantity.add(
	 * pickUpCancelledQuantity));
	 * omsOrposCustOrdItm.setCompletedAmount(headCompletedAmount.add(
	 * pickUpCompletedAmount).add(pickUpRepricedAmount));
	 * omsOrposCustOrdItm.setCancelledAmount(headCancelledAmount.add(
	 * pickUpCancelledAmount));
	 * omsOrposCustOrdItm.setPaidAmount(headPaidAmount.add(pickUpPaidAmount));
	 * omsOrposCustOrdItm.setCompletedDiscountAmount(headCompletedDiscountAmount.add
	 * (pickUpCompletedDiscountAmount).add(pickUpRepricedDiscountAmount));
	 * omsOrposCustOrdItm.setCancelledDiscountAmount(headCancelledDiscountAmount.add
	 * (pickUpCancelledDiscountAmount));
	 * omsOrposCustOrdItm.setCompletedTaxAmount(headCompletedTaxAmount.add(
	 * pickUpCompletedTaxAmount).add(pickUpRepricedTaxAmount));
	 * omsOrposCustOrdItm.setCancelledTaxAmount(headCancelledTaxAmount.add(
	 * pickUpCancelledTaxAmount));
	 * omsOrposCustOrdItm.setCompletedInclusiveTaxAmount(
	 * headCompletedInclusiveTaxAmount.add(pickUpCompletedInclusiveTaxAmount).add(
	 * pickUpRepricedIncTaxAmount));
	 * omsOrposCustOrdItm.setCancelledInclusiveTaxAmount(
	 * headCancelledInclusiveTaxAmount.add(pickUpCancelledInclusiveTaxAmount));
	 * session.persistOmsOrposCustOrdItm(omsOrposCustOrdItm);
	 * 
	 * //Updating the OmsOrposDiscntLine table
	 * updateOmsOrposDiscntLine(omsOrposCustOrderId,custOrderPicVoSeq,
	 * headCapturedLineItemNo,headItemId,headLineItemNo);
	 * 
	 * //Updating the OmsOrposTaxLine table
	 * updateOmsOrposTaxLine(omsOrposCustOrderId,custOrderPicVoSeq,
	 * headCapturedLineItemNo,headItemId,headLineItemNo); } } }
	 * log.info("Updated OmsOrposCustOrdItm successfully"); }
	 */

	// Updating the OmsOrposDiscntLine table with the help of cust_order_id
	/*
	 * public void updateOmsOrposDiscntLine(BigDecimal
	 * omsOrposCustOrderId,BigDecimal custOrderPicVoSeq,BigDecimal
	 * capturedLineItemNo,String itemId,BigDecimal lineItemNo) throws SOAPException
	 * { OMSUtilSessionEJB session = OMSUtil.doLookup(); System.out.
	 * println("=====================Inside OmsOrposDiscntLine method========================================="
	 * ); log.info("OmsOrposDiscntLine method"); OmsOrposDiscntLine
	 * omsOrposDiscntLine=new OmsOrposDiscntLine(); DiscntLinePkVo
	 * discntLinePkVo=new DiscntLinePkVo();
	 * 
	 * //Initializing values for OmsOrposDiscntLine BigDecimal
	 * headCompletedDiscountAmount=null; BigDecimal
	 * headCancelledDiscountAmount=null;
	 * 
	 * //Initializing values for OmsOrposDiscntLinePkVo BigDecimal
	 * pickupCompletedDiscountAmount=null; BigDecimal
	 * pickupCancelledDiscountAmount=null; BigDecimal
	 * pickupRepricedDiscountAmount=null;
	 * 
	 * List<OmsOrposDiscntLine>
	 * omsOrposDiscntLineList=session.getOmsOrposDiscntLineFindColumns(
	 * omsOrposCustOrderId, capturedLineItemNo, itemId); for(OmsOrposDiscntLine
	 * omsOrposDiscntLineLoop: omsOrposDiscntLineList) {
	 * log.info("Inside OmsOrposDiscntLine Loop");
	 * headCompletedDiscountAmount=omsOrposDiscntLineLoop.getCompletedDiscountAmount
	 * ();
	 * headCancelledDiscountAmount=omsOrposDiscntLineLoop.getCancelledDiscountAmount
	 * (); if(headCompletedDiscountAmount==null||headCancelledDiscountAmount==null)
	 * { headCompletedDiscountAmount=new BigDecimal(0);
	 * headCancelledDiscountAmount=new BigDecimal(0); }
	 * List<OmsOrposDiscntLinePickup>
	 * omsOrposDiscntLinePickupList=session.getOmsOrposDiscntLinePickupFindColumns(
	 * omsOrposCustOrderId, custOrderPicVoSeq,lineItemNo);
	 * for(OmsOrposDiscntLinePickup
	 * omsOrposDiscntLinePickupLoop:omsOrposDiscntLinePickupList) {
	 * log.info("Inside OmsOrposDiscntLinePickup Loop");
	 * pickupCompletedDiscountAmount=omsOrposDiscntLinePickupLoop.
	 * getCompletedDiscountAmount();
	 * pickupCancelledDiscountAmount=omsOrposDiscntLinePickupLoop.
	 * getCancelledDiscountAmount();
	 * pickupRepricedDiscountAmount=omsOrposDiscntLinePickupLoop.
	 * getRepricedDiscountAmount();
	 * if(pickupCompletedDiscountAmount==null||pickupCancelledDiscountAmount==null||
	 * pickupRepricedDiscountAmount==null) { pickupCompletedDiscountAmount=new
	 * BigDecimal(0); pickupCancelledDiscountAmount=new BigDecimal(0);
	 * pickupRepricedDiscountAmount=new BigDecimal(0); } System.out.
	 * println("======Initial Values for the completed and cancelled discount amount======="
	 * ); System.out.println("======completedDiscountAmount============"+
	 * headCompletedDiscountAmount);
	 * System.out.println("======cancelledDiscountAmount============"+
	 * headCancelledDiscountAmount); System.out.
	 * println("***************Adding the values****************************************"
	 * ); System.out.println("=========adding the completedDiscountAmount======="
	 * +headCompletedDiscountAmount.add(pickupCompletedDiscountAmount));
	 * System.out.println("=========adding the cancelledDiscountAmount======="
	 * +headCancelledDiscountAmount.add(pickupCancelledDiscountAmount));
	 * 
	 * omsOrposDiscntLineLoop.setCompletedDiscountAmount(headCompletedDiscountAmount
	 * .add(pickupCompletedDiscountAmount).add(pickupRepricedDiscountAmount));
	 * omsOrposDiscntLineLoop.setCancelledDiscountAmount(headCancelledDiscountAmount
	 * .add(pickupCancelledDiscountAmount));
	 * session.persistOmsOrposDiscntLine(omsOrposDiscntLineLoop); } }
	 * log.info("Successfully updated the OmsOrposDiscntLine table");
	 * 
	 * }
	 */

	// Updating the OmsOrposTaxLine table with the help of cust_order_id
	/*
	 * public void updateOmsOrposTaxLine(BigDecimal omsOrposCustOrderId,BigDecimal
	 * custOrderPicVoSeq,BigDecimal capturedLineItemNo,String itemId,BigDecimal
	 * lineItemNo) throws SOAPException { OMSUtilSessionEJB session =
	 * OMSUtil.doLookup(); System.out.
	 * println("============================Inside updateTaxLine method========================"
	 * ); OmsOrposTaxLine omsOrposTaxLine=new OmsOrposTaxLine(); TaxLinePkVo
	 * taxLinePkVo=new TaxLinePkVo();
	 * 
	 * //Initializing values for OmsOrposTaxLine BigDecimal
	 * headCompletedTaxAmount=null; BigDecimal headCancelledTaxAmount=null;
	 * 
	 * //Initializing values for OmsOrposTaxLinePkVo BigDecimal
	 * pickupCompletedTaxAmount=null; BigDecimal pickupCancelledTaxAmount=null;
	 * BigDecimal pickupRepricedTaxAmount=null; List<OmsOrposTaxLine>
	 * omsOrposTaxLineList=session.getOmsOrposTaxLineFindColumns(
	 * omsOrposCustOrderId, capturedLineItemNo, itemId); for(OmsOrposTaxLine
	 * omsOrposTaxLineLoop:omsOrposTaxLineList) {
	 * log.info("Inside omsOrposTaxLine loop");
	 * headCompletedTaxAmount=omsOrposTaxLineLoop.getCompletedTaxAmount();
	 * headCancelledTaxAmount=omsOrposTaxLineLoop.getCancelledTaxAmount();
	 * if(headCompletedTaxAmount==null||headCancelledTaxAmount==null) {
	 * headCompletedTaxAmount=new BigDecimal(0); headCancelledTaxAmount=new
	 * BigDecimal(0); } List<OmsOrposTaxLinePickup>
	 * omsOrposTaxLinePickupList=session.getOmsOrposTaxLinePickupFindColumns(
	 * omsOrposCustOrderId, custOrderPicVoSeq,lineItemNo); for(OmsOrposTaxLinePickup
	 * omsOrposTaxLinePickupLoop:omsOrposTaxLinePickupList) {
	 * pickupCompletedTaxAmount=omsOrposTaxLinePickupLoop.getCompletedTaxAmount();
	 * pickupCancelledTaxAmount=omsOrposTaxLinePickupLoop.getCancelledTaxAmount();
	 * pickupRepricedTaxAmount=omsOrposTaxLinePickupLoop.getRepricedTaxAmount();
	 * 
	 * if(pickupCompletedTaxAmount==null||pickupCancelledTaxAmount==null||
	 * pickupRepricedTaxAmount==null) { pickupCompletedTaxAmount=new BigDecimal(0);
	 * pickupCancelledTaxAmount=new BigDecimal(0); pickupRepricedTaxAmount=new
	 * BigDecimal(0); } System.out.
	 * println("======Initial Values for the completed and cancelled tax amount======="
	 * ); System.out.println("======completedTaxAmount============"+
	 * headCompletedTaxAmount);
	 * System.out.println("======cancelledTaxAmount============"+
	 * headCancelledTaxAmount);
	 * System.out.println("======RepricedTaxAmount============="+
	 * pickupRepricedTaxAmount); System.out.
	 * println("***************Adding the values****************************************"
	 * ); System.out.println("=========adding the completedtaxAmount======="
	 * +headCompletedTaxAmount.add(pickupCompletedTaxAmount));
	 * System.out.println("=========adding the cancelledTaxAmount======="
	 * +headCancelledTaxAmount.add(pickupCancelledTaxAmount));
	 * 
	 * omsOrposTaxLineLoop.setCompletedTaxAmount(headCompletedTaxAmount.add(
	 * pickupCompletedTaxAmount).add(pickupRepricedTaxAmount));
	 * omsOrposTaxLineLoop.setCancelledTaxAmount(headCancelledTaxAmount.add(
	 * pickupCancelledTaxAmount));
	 * session.mergeOmsOrposTaxLine(omsOrposTaxLineLoop); } }
	 * log.info("Successfully updated the OmsOrposTaxLine table"); }
	 */

	public CustOrdItmPkVo filterForDummyCancellation(CustOrdItmPkVo custOrdItmPkVo, String customerOrderNo,
			CustOrderPicVo custOrderPicVo) throws SOAPException, Exception {
		log.info("inside filter for DummyCancellation " + customerOrderNo);
		// Actual cancelled qty
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		RMARefundData rmaRefundData1 = null;
		DummyCancelData dummyCancelData1 = null;
		String indicator = null;
		boolean flagforRMAandCancel = false;
		BigDecimal omsCancelId1 = new BigDecimal(0);
		BigDecimal actual_Qty = new BigDecimal(0);
		BigDecimal dummy_Qty = new BigDecimal(0);
		BigDecimal requsted_CancelQty = custOrdItmPkVo.getCancelledQuantity();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();

		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		BigDecimal lineNo = new BigDecimal(custOrdItmPkVo.getLineItemNo());
		log.info("omsCustOrdNumber " + omsCustOrdHead.getOmsCustOrdNo());
		log.info("lineNo " + lineNo);

		List<OmsCustOrdItem> omsCustOrdItemList = session
				.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), lineNo);
		log.info("omsCustOrdItemList size " + omsCustOrdItemList.size());
		dummy_Qty = BigDecimal.ZERO;

		log.info("dummy Cancelled Qty " + dummy_Qty);

		// RMA
		Map<BigDecimal, BigDecimal> rmaItemCancelMap = oMSUtilCommons
				.getCancelRequestQtyForALineNoFromRMA(customerOrderNo);
		log.info("<-----------------------------Beginning RMA-------------------------------->");

		List<OmsRmaReq> omsRmaReqList = session.getOmsRmaReqFindByOmscustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
		log.info("omsRmaReqList size" + omsRmaReqList.size());
		BigDecimal rmaId1 = BigDecimal.ZERO;
		for (OmsRmaReq omsRmaReq1 : omsRmaReqList) {
			log.info("inside omsRmaReq1 loop");
			rmaId1 = omsRmaReq1.getRmaId();
			log.info("rmaId1 " + rmaId1);
			indicator = omsRmaReq1.getRefundCompltInd();
			log.info("Refund CompltInd indicator " + indicator);
			log.info("Return Status " + omsRmaReq1.getReturnStatus());
			log.info("omsRmaReq1.getSubCustOrderNo() " + omsRmaReq1.getSubCustOrderNo());
			log.info("inside omsRmaReq1.getSubCustOrderNo().equals(subOrderNo) if condition");
			List<OmsRmaReqItem> omsRmaReqItemList = session.getOmsRmaReqItemFindByRmaId(rmaId1);
			log.info("omsRmaReqItemList size " + omsRmaReqItemList.size());
			for (OmsRmaReqItem omsRmaReqItem1 : omsRmaReqItemList) {
				log.info("inside omsRmaReqItem1 loop");
				log.info("omsRmaReqItem1.getLineNo() " + omsRmaReqItem1.getLineNo());
				log.info("itemList.getLineItemNo() " + custOrdItmPkVo.getLineItemNo());
				if (omsRmaReqItem1.getLineNo().intValue() == custOrdItmPkVo.getLineItemNo()) {

					log.info("<--------------------checking indicator is N or Y------------>");
					if (omsRmaReq1.getStatus().equals("S")) {

						if (indicator.equals("N") && omsRmaReq1.getReturnStatus().equals("Y")) {
							log.info("inisde if condition");
							dummy_Qty = dummy_Qty.add(omsRmaReqItem1.getOrigRmaQty());
							log.info("======================Dummy_QTY in RMA=====================" + dummy_Qty);
							log.info("Indicator is N,setting it to Y");
							log.info("<---- checking line no is present in the map or not rmaItemCancelMap ---->");
							Boolean lineCheckNo = rmaItemCancelMap
									.containsKey(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
							log.info("<---------------------- line check No---->" + lineCheckNo + "<--->");
							Boolean resultCheck = null;
							status = "S";
							if (Boolean.TRUE == lineCheckNo) {
								BigDecimal fromTablermaQtyId = rmaItemCancelMap
										.get(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
								if (fromTablermaQtyId.compareTo(custOrdItmPkVo.getCancelledQuantity()) == 0) {
									resultCheck = Boolean.TRUE;
								} else {
									resultCheck = Boolean.FALSE;
								}
							}
							log.info(" Quantity Result from  Query adnd input qunatity are same or not " + resultCheck);
							if (Boolean.TRUE != resultCheck) {
								status = "F";
								error = "ERROR_201";
								break;
							}

							if ("S".equals(status)) {
								rmaCancelList.add(rmaId1);
								rmaRefundData1 = new RMARefundData();
								rmaRefundData1.setLineNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
								rmaRefundData1.setRmaId(rmaId1);
								log.info("<----custOrdItmPkVo.getCancelledQuantity()---->"
										+ custOrdItmPkVo.getCancelledQuantity());
								rmaRefundData1.setQuantity(omsRmaReqItem1.getOrigRmaQty().negate());
								rmaRefundData1.setEventId("RMR");
								rmaRefundData.add(rmaRefundData1);
								flagforRMAandCancel = true;
								globalRmaFalg = true;
							}
						} else {

						}
					}

					log.info("<---- Status ----------->" + status);
					log.info("<-------------- Error Code " + error);
				}
			}

			session.mergeOmsRmaReq(omsRmaReq1);
		}

		Map<BigDecimal, BigDecimal> OmsCancelLineItemMap = oMSUtilCommons
				.getCancelRequestQtyForALineNo(customerOrderNo);
		Map<BigDecimal, BigDecimal> OmsCancelInputLineCheck = null;
		if (flagforRMAandCancel == false) {
			List<OmsCoCancelHead> omsCoCancelHeadList = session.getOmsCoCancelHeadFindByCustOrdNo(customerOrderNo);
			log.info("omsCoCancelHeadList size" + omsCoCancelHeadList.size());
			for (OmsCoCancelHead omsCoCancelHead1 : omsCoCancelHeadList) {
				log.info("inside omsCoCancelHead1 loop");
				omsCancelId1 = omsCoCancelHead1.getOmsCancelId();
				log.info("omsCancelId1 " + omsCancelId1);
				indicator = omsCoCancelHead1.getRefundCompltInd();
				log.info("indicator " + indicator);
				List<OmsCoCancelItem> omsCoCancelItemList = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId1);
				log.info("omsCoCancelItem1 size " + omsCoCancelItemList.size());
				for (OmsCoCancelItem omsCoCancelItem1 : omsCoCancelItemList) {
					log.info("inside omsCoCancelItem1 loop");
					log.info("omsCoCancelItem1.getLineNo() " + omsCoCancelItem1.getLineNo());
					log.info("itemList.getLineItemNo() " + custOrdItmPkVo.getLineItemNo());
					if (omsCoCancelItem1.getLineNo().intValue() == custOrdItmPkVo.getLineItemNo()
							&& omsCoCancelItem1.getCancelConfQty().intValue() > 0) {
						log.info("checking indicator is N or Y");
						if (indicator.equals("N")) {
							log.info(" checking whether line no present in OmsCancelLineItemMap "
									+ custOrdItmPkVo.getLineItemNo());
							Boolean lineNoCheck = OmsCancelLineItemMap
									.containsKey(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
							log.info("Result of lineNo check is  " + lineNoCheck);
							status = "S";
							if (Boolean.TRUE == lineNoCheck) {
								Boolean qtyresultCheck = null;
								BigDecimal fromTableCancelQty = OmsCancelLineItemMap
										.get(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
								log.info("<------------------ fromTableCancelQty ----> " + fromTableCancelQty);
								log.info("custOrdItmPkVo.getCancelledQuantity().negate()"
										+ custOrdItmPkVo.getCancelledQuantity().negate());

								if (fromTableCancelQty.compareTo(custOrdItmPkVo.getCancelledQuantity()) == 0) {
									qtyresultCheck = Boolean.TRUE;
								} else {
									qtyresultCheck = Boolean.FALSE;
								}
								log.info(" Quantity Result from  Query adnd input qunatity are same or not "
										+ qtyresultCheck);

								if (qtyresultCheck != Boolean.TRUE) {
									status = "F";
									error = "ERROR_201";
									break;
								}

							}
							if ("S".equals(status)) {
								log.info("+++omsCancelId " + omsCancelId1);
								dummyCancelData1 = new DummyCancelData();
								dummy_Qty = dummy_Qty.add(omsCoCancelItem1.getCancelReqQty());
								log.info("+++omsCoCancelItem1.getOmsCancelId()-->" + omsCoCancelItem1.getOmsCancelId());
								dummycancelidList.add(omsCancelId1);
								dummyCancelData1.setOmsCancelId(omsCancelId1);
								dummyCancelData1.setLineNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
								dummyCancelData1.setEventId("CAR");
								dummyCancelData1.setQuantity(omsCoCancelItem1.getCancelConfQty().negate());
								dummyCancelData.add(dummyCancelData1);
								int count = 0;
								count = sumofOmsCustCancelTender(omsCancelId1, omsCustOrdHead.getOmsCustOrdNo());
								if (count == 0) {
									log.info("Inside if block of count for canceltender" + count
											+ "****************************");
									persistOmsCustOrdCancelTenderMethod(omsCancelId1, omsCustOrdHead.getOmsCustOrdNo(),
											custOrderPicVo);
								}

							}
						} // end of N equal
					} // end of if
				} // end of for
				log.info("calling OmsCustCancelTender");

			}
		}
		log.info("dummyCancelData Size is " + dummyCancelData.size());
		log.info("Requested Cancelled Qty in OmsOrposCustOrdpickUpItem " + requsted_CancelQty);
		log.info("Requested dummy Qty in OmsOrposCustOrdpickUpItem " + dummy_Qty);

		actual_Qty = requsted_CancelQty.subtract(dummy_Qty);
		log.info(" actual_Qty " + actual_Qty);
		custOrdItmPkVo.setCancelledQuantity(actual_Qty);

		return custOrdItmPkVo;
	}

	public void persistOmsCustOrdCancelTenderMethod(BigDecimal omsCancelId, BigDecimal omsCustOrdNumber,
			CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("inside OmsCustCancelTender method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustCancelTender omsCustCancelTender = new OmsCustCancelTender();
		TenderTypeClassifier tenderTypeClassifier = new TenderTypeClassifier();
		OmsCoCancelHead omsCoCancelHead = session.getOmsCoCancelHeadFindCustOrderNoByomsCancelId(omsCancelId);
		BigDecimal tenderTypeId = null;
		BigDecimal tenderSeqNo = null;
		String tgroup = null;
		int i = 0;
		if (custOrderPicVo.getPaymentColDesc() != null) {
			PaymentColDesc paymentColDesc = custOrderPicVo.getPaymentColDesc();
			List<PaymentDesc> paymentDesc = paymentColDesc.getPaymentDesc();
			log.info("paymentDesc.size() " + paymentDesc.size());
			for (PaymentDesc paymentDescLoop : paymentDesc) {

				if (i == 0) {
					log.info("I is 0");
					log.info("omsCancelId " + omsCoCancelHead.getOmsCancelId());
					omsCustCancelTender.setOmsCancelId(omsCancelId);
					log.info("omsCustOrdNumber " + omsCustOrdNumber);
					omsCustCancelTender.setOmsCustOrdNo(omsCustOrdNumber);
					try {
						tenderSeqNo = session.getOmsCustOrdTenderCancelFindByOmsCancelIdandomsCustOrdNumberByMaxSeqNo(
								omsCancelId, omsCustOrdNumber);
					} catch (Exception e) {
						log.info("no records found in OmsCustOrdTenderCancel omsCancelId " + omsCancelId + "and"
								+ "omsCustOrdNumber" + omsCustOrdNumber);
					}
					log.info("tenderSeqNo " + tenderSeqNo);
					log.info("TenderSeqNo is set");
					if (tenderSeqNo != null) {
						omsCustCancelTender.setTenderSeqNo(tenderSeqNo.add(new BigDecimal(1)));
						log.info("TenderSeqNo " + tenderSeqNo.add(new BigDecimal(1)));
					} else {
						omsCustCancelTender.setTenderSeqNo(new BigDecimal(0));
						log.info("TenderSeqNo " + 0);
					}

					log.info("getting tender type id");
					log.info("<----paymentDescLoop------>" + paymentDescLoop.getSeqNo() + "<-------->"
							+ paymentDescLoop.getPaymentType() + "<-------->");
					tenderTypeId = tenderTypeClassifier.getTenderTypeIdBasedOnParameterValueForPickup(paymentDescLoop,
							custOrderPicVo);
					log.info("tender type id is=============" + tenderTypeId);
					omsCustCancelTender.setTenderTypeId(tenderTypeId);
					log.info("tender type id is set");
					tgroup = session.getPosTenderTypeHeadFindByTenderTypeId(tenderTypeId);
					log.info("tgroup " + tgroup);
					omsCustCancelTender.setTenderTypeGroup(tgroup);
					log.info("Tender type group");
					log.info("Refund amount from OmsCoCancelHead for OMSCoCancelId " + omsCancelId + "amount is "
							+ omsCoCancelHead.getRefundAmount());
					omsCustCancelTender.setTenderAmt(omsCoCancelHead.getRefundAmount());
					if (paymentDescLoop.getCreditDebitTender() != null) {
						omsCustCancelTender.setCcNo(paymentDescLoop.getCreditDebitTender().getMaskedAccountNumber());
						log.info("CC no is set");
						omsCustCancelTender.setCcAuthNo(paymentDescLoop.getCreditDebitTender().getAuthorizationCode());
						log.info("CC auth no is set");
						omsCustCancelTender.setCcAuthSrc(null);
						log.info("CC auth src is set");
						omsCustCancelTender.setCcCardholderVerf(null);
						log.info("CCCardHolder is set");
						omsCustCancelTender.setCcExpDate(null);
						log.info("CCexpDate is set");
						omsCustCancelTender.setCcEntryMode(null);
						log.info("Cc Entry mode is set");
						omsCustCancelTender.setCcTermId(null);
						log.info("Cc Term id");
						omsCustCancelTender.setCcSpecCond(null);
						log.info("Cc Spec Cond is set");
						log.info("==============================");
						log.info("TenderRefId :paymentDescLoop.getCreditDebitTender().getCardToken() "
								+ paymentDescLoop.getCreditDebitTender().getCardToken());
						omsCustCancelTender.setTenderRefId(paymentDescLoop.getCreditDebitTender().getCardToken());
						log.info("Tender Ref Id");
					}
					omsCustCancelTender.setCreateDatetime(new Timestamp(new Date().getTime()));
					log.info("CreateDatetime is set");
					session.persistOmsCustCancelTender(omsCustCancelTender);
					log.info("Successfully persisted into omsCustCancelTender table");
				}
				i++;
			} // end of for-loop

		}

	}

	public void persistOmsRtlogPublishLog(BigDecimal omsCancelId, BigDecimal omsCustOrderNo,
			List<OmsCoCancelItem> omsCoCancelItemList, BigDecimal locid) throws SOAPException {
		log.info("inside persistOmsRtlogPublishLog method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
		try {
			for (OmsCoCancelItem omsCoCancelItem : omsCoCancelItemList) {
				log.info("OmsCoCancelItem.getItem() " + omsCoCancelItem.getItem());
				omsRtlogPublishLog.setItem(omsCoCancelItem.getItem());
				log.info("omsCustOrderNo " + omsCustOrderNo);
				omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrderNo);
				// omsRtlogPublishLog.setFulfillOrderNo(new BigDecimal(fulilmentOrdNo));//not
				// needed
				log.info("PublishedInd N");
				omsRtlogPublishLog.setPublishedInd("N"); // need to confirm its value
				omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
				log.info("PublishLog ORC");
				omsRtlogPublishLog.setTranType("ORC");
				log.info("omsCoCancelItem.getLineNo().intValue() " + omsCoCancelItem.getLineNo().intValue());
				omsRtlogPublishLog.setLineNo(new BigDecimal(omsCoCancelItem.getLineNo().intValue()));
				log.info("omsCoCancelItem.getCancelReqQty() " + omsCoCancelItem.getCancelReqQty());
				omsRtlogPublishLog.setQty(omsCoCancelItem.getCancelReqQty()); // need to confirm
				log.info("omsCancelId " + omsCancelId);
				omsRtlogPublishLog.setOmsCancelId(omsCancelId);
				log.info("locid " + locid);
				omsRtlogPublishLog.setLocation(locid);
				log.info("persisting session");
				session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
				log.info("Successfully persisted into OmsRtlogPublishLog");
			}
		} catch (Exception e) {
			String errString = OMSUtilCommons.formErrorDescription(OMSConstants.ERR_TABLE_INSERT, "1",
					new String[] { "oms_rtlog_publish_log" });
			log.error("+++++++++++++++++++++" + errString);
			// throw new SOAPException(errString);
		}
	}

	public void persistOmsOrposRefundTenderMethod(String customerOrderNo, CustOrderPicVo custOrderPicVo,
			BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("inside persistOmsOrposRefundTenderMethod");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposRefundTender omsOrposRefundTender = new OmsOrposRefundTender();
		TenderTypeClassifier tenderTypeClassifier = new TenderTypeClassifier();
		log.info("omsCustOrdNo " + omsCustOrdNo);
		if (custOrderPicVo.getPaymentColDesc() != null) {
			log.info("+++++++++inside PaymentColDesc not null condition++++++");
			PaymentColDesc paymentColDesc = custOrderPicVo.getPaymentColDesc();
			log.info("OmsOrposRefundCustOrdTender paymentColDesc " + paymentColDesc.getCollectionSize());
			List<PaymentDesc> paymentDesc = paymentColDesc.getPaymentDesc();
			log.info("OmsOrposRefundCustOrdTender paymentDesc " + paymentDesc.size());
			BigDecimal tenderTypeId = null;
			BigDecimal paymentSeqNo = null;
			BigDecimal unitRetail = null;
			BigDecimal tenderAmt = null;
			BigDecimal tenderSeqNo = null;
			String tgroup = null;

			for (PaymentDesc paymentDescLoop : paymentDesc) {
				log.info("inside paymentDescLoop");
				tenderTypeId = tenderTypeClassifier.getTenderTypeIdBasedOnParameterValueForPickup(paymentDescLoop,
						custOrderPicVo);
				log.info("tender type id for OmsOrposRefundCustOrdTender" + tenderTypeId);
				try {
					tenderSeqNo = session.getOmsOrposRefundCustOrdTenderFindMaxTenderSeqNo(omsCustOrdNo);
					log.info("tenderSeqNo " + tenderSeqNo);
				} catch (Exception e) {
					log.info("no record exist in OmsOrposRefundCustOrdTender for omsCustOrdNo " + omsCustOrdNo);
				}
				omsOrposRefundTender.setOmsCustOrdNo(omsCustOrdNo);
				log.info("OmsCustOrderNo is set");
				if (tenderSeqNo != null) {
					omsOrposRefundTender.setTenderSeqNo(tenderSeqNo.add(new BigDecimal(1)));
					log.info("TenderSeqNo is set " + tenderSeqNo.add(new BigDecimal(1)));
				} else {
					omsOrposRefundTender.setTenderSeqNo(new BigDecimal(0));
					log.info("TenderSeqNo is set " + 0);
				}

				omsOrposRefundTender.setTenderTypeId(tenderTypeId);

				tgroup = session.getPosTenderTypeHeadFindByTenderTypeId(tenderTypeId);

				omsOrposRefundTender.setTenderTypeGroup(tgroup);

				omsOrposRefundTender.setTenderAmt(paymentDescLoop.getAmount());

				log.info("The Payment Type entered is: ---" + paymentDescLoop.getPaymentType());
				if (paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("CREDIT"))
						|| paymentDescLoop.getPaymentType().equals(PaymentType.valueOf("DEBIT"))) {
					if (paymentDescLoop.getCreditDebitTender() != null) {
						omsOrposRefundTender.setCcNo(paymentDescLoop.getCreditDebitTender().getMaskedAccountNumber());
						log.info("CC no is set");
						omsOrposRefundTender.setCcAuthNo(paymentDescLoop.getCreditDebitTender().getAuthorizationCode());
						log.info("CC auth no is set");
						omsOrposRefundTender.setCcAuthSrc(null);
						log.info("CC auth src is set");
						omsOrposRefundTender.setCcCardholderVerf(null);
						log.info("CCCardHolder is set");
						omsOrposRefundTender.setCcExpDate(null);
						log.info("CCexpDate is set");
						omsOrposRefundTender.setCcEntryMode(null);
						log.info("Cc Entry mode is set");
						omsOrposRefundTender.setCcTermId(null);
						log.info("Cc Term id");
						omsOrposRefundTender.setCcSpecCond(null);
						log.info("Cc Spec Cond is set");
						omsOrposRefundTender.setTenderRefId(paymentDescLoop.getCreditDebitTender().getCardToken());
						log.info("Tender Ref Id");
					}
				}

				omsOrposRefundTender.setCreateDatetime(new Timestamp(new Date().getTime()));
				log.info("CreateDatetime is set");
				unitRetail = session.getOmsCustOrdItemSumUnitRetail(omsCustOrdNo);
				log.info("Unit retail is set");

				tenderAmt = session.getOmsCustOrdTenderSumOfTenderAmt(omsCustOrdNo);
				if (tenderAmt == null) {
					tenderAmt = BigDecimal.ZERO;
				}
				if (unitRetail == null) {
					unitRetail = BigDecimal.ZERO;
				}
				log.info("=============tenderAmt=============" + tenderAmt);
				log.info("tenderAmt from query is set");
				BigDecimal valueDiff = new BigDecimal(0);
				if (tenderAmt != null) {
					valueDiff = unitRetail.subtract(tenderAmt);
				} else {
					valueDiff = unitRetail;
				}
				log.info("==========valueDiff========" + valueDiff);
				if (valueDiff.intValue() > 0) {
					omsOrposRefundTender.setPaymentStatusInd("P");
					log.info("Payment Status Ind is set to P");
				} else {
					omsOrposRefundTender.setPaymentStatusInd("S");
					log.info("Payment Status Ind is set to S");
				}
				log.info("RtlogPubInd " + "N");
				omsOrposRefundTender.setRtlogPubInd("N");
				session.persistOmsOrposRefundTender(omsOrposRefundTender);
			}
		}
	}

	public PickupCustomerOrderItemDetailsRef createResponseStatusFailed(CustOrderPicVo custOrderPicVo, String status)
			throws SOAPException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException {
		log.info("Inside createResponse method for pickup");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(customerOrderNo);
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = new PickupCustomerOrderItemDetailsRef();
		// String status=checkStatusForPickupOrder(custOrderPicVo);
		CustOrderRef custOrderRef = new CustOrderRef();
		log.info("CustOrderRef is intialized");
		OmsCustOrdHead omsCustOrderHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("After fetching the values from OmsCustOrdHead table");
		custOrderRef.setOrderId(customerOrderNo);
		log.info("The order is set---->" + customerOrderNo);
		pickupCustomerOrderItemDetailsRef.setCustOrderRef(custOrderRef);
		log.info("The CustOrderRef is set");
		pickupCustomerOrderItemDetailsRef.setPickupStatus(status);
		log.info("Status is set-----" + status);
		return pickupCustomerOrderItemDetailsRef;
	}

	public PickupCustomerOrderItemDetailsRef createResponseForPickUp(CustOrderPicVo custOrderPicVo, String status)
			throws SOAPException {
		log.info("transactionNumber " + transactionNumber + "Inside createResponse method for pickup");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(customerOrderNo);
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = new PickupCustomerOrderItemDetailsRef();
		// String status=checkStatusForPickupOrder(custOrderPicVo);
		CustOrderRef custOrderRef = new CustOrderRef();
		log.info("transactionNumber " + transactionNumber + "CustOrderRef is intialized");
		OmsCustOrdHead omsCustOrderHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("transactionNumber " + transactionNumber + "After fetching the values from OmsCustOrdHead table");
		custOrderRef.setOrderId(customerOrderNo);
		log.info("transactionNumber " + transactionNumber + "The order is set---->" + customerOrderNo);
		pickupCustomerOrderItemDetailsRef.setCustOrderRef(custOrderRef);
		log.info("transactionNumber " + transactionNumber + "The CustOrderRef is set");
		pickupCustomerOrderItemDetailsRef.setPickupStatus(status);
		log.info("transactionNumber " + transactionNumber + "Status is set-----" + status);
		return pickupCustomerOrderItemDetailsRef;
	}

	public PickupCustomerOrderItemDetailsRef createResponseForCancellation(CustOrderPicVo custOrderPicVo)
			throws SOAPException {
		log.info("transactionNumber " + transactionNumber + "Inside createResponse method for cancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(customerOrderNo);
		log.info("transactionNumber " + transactionNumber + "---OmsCustOrdNo---" + omsCustOrdNo);
		// String status=checkStatusForPickupOrder(custOrderPicVo);
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = new PickupCustomerOrderItemDetailsRef();
		log.info("transactionNumber " + transactionNumber + "After pickup object initialization");
		CustOrderRef custOrderRef = new CustOrderRef();
		log.info("transactionNumber " + transactionNumber + "CustOrdRef initialized");
		OmsCustOrdHead omsCustOrderHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("transactionNumber " + transactionNumber + "After getting the values from oms_cust_ord_head");
		log.info("transactionNumber " + transactionNumber + "Setting the response started");
		// pickupCustomerOrderItemsResponse.setCustOrderRef(value);
		log.info("transactionNumber " + transactionNumber + "Customer order id----" + customerOrderNo);
		// custOrderRef.setOrderId(custOrderPicVo.getCustomerOrderId());
		try {
			custOrderRef.setOrderId(customerOrderNo);
		} catch (Exception e) {
			log.info(e);
		}
		log.info("transactionNumber " + transactionNumber + "Order ID is set----" + customerOrderNo);
		pickupCustomerOrderItemDetailsRef.setCustOrderRef(custOrderRef);
		log.info("transactionNumber " + transactionNumber + "CustOrderRef response is set");
		pickupCustomerOrderItemDetailsRef.setPickupStatus("S");
		pickupCustomerOrderItemDetailsRef.setCustOrdCancelItemDetailsCol(
				createResponseForCustOrderCancelItemsDetails(customerOrderNo, custOrderPicVo));
		return pickupCustomerOrderItemDetailsRef;
	}

	public CustOrdCancelItemDetailsCol createResponseForCustOrderCancelItemsDetails(String customerOrderNo,
			CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("transactionNumber " + transactionNumber
				+ "Inside createResponse method for createResponseForCustOrderCancelItemsDetails");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrdCancelItemDetailsCol custOrdCancelItemDetailsCol = new CustOrdCancelItemDetailsCol();
		BigDecimal omsCancelId;
		BigDecimal finalCancelId = new BigDecimal(0);
		List<OmsCoCancelHead> omsCoCancelHead = session.getOmsCoCancelHeadFindByCustOrdNo(customerOrderNo);
		for (OmsCoCancelHead omsCoCancelHeadLoop : omsCoCancelHead) {
			omsCancelId = omsCoCancelHeadLoop.getOmsCancelId();
			if (omsCancelId.intValue() > finalCancelId.intValue()) {
				finalCancelId = omsCancelId;
			}
		}
		List<OmsCoCancelItem> omsCoCancelItem = session.getOmsCoCancelItemFindByOmsCancelId(finalCancelId);
		log.info("transactionNumber " + transactionNumber + "After getting the values from oms_co_cancel_item");
		List<CustOrdCancelItemDetails> custOrdCancelItemDetailsList = custOrdCancelItemDetailsCol
				.getCustOrdCancelItemDetails();
		log.info("transactionNumber " + transactionNumber + "List is intialized");
		List<CustOrdItmPkVo> custOrdItmPkVo = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
		for (OmsCoCancelItem omsCoCancelItemLoop : omsCoCancelItem) {
			for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
				if (custOrdItmPkVoLoop.getLineItemNo() == omsCoCancelItemLoop.getLineNo().intValue()) {
					log.info("Inside omsCustOrdItemLoop for response");
					CustOrdCancelItemDetails custOrdCancelItemDetails = new CustOrdCancelItemDetails();
					log.info("Inside loop for OmsCustOrdItem");
					custOrdCancelItemDetails.setLineNo(omsCoCancelItemLoop.getLineNo());
					log.info("Line No is set -----" + omsCoCancelItemLoop.getLineNo());
					custOrdCancelItemDetails.setItem(omsCoCancelItemLoop.getItem());
					log.info("Item is set----" + omsCoCancelItemLoop.getItem());
					custOrdCancelItemDetails.setRequestedCancelQty(custOrdItmPkVoLoop.getCancelledQuantity());
					custOrdCancelItemDetails.setCancelledQty(omsCoCancelItemLoop.getCancelReqQty());
					log.info("Cancelled Qty is set---" + omsCoCancelItemLoop.getCancelReqQty());
					log.info("custOrdItmPkVoLoop.getCancelledQuantity() " + custOrdItmPkVoLoop.getCancelledQuantity());
					if (omsCoCancelItemLoop.getCancelReqQty().intValue() == omsCoCancelItemLoop.getCancelConfQty()
							.intValue()) {
						custOrdCancelItemDetails.setStatus("SUCCESS");
						log.info("Status is set----" + custOrdCancelItemDetails.getStatus());
						custOrdCancelItemDetails.setMessageCode("Success");
						custOrdCancelItemDetails.setMessageDesc("Item Cancelled Successfully.");
					} else {
						log.info("In the else block");
						custOrdCancelItemDetails.setStatus("FAILED");
						log.info("Status is set----" + custOrdCancelItemDetails.getStatus());
						custOrdCancelItemDetails.setMessageCode("Failure");
						custOrdCancelItemDetails.setMessageDesc("This item couldn't be cancelled.");

					}
					log.info("transactionNumber " + transactionNumber + "end of createResponse method");
					log.info("transactionNumber " + transactionNumber + "Successfully created the response !");
					custOrdCancelItemDetailsList.add(custOrdCancelItemDetails);
					log.info("transactionNumber " + transactionNumber + "Response completed !");
				} // end of if condtion input line_no = db line no
			} // end of CustOrdItmPkVo
		} // end of OmsCoCancelItem loop

		return custOrdCancelItemDetailsCol;
	}

	public PickupCustomerOrderItemDetailsRef createResponseForDefaultDummyandRMACancellation(
			CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("transactionNumber " + transactionNumber
				+ "Inside createResponse method for DefaultDummyandRMA cancellation");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustOrdNo;
		omsCustOrdNo = getCustomerOrderAlongWithSubOrderNo(customerOrderNo);
		log.info("---OmsCustOrdNo---" + omsCustOrdNo);
		// String status=checkStatusForPickupOrder(custOrderPicVo);
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = new PickupCustomerOrderItemDetailsRef();
		log.info("After pickup object initialization");
		CustOrderRef custOrderRef = new CustOrderRef();
		log.info("CustOrdRef initialized");
		OmsCustOrdHead omsCustOrderHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("After getting the values from oms_cust_ord_head");
		log.info("Setting the response started");
		// pickupCustomerOrderItemsResponse.setCustOrderRef(value);
		log.info("Customer order id----" + customerOrderNo);
		try {
			custOrderRef.setOrderId(customerOrderNo);
		} catch (Exception e) {
			log.info(e);
		}
		log.info("transactionNumber " + transactionNumber + "Order ID is set----" + customerOrderNo);
		pickupCustomerOrderItemDetailsRef.setCustOrderRef(custOrderRef);
		log.info("transactionNumber " + transactionNumber + "CustOrderRef response is set");
		pickupCustomerOrderItemDetailsRef.setPickupStatus("S");
		log.info("transactionNumber " + transactionNumber + "Status is set-----" + status);
		pickupCustomerOrderItemDetailsRef.setCustOrdCancelItemDetailsCol(
				createResponseForDefaultDummyandRMACustOrderCancelItemsDetails(omsCustOrdNo, customerOrderNo,
						custOrderPicVo));
		return pickupCustomerOrderItemDetailsRef;

	}

	public CustOrdCancelItemDetailsCol createResponseForDefaultDummyandRMACustOrderCancelItemsDetails(
			BigDecimal omsCustOrdNo, String customerOrderNo, CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("Inside createResponse method for createResponseForDefaultDummyandRMACustOrderCancelItemsDetails");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		boolean flag = false;
		CustOrdCancelItemDetailsCol custOrdCancelItemDetailsCol = new CustOrdCancelItemDetailsCol();
		BigDecimal rmaId = new BigDecimal(0);
		BigDecimal omscancelId = BigDecimal.ZERO;
		// List<OmsRmaReq>
		// omsRmaReq=session.getOmsRmaReqFindByOmscustOrdNo(omsCustOrdNo);
		if (rmaCancelList.size() > 0) {
			for (BigDecimal rma_Id : rmaCancelList) {
				flag = true;
				log.info("rmaId " + rmaId);
				log.info("rma_Id " + rma_Id);
				if (rmaId.intValue() == rma_Id.intValue()) {
					break;
				}
				rmaId = rma_Id;
				List<OmsRmaReqItem> omsRmaReqItem = session.getOmsRmaReqItemFindByRmaId(rmaId);
				log.info("After getting the values from OmsRmaReqItem");
				List<CustOrdCancelItemDetails> custOrdCancelItemDetailsList = custOrdCancelItemDetailsCol
						.getCustOrdCancelItemDetails();
				log.info("List is intialized");
				List<CustOrdItmPkVo> custOrdItmPkVo = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
				for (OmsRmaReqItem OmsRmaReqItemLoop : omsRmaReqItem) {
					for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
						if (custOrdItmPkVoLoop.getLineItemNo() == OmsRmaReqItemLoop.getLineNo().intValue()) {
							log.info("Inside omsCustOrdItemLoop for response");
							CustOrdCancelItemDetails custOrdCancelItemDetails = new CustOrdCancelItemDetails();
							log.info("Inside loop for OmsCustOrdItem");
							custOrdCancelItemDetails.setLineNo(OmsRmaReqItemLoop.getLineNo());
							log.info("Line No is set -----" + OmsRmaReqItemLoop.getLineNo());
							custOrdCancelItemDetails.setStatus("S");
							log.info("Status is set----" + custOrdCancelItemDetails.getStatus());
							custOrdCancelItemDetails.setMessageCode("Success");
							custOrdCancelItemDetails.setMessageDesc("Item Cancelled Successfully.");
							log.info("end of createResponse method");
							log.info("Successfully created the response !");
							custOrdCancelItemDetailsList.add(custOrdCancelItemDetails);
							log.info("Response completed !");

						}
					} // end of if condtion input line_no = db line no
				} // end of CustOrdItmPkVo
			} // end of OmsRmaReqItem loop

		}

		if (flag == false) {
			if (dummycancelidList.size() > 0) {
				for (BigDecimal oms_cancel_id : dummycancelidList) {
					log.info("omscancelId " + omscancelId);
					log.info("oms_cancel_id " + oms_cancel_id);
					if (omscancelId.intValue() == oms_cancel_id.intValue()) {
						break;
					}
					omscancelId = oms_cancel_id;
					List<OmsCoCancelItem> omsCoCancelItem = session.getOmsCoCancelItemFindByOmsCancelId(omscancelId);
					log.info(
							"transactionNumber " + transactionNumber + "After getting the values from OmsCoCancelItem");
					List<CustOrdCancelItemDetails> custOrdCancelItemDetailsList = custOrdCancelItemDetailsCol
							.getCustOrdCancelItemDetails();
					log.info("transactionNumber " + transactionNumber + "List is intialized");
					List<CustOrdItmPkVo> custOrdItmPkVo = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
					for (OmsCoCancelItem omsCoCancelItemLoop : omsCoCancelItem) {
						for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
							if (custOrdItmPkVoLoop.getLineItemNo() == omsCoCancelItemLoop.getLineNo().intValue()) {
								log.info("transactionNumber " + transactionNumber
										+ "Inside omsCustOrdItemLoop for response");
								CustOrdCancelItemDetails custOrdCancelItemDetails = new CustOrdCancelItemDetails();
								log.info("transactionNumber " + transactionNumber + "Inside loop for OmsCustOrdItem");
								custOrdCancelItemDetails.setLineNo(omsCoCancelItemLoop.getLineNo());
								log.info("transactionNumber " + transactionNumber + "Line No is set -----"
										+ omsCoCancelItemLoop.getLineNo());
								custOrdCancelItemDetails.setStatus("S");
								log.info("transactionNumber " + transactionNumber + "Status is set----"
										+ custOrdCancelItemDetails.getStatus());
								custOrdCancelItemDetails.setMessageCode("Success");
								custOrdCancelItemDetails.setMessageDesc("Item Cancelled Successfully.");
								log.info("transactionNumber " + transactionNumber + "end of createResponse method");
								log.info("Successfully created the response !");
								custOrdCancelItemDetailsList.add(custOrdCancelItemDetails);
								log.info("transactionNumber " + transactionNumber + "Response completed !");

							}

						} // end of if condtion input line_no = db line no
					} // end of CustOrdItmPkVo
				}
			} // end of boolean condition
		}
		return custOrdCancelItemDetailsCol;

	}

	public BigDecimal checkNullValueForNumber(BigDecimal value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public List<OmsCoFulfillDetail> shuffleFulfillOrderNo(List<OmsCoFulfillDetail> fulfillDetailList) {

		log.info("prcessing method shuffleFulfillOrderNo");
		List<OmsCoFulfillDetail> newFulfillDetailList = new ArrayList<OmsCoFulfillDetail>();
		List<OmsCoFulfillDetail> poFulfillDetail = new ArrayList<OmsCoFulfillDetail>();

		for (OmsCoFulfillDetail omsCoFulfillDetail : fulfillDetailList) {
			if (omsCoFulfillDetail.getSourceLocType().equals("SU") == true) {
				poFulfillDetail.add(omsCoFulfillDetail);
			}

		}
		if (poFulfillDetail != null) {

			newFulfillDetailList.addAll(poFulfillDetail);
			for (OmsCoFulfillDetail omsCoFulfillDetail : fulfillDetailList) {
				if (omsCoFulfillDetail.getSourceLocType().equals("SU") == false) {
					log.info("Adding fulfilment detail with fufill order no=" + omsCoFulfillDetail.getFulfillOrderNo());
					newFulfillDetailList.add(omsCoFulfillDetail);
				}
			}
		} else {
			newFulfillDetailList = fulfillDetailList;
		}
		return newFulfillDetailList;
	}

	public void reconcilePickCancel(PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef,
			CustOrderPicVo custOrderPicVo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("customerOrderNo " + customerOrderNo);
		log.info("transactionNumber " + transactionNumber);

		if (transactionNumber != null && !transactionNumber.equals("")) {
			if (pickupCustomerOrderItemDetailsRef.getPickupStatus().equals("S")) {
				persistantOmsOrposMasterAuditPickUp(pickupCustomerOrderItemDetailsRef, custOrderPicVo);
			}
			log.info("rmaRefundData.size() " + rmaRefundData.size());
			log.info("dummyCancelData.size() " + dummyCancelData.size());
			if ((rmaRefundData.size() != 0 && rmaRefundData != null)
					|| (dummyCancelData.size() != 0 && dummyCancelData != null)) {
				persistantOrposMasterAuditRmaAndCancel(rmaRefundData, dummyCancelData);

			}
			if ((rmaRefundData.size() == 0 && dummyCancelData.size() == 0)) {
				try {
					CustOrdCancelItemDetailsCol custOrdCancelItemDetailsCol = pickupCustomerOrderItemDetailsRef
							.getCustOrdCancelItemDetailsCol();
					if (null != custOrdCancelItemDetailsCol) {
						List<CustOrdCancelItemDetails> custOrdCancelItemDetailsList = custOrdCancelItemDetailsCol
								.getCustOrdCancelItemDetails();
						log.info("transactionNumber " + transactionNumber + "custOrdCancelItemDetailsList.size()"
								+ custOrdCancelItemDetailsList.size());
						for (CustOrdCancelItemDetails custOrdCancelItemDetails : custOrdCancelItemDetailsList) {
							log.info("transactionNumber " + transactionNumber
									+ "Inside for loop of custOrdCancelItemDetails");
							if (custOrdCancelItemDetails.getStatus().equals("SUCCESS")
									&& custOrdCancelItemDetails.getCancelledQty().intValue() > 0) {
								log.info("transactionNumber " + transactionNumber
										+ "Inside If Condition custOrdCancelItemDetails.getStatus().equals(SUCCESS)");
								persistantOrposMasterAuditCancel(custOrderPicVo, custOrdCancelItemDetails);
							}
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public void persistantOmsOrposMasterAuditPickUp(PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef,
			CustOrderPicVo custOrderPicVo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();

		PickCustOrdItemBean pickCustOrdItemBean = new PickCustOrdItemBean();
		CustOrdItmPkColVo custOrdItmPkColVo = custOrderPicVo.getCustOrdItmPkColVo();
		List<CustOrdItmPkVo> custOrdItmPkVoList = custOrdItmPkColVo.getCustOrdItmPkVo();

		OmsCustOrdHead omscustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("CustOrdItmPkVo list initialized");
		for (CustOrdItmPkVo custOrdItmPkVo : custOrdItmPkVoList) {
			log.info("Inside CustOrdItmPkVo list");
			log.info("Completed Quantity: " + custOrdItmPkVo.getCompletedQuantity());
			if (custOrdItmPkVo.getCompletedQuantity().intValue() > 0 && custOrdItmPkVo.getCompletedQuantity() != null) {
				log.info("CustOrdItmPkVo loop starts");
				OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();

				omsOrposMasterAudit.setOrderId(pickupCustomerOrderItemDetailsRef.getCustOrderRef().getOrderId());
				omsOrposMasterAudit.setLineItemNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()));
				omsOrposMasterAudit.setOmsCustOrderNo(omscustOrdHead.getOmsCustOrdNo());
				omsOrposMasterAudit.setOrposTransactionNumber(transactionNumber);
				omsOrposMasterAudit.setPickupQuantity(custOrdItmPkVo.getCompletedQuantity());
				omsOrposMasterAudit.setRmaReqId(new BigDecimal(-1));
				omsOrposMasterAudit.setCancelReqId(new BigDecimal(-1));
				log.info("setPickupQuantity: " + custOrdItmPkVo.getCompletedQuantity());
				omsOrposMasterAudit.setEventId("PK");
				log.info("setEventId: " + "PK");
				omsOrposMasterAudit.setStatus("N");
				log.info("setStatus: " + "N");
				// CREATE TIMESTAMP
				omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
				try {
					session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
					log.info("transactionNo " + transactionNumber + "successfully persisted OmsOrposMasterAudit");

				} catch (Exception e) {
					log.info("transactionNo " + transactionNumber
							+ "Exception while persisting into master audit table " + e.getMessage());
				}
			}
		}
	}

	public void persistantOrposMasterAuditCancel(CustOrderPicVo custOrderPicVo,
			CustOrdCancelItemDetails custOrdCancelItemDetails)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			SOAPException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();

		log.info("Inside persistantOrposMasterAuditCancel method");
		OmsCustOrdHead omscustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("CustOrdItmPkVo loop starts");
		OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();
		log.info("After omsOrposMasterAudit object creation");
		omsOrposMasterAudit.setCancelQuantity(custOrdCancelItemDetails.getCancelledQty());
		log.info("setCancelQuantity: " + custOrdCancelItemDetails.getCancelledQty());
		omsOrposMasterAudit.setLineItemNo(custOrdCancelItemDetails.getLineNo());
		log.info("setLineItemNo: " + custOrdCancelItemDetails.getLineNo());
		omsOrposMasterAudit.setEventId("CA");
		log.info("setEventId: " + "CA");
		omsOrposMasterAudit.setOmsCustOrderNo(omscustOrdHead.getOmsCustOrdNo());
		log.info("setOmsCustOrderNo: " + omscustOrdHead.getOmsCustOrdNo());
		omsOrposMasterAudit.setOrderId(customerOrderNo);
		log.info("setOrderId: " + customerOrderNo);
		omsOrposMasterAudit.setOrposTransactionNumber(transactionNumber);
		log.info("setOrposTransactionNumber: " + transactionNumber);
		omsOrposMasterAudit.setRmaReqId(new BigDecimal(-1));
		omsOrposMasterAudit.setCancelReqId(new BigDecimal(-1));
		// CREATE TIMESTAMP
		omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
		omsOrposMasterAudit.setStatus("N");
		try {
			session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
			log.info("transactionNo " + transactionNumber + "successfully persisted OmsOrposMasterAudit");

		} catch (Exception e) {
			log.info("transactionNo " + transactionNumber + "Exception while persisting into master audit table "
					+ e.getMessage());
		}

	}

	public void persistantOrposMasterAuditRmaAndCancel(List<RMARefundData> rmarefundList,
			List<DummyCancelData> dummyCancelDataList)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			SOAPException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		OmsCustOrdHead omscustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("rmarefundList " + rmarefundList.size());
		if (rmarefundList.size() != 0 && rmarefundList != null) {
			for (RMARefundData rma : rmarefundList) {
				log.info("Inside for loop of omsRmaReqItem");
				OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();
				log.info("After omsOrposMasterAudit instance creation");
				omsOrposMasterAudit.setRmaReqId(rma.getRmaId());
				log.info("Setting RmaReqId: " + rma.getRmaId());
				omsOrposMasterAudit.setLineItemNo(rma.getLineNo());
				log.info("Setting LineItem no: " + rma.getLineNo());
				omsOrposMasterAudit.setCancelReqId(new BigDecimal(-1));
				// omsOrposMasterAudit.setReturnQuantity(custOrdItmPkVo.getCancelledQuantity());
				omsOrposMasterAudit.setReturnQuantity(rma.getQuantity());
				log.info("Setting Return quantity: " + rma.getQuantity());
				omsOrposMasterAudit.setEventId(rma.getEventId());
				log.info("Setting Event Id:" + rma.getEventId());
				omsOrposMasterAudit.setOrderId(customerOrderNo);
				log.info("Setting Order Id: " + customerOrderNo);
				omsOrposMasterAudit.setOrposTransactionNumber(transactionNumber);
				log.info("Setting Transaction No: " + transactionNumber);
				omsOrposMasterAudit.setStatus("N");
				log.info("Setting Status: N");
				omsOrposMasterAudit.setOmsCustOrderNo(omscustOrdHead.getOmsCustOrdNo());
				log.info("Setting OmsCustOrderNo: " + omscustOrdHead.getOmsCustOrdNo());
				omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
				try {
					session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
					log.info("transactionNo " + transactionNumber + "successfully persisted OmsOrposMasterAudit");
				} catch (Exception e) {
					log.info("transactionNo " + transactionNumber
							+ "Exception while persisting into master audit table " + e.getMessage());
				}
			}
		}

		log.info("dummyCancelDataList.size()" + dummyCancelDataList.size());

		if (dummyCancelDataList.size() != 0 && dummyCancelDataList != null) {

			for (DummyCancelData dummyCancel : dummyCancelDataList) {

				log.info("Inside for loop of omsCoCancelItem");
				OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();
				log.info("After omsOrposMasterAudit instance creation");
				omsOrposMasterAudit.setCancelReqId(dummyCancel.getOmsCancelId());
				omsOrposMasterAudit.setRmaReqId(new BigDecimal(-1));
				log.info("Setting CancelReqId: " + dummyCancel.getOmsCancelId());
				omsOrposMasterAudit.setLineItemNo(dummyCancel.getLineNo());
				log.info("Setting LineItemNo: " + dummyCancel.getLineNo());
				omsOrposMasterAudit.setCancelQuantity(dummyCancel.getQuantity());
				log.info("Setting cancelQuantity: " + dummyCancel.getQuantity());
				omsOrposMasterAudit.setEventId(dummyCancel.getEventId());
				log.info("Setting EventId: " + dummyCancel.getEventId());
				omsOrposMasterAudit.setOrderId(customerOrderNo);
				log.info("Setting OrderId: " + customerOrderNo);
				omsOrposMasterAudit.setOrposTransactionNumber(transactionNumber);
				log.info("Setting Transaction No: " + transactionNumber);
				omsOrposMasterAudit.setStatus("N");
				log.info("Setting Status: N");
				omsOrposMasterAudit.setOmsCustOrderNo(omscustOrdHead.getOmsCustOrdNo());
				log.info("Setting omsCustOrderNo: " + omscustOrdHead.getOmsCustOrdNo());
				omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
				try {
					session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
					log.info("transactionNo " + transactionNumber + "successfully persisted OmsOrposMasterAudit");
				} catch (Exception e) {
					log.info("transactionNo " + transactionNumber
							+ "Exception while persisting into master audit table " + e.getMessage());
				}
			}
		}

	}

	// Adding the method(code) for the cancellation

	public String checkOpenDelivery(CustOrderPicVo custOrderPicVo, BigDecimal omsCustOrderNo) throws SOAPException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
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
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("--> inside checkOpenDelivery for oms_cust_ord_no=" + omsCustOrderNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrderNo);

		boolean isCancellable = false;
		List<CustOrdItmPkVo> custOrdItmPkVo = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
		for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
			List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
			for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
				if (omsCustOrdItem.getLineNo().intValue() == custOrdItmPkVoLoop.getLineItemNo()) {
					log.info("inside for of item, omsCustOrderNo=" + omsCustOrderNo + "inputItem.getLineNo()"
							+ custOrdItmPkVoLoop.getLineItemNo() + "omsCustOrdItem.getItem()"
							+ omsCustOrdItem.getItem());
					List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByItem(
							omsCustOrderNo, new BigDecimal(custOrdItmPkVoLoop.getLineItemNo()),
							omsCustOrdItem.getItem());
					log.info("omsCoFulfillDetailList size" + omsCoFulfillDetailList.size());
					int handeOverToCourierQty = 0;
					int openQty = 0;
					int backOrderQuantity = 0;
					List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(
							omsCustOrderNo, omsCustOrdItem.getItem(),
							new BigDecimal(custOrdItmPkVoLoop.getLineItemNo()));
					for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
						backOrderQuantity = backOrderQuantity + omsBackOrderDtl.getSourceQty().intValue()
								- omsBackOrderDtl.getFulfillQty().intValue();
					}

					for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
						log.info("checking open delivery,inside fulfill order loop---"
								+ omsCustOrdHead.getDeliveryType() + "----" + omsCoFulfillDetail.getSourceLocType());
						openQty = openQty + omsCoFulfillDetail.getFulfillReqQty().intValue()
								- (omsCoFulfillDetail.getFulfillDeliverQty().intValue()
										+ omsCoFulfillDetail.getFulfillCancelQty().intValue());

						/*
						 * try { OmsBackOrderDtl omsBackOrderDtl =
						 * session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(omsCustOrderNo,
						 * omsCoFulfillDetail.getLineNo(), omsCoFulfillDetail.getSourceLoc());
						 * backOrderQuantity =
						 * backOrderQuantity+omsBackOrderDtl.getSourceQty().intValue() -
						 * omsBackOrderDtl.getFulfillQty().intValue(); } catch (Exception e) {
						 * log.info("Exception while fetching the values from backorder table " +
						 * e.getMessage()); }
						 */

						if (omsCustOrdHead.getDeliveryType().equals("S")
								&& omsCoFulfillDetail.getSourceLocType().equals("WH") == false
								&& omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
							OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
							log.info("calling findAvailableQtyForCancellation ");
							OpenDeliveryBean openDeliveryBean = omsUtilCommmons
									.findAvailableQtyForCancellation(omsCoFulfillDetail, customerOrderNo);

							if (openDeliveryBean.getItem() != null) {

								handeOverToCourierQty = handeOverToCourierQty + Math.abs((openDeliveryBean.getQuantity()
										- omsCoFulfillDetail.getFulfillDeliverQty().intValue()));
								log.info("handeOverToCourierQty=" + handeOverToCourierQty);
							} else {
								isCancellable = true;
							}

						} else if (omsCustOrdHead.getDeliveryType().equals("S")
								&& omsCoFulfillDetail.getSourceLocType().equals("WH")) {
							// ship to customer from ware house
							log.info("omsCustOrdHead.getDeliveryType().equals(\"S\") &&\n"
									+ "                       omsCoFulfillDetail.getSourceLoc().equals(\"WH\") == false");
							// List<Ordcust> ordCustList =
							// session.getOrdcustFindByFulfilOrdNo(input.getCustOrderNo().toString(),
							// omsCoFulfillDetail.getFulfillOrderNo().toString());
							log.info("extCustOrdNo " + extCustOrdNo);
							log.info(" omsCoFulfillDetail.getFulfillOrderNo().toString() "
									+ omsCoFulfillDetail.getFulfillOrderNo().toString());
							List<Ordcust> ordCustList = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,
									omsCoFulfillDetail.getFulfillOrderNo().toString(),
									omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc());
							if (ordCustList.get(0).getTsfNo() != null
									&& ordCustList.get(0).getTsfNo().intValue() != 0) {
								log.info("Transfer No" + ordCustList.get(0).getTsfNo());
								log.info("Item " + omsCoFulfillDetail.getItem().toString());
								BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
								OMSUtilJdbc omsUtilJdbc = new OMSUtilJdbc();
								try {
									totalSelectedDistroQty = omsUtilJdbc.getselectedandDistroQty(
											omsCoFulfillDetail.getItem(), ordCustList.get(0).getTsfNo());
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
						} else {
							isCancellable = true;
							log.info("Cancel order");
							// --- processCancellation(input);
						}
					} // for end
					log.info("handeOverToCourierQty=" + handeOverToCourierQty + "  openQty=" + openQty);
					if (omsCoFulfillDetailList.size() != 0) {
						int a = (openQty + backOrderQuantity) - handeOverToCourierQty;
						log.info("*" + a);
						if (custOrdItmPkVoLoop.getCancelledQuantity().intValue() > (openQty + backOrderQuantity)
								- handeOverToCourierQty) {
							log.info("Cancel Quantity : " + custOrdItmPkVoLoop.getCancelledQuantity());
							isCancellable = false;
							// throw new SOAPException(OMSUtilCommons.formErrorDescription("CANT_CANCEL",
							// "1",new String[] { omsCustOrdItem.get(0).getItem() }));
							// Delivery is in progress
							error = "ERROR_207";
							status = "F";
						} else {
							isCancellable = true;
						}
					} else {
						// cancellation of back order
						isCancellable = true;
					}

					if (isCancellable == true) {
						status = "S";
						error = "success";
					}
				}
			}
		}
		return status;
		// processCancellation(custOrdItmPkVoLoop, custOrderPicVo);
	} // End of checkOpenDelivery method

	// Added method for LinkLineItem

	public String checkLinkedItem(CustOrderPicVo custOrderPicVo, BigDecimal omsCustOrderNo) throws SOAPException {
		log.info("-->inside checkLinkedItem");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Map<BigDecimal, String> inputItem = new HashMap<BigDecimal, String>();
		Map<BigDecimal, String> itemTab = new HashMap<BigDecimal, String>();
		Map<BigDecimal, BigDecimal> inputmap = new HashMap<BigDecimal, BigDecimal>();
		OMSUtilCommons omsutilCommons = new OMSUtilCommons();
		String desc = null;
		String lang = session.getOmsCustOrdHeadFindLanguage(omsCustOrderNo);
		log.info("lang " + lang);
		OmsErrorCodes omsErrorCodes = OMSUtilCommons.getOmsErrorCodesObject("LINKED_ITEM", lang);
		desc = omsErrorCodes.getOmsErrLangDesc();

		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omsCustOrderNo);
		List<OmsCustOrdItem> omsCustOrdItemList2 = null;
		List<CustOrdItmPkVo> custOrdItmPkVo = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();

		for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
			inputmap.put(new BigDecimal(custOrdItmPkVoLoop.getLineItemNo()), custOrdItmPkVoLoop.getCancelledQuantity());
		}
		for (CustOrdItmPkVo custOrdItmPkVoLoop : custOrdItmPkVo) {
			for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
				if (omsCustOrdItem.getLineNo().intValue() == custOrdItmPkVoLoop.getLineItemNo()) {
					log.info("**** omsCustOrdItem.getLineNo() : " + omsCustOrdItem.getLineNo());
					log.info("**** custOrdItmPkVoLoop.getLineItemNo() : " + custOrdItmPkVoLoop.getLineItemNo());
					inputItem.put(new BigDecimal(custOrdItmPkVoLoop.getLineItemNo()), omsCustOrdItem.getItem());

					// added code for 2565
					if (omsCustOrdItem.getLineLinkNo() != null) {
						BigDecimal availabeQtytoCancel = omsCustOrdItem.getQtyOrderedSuom()
								.subtract(omsCustOrdItem.getCumQtyDelivered().add(omsCustOrdItem.getQtyCancelled()));
						log.info("availabeQtytoCancel " + availabeQtytoCancel);
						BigDecimal retail_amt = omsutilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrderNo,
								omsCustOrdItem.getLineNo(), omsCustOrdItem.getUnitRetail());
						log.info("retail_amt " + retail_amt);

						if (custOrdItmPkVoLoop.getCancelledQuantity().intValue() != availabeQtytoCancel.intValue()) {
							log.info("removing lineNo " + custOrdItmPkVoLoop.getLineItemNo());
							if (retail_amt.intValue() > 0) {
								inputItem.remove(new BigDecimal(custOrdItmPkVoLoop.getLineItemNo()));
							}
						}
					}

					try {
						log.info("Inside Try Block");
						omsCustOrdItemList2 = session.getOmsCustOrdItemFindByLinkLineNo(omsCustOrderNo,
								omsCustOrdItem.getLineNo());
						if (omsCustOrdItemList2.size() > 0) {
							for (OmsCustOrdItem ordItem : omsCustOrdItemList2) {

								log.info("ordItem.getQtyOrderedSuom() " + ordItem.getQtyOrderedSuom());
								log.info("ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled() "
										+ (ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled())));
								if (ordItem.getQtyOrderedSuom().intValue() != (ordItem.getCumQtyDelivered()
										.add(ordItem.getQtyCancelled()).intValue())) {
									log.info(
											"==============================11111111111111111111111111111111111====================");
									log.info("===========item.getLineNo()=============== "
											+ custOrdItmPkVoLoop.getLineItemNo());
									log.info("==========ordItem.getLineNo()================ " + ordItem.getLineNo());
									itemTab.put(ordItem.getLineNo(), ordItem.getItem()); // b
									itemTab.put(ordItem.getLineLinkNo(), ordItem.getItem()); // a

								} else {
									log.info(
											"==============================22222222222222222222222222222222====================");

									log.info("==========ordItem.getLineLinkNo()================ "
											+ ordItem.getLineLinkNo());
									itemTab.put(ordItem.getLineLinkNo(), ordItem.getItem());

								}
							}
						} else {
							log.info(
									"==============================33333333333333333333333333333333333333333333====================");

							log.info("No records exist for link lineNo " + omsCustOrdItem.getLineLinkNo());
							log.info("==========omsCustOrdItem.getLineLinkNo()================ "
									+ omsCustOrdItem.getLineLinkNo());
							log.info("omsCustOrdItem.getUnitRetail() " + omsCustOrdItem.getUnitRetail());
							OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
							BigDecimal retail_amt = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrderNo,
									omsCustOrdItem.getLineNo(), omsCustOrdItem.getUnitRetail());
							log.info("retail_amt " + retail_amt);

							if (omsCustOrdItem.getLineLinkNo() != null
									&& inputItem.containsKey(omsCustOrdItem.getLineLinkNo())
									&& retail_amt.intValue() > 0) {

								itemTab.put(omsCustOrdItem.getLineLinkNo(), omsCustOrdItem.getItem());
							} else if (retail_amt.intValue() > 0
									&& !inputItem.containsKey(omsCustOrdItem.getLineLinkNo())) {
								log.info("Unit Retail is greater than Zero should be cancelled with main item");

								if (omsCustOrdItem.getLineLinkNo() != null) {
									itemTab.put(omsCustOrdItem.getLineLinkNo(), omsCustOrdItem.getItem());
								}
							}

							// Added code for 2565 - for a 0 priced scenario
							else if (retail_amt.intValue() == 0
									&& inputmap.keySet().contains(omsCustOrdItem.getLineLinkNo())) {
								BigDecimal mainItemInputQty = inputmap.get(omsCustOrdItem.getLineLinkNo());
								BigDecimal mainItemAvailableQuantity = BigDecimal.ZERO;
								log.info("mainItemInputQty -------------> " + mainItemInputQty);

								List<OmsCustOrdItem> omsCustOrdItem1 = session
										.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrderNo,
												omsCustOrdItem.getLineLinkNo());
								mainItemAvailableQuantity = omsCustOrdItem1.get(0).getQtyOrderedSuom()
										.subtract(omsCustOrdItem1.get(0).getCumQtyDelivered()
												.add(omsCustOrdItem1.get(0).getQtyCancelled()));
								log.info("mainItemAvailableQuantity-------------------->" + mainItemAvailableQuantity);
								BigDecimal childItemAvailableQty = omsCustOrdItem.getQtyOrderedSuom().subtract(
										omsCustOrdItem.getCumQtyDelivered().add(omsCustOrdItem.getQtyCancelled()));
								log.info("-----childItemAvailableQty : " + childItemAvailableQty);
								if (inputmap.keySet().contains(omsCustOrdItem.getLineNo())) {
									log.info("---------------------^^^^^^^^^^^^^^^^^^^^^----------------------");
									BigDecimal childItemQty = inputmap.get(omsCustOrdItem.getLineNo());
									if (childItemQty.intValue() != childItemAvailableQty.intValue()
											&& mainItemAvailableQuantity.intValue() == mainItemInputQty.intValue()) {
										log.info("*******************IF LOOP**********************");
										inputItem.remove(omsCustOrdItem.getLineNo());
										log.info("<--------------- Child ItemTab ------------- " + itemTab.keySet()
												+ "---------------");
										log.info("<-------------child input map----------" + inputmap.keySet()
												+ "------------------------>");
										break;
									}

								}

							}
						}
					} catch (Exception e) {
						log.info("No records exist in OmsCustOrdItem for link line No " + omsCustOrdItem.getLineNo()
								+ e.getMessage());
					}
				}
			}
		}
		log.info("itemTab " + itemTab.keySet());
		log.info("input " + inputItem.keySet());
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
					error = "ERROR_208";
					status = "F";
					break;
				}
			}
			log.info("flag value after existing the loop " + flag);

			if (flag) {
				log.info("Allow to cancel");
				log.info("Request contains both the items");
				status = "S";
				log.info("Reqest contains both the items");
			}
		} else {
			log.info("Allow to cancel");
			status = "S";
		}
		return status;
	} // End of checkLinkedItem method

	public int sumofOmsCustCancelTender(BigDecimal omsCancelId, BigDecimal omsCustOrdNo) throws Exception {
		log.info("inside sumofOmsCustCancelTender ");
		log.info("omsCancelId : " + omsCancelId);
		log.info("omsCustOrderNo " + omsCustOrdNo);
		int count = 0;
		String query = "select count(*) from OMS_CUST_CANCEL_TENDER where oms_cancel_id = ? and oms_cust_ord_no = ?";
		log.info("query " + query);

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setBigDecimal(1, omsCancelId);
			preparedStatement.setBigDecimal(2, omsCustOrdNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				count = rs.getInt(1);
				log.info("@@@Count : " + count);
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}

		return count;

	}

	public void updateConfirmQty(CustOrderPicVo input) throws SOAPException {
		log.info("inside updateConfirmQty ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCancelId " + omsCancelId);
		List<OmsCoCancelItem> omsCoCancelItemList = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		for (OmsCoCancelItem omsCoCancelItem : omsCoCancelItemList) {
			if (omsCoCancelItem.getCancelReqQty().intValue() > 0) {
				if (omsCoCancelItem.getCancelConfQty().intValue() == 0) {
					log.info("omsCoCancelItem lineno : " + omsCoCancelItem.getLineNo());
					log.info("omsCoCancelItem item : " + omsCoCancelItem.getItem());
					omsCoCancelItem.setCancelConfQty(omsCoCancelItem.getCancelReqQty());
					session.mergeOmsCoCancelItem(omsCoCancelItem);
					log.info("updated confirmQty");
				}
			}
			if (omsCoCancelItem.getCancelReqQty().intValue() < 0 || omsCoCancelItem.getCancelConfQty().intValue() < 0) {
				omsCoCancelItem.setCancelConfQty(BigDecimal.ZERO);
				session.mergeOmsCoCancelItem(omsCoCancelItem);

			}
		}
	}

	public int getPickandDeliveryFulfilorderNo(String customerOrderNo, String item, int fulfilOrdNo) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int quantity_Picked = 0;

		String query = "select b.quantity_picked-b.quantity_delivered  from ful_ord a, ful_ord_line_item b  "
				+ "where b.ful_ord_id = a.id and a.cust_order_id=? and b.item_id=? and a.external_id=?";

		log.info("query " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, customerOrderNo);
			preparedStatement.setString(2, item);
			preparedStatement.setInt(3, fulfilOrdNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				quantity_Picked = rs.getInt(1);
				log.info("quantity_Picked " + quantity_Picked);
			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return quantity_Picked;
	} // end of method

	public String cancelNonInventoryShippingChargeItems(CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("inside cancelNonInventoryShippingChargeItems");
		status = checkNonInventoryItem(custOrderPicVo);
		status = checkShippingChargeItem(custOrderPicVo);
		if (status.equals("F")) {
			status = "F";
			error = "ERROR_201";
		} else {
			status = "S";
			error = "success";
		}
		return status;
	}

	public String checkNonInventoryItem(CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("Check only for non-inventory items");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("omsCustOrdHead.getOmsCustOrdNo() " + omsCustOrdHead.getOmsCustOrdNo());

		Map<BigDecimal, BigDecimal> inventoryItemMap = new HashMap<BigDecimal, BigDecimal>();
		String shippingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT",
				"OMS_SYSTEM_OPTION");
		String inventoryIndicator = "N";
		BigDecimal itemDept = null;
		boolean cancellationNonIventoryCheck = false;
		List<CustOrdItmPkVo> cancellList = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
		for (CustOrdItmPkVo customerOrderCancellation : cancellList) {
			List<OmsCustOrdItem> itemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(
					omsCustOrdHead.getOmsCustOrdNo(), new BigDecimal(customerOrderCancellation.getLineItemNo()));
			itemDept = session.getItemMasterFindDept(itemList.get(0).getItem());
			inventoryIndicator = session.getItemMasterFindInventoryInd(itemList.get(0).getItem(), itemDept);
			log.info("inventoryIndicator " + inventoryIndicator);
			if (inventoryIndicator.equals("N") && !itemDept.toString().equals(shippingChargeDept.toString())) {
				nonInventoryItemMap.put(new BigDecimal(customerOrderCancellation.getLineItemNo()),
						customerOrderCancellation.getCancelledQuantity());
			} else if (!itemDept.toString().equals(shippingChargeDept.toString())) {
				inventoryItemMap.put(new BigDecimal(customerOrderCancellation.getLineItemNo()),
						customerOrderCancellation.getCancelledQuantity());
			}
		}
		log.info("inventoryItemMap.size() " + inventoryItemMap.size());
		if (inventoryItemMap == null || inventoryItemMap.size() == 0) {
			List<OmsCustOrdItem> omsCustOrdItemList = session
					.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
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
					if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue()
							+ omsCustOrdItem.getQtyCancelled().intValue())) {
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
				List<OmsCustOrdItem> omsCustOrdItemList = session
						.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), lineNo);
				if (cancelQty.intValue() + (omsCustOrdItemList.get(0).getCumQtyDelivered().intValue()
						+ omsCustOrdItemList.get(0).getQtyCancelled().intValue()) != omsCustOrdItemList.get(0)
								.getQtyOrderedSuom().intValue()) {
					log.info("Main item qty is less in input so cannot cancel the shipping charge item");
					cancellationNonIventoryCheck = true;
					break;
				}
			}

			if (cancellationNonIventoryCheck == false) {
				List<OmsCustOrdItem> omsCustOrdItemList = session
						.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
				for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
					cancellationNonIventoryCheck = false;
					itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
					log.info("omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
					log.info("shippingChargeDept " + shippingChargeDept);
					log.info("itemDept " + itemDept);
					inventoryIndicator = session.getItemMasterFindInventoryInd(omsCustOrdItem.getItem(), itemDept);
					if (!inventoryItemMap.containsKey(omsCustOrdItem.getLineNo())
							&& itemDept.toString().equals(shippingChargeDept.toString()) == false
							&& inventoryIndicator.equals("Y")) {
						if (omsCustOrdItem.getQtyOrderedSuom()
								.intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue()
										+ omsCustOrdItem.getQtyCancelled().intValue())) {
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
			status = "F";
			error = "ERROR_201";
		}
		return status;
	}

	public String checkShippingChargeItem(CustOrderPicVo custOrderPicVo) throws SOAPException {
		log.info("Check only for ShippingCharge items");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
		log.info("omsCustOrdHead.getOmsCustOrdNo() " + omsCustOrdHead.getOmsCustOrdNo());
		Map<BigDecimal, BigDecimal> shippingChargeItemMap = new HashMap<BigDecimal, BigDecimal>();
		Map<BigDecimal, BigDecimal> mainItemMap = new HashMap<BigDecimal, BigDecimal>();
		Map<BigDecimal, BigDecimal> ItemMap = new HashMap<BigDecimal, BigDecimal>();

		String shippingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT",
				"OMS_SYSTEM_OPTION");
		String inventoryIndicator = null;
		BigDecimal itemDept = null;
		boolean shippingChargeItemcheck = false;
		List<CustOrdItmPkVo> cancellList = custOrderPicVo.getCustOrdItmPkColVo().getCustOrdItmPkVo();
		for (CustOrdItmPkVo customerOrderCancellation : cancellList) {
			List<OmsCustOrdItem> itemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(
					omsCustOrdHead.getOmsCustOrdNo(), new BigDecimal(customerOrderCancellation.getLineItemNo()));
			itemDept = session.getItemMasterFindDept(itemList.get(0).getItem());
			inventoryIndicator = session.getItemMasterFindInventoryInd(itemList.get(0).getItem(), itemDept);
			log.info("inventoryIndicator " + inventoryIndicator);
			if (itemDept.toString().equals(shippingChargeDept.toString()) == true) {
				shippingChargeItemMap.put(new BigDecimal(customerOrderCancellation.getLineItemNo()),
						customerOrderCancellation.getCancelledQuantity());
			} else {
				mainItemMap.put(new BigDecimal(customerOrderCancellation.getLineItemNo()),
						customerOrderCancellation.getCancelledQuantity());
			}
		}
		if (mainItemMap == null || mainItemMap.size() == 0) {
			List<OmsCustOrdItem> omsCustOrdItemList = session
					.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
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
					if (omsCustOrdItem.getQtyOrderedSuom().intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue()
							+ omsCustOrdItem.getQtyCancelled().intValue())) {
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
					List<OmsCustOrdItem> omsCustOrdItemList = session
							.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), lineNo);
					if (cancelQty.intValue() + (omsCustOrdItemList.get(0).getCumQtyDelivered().intValue()
							+ omsCustOrdItemList.get(0).getQtyCancelled().intValue()) != omsCustOrdItemList.get(0)
									.getQtyOrderedSuom().intValue()) {
						log.info("Main item qty is less in input so cannot cancel the shipping charge item");
						shippingChargeItemcheck = true;
						break;
					}
				}
				if (shippingChargeItemcheck == false) {
					List<OmsCustOrdItem> omsCustOrdItemList = session
							.getOmsCustOrdItemFindByCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
					for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
						shippingChargeItemcheck = false;
						if (!ItemMap.containsKey(omsCustOrdItem.getLineNo())) {
							if (omsCustOrdItem.getQtyOrderedSuom()
									.intValue() != (omsCustOrdItem.getCumQtyDelivered().intValue()
											+ omsCustOrdItem.getQtyCancelled().intValue())) {
								log.info(
										"Still items are there to cancel and its not present in the cancellation input");
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
			status = "F";
			error = "ERROR_201";
		}

		return status;
	}

} // End of PickCustOrdItemBean class
