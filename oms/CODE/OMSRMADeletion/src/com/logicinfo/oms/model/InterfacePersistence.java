package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.pendrtrnref.v1.PendRtrnRef;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsPortType;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsService;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class InterfacePersistence {
    public InterfacePersistence() {
        super();
    }
    private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName()); 
public void callRWMSWebservice(RMADeleteRequest input,BigDecimal lineNo) throws IllegalArgumentWSFaultException, IllegalStateWSFaultException,
                                            ValidationWSFaultException, SOAPException {
    log.info("Calling RWMS web service");
    OMSUtilSessionEJB session =OMSUtil.doLookup();
  
   // String reasonCode=session.getOmsSystemParametersFindIndValue("REASON_CODE", "RMA_REQ");
    OmsRmaReq omsRmaReq= session.getOmsRmaReqFindByRmaId(new BigDecimal(input.getRmaId()));
    PendingReturnsService pendingReturnsService = new PendingReturnsService();
    PendingReturnsPortType pendingReturnsPortType = pendingReturnsService.getPendingReturnsPort();
    PendRtrnRef pendRtrnRef=new PendRtrnRef();
    pendRtrnRef.setPhysicalWh(omsRmaReq.getReturnLocId().longValue());
    pendRtrnRef.setRmaNbr(String.valueOf(input.getRmaId()));
    pendRtrnRef.setLineItemNbr(lineNo.longValue());
    //pendRtrnRef.setReasonCode(reasonCode);
    pendingReturnsPortType.pendReturnDtlDelete(pendRtrnRef);
}
}


