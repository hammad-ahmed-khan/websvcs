package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposContactPK implements Serializable {
    public BigDecimal contactSeq;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposContactPK() {
    }

    public OmsOrposContactPK(BigDecimal contactSeq, BigDecimal omsOrposCustOrderId) {
        this.contactSeq = contactSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposContactPK) {
            final OmsOrposContactPK otherOmsOrposContactPK = (OmsOrposContactPK)other;
            final boolean areEqual =
                (otherOmsOrposContactPK.contactSeq.equals(contactSeq) && otherOmsOrposContactPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getContactSeq() {
        return contactSeq;
    }

    public void setContactSeq(BigDecimal contactSeq) {
        this.contactSeq = contactSeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
