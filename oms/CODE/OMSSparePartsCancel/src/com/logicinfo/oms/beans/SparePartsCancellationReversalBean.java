package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;

import com.logicinfo.oms.model.SparePartsCancelRequest;
import com.logicinfo.oms.model.SparePartsCancellationProcessedFulfillmentObjects;
import com.logicinfo.oms.model.SparePartsCancellationProcessedObjects;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.util.Date;

import java.sql.Timestamp;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class SparePartsCancellationReversalBean extends SparePartsCancelBean {

    private final static Logger log = Logger.getLogger(SparePartsCancellationReversalBean.class.getName());

    OracleIntegrationSevicesSIM simOracleIntegrationSevices;

    public SparePartsCancellationReversalBean() {
        super();
    }

    public void reverseSuccessfulTransactions(SparePartsCancellationProcessedObjects successfullyProcessedRecs,
                                              SparePartsCancelRequest input) throws SOAPException {

        simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();

        //Fetch all records saved in the OMS_SPARE_PART_FULFILL
        try {
            OMSUtilSessionEJB session = OMSUtil.doLookup();

            OmsSparePartHeader theHeaderData = successfullyProcessedRecs.getSparePartsHeaderObj();

            //Update the OMS_SPARE_PART_CANCEL_HDR record for status
            //Updating the cumulative quantities is not required here as the cumulative quantities in the spare parts header table will be
            //updated only after SUCCESSFUL processing of the detailed record.
            OmsSparePartCancelHdr omsSparePartCancelHdr =
                session.getOmsSparePartCancelHdrByKey(input.getCancellationId(), input.getSequenceId(),
                                                      input.getServiceRequestId());

            omsSparePartCancelHdr.setStatus(statusRejected);
            session.mergeOmsSparePartCancelHdr(omsSparePartCancelHdr);
            log.info("  --> Spare Parts Cancel Header table status updated as 'Rejected'.");

            //If there are any records in the Spare parts Fulfillment Tables
            log.info("  --> Reversing " + successfullyProcessedRecs.getProcessedFulfillObj().size() +
                     " records in the fulfill table.");

            if (null != successfullyProcessedRecs.getProcessedFulfillObj() &&
                successfullyProcessedRecs.getProcessedFulfillObj().size() > 0) {
                String eventType = OMSConstants.QUANTITY_REJECTED;
                BigDecimal auditQty;
                BigDecimal locationId;
                String theItem = theHeaderData.getItemId();
                OmsSparePartFulfill sparePartsFulfillObj;
                long simTransactionId=0;
                String txnType=null;
                boolean isShipped=false;

                //Iterate through each record and
                for (SparePartsCancellationProcessedFulfillmentObjects theProcessedFulfillmentObj :
                     successfullyProcessedRecs.getProcessedFulfillObj()) {

                    sparePartsFulfillObj = theProcessedFulfillmentObj.getSparePartsFulfillObj();

                    auditQty = theProcessedFulfillmentObj.getQuantityProcessed();
                    locationId = sparePartsFulfillObj.getSourceLocation();
                    isShipped = sparePartsFulfillObj.getQuantityShipped().floatValue()>0;

                    if (theProcessedFulfillmentObj.isSimProcessed()) {
                        //Reverse the record in SIM
                        if (theHeaderData.getStoreId().longValue() ==
                            sparePartsFulfillObj.getSourceLocation().longValue()) { //Reverse Adjust Inventory

                            //Get the reason code
                            String theReasonCode =
                                session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryReserveCode,
                                                                           OMSConstants.inventoryAdjReasonCode);

                            //Reverse the SIM Inv Adjustment entry.
                            simTransactionId =
                                    simOracleIntegrationSevices.adjustInventoryByItemLocation(theItem, sparePartsFulfillObj.getSourceLocation(),
                                                                                              new BigDecimal(theReasonCode),
                                                                                              theProcessedFulfillmentObj.getQuantityProcessed(),
                                                                                              input.getServiceRequestId());
                            txnType = transTypeAdjustment;

                        } else { //Reverse Inv Transfer
                            //As the transfer request.
                            simTransactionId =
                                    simOracleIntegrationSevices.saveTransferRequestStore2Store(theItem, sparePartsFulfillObj.getSourceLocation(),
                                                                                               theHeaderData.getStoreId(),
                                                                                               theProcessedFulfillmentObj.getQuantityProcessed(),
                                                                                               input.getServiceRequestId());
                            txnType = transTypeTransfer;

                        }
                        log.info("      --> Reversed the SIM entries for location id " +
                                 sparePartsFulfillObj.getSourceLocation());
                    }

                    if (theProcessedFulfillmentObj.isFulfillmentProcessed())
                        createSparePartsFulfillmentEntry(sparePartsFulfillObj, simTransactionId, txnType,
                                                         theProcessedFulfillmentObj.getQuantityProcessed());

                    if (theProcessedFulfillmentObj.isHeaderProcessed())
                        updateSparePartHeaderData(theHeaderData, theProcessedFulfillmentObj.getQuantityProcessed().negate(),
                                                  txnType,new BigDecimal(0),isShipped);

                    //Enter a record in the Audit table for reversal.
                    createSparePartsAuditEntry(theItem, eventType, auditQty, locationId,
                                               sparePartsFulfillObj.getOmsServiceReqSeqId());
                    log.info("      --> Created an audit entry for the reversals for location id " +
                             sparePartsFulfillObj.getSourceLocation());

                } //End Looping

            }

        } catch (Exception e) {
            log.error("     --> ERROR while reversing the spare parts transaction :" + e.getMessage());
            throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1",
                                                                        new String[] { }));
        }
    }


    public void createSparePartsFulfillmentEntry(OmsSparePartFulfill pSparePartsFulfill, long pTxnId, String pTxnType,
                                                 BigDecimal pQty) throws SOAPException {

        OMSUtilSessionEJB session = OMSUtil.doLookup();
        pSparePartsFulfill.setTranId(new BigDecimal(pTxnId));
        pSparePartsFulfill.setTranType(pTxnType);
        pSparePartsFulfill.setCreatedBy(webServiceName);
        pSparePartsFulfill.setCreateDatetime(new Timestamp(new Date().getTime()));


        //Initializing quantities
        pSparePartsFulfill.setQuantityCancelled(null);
        pSparePartsFulfill.setQuantityDeducted(null);
        pSparePartsFulfill.setQuantityReceived(null);
        pSparePartsFulfill.setQuantityReserved(null);
        pSparePartsFulfill.setQuantityShipped(null);
        pSparePartsFulfill.setQuantityTsfReserved(null);
        pSparePartsFulfill.setQuantityUnreserved(null);

        if (transTypeTransfer.equals(pTxnType))
            pSparePartsFulfill.setQuantityTsfReserved(pQty);
        else
            pSparePartsFulfill.setQuantityReserved(pQty);

        session.persistOmsSparePartFulfill(pSparePartsFulfill);
    }


}
