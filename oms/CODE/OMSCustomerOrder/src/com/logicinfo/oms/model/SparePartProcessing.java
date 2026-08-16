package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoSasInvAdj;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OracleBaseAPIUtil;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException;

public class SparePartProcessing {
    public SparePartProcessing() {
        super();
    }
    int noOfRetry=2;
    private final static Logger log = Logger.getLogger(SparePartProcessing.class.getName());
    public void processSparePart(CustomerOrder input,OmsTempCoFo temp, BigDecimal omsCustOrdNo,int lineNo) throws SOAPException, IllegalArgumentWSFaultException,
                                                    IllegalStateWSFaultException, ValidationWSFaultException {
        log.info("inside processSparePart");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
             

        String reasonCode=session.getOmsSystemParametersFindIndValue( "SAS_INV_RESV_CODE","OMS_INV_ADJ_RSN_CODE");
             callSIMInvAdjWebservice(temp, new BigDecimal(reasonCode));
                
                OmsCoSasInvAdj omsCoSasInvAdj =new OmsCoSasInvAdj();
                omsCoSasInvAdj.setCustOrderNo(input.getCustomerOrderNo());
                omsCoSasInvAdj.setSubCustOrderNo(input.getCustomerSubOrderNo());
                omsCoSasInvAdj.setItem(temp.getItem());
                omsCoSasInvAdj.setLineItemNo(new BigDecimal(lineNo));
          
                omsCoSasInvAdj.setLocationId(temp.getSourceLocId());//check
                omsCoSasInvAdj.setOmsCustOrdNo(omsCustOrdNo);
                omsCoSasInvAdj.setResvDatetime(new Timestamp(new Date().getTime()));
                omsCoSasInvAdj.setResvQty(temp.getOrderQty());      //check         
                omsCoSasInvAdj.setResvReasonCode(new BigDecimal(reasonCode));
                session.persistOmsCoSasInvAdj(omsCoSasInvAdj);
                log.info("Persisted in oms_co_sas_inv_adj");
            
        
    }
    public void callSIMInvAdjWebservice(OmsTempCoFo temp,BigDecimal reasonCode) throws SOAPException,
                                                                        IllegalArgumentWSFaultException,
                                                                        IllegalStateWSFaultException,
                                                                        ValidationWSFaultException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
      
            StrAdjModVo strAdjModVo = new StrAdjModVo();
            strAdjModVo.setComments("SAS-invAdj");
            strAdjModVo.setStoreId(temp.getSourceLocId().longValue());
            StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
            strAdjItemMod.setItemId(temp.getItem());
            strAdjItemMod.setQuantity(temp.getOrderQty());
            strAdjItemMod.setReasonId(reasonCode.longValue());
            strAdjItemMod.setCaseSize(new BigDecimal(1));
            strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
       


        try {
        	SaveAndConfirmInventoryAdjustment adjustment = new SaveAndConfirmInventoryAdjustment();
        	adjustment.setStrAdjModVo(strAdjModVo);
            OracleBaseAPIUtil.getOracleSIMClient().saveAndConfirmInventoryAdjustment(adjustment).getStrAdjRef();
           temp.setStatus("C");
            session.mergeOmsTempCoFo(temp);

        } catch (Exception e) {
            log.error("SIM webservice call failed , reason=" + e);
            temp.setStatus("FAILED");
            session.mergeOmsTempCoFo(temp);
            
            if (noOfRetry < 1) {
                log.info("Retrying " + noOfRetry + "  time");
                noOfRetry++;
                callSIMInvAdjWebservice(temp,reasonCode);
            }
            
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.getMessage()));
        }
        }
    
}
