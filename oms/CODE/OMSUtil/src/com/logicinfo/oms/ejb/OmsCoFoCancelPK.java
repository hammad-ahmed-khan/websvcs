package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoFoCancelPK implements Serializable {
    public BigDecimal fulfillOrderNo;
    public String item;
    public BigDecimal lineNo;
    public BigDecimal omsCancelId;

    public OmsCoFoCancelPK()
    {
    }

    public OmsCoFoCancelPK(BigDecimal fulfillOrderNo, String item, BigDecimal lineNo, BigDecimal omsCancelId)
    {
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.lineNo = lineNo;
        this.omsCancelId = omsCancelId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoFoCancelPK) {
            final OmsCoFoCancelPK otherOmsCoFoCancelPK = (OmsCoFoCancelPK)other;
            final boolean areEqual =
                (otherOmsCoFoCancelPK.fulfillOrderNo.equals(fulfillOrderNo) && otherOmsCoFoCancelPK.item.equals(item) &&
                 otherOmsCoFoCancelPK.lineNo.equals(lineNo) && otherOmsCoFoCancelPK.omsCancelId.equals(omsCancelId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
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

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }
}
