package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdReservePK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public BigDecimal rmsResvLoc;
    public BigDecimal omsCustOrdNo;

    public OmsCustOrdReservePK() {
    }

    public OmsCustOrdReservePK(String item, BigDecimal lineNo, BigDecimal rmsResvLoc, BigDecimal omsCustOrdNo) {
        this.item = item;
        this.lineNo = lineNo;
        this.rmsResvLoc = rmsResvLoc;
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdReservePK) {
            final OmsCustOrdReservePK otherOmsCustOrdReservePK = (OmsCustOrdReservePK)other;
            final boolean areEqual =
                (otherOmsCustOrdReservePK.item.equals(item) && otherOmsCustOrdReservePK.lineNo.equals(lineNo) &&
                 otherOmsCustOrdReservePK.rmsResvLoc.equals(rmsResvLoc) &&
                 otherOmsCustOrdReservePK.omsCustOrdNo.equals(omsCustOrdNo));
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

    public BigDecimal getRmsResvLoc() {
        return rmsResvLoc;
    }

    public void setLoc(BigDecimal rmsResvLoc) {
        this.rmsResvLoc = rmsResvLoc;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }
}
