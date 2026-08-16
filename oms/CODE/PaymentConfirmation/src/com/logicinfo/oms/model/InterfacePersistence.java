package com.logicinfo.oms.model;


import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.ejb.OrdcustDetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.ConfirmType;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfilOrdDesc;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef;
import com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc;
import com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderService;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentService;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryPortType;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import retail.siebel.com.integration.CustomerDetails;
import retail.siebel.com.integration.CustomerOrderHeaderLevel;
import retail.siebel.com.integration.CustomerOrderHeaderLevelResponse;
import retail.siebel.com.integration.FulfillmentDetails;
import retail.siebel.com.integration.InboundordercreationbpelprocessClientEp;
import retail.siebel.com.integration.ItemLevelDetails;
import retail.siebel.com.integration.SiebelOrderFeedWebservice;
import retail.siebel.com.integration.TenderDetails;


public class InterfacePersistence
{
    public InterfacePersistence()
    {
	super();
    }
    private final static Logger log=Logger.getLogger(InterfacePersistence.class.getName());
    FulfilOrdCfmCol fulfilOrdCfmCol=null;
    ConfirmType confirmType=null;
    String extCustOrdNo="";
    HashSet<String> transferSet=new HashSet<String>();

    public String processWebserviceResponse(FulfilOrdCfmCol fulfilOrdCfmCol,
					    ArrayList<OmsTempCoFo> temp) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	log.info("inside processWebserviceResponse");

	String responseStatus="";

