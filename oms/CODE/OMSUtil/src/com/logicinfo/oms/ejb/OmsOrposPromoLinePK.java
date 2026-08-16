package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposPromoLinePK implements Serializable {
    public BigDecimal capturedLineItemNo;
    public String itemId;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposPromoLinePK() {
    }

    public OmsOrposPromoLinePK(BigDecimal capturedLineItemNo, String itemId, BigDecimal omsOrposCustOrderId) {
        this.capturedLineItemNo = capturedLineItemNo;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposPromoLinePK) {
            final OmsOrposPromoLinePK otherOmsOrposPromoLinePK = (OmsOrposPromoLinePK)other;
            final boolean areEqual =
                (otherOmsOrposPromoLinePK.capturedLineItemNo.equals(capturedLineItemNo) && otherOmsOrposPromoLinePK.itemId.equals(itemId) &&
                 otherOmsOrposPromoLinePK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
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

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
