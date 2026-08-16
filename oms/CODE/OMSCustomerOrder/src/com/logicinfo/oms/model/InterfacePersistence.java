package com.logicinfo.oms.model;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.bds.bean.carrera.TransferCreationResponse;
import com.google.gson.Gson;
import com.logicinfo.oms.beans.BackOrderAllocatedInventory;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.ArrayOfOmsOrderStatusUpdateDetail;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderDetailStatus;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.logicinfo.oms.util.OracleBaseAPIUtil;
import com.logicinfo.oms.util.StoreInventory;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.ConfirmType;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfilOrdDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.LocType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvItmMod;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfItm;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfCriStatus;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfHdrCriVo;
import com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1.StsTsfHdrDesc;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDesc;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.LookupTransferHeader;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReadTransferDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequest;

import retail.siebel.com.integration.CustomerDetails;
import retail.siebel.com.integration.CustomerOrderHeaderLevel;
import retail.siebel.com.integration.CustomerOrderHeaderLevelResponse;
import retail.siebel.com.integration.FulfillmentDetails;
import retail.siebel.com.integration.InboundordercreationbpelprocessClientEp;
import retail.siebel.com.integration.ItemLevelDetails;
import retail.siebel.com.integration.SiebelOrderFeedWebservice;
import retail.siebel.com.integration.TenderDetails;

public class InterfacePersistence {
	public InterfacePersistence() {
		super();
	}

