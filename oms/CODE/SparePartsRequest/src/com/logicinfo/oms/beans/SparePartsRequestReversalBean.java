package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;
import com.logicinfo.oms.model.SparePartsProcessedObject;
import com.logicinfo.oms.model.SparePartsRequest;

import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class SparePartsRequestReversalBean extends SparePartsRequestBean 
{

    private final static Logger log = Logger.getLogger(SparePartsRequestReversalBean.class.getName());
    OracleIntegrationSevicesSIM simOracleIntegrationSevices;

    public SparePartsRequestReversalBean()
    {
        super();
    }

    public void reverseSuccessfulTransactions(List<SparePartsProcessedObject> successfullyProcessedRecs,
                                              SparePartsRequest input, String omsServiceReqId) throws SOAPException
    {
        simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();

        //Fetch all records saved in the OMS_SPARE_PART_FULFILL
        try
        {
            OMSUtilSessionEJB session = OMSUtil.doLookup();

            //Update the OMS_SPARE_PART_HEADER record for status
            //Updating the cumulative quantities is not required here as the cumulative quantities in the spare parts header table will be
            //updated only after SUCCESSFUL processing of the detailed record.
            OmsSparePartHeader omsSparePartHeader = session.getOmsSparePartHeaderByIntKey(omsServiceReqId);
            omsSparePartHeader.setStatus(statusRejected);
            session.mergeOmsSparePartHeader(omsSparePartHeader);
            log.info("  --> Status updated as 'Rejected' in Spare Parts Header table.");

            //If there are any records in the Spare parts Fulfillment Tables
            log.info("  --> Reversing " + successfullyProcessedRecs.size() + " records in the fulfill table.");
            if (null != successfullyProcessedRecs && successfullyProcessedRecs.size() > 0)
            {
                String eventType = OMSConstants.QUANTITY_REJECTED;
                BigDecimal auditQty;
                BigDecimal locationId;
                String theItem = input.getItemId();
                OmsSparePartFulfill sparePartsFulfillObj;

                //Iterate through each record and
                for (SparePartsProcessedObject theProcessedObj : successfullyProcessedRecs)
                {

                    sparePartsFulfillObj = theProcessedObj.getSparePartsFulfillObj();

                    auditQty = null;
                    locationId = sparePartsFulfillObj.getSourceLocation();


                    if (theProcessedObj.isSimProcessed()) 
                    {
                        //Reverse the record in SIM
                        if (input.getStoreId() ==sparePartsFulfillObj.getSourceLocation().longValue())
                        { //Reverse Adjust Inventory
                            String theReasonCode = session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryUnReserveCode,OMSConstants.inventoryAdjReasonCode);
                            simOracleIntegrationSevices.adjustInventoryByItemLocation(theItem,
                                                                                      sparePartsFulfillObj.getSourceLocation(),
                                                                                      new BigDecimal(theReasonCode),
                                                                                      sparePartsFulfillObj.getQuantityReserved(),
                                                                                      input.getServiceRequestId());
                            auditQty = sparePartsFulfillObj.getQuantityReserved();

                        }
                        else 
                        { //Reverse Inv Transfer
                            //Cancel the existing transfer request.
                            simOracleIntegrationSevices.cancelTransferRequestStore2Store(sparePartsFulfillObj.getSourceLocation(),
                                                                                         new BigDecimal(theProcessedObj.getTransactionId()));
                            auditQty = sparePartsFulfillObj.getQuantityTsfReserved();

                        }
                        log.info("      --> Reversed the SIM entries for location id " +sparePartsFulfillObj.getSourceLocation());
                    }

                    if (theProcessedObj.isFulfillProcessed())
                    {
                        //Update the OMS_SPARE_PART_FULFILL record to zero
                        reverseSparePartsFulfillmentEntry(sparePartsFulfillObj);
                        log.info(" --> Reversed spare parts fulfill info for location id " +sparePartsFulfillObj.getSourceLocation());
                    }
                    
                    //Enter a record in the Audit table for reversal.
                    createSparePartsAuditEntry(theItem, eventType, auditQty, locationId,
                                               sparePartsFulfillObj.getOmsServiceReqSeqId());
                    log.info("      --> Created an audit entry for the reversals for locatio id " +sparePartsFulfillObj.getSourceLocation());

                } //End Looping

            }

        } 
        catch (Exception e)
        {
            log.error("--> ERROR while reversing the spare parts transaction :" + e.getMessage());

        }
    }


    public void reverseSparePartsFulfillmentEntry(OmsSparePartFulfill pSparePartsFulfill) throws SOAPException
    {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        pSparePartsFulfill.setQuantityReserved(null == pSparePartsFulfill.getQuantityReserved() ? null : new BigDecimal(0));
        pSparePartsFulfill.setQuantityTsfReserved(null == pSparePartsFulfill.getQuantityTsfReserved() ? null : new BigDecimal(0));
        session.mergeOmsSparePartFulfill(pSparePartsFulfill);
    }
    //createSparePartsAuditEntry
}
