package com.logicinfo.oms.beans;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.bds.bean.carrera.TransferCreationResponse;
import com.extra.bds.bean.carrera.TransferRequest;
import com.extra.oms.service.client.ICarreraClient;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoCancelHead;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFoCancel;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.ejb.OmsWSPublishData;
import com.logicinfo.oms.util.ArrayOfOmsOrderStatusUpdateDetail;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderDetailStatus;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.logicinfo.oms.util.OracleBaseAPIUtil;
import com.logicinfo.oms.util.StoreInventory;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;
import com.oracle.retail.integration.base.bo.custorditmpkvo.v1.CustOrdItmPkVo;
import com.oracle.retail.integration.base.bo.custorditmrtvo.v1.CustOrdItmRtVo;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreItmMod;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreModVo;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
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
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriStatus;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriType;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
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
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.LookupFulfillmentOrderDeliveryHeaders;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ConfirmReversePick;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.CreateReversePick;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.LookupFulfillmentOrderHeaders;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.Ping;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ReadFulfillmentOrderDetail;
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
import retail.siebel.com.integration.OmsorderupdatebpelprocessClientEp;
import retail.siebel.com.integration.SiebelOrderFeedWebservice;
import retail.siebel.com.integration.SiebelStatusUpdateDetails;
import retail.siebel.com.integration.SiebelStatusUpdateInfo;
import retail.siebel.com.integration.SiebelStatusUpdateResponse;
import retail.siebel.com.integration.SiebelStatusUpdateWebService;
import retail.siebel.com.integration.TenderDetails;

public class InterfacePersistence {

	public InterfacePersistence() {
		super();
	}

