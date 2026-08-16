package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsRmaReqPK implements Serializable {
    public BigDecimal omsCustOrdNo;
    public String rmaReqId;

    public OmsRmaReqPK() {
    }

    public OmsRmaReqPK(BigDecimal omsCustOrdNo, String rmaReqId) {
        this.omsCustOrdNo = omsCustOrdNo;
        this.rmaReqId = rmaReqId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsRmaReqPK) {
            final OmsRmaReqPK otherOmsRmaReqPK = (OmsRmaReqPK)other;
            final boolean areEqual =
                (otherOmsRmaReqPK.omsCustOrdNo.equals(omsCustOrdNo) && otherOmsRmaReqPK.rmaReqId.equals(rmaReqId));
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

    public String getRmaReqId() {
        return rmaReqId;
    }

    public void setRmaReqId(String rmaReqId) {
        this.rmaReqId = rmaReqId;
    }
}
