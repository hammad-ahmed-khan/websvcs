package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class TsfdetailPK implements Serializable {
    public BigDecimal tsfNo;
    public BigDecimal tsfSeqNo;

    public TsfdetailPK() {
    }

    public TsfdetailPK(BigDecimal tsfNo, BigDecimal tsfSeqNo) {
        this.tsfNo = tsfNo;
        this.tsfSeqNo = tsfSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof TsfdetailPK) {
            final TsfdetailPK otherTsfdetailPK = (TsfdetailPK)other;
            final boolean areEqual =
                (otherTsfdetailPK.tsfNo.equals(tsfNo) && otherTsfdetailPK.tsfSeqNo.equals(tsfSeqNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public BigDecimal getTsfSeqNo() {
        return tsfSeqNo;
    }

    public void setTsfSeqNo(BigDecimal tsfSeqNo) {
        this.tsfSeqNo = tsfSeqNo;
    }
}