	private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName());
	FulfilOrdCfmCol fulfilOrdCfmCol = null;
	ConfirmType confirmType = null;
	String extCustOrdNo = "";

	public long callSIMStoreInventory(String item, BigDecimal nextLoc, String applicationId) throws com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, SOAPException {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		long SOH = 0;
		try {
			StoreInventory storeInventoryforPos = new StoreInventory();
			SOH = storeInventoryforPos.getStockForAnItemAndLocationFromSIM(item, nextLoc, applicationId);
			log.info("Before Calculation : " + SOH);
			SOH = oMSUtilCommons.sohCalculation(new BigDecimal(SOH), item, nextLoc);
			log.info("After Calculation : " + SOH);
		} catch (IndexOutOfBoundsException e) {
			SOH = 0;
		}
		return SOH;
	}

	public StrFordHdrColDesc callSIMLookupFulfillmentOrderHeaders(OmsCoFulfillDetail fulfilDetail, long fulilmentOrdNo, String extCustOrdNo)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		try {
			StrFordHdrCriVo strFordHdrCriVo = new StrFordHdrCriVo();
			strFordHdrCriVo.setItemId(fulfilDetail.getItem());
			log.info("item: " + strFordHdrCriVo.getItemId());
			StrFordCriStatus strFordCriStatus = StrFordCriStatus.fromValue("NO_VALUE");
			strFordHdrCriVo.setOrderType(StrFordCriType.fromValue("WEB_ORDER"));
			strFordHdrCriVo.setStoreId(fulfilDetail.getFulfillLoc().longValue());
			log.info("store id is" + strFordHdrCriVo.getStoreId());
			strFordHdrCriVo.setExtFulfillmentOrderId(String.valueOf(fulilmentOrdNo));
			log.info("ExtFulfillmentOrderId" + strFordHdrCriVo.getExtFulfillmentOrderId());
			strFordHdrCriVo.setCustomerOrderId(extCustOrdNo);
			strFordHdrCriVo.setStatus(strFordCriStatus);
			
			LookupFulfillmentOrderHeaders headers = new LookupFulfillmentOrderHeaders();
			headers.setStrFordHdrCriVo(strFordHdrCriVo);
			StrFordHdrColDesc strFordHdrColDesc = OracleBaseAPIUtil.getOracleSIMClient().lookupFulfillmentOrderHeaders(headers).getStrFordHdrColDesc();
			return strFordHdrColDesc;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice lookupFulfillmentOrderHeaders");
			throw new SOAPException("Error in calling sim webservice lookupFulfillmentOrderHeaders.");
		}
	}

	public StrFordDesc callSIMReadFulfillmentOrderDetail(StrFordHdrColDesc strFordHdrColDesc)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		try {
			long int_fulfillment_order_id = strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId();
			StrFordRef strFordRef = new StrFordRef();
			strFordRef.setIntFulfillmentOrderId(int_fulfillment_order_id);
			
			ReadFulfillmentOrderDetail detail = new ReadFulfillmentOrderDetail();
			detail.setStrFordRef(strFordRef);
			StrFordDesc strFordDesc = OracleBaseAPIUtil.getOracleSIMClient().readFulfillmentOrderDetail(detail).getStrFordDesc();
			return strFordDesc;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice readFulfillmentOrderDetail");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice readFulfillmentOrderDetail");
		}
	}

	public boolean callSIMPingMethod() {
		boolean simvalue = false;
		String simResponse = null;
		log.info("inside callSIMPingMethod ");
		Ping ping = new Ping();
		ping.setArg0("SIM");
		simResponse = OracleBaseAPIUtil.getOracleSIMClient().ping(ping).getReturn();
		log.info("simResponse " + simResponse);
		if (simResponse.equalsIgnoreCase("Service(StoreFulfillmentOrderService) pinged with data(SIM).")) {
			simvalue = true;
		}
		return simvalue;
	}

	public boolean callRMSPingMethod() {
		boolean rmsvalue = false;
		String rmsResponse = null;
		log.info("inside callRMSPingMethod ");
		com.oracle.retail.rms.integration.services.fulfillorderservice.v1.Ping ping = new com.oracle.retail.rms.integration.services.fulfillorderservice.v1.Ping();
		ping.setArg0("RMS");
		rmsResponse = OracleBaseAPIUtil.getOracleRMSClient().ping(ping).getReturn();
		log.info("rmsResponse " + rmsResponse);
		if (rmsResponse.equalsIgnoreCase("Service(FulfillOrderService) pinged with data(RMS).")) {
			rmsvalue = true;
		}
		return rmsvalue;
	}

	public void callSIMCancelFulfillmentOrderDetail(CustOrderPicVo input, OmsCoFulfillDetail fulfilDetail, String item, long fulilmentOrdNo, OmsCustOrdItem omsCustOrdItem, long actual_reserved_qty,
			BigDecimal qtyToBeCanceledinSIM, String extCustOrdNo) throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		try {
			Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
			log.info("Condition 1");
			FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
			fulfilOrdColRef.value = fulfilOrdColRef1;
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
			fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
			fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
			fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(fulfilDetail.getFulfillLocType()));
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue()); // **
			fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(fulfilDetail.getSourceLocType())); // **
			fulfilOrdRef.setFulfillOrderNo(String.valueOf(fulilmentOrdNo));
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			/*
			 * if(item.getCancelQtySuom().longValue()<=actual_reserved_qty) {
			 * fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());
			 * 
			 * } else if(item.getCancelQtySuom().longValue()>actual_reserved_qty) {
			 * fulfilOrdDtlRef.setCancelQtySuom(new BigDecimal(actual_reserved_qty)); }
			 */
			fulfilOrdDtlRef.setCancelQtySuom(qtyToBeCanceledinSIM);
			fulfilOrdDtlRef.setItem(item);
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
			fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
			log.info("calling sim webservice method cancelFulfillmentOrderDetail() when pickedQty==delievedQty");
			CancelFulfillmentOrderDetail detail = new CancelFulfillmentOrderDetail();
			detail.setFulfilOrdColRef(fulfilOrdColRef1);
			OracleBaseAPIUtil.getOracleSIMClient().cancelFulfillmentOrderDetail(detail).getFulfilOrdColRef();
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice cancelFulfillmentOrderDetail");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice cancelFulfillmentOrderDetail");
		}
	}

	public InvocationSuccess callSIMConfirmReversePick(ForpRef forpRef) throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		try {
			ConfirmReversePick pick = new ConfirmReversePick();
			pick.setForpRef(forpRef);
			InvocationSuccess invocationSuccess = OracleBaseAPIUtil.getOracleSIMClient().confirmReversePick(pick).getInvocationSuccess();
			return invocationSuccess;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice confirmReversePick", e);
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice confirmReversePick");
		}
	}

	public ForpRef callSIMCreateReversePick(long lineId, long reverseQty, StrFordHdrColDesc strFordHdrColDesc)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		try {
			ForpCreModVo forpCreModVo = new ForpCreModVo();
			ForpCreItmMod forpCreItmMod = new ForpCreItmMod();
			forpCreItmMod.setFulfillmentOrderLineId(lineId);
			forpCreItmMod.setQuantity(new BigDecimal(reverseQty));
			forpCreModVo.setIntFulfillmentOrderId(strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
			forpCreModVo.getForpCreItmMod().add(forpCreItmMod);
			// log.info("Reverse qty in method createReversePick="+to_be_picekd);
			
			CreateReversePick pick = new CreateReversePick();
			pick.setForpCreModVo(forpCreModVo);
			ForpRef forpRef = OracleBaseAPIUtil.getOracleSIMClient().createReversePick(pick).getForpRef();
			return forpRef;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice createReversePick");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice createReversePick");
		}
	}

	public void callRMSCancelFulfilOrdColRef(CustOrderPicVo input, OmsCoFulfillDetail fulfilDetail, String item, long L_fo_open_qty, OmsCustOrdItem omsCustOrdItem, String extCustOrdNo)
			throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException, SOAPException {
		String sourceLocId = null;
		String sourceLocType = null;
		FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
		fulfilOrdColRef.setCollectionSize(1); // collection size
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
		fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
		if (fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) != 0) {
			sourceLocId = fulfilDetail.getSourceLoc().toString();
			fulfilOrdRef.getSourceLocType();
			sourceLocType = SourceLocType.fromValue(fulfilDetail.getSourceLocType()).toString();
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue()); // **
			fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(fulfilDetail.getSourceLocType()));
		}
		fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(fulfilDetail.getFulfillLocType()));
		fulfilOrdRef.setFulfillOrderNo(String.valueOf(fulfilDetail.getFulfillOrderNo()));
		log.info("fulfill ord no sent to rms:" + fulfilDetail.getFulfillOrderNo() + "for item:" + item + "cust_ord_no:" + input.getCustomerOrderId());
		// fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue());
		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(new BigDecimal(L_fo_open_qty));
		fulfilOrdDtlRef.setItem(item);
		// fulfilOrdDtlRef.setRefItem(value);
		fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
		fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
		try {
			
			CancelFulfilOrdColRef colRef = new CancelFulfilOrdColRef();
			colRef.setFulfilOrdColRef(fulfilOrdColRef);
			OracleBaseAPIUtil.getOracleRMSClient().cancelFulfilOrdColRef(colRef);
		} catch (Exception e) {
			log.error("-->Unable to call RMS web service cancelFulfilOrdColRef");
			// Added code for 3260 ITEM_LOC_SOH table lock
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdItem.getOmsCustOrdNo());
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL RMS Cancellation  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(extCustOrdNo);
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_FULFIL_ORDER");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1\" xmlns:v13=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1\">\n"
					+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:cancelFulfilOrdColRef>";
			String xml = null;
			if (sourceLocId != null && sourceLocType != null) {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + extCustOrdNo + "</v12:customer_order_no>\n"
						+ "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:source_loc_type>" + sourceLocType + "</v12:source_loc_type>\n"
						+ "<v12:source_loc_id>" + sourceLocId + "</v12:source_loc_id>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n"
						+ "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item + "</v13:item>\n"
						+ "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n" + "<v13:transaction_uom>EA</v13:transaction_uom>\n"
						+ "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
			} else {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + extCustOrdNo + "</v12:customer_order_no>\n"
						+ "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType()
						+ "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item
						+ "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n"
						+ "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
			}
			String end = "</v1:cancelFulfilOrdColRef>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
			omsRepublishData.setXmlMsg(start + xml + end);
			log.info("XML " + start + xml + end);
			try {
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisting in omsRepublish data for RMS Cancellation");
			} catch (Exception f) {
				log.error("persisting in omsRepublish data failed" + f.getMessage());
			}
		}
		log.info("--> RMS cancelFulfilOrdColRef success");
	}

	public void callRMSCancelFulfilOrdColRefforApprovedTransfers(OmsCoFulfillDetail fulfilDetail, BigDecimal L_fo_open_qty, String item, OmsCustOrdItem omsCustOrdItem, String extCustOrdNo)
			throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException, SOAPException {
		String sourceLocId = null;
		String sourceLocType = null;
		FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
		fulfilOrdColRef.setCollectionSize(1); // collection size
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
		fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
		if (fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) != 0) {
			sourceLocId = fulfilDetail.getSourceLoc().toString();
			sourceLocType = fulfilDetail.getSourceLocType();
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue()); // **
			fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(fulfilDetail.getSourceLocType()));
		}
		fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(fulfilDetail.getFulfillLocType()));
		fulfilOrdRef.setFulfillOrderNo(String.valueOf(fulfilDetail.getFulfillOrderNo()));
		log.info("fulfill ord no sent to rms:" + fulfilDetail.getFulfillOrderNo() + "for item:" + fulfilDetail.getItem() + "cust_ord_no:" + extCustOrdNo);
		// fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue());
		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(L_fo_open_qty);
		fulfilOrdDtlRef.setItem(item);
		// fulfilOrdDtlRef.setRefItem(value);
		fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
		fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
		try {
			
			CancelFulfilOrdColRef colRef = new CancelFulfilOrdColRef();
			colRef.setFulfilOrdColRef(fulfilOrdColRef);
			OracleBaseAPIUtil.getOracleRMSClient().cancelFulfilOrdColRef(colRef);
		} catch (Exception e) {
			log.error("-->Unable to call RMS web service cancelFulfilOrdColRef");
			// Added code for 3244 ITEM_LOC_SOH table lock
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdItem.getOmsCustOrdNo());
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL RMS Cancellation  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(extCustOrdNo);
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_FULFIL_ORDER");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1\" xmlns:v13=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1\">\n"
					+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:cancelFulfilOrdColRef>";
			String xml = null;
			if (sourceLocId != null && sourceLocType != null) {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + extCustOrdNo + "</v12:customer_order_no>\n"
						+ "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:source_loc_type>" + sourceLocType + "</v12:source_loc_type>\n"
						+ "<v12:source_loc_id>" + sourceLocId + "</v12:source_loc_id>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n"
						+ "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item + "</v13:item>\n"
						+ "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n" + "<v13:transaction_uom>EA</v13:transaction_uom>\n"
						+ "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
			} else {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + extCustOrdNo + "</v12:customer_order_no>\n"
						+ "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType()
						+ "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item
						+ "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n"
						+ "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
			}
			String end = "</v1:cancelFulfilOrdColRef>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
			omsRepublishData.setXmlMsg(start + xml + end);
			log.info("XML " + start + xml + end);
			try {
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisting in omsRepublish data for RMS Cancellation");
			} catch (Exception f) {
				log.error("persisting in omsRepublish data failed" + f.getMessage());
			}
		}
		log.info("--> RMS cancelFulfilOrdColRef success");
	}

	public ProcessedObject callWebservices(BigDecimal omsCustOrdNo, ArrayList<OmsTempCoFo> temp, BigDecimal omsOrposCustOrderId, long intiateLocId, String shipToStore)
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
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside callWebservices");
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);

		ProcessedObject processedObject = new ProcessedObject();
		// ArrayList<OmsTempCoFo> returnList= processedObject.getOmsTempCoFoList();
		ArrayList<OmsTempCoFo> returnList = new ArrayList<OmsTempCoFo>();
		log.info("omsCustOrdNo " + omsCustOrdNo + "processed object" + returnList.size());
		extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
		int subNo = Integer.parseInt(omsCustOrdHead.getSubCustOrderNo().trim());
		log.info("omsCustOrdNo " + omsCustOrdNo + "=====================SubNo=============" + subNo);
		if (subNo == 1) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside subOrderN0");
			extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim();
		} else if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "=============inside omsCustOrdHead.getSubCustOrderNo().trim().length()==========");
			if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "0" + omsCustOrdHead.getSubCustOrderNo().trim();
			} else {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "00" + omsCustOrdHead.getSubCustOrderNo().trim();
			}
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Customer Order no passed to SIM/RMA is " + extCustOrdNo);
		String partialDeliveryInd = session.getOmsSystemParametersFindIndValue("OMS_PARTIAL_DLV_IND", "OMS_SYSTEM_OPTION");
		// FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();
		// FulfilOrdDesc fulfilOrdDesc=new FulfilOrdDesc();
		log.info("omsCustOrdNo " + omsCustOrdNo + "Item Id --->" + temp.get(0).getItem());
		log.info("omsCustOrdNo " + omsCustOrdNo + "the source loc id --->" + temp.get(0).getSourceLocId());
		log.info("omsCustOrdNo " + omsCustOrdNo + "Fulfill loc id------>" + temp.get(0).getFulfillLocId());
		// log.info("omsCustOrdNo "+omsCustOrdNo +"Quantity
		// Confirmed------>"+temp.get(0).getFoConfQty());
		if (temp.get(0).getSourceLocId().compareTo(temp.get(0).getFulfillLocId()) == 0) {
			// souce loc type=null then reservation
			log.info("omsCustOrdNo " + omsCustOrdNo + "reservation condition");
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo);
			FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();
			FulfilOrdDesc fulfilOrdDesc = null;
			fulfilOrdColDesc.setCollectionSize(1);
			fulfilOrdDesc = new FulfilOrdDesc();
			OmsOrposCustOrdItm omsOrposCustOrdItm = null;
			// looping thru the items over OmsTempCoFo
			for (OmsTempCoFo item : temp) {
				// creation of object should always be inside loop otherwise it wont loop the
				// elements properly for fulfillment

				fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);
				fulfilOrdDesc.setFulfillOrderNo(item.getFulfillOrderNo().toString());
				log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilmentOrderNo=" + fulfilOrdDesc.getFulfillOrderNo());
				fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.fromValue(item.getFulfillLocationType()));
				fulfilOrdDesc.setFulfillLocId(item.getFulfillLocId().longValue());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SIM-Fulfil loc id=" + fulfilOrdDesc.getFulfillLocId() + " loc type= " + fulfilOrdDesc.getFulfillLocType().value());
				fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.fromValue(partialDeliveryInd)); // check
				fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(omsCustOrdHead.getDeliveryType()));
				log.info("omsCustOrdNo " + omsCustOrdNo + "delivery type=" + fulfilOrdDesc.getDeliveryType().value());
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				if (omsCustOrdHead.getConsumerDlyTime() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "+++++++when reservation condition++++++++++");
					log.info("omsCustOrdNo " + omsCustOrdNo + "============================am inside if block getConsumerDlyTime()==============================");
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
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					// Change for persisting consumer delivery date from input
					now.setYear(now1.get(Calendar.YEAR));

					log.info("omsCustOrdNo:" + now.getYear());
					fulfilOrdDesc.setConsumerDeliveryDate(now);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Consumer delivery date=" + fulfilOrdDesc.getConsumerDeliveryDate());
				} else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "==================================inside else block================getConsumerDlyTime");
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						log.warn(e.toString());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					String consumerDlyDaysToBeAdded = session.getOmsSystemParametersFindIndValue("CONSUMER_DLY_DATE", "OMS_SYSTEM_OPTION");
					log.info("omsCustOrdNo " + omsCustOrdNo + "======================consumerDlyDaysToBeAdded===============================+consumerDlyDaysToBeAdded");
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					if (temp.get(0).getSourceLocationType().equals("WH")) {
						now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
						now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
						now.setMonth(now1.get(Calendar.MONTH) + 1);
						now.setDay(now1.get(Calendar.DAY_OF_MONTH));
						now.setYear(now1.get(Calendar.YEAR));
						// Change for persisting consumer delivery date from input
//          now.setYear(now1.get(Calendar.YEAR)+4); //In case of WH the comsumer delivery should be 4 years ahead to avoid picking of this customer order from WMS untill and unless this is being updated from Appointment Booking from RMS.
					} else {
						now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
						now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
						now.setMonth(now1.get(Calendar.MONTH) + 1);
						now.setYear(now1.get(Calendar.YEAR));
						// now.setDay(now1.get(Calendar.DAY_OF_MONTH)+Integer.valueOf(consumerDlyDaysToBeAdded));
						now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					}
					fulfilOrdDesc.setConsumerDeliveryDate(now);
				}
				if (omsCustOrdHead.getComments() != null) {
					fulfilOrdDesc.setComments(omsCustOrdHead.getComments());
				}
				FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
				fulfilOrdDtl.setItem(item.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SIM:item=" + item.getItem());
				fulfilOrdDtl.setOrderQtySuom(item.getOrderQty());
				log.info("omsCustOrdNo " + omsCustOrdNo + "Line number-----" + item.getLineNo());
				fulfilOrdDtl.setTransactionUom(session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(), item.getLineNo()).getTransactionUom());
				log.info("omsCustOrdNo " + omsCustOrdNo + "After set transaction UOM" + fulfilOrdDtl.getTransactionUom());
				fulfilOrdDtl.setStandardUom(session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(), item.getLineNo()).getStandardUom());
				log.info("omsCustOrdNo " + omsCustOrdNo + "After set standard Uom");
				fulfilOrdDtl.setSubstituteInd(session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(), item.getLineNo()).getSubstituteAllowInd());
				OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(), item.getLineNo());
				// Added code for finding sum of discounts Mantis Bug 2468
				if (omsCustOrdItem.getUnitRetail().intValue() != 0) {
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					log.info("%%%% omsCustOrdNo : " + omsCustOrdNo);
					log.info("%%%% item.getLineNo() : " + item.getLineNo());
					log.info("%%%% omsCustOrdItem.getUnitRetail() : " + omsCustOrdItem.getUnitRetail());
					BigDecimal unitRetail = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo, item.getLineNo(), omsCustOrdItem.getUnitRetail());
					log.info("unitRetail : " + unitRetail);
					if (unitRetail.intValue() > 0) {
						fulfilOrdDtl.setUnitRetail(unitRetail);
						log.info("Unit Retail--------" + fulfilOrdDtl.getUnitRetail());
						fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); // Unit retail and unit retail
																					// currency should be both populated
																					// or both null.
					}
					// fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
					// log.info("omsCustOrdNo " + omsCustOrdNo + "Unit Retail--------" +
					// fulfilOrdDtl.getUnitRetail());
					// fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr());
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "After set substitute ind");
				fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
				// Setting avalilabe quantity in orpos item table, pmerge in table after RMS,SIM
				// confirmation
				log.info("omsCustOrdNo " + omsCustOrdNo + "Setting into oms orpos item table");
				omsOrposCustOrdItm = session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposCustOrderId, item.getItem(), item.getLineNo());
				omsOrposCustOrdItm.setAvailableQuantity(item.getOrderQty());
			} // removed the } from here
			FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
			try {
				OmsCustOrdAddress omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
				if (null != omsCustOrdHead.getCustId() || omsCustOrdHead.getCustId().isEmpty() == false) {
					fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
					log.info("omsCustOrdNo " + omsCustOrdNo + "customer id" + fulfilOrdCustDesc.getCustomerNo());
				}
				if (omsCustOrdAddress.getDeliverFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "deliver name");
				}
				if (omsCustOrdAddress.getDeliverLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "before add 1");
				try {
					if (null != omsCustOrdAddress.getDeliverAdd1() || omsCustOrdAddress.getDeliverAdd1().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
						log.info("omsCustOrdNo " + omsCustOrdNo + "deliver add");
					}
					if (null != omsCustOrdAddress.getDeliverAdd2() || omsCustOrdAddress.getDeliverAdd2().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());
					}
					if (omsCustOrdAddress.getDeliverAdd3().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
					}
				} catch (Exception e) {
				}
				try {
					if (omsCustOrdAddress.getDeliverPhoneNo() != null) {
						fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
						log.info("omsCustOrdNo " + omsCustOrdNo + "deliver phone");
					}
					if (omsCustOrdAddress.getDeliverCity().isEmpty() == false) {
						fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());
						log.info("omsCustOrdNo " + omsCustOrdNo + "deliver city" + fulfilOrdCustDesc.getDeliverCity());
					}
					/*
					 * if(omsCustOrdAddress.getDeliverCountry().isEmpty()==false) {
					 * fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
					 * fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry()); }
					 * if(omsCustOrdAddress.getDeliverState().isEmpty()==false) {
					 * fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState()); }
					 */
					// if(omsCustOrdAddress.getDeliverPost().isEmpty()==false ||
					// omsCustOrdAddress.getDeliverPost()!=null)
					// {
					// fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());
					// }
					// else
					// {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside else for deliver post");
					fulfilOrdCustDesc.setDeliverPost("560048");
					// }
					if (omsCustOrdAddress.getDeliverPhoneNo() != null) {
						fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
						log.info("omsCustOrdNo " + omsCustOrdNo + "deliver phone");
					}
				} catch (Exception e) {
				}
				try {
					if (omsCustOrdAddress.getBillFirstName().isEmpty() == false) {
						fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());
					}
					if (omsCustOrdAddress.getBillLastName().isEmpty() == false) {
						fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());
					}
				} catch (Exception e) {
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
				}
				try {
					if (omsCustOrdAddress.getBillCity() != null) {
						fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());
					}
					/*
					 * if(omsCustOrdAddress.getBillState().isEmpty()==false) {
					 * fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState()); }
					 * if(omsCustOrdAddress.getBillCountry().isEmpty()==false) {
					 * fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());
					 * fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry()); }
					 */
					// if(omsCustOrdAddress.getBillPost().isEmpty()==false
					// ||omsCustOrdAddress.getBillPost()!=null)
					// {
					// fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());
					// }
					// else
					// {
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside else for billpost");
					fulfilOrdCustDesc.setBillPost("560048");
					// }
					fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
				} catch (Exception e) {
				}
			} catch (Exception e) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Adddress not present");
			}
			fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			// fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			// fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
			// }//end of main - OmsTempCoFo loop (initial loop for fulfilment order service)
			for (OmsTempCoFo omstempCoFo : temp) {
				omstempCoFo.setProcessingApp("SIM");
				// returnList.add(omstempCoFo);
				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "Calling SIM");
			// calling SIM
			log.info("omsCustOrdNo " + omsCustOrdNo + "customer id sent to rms is " + fulfilOrdCustDesc.getCustomerNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + "calling RMS and SIM for custOrdNo " + omsCustOrdHead.getCustOrderNo() + "and omsCustOrdNo " + omsCustOrdNo + "and " + "fulfillOrderNo "
					+ fulfilOrdDesc.getFulfillOrderNo());
			
			CreateFulfillmentOrderDetail detail = new CreateFulfillmentOrderDetail();
			detail.setFulfilOrdColDesc(fulfilOrdColDesc);
			OracleBaseAPIUtil.getOracleSIMClient().createFulfillmentOrderDetail(detail); // SIM table - fulord table
			for (OmsTempCoFo omstempCoFo : temp) {
				omstempCoFo.setProcessingApp("RMS");
				returnList.add(omstempCoFo);
				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			// calling RMS
			log.info("omsCustOrdNo " + omsCustOrdNo + "Calling RMS for reservation");
			log.info("omsCustOrdNo " + omsCustOrdNo + "calling RMS and SIM for custOrdNo " + omsCustOrdHead.getCustOrderNo() + "and omsCustOrdNo " + omsCustOrdNo + "and " + "fulfillOrderNo "
					+ fulfilOrdDesc.getFulfillOrderNo());
			
			CreateFulfilOrdColDesc colDesc = new CreateFulfilOrdColDesc();
			colDesc.setFulfilOrdColDesc(fulfilOrdColDesc);
			fulfilOrdCfmCol = OracleBaseAPIUtil.getOracleRMSClient().createFulfilOrdColDesc(colDesc).getFulfilOrdCfmCol(); // RMS - ordcust table
			log.info("omsCustOrdNo " + omsCustOrdNo + "Call successful");
			// Updating available quantity in orpos table in case of reservation
			session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);
			log.info("omsCustOrdNo " + omsCustOrdNo + "merged into oms orpos table" + omsOrposCustOrdItm.getAvailableQuantity());
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdCfmCol before setting " + fulfilOrdCfmCol.getFulfilOrdCfmDesc().size());
			processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);
		} else if (("WH".equals(temp.get(0).getSourceLocationType()) && "W".equals(temp.get(0).getFulfillLocationType())) || ("WH".equals(temp.get(0).getSourceLocationType()) && "S".equals(temp.get(0).getFulfillLocationType()) && (String.valueOf(intiateLocId).startsWith("8") || "Y".equals(shipToStore)))) {

			OmsTempCoFo omsTempCoFo = temp.get(0);

			BigDecimal phtWHId = session.getWhFindPhyWhForVirtualWh(omsTempCoFo.getSourceLocId());

			TransferCreationRequest request = new TransferCreationRequest();
			request.setCust_ord_no(extCustOrdNo);
			request.setDest_id(omsTempCoFo.getFulfillLocId().intValue());
			request.setFul_loc(omsTempCoFo.getFulfillLocId().intValue());
			request.setFul_ord_no(omsTempCoFo.getFulfillOrderNo().intValue());
			request.setRef_no(extCustOrdNo + omsTempCoFo.getFulfillOrderNo());
			request.setSrc_id(omsTempCoFo.getSourceLocId().intValue());
			request.setSrc_loc(phtWHId.intValue());
			log.info("---srcId in carrera request ---" + omsTempCoFo.getSourceLocId().intValue());
			TransferRequest itemReq = new TransferRequest();
			itemReq.setItem(omsTempCoFo.getItem());
			itemReq.setQty(omsTempCoFo.getOrderQty().intValue());
			List<TransferRequest> items = new ArrayList<TransferRequest>();
			items.add(itemReq);

			request.setCustomerItems(items);
			TransferCreationResponse response = null;
			try {
				ICarreraClient carreraTransfer = getCarreraClient();
				response = carreraTransfer.getOrderItemsResponse(request);
			} catch (Exception e) {
				throw new BusinessException("", e);
			}
			omsTempCoFo.setSourceLocId(phtWHId);
			if (!response.isSuccess()) {
				log.error("Error while creating the carrera transfer. " + response.getMessage());
				throw new BusinessException(response.getMessage());
			}
			omsTempCoFo.setProcessingApp("CARRERA");
			returnList.add(omsTempCoFo);
		} else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "***PO/Transfer");
			FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();
			fulfilOrdColDesc.setCollectionSize(1);
			FulfilOrdDesc fulfilOrdDesc = null;
			fulfilOrdDesc = new FulfilOrdDesc();
			log.info("temp.size " + temp.size());
			for (OmsTempCoFo omsTempCoFo : temp) {
				log.info("====================temp(0) object========================== ");
				log.info("temp.get(0).getFulfillOrderNo() " + temp.get(0).getFulfillOrderNo());
				log.info("temp.get(0).getItem " + temp.get(0).getItem());
				log.info("temp.get(0).getLineNo " + temp.get(0).getLineNo());
				log.info("temp.get(0).getSrcId " + temp.get(0).getSourceLocId());
				log.info("temp.get(0).getSrcLocType " + temp.get(0).getSourceLocationType());
				log.info("temp.get(0).getFulfillLocId() " + temp.get(0).getFulfillLocId());
				log.info("temp.get(0).getFulfillLocationType() " + temp.get(0).getFulfillLocationType());
				log.info("temp.get(0).getQty " + temp.get(0).getOrderQty());
				/*
				 * log.info("+++++++++++++++++++++++++OmsTempCo Object=====================");
				 * 
				 * log.info("omsTempCoFo.getFulfillOrderNo() "+omsTempCoFo.getFulfillOrderNo());
				 * log.info("omsTempCoFo.getItem "+omsTempCoFo.getItem());
				 * log.info("omsTempCoFo.getLineNo "+omsTempCoFo.getLineNo());
				 * log.info("omsTempCoFo.getSrcId "+omsTempCoFo.getSourceLocId());
				 * log.info("omsTempCoFo.getSrcLocType "+omsTempCoFo.getSourceLocationType());
				 * log.info("omsTempCoFo.getFulfillLocId() "+omsTempCoFo.getFulfillLocId());
				 * log.info("omsTempCoFo.getFulfillLocationType() "+omsTempCoFo.
				 * getFulfillLocationType());
				 * log.info("omsTempCoFo.getQty "+omsTempCoFo.getOrderQty());
				 */
				/*
				 * log.info("+++++++++++++++++++++++++fulfilOrdDesc Object====================="
				 * );
				 * 
				 * log.info("fulfilOrdDesc.getFulfillOrderNo() "+fulfilOrdDesc.getFulfillOrderNo
				 * ());
				 * log.info("fulfilOrdDesc.getSrcLocType "+fulfilOrdDesc.getSourceLocType());
				 * log.info("fulfilOrdDesc.getSourceLocType() "+fulfilOrdDesc.getSourceLocType()
				 * );
				 * log.info("fulfilOrdDesc.getFulfillLocId() "+fulfilOrdDesc.getFulfillLocId());
				 * log.info("fulfilOrdDesc.getFulfillLocType "+fulfilOrdDesc.getFulfillLocType()
				 * ); log.info("Item from fullDtl "+
				 * fulfilOrdDesc.getFulfilOrdDtl().get(0).getItem());
				 */
				fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);
				fulfilOrdDesc.getSourceLocType();
				// log.info("FulfilOder no" + String.valueOf(omsCustOrdNo.longValue() + i));
				// fulfilOrdDesc.setSourceLocType(fulfilOrdDesc.getSourceLocType().fromValue(omsTempCoFo.getSourceLocationType()));
				// fulfilOrdDesc.setSourceLocId(omsTempCoFo.getSourceLocId().longValue());
				// fulfilOrdDesc.setFulfillLocType(fulfilOrdDesc.getFulfillLocType().fromValue(omsTempCoFo.getFulfillLocationType()));
				// fulfilOrdDesc.setFulfillLocId(omsTempCoFo.getFulfillLocId().longValue());
				fulfilOrdDesc.setSourceLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType.fromValue(temp.get(0).getSourceLocationType()));
				fulfilOrdDesc.setSourceLocId(temp.get(0).getSourceLocId().longValue());
				fulfilOrdDesc.getFulfillLocType();
				fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));
				fulfilOrdDesc.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
				log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getSourceLocId()" + temp.get(0).getSourceLocId().longValue());
				log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getSrcLocType()" + temp.get(0).getSourceLocationType());
				log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getFulfillLocId " + fulfilOrdDesc.getFulfillLocId());
				log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getFulfillLocType()" + fulfilOrdDesc.getFulfillLocType());
				fulfilOrdDesc.getPartialDeliveryInd();
				fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.fromValue(partialDeliveryInd));
				log.info("omsCustOrdNo " + omsCustOrdNo + "getPartialDeliveryInd" + fulfilOrdDesc.getPartialDeliveryInd());
				fulfilOrdDesc.getDeliveryType();
				fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(omsCustOrdHead.getDeliveryType()));
				// fulfilOrdDesc.setDeliveryType(fulfilOrdDesc.getDeliveryType().S);
				log.info("omsCustOrdNo " + omsCustOrdNo + "DeliveryType" + fulfilOrdDesc.getDeliveryType());
				// fulfilOrdDesc.setConsumerDeliveryDate(input.getConsumerDeliveryDate());
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				// changed the code for PO-Transfer scenario
				if (omsCustOrdHead.getConsumerDlyTime() != null) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "++++++Consumer delivery time in case of PO-Transfer scenario++++++");
					log.info("omsCustOrdNo " + omsCustOrdNo + "============================am inside if block getConsumerDlyTime()==============================");
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
					now.setDay(now1.get(Calendar.DAY_OF_MONTH));
					// Change for persisting consumer delivery date from input
					if (temp.get(0).getSourceLocationType().equals("WH")) {
						now.setYear(now1.get(Calendar.YEAR) + 4);
					} else {
						now.setYear(now1.get(Calendar.YEAR));
					}
					log.info("omsCustOrdNo:" + now.getYear());

					fulfilOrdDesc.setConsumerDeliveryDate(now);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Consumer delivery date=" + fulfilOrdDesc.getConsumerDeliveryDate());
				} else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "==================================inside else block================getConsumerDlyTime");
					DatatypeFactory datatypeFactory = null;
					try {
						datatypeFactory = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						log.warn(e.toString());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					String consumerDlyDaysToBeAdded = session.getOmsSystemParametersFindIndValue("CONSUMER_DLY_DATE", "OMS_SYSTEM_OPTION");
					log.info("omsCustOrdNo " + omsCustOrdNo + "======================consumerDlyDaysToBeAdded===============================" + consumerDlyDaysToBeAdded);
					XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					Calendar now1 = Calendar.getInstance();
					if (temp.get(0).getSourceLocationType().equals("WH")) {
						now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
						now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
						now.setMonth(now1.get(Calendar.MONTH) + 1);
						now.setDay(now1.get(Calendar.DAY_OF_MONTH));

						// Change for persisting consumer delivery date from input
						// now.setYear(now1.get(Calendar.YEAR));
						now.setYear(now1.get(Calendar.YEAR) + 4); // In case of WH the comsumer delivery should be 4
																	// years ahead to avoid picking of this customer
																	// order from WMS untill and unless this is being
																	// updated from Appointment Booking from RMS.
					} else {
						now1.add(Calendar.DATE, Integer.valueOf(consumerDlyDaysToBeAdded));
						now1.setTimeInMillis(new Timestamp(new Date().getTime()).getTime());
						now.setMonth(now1.get(Calendar.MONTH) + 1);
						now.setYear(now1.get(Calendar.YEAR));
						// now.setDay(now1.get(Calendar.DAY_OF_MONTH)+Integer.valueOf(consumerDlyDaysToBeAdded));
						now.setDay(now1.get(Calendar.DAY_OF_MONTH));
						now.setYear(now1.get(Calendar.YEAR) + 4);
					}
					fulfilOrdDesc.setConsumerDeliveryDate(now);
				}
				/*
				 * DatatypeFactory datatypeFactory = null; try { datatypeFactory =
				 * DatatypeFactory.newInstance(); } catch (DatatypeConfigurationException e) {
				 * throw new
				 * SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString())); }
				 * XMLGregorianCalendar now =
				 * datatypeFactory.newXMLGregorianCalendar(gregorianCalendar); Calendar now1 =
				 * Calendar.getInstance();
				 * now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
				 * now.setMonth(now1.get(Calendar.MONTH)+1);
				 * now.setYear(now1.get(Calendar.YEAR));
				 * now.setDay(now1.get(Calendar.DAY_OF_MONTH));
				 * 
				 * fulfilOrdDesc.setConsumerDeliveryDate(now);
				 * log.info("getConsumerDeliveryDate"+fulfilOrdDesc.getConsumerDeliveryDate());
				 */
				// fulfilOrdDesc.setConsumerDeliveryTime(input.getConsumerDeliveryTime());
				// fulfilOrdDesc.setDeliveryCharges(input.g);
				// fulfilOrdDesc.setDeliveryChargesCurr(value);
				fulfilOrdDesc.setComments(omsCustOrdHead.getComments());
				OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
				// for(OmsTempCoFo omsTempCoFo:temp)
				// {
				fulfilOrdDesc.setFulfillOrderNo(omsTempCoFo.getFulfillOrderNo().toString());
				log.info("omsCustOrdNo " + omsCustOrdNo + "FulfillOrderno is" + fulfilOrdDesc.getFulfillOrderNo());
				/*
				 * log.info("omsCustOrdNo "+omsCustOrdNo +" lineNo= "+omsTempCoFo.getLineNo());
				 * log.info("omsCustOrdNo "+omsCustOrdNo +" item= "+omsTempCoFo.getItem());
				 * log.info("omsCustOrdNo "+omsCustOrdNo
				 * +" Quantity Ordered= "+omsTempCoFo.getOrderQty());
				 * log.info("omsCustOrdNo "+omsCustOrdNo
				 * +" Fulfilmetn OrderNo="+omsTempCoFo.getFulfillOrderNo());
				 * log.info("omsCustOrdNo "+omsCustOrdNo
				 * +" source loc id= "+omsTempCoFo.getSourceLocId());
				 * log.info("omsCustOrdNo "+omsCustOrdNo
				 * +" Fulfill loc id= "+omsTempCoFo.getFulfillLocId());
				 */
				FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
				fulfilOrdDtl.setItem(omsTempCoFo.getItem());
				fulfilOrdDtl.setOrderQtySuom(omsTempCoFo.getOrderQty());
				fulfilOrdDtl.setTransactionUom(omsCustOrdItem.getTransactionUom());
				fulfilOrdDtl.setStandardUom(omsCustOrdItem.getStandardUom());
				// Added for finding sum of discounts
				if (omsCustOrdItem.getUnitRetail().intValue() != 0) {
					OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
					log.info("%%%% omsCustOrdNo : " + omsCustOrdNo);
					log.info("%%%% item.getLineNo() : " + omsTempCoFo.getLineNo());
					log.info("%%%% omsCustOrdItem.getUnitRetail() : " + omsCustOrdItem.getUnitRetail());
					BigDecimal unitRetail = oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo, omsTempCoFo.getLineNo(), omsCustOrdItem.getUnitRetail());
					log.info("unitRetail : " + unitRetail);
					// fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
					if (unitRetail.intValue() > 0) {
						fulfilOrdDtl.setUnitRetail(unitRetail);
						log.info("Unit Retail--------" + fulfilOrdDtl.getUnitRetail());
						fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); // Unit retail and unit retail
																					// currency should be both populated
																					// or both null.
					}
					// fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
					// log.info("omsCustOrdNo " + omsCustOrdNo + "Unit Retail--------" +
					// fulfilOrdDtl.getUnitRetail());
					// fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); //Unit retail and
					// unit retail currency should be both populated or both null.
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo.getItem() " + omsTempCoFo.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo.getLineNo() " + omsTempCoFo.getLineNo());
				fulfilOrdDtl.setSubstituteInd(session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo()).getSubstituteAllowInd());
				log.info("omsCustOrdNo " + omsCustOrdNo + "getSubstituteInd" + fulfilOrdDtl.getSubstituteInd());
				fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "after sustitute ind");
			FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
			OmsCustOrdAddress omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
			try {
				if (omsCustOrdHead.getCustId().isEmpty() == false) {
					fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Customer No is ---" + fulfilOrdCustDesc.getCustomerNo());
				}
				if (omsCustOrdAddress.getDeliverFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver First Name--" + fulfilOrdCustDesc.getDeliverFirstName());
				}
				if (omsCustOrdAddress.getDeliverLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver Last Name----" + fulfilOrdCustDesc.getDeliverLastName());
				}
			} catch (Exception e) {
				log.error("omsCustOrdNo " + omsCustOrdNo + "Error in cust no or last name");
			}
			try {
				if (null != omsCustOrdAddress.getDeliverAdd1() || omsCustOrdAddress.getDeliverAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver Add1---" + fulfilOrdCustDesc.getDeliverAdd1());
				}
				if (null != omsCustOrdAddress.getDeliverAdd2() || omsCustOrdAddress.getDeliverAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Delver Add2---" + fulfilOrdCustDesc.getDeliverAdd2());
				}
				if (omsCustOrdAddress.getDeliverAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver Add 3----" + fulfilOrdCustDesc.getDeliverAdd3());
				}
			} catch (Exception e) {
				log.error("omsCustOrdNo " + omsCustOrdNo + "error in deliver add");
			}
			try {
				if (omsCustOrdAddress.getDeliverCity().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver City---" + fulfilOrdCustDesc.getDeliverCity());
				}
				if (omsCustOrdAddress.getDeliverCountry().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver COuntry ID---" + fulfilOrdCustDesc.getDeliverCountryId());
					fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver Country----" + fulfilOrdCustDesc.getDeliverCounty());
				}
				if (omsCustOrdAddress.getDeliverState().isEmpty() == false) {
					fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Deliver State----" + fulfilOrdCustDesc.getDeliverState());
				}
				// if(omsCustOrdAddress.getDeliverPost().isEmpty()==false ||
				// omsCustOrdAddress.getDeliverPost()!=null)
				// {
				// fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());
				// log.info("Deliver Post----"+fulfilOrdCustDesc.getDeliverPost());
				// }
				// else
				// {
				// log.info("inside else for deliver post");
				fulfilOrdCustDesc.setDeliverPost("560048");
				// }
				if (omsCustOrdAddress.getDeliverPhoneNo() != null) {
					fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
					log.info("omsCustOrdNo " + omsCustOrdNo + "DeliverPhone-----" + fulfilOrdCustDesc.getDeliverPhone());
				}
			} catch (Exception e) {
				log.error("omsCustOrdNo " + omsCustOrdNo + "error in cust desc");
			}
			try {
				if (omsCustOrdAddress.getBillFirstName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill First Name---" + fulfilOrdCustDesc.getBillFirstName());
				}
				if (omsCustOrdAddress.getBillLastName().isEmpty() == false) {
					fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill Last Name---" + fulfilOrdCustDesc.getBillLastName());
				}
			} catch (Exception e) {
				log.error("omsCustOrdNo " + omsCustOrdNo + "error  in bill name");
			}
			try {
				if (omsCustOrdAddress.getBillAdd1().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd1(omsCustOrdAddress.getBillAdd1());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill Add 1---" + fulfilOrdCustDesc.getBillAdd1());
				}
				if (omsCustOrdAddress.getBillAdd2().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd2(omsCustOrdAddress.getBillAdd2());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill add 2----" + fulfilOrdCustDesc.getBillAdd2());
				}
				if (omsCustOrdAddress.getBillAdd3().isEmpty() == false) {
					fulfilOrdCustDesc.setBillAdd3(omsCustOrdAddress.getBillAdd3());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill add 3---" + fulfilOrdCustDesc.getBillAdd3());
				}
			} catch (Exception e) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "error in address");
			}
			try {
				if (omsCustOrdAddress.getBillCity().isEmpty() == false) {
					fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill Cityyy----" + fulfilOrdCustDesc.getBillCity());
				}
				if (omsCustOrdAddress.getBillState().isEmpty() == false) {
					fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill State---" + fulfilOrdCustDesc.getBillState());
				}
				if (omsCustOrdAddress.getBillCountry().isEmpty() == false) {
					fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill Country ID-- " + fulfilOrdCustDesc.getBillCountryId());
					fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Bill County---" + fulfilOrdCustDesc.getBillCounty());
				}
				// if(omsCustOrdAddress.getBillPost().isEmpty()==false
				// ||omsCustOrdAddress.getBillPost()!=null )
				// {
				// fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());
				// log.info("Bill Post --"+fulfilOrdCustDesc.getBillPost());
				// }
				// else
				// {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside else for billpost");
				fulfilOrdCustDesc.setBillPost("560048");
				// }
				fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "Bill phone--" + fulfilOrdCustDesc.getBillPhone());
			} catch (Exception e) {
				log.error("omsCustOrdNo " + omsCustOrdNo + "error in bill detail");
			}
			fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
			// fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
			fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
			for (OmsTempCoFo omstempCoFo : temp) {
				log.info("omsCustOrdNo " + omsCustOrdNo + " Processing app is RMS");
				log.info("omsCustOrdNo " + omsCustOrdNo + " Item " + omstempCoFo.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + " line No " + omstempCoFo.getLineNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + " Quantity Ordered= " + omstempCoFo.getOrderQty());
				log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfilmetn OrderNo=" + omstempCoFo.getFulfillOrderNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + " source loc id= " + omstempCoFo.getSourceLocId());
				log.info("omsCustOrdNo " + omsCustOrdNo + " Src Loc Type= " + omstempCoFo.getSourceLocationType());
				log.info("omsCustOrdNo " + omsCustOrdNo + " FullLocId= " + omstempCoFo.getFulfillLocId());
				log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfill loc Type= " + omstempCoFo.getFulfillLocationType());
				omstempCoFo.setProcessingApp("RMS");
				try {
					returnList.add(omstempCoFo);
				} catch (Exception e) {
					log.error("omsCustOrdNo " + omsCustOrdNo + "error in adding to return list" + e);
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "Added in return list");
				// session.mergeOmsTempCoFo(omstempCoFo);
			}
			// calling RMS
			log.info("omsCustOrdNo " + omsCustOrdNo + "Calling RMS");
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getFulfilOrdDtl()" + fulfilOrdColDesc.getFulfilOrdDesc().get(0).getFulfilOrdDtl().size());
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdDesc.getFulfilOrdDtl()" + fulfilOrdDesc.getFulfilOrdCustDesc());
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilOrdColDesc" + fulfilOrdColDesc.getFulfilOrdDesc().size());
			log.info("+++++++++++++++++++++++++++calling create FulFilOrdColDesc+++++++++++++++++++++ ");
			log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfilmetn OrderNo=" + fulfilOrdDesc.getFulfillOrderNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + " source loc id= " + fulfilOrdDesc.getSourceLocId());
			log.info("omsCustOrdNo " + omsCustOrdNo + " Src Loc Type= " + fulfilOrdDesc.getSourceLocType());
			log.info("omsCustOrdNo " + omsCustOrdNo + " FullLocId= " + fulfilOrdDesc.getFulfillLocId());
			log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfill loc Type= " + fulfilOrdDesc.getFulfillLocType());
			
			CreateFulfilOrdColDesc colDesc = new CreateFulfilOrdColDesc();
			colDesc.setFulfilOrdColDesc(fulfilOrdColDesc);
			fulfilOrdCfmCol = OracleBaseAPIUtil.getOracleRMSClient().createFulfilOrdColDesc(colDesc).getFulfilOrdCfmCol();
			processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);
			processedObject.setCurrentFulFillOrderNo(new BigDecimal(fulfilOrdDesc.getFulfillOrderNo()).intValue());
			log.info("omsCustOrdNo " + omsCustOrdNo + "call successful");
		}
		processedObject.setOmsTempCoFoList(returnList);
		return processedObject;
	}

	public String processWebserviceResponse(BigDecimal omsCustOrdNo, FulfilOrdCfmCol fulfilOrdCfmCol, ArrayList<OmsTempCoFo> temp, BigDecimal omsOrposCustOrderId) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside processWebserviceResponse");
		String responseStatus = "";
		for (OmsTempCoFo omsTempCoFo : temp) {
			OmsCustOrdItem omscustOrdItem = session.getOmsCustOrdItemFindByItem(omsTempCoFo.getOmsCustOrdNo(), omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
			if ("CARRERA".equals(omsTempCoFo.getProcessingApp())) {
				omsTempCoFo.setStatus("PROCESSED");
				omsTempCoFo.setFoConfQty(omsTempCoFo.getOrderQty());
				omscustOrdItem.setStatus("S");
				session.mergeOmsCustOrdItem(omscustOrdItem);
			} else if (fulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty() && fulfilOrdCfmCol.getCollectionSize() == 0) {
				// confirmed
				responseStatus = "C";
				log.info("omsCustOrdNo " + omsCustOrdNo + "Confirmed for fullfillordeNo" + omsTempCoFo.getFulfillOrderNo());
				omsTempCoFo.setRmsResponseCode("C");
				omsTempCoFo.setStatus("PROCESSED");
				omsTempCoFo.setFoConfQty(omsTempCoFo.getOrderQty());
				omscustOrdItem.setStatus("S");
				session.mergeOmsCustOrdItem(omscustOrdItem);
			} else {
				confirmType = fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getConfirmType();
				if (confirmType != null && confirmType.value().equals("X")) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Confirm type is X for fullfillordeNo" + omsTempCoFo.getFulfillOrderNo());
					responseStatus = "X";
					omsTempCoFo.setRmsResponseCode("X");
					// session.mergeOmsTempCoFo(omsTempCoFo);
					omscustOrdItem.setQtyCancelled(omsTempCoFo.getOrderQty());
					omscustOrdItem.setStatus("X");
					// session.mergeOmsCustOrdItem(omscustOrdItem);
					OmsOrposCustOrdItm omsOrposCustOrdItm = session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposCustOrderId, omscustOrdItem.getItem(), omscustOrdItem.getLineNo());
					omsOrposCustOrdItm.setCancelledQuantity(omscustOrdItem.getQtyOrderedSuom());
					// session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);
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
					log.info("omsCustOrdNo " + omsCustOrdNo + "Validation error");
					omsTempCoFo.setRmsResponseCode("E");
					omsTempCoFo.setStatus("F");
					// session.mergeOmsTempCoFo(omsTempCoFo);
					omscustOrdItem.setQtyCancelled(omsTempCoFo.getOrderQty());
					omscustOrdItem.setStatus("F");
					session.mergeOmsCustOrdItem(omscustOrdItem);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Validation error"));
				}
			}
		}
		return responseStatus;
	}

	public void callSeibelWebserviceForPickUp(String custOrderNo, String custId, List<CustOrdItmPkVo> pickUpItemsList, Map<String, BigDecimal> seibelDataForPickUp) throws SOAPException {
		log.info("inside callSeibelWebserviceForPickUp method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		SiebelStatusUpdateInfo siebelStatusUpdate = new SiebelStatusUpdateInfo();
		SiebelStatusUpdateResponse siebelStatusUpdateResponse = null;
		List<SiebelStatusUpdateDetails> siebelStatusUpdateDetailsList = null;
		OmsorderupdatebpelprocessClientEp omsorderupdatebpelprocessClientEp = null;
		SiebelStatusUpdateDetails siebelStatusUpdateDetails = null;
		// SiebelStatusUpdateWebService siebelStatusUpdateWebService = null;
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(custOrderNo, "S");
		log.info("omsCustOrdNo " + omsCustOrdNo);
		log.info("seibelDataForPickUp.keySet() " + seibelDataForPickUp.keySet());
		// List<OmsCoFoCancel>
		// omsCoFoCancelList=session.getOmsCoFoCancelFindByOmsCancelId(omsCancelId);
		log.info("pickUpItemsList.size() " + pickUpItemsList.size());
		// for (CustOrdItmPkVo custOrdItmPkVo : pickUpItemsList)
		// {
		log.info("inside custOrdItmPkVo loop");
		if (omsCustOrdHead.getSubCustOrderNo() == null) {
			siebelStatusUpdate.setSubCustOrderNo("1");
		} else {
			siebelStatusUpdate.setSubCustOrderNo(omsCustOrdHead.getSubCustOrderNo());
		}
		log.info("custOrderNo " + custOrderNo);
		siebelStatusUpdate.setCustId(omsCustOrdHead.getCustId());
		siebelStatusUpdate.setCustOrderNo(omsCustOrdHead.getCustOrderNo());
		siebelStatusUpdate.setApplicationId(omsCustOrdHead.getApplicationId());
		log.info("omsCustOrdHead.getApplicationId() " + omsCustOrdHead.getApplicationId());
		siebelStatusUpdate.setOmsCustomerOrderNo(omsCustOrdNo.longValue());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now " + now);
		siebelStatusUpdate.setCancelDatetime(now);
		// ==============================================================================
		DatatypeFactory datatypeFactory2 = null;
		try {
			datatypeFactory2 = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now2 = datatypeFactory2.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now2 " + now2);
		siebelStatusUpdate.setConsumerDlyTime(now2);
		// ==============================================================================
		DatatypeFactory datatypeFactory3 = null;
		try {
			datatypeFactory3 = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now3 = datatypeFactory3.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now3 " + now3);
		siebelStatusUpdate.setCloseDatetime(now3);
		siebelStatusUpdateDetailsList = siebelStatusUpdate.getCustomerOrderStatusUpdateDetails();
		// for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList)
		for (String key : seibelDataForPickUp.keySet()) {
			// if ((omsCoFulfillDetail.getLineNo().intValue() ==
			// custOrdItmPkVo.getLineItemNo()) &&
			// omsCoFulfillDetail.getSourceLoc().intValue() ==
			// omsCoFulfillDetail.getFulfillLoc().intValue())
			// {
			siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
			String keyData[] = key.split(",");
			BigDecimal deliveredQty = seibelDataForPickUp.get(key);
			BigDecimal fulFillOrderNo = new BigDecimal(keyData[0]);
			BigDecimal lineNo = new BigDecimal(keyData[1]);
			BigDecimal sourceLoc = new BigDecimal(keyData[2]);
			String sourcelocType = keyData[3];
			BigDecimal fulFillLoc = new BigDecimal(keyData[4]);
			String fulFillLocType = keyData[5];
			String item = keyData[6];
			log.info("========fulFillOrderNo=======" + fulFillOrderNo);
			log.info("=========lineNo===========" + lineNo);
			log.info("=========sourceLoc===========" + sourceLoc);
			log.info("=======sourceClocType==========" + sourcelocType);
			log.info("=========fulFillLoc========" + fulFillLoc);
			log.info("=========fulFillLocType======" + fulFillLocType);
			log.info("========item==============" + item);
			log.info("Delivedred Qty============" + deliveredQty);
			siebelStatusUpdateDetails.setItem(item);
			siebelStatusUpdateDetails.setLineNo(lineNo.longValue());
			siebelStatusUpdateDetails.setQty(deliveredQty);
			siebelStatusUpdateDetails.setEventId("DL");
			siebelStatusUpdateDetails.setEventComments("Delivered to customer");
			siebelStatusUpdateDetails.setSourceLoc(sourceLoc.longValue());
			siebelStatusUpdateDetails.setSourceLocType(sourcelocType);
			siebelStatusUpdateDetails.setFulfillLocType(fulFillLocType);
			siebelStatusUpdateDetails.setFulfillLoc(fulFillLoc.longValue());
			GregorianCalendar gregorianCalendar1 = new GregorianCalendar();
			DatatypeFactory datatypeFactory1 = null;
			try {
				datatypeFactory1 = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				log.warn(e.toString());
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
			}
			XMLGregorianCalendar now1 = datatypeFactory1.newXMLGregorianCalendar(gregorianCalendar1);
			siebelStatusUpdateDetails.setUpdateDatetime(now1);
			log.info("now1 " + now1);
			siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
			// }
			log.info("siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size());
			// }
			log.info("Records in seibel");
			log.info("siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size());
			for (SiebelStatusUpdateDetails siebel : siebelStatusUpdateDetailsList) {
				log.info("===============siebel.getLineNo()=================== " + siebel.getLineNo());
				log.info("siebel.getItem() " + siebel.getItem());
				log.info("siebel.getSourceLoc() " + siebel.getSourceLoc());
				log.info("siebel.getSourceLocType() " + siebel.getSourceLocType());
				log.info("siebel.getFulfillLoc() " + siebel.getFulfillLoc());
				log.info("siebel.getFulfillLocType() " + siebel.getFulfillLocType());
				log.info("siebel.getQty() " + siebel.getQty());
				log.info("siebel.getEventId() " + siebel.getEventId());
			}
		}

		Writer os = new StringWriter();
		try {
			omsorderupdatebpelprocessClientEp = new OmsorderupdatebpelprocessClientEp();
			log.info("omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt() " + omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt());
			SiebelStatusUpdateWebService siebelStatusUpdateWebService = omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt();

			JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
			Marshaller marshel = context.createMarshaller();
			marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");

			marshel.marshal(siebelStatusUpdate, os);
			log.info("Request for sending the seibel -> " + os.toString());

			siebelStatusUpdateResponse = siebelStatusUpdateWebService.processSiebelStatusUpdate(siebelStatusUpdate);
			log.info("Response from siebel is" + siebelStatusUpdateResponse.getMessage());
		} catch (Exception e) {
			log.error("Publishing to seibel failed" + e);
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			try {
				omsRepublishData.setXmlMsg(os.toString());
				session.persistOmsRepublishData(omsRepublishData);
				// log.info("persisting in omsRepublish data");
			} catch (Exception f) {
				log.error("persisting in omsRepublish data failed" + f);
			}
		}
		if (siebelStatusUpdateResponse.getMessage().equalsIgnoreCase("Failed") && omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			try {
				JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
				Marshaller marshel = context.createMarshaller();
				marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
				os = new StringWriter();
				marshel.marshal(siebelStatusUpdate, os);
				omsRepublishData.setXmlMsg(os.toString());
				log.info(omsRepublishData.getXmlMsg());
				session.persistOmsRepublishData(omsRepublishData);
				// log.info("persisten in omsRepublish data");
			} catch (Exception f) {
				log.info("Exception while calling Siebel " + f.getMessage());
			}
		}
	}

	public void callSeibelWebserviceForCancellation(BigDecimal omsCancelId, String custId, List<CustOrdItmPkVo> cancelItemList, TreeMap<String, BigDecimal> backorderTreeMap) throws SOAPException {
		log.info("********** Begin of callSeibelWebserviceForCancellation ******************** ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		SiebelStatusUpdateInfo siebelStatusUpdate = new SiebelStatusUpdateInfo();
		SiebelStatusUpdateResponse siebelStatusUpdateResponse = null;
		List<SiebelStatusUpdateDetails> siebelStatusUpdateDetailsList = null;
		SiebelStatusUpdateDetails siebelStatusUpdateDetails = null;
		OmsorderupdatebpelprocessClientEp omsorderupdatebpelprocessClientEp = null;
		SiebelStatusUpdateWebService siebelStatusUpdateWebService = null;
		siebelStatusUpdateDetailsList = siebelStatusUpdate.getCustomerOrderStatusUpdateDetails();
		log.info("cancelItemList " + cancelItemList.size());
		log.info("omsCancelId " + omsCancelId);
		OmsCoCancelHead omsCoCancelHead = session.getOmsCoCancelHeadFindCustOrderNoByomsCancelId(omsCancelId);
		List<OmsCoFoCancel> omsCoFoCancelList = session.getOmsCoFoCancelFindByOmsCancelId(omsCancelId);
		OmsCoCancelItem omsCoCancelItem = null;
		OmsCustOrdHead omsCustOrdHead = null;
		siebelStatusUpdate.setCustOrderNo(omsCoCancelHead.getCustOrdNo());
		omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(omsCoCancelHead.getCustOrdNo(), "S");
		siebelStatusUpdate.setApplicationId(omsCustOrdHead.getApplicationId());
		log.info("omsCustOrdHead.getApplicationId() " + omsCustOrdHead.getApplicationId());
		BigDecimal omsCustOrdNo = omsCustOrdHead.getOmsCustOrdNo();
		log.info("omsCustOrdNo " + omsCustOrdNo);
		if (omsCoCancelHead.getSubCustOrdNo() == null) {
			siebelStatusUpdate.setSubCustOrderNo("1");
		} else {
			siebelStatusUpdate.setSubCustOrderNo(omsCoCancelHead.getSubCustOrdNo());
		}
		log.info("custId " + custId);
		siebelStatusUpdate.setCustId(custId);
		siebelStatusUpdate.setOmsCustomerOrderNo(omsCustOrdNo.longValue());
		// cancel date time
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now " + now);
		siebelStatusUpdate.setCancelDatetime(now);
		// ==============================================================================
		// consumer date time
		DatatypeFactory datatypeFactory2 = null;
		try {
			datatypeFactory2 = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now2 = datatypeFactory2.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now2 " + now2);
		siebelStatusUpdate.setConsumerDlyTime(now2);
		// ==============================================================================
		// close date time
		DatatypeFactory datatypeFactory3 = null;
		try {
			datatypeFactory3 = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now3 = datatypeFactory3.newXMLGregorianCalendar(gregorianCalendar);
		log.info("now3 " + now3);
		siebelStatusUpdate.setCloseDatetime(now3);
		siebelStatusUpdateDetailsList = siebelStatusUpdate.getCustomerOrderStatusUpdateDetails();
		// Code added for bug 2787
		log.info("========== backorderTreeMap.keySet() : " + backorderTreeMap.keySet());
		log.info("Sending Seibel Status while BackOrder Cancellation in POS SIEBEL customer order status update");
		if (backorderTreeMap.keySet() != null && backorderTreeMap.keySet().size() > 0) {
			for (String key : backorderTreeMap.keySet()) {
				String keyData[] = key.split(",");
				BigDecimal sourceQty = backorderTreeMap.get(key);
				BigDecimal lineno = new BigDecimal(keyData[0]);
				String item = keyData[1];
				BigDecimal sourceLoc = new BigDecimal(keyData[2]);
				String sourcelocType = keyData[3];
				BigDecimal fulFillLoc = new BigDecimal(keyData[4]);
				String fulFillLocType = keyData[5];
				log.info("Source Qty : " + sourceQty);
				log.info("Line No : " + lineno);
				log.info("Item : " + item);
				log.info("Source Loc : " + sourceLoc);
				log.info("Source Loc Type : " + sourcelocType);
				log.info("Fulfil Loc : " + fulFillLoc);
				log.info("Fulfil Loc Type : " + fulFillLocType);
				siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
				siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
				siebelStatusUpdateDetails.setItem(item.toString());
				siebelStatusUpdateDetails.setLineNo(lineno.longValue());
				siebelStatusUpdateDetails.setSourceLoc(sourceLoc.longValue());
				siebelStatusUpdateDetails.setSourceLocType(sourcelocType.toString());
				siebelStatusUpdateDetails.setFulfillLocType(fulFillLocType.toString());
				siebelStatusUpdateDetails.setFulfillLoc(fulFillLoc.longValue());
				siebelStatusUpdateDetails.setQty(sourceQty);
				siebelStatusUpdateDetails.setEventId("CAC");
				siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
				// update datetime
				GregorianCalendar gregorianCalendar4 = new GregorianCalendar();
				DatatypeFactory datatypeFactory4 = null;
				try {
					datatypeFactory4 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar now4 = datatypeFactory4.newXMLGregorianCalendar(gregorianCalendar4);
				siebelStatusUpdateDetails.setUpdateDatetime(now4);
				log.info("now4 " + now4);
				siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
			}
		}
		log.info("Size of omsCoFoCancelList : " + omsCoFoCancelList.size());
		if (omsCoFoCancelList.size() > 0) {
			for (CustOrdItmPkVo custOrdItmPkVo : cancelItemList) {
				try {
					List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
					omsCoCancelItem = session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId, new BigDecimal(custOrdItmPkVo.getLineItemNo()));
					log.info("item " + omsCoCancelItem.getItem());
					log.info("LineNo " + omsCoCancelItem.getLineNo());
					omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()), omsCustOrdNo);
					log.info(omsCoFulfillDetailList.size() + " omsCoFulfillDetailList.size()");
					if (omsCoFulfillDetailList != null && omsCoFulfillDetailList.size() > 0) {
						for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
							for (OmsCoFulfillDetail fulfillDetail : omsCoFulfillDetailList) {
								if (omsCoFoCancel.getLineNo().intValue() == fulfillDetail.getLineNo().intValue()
										&& omsCoFoCancel.getFulfillOrderNo().intValue() == fulfillDetail.getFulfillOrderNo().intValue() && omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
									siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
									log.info("---------------omsCoFoCancel.getFulfillOrderNo().intValue()" + omsCoFoCancel.getFulfillOrderNo().intValue());
									log.info("---------------fulfillDetail.getFulfillOrderNo().intValue()" + fulfillDetail.getFulfillOrderNo().intValue());
									log.info("******* siebelStatusUpdateDetails property Values********************* ");
									siebelStatusUpdateDetails.setItem(omsCoFoCancel.getItem());
									siebelStatusUpdateDetails.setLineNo(omsCoFoCancel.getLineNo().longValue());
									log.info("Item=" + omsCoFoCancel.getItem() + "Line no=" + omsCoFoCancel.getLineNo());
									log.info("-----------------Quantity value------" + new BigDecimal(omsCoFoCancel.getFoCancelledOty().intValue()) + "--------------");
									siebelStatusUpdateDetails.setQty(new BigDecimal(omsCoFoCancel.getFoCancelledOty().intValue()));
									log.info("---------------Source Location type value is-------" + fulfillDetail.getSourceLocType());
									siebelStatusUpdateDetails.setSourceLocType(fulfillDetail.getSourceLocType());
									log.info("---------Source Location  value is------------------" + fulfillDetail.getSourceLoc());
									siebelStatusUpdateDetails.setSourceLoc(fulfillDetail.getSourceLoc().longValue());
									log.info("---------FulFill Location type value is--------" + fulfillDetail.getFulfillLocType());
									siebelStatusUpdateDetails.setFulfillLocType(fulfillDetail.getFulfillLocType());
									log.info("---------FulFill Location  value is-------------" + fulfillDetail.getFulfillLoc());
									siebelStatusUpdateDetails.setFulfillLoc(fulfillDetail.getFulfillLoc().longValue());
									siebelStatusUpdateDetails.setEventId("CAC");
									siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
									log.info("cancellationItems.getOmsCancelId().longValue() " + omsCancelId);
									siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
									GregorianCalendar gregorianCalendar1 = new GregorianCalendar();
									DatatypeFactory datatypeFactory1 = null;
									try {
										datatypeFactory1 = DatatypeFactory.newInstance();
									} catch (DatatypeConfigurationException e) {
										log.warn(e.toString());
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
									}
									XMLGregorianCalendar now1 = datatypeFactory1.newXMLGregorianCalendar(gregorianCalendar1);
									siebelStatusUpdateDetails.setUpdateDatetime(now1);
									log.info("now1 " + now1);
									siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
									log.info("siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size());
								}
							} // end of for (OmsCoFulfillDetail fulfillDetail : fulfillDetailList)
						} // end of for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList)
					} // else if(omsCoFulfillDetailList!=null && omsCoFulfillDetailList.size()>0)
				} catch (Exception e) {
					log.error("-->SIEBEL customer order status update failed." + e.getMessage());
				}
			}
		}
		for (CustOrdItmPkVo custOrdItmPkVo : cancelItemList) {
			log.info("omscancelId " + omsCancelId + "checking is any the shipping and non-invnetory item is present in OmsCustOrdItem");
			List<OmsCustOrdItem> omsCustordItem = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, new BigDecimal(custOrdItmPkVo.getLineItemNo()));
			// Updated of 3004 Production bug for CAC event for Non-Inventory or shipping
			// charge Item
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(omsCustordItem.get(0).getItem());
			String inventoryIndn = session.getItemMasterFindInventoryInd(omsCustordItem.get(0).getItem(), itemDept);
			if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
				try {
					SiebelStatusUpdateDetails siebelStatusUpdateDetails1 = new SiebelStatusUpdateDetails();
					log.info("omscancelId " + omsCoCancelItem.getOmsCancelId() + "sending the event for shipping charge or non-inventory item");
					siebelStatusUpdateDetails1.setItem(omsCustordItem.get(0).getItem());
					log.info("cancellationItems.getItem() : " + omsCustordItem.get(0).getItem());
					siebelStatusUpdateDetails1.setLineNo(omsCustordItem.get(0).getLineNo().longValue());
					log.info("Item=" + omsCustordItem.get(0).getItem() + "Line no=" + omsCustordItem.get(0).getLineNo());
					siebelStatusUpdateDetails1.setQty(custOrdItmPkVo.getCancelledQuantity());
					log.info("omsCancelId " + omsCancelId.longValue() + "custOrdItmPkVo.getCancelledQuantity() : " + custOrdItmPkVo.getCancelledQuantity());
					siebelStatusUpdateDetails1.setEventId("CAC");
					log.info("siebelStatusUpdateDetails.setEventId(\"CAC\") : CAC");
					siebelStatusUpdateDetails1.setEventComments("Cancelled by customer");
					log.info("siebelStatusUpdateDetails.setEventComments(\"Cancelled by customer\") : Cancelled by customer");
					siebelStatusUpdateDetails1.setOmsCancelId(omsCancelId.longValue());
					log.info("omsCancelId.longValue() : " + omsCancelId.longValue());
					siebelStatusUpdateDetails1.setSourceLoc(omsCustOrdHead.getOrderRequestorId().longValue());
					siebelStatusUpdateDetails1.setSourceLocType("ST");
					siebelStatusUpdateDetails1.setFulfillLocType("S");
					siebelStatusUpdateDetails1.setFulfillLoc(omsCustOrdHead.getOrderRequestorId().longValue());
					GregorianCalendar gregorianCalendar7 = new GregorianCalendar();
					DatatypeFactory datatypeFactory7 = null;
					try {
						datatypeFactory7 = DatatypeFactory.newInstance();
					} catch (DatatypeConfigurationException e) {
						log.warn(e.toString());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
					}
					XMLGregorianCalendar now7 = datatypeFactory7.newXMLGregorianCalendar(gregorianCalendar7);
					siebelStatusUpdateDetails1.setUpdateDatetime(now7);
					siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails1);
				} catch (Exception e) {
					log.info("Exception " + e.getMessage());
				}
			} // end of if for non-inventory or shipping charge check
		}
		log.info("=================siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size() + "==============================");
		for (SiebelStatusUpdateDetails siebel : siebelStatusUpdateDetailsList) {
			log.info("OmsCancelId for Siebel Data cancellation " + siebel.getOmsCancelId());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getItem() " + siebel.getItem());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getSourceLoc() " + siebel.getSourceLoc());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getSourceLocType() " + siebel.getSourceLocType());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getFulfillLoc() " + siebel.getFulfillLoc());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getFulfillLocType() " + siebel.getFulfillLocType());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getQty() " + siebel.getQty());
			log.info("OmsCancelId " + siebel.getOmsCancelId() + "siebel.getEventId() " + siebel.getEventId());
			log.info("OmsCancelId " + siebel.getEventComments() + "siebel.getEventComments() " + siebel.getEventComments());
		}
		Writer os = null;
		if (siebelStatusUpdateDetailsList.size() > 0) {
			try {
				omsorderupdatebpelprocessClientEp = new OmsorderupdatebpelprocessClientEp();
				siebelStatusUpdateWebService = omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt();

				JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
				Marshaller marshel = context.createMarshaller();
				marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
				os = new StringWriter();
				marshel.marshal(siebelStatusUpdate, os);
				log.info("Request XML for cancellation" + os.toString());

				siebelStatusUpdateResponse = siebelStatusUpdateWebService.processSiebelStatusUpdate(siebelStatusUpdate);
				log.info("Response from siebel is" + siebelStatusUpdateResponse.getMessage());
			} catch (Exception e) {
				log.error("Publishing to seibel failed" + e);
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				try {

					omsRepublishData.setXmlMsg(os.toString());
					session.persistOmsRepublishData(omsRepublishData);
					log.info("persisting in omsRepublish data");
				} catch (Exception f) {
					log.error("persisting in omsRepublish data failed" + f);
				}
			}
			if (siebelStatusUpdateResponse.getMessage().equalsIgnoreCase("Failed") && omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				try {
					JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
					Marshaller marshel = context.createMarshaller();
					marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
					marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
					os = new StringWriter();
					marshel.marshal(siebelStatusUpdate, os);
					omsRepublishData.setXmlMsg(os.toString());
					log.info(omsRepublishData.getXmlMsg());
					session.persistOmsRepublishData(omsRepublishData);
					// log.info("persisten in omsRepublish data");
				} catch (Exception f) {
					log.info("Exception while persisting data into siebel" + f.getMessage());
				}
			}
		}
		log.info("********** End of callSeibelWebserviceForCancellation **********************");
	} // End of callSeibelWebserviceForCancellation method

	public void callSeibelWebservice(CustOrderDesc custOrderDesc, BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("calling siebel webservice");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustomerOrderHeaderLevel customerOrderHeaderLevel = new CustomerOrderHeaderLevel();
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		if (omsCustOrdHead.getCustId() != null)
			customerOrderHeaderLevel.setCustId(omsCustOrdHead.getCustId());
		customerOrderHeaderLevel.setOmsCustOrdNo(omsCustOrdNo.toString());
		customerOrderHeaderLevel.setSubCustOrderNo(Long.valueOf(omsCustOrdHead.getSubCustOrderNo()));
		customerOrderHeaderLevel.setCustomerPhoneNo(omsCustOrdHead.getCustPhoneNo());
		customerOrderHeaderLevel.setCustOrderNo(omsCustOrdHead.getCustOrderNo());
		customerOrderHeaderLevel.setCustOrderType(omsCustOrdHead.getCustOrderType());
		customerOrderHeaderLevel.setPaymentStatus(omsCustOrdHead.getOrdPaymentStatus());
		customerOrderHeaderLevel.setOrderRequestorId(omsCustOrdHead.getOrderRequestorId().toString());
		customerOrderHeaderLevel.setDeliveryType(omsCustOrdHead.getDeliveryType());
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
				if (omsCustOrdHead.getOrderCreateReserveInd().equals("R")) {
					BigDecimal loc = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCoFulfillDetail.getItem(), (item.getLineNo())).get(0).getRmsResvLoc();
					if (loc != null && loc.longValue() >= 0) {
						fulfillmentDetails.setRMSResvLoc(loc.longValue());
					}
					String locType = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCoFulfillDetail.getItem(), (item.getLineNo())).get(0).getRmsResvLocType();
					if (locType != null && !locType.isEmpty()) {
						fulfillmentDetails.setRMSResvLocType(locType);
					}
					BigDecimal resvQty = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, omsCoFulfillDetail.getItem(), (item.getLineNo())).get(0).getRmsResvQty();
					if (resvQty != null && resvQty.longValue() >= 0) {
						fulfillmentDetails.setRMSResvQty(resvQty.longValue());
					}
					try {
						BigDecimal poNo = session
								.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
								.get(0).getOrderNo();
						if (poNo != null) {
							fulfillmentDetails.setPoNo(poNo.longValue());
						}
						BigDecimal tsfN0 = session
								.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
								.get(0).getTsfNo();
						if (tsfN0 != null) {
							log.info("tsfN0 " + tsfN0);
							log.info("omsCoFulfillDetail.getFulfillOrderNo() " + omsCoFulfillDetail.getFulfillOrderNo());
							fulfillmentDetails.setTsfNo(session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0).getTsfNo().longValue());
						}
					} catch (Exception e) {
					}
				}
				fulfillmentDetailsList.add(fulfillmentDetails);
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
			omsRepublishData.setErrorMsg("UNABLE TO CALL WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			try {
				JAXBContext context = JAXBContext.newInstance(CustomerOrderHeaderLevel.class);
				Marshaller marshel = context.createMarshaller();
				marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
				Writer os = new StringWriter();
				marshel.marshal(customerOrderHeaderLevel, os);
				omsRepublishData.setXmlMsg(os.toString());
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisten in omsRepublish data");
			} catch (Exception f) {
				log.error("JAXB failed" + e);
			}
		}
		try {
			if (customerOrderHeaderLevelResponse.getMessage().equals("SUCCESS")) {
				log.info("customerOrderHeaderLevelResponse.getMessage()=" + customerOrderHeaderLevelResponse.getMessage());
			} else {
				// add entry into republish_data
				log.info("customerOrderHeaderLevelResponse.getMessage()=" + customerOrderHeaderLevelResponse.getMessage());
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey("ORDER FEED");
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				log.info("before marshalling");
				try {
					JAXBContext context = JAXBContext.newInstance(CustomerOrderHeaderLevel.class);
					Marshaller marshel = context.createMarshaller();
					marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
					marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
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
				log.info("--------------------------------------------------------");
			}
		} catch (Exception e) {
		}
	}

	public void callSeibelWebserviceForReturn(List<CustOrdItmRtVo> custOrdItmRtVoList, String customerNo, BigDecimal omsCustOrdNo) throws SOAPException {
		log.info("inside callSeibelWebserviceForReturn");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		SiebelStatusUpdateInfo siebelStatusUpdate = new SiebelStatusUpdateInfo();
		SiebelStatusUpdateResponse siebelStatusUpdateResponse = null;
		List<SiebelStatusUpdateDetails> siebelStatusUpdateDetailsList = null;
		SiebelStatusUpdateDetails siebelStatusUpdateDetails = null;
		OmsorderupdatebpelprocessClientEp omsorderupdatebpelprocessClientEp = null;
		SiebelStatusUpdateWebService siebelStatusUpdateWebService = null;
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerNo, "S");
		for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
			try {
				siebelStatusUpdate.setCustOrderNo(customerNo);
				siebelStatusUpdate.setApplicationId(omsCustOrdHead.getApplicationId());
				log.info("omsCustOrdHead.getApplicationId() " + omsCustOrdHead.getApplicationId());
				log.info("omsCustOrdNo " + omsCustOrdNo);
				List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
				try {
					siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
					log.info("fetching  record from omsCoFulfillDetail");
					log.info("omsCustOrdNo " + omsCustOrdNo);
					log.info("custOrdItmPkVo.getLineItemNo() " + custOrdItmRtVo.getLineItemNo());
					omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(custOrdItmRtVo.getLineItemNo()), omsCustOrdNo);
					log.info(omsCoFulfillDetailList.size() + " omsCoFulfillDetailList.size()");
					if (omsCoFulfillDetailList.size() > 0) {
						log.info("====================");
						// omsCoFulfillDetail=session.getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(omsCustOrdNo,
						// new BigDecimal(custOrdItmRtVo.getLineItemNo()));
						for (OmsCoFulfillDetail omsCoFulfillDetailList2 : omsCoFulfillDetailList) {
							if (omsCoFulfillDetailList2.getSourceLoc().intValue() == omsCoFulfillDetailList2.getFulfillLoc().intValue()
									&& omsCoFulfillDetailList2.getLineNo().intValue() == custOrdItmRtVo.getLineItemNo()) {
								siebelStatusUpdateDetails.setItem(omsCoFulfillDetailList2.getItem());
								siebelStatusUpdateDetails.setLineNo(omsCoFulfillDetailList2.getLineNo().longValue());
								log.info("Item=" + omsCoFulfillDetailList2.getItem() + "Line no=" + omsCoFulfillDetailList2.getLineNo());
								log.info("omsCoFulfillDetail.getSourceLoc().longValue() " + omsCoFulfillDetailList2.getSourceLoc().longValue());
								siebelStatusUpdateDetails.setSourceLoc(omsCoFulfillDetailList2.getSourceLoc().longValue());
								log.info("omsCoFulfillDetail.getSourceLocType() " + omsCoFulfillDetailList2.getSourceLocType());
								siebelStatusUpdateDetails.setSourceLocType(omsCoFulfillDetailList2.getSourceLocType());
								log.info("omsCoFulfillDetail.getFulfillLocType() " + omsCoFulfillDetailList2.getFulfillLocType());
								siebelStatusUpdateDetails.setFulfillLocType(omsCoFulfillDetailList2.getFulfillLocType());
								log.info("omsCoFulfillDetail.getFulfillLoc().longValue() " + omsCoFulfillDetailList2.getFulfillLoc().longValue());
								siebelStatusUpdateDetails.setFulfillLoc(omsCoFulfillDetailList2.getFulfillLoc().longValue());
							}
						}
					} else {
						log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						siebelStatusUpdateDetails.setItem(omsCoFulfillDetailList.get(0).getItem());
						siebelStatusUpdateDetails.setLineNo(omsCoFulfillDetailList.get(0).getLineNo().longValue());
						log.info("Item=" + omsCoFulfillDetailList.get(0).getItem() + "Line no=" + omsCoFulfillDetailList.get(0).getLineNo());
						log.info("omsCoFulfillDetail.getSourceLoc().longValue() " + omsCoFulfillDetailList.get(0).getSourceLoc().longValue());
						siebelStatusUpdateDetails.setSourceLoc(omsCoFulfillDetailList.get(0).getSourceLoc().longValue());
						log.info("omsCoFulfillDetail.getSourceLocType() " + omsCoFulfillDetailList.get(0).getSourceLocType());
						siebelStatusUpdateDetails.setSourceLocType(omsCoFulfillDetailList.get(0).getSourceLocType());
						log.info("omsCoFulfillDetail.getFulfillLocType() " + omsCoFulfillDetailList.get(0).getFulfillLocType());
						siebelStatusUpdateDetails.setFulfillLocType(omsCoFulfillDetailList.get(0).getFulfillLocType());
						log.info("omsCoFulfillDetail.getFulfillLoc().longValue() " + omsCoFulfillDetailList.get(0).getFulfillLoc().longValue());
						siebelStatusUpdateDetails.setFulfillLoc(omsCoFulfillDetailList.get(0).getFulfillLoc().longValue());
					}
				} catch (Exception e) {
				}
				if (omsCustOrdHead.getSubCustOrderNo() == null) {
					siebelStatusUpdate.setSubCustOrderNo("1");
				} else {
					siebelStatusUpdate.setSubCustOrderNo(omsCustOrdHead.getSubCustOrderNo());
				}
				log.info("custId " + omsCustOrdHead.getCustId());
				siebelStatusUpdate.setCustId(omsCustOrdHead.getCustId());
				siebelStatusUpdate.setOmsCustomerOrderNo(omsCustOrdNo.longValue());
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = null;
				try {
					datatypeFactory = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				log.info("now " + now);
				siebelStatusUpdate.setCancelDatetime(now);
				// ==============================================================================
				DatatypeFactory datatypeFactory2 = null;
				try {
					datatypeFactory2 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar now2 = datatypeFactory2.newXMLGregorianCalendar(gregorianCalendar);
				log.info("now2 " + now2);
				siebelStatusUpdate.setConsumerDlyTime(now2);
				// ==============================================================================
				DatatypeFactory datatypeFactory3 = null;
				try {
					datatypeFactory3 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar now3 = datatypeFactory3.newXMLGregorianCalendar(gregorianCalendar);
				log.info("now3 " + now3);
				siebelStatusUpdate.setCloseDatetime(now3);
				siebelStatusUpdateDetailsList = siebelStatusUpdate.getCustomerOrderStatusUpdateDetails();
				log.info("Qty " + new BigDecimal(custOrdItmRtVo.getReturnedQuantity().intValue()));
				siebelStatusUpdateDetails.setQty(new BigDecimal(custOrdItmRtVo.getReturnedQuantity().intValue()));
				siebelStatusUpdateDetails.setEventId("RTN");
				siebelStatusUpdateDetails.setEventComments("Returned from POS");
				GregorianCalendar gregorianCalendar1 = new GregorianCalendar();
				DatatypeFactory datatypeFactory1 = null;
				try {
					datatypeFactory1 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar now1 = datatypeFactory1.newXMLGregorianCalendar(gregorianCalendar1);
				siebelStatusUpdateDetails.setUpdateDatetime(now1);
				log.info("now1 " + now1);
				siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
				log.info("siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size());
			} catch (Exception e) {
				log.error("-->SIEBEL customer order status update failed." + e);
			}
		}
		Writer os = null;
		try {
			omsorderupdatebpelprocessClientEp = new OmsorderupdatebpelprocessClientEp();
			siebelStatusUpdateWebService = omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt();

			JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
			Marshaller marshel = context.createMarshaller();
			marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
			os = new StringWriter();
			marshel.marshal(siebelStatusUpdate, os);
			log.info("Request XML for Return" + os.toString());

			siebelStatusUpdateResponse = siebelStatusUpdateWebService.processSiebelStatusUpdate(siebelStatusUpdate);
			log.info("Response from siebel is" + siebelStatusUpdateResponse.getMessage());
		} catch (Exception e) {
			log.error("Publishing to seibel failed" + e);
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(customerNo);
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			try {

				omsRepublishData.setXmlMsg(os.toString());
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisting in omsRepublish data");
			} catch (Exception f) {
				log.error("persisting in omsRepublish data failed" + f);
			}
		}
		if (siebelStatusUpdateResponse.getMessage().equalsIgnoreCase("Failed") && omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(omsCustOrdHead.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			try {
				JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
				Marshaller marshel = context.createMarshaller();
				marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
				os = new StringWriter();
				marshel.marshal(siebelStatusUpdate, os);
				omsRepublishData.setXmlMsg(os.toString());
				log.info(omsRepublishData.getXmlMsg());
				session.persistOmsRepublishData(omsRepublishData);
				// log.info("persisten in omsRepublish data");
			} catch (Exception f) {
			}
			// }
		}
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
		try {
			CreateInvBackOrdColDesc colDesc = new CreateInvBackOrdColDesc();
			colDesc.setInvBackOrdColDesc(invBackOrdColDesc);
			OracleBaseAPIUtil.getOracleRMSClient().createInvBackOrdColDesc(colDesc);
			log.info("called BO Successfully");
		} catch (Exception e) {
			log.info("Exception while calling BO " + e.getMessage());
		}
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
		log.info("temp.get(0).getSourceLocId().longValue() " + temp.get(0).getSourceLocId().longValue());
		log.info("temp.get(0).getSourceLocationType() " + temp.get(0).getSourceLocationType());
		if (temp.get(0).getSourceLocId().longValue() != temp.get(0).getFulfillLocId().longValue()) {
			fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue());
			fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(temp.get(0).getSourceLocationType())); // **
		} else {
			log.info("cancelling reservation ");
			fulfilOrdRef.setSourceLocId(null);
			fulfilOrdRef.setSourceLocType(null); // **
		}
		for (OmsTempCoFo omsTempCoFo : temp) {
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			log.info("omsTempCoFo.getFoConfQty() " + omsTempCoFo.getOrderQty());
			fulfilOrdDtlRef.setCancelQtySuom(omsTempCoFo.getFoConfQty()); // check
			fulfilOrdDtlRef.setItem(omsTempCoFo.getItem());
			log.info("item " + omsTempCoFo.getItem());
			// fulfilOrdDtlRef.setRefItem(value);
			OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		}
		// fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));
		log.info("adding into fulfilOrdRef");
		fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
		log.info("calling cancelFulfilOrdColRef");
		log.info("fulfilOrdColRef " + fulfilOrdColRef.getFulfilOrdRef().get(0).getCustomerOrderNo());
		try {
			CancelFulfilOrdColRef cancelFulfilOrdColRef = new CancelFulfilOrdColRef();
			cancelFulfilOrdColRef.setFulfilOrdColRef(fulfilOrdColRef);
			InvocationSuccess result = OracleBaseAPIUtil.getOracleRMSClient().cancelFulfilOrdColRef(cancelFulfilOrdColRef).getInvocationSuccess();
			log.info("Result from rms---" + result.getSuccessMessage());
		} catch (Exception e) {
			log.info("Exception " + e);
		}
	}

	public String approveTransfers(BigDecimal tsfNo, BigDecimal sourceLoc) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException, SOAPException {
		String status = "F";
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			String waitTime = session.getOmsSystemParametersFindIndValue("TSF_APPROVAL_WAIT_TIME", "OMS_SYSTEM_OPTION");
			log.info("waitTime" + waitTime);
			Thread.sleep(Long.valueOf(waitTime));
		} catch (InterruptedException e) {
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
		return status;
	}

	public FodHdrColDesc callSIMlookupFulfillmentOrderDeliveryHeaders(long intFulfillOrderId)
			throws com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException, SOAPException {
		StrFordRef strFordRef = new StrFordRef();
		strFordRef.setIntFulfillmentOrderId(intFulfillOrderId);
		try {
			
			LookupFulfillmentOrderDeliveryHeaders headers = new LookupFulfillmentOrderDeliveryHeaders();
			headers.setStrFordRef(strFordRef);
			FodHdrColDesc fodHdrColDesc = OracleBaseAPIUtil.getOracleSIMClient().lookupFulfillmentOrderDeliveryHeaders(headers).getFodHdrColDesc();
			return fodHdrColDesc;
		} catch (Exception e) {
			throw new SOAPException("Error in calling sim webservice lookupFulfillmentOrderDeliveryHeaders");
		}
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

	public OmsOrderStatusUpdateHeader getOmsOrderStatusUpdateHeaderForReturn(List<CustOrdItmRtVo> custOrdItmRtVoList, String customerNo, BigDecimal omsCustOrdNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderReturn = new OmsOrderStatusUpdateHeader();
		ArrayOfOmsOrderStatusUpdateDetail arrayOfOmsOrderStatusUpdateDetailReturn = new ArrayOfOmsOrderStatusUpdateDetail();
		OmsOrderDetailStatus omsOrderDetailStatusReturn = null;
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerNo, "S");
		omsOrderStatusUpdateHeaderReturn.setEntityId(omsCustOrdHead.getEntityId());
		omsOrderStatusUpdateHeaderReturn.setApplicationId(omsCustOrdHead.getApplicationId());
		omsOrderStatusUpdateHeaderReturn.setOrderId(omsCustOrdHead.getCustOrderNo());
		omsOrderStatusUpdateHeaderReturn.setOmsOrderId(omsCustOrdHead.getOmsCustOrdNo() + "");
		omsOrderStatusUpdateHeaderReturn.setDeliveryDate(toXMLGregorianCalendar(new Date()));
		Timestamp returnHeaderStatusDate = omsCustOrdHead.getLastUpdateDatetime();
		if (null == returnHeaderStatusDate) {
			returnHeaderStatusDate = new Timestamp(new Date().getTime());
		}
		omsOrderStatusUpdateHeaderReturn.setUpdateDate(toXMLGregorianCalendar(new Date(returnHeaderStatusDate.getTime())));
		for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
			omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(custOrdItmRtVo.getLineItemNo()), omsCustOrdNo);
			if (omsCoFulfillDetailList.size() > 0) {
				// Fulfill from store to store..........
				for (OmsCoFulfillDetail omsCoFulfillDetailRecordList : omsCoFulfillDetailList) {
					if (omsCoFulfillDetailRecordList.getSourceLoc().intValue() == omsCoFulfillDetailRecordList.getFulfillLoc().intValue()
							&& omsCoFulfillDetailRecordList.getLineNo().intValue() == custOrdItmRtVo.getLineItemNo()) {
						omsOrderDetailStatusReturn = new OmsOrderDetailStatus();
						omsOrderDetailStatusReturn.setOrderDetailId(omsCoFulfillDetailRecordList.getLineNo().longValue());
						omsOrderDetailStatusReturn.setProductSku(omsCoFulfillDetailRecordList.getItem());
						omsOrderDetailStatusReturn.setQuantity(custOrdItmRtVo.getReturnedQuantity().intValue());
						omsOrderDetailStatusReturn.setSourceId(omsCoFulfillDetailRecordList.getSourceLoc().intValue());
						omsOrderDetailStatusReturn.setSourceType(omsCoFulfillDetailRecordList.getSourceLocType());
						omsOrderDetailStatusReturn.setFulfillId(omsCoFulfillDetailRecordList.getFulfillLoc().intValue());
						omsOrderDetailStatusReturn.setFulfillType(omsCoFulfillDetailRecordList.getFulfillLocType());
						omsOrderDetailStatusReturn.setEventId("RTN");
						omsOrderDetailStatusReturn.setEventComment("Return From POS");
						omsOrderDetailStatusReturn.setEventReferenceId(omsCustOrdNo.intValue());
						Timestamp returnlastupdatetimeReturn = omsCoFulfillDetailRecordList.getLastUpdateDatetime();
						if (null == returnlastupdatetimeReturn) {
							returnlastupdatetimeReturn = new Timestamp(new java.util.Date().getTime());
						}
						omsOrderDetailStatusReturn.setUpdateDate(toXMLGregorianCalendar(new Date(returnlastupdatetimeReturn.getTime())));
						arrayOfOmsOrderStatusUpdateDetailReturn.getOrderDetailStatus().add(omsOrderDetailStatusReturn);
					} else {
						log.info("++++++++++++++++++Other than reservation+++++++++++++++");
						omsOrderDetailStatusReturn = new OmsOrderDetailStatus();
						omsOrderDetailStatusReturn.setOrderDetailId(omsCoFulfillDetailRecordList.getLineNo().longValue());
						omsOrderDetailStatusReturn.setProductSku(omsCoFulfillDetailRecordList.getItem());
						omsOrderDetailStatusReturn.setQuantity(custOrdItmRtVo.getReturnedQuantity().intValue());
						omsOrderDetailStatusReturn.setSourceId(omsCoFulfillDetailRecordList.getSourceLoc().intValue());
						omsOrderDetailStatusReturn.setSourceType(omsCoFulfillDetailRecordList.getSourceLocType());
						omsOrderDetailStatusReturn.setFulfillId(omsCoFulfillDetailRecordList.getFulfillLoc().intValue());
						omsOrderDetailStatusReturn.setFulfillType(omsCoFulfillDetailRecordList.getFulfillLocType());
						omsOrderDetailStatusReturn.setEventId("RTN");
						omsOrderDetailStatusReturn.setEventComment("Return From POS");
						omsOrderDetailStatusReturn.setEventReferenceId(omsCustOrdNo.intValue());
						Timestamp returnlastupdatetimeReturn = omsCoFulfillDetailRecordList.getLastUpdateDatetime();
						if (null == returnlastupdatetimeReturn) {
							returnlastupdatetimeReturn = new Timestamp(new java.util.Date().getTime());
						}
						omsOrderDetailStatusReturn.setUpdateDate(toXMLGregorianCalendar(new Date(returnlastupdatetimeReturn.getTime())));
						arrayOfOmsOrderStatusUpdateDetailReturn.getOrderDetailStatus().add(omsOrderDetailStatusReturn);
					}
				}
			}
		} // end of the for loop
		omsOrderStatusUpdateHeaderReturn.setOrderDetailStatuses(arrayOfOmsOrderStatusUpdateDetailReturn);
		return omsOrderStatusUpdateHeaderReturn;
	} // end of the method..

	public OmsOrderStatusUpdateHeader getOmsOrderStatusUpdateHeaderForPickup(String custOrderNo, String custId, List<CustOrdItmPkVo> pickUpItemsList, Map<String, BigDecimal> seibelDataForPickUp)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderForPickup = new OmsOrderStatusUpdateHeader();
		OmsOrderDetailStatus OmsOrderDetailStatusPickup = new OmsOrderDetailStatus();
		ArrayOfOmsOrderStatusUpdateDetail arrayOfOmsOrderStatusUpdateDetailPickup = new ArrayOfOmsOrderStatusUpdateDetail();
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(custOrderNo, "S");
		log.info("omsCustOrdNo " + omsCustOrdNo);
		log.info("seibelDataForPickUp.keySet() " + seibelDataForPickUp.keySet());
		log.info("pickUpItemsList.size() " + pickUpItemsList.size());
		log.info("inside custOrdItmPkVo loop");
		if (null != omsCustOrdHead.getSubCustOrderNo()) {
		}
		Timestamp headerTimestampLastupdate = omsCustOrdHead.getLastUpdateDatetime();
		if (null == headerTimestampLastupdate) {
			headerTimestampLastupdate = new Timestamp(new Date().getTime());
		}
		omsOrderStatusUpdateHeaderForPickup.setEntityId(omsCustOrdHead.getEntityId());
		omsOrderStatusUpdateHeaderForPickup.setApplicationId(omsCustOrdHead.getApplicationId());
		omsOrderStatusUpdateHeaderForPickup.setOmsOrderId(omsCustOrdHead.getOmsCustOrdNo() + "");
		omsOrderStatusUpdateHeaderForPickup.setOrderId(omsCustOrdHead.getCustOrderNo());
		omsOrderStatusUpdateHeaderForPickup.setDeliveryDate(toXMLGregorianCalendar(new Date()));
		omsOrderStatusUpdateHeaderForPickup.setUpdateDate(toXMLGregorianCalendar(new Date(headerTimestampLastupdate.getTime())));
		for (String key : seibelDataForPickUp.keySet()) {
			String keyData[] = key.split(",");
			BigDecimal deliveredQty = seibelDataForPickUp.get(key);
			BigDecimal fulFillOrderNo = new BigDecimal(keyData[0]);
			BigDecimal lineNo = new BigDecimal(keyData[1]);
			BigDecimal sourceLoc = new BigDecimal(keyData[2]);
			String sourcelocType = keyData[3];
			BigDecimal fulFillLoc = new BigDecimal(keyData[4]);
			String fulFillLocType = keyData[5];
			String item = keyData[6];
			log.info("========fulFillOrderNo=======" + fulFillOrderNo);
			log.info("=========lineNo===========" + lineNo);
			log.info("=========sourceLoc===========" + sourceLoc);
			log.info("=======sourceClocType==========" + sourcelocType);
			log.info("=========fulFillLoc========" + fulFillLoc);
			log.info("=========fulFillLocType======" + fulFillLocType);
			log.info("========item==============" + item);
			log.info("========Delivered Qty============" + deliveredQty);
			OmsOrderDetailStatusPickup = new OmsOrderDetailStatus();
			OmsOrderDetailStatusPickup.setOrderDetailId(lineNo.intValue());
			OmsOrderDetailStatusPickup.setProductSku(item);
			OmsOrderDetailStatusPickup.setQuantity(deliveredQty.intValue());
			OmsOrderDetailStatusPickup.setSourceId(sourceLoc.intValue());
			OmsOrderDetailStatusPickup.setSourceType(sourcelocType);
			OmsOrderDetailStatusPickup.setFulfillId(fulFillLoc.intValue());
			OmsOrderDetailStatusPickup.setFulfillType(fulFillLocType);
			OmsOrderDetailStatusPickup.setEventComment("Delivered to customer");
			OmsOrderDetailStatusPickup.setEventId("DL");
			OmsOrderDetailStatusPickup.setEventReferenceId(omsCustOrdNo.intValue());
			GregorianCalendar lastupdatetime = new GregorianCalendar();
			DatatypeFactory lastupdatetimedatatypeFactory1 = null;
			try {
				lastupdatetimedatatypeFactory1 = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				log.info("-----------------------------------");
			}
			XMLGregorianCalendar lastupdatetimeXMLdatatypeFactory = lastupdatetimedatatypeFactory1.newXMLGregorianCalendar(lastupdatetime);
			OmsOrderDetailStatusPickup.setUpdateDate(lastupdatetimeXMLdatatypeFactory);
			arrayOfOmsOrderStatusUpdateDetailPickup.getOrderDetailStatus().add(OmsOrderDetailStatusPickup);
		}
		log.info(" OrderDetailpkcup Detail size is " + arrayOfOmsOrderStatusUpdateDetailPickup.getOrderDetailStatus().size());
		omsOrderStatusUpdateHeaderForPickup.setOrderDetailStatuses(arrayOfOmsOrderStatusUpdateDetailPickup);
		return omsOrderStatusUpdateHeaderForPickup;
	} // end of method ....

	public OmsOrderStatusUpdateHeader getOmsOrderStatusUpdateHeaderForCancellation(BigDecimal omsCancelId, String custId, List<CustOrdItmPkVo> cancelItemList,
			TreeMap<String, BigDecimal> backorderTreeMap) throws SOAPException {
		OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderForCancellation = new OmsOrderStatusUpdateHeader();
		OmsOrderDetailStatus omsOrderDetailStatusForCancellation = null;
		ArrayOfOmsOrderStatusUpdateDetail arrayOrderDetailStatusForCancellation = new ArrayOfOmsOrderStatusUpdateDetail();
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("cancelItemList " + cancelItemList.size());
		log.info("omsCancelId " + omsCancelId);
		OmsCoCancelHead omsCoCancelHead = session.getOmsCoCancelHeadFindCustOrderNoByomsCancelId(omsCancelId);
		List<OmsCoFoCancel> omsCoFoCancelList = session.getOmsCoFoCancelFindByOmsCancelId(omsCancelId);
		OmsCoCancelItem omsCoCancelItem = null;
		OmsCustOrdHead omsCustOrdHeadRecord = null;
		omsCustOrdHeadRecord = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(omsCoCancelHead.getCustOrdNo(), "S");
		BigDecimal omsCustOrdNo = omsCustOrdHeadRecord.getOmsCustOrdNo();
		omsOrderStatusUpdateHeaderForCancellation.setEntityId(omsCustOrdHeadRecord.getEntityId());
		omsOrderStatusUpdateHeaderForCancellation.setApplicationId(omsCustOrdHeadRecord.getApplicationId());
		omsOrderStatusUpdateHeaderForCancellation.setOmsOrderId(omsCustOrdHeadRecord.getOmsCustOrdNo() + "");
		omsOrderStatusUpdateHeaderForCancellation.setOrderId(omsCustOrdHeadRecord.getCustOrderNo());
		omsOrderStatusUpdateHeaderForCancellation.setDeliveryDate(toXMLGregorianCalendar(new Date()));
		Timestamp headerCancellationlastupdatetime = omsCustOrdHeadRecord.getLastUpdateDatetime();
		if (null == headerCancellationlastupdatetime) {
			headerCancellationlastupdatetime = new Timestamp(new Date().getTime());
		}
		omsOrderStatusUpdateHeaderForCancellation.setUpdateDate(toXMLGregorianCalendar(new Date(headerCancellationlastupdatetime.getTime())));
		// Code added for bug 2787
		log.info("========== backorderTreeMap.keySet() : " + backorderTreeMap.keySet());
		log.info("Sending Sibel Status while BackOrder Cancellation in POS SIEBEL customer order status update");
		if (backorderTreeMap.keySet() != null && backorderTreeMap.keySet().size() > 0) {
			for (String key : backorderTreeMap.keySet()) {
				String keyData[] = key.split(",");
				BigDecimal sourceQty = backorderTreeMap.get(key);
				BigDecimal lineno = new BigDecimal(keyData[0]);
				String item = keyData[1];
				BigDecimal sourceLoc = new BigDecimal(keyData[2]);
				String sourcelocType = keyData[3];
				BigDecimal fulFillLoc = new BigDecimal(keyData[4]);
				String fulFillLocType = keyData[5];
				log.info("Source Qty : " + sourceQty);
				log.info("Line No : " + lineno);
				log.info("Item : " + item);
				log.info("Source Loc : " + sourceLoc);
				log.info("Source Loc Type : " + sourcelocType);
				log.info("Fulfil Loc : " + fulFillLoc);
				log.info("Fulfil Loc Type : " + fulFillLocType);
				omsOrderDetailStatusForCancellation = new OmsOrderDetailStatus();
				omsOrderDetailStatusForCancellation.setOrderDetailId(lineno.intValue());
				omsOrderDetailStatusForCancellation.setProductSku(item);
				omsOrderDetailStatusForCancellation.setQuantity(sourceQty.intValue());
				omsOrderDetailStatusForCancellation.setSourceId(sourceLoc.intValue());
				omsOrderDetailStatusForCancellation.setSourceType(sourcelocType);
				omsOrderDetailStatusForCancellation.setFulfillId(fulFillLoc.intValue());
				omsOrderDetailStatusForCancellation.setFulfillType(fulFillLocType);
				omsOrderDetailStatusForCancellation.setEventId("CAC");
				omsOrderDetailStatusForCancellation.setEventComment("Canceled by customer");
				omsOrderDetailStatusForCancellation.setEventReferenceId(omsCancelId.intValue());
				GregorianCalendar cancelationlastupdategregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory4 = null;
				try {
					datatypeFactory4 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
				}
				XMLGregorianCalendar lastupdatetimeCancelationtime = datatypeFactory4.newXMLGregorianCalendar(cancelationlastupdategregorianCalendar);
				omsOrderDetailStatusForCancellation.setUpdateDate(lastupdatetimeCancelationtime);
				arrayOrderDetailStatusForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusForCancellation);
			} // end of back order for loop
		}
		log.info("Size of omsCoFoCancelList : " + omsCoFoCancelList.size());
		if (omsCoFoCancelList.size() > 0) {
			for (CustOrdItmPkVo custOrdItmPkVo : cancelItemList) {
				try {
					List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
					omsCoCancelItem = session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCancelId, new BigDecimal(custOrdItmPkVo.getLineItemNo()));
					log.info("item " + omsCoCancelItem.getItem());
					log.info("LineNo " + omsCoCancelItem.getLineNo());
					omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(custOrdItmPkVo.getLineItemNo()), omsCustOrdNo);
					log.info(omsCoFulfillDetailList.size() + " omsCoFulfillDetailList.size()");
					if (omsCoFulfillDetailList != null && omsCoFulfillDetailList.size() > 0) {
						for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
							for (OmsCoFulfillDetail fulfillDetail : omsCoFulfillDetailList) {
								if (omsCoFoCancel.getLineNo().intValue() == fulfillDetail.getLineNo().intValue()
										&& omsCoFoCancel.getFulfillOrderNo().intValue() == fulfillDetail.getFulfillOrderNo().intValue() && omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
									omsOrderDetailStatusForCancellation = new OmsOrderDetailStatus();
									omsOrderDetailStatusForCancellation.setOrderDetailId(omsCoFoCancel.getLineNo().longValue());
									omsOrderDetailStatusForCancellation.setProductSku(omsCoFoCancel.getItem());
									omsOrderDetailStatusForCancellation.setQuantity(omsCoFoCancel.getFoCancelledOty().intValue());
									omsOrderDetailStatusForCancellation.setSourceId(fulfillDetail.getSourceLoc().intValue());
									omsOrderDetailStatusForCancellation.setSourceType(fulfillDetail.getSourceLocType());
									omsOrderDetailStatusForCancellation.setFulfillId(fulfillDetail.getFulfillLoc().intValue());
									omsOrderDetailStatusForCancellation.setFulfillType(fulfillDetail.getFulfillLocType());
									omsOrderDetailStatusForCancellation.setEventId("CAC");
									omsOrderDetailStatusForCancellation.setEventComment("Canceled by customer");
									omsOrderDetailStatusForCancellation.setEventReferenceId(omsCancelId.longValue());
									GregorianCalendar lastupdatecancellationgregorianCalendar = new GregorianCalendar();
									DatatypeFactory datatypeFactory1 = null;
									try {
										datatypeFactory1 = DatatypeFactory.newInstance();
									} catch (DatatypeConfigurationException e) {
										log.info("------------------------" + e);
									}
									XMLGregorianCalendar cancellationLastUpdatedatetime = datatypeFactory1.newXMLGregorianCalendar(lastupdatecancellationgregorianCalendar);
									omsOrderDetailStatusForCancellation.setUpdateDate(cancellationLastUpdatedatetime);
									arrayOrderDetailStatusForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusForCancellation);
								} // Omscofo cancel condition
							} // OmsCoFulfillDetail for loop
						} // OmsCoFoCancel
					} // if OmsCoFulfillDetail .size()>0
				} catch (Exception e) {
					log.error("-->SIEBEL customer order status update failed." + e.getMessage());
				}
			}
		} // end of omsCoFoCancelList.size()>0
			// Updated of 3004 Production bug for CAC event for Non-Inventory or shipping
			// charge Item
		for (CustOrdItmPkVo custOrdItmPkVo : cancelItemList) {
			log.info("omscancelId " + omsCancelId + "checking is any the shipping and non-inventory item is present in OmsCustOrdItem");
			List<OmsCustOrdItem> omsCustordItem = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, new BigDecimal(custOrdItmPkVo.getLineItemNo()));
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(omsCustordItem.get(0).getItem());
			String inventoryIndn = session.getItemMasterFindInventoryInd(omsCustordItem.get(0).getItem(), itemDept);
			if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
				omsOrderDetailStatusForCancellation = new OmsOrderDetailStatus();
				omsOrderDetailStatusForCancellation.setOrderDetailId(omsCustordItem.get(0).getLineNo().intValue());
				omsOrderDetailStatusForCancellation.setProductSku(omsCustordItem.get(0).getItem());
				omsOrderDetailStatusForCancellation.setQuantity(custOrdItmPkVo.getCancelledQuantity().intValue());
				omsOrderDetailStatusForCancellation.setSourceId(omsCustOrdHeadRecord.getOrderRequestorId().intValue());
				omsOrderDetailStatusForCancellation.setSourceType("ST");
				omsOrderDetailStatusForCancellation.setFulfillId(omsCustOrdHeadRecord.getOrderRequestorId().intValue());
				omsOrderDetailStatusForCancellation.setFulfillType("S");
				omsOrderDetailStatusForCancellation.setEventId("CAC");
				omsOrderDetailStatusForCancellation.setEventComment("Canceled by Customer");
				omsOrderDetailStatusForCancellation.setEventReferenceId(omsCancelId.longValue());
				GregorianCalendar lastUpdatetimeOfnonInventoryItemgregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory7 = null;
				try {
					datatypeFactory7 = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.info("----------------------" + e);
				}
				XMLGregorianCalendar noninventoryitemLastUpdate = datatypeFactory7.newXMLGregorianCalendar(lastUpdatetimeOfnonInventoryItemgregorianCalendar);
				omsOrderDetailStatusForCancellation.setUpdateDate(noninventoryitemLastUpdate);
				arrayOrderDetailStatusForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusForCancellation);
			} // non inentory item check condition
		} // for loop condition... check..
		omsOrderStatusUpdateHeaderForCancellation.setOrderDetailStatuses(arrayOrderDetailStatusForCancellation);
		return omsOrderStatusUpdateHeaderForCancellation;
	}

	protected String getDeliveryLeadTimeForSMALLorBIG(String city, String classification, Long code, int maxpriority) throws SOAPException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int leadTime = 0;
		String addedTime = null;
		String query = " select DELV_LEAD_TIME from OMS_CUST_ORDER_DLT where upper(city)=? and upper(classification)=? and wh_code=? AND STATUS = 'A' ";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city.toUpperCase());
			pstmt.setString(2, classification.toUpperCase());
			pstmt.setLong(3, code);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				leadTime = rs.getBigDecimal("DELV_LEAD_TIME").intValue();
			}
			if (leadTime == 0) {
				leadTime = 5;
			}
			log.info("lead time from DB " + leadTime);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Using today's date
			c.add(Calendar.DATE, leadTime);
			addedTime = sdf.format(c.getTime());
			log.info("lead time after add with system date " + addedTime);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for deliveryLeadTime " + e.getMessage());
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
		return addedTime;
	}

	private String generateRequesXML(OmsOrderStatusUpdateHeader statusHeader) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("oms", "http://www.extra.com/Services/OmsStatus");
		envelope.addNamespaceDeclaration("ext", "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms");

		SOAPBody soapBody = envelope.getBody();

		OmsOrderStatusUpdateHeader header = new OmsOrderStatusUpdateHeader();
		header.setEntityId("E_COMMERCE");

		SOAPElement updateOrderElem = soapBody.addChildElement("UpdateOrderStatus", "oms");
		SOAPElement orderStatusElem = updateOrderElem.addChildElement("orderStatus", "oms");

		orderStatusElem.addChildElement("EnitityId", "ext").addTextNode(statusHeader.getEntityId());
		orderStatusElem.addChildElement("ApplicationId", "ext").addTextNode(statusHeader.getApplicationId());
		orderStatusElem.addChildElement("OrderId", "ext").addTextNode(statusHeader.getOrderId());
		if (statusHeader.getSubOrderId() != null) {
			orderStatusElem.addChildElement("SubOrderId", "ext").addTextNode(statusHeader.getSubOrderId());
		}
		orderStatusElem.addChildElement("OmsOrderId", "ext").addTextNode(statusHeader.getOmsOrderId());
		if (statusHeader.getDeliveryDate() != null) {
			orderStatusElem.addChildElement("DeliveryDate", "ext").addTextNode(statusHeader.getDeliveryDate().toXMLFormat());
		}
		if (statusHeader.getUpdateDate() != null) {
			orderStatusElem.addChildElement("UpdateDate", "ext").addTextNode(statusHeader.getUpdateDate().toXMLFormat());
		}

		SOAPElement detailElem = orderStatusElem.addChildElement("OrderDetailStatuses", "ext");
		for (OmsOrderDetailStatus detailStatus : statusHeader.getOrderDetailStatuses().getOrderDetailStatus()) {
			SOAPElement orderDetail = detailElem.addChildElement("OrderDetailStatus", "ext");
			orderDetail.addChildElement("OrderDetailId", "ext").addTextNode(String.valueOf(detailStatus.getOrderDetailId()));
			orderDetail.addChildElement("ProductSku", "ext").addTextNode(detailStatus.getProductSku());
			orderDetail.addChildElement("Quantity", "ext").addTextNode(String.valueOf(detailStatus.getQuantity()));
			orderDetail.addChildElement("SourceType", "ext").addTextNode(detailStatus.getSourceType());
			orderDetail.addChildElement("SourceId", "ext").addTextNode(String.valueOf(detailStatus.getSourceId()));
			orderDetail.addChildElement("FulfillType", "ext").addTextNode(detailStatus.getFulfillType());
			orderDetail.addChildElement("FulfillId", "ext").addTextNode(String.valueOf(detailStatus.getFulfillId()));
			orderDetail.addChildElement("EventId", "ext").addTextNode(detailStatus.getEventId());
			orderDetail.addChildElement("EventComment", "ext").addTextNode(detailStatus.getEventComment());
			orderDetail.addChildElement("EventReferenceId", "ext").addTextNode(String.valueOf(detailStatus.getEventReferenceId()));
			if (detailStatus.getUpdateDate() != null) {
				orderDetail.addChildElement("UpdateDate", "ext").addTextNode(detailStatus.getUpdateDate().toXMLFormat());
			}
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		soapMessage.saveChanges();
		soapMessage.writeTo(stream);
		String message = stream.toString();
		try {
			stream.close();
		} catch (Exception e) {
			log.warn("Error while closing the byte stream", e);
		}
		return message;
	}

	public void saveOmsWSeibelRequest(OmsOrderStatusUpdateHeader omsOrderStatus, OMSUtilSessionEJB session) throws Exception {
		OmsWSPublishData wsData = new OmsWSPublishData();
		Timestamp curTime = new Timestamp(System.currentTimeMillis());
		wsData.setApplicationId(omsOrderStatus.getApplicationId());
		wsData.setAttemtCnt(0);
		wsData.setFirstAttemptDateTime(curTime);
		wsData.setLastAttemptDateTme(curTime);
		wsData.setMessage(generateRequesXML(omsOrderStatus));
		wsData.setTransactionKey(omsOrderStatus.getOrderId());
		wsData.setWebServiceId("16");
		log.info("Generated xml for request --> " + wsData.getMessage());
		log.info("Saving to request to db");
		session.persistOmsWSPublishData(wsData);
		log.info("Web service saved successfully to db");
	}

	private static ICarreraClient getCarreraClient() throws Exception {
		return OracleBaseAPIUtil.getCarreraClient();
	}

	public void callCarreraCancellation(OmsTempCoFo omsTempCoFo) {
		Connection con = null;
		CallableStatement pstmt = null;
		PreparedStatement prepStatement = null;
		BigDecimal tsfN0 = null;
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			tsfN0 = session.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsTempCoFo.getFulfillOrderNo().toString(), omsTempCoFo.getSourceLocId(), omsTempCoFo.getFulfillLocId()).get(0).getTsfNo();
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareCall(" { ? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) } ");
			pstmt.registerOutParameter(1, Types.VARCHAR);
			pstmt.registerOutParameter(2, Types.VARCHAR);
			pstmt.setBigDecimal(3, tsfN0);
			pstmt.setString(4, omsTempCoFo.getItem());
			pstmt.setLong(5, omsTempCoFo.getFoConfQty().longValue());
			pstmt.execute();
			// rs = stmt.getString(1);
			String errorTextflag = pstmt.getString(2);
			log.info("FLAG : " + errorTextflag);
			String errorReturned = pstmt.getString(1);
			log.info("FLAG : " + errorReturned);
			if (errorReturned.equals("Success") && errorTextflag.equals("Success")) {
				log.info("Package call is successful");
				log.info("Update in Ordcust detail cancel qty");

				String query = "UPDATE ORDCUST_DETAIL set QTY_CANCELLED_SUOM = ? WHERE ordcust_no = (select ordcust_no from ordcust where tsf_no = ?)";
				prepStatement = con.prepareStatement(query);
				prepStatement.setLong(1, omsTempCoFo.getFoConfQty().longValue());
				prepStatement.setBigDecimal(2, tsfN0);
				log.info("setted tsfNo in preparedStatement");
				prepStatement.executeUpdate();
			} else {
				log.warn("Package for Rollback of Carrera Cancellation Stub... Not working for tsf no " + tsfN0 + " is " + errorReturned);
			}
		} catch (Exception e) {
			log.warn("Error while cancelling the carrera fulfilment for tsf no " + tsfN0, e);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				// EAT Exception
			}
			try {
				prepStatement.close();
			} catch (Exception e) {
				// EAT Exception
			}
			try {
				con.close();
			} catch (Exception e) {
				// EAT Exception
			}
		}
	}
}
