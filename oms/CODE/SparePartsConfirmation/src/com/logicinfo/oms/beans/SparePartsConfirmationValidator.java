package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.model.ServiceConfirmationDetailType;
import com.logicinfo.oms.util.OMSConstants;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class SparePartsConfirmationValidator {
    
    private final static Logger log = Logger.getLogger(SparePartsConfirmationValidator.class.getName());
    
    public SparePartsConfirmationValidator() {
        super();
    }
    
    /**
     * Check if the pending quantity from the request is more than the confirmation quantity. 
     * If confirmation quantity requested is more than pending quantity, then terminate processing.
     * @param theSparePartHeadObj
     * @param theDetails
     * @return
     * @throws SOAPException
     */
    public void isPendingQtyMoreThanConfirmationQty(OmsSparePartHeader theSparePartHeadObj,
                                                       ServiceConfirmationDetailType theDetails) throws SOAPException 
    {
        boolean requestedQtyIsMore = false;
        BigDecimal pendingQty = null==theSparePartHeadObj.getPendingQty()?new BigDecimal(0):theSparePartHeadObj.getPendingQty();
        try 
        {
            requestedQtyIsMore =
                    (theDetails.getQuantity().subtract(pendingQty).longValue() > 0 ? true : false);
            
            if (requestedQtyIsMore) {
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_REQ_QTY_MORE_PEND_QTY,
                                                                            "1",
                                                                            new String[] { "Spare Parts Confirm ", theDetails.getQuantity().toString(),
                                                                                           theSparePartHeadObj.getPendingQty().toString() }));
            }
        }catch (SOAPException se){
            log.error("     --> Validation Error: "+ se.getMessage());
            throw new SOAPException(se.getMessage());
        }catch (Exception e) {
            log.error("Unknown error at Excepion "+ e.getMessage());
            throw new SOAPException("Unknown Error validating pending quantity.");
        }
    }
    
    /**
     * Check if the confirmation quantity from the request is less than the Cumulative Reserved quantity. 
     * If confirmation quantity requested is more than Cumulative Reserved quantity, then terminate processing. WHY?
     * @param theSparePartHeadObj
     * @param theDetails
     * @return
     * @throws SOAPException
     */
    public void isCumReservedQtyMoreThanConfirmationQty(OmsSparePartHeader theSparePartHeadObj,
                                                       ServiceConfirmationDetailType theDetails) throws SOAPException {
        boolean requestedQtyIsMore = false;
        BigDecimal compareQty = new BigDecimal(0);
        BigDecimal cumReservedQty = null==theSparePartHeadObj.getCumQuantityReserved()?new BigDecimal(0):theSparePartHeadObj.getCumQuantityReserved();
        BigDecimal cumUnReservedQty = null==theSparePartHeadObj.getCumQuantityUnreserved()?new BigDecimal(0):theSparePartHeadObj.getCumQuantityUnreserved();
        BigDecimal cumCancelledQty = null==theSparePartHeadObj.getCumQuantityUnreserved()?new BigDecimal(0):theSparePartHeadObj.getCumQuantityUnreserved();
        try {
            //Net of Cumulative Reserved 
            compareQty = cumReservedQty.subtract(cumUnReservedQty);
            log.info("compareQty :"+compareQty);
            requestedQtyIsMore =
                    (theDetails.getQuantity().subtract(compareQty).longValue() > 0 ? true : false);
        
            if (requestedQtyIsMore) {
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_REQ_QTY_MORE_PEND_QTY,
                                                                            "1",
                                                                            new String[] { "Spare Parts Confirm ", theDetails.getQuantity().toString(),
                                                                                           compareQty.toString()+" (Net of Cum Res and Unres Qty)" }));
            }
        }catch (SOAPException se){
            log.error("     --> Validation Error: "+ se.getMessage());
            throw new SOAPException(se.getMessage());
        }catch (Exception e) {
            log.error("Unknown error at Excepion "+ e.getMessage());
            throw new SOAPException("Unknown Error validating cumulative reserved quantity.");
        }
    }
}
