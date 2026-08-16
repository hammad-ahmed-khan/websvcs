package com.logicinfo.oms.ejb;
import java.io.Serializable;

import java.math.BigDecimal;

public class OmsTsfCancelledQtySummPK implements Serializable {
    public String custOrderNo;
    public BigDecimal omsCustOrdNo;
    public BigDecimal tsfNo;

    public OmsTsfCancelledQtySummPK() {
    }

    public OmsTsfCancelledQtySummPK(String custOrderNo, BigDecimal omsCustOrdNo, BigDecimal tsfNo) {
        this.custOrderNo = custOrderNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tsfNo = tsfNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsTsfCancelledQtySummPK) {
            final OmsTsfCancelledQtySummPK otherOmsTsfCancelledQtySummPK = (OmsTsfCancelledQtySummPK)other;
            final boolean areEqual =
                (otherOmsTsfCancelledQtySummPK.custOrderNo.equals(custOrderNo) && otherOmsTsfCancelledQtySummPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsTsfCancelledQtySummPK.tsfNo.equals(tsfNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }
}
