package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposAddrbookEntryPK implements Serializable {
    public BigDecimal addrbookEntrySeq;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposAddrbookEntryPK() {
    }

    public OmsOrposAddrbookEntryPK(BigDecimal addrbookEntrySeq, BigDecimal omsOrposCustOrderId) {
        this.addrbookEntrySeq = addrbookEntrySeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposAddrbookEntryPK) {
            final OmsOrposAddrbookEntryPK otherOmsOrposAddrbookEntryPK = (OmsOrposAddrbookEntryPK)other;
            final boolean areEqual =
                (otherOmsOrposAddrbookEntryPK.addrbookEntrySeq.equals(addrbookEntrySeq) && otherOmsOrposAddrbookEntryPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getAddrbookEntrySeq() {
        return addrbookEntrySeq;
    }

    public void setAddrbookEntrySeq(BigDecimal addrbookEntrySeq) {
        this.addrbookEntrySeq = addrbookEntrySeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
