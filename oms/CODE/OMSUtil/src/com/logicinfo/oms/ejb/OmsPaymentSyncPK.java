package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsPaymentSyncPK implements Serializable {
    public BigDecimal lineNo;
    public BigDecimal location;
    public BigDecimal omsCustOrdNo;

    public OmsPaymentSyncPK() {
    }

    public OmsPaymentSyncPK(BigDecimal lineNo, BigDecimal location, BigDecimal omsCustOrdNo) {
        this.lineNo = lineNo;
        this.location = location;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsPaymentSyncPK) {
            final OmsPaymentSyncPK otherOmsPaymentSyncPK = (OmsPaymentSyncPK)other;
            final boolean areEqual =
                (otherOmsPaymentSyncPK.lineNo.equals(lineNo) && otherOmsPaymentSyncPK.location.equals(location) &&
                 otherOmsPaymentSyncPK.omsCustOrdNo.equals(omsCustOrdNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
