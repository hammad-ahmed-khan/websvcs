package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdTenderPK implements Serializable {
    public BigDecimal omsCustOrdNo;
    public BigDecimal tenderSeqNo;

    public OmsCustOrdTenderPK() {
    }

    public OmsCustOrdTenderPK(BigDecimal omsCustOrdNo, BigDecimal tenderSeqNo) {
        this.omsCustOrdNo = omsCustOrdNo;
        this.tenderSeqNo = tenderSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdTenderPK) {
            final OmsCustOrdTenderPK otherOmsCustOrdTenderPK = (OmsCustOrdTenderPK)other;
            final boolean areEqual =
                (otherOmsCustOrdTenderPK.omsCustOrdNo.equals(omsCustOrdNo) && otherOmsCustOrdTenderPK.tenderSeqNo.equals(tenderSeqNo));
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
