package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsRmaModDetailPK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public String rmaModReqId;

    public OmsRmaModDetailPK() {
    }

    public OmsRmaModDetailPK(String item, BigDecimal lineNo, String rmaModReqId) {
        this.item = item;
        this.lineNo = lineNo;
        this.rmaModReqId = rmaModReqId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsRmaModDetailPK) {
            final OmsRmaModDetailPK otherOmsRmaModDetailPK = (OmsRmaModDetailPK)other;
            final boolean areEqual =
                (otherOmsRmaModDetailPK.item.equals(item) && otherOmsRmaModDetailPK.lineNo.equals(lineNo) &&
                 otherOmsRmaModDetailPK.rmaModReqId.equals(rmaModReqId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public String getRmaModReqId() {
        return rmaModReqId;
    }

    public void setRmaModReqId(String rmaModReqId) {
        this.rmaModReqId = rmaModReqId;
    }
}
