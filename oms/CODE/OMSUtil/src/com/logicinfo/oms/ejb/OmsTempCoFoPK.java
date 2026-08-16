package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsTempCoFoPK implements Serializable {
    public String item;
    public BigDecimal omsCustOrdNo;
    public BigDecimal sourceLocId;

    public OmsTempCoFoPK() {
    }

    public OmsTempCoFoPK(String item, BigDecimal omsCustOrdNo, BigDecimal sourceLocId) {
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
        this.sourceLocId = sourceLocId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsTempCoFoPK) {
            final OmsTempCoFoPK otherOmsTempCoFoPK = (OmsTempCoFoPK)other;
            final boolean areEqual =
                (otherOmsTempCoFoPK.item.equals(item) && otherOmsTempCoFoPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsTempCoFoPK.sourceLocId.equals(sourceLocId));
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getSourceLocId() {
        return sourceLocId;
    }

    public void setSourceLocId(BigDecimal sourceLocId) {
        this.sourceLocId = sourceLocId;
    }
}
