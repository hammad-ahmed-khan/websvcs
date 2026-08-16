package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsSparePartFulfillPK implements Serializable {
    public String omsServiceReqSeqId;
    public BigDecimal sourceLocation;
    public BigDecimal tranId;

    public OmsSparePartFulfillPK() {
    }

    public OmsSparePartFulfillPK(String omsServiceReqSeqId, BigDecimal sourceLocation, BigDecimal tranId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
        this.sourceLocation = sourceLocation;
        this.tranId = tranId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsSparePartFulfillPK) {
            final OmsSparePartFulfillPK otherOmsSparePartFulfillPK = (OmsSparePartFulfillPK)other;
            final boolean areEqual =
                (otherOmsSparePartFulfillPK.omsServiceReqSeqId.equals(omsServiceReqSeqId) && otherOmsSparePartFulfillPK.sourceLocation.equals(sourceLocation) &&
                 otherOmsSparePartFulfillPK.tranId.equals(tranId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getOmsServiceReqSeqId() {
        return omsServiceReqSeqId;
    }

    public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
    }

    public BigDecimal getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(BigDecimal sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public BigDecimal getTranId() {
        return tranId;
    }

    public void setTranId(BigDecimal tranId) {
        this.tranId = tranId;
    }
}
