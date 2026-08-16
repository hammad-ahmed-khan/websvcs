package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdSadadPK implements Serializable {
    public String item;
    public BigDecimal loc;
    public BigDecimal omsCustOrdNo;

    public OmsCustOrdSadadPK() {
    }

    public OmsCustOrdSadadPK(String item, BigDecimal loc, BigDecimal omsCustOrdNo) {
        this.item = item;
        this.loc = loc;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdSadadPK) {
            final OmsCustOrdSadadPK otherOmsCustOrdSadadPK = (OmsCustOrdSadadPK)other;
            final boolean areEqual =
                (otherOmsCustOrdSadadPK.item.equals(item) && otherOmsCustOrdSadadPK.loc.equals(loc) &&
                 otherOmsCustOrdSadadPK.omsCustOrdNo.equals(omsCustOrdNo));
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
