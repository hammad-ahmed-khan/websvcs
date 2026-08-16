package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.ejb.OmsSparePartConfirmDtl;
import com.logicinfo.oms.ejb.OmsSparePartConfirmHdr;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;

import com.logicinfo.oms.model.SparePartsConfirmationProcessedObject;
import com.logicinfo.oms.model.SparePartsFulfillProcessedObject;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class SparePartsConfirmationReversalBean extends SparePartsConfirmationBean {

    private final static Logger log = Logger.getLogger(SparePartsConfirmationReversalBean.class.getName());

    OracleIntegrationSevicesSIM simOracleIntegrationSevices;

    public SparePartsConfirmationReversalBean() {
        super();
    }

    public void reverseSuccessfulTransactions(SparePartsConfirmationProcessedObject successfullyProcessedInfo,
                                              OmsSparePartConfirmDtl input, String serviceReqId,
                                              String confirmationId) throws SOAPException {

        simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();
        OmsSparePartFulfill sparePartsFulfillObj;
        //Fetch all records saved in the OMS_SPARE_PART_FULFILL
        try {
            OMSUtilSessionEJB session = OMSUtil.doLookup();

            //Update the OMS_SPARE_PART_CONFIRM_HDR record for status
            //Updating the cumulative quantities is not required here as the cumulative quantities in the spare parts header table will be
            //updated only after SUCCESSFUL processing of the detailed record.
            OmsSparePartConfirmHdr omsSparePartConfirmHdr =
                session.getOmsSparePartConfirmHdrFindByKey(serviceReqId, confirmationId);

            omsSparePartConfirmHdr.setStatus(statusRejected);
            session.mergeOmsSparePartConfirmHdr(omsSparePartConfirmHdr);
            log.info("      --> Spare Parts Confirm Header table status updated as 'Rejected'.");
            //for (SparePartsConfirmationProcessedObject successfullyProcessedInfo : successfullyProcessedInfoList) {
                BigDecimal qtyConfirmed = successfullyProcessedInfo.getQuantityProcessed();

                OmsSparePartHeader theSparePartsHeaderObj =
                    session.getOmsSparePartHeaderByIntKey(input.getOmsServiceReqSeqId());
                if (successfullyProcessedInfo.isSpHeaderProcessed()) {
                    //Reverse Entries in the Spare Parts Header Table
                    theSparePartsHeaderObj = setSparePartHeaderQuantity(theSparePartsHeaderObj, qtyConfirmed.negate());
                    session.mergeOmsSparePartHeader(theSparePartsHeaderObj);
                    log.info("      --> Spare Parts Header table data reversed.");
                }
                
                //Reverse Entries in the Spare Parts Fulfill Table
                if (successfullyProcessedInfo.isFulfillmentProcessed()) {
                    List<SparePartsFulfillProcessedObject> theFulfillRecs =
                        successfullyProcessedInfo.getProcessedFulfilledRecs();
                    for (SparePartsFulfillProcessedObject theFulfillmentRecDetail : theFulfillRecs) {
                        OmsSparePartFulfill theFulfillRec = theFulfillmentRecDetail.getSparePartsFulfillRec();
                        theFulfillRec =
                                setSparePartFulfillQuantity(theFulfillRec, theFulfillmentRecDetail.getQuantityConfirmed().negate());
                        session.mergeOmsSparePartFulfill(theFulfillRec);
                    } //End Looping
                    log.info("      --> Spare Parts Fulfill table data reversed.");
                }


                if (null != successfullyProcessedInfo.getSparePartsConfirmDtlObj()) {
                    String eventType = OMSConstants.QUANTITY_RECEIVED;
                    BigDecimal auditQty;
                    BigDecimal locationId;
                    String theItem = theSparePartsHeaderObj.getItemId();

                    auditQty = successfullyProcessedInfo.getQuantityProcessed();
                    locationId = theSparePartsHeaderObj.getStoreId();

                    String reasonCode = null;
                    if (successfullyProcessedInfo.isSimUnreserveProcessed()) {
                        reasonCode =
                                session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryUnReserveCode, OMSConstants.inventoryAdjReasonCode);

                        //Call SIM Inv Adjustment WS to Reducing Inv Adjustment
                        simOracleIntegrationSevices.adjustInventoryByItemLocation(theSparePartsHeaderObj.getItemId(),
                                                                                  theSparePartsHeaderObj.getStoreId(),
                                                                                  new BigDecimal(reasonCode),
                                                                                  input.getConfirmQty(),
                                                                                  theSparePartsHeaderObj.getServiceRequestId());
                        log.info("      --> Unreserved quantity in SIM reverted.");
                    }
                    if (successfullyProcessedInfo.isSimDeductProcessed()) {
                        //Get the Response code for Deducting Qunatity
                        reasonCode = successfullyProcessedInfo.getSparePartsConfirmDtlObj().getReasonCode();
                        //session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryDeductCode, OMSConstants.inventoryAdjReasonCode);
                        //Call SIM Inv Adjustment WS to Deducting/Reducing Inventory
                        simOracleIntegrationSevices.adjustInventoryByItemLocation(theSparePartsHeaderObj.getItemId(),
                                                                                  theSparePartsHeaderObj.getStoreId(),
                                                                                  new BigDecimal(reasonCode),
                                                                                  input.getConfirmQty().negate(),
                                                                                  theSparePartsHeaderObj.getServiceRequestId());
                        log.info("      --> Deducted quantity in SIM reverted.");
                    }

                    //Enter a record in the Audit table for reversal.
                    createSparePartsAuditEntry(theItem, eventType, auditQty, locationId,
                                               theSparePartsHeaderObj.getOmsServiceReqSeqId());
                    log.info("      --> Created an audit entry for the reversals for location id " +
                             theSparePartsHeaderObj.getStoreId());

                }
            //} //End Loop;
        } catch (Exception e) {
            log.error("     --> ERROR while reversing the spare parts confirmation transaction :" + e.getMessage());
            throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1",
                                                                        new String[] { }));
        }
    }


}
