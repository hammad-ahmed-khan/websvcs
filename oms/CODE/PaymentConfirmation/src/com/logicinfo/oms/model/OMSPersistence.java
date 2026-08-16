package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class OMSPersistence {
    public final static Logger log =
       Logger.getLogger(com.logicinfo.oms.model.OMSPersistence.class.getName());
    public OMSPersistence() {
        super();
    }
    
    public void persistOmsCustOrdReserve(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {
         log.info("***Start-persistCustOrdReserve***");
         OMSUtilSessionEJB session =OMSUtil.doLookup();
         List<OmsCustOrdReserve> omsCustOrdReserveList=null;
        try
        {
          omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
        }
        catch(Exception e)
        {
            log.error("-->No record exist in oms_cust_ord_reserve table");
        }
         for (OmsCustOrdReserve custOrd : omsCustOrdReserveList) 
         {
             OmsCustOrdReserve omsCustOrdReserve =custOrd;
             omsCustOrdReserve.setConfTs(new Timestamp(new Date().getTime()));//need to confirm             
            omsCustOrdReserve.setPaymentStatus("S");
             omsCustOrdReserve.setResvStatus("CNF");     
             try
             {
             session.mergeOmsCustOrdReserve(omsCustOrdReserve);
             }
             catch(Exception e) 
             {
                 String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
                Boolean flag =
                    OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.errorUnHandledExceptionCode,
                                                            languageCode);
                String errString = null;
                if (Boolean.TRUE == flag) {
                    OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.errorUnHandledExceptionCode,
                                                        languageCode, new String[] { });
                } else {
                    OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.errorUnHandledExceptionCode, "1",
                                                        new String[] { });
                }
                 log.error("-->Failed in persisting in oms_cust_ord_reserve table.");
                 throw new SOAPException(errString);                                    
             }
             log.info("omsCustOrdNo "+omsCustOrdNo+"Succesfully persisted in oms_cust_ord_reserve table");
         }
     }
}
