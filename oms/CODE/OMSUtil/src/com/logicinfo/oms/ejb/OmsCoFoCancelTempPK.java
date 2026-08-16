package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoFoCancelTempPK implements Serializable {
    public BigDecimal fulfillOrderNo;
    public String item;
    public BigDecimal omsCancelId;

    public OmsCoFoCancelTempPK() {
    }

    public OmsCoFoCancelTempPK(BigDecimal fulfillOrderNo, String item, BigDecimal omsCancelId) {
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.omsCancelId = omsCancelId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoFoCancelTempPK) {
            final OmsCoFoCancelTempPK otherOmsCoFoCancelTempPK = (OmsCoFoCancelTempPK)other;
            final boolean areEqual =
                (otherOmsCoFoCancelTempPK.fulfillOrderNo.equals(fulfillOrderNo) && otherOmsCoFoCancelTempPK.item.equals(item) &&
                 otherOmsCoFoCancelTempPK.omsCancelId.equals(omsCancelId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }
}
