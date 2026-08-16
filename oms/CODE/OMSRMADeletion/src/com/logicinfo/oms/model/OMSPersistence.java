package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRmaDelDetail;
import com.logicinfo.oms.ejb.OmsRmaDelHead;

import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

public class OMSPersistence {
    public OMSPersistence() {
        super();
    }
    public void persistOmsRMADelReq(RMADeleteRequest input) throws SOAPException {
        OMSUtilSessionEJB session =OMSUtil.doLookup();
        OmsRmaDelHead omsRmaDelHead=new OmsRmaDelHead();
        omsRmaDelHead.setRmaId(new BigDecimal(input.getRmaId()));
        omsRmaDelHead.setRmaDelReqId(input.getRmaDelReqId());
        omsRmaDelHead.setStatus("N");
        omsRmaDelHead.setCreateDatetime(new Timestamp(new Date().getTime()));
        session.persistOmsRmaDelHead(omsRmaDelHead);
    }
    
    public void persistOmsRMADelDetail(RMADeleteRequest input) throws SOAPException {
        OMSUtilSessionEJB session =OMSUtil.doLookup();
      List<RMADeleteDetail> rmaDeleteDetailList = input.getRMADeleteDetail();
      for(RMADeleteDetail rmaDeleteDetail:rmaDeleteDetailList) 
      {
          OmsRmaReqItem omsRmaReqItem= session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), rmaDeleteDetail.getItem(),new BigDecimal(rmaDeleteDetail.getLineNo()));
          OmsRmaDelDetail omsRmaDelDetail=new OmsRmaDelDetail();
          omsRmaDelDetail.setLineNo(omsRmaDelDetail.getLineNo());
          omsRmaDelDetail.setItem(rmaDeleteDetail.getItem());
          omsRmaDelDetail.setRmaDelReqId(input.getRmaDelReqId());
          omsRmaDelDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
          omsRmaDelDetail.setLineNo(omsRmaReqItem.getLineNo());
          session.persistOmsRmaReqItem(omsRmaReqItem);
      }
    }
}
