package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsRmaDelDetail;
import com.logicinfo.oms.ejb.OmsRmaDelHead;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;

import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class RMADeletetionBean 
{
    public RMADeletetionBean()
    {
        super();
    }
    private final static Logger log = Logger.getLogger(RMADeletetionBean.class.getName());
    String language="1";
    OmsRmaReq omsRmaReq=null;
public void validate(RMADeleteRequest input) throws SOAPException
{
    log.info("beginning of validate()");
    OMSUtilSessionEJB session = OMSUtil.doLookup();
   
   // language= session.getOmsCustOrdHeadFindLanguage(omsRmaReq.getOmsCustOrdNo());
   try
   {
      omsRmaReq=  session.getOmsRmaReqFindByRmaId(new BigDecimal(input.getRmaId()));
   }
   catch(Exception e) 
   {
       String errString =OMSUtilCommons.formErrorDescription("INVALID_RMA_ID", "1", new String[] { String.valueOf(input.getRmaId())
                                                                                                    });
       log.error(errString);
       throw new SOAPException(errString);
   }
  try
  {
    log.info("beginning of try block in validation"); 
    String status="S";  
    OmsRmaDelHead  omsRmaDelHead= session.getOmsRmaDelHeadFindByRmaId(new BigDecimal(input.getRmaId()),status);
    
     log.info("Status="+omsRmaDelHead.getStatus());
      if(omsRmaDelHead.getStatus().equals("S"))
      {
          String errString =OMSUtilCommons.formErrorDescription("RMA_ALRDY_CLD", "1", new String[] { String.valueOf(input.getRmaId())});
          log.error(errString);
          throw new SOAPException(errString);
      }
  }
  catch(Exception e)
  {
      //throw new SOAPException(e);
  }
  
    log.info("Validation done successfully");
}

    public void persistData(RMADeleteRequest input) throws SOAPException {
        log.info("persistData");
        OMSPersistence omsPersistence = new OMSPersistence();
        omsPersistence.persistOmsRMADelReq(input);
        //  omsPersistence.persistOmsRMADelDetail(input);

    }

    public void callRWMSWebservice(RMADeleteRequest input) throws IllegalArgumentWSFaultException,
                                                                  IllegalStateWSFaultException,
                                                                  ValidationWSFaultException, SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        for (RMADeleteDetail rmaDeleteDetail : input.getRMADeleteDetail()) {
            log.info("Starting callRWMSWebservice");
            OmsRmaDelDetail omsRmaDelDetail = new OmsRmaDelDetail();
            OmsRmaReqItem omsRmaReqItem=null;
                try {
             omsRmaReqItem =
                session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), rmaDeleteDetail.getItem(),new BigDecimal(rmaDeleteDetail.getLineNo()));
            
            omsRmaDelDetail.setItem(rmaDeleteDetail.getItem());
            omsRmaDelDetail.setRmaDelReqId((input.getRmaDelReqId()));
            omsRmaDelDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
            omsRmaDelDetail.setLineNo(omsRmaReqItem.getLineNo());
            omsRmaDelDetail.setStatus("S");
            
                InterfacePersistence interfacePersistence = new InterfacePersistence();
                interfacePersistence.callRWMSWebservice(input,omsRmaReqItem.getLineNo());
                OmsRmaDelHead omsRmaDelHead=    session.getOmsRmaDelHeadFindByRmaDelReqId(input.getRmaDelReqId(), new BigDecimal(input.getRmaId()));
                    omsRmaDelDetail.setStatus("S");
                    session.mergeOmsRmaDelHead(omsRmaDelHead);
               
            } catch (Exception e) {
                log.error("Failed calling webservice because of error="+e);
                omsRmaDelDetail.setStatus("F");
                omsRmaDelDetail.setErrorCode("SYSTEM_ERROR");
                omsRmaDelDetail.setErrorMessage(e.getMessage());
                OmsRmaDelHead omsRmaDelHead=    session.getOmsRmaDelHeadFindByRmaDelReqId(input.getRmaDelReqId(), new BigDecimal(input.getRmaId()));
                    omsRmaDelDetail.setStatus("F");
                    session.mergeOmsRmaDelHead(omsRmaDelHead);
            }
            OmsCustOrdItem omsCustOrdItem  = session.getOmsCustOrdItemFindByItem(omsRmaReq.getOmsCustOrdNo(), omsRmaDelDetail.getItem(), (omsRmaDelDetail.getLineNo()));
          OmsRmaReqItem omsRmaReqItem1=  session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), omsRmaDelDetail.getItem(), omsRmaDelDetail.getLineNo());
           
            omsCustOrdItem.setQtyReturned(omsCustOrdItem.getQtyReturned().subtract(omsRmaReqItem1.getRmaQty()));
            session.mergeOmsCustOrdItem(omsCustOrdItem);
            session.persistOmsRmaDelDetail(omsRmaDelDetail);
        }
    }
public RMADeleteResponse createResponse(RMADeleteRequest input,String errMessage) throws SOAPException {
    ResponseProcessing responseProcessing=new ResponseProcessing();
   return responseProcessing.createResponse(input,errMessage);
}

}
