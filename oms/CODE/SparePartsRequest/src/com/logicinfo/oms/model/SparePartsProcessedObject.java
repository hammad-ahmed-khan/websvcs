package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsSparePartFulfill;

public class SparePartsProcessedObject {
    
    private OmsSparePartFulfill sparePartsFulfillObj;
    private boolean simProcessed;
    private boolean auditProcessed;
    private boolean fulfillProcessed;
    private String transactionId;
    
    
    
    
    public SparePartsProcessedObject() {
        super();
        this.auditProcessed=false;
        this.simProcessed=false;
        this.fulfillProcessed = false;
        this.transactionId = null;
        this.sparePartsFulfillObj=null;
    }


    public void setSparePartsFulfillObj(OmsSparePartFulfill sparePartsFulfillObj) {
        this.sparePartsFulfillObj = sparePartsFulfillObj;
    }

    public OmsSparePartFulfill getSparePartsFulfillObj() {
        return sparePartsFulfillObj;
    }

    public void setSimProcessed(boolean simProcessed) {
        this.simProcessed = simProcessed;
    }

    public boolean isSimProcessed() {
        return simProcessed;
    }

    public void setAuditProcessed(boolean auditProcessed) {
        this.auditProcessed = auditProcessed;
    }

    public boolean isAuditProcessed() {
        return auditProcessed;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setFulfillProcessed(boolean fulfillProcessed) {
        this.fulfillProcessed = fulfillProcessed;
    }

    public boolean isFulfillProcessed() {
        return fulfillProcessed;
    }
}
