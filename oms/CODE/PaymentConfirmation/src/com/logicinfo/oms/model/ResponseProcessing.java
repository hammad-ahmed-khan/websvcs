package com.logicinfo.oms.model;


import com.logicinfo.oms.ejb.CfsSmsEmailStatusInfo;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsPaymentSync;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;

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
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class ResponseProcessing
{
    public ResponseProcessing()
    {
	super();
    }
    public final static Logger log=Logger.getLogger(com.logicinfo.oms.model.ResponseProcessing.class.getName());
    boolean inventoryFlag=false;

    public CoPaymentConfResponse createResponse(CoPaymentConf input,BigDecimal custOrdHeadSeqNo,String errMessage,
						ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> completeMap) throws SOAPException,
															 EntityAlreadyExistsWSFaultException,IllegalStateWSFaultException,IllegalStateWSFaultException,
															 IllegalStateWSFaultException,IllegalArgumentWSFaultException,IllegalArgumentWSFaultException,
															 ValidationWSFaultException,ValidationWSFaultException,ValidationWSFaultException,
															 ValidationWSFaultException,
															 com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
															 com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
															 com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
															 com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException, BusinessException
    {
	log.info("omscustordNo "+custOrdHeadSeqNo+" inside createResponse");
	log.info("omscustordNo "+custOrdHeadSeqNo+" errMessage"+errMessage);

	OMSUtilSessionEJB session=OMSUtil.doLookup();
	CoPaymentConfResponse response=new CoPaymentConfResponse();
	response.setApplicationId(input.getApplicationId());
	response.setCustomerOrderNo(input.getCustomerOrderNo());
	response.setEntityId(input.getEntityId());
	response.setComments(input.getComments());
	response.setRequestDatetimestamp(input.getRequestDatetimestamp());
	GregorianCalendar gregorianCalendar=new GregorianCalendar();
	Map<BigDecimal,ArrayList<OmsCoFulfillDetail>> responseMap=null;
	ArrayList<SfsConfirmationPojo> arrayList=null;
	DatatypeFactory datatypeFactory=null;
	//madhu added
	Map<BigDecimal,ArrayList<SfsConfirmationPojo>> sfsConfirmationPojomap=null;
	ArrayList<SfsConfirmationPojo> SfsConfirmationPojoList=null;
	BigDecimal testomscustordernumber=BigDecimal.ZERO;
	try
	{
	    datatypeFactory=DatatypeFactory.newInstance();
	}
	catch(DatatypeConfigurationException e)
	{
	    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
	}
	XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
	response.setResponseDatetimestamp(now);

	OmsCustOrdHead omsCustOrdHead=null;
	try
	{
	    omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(custOrdHeadSeqNo);
	}
	catch(Exception e)
	{
	    OmsErrorCodes theErrorObj=OMSUtil.parseErrorString(errMessage);
	    log.info("omscustordNo "+custOrdHeadSeqNo+" theErrorObj.getOmsErrLangDesc()="+theErrorObj.getOmsErrLangDesc());
	    response.setMessageDesc(theErrorObj.getOmsErrLangDesc());
	    response.setMessageCode(theErrorObj.getOmsErrorCode());
	    response.setMessageStatus("F");
	    return response;
	}
	log.info("omscustordNo "+custOrdHeadSeqNo+" After return");
	response.setOmsCustomerOrdNo(custOrdHeadSeqNo.longValue());
	if(errMessage.equals("S")==false)
	{
	    log.info("omscustordNo "+custOrdHeadSeqNo+"failed");
	    OmsErrorCodes theErrorObj=OMSUtil.parseErrorString(errMessage);
	    log.info("omscustordNo "+custOrdHeadSeqNo+" theErrorObj.getOmsErrLangDesc()"+theErrorObj.getOmsErrLangDesc()+"theErrorObj.getOmsErrorCode()"+theErrorObj.getOmsErrorCode());
	    response.setMessageDesc(theErrorObj.getOmsErrLangDesc());
	    response.setMessageCode(theErrorObj.getOmsErrorCode());
	    response.setMessageStatus("F");
	}
	else
	{

	    log.info("omscustordNo "+custOrdHeadSeqNo+" Status=S");
	    log.info("omscustordNo "+custOrdHeadSeqNo+" before resvlist");
	    List<OmsCustOrdReserve> resvList=null;
	    try
	    {
		log.info("omscustordNo "+custOrdHeadSeqNo+" inside first try");
		resvList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
		log.info("omscustordNo "+custOrdHeadSeqNo+" after resvlist");
	    }
	    catch(Exception e)
	    {

		resvList=null;
		log.info("omsCustOrdNo "+omsCustOrdHead.getOmsCustOrdNo()+"inside resvList exception "+e.getMessage());

	    }
	    try
	    {
		log.info("omscustordNo "+custOrdHeadSeqNo+"inside 2nd try");
		List<CustomerOrderResponseItems> responseItemList=responseItemList=response.getCustomerOrderResponseItems();
		log.info("omscustordNo "+custOrdHeadSeqNo+"after responseItemList");
		boolean resvFlag=false;
		log.info("omscustordNo "+custOrdHeadSeqNo+"after resvFlag and reserveflag value"+resvFlag);
		if(null!=resvList&&resvList.size()>0)
		{
		    log.info("omscustordNo "+custOrdHeadSeqNo+"inside not null if");
		    for(OmsCustOrdReserve resv:resvList)
		    {
			if(resv.getResvStatus().equals("CNF")&&resv.getQty().intValue()>0)
			{
			    resvFlag=true;
			}
		    }
		}


		log.info("omscustordNo "+custOrdHeadSeqNo+" after 2nd try"+resvFlag);

		if(resvFlag==true)
		{
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+" calling updateOmsCustOrdHeadforOrdPaymentStatus");
		    int rowUpdated=session.updateOmsCustOrdHeadforOrdPaymentStatus(omsCustOrdHead.getOmsCustOrdNo());
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+" No of rows Updated "+rowUpdated);
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"updated PaymentStatus to S for customer order "+omsCustOrdHead.getCustOrderNo());
		    List<OmsCustOrdTender> omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(custOrdHeadSeqNo);
		    for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
		    {
			omsCustOrdTender.setPaymentStatusInd("S");
			log.info("omscustordNo "+custOrdHeadSeqNo+"Setting last update date time");
			omsCustOrdTender.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			session.mergeOmsCustOrdTender(omsCustOrdTender);
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+"updated Tender to S for omscustordNo "+omsCustOrdHead.getOmsCustOrdNo());

		    }
             
		    response.setMessageDesc("SUCCESS");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"setted MessageDesc to SUCCESS for first time ");
		    response.setMessageCode("S");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"setted MessageCode to S for first time ");
		    response.setMessageStatus("S");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"setted MessageStatus to S for first time ");
		    List<OmsCoFulfillDetail> omsCoFulfillDetailList=session.getOmsCoFulfillDetailFindByOmsCustOrdNo(custOrdHeadSeqNo);
		    responseMap=new HashMap<BigDecimal,ArrayList<OmsCoFulfillDetail>>();

		    //madhu	added																			
		    sfsConfirmationPojomap=new HashMap<BigDecimal,ArrayList<SfsConfirmationPojo>>();
		    SfsConfirmationPojoList=new ArrayList<SfsConfirmationPojo>();

		    for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList)
		    {
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+"inside for loop of omsCoFulfillDetail");
			if(responseMap.get(omsCoFulfillDetail.getLineNo())==null||
			   responseMap.get(omsCoFulfillDetail.getLineNo()).size()==0)
			{
			    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"inside if block ");
			    ArrayList<OmsCoFulfillDetail> tempList=new ArrayList<OmsCoFulfillDetail>();
			    tempList.add(omsCoFulfillDetail);
			    responseMap.put(omsCoFulfillDetail.getLineNo(),tempList);
			    //madhu																																																				
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"checking TSF number("+omsCoFulfillDetail.getTsfNo()+") is null and sourcelocatiotype is ST for the first iteration of omsCoFulfillDetail ");
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"checking source location type "+omsCoFulfillDetail.getSourceLocType());
			    if(omsCoFulfillDetail.getTsfNo()==null&&omsCoFulfillDetail.getSourceLocType().equals("ST"))
			    {
				log.info("omsCustOrdNo "+custOrdHeadSeqNo+"condition is true and source location is "+omsCoFulfillDetail.getSourceLocType());
				SfsConfirmationPojo obj=new SfsConfirmationPojo();
				obj.setOmscustOrderNo(omsCoFulfillDetail.getOmsCustOrdNo().toString());
				obj.setStoreNo(omsCoFulfillDetail.getSourceLoc());
				testomscustordernumber=omsCoFulfillDetail.getOmsCustOrdNo();
				SfsConfirmationPojoList.add(obj);
			    }

			}
			else
			{
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"inside else block ");
			    ArrayList<OmsCoFulfillDetail> existingList=responseMap.get(omsCoFulfillDetail.getLineNo());
			    existingList.add(omsCoFulfillDetail);
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Creating map for response"+omsCoFulfillDetail.getLineNo()+omsCoFulfillDetail.getItem());
			    responseMap.put(omsCoFulfillDetail.getLineNo(),existingList);

			    //madhu added
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"checking TSF number("+omsCoFulfillDetail.getTsfNo()+") is null and sourcelocatiotype is ST for the first iteration of omsCoFulfillDetail ");
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"checking source location type "+omsCoFulfillDetail.getSourceLocType());
			    if(omsCoFulfillDetail.getTsfNo()==null&&omsCoFulfillDetail.getSourceLocType().equals("ST"))
			    {
				log.info("omsCustOrdNo "+custOrdHeadSeqNo+"condition is true and source location is "+omsCoFulfillDetail.getSourceLocType());
				SfsConfirmationPojo obj=new SfsConfirmationPojo();
				obj.setOmscustOrderNo(omsCoFulfillDetail.getOmsCustOrdNo().toString());
				obj.setStoreNo(omsCoFulfillDetail.getSourceLoc());
				testomscustordernumber=omsCoFulfillDetail.getOmsCustOrdNo();
				SfsConfirmationPojoList.add(obj);
			    }
			}
		    }

		    for(BigDecimal key:responseMap.keySet())
		    {

			log.info("omsCustOrdNo "+custOrdHeadSeqNo+"inside responseMap.keySet() loop ");
			ArrayList<OmsCoFulfillDetail> list=responseMap.get(key);

			CustomerOrderResponseItems customerOrderResponseItems=new CustomerOrderResponseItems();
			List<CustomerOrderResponseItemFulfillment> fulfilList=customerOrderResponseItems.getCustomerOrderResponseItemFulfillment();
			customerOrderResponseItems.setLineNo(key.longValue());
			customerOrderResponseItems.setStatus("AVAILABLE");
			customerOrderResponseItems.setStatusMessage("SUCCESS");

			BigDecimal orderQty=BigDecimal.ZERO;
			BigDecimal fulfilQty=BigDecimal.ZERO;
			for(OmsCoFulfillDetail omsCoFulfillDetail:list)
			{
			    customerOrderResponseItems.setItem(omsCoFulfillDetail.getItem());
			    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"omsCoFulfillDetail.getItem() "+omsCoFulfillDetail.getItem());
			    orderQty=omsCoFulfillDetail.getFulfillReqQty().add(orderQty);
			    fulfilQty=omsCoFulfillDetail.getFulfillConfQty().add(fulfilQty);
			    customerOrderResponseItems.setOrderQtySuom(orderQty);
			    customerOrderResponseItems.setFulfillQtySuom(fulfilQty);
			    CustomerOrderResponseItemFulfillment item=new CustomerOrderResponseItemFulfillment();
			    item.setItem(omsCoFulfillDetail.getItem());
			    item.setFulfillOrderNo(omsCoFulfillDetail.getFulfillOrderNo().longValue());
			    item.setOrderQtySuom(omsCoFulfillDetail.getFulfillReqQty());
			    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"omsCoFulfillDetail.getFulfillReqQty() "+omsCoFulfillDetail.getFulfillReqQty());
			    item.setFulfillLoc(omsCoFulfillDetail.getFulfillLoc().longValue());
			    item.setFulfillLocType(omsCoFulfillDetail.getFulfillLocType());
			    item.setFulfillQtySuom(omsCoFulfillDetail.getFulfillConfQty());
			    item.setSourceLoc(omsCoFulfillDetail.getSourceLoc().longValue());
			    item.setSourceLocType(omsCoFulfillDetail.getSourceLocType());

			    try
			    {
				BigDecimal poNo=session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(),omsCoFulfillDetail.getFulfillOrderNo().toString(),
						  omsCoFulfillDetail.getSourceLoc(),
						  omsCoFulfillDetail.getFulfillLoc()).get(0).getOrderNo();
				if(poNo!=null)
				{
				    item.setPoNo(poNo.longValue());

				}
				BigDecimal tsfN0=session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(),omsCoFulfillDetail.getFulfillOrderNo().toString(),
						 omsCoFulfillDetail.getSourceLoc(),
						 omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo();
				if(tsfN0!=null)
				{
				    item.setTsfNo(session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead.getCustOrderNo(),
										      omsCoFulfillDetail.getFulfillOrderNo().toString(),omsCoFulfillDetail.getSourceLoc(),
										      omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo().longValue());
				}
			    }
			    catch(Exception e)
			    {
				log.info("omsCustOrdNo"+custOrdHeadSeqNo+" No po found "+e.getMessage());
			    }
			    fulfilList.add(item);
			}
			responseItemList.add(customerOrderResponseItems);
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" Added to response items ");

		    }
		    
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"Returning "+response.getMessageStatus()+" response");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"Returning "+response.getMessageDesc()+ " response");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"Returning "+response.getMessageCode()+ " response");
		} //end of if


		else
		{
		    //BO

		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+" resvFlag "+resvFlag);
		    int rowUpdated=session.updateOmsCustOrdHeadforOrdPaymentStatus(omsCustOrdHead.getOmsCustOrdNo());
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+" No of rows Updated "+rowUpdated);

		    log.info("omsCustOrdNo"+omsCustOrdHead.getOmsCustOrdNo()+"updated PaymentStatus to S for customer order "+omsCustOrdHead.getCustOrderNo());
		    List<OmsCustOrdTender> omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(custOrdHeadSeqNo);
		    for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
		    {
			omsCustOrdTender.setPaymentStatusInd("S");
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" Setting last update time in oms cust Ord Tender");
			omsCustOrdTender.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			session.mergeOmsCustOrdTender(omsCustOrdTender);
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+"updated Tender to S for omscustordNo "+omsCustOrdHead.getOmsCustOrdNo());

		    }
		    List<OmsBackOrderDtl> omsBackOrderDtlList=session.getOmsBackOrderDtlFindByOmsCustOrdNo(custOrdHeadSeqNo);
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after omsBackOrderDtlList");
		    for(OmsBackOrderDtl coItem:omsBackOrderDtlList)

		    {
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after for loop");
			CustomerOrderResponseItems item=new CustomerOrderResponseItems();
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after CustomerOrderResponseItems");
			item.setItem(coItem.getItem());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after set item"+coItem.getItem());
			item.setLineNo(coItem.getLineNo().longValue());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after set line no"+coItem.getLineNo().longValue());
			item.setOrderQtySuom(coItem.getSourceQty());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after setOrderQtySuom"+coItem.getSourceQty());
			List<CustomerOrderResponseItemFulfillment> customerOrderResponseItemFulfillmentList=item.getCustomerOrderResponseItemFulfillment();
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after list customerOrderResponseItemFulfillmentList");
			CustomerOrderResponseItemFulfillment customerOrderResponseItemFulfillment=new CustomerOrderResponseItemFulfillment();
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after list customerOrderResponseItemFulfillment");
			customerOrderResponseItemFulfillment.setBackorderQty(coItem.getSourceQty().longValue());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after setBackorderQty"+coItem.getSourceQty().longValue());
			customerOrderResponseItemFulfillment.setSourceLoc(coItem.getSourceLoc().longValue());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after setSourceLoc"+coItem.getSourceLoc().longValue());
			customerOrderResponseItemFulfillment.setSourceLocType(coItem.getSourceLocType());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after setSourceLocType"+coItem.getSourceLocType());
			customerOrderResponseItemFulfillment.setFulfillLoc(coItem.getFulfillLoc().longValue());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after getFulfillLoc"+coItem.getFulfillLoc().longValue());

			customerOrderResponseItemFulfillment.setFulfillLocType(coItem.getFulfillLocType());
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after setFulfillLocType"+coItem.getFulfillLocType());

			customerOrderResponseItemFulfillmentList.add(customerOrderResponseItemFulfillment);
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after adding customerOrderResponseItemFulfillment to customerOrderResponseItemFulfillmentList");

			responseItemList.add(item);
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after adding item tp responseItemList");
			response.setMessageDesc("SUCCESS");
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" omsCustOrdNo"+omsCustOrdHead.getOmsCustOrdNo()+"setted MessageDesc to SUCCESS ");
			response.setMessageCode("S");
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" setted MessageCode to S ");
			response.setMessageStatus("S");
			log.info("omsCustOrdNo"+custOrdHeadSeqNo+" setted to MessageDesc SUCCESSFUL");
			//madhu											
			if(coItem.getSourceLocType().equals("ST"))
			{
			    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" condition is true and source location is "+coItem.getSourceLocType());
			    SfsConfirmationPojo obj=new SfsConfirmationPojo();
			    obj.setOmscustOrderNo(coItem.getOmsCustOrdNo().toString());
			    obj.setStoreNo(coItem.getSourceLoc());
			    testomscustordernumber=coItem.getOmsCustOrdNo();
			    SfsConfirmationPojoList.add(obj);
			}
		    }
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after method");
		    
		    response.setMessageDesc("SUCCESS");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" omsCustOrdNo"+omsCustOrdHead.getOmsCustOrdNo()+"setted MessageDesc to SUCCESS ");
		    response.setMessageCode("S");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" setted MessageCode to S ");
		    response.setMessageStatus("S");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" setted to MessageDesc SUCCESSFUL");
		}
		log.info("omsCustOrdNo"+custOrdHeadSeqNo+" before checking resp msg code S ");
		log.info("omsCustOrdNo"+custOrdHeadSeqNo+" response.getMessageCode() "+response.getMessageCode());
	        log.info("omsCustOrdNo"+custOrdHeadSeqNo+" response.getMessageDesc() "+response.getMessageDesc());
	        log.info("omsCustOrdNo"+custOrdHeadSeqNo+" response.getMessageStatus() "+response.getMessageStatus());
		
		if(response.getMessageCode().equals("S"))
		{
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after checking resp msg code S  ");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" persiting into RTLog for Backorder payment confirmation");
		    ExtSystemUpdate extSystemUpdate=new ExtSystemUpdate();
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after extSystemUpdate");
		    extSystemUpdate.persistRTLog(custOrdHeadSeqNo);
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+" after persistRTLog");
		}
		//added madhu
		 
		 if(sfsConfirmationPojomap!=null)
	         {
		 sfsConfirmationPojomap.put(custOrdHeadSeqNo,SfsConfirmationPojoList);
		 }
		
		log.info("omsCustOrdNo"+custOrdHeadSeqNo+" end if and before excp");
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo"+custOrdHeadSeqNo+" inside Parent class Exception "+e.getMessage());
		//calling rollback for timeout exception
		OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
		log.info("omsCustOrdNo"+custOrdHeadSeqNo+"calling rollback for : "+input.getCustomerOrderNo());
		log.info("For omsCustOrdNo :"+custOrdHeadSeqNo+" the keyset contains : "+completeMap.keySet()+"----------------->");
		omsCustomerOrderBean.rollbackForTimeout(custOrdHeadSeqNo,input.getCustomerOrderNo());
		log.info("For omsCustOrdNo :"+custOrdHeadSeqNo+" After calling rollback for Timeout Exception");
		response.setMessageDesc("SYSTEM ERROR");
		response.setMessageCode("FAILED");
		response.setMessageStatus("F");
	    }
	    try
	    {
		List<OmsCustOrdReserve> omsCustOrdReserveList1=session.getOmsCustOrdReserveFindByOmsCustOrdNo(custOrdHeadSeqNo);
		boolean flag=false;
		if(omsCustOrdReserveList1.size()>0)
		{
		    for(OmsCustOrdReserve omsCustOrdReserve:omsCustOrdReserveList1)
		    {
			if(omsCustOrdReserve.getResvStatus().equals("RES"))
			{
			    flag=true;
			    break;
			}
		    }
		    if(flag)
		    {
			RMSPackage rMSPackage=new RMSPackage();
			log.info("For omsCustOrdNo :"+custOrdHeadSeqNo+" Calling rollbackRMSPackageCall : "+input.getCustomerOrderNo());
			rMSPackage.rollbackRmsPackageCall(input,custOrdHeadSeqNo);
			log.info("For omsCustOrdNo :"+custOrdHeadSeqNo+" End of Calling rollbackRMSPackageCall : "+input.getCustomerOrderNo());
			InterfacePersistence interfacePersistence=new InterfacePersistence();
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+" calling rollbackAdjustInventoryByItemLocation");
			interfacePersistence.rollbackAdjustInventoryByItemLocation(input,custOrdHeadSeqNo);
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+" after calling rollbackAdjustInventoryByItemLocation");
			inventoryFlag=true;
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+"inventoryFlag "+inventoryFlag);
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+" inside item_status false condition");
			response.setMessageDesc("SYSTEM ERROR");
			response.setMessageCode("FAILED");
			response.setMessageStatus("F");
			//Adding for Payment conf rollback
			OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+" calling rollback for RMS and SIM ");
			omsCustomerOrderBean.rollbackForTimeout(custOrdHeadSeqNo,input.getCustomerOrderNo());
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+"After calling rollback for RMS and SIM ");
			OmsCustOrdHead omsCustOrd=session.getOmsCustOrdHeadFindByOmsCustOrdNo(custOrdHeadSeqNo);
			omsCustOrd.setOrdPaymentStatus("P");
			session.mergeOmsCustOrdHead(omsCustOrd);
		        log.info("omsCustOrdNo "+custOrdHeadSeqNo+" upadted the head table payment status to P");
			List<OmsCustOrdTender> omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(custOrdHeadSeqNo);
			for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
			{
			    omsCustOrdTender.setPaymentStatusInd("P");
			    omsCustOrdTender.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
			    session.mergeOmsCustOrdTender(omsCustOrdTender);
			    log.info("For omsCustOrdNo :"+custOrdHeadSeqNo+"updated Tender to P and lastdatetime for tender ");

			}
			List<OmsRtlogPublishLog> omsRtlogPublishLogList=session.getOmsRtlogPublishLogFindByOmsCustOrderNo(custOrdHeadSeqNo);
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+"<-------------Size of the omsRtlogPublishLogList : "+omsRtlogPublishLogList.size()+"------------->");
			if(omsRtlogPublishLogList.size()>0)
			{
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Setting Publish Indicator to F in OmsRtlogPublishLog ");
			    for(OmsRtlogPublishLog omsRtlogPublishLog:omsRtlogPublishLogList)
			    {
				omsRtlogPublishLog.setPublishedInd("F");
				log.info("omsCustOrdNo "+custOrdHeadSeqNo+"<--------------Indicator : "+omsRtlogPublishLog.getPublishedInd()+"-------------->");
				session.mergeOmsRtlogPublishLog(omsRtlogPublishLog);
			    }
			}

		    }

		}
	    }
	    catch(Exception e)
	    {
		log.info("omsCustOrdNo "+custOrdHeadSeqNo+" Exception occured "+e.getMessage());
		response.setMessageDesc("SYSTEM ERROR");
		response.setMessageCode("FAILED");
		response.setMessageStatus("F");
		OmsCustOrdHead omsCustOrd=session.getOmsCustOrdHeadFindByOmsCustOrdNo(custOrdHeadSeqNo);
		omsCustOrd.setOrdPaymentStatus("P");
		session.mergeOmsCustOrdHead(omsCustOrd);
	        log.info("omsCustOrdNo "+custOrdHeadSeqNo+" updated the payment status to p in head table");
		if(inventoryFlag==false)
		{
		    RMSPackage rMSPackage=new RMSPackage();
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+" Calling RMSPackage Call : "+input.getCustomerOrderNo());
		    rMSPackage.rollbackRmsPackageCall(input,custOrdHeadSeqNo);
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"End of Calling RMSPackage Call : "+input.getCustomerOrderNo());
		    InterfacePersistence interfacePersistence=new InterfacePersistence();
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"calling rollbackAdjustInventoryByItemLocation");
		    interfacePersistence.rollbackAdjustInventoryByItemLocation(input,custOrdHeadSeqNo);
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"omsCustOrdNo "+custOrdHeadSeqNo+"after calling rollbackAdjustInventoryByItemLocation");
		}

		OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
		log.info("omsCustOrdNo "+custOrdHeadSeqNo+" calling rollback for RMS and SIM ");
		omsCustomerOrderBean.rollbackForTimeout(custOrdHeadSeqNo,input.getCustomerOrderNo());
		log.info("omsCustOrdNo "+custOrdHeadSeqNo+"After calling rollback for RMS and SIM ");

		List<OmsCustOrdTender> omsCustOrdTenderList= session.getOmsCustOrdTenderFindByOmsCustOrdNo(custOrdHeadSeqNo);
		for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
		{
		    omsCustOrdTender.setPaymentStatusInd("P");
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"Setting last update time oms cust Ord Tender table");
		    omsCustOrdTender.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
		    session.mergeOmsCustOrdTender(omsCustOrdTender);
		    log.info("omsCustOrdNo"+custOrdHeadSeqNo+"updated Tender to P for omscustordNo "+omsCustOrdHead.getOmsCustOrdNo());

		}
		List<OmsRtlogPublishLog> omsRtlogPublishLogList=session.getOmsRtlogPublishLogFindByOmsCustOrderNo(custOrdHeadSeqNo);
		log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Size of the omsRtlogPublishLogList : "+omsRtlogPublishLogList.size()+"------------->");
		if(omsRtlogPublishLogList.size()>0)
		{
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Setting Publish Indicator to F in OmsRtlogPublishLog ");
		    for(OmsRtlogPublishLog omsRtlogPublishLog:omsRtlogPublishLogList)
		    {
			omsRtlogPublishLog.setPublishedInd("F");
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+"<--------------Indicator : "+omsRtlogPublishLog.getPublishedInd()+"-------------->");
			session.mergeOmsRtlogPublishLog(omsRtlogPublishLog);
		    }
		}
	    }


	} // end of else
	//added code fix for  - deleting the rows.
	try
	{

	    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"for removing the records from OmsPaymentSync ");
	    List<OmsPaymentSync> omsPaymentSyncList=session.getOmsPaymentSychfindByOmsCustOrdNo(custOrdHeadSeqNo);
	    if(omsPaymentSyncList.size()>0)
	    {
		for(OmsPaymentSync omsPaymentSync:omsPaymentSyncList)
		{
		    session.removeOmsPaymentSync(omsPaymentSync);
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Successfully removed from lineNo "+omsPaymentSync.getLineNo()+"Location "+omsPaymentSync.getLocation());
		}
   	    }
         
            log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Deleting the record from paymentAudit table");
            PersistPaymentRequest.deletePaymentAuditTable(input.getCustomerOrderNo(),custOrdHeadSeqNo);
            log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Deleted the record from paymentAudit table");
          
	}
	catch(Exception e)
	{
	    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Exception while deleting the record from OmsPaymentSync "+e.getMessage());
	}
	log.info("omsCustOrdNo "+custOrdHeadSeqNo+"MessageCode "+custOrdHeadSeqNo+response.getMessageCode());
	log.info("omsCustOrdNo "+custOrdHeadSeqNo+"MessageDesc "+response.getMessageDesc());
	log.info("omsCustOrdNo "+custOrdHeadSeqNo+"MessageStatus "+response.getMessageStatus());

	// insert into cfs_sfs_email_Status_info table
	// order delivery type should be customer pickup and payment status should be 'S'
	//Order  payment is confrimed we need to cfs_sfs_email_Status_info to send the mail to
	// store Manager to pickup the order( It will be taken care by batch)
	log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Logic to insert into cfs_sms_email_status_info table");
	OmsCustOrdHead omscustordHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(custOrdHeadSeqNo);

	if("C".equals(omscustordHead.getDeliveryType())&&"S".equals(omscustordHead.getStatus())&&"S".equals(omscustordHead.getOrdPaymentStatus()))
	{
	    insertintocfssmsemailinfotable(omscustordHead.getPickLoc().intValue(),omscustordHead.getCustOrderNo());
	}
	//madhu
	if("S".equals(omscustordHead.getDeliveryType())&&"S".equals(omscustordHead.getOrdPaymentStatus())&& "S".equals(omscustordHead.getStatus()))
	{
	    log.info("omsCustOrdNo "+custOrdHeadSeqNo +"deliverytype is S");
	    if(sfsConfirmationPojomap!=null && !sfsConfirmationPojomap.isEmpty())
	    {
		log.info("omsCustOrdNo "+custOrdHeadSeqNo+" SFS map is not null");
		try
		{
		    ArrayList<SfsConfirmationPojo> list=sfsConfirmationPojomap.get(omscustordHead.getOmsCustOrdNo());
		    if(list.size()>0)
		    {
			log.info("omsCustOrdNo "+custOrdHeadSeqNo+" checking SFS list is not null");
			for(SfsConfirmationPojo pojo:list)
			{
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"inside SFS list is not null");
			    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"inserting into emailconfirmation table and store "+ pojo.getStoreNo());
			    insertintocfssmsemailinfotable(pojo.getStoreNo().intValue(),omscustordHead.getCustOrderNo());
			}
		    }
		}
		catch(Exception e)
		{
		    log.info("omsCustOrdNo "+custOrdHeadSeqNo+"Exception in SFS menthod"+e.getMessage());
		}
	    }
	}
	return response;
    } //end of method

    private void insertintocfssmsemailinfotable(int storeNo,String customerOrderNo) throws SOAPException
    {
	log.info("insertintocfssmsemailinfotable methods Begin  store no is "+storeNo+"Customer Order No is "+customerOrderNo);
	// OMSUtilSessionEJB session = OMSUtil.doLookup();
	ResponseProcessing responseProcessing=new ResponseProcessing();
	// log.info("session value is "+session);
	CfsSmsEmailStatusInfo cfsSmsEmailStatusInfoObj=new CfsSmsEmailStatusInfo();
	log.info(customerOrderNo+" 1.setting the store no");
	String newStore=storeNo+"";
	cfsSmsEmailStatusInfoObj.setStoreNo(new BigDecimal(newStore));
	log.info(customerOrderNo+"2.setting the custoemr order no ");
	cfsSmsEmailStatusInfoObj.setCustOrderNo(customerOrderNo);
	log.info(customerOrderNo+"setting the timestamp");
	//new Timestamp(new Date().getTime())
	cfsSmsEmailStatusInfoObj.setCreateDatetime(new Timestamp(new Date().getTime()));
	cfsSmsEmailStatusInfoObj.setSmsStatusCode("0");
	cfsSmsEmailStatusInfoObj.setEmailStatusCode("0");
	cfsSmsEmailStatusInfoObj.setOrderPickStatus("0");
	cfsSmsEmailStatusInfoObj.setEmailProcessInd("N");
	cfsSmsEmailStatusInfoObj.setSmsProcessInd("N");
	log.info(customerOrderNo+" calling persists  methods to save the record into DataBase"+cfsSmsEmailStatusInfoObj.hashCode());
	try
	{
	    //session.persistCfsSmsEmailStatusInfo(cfsSmsEmailStatusInfoObj);
	    responseProcessing.insertCfsSmsEmailInfo(cfsSmsEmailStatusInfoObj);
	}
	catch(Exception e)
	{
	    log.info(customerOrderNo+" Failed to inserting into cfsSmsEmailStatusInfo table : "+e.getMessage());
	}
	log.info(customerOrderNo+"insert into cfssmsemailinfotable methods Begin");
    }

    private void insertCfsSmsEmailInfo(CfsSmsEmailStatusInfo cfsBean) throws Exception
    {
	log.info(cfsBean.getCustOrderNo()+"***insert into CFS_SMS_EMAIL_STATUS_INFO ***");
	Connection conn=null;
	PreparedStatement statement=null;
	ResultSet rs=null;
	//String sequenceQuery = "select BACK_ORDER_BATCH_SEQ.NEXTVAL as SEQUENCE_NUM from dual";
	String query=
		   "insert into CFS_SMS_EMAIL_STATUS_INFO(ID, CUST_ORDER_NO, STORE_NO, ORDER_PICK_STATUS, CREATE_DATETIME, LAST_UPDATE_DATETIME, EMAIL_STATUS_CODE, SMS_STATUS_CODE, EMAIL_PROCESS_IND, SMS_PROCESS_IND) values(CFS_SMS_EMAIL_STATUS_INFO_SEQ.nextval,?,?,?,systimestamp,systimestamp,?,?,?,?)";
	try
	{
	    conn=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
	    statement=conn.prepareStatement(query);
	    statement.setString(1,cfsBean.getCustOrderNo());
	    statement.setBigDecimal(2,cfsBean.getStoreNo());
	    statement.setString(3,cfsBean.getOrderPickStatus());
	    statement.setString(4,cfsBean.getEmailStatusCode());
	    statement.setString(5,cfsBean.getSmsStatusCode());
	    statement.setString(6,cfsBean.getEmailProcessInd());
	    statement.setString(7,cfsBean.getSmsProcessInd());
	    statement.executeUpdate();
	    log.info(cfsBean.getCustOrderNo()+"Successfully inserted into CFS_SMS_EMAIL_STATUS_INFO..");
	}
	catch(Exception e)
	{
	    log.info(cfsBean.getCustOrderNo()+" Failed to inserting into cfsSmsEmailStatusInfo table : "+e.getMessage());
	}
	finally
	{
	    try
	    {
		OMSUtil.closeDBConnection(conn,statement,rs);
	    }
	    catch(Exception e)
	    {
		log.info(cfsBean.getCustOrderNo()+"Exception occured in finally block "+e.getMessage());
	    }
	}
    }

} // end of class
