package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsSparePartAudit;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.model.SparePartsRequest;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;

public class SparePartsRequestHelperBean
{
    String OMSSparePartsServiceId;
    final String webServiceName = "Spare Parts Request WebService";
    final String statusInProcess = "IP";
    final String statusRejected = "RJ";

    
    public SparePartsRequestHelperBean() 
    {
        super();
    }

    public OmsSparePartHeader formOmsSparePartsHeaderObject(SparePartsRequest inputData) 
    {

        OmsSparePartHeader theSparePartsHeaderObject = new OmsSparePartHeader();
        theSparePartsHeaderObject.setServiceRequestId(inputData.getServiceRequestId());
        theSparePartsHeaderObject.setSequenceId(inputData.getSequenceId());
        theSparePartsHeaderObject.setItemId(inputData.getItemId());
        theSparePartsHeaderObject.setQuantityRequested(inputData.getQuantity());
        theSparePartsHeaderObject.setPendingQty(inputData.getQuantity());
        theSparePartsHeaderObject.setStoreId(new BigDecimal(inputData.getStoreId()));
        theSparePartsHeaderObject.setReadyToNotifyFlag("N");
        theSparePartsHeaderObject.setNotifiedFlag("N");
        theSparePartsHeaderObject.setStatus(statusInProcess);
        theSparePartsHeaderObject.setCreatedBy(webServiceName);
        theSparePartsHeaderObject.setCreateDatetime(new Timestamp((new Date()).getTime()));


        return theSparePartsHeaderObject;
    }

    public OmsSparePartFulfill formOmsSparePartFulfillObject(String item,
                                                             BigDecimal sourceLocation, 
                                                             BigDecimal quantityDeducted,
                                                             BigDecimal quantityReceived, 
                                                             BigDecimal quantityReserved, 
                                                             BigDecimal quantityShipped,
                                                             BigDecimal quantityTsfReserved, 
                                                             BigDecimal quantityUnreserved,
                                                             BigDecimal transactionId,
                                                             String transactionType)
    {

        OmsSparePartFulfill theOmsSparePartFulfillObject = new OmsSparePartFulfill();
        theOmsSparePartFulfillObject.setOmsServiceReqSeqId(OMSSparePartsServiceId);
        theOmsSparePartFulfillObject.setQuantityDeducted(quantityDeducted);
        theOmsSparePartFulfillObject.setQuantityReceived(quantityReceived);
        theOmsSparePartFulfillObject.setQuantityReserved(quantityReserved);
        theOmsSparePartFulfillObject.setQuantityShipped(quantityShipped);
        theOmsSparePartFulfillObject.setQuantityTsfReserved(quantityTsfReserved);
        theOmsSparePartFulfillObject.setQuantityUnreserved(quantityUnreserved);
        theOmsSparePartFulfillObject.setTranId(transactionId);
        theOmsSparePartFulfillObject.setTranType(transactionType);
        
        theOmsSparePartFulfillObject.setItemId(item);
        theOmsSparePartFulfillObject.setSourceLocation(sourceLocation);
        
        theOmsSparePartFulfillObject.setCreatedBy(webServiceName);
        theOmsSparePartFulfillObject.setCreateDatetime(new Timestamp((new Date()).getTime()));
        theOmsSparePartFulfillObject.setLastUpdatedDatetime(new Timestamp((new Date()).getTime()));

        return theOmsSparePartFulfillObject;
    }

    public OmsSparePartAudit formOmsSparePartAuditObject(String itemId, String eventType,
                                                        BigDecimal quantity, BigDecimal sourceLocation,
                                                        String omsServiceRequestId) 
    {
        
        OmsSparePartAudit theOmsSparePartAuditObject = new OmsSparePartAudit();
        
        theOmsSparePartAuditObject.setOmsServiceReqSeqId(omsServiceRequestId);
        theOmsSparePartAuditObject.setItemId(itemId);
        theOmsSparePartAuditObject.setQuantity(quantity);
        theOmsSparePartAuditObject.setSourceLocation(sourceLocation);
        theOmsSparePartAuditObject.setEventType(eventType);
        
        theOmsSparePartAuditObject.setCreatedBy(webServiceName);
        theOmsSparePartAuditObject.setCreateDatetime(new Timestamp((new Date()).getTime()));

        return theOmsSparePartAuditObject;
    }
}
