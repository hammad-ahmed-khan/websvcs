package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;

import java.math.BigDecimal;

public class SparePartsCancellationProcessedFulfillmentObjects {
    
    private OmsSparePartFulfill sparePartsFulfillObj;
    private boolean simProcessed;
    private boolean fulfillProcessed;
    private boolean headerProcessed;
    private boolean auditProcessed;
    private BigDecimal quantityProcessed;
    
    public SparePartsCancellationProcessedFulfillmentObjects() {
        super();
        this.simProcessed = false;
        this.fulfillProcessed = false;
        this.headerProcessed = false;
        this.auditProcessed = false;
        this.sparePartsFulfillObj = null;
        this.quantityProcessed = null;
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

    public void setFulfillmentProcessed(boolean fulfillProcessed) {
        this.fulfillProcessed = fulfillProcessed;
    }

    public boolean isFulfillmentProcessed() {
        return fulfillProcessed;
    }

    public void setHeaderProcessed(boolean headerProcessed) {
        this.headerProcessed = headerProcessed;
    }

    public boolean isHeaderProcessed() {
        return headerProcessed;
    }

    public void setAuditProcessed(boolean auditProcessed) {
        this.auditProcessed = auditProcessed;
    }

    public boolean isAuditProcessed() {
        return auditProcessed;
    }

    public void setQuantityProcessed(BigDecimal quantityProcessed) {
        this.quantityProcessed = quantityProcessed;
    }

    public BigDecimal getQuantityProcessed() {
        return quantityProcessed;
    }

}
