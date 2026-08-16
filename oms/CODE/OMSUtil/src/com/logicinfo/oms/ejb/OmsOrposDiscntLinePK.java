package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposDiscntLinePK implements Serializable {
    public BigDecimal capturedLineItemNo;
    public String itemId;
    public BigDecimal lineNo;
    public BigDecimal omsCustOrdNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposDiscntLinePK() {
    }

    public OmsOrposDiscntLinePK(BigDecimal capturedLineItemNo, String itemId, BigDecimal lineNo,
                                BigDecimal omsCustOrdNo, BigDecimal omsOrposCustOrderId) {
        this.capturedLineItemNo = capturedLineItemNo;
        this.itemId = itemId;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposDiscntLinePK) {
            final OmsOrposDiscntLinePK otherOmsOrposDiscntLinePK = (OmsOrposDiscntLinePK)other;
            final boolean areEqual =
                (otherOmsOrposDiscntLinePK.capturedLineItemNo.equals(capturedLineItemNo) && otherOmsOrposDiscntLinePK.itemId.equals(itemId) &&
                 otherOmsOrposDiscntLinePK.lineNo.equals(lineNo) &&
                 otherOmsOrposDiscntLinePK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsOrposDiscntLinePK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
