package com.logicinfo.oms.model;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFoCancel;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.util.ArrayOfOmsOrderStatusUpdateDetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderDetailStatus;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreItmMod;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreModVo;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriStatus;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriType;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderService;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryService;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.FulfillmentOrderReversePickPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.FulfillmentOrderReversePickService;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentService;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;

import retail.siebel.com.integration.OmsorderupdatebpelprocessClientEp;
import retail.siebel.com.integration.SiebelStatusUpdateDetails;
import retail.siebel.com.integration.SiebelStatusUpdateInfo;
import retail.siebel.com.integration.SiebelStatusUpdateResponse;
import retail.siebel.com.integration.SiebelStatusUpdateWebService;

public class InterfacePersistence {
	private final static Logger log = Logger.getLogger(com.logicinfo.oms.model.InterfacePersistence.class.getName());

	public InterfacePersistence() {
		super();
	}

	String extCustOrdNo = "";

	public void callRMSCancelFulfilOrdColRef(CustomerOrderCancellation input, OmsCoFulfillDetail fulfilDetail, CustomerOrderCancellationItems item, long L_fo_open_qty, OmsCustOrdItem omsCustOrdItem,
			String extCustOrdNo) throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException {
		FulfillOrderService fulfillOrderService = new FulfillOrderService();
		FulfillOrderPortType fulfillOrderPortType = fulfillOrderService.getFulfillOrderPort();
		String sourceLocId = null;
		String sourceLocType = null;
		// Call package if warehouse to warehouse scenario
		if (fulfilDetail.getSourceLocType().equals("WH") && fulfilDetail.getFulfillLocType().equals("W")) {
			log.info("Inside warehouse to warehouse cancellation scenario");
			log.info("Transfer number being passed in the loop : " + fulfilDetail.getTsfNo());
			log.info("Item for cancellation : " + item.getItem());
			log.info("Quantity for cancellation : " + L_fo_open_qty);
			Connection con = null;
			CallableStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
				// pstmt = "{call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?) }";
				pstmt = con.prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?)}");
				pstmt.registerOutParameter(1, Types.VARCHAR);
				pstmt.registerOutParameter(2, Types.VARCHAR);
				pstmt.setBigDecimal(3, fulfilDetail.getTsfNo());
				pstmt.setString(4, item.getItem());
				pstmt.setLong(5, L_fo_open_qty);
				pstmt.execute();
				// rs = stmt.getString(1);
				String errorTextflag = pstmt.getString(2);
				log.info("FLAG : " + errorTextflag);
				String errorReturned = pstmt.getString(1);
				log.info("FLAG : " + errorReturned);
				if (errorReturned.equals("Success") && errorTextflag.equals("Success")) {
					log.info("Package call is successful");
					log.info("Update in Ordcust detail cancel qty");
					updateCancelQty(fulfilDetail.getTsfNo(), L_fo_open_qty);
				} else {
					log.info("Package for Rollback of Cancellation Stub... Not working");
				}
			} catch (Exception e) {
				log.error(e);
			} finally {
				try {
					pstmt.close();
					con.close();
				} catch (SQLException e) {
					log.error(e);
					log.error("-->Unable to call RMS web service cancelFulfilOrdColRef" + e);
					// Added code for 3260 ITEM_LOC_SOH table lock
					OMSUtilSessionEJB session = OMSUtil.doLookup();
					OmsRepublishData omsRepublishData = new OmsRepublishData();
					omsRepublishData.setApplicationId(input.getApplicationId());
					omsRepublishData.setErrorMsg("UNABLE TO CALL RMS Cancellation  WEB SERVICE.");
					omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
					omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
					omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
					omsRepublishData.setRepublishStatus("F");
					omsRepublishData.setTransactionKey(input.getCustOrderNo());
					BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_FULFIL_ORDER");
					omsRepublishData.setWebServiceId(webserviceid.toString());
					String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1\" xmlns:v13=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1\">\n"
							+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:cancelFulfilOrdColRef>";
					String xml = null;
					if (fulfilDetail.getSourceLoc().toString() != null && fulfilDetail.getSourceLocType().toString() != null) {
						xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
								+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:source_loc_type>"
								+ fulfilDetail.getSourceLocType().toString() + "</v12:source_loc_type>\n" + "<v12:source_loc_id>" + fulfilDetail.getSourceLoc().toString() + "</v12:source_loc_id>\n"
								+ "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc()
								+ "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item.getItem() + "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty
								+ "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n" + "<v13:transaction_uom>EA</v13:transaction_uom>\n"
								+ "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
					} else {
						xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
								+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:fulfill_loc_type>"
								+ fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n"
								+ "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item.getItem() + "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n"
								+ "<v13:standard_uom>EA</v13:standard_uom>\n" + "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
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
			}
		}
		// Added else { for if else condition in carrera
		else {
			FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
			fulfilOrdColRef.setCollectionSize(1); // collection size
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
			fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
			fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
			if (fulfilDetail.getSourceLoc().compareTo(fulfilDetail.getFulfillLoc()) != 0) {
				sourceLocId = fulfilDetail.getSourceLoc().toString();
				sourceLocType = fulfilOrdRef.getSourceLocType().fromValue(fulfilDetail.getSourceLocType()).toString();
				fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue()); // **
				fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(fulfilDetail.getSourceLocType()));
			}
			FulfillLocType fulfillLocType = fulfilOrdRef.getFulfillLocType();
			fulfilOrdRef.setFulfillLocType(fulfillLocType.fromValue(fulfilDetail.getFulfillLocType()));
			fulfilOrdRef.setFulfillOrderNo(String.valueOf(fulfilDetail.getFulfillOrderNo()));
			// fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue());
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			fulfilOrdDtlRef.setCancelQtySuom(new BigDecimal(L_fo_open_qty));
			fulfilOrdDtlRef.setItem(item.getItem());
			log.info("fulfill ord no sent to rms:" + fulfilDetail.getFulfillOrderNo() + "for item:" + item.getItem() + "cust_ord_no:" + input.getCustOrderNo() + " with quantity=" + L_fo_open_qty);
			// fulfilOrdDtlRef.setRefItem(value);
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
			fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
			try {
				InvocationSuccess result = fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
			} catch (Exception e) {
				log.error("-->Unable to call RMS web service cancelFulfilOrdColRef" + e);
				// Added code for 3260 ITEM_LOC_SOH table lock
				OMSUtilSessionEJB session = OMSUtil.doLookup();
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(input.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL RMS Cancellation  WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(input.getCustOrderNo());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_FULFIL_ORDER");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1\" xmlns:v13=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1\">\n"
						+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:cancelFulfilOrdColRef>";
				String xml = null;
				if (sourceLocId != null && sourceLocType != null) {
					xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
							+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:source_loc_type>" + sourceLocType
							+ "</v12:source_loc_type>\n" + "<v12:source_loc_id>" + sourceLocId + "</v12:source_loc_id>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType()
							+ "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>"
							+ item.getItem() + "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n"
							+ "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
				} else {
					xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
							+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:fulfill_loc_type>"
							+ fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n"
							+ "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item.getItem() + "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n"
							+ "<v13:standard_uom>EA</v13:standard_uom>\n" + "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
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
		} // closing else for DC to DC if-else
		log.info("--> RMS cancelFulfilOrdColRef success");
	}

	public StrFordHdrColDesc callSIMLookupFulfillmentOrderHeaders(OmsCoFulfillDetail fulfilDetail, long fulilmentOrdNo, String extCustOrdNo)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		try {
			StrFordHdrCriVo strFordHdrCriVo = new StrFordHdrCriVo();
			strFordHdrCriVo.setItemId(fulfilDetail.getItem());
			log.info("item: " + strFordHdrCriVo.getItemId());
			StrFordCriStatus strFordCriStatus = StrFordCriStatus.fromValue("NO_VALUE");
			strFordHdrCriVo.setOrderType(StrFordCriType.fromValue("WEB_ORDER"));
			strFordHdrCriVo.setStoreId(fulfilDetail.getFulfillLoc().longValue());
			strFordHdrCriVo.setCustomerOrderId(extCustOrdNo);
			log.info("store id is" + strFordHdrCriVo.getStoreId());
			strFordHdrCriVo.setExtFulfillmentOrderId(String.valueOf(fulilmentOrdNo));
			log.info("ExtFulfillmentOrderId" + strFordHdrCriVo.getExtFulfillmentOrderId());
			strFordHdrCriVo.setStatus(strFordCriStatus);
			StrFordHdrColDesc strFordHdrColDesc = storeFulfillmentOrderPortType.lookupFulfillmentOrderHeaders(strFordHdrCriVo);
			return strFordHdrColDesc;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice lookupFulfillmentOrderHeaders");
			throw new SOAPException("Error in calling sim webservice lookupFulfillmentOrderHeaders.");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
		}
	}

	public boolean callSIMPingMethod() {
		boolean simvalue = false;
		String simResponse = null;
		log.info("inside callSIMPingMethod ");
		StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		simResponse = storeFulfillmentOrderPortType.ping("SIM");
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
		FulfillOrderService fulfillOrderService = new FulfillOrderService();
		FulfillOrderPortType fulfillOrderPortType = fulfillOrderService.getFulfillOrderPort();
		rmsResponse = fulfillOrderPortType.ping("RMS");
		log.info("rmsResponse " + rmsResponse);
		if (rmsResponse.equalsIgnoreCase("Service(FulfillOrderService) pinged with data(RMS).")) {
			rmsvalue = true;
		}
		return rmsvalue;
	}

	public boolean callSIMRevPingMethod() {
		boolean simRevvalue = false;
		String simRevResponse = null;
		log.info("inside callSIMRevPingMethod ");
		FulfillmentOrderReversePickService fulfillmentOrderReversePickService = new FulfillmentOrderReversePickService();
		FulfillmentOrderReversePickPortType fulfillmentOrderReversePickPortType = fulfillmentOrderReversePickService.getFulfillmentOrderReversePickPort();
		simRevResponse = fulfillmentOrderReversePickPortType.ping("SIMRev");
		log.info("simRevResponse " + simRevResponse);
		if (simRevResponse.equalsIgnoreCase("Service(FulfillmentOrderReversePickService) pinged with data(SIMRev).")) {
			simRevvalue = true;
		}
		return simRevvalue;
	}

	public StrFordDesc callSIMReadFulfillmentOrderDetail(StrFordHdrColDesc strFordHdrColDesc)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		try {
			log.info("inside callSIMReadFulfillmentOrderDetail");
			long int_fulfillment_order_id = strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId();
			log.info("int_fulfillment_order_id " + int_fulfillment_order_id);
			StrFordRef strFordRef = new StrFordRef();
			strFordRef.setIntFulfillmentOrderId(int_fulfillment_order_id);
			StrFordDesc strFordDesc = storeFulfillmentOrderPortType.readFulfillmentOrderDetail(strFordRef);
			return strFordDesc;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice readFulfillmentOrderDetail");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice readFulfillmentOrderDetail");
		}
	}

	public void callSIMCancelFulfillmentOrderDetail(CustomerOrderCancellation input, OmsCoFulfillDetail fulfilDetail, CustomerOrderCancellationItems item, long fulilmentOrdNo,
			OmsCustOrdItem omsCustOrdItem, long actual_reserved_qty, BigDecimal qtyToBeCanceledinSIM, String extCustOrdNo)
			throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		log.info("callSIMCancelFulfillmentOrderDetail");
		StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		try {
			Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
			log.info("Condition 1");
			FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
			fulfilOrdColRef.value = fulfilOrdColRef1;
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
			fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
			fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
			fulfilOrdRef.setFulfillLocType(fulfilOrdRef.getFulfillLocType().fromValue(fulfilDetail.getFulfillLocType()));
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue()); // **
			fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(fulfilDetail.getSourceLocType())); // **
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
			fulfilOrdDtlRef.setItem(item.getItem());
			fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
			fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
			fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
			log.info("calling sim webservice method cancelFulfillmentOrderDetail() when pickedQty==delievedQty");
			storeFulfillmentOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice cancelFulfillmentOrderDetail");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice cancelFulfillmentOrderDetail");
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
		FulfillmentOrderReversePickService fulfillmentOrderReversePickService = new FulfillmentOrderReversePickService();
		FulfillmentOrderReversePickPortType fulfillmentOrderReversePickPortType = fulfillmentOrderReversePickService.getFulfillmentOrderReversePickPort();
		try {
			ForpCreModVo forpCreModVo = new ForpCreModVo();
			ForpCreItmMod forpCreItmMod = new ForpCreItmMod();
			forpCreItmMod.setFulfillmentOrderLineId(lineId);
			forpCreItmMod.setQuantity(new BigDecimal(reverseQty));
			forpCreModVo.setIntFulfillmentOrderId(strFordHdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
			forpCreModVo.getForpCreItmMod().add(forpCreItmMod);
			// log.info("Reverse qty in method createReversePick="+to_be_picekd);
			ForpRef forpRef = fulfillmentOrderReversePickPortType.createReversePick(forpCreModVo);
			return forpRef;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice createReversePick");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice createReversePick");
		}
	}

	public InvocationSuccess callSIMConfirmReversePick(ForpRef forpRef) throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException, SOAPException {
		FulfillmentOrderReversePickService fulfillmentOrderReversePickService = new FulfillmentOrderReversePickService();
		FulfillmentOrderReversePickPortType fulfillmentOrderReversePickPortType = fulfillmentOrderReversePickService.getFulfillmentOrderReversePickPort();
		try {
			InvocationSuccess invocationSuccess = fulfillmentOrderReversePickPortType.confirmReversePick(forpRef);
			return invocationSuccess;
		} catch (Exception e) {
			log.error("--> Error in calling sim webservice confirmReversePick");
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			throw new SOAPException("Error in calling sim webservice confirmReversePick");
		}
	}

	public FodHdrColDesc callSIMlookupFulfillmentOrderDeliveryHeaders(long intFulfillOrderId)
			throws com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException, SOAPException {
		FulfillmentOrderDeliveryService fulfillmentOrderDeliveryService = new FulfillmentOrderDeliveryService();
		FulfillmentOrderDeliveryPortType fulfillmentOrderDeliveryPortType = fulfillmentOrderDeliveryService.getFulfillmentOrderDeliveryPort();
		StrFordRef strFordRef = new StrFordRef();
		strFordRef.setIntFulfillmentOrderId(intFulfillOrderId);
		try {
			FodHdrColDesc fodHdrColDesc = fulfillmentOrderDeliveryPortType.lookupFulfillmentOrderDeliveryHeaders(strFordRef);
			return fodHdrColDesc;
		} catch (Exception e) {
			throw new SOAPException("Error in calling sim webservice lookupFulfillmentOrderDeliveryHeaders");
		}
	}

	public void callSeibelWebservice(CustomerOrderCancellation input, String custId, BigDecimal omsCancelId, TreeMap<String, BigDecimal> backorderTreeMap) throws SOAPException {
		log.info("Inside callSeibelWebservice method");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		SiebelStatusUpdateInfo siebelStatusUpdate = new SiebelStatusUpdateInfo();
		log.info("omsCancelId " + omsCancelId);
		List<OmsCoFoCancel> omsCoFoCancelList = session.getOmsCoFoCancelFindByOmsCancelId(omsCancelId);
		List<OmsCoCancelItem> omsCoCanelItemList = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		SiebelStatusUpdateResponse siebelStatusUpdateResponse = null;
		List<SiebelStatusUpdateDetails> siebelStatusUpdateDetailsList = null;
		try {
			log.info("Inside try block of callSeibelWebservice method");
			String subCustOrdNo = "1";
			siebelStatusUpdate.setCustOrderNo(input.getCustOrderNo());
			if (input.getSubCustOrderNo() == null) {
				siebelStatusUpdate.setSubCustOrderNo("1");
			} else {
				subCustOrdNo = input.getSubCustOrderNo();
				siebelStatusUpdate.setSubCustOrderNo(input.getSubCustOrderNo());
			}
			siebelStatusUpdate.setCustId(custId);
			List<BigDecimal> omsCustList = session.getOmsCustOrdHeadFindByExternalCustOrdNo(input.getCustOrderNo(), subCustOrdNo, input.getApplicationId());
			log.info("omsCustList : " + omsCustList.size());
			log.info("Customer Order Number : " + input.getCustOrderNo());
			log.info("Application Id : " + input.getApplicationId());
			BigDecimal omsCustOrdNo = omsCustList.get(0);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			siebelStatusUpdate.setApplicationId(omsCustOrdHead.getApplicationId());
			log.info("omsCustOrdHead.getApplicationId() : " + omsCustOrdHead.getApplicationId());
			siebelStatusUpdate.setOmsCustomerOrderNo(omsCustOrdNo.longValue());
			log.info("omsCustOrdNo.longValue() : " + omsCustOrdNo.longValue());
			// CancelDatetime
			GregorianCalendar gregorianCalendar1 = new GregorianCalendar();
			DatatypeFactory datatypeFactory1 = null;
			try {
				datatypeFactory1 = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				log.warn(e.toString());
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
			}
			XMLGregorianCalendar now1 = datatypeFactory1.newXMLGregorianCalendar(gregorianCalendar1);
			log.info("now1 " + now1);
			siebelStatusUpdate.setCancelDatetime(now1);
			// ConsumerDlyTime
			GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
			DatatypeFactory datatypeFactory2 = null;
			try {
				datatypeFactory2 = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				log.warn(e.toString());
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
			}
			XMLGregorianCalendar now2 = datatypeFactory2.newXMLGregorianCalendar(gregorianCalendar2);
			log.info("now2 " + now2);
			siebelStatusUpdate.setConsumerDlyTime(now2);
			// CloseDatetime
			GregorianCalendar gregorianCalendar3 = new GregorianCalendar();
			DatatypeFactory datatypeFactory3 = null;
			try {
				datatypeFactory3 = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				log.warn(e.toString());
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
			}
			XMLGregorianCalendar now3 = datatypeFactory3.newXMLGregorianCalendar(gregorianCalendar3);
			log.info("now3 " + now3);
			siebelStatusUpdate.setCloseDatetime(now3);
			siebelStatusUpdateDetailsList = siebelStatusUpdate.getCustomerOrderStatusUpdateDetails();
			// Code added for bug 2787
			if (backorderTreeMap != null && backorderTreeMap.keySet().size() > 0) {
				log.info("========== backorderTreeMap.keySet() : " + backorderTreeMap.keySet());
				log.info("Sending Seibel Status while BackOrder Cancellation");
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
					SiebelStatusUpdateDetails siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
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
			log.info("omsCoFoCancelList.size() " + omsCoFoCancelList.size());
			if (omsCoFoCancelList.size() > 0) {
				for (CustomerOrderCancellationItems cancellationItems : input.getCancellationItems()) {
					List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(cancellationItems.getLineNo()), omsCustOrdNo);
					List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, cancellationItems.getItem(),
							new BigDecimal(cancellationItems.getLineNo()));
					if (omsCoFulfillDetailList.size() == 0) {
						for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList) {
							for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
								if (omsCoFoCancel.getLineNo().intValue() == cancellationItems.getLineNo()
										&& omsCoFoCancel.getFulfillOrderNo().intValue() == omsCustOrdReserve.getFulfillOrderNo().intValue() && omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
									log.info("sending cancel event for cancellationItems.getLineNo() " + cancellationItems.getLineNo());
									log.info("fullfill Order No for Reserve :" + omsCoFoCancel.getFulfillOrderNo());
									SiebelStatusUpdateDetails siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
									siebelStatusUpdateDetails.setItem(cancellationItems.getItem());
									log.info("cancellationItems.getItem() : " + cancellationItems.getItem());
									siebelStatusUpdateDetails.setLineNo(cancellationItems.getLineNo());
									log.info("cancellationItems.getLineNo() : " + cancellationItems.getLineNo());
									log.info("Item=" + cancellationItems.getItem() + "Line no=" + cancellationItems.getLineNo());
									siebelStatusUpdateDetails.setQty(omsCoFoCancel.getFoCancelledOty());
									log.info("omsCoFoCancel.getCancelQtySuom() : " + omsCoFoCancel.getFoCancelledOty());
									siebelStatusUpdateDetails.setEventId("CAC");
									log.info("siebelStatusUpdateDetails.setEventId(\"CAC\") : CAC");
									siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
									log.info("siebelStatusUpdateDetails.setEventComments(\"Cancelled by customer\") : Cancelled by customer");
									siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
									log.info("omsCancelId.longValue() : " + omsCancelId.longValue());
									siebelStatusUpdateDetails.setSourceLoc(omsCustOrdReserve.getRmsResvLoc().longValue());
									log.info("omsCustOrdReserve.getRmsResvLoc().longValue() : " + omsCustOrdReserve.getRmsResvLoc().longValue());
									siebelStatusUpdateDetails.setSourceLocType(omsCustOrdReserve.getRmsResvLocType());
									log.info("omsCustOrdReserve.getRmsResvLocType() : " + omsCustOrdReserve.getRmsResvLocType());
									siebelStatusUpdateDetails.setFulfillLocType(omsCustOrdReserve.getLocType());
									log.info("omsCustOrdReserve.getLocType() : " + omsCustOrdReserve.getLocType());
									siebelStatusUpdateDetails.setFulfillLoc(omsCustOrdReserve.getLoc().longValue());
									log.info("omsCustOrdReserve.getLoc().longValue() : " + omsCustOrdReserve.getLoc().longValue());
									GregorianCalendar gregorianCalendar = new GregorianCalendar();
									DatatypeFactory datatypeFactory = null;
									try {
										datatypeFactory = DatatypeFactory.newInstance();
									} catch (DatatypeConfigurationException e) {
										log.warn(e.toString());
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
									}
									XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
									siebelStatusUpdateDetails.setUpdateDatetime(now);
									siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
								}
							}
						}
					}
					for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
						log.info("Inside the FIRST for loop of OmsCoFoCancel omsCoFoCancel:omsCoFoCancelList");
						for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
							log.info("Inside the SECOND for loop of OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList");
							SiebelStatusUpdateDetails siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
							if (omsCoFoCancel.getLineNo().intValue() == cancellationItems.getLineNo()
									&& omsCoFoCancel.getFulfillOrderNo().intValue() == omsCoFulfillDetail.getFulfillOrderNo().intValue() && omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
								if (omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
									log.info("cancelling for line No cancellationItems.getLineNo() " + cancellationItems.getLineNo());
									log.info("cancel for reserved item " + omsCoFoCancel.getFulfillOrderNo());
									siebelStatusUpdateDetails.setItem(cancellationItems.getItem());
									log.info("cancellationItems.getItem() : " + cancellationItems.getItem());
									siebelStatusUpdateDetails.setLineNo(cancellationItems.getLineNo());
									log.info("Item=" + cancellationItems.getItem() + "Line no=" + cancellationItems.getLineNo());
									siebelStatusUpdateDetails.setQty(omsCoFoCancel.getFoCancelledOty());
									log.info("omsCoFoCancel.getCancelQtySuom() : " + omsCoFoCancel.getFoCancelledOty());
									siebelStatusUpdateDetails.setEventId("CAC");
									log.info("siebelStatusUpdateDetails.setEventId(\"CAC\") : CAC");
									siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
									log.info("siebelStatusUpdateDetails.setEventComments(\"Cancelled by customer\") : Cancelled by customer");
									siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
									log.info("omsCancelId.longValue() : " + omsCancelId.longValue());
									siebelStatusUpdateDetails.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
									log.info("omsCoFulfillDetail.getSourceLoc().longValue() : " + omsCoFulfillDetail.getSourceLoc().longValue());
									siebelStatusUpdateDetails.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
									log.info("omsCoFulfillDetail.getSourceLocType() : " + omsCoFulfillDetail.getSourceLocType());
									siebelStatusUpdateDetails.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
									log.info("omsCoFulfillDetail.getFulfillLocType() : " + omsCoFulfillDetail.getFulfillLocType());
									siebelStatusUpdateDetails.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
									log.info("omsCoFulfillDetail.getFulfillLoc().longValue() : " + omsCoFulfillDetail.getFulfillLoc().longValue());
									GregorianCalendar gregorianCalendar = new GregorianCalendar();
									DatatypeFactory datatypeFactory = null;
									try {
										datatypeFactory = DatatypeFactory.newInstance();
									} catch (DatatypeConfigurationException e) {
										log.warn(e.toString());
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
									}
									XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
									siebelStatusUpdateDetails.setUpdateDatetime(now);
									siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
								} else {
									log.info("cancel for transfer item for FulfillOrderNo" + omsCoFoCancel.getFulfillOrderNo());
									siebelStatusUpdateDetails.setItem(cancellationItems.getItem());
									log.info("cancellationItems.getItem() : " + cancellationItems.getItem());
									siebelStatusUpdateDetails.setLineNo(cancellationItems.getLineNo());
									log.info("Item=" + cancellationItems.getItem() + "Line no=" + cancellationItems.getLineNo());
									siebelStatusUpdateDetails.setQty(omsCoFoCancel.getFoCancelledOty());
									log.info("omsCoFoCancel.getCancelQtySuom() : " + omsCoFoCancel.getFoCancelledOty());
									siebelStatusUpdateDetails.setEventId("CAC");
									log.info("siebelStatusUpdateDetails.setEventId(\"CAC\") : CAC");
									siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
									log.info("siebelStatusUpdateDetails.setEventComments(\"Cancelled by customer\") : Cancelled by customer");
									siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
									log.info("omsCancelId.longValue() : " + omsCancelId.longValue());
									siebelStatusUpdateDetails.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
									log.info("omsCoFulfillDetail.getSourceLoc().longValue() : " + omsCoFulfillDetail.getSourceLoc().longValue());
									siebelStatusUpdateDetails.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
									log.info("omsCoFulfillDetail.getSourceLocType() : " + omsCoFulfillDetail.getSourceLocType());
									siebelStatusUpdateDetails.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
									log.info("omsCoFulfillDetail.getFulfillLocType() : " + omsCoFulfillDetail.getFulfillLocType());
									siebelStatusUpdateDetails.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
									log.info("omsCoFulfillDetail.getFulfillLoc().longValue() : " + omsCoFulfillDetail.getFulfillLoc().longValue());
									GregorianCalendar gregorianCalendar = new GregorianCalendar();
									DatatypeFactory datatypeFactory = null;
									try {
										datatypeFactory = DatatypeFactory.newInstance();
									} catch (DatatypeConfigurationException e) {
										log.warn(e.toString());
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
									}
									XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
									siebelStatusUpdateDetails.setUpdateDatetime(now);
									siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
								} // end of else
							} // end of if for line No validation
						} // End of for cofull
					} // End of for omscofocancel
				}
			}
			for (CustomerOrderCancellationItems cancellationItems : input.getCancellationItems()) {
				// Updated of 3004 Production bug for CAC event for Non-Inventory or shipping
				// charge Item
				String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
				BigDecimal itemDept = session.getItemMasterFindDept(cancellationItems.getItem());
				String inventoryIndn = session.getItemMasterFindInventoryInd(cancellationItems.getItem(), itemDept);
				if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
					try {
						SiebelStatusUpdateDetails siebelStatusUpdateDetails = new SiebelStatusUpdateDetails();
						siebelStatusUpdateDetails.setItem(cancellationItems.getItem());
						log.info("cancellationItems.getItem() : " + cancellationItems.getItem());
						siebelStatusUpdateDetails.setLineNo(cancellationItems.getLineNo());
						log.info("Item=" + cancellationItems.getItem() + "Line no=" + cancellationItems.getLineNo());
						siebelStatusUpdateDetails.setQty(cancellationItems.getCancelQtySuom());
						log.info("omsCancelId" + omsCancelId.longValue() + "Non-inventory or Shipping Charge item qty to siebel : " + cancellationItems.getCancelQtySuom());
						siebelStatusUpdateDetails.setEventId("CAC");
						log.info("siebelStatusUpdateDetails.setEventId(\"CAC\") : CAC");
						siebelStatusUpdateDetails.setEventComments("Cancelled by customer");
						log.info("siebelStatusUpdateDetails.setEventComments(\"Cancelled by customer\") : Cancelled by customer");
						siebelStatusUpdateDetails.setOmsCancelId(omsCancelId.longValue());
						log.info("omsCancelId.longValue() : " + omsCancelId.longValue());
						siebelStatusUpdateDetails.setSourceLoc(omsCustOrdHead.getOrderRequestorId().longValue());
						siebelStatusUpdateDetails.setSourceLocType("ST");
						siebelStatusUpdateDetails.setFulfillLocType("S");
						siebelStatusUpdateDetails.setFulfillLoc(omsCustOrdHead.getOrderRequestorId().longValue());
						GregorianCalendar gregorianCalendar = new GregorianCalendar();
						DatatypeFactory datatypeFactory = null;
						try {
							datatypeFactory = DatatypeFactory.newInstance();
						} catch (DatatypeConfigurationException e) {
							log.warn(e.toString());
							throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
						}
						XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
						siebelStatusUpdateDetails.setUpdateDatetime(now);
						siebelStatusUpdateDetailsList.add(siebelStatusUpdateDetails);
					} catch (Exception e) {
						log.info("Exception " + e.getMessage());
					}
				} // end of if for non-inventory or shipping charge check
			}
			log.info("siebelStatusUpdateDetailsList.size() " + siebelStatusUpdateDetailsList.size());
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
		} catch (Exception e) {
			log.error("-->SIEBEL customer order status update failed." + e);
		}
		if (siebelStatusUpdateDetailsList.size() > 0) {
			try {
				OmsorderupdatebpelprocessClientEp omsorderupdatebpelprocessClientEp = new OmsorderupdatebpelprocessClientEp();
				SiebelStatusUpdateWebService siebelStatusUpdateWebService = omsorderupdatebpelprocessClientEp.getSiebelStatusUpdateWebServicePt();
				siebelStatusUpdateResponse = siebelStatusUpdateWebService.processSiebelStatusUpdate(siebelStatusUpdate);
				log.info("Response from siebel is " + siebelStatusUpdateResponse.getMessage());
			} catch (Exception e) {
				log.error("Publishing to siebel failed" + e);
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(input.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(input.getCustOrderNo());
				// String
				// webserviceURL=session.getOmsWebserviceUriDetailFindByWebserviceName("SIEBEL_STATUS_UPDATE");
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				try {
					JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
					Marshaller marshel = context.createMarshaller();
					Writer os = new StringWriter();
					marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
					marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
					marshel.marshal(siebelStatusUpdate, os);
					// marshel.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					omsRepublishData.setXmlMsg(os.toString());
					session.persistOmsRepublishData(omsRepublishData);
					log.info("persisting in omsRepublish data");
				} catch (Exception f) {
					log.error("persisting in omsRepublish data failed" + e);
				}
			}
			if (siebelStatusUpdateResponse.getMessage().equals("Success") == false) {
				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId(input.getApplicationId());
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIEBEL_STATUS_UPDATE  WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(input.getCustOrderNo());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIEBEL_STATUS_UPDATE");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				try {
					JAXBContext context = JAXBContext.newInstance(SiebelStatusUpdateInfo.class);
					Marshaller marshel = context.createMarshaller();
					Writer os = new StringWriter();
					marshel.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
					marshel.setProperty("com.sun.xml.bind.xmlHeaders", "");
					marshel.marshal(siebelStatusUpdate, os);
					omsRepublishData.setXmlMsg(os.toString());
					log.info("XML Msg for CAC event" + omsRepublishData.getXmlMsg());
					session.persistOmsRepublishData(omsRepublishData);
					log.info("persisting in omsRepublish data");
				} catch (Exception f) {
					log.info("Exception while persisting data into siebel" + f.getMessage());
				}
			}
		}
	}

	public void adjustInventoryByItemLocation(CustomerOrderCancellation input, CustomerOrderCancellationItems customerOrderCancellationItems, BigDecimal omsCustOrdNo, BigDecimal cancelQty,
			OmsCustOrdReserve custOrdItems) throws SOAPException {
		log.info("Calling SIM invetory adustment in case of Unreserve");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String errorReason = "";
		List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, customerOrderCancellationItems.getItem(),
				new BigDecimal(customerOrderCancellationItems.getLineNo()));
		// for (OmsCustOrdReserve custOrdItems : omsCustOrdReserveList) {
		if (custOrdItems.getRmsResvLocType().equals("SU") == false && custOrdItems.getRmsResvLocType().equals("WH") == false) {
			try {
				InventoryAdjustmentService inventoryAdjustmentService = new InventoryAdjustmentService();
				InventoryAdjustmentPortType inventoryAdjustmentPortType = inventoryAdjustmentService.getInventoryAdjustmentPort();
				StrAdjModVo strAdjModVo = new StrAdjModVo();
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(custOrdItems.getItem());
				strAdjItemMod.setReasonId(Integer.parseInt(session.getOmsSystemParametersFindIndValue("INV_UNRESV_CODE", "OMS_INV_ADJ_RSN_CODE")));
				strAdjItemMod.setQuantity(cancelQty);
				strAdjItemMod.setCaseSize(new BigDecimal(1));
				strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
				strAdjModVo.setStoreId(custOrdItems.getRmsResvLoc().longValue());
				strAdjModVo.setComments("Unreserved for customer order id :" + input.getCustOrderNo());
				StrAdjRef strAdjRef = inventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment(strAdjModVo);
				log.info("Call to SIM invetory adustment in case of reserve success");
				// return strAdjRef.getAdjustmentId();
			} catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException iawsfe) {
				errorReason = iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
				log.error(" --> " + errorReason);
				throw new SOAPException(errorReason);
			} catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException iswsfe) {
				errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
				log.error(" --> " + errorReason);
				throw new SOAPException(errorReason);
			} catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException vwsfe) {
				errorReason = vwsfe.getMessage() + " " + " Error Reason :" + vwsfe.getFaultInfo().getErrorDescription();
				log.error(" --> " + errorReason);
				throw new SOAPException(errorReason);
			} catch (Exception e) {
				log.error(" --> Exception ST: " + e);
				throw new SOAPException(e.getMessage());
			}
		}
		// } //for ends
	}

	public void callRMSCancelFulfilOrdColRefforApprovedTransfers(CustomerOrderCancellation input, OmsCoFulfillDetail fulfilDetail, BigDecimal L_fo_open_qty, String item, OmsCustOrdItem omsCustOrdItem,
			String extCustOrdNo) throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException {
		FulfillOrderService fulfillOrderService = new FulfillOrderService();
		FulfillOrderPortType fulfillOrderPortType = fulfillOrderService.getFulfillOrderPort();
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
			fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(fulfilDetail.getSourceLocType()));
		}
		FulfillLocType fulfillLocType = fulfilOrdRef.getFulfillLocType();
		fulfilOrdRef.setFulfillLocType(fulfillLocType.fromValue(fulfilDetail.getFulfillLocType()));
		fulfilOrdRef.setFulfillOrderNo(String.valueOf(fulfilDetail.getFulfillOrderNo()));
		log.info("fulfill ord no sent to rms:" + fulfilDetail.getFulfillOrderNo() + "for item:" + fulfilDetail.getItem() + "cust_ord_no:" + input.getCustOrderNo());
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
			InvocationSuccess result = fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
		} catch (Exception e) {
			log.error("-->Unable to call RMS web service cancelFulfilOrdColRef");
			// Added code for 3260 ITEM_LOC_SOH table lock
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId(input.getApplicationId());
			omsRepublishData.setErrorMsg("UNABLE TO CALL RMS Cancellation  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey(input.getCustOrderNo());
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_FULFIL_ORDER");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1\" xmlns:v13=\"http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1\">\n"
					+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:cancelFulfilOrdColRef>";
			String xml = null;
			if (sourceLocId != null && sourceLocType != null) {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
						+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:source_loc_type>" + sourceLocType
						+ "</v12:source_loc_type>\n" + "<v12:source_loc_id>" + sourceLocId + "</v12:source_loc_id>\n" + "<v12:fulfill_loc_type>" + fulfilDetail.getFulfillLocType()
						+ "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n" + "<v13:item>" + item
						+ "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n"
						+ "<v13:transaction_uom>EA</v13:transaction_uom>\n" + "</v13:FulfilOrdDtlRef></v12:FulfilOrdRef></v11:FulfilOrdColRef>";
			} else {
				xml = "<v11:FulfilOrdColRef>\n" + "<v11:collection_size>1</v11:collection_size>\n" + "<v12:FulfilOrdRef>\n" + "<v12:customer_order_no>" + input.getCustOrderNo()
						+ "</v12:customer_order_no>\n" + "<v12:fulfill_order_no>" + fulfilDetail.getFulfillOrderNo() + "</v12:fulfill_order_no>\n" + "<v12:fulfill_loc_type>"
						+ fulfilDetail.getFulfillLocType() + "</v12:fulfill_loc_type>\n" + "<v12:fulfill_loc_id>" + fulfilDetail.getFulfillLoc() + "</v12:fulfill_loc_id>\n" + "<v13:FulfilOrdDtlRef>\n"
						+ "<v13:item>" + item + "</v13:item>\n" + "<v13:cancel_qty_suom>" + L_fo_open_qty + "</v13:cancel_qty_suom>\n" + "<v13:standard_uom>EA</v13:standard_uom>\n"
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

	private static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTimeInMillis(date.getTime());
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
			log.info("-------------------------------------------");
		}
		return xmlCalendar;
	}

	public OmsOrderStatusUpdateHeader getOmsOrderStatusUpdateforCancellation(CustomerOrderCancellation input, String custId, BigDecimal omsCancelId, TreeMap<String, BigDecimal> backorderTreeMap)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<OmsCoFoCancel> omsCoFoCancelList = session.getOmsCoFoCancelFindByOmsCancelId(omsCancelId);
		List<OmsCoCancelItem> omsCoCanelItemList = session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
		String custOrderNo = input.getCustOrderNo();
		String subCustOrderNo = input.getSubCustOrderNo();
		if (null == subCustOrderNo) {
			subCustOrderNo = "1";
		}
		List<BigDecimal> omsCustList = session.getOmsCustOrdHeadFindByExternalCustOrdNo(input.getCustOrderNo(), subCustOrderNo, input.getApplicationId());
		BigDecimal omsCustOrdNo = omsCustList.get(0);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		Timestamp consumerDeliverytime = omsCustOrdHead.getConsumerDlyTime();
		Timestamp lastupdatedatetime = omsCustOrdHead.getLastUpdateDatetime();
		log.info(" Last update time is" + lastupdatedatetime);
		OmsOrderStatusUpdateHeader headerStatuCancellation = new OmsOrderStatusUpdateHeader();
		ArrayOfOmsOrderStatusUpdateDetail arrayofOmsOrderStatusUpateDetailForCancellation = new ArrayOfOmsOrderStatusUpdateDetail();
		OmsOrderDetailStatus omsOrderDetailStatusUpdateforCancellation = null;
		headerStatuCancellation.setEntityId(omsCustOrdHead.getEntityId());
		headerStatuCancellation.setApplicationId(omsCustOrdHead.getApplicationId());
		headerStatuCancellation.setOmsOrderId(omsCustOrdNo.longValue() + "");
		headerStatuCancellation.setOrderId(custOrderNo);
		headerStatuCancellation.setDeliveryDate(toXMLGregorianCalendar(new Date(consumerDeliverytime.getTime())));
		headerStatuCancellation.setUpdateDate(toXMLGregorianCalendar(new Date(lastupdatedatetime.getTime())));
		// Detail level for soa services...
		if (backorderTreeMap != null && backorderTreeMap.keySet().size() > 0) {
			log.info("========== backorderTreeMap.keySet() : " + backorderTreeMap.keySet());
			log.info("Sending Siebel Status while BackOrder Cancellation");
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
				omsOrderDetailStatusUpdateforCancellation = new OmsOrderDetailStatus();
				omsOrderDetailStatusUpdateforCancellation.setOrderDetailId(lineno.longValue());
				omsOrderDetailStatusUpdateforCancellation.setProductSku(item);
				omsOrderDetailStatusUpdateforCancellation.setQuantity(sourceQty.intValue());
				omsOrderDetailStatusUpdateforCancellation.setFulfillId(fulFillLoc.intValue());
				omsOrderDetailStatusUpdateforCancellation.setFulfillType(fulFillLocType);
				omsOrderDetailStatusUpdateforCancellation.setSourceId(sourceLoc.intValue());
				omsOrderDetailStatusUpdateforCancellation.setSourceType(sourcelocType);
				omsOrderDetailStatusUpdateforCancellation.setEventId("CAC");
				omsOrderDetailStatusUpdateforCancellation.setEventReferenceId(omsCancelId.longValue());
				omsOrderDetailStatusUpdateforCancellation.setEventComment("Canceled By Customer");
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = null;
				try {
					datatypeFactory = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
				}
				XMLGregorianCalendar omsCancellationUpdatetime = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				omsOrderDetailStatusUpdateforCancellation.setUpdateDate(omsCancellationUpdatetime);
				arrayofOmsOrderStatusUpateDetailForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusUpdateforCancellation);
			}
		} // End of Back order call.....
		log.info("omsCoFoCancelList.size() " + omsCoFoCancelList.size());
		if (omsCoFoCancelList.size() > 0) {
			for (CustomerOrderCancellationItems cancellationItems : input.getCancellationItems()) {
				List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindFulfillByLineNo(new BigDecimal(cancellationItems.getLineNo()), omsCustOrdNo);
				List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdNo, cancellationItems.getItem(),
						new BigDecimal(cancellationItems.getLineNo()));
				if (omsCoFulfillDetailList.size() == 0) {
					for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList) {
						for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
							if (omsCoFoCancel.getLineNo().intValue() == cancellationItems.getLineNo()
									&& omsCoFoCancel.getFulfillOrderNo().intValue() == omsCustOrdReserve.getFulfillOrderNo().intValue() && omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
								log.info("sending cancel event for cancellationItems.getLineNo() " + cancellationItems.getLineNo());
								log.info("fullfill Order No for Reserve :" + omsCoFoCancel.getFulfillOrderNo());
								omsOrderDetailStatusUpdateforCancellation = new OmsOrderDetailStatus();
								omsOrderDetailStatusUpdateforCancellation.setProductSku(cancellationItems.getItem());
								omsOrderDetailStatusUpdateforCancellation.setOrderDetailId(cancellationItems.getLineNo());
								omsOrderDetailStatusUpdateforCancellation.setEventComment("Canceled by customer");
								omsOrderDetailStatusUpdateforCancellation.setEventReferenceId(omsCancelId.intValue());
								omsOrderDetailStatusUpdateforCancellation.setQuantity(omsCoFoCancel.getFoCancelledOty().intValue());
								omsOrderDetailStatusUpdateforCancellation.setEventId("CAC");
								omsOrderDetailStatusUpdateforCancellation.setFulfillId(omsCustOrdReserve.getLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setFulfillType(omsCustOrdReserve.getLocType());
								omsOrderDetailStatusUpdateforCancellation.setSourceId(omsCustOrdReserve.getRmsResvLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setSourceType(omsCustOrdReserve.getRmsResvLocType());
								GregorianCalendar gregorianCalendar = new GregorianCalendar();
								DatatypeFactory datatypeFactory = null;
								try {
									datatypeFactory = DatatypeFactory.newInstance();
								} catch (DatatypeConfigurationException e) {
									log.warn(e.toString());
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
								}
								XMLGregorianCalendar omsCancellationUpdatetime = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
								omsOrderDetailStatusUpdateforCancellation.setUpdateDate(omsCancellationUpdatetime);
								arrayofOmsOrderStatusUpateDetailForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusUpdateforCancellation);
							}
						}
					}
				} // omsFulfill detail size is Zero.......
				for (OmsCoFoCancel omsCoFoCancel : omsCoFoCancelList) {
					log.info("Inside the FIRST for loop of OmsCoFoCancel omsCoFoCancel:omsCoFoCancelList");
					for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
						omsOrderDetailStatusUpdateforCancellation = new OmsOrderDetailStatus();
						if (omsCoFoCancel.getLineNo().intValue() == cancellationItems.getLineNo() && omsCoFoCancel.getFulfillOrderNo().intValue() == omsCoFulfillDetail.getFulfillOrderNo().intValue()
								&& omsCoFoCancel.getFoCancelledOty().intValue() > 0) {
							if (omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
								omsOrderDetailStatusUpdateforCancellation.setEventComment("Canceled By Customer");
								omsOrderDetailStatusUpdateforCancellation.setEventId("CAC");
								omsOrderDetailStatusUpdateforCancellation.setEventReferenceId(omsCancelId.intValue());
								omsOrderDetailStatusUpdateforCancellation.setFulfillId(omsCoFulfillDetail.getFulfillLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setFulfillType(omsCoFulfillDetail.getFulfillLocType());
								omsOrderDetailStatusUpdateforCancellation.setSourceId(omsCoFulfillDetail.getSourceLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setSourceType(omsCoFulfillDetail.getSourceLocType());
								omsOrderDetailStatusUpdateforCancellation.setOrderDetailId(omsCoFoCancel.getLineNo().intValue());
								omsOrderDetailStatusUpdateforCancellation.setProductSku(cancellationItems.getItem());
								omsOrderDetailStatusUpdateforCancellation.setQuantity(omsCoFoCancel.getFoCancelledOty().intValue());
								GregorianCalendar gregorianCalendar = new GregorianCalendar();
								DatatypeFactory datatypeFactory = null;
								try {
									datatypeFactory = DatatypeFactory.newInstance();
								} catch (DatatypeConfigurationException e) {
									log.warn(e.toString());
								}
								XMLGregorianCalendar itemcancellationlastupdate = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
								omsOrderDetailStatusUpdateforCancellation.setUpdateDate(itemcancellationlastupdate);
								arrayofOmsOrderStatusUpateDetailForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusUpdateforCancellation);
							} else {
								omsOrderDetailStatusUpdateforCancellation.setEventComment("Canceled By Customer");
								omsOrderDetailStatusUpdateforCancellation.setEventId("CAC");
								omsOrderDetailStatusUpdateforCancellation.setEventReferenceId(omsCancelId.intValue());
								omsOrderDetailStatusUpdateforCancellation.setFulfillId(omsCoFulfillDetail.getFulfillLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setFulfillType(omsCoFulfillDetail.getFulfillLocType());
								omsOrderDetailStatusUpdateforCancellation.setSourceId(omsCoFulfillDetail.getSourceLoc().intValue());
								omsOrderDetailStatusUpdateforCancellation.setSourceType(omsCoFulfillDetail.getSourceLocType());
								omsOrderDetailStatusUpdateforCancellation.setProductSku(cancellationItems.getItem());
								omsOrderDetailStatusUpdateforCancellation.setQuantity(omsCoFoCancel.getFoCancelledOty().intValue());
								omsOrderDetailStatusUpdateforCancellation.setOrderDetailId(cancellationItems.getLineNo());
								GregorianCalendar gregorianCalendar = new GregorianCalendar();
								DatatypeFactory datatypeFactory = null;
								try {
									datatypeFactory = DatatypeFactory.newInstance();
								} catch (DatatypeConfigurationException e) {
									log.warn(e.toString());
								}
								XMLGregorianCalendar itemcancellationlastupdate = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
								omsOrderDetailStatusUpdateforCancellation.setUpdateDate(itemcancellationlastupdate);
								arrayofOmsOrderStatusUpateDetailForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusUpdateforCancellation);
							} //
						} // end of if for line No validation
					} // end of for cofuldetail
				} // end of OmsCoFoCancel List ...
			} // end of omscocancellation list..
		} // end of if condition of oms co fo cancel...
			// setOrderDetailStatuses(orderDetailCancellationStatusArray);
			// Updated of 3004 Production bug for CAC event for Non-Inventory or shipping
			// charge Item
		for (CustomerOrderCancellationItems cancellationItems : input.getCancellationItems()) {
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(cancellationItems.getItem());
			String inventoryIndn = session.getItemMasterFindInventoryInd(cancellationItems.getItem(), itemDept);
			if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
				omsOrderDetailStatusUpdateforCancellation = new OmsOrderDetailStatus();
				omsOrderDetailStatusUpdateforCancellation.setEventComment("Canceled By customer");
				omsOrderDetailStatusUpdateforCancellation.setEventId("CAC");
				omsOrderDetailStatusUpdateforCancellation.setEventReferenceId(omsCancelId.intValue());
				omsOrderDetailStatusUpdateforCancellation.setFulfillId(omsCustOrdHead.getOrderRequestorId().intValue());
				omsOrderDetailStatusUpdateforCancellation.setFulfillType("S");
				omsOrderDetailStatusUpdateforCancellation.setSourceId(omsCustOrdHead.getOrderRequestorId().intValue());
				omsOrderDetailStatusUpdateforCancellation.setSourceType("ST");
				omsOrderDetailStatusUpdateforCancellation.setProductSku(cancellationItems.getItem());
				omsOrderDetailStatusUpdateforCancellation.setQuantity(cancellationItems.getCancelQtySuom().intValue());
				omsOrderDetailStatusUpdateforCancellation.setOrderDetailId(cancellationItems.getLineNo());
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = null;
				try {
					datatypeFactory = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException e) {
					log.warn(e.toString());
				}
				XMLGregorianCalendar itemcancellationlastupdate = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				omsOrderDetailStatusUpdateforCancellation.setUpdateDate(itemcancellationlastupdate);
				arrayofOmsOrderStatusUpateDetailForCancellation.getOrderDetailStatus().add(omsOrderDetailStatusUpdateforCancellation);
			}
		}
		headerStatuCancellation.setOrderDetailStatuses(arrayofOmsOrderStatusUpateDetailForCancellation);
		System.out.println("headerCancellation" + headerStatuCancellation.getEntityId());
		System.out.println("headerCancellation" + headerStatuCancellation.getApplicationId());
		System.out.println("headerCancellation" + headerStatuCancellation.getOmsOrderId());
		log.info("Size of the list is " + headerStatuCancellation.getOrderDetailStatuses().getOrderDetailStatus().size());
		List<OmsOrderDetailStatus> omsOrderStatusList = headerStatuCancellation.getOrderDetailStatuses().getOrderDetailStatus();
		for (OmsOrderDetailStatus omsOrderStatus : omsOrderStatusList) {
			log.info("omsOrderStatus event comments" + omsOrderStatus.getEventComment());
			log.info("omsOrderStatus event id " + omsOrderStatus.getEventId());
			log.info("omsOrderStatus event refernce id" + omsOrderStatus.getEventReferenceId());
			log.info("omsOrderStatus fulfill id" + omsOrderStatus.getFulfillId());
			log.info("omsOrderStatus fullfill type" + omsOrderStatus.getFulfillType());
			log.info("omsOrderStatus source loc id" + omsOrderStatus.getSourceId());
			log.info("omsOrderStatus source loc type" + omsOrderStatus.getSourceType());
			log.info("omsOrderStatus lineno" + omsOrderStatus.getOrderDetailId());
			log.info("omsOrderStatus item" + omsOrderStatus.getProductSku());
			log.info("omsOrderStatus quantity" + omsOrderStatus.getQuantity());
		}
		return headerStatuCancellation;
	}

	private void updateCancelQty(BigDecimal tsfNo, Long cancelQty) {
		log.info("Update Cancel Qty");
		Connection connection = null;
		Connection connection1 = null;
		PreparedStatement prepStatement = null;
		PreparedStatement prepStatement1 = null;
		int candidateId = 0;
		ResultSet rs = null;
		BigDecimal ordcust_no = null;
		try {
			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			String query = "select ordcust_no from ordcust where tsf_no = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setBigDecimal(1, tsfNo);
			log.info("setted tsfNo in preparedStatement");
			rs = prepStatement.executeQuery();
			log.info("after getting rs");
			while (rs.next()) {
				ordcust_no = rs.getBigDecimal("ordcust_no");
				log.info("ordcust_no for " + tsfNo + "is" + ordcust_no);
			}
			updateOrdcustCancelQty(ordcust_no, cancelQty);
		} catch (Exception e) {
			log.info("");
		} finally {
			try {
				OMSUtil.closeDBConnection(connection, prepStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
	} // End of updateCancelQty

	private void updateOrdcustCancelQty(BigDecimal ordcustNo, Long cancelQty) {
		Connection connection1 = null;
		PreparedStatement prepStatement1 = null;
		int candidateId = 0;
		ResultSet rs = null;
		try {
			connection1 = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			String query = "update ORDCUST_DETAIL set QTY_CANCELLED_SUOM = ? where ordcust_no = ?";
			prepStatement1 = connection1.prepareStatement(query);
			prepStatement1.setLong(1, cancelQty);
			prepStatement1.setBigDecimal(2, ordcustNo);
			log.info("setted tsfNo in preparedStatement");
			int rowAffected = prepStatement1.executeUpdate();
			if (rowAffected == 1) {
				// get candidate id
				rs = prepStatement1.getGeneratedKeys();
				if (rs.next())
					candidateId = rs.getInt(1);
			}
			log.info("UPDATE cancel qty for ordcust No" + ordcustNo + "Is succesful");
		} catch (Exception e) {
			log.info("Exception e " + e);
		} finally {
			try {
				OMSUtil.closeDBConnection(connection1, prepStatement1, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
	} // End of updateOrdcustCancelQty

	public String pickQtyCheck(OmsCoFulfillDetail omsCoFulfillDetail, String custOrderId, BigDecimal cacelReqQty) {
		log.info("pickQtyCheck ");
		Connection conn = null;
		String status = "";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int quantity = 0;
		String query = "SELECT 'Item picked' status " + "FROM oms_cust_ord_head omsh," + "oms_cust_ord_item omsi," + "oms_co_fulfill_detail ocf," + "ful_ord@simqa fo," + "ful_ord_line_item@simqa  fi "
				+ "WHERE omsh.oms_cust_ord_no = omsi.oms_cust_ord_no" + "   AND ocf.oms_cust_ord_no  = omsi.oms_cust_ord_no" + "   AND ocf.line_no          = omsi.line_no"
				+ "   AND ocf.fulfill_order_no = fo.external_id" + "   AND omsh.cust_order_no   = fo.cust_order_id" + "   AND fo.ID                = fi.ful_ord_id"
				+ "   AND fi.item_id           = omsi.item" + "   AND omsh.cust_order_no   = ?" + "   AND omsi.item            = ?" + "   AND omsi.line_no         = ?"
				+ "   AND (fi.quantity_picked - fi.quantity_delivered) > 0" + "   AND ((qty_ordered_suom - cum_qty_delivered - qty_cancelled) - (fi.quantity_picked  - fi.quantity_delivered)) < ? ";
		try {
			log.info("pickQtyCheck  " + query);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderId);
			preparedStatement.setString(2, omsCoFulfillDetail.getItem());
			preparedStatement.setInt(3, omsCoFulfillDetail.getLineNo().intValue());
			preparedStatement.setInt(4, cacelReqQty.intValue());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				status = "N";
			} else {
				status = "Y";
			}
		} catch (Exception el) {
			log.info(el.getMessage());
			log.info("e1 " + el);
		}
		return status;
	}

//modified by madhu for sim picking qty check
	public String pickQtyCheckMod(OmsCoFulfillDetail omsCoFulfillDetail, String custOrderId, BigDecimal cacelReqQty) {
		log.info("pickQtyCheck ");
		Connection conn = null;
		String status = "";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int quantity = 0;
		String query = "SELECT 'NOT_PICKED' status " + "FROM ful_ord fo," + "ful_ord_line_item fi " + "WHERE  fo.cust_order_id=?" + " AND fo.ID = fi.ful_ord_id" + " AND fo.external_id=?"
				+ " AND fi.item_id = ?" + " AND (fi.QUANTITY_ORDERED-(fi.QUANTITY_PICKED+fi.QUANTITY_CANCELED)) >=?";
		try {
			log.info("pickQtyCheck  " + query);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderId);
			preparedStatement.setInt(2, omsCoFulfillDetail.getFulfillOrderNo().intValue());
			preparedStatement.setString(3, omsCoFulfillDetail.getItem());
			preparedStatement.setInt(4, cacelReqQty.intValue());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				status = "NOT_PICKED";
			} else {
				status = "PICKED";
			}
		} catch (Exception el) {
			log.info(el.getMessage());
			log.info("e1 " + el);
		}
		// 26-10-2020 changes for sim pick up close connection -- mani and madhu
		finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.info("Error while closing the sim connection for order no: " + custOrderId);
				log.info(e.getMessage());
			}
		}
		return status;
	}

