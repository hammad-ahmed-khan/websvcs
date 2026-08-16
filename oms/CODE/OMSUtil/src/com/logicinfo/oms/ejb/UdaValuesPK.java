package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class UdaValuesPK implements Serializable {
    public BigDecimal udaId;
    public BigDecimal udaValue;

    public UdaValuesPK() {
    }

    public UdaValuesPK(BigDecimal udaId, BigDecimal udaValue) {
        this.udaId = udaId;
        this.udaValue = udaValue;
    }

    public boolean equals(Object other) {
        if (other instanceof UdaValuesPK) {
            final UdaValuesPK otherUdaValuesPK = (UdaValuesPK)other;
            final boolean areEqual =
                (otherUdaValuesPK.udaId.equals(udaId) && otherUdaValuesPK.udaValue.equals(udaValue));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
