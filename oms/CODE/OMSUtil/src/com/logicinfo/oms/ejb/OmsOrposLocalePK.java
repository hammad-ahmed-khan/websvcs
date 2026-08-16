package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposLocalePK implements Serializable {
    public BigDecimal localeSeq;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposLocalePK() {
    }

    public OmsOrposLocalePK(BigDecimal localeSeq, BigDecimal omsOrposCustOrderId) {
        this.localeSeq = localeSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposLocalePK) {
            final OmsOrposLocalePK otherOmsOrposLocalePK = (OmsOrposLocalePK)other;
            final boolean areEqual =
                (otherOmsOrposLocalePK.localeSeq.equals(localeSeq) && otherOmsOrposLocalePK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getLocaleSeq() {
        return localeSeq;
    }

    public void setLocaleSeq(BigDecimal localeSeq) {
        this.localeSeq = localeSeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
