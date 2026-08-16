package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdItemDiscPK implements Serializable {
    public BigDecimal discLineNo;
    public BigDecimal lineNo;
    public BigDecimal omsCustOrdNo;

    public OmsCustOrdItemDiscPK() {
    }

    public OmsCustOrdItemDiscPK(BigDecimal discLineNo, BigDecimal lineNo, BigDecimal omsCustOrdNo) {
        this.discLineNo = discLineNo;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdItemDiscPK) {
            final OmsCustOrdItemDiscPK otherOmsCustOrdItemDiscPK = (OmsCustOrdItemDiscPK)other;
            final boolean areEqual =
                (otherOmsCustOrdItemDiscPK.discLineNo.equals(discLineNo) && otherOmsCustOrdItemDiscPK.lineNo.equals(lineNo) &&
                 otherOmsCustOrdItemDiscPK.omsCustOrdNo.equals(omsCustOrdNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getDiscLineNo() {
        return discLineNo;
    }

    public void setDiscLineNo(BigDecimal discLineNo) {
        this.discLineNo = discLineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
