package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsRmaReqItemPK implements Serializable {
    public String item;
    public BigDecimal rmaId;

    public OmsRmaReqItemPK() {
    }

    public OmsRmaReqItemPK(String item, BigDecimal rmaId) {
        this.item = item;
        this.rmaId = rmaId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsRmaReqItemPK) {
            final OmsRmaReqItemPK otherOmsRmaReqItemPK = (OmsRmaReqItemPK)other;
            final boolean areEqual =
                (otherOmsRmaReqItemPK.item.equals(item) && otherOmsRmaReqItemPK.rmaId.equals(rmaId));
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

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }
}
