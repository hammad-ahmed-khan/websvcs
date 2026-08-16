package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposTaxLinePK implements Serializable {
    public BigDecimal capturedLineItemNo;
    public String itemId;
    public BigDecimal lineNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposTaxLinePK() {
    }

    public OmsOrposTaxLinePK(BigDecimal capturedLineItemNo, String itemId, BigDecimal lineNo,
                             BigDecimal omsOrposCustOrderId) {
        this.capturedLineItemNo = capturedLineItemNo;
        this.itemId = itemId;
        this.lineNo = lineNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposTaxLinePK) {
            final OmsOrposTaxLinePK otherOmsOrposTaxLinePK = (OmsOrposTaxLinePK)other;
            final boolean areEqual =
                (otherOmsOrposTaxLinePK.capturedLineItemNo.equals(capturedLineItemNo) && otherOmsOrposTaxLinePK.itemId.equals(itemId) &&
                 otherOmsOrposTaxLinePK.lineNo.equals(lineNo) &&
                 otherOmsOrposTaxLinePK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
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

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
