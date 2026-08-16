package com.logicinfo.oms.model;


import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.beans.PartialResponse;
import com.logicinfo.oms.beans.ReturnPartialResponseObject;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.ejb.OmsUnapprovedTransfers;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.ConfirmType;
import com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1.FulfilOrdCfmDtl;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class ExtSystemUpdate
{
    public ExtSystemUpdate()
    {
	super();
    }
    public final static Logger log=Logger.getLogger(com.logicinfo.oms.model.ExtSystemUpdate.class.getName());
    BigDecimal fulfilmentOrderNo=null;
    FulfilOrdCfmCol fulfilOrdCfmCol=null;
    ConfirmType confirmType=null;

    FulfilOrdCfmCol pfulfilOrdCfmCol=null;
    ConcurrentHashMap<BigDecimal,FulfilOrdCfmCol> pfulfilOrdCfmCols=
	new ConcurrentHashMap<BigDecimal,FulfilOrdCfmCol>();
    ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> xfulfilOrdCfmCols=
	new ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>>();
    List<ProcessedObject> processedObjectList=null;
    int retryWS=2;
    boolean flag=false;
    int currentFulfilOrderNo=0;
    int maxFulfilOrderNo=0;
    String status="F";
    ArrayList<BigDecimal> transferList=new ArrayList<BigDecimal>();

    public ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> processPaymentConfirmation(CoPaymentConf input,
											   BigDecimal omsCustOrdNo) throws SOAPException,
															   com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
															   com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
															   com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
															   com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
															   com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
															   com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
															   com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
															   com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
															   com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
															   EntityAlreadyExistsWSFaultException, BusinessException
    {
	log.info(" ***********************Begin of  processPaymentConfirmation method ***************************** ");
	log.info("omsCustOrdNo "+omsCustOrdNo+"***Start : processSadad");
	OMSUtilSessionEJB session=OMSUtil.doLookup();

	TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> newFulfillDetailMap=null;
	processedObjectList=new ArrayList<ProcessedObject>();
	OMSCustomerOrderBean oMSCustomerOrderBean=new OMSCustomerOrderBean();
	List<OmsCustOrdReserve> omsCustOrdReserveList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
	log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdReserveList size="+omsCustOrdReserveList.size());
	ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap=
		   new ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>>();
	ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> detailMap=
		   new ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>>();

	BigDecimal physicalWH;
	if(omsCustOrdReserveList.size()>0)
	{
	    for(OmsCustOrdReserve omsCustOrdReserveTemp:omsCustOrdReserveList)
	    {
		if(omsCustOrdReserveTemp.getQty().intValue()>0)
		{
		    OmsTempCoFo omsTempCoFo=convertOmsCustOrdResvToOmsCustOrdTemp(omsCustOrdReserveTemp);
		    log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdReserveTemp value item="+omsTempCoFo.getItem()+"Lineno="+
			     omsTempCoFo.getLineNo()+"fulfillment no="+omsTempCoFo.getFulfillOrderNo()+" sourcLoc="+
			     omsTempCoFo.getSourceLocId());
		    if(fulfillDetailMap.get(omsTempCoFo.getFulfillOrderNo())==null||
		       fulfillDetailMap.get(omsTempCoFo.getFulfillOrderNo()).size()==0)
		    {

			if(omsTempCoFo.getSourceLocationType().equals("WH"))
			{
			    physicalWH=session.getWhFindPhyWhForVirtualWh(omsTempCoFo.getSourceLocId());
			    omsTempCoFo.setSourceLocId(physicalWH);
			}
			ArrayList<OmsTempCoFo> tempList=new ArrayList<OmsTempCoFo>();
			tempList.add(omsTempCoFo);
			log.info("omsCustOrdNo "+omsCustOrdNo+"Fulfildetail Map value item="+omsTempCoFo.getItem()+"Lineno="+
				 omsTempCoFo.getLineNo()+"fulfillment no="+omsTempCoFo.getFulfillOrderNo()+" sourcLoc="+
				 omsTempCoFo.getSourceLocId());

			fulfillDetailMap.put(omsTempCoFo.getFulfillOrderNo(),tempList);
		    }
		    else
		    {
			log.info("omsCustOrdNo "+omsCustOrdNo+"Ship to customer scenario setting in to soh map with fulfill loc="+
				 omsCustOrdReserveTemp.getLoc());
			ArrayList<OmsTempCoFo> existingList=null;
			existingList=fulfillDetailMap.get(omsCustOrdReserveTemp.getFulfillOrderNo());
			if(omsTempCoFo.getSourceLocationType().equals("WH"))
			{
			    physicalWH=session.getWhFindPhyWhForVirtualWh(omsTempCoFo.getSourceLocId());
			    omsTempCoFo.setSourceLocId(physicalWH);
			}
			existingList.add(omsTempCoFo);
			log.info("omsCustOrdNo "+omsCustOrdNo+"Fulfildetail Map value item="+omsTempCoFo.getItem()+"Lineno="+
				 omsTempCoFo.getLineNo()+"fulfillment no="+omsTempCoFo.getFulfillOrderNo()+" sourcLoc="+
				 omsTempCoFo.getSourceLocId());

			fulfillDetailMap.put(omsTempCoFo.getFulfillOrderNo(),existingList);
		    }
		}
	    }
	    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfillDetailMap size is "+fulfillDetailMap.size()+
		     "*****************");

	    ArrayList<OmsTempCoFo> tempList=null;
	    InterfacePersistence interfacePersistence=new InterfacePersistence();
	    OMSUtilCommons oMSUtilCommons=new OMSUtilCommons();
	    ProcessedObject processedObject=null;
	    log.info("fulfillDetailMap before getting from RMS "+fulfillDetailMap.keySet());
	    //check the fulFilOrdNo for this order from RMS and SIM - Bug Fix for 2713
	    maxFulfilOrderNo=oMSUtilCommons.returnMaxFulFilOrdNo(input.getCustomerOrderNo());
	    log.info("maxFulfilOrderNo from base tables "+maxFulfilOrderNo);
	    if(maxFulfilOrderNo>1)
	    {
		int j=0;

		for(BigDecimal keyName:fulfillDetailMap.keySet())
		{
		    if(j==0)
		    {
			//no change in maxfulfilOrdNo
			log.info("maxFulfilOrderNo no change "+maxFulfilOrderNo);
		    }
		    else
		    {
			maxFulfilOrderNo=maxFulfilOrderNo+1;
			log.info("maxFulfilOrderNo in else "+maxFulfilOrderNo);
		    }
		    log.info("keyName "+keyName);
		    tempList=fulfillDetailMap.get(keyName);
		    for(OmsTempCoFo temp:tempList)
		    {
			log.info("tempList.size "+tempList.size());

			temp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));

			if(detailMap.get(temp.getFulfillOrderNo())==null||
			   detailMap.get(temp.getFulfillOrderNo()).size()==0)
			{
			    log.info("Adding into map  with line no="+temp.getLineNo()+" source loc is "+temp.getSourceLocId()+
				     " with fulOrdNo="+temp.getFulfillOrderNo());
			    ArrayList<OmsTempCoFo> tempList1=new ArrayList<OmsTempCoFo>();
			    tempList1.add(temp);
			    detailMap.put(temp.getFulfillOrderNo(),tempList1);
			}
			else
			{
			    log.info("Adding into existing object with line no="+temp.getLineNo()+" source loc is "+temp.getSourceLocId()+
				     " with fulOrdNo="+temp.getFulfillOrderNo());
			    ArrayList<OmsTempCoFo> existingList=null;
			    existingList=detailMap.get(temp.getFulfillOrderNo());
			    existingList.add(temp);
			    detailMap.put(temp.getFulfillOrderNo(),existingList);

			}
		    }
		    log.info("out of for loop ");
		    j++;
		    log.info("Removing the keyName "+keyName);
		    fulfillDetailMap.remove(keyName);

		}
		fulfillDetailMap.clear();
		fulfillDetailMap.putAll(detailMap);
		log.info("fulfillDetailMap keySet "+fulfillDetailMap.keySet());
	    }
	    TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> tempMap1=new TreeMap<BigDecimal,ArrayList<OmsTempCoFo>>();
	    tempMap1.putAll(fulfillDetailMap);

	    for(int k=1;k<=tempMap1.lastKey().intValue();k++)
	    {
		BigDecimal key=new BigDecimal(k);
		try
		{
		    tempList=fulfillDetailMap.get(key);
		    if(tempList==null)
		    {
			// k--;
			continue;
		    }
		    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfillDetailMap.keySet() "+fulfillDetailMap.keySet());
		    log.info("omsCustOrdNo "+omsCustOrdNo+"inside try of process Non Sadad");
		    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfillDetailMap.keySet() "+fulfillDetailMap.keySet());
		    log.info("omsCustOrdNo "+omsCustOrdNo+"key :"+key);
		    if(tempList.get(0).getRmsResponseCode()==null)
		    {
			log.info("Calling CallWebservices at line no 209");
			processedObject=interfacePersistence.callWebservices(omsCustOrdNo,tempList);
			log.info("omsCustOrdNo "+omsCustOrdNo+"Call webservice success");
			fulfilOrdCfmCol=processedObject.getFulfilOrdCfmCol();
			currentFulfilOrderNo=processedObject.getCurrentFulFillOrderNo();
			log.info("currentFulfilOrderNo after calling callWebservices "+currentFulfilOrderNo);
			tempList=processedObject.getOmsTempCoFoList();
			log.info("omsCustOrdNo "+omsCustOrdNo+"Processed object size"+tempList.size());
			fulfillDetailMap.put(key,tempList);
			maxFulfilOrderNo=tempMap1.lastEntry().getKey().intValue();
			log.info("maxFulfilOrderNo in the fullfillment "+maxFulfilOrderNo);
		    }
		    else if(tempList.get(0).getRmsResponseCode().equals("X"))
		    {
			XResponseProcessingObj xResponseProcessingObj=
						 processXResponse(omsCustOrdNo,tempList,maxFulfilOrderNo,fulfillDetailMap,newFulfillDetailMap,
								  input);
			fulfillDetailMap=xResponseProcessingObj.getFulfillDetailMap();
			tempMap1.putAll(fulfillDetailMap);
			maxFulfilOrderNo=tempMap1.lastKey().intValue();
			log.info("omsCustOrdNo "+omsCustOrdNo+"maxFulfilOrderNo after calling X response "+
				 maxFulfilOrderNo);
		    }
		    else
		    {
			log.info("omsCustOrdNo "+omsCustOrdNo+"inside else block tempList.get(0).getRmsResponseCode() "+
				 tempList.get(0).getRmsResponseCode());
			log.info("omsCustOrdNo "+omsCustOrdNo+"RmsResponseCode is not equal X or P");
		    }
		}
		catch(SOAPException e)
		{
		    log.error("SOAP Exception occured="+e);
		    // throw e;
		    return fulfillDetailMap;
		}
		catch(javax.xml.ws.WebServiceException f)
		{
		    log.info("omsCustOrdNo "+omsCustOrdNo+"WSDL unanavailbale error "+f.getMessage());
		    for(OmsTempCoFo omsTempCoFo:tempList)
		    {
			// OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(),omsTempCoFo.getLineNo());
			// omsCustOrdItem.setStatus("F");
			// session.mergeOmsCustOrdItem(omsCustOrdItem);
		    }
		    log.info("omsCustOrdNo "+omsCustOrdNo+"Merging omsCustORdHead");
		    //OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		    //omsCustOrdHead.setStatus("F");
		    //session.mergeOmsCustOrdHead(omsCustOrdHead);
		    OMSCustomerOrderBean bean=new OMSCustomerOrderBean();
		    log.info("omsCustOrdNo "+omsCustOrdNo+"Rollbacking");

		    //bean.rollback(fulfillDetailMap);
		    log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
			     "----------------->");
		    log.info("calling rollback for : "+input.getCustomerOrderNo());
		    bean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());

		    log.info("omsCustOrdNo "+omsCustOrdNo+"throwing RMS_UNAVL");
		    // throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
		    return fulfillDetailMap;
		}
		catch(Exception e)
		{
		    if(processedObject.getTsf_no()!=null)
		    {
			break;
		    }
		    log.error("omsCustOrdNo "+omsCustOrdNo+"Error in processing fulfillment :"+e.getMessage());
		    log.info("calling rollbackForTimeout");
		    log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
			     "----------------->");
		    log.info("calling rollback for : "+input.getCustomerOrderNo());
		    OMSCustomerOrderBean bean=new OMSCustomerOrderBean();
		    bean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());
		    return fulfillDetailMap;
		}
		log.info("-----------------------------------------------------------------------------------");
		//String responseStatus = interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);

		String responseStatus="";
		if(processedObject.getTsf_no()!=null)
		{
		    responseStatus="C";
		}
		else
		{
		    responseStatus=interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol,tempList);
		}
		log.info("-----------------------Response received is"+responseStatus+
			 "---------------------------------");

		// responseStatus.equals("C") removed to fix 2473 bug
		while(responseStatus.equals("X")||responseStatus.equals("P"))
		{
		    log.info("******** responseStatus : "+responseStatus+"*************************************");
		    if(responseStatus.equals("X"))
		    {
			log.info("Received X response.");
			log.info("omsCustOrdNo "+omsCustOrdNo+"Received X response.");
			log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
			log.info("currentFulfilOrderNo loop "+currentFulfilOrderNo);
			log.info("fulfillDetailMap.keySet() "+fulfillDetailMap.keySet());
			try
			{
			    XResponseProcessingObj xResponseProcessingObj=
						     processXResponse(omsCustOrdNo,tempList,maxFulfilOrderNo,fulfillDetailMap,newFulfillDetailMap,
								      input);
			    fulfillDetailMap=xResponseProcessingObj.getFulfillDetailMap();
			    tempMap1.putAll(fulfillDetailMap);
			    maxFulfilOrderNo=tempMap1.lastKey().intValue();
			    log.info("maxFulfilOrderNo after calling X response "+maxFulfilOrderNo);
			    for(BigDecimal tempKey:fulfillDetailMap.keySet())
			    {

				log.info("tempKey "+tempKey);
				tempList=fulfillDetailMap.get(tempKey);
				for(OmsTempCoFo omsTemp:tempList)
				{
				    if(omsTemp.getRmsResponseCode()!=null&&!"".equals(omsTemp.getRmsResponseCode()))
				    {
					log.info("omsCustOrdNo "+omsCustOrdNo+"tempList.size() inside newFulfillDetailMap "+
						 tempList.size());
					log.info("omsCustOrdNo "+omsCustOrdNo+"Fulfill order No in tempList loop"+
						 omsTemp.getFulfillOrderNo());
					log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdNo "+
						 omsTemp.getOmsCustOrdNo());
					log.info("omsCustOrdNo "+omsCustOrdNo+" lineNo= "+omsTemp.getLineNo());
					log.info("omsCustOrdNo "+omsCustOrdNo+" item= "+omsTemp.getItem());
					log.info("omsCustOrdNo "+omsCustOrdNo+" Quantity Ordered= "+
						 omsTemp.getOrderQty());
					log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfilmetn OrderNo="+
						 omsTemp.getFulfillOrderNo());
					log.info("omsCustOrdNo "+omsCustOrdNo+" source loc id= "+
						 omsTemp.getSourceLocId());
					log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfill loc id= "+
						 omsTemp.getFulfillLocId());
					log.info("omsCustOrdNo "+omsCustOrdNo+" Quantity Confirmed= "+
						 omsTemp.getFoConfQty());
					log.info("omsCustOrdNo "+omsCustOrdNo+" RmsResponseCode="+
						 omsTemp.getRmsResponseCode());
					responseStatus=omsTemp.getRmsResponseCode();
					log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				    }
				}


			    }

			    //Code for P/X, process P responses after X response
			    for(BigDecimal pkey:pfulfilOrdCfmCols.keySet())
			    {
				log.info("<--------------- Process One by One P map in X--pkey----->"+pkey+"<----->");
				tempList=findUnProcessed_P_MapObject(fulfillDetailMap);
				if(tempList!=null)
				{
				    BigDecimal tempkey=getUnProceesedPResponsekey(fulfillDetailMap);
				    ArrayList<OmsTempCoFo> unfulfilledItems=new ArrayList<OmsTempCoFo>();
				    log.info("------------>The value of temp list is ----------------------->"+tempkey+
					     "<----------------");
				    log.info("Calling  ProceessPResponseFulFillmentOrder  method --------------------->");
				    unfulfilledItems=
		 ProceessPResponseFulFillmentOrder(tempList,fulfillDetailMap,omsCustOrdNo,tempkey);
				    if(unfulfilledItems.size()>0)
				    {
					PResponseProcessingObj pResponseProcessingObj=null;
					try
					{
					    pResponseProcessingObj=
		  processPResponse(omsCustOrdNo,unfulfilledItems,fulfilOrdCfmCol,maxFulfilOrderNo,fulfillDetailMap,
				   input);
					}
					catch(Exception e)
					{
					    log.info(" ********************Error Occured while unfulfilledItems are processed***********  ");
					}
					log.info(" 2.0  Execution line  Below line ");
					log.info("pResponseProcessingObj.getNewFulfillMap()"+
						 pResponseProcessingObj.getNewFulfillMap());
					newFulfillDetailMap=pResponseProcessingObj.getNewFulfillMap();
					log.info("newFulfillDetailMap.keySet() after calling processPresponse "+
						 newFulfillDetailMap.keySet());
					fulfillDetailMap.putAll(newFulfillDetailMap);
					tempMap1.putAll(fulfillDetailMap);
					log.info("<----fulfillDetailMap--->"+fulfillDetailMap.keySet()+"<---->");
					maxFulfilOrderNo=tempMap1.lastKey().intValue();
					log.info("<---maxFulfilOrderNo--->"+maxFulfilOrderNo+"<----->");

				    } // end of  unfulfilledItems size
				} // end of if condition..........
			    }

			    for(BigDecimal pkey:pfulfilOrdCfmCols.keySet())
			    {
				log.info("<--------------- Process One by One P map in X--pkey----->"+pkey+"<----->");
				tempList=findUnProcessed_P_MapObject(fulfillDetailMap);
				if(tempList!=null)
				{
				    BigDecimal tempkey=getUnProceesedPResponsekey(fulfillDetailMap);
				    ArrayList<OmsTempCoFo> unfulfilledItems=new ArrayList<OmsTempCoFo>();
				    log.info("------------>The value of temp list is ----------------------->"+tempkey+
					     "<----------------");
				    log.info("Calling  ProceessPResponseFulFillmentOrder  method --------------------->");
				    unfulfilledItems=
		 ProceessPResponseFulFillmentOrder(tempList,fulfillDetailMap,omsCustOrdNo,tempkey);
				    if(unfulfilledItems.size()>0)
				    {
					PResponseProcessingObj pResponseProcessingObj=null;
					try
					{
					    pResponseProcessingObj=
		  processPResponse(omsCustOrdNo,unfulfilledItems,fulfilOrdCfmCol,maxFulfilOrderNo,fulfillDetailMap,
				   input);
					}
					catch(Exception e)
					{
					    log.info(" ********************Error Occured while unfulfilledItems are processed***********  ");
					}
					log.info("pResponseProcessingObj.getNewFulfillMap()"+
						 pResponseProcessingObj.getNewFulfillMap());
					newFulfillDetailMap=pResponseProcessingObj.getNewFulfillMap();
					log.info("newFulfillDetailMap.keySet() after calling processPresponse"+
						 newFulfillDetailMap.keySet());
					fulfillDetailMap.putAll(newFulfillDetailMap);
					tempMap1.putAll(fulfillDetailMap);
					maxFulfilOrderNo=tempMap1.lastKey().intValue();
					log.info("<-----fulfillDetailMap---->"+fulfillDetailMap.keySet()+"<---->");
					log.info("<------ maxFulfilOrderNo------>"+maxFulfilOrderNo+"<---->");
				    } // end of  unfulfilledItems size
				} // end of if condition..........
			    }

			    log.info("<-----------------Calling findUnProcessed_X_MapObject Method-------------------------->");
			    for(BigDecimal xKey:xfulfilOrdCfmCols.keySet())
			    {
				log.info("<-------- Processing X map One to One xKey--->"+xKey+
					 "<-------------------------------------->");
				tempList=findUnProcessed_X_MapObject(fulfillDetailMap);
				if(tempList!=null)
				{
				    log.info("<------------Processing  un Processed  X Response temp list Object---------->");
				    try
				    {

					xResponseProcessingObj=
		  processXResponse(omsCustOrdNo,tempList,maxFulfilOrderNo,fulfillDetailMap,newFulfillDetailMap,input);
					fulfillDetailMap=xResponseProcessingObj.getFulfillDetailMap();
					tempMap1.putAll(fulfillDetailMap);
					maxFulfilOrderNo=tempMap1.lastKey().intValue();
					log.info("<--------maxFulfilOrderNo-------->"+maxFulfilOrderNo+
						 "<---------------->");
					responseStatus=
			  interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol,tempList);
				    }
				    catch(Exception e)
				    {
					log.error("Failed in processing X response"+e);
					//code for updating status of head table
					// throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
					log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
						 "----------------->");
					//bean.rollbackForTimeout(omsCustOrdNo, input.getCustomerOrderNo());
				    }
				}
			    }


			    //code for P/X end


			}
			catch(Exception e)
			{
			    log.error("Failed in processing X response"+e);
			    //code for updating status of head table
			    OMSCustomerOrderBean bean=new OMSCustomerOrderBean();
			    try
			    {
				// bean.rollback(fulfillDetailMap);
				log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
					 "----------------->");
				log.info("calling rollback for : "+input.getCustomerOrderNo());
				bean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());

			    }
			    catch(Exception f)
			    {
				log.error("Failed in cancellation while rollbacking with error "+f.getMessage());
			    }
			    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
			}
		    }
		    else if(responseStatus.equals("P"))
		    {
			log.info("inside P response equals");
			log.info("omsCustOrdNo "+omsCustOrdNo+"Received P response.");
			log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
			log.info("currentFulfilOrderNo loop "+currentFulfilOrderNo);
			log.info("fulfillDetailMap.keySet() "+fulfillDetailMap.keySet());
			List<FulfilOrdCfmDtl> fulfilOrdCfmDtlList=null;
			try
			{
			    if(fulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty()&&fulfilOrdCfmCol.getCollectionSize()==0)
			    {

				log.info("<----------fulfilOrdCfmCol variable is null -------------->");
				log.info("<---------------------Response code is C-----------------------------------> ");
			    }
			    else
			    {

				fulfilOrdCfmDtlList=
	      this.fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getFulfilOrdCfmDtl();
			    }
			}
			catch(Exception e)
			{
			    log.info("-------->Error Occurred------->"+e.getMessage()+"------------->");
			}

			ArrayList<OmsTempCoFo> unfulfilledItems=new ArrayList<OmsTempCoFo>();
			Map<String,BigDecimal> tempMap=new HashMap<String,BigDecimal>();
			log.info("----------------fulfilOrdCfmDtlList--------------------------");
			if(fulfilOrdCfmDtlList!=null&&fulfilOrdCfmDtlList.size()>0)
			{
			    for(FulfilOrdCfmDtl fulfilOrdCfmDtl:fulfilOrdCfmDtlList)
			    {
				log.info(" ----------Item map contains"+fulfilOrdCfmDtl.getItem());
				tempMap.put(fulfilOrdCfmDtl.getItem(),fulfilOrdCfmDtl.getConfirmQty());
			    }
			    for(BigDecimal mapkey:fulfillDetailMap.keySet())
			    {
				tempList=fulfillDetailMap.get(mapkey);
				for(OmsTempCoFo tempCoFo:tempList)
				{
				    log.info(" tempCoFo.getItem()"+tempCoFo.getItem());
				    if(tempMap.containsKey(tempCoFo.getItem())==true&&tempCoFo.getRmsResponseCode()!=null&&
				       tempCoFo.getRmsResponseCode().equals("P"))
				    {
					log.info("key contains in tempmap");
					log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
					OmsTempCoFo unfulfilledTemp=new OmsTempCoFo();
					log.info("tempCoFo.getOrderQty() "+tempCoFo.getOrderQty());
					log.info("remaing Qty "+
						 tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
					if((tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())).intValue()>
					    0))
					{
					    // maxFulfilOrderNo=maxFulfilOrderNo+1;
					    log.info("maxFulfilOrderNo after adding "+maxFulfilOrderNo);
					    unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
					    unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
					    unfulfilledTemp.setItem(tempCoFo.getItem());
					    unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
					    log.info("tempCoFo.getOrderQty() "+tempCoFo.getOrderQty());
					    log.info("remaing Qty "+
						     tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
					    unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
					    unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
					    unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
					    unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
					    unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
					    unfulfilledTemp.setRmsResponseCode("P");
					    log.info("unfulfilledTemp.getSourceLocId() "+
						     unfulfilledTemp.getSourceLocId());
					    log.info("unfulfilledTemp.getCombinationId()"+
						     unfulfilledTemp.getCombinationId());
					    log.info("unfulfilledTemp.getRmsResponseCode()"+
						     unfulfilledTemp.getRmsResponseCode());
					    log.info("unfulfilledTemp.getOrderQty() "+unfulfilledTemp.getOrderQty());
					    tempCoFo.setOrderQty(tempMap.get(tempCoFo.getItem()));
					    tempCoFo.setFoConfQty(tempMap.get(tempCoFo.getItem()));
					    if(unfulfilledTemp.getItem().equals(tempCoFo.getItem()))
					    {
						tempCoFo.setRmsResponseCode("C");
						log.info("tempCoFo.getRmsResponseCode()"+
							 tempCoFo.getRmsResponseCode());
						log.info("tempCoFo.orderQty"+tempMap.get(tempCoFo.getItem()));
						log.info("tempCoFo.ConfQty "+tempMap.get(tempCoFo.getItem()));
					    }
					    unfulfilledItems.add(unfulfilledTemp);
					}
					else
					{
					    tempCoFo.setRmsResponseCode("C");
					    tempCoFo.setFoConfQty(tempCoFo.getOrderQty());
					}


				    }
				    else if(tempCoFo.getRmsResponseCode()!=null&&
					    tempCoFo.getRmsResponseCode().equals("P"))
				    {
					log.info("key doesnot contain in tempmap");
					log.info("passing currentFulfilOrderNo in fullfilldetailMap "+
						 currentFulfilOrderNo);
					tempList=fulfillDetailMap.get(new BigDecimal(currentFulfilOrderNo));
					// Removing the loop as part of P/x
					// for (OmsTempCoFo temp : tempList) {
					log.info("Start of temp for loop");
					log.info("maxFulfilOrderNo "+maxFulfilOrderNo);

					log.info("temp.getOrderQty() "+tempCoFo.getOrderQty());
					log.info("temp.getItem()"+tempCoFo.getItem());
					// log.info("tempMap.get(temp.getItem())" + tempMap.get(tempCoFo.getItem()));
					//removed if condition P/X
					log.info("remaing Qty "+tempCoFo.getOrderQty());
					log.info("temp.getRmsResponseCode() "+tempCoFo.getRmsResponseCode());
					if(tempCoFo.getOrderQty().intValue()>0)
					{
					    log.info("1.************************************");
					    OmsTempCoFo unfulfilledTemp=new OmsTempCoFo();
					    //maxFulfilOrderNo=maxFulfilOrderNo+1;
					    log.info("maxFulfilOrderNo after adding "+maxFulfilOrderNo);
					    unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
					    unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
					    unfulfilledTemp.setItem(tempCoFo.getItem());
					    unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
					    unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty());
					    unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
					    unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
					    unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
					    unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
					    unfulfilledTemp.setRmsResponseCode("P");
					    log.info("unfulfilledTemp.getSourceLocId() "+
						     unfulfilledTemp.getSourceLocId());
					    log.info("unfulfilledTemp.getCombinationId()"+
						     unfulfilledTemp.getCombinationId());
					    log.info("unfulfilledTemp.getRmsResponseCode()"+
						     unfulfilledTemp.getRmsResponseCode());
					    log.info("unfulfilledTemp.getOrderQty() "+unfulfilledTemp.getOrderQty());
					    tempCoFo.setOrderQty(tempCoFo.getOrderQty()); // P/X processing
					    tempCoFo.setFoConfQty(new BigDecimal(0)); // P/X processing

					    if(unfulfilledTemp.getItem().equals(tempCoFo.getItem()))
					    {
						tempCoFo.setRmsResponseCode("C");
						log.info("temp.getRmsResponseCode()"+tempCoFo.getRmsResponseCode());
					    }
					    unfulfilledItems.add(unfulfilledTemp);
					}
					else
					{
					    log.info("2.*******************************************************");
					    tempCoFo.setRmsResponseCode("C");
					    tempCoFo.setFoConfQty(new BigDecimal(0)); //P/X processing
					}
					//    }

				    }


				}
			    }
			} //end of if added for P/X

			log.info("unfulfilledItems "+unfulfilledItems.size());
			PResponseProcessingObj pResponseProcessingObj=null;
			if(unfulfilledItems.size()>0)
			{
			    log.info("-----------------------------pResponseProcessingObj***********************");
			    pResponseProcessingObj=
    processPResponse(omsCustOrdNo,unfulfilledItems,fulfilOrdCfmCol,maxFulfilOrderNo,fulfillDetailMap,input);
			    newFulfillDetailMap=pResponseProcessingObj.getNewFulfillMap();
			    log.info("newFulfillDetailMap.keySet() after calling processPresponse "+
				     newFulfillDetailMap.keySet());
			    fulfillDetailMap.putAll(newFulfillDetailMap);
			    tempMap1.putAll(fulfillDetailMap);
			    maxFulfilOrderNo=tempMap1.lastKey().intValue();
			    log.info("maxFulfilOrderNo after calling P response "+maxFulfilOrderNo);
			    //code for P/X, process nested P response, one by one , one fulfillOrderNo will be called once and it will contain the single item
			    log.info("----------------- finding in a map  templist which are un processed-------->");
			    for(BigDecimal pkey:pfulfilOrdCfmCols.keySet())
			    {
				log.info("<---- Process P map One by One-----pkey-------------->"+pkey+"<----->");
				tempList=findUnProcessed_P_MapObject(fulfillDetailMap);
				if(tempList!=null)
				{
				    // tempList contains an Response Code 'P'
				    BigDecimal tempkey=getUnProceesedPResponsekey(fulfillDetailMap);
				    log.info("------------>The value of temp list is ----------------------->"+key+
					     "<----------------");
				    log.info("Calling  ProceessPResponseFulFillmentOrder  method --------------------->");
				    unfulfilledItems=
		 ProceessPResponseFulFillmentOrder(tempList,fulfillDetailMap,omsCustOrdNo,tempkey);
				    log.info("<------------unfulfilledItems  are --------------------------------------------->");
				    if(unfulfilledItems.size()>0)
				    {
					try
					{
					    pResponseProcessingObj=
		  processPResponse(omsCustOrdNo,unfulfilledItems,fulfilOrdCfmCol,maxFulfilOrderNo,fulfillDetailMap,
				   input);
					}
					catch(Exception e)
					{
					    log.info(" ********************Error Occured while processing unfulfilledItems **********  ");
					}
					log.info("pResponseProcessingObj.getNewFulfillMap()"+
						 pResponseProcessingObj.getNewFulfillMap());
					newFulfillDetailMap=pResponseProcessingObj.getNewFulfillMap();
					log.info("newFulfillDetailMap.keySet() after calling processPresponse "+
						 newFulfillDetailMap.keySet());
					fulfillDetailMap.putAll(newFulfillDetailMap);
					tempMap1.putAll(fulfillDetailMap);
					maxFulfilOrderNo=tempMap1.lastKey().intValue();
				    }

				} // end of  P Response.
				log.info("<------------EndProcess P Map----------------------------------------->");
			    }
			    //Process nested X response for each fulfilment order
			    log.info("<-------------Calling findUnProcessed_X_MapObject method --------------->");
			    for(BigDecimal xkey:xfulfilOrdCfmCols.keySet())
			    {
				log.info("<----Processing X map One By One---xkey ------->"+xkey+"<----->");
				tempList=findUnProcessed_X_MapObject(fulfillDetailMap);
				if(tempList!=null)
				{
				    try
				    {
					log.info("<------omsCustOrdNo-------->"+omsCustOrdNo+"<----------------->");
					XResponseProcessingObj xResponseProcessingObj=
									       processXResponse(omsCustOrdNo,tempList,maxFulfilOrderNo,fulfillDetailMap,newFulfillDetailMap,
												input);
					fulfillDetailMap=xResponseProcessingObj.getFulfillDetailMap();
					tempMap1.putAll(fulfillDetailMap);
					maxFulfilOrderNo=tempMap1.lastKey().intValue();
				    }
				    catch(Exception e)
				    {
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
				    }
				}
				log.info("<----------------------------------->");
			    }

			    //code end for P/X
			}
			else
			{
			    break;
			}

		    }
		    log.info("******** responseStatus : "+responseStatus+"*************************************");
		} //while loop end

		log.info("After adding the newFulfillDetailMap into fulfillDetailMap is "+fulfillDetailMap.keySet());
		for(BigDecimal tempKey:fulfillDetailMap.keySet())
		{
		    tempList=fulfillDetailMap.get(tempKey);
		    for(OmsTempCoFo omsTemp:tempList)
		    {
			if(omsTemp.getRmsResponseCode()!=null&&!"".equals(omsTemp.getRmsResponseCode()))
			{
			    log.info("tempKey "+tempKey);
			    log.info("omsCustOrdNo "+omsCustOrdNo+"tempList.size() inside newFulfillDetailMap "+
				     tempList.size());
			    log.info("omsCustOrdNo "+omsCustOrdNo+"Fulfill order No in tempList loop"+
				     omsTemp.getFulfillOrderNo());
			    log.info("omsCustOrdNo "+omsCustOrdNo+"omsCustOrdNo "+omsTemp.getOmsCustOrdNo());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" lineNo= "+omsTemp.getLineNo());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" item= "+omsTemp.getItem());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" Quantity Ordered= "+omsTemp.getOrderQty());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfilmetn OrderNo="+omsTemp.getFulfillOrderNo());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" source loc id= "+omsTemp.getSourceLocId());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfill loc id= "+omsTemp.getFulfillLocId());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" Quantity Confirmed= "+omsTemp.getFoConfQty());
			    log.info("omsCustOrdNo "+omsCustOrdNo+" RmsResponseCode="+omsTemp.getRmsResponseCode());
			    responseStatus=omsTemp.getRmsResponseCode();
			}
			log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		    }


		}

	    } //for loop end


	    //List<OmsCustOrdReserve> custOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
	    OmsCoFulfillDetail omsCoFulfillDetail=null;
	    log.info("persisting into OmsCoFulfillDetail ");
	    for(BigDecimal tempKey:fulfillDetailMap.keySet())
	    {
		OmsRtlogPublishLog omsRtlogPublishLog=new OmsRtlogPublishLog();
		tempList=fulfillDetailMap.get(tempKey);
		log.info("tempKey "+tempKey);
		for(OmsTempCoFo omsTemp:tempList)
		{
		    if(omsTemp.getOrderQty().intValue()>0&&omsTemp.getFoConfQty().intValue()>0)
		    {
			omsCoFulfillDetail=new OmsCoFulfillDetail();
			log.info("omsCustOrdNo "+omsCustOrdNo+" tempList.size() "+tempList.size());
			log.info("omsCustOrdNo "+omsCustOrdNo+" Fulfill Order No"+omsTemp.getFulfillOrderNo());
			log.info("omsCustOrdNo "+omsCustOrdNo+" Line no "+omsTemp.getLineNo());
			log.info("omsCustOrdNo "+omsCustOrdNo+" Item "+omsTemp.getItem());
			log.info("omsCustOrdNo "+omsCustOrdNo+" Source Loc Type "+omsTemp.getSourceLocationType());
			log.info("omsCustOrdNo "+omsCustOrdNo+" Source Loc "+omsTemp.getSourceLocId());
			log.info("omsCustOrdNo "+omsCustOrdNo+" FulFil Loc Type "+omsTemp.getFulfillLocationType());
			log.info("omsCustOrdNo "+omsCustOrdNo+" FulFil Loc Id "+omsTemp.getFulfillLocId());
			log.info("omsCustOrdNo "+omsCustOrdNo+" orderedQty "+omsTemp.getOrderQty());
			log.info("omsCustOrdNo "+omsCustOrdNo+" ConfQty "+omsTemp.getFoConfQty());
			omsCoFulfillDetail.setFulfillOrderNo(omsTemp.getFulfillOrderNo());
			omsCoFulfillDetail.setFulfillLocType(omsTemp.getFulfillLocationType());
			omsCoFulfillDetail.setFulfillLoc(omsTemp.getFulfillLocId());
			omsCoFulfillDetail.setItem(omsTemp.getItem());
			omsCoFulfillDetail.setLineNo(omsTemp.getLineNo());
			omsCoFulfillDetail.setOmsCustOrdNo(omsCustOrdNo);
			omsCoFulfillDetail.setSourceLoc(omsTemp.getSourceLocId());
			omsCoFulfillDetail.setCombinationId(omsTemp.getCombinationId());
			omsCoFulfillDetail.setSourceLocType(omsTemp.getSourceLocationType());
			omsCoFulfillDetail.setFulfillReqQty(omsTemp.getOrderQty());
			omsCoFulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
			omsCoFulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
			omsCoFulfillDetail.setFulfillConfQty(omsTemp.getFoConfQty());
			omsCoFulfillDetail.setFulfillStatus("C");
			omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
			OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			String extCustOrdNo=omsCustOrdHead.getCustOrderNo();
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
				    extCustOrdNo=
		     omsCustOrdHead.getCustOrderNo().trim()+"-0"+omsCustOrdHead.getSubCustOrderNo().trim();
				}
				else
				{
				    extCustOrdNo=
		     omsCustOrdHead.getCustOrderNo().trim()+"-00"+omsCustOrdHead.getSubCustOrderNo().trim();
				}
			    }
			}
			try
			{

			    BigDecimal tsfNo=null;
			    log.info("omsCustOrdNo "+omsCustOrdNo+"fetching the TsfNo for "+" FulfillOrderNo "+
				     omsCoFulfillDetail.getFulfillOrderNo()+"item "+omsCoFulfillDetail.getItem()+"lineNo "+
				     omsCoFulfillDetail.getLineNo());
			    tsfNo=session.getOrdcustFindByFulfilOrdNo(extCustOrdNo,omsCoFulfillDetail.getFulfillOrderNo().toString(),omsCoFulfillDetail.getSourceLoc(),
							 omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo();
			    log.info("omsCustOrdNo "+omsCustOrdNo+"tsfNo "+tsfNo);
			    if(tsfNo!=null&&"ST".equals(omsCoFulfillDetail.getSourceLocType()))
			    {
				omsCoFulfillDetail.setTsfNo(tsfNo);
				try
				{
				    String status=oMSUtilCommons.approveTransfers(tsfNo,omsTemp.getSourceLocId());
				    if(status.equals("A"))
				    {
					transferList.add(tsfNo);
					omsCoFulfillDetail.setTsfApprovalStatus("A");
					log.info("Transfer approved succesfully");
				    }
				    else
				    {
					if(omsTemp.getSourceLocationType().equals("ST"))
					{
					    if(!transferList.toString().contains(tsfNo.toString()))
					    {
						OmsUnapprovedTransfers omsUnapprovedTransfers=new OmsUnapprovedTransfers();
						omsUnapprovedTransfers.setTsfNo(tsfNo);
						omsUnapprovedTransfers.setItem(omsTemp.getItem());
						omsUnapprovedTransfers.setLocation(omsTemp.getSourceLocId());
						omsUnapprovedTransfers.setUnapprovedQty((omsTemp.getOrderQty()));
						omsUnapprovedTransfers.setOmsCustOrdNo(omsCustOrdNo);
						omsUnapprovedTransfers.setCreateDatetime(new Timestamp(new Date().getTime()));
						session.persistOmsUnapprovedTransfers(omsUnapprovedTransfers);
						log.info("omsCustOrdNo"+omsCustOrdNo+"Persisting into OmsUnapprovedTransfers for item ="+omsTemp.getItem()+
							 " at location "+omsTemp.getSourceLocId()+" with qty="+
							 omsUnapprovedTransfers.getUnapprovedQty());
					    }
					}
				    }
				}
				catch(Exception e)
				{
				    log.error("Failed in approving transfer");
				}

			    }
			    else if(tsfNo!=null)
			    {
				omsCoFulfillDetail.setTsfApprovalStatus("A");
				omsCoFulfillDetail.setTsfNo(tsfNo);
			    }

			}
			catch(Exception e)
			{
			    log.info("Inside Catch Block : "+e.getMessage());
			}
			try
			{
			    session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
			}
			catch(Exception e)
			{
			    String errString=
      OMSUtilCommons.formErrorDescription("ERR_UNHANDLED_EXCEPTION","1",new String[]{ });
			    log.error("-->Failed in persisting in oms_co_fulfil_detail table.");
			    log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+
				     fulfillDetailMap.keySet()+"----------------->");
			    log.info("calling rollback for : "+input.getCustomerOrderNo());
			    oMSCustomerOrderBean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());
			    return fulfillDetailMap;
			    //throw new SOAPException(errString);
			}


		    }
		    List<OmsCustOrdItem> omsCustOrdItemList=session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
		    String shipingChargeDept=
				      session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT","OMS_SYSTEM_OPTION");

		    log.info("***End : processSadad");
		}
	    } //end of for loop

	    //delete the approved transfer from unapproved transfer table
	    if(transferList!=null&&transferList.size()>0)
	    {
		oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList,omsCustOrdNo);
	    }

	}
	return fulfillDetailMap;
    }


    public XResponseProcessingObj processXResponse(BigDecimal omsCustOrdNo,ArrayList<OmsTempCoFo> tempList,
						   int maxFulfilOrderNo,ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap,
						   TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> newFulfillMap,
						   CoPaymentConf input) throws EntityAlreadyExistsWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
									       SOAPException, BusinessException
    {
	log.info("inside processXResponse");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	XResponseProcessingObj xResponseProcessingObj=new XResponseProcessingObj();
	ProcessedObject processedObject=null;
	InterfacePersistence interfacePersistence=new InterfacePersistence();
	String responseStatus="";
	BigDecimal combinationId=null;

	Map<BigDecimal,ArrayList<OmsTempCoFo>> sohMap=new HashMap<BigDecimal,ArrayList<OmsTempCoFo>>();
	ReturnPartialResponseObject returnPartialResponse=new ReturnPartialResponseObject();
	BigDecimal virtualWH=BigDecimal.ZERO;
	try
	{
	    log.info("tempList.size() for Xresponse"+tempList.size());
	    String storeMapValue=null;
	    String srcLocfulfiLoc=null;
	    TreeMap<BigDecimal,String> storeFulFillMap=new TreeMap<BigDecimal,String>();
	    for(BigDecimal mapkey:fulfillDetailMap.keySet())
	    {
		tempList=fulfillDetailMap.get(mapkey);
		log.info("mapkey "+mapkey);
		for(OmsTempCoFo omsTempCoFo:tempList)
		{
		    log.info("tempList.size()"+tempList.size());
		    log.info("checking whether the omsTempCoFo.getRmsResponseCode() is not equal to null,C and P");
		    log.info("omsTempCoFo.getRmsResponseCode() "+omsTempCoFo.getRmsResponseCode());
		    if(omsTempCoFo.getRmsResponseCode()!=null&&omsTempCoFo.getRmsResponseCode().equals("X"))
		    {


			log.info("tempCoFo.getLineNo()"+omsTempCoFo.getLineNo());
			log.info("tempCoFo.getItem() "+omsTempCoFo.getItem());
			log.info("tempCoFo.getFulfillOrderNo() "+omsTempCoFo.getFulfillOrderNo());
			log.info("tempCoFo.getRmsResponseCode() "+omsTempCoFo.getRmsResponseCode());
			log.info("tempCoFo.getOrderQty() "+omsTempCoFo.getOrderQty());
			log.info("tempCoFo.getSourceLocId() "+omsTempCoFo.getSourceLocId());
			log.info("tempCoFo.getSourceLocationType() "+omsTempCoFo.getSourceLocationType());
			log.info("tempCoFo.getFulfillLocId() "+omsTempCoFo.getFulfillLocId());
			log.info("tempCoFo.getFulfillLocationType() "+omsTempCoFo.getFulfillLocationType());
			log.info("omsTempCoFo.getCombinationId() "+omsTempCoFo.getCombinationId());
			String item=omsTempCoFo.getItem();
			BigDecimal lineNo=omsTempCoFo.getLineNo();

			long SOH=0;
			long pendingQty=0;
			long availQty=0;
			long orderQty=0;
			log.info("item "+item);
			log.info("lineNo "+lineNo);
			log.info("omsCustOrdNo "+omsCustOrdNo);
			orderQty=omsTempCoFo.getOrderQty().intValue();
			PartialResponse PartialResponse=new PartialResponse();
			OmsFulfillMatrixExtDetail matrixDetail=null;
			combinationId=omsTempCoFo.getCombinationId();
			log.info("combinationId "+combinationId);
			log.info("omsTempCoFo.getSourceLocId() "+omsTempCoFo.getSourceLocId());
			log.info("omsTempCoFo.getVirtualWH() "+omsTempCoFo.getVirtualWH());
			log.info("omsTempCoFo.getSourceLocationType()"+omsTempCoFo.getSourceLocationType());
			if(omsTempCoFo.getSourceLocationType().equals("WH"))
			{

			    virtualWH=omsTempCoFo.getVirtualWH();
			    matrixDetail=session.getOmsFulfillMatrixExtDetailFindPriority(combinationId,virtualWH);
			}
			else
			{
			    matrixDetail=
	      session.getOmsFulfillMatrixExtDetailFindPriority(combinationId,omsTempCoFo.getSourceLocId());

			}
			FindNextfulfillLoc findNextfulfillLoc=new FindNextfulfillLoc();
			int priority=matrixDetail.getPriority().add(BigDecimal.ONE).intValue();
			log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
			log.info("omsTempCoFo.getItem() "+omsTempCoFo.getItem());

			// Changes are made for the fix 2473 bug, orderQty!=0 is removed and used orderQty>0
			while(orderQty>0)
			{
			    log.info("priority "+priority);
			    log.info("omsCustOrdNo "+omsCustOrdNo+"Finding next location with combination id "+combinationId+
				     "and priority "+(priority));
			    matrixDetail=findNextfulfillLoc.processFulfillmentMatrix(combinationId,priority);
			    BigDecimal sourceLocId=matrixDetail.getLocation();
			    BigDecimal fulfillLocId=matrixDetail.getDeliveryFromLoc();
			    String srcLocType=matrixDetail.getLocationType();
			    String fulFillLocType=matrixDetail.getDeliveryFromLocType();
			    if(matrixDetail.getLocationType().equals("ST"))
			    {
				//Find SOH from SIM
				log.info("omsCustOrdNo "+omsCustOrdNo+"Calling SIM webservice for finding SOH for item ="+omsTempCoFo.getItem()+
					 " in store="+sourceLocId);

				try
				{
				    SOH=interfacePersistence.callSIMStoreInventory(item,sourceLocId);
				    returnPartialResponse=
	    PartialResponse.STLocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				       lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,omsTempCoFo.getOrderQty(),
				       srcLocfulfiLoc,storeMapValue,storeFulFillMap);
				    orderQty=returnPartialResponse.getOrderQty();
				    maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
				    sohMap=returnPartialResponse.getSohMap();
				    pendingQty=returnPartialResponse.getPendingQty();
				    omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
				    storeFulFillMap=returnPartialResponse.getStoreFulFillMap();
				    log.info("=========returnPartialResponse objects for ST=================");
				    log.info("orderQty "+orderQty);
				    log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
				    log.info("pendingQty "+pendingQty);
				    log.info("sohMap "+sohMap.keySet());
				    log.info("storeFulFillMap "+storeFulFillMap.keySet());
				    log.info("=========returnPartialResponse objects for ST=================");
				}
				catch(Exception e)
				{
				    log.info("<----------fulfillDetailMap-- "+fulfillDetailMap.keySet());
				    return xResponseProcessingObj;
				    // throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
				}
			    }

			    if(matrixDetail.getLocationType().equals("WH"))
			    {
				log.info("omsCustOrdNo "+omsCustOrdNo+"inside WH ");
				log.info("omsCustOrdNo "+omsCustOrdNo+
					 "Started the code for Virtual Warehouse scenario");
				List<Object[]> tempWhObject=session.getWhFindPhysicalWH(matrixDetail.getLocation());
				BigDecimal physicalWH=BigDecimal.ZERO;
				BigDecimal channelId=BigDecimal.ZERO;
				for(Object[] result:tempWhObject)
				{
				    log.info("omsCustOrdNo "+omsCustOrdNo+"inside loop");
				    log.info("omsCustOrdNo "+omsCustOrdNo+"WH="+result[0]);
				    physicalWH=new BigDecimal(result[0].toString());
				    channelId=new BigDecimal(result[1].toString());
				    log.info("omsCustOrdNo "+omsCustOrdNo+"channel id="+result[1]);
				}
				sourceLocId=physicalWH;
				log.info("omsCustOrdNo "+omsCustOrdNo+"sourceLocId "+sourceLocId);
				fulfillLocId=matrixDetail.getDeliveryFromLoc();
				virtualWH=matrixDetail.getLocation();
				log.info("omsCustOrdNo "+omsCustOrdNo+"fulfillLocId for WH "+fulfillLocId);
				fulFillLocType=matrixDetail.getDeliveryFromLocType();
				log.info("omsCustOrdNo "+omsCustOrdNo+"fulFillLocType "+fulFillLocType);
				log.info("omsCustOrdNo "+omsCustOrdNo+" physicalWH "+physicalWH);
				log.info("omsCustOrdNo "+omsCustOrdNo+" channelId "+channelId);
				List<BigDecimal> locList=session.getWhFindVirtualWh(physicalWH,channelId);
				int i=0;
				while(i<locList.size())
				{
				    log.info("omsCustOrdNo "+omsCustOrdNo+locList.get(i));
				    i++;
				}
				OMSUtilCommons omsUtilCommons=new OMSUtilCommons();
				// adding sleep as DAS and OMS not in sync, P/X
				try
				{
				    Thread.sleep(5000);
				}
				catch(Exception e)
				{
				    log.info("Error ocured while thread is sleeping");

				}
		           String applicationId = "E-COMMERCE";
				SOH=omsUtilCommons.checkSOHForWH(omsTempCoFo.getItem(),locList, applicationId).longValue();
				returnPartialResponse=
	    PartialResponse.WHLocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				       lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,
				       omsTempCoFo.getOrderQty());
				orderQty=returnPartialResponse.getOrderQty();
				maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
				sohMap=returnPartialResponse.getSohMap();
				pendingQty=returnPartialResponse.getPendingQty();
				omsTempCoFo.setOrderQty(new BigDecimal(pendingQty)); // removing commnent as fix for P/X
				log.info("=========returnPartialResponse objects for WH=================");
				log.info("orderQty "+orderQty);
				log.info("pendingQty "+pendingQty);
				log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
				log.info("sohMap "+sohMap.keySet());
				log.info("=========returnPartialResponse objects for WH=================");
			    }

			    if(matrixDetail.getLocationType().equals("SU"))
			    {

				CheckItemLocSOH checkItemLocSOH=new CheckItemLocSOH();
				String item_status=checkItemLocSOH.findItemStatus(omsTempCoFo.getItem(),fulfillLocId);
				if(item_status.equals("A")==false)
				{
				    log.info("omsCustOrdNo "+omsCustOrdNo+" inside item_status false condition");
				    //OmsCustOrdHead omsCustOrdHead =
				    // session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				    // omsCustOrdHead.setStatus("F");
				    // session.mergeOmsCustOrdHead(omsCustOrdHead);
				    //throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
				    return xResponseProcessingObj;
				    //break;

				}
				//sourceLocId = new BigDecimal(checkItemLocSOH.findSupplier(omsTempCoFo.getItem(), "Y"));
				log.info(" Delivery From location type value is"+
					 matrixDetail.getDeliveryFromLocType());
				if("S".equals(matrixDetail.getDeliveryFromLocType()))
				{
				    log.info(" Calling getPrimarySupplierFromItemLocation method by passing item= "+omsTempCoFo.getItem()+
					     "... and location is..="+matrixDetail.getDeliveryFromLoc().longValue());
				    sourceLocId=
		      checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(),matrixDetail.getDeliveryFromLoc().longValue());
				}
				else
				{
				    // calling getorderRequestorId method  returns orderRequestorId by passing csutomerOrderNo
				    log.info("Value of customerOrderNo"+input.getCustomerOrderNo());
				    BigDecimal OrderRequestorId=getorderRequestorId(input.getCustomerOrderNo());
				    log.info(" Calling getPrimarySupplierFromItemLocation method by passing item= "+omsTempCoFo.getItem()+
					     "... and location is..="+OrderRequestorId.longValue());
				    sourceLocId=
		      checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(),OrderRequestorId.longValue());
				}
				log.info("Calling checkDirectShipIndicatoryofaGivenSupplier  method by passing item ="+omsTempCoFo.getItem()+
					 "... and location is...="+sourceLocId.longValue());
				Boolean flag=
		 checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(omsTempCoFo.getItem(),sourceLocId.longValue());
				log.info("Value of flag returned by checkDirectShipIndicatoryofaGivenSupplier method is"+
					 flag);
				log.info("sourceLocId "+sourceLocId);
				log.info("omsCustOrdNo "+omsCustOrdNo+"supplier value from findSupplier method "+
					 sourceLocId);
				if(flag==Boolean.FALSE)
				{
				    //  OmsCustOrdHead omsCustOrdHead =session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				    // omsCustOrdHead.setStatus("F");
				    //  session.mergeOmsCustOrdHead(omsCustOrdHead);
				    //throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SRC_LOC_ID==0"));
				    return xResponseProcessingObj;
				}
				if(session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId))!=
				   0)
				{
				    log.info("omsCustOrdNo "+omsCustOrdNo+"Org unit not matched");

				    //OmsCustOrdHead omsCustOrdHead =session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				    //omsCustOrdHead.setStatus("F");
				    //   session.mergeOmsCustOrdHead(omsCustOrdHead);
				    //throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
				    return xResponseProcessingObj;
				}
				SOH=orderQty;
				log.info("========SOH========= in supplier "+SOH);
				availQty=SOH;
				returnPartialResponse=
	    PartialResponse.SULocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				       lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,omsTempCoFo.getOrderQty(),
				       srcLocfulfiLoc,storeMapValue,storeFulFillMap);
				orderQty=returnPartialResponse.getOrderQty();
				maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
				sohMap=returnPartialResponse.getSohMap();
				pendingQty=returnPartialResponse.getPendingQty();
				omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
				log.info("=========returnPartialResponse objects for SU=================");
				log.info("orderQty "+orderQty);
				log.info("pendingQty "+pendingQty);
				log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
				log.info("sohMap "+sohMap.keySet());
				log.info("=========returnPartialResponse objects for SU=================");
			    }

			    newFulfillMap=createFulfillDetailMap(sohMap,maxFulfilOrderNo);
			    log.info("newFulfillMap.keySet() "+newFulfillMap.keySet());
			    if(orderQty==0)
			    {
				log.info("Removing FulfillOrderNo from Map "+omsTempCoFo.getFulfillOrderNo());
				fulfillDetailMap.remove(omsTempCoFo.getFulfillOrderNo());
				break;
			    }
			    priority++;
			}

		    }
		    else
		    {
			log.info("Doesnot contain X");
		    }
		}
	    }
	    log.info("newFulfillMap "+newFulfillMap.keySet());
	    for(BigDecimal key:newFulfillMap.keySet())
	    {
		log.info("newFulfillMap.keySet() "+newFulfillMap.keySet());
		log.info("key "+key);
		tempList=newFulfillMap.get(key);
		log.info("tempList.size()"+tempList.size());
		log.info("omsCustOrdNo "+omsCustOrdNo+
			 "after getting X response and checking for next location calling the callWebservices method");
		try
		{
		    log.info("tempList.get(0).getRmsResponseCode() "+tempList.get(0).getRmsResponseCode());
		    if(tempList.get(0).getRmsResponseCode()==null)
		    {
			log.info("Calling CallWebservices at line no 1168");
			processedObject=interfacePersistence.callWebservices(omsCustOrdNo,tempList);
			fulfilOrdCfmCol=processedObject.getFulfilOrdCfmCol();
			currentFulfilOrderNo=processedObject.getCurrentFulFillOrderNo();
			log.info("currentFulfilOrderNo inside xresponse after calling RMS "+currentFulfilOrderNo);
			log.info("omsCustOrdNo "+omsCustOrdNo+"+++++++++++++++++ fulfilOrdCfmCol "+
				 fulfilOrdCfmCol.getCollectionSize());
			tempList=processedObject.getOmsTempCoFoList();
			newFulfillMap.put(key,tempList);
			log.info("omsCustOrdNo "+omsCustOrdNo+"After getting from PResponseProcessingObj fulfilOrdCfmCol "+
				 fulfilOrdCfmCol.getCollectionSize());
			xResponseProcessingObj.setFulfilOrdCfmCol(fulfilOrdCfmCol);
			// xResponseProcessingObj.setNewFulfillMap(newFulfillMap);
			responseStatus=interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol,tempList);
			//code for nested P/X
			if("P".equals(responseStatus))
			{
			    this.pfulfilOrdCfmCols.put(key,fulfilOrdCfmCol);
			}
			else if("X".equals(responseStatus))
			{
			    this.xfulfilOrdCfmCols.put(key,tempList);
			}
			log.info("responseStatus "+responseStatus);
			fulfillDetailMap.put(key,tempList);
			xResponseProcessingObj.setFulfillDetailMap(fulfillDetailMap);
		    }
		}
		catch(Exception e)
		{
		    log.info("exception "+e.getMessage());
		    return xResponseProcessingObj;
		}


	    }

	}
	catch(Exception e)
	{

	    //OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	    //omsCustOrdHead.setStatus("F");
	    //   session.mergeOmsCustOrdHead(omsCustOrdHead);
	    OMSCustomerOrderBean oMSCustomerOrderBean=new OMSCustomerOrderBean();
	    //oMSCustomerOrderBean.rollback(fulfillDetailMap);
	    log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
		     "----------------->");
	    log.info("calling rollback for : "+input.getCustomerOrderNo());
	    oMSCustomerOrderBean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());
	    // throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM ERROR"));
	    return xResponseProcessingObj;
	}


	return xResponseProcessingObj;
    }


    public PResponseProcessingObj processPResponse(BigDecimal omsCustOrdNo,ArrayList<OmsTempCoFo> tempList,
						   FulfilOrdCfmCol fulfilOrdCfmCol,int maxFulfilOrderNo,
						   ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap,
						   CoPaymentConf input) throws EntityAlreadyExistsWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
									       com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
									       SOAPException, BusinessException
    {

	log.info("**************************Begin processPResponse Method******************************************");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	BigDecimal combinationId=null;
	ProcessedObject processedObject=null;
	String responseStatus="";
	InterfacePersistence interfacePersistence=new InterfacePersistence();
	TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> newFulfillMap=null;
	Map<BigDecimal,ArrayList<OmsTempCoFo>> sohMap=new HashMap<BigDecimal,ArrayList<OmsTempCoFo>>();

	PResponseProcessingObj pResponseProcessingObj=new PResponseProcessingObj();
	ReturnPartialResponseObject returnPartialResponse=new ReturnPartialResponseObject();
	BigDecimal virtualWH=BigDecimal.ZERO;
	try
	{
	    log.info("tempList.size() for presponse"+tempList.size());
	    String storeMapValue=null;
	    String srcLocfulfiLoc=null;
	    TreeMap<BigDecimal,String> storeFulFillMap=new TreeMap<BigDecimal,String>();

	    for(OmsTempCoFo omsTempCoFo:tempList)
	    {
		OmsFulfillMatrixExtDetail matrixDetail=null;
		combinationId=omsTempCoFo.getCombinationId();
		log.info("combinationId "+combinationId);
		log.info("omsTempCoFo.getSourceLocId() "+omsTempCoFo.getSourceLocId());
		log.info("omsTempCoFo.getVirtualWH() "+omsTempCoFo.getVirtualWH());
		log.info("omsTempCoFo.getSourceLocationType()"+omsTempCoFo.getSourceLocationType());
		if(omsTempCoFo.getSourceLocationType().equals("WH"))
		{
		    // virtualWH=new BigDecimal(1072);
		    virtualWH=omsTempCoFo.getVirtualWH();
		    matrixDetail=session.getOmsFulfillMatrixExtDetailFindPriority(combinationId,virtualWH);
		}
		else
		{
		    log.info("--------------calling getOmsFulfillMatrixExtDetailFindPriority ** combinationId"+combinationId+
			     "omsTempCoFo.getSourceLocId()"+omsTempCoFo.getSourceLocId());
		    matrixDetail=
       session.getOmsFulfillMatrixExtDetailFindPriority(combinationId,omsTempCoFo.getSourceLocId());

		}
		log.info("1.-----------FindNextfulfillLoc----------------------------------");
		FindNextfulfillLoc findNextfulfillLoc=new FindNextfulfillLoc();
		int priority=matrixDetail.getPriority().add(BigDecimal.ONE).intValue();
		log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
		log.info("omsTempCoFo.getItem() "+omsTempCoFo.getItem());
		log.info("omsTempCoFo.getRmsResponseCode() "+omsTempCoFo.getRmsResponseCode());
		long orderQty=omsTempCoFo.getOrderQty().intValue();
		log.info("2.omsTempCoFo.getRmsResponseCode().equals()");
		if(omsTempCoFo.getRmsResponseCode().equals("P"))
		{

		    // Changes are made for the fix 2473,  orderQty!=0 removed and used orderQty>0
		    while(orderQty>0)
		    {

			log.info("orderQty "+orderQty);
			log.info("priority "+priority);
			log.info("omsCustOrdNo "+omsCustOrdNo+"Finding next location with combination id "+combinationId+"and priority "+
				 (priority));
			matrixDetail=findNextfulfillLoc.processFulfillmentMatrix(combinationId,priority);
			BigDecimal sourceLocId=matrixDetail.getLocation();
			BigDecimal fulfillLocId=matrixDetail.getDeliveryFromLoc();
			String srcLocType=matrixDetail.getLocationType();
			String fulFillLocType=matrixDetail.getDeliveryFromLocType();
			String item=omsTempCoFo.getItem();
			BigDecimal lineNo=omsTempCoFo.getLineNo();

			long SOH=0;
			long pendingQty=0;
			long availQty=0;

			log.info("item "+item);
			log.info("lineNo "+lineNo);
			log.info("omsCustOrdNo "+omsCustOrdNo);

			PartialResponse PartialResponse=new PartialResponse();
			InterfacePersistence interfacePersistece=new InterfacePersistence();
			if(matrixDetail.getLocationType().equals("ST"))
			{
			    //Find SOH from SIM
			    log.info("omsCustOrdNo "+omsCustOrdNo+"Calling SIM webservice for finding SOH for item ="+omsTempCoFo.getItem()+
				     " in store="+sourceLocId);
			    try
			    {
				SOH=interfacePersistece.callSIMStoreInventory(item,sourceLocId);
				returnPartialResponse=
	    PartialResponse.STLocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				       lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,omsTempCoFo.getOrderQty(),
				       srcLocfulfiLoc,storeMapValue,storeFulFillMap);
				orderQty=returnPartialResponse.getOrderQty();
				maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
				sohMap=returnPartialResponse.getSohMap();
				pendingQty=returnPartialResponse.getPendingQty();
				omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
				storeFulFillMap=returnPartialResponse.getStoreFulFillMap();
				log.info("=========returnPartialResponse objects for ST=================");
				log.info("orderQty "+orderQty);
				log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
				log.info("pendingQty "+pendingQty);
				log.info("sohMap "+sohMap.keySet());
				log.info("storeFulFillMap "+storeFulFillMap.keySet());
				log.info("=========returnPartialResponse objects for ST=================");
			    }
			    catch(Exception e)
			    {
				// throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
				return pResponseProcessingObj;
			    }

			}

			if(matrixDetail.getLocationType().equals("WH"))
			{


			    log.info("omsCustOrdNo "+omsCustOrdNo+"inside WH ");
			    log.info("omsCustOrdNo "+omsCustOrdNo+"Started the code for Virtual Warehouse scenario");
			    List<Object[]> tempWhObject=session.getWhFindPhysicalWH(matrixDetail.getLocation());
			    BigDecimal physicalWH=BigDecimal.ZERO;
			    BigDecimal channelId=BigDecimal.ZERO;
			    for(Object[] result:tempWhObject)
			    {
				log.info("omsCustOrdNo "+omsCustOrdNo+"inside loop");
				log.info("omsCustOrdNo "+omsCustOrdNo+"WH="+result[0]);
				physicalWH=new BigDecimal(result[0].toString());
				channelId=new BigDecimal(result[1].toString());
				log.info("omsCustOrdNo "+omsCustOrdNo+"channel id="+result[1]);
			    }
			    sourceLocId=physicalWH;
			    log.info("omsCustOrdNo "+omsCustOrdNo+"sourceLocId "+sourceLocId);
			    fulfillLocId=matrixDetail.getDeliveryFromLoc();
			    virtualWH=matrixDetail.getLocation();
			    log.info("omsCustOrdNo "+omsCustOrdNo+"fulfillLocId for WH "+fulfillLocId);
			    fulFillLocType=matrixDetail.getDeliveryFromLocType();
			    log.info("omsCustOrdNo "+omsCustOrdNo+"fulFillLocType "+fulFillLocType);
			    log.info("omsCustOrdNo "+omsCustOrdNo+" physicalWH "+physicalWH);
			    log.info("omsCustOrdNo "+omsCustOrdNo+" channelId "+channelId);
			    List<BigDecimal> locList=session.getWhFindVirtualWh(physicalWH,channelId);
			    int i=0;
			    while(i<locList.size())
			    {
				log.info("omsCustOrdNo "+omsCustOrdNo+locList.get(i));
				i++;
			    }
			    OMSUtilCommons omsUtilCommons=new OMSUtilCommons();
			    //Need to add sleep DAs and RMS not in sync
			    try
			    {
				Thread.sleep(5000);
			    }
			    catch(Exception e)
			    {
				log.info("Error occured while thread sleep");

			    }
		           String applicationId = "E-COMMERCE";
			    SOH=omsUtilCommons.checkSOHForWH(omsTempCoFo.getItem(),locList, applicationId).longValue();
			    returnPartialResponse=
     PartialResponse.WHLocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,
				omsTempCoFo.getOrderQty());
			    orderQty=returnPartialResponse.getOrderQty();
			    maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
			    sohMap=returnPartialResponse.getSohMap();
			    pendingQty=returnPartialResponse.getPendingQty();
			    omsTempCoFo.setOrderQty(new BigDecimal(pendingQty)); // removing comment P/X
			    log.info("=========returnPartialResponse objects for WH=================");
			    log.info("orderQty "+orderQty);
			    log.info("pendingQty "+pendingQty);
			    log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
			    log.info("sohMap "+sohMap.keySet());
			    log.info("=========returnPartialResponse objects for WH=================");

			}


			log.info("------3.matrixDetail.getLocationType().equals()-------------------");
			if(matrixDetail.getLocationType().equals("SU"))
			{

			    log.info("orderQty in supplier "+orderQty);
			    CheckItemLocSOH checkItemLocSOH=new CheckItemLocSOH();
			    String item_status=checkItemLocSOH.findItemStatus(omsTempCoFo.getItem(),fulfillLocId);
			    if(item_status.equals("A")==false)
			    {
				log.info("omsCustOrdNo "+omsCustOrdNo+" inside item_status false condition");
				// OmsCustOrdHead omsCustOrdHead =session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				//omsCustOrdHead.setStatus("F");
				//     session.mergeOmsCustOrdHead(omsCustOrdHead);
				//   throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
				return pResponseProcessingObj;
				//break;

			    }
			    //sourceLocId = new BigDecimal(checkItemLocSOH.findSupplier(omsTempCoFo.getItem(), "Y"));
			    log.info(" Delivery From location type value is"+matrixDetail.getDeliveryFromLocType());
			    if("S".equals(matrixDetail.getDeliveryFromLocType()))
			    {
				log.info(" Calling getPrimarySupplierFromItemLocation method by passing item= "+omsTempCoFo.getItem()+
					 "... and location is..="+matrixDetail.getDeliveryFromLoc().longValue());
				sourceLocId=
		      checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(),matrixDetail.getDeliveryFromLoc().longValue());
			    }
			    else
			    {

				// calling getorderRequestorId method  returns orderRequestorId by passing csutomerOrderNo
				log.info("Value of customerOrderNo....."+input.getCustomerOrderNo());
				BigDecimal OrderRequestorId=getorderRequestorId(input.getCustomerOrderNo());
				log.info(" Calling getPrimarySupplierFromItemLocation method by passing item= "+omsTempCoFo.getItem()+
					 "... and location is..="+OrderRequestorId.longValue());
				sourceLocId=
		      checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(),OrderRequestorId.longValue());
			    }
			    log.info("Calling checkDirectShipIndicatoryofaGivenSupplier  method by passing item ="+omsTempCoFo.getItem()+
				     "... and location is="+sourceLocId.longValue());
			    Boolean flag=
	  checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(omsTempCoFo.getItem(),sourceLocId.longValue());
			    log.info("Value of flag returned by checkDirectShipIndicatoryofaGivenSupplier method is"+
				     flag);
			    log.info("sourceLocId "+sourceLocId);
			    log.info("omsCustOrdNo "+omsCustOrdNo+"supplier value from findSupplier method "+
				     sourceLocId);
			    if(flag==Boolean.FALSE)
			    {
				// OmsCustOrdHead omsCustOrdHead =session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				//omsCustOrdHead.setStatus("F");
				//    session.mergeOmsCustOrdHead(omsCustOrdHead);
				//throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SRC_LOC_ID==0"));
				return pResponseProcessingObj;
			    }
			    if(session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId))!=
			       0)
			    {
				log.info("omsCustOrdNo "+omsCustOrdNo+"Org unit not matched");

				// OmsCustOrdHead omsCustOrdHead =session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				//omsCustOrdHead.setStatus("F");
				//   session.mergeOmsCustOrdHead(omsCustOrdHead);
				//throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
				return pResponseProcessingObj;
			    }

			    SOH=orderQty;
			    log.info("========SOH========= in supplier "+SOH);
			    availQty=SOH;
			    returnPartialResponse=
     PartialResponse.SULocation(item,sourceLocId,availQty,pendingQty,omsCustOrdNo,SOH,orderQty,maxFulfilOrderNo,sohMap,
				lineNo,combinationId,fulFillLocType,srcLocType,fulfillLocId,virtualWH,omsTempCoFo.getOrderQty(),srcLocfulfiLoc,
				storeMapValue,storeFulFillMap);
			    orderQty=returnPartialResponse.getOrderQty();
			    maxFulfilOrderNo=returnPartialResponse.getMaxFulfilOrderNo();
			    sohMap=returnPartialResponse.getSohMap();
			    pendingQty=returnPartialResponse.getPendingQty();
			    omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
			    log.info("=========returnPartialResponse objects for SU=================");
			    log.info("orderQty "+orderQty);
			    log.info("pendingQty "+pendingQty);
			    log.info("maxFulfilOrderNo "+maxFulfilOrderNo);
			    log.info("sohMap "+sohMap.keySet());
			    log.info("=========returnPartialResponse objects for SU=================");
			}


			//  SourceLocIdentify sourceLocIdentify=new SourceLocIdentify();
			newFulfillMap=createFulfillDetailMap(sohMap,maxFulfilOrderNo);
			log.info("newFulfillMap.keySet() "+newFulfillMap.keySet());
			if(orderQty==0)
			{
			    break;
			}
			priority++;
		    }

		}
	    }
	    log.info("newFulfillMap "+newFulfillMap.keySet());
	    for(BigDecimal key:newFulfillMap.keySet())
	    {
		log.info("newFulfillMap.keySet() "+newFulfillMap.keySet());
		log.info("key "+key);
		tempList=newFulfillMap.get(key);
		log.info("tempList.size()"+tempList.size());
		log.info("omsCustOrdNo "+omsCustOrdNo+
			 "after getting P response and checking for next location calling the callWebservices method");
		try
		{
		    log.info("Calling CallWebservices at line no 1506");
		    processedObject=interfacePersistence.callWebservices(omsCustOrdNo,tempList);
		}
		catch(Exception e)
		{
		    log.info("exception "+e.getMessage());
		    return pResponseProcessingObj;
		}
		this.fulfilOrdCfmCol=processedObject.getFulfilOrdCfmCol(); //P/X
		currentFulfilOrderNo=processedObject.getCurrentFulFillOrderNo();
		log.info("currentFulfilOrderNo inside presponse "+currentFulfilOrderNo);
		log.info("omsCustOrdNo "+omsCustOrdNo+"+++++++++++++++++ fulfilOrdCfmCol "+
			 this.fulfilOrdCfmCol.getCollectionSize());
		tempList=processedObject.getOmsTempCoFoList();
		newFulfillMap.put(key,tempList);
		log.info("omsCustOrdNo "+omsCustOrdNo+"After getting from PResponseProcessingObj fulfilOrdCfmCol "+
			 this.fulfilOrdCfmCol.getCollectionSize());
		pResponseProcessingObj.setFulfilOrdCfmCol(this.fulfilOrdCfmCol);
		pResponseProcessingObj.setNewFulfillMap(newFulfillMap);
		responseStatus=interfacePersistence.processWebserviceResponse(this.fulfilOrdCfmCol,tempList);

		// add code for nested P and X
		if("P".equals(responseStatus))
		{
		    this.pfulfilOrdCfmCols.put(key,this.fulfilOrdCfmCol);
		    //this.pfulfilOrdCfmCol=this.fulfilOrdCfmCol;
		}
		else if("X".equals(responseStatus))
		{
		    this.xfulfilOrdCfmCols.put(key,tempList);
		}
		log.info("responseStatus "+responseStatus);
	    }

	}
	catch(Exception e)
	{

	    // OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	    //omsCustOrdHead.setStatus("F");
	    //     session.mergeOmsCustOrdHead(omsCustOrdHead);
	    OMSCustomerOrderBean oMSCustomerOrderBean=new OMSCustomerOrderBean();
	    //oMSCustomerOrderBean.rollback(fulfillDetailMap);
	    log.info("<------------------For omsCustOrdNo :"+omsCustOrdNo+"the keyset contains : "+fulfillDetailMap.keySet()+
		     "----------------->");
	    log.info("calling rollback for : "+input.getCustomerOrderNo());
	    oMSCustomerOrderBean.rollbackForTimeout(omsCustOrdNo,input.getCustomerOrderNo());
	    //throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM ERROR"));
	    return pResponseProcessingObj;
	}
	return pResponseProcessingObj;
    }


    public OmsTempCoFo convertOmsCustOrdResvToOmsCustOrdTemp(OmsCustOrdReserve omsCustOrdReserve)

    {
	OmsTempCoFo omsTempCoFo=new OmsTempCoFo();
	if(omsCustOrdReserve.getQty().intValue()>0)
	{
	    omsTempCoFo.setCombinationId(omsCustOrdReserve.getCombinationId());
	    omsTempCoFo.setCreateDatetime(omsCustOrdReserve.getCreateDatetime());
	    omsTempCoFo.setFoConfQty(omsCustOrdReserve.getRmsResvQty());
	    omsTempCoFo.setFulfillLocId(omsCustOrdReserve.getLoc());
	    omsTempCoFo.setFulfillLocationType(omsCustOrdReserve.getLocType());
	    omsTempCoFo.setItem(omsCustOrdReserve.getItem());
	    omsTempCoFo.setLineNo(omsCustOrdReserve.getLineNo());
	    omsTempCoFo.setOmsCustOrdNo(omsCustOrdReserve.getOmsCustOrdNo());
	    omsTempCoFo.setOrderQty(omsCustOrdReserve.getQty());
	    omsTempCoFo.setSourceLocId(omsCustOrdReserve.getRmsResvLoc());
	    omsTempCoFo.setFulfillOrderNo(omsCustOrdReserve.getFulfillOrderNo());
	    omsTempCoFo.setSourceLocationType(omsCustOrdReserve.getRmsResvLocType());
	    if(omsCustOrdReserve.getRmsResvLocType().equals("WH"))
	    {
		omsTempCoFo.setVirtualWH(omsCustOrdReserve.getVirtualWH());
	    }
	    else
	    {
		omsTempCoFo.setVirtualWH(BigDecimal.ZERO);
	    }
	}
	return omsTempCoFo;
    }

    public TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> createFulfillDetailMap(Map<BigDecimal,ArrayList<OmsTempCoFo>> sohMap,
									     int fulfillOrderNo)
    {
	TreeMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap=new TreeMap<BigDecimal,ArrayList<OmsTempCoFo>>();

	for(BigDecimal key:sohMap.keySet())
	{
	    log.info("sohMap.keySet()"+sohMap.keySet());
	    ArrayList<OmsTempCoFo> list=sohMap.get(key);
	    fulfillDetailMap.put(key,list);
	    log.info("after putting into fulfillDetailMap the list objetcs");
	    for(OmsTempCoFo temp:list)
	    {
		log.info("Fulfilmetn OrderNo="+temp.getFulfillOrderNo()+"for item="+temp.getItem());
		log.info("temp.getOrderQty() "+temp.getOrderQty());
		log.info("temp.confQty "+temp.getFoConfQty());
		log.info("srcLoc "+temp.getSourceLocId());
		log.info("srcLocType "+temp.getSourceLocationType());
		log.info("fulllictype "+temp.getFulfillLocationType());
		log.info("fullfillocId "+temp.getFulfillLocId());
	    }


	}
	return fulfillDetailMap;
    }

    private BigDecimal getorderRequestorId(String custOrderNo) throws SOAPException
    {
	log.info(" Begin Of getorderRequestorId method ");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	log.info("Establish of DataBase Connection Successfull");
	OmsCustOrdHead OmsCustOrdHeadrow=null;
	log.info(" Calling getOmsCustOrdHeadFindColumns method by passing "+custOrderNo);
	List<OmsCustOrdHead> omscustomerOrderHeadResult=session.getOmsCustOrdHeadFindColumns(custOrderNo);
	if(null!=omscustomerOrderHeadResult)
	{
	    Iterator omscustomerOrderHeadIterator=omscustomerOrderHeadResult.iterator();
	    if(omscustomerOrderHeadIterator.hasNext())
	    {
		OmsCustOrdHeadrow=(OmsCustOrdHead)omscustomerOrderHeadIterator.next();
	    }
	}
	log.info("Value of Order Requestor Id......."+OmsCustOrdHeadrow.getOrderRequestorId());
	log.info("End of getorderRequestorId method");
	return OmsCustOrdHeadrow.getOrderRequestorId();
    }

    // added as part P/X ,add it to OMSUtil

    private BigDecimal getUnProceesedPResponsekey(ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap)
    {
	log.info("<-------------------------Begin of getUnProceesedPResponsekey method------------------>");
	BigDecimal unProcessedPResponsekey=null;
	ArrayList<OmsTempCoFo> tempList=null;
	for(BigDecimal tempKey:fulfillDetailMap.keySet())
	{
	    log.info("<-------------- temp key ------------>"+tempKey+"<---------------------");
	    tempList=fulfillDetailMap.get(tempKey);
	    for(OmsTempCoFo omsTemp:tempList)
	    {
		if(omsTemp.getRmsResponseCode()!=null&&!"".equals(omsTemp.getRmsResponseCode()))
		{
		    if("P".equals(omsTemp.getRmsResponseCode()))
		    {
			unProcessedPResponsekey=tempKey;
			log.info("------->Map Object With-------->"+tempKey+"---------->Need to Procees....");
			return unProcessedPResponsekey;
		    }
		}
	    }
	}
	log.info("<-------------------------End of getUnProceesedPResponsekey method------------------>");
	return unProcessedPResponsekey;
    }
    // Method added as part of P/X processing, add it to OMSUtil

    private ArrayList<OmsTempCoFo> findUnProcessed_X_MapObject(ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap)
    {
	log.info("<----------------- Begin findUnProcessed_X_MapObject method------------------->");
	ArrayList<OmsTempCoFo> tempList=null;
	ArrayList<OmsTempCoFo> tempList1=null;
	for(BigDecimal tempKey:fulfillDetailMap.keySet())
	{
	    log.info("<-------------- temp key ------------>"+tempKey+"<---------------------");
	    tempList=fulfillDetailMap.get(tempKey);
	    for(OmsTempCoFo omsTemp:tempList)
	    {
		if(omsTemp.getRmsResponseCode()!=null&&!"".equals(omsTemp.getRmsResponseCode()))
		{
		    if("X".equals(omsTemp.getRmsResponseCode()))
		    {
			log.info("<---------------->"+omsTemp.getRmsResponseCode()+"<--------------------->");
			tempList1=tempList;
			return tempList1;
		    }
		} // end of if condition
	    } // end of inner loop
	} // end of outer loop
	log.info("<-------------- Before returning findUnProcessed_X_MapObject------------------------->"+tempList1+
		 "<-----------");
	return tempList1;
    } // end of function

    // for P/X processing. code for consecutive P response

    private ArrayList<OmsTempCoFo> findUnProcessed_P_MapObject(ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap)
    {
	log.info("<----------------- Begin findUnProcessed_P_MapObject method------------------->");
	ArrayList<OmsTempCoFo> tempList=null;
	ArrayList<OmsTempCoFo> tempList1=null;
	for(BigDecimal tempKey:fulfillDetailMap.keySet())
	{
	    log.info("<-------------- temp key ------------>"+tempKey+"<---------------------");
	    tempList=fulfillDetailMap.get(tempKey);
	    for(OmsTempCoFo omsTemp:tempList)
	    {
		if(omsTemp.getRmsResponseCode()!=null&&!"".equals(omsTemp.getRmsResponseCode()))
		{
		    if("P".equals(omsTemp.getRmsResponseCode()))
		    {
			log.info("<---------------->"+omsTemp.getRmsResponseCode()+"<--------------------->");
			tempList1=tempList;
			return tempList1;
		    }
		}
	    }

	}
	log.info("<-------------- Before returning findUnProcessed_P_MapObject------------------------->"+tempList1+
		 "<-----------");
	return tempList1;
    } // end of findUnProcessed_P_MapObject method....

    //Added method for P/X handling, creats the map for unfilled P response objects

    private ArrayList<OmsTempCoFo> ProceessPResponseFulFillmentOrder(ArrayList<OmsTempCoFo> tempList,
								     ConcurrentHashMap<BigDecimal,ArrayList<OmsTempCoFo>> fulfillDetailMap,BigDecimal omsCustOrdNo,
								     BigDecimal key)
    {
	log.info(" <-----------------------Begin of ProceessPResponseFulFillmentOrder--------------------------->");
	ArrayList<OmsTempCoFo> unfulfilledItems=new ArrayList<OmsTempCoFo>();
	Map<String,BigDecimal> tempMap=new HashMap<String,BigDecimal>();
	List<FulfilOrdCfmDtl> fulfilOrdCfmDtlList=null;
	log.info("The key is -------------------------->"+key+"<----------------------------------");
	this.pfulfilOrdCfmCol=this.pfulfilOrdCfmCols.get(key);
	log.info("<---------------------------------------------------------------------------------------------->");
	try
	{
	    if(this.pfulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty()&&this.pfulfilOrdCfmCol.getCollectionSize()==0)
	    {
		log.info("<----------**********pfulfilOrdCfmCol variable is null************-------------->");
		log.info("<----------*************Response code is C**********************---------------> ");
	    }
	    else
	    {
		log.info("<22222222222222222------------------------------------------------------------->");
		fulfilOrdCfmDtlList=this.pfulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getFulfilOrdCfmDtl();
	    }
	}
	catch(Exception e)
	{
	    log.info("-------->Error Occurred------->"+e.getMessage()+"------------->");
	    return unfulfilledItems;
	}

	if(fulfilOrdCfmDtlList!=null&&fulfilOrdCfmDtlList.size()>0)
	{
	    for(FulfilOrdCfmDtl fulfilOrdCfmDtl:fulfilOrdCfmDtlList)
	    {
		log.info("<--------------P.fulfilOrdCfmDtl.getItem()------------>"+fulfilOrdCfmDtl.getItem()+
			 "----fulfilOrdCfmDtl.getConfirmQty()-----"+fulfilOrdCfmDtl.getConfirmQty()+"--->");
		tempMap.put(fulfilOrdCfmDtl.getItem(),fulfilOrdCfmDtl.getConfirmQty());
	    }

	    // for (BigDecimal mapkey : fulfillDetailMap.keySet())
	    log.info("<------ Process The list with Key------>"+key+"<-------->");
	    if(key!=null)
	    {
		tempList=fulfillDetailMap.get(key);
		log.info("<---------------------------  mapkey-------------->"+key+"<------>");
		for(OmsTempCoFo tempCoFo:tempList)
		{
		    if(tempMap.containsKey(tempCoFo.getItem())==true&&tempCoFo.getRmsResponseCode()!=null&&
		       tempCoFo.getRmsResponseCode().equals("P"))
		    {
			log.info("<---Map-->"+key+"<------ Item --- --->"+tempCoFo.getItem()+"<-------------------->");
			OmsTempCoFo unfulfilledTemp=new OmsTempCoFo();
			log.info("tempCoFo.getOrderQty()---->"+tempCoFo.getOrderQty()+"----------->");
			log.info("tempCoFo.getItem()---->"+tempCoFo.getItem()+"----------->");
			log.info("tempMap.get(tempCoFo.getItem())--->"+tempMap.get(tempCoFo.getItem())+"------->");
			log.info("remaing Qty "+tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
			if((tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())).intValue()>0))
			{
			    log.info("omsCustOrdNo----->"+omsCustOrdNo+"------->");
			    unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
			    unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
			    log.info("tempCoFo.getItem()----->"+tempCoFo.getItem()+"----->");
			    unfulfilledTemp.setItem(tempCoFo.getItem());
			    log.info("tempCoFo.getLineNo()"+tempCoFo.getLineNo()+"------>");
			    unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
			    log.info("tempCoFo.getOrderQty() "+tempCoFo.getOrderQty()+"----->");
			    log.info("tempCoFo.getItem()"+tempCoFo.getItem()+"----->");
			    log.info("tempMap.get(tempCoFo.getItem())"+tempMap.get(tempCoFo.getItem())+"----->");
			    log.info("remaing Qty "+tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
			    log.info("<---------New Order Quantity---------->"+
				     tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem()))+"--------->");
			    unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
			    log.info("<-------------tempCoFo.getSourceLocId()--------->"+tempCoFo.getSourceLocId());
			    unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
			    log.info("tempCoFo.getSourceLocationType()"+tempCoFo.getSourceLocationType()+"-------->");
			    unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
			    log.info("tempCoFo.getVirtualWH()"+tempCoFo.getVirtualWH()+"----------->");
			    unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
			    log.info("tempCoFo.getCombinationId()"+tempCoFo.getCombinationId()+"<-------------->");
			    unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
			    unfulfilledTemp.setRmsResponseCode("P");
			    log.info("Setting Response Code to P");
			    log.info("unfulfilledTemp.getSourceLocId() "+unfulfilledTemp.getSourceLocId()+"------->");
			    log.info("unfulfilledTemp.getCombinationId()"+unfulfilledTemp.getCombinationId()+
				     "------->");
			    log.info("unfulfilledTemp.getRmsResponseCode()"+unfulfilledTemp.getRmsResponseCode()+
				     "------->");
			    log.info("unfulfilledTemp.getOrderQty() "+unfulfilledTemp.getOrderQty()+"------>");
			    tempCoFo.setOrderQty(tempMap.get(tempCoFo.getItem()));
			    log.info("tempMap.get(tempCoFo.getItem())---->"+tempMap.get(tempCoFo.getItem())+"---->");
			    tempCoFo.setFoConfQty(tempMap.get(tempCoFo.getItem()));
			    log.info("<----- Confrim Qauntity------->"+tempCoFo.getFoConfQty()+"<------>");
			    if(unfulfilledTemp.getItem().equals(tempCoFo.getItem()))
			    {
				tempCoFo.setRmsResponseCode("C");
				log.info("<---------tempCoFo.getRmsResponseCode()"+tempCoFo.getRmsResponseCode()+
					 "-------->");
				log.info("<--------tempCoFo.orderQty----->"+tempMap.get(tempCoFo.getItem())+
					 "-------->");
				log.info("<---------tempCoFo.ConfQty------->"+tempMap.get(tempCoFo.getItem())+
					 "--------->");
			    }
			    unfulfilledItems.add(unfulfilledTemp);
			}
			else
			{
			    tempCoFo.setRmsResponseCode("C");
			    tempCoFo.setFoConfQty(tempCoFo.getOrderQty());
			}
		    } // end of templist response code is 'P'.
		    else if(tempCoFo.getRmsResponseCode()!=null&&tempCoFo.getRmsResponseCode().equals("P"))
		    {
			// tempmap we are not considering
			log.info("<-------key doesnot contain in in tempmap-------------------->");
			log.info("passing currentFulfilOrderNo in fullfilldetailMap "+currentFulfilOrderNo+"<------>");
			tempList=fulfillDetailMap.get(new BigDecimal(currentFulfilOrderNo));
			// for (OmsTempCoFo temp : tempList) { // Removed from handle
			log.info("maxFulfilOrderNo "+maxFulfilOrderNo+"<-------->");
			log.info("temp.getOrderQty() "+tempCoFo.getOrderQty()+"<------>");
			log.info("temp.getItem()"+tempCoFo.getItem()+"<---->");
			log.info("remaing Qty "+tempCoFo.getOrderQty()+"<------>");
			log.info("temp.getRmsResponseCode() "+tempCoFo.getRmsResponseCode()+"<----->");
			if(tempCoFo.getOrderQty().intValue()>0)
			{
			    log.info(" <---------Creating an Object Of unfulfilledTemp------------->");
			    OmsTempCoFo unfulfilledTemp=new OmsTempCoFo();
			    log.info("<---omsCustOrdNo--->"+omsCustOrdNo+"<--maxFulfilOrderNo-->"+maxFulfilOrderNo+"Item"+
				     tempCoFo.getItem());
			    unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
			    unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
			    unfulfilledTemp.setItem(tempCoFo.getItem());
			    log.info("Line No"+tempCoFo.getLineNo()+"Order Qunatity"+tempCoFo.getOrderQty()+"Source Loc Id"+
				     tempCoFo.getSourceLocId());
			    unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
			    unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty());
			    unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
			    log.info("Source Location Type"+tempCoFo.getSourceLocationType()+" Virtual WH"+tempCoFo.getVirtualWH()+
				     "CombinationId"+tempCoFo.getCombinationId());
			    unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
			    unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
			    unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
			    unfulfilledTemp.setRmsResponseCode("P");
			    log.info("<---OrderQty-->"+tempCoFo.getOrderQty());
			    tempCoFo.setOrderQty(tempCoFo.getOrderQty());
			    tempCoFo.setFoConfQty(new BigDecimal(0));
			    if(unfulfilledTemp.getItem().equals(tempCoFo.getItem()))
			    {
				tempCoFo.setRmsResponseCode("C");
				log.info("temp.getRmsResponseCode()"+tempCoFo.getRmsResponseCode()+"<------>");
			    }
			    log.info("<--------Unfulfilled Items add into------->");
			    unfulfilledItems.add(unfulfilledTemp);
			}
			else
			{
			    log.info("We are setting Response Code into C-------->");
			    tempCoFo.setRmsResponseCode("C");
			    tempCoFo.setFoConfQty(new BigDecimal(0));
			}
			//} // end of for loop
		    } // end of  else if part.
		} // end of inner loop
	    } // map key ................... end of outer loop

	} // end if statement of fulfilOrdCfmDtlList
	log.info("<------------------ Before returning ProceessPResponseFulFillmentOrder --------------------->");
	return unfulfilledItems;
    }


    public void persistRTLog(BigDecimal omsCustOrdNo) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	log.info("inside OmsRtlogPublishLog "+omsCustOrdNo);
	List<OmsCustOrdItem> omsCustOrdItemList=session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
	OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
	for(OmsCustOrdItem customerOrderItems:omsCustOrdItemList)
	{
	    OmsRtlogPublishLog omsRtlogPublishLog=new OmsRtlogPublishLog();

	    if(customerOrderItems.getQtyOrderedSuom().intValue()!=customerOrderItems.getQtyCancelled().intValue())
	    {

		omsRtlogPublishLog.setLocation(omsCustOrdHead.getOrderRequestorId());
		omsRtlogPublishLog.setItem(customerOrderItems.getItem());
		omsRtlogPublishLog.setLineNo((customerOrderItems.getLineNo()));
		omsRtlogPublishLog.setPublishedInd("N");
		omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrdNo);
		omsRtlogPublishLog.setTranType("ORI");
		log.info("omsCustOrdNo "+omsCustOrdNo+"Qty in RTLog is "+
			 customerOrderItems.getQtyOrderedSuom().subtract(customerOrderItems.getQtyCancelled()));
		omsRtlogPublishLog.setQty(customerOrderItems.getQtyOrderedSuom().subtract(customerOrderItems.getQtyCancelled()));
		omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
	    }
	    try
	    {
		session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
	    }
	    catch(Exception e)
	    {
		String languageCode=OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
		Boolean flag=
   OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode);
		if(Boolean.FALSE==flag)
		{
		    languageCode=OmsErrorCodesConstant.baseLanguageCodeValue;
		}
		String errString=
OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode,new String[]{ });
		log.error("-->Failed in persisting in oms_rtlog_publish_log table.");
		throw new SOAPException(errString);
	    }

	}


    }

}

