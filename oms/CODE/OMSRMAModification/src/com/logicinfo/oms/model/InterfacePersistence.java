package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.ejb.OmsSystemParameters;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlActCodeDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlDesc;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlRsnCodeDesc;
import com.oracle.retail.integration.base.bo.pendrtrnref.v1.PendRtrnRef;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsPortType;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendingReturnsService;

import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

public class InterfacePersistence 
{
    public InterfacePersistence()
    {
        super();
    }
    private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName());
    
    public void callRWMSWebservice(RMAModifyRequest input) throws SOAPException, IllegalArgumentWSFaultException,
                                            IllegalStateWSFaultException, ValidationWSFaultException 
    {
        log.info("Calling RWMS web service");
        OMSUtilSessionEJB session =OMSUtil.doLookup();
        
        // String reasonCode=session.getOmsSystemParametersFindIndValue("REASON_CODE", "RMA_REQ");
        OmsRmaReq omsRmaReq= session.getOmsRmaReqFindByRmaId(new BigDecimal(input.getRmaId()));
        String reasonCode = "";
        String reason_description = "";
        String actionCode = "";
        List<OmsSystemParameters> sysParams = session.getOmsSystemParametersFindByParameterId("RMA_REQ");
        for (OmsSystemParameters parameter : sysParams) 
        {
            if (parameter.getParameterName().equals("REASON_CODE")) 
            {
                reasonCode = parameter.getParameterValue();
            }
            if (parameter.getParameterName().equals("REASON_DESCRIPTION")) 
            {
                reason_description = parameter.getParameterValue();
            }
            if (parameter.getParameterName().equals("ACTION_CODE")) 
            {
                actionCode = parameter.getParameterValue();
            }
        }
        PendingReturnsService pendingReturnsService = new PendingReturnsService();
        PendingReturnsPortType pendingReturnsPortType = pendingReturnsService.getPendingReturnsPort();
        PendRtrnDesc pendRtrnDesc=new PendRtrnDesc();
        pendRtrnDesc.setCustOrderNbr(omsRmaReq.getCustOrderNo());
        pendRtrnDesc.setPhysicalWh(omsRmaReq.getReturnLocId().longValue());
        pendRtrnDesc.setRmaNbr(String.valueOf(input.getRmaId()));
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        DatatypeFactory datatypeFactory = null;
        try 
        {
            datatypeFactory = DatatypeFactory.newInstance();
        } 
        catch (DatatypeConfigurationException e) 
        {
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
        }
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        Calendar now1 = Calendar.getInstance();
        now1.setTimeInMillis(omsRmaReq.getReturnDateTime().getTime());
        now.setMonth(now1.get(Calendar.MONTH)+1);
        now.setYear(now1.get(Calendar.YEAR));
        now.setDay(now1.get(Calendar.DAY_OF_MONTH));
        pendRtrnDesc.setExpectedReceipt(now);
        List<PendRtrnDtlDesc> pendRtrnDtlDescList=pendRtrnDesc.getPendRtrnDtlDesc();
        for(RMAModifyDetail rmaModifyDetail:input.getRMAModifyDetail()) 
        {
            //changed the code for line no
             OmsRmaReqItem omsRmaReqItem= session.getOmsRmaReqItemFindByRmaIdAndItem(new BigDecimal(input.getRmaId()), rmaModifyDetail.getItem(),new BigDecimal(rmaModifyDetail.getLineNo()));
             PendRtrnDtlDesc pendRtrnDtlDesc=new PendRtrnDtlDesc();
             pendRtrnDtlDesc.setItemId(rmaModifyDetail.getItem());
             PendRtrnDtlRsnCodeDesc pendRtrnDtlRsnCodeDesc = new PendRtrnDtlRsnCodeDesc();
             pendRtrnDtlRsnCodeDesc.setReasonCode(reasonCode);
             pendRtrnDtlRsnCodeDesc.setDescription(reason_description);
             pendRtrnDtlDesc.getPendRtrnDtlRsnCodeDesc().add(pendRtrnDtlRsnCodeDesc);
             PendRtrnDtlActCodeDesc pendRtrnDtlActCodeDesc = new PendRtrnDtlActCodeDesc();
             pendRtrnDtlActCodeDesc.setActionCode(actionCode);
             // pendRtrnDtlActCodeDesc.setUnitQty(value);
            // pendRtrnDtlActCodeDesc.setComments(items.getItemComments());
             pendRtrnDtlDesc.getPendRtrnDtlActCodeDesc().add(pendRtrnDtlActCodeDesc);
             pendRtrnDtlDescList.add(pendRtrnDtlDesc);
             pendRtrnDtlDesc.setExpectedUnitQty(new BigDecimal(rmaModifyDetail.getQty()));
             //changed the code for line number coming in from input XSD.
             pendRtrnDtlDesc.setLineItemNbr(rmaModifyDetail.getLineNo());
             pendRtrnDtlDescList.add(pendRtrnDtlDesc);
             omsRmaReqItem.setRmaQty(new BigDecimal(rmaModifyDetail.getQty()));
             session.mergeOmsRmaReqItem(omsRmaReqItem);
         }
      
        //pendRtrnRef.setReasonCode(reasonCode);
        pendingReturnsPortType.pendReturnDtlModify(pendRtrnDesc);   
       
        log.info("Webservice call successful");
        
    }
}