	private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName());
	FulfilOrdCfmCol fulfilOrdCfmCol = null;
	ConfirmType confirmType = null;
	String extCustOrdNo = "";

	public void callRMSCancellationWebservice(BigDecimal omsCustOrdNo, List<OmsTempCoFo> temp)
			throws EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
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
		FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
		fulfilOrdColRef.setCollectionSize(1); // collection size
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
		fulfilOrdRef.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));
		fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.get(0).getFulfillOrderNo()));
		log.info("fulfill ord no sent to rms:" + temp.get(0).getFulfillOrderNo() + "for item:" + temp.get(0).getItem() + "oms_cust_ord_n0:" + omsCustOrdNo);
		fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue());
		fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(temp.get(0).getSourceLocationType())); // **
		for (OmsTempCoFo omsTempCoFo : temp) {
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			fulfilOrdDtlRef.setCancelQtySuom(omsTempCoFo.getFoConfQty()); // check
			fulfilOrdDtlRef.setItem(omsTempCoFo.getItem());
			// fulfilOrdDtlRef.setRefItem(value);
			OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		}

		fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(temp.get(0).getSourceLocationType()));
		fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);

		CancelFulfilOrdColRef colRef = new CancelFulfilOrdColRef();
		colRef.setFulfilOrdColRef(fulfilOrdColRef);
		InvocationSuccess result = OracleBaseAPIUtil.getOracleRMSClient().cancelFulfilOrdColRef(colRef).getInvocationSuccess();
		log.info("Result from rms---" + result.getSuccessMessage());
	}

	public void callSIMCancelllationWS(BigDecimal omsCustOrdNo, List<OmsTempCoFo> temp) throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,

			SOAPException, EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,

			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,

			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,

			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException {
		log.info("callSIMCancelllationWS started as part of rollback");

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
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
		Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
		FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
		fulfilOrdColRef1.setCollectionSize(1);

		fulfilOrdColRef.value = fulfilOrdColRef1;
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
		fulfilOrdRef.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));

		fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue()); // **
		fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(temp.get(0).getSourceLocationType()));

		fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.get(0).getFulfillOrderNo()));
		log.info("fulfil ord_no=" + temp.get(0).getFulfillOrderNo() + "Sourc_loc=" + temp.get(0).getSourceLocId() + fulfilOrdRef.getSourceLocType() + "fulfill_loc=" + temp.get(0).getFulfillLocId()
				+ fulfilOrdRef.getFulfillLocType());
		for (OmsTempCoFo tempCoFo : temp) {
			log.info("extCustOrdNo=" + extCustOrdNo + "Item sent for cancelllatin is " + tempCoFo.getItem());
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			// fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());
			log.info("cancel qy=" + tempCoFo.getOrderQty());
			fulfilOrdDtlRef.setCancelQtySuom(tempCoFo.getOrderQty()); // check
			fulfilOrdDtlRef.setItem(tempCoFo.getItem());
			OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, tempCoFo.getItem(), tempCoFo.getLineNo());
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		}
		fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
		fulfilOrdColRef.value = fulfilOrdColRef1;
		try {
			log.info("fulfilOrdColRef size=" + fulfilOrdColRef.value.getFulfilOrdRef().size());
			log.info("---" + fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());

			CancelFulfillmentOrderDetail detail = new CancelFulfillmentOrderDetail();
			detail.setFulfilOrdColRef(fulfilOrdColRef1);
			OracleBaseAPIUtil.getOracleSIMClient().cancelFulfillmentOrderDetail(detail);

		} catch (Exception e) {
			log.error("Error in sim cancellation");
		}
	}

	public void callSeibelWebservice(CustomerOrder input, BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("calling siebel webservice");
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		CustomerOrderHeaderLevel customerOrderHeaderLevel = new CustomerOrderHeaderLevel();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		String extCustOrdNo = input.getCustomerOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
		if (omsCustOrdHead.getSubCustOrderNo().equals("1")) {
			extCustOrdNo = input.getCustomerOrderNo();
		} else {
			if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
				if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
					extCustOrdNo = input.getCustomerOrderNo().trim() + "0" + omsCustOrdHead.getSubCustOrderNo().trim();
				} else {
					extCustOrdNo = input.getCustomerOrderNo().trim() + "00" + omsCustOrdHead.getSubCustOrderNo().trim();
				}
			}
		}
		if (omsCustOrdHead.getCustId() != null)
			customerOrderHeaderLevel.setCustId(omsCustOrdHead.getCustId());
		customerOrderHeaderLevel.setOmsCustOrdNo(omsCustOrdNo.toString());
		customerOrderHeaderLevel.setSubCustOrderNo(Long.valueOf(omsCustOrdHead.getSubCustOrderNo()));
		customerOrderHeaderLevel.setCustomerPhoneNo(omsCustOrdHead.getCustPhoneNo());
		customerOrderHeaderLevel.setCustOrderNo(omsCustOrdHead.getCustOrderNo());
		customerOrderHeaderLevel.setCustOrderType(omsCustOrdHead.getCustOrderType());
		customerOrderHeaderLevel.setPaymentStatus(omsCustOrdHead.getOrdPaymentStatus());
		customerOrderHeaderLevel.setOrderRequestorId(omsCustOrdHead.getOrderRequestorId().toString());
		customerOrderHeaderLevel.setDeliveryType(String.valueOf(omsCustOrdHead.getDeliveryType().charAt(0)));
		customerOrderHeaderLevel.setFirstName(omsCustOrdHead.getCustFirstName());
		customerOrderHeaderLevel.setLastName(omsCustOrdHead.getCustLastName());
		GregorianCalendar c = new GregorianCalendar();
		if (omsCustOrdHead.getConsumerDlyTime() != null)
			c.setTime(omsCustOrdHead.getConsumerDlyTime());
		XMLGregorianCalendar consumerDlyTime = null;
		try {
			consumerDlyTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
		}
		if (omsCustOrdHead.getConsumerDlyTime() != null)
			customerOrderHeaderLevel.setConsumerDlyTime(consumerDlyTime);
		customerOrderHeaderLevel.setCustOrderNo((omsCustOrdHead.getCustOrderNo()));

		// customerOrderHeaderLevel.setComment(custOrderDesc.getComments());
		c.setTime(omsCustOrdHead.getCreateDatetime());
		XMLGregorianCalendar createDatetime = null;
		try {
			createDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
		}
		customerOrderHeaderLevel.setConsumerDlyTime(createDatetime);
		customerOrderHeaderLevel.setCreateDatetime(createDatetime);
		if (omsCustOrdHead.getPickLoc() != null)
			customerOrderHeaderLevel.setPickLoc(omsCustOrdHead.getPickLoc().longValue());

		OmsCustOrdAddress omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);

		CustomerDetails customerDetails = new CustomerDetails();
		if (omsCustOrdAddress.getBillAdd1() != null)
			customerDetails.setBillAdd1(omsCustOrdAddress.getBillAdd1());
		if (omsCustOrdAddress.getBillAdd2() != null)
			customerDetails.setBillAdd2(omsCustOrdAddress.getBillAdd2());
		if (omsCustOrdAddress.getBillAdd3() != null)
			customerDetails.setBillAdd3(omsCustOrdAddress.getBillAdd3());
		if (omsCustOrdAddress.getDeliverAdd1() != null)
			customerDetails.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
		if (omsCustOrdAddress.getDeliverAdd2() != null)
			customerDetails.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd2());
		if (omsCustOrdAddress.getDeliverAdd3() != null)
			customerDetails.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd3());
		if (omsCustOrdAddress.getBillCity() != null)
			customerDetails.setBillCity(omsCustOrdAddress.getBillCity());
		if (omsCustOrdAddress.getBillCountry() != null)
			customerDetails.setBillCountry(omsCustOrdAddress.getBillCountry());
		if (omsCustOrdAddress.getBillFirstName() != null)
			customerDetails.setBillFirstName(omsCustOrdAddress.getBillFirstName());
		if (omsCustOrdAddress.getBillLastName() != null)
			customerDetails.setBillLastName(omsCustOrdAddress.getBillLastName());
		if (omsCustOrdAddress.getDeliverCity() != null)
			customerDetails.setDeliverCity(omsCustOrdAddress.getDeliverCity());
		if (omsCustOrdAddress.getDeliverCountry() != null)
			customerDetails.setDeliverCountry(omsCustOrdAddress.getDeliverCountry());
		if (omsCustOrdAddress.getDeliverFirstName() != null)
			customerDetails.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
		if (omsCustOrdAddress.getDeliverLastName() != null)
			customerDetails.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
		if (omsCustOrdAddress.getDeliverPhoneNo() != null)
			customerDetails.setDeliverPhoneNo(omsCustOrdAddress.getDeliverPhoneNo());
		if (omsCustOrdAddress.getCustId() != null)
			customerDetails.setCustId(omsCustOrdAddress.getCustId());
		customerOrderHeaderLevel.setCustomerDetails(customerDetails);

		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
		for (OmsCustOrdItem item : omsCustOrdItemList) {
			ItemLevelDetails itemLevelDetails = new ItemLevelDetails();
			log.info("Item=" + item.getItem() + "Line no=" + item.getLineNo());
			itemLevelDetails.setItem(item.getItem());

			itemLevelDetails.setQtyOrderedSuom(item.getQtyOrderedSuom());
			// itemLevelDetails.setItemType(item.getItemType());

			if (item.getBackorderDlyDate() != null) {
				c.setTime(item.getBackorderDlyDate());
				XMLGregorianCalendar backorderDlyDate = null;
				try {
					backorderDlyDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				} catch (DatatypeConfigurationException e) {
				}
				itemLevelDetails.setBackorderDlyDate(backorderDlyDate);
			}

			if (item.getComments() != null)
				itemLevelDetails.setComments(item.getComments());
			if (item.getOrigUnitRetail() != null)
				itemLevelDetails.setRetailCurr(item.getOrigUnitRetail().toString());
			if (item.getStandardUom() != null)
				itemLevelDetails.setStandardUom(item.getStandardUom());
			if (item.getSubstituteAllowInd() != null)
				itemLevelDetails.setSubstituteAllowInd(item.getSubstituteAllowInd());
			if (item.getTransactionUom() != null)
				itemLevelDetails.setTransactionUom(item.getTransactionUom());
			if (item.getStandardUom() != null)
				itemLevelDetails.setUnitRetail(item.getUnitRetail());
			if (item.getLineNo() != null)
				itemLevelDetails.setLineNo(item.getLineNo().longValue());
			itemLevelDetails.setShippingClassification(item.getShipClassification());
			itemLevelDetails.setBackorderInd(item.getBackorderInd());
			List<OmsCoFulfillDetail> omsCoFulfillDetailLst = session.getOmsCoFulfillDetailFindFulfillByLineNo(item.getLineNo(), omsCustOrdHead.getOmsCustOrdNo());
			List<FulfillmentDetails> fulfillmentDetailsList = itemLevelDetails.getFulfillmentDetails();
			log.info("Adding fulfilment");
			for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailLst) {
				FulfillmentDetails fulfillmentDetails = new FulfillmentDetails();
				fulfillmentDetails.setFulfillOrderNo(omsCoFulfillDetail.getFulfillOrderNo().longValue());
				fulfillmentDetails.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
				fulfillmentDetails.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
				fulfillmentDetails.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
				fulfillmentDetails.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
				fulfillmentDetails.setItem(omsCoFulfillDetail.getItem());
				log.info("Item=" + omsCoFulfillDetail.getItem());

				try {

					BigDecimal poNo = session
							.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc()).get(0)
							.getOrderNo();
					if (poNo != null) {
						fulfillmentDetails.setPoNo(poNo.longValue());
					}

				} catch (Exception e) {

				}
				try {
					log.info("inside tsf no");
					log.info("extCustOrdNo=" + extCustOrdNo + "omsCoFulfillDetail.getFulfillOrderNo()" + omsCoFulfillDetail.getFulfillOrderNo());
					BigDecimal tsfN0 = session
							.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc()).get(0)
							.getTsfNo();
					if (tsfN0 != null) {
						fulfillmentDetails.setTsfNo(session
								.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
								.get(0).getTsfNo().longValue());

					}
					log.info("tsfN0=" + tsfN0);
				} catch (Exception e) {

				}
				for (CustomerOrderItems coItem : input.getCustomerOrderItems()) {
					if (coItem.getBackOrderInd().equals("Y")) {
						log.info("setting response for back order");

						fulfillmentDetails.setBackorderQty(coItem.getOrderQtySuom().longValue());

					}
				}
				fulfillmentDetailsList.add(fulfillmentDetails);
			}
			if (omsCustOrdHead.getOrderCreateReserveInd().equals("R")) {
				List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
				for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList) {
					FulfillmentDetails fulfillmentDetail = new FulfillmentDetails();
					fulfillmentDetail.setFulfillOrderNo(omsCustOrdReserve.getFulfillOrderNo().longValue());
					log.info("fulfill order no" + fulfillmentDetail.getFulfillOrderNo());
					// fulfillmentDetail.setFulfillLoc(omsCustOrdReserve.getLoc().longValue());
					// fulfillmentDetail.setFulfillLocType(omsCustOrdReserve.getLocType());
					// fulfillmentDetail.setSourceLoc(omsCustOrdReserve.getRmsResvLoc().longValue());
					// fulfillmentDetail.setSourceLocType(omsCustOrdReserve.getRmsResvLocType());
					fulfillmentDetail.setItem(omsCustOrdReserve.getItem());
					fulfillmentDetail.setOrderQtySuom(omsCustOrdReserve.getQty());
					log.info("item=" + fulfillmentDetail.getItem());
					FulfillmentDetails fulfillmentDetails = new FulfillmentDetails();
					BigDecimal loc = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCustOrdReserve.getItem(), (item.getLineNo())).get(0).getRmsResvLoc();
					if (loc != null && loc.longValue() >= 0) {
						log.info("loc.longValue()" + loc.longValue());
						fulfillmentDetails.setRMSResvLoc(loc.longValue());
					}
					String locType = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCustOrdReserve.getItem(), (item.getLineNo())).get(0).getRmsResvLocType();
					if (locType != null && locType.isEmpty() == false) {
						fulfillmentDetails.setRMSResvLocType(locType);
						log.info("REsev loc type" + locType);
					}
					BigDecimal resvQty = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCustOrdReserve.getItem(), (item.getLineNo())).get(0).getRmsResvQty();
					if (resvQty != null && resvQty.longValue() >= 0) {
						fulfillmentDetails.setRMSResvQty(resvQty.longValue());
					}
					fulfillmentDetailsList.add(fulfillmentDetails);
				}

			}
			customerOrderHeaderLevel.getItemLevelDetails().add(itemLevelDetails);
		}
		List<OmsCustOrdTender> omsCustOrdTenderList = session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrdNo);

		for (OmsCustOrdTender tender : omsCustOrdTenderList) {
			TenderDetails tenderDetails = new TenderDetails();
			if (tender.getCcAuthNo() != null)
				tenderDetails.setCcAuthNo(tender.getCcAuthNo());
			if (tender.getCcAuthSrc() != null)
				tenderDetails.setCcAuthSrc(tender.getCcAuthSrc());
			if (tender.getCcCardholderVerf() != null)
				tenderDetails.setCcCardholderVerf(tender.getCcCardholderVerf());
			if (tender.getCcEntryMode() != null)
				tenderDetails.setCcEntryMode(tender.getCcEntryMode());
			if (tender.getCcExpDate() != null)
				c.setTime(tender.getCcExpDate());
			XMLGregorianCalendar ccExpDate = null;
			try {
				ccExpDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			} catch (DatatypeConfigurationException e) {
			}
			if (tender.getCcExpDate() != null)
				tenderDetails.setCcExpDate(ccExpDate);
			if (tender.getCcNo() != null)
				tenderDetails.setCcNo(tender.getCcNo());
			if (tender.getCcSpecCond() != null)
				tenderDetails.setCcSpecCond(tender.getCcSpecCond());
			if (tender.getCcTermId() != null)
				tenderDetails.setCcTermId(tender.getCcTermId());
			if (tender.getTenderAmt() != null)
				tenderDetails.setTenderAmt(tender.getTenderAmt());
			if (tender.getTenderTypeGroup() != null)
				tenderDetails.setTenderTypeGroup(tender.getTenderTypeGroup());
			tenderDetails.setTenderSeqNo(tender.getTenderSeqNo().longValue());
			if (tender.getTenderTypeId() != null)
				tenderDetails.setTenderTypeId(tender.getTenderTypeId().longValue());

			customerOrderHeaderLevel.getTenderDetails().add(tenderDetails);
		}

		CustomerOrderHeaderLevelResponse customerOrderHeaderLevelResponse = null;
		try {
			InboundordercreationbpelprocessClientEp inboundordercreationbpelprocessClientEp = new InboundordercreationbpelprocessClientEp();
			SiebelOrderFeedWebservice siebelOrderFeedWebservice = inboundordercreationbpelprocessClientEp.getSiebelOrderFeedWebservicePt();
			customerOrderHeaderLevelResponse = siebelOrderFeedWebservice.publishToSiebel(customerOrderHeaderLevel);

		} catch (Exception e) {
			log.error("Publishing to seibel failed" + e);
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());

			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("P");
			omsRepublishData.setTransactionKey("ORDER FEED");
			String webserviceURL = session.getOmsWebserviceUriDetailFindByWebserviceName("SIEBEL_ORDER_FEED");
			omsRepublishData.setWebServiceId(webserviceURL);
			try {
				JAXBContext context = JAXBContext.newInstance(CustomerOrderHeaderLevel.class);
				Marshaller marshel = context.createMarshaller();
				Writer os = new StringWriter();
				marshel.marshal(customerOrderHeaderLevel, os);
				omsRepublishData.setXmlMsg(os.toString());
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisten in omsRepublish data");
			} catch (Exception f) {
				log.error("JAXB failed" + e);
			}

		}
		if (customerOrderHeaderLevelResponse.getMessage().equals("SUCCESS")) {
			log.info("customerOrderHeaderLevelResponse.getMessage()=" + customerOrderHeaderLevelResponse.getMessage());
		} else {
			// add entry into republish_data
			log.info("customerOrderHeaderLevelResponse.getMessage()=" + customerOrderHeaderLevelResponse.getMessage());
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());

			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_ORDER_FEED WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_ORDER_FEED");
			omsRepublishData.setWebServiceId(webserviceid.toString());

			log.info("before marshalling");
			try {
				JAXBContext context = JAXBContext.newInstance(CustomerOrderHeaderLevel.class);
				Marshaller marshel = context.createMarshaller();
				Writer os = new StringWriter();
				marshel.marshal(customerOrderHeaderLevel, os);
				log.info(os.toString());
				omsRepublishData.setXmlMsg(os.toString());
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisten in omsRepublish data");
			} catch (Exception e) {
				log.error("Failed in persisting in republish data table--" + e);
			}

			// os.toString();
			log.info("------");
		}
	}
	// ----------------------------------------------------------------------------------------

	public ProcessedObject callWebservices(BigDecimal omsCustOrdNo, ArrayList<OmsTempCoFo> temp)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, BusinessException {

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside callWebservices");
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);

		ProcessedObject processedObject = new ProcessedObject();
		// ArrayList<OmsTempCoFo> returnList= processedObject.getOmsTempCoFoList();
		ArrayList<OmsTempCoFo> returnList = new ArrayList<OmsTempCoFo>();
		log.info("processed object" + returnList.size());
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
		log.info("extCustOrdNo=" + extCustOrdNo);
		String partialDeliveryInd = session.getOmsSystemParametersFindIndValue("OMS_PARTIAL_DLV_IND", "OMS_SYSTEM_OPTION");
		log.info("Check for values , tesing purpose SOURCE LOC========= " + temp.get(0).getSourceLocationType());
		log.info("Check for values , tesing purpose FULFIL LOC========= " + temp.get(0).getFulfillLocationType());

		if (temp.get(0).getSourceLocId().compareTo(temp.get(0).getFulfillLocId()) == 0) {
			// souce loc type=null then reservation
			log.info("omsCustOrdNo" + omsCustOrdNo + "reservation condition");
			FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();
			FulfilOrdDesc fulfilOrdDesc = null;
			fulfilOrdColDesc.setCollectionSize(1);
			fulfilOrdDesc = new FulfilOrdDesc();
			for (OmsTempCoFo item : temp) {
				log.info("Fulfill loc id=" + item.getFulfillLocId());
				// fulfilOrdDesc.setCustomerOrderNo(String.valueOf(omsCustOrdNo));
				fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);
				fulfilOrdDesc.setComments("Application id:" + omsCustOrdHead.getApplicationId());
				fulfilOrdDesc.setFulfillOrderNo(temp.get(0).getFulfillOrderNo().toString());
				log.info("fulfilmentOrderNo=" + fulfilOrdDesc.getFulfillOrderNo());
				fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));

				fulfilOrdDesc.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());

				log.info("SIM-Fulfil loc id=" + fulfilOrdDesc.getFulfillLocId());
				fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.fromValue(partialDeliveryInd)); // check

				fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(String.valueOf(omsCustOrdHead.getDeliveryType().charAt(0))));
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				if (omsCustOrdHead.getConsumerDlyTime() != null) {
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
					now.setMonth(now1.get(Calendar.MONTH) + 1);
					if (temp.get(0).getSourceLocationType().equals("WH")) {
						now.setYear(now1.get(Calendar.YEAR) + 4);
						// In case of WH the comsumer delivery should be 5 years ahead to avoid picking
						// of this customer order from WMS untill and unless this is being updated from
						// Appointment Booking from RMS.
					} else {
						now.setYear(now1.get(Calendar.YEAR));
					}
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					fulfilOrdDesc.setConsumerDeliveryDate(now);
					log.info("Consumer delivery date=" + fulfilOrdDesc.getConsumerDeliveryDate());

				} else {
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						log.warn(e.toString());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					String consumerDlyDaysToBeAdded = session.getOmsSystemParametersFindIndValue("CONSUMER_DLY_DATE", "OMS_SYSTEM_OPTION");
					log.info("======================consumerDlyDaysToBeAdded===============================+consumerDlyDaysToBeAdded");
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
					now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
					now.setMonth(now1.get(Calendar.MONTH) + 1);
					now.setYear(now1.get(Calendar.YEAR));
					// now.setDay(now1.get(Calendar.DAY_OF_MONTH)+Integer.valueOf(consumerDlyDaysToBeAdded));
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					fulfilOrdDesc.setConsumerDeliveryDate(now);
				}
				fulfilOrdDesc.setComments("Application id:" + omsCustOrdHead.getApplicationId());
				FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
				fulfilOrdDtl.setItem(item.getItem());
				log.info("SIM:item=" + item.getItem());
				fulfilOrdDtl.setOrderQtySuom(item.getOrderQty());
				log.info("Order qty=" + item.getOrderQty());

				OmsCustOrdItem omsCustOrdItem = null;
				try {
					log.info("omsCustOrdNo=" + omsCustOrdNo + "Item=" + item.getItem() + "Line no=" + item.getLineNo());
					omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(), item.getLineNo());
					log.info("setting item comments in fulfilOrdDtl " + omsCustOrdItem.getComments());
					fulfilOrdDtl.setComments(omsCustOrdItem.getComments());
					log.info("setted item comments in fulfilOrdDtl ");
				} catch (Exception e) {
					log.error(" session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(),item.getLineNo()); failed");
				}
				// Added for finding sum of discounts
				if (omsCustOrdItem.getUnitRetail().intValue() != 0) {
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					log.info("%%%% omsCustOrdNo : " + omsCustOrdNo);
					log.info("omsCustOrdNo" + omsCustOrdNo + "%%%% item.getLineNo() : " + item.getLineNo());
					log.info("omsCustOrdNo" + omsCustOrdNo + "%%%% omsCustOrdItem.getUnitRetail() : " + omsCustOrdItem.getUnitRetail());
					BigDecimal unitRetail = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo, item.getLineNo(), omsCustOrdItem.getUnitRetail());
					if (unitRetail.signum() < 0) {
						log.error("Unit price after discount going negative. please validate the discount");
						throw new BusinessException("UNITRETAIL_ERROR");
					}
					log.info("unitRetail : " + unitRetail);
					// fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
					fulfilOrdDtl.setUnitRetail(unitRetail);

					log.info("Unit Retail--------" + fulfilOrdDtl.getUnitRetail());
					fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); // Unit retail and unit retail currency should be both populated or both null.
				}
				fulfilOrdDtl.setTransactionUom(omsCustOrdItem.getTransactionUom());
				fulfilOrdDtl.setStandardUom(omsCustOrdItem.getStandardUom());
				fulfilOrdDtl.setSubstituteInd(omsCustOrdItem.getSubstituteAllowInd());
				fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			}

			FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
			log.info("Fetching address");
			OmsCustOrdAddress omsCustOrdAddress = null;
			try {
				omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
			} catch (Exception e) {
				log.info("Address not present in the request");
			}
			log.info("fetched addresss successflly");
			if (omsCustOrdAddress != null && null != omsCustOrdHead.getCustId() || omsCustOrdHead.getCustId().isEmpty() == false) {
				fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
			}
			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
				}
			} catch (Exception e) {
				log.info("error in deliverFirstName " + e.getMessage());
			}
			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
				}
			} catch (Exception e) {
				log.info("error in DeliverLastName " + e.getMessage());
			}
			log.info("before add 1");
			try {
				if (omsCustOrdAddress != null && null != omsCustOrdAddress.getDeliverAdd1() || omsCustOrdAddress.getDeliverAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
				}
				if (omsCustOrdAddress != null && null != omsCustOrdAddress.getDeliverAdd2() || omsCustOrdAddress.getDeliverAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());

				}
				if (omsCustOrdAddress != null && null != omsCustOrdAddress.getDeliverAdd3() || omsCustOrdAddress.getDeliverAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
				}
			} catch (Exception e) {
				log.warn("error while setting devliveryadd ", e);
			}

			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCity().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());
				}

				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverState().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState());
					if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCountry().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
						fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
					}
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverPhoneNo() != null) {
					fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverPost() != null) {
					fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());
				}
			} catch (Exception e) {
				log.warn("error while setting devliveryadd", e);
			}
			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());
				}
			} catch (Exception e) {
				log.warn("error adding bill firstname and lastname");
			}
			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd1(omsCustOrdAddress.getBillAdd1());
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd2(omsCustOrdAddress.getBillAdd2());
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd3(omsCustOrdAddress.getBillAdd3());
				}
			} catch (Exception e) {

			}
			try {
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillCity().isEmpty() == false) {
					fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());
				}
				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillState().isEmpty() == false) {
					fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState());
					if (omsCustOrdAddress != null && omsCustOrdAddress.getBillCountry().isEmpty() == false) {
						fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());
						fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry());
					}
				}

				fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());

				if (omsCustOrdAddress != null && omsCustOrdAddress.getBillPost() != null) {
					fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());
				}
			} catch (Exception e) {
				log.warn("error add bill details", e);
			}
			fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
			fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			// fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);

			for (OmsTempCoFo omstempCoFo : temp) {
				omstempCoFo.setProcessingApp("RMS");
				returnList.add(omstempCoFo);

				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			// calling RMS
			log.info("Calling RMS for reservation");
			try {
				Date d1 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "while calling RMS WS to full fill the order" + d1);

				CreateFulfilOrdColDesc colDesc = new CreateFulfilOrdColDesc();
				colDesc.setFulfilOrdColDesc(fulfilOrdColDesc);
				fulfilOrdCfmCol = OracleBaseAPIUtil.getOracleRMSClient().createFulfilOrdColDesc(colDesc).getFulfilOrdCfmCol();
				Date d2 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "after calling RMS WS to full fill the order" + (d2.getTime() - d1.getTime()) + " in milliseconds");
			} catch (javax.xml.ws.WebServiceException f) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
			}
			log.info("omsCustOrdNo" + omsCustOrdNo + "fulfilOrdCfmCol before setting " + fulfilOrdCfmCol.getFulfilOrdCfmDesc().size());

			processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);

			for (OmsTempCoFo omstempCoFo : temp) {
				omstempCoFo.setProcessingApp("SIM");
				// returnList.add(omstempCoFo);
				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			log.info("omsCustOrdNo" + omsCustOrdNo + "calling RMS and SIM for custOrdNo " + omsCustOrdHead.getCustOrderNo() + "and omsCustOrdNo " + omsCustOrdNo + "and " + "fulfillOrderNo "
					+ fulfilOrdDesc.getFulfillOrderNo());
			log.info("Calling SIM");
			// calling SIM
			try {
				Date d1 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "while calling SIM WS to full fill the order" + d1);

				CreateFulfillmentOrderDetail detail = new CreateFulfillmentOrderDetail();
				detail.setFulfilOrdColDesc(fulfilOrdColDesc);
				FulfilOrdCfmCol fulfilOrdCfmCol = OracleBaseAPIUtil.getOracleSIMClient().createFulfillmentOrderDetail(detail).getFulfilOrdCfmCol();
				Date d2 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "after calling SIM WS to full fill the order" + (d2.getTime() - d1.getTime()) + " in milliseconds");
				log.info("omsCustOrdNo " + omsCustOrdNo + "SIM " + fulfilOrdCfmCol.getCollectionSize());
			} catch (javax.xml.ws.WebServiceException f) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
			}

			log.info("Processed Object CurrentFulFilOrderNo : *****" + processedObject.getCurrentFulFilOrderNo());
			log.info("Processed Object FulfilOrdCfmCol : *****" + processedObject.getFulfilOrdCfmCol());
		}
		// Package call for Carrera ---- DC to DC changes and added ST to DC changes
		else if (temp.get(0).getSourceLocationType().equals("WH") && temp.get(0).getFulfillLocationType().equals("W")
				|| temp.get(0).getSourceLocationType().equals("ST") && temp.get(0).getFulfillLocationType().equals("W")) {
			TransferCreationResponse response = null;
			TransferCreationRequest transferRequest = null;
			String jsonReqString = null;
			// for (OmsTempCoFo omsTempCoFo : temp) {
			// OmsCustOrdItem omsCustOrdItem =
			// session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(),
			// omsTempCoFo.getLineNo());
			BigDecimal combId = temp.get(0).getCombinationId();
			log.info("***For wh to wh check comb id :" + combId);

			for (OmsTempCoFo omstempCoFo : temp) {
				log.info("Processing app is RIB Package");
				log.info("--***** Item from temp CO :" + omstempCoFo.getItem());
				log.info("--***** Item Quantity :" + omstempCoFo.getOrderQty());

				log.info("--***** Item from temp " + temp.get(0).getItem());
				log.info("--***** Item Qty from temp " + temp.get(0).getOrderQty());
				log.info("VIRTUAL WAREHOUSE : " + temp.get(0).getVirtualWH());
				// BigDecimal sourcelocId = checkphysicalwh(combId,
				// temp.get(0).getFulfillLocId());

				List<com.extra.bds.bean.carrera.TransferRequest> transferItemsList = new ArrayList<com.extra.bds.bean.carrera.TransferRequest>();
				com.extra.bds.bean.carrera.TransferRequest transferItems = new com.extra.bds.bean.carrera.TransferRequest();
				transferItems.setItem(omstempCoFo.getItem());
				log.info("****Print item : " + transferItems.getItem());
				transferItems.setQty(omstempCoFo.getOrderQty().intValue());
				log.info("****Print Qty : " + transferItems.getQty());
				transferItemsList.add(transferItems);
				transferRequest = new TransferCreationRequest();
				transferRequest.setSrc_id(temp.get(0).getVirtualWH().intValue());
				log.info("Source Location ID  : VIRTUAL : " + temp.get(0).getVirtualWH());
				transferRequest.setRef_no(omsCustOrdNo.toString().concat(temp.get(0).getLineNo().toString()));
				log.info("****Print ref number : " + omsCustOrdNo.toString().concat(temp.get(0).getLineNo().toString()));
				transferRequest.setDest_id(temp.get(0).getFulfillLocId().intValue());
				transferRequest.setCustomerItems(transferItemsList);
				transferRequest.setSrc_loc(omstempCoFo.getSourceLocId().intValue());
				transferRequest.setFul_loc(omstempCoFo.getFulfillLocId().intValue());
				transferRequest.setFul_ord_no(omstempCoFo.getFulfillOrderNo().intValue());
				transferRequest.setCust_ord_no(extCustOrdNo);

				omstempCoFo.setProcessingApp("RIB Package");
				try {
					omstempCoFo.setOrderQty(temp.get(0).getOrderQty());
					log.info("Order Qty Temp: " + omstempCoFo.getOrderQty());
					returnList.add(omstempCoFo);
				} catch (Exception e) {
					log.error("error in adding to return list" + e);
				}
				log.info("Added in return list");
				// session.mergeOmsTempCoFo(omstempCoFo);
			}

			Gson gson = new Gson();
			/* added by madhu for dc to dc transfer creation */

			// convert object to JSON string
			jsonReqString = gson.toJson(transferRequest);
			log.info("JSON Transfer Request String in format" + jsonReqString);

			response = OracleBaseAPIUtil.getCarreraClient().getOrderItemsResponse(transferRequest);
			log.info("Transfer response *********" + response.getTsf_No());

			try {
				if (response.getTsf_No() != null) {
					if (response.getTsf_No().contains("locked")) {
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("TABLE_LOCKED"));
					} else {
						// updateTransferType(response.getTsf_no(), extCustOrdNo, omsCustOrdNo);
						updateOmsCoFulfill(temp, response.getTsf_No());
						// updateTransferPubInfo(response.getTsf_no());
						// persistOrdcust(temp, omsCustOrdNo, extCustOrdNo, response.getTsf_no());
					}
				}
			} catch (Exception e) {
				log.info("Encountered an exception : " + e);
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("TABLE_LOCKED"));
			}

			processedObject.setCurrentFulFilOrderNo(temp.get(0).getFulfillOrderNo().intValue());
			processedObject.setTsf_no(response.getTsf_No());

		} else {

			log.info("omsCustOrdNo" + omsCustOrdNo + "***PO/Transfer");

			log.info("omsCustOrdNo" + omsCustOrdNo);

			log.info("***temp size=" + temp.size());
			FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();
			fulfilOrdColDesc.setCollectionSize(1);
			FulfilOrdDesc fulfilOrdDesc = null;
			fulfilOrdDesc = new FulfilOrdDesc();
			for (OmsTempCoFo omsTempCoFo : temp) {

				// fulfilOrdDesc = new FulfilOrdDesc();

				fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);

				// log.info("FulfilOder no" + String.valueOf(omsCustOrdNo.longValue() + i));
				fulfilOrdDesc.setSourceLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType.fromValue(temp.get(0).getSourceLocationType()));
				log.info("Source Loc type=" + com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType.fromValue(temp.get(0).getSourceLocationType()));
				fulfilOrdDesc.setSourceLocId(temp.get(0).getSourceLocId().longValue());
				log.info("fulfilOrdDesc.getSourceLocId()" + fulfilOrdDesc.getSourceLocId());
				fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));
				log.info("fulfilOrdDesc.getFulfillLocType()" + fulfilOrdDesc.getFulfillLocType());
				fulfilOrdDesc.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
				log.info("getFulfillLocId" + fulfilOrdDesc.getFulfillLocId());

				fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.fromValue(partialDeliveryInd));

				fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(String.valueOf(omsCustOrdHead.getDeliveryType().charAt(0))));
				// fulfilOrdDesc.setDeliveryType(fulfilOrdDesc.getDeliveryType().S);
				log.info("DeliveryType" + fulfilOrdDesc.getDeliveryType());
				// fulfilOrdDesc.setConsumerDeliveryDate(input.getConsumerDeliveryDate());
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				if (omsCustOrdHead.getConsumerDlyTime() != null) {
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {

						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
					now.setMonth(now1.get(Calendar.MONTH) + 1);
					if (temp.get(0).getSourceLocationType().equals("WH")) {
						now.setYear(now1.get(Calendar.YEAR) + 4);
						// In case of WH the comsumer delivery should be 5 years ahead to avoid picking
						// of this customer order from WMS untill and unless this is being updated from
						// Appointment Booking from RMS.
					} else {
						now.setYear(now1.get(Calendar.YEAR));
					}
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));

					fulfilOrdDesc.setConsumerDeliveryDate(now);
					log.info("getConsumerDeliveryDate" + fulfilOrdDesc.getConsumerDeliveryDate());
				} else {
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						log.warn(e.toString());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					String consumerDlyDaysToBeAdded = session.getOmsSystemParametersFindIndValue("CONSUMER_DLY_DATE", "OMS_SYSTEM_OPTION");
					log.info("======================consumerDlyDaysToBeAdded===============================+consumerDlyDaysToBeAdded");
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
					now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
					now.setMonth(now1.get(Calendar.MONTH) + 1);
					now.setYear(now1.get(Calendar.YEAR));
					// now.setDay(now1.get(Calendar.DAY_OF_MONTH)+Integer.valueOf(consumerDlyDaysToBeAdded));
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					fulfilOrdDesc.setConsumerDeliveryDate(now);
				}
				// fulfilOrdDesc.setConsumerDeliveryTime(input.getConsumerDeliveryTime());
				// fulfilOrdDesc.setDeliveryCharges(input.g);
				// fulfilOrdDesc.setDeliveryChargesCurr(value);
				fulfilOrdDesc.setComments("Application id:" + omsCustOrdHead.getApplicationId());

				OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
				// for(OmsTempCoFo omsTempCoFo:temp)
				// {

				fulfilOrdDesc.setFulfillOrderNo(omsTempCoFo.getFulfillOrderNo().toString());
				FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
				fulfilOrdDtl.setItem(omsTempCoFo.getItem());

				fulfilOrdDtl.setOrderQtySuom(omsTempCoFo.getOrderQty());
				log.info("Order qty=" + fulfilOrdDtl.getOrderQtySuom());
				fulfilOrdDtl.setTransactionUom(omsCustOrdItem.getTransactionUom());
				fulfilOrdDtl.setStandardUom(omsCustOrdItem.getStandardUom());
				log.info("setting comments for fulfilOrdDtl " + omsCustOrdItem.getComments());
				fulfilOrdDtl.setComments(omsCustOrdItem.getComments());
				log.info("setted comments for fulfilOrdDtl ");
				// Added for finding sum of discounts
				if (omsCustOrdItem.getUnitRetail().intValue() != 0) {
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					log.info("%%%% omsCustOrdNo : " + omsCustOrdNo);
					log.info("%%%% item.getLineNo() : " + omsTempCoFo.getLineNo());
					log.info("%%%% omsCustOrdItem.getUnitRetail() : " + omsCustOrdItem.getUnitRetail());
					BigDecimal unitRetail = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo, omsTempCoFo.getLineNo(), omsCustOrdItem.getUnitRetail());
					log.info("unitRetail : " + unitRetail);
					// fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
					fulfilOrdDtl.setUnitRetail(unitRetail);

					log.info("Unit Retail--------" + fulfilOrdDtl.getUnitRetail());
					fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); // Unit retail and unit retail currency should be both populated or both null.
				}
				fulfilOrdDtl.setSubstituteInd(omsCustOrdItem.getSubstituteAllowInd());

				log.info("Adding item detail with item-" + fulfilOrdDtl.getItem());
				fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			}
			log.info("after sustitute ind");
			FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
			OmsCustOrdAddress omsCustOrdAddress = null;
			try {
				omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
			} catch (Exception e) {
				log.info("Address not present");
			}
			try {
				if (omsCustOrdHead.getCustId().isEmpty() == false) {
					fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
				}
				if (omsCustOrdAddress.getDeliverFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
				}
				if (omsCustOrdAddress.getDeliverLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
				}
			} catch (Exception e) {
				log.error(" cust no or last name  not present");
			}
			try {
				if (null != omsCustOrdAddress.getDeliverAdd1() || omsCustOrdAddress.getDeliverAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
				}
				if (null != omsCustOrdAddress.getDeliverAdd2() || omsCustOrdAddress.getDeliverAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());
				}

				if (omsCustOrdAddress.getDeliverAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
				}
			} catch (Exception e) {
				log.error(" deliver add not present");
			}
			try {
				if (omsCustOrdAddress.getDeliverCity().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());

				}

				if (omsCustOrdAddress.getDeliverState().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState());
					if (omsCustOrdAddress.getDeliverCountry().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
						fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
					}
				}
				if (omsCustOrdAddress.getDeliverPhoneNo() != null) {
					fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());

				}
				if (omsCustOrdAddress.getDeliverPost().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());

				}

			} catch (Exception e) {
				log.error(" cust desc not present");
			}
			try {
				if (omsCustOrdAddress.getBillFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());

				}
				if (omsCustOrdAddress.getBillLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());

				}
			} catch (Exception e) {
				log.error(" bill name not present");
			}
			try {
				if (omsCustOrdAddress.getBillAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd1(omsCustOrdAddress.getBillAdd1());

				}
				if (omsCustOrdAddress.getBillAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd2(omsCustOrdAddress.getBillAdd2());

				}
				if (omsCustOrdAddress.getBillAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd3(omsCustOrdAddress.getBillAdd3());

				}
			} catch (Exception e) {
				log.info("bill address not present");
			}
			try {
				if (omsCustOrdAddress.getBillCity().isEmpty() == false) {
					fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());

				}
				if (omsCustOrdAddress.getBillState().isEmpty() == false) {
					fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState());

				}
				if (omsCustOrdAddress.getBillCountry().isEmpty() == false) {
					fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());

					fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry());

				}

				fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
				if (omsCustOrdAddress.getBillPost().isEmpty() == false) {
					fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());

				}
			} catch (Exception e) {
				log.error("bill detail not present");
			}
			if (omsCustOrdHead.getLockerId() != null) {
				fulfilOrdCustDesc.setDeliverPhoneticLast(omsCustOrdHead.getLockerId());
			}
			fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			// fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
			for (OmsTempCoFo omstempCoFo : temp) {
				log.info("Processing app is RMS");
				log.info("--" + omstempCoFo.getItem());
				omstempCoFo.setProcessingApp("RMS");
				try {
					returnList.add(omstempCoFo);
				} catch (Exception e) {
					log.error("error in adding to return list" + e);
				}
				log.info("Added in return list");
				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			// calling RMS
			log.info("omsCustOrdNo" + omsCustOrdNo + "Calling RMS");
			log.info("omsCustOrdNo" + omsCustOrdNo + "fulfilOrdDesc.getFulfilOrdDtl()" + fulfilOrdDesc.getFulfilOrdDtl().size());
			log.info("omsCustOrdNo" + omsCustOrdNo + "fulfilOrdColDesc" + fulfilOrdColDesc.getFulfilOrdDesc().size());
			log.info("omsCustOrdNo" + omsCustOrdNo + "calling RMS and SIM for custOrdNo " + omsCustOrdHead.getCustOrderNo() + "and omsCustOrdNo " + omsCustOrdNo + "and " + "fulfillOrderNo "
					+ fulfilOrdDesc.getFulfillOrderNo());
			try {
				Date date1 = new Date();

				CreateFulfilOrdColDesc colDesc = new CreateFulfilOrdColDesc();
				colDesc.setFulfilOrdColDesc(fulfilOrdColDesc);
				fulfilOrdCfmCol = OracleBaseAPIUtil.getOracleRMSClient().createFulfilOrdColDesc(colDesc).getFulfilOrdCfmCol();
				Date date2 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + " Time consumed in calling RMS" + (date2.getTime() - date1.getTime()) + "milli seconds");
				log.info("omsCustOrdNo" + omsCustOrdNo + "fulfilOrdCfmCol before setting " + fulfilOrdCfmCol.getFulfilOrdCfmDesc().size());
				processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);
				processedObject.setCurrentFulFilOrderNo(new BigDecimal(fulfilOrdDesc.getFulfillOrderNo()).intValue());
				log.info("omsCustOrdNo" + omsCustOrdNo + "currentFulFilOrdNo " + processedObject.getCurrentFulFilOrderNo());
			} catch (javax.xml.ws.WebServiceException f) {
				f.printStackTrace();
				log.info("RMS Webservices are down");
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
			}
			// catch (Exception e) {
			// log.info("Exception occurred while calling RMS fulfillment web service : " +
			// e.getMessage());
			// e.printStackTrace();
			// throw e;
			// }
			log.info("call successful");

		}
		processedObject.setOmsTempCoFoList(returnList);

		for (int i = 0; i < returnList.size(); i++) {
			log.info(returnList.get(i));
		}
		return processedObject;
	}

	public String processWebserviceResponse(FulfilOrdCfmCol fulfilOrdCfmCol, ArrayList<OmsTempCoFo> temp) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside processWebserviceResponse");
		log.info("=====================================================");
		log.info("fulfilOrdCfmCol " + fulfilOrdCfmCol.getCollectionSize());
		String responseStatus = "";
		for (OmsTempCoFo omsTempCoFo : temp) {
			OmsCustOrdItem omscustOrdItem = session.getOmsCustOrdItemFindByItem(omsTempCoFo.getOmsCustOrdNo(), omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
			if (fulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty() && fulfilOrdCfmCol.getCollectionSize() == 0) {
				// confirmed
				responseStatus = "C";
				log.info("Confirmed");
				// confirmType = fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getConfirmType();
				omsTempCoFo.setRmsResponseCode("C");
				omsTempCoFo.setStatus("PROCESSED");
				omsTempCoFo.setFoConfQty(omsTempCoFo.getOrderQty());
				// session.mergeOmsTempCoFo(omsTempCoFo);
				omscustOrdItem.setStatus("S");
				session.mergeOmsCustOrdItem(omscustOrdItem);
			} else {
				confirmType = fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getConfirmType();
				if (confirmType != null && confirmType.value().equals("X")) {
					log.info("Confirm type is X");
					responseStatus = "X";
					omsTempCoFo.setRmsResponseCode("X");
					// session.mergeOmsTempCoFo(omsTempCoFo);
					omscustOrdItem.setStatus("X");
					session.mergeOmsCustOrdItem(omscustOrdItem);

				} else if (confirmType != null && confirmType.value().equals("P")) {
					log.info("inside confirmType P condition omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
					log.info("Confirm type is P");

					responseStatus = "P";
					if (omsTempCoFo.getRmsResponseCode() == null || omsTempCoFo.getRmsResponseCode().isEmpty()) {

						omsTempCoFo.setRmsResponseCode("P");
					}

					omscustOrdItem.setStatus("P");
					session.mergeOmsCustOrdItem(omscustOrdItem);
				} else {
					// received validation error from RMS
					responseStatus = "E";
					log.info("Validation error");
					omsTempCoFo.setRmsResponseCode("E");
					omsTempCoFo.setStatus("F");
					// session.mergeOmsTempCoFo(omsTempCoFo);

					omscustOrdItem.setStatus("F");
					session.mergeOmsCustOrdItem(omscustOrdItem);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Validation error"));
				}

			}

		}
		return responseStatus;
	}

	public BigDecimal getAvailableQtyFromOmsBackOrderDtl(BigDecimal location, String item) throws SOAPException

	{
		BigDecimal qty = BigDecimal.ZERO;
		BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();
		try {
			qty = backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(location, item);
			if (qty == null || qty.intValue() < 0) {
				qty = BigDecimal.ZERO;
			}
		} catch (Exception e) {
			qty = BigDecimal.ZERO;
		}
		log.info(" unfulfilled Qty from BO for the item " + item + " and location " + location + "is " + qty);
		return qty;
	}

	public long callSIMStoreInventory(String item, BigDecimal nextLoc, String applicationId) throws com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, SOAPException {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		long SOH = 0;
		try {
			// Calling of lookup inventory web service is removed and added code to call sim
			// inventory view
			// Below Function returns threshold inventory
			StoreInventory storeInventory = new StoreInventory();
			SOH = storeInventory.getStockForAnItemAndLocationFromSIM(item, nextLoc, applicationId);
			log.info("SOH value from view for an item " + item + " loc " + nextLoc + " and for an applicationId " + applicationId + " is " + SOH);
			SOH = SOH - getAvailableQtyFromOmsBackOrderDtl(nextLoc, item).intValue();
			log.info("SOH value after calculating from BO table for an item " + item + " loc " + nextLoc + "is " + SOH);
			// Bug 2603
			log.info("Before sohCalculation for an item " + item + " loc " + nextLoc + "is " + SOH);

			SOH = oMSUtilCommons.sohCalculation(new BigDecimal(SOH), item, nextLoc);
			log.info("After sohCalculation for an item " + item + " loc " + nextLoc + "is " + SOH);
		} catch (IndexOutOfBoundsException in) {
			SOH = 0;
		}
		return SOH;
	}

	public void callRMSBackorderWS(String item, BigDecimal backOrdQty, long location, String locType, String unitOfMeasure, BigDecimal channelId)
			throws com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("inside backorder");
		InvBackOrdColDesc invBackOrdColDesc = new InvBackOrdColDesc();
		invBackOrdColDesc.setCollectionSize(1);
		InvBackOrdDesc invBackOrdDesc = new InvBackOrdDesc();
		invBackOrdDesc.setItem(item);
		invBackOrdDesc.setBackorderQty(backOrdQty);
		invBackOrdDesc.setLocType(LocType.fromValue(locType));
		invBackOrdDesc.setLocation(location);
		if (channelId.intValue() > 0) {
			invBackOrdDesc.setChannelId(channelId.intValue());
			log.info("Setting channel id to" + channelId + "for calling back order WS");
		}
		invBackOrdDesc.setUnitOfMeasure(unitOfMeasure);
		invBackOrdColDesc.getInvBackOrdDesc().add(invBackOrdDesc);

		CreateInvBackOrdColDesc colDesc = new CreateInvBackOrdColDesc();
		colDesc.setInvBackOrdColDesc(invBackOrdColDesc);
		OracleBaseAPIUtil.getOracleRMSClient().createInvBackOrdColDesc(colDesc);
	}

	public void adjustInventoryByItemLocation(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap, String customerOrderNo) throws SOAPException {
		log.info("Calling SIM invetory adustment in case of reserve");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
			for (OmsTempCoFo omsTempCoFo : list) {

				if (omsTempCoFo.getSourceLocationType().equals("SU") == false && omsTempCoFo.getSourceLocationType().equals("WH") == false) {

					try {
						log.info("Calling SIM inventory adjustment ");
						StrAdjModVo strAdjModVo = new StrAdjModVo();
						StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
						// if(omsCustOrdItem.getBackorderInd().equals("N"))
						// {
						strAdjItemMod.setItemId(omsTempCoFo.getItem());
						strAdjItemMod.setReasonId(Integer.parseInt(session.getOmsSystemParametersFindIndValue("INV_RESV_CODE", "OMS_INV_ADJ_RSN_CODE")));
						log.info("reason Id " + Integer.parseInt(session.getOmsSystemParametersFindIndValue("INV_RESV_CODE", "OMS_INV_ADJ_RSN_CODE")));
						strAdjItemMod.setQuantity(omsTempCoFo.getOrderQty());
						log.info("quantity " + omsTempCoFo.getOrderQty());
						strAdjItemMod.setCaseSize(new BigDecimal(1));
						strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
						log.info("added  to strAdjItemMod");
						strAdjModVo.setStoreId(omsTempCoFo.getSourceLocId().longValue());
						log.info("storeId " + omsTempCoFo.getSourceLocId());
						strAdjModVo.setComments("Reserved for customer order id :" + customerOrderNo);
						log.info("calling saveAndConfirmInventoryAdjustment");
						Date d1 = new Date();
						log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() + "while calling saveAndConfirmInventoryAdjustment" + d1.getTime() + " in milliseconds");

						SaveAndConfirmInventoryAdjustment adjustment = new SaveAndConfirmInventoryAdjustment();
						adjustment.setStrAdjModVo(strAdjModVo);
						OracleBaseAPIUtil.getOracleSIMClient().saveAndConfirmInventoryAdjustment(adjustment);
						Date d2 = new Date();
						log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() + "after calling saveAndConfirmInventoryAdjustment " + (d2.getTime() - d1.getTime()) + " in milliseconds");
						log.info("Call to SIM invetory adustment in case of reserve success");
						// }
						//
						// return strAdjRef.getAdjustmentId();
					} catch (Exception e) {
						log.error(" --> Exception ST: " + e);
						throw new SOAPException(e.getMessage());
					}

				}
			} // else end
		} // for ends

	}

	public String approveTransfers(BigDecimal tsfNo, BigDecimal sourceLoc) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException, SOAPException {
		String status = "F";
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			try {
				String waitTime = session.getOmsSystemParametersFindIndValue("TSF_APPROVAL_WAIT_TIME", "OMS_SYSTEM_OPTION");
				log.info("waitTime" + waitTime);
				Thread.sleep(Long.valueOf(waitTime));
			} catch (InterruptedException e) {
				log.info("Exception in the wait time");
			}
			StsTsfHdrCriVo stsTsfHdrCriVo = new StsTsfHdrCriVo();
			stsTsfHdrCriVo.setStoreId(sourceLoc.longValue());
			stsTsfHdrCriVo.setStatus(StsTsfCriStatus.ACTIVE);
			stsTsfHdrCriVo.setExternalId(tsfNo.longValue());
			log.info("calling lookupTransferHeader");

			LookupTransferHeader header = new LookupTransferHeader();
			header.setStsTsfHdrCriVo(stsTsfHdrCriVo);
			StsTsfHdrColDesc stsTsfHdrColDesc = OracleBaseAPIUtil.getOracleSIMClient().lookupTransferHeader(header).getStsTsfHdrColDesc();
			log.info("stsTsfHdrColDesc.getCollectionSize()" + stsTsfHdrColDesc.getCollectionSize() + "sourceLoc=" + sourceLoc + "tsfNo=" + tsfNo);
			if (stsTsfHdrColDesc.getCollectionSize() != 0) {
				List<StsTsfHdrDesc> stsTsfHdrDescList = stsTsfHdrColDesc.getStsTsfHdrDesc();
				StsTsfHdrDesc StsTsfHdrDesc = stsTsfHdrDescList.get(0);
				StsTsfRef stsTsfRef = new StsTsfRef();
				stsTsfRef.setTransferId(StsTsfHdrDesc.getTransferId());
				stsTsfRef.setStoreId(StsTsfHdrDesc.getSendingStoreId());
				log.info("calling readTransferDetail");

				ReadTransferDetail detail = new ReadTransferDetail();
				detail.setStsTsfRef(stsTsfRef);
				StsTsfDesc stsTsfDesc = OracleBaseAPIUtil.getOracleSIMClient().readTransferDetail(detail).getStsTsfDesc();

				StsTsfApvModVo stsTsfApvModVo = new StsTsfApvModVo();
				stsTsfApvModVo.setTransferId(stsTsfDesc.getTransferId());
				List<StsTsfApvItmMod> stsTsfApvItmLsit = stsTsfApvModVo.getStsTsfApvItmMod();

				for (StsTsfItm stsTsfItm : stsTsfDesc.getStsTsfItm()) {
					StsTsfApvItmMod stsTsfApvItmMod = new StsTsfApvItmMod();
					stsTsfApvItmMod.setLineId(stsTsfItm.getLineId());
					stsTsfApvItmMod.setApprovedQuantity(stsTsfItm.getRequestedQuantity());
					stsTsfApvItmLsit.add(stsTsfApvItmMod);
					stsTsfApvModVo.getStsTsfApvItmMod().add(stsTsfApvItmMod);
				}
				log.info("calling savePendingTransferRequest");

				SavePendingTransferRequest request = new SavePendingTransferRequest();
				request.setStsTsfApvModVo(stsTsfApvModVo);
				OracleBaseAPIUtil.getOracleSIMClient().savePendingTransferRequest(request);
				log.info("calling approveTransfer");

				ApproveTransfer approveTransfer = new ApproveTransfer();
				approveTransfer.setStsTsfRef(stsTsfRef);
				InvocationSuccess invocationSuccess1 = OracleBaseAPIUtil.getOracleSIMClient().approveTransfer(approveTransfer).getInvocationSuccess();
				if (invocationSuccess1.getSuccessMessage().equals("Service Operation Complete")) {
					status = "A";
					log.info("Message Success Transfer approved");
				} else {
					log.info("transfer not approved");
				}

			}
		} catch (Exception e) {
			log.info("Failed in approveTransfer method");

		}
		return status;
	}

	private static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTimeInMillis(date.getTime());
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
			log.info("------------------------");
		}
		log.info("xmlCalendar-----------" + xmlCalendar);
		return xmlCalendar;
	}

	public OmsOrderStatusUpdateHeader getOmsOrderStatusUpdateHeader(CustomerOrder input, BigDecimal omsCustOrdNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrderStatusUpdateHeader headerStatusObject = new OmsOrderStatusUpdateHeader();
		OmsOrderDetailStatus OmsOrderDetailStatusObject = null;
		ArrayOfOmsOrderStatusUpdateDetail arrayOfOmsOrderStatusUpdateDetail = new ArrayOfOmsOrderStatusUpdateDetail();

		headerStatusObject.setApplicationId(input.getApplicationId());
		headerStatusObject.setEntityId(input.getEntityId());
		headerStatusObject.setOrderId(input.getCustomerOrderNo());
		headerStatusObject.setOmsOrderId(omsCustOrdNo + "");
		headerStatusObject.setDeliveryDate(toXMLGregorianCalendar(input.getConsumerDeliveryDate().toGregorianCalendar().getTime()));
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		Timestamp headerlastupdateTime = omsCustOrdHead.getLastUpdateDatetime();
		if (null == headerlastupdateTime) {
			headerlastupdateTime = new Timestamp(new Date().getTime());
		}
		headerStatusObject.setUpdateDate(toXMLGregorianCalendar(new Date(headerlastupdateTime.getTime())));
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);

		for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
			OmsOrderDetailStatusObject = new OmsOrderDetailStatus();

			OmsOrderDetailStatusObject.setProductSku(omsCoFulfillDetail.getItem());
			OmsOrderDetailStatusObject.setQuantity(omsCoFulfillDetail.getFulfillConfQty().intValue());
			OmsOrderDetailStatusObject.setOrderDetailId(omsCoFulfillDetail.getLineNo().longValue());

			OmsOrderDetailStatusObject.setSourceId(omsCoFulfillDetail.getSourceLoc().intValue());
			OmsOrderDetailStatusObject.setSourceType(omsCoFulfillDetail.getSourceLocType());
			OmsOrderDetailStatusObject.setFulfillId(omsCoFulfillDetail.getFulfillLoc().intValue());
			OmsOrderDetailStatusObject.setFulfillType(omsCoFulfillDetail.getFulfillLocType());

			OmsOrderDetailStatusObject.setEventComment("Order Created By E-commerce");
			OmsOrderDetailStatusObject.setEventId("CRE");
			OmsOrderDetailStatusObject.setEventReferenceId(omsCoFulfillDetail.getOmsCustOrdNo().longValue());

			OmsOrderDetailStatusObject.setUpdateDate(toXMLGregorianCalendar(new Timestamp(new Date().getTime())));
			arrayOfOmsOrderStatusUpdateDetail.getOrderDetailStatus().add(OmsOrderDetailStatusObject);
		}
		headerStatusObject.setOrderDetailStatuses(arrayOfOmsOrderStatusUpdateDetail);

		return headerStatusObject;

	}

	public static Connection connectJDBCForOracleDriver() {
		Connection SQLcon = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLcon = DriverManager.getConnection("jdbc:oracle:thin:@exvm-qarmsdb02.extrastores.com:1521/RMSQA.extrastores.com", "RMS14", "retQAtrex123");
		} catch (SQLException e) {
			log.info("Error : " + e.getMessage());
		} catch (Exception e) {
			log.info("Error : " + e.getMessage());
		}
		return SQLcon;
	} // End of method

	private void updateOmsCoFulfill(ArrayList<OmsTempCoFo> omsTempCoFo, String transferNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCoFulfillDetail omsCoFulfillDetail = null;
		omsCoFulfillDetail = new OmsCoFulfillDetail();
		omsCoFulfillDetail.setFulfillOrderNo(omsTempCoFo.get(0).getFulfillOrderNo());
		omsCoFulfillDetail.setFulfillLocType(omsTempCoFo.get(0).getFulfillLocationType());
		omsCoFulfillDetail.setFulfillLoc(omsTempCoFo.get(0).getFulfillLocId());
		omsCoFulfillDetail.setLineNo(omsTempCoFo.get(0).getLineNo());
		omsCoFulfillDetail.setItem(omsTempCoFo.get(0).getItem());
		omsCoFulfillDetail.setOmsCustOrdNo(omsTempCoFo.get(0).getOmsCustOrdNo());
		omsCoFulfillDetail.setSourceLoc(omsTempCoFo.get(0).getSourceLocId());
		omsCoFulfillDetail.setSourceLocType(omsTempCoFo.get(0).getSourceLocationType());
		omsCoFulfillDetail.setFulfillReqQty(omsTempCoFo.get(0).getOrderQty());
		omsCoFulfillDetail.setFulfillConfQty(omsTempCoFo.get(0).getOrderQty());
		omsCoFulfillDetail.setCombinationId(omsTempCoFo.get(0).getCombinationId());
		omsCoFulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
		omsCoFulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
		omsCoFulfillDetail.setFulfillConfQty(omsTempCoFo.get(0).getOrderQty());
		omsCoFulfillDetail.setFulfillStatus("C");
		omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
		try {
			log.info("Getting Transfer No for Reservation");
			log.info("extCustOrdNo " + extCustOrdNo);
			log.info("omsCoFulfillDetail.getFulfillOrderNo() " + omsCoFulfillDetail.getFulfillOrderNo());
			log.info("omsCoFulfillDetail.getSourceLoc() " + omsCoFulfillDetail.getSourceLoc());
			log.info("omsCoFulfillDetail.getFulfillLoc() " + omsCoFulfillDetail.getFulfillLoc());

			log.info("----------------WareHouse Transfer-------------------------------");
			omsCoFulfillDetail.setTsfNo(new BigDecimal(transferNo));
			omsCoFulfillDetail.setTsfApprovalStatus("A");
		} catch (Exception e) {
			log.error("---------");
		}
		Date d1 = new Date();
		session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
		Date d2 = new Date();
		log.info("omsCustOrdNo" + omsTempCoFo.get(0).getOmsCustOrdNo() + "after persisting into omsCoFulfillDetail" + (d2.getTime() - d1.getTime()) + " in milliseconds");
	}
}
