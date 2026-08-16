package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsResUnresvCustOrderLogPK implements Serializable {
    public BigDecimal logId;
    public BigDecimal omsCustOrdNo;

    public OmsResUnresvCustOrderLogPK() {
    }

    public OmsResUnresvCustOrderLogPK(BigDecimal logId, BigDecimal omsCustOrdNo) {
        this.logId = logId;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsResUnresvCustOrderLogPK) {
            final OmsResUnresvCustOrderLogPK otherOmsResUnresvCustOrderLogPK = (OmsResUnresvCustOrderLogPK)other;
            final boolean areEqual =
                (otherOmsResUnresvCustOrderLogPK.logId.equals(logId) && otherOmsResUnresvCustOrderLogPK.omsCustOrdNo.equals(omsCustOrdNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
