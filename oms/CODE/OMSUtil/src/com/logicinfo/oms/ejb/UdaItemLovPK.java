package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class UdaItemLovPK implements Serializable {
    public String item;
    public BigDecimal udaId;
    public BigDecimal udaValue;

    public UdaItemLovPK() {
    }

    public UdaItemLovPK(String item, BigDecimal udaId, BigDecimal udaValue) {
        this.item = item;
        this.udaId = udaId;
        this.udaValue = udaValue;
    }

    public boolean equals(Object other) {
        if (other instanceof UdaItemLovPK) {
            final UdaItemLovPK otherUdaItemLovPK = (UdaItemLovPK)other;
            final boolean areEqual =
                (otherUdaItemLovPK.item.equals(item) && otherUdaItemLovPK.udaId.equals(udaId) && otherUdaItemLovPK.udaValue.equals(udaValue));
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

    public BigDecimal getUdaId() {
        return udaId;
    }

    public void setUdaId(BigDecimal udaId) {
        this.udaId = udaId;
    }

    public BigDecimal getUdaValue() {
        return udaValue;
    }

    public void setUdaValue(BigDecimal udaValue) {
        this.udaValue = udaValue;
    }
}
