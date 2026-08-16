package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsRevsPickPK implements Serializable {
    public BigDecimal fulfillOrderNo;
    public String item;
    public BigDecimal omsCustOrdNo;
    public BigDecimal omsRevPicSeq;

    public OmsRevsPickPK() {
    }

    public OmsRevsPickPK(BigDecimal fulfillOrderNo, String item, BigDecimal omsCustOrdNo, BigDecimal omsRevPicSeq) {
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsRevPicSeq = omsRevPicSeq;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsRevsPickPK) {
            final OmsRevsPickPK otherOmsRevsPickPK = (OmsRevsPickPK)other;
            final boolean areEqual =
                (otherOmsRevsPickPK.fulfillOrderNo.equals(fulfillOrderNo) && otherOmsRevsPickPK.item.equals(item) &&
                 otherOmsRevsPickPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsRevsPickPK.omsRevPicSeq.equals(omsRevPicSeq));
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

    public BigDecimal getOmsRevPicSeq() {
        return omsRevPicSeq;
    }

    public void setOmsRevPicSeq(BigDecimal omsRevPicSeq) {
        this.omsRevPicSeq = omsRevPicSeq;
    }
}
