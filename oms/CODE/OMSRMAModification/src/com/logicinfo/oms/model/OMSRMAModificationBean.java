package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRmaModHead;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.service.RMAModifyWebServiceImpl;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class OMSRMAModificationBean 
{
    public OMSRMAModificationBean() 
    {
        super();
    }   
    String language="1";
    private final static Logger log = Logger.getLogger(OMSRMAModificationBean.class.getName());
   
    public void persistOmsRmaMod(RMAModifyRequest input) throws SOAPException 
    {
        OMSPersistence omsPersistence=new OMSPersistence();
        omsPersistence.persistOMSRMAModifyHead(input);  
        omsPersistence.peristOMSRmaModDetail(input);
    }
    
    public void validate(RMAModifyRequest input) throws SOAPException 
    {
        log.info("***validating request***");
        OMSUtilSessionEJB session =OMSUtil.doLookup();  
        OmsRmaReq omsRmaReq=null;
        try
        {
            OmsRmaReq omsRmaReq1=session.getOmsRmaReqFindByRmaId(new BigDecimal(input.getRmaId()));
            log.info("the customer order no is ---"+omsRmaReq1.getOmsCustOrdNo());
            language= session.getOmsCustOrdHeadFindLanguage(omsRmaReq1.getOmsCustOrdNo());
        }
        catch(Exception e) 
        {
            log.error("Unable to find customer language");
        }
        try
        {
            OmsRmaModHead omsRmaModHead=session.getOmsRmaModHeadFindByRmaModReqId(input.getRmaModReqId());
            log.info("before throw"+omsRmaModHead.getRmaModReqId());
            String errString = OMSUtilCommons.formErrorDescription("INVALID_RMA_MOD_ID", language, new String[] { input.getRmaModReqId()});
            log.error(errString);
            throw new SOAPException(errString);
        }
        catch(SOAPException e) 
        {
            log.error("SOAP exception is"+e.getMessage());
            if (e.getMessage().equals("No Record")) 
            {
                
            }
            else 
            {
                log.error("Record already exist");
                throw new SOAPException(e);
            }
            //continue 
        }
        try
        {
            omsRmaReq =  session.getOmsRmaReqFindByRmaId(new BigDecimal(input.getRmaId()));
        }
        catch(Exception e) 
        {
           String errString = OMSUtilCommons.formErrorDescription("INVALID_RMA_ID", language, new String[] { String.valueOf(input.getRmaId())});
           log.error(errString);
           throw new SOAPException(errString);
        }
        for(RMAModifyDetail rmaModifyDetail:input.getRMAModifyDetail()) 
        {
         log.info("OmsCustORdNo="+omsRmaReq.getOmsCustOrdNo()+"item="+rmaModifyDetail.getItem()+"lineno="+rmaModifyDetail.getLineNo());
            //changed the code for line_no
            BigDecimal deliverdQty =  session.getOmsCustOrdItemFindDeilverdQuantity(omsRmaReq.getOmsCustOrdNo(),rmaModifyDetail.getItem(),new BigDecimal(rmaModifyDetail.getLineNo()));
           log.info("deliverdQty"+deliverdQty);
            if(rmaModifyDetail.getQty()>deliverdQty.longValue()) 
            {
                //modified_qty should be <= qty deliverd in customer order
                // for item @@value1 the modified quantity should not be greater than delivered quantity.
                String errString = OMSUtilCommons.formErrorDescription("RMA_MOD_GT_QTY", language, new String[] {rmaModifyDetail.getItem()});
                log.error(errString);
                throw new SOAPException(errString);
            }
            log.info("rma id"+input.getRmaId()+"Item="+ rmaModifyDetail.getItem()+"Line_no="+rmaModifyDetail.getLineNo());
            BigDecimal receivedQty=session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), rmaModifyDetail.getItem(),new BigDecimal(rmaModifyDetail.getLineNo())).getReceivedQty();
           log.info("receivedQty"+receivedQty);
            if(rmaModifyDetail.getQty()<receivedQty.longValue()) 
           
            {
                //modified_qty >= qty received in WH against RMA
               // for item @@value1 the modified quantity should  be greater than received quantity.
                String errString = OMSUtilCommons.formErrorDescription("RMA_MOD_LT_QTY", language, new String[] {rmaModifyDetail.getItem()});
                log.error(errString);
                throw new SOAPException(errString);
            }
        }
        log.info("vslidation ends");
    }
                     
   public void callRWMSWebservice(RMAModifyRequest input) throws SOAPException, IllegalArgumentWSFaultException,
                                                                  IllegalStateWSFaultException,
                                                                  ValidationWSFaultException 
   {
       InterfacePersistence interfacePersistence=new InterfacePersistence();
       interfacePersistence.callRWMSWebservice(input);      
   }
   
   public RMAModifyResponse createResponse(RMAModifyRequest input,String errMessage) throws SOAPException 
   {
       ResponseProcessing responseProcessing=new ResponseProcessing();
       return  responseProcessing.createResponse(input, errMessage);
   }
}
