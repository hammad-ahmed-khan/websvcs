package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsStsTrfItemPK implements Serializable {
    public String item;
    public BigDecimal omsTrfReqId;

    public OmsStsTrfItemPK() {
    }

    public OmsStsTrfItemPK(String item, BigDecimal omsTrfReqId) {
        this.item = item;
        this.omsTrfReqId = omsTrfReqId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsStsTrfItemPK) {
            final OmsStsTrfItemPK otherOmsStsTrfItemPK = (OmsStsTrfItemPK)other;
            final boolean areEqual =
                (otherOmsStsTrfItemPK.item.equals(item) && otherOmsStsTrfItemPK.omsTrfReqId.equals(omsTrfReqId));
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

    public BigDecimal getOmsTrfReqId() {
        return omsTrfReqId;
    }

    public void setOmsTrfReqId(BigDecimal omsTrfReqId) {
        this.omsTrfReqId = omsTrfReqId;
    }
}
