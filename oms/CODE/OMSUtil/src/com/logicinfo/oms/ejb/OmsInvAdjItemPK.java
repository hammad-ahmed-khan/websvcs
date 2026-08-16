package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsInvAdjItemPK implements Serializable {
    public String item;
    public BigDecimal omsAdjReqId;

    public OmsInvAdjItemPK() {
    }

    public OmsInvAdjItemPK(String item, BigDecimal omsAdjReqId) {
        this.item = item;
        this.omsAdjReqId = omsAdjReqId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsInvAdjItemPK) {
            final OmsInvAdjItemPK otherOmsInvAdjItemPK = (OmsInvAdjItemPK)other;
            final boolean areEqual =
                (otherOmsInvAdjItemPK.item.equals(item) && otherOmsInvAdjItemPK.omsAdjReqId.equals(omsAdjReqId));
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

    public BigDecimal getOmsAdjReqId() {
        return omsAdjReqId;
    }

    public void setOmsAdjReqId(BigDecimal omsAdjReqId) {
        this.omsAdjReqId = omsAdjReqId;
    }
}
