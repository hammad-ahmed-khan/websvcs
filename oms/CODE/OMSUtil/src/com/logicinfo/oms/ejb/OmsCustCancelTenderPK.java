package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustCancelTenderPK implements Serializable {
    public BigDecimal omsCancelId;
    public BigDecimal tenderSeqNo;

    public OmsCustCancelTenderPK() {
    }

    public OmsCustCancelTenderPK(BigDecimal omsCancelId, BigDecimal tenderSeqNo) {
        this.omsCancelId = omsCancelId;
        this.tenderSeqNo = tenderSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustCancelTenderPK) {
            final OmsCustCancelTenderPK otherOmsCustCancelTenderPK = (OmsCustCancelTenderPK)other;
            final boolean areEqual =
                (otherOmsCustCancelTenderPK.omsCancelId.equals(omsCancelId) && otherOmsCustCancelTenderPK.tenderSeqNo.equals(tenderSeqNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }

    public BigDecimal getTenderSeqNo() {
        return tenderSeqNo;
    }

    public void setTenderSeqNo(BigDecimal tenderSeqNo) {
        this.tenderSeqNo = tenderSeqNo;
    }
}
