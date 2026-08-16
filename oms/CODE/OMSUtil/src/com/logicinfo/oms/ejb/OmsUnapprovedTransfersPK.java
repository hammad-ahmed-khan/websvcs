package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsUnapprovedTransfersPK implements Serializable {
    public String item;
    public BigDecimal location;
    public BigDecimal omsCustOrdNo;
    public BigDecimal tsfNo;

    public OmsUnapprovedTransfersPK() {
    }

    public OmsUnapprovedTransfersPK(String item, BigDecimal location, BigDecimal omsCustOrdNo, BigDecimal tsfNo) {
        this.item = item;
        this.location = location;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tsfNo = tsfNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsUnapprovedTransfersPK) {
            final OmsUnapprovedTransfersPK otherOmsUnapprovedTransfersPK = (OmsUnapprovedTransfersPK)other;
            final boolean areEqual =
                (otherOmsUnapprovedTransfersPK.item.equals(item) && otherOmsUnapprovedTransfersPK.location.equals(location) &&
                 otherOmsUnapprovedTransfersPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsUnapprovedTransfersPK.tsfNo.equals(tsfNo));
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

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }
}
