package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrdDelPK implements Serializable {
    public BigDecimal custOrdDelId;
    public BigDecimal custOrdDelSeqNo;

    public OmsOrposCustOrdDelPK() {
    }

    public OmsOrposCustOrdDelPK(BigDecimal custOrdDelId, BigDecimal custOrdDelSeqNo) {
        this.custOrdDelId = custOrdDelId;
        this.custOrdDelSeqNo = custOrdDelSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrdDelPK) {
            final OmsOrposCustOrdDelPK otherOmsOrposCustOrdDelPK = (OmsOrposCustOrdDelPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrdDelPK.custOrdDelId.equals(custOrdDelId) && otherOmsOrposCustOrdDelPK.custOrdDelSeqNo.equals(custOrdDelSeqNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCustOrdDelId() {
        return custOrdDelId;
    }

    public void setCustOrdDelId(BigDecimal custOrdDelId) {
        this.custOrdDelId = custOrdDelId;
    }

    public BigDecimal getCustOrdDelSeqNo() {
        return custOrdDelSeqNo;
    }

    public void setCustOrdDelSeqNo(BigDecimal custOrdDelSeqNo) {
        this.custOrdDelSeqNo = custOrdDelSeqNo;
    }
}
