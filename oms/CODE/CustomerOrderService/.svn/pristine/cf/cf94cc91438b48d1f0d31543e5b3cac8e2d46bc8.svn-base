package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRmaDelDetail;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.util.List;

import javax.xml.soap.SOAPException;

public class ResponseProcessing {
    public ResponseProcessing() {
        super();
    }
    
    public RMADeleteResponse createResponse(RMADeleteRequest input,String errMessage) throws SOAPException {
        OMSUtilSessionEJB session =OMSUtil.doLookup();
        RMADeleteResponse rmaDeleteResponse=new RMADeleteResponse();
        rmaDeleteResponse.setRmaDelReqId(input.getRmaDelReqId());
        rmaDeleteResponse.setMessageStatus("SUCCESS");
        if (null != errMessage) {
           
            OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
            //theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
            rmaDeleteResponse.setMessageStatus(theErrorObj.getOmsErrLangDesc());
           
        } else {
            //theResponse.setMessageDesc("");
            //theResponse.setMessageCode("");
            rmaDeleteResponse.setMessageStatus("");
        }
        List<OmsRmaDelDetail> omsRmaDelDetailList=session.getOmsRmaDelDetailFindByRmaDelReqId((input.getRmaDelReqId()));
       
        List<RMADeleteDetailResponse> rmaDeleteDetailResponseList=rmaDeleteResponse.getRMADeleteDetailResponse();
        for(OmsRmaDelDetail omsRmaDelDetail:omsRmaDelDetailList) {
            
            RMADeleteDetailResponse rmaDeleteDetailResponse=new RMADeleteDetailResponse();
            if (null != errMessage) {
               
                OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
                //theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
                rmaDeleteDetailResponse.setMessageCode(theErrorObj.getOmsErrorCode());
                rmaDeleteDetailResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
            } else {
                //theResponse.setMessageDesc("");
                //theResponse.setMessageCode("");
                rmaDeleteDetailResponse.setMessageDesc("");
            }
            rmaDeleteDetailResponse.setLineNo(omsRmaDelDetail.getLineNo().longValue());
            rmaDeleteDetailResponse.setItem(omsRmaDelDetail.getItem());
            rmaDeleteDetailResponse.setMessageCode(omsRmaDelDetail.getStatus());
            rmaDeleteDetailResponseList.add(rmaDeleteDetailResponse);
        }
        return rmaDeleteResponse;
    }
}
