package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdItemPK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public BigDecimal omsCustOrdNo;

    public OmsCustOrdItemPK() {
    }

    public OmsCustOrdItemPK(String item, BigDecimal lineNo, BigDecimal omsCustOrdNo) {
        this.item = item;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdItemPK) {
            final OmsCustOrdItemPK otherOmsCustOrdItemPK = (OmsCustOrdItemPK)other;
            final boolean areEqual =
                (otherOmsCustOrdItemPK.item.equals(item) && otherOmsCustOrdItemPK.lineNo.equals(lineNo) &&
                 otherOmsCustOrdItemPK.omsCustOrdNo.equals(omsCustOrdNo));
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
}
