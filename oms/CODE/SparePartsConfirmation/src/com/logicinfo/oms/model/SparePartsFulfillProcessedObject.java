package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsSparePartFulfill;

import java.math.BigDecimal;

public class SparePartsFulfillProcessedObject {
    public SparePartsFulfillProcessedObject() {
        super();
        this.quantityConfirmed=null;
        this.sparePartsFulfillRec=null;
    }
    
    private OmsSparePartFulfill sparePartsFulfillRec;
    private BigDecimal quantityConfirmed;

    public void setSparePartsFulfillRec(OmsSparePartFulfill sparePartsFulfillRec) {
        this.sparePartsFulfillRec = sparePartsFulfillRec;
    }

    public OmsSparePartFulfill getSparePartsFulfillRec() {
        return sparePartsFulfillRec;
    }

    public void setQuantityConfirmed(BigDecimal quantityConfirmed) {
        this.quantityConfirmed = quantityConfirmed;
    }

    public BigDecimal getQuantityConfirmed() {
        return quantityConfirmed;
    }
}