//mani changes new function created from util
	public BigDecimal getselectedandDistroQty(String item, BigDecimal tsfNo) throws Exception {
		log.info("***Start getselectedandDistroQty ***");
		BigDecimal totalSelectedDistroQty = BigDecimal.ZERO;
		BigDecimal totalDistroQty = BigDecimal.ZERO;
		BigDecimal totalShipQty = BigDecimal.ZERO;
		Connection conn = null;
		BigDecimal selectedQty = BigDecimal.ZERO;
		BigDecimal distroQty = BigDecimal.ZERO;
		BigDecimal shipQty = BigDecimal.ZERO;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		log.info("item " + item);
		log.info("tsfNo " + tsfNo);
		String query = "select distro_Qty,selected_Qty,ship_Qty  from tsfdetail where item= ? and tsf_no =?";
		log.info("query " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			log.info("setted item in preparedStatement");
			preparedStatement.setBigDecimal(2, tsfNo);
			log.info("setted tsfNo in preparedStatement");
			rs = preparedStatement.executeQuery();
			log.info("after getting rs");
			while (rs.next()) {
				log.info("inside while");
				distroQty = rs.getBigDecimal("distro_Qty");
				selectedQty = rs.getBigDecimal("selected_Qty");
				shipQty = rs.getBigDecimal("ship_Qty");
				log.info("selectedQty " + selectedQty);
				log.info("distroQty " + distroQty);
				log.info("shipQty " + shipQty);
				if (selectedQty == null) {
					selectedQty = BigDecimal.ZERO;
				}
				if (distroQty == null) {
					distroQty = BigDecimal.ZERO;
				}
				if (shipQty == null) {
					shipQty = BigDecimal.ZERO;
				}
				log.info("+++++++++++++before setting value for totalSelectedDistroQty" + totalSelectedDistroQty);
				log.info("+++++++++++++before setting value for totalSelectedDistroQty with shipQty" + totalSelectedDistroQty);
				totalDistroQty = selectedQty.add(distroQty);
				log.info("+++++++++++++after setting value for totalSelectedDistroQty with shipQty" + totalDistroQty);
				// mani changes old chages
				// totalShipQty=shipQty.add(distroQty);
				totalShipQty = shipQty;
				log.info("+++++++++++++before setting value for totalSelectedDistroQty with distroQty" + totalShipQty);
				totalSelectedDistroQty = totalShipQty.add(totalDistroQty);
				log.info("+++++++++++++after setting value for totalSelectedDistroQty with distroQty" + totalSelectedDistroQty);
				log.info("totalSelecetedDistroQty " + totalSelectedDistroQty);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return totalSelectedDistroQty;
	}
}
