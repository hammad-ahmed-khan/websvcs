package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRmaModDetail;
import com.logicinfo.oms.ejb.OmsRmaModHead;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class OMSPersistence 
{
    public OMSPersistence() 
    {
        super();
    }
    private final static Logger log = Logger.getLogger(OMSPersistence.class.getName());
   
    public void persistOMSRMAModifyHead(RMAModifyRequest input) throws SOAPException 
    {
        log.info("Persisting in oms_rma_modify_head***");
        OMSUtilSessionEJB session =OMSUtil.doLookup();  
        OmsRmaModHead omsRmaModHead=new OmsRmaModHead();
        omsRmaModHead.setRmaId(new BigDecimal(input.getRmaId()));
        omsRmaModHead.setRmaModReqId(input.getRmaModReqId());
        omsRmaModHead.setCreateDatetime(new Timestamp(new Date().getTime()));
        omsRmaModHead.setStatus("N");  
        session.persistOmsRmaModHead(omsRmaModHead);
    }
    
    public void peristOMSRmaModDetail(RMAModifyRequest input) throws SOAPException 
    {
        log.info("Persisting in oms_rma_modify_detail***");
        OMSUtilSessionEJB session =OMSUtil.doLookup();  
        OmsRmaModDetail  omsRmaModDetail=new OmsRmaModDetail();
        for(RMAModifyDetail rmaModifyDetail:input.getRMAModifyDetail()) 
        {
            //changed the code for line number
             OmsRmaReqItem omsRmaReqItem = session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), rmaModifyDetail.getItem(),new BigDecimal(rmaModifyDetail.getLineNo()));
             omsRmaModDetail.setItem(rmaModifyDetail.getItem());
             omsRmaModDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
             omsRmaModDetail.setRmaModReqId(input.getRmaModReqId());
             omsRmaModDetail.setQuantity(new BigDecimal(rmaModifyDetail.getQty()));
             //changed the line number code coming from input XSD.
             omsRmaModDetail.setLineNo(new BigDecimal(rmaModifyDetail.getLineNo()));
             omsRmaModDetail.setStatus("N");
             session.persistOmsRmaModDetail(omsRmaModDetail);
        }
    }
}
