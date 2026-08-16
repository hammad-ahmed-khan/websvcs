package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsRmaDelDetailPK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public String rmaDelReqId;

    public OmsRmaDelDetailPK() {
    }

    public OmsRmaDelDetailPK(String item, BigDecimal lineNo, String rmaDelReqId) {
        this.item = item;
        this.lineNo = lineNo;
        this.rmaDelReqId = rmaDelReqId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsRmaDelDetailPK) {
            final OmsRmaDelDetailPK otherOmsRmaDelDetailPK = (OmsRmaDelDetailPK)other;
            final boolean areEqual =
                (otherOmsRmaDelDetailPK.item.equals(item) && otherOmsRmaDelDetailPK.lineNo.equals(lineNo) &&
                 otherOmsRmaDelDetailPK.rmaDelReqId.equals(rmaDelReqId));
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

    public String getRmaDelReqId() {
        return rmaDelReqId;
    }

    public void setRmaDelReqId(String rmaDelReqId) {
        this.rmaDelReqId = rmaDelReqId;
    }
}
