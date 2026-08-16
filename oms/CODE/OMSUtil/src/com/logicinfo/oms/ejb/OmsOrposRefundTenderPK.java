package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposRefundTenderPK implements Serializable {
    public BigDecimal omsCustOrdNo;
    public BigDecimal tenderSeqNo;

    public OmsOrposRefundTenderPK() {
    }

    public OmsOrposRefundTenderPK(BigDecimal omsCustOrdNo, BigDecimal tenderSeqNo) {
        this.omsCustOrdNo = omsCustOrdNo;
        this.tenderSeqNo = tenderSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposRefundTenderPK) {
            final OmsOrposRefundTenderPK otherOmsOrposRefundTenderPK = (OmsOrposRefundTenderPK)other;
            final boolean areEqual =
                (otherOmsOrposRefundTenderPK.omsCustOrdNo.equals(omsCustOrdNo) && otherOmsOrposRefundTenderPK.tenderSeqNo.equals(tenderSeqNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTenderSeqNo() {
        return tenderSeqNo;
    }

    public void setTenderSeqNo(BigDecimal tenderSeqNo) {
        this.tenderSeqNo = tenderSeqNo;
    }
}
