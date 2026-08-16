package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.model.SparePartsCancelRequest;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class SparePartsCancellationValidator {
    private final static Logger log = Logger.getLogger(SparePartsCancellationValidator.class.getName());

    public SparePartsCancellationValidator() {
        super();
    }

    /**
     * Validating cancellation Data
     * @param input
     * @throws SOAPException
     */
    public OmsSparePartHeader validateCancellationData(SparePartsCancelRequest input) throws SOAPException {

        //Validation 1: Check if Spare Part Request already existin in SPARE_PARTS_HEADER table
        OmsSparePartHeader theSparePartHeadObj = checkIfRequestExistsForCancellation(input);
        log.info("  --> Spare Part Header Exists.");

        //Validation 2: Check if cancel qty in the request is >0
        if (input.getQuantity().floatValue() <= 0)
            throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_INVALID_INPUT, "1",
                                                                        new String[] { input.getQuantity().toString() +
                                                                                       ", for cancel quantity" }));

        //Validation 3: Check for duplicate cancel request.
        checkIfCancelRequestIsDuplicate(input);
        log.info("  --> New Spare Parts Cancel Request.");

        //Validation 2: Pending Qty check
        isPendingQtyMoreThanCancellationQty(theSparePartHeadObj, input);
        return theSparePartHeadObj;
    }

    /**
     * Check if Spare Part Request already existin in SPARE_PARTS_HEADER table
     *       If it does not exists then no cancellation is possible.
     * @param input
     * @return
     * @throws SOAPException
     */
    public OmsSparePartHeader checkIfRequestExistsForCancellation(SparePartsCancelRequest input) throws SOAPException {
        OMSUtilSessionEJB session;
        //Step 1 - Get an instance of the EJB session.
        try 
        {
            session = OMSUtil.doLookup();
        } catch (Exception e)
        {
            throw new SOAPException("ERROR while looking up OMSUtil Session EJB." + e.getMessage());
        }
        OmsSparePartHeader theSparePartHeadObj = null;

        //Step 2- Get the Spare Parts Header Information.
        try
        {
            log.info("feteching data from the EJB method getOmsSparePartHeaderByExtKeys ");
            log.info("input.getServiceRequestId() "+input.getServiceRequestId());
            log.info("input.getSequenceId() "+input.getSequenceId());
            theSparePartHeadObj =session.getOmsSparePartHeaderByExtKeys(input.getServiceRequestId(), input.getSequenceId());
            //validation to check combination IDs
            if(!theSparePartHeadObj.getOmsServiceReqSeqId().equals(input.getOMSServiceId())){
                log.error("  --> Input OMS Service ID "+input.getOMSServiceId()+
                          "  does not match OMS Service Id "+theSparePartHeadObj.getOmsServiceReqSeqId()+  "  from database."); 
                throw new SOAPException("No Record");
            }

            //Step 3 - If a record does not exists in the table for the input combination then throw exception.
        } catch (SOAPException se) {
            if (se.getMessage().equals("No Record")) {
                log.info("  --> NoResultException - No Spare Part Request for Req ID: " + input.getServiceRequestId() +
                         " and Seq Id " + new Long(input.getSequenceId()).toString());
                String errValueString =
                    " Service Request Id " + input.getServiceRequestId() + " and Sequence Id " + input.getSequenceId() 
                    + "  and OMS Service Id " + input.getOMSServiceId();
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_NOT_EXISTS, "1",
                                                                            new String[] { errValueString }));
            } else {
                throw new SOAPException(se.getMessage());
            }
        } catch (Exception e) {
            log.error("Unknown error from Exception" + e.getMessage());
            throw new SOAPException(e.getMessage());
        }
        //Step 4: If no Exceptions then proceed.
        return theSparePartHeadObj;
    }

    /**
     * Check if Spare Part Request already existing in SPARE_PARTS_CANCEL_HDR table
     *       If it exists raise exception. Duplicate requests cannot be worked on.
     * @param input
     * @return
     * @throws SOAPException
     */
    public OmsSparePartCancelHdr checkIfCancelRequestIsDuplicate(SparePartsCancelRequest input) throws SOAPException {
        log.info("  ------------ checkIfCancelRequestIsDuplicate " );
        OMSUtilSessionEJB session;
        //Step 1 - Get an instance of the EJB session.
        try {
            session = OMSUtil.doLookup();
        } catch (Exception e) {
            throw new SOAPException("ERROR while looking up OMSUtil Session EJB." + e.getMessage());
        }
        OmsSparePartCancelHdr theSparePartCancelHdr = null;

        //Step 2- Get the Spare Parts Header Information.
        try {
            theSparePartCancelHdr =
                    session.getOmsSparePartCancelHdrByKey(input.getCancellationId(), input.getSequenceId(),
                                                          input.getServiceRequestId());
            //If same request exists throw error.
            if (null != theSparePartCancelHdr && null!= theSparePartCancelHdr.getCancellationId()) {
                String errValueString =
                    " Service Request Id " + input.getServiceRequestId() +
                    " and Cancellation Id " + input.getCancellationId()+". Duplicate request.";
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_EXISTS, "1",
                                                                            new String[] { errValueString }));
            }
        } catch (SOAPException se) {
            throw new SOAPException(se.getMessage());
        } catch (Exception e) {
            log.error("Unknown error from Exception" + e.getMessage());
            throw new SOAPException(e.getMessage());
        }
        //Step 4: If no Exceptions then proceed.
        return theSparePartCancelHdr;
    }


    /**
     * Check if the pending quantity from the request is more than or equal to the
     * cancellation request quantity. If cancellation quantity requested is more than pending quantity, then terminate processing.
     * @param theSparePartHeadObj
     * @param input
     * @return
     * @throws SOAPException
     */
    public void isPendingQtyMoreThanCancellationQty(OmsSparePartHeader theSparePartHeadObj,
                                                    SparePartsCancelRequest input) throws SOAPException {
        log.info("  ------------ isPendingQtyMoreThanCancellationQty ");

        boolean requestedQtyIsMore = false;
        BigDecimal pendingQty =
            null == theSparePartHeadObj.getPendingQty() ? new BigDecimal(0) : theSparePartHeadObj.getPendingQty();
        try {
            requestedQtyIsMore = ((input.getQuantity().subtract(pendingQty)).longValue() > 0 ? true : false);

            if (requestedQtyIsMore) {
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_REQ_QTY_MORE_PEND_QTY,
                                                                            "1",
                                                                            new String[] { "Spare Parts Cancel ", input.getQuantity().toString(),
                                                                                           theSparePartHeadObj.getPendingQty().toString() }));
            }
        } catch (SOAPException se) {
            throw new SOAPException(se.getMessage());
        } catch (Exception e) {
            log.error("Unknown error at Excepion " + e.getMessage());
            throw new SOAPException("Unknown Error validating pending quantity.");
        }
    }

}
