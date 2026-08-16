package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoFulfillDetailPK implements Serializable {
    public BigDecimal fulfillOrderNo;
    public String item;
    public BigDecimal omsCustOrdNo;

    public OmsCoFulfillDetailPK() {
    }

    public OmsCoFulfillDetailPK(BigDecimal fulfillOrderNo, String item, BigDecimal omsCustOrdNo) {
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoFulfillDetailPK) {
            final OmsCoFulfillDetailPK otherOmsCoFulfillDetailPK = (OmsCoFulfillDetailPK)other;
            final boolean areEqual =
                (otherOmsCoFulfillDetailPK.fulfillOrderNo.equals(fulfillOrderNo) && otherOmsCoFulfillDetailPK.item.equals(item) &&
                 otherOmsCoFulfillDetailPK.omsCustOrdNo.equals(omsCustOrdNo));
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