	for(OmsTempCoFo omsTempCoFo:temp)
	{
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"inside loop");
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+omsTempCoFo.getOmsCustOrdNo());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getItem() "+omsTempCoFo.getItem());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getLineNo() "+omsTempCoFo.getLineNo());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getFulfillOrderNo() "+
		     omsTempCoFo.getFulfillOrderNo());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getSourceLocId() "+
		     omsTempCoFo.getSourceLocId());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getSourceLocationType() "+
		     omsTempCoFo.getSourceLocationType());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getFulfillLocId() "+
		     omsTempCoFo.getFulfillLocId());
	    log.info("omsCustOrdNo "+omsTempCoFo.getOmsCustOrdNo()+"omsTempCoFo.getFulfillLocationType() "+
		     omsTempCoFo.getFulfillLocationType());

	    OmsCustOrdItem omscustOrdItem=
		       session.getOmsCustOrdItemFindByItem(omsTempCoFo.getOmsCustOrdNo(),omsTempCoFo.getItem(),
							   omsTempCoFo.getLineNo());
	    log.info("after fetching from item table");
	    //log.info("fulfilOrdCfmCol.getCollectionSize() "+fulfilOrdCfmCol.getCollectionSize());
	    if(fulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty()&&fulfilOrdCfmCol.getCollectionSize()==0)
	    {
		log.info("inside if condition");
		//confirmed
		responseStatus="C";
		log.info("Confirmed");

		omsTempCoFo.setRmsResponseCode("C");

		omscustOrdItem.setStatus("S");
		session.mergeOmsCustOrdItem(omscustOrdItem);
	    }
	    else
	    {
		confirmType=fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getConfirmType();
		if(confirmType!=null&&confirmType.value().equals("X"))
		{
		    log.info("Confirm type is X");
		    responseStatus="X";
		    omsTempCoFo.setRmsResponseCode("X");


		}
		else if(confirmType!=null&&confirmType.value().equals("P"))
		{
		    log.info("inside confirmType P condition omsTempCoFo.getFulfillOrderNo() "+
			     omsTempCoFo.getFulfillOrderNo());
		    log.info("Confirm type is P");

		    responseStatus="P";
		    if(omsTempCoFo.getRmsResponseCode()==null||omsTempCoFo.getRmsResponseCode().isEmpty())
		    {

			omsTempCoFo.setRmsResponseCode("P");
		    }
		    omscustOrdItem.setStatus("P");
		    session.mergeOmsCustOrdItem(omscustOrdItem);
		}
		else
		{
		    //received validation error from RMS
		    responseStatus="E";
		    log.info("Validation error");


		    omscustOrdItem.setStatus("F");
		    session.mergeOmsCustOrdItem(omscustOrdItem);
		    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Validation error"));
		}

	    }

	}
	return responseStatus;
    }

    public void callRMSCancellationWebservice(ProcessedObject temp) throws EntityAlreadyExistsWSFaultException,
									   IllegalArgumentWSFaultException,IllegalStateWSFaultException,ValidationWSFaultException,
									   SOAPException
    {
	log.info(" cancelling in RMS");
	Date date1=new Date();
	log.info("Start Time in cancelling in RMS "+date1);
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	FulfillOrderService fulfillOrderService=new FulfillOrderService();
	FulfillOrderPortType fulfillOrderPortType=fulfillOrderService.getFulfillOrderPort();
	FulfilOrdColRef fulfilOrdColRef=new FulfilOrdColRef();
	fulfilOrdColRef.setCollectionSize(1); //collection size
	FulfilOrdRef fulfilOrdRef=new FulfilOrdRef();
	fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
	fulfilOrdRef.setFulfillLocId(temp.getOmsCustOrdReserve().getLoc().longValue());
	FulfillLocType fulfillLocType=fulfilOrdRef.getFulfillLocType();
	fulfilOrdRef.setFulfillLocType(fulfillLocType.fromValue(temp.getOmsCustOrdReserve().getLocType()));
	fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.getOmsCustOrdReserve().getFulfillOrderNo()));
	log.info("fulfill ord no sent to rms:"+temp.getOmsCustOrdReserve().getFulfillOrderNo()+"for item:"+
		 temp.getOmsCustOrdReserve().getItem()+"oms_cust_ord_n0:"+
		 temp.getOmsCustOrdReserve().getOmsCustOrdNo());
	fulfilOrdRef.setSourceLocId(temp.getOmsCustOrdReserve().getRmsResvLoc().longValue());
	FulfilOrdDtlRef fulfilOrdDtlRef=new FulfilOrdDtlRef();
	fulfilOrdDtlRef.setCancelQtySuom(temp.getOmsCustOrdReserve().getQty()); //check
	fulfilOrdDtlRef.setItem(temp.getOmsCustOrdReserve().getItem());
	//   fulfilOrdDtlRef.setRefItem(value);
	OmsCustOrdItem omsCustOrdItem=
		   session.getOmsCustOrdItemFindByItem(temp.getOmsCustOrdReserve().getOmsCustOrdNo(),temp.getOmsCustOrdReserve().getItem(),
						       temp.getOmsCustOrdReserve().getLineNo());
	fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
	fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
	fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
	fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.getOmsCustOrdReserve().getRmsResvLocType()));
	fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
	InvocationSuccess result=fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
	log.info("Result from rms---"+result.getSuccessMessage());
	Date date2=new Date();
	log.info("End Time in cancelling in RMS "+date2);
	log.info("Difference in Start and End Time in cancelling in RMS "+(date2.getTime()-date1.getTime())/1000+
		 " seconds");
    }

    public void callSIMCancelllationWS(BigDecimal omsCustOrdNo,
				       List<OmsTempCoFo> temp) throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,

	    SOAPException,EntityAlreadyExistsWSFaultException,
		   com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,

	    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,

	    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,

	    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException
    {
	log.info("callSIMCancelllationWS started as part of rollback");

	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	if(omsCustOrdHead.getSubCustOrderNo().equals("1"))
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo();
	}
	else
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+omsCustOrdHead.getSubCustOrderNo();
	    if(omsCustOrdHead.getSubCustOrderNo().trim().length()<3)
	    {
		if(omsCustOrdHead.getSubCustOrderNo().trim().length()==2)
		{
		    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+"-0"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
		else
		{
		    extCustOrdNo=
       omsCustOrdHead.getCustOrderNo().trim()+"-00"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
	    }
	}
	StoreFulfillmentOrderService storeFulfillmentOrderService=new StoreFulfillmentOrderService();
	StoreFulfillmentOrderPortType storeFulfillmentOrderPortType=
		   storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
	Holder<FulfilOrdColRef> fulfilOrdColRef=new Holder<FulfilOrdColRef>();
	FulfilOrdColRef fulfilOrdColRef1=new FulfilOrdColRef();
	fulfilOrdColRef1.setCollectionSize(1);

	fulfilOrdColRef.value=fulfilOrdColRef1;
	FulfilOrdRef fulfilOrdRef=new FulfilOrdRef();
	fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
	fulfilOrdRef.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
	fulfilOrdRef.setFulfillLocType(fulfilOrdRef.getFulfillLocType().fromValue(temp.get(0).getFulfillLocationType()));

	fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue()); //**
	fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));

	fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.get(0).getFulfillOrderNo()));
	log.info("fulfil ord_no="+temp.get(0).getFulfillOrderNo()+"Sourc_loc="+temp.get(0).getSourceLocId()+
		 fulfilOrdRef.getSourceLocType()+"fulfill_loc="+temp.get(0).getFulfillLocId()+
		 fulfilOrdRef.getFulfillLocType());
	for(OmsTempCoFo tempCoFo:temp)
	{
	    log.info("extCustOrdNo="+extCustOrdNo+"Item sent for cancelllatin is "+tempCoFo.getItem());
	    FulfilOrdDtlRef fulfilOrdDtlRef=new FulfilOrdDtlRef();
	    // fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());
	    log.info("cancel qy="+tempCoFo.getOrderQty());
	    fulfilOrdDtlRef.setCancelQtySuom(tempCoFo.getOrderQty()); //check
	    fulfilOrdDtlRef.setItem(tempCoFo.getItem());

	    OmsCustOrdItem omsCustOrdItem=
		       session.getOmsCustOrdItemFindByItem(omsCustOrdNo,tempCoFo.getItem(),tempCoFo.getLineNo());
	    fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
	    fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
	    fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
	}
	fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
	fulfilOrdColRef.value=fulfilOrdColRef1;
	try
	{
	    log.info("fulfilOrdColRef size="+fulfilOrdColRef.value.getFulfilOrdRef().size());
	    log.info("---"+fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());
	    storeFulfillmentOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);

	}
	catch(Exception e)
	{
	    log.error("Error in sim cancellation");
	}
    }

    public void callRMSCancellationWebservice(BigDecimal omsCustOrdNo,
					      List<OmsTempCoFo> temp) throws EntityAlreadyExistsWSFaultException,IllegalArgumentWSFaultException,
									     IllegalStateWSFaultException,ValidationWSFaultException,
									     SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	if(omsCustOrdHead.getSubCustOrderNo().equals("1"))
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo();
	}
	else
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+omsCustOrdHead.getSubCustOrderNo();
	    if(omsCustOrdHead.getSubCustOrderNo().trim().length()<3)
	    {
		if(omsCustOrdHead.getSubCustOrderNo().trim().length()==2)
		{
		    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+"-0"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
		else
		{
		    extCustOrdNo=
       omsCustOrdHead.getCustOrderNo().trim()+"-00"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
	    }
	}
	FulfillOrderService fulfillOrderService=new FulfillOrderService();
	FulfillOrderPortType fulfillOrderPortType=fulfillOrderService.getFulfillOrderPort();
	FulfilOrdColRef fulfilOrdColRef=new FulfilOrdColRef();
	fulfilOrdColRef.setCollectionSize(1); //collection size
	FulfilOrdRef fulfilOrdRef=new FulfilOrdRef();
	fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
	fulfilOrdRef.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
	FulfillLocType fulfillLocType=fulfilOrdRef.getFulfillLocType();
	fulfilOrdRef.setFulfillLocType(fulfillLocType.fromValue(temp.get(0).getFulfillLocationType()));
	fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.get(0).getFulfillOrderNo()));
	log.info("fulfill ord no sent to rms:"+temp.get(0).getFulfillOrderNo()+"for item:"+temp.get(0).getItem()+
		 "oms_cust_ord_n0:"+omsCustOrdNo);
	fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue());
	fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.get(0).getSourceLocationType())); //**
	for(OmsTempCoFo omsTempCoFo:temp)
	{
	    FulfilOrdDtlRef fulfilOrdDtlRef=new FulfilOrdDtlRef();
	    fulfilOrdDtlRef.setCancelQtySuom(omsTempCoFo.getFoConfQty()); //check
	    fulfilOrdDtlRef.setItem(omsTempCoFo.getItem());
	    //   fulfilOrdDtlRef.setRefItem(value);
	    OmsCustOrdItem omsCustOrdItem=
		       session.getOmsCustOrdItemFindByItem(omsCustOrdNo,omsTempCoFo.getItem(),omsTempCoFo.getLineNo());
	    fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
	    fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
	    fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
	}

	fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));
	fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
	InvocationSuccess result=fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
	log.info("Result from rms---"+result.getSuccessMessage());
    }

    public void callSeibelWebservice(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {
	log.info("calling siebel webservice for omsCustOrdNo"+omsCustOrdNo);
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	CustomerOrderHeaderLevel customerOrderHeaderLevel=new CustomerOrderHeaderLevel();
	try
	{

	    if(omsCustOrdHead.getCustId()!=null)
		customerOrderHeaderLevel.setCustId(omsCustOrdHead.getCustId());
	    customerOrderHeaderLevel.setOmsCustOrdNo(omsCustOrdNo.toString());
	    if(omsCustOrdHead.getSubCustOrderNo()!=null)
	    {
		customerOrderHeaderLevel.setSubCustOrderNo(Long.valueOf(omsCustOrdHead.getSubCustOrderNo()));
	    }
	    GregorianCalendar c=new GregorianCalendar();
	    if(omsCustOrdHead.getConsumerDlyTime()!=null)
		c.setTime(omsCustOrdHead.getConsumerDlyTime());
	    XMLGregorianCalendar consumerDlyTime=null;
	    try
	    {
		consumerDlyTime=DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    }
	    catch(DatatypeConfigurationException e)
	    {
	    }
	    if(omsCustOrdHead.getConsumerDlyTime()!=null)
		customerOrderHeaderLevel.setConsumerDlyTime(consumerDlyTime);
	    customerOrderHeaderLevel.setCustOrderNo((omsCustOrdHead.getCustOrderNo()));

	    // customerOrderHeaderLevel.setComment(custOrderDesc.getComments());
	    c.setTime(omsCustOrdHead.getCreateDatetime());
	    XMLGregorianCalendar createDatetime=null;
	    try
	    {
		createDatetime=DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    }
	    catch(DatatypeConfigurationException e)
	    {
	    }
	    customerOrderHeaderLevel.setCreateDatetime(createDatetime);
	    customerOrderHeaderLevel.setCustOrderType(omsCustOrdHead.getCustOrderType());
	    customerOrderHeaderLevel.setOrderRequestorId(String.valueOf(omsCustOrdHead.getOrderRequestorId()));
	    customerOrderHeaderLevel.setDeliveryType(omsCustOrdHead.getDeliveryType());
	    if(omsCustOrdHead.getPickLoc()!=null)
		customerOrderHeaderLevel.setPickLoc(omsCustOrdHead.getPickLoc().longValue());
	    // customerOrderHeaderLevel.setServiceReqType(omsCustOrdHead.getServiceReqType());
	    CustomerDetails customerDetails=new CustomerDetails();
	    try
	    {
		OmsCustOrdAddress omsCustOrdAddress=session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
		if(omsCustOrdAddress.getBillAdd1()!=null)
		    customerDetails.setBillAdd1(omsCustOrdAddress.getBillAdd1());

		if(omsCustOrdAddress.getBillAdd2()!=null)
		    customerDetails.setBillAdd2(omsCustOrdAddress.getBillAdd2());
		if(omsCustOrdAddress.getBillAdd3()!=null)
		    customerDetails.setBillAdd3(omsCustOrdAddress.getBillAdd3());
		if(omsCustOrdAddress.getBillCity()!=null)
		    customerDetails.setBillCity(omsCustOrdAddress.getBillCity());
		if(omsCustOrdAddress.getBillFirstName()!=null)
		    customerDetails.setBillFirstName(omsCustOrdAddress.getBillFirstName());
		if(omsCustOrdAddress.getBillLastName()!=null)
		    customerDetails.setBillLastName(omsCustOrdAddress.getBillLastName());
		if(omsCustOrdAddress.getBillPost()!=null)
		    customerDetails.setBillPost(omsCustOrdAddress.getBillPost());
		if(omsCustOrdAddress.getDeliverAdd1()!=null)
		    customerDetails.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
		if(omsCustOrdAddress.getDeliverAdd2()!=null)
		    customerDetails.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());
		if(omsCustOrdAddress.getDeliverAdd3()!=null)
		    customerDetails.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
		if(omsCustOrdAddress.getDeliverCity()!=null)
		    customerDetails.setDeliverCity(omsCustOrdAddress.getDeliverCity());
		if(omsCustOrdAddress.getDeliverCountry()!=null)
		    customerDetails.setDeliverCountry(omsCustOrdAddress.getDeliverCountry());
		if(omsCustOrdAddress.getDeliverFirstName()!=null)
		    customerDetails.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
		if(omsCustOrdAddress.getDeliverLastName()!=null)
		    customerDetails.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
		if(omsCustOrdAddress.getDeliverPost()!=null)
		    customerDetails.setDeliverPost(omsCustOrdAddress.getDeliverPost());
		if(omsCustOrdAddress.getDeliverState()!=null)
		    customerDetails.setDeliverState(omsCustOrdAddress.getDeliverState());
		log.info(omsCustOrdHead.getOmsCustOrdNo()+"Address done successfully");
		customerOrderHeaderLevel.setCustomerDetails(customerDetails);
		log.info(omsCustOrdHead.getOmsCustOrdNo()+"Added Customer Details");
	    }
	    catch(Exception e)
	    {
                log.info(omsCustOrdHead.getOmsCustOrdNo()+" Exception while setting address in payment confirmation "+e.getMessage());
	    }
	    List<OmsCustOrdItem> omsCustOrdItemList=session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
	    for(OmsCustOrdItem item:omsCustOrdItemList)
	    {
		log.info("Setting items");
		ItemLevelDetails itemLevelDetails=new ItemLevelDetails();
		log.info("Item detail created");
		itemLevelDetails.setItem(item.getItem());
		itemLevelDetails.setQtyOrderedSuom(item.getQtyOrderedSuom());
		//itemLevelDetails.setItemType(item.getItemType());
		itemLevelDetails.setBackorderInd(item.getBackorderInd());
		if(item.getBackorderDlyDate()!=null)
		{
		    c.setTime(item.getBackorderDlyDate());
		    XMLGregorianCalendar backorderDlyDate=null;
		    try
		    {
			backorderDlyDate=DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		    }
		    catch(DatatypeConfigurationException e)
		    {
		    }
		    itemLevelDetails.setBackorderDlyDate(backorderDlyDate);
		}
		if(item.getComments()!=null)
		    itemLevelDetails.setComments(item.getComments());
		if(item.getOrigUnitRetail()!=null)
		    itemLevelDetails.setRetailCurr(item.getOrigUnitRetail().toString());
		if(item.getStandardUom()!=null)
		    itemLevelDetails.setStandardUom(item.getStandardUom());
		if(item.getSubstituteAllowInd()!=null)
		    itemLevelDetails.setSubstituteAllowInd(item.getSubstituteAllowInd());
		if(item.getTransactionUom()!=null)
		    itemLevelDetails.setTransactionUom(item.getTransactionUom());
		if(item.getStandardUom()!=null)
		    itemLevelDetails.setUnitRetail(item.getUnitRetail());
		if(item.getStandardUom()!=null)
		    itemLevelDetails.setLineNo(item.getLineNo().longValue());
		log.info("Before fetching fulfilment detail");
		List<OmsCoFulfillDetail> omsCoFulfillDetailLst=
				  session.getOmsCoFulfillDetailFindFulfillByLineNo(item.getLineNo(),omsCustOrdHead.getOmsCustOrdNo());
		List<FulfillmentDetails> fulfillmentDetailsList=itemLevelDetails.getFulfillmentDetails();
		log.info("Setting fulfilment");
		for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailLst)
		{
		    FulfillmentDetails fulfillmentDetails=new FulfillmentDetails();
		    fulfillmentDetails.setFulfillOrderNo(omsCoFulfillDetail.getFulfillOrderNo().longValue());
		    fulfillmentDetails.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
		    fulfillmentDetails.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
		    fulfillmentDetails.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
		    fulfillmentDetails.setSourceLocType(omsCoFulfillDetail.getSourceLocType());
		    fulfillmentDetails.setItem(omsCoFulfillDetail.getItem());
		    if(omsCustOrdHead.getOrderCreateReserveInd().equals("R"))
		    {
			BigDecimal loc=
	session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdHead.getOmsCustOrdNo(),omsCoFulfillDetail.getItem(),
							      (item.getLineNo())).get(0).getRmsResvLoc();
			if(loc!=null||loc.longValue()>=0)
			{

			    fulfillmentDetails.setRMSResvLoc(loc.longValue());
			}
			String locType=
	session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdHead.getOmsCustOrdNo(),omsCoFulfillDetail.getItem(),
							      (item.getLineNo())).get(0).getRmsResvLocType();
			if(locType!=null||locType.isEmpty()==false)
			{
			    fulfillmentDetails.setRMSResvLocType(locType);
			}
			BigDecimal resvQty=
    session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustOrdHead.getOmsCustOrdNo(),omsCoFulfillDetail.getItem(),
							  (item.getLineNo())).get(0).getRmsResvQty();
			if(resvQty!=null||resvQty.longValue()>=0)
			{
			    fulfillmentDetails.setRMSResvQty(resvQty.longValue());
			}
			try
			{

			    BigDecimal poNo=session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,omsCoFulfillDetail.getFulfillOrderNo().toString(),omsCoFulfillDetail.getSourceLoc(),omsCoFulfillDetail.getFulfillLoc()).get(0).getOrderNo();
			    if(poNo!=null)
			    {
				fulfillmentDetails.setPoNo(poNo.longValue());
			    }

			}
			catch(Exception e)
			{

			}
			try
			{

			    log.info("inside tsf no");
			    log.info("extCustOrdNo="+extCustOrdNo+"omsCoFulfillDetail.getFulfillOrderNo()"+
				     omsCoFulfillDetail.getFulfillOrderNo());
			    BigDecimal tsfN0=session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,omsCoFulfillDetail.getFulfillOrderNo().toString(),
					  omsCoFulfillDetail.getSourceLoc(),
					  omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo();
			    if(tsfN0!=null)
			    {
				fulfillmentDetails.setTsfNo(session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,
												omsCoFulfillDetail.getFulfillOrderNo().toString(),omsCoFulfillDetail.getSourceLoc(),
												omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo().longValue());

			    }
			    log.info("tsfN0="+tsfN0);
			}
			catch(Exception e)
			{

			}

		    }
		    fulfillmentDetailsList.add(fulfillmentDetails);
		    log.info("Added fulfilment");
		}

		customerOrderHeaderLevel.getItemLevelDetails().add(itemLevelDetails);
	    }
	    log.info("item successful");
	    List<OmsCustOrdTender> omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrdNo);
	    TenderDetails tenderDetails=new TenderDetails();
	    for(OmsCustOrdTender tender:omsCustOrdTenderList)
	    {
		if(tender.getCcAuthNo()!=null)
		    tenderDetails.setCcAuthNo(tender.getCcAuthNo());
		if(tender.getCcAuthSrc()!=null)
		    tenderDetails.setCcAuthSrc(tender.getCcAuthSrc());
		if(tender.getCcCardholderVerf()!=null)
		    tenderDetails.setCcCardholderVerf(tender.getCcCardholderVerf());
		if(tender.getCcEntryMode()!=null)
		    tenderDetails.setCcEntryMode(tender.getCcEntryMode());
		if(tender.getCcExpDate()!=null)
		    c.setTime(tender.getCcExpDate());
		XMLGregorianCalendar ccExpDate=null;
		try
		{
		    ccExpDate=DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		}
		catch(DatatypeConfigurationException e)
		{
		}
		if(tender.getCcExpDate()!=null)
		    tenderDetails.setCcExpDate(ccExpDate);
		if(tender.getCcNo()!=null)
		    tenderDetails.setCcNo(tender.getCcNo());
		if(tender.getCcSpecCond()!=null)
		    tenderDetails.setCcSpecCond(tender.getCcSpecCond());
		if(tender.getCcTermId()!=null)
		    tenderDetails.setCcTermId(tender.getCcTermId());
		if(tender.getTenderAmt()!=null)
		    tenderDetails.setTenderAmt(tender.getTenderAmt());
		if(tender.getTenderTypeGroup()!=null)
		    tenderDetails.setTenderTypeGroup(tender.getTenderTypeGroup());
		tenderDetails.setTenderSeqNo((session.getOmsCustOrdTenderFindByOmsCustOrdNo((omsCustOrdNo)).get(0).getTenderSeqNo().longValue()));
		if(tender.getTenderTypeId()!=null)
		    tenderDetails.setTenderTypeId(tender.getTenderTypeId().longValue());
		log.info("tender successful");
		customerOrderHeaderLevel.getTenderDetails().add(tenderDetails);
	    }
	}
	catch(Exception e)
	{
	    log.error("Error in setting objects for JAXB"+e);
	}
	CustomerOrderHeaderLevelResponse customerOrderHeaderLevelResponse=null;
	try
	{

	    InboundordercreationbpelprocessClientEp inboundordercreationbpelprocessClientEp=
		       new InboundordercreationbpelprocessClientEp();
	    SiebelOrderFeedWebservice siebelOrderFeedWebservice=
		       inboundordercreationbpelprocessClientEp.getSiebelOrderFeedWebservicePt();

	    customerOrderHeaderLevelResponse=siebelOrderFeedWebservice.publishToSiebel(customerOrderHeaderLevel);
	    log.info("publishing passes");

	}
	catch(Exception e)
	{
	    log.error("Publishing to seibel failed"+e);
	    OmsRepublishData omsRepublishData=new OmsRepublishData();
	    omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());

	    omsRepublishData.setApplicationId("ORPOS");
	    omsRepublishData.setErrorMsg("UNABLE TO CALL WEB SERVICE.");
	    omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
	    omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
	    omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
	    omsRepublishData.setRepublishStatus("P");
	    omsRepublishData.setTransactionKey("ORDER FEED");
	    String webserviceURL=session.getOmsWebserviceUriDetailFindByWebserviceName("SIEBEL_ORDER_FEED");
	    omsRepublishData.setWebServiceId(webserviceURL);
	    try
	    {
		JAXBContext context=JAXBContext.newInstance(CustomerOrderHeaderLevel.class);
		Marshaller marshel=context.createMarshaller();
		Writer os=new StringWriter();
		marshel.marshal(customerOrderHeaderLevel,os);
		omsRepublishData.setXmlMsg(os.toString());
		session.persistOmsRepublishData(omsRepublishData);
		log.info("persisten in omsRepublish data");
	    }
	    catch(Exception f)
	    {
		log.error("JAXB failed"+e);
	    }

	}

    }

    //------------------------------------------------------------------------------------------------------


    public void adjustInventoryByItemLocation(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {
	log.info("Calling SIM invetory adustment in case of Unreserve");
	Date startTimeSIM_Invetory=new Date();
	log.info("startTime of SIM_Invetory "+startTimeSIM_Invetory);
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	String errorReason="";
	List<OmsCustOrdReserve> omsCustOrdReserveList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
	for(OmsCustOrdReserve custOrdItems:omsCustOrdReserveList)
	{
	    if(custOrdItems.getRmsResvLocType().equals("SU")==false&&
	       custOrdItems.getRmsResvLocType().equals("WH")==false)
	    {
		if(custOrdItems.getQty().intValue()>0)
		{
		    try
		    {
			InventoryAdjustmentService inventoryAdjustmentService=new InventoryAdjustmentService();
			InventoryAdjustmentPortType inventoryAdjustmentPortType=
						 inventoryAdjustmentService.getInventoryAdjustmentPort();

			StrAdjModVo strAdjModVo=new StrAdjModVo();
			StrAdjItmMod strAdjItemMod=new StrAdjItmMod();

			strAdjItemMod.setItemId(custOrdItems.getItem());
			strAdjItemMod.setReasonId(Integer.parseInt(session.getOmsSystemParametersFindIndValue("INV_UNRESV_CODE",
													      "OMS_INV_ADJ_RSN_CODE")));
			strAdjItemMod.setQuantity(custOrdItems.getQty());
			strAdjItemMod.setCaseSize(new BigDecimal(1));
			strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			strAdjModVo.setStoreId(custOrdItems.getRmsResvLoc().longValue());
			strAdjModVo.setComments("Unreserved for customer order id :"+input.getCustomerOrderNo());

			StrAdjRef strAdjRef=inventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment(strAdjModVo);
			log.info("Call to SIM invetory adustment in case of reserve success");
			//       return strAdjRef.getAdjustmentId();
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException iawsfe)
		    {
			errorReason=
	       iawsfe.getMessage()+" "+" Error Reason :"+iawsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException iswsfe)
		    {
			errorReason=
	       iswsfe.getMessage()+" "+" Error Reason :"+iswsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException vwsfe)
		    {
			errorReason=
	       vwsfe.getMessage()+" "+" Error Reason :"+vwsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(Exception e)
		    {
			log.error(" --> Exception ST: "+e);
			throw new SOAPException(e.getMessage());
		    }
		}
	    }

	} //for ends

	Date endTimeSIM_Invetory=new Date();
	log.info("startTime of SIM_Invetory "+endTimeSIM_Invetory);

	log.info("Difference in  SIM_Invetory timings "+(endTimeSIM_Invetory.getTime()-startTimeSIM_Invetory.getTime())/1000+
		 " seconds");

    }


    public void rollbackAdjustInventoryByItemLocation(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {
	log.info("Calling SIM invetory adustment in case of Unreserve");
	Date startTimeSIM_Invetory=new Date();
	log.info("startTime of SIM_Invetory "+startTimeSIM_Invetory);
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	String errorReason="";
	List<OmsCustOrdReserve> omsCustOrdReserveList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
	for(OmsCustOrdReserve custOrdItems:omsCustOrdReserveList)
	{
	    if(custOrdItems.getRmsResvLocType().equals("SU")==false&&
	       custOrdItems.getRmsResvLocType().equals("WH")==false)
	    {
		if(custOrdItems.getQty().intValue()>0)
		{
		    try
		    {
			InventoryAdjustmentService inventoryAdjustmentService=new InventoryAdjustmentService();
			InventoryAdjustmentPortType inventoryAdjustmentPortType=
						 inventoryAdjustmentService.getInventoryAdjustmentPort();

			StrAdjModVo strAdjModVo=new StrAdjModVo();
			StrAdjItmMod strAdjItemMod=new StrAdjItmMod();

			strAdjItemMod.setItemId(custOrdItems.getItem());
			strAdjItemMod.setReasonId(Integer.parseInt(session.getOmsSystemParametersFindIndValue("INV_RESV_CODE",
													      "OMS_INV_ADJ_RSN_CODE")));
			strAdjItemMod.setQuantity(custOrdItems.getQty());
			strAdjItemMod.setCaseSize(new BigDecimal(1));
			strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			strAdjModVo.setStoreId(custOrdItems.getRmsResvLoc().longValue());
			strAdjModVo.setComments("Reserved for customer order id : :"+input.getCustomerOrderNo());

			StrAdjRef strAdjRef=inventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment(strAdjModVo);
			log.info("Call to SIM invetory adustment in case of reserve success");
			//       return strAdjRef.getAdjustmentId();
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException iawsfe)
		    {
			errorReason=
	       iawsfe.getMessage()+" "+" Error Reason :"+iawsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException iswsfe)
		    {
			errorReason=
	       iswsfe.getMessage()+" "+" Error Reason :"+iswsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException vwsfe)
		    {
			errorReason=
	       vwsfe.getMessage()+" "+" Error Reason :"+vwsfe.getFaultInfo().getErrorDescription();
			log.error(" --> "+errorReason);
			throw new SOAPException(errorReason);
		    }
		    catch(Exception e)
		    {
			log.error(" --> Exception ST: "+e);
			throw new SOAPException(e.getMessage());
		    }
		}
	    }

	} //for ends

	Date endTimeSIM_Invetory=new Date();
	log.info("startTime of SIM_Invetory "+endTimeSIM_Invetory);

	log.info("Difference in  SIM_Invetory timings "+(endTimeSIM_Invetory.getTime()-startTimeSIM_Invetory.getTime())/1000+
		 " seconds");

    }


    public ProcessedObject callWebservices(BigDecimal omsCustOrdNo,ArrayList<OmsTempCoFo> temp) throws SOAPException,
												       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
												       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
												       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
												       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
												       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
												       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
												       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
												       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
												       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
												       EntityAlreadyExistsWSFaultException
    {

	OMSUtilSessionEJB session=OMSUtil.doLookup();
	log.info("omsCustOrdNo "+omsCustOrdNo+"inside callWebservices");
	Date startTimeofCallingWS=new Date();
	log.info("omsCustOrdNo "+omsCustOrdNo+"startTime of CallingWS "+startTimeofCallingWS);
	log.info("omsCustOrdNo="+omsCustOrdNo);
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	FulfillOrderService fulfillOrderService=null;
	FulfillOrderPortType fulfillOrderPortType=null;
	try
	{
	    fulfillOrderService=new FulfillOrderService();
	    fulfillOrderPortType=fulfillOrderService.getFulfillOrderPort();
	}
	catch(javax.xml.ws.WebServiceException f)
	{
	    log.info("throwing RMS_UNAVL");
	    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
	}
	ProcessedObject processedObject=new ProcessedObject();
	//  ArrayList<OmsTempCoFo> returnList= processedObject.getOmsTempCoFoList();
	ArrayList<OmsTempCoFo> returnList=new ArrayList<OmsTempCoFo>();
	log.info("processed object"+returnList.size());
	if(omsCustOrdHead.getSubCustOrderNo().equals("1"))
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo();
	}
	else
	{
	    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+omsCustOrdHead.getSubCustOrderNo();
	    if(omsCustOrdHead.getSubCustOrderNo().trim().length()<3)
	    {
		if(omsCustOrdHead.getSubCustOrderNo().trim().length()==2)
		{
		    extCustOrdNo=omsCustOrdHead.getCustOrderNo().trim()+"-0"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
		else
		{
		    extCustOrdNo=
       omsCustOrdHead.getCustOrderNo().trim()+"-00"+omsCustOrdHead.getSubCustOrderNo().trim();
		}
	    }
	}
	String partialDeliveryInd=
		   session.getOmsSystemParametersFindIndValue("OMS_PARTIAL_DLV_IND","OMS_SYSTEM_OPTION");
	if(temp.get(0).getSourceLocId().compareTo(temp.get(0).getFulfillLocId())==0)
	{
	    StoreFulfillmentOrderService storeFulfillmentOrderService=new StoreFulfillmentOrderService();
	    StoreFulfillmentOrderPortType storeFulfillmentOrderPortType=
		       storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
	    //souce loc type=null then reservation
	    log.info("omsCustOrdNo "+omsCustOrdNo+"reservation condition");
	    log.info("omsCustOrdNo"+omsCustOrdNo);
	    FulfilOrdColDesc fulfilOrdColDesc=new FulfilOrdColDesc();
	    FulfilOrdDesc fulfilOrdDesc=null;
	    fulfilOrdColDesc.setCollectionSize(1);
	    fulfilOrdDesc=new FulfilOrdDesc();
	    for(OmsTempCoFo item:temp)
	    {

		// fulfilOrdDesc.setCustomerOrderNo(String.valueOf(omsCustOrdNo));
		fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);
		fulfilOrdDesc.setComments("Application id:"+omsCustOrdHead.getApplicationId());
		fulfilOrdDesc.setFulfillOrderNo(temp.get(0).getFulfillOrderNo().toString());
		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilmentOrderNo="+fulfilOrdDesc.getFulfillOrderNo());
		if(temp.get(0).getSourceLocationType().equals("ST"))
		{
		    fulfilOrdDesc.setFulfillLocType(fulfilOrdDesc.getFulfillLocType().fromValue("S"));

		}
		else
		{
		    fulfilOrdDesc.setFulfillLocType(fulfilOrdDesc.getFulfillLocType().fromValue("V"));
		}


		fulfilOrdDesc.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());

		log.info("omsCustOrdNo "+omsCustOrdNo+"SIM-Fulfil loc id="+fulfilOrdDesc.getFulfillLocId());
		fulfilOrdDesc.setPartialDeliveryInd(fulfilOrdDesc.getPartialDeliveryInd().fromValue(partialDeliveryInd)); //check

		fulfilOrdDesc.setDeliveryType(fulfilOrdDesc.getDeliveryType().valueOf(omsCustOrdHead.getDeliveryType()));
		GregorianCalendar gregorianCalendar=new GregorianCalendar();

		DatatypeFactory datatypeFactory=null;
		try
		{
		    datatypeFactory=DatatypeFactory.newInstance();
		}
		catch(DatatypeConfigurationException e)
		{
		    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		Calendar now1=Calendar.getInstance();
		now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
		now.setMonth(now1.get(Calendar.MONTH)+1);
		if(temp.get(0).getSourceLocationType().equals("WH"))
		{
		    now.setYear(now1.get(Calendar.YEAR)+ 4);
		}
		else
		{
		    now.setYear(now1.get(Calendar.YEAR));
		}

		now.setDay(now1.get(Calendar.DAY_OF_MONTH));


		log.info("");
		fulfilOrdDesc.setConsumerDeliveryDate(now);
		log.info("omsCustOrdNo "+omsCustOrdNo+"Consumer delivery date="+
			 fulfilOrdDesc.getConsumerDeliveryDate());


		fulfilOrdDesc.setComments("Application id:"+omsCustOrdHead.getApplicationId());
		FulfilOrdDtl fulfilOrdDtl=new FulfilOrdDtl();
		fulfilOrdDtl.setItem(item.getItem());
		log.info("omsCustOrdNo "+omsCustOrdNo+"SIM:item="+item.getItem());
		log.info("=========================item.getFoConfQty()============== "+item.getFoConfQty());
		fulfilOrdDtl.setOrderQtySuom(item.getFoConfQty());
		OmsCustOrdItem omsCustOrdItem=null;
		try
		{
		    log.info("omsCustOrdNo="+omsCustOrdNo+"Item="+item.getItem()+"Line no="+item.getLineNo());
		    omsCustOrdItem=session.getOmsCustOrdItemFindByItem(omsCustOrdNo,item.getItem(),item.getLineNo());
		    log.info("setting item comments in fulfilOrdDtl "+omsCustOrdItem.getComments());
		    fulfilOrdDtl.setComments(omsCustOrdItem.getComments());
		    log.info("setted item comments in fulfilOrdDtl ");
		}
		catch(Exception e)
		{
		    log.error(" session.getOmsCustOrdItemFindByItem(omsCustOrdNo, item.getItem(),item.getLineNo()); failed");
		}

		// Added for finding sum of discounts
		if(omsCustOrdItem.getUnitRetail().intValue()!=0)
		{
		    OMSUtilCommons oMSUtilCommons=new OMSUtilCommons();
		    log.info("%%%% omsCustOrdNo : "+omsCustOrdNo);
		    log.info("%%%% item.getLineNo() : "+item.getLineNo());
		    log.info("%%%% omsCustOrdItem.getUnitRetail() : "+omsCustOrdItem.getUnitRetail());
		    BigDecimal unitRetail=
				      oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo,item.getLineNo(),omsCustOrdItem.getUnitRetail());
		    log.info("unitRetail : "+unitRetail);

		    if(unitRetail.intValue()>0)
		    {
			fulfilOrdDtl.setUnitRetail(unitRetail);
			log.info("Unit Retail--------"+fulfilOrdDtl.getUnitRetail());
			fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); //Unit retail and unit retail currency should be both populated or both null.
		    }
		}
		fulfilOrdDtl.setTransactionUom(omsCustOrdItem.getTransactionUom());
		fulfilOrdDtl.setStandardUom(omsCustOrdItem.getStandardUom());
		fulfilOrdDtl.setSubstituteInd(omsCustOrdItem.getSubstituteAllowInd());
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
	    }

	    FulfilOrdCustDesc fulfilOrdCustDesc=new FulfilOrdCustDesc();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Fetching address");
	    OmsCustOrdAddress omsCustOrdAddress=null;
	    try
	    {
		omsCustOrdAddress=session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
	    }
	    catch(Exception e)
	    {
		log.info("Address not present in the request");
	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"fetched addresss succesfully");
	    log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdHead.getCustId() "+omsCustOrdHead.getCustId());
	    if(omsCustOrdAddress!=null&&null!=omsCustOrdHead.getCustId()||omsCustOrdHead.getCustId().isEmpty()==false)
	    {
		fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
	    }
	    try
	    {
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverFirstName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverLastName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
		}
	    }
	    catch(Exception e)
	    {

	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"before add 1");
	    try
	    {
		log.info("omsCustOrdAddress.getDeliverAdd1() "+omsCustOrdAddress.getDeliverAdd1());
		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverAdd1()||
		   omsCustOrdAddress.getDeliverAdd1().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
		}

		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverAdd2()||
		   omsCustOrdAddress.getDeliverAdd2().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());

		}

		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverAdd3()||
		   omsCustOrdAddress.getDeliverAdd3().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+
			 "***Exception occurred during omsCustOrdAddress.getDeliverAdd1()***");
	    }

	    log.info(" Omscust OrdNo"+omsCustOrdNo+" Getting the delivery address");
	    try
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdAddress.getDeliverCity()"+
			 omsCustOrdAddress.getDeliverCity());
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverCity().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverState().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState());

		    if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverCountry().isEmpty()==false)
		    {
			log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdAddress.getDeliverCountry() "+
				 omsCustOrdAddress.getDeliverCountry());
			fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
			fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
		    }
		}
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverPost().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());
		}
		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverPhoneNo())
		{
		    log.info("omsCustOrdNo "+omsCustOrdNo+"Setting the delivery phone is"+
			     omsCustOrdAddress.getDeliverPhoneNo());
		    fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
		    log.info("omsCustOrdNo "+omsCustOrdNo+"Getting the delivery phone is"+
			     fulfilOrdCustDesc.getDeliverPhone());

		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"Exception while omsCustOrdAddress.getDeliverCity()***");
	    }
	    try
	    {

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillFirstName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillLastName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());
		}
	    }
	    catch(Exception e)
	    {

	    }
	    try
	    {

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillAdd1().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillAdd1(omsCustOrdAddress.getBillAdd1());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillAdd2().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillAdd2(omsCustOrdAddress.getBillAdd2());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillAdd3().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillAdd3(omsCustOrdAddress.getBillAdd3());
		}
	    }
	    catch(Exception e)
	    {

	    }
	    try
	    {
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillCity().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillState().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState());

		    if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillCountry().isEmpty()==false)
		    {
			fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());
			fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry());
		    }
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillPost().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());
		}

		fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
	    }
	    catch(Exception e)
	    {

	    }
	    fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
	    fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
	    fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
	    //  fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
	    fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
	    for(OmsTempCoFo omstempCoFo:temp)
	    {
		omstempCoFo.setProcessingApp("SIM");

	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Calling SIM");
	    //calling SIM
	    Date simdate1=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"SIM WS Start Time  "+simdate1);
	    try
	    {
		storeFulfillmentOrderPortType.createFulfillmentOrderDetail(fulfilOrdColDesc);
	    }
	    catch(javax.xml.ws.WebServiceException f)
	    {
		log.info("throwing SIM_UNAVL");
		throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
	    }
	    Date simdate2=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"SIM WS End Time "+simdate2);
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Time difference between SIM WS start and end time"+
		     (simdate2.getTime()-simdate1.getTime())+"milli seconds");
	    for(OmsTempCoFo omstempCoFo:temp)
	    {
		omstempCoFo.setProcessingApp("RMS");
		returnList.add(omstempCoFo);

	    }
	    // calling RMS
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Calling RMS for reservation");
	    Date rmsDate1=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"RMS WS Start Time  "+rmsDate1);
	    try
	    {
		fulfilOrdCfmCol=fulfillOrderPortType.createFulfilOrdColDesc(fulfilOrdColDesc);
	    }
	    catch(javax.xml.ws.WebServiceException f)
	    {
		log.info("throwing RMS_UNAVL");
		throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Call successful");
	    Date rmsdate2=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"RMS WS End Time "+simdate2);
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Time difference between RMS WS start and end time"+
		     (rmsdate2.getTime()-rmsDate1.getTime())+"milli seconds");


	    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdCfmCol before setting "+
		     fulfilOrdCfmCol.getFulfilOrdCfmDesc().size());
	    processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);
	}

	// Package call for Carrera ---- DC to DC changes
	else if(temp.get(0).getSourceLocationType().equals("WH")&&temp.get(0).getFulfillLocationType().equals("W"))
	{
	    TransferResponse response=null;
	    TransferRequest transferRequest=null;
	    String jsonReqString=null;
	    //            for (OmsTempCoFo omsTempCoFo : temp) {
	    //            OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
	    BigDecimal combId=temp.get(0).getCombinationId();
	    log.info("***For wh to wh check comb id :"+combId);

	    for(OmsTempCoFo omstempCoFo:temp)
	    {
		log.info("Processing app is RIB Package");
		log.info("--***** Item from temp CO :"+omstempCoFo.getItem());
		log.info("--***** Item Quantity :"+omstempCoFo.getOrderQty());

		log.info("--***** Item from temp "+temp.get(0).getItem());
		log.info("--***** Item Qty from temp "+temp.get(0).getOrderQty());

		//BigDecimal sourcelocId = checkphysicalwh(combId, temp.get(0).getFulfillLocId());

		List<TransferItems> transferItemsList=new ArrayList<TransferItems>();
		TransferItems transferItems=new TransferItems();
		transferItems.setItem(omstempCoFo.getItem());
		log.info("****Print item : "+transferItems.getItem());
		transferItems.setQty(omstempCoFo.getOrderQty().intValue());
		log.info("****Print Qty : "+transferItems.getQty());
		transferItemsList.add(transferItems);
		transferRequest=new TransferRequest();
		transferRequest.setSrc_id(temp.get(0).getVirtualWH().toString());
		transferRequest.setRef_no(omsCustOrdNo.toString().concat(temp.get(0).getLineNo().toString()));
		log.info("****Print ref number : "+omsCustOrdNo.toString().concat(temp.get(0).getLineNo().toString()));
		transferRequest.setDest_id(temp.get(0).getFulfillLocId().toString());
		transferRequest.setCustomerItems(transferItemsList);
		transferRequest.setSrc_loc(omstempCoFo.getSourceLocId().intValue());
		transferRequest.setFul_loc(omstempCoFo.getFulfillLocId().intValue());
		transferRequest.setFul_ord_no(omstempCoFo.getFulfillOrderNo().intValue());
		transferRequest.setCust_ord_no(extCustOrdNo);
		omstempCoFo.setProcessingApp("RIB Package");
		try
		{
		    omstempCoFo.setOrderQty(temp.get(0).getOrderQty());
		    log.info("Order Qty Temp: "+omstempCoFo.getOrderQty());
		    returnList.add(omstempCoFo);
		}
		catch(Exception e)
		{
		    log.error("error in adding to return list"+e);
		}
		log.info("Added in return list");
	    }
	    Gson gson=new Gson();
	    //convert object to JSON string
	    jsonReqString=gson.toJson(transferRequest);
	    log.info("JSON Transfer Request String in format"+jsonReqString);
	    response=invokeNewTransferCreationWebService(jsonReqString);
	    log.info("Transfer response *********"+response.getTsf_no());
	    
	    try
	    {
		if(response.getTsf_no()!=null)
		{
		    //updateTransferType(response.getTsf_no(), extCustOrdNo, omsCustOrdNo);
		    // updateTransferPubInfo(response.getTsf_no());
		    // persistOrdcust(temp, omsCustOrdNo, extCustOrdNo, response.getTsf_no());
		    //updateOmsCoFulfill(temp, response.getTsf_no());
		}
	    }
	    catch(Exception e)
	    {
		log.info("Encountered an exception : "+e);
	    }

	    processedObject.setCurrentFulFillOrderNo(temp.get(0).getFulfillOrderNo().intValue());
	    processedObject.setTsf_no(response.getTsf_no());

	}

	else
	{
	    log.info("omsCustOrdNo "+omsCustOrdNo+"***PO/Transfer");

	    log.info("omsCustOrdNo"+omsCustOrdNo);

	    log.info("omsCustOrdNo "+omsCustOrdNo+"***temp size="+temp.size());
	    FulfilOrdColDesc fulfilOrdColDesc=new FulfilOrdColDesc();
	    fulfilOrdColDesc.setCollectionSize(1);
	    FulfilOrdDesc fulfilOrdDesc=null;
	    fulfilOrdDesc=new FulfilOrdDesc();
	    for(OmsTempCoFo omsTempCoFo:temp)
	    {

		//    fulfilOrdDesc = new FulfilOrdDesc();

		fulfilOrdDesc.setCustomerOrderNo(extCustOrdNo);

		//log.info("FulfilOder no" + String.valueOf(omsCustOrdNo.longValue() + i));
		fulfilOrdDesc.setSourceLocType(fulfilOrdDesc.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));
		log.info("omsCustOrdNo "+omsCustOrdNo+"Source Loc type="+
			 fulfilOrdDesc.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));
		fulfilOrdDesc.setSourceLocId(temp.get(0).getSourceLocId().longValue());
		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdDesc.getSourceLocId()"+fulfilOrdDesc.getSourceLocId());
		if(temp.get(0).getFulfillLocationType().equals("ST")||temp.get(0).getFulfillLocationType().equals("S"))
		{
		    fulfilOrdDesc.setFulfillLocType(fulfilOrdDesc.getFulfillLocType().fromValue("S"));
		}
		else
		{
		    fulfilOrdDesc.setFulfillLocType(fulfilOrdDesc.getFulfillLocType().fromValue("V"));
		}

		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdDesc.getFulfillLocType()"+
			 fulfilOrdDesc.getFulfillLocType());
		fulfilOrdDesc.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
		log.info("omsCustOrdNo "+omsCustOrdNo+"getFulfillLocId"+fulfilOrdDesc.getFulfillLocId());

		fulfilOrdDesc.setPartialDeliveryInd(fulfilOrdDesc.getPartialDeliveryInd().fromValue(partialDeliveryInd));

		fulfilOrdDesc.setDeliveryType(fulfilOrdDesc.getDeliveryType().valueOf(omsCustOrdHead.getDeliveryType()));
		//   fulfilOrdDesc.setDeliveryType(fulfilOrdDesc.getDeliveryType().S);

		// fulfilOrdDesc.setConsumerDeliveryDate(input.getConsumerDeliveryDate());
		GregorianCalendar gregorianCalendar=new GregorianCalendar();

		DatatypeFactory datatypeFactory=null;
		try
		{
		    datatypeFactory=DatatypeFactory.newInstance();
		}
		catch(DatatypeConfigurationException e)
		{

		    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		Calendar now1=Calendar.getInstance();
		now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
		now.setMonth(now1.get(Calendar.MONTH)+1);
		if(temp.get(0).getSourceLocationType().equals("WH"))
		{
		    now.setYear(now1.get(Calendar.YEAR)+4);
		}
		else
		{
		    now.setYear(now1.get(Calendar.YEAR));
		}

		now.setDay(now1.get(Calendar.DAY_OF_MONTH));

		fulfilOrdDesc.setConsumerDeliveryDate(now);
		log.info("omsCustOrdNo "+omsCustOrdNo+"getConsumerDeliveryDate"+
			 fulfilOrdDesc.getConsumerDeliveryDate());
		// fulfilOrdDesc.setConsumerDeliveryTime(input.getConsumerDeliveryTime());
		//fulfilOrdDesc.setDeliveryCharges(input.g);
		//fulfilOrdDesc.setDeliveryChargesCurr(value);
		fulfilOrdDesc.setComments("Application id:"+omsCustOrdHead.getApplicationId());

		OmsCustOrdItem omsCustOrdItem=
				  session.getOmsCustOrdItemFindByItem(omsCustOrdNo,omsTempCoFo.getItem(),omsTempCoFo.getLineNo());
		//    for(OmsTempCoFo omsTempCoFo:temp)
		//      {

		// fulfilOrdDesc.setFulfillOrderNo(omsTempCoFo.getFulfillOrderNo().toString());
		fulfilOrdDesc.setFulfillOrderNo(temp.get(0).getFulfillOrderNo().toString());
		log.info("omsCustOrdNo "+omsCustOrdNo+"**Fulfilorderno is "+fulfilOrdDesc.getFulfillOrderNo());
		FulfilOrdDtl fulfilOrdDtl=new FulfilOrdDtl();
		fulfilOrdDtl.setItem(omsTempCoFo.getItem());
		fulfilOrdDtl.setOrderQtySuom(omsTempCoFo.getFoConfQty());
		log.info("setting item comments in fulfilOrdDtl "+omsCustOrdItem.getComments());
		fulfilOrdDtl.setComments(omsCustOrdItem.getComments());
		log.info("setted item comments in fulfilOrdDtl ");
		fulfilOrdDtl.setTransactionUom(omsCustOrdItem.getTransactionUom());
		fulfilOrdDtl.setStandardUom(omsCustOrdItem.getStandardUom());

		// Added for finding sum of discounts
		if(omsCustOrdItem.getUnitRetail().intValue()!=0)
		{
		    OMSUtilCommons oMSUtilCommons=new OMSUtilCommons();
		    log.info("%%%% omsCustOrdNo : "+omsCustOrdNo);
		    log.info("%%%% item.getLineNo() : "+omsTempCoFo.getLineNo());
		    log.info("%%%% omsCustOrdItem.getUnitRetail() : "+omsCustOrdItem.getUnitRetail());
		    BigDecimal unitRetail=
				      oMSUtilCommons.getUnitRetailBySumofUnitDiscounts(omsCustOrdNo,omsTempCoFo.getLineNo(),omsCustOrdItem.getUnitRetail());

		    log.info("unitRetail : "+unitRetail);
		    //fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());

		    if(unitRetail.intValue()>0)
		    {
			fulfilOrdDtl.setUnitRetail(unitRetail);
			log.info("Unit Retail--------"+fulfilOrdDtl.getUnitRetail());
			fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr()); //Unit retail and unit retail currency should be both populated or both null.
		    }

		    //fulfilOrdDtl.setUnitRetail(omsCustOrdItem.getUnitRetail());
		    //log.info("omsCustOrdNo "+omsCustOrdNo+"Unit Retail--------"+fulfilOrdDtl.getUnitRetail());
		    //fulfilOrdDtl.setRetailCurr(omsCustOrdItem.getRetailCurr());//Unit retail and unit retail currency should be both populated or both null.
		}
		fulfilOrdDtl.setSubstituteInd(omsCustOrdItem.getSubstituteAllowInd());

		log.info("omsCustOrdNo "+omsCustOrdNo+"Adding item detail with item-"+fulfilOrdDtl.getItem());
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
	    }

	    FulfilOrdCustDesc fulfilOrdCustDesc=new FulfilOrdCustDesc();

	    OmsCustOrdAddress omsCustOrdAddress=null;

	    try
	    {
		omsCustOrdAddress=session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("omsCustOrdNo "+omsCustOrdNo+"fetched addresss successfully");
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"Address not present in the request");
	    }


	    if(omsCustOrdAddress!=null&&null!=omsCustOrdHead.getCustId()||omsCustOrdHead.getCustId().isEmpty()==false)
	    {

		fulfilOrdCustDesc.setCustomerNo(omsCustOrdHead.getCustId());
	    }

	    // log.info("omsCustOrdAddress.getDeliverFirstName() "+omsCustOrdAddress.getDeliverFirstName());
	    if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverFirstName().isEmpty()==false)
	    {
		fulfilOrdCustDesc.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
	    }
	    try
	    {
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverLastName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
		}
	    }
	    catch(Exception e)
	    {
		log.info("********************************************************************");

	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"before add 1");
	    log.info("omsCustOrdNo "+omsCustOrdNo+"*****************************************************");
	    try
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdAddress.getDeliverAdd1() "+
			 omsCustOrdAddress.getDeliverAdd1());
		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverAdd1()||
		   omsCustOrdAddress.getDeliverAdd1().isEmpty()==false)
		{

		    fulfilOrdCustDesc.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
		}

		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverAdd2())
		{

		    fulfilOrdCustDesc.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());

		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverAdd3().isEmpty()==false)
		{

		    fulfilOrdCustDesc.setDeliverAdd3(omsCustOrdAddress.getDeliverAdd3());
		}
	    }
	    catch(Exception e)
	    {
		log.info(" Setting the address delivery address 1,2 ,3 got Exception ********");
	    }

	    log.info("************************ Setting the Delivery City , Country , Post and Phone ***********************");
	    try
	    {
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverCity().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverCity(omsCustOrdAddress.getDeliverCity());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverState().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setDeliverState(omsCustOrdAddress.getDeliverState());
		    if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverCountry().isEmpty()==false)
		    {
			fulfilOrdCustDesc.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
			fulfilOrdCustDesc.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
		    }
		}
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverPhoneNo()!=null)
		{
		    log.info("OmscustOrdNo"+omsCustOrdNo+" the Delivery Phone");
		    fulfilOrdCustDesc.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
		}
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getDeliverPost().isEmpty()==false)
		{
		    log.info("OmscustOrdNo"+omsCustOrdNo+" the Delivery Post");
		    fulfilOrdCustDesc.setDeliverPost(omsCustOrdAddress.getDeliverPost());
		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo"+omsCustOrdNo+"Exception the Delivery Country Id , Delivery phone , pos ");
	    }
	    try
	    {
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillFirstName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillFirstName(omsCustOrdAddress.getBillFirstName());
		}
		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillLastName().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillLastName(omsCustOrdAddress.getBillLastName());
		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo"+omsCustOrdNo+"Exception in  Bill First name and Bill Last Name  ");
	    }
	    try
	    {

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillAdd1().isEmpty()==false)
		{
		    log.info("******OmscustOrdNo******"+omsCustOrdNo+" Bill Addresss1"+
			     omsCustOrdAddress.getBillAdd1());
		    fulfilOrdCustDesc.setBillAdd1(omsCustOrdAddress.getBillAdd1());
		}

		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getBillAdd2())
		{
		    log.info("******OmscustOrdNo******"+omsCustOrdNo+"***************** Bill Addresss2"+
			     omsCustOrdAddress.getBillAdd2());
		    fulfilOrdCustDesc.setBillAdd2(omsCustOrdAddress.getBillAdd2());
		}

		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getBillAdd3())
		{
		    log.info("******OmscustOrdNo******"+omsCustOrdNo+"*********Bill Addresss3"+
			     omsCustOrdAddress.getBillAdd3());
		    fulfilOrdCustDesc.setBillAdd3(omsCustOrdAddress.getBillAdd3());
		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo"+omsCustOrdNo+"Exception in  Bill Address  ");
	    }

	    log.info("***************omsCustOrdNo "+omsCustOrdNo+" Bill City and Bill state information setting");
	    try
	    {

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillCity().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillCity(omsCustOrdAddress.getBillCity());
		}

		if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillState().isEmpty()==false)
		{
		    fulfilOrdCustDesc.setBillState(omsCustOrdAddress.getBillState());

		    if(omsCustOrdAddress!=null&&omsCustOrdAddress.getBillCountry().isEmpty()==false)
		    {
			fulfilOrdCustDesc.setBillCountryId(omsCustOrdAddress.getBillCountry());
			fulfilOrdCustDesc.setBillCounty(omsCustOrdAddress.getBillCountry());
		    }
		}


	    }
	    catch(Exception e)
	    {
		log.error("omsCustOrdNo "+omsCustOrdNo+"error in bill detail");
	    }

	    log.info("The Bill post is setting in the FulOrdCustDesc");

	    try
	    {
		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getBillPost())
		{
		    fulfilOrdCustDesc.setBillPost(omsCustOrdAddress.getBillPost());
		}
		log.info("***Getting Bill Post***");
		if(omsCustOrdAddress!=null&&null!=omsCustOrdAddress.getDeliverPhoneNo())
		{
		    fulfilOrdCustDesc.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
		}
		log.info("***Getting Deliver PhoneNo***");
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"error in bill post");
	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"---------------------------------------");
	    log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdNo "+omsCustOrdNo+"getBillAdd1"+
		     fulfilOrdCustDesc.getBillAdd1());
	    log.info("omsCustOrdNo "+omsCustOrdNo+"getDeliverAdd1"+fulfilOrdCustDesc.getDeliverAdd1());
	    fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
	    //   fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
	    fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
	    for(OmsTempCoFo omstempCoFo:temp)
	    {
		log.info("omsCustOrdNo "+omsCustOrdNo+"Processing app is RMS");
		log.info("omsCustOrdNo "+omsCustOrdNo+"--"+omstempCoFo.getItem());
		omstempCoFo.setProcessingApp("RMS");
		try
		{
		    returnList.add(omstempCoFo);
		}
		catch(Exception e)
		{
		    log.error("omsCustOrdNo "+omsCustOrdNo+"error in adding to return list"+e);
		}
		log.info("omsCustOrdNo "+omsCustOrdNo+"Added in return list");

	    }
	    // calling RMS

	    log.info("omsCustOrdNo "+omsCustOrdNo+"Calling RMS");
	    Date rmsstarttime=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"RMS Start Time "+rmsstarttime);
	    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdDesc.getFulfilOrdDtl()"+
		     fulfilOrdDesc.getFulfilOrdDtl().size());


	    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdColDesc"+fulfilOrdColDesc.getFulfilOrdDesc().size());
	    try
	    {
		log.info("+++++++++++++++++++++++++++calling create FulFilOrdColDesc+++++++++++++++++++++ ");

		log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfilmetn OrderNo="+fulfilOrdDesc.getFulfillOrderNo());
		log.info("omsCustOrdNo "+omsCustOrdNo+" source loc id= "+fulfilOrdDesc.getSourceLocId());
		log.info("omsCustOrdNo "+omsCustOrdNo+" Src Loc Type= "+fulfilOrdDesc.getSourceLocType());
		log.info("omsCustOrdNo "+omsCustOrdNo+" FullLocId= "+fulfilOrdDesc.getFulfillLocId());
		log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfill loc Type= "+fulfilOrdDesc.getFulfillLocType());
		log.info("omsCustOrdNo "+omsCustOrdNo+" fulfilOrdDesc.getCustomerOrderNo()"+
			 fulfilOrdDesc.getCustomerOrderNo());
		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdDesc.getFulfilOrdDtl().get(0).getItem()"+
			 fulfilOrdDesc.getFulfilOrdDtl().get(0).getItem());

		log.info("omscustOrdNo"+omsCustOrdNo+"Delivery First Name"+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverFirstName()+"Last Name"+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverLastName());
		log.info("omscustOrdNo"+omsCustOrdNo+"Bill First Name"+fulfilOrdDesc.getFulfilOrdCustDesc().getBillFirstName()+
			 "Last Name"+fulfilOrdDesc.getFulfilOrdCustDesc().getBillLastName());
		log.info("omscustOrdNo"+omsCustOrdNo+"Delivery Country Id"+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverCountryId()+"deLivery Phone no "+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverPhone());
		log.info("omscustOrdNo"+omsCustOrdNo+"Delivery City "+fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverCity()+
			 "Delivery State "+fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverState());
		log.info("omscustOrdNo"+omsCustOrdNo+"Bill Country Id"+fulfilOrdDesc.getFulfilOrdCustDesc().getBillCountryId()+
			 "Bill Phone no "+fulfilOrdDesc.getFulfilOrdCustDesc().getBillPhone());
		log.info("omscustOrdNo"+omsCustOrdNo+"Bill City "+fulfilOrdDesc.getFulfilOrdCustDesc().getBillCity()+"Bill State "+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getBillState());
		log.info("omsCustOrdNo Bill Address 1 & Delivery Adress"+omsCustOrdNo+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getBillAdd1()+"Delivery Address"+
			 fulfilOrdDesc.getFulfilOrdCustDesc().getDeliverAdd1());
		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdDesc.getFulfilOrdDtl().get(0).getOrderQty()"+
			 fulfilOrdDesc.getFulfilOrdDtl().get(0).getOrderQtySuom());

		fulfilOrdCfmCol=fulfillOrderPortType.createFulfilOrdColDesc(fulfilOrdColDesc);
		log.info("omsCustOrdNo "+omsCustOrdNo+"-----------");
		log.info("omsCustOrdNo "+omsCustOrdNo+"fulfilOrdCfmCol before setting "+
			 fulfilOrdCfmCol.getFulfilOrdCfmDesc().size());
		processedObject.setFulfilOrdCfmCol(fulfilOrdCfmCol);
		processedObject.setCurrentFulFillOrderNo(new BigDecimal(fulfilOrdDesc.getFulfillOrderNo()).intValue());
		log.info("currrentFullfillOrderno after calling createFulfilOrdColDesc "+
			 fulfilOrdDesc.getFulfillOrderNo());

	    }
	    catch(javax.xml.ws.WebServiceException f)
	    {
		log.info("throwing RMS_UNAVL");
		throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
	    }
	    catch(EntityAlreadyExistsWSFaultException e)
	    {
		throw e;
	    }
	    catch(IllegalArgumentWSFaultException e)
	    {
		throw e;
	    }
	    catch(IllegalStateWSFaultException e)
	    {
		throw e;
	    }
	    catch(ValidationWSFaultException e)
	    {
		throw e;
	    }
	    Date rmsWSEndTime=new Date();
	    log.info("omsCustOrdNo "+omsCustOrdNo+"rms WS EndTime "+rmsWSEndTime);
	    log.info("omsCustOrdNo "+omsCustOrdNo+"Difference in RMS Start time and End Time "+
		     (rmsWSEndTime.getTime()-rmsstarttime.getTime())/1000+" seconds");
	    log.info("omsCustOrdNo "+omsCustOrdNo+"call successful");

	}
	Date endTimeofCallingWS=new Date();
	log.info("omsCustOrdNo "+omsCustOrdNo+"End Time of calling webservices "+endTimeofCallingWS);


	log.info("omsCustOrdNo "+omsCustOrdNo+"Difference in calling webservices "+
		 (endTimeofCallingWS.getTime()-startTimeofCallingWS.getTime())/1000+" seconds");
	processedObject.setOmsTempCoFoList(returnList);
	return processedObject;
    }

    public long callSIMStoreInventory(String item,
				      BigDecimal nextLoc) throws com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
								 com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
								 com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
								 com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException
    {
	long SOH=0;
	try
	{

	    StoreInventoryService storeInventoryService=new StoreInventoryService();
	    StoreInventoryPortType storeInventoryPortType=storeInventoryService.getStoreInventoryPort();
	    StrInvCriVo strInvCriVo=new StrInvCriVo();
	    strInvCriVo.getItemIdCol().add(item);
	    strInvCriVo.getStoreIdCol().add(nextLoc.longValue());
	    strInvCriVo.setUomType(strInvCriVo.getUomType().fromValue("STANDARD"));
	    StrInvColDesc strInvColDesc=storeInventoryPortType.lookupInventoryInStore(strInvCriVo);
	    SOH=strInvColDesc.getStrInvDesc().get(0).getAvailableQty().longValue();
	}
	catch(IndexOutOfBoundsException in)
	{
	    SOH=0;
	}
	return SOH;
    }

    private BigDecimal checkphysicalwh(BigDecimal combid,BigDecimal fulfilid)
    {
	BigDecimal sourceLocId=null;
	BigDecimal fulfillLocIdFromMtrix=null;

	java.util.List<OmsFulfillMatrixExtDetail> listMatrix=null;
	Boolean match=false;
	try
	{
	    OMSUtilSessionEJB session=OMSUtil.doLookup();
	    //combinationID = session.getOmsFulfillMatrixExtHeadFindCombination(reqId, itemType, custCity, modeOfDelv);
	    listMatrix=session.getOmsFulfillMatrixExtDetailFindByCombId(combid);
	}
	catch(Exception exp)
	{

	}
	log.info("List Size ************"+listMatrix.size());

	for(int i=0;i<=listMatrix.size();i++)
	{
	    fulfillLocIdFromMtrix=listMatrix.get(i).getDeliveryFromLoc();
	    if(fulfilid.equals(fulfillLocIdFromMtrix))
	    {
		sourceLocId=listMatrix.get(i).getLocation();
		log.info("source loc if for WH to WH ***********"+sourceLocId);
		break;
	    }
	    else
	    {
		log.info("Location not found");
		i++;
	    }

	}
	return sourceLocId;
    } //end of method checkphysicalwh

    private void updateTransferType(String transferNo,String customerOrderNo,
				    BigDecimal omsCustOrdNo) throws SOAPException,ParseException
    {
	log.info("TRANSFER TYPE UPDATION Calling updateTransferType method");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);

	Connection connection=null;
	PreparedStatement prepStatement=null;
	String query=null;
	ResultSet rs=null;
	int candidateId=0;
	Long l=new Long(10);
	int i=l.intValue();
	java.sql.Date sqlDt=null;
	GregorianCalendar gregorianCalendar=new GregorianCalendar();
	if(omsCustOrdHead.getConsumerDlyTime()!=null)
	{
	    DatatypeFactory datatypeFactory=null;
	    try
	    {
		datatypeFactory=DatatypeFactory.newInstance();
	    }
	    catch(DatatypeConfigurationException e)
	    {

		throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
	    }
	    XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
	    Calendar now1=Calendar.getInstance();
	    now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
	    now.setMonth(now1.get(Calendar.MONTH)+1);
	    now.setYear(now1.get(Calendar.YEAR)+4);
	    now.setDay(now1.get(Calendar.DAY_OF_MONTH));
	    Date date=now.toGregorianCalendar().getTime();
	    sqlDt=new java.sql.Date(date.getTime());


	}
	try
	{
	    log.info("connecting to OMS Schema");
	    log.info("OMSConstants.DS_OMS_STRING "+OMSConstants.DS_OMS_STRING);
	    connection=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);

	    String update="update tsfhead set TSF_TYPE=? , EXT_REF_NO =?, EXP_DC_DATE  = ? where TSF_NO=?";
	    prepStatement=connection.prepareStatement(update);
	    prepStatement.setString(1,"CO");
	    prepStatement.setString(2,customerOrderNo);
	    prepStatement.setDate(3,sqlDt);
	    prepStatement.setString(4,transferNo);


	    int rowAffected=prepStatement.executeUpdate();
	    if(rowAffected==1)
	    {
		// get candidate id
		rs=prepStatement.getGeneratedKeys();
		if(rs.next())
		    candidateId=rs.getInt(1);
	    }
	    log.info("UPDATE Transfer Type"+"CO"+","+transferNo+" Is succesful");
	}
	catch(Exception e)
	{
	    log.info("Exception e "+e.getMessage());
	}
	finally
	{
	    try
	    {
		//OMSUtil.closeDBConnection(connection, prepStatement, rs);
		prepStatement.close();
		rs.close();
		connection.close();
	    }
	    catch(Exception e)
	    {
		log.error(e.getMessage());
		//throw new SOAPException(e.getMessage());
	    }
	}
    } //end of method updateTransferTyoe


    private void updateOmsCoFulfill(ArrayList<OmsTempCoFo> omsTempCoFo,String transferNo) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCoFulfillDetail omsCoFulfillDetail=null;
	omsCoFulfillDetail=new OmsCoFulfillDetail();
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
	omsCoFulfillDetail.setFulfillConfQty(omsTempCoFo.get(0).getFoConfQty());
	omsCoFulfillDetail.setFulfillStatus("C");
	omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
	try
	{
	    BigDecimal tsfNo=null;
	    log.info("Getting Transfer No for Reservation");
	    log.info("extCustOrdNo "+extCustOrdNo);
	    log.info("omsCoFulfillDetail.getFulfillOrderNo() "+omsCoFulfillDetail.getFulfillOrderNo());
	    log.info("omsCoFulfillDetail.getSourceLoc() "+omsCoFulfillDetail.getSourceLoc());
	    log.info("omsCoFulfillDetail.getFulfillLoc() "+omsCoFulfillDetail.getFulfillLoc());


	    log.info("----------------WareHouse Transfer-------------------------------");
	    omsCoFulfillDetail.setTsfNo(new BigDecimal(transferNo));
	    omsCoFulfillDetail.setTsfApprovalStatus("A");
	}
	catch(Exception e)
	{
	    log.error("---------");
	}
	Date d1=new Date();
	session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
	Date d2=new Date();
	log.info("omsCustOrdNo"+omsTempCoFo.get(0).getOmsCustOrdNo()+"after persisting into omsCoFulfillDetail"+
		 (d2.getTime()-d1.getTime())+" in milliseconds");
    }

    private TransferResponse invokeNewTransferCreationWebService(String request) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	String webserviceURL=session.getOmsWebserviceUriDetailFindByWebserviceName("CARRERA_TRANSFER_CREATION");
	//String uri = "http://192.168.41.193:7701/transferCreation/NewTransferCreation";
	TransferResponse transferResponse=null;
	log.info("Transfer creation URL is "+webserviceURL);
	HttpURLConnection conn=null;
	BufferedReader reader=null;
	OutputStream writer=null;
	
	
	URL url;
	try
	{
	    url=new URL(webserviceURL);

	    conn=(HttpURLConnection)url.openConnection();
	    conn.setDoOutput(true);
	    conn.setConnectTimeout(30000);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type","application/json");
	    conn.connect();
	    conn.disconnect();
	    conn.setReadTimeout(12000);
	    writer=conn.getOutputStream();

	    writer.write(request.getBytes());
	    writer.flush();

	    reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    transferResponse=getJsonTransferResponse(reader);
	    log.info("transferResponse Code : "+transferResponse.getCode());
	    log.info("transferResponse Success : "+transferResponse.getSuccess());
	    log.info("transferResponse Transfer No : "+transferResponse.getTsf_no());
	    log.info("transferResponse message : "+transferResponse.getMessage());
//	    TransferNumbers transferNumbers = new TransferNumbers();
//	    transferNumbers.setTsfNo(transferResponse.getTsf_no().toString());
//	    transferSet.add(transferResponse.getTsf_no().toString());
//	    log.info("Added the transfer no to set");
//	    transferNumbers.setTransferSet(transferSet);
	}
	catch(IOException io)
	{
	    log.info("IOException in  invokeNewTransferCreationWebService "+io.getMessage());

	}
	catch(Exception ioe)
	{
	    log.info("Exception in invokeNewTransferCreationWebService "+ioe.getMessage());

	}
	finally
	{
	    try
	    {
		if(writer!=null)
		{
		    writer.close();
		}
		if(reader!=null)
		{
		    reader.close();
		}
	    }
	    catch(IOException e)
	    {
		System.out.println("IOException in  invokeNewTransferCreationWebService while calling the finally block "+
				   e.getMessage());

	    }
	}
	return transferResponse;
    } //End of method invokeNewTransferCreationWebService

    private TransferResponse getJsonTransferResponse(BufferedReader br)
    {
	TransferResponse transferResponse=null;
	try
	{
	    String output;
	    String jsonResponseString="";
	    transferResponse=new TransferResponse();
	    while((output=br.readLine())!=null)
	    {
		jsonResponseString=jsonResponseString+output;
	    }

	    JsonObject json=new JsonParser().parse(jsonResponseString).getAsJsonObject();
	    transferResponse.setCode(json.get("code").getAsString());
	    transferResponse.setSuccess(json.get("success").getAsString());
	    transferResponse.setTsf_no(json.get("tsf_No").getAsString());
	    transferResponse.setMessage(json.get("message").getAsString());

	}
	catch(Exception e)
	{

	    log.info("Exception in getJsonCustomeResponse "+e.getMessage());
	}

	return transferResponse;
    } // End of method getJsonTransferResponse

    private void updateTransferPubInfo(String tsfNo)
    {
	log.info("TRANSFER TYPE UPDATION Calling updateTransferPubInfo method");
	Connection connection=null;
	PreparedStatement prepStatement=null;
	String query=null;
	ResultSet rs=null;
	int candidateId=0;
	Long l=new Long(10);
	int i=l.intValue();
	try
	{
	    log.info("connecting to OMS Schema");
	    log.info("OMSConstants.DS_OMS_STRING "+OMSConstants.DS_OMS_STRING);
	    connection=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);

	    String update="update transfers_pub_info set TSF_TYPE=? where TSF_NO=?";
	    prepStatement=connection.prepareStatement(update);
	    prepStatement.setString(1,"CO");
	    prepStatement.setString(2,tsfNo);


	    int rowAffected=prepStatement.executeUpdate();
	    if(rowAffected==1)
	    {
		// get candidate id
		rs=prepStatement.getGeneratedKeys();
		if(rs.next())
		    candidateId=rs.getInt(1);
	    }
	    log.info("UPDATE Transfer Type"+"CO"+","+tsfNo+" Is succesful");
	}
	catch(Exception e)
	{
	    log.info("Exception e "+e.getMessage());
	}
	finally
	{
	    try
	    {
		//OMSUtil.closeDBConnection(connection, prepStatement, rs);
		prepStatement.close();
		rs.close();
		connection.close();
	    }
	    catch(Exception e)
	    {
		log.error(e.getMessage());
		//throw new SOAPException(e.getMessage());
	    }
	}
    } //End of updateTransferPubInfo


    private void persistOrdcust(ArrayList<OmsTempCoFo> temp,BigDecimal omsCustOrdNo,String customerOrderNo,
				String transferNo) throws SOAPException
    {
	log.info("Persisting into ORDCUST table");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);

	OmsCustOrdItem omsCustOrdItem=null;
	OmsCustOrdAddress omsCustOrdAddress=null;
	Ordcust ordcust=new Ordcust();
	Boolean flag=false;
	BigDecimal ordcustNo=null;
	log.info("ORDCUST NO : "+ordcustNo);
	GregorianCalendar gregorianCalendar=new GregorianCalendar();
	if(omsCustOrdHead.getConsumerDlyTime()!=null)
	{
	    DatatypeFactory datatypeFactory=null;
	    try
	    {
		datatypeFactory=DatatypeFactory.newInstance();
	    }
	    catch(DatatypeConfigurationException e)
	    {

		throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
	    }
	    XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

	    Calendar now1=Calendar.getInstance();
	    now1.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
	    now.setMonth(now1.get(Calendar.MONTH)+1);
	    if(temp.get(0).getSourceLocationType().equals("WH"))
	    {
		now.setYear(now1.get(Calendar.YEAR)+4);
		//In case of WH the comsumer delivery should be 5 years ahead to avoid picking of this customer order from WMS untill and unless this is being updated from Appointment Booking from RMS.
	    }
	    else
	    {
		now.setYear(now1.get(Calendar.YEAR));
	    }
	    now.setDay(now1.get(Calendar.DAY_OF_MONTH));
	    Date date=now.toGregorianCalendar().getTime();
	    ordcust.setConsumerDeliveryDate(date);
	}
	try
	{

	    omsCustOrdItem=
			   session.getOmsCustOrdItemFindByItem(omsCustOrdNo,temp.get(0).getItem(),temp.get(0).getLineNo());
	}
	catch(Exception e)
	{
	    log.info("Item not present in the request");
	}
	try
	{
	    omsCustOrdAddress=session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
	}
	catch(Exception e)
	{
	    log.info("Address not present in the request");
	}
	ordcustNo=new BigDecimal(omsCustOrdNo.toString().concat(temp.get(0).getLineNo().toString()));
	ordcust.setOrdcustNo(ordcustNo);
	ordcust.setStatus("C");
	ordcust.setTsfNo(new BigDecimal(transferNo));
	ordcust.setSourceLocType(temp.get(0).getSourceLocationType());
	ordcust.setSourceLocId(temp.get(0).getSourceLocId());
	ordcust.setFulfillLocType(temp.get(0).getFulfillLocationType());
	ordcust.setFulfillLocId(temp.get(0).getFulfillLocId());
	if(omsCustOrdAddress.getCustId()!=null)
	{
	    ordcust.setCustomerNo(omsCustOrdAddress.getCustId());
	}
	ordcust.setCustomerOrderNo(customerOrderNo);
	ordcust.setFulfillOrderNo(temp.get(0).getFulfillOrderNo().toString());
	ordcust.setPartialDeliveryInd("Y");
	ordcust.setDeliveryType("S");
	if(omsCustOrdAddress.getBillFirstName()!=null)
	{
	    ordcust.setBillFirstName(omsCustOrdAddress.getBillFirstName());
	}
	if(omsCustOrdAddress.getBillLastName()!=null)
	{
	    ordcust.setBillLastName(omsCustOrdAddress.getBillLastName());
	}
	if(omsCustOrdAddress.getBillAdd1()!=null)
	{
	    ordcust.setBillAdd1(omsCustOrdAddress.getBillAdd1());
	}
	if(omsCustOrdAddress.getBillAdd2()!=null)
	{
	    ordcust.setBillAdd2(omsCustOrdAddress.getBillAdd2());
	}
	if(omsCustOrdAddress.getBillCountry()!=null)
	{
	    ordcust.setBillCounty(omsCustOrdAddress.getBillCountry());
	}
	if(omsCustOrdAddress.getBillCity()!=null)
	{
	    ordcust.setBillCity(omsCustOrdAddress.getBillCity());
	}
	if(omsCustOrdAddress.getBillState()!=null)
	{
	    ordcust.setBillState(omsCustOrdAddress.getBillState());
	}
	if(omsCustOrdAddress.getBillCountry()!=null)
	{
	    ordcust.setBillCountryId(omsCustOrdAddress.getBillCountry());
	}
	if(omsCustOrdAddress.getBillPost()!=null)
	{
	    ordcust.setBillPost(omsCustOrdAddress.getBillPost());
	}
	if(omsCustOrdAddress.getDeliverPhoneNo()!=null)
	{
	    ordcust.setBillPhone(omsCustOrdAddress.getDeliverPhoneNo());
	}
	if(omsCustOrdAddress.getDeliverFirstName()!=null)
	{
	    ordcust.setDeliverFirstName(omsCustOrdAddress.getDeliverFirstName());
	}
	if(omsCustOrdAddress.getDeliverLastName()!=null)
	{
	    ordcust.setDeliverLastName(omsCustOrdAddress.getDeliverLastName());
	}
	if(omsCustOrdAddress.getDeliverAdd1()!=null)
	{
	    ordcust.setDeliverAdd1(omsCustOrdAddress.getDeliverAdd1());
	}
	if(omsCustOrdAddress.getDeliverAdd2()!=null)
	{
	    ordcust.setDeliverAdd2(omsCustOrdAddress.getDeliverAdd2());
	}
	if(omsCustOrdAddress.getDeliverCountry()!=null)
	{
	    ordcust.setDeliverCounty(omsCustOrdAddress.getDeliverCountry());
	}
	if(omsCustOrdAddress.getDeliverCity()!=null)
	{
	    ordcust.setDeliverCity(omsCustOrdAddress.getDeliverCity());
	}
	if(omsCustOrdAddress.getDeliverState()!=null)
	{
	    ordcust.setDeliverState(omsCustOrdAddress.getDeliverState());
	}
	if(omsCustOrdAddress.getDeliverCountry()!=null)
	{
	    ordcust.setDeliverCountryId(omsCustOrdAddress.getDeliverCountry());
	}
	if(omsCustOrdAddress.getDeliverPost()!=null)
	{
	    ordcust.setDeliverPost(omsCustOrdAddress.getDeliverPost());
	}
	if(omsCustOrdAddress.getDeliverPhoneNo()!=null)
	{
	    ordcust.setDeliverPhone(omsCustOrdAddress.getDeliverPhoneNo());
	}
	ordcust.setComments("Application id:E-COMMERCE");
	ordcust.setCreateDatetime(new Timestamp(new Date().getTime()));
	ordcust.setCreateId("RIBWS");
	ordcust.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
	ordcust.setLastUpdateId("RIBWS");
	try
	{
	    session.persistOrdcust(ordcust);
	    flag=true;
	}
	catch(Exception e)
	{
	    log.info("Failed in Persiting in Ordcust Table");
	}
	if(flag==true)
	{
	    persistOrdcustDetail(temp,ordcustNo,omsCustOrdItem);
	}
    } // End of persistOrdcust

    private void persistOrdcustDetail(ArrayList<OmsTempCoFo> temp,BigDecimal ordcustNo,
				      OmsCustOrdItem omsCustOrdItem) throws SOAPException
    {
	log.info("Persisting Ordcust Detail");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OrdcustDetail ordDetail=new OrdcustDetail();

	ordDetail.setOrdcustNo(ordcustNo);
	ordDetail.setItem(temp.get(0).getItem());
	ordDetail.setQtyOrderedSuom(temp.get(0).getOrderQty());
	ordDetail.setQtyCancelledSuom(BigDecimal.ZERO);
	ordDetail.setUnitRetail(omsCustOrdItem.getUnitRetail());
	ordDetail.setRetailCurrencyCode(omsCustOrdItem.getRetailCurr());
	ordDetail.setStandardUom("EA");
	ordDetail.setTransactionUom("EA");
	ordDetail.setSubstituteAllowedInd("N");
	ordDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
	ordDetail.setCreateId("RIBWS");
	ordDetail.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
	ordDetail.setLastUpdateId("RIBWS");
	try
	{
	    session.persistOrdcustDetail(ordDetail);
	}
	catch(Exception e)
	{
	    log.info("Failed in Persiting in Ordcust Detail Table");
	}

    } //End of persistOrdcustDetail

}
