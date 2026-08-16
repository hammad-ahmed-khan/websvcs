package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRmaModDetail;
import com.logicinfo.oms.util.OMSUtil;

import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class ResponseProcessing 
{
    public ResponseProcessing() 
    {
        super();
    }
    
    private final static Logger log = Logger.getLogger(ResponseProcessing.class.getName());
   
    public RMAModifyResponse createResponse(RMAModifyRequest input,String errMessage) throws SOAPException 
    {
        OMSUtilSessionEJB session =OMSUtil.doLookup();  
        RMAModifyResponse rmaModifyResponse=new RMAModifyResponse(); 
        rmaModifyResponse.setRmaModReqId(input.getRmaModReqId());
        rmaModifyResponse.setStatus("F");
        List<RMAModifyDetailResponse> RMADModifyDetailResponseList=rmaModifyResponse.getRMAModifyDetailResponse();
        List<OmsRmaModDetail> omsRmaModDetailList=session.getOmsRmaModDetailFindByRmaModReqId(input.getRmaModReqId());
        if (null != errMessage && errMessage!="") 
        {
            log.info("inside errMessage:"+errMessage);
            OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
            //theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
            rmaModifyResponse.setMessageCode("F");
            rmaModifyResponse.setStatus("FAILED");
            rmaModifyResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
        } 
        else 
        { 
            rmaModifyResponse.setStatus("SUCCESS");
            rmaModifyResponse.setMessageCode("S");
            rmaModifyResponse.setMessageDesc("SUCCESS");
            for(OmsRmaModDetail omsRmaModDetail: omsRmaModDetailList)
            {
                RMAModifyDetailResponse rmaModifyDetailResponse= new RMAModifyDetailResponse();
                rmaModifyDetailResponse.setItem(omsRmaModDetail.getItem());
                rmaModifyDetailResponse.setLineNo(omsRmaModDetail.getLineNo().longValue());
                RMADModifyDetailResponseList.add(rmaModifyDetailResponse);
            } 
        }
        return rmaModifyResponse;
    }
}
