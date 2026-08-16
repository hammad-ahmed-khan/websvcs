package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import org.apache.log4j.Logger;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;


import java.util.List;

import javax.xml.soap.SOAPException;

public class ValidatePayment
{
    public final static Logger logger=Logger.getLogger(com.logicinfo.oms.model.ValidatePayment.class.getName());
   
   
    public static String getTenderStatus(String customerOrderNo) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	OmsCustOrdHead omsCustOrdHead=null;
	String tenderstatus="pending";
	List<OmsCustOrdTender> omsCustOrdTenderList=null;
	omsCustOrdHead=session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo,"S");
	if(omsCustOrdHead!=null&&omsCustOrdHead.getOrderCreateReserveInd().equalsIgnoreCase("R"))
	{
	    logger.info("check tenderstatus for omscustordNo "+omsCustOrdHead.getOmsCustOrdNo());
	    omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
	    for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
	    {
		if(omsCustOrdTender.getPaymentStatusInd().equalsIgnoreCase("P"))
		{
		    tenderstatus="pending";
		}
		else if(omsCustOrdTender.getPaymentStatusInd().equalsIgnoreCase("A"))
		{
		    return "PAYMNT_INPROGRESS";
		    
		}
		else
		{
		    return "PAYMNT_ALDRY_CONFD";
		}
	    }
	}
	if(tenderstatus.equalsIgnoreCase("pending"))
	{
	    updateOmsTenderPaymentStatus(omsCustOrdHead.getOmsCustOrdNo());
	}
	return tenderstatus;
    }
 
    public static void updateOmsTenderPaymentStatus(BigDecimal omscustordno) throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
        List<OmsCustOrdTender> omsCustOrdTenderList=session.getOmsCustOrdTenderFindByOmsCustOrdNo(omscustordno);
	for(OmsCustOrdTender omsCustOrdTender:omsCustOrdTenderList)
	{
	    omsCustOrdTender.setPaymentStatusInd("A");
	    logger.info("omsCustOrdNo"+omscustordno+" Setting last update date time for payment status A");
	    omsCustOrdTender.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
	    session.mergeOmsCustOrdTender(omsCustOrdTender);
	    logger.info("omsCustOrdNo"+omscustordno+" updated Tender to A for omscustordNo "+omscustordno);
	}
    }
    public static CoPaymentConfResponse returnTenderStatusResponse(CoPaymentConf input,String status) throws SOAPException
    {
	CoPaymentConfResponse response=new CoPaymentConfResponse();
	response.setApplicationId(input.getApplicationId());
	response.setCustomerOrderNo(input.getCustomerOrderNo());
	response.setEntityId(input.getEntityId());
	response.setComments(input.getComments());
        response.setMessageStatus("F"); 
	response.setRequestDatetimestamp(input.getRequestDatetimestamp());
	response.setMessageCode(status);
	if(status.equalsIgnoreCase("PAYMNT_ALDRY_CONFD"))
	{
	    response.setMessageDesc("Payment already confirmed");
	}
	else
	{
	    response.setMessageDesc("Payment inProgress");
	}
	return response;
    }
    
    
    
}
