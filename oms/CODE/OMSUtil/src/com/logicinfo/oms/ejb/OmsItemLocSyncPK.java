package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsItemLocSyncPK implements Serializable {
    public String item;
    public BigDecimal loc;

    public OmsItemLocSyncPK() {
    }

    public OmsItemLocSyncPK(String item, BigDecimal loc) {
        this.item = item;
        this.loc = loc;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsItemLocSyncPK) {
            final OmsItemLocSyncPK otherOmsItemLocSyncPK = (OmsItemLocSyncPK)other;
            final boolean areEqual =
                (otherOmsItemLocSyncPK.item.equals(item) && otherOmsItemLocSyncPK.loc.equals(loc));
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

    public BigDecimal getLoc() {
        return loc;
    }

    public void setLoc(BigDecimal loc) {
        this.loc = loc;
    }
}
