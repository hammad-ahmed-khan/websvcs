package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsBackOrderDtlPK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public BigDecimal omsCustOrdNo;
    public BigDecimal sourceLoc;

    public OmsBackOrderDtlPK() {
    }

    public OmsBackOrderDtlPK(String item, BigDecimal lineNo, BigDecimal omsCustOrdNo, BigDecimal sourceLoc) {
        this.item = item;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.sourceLoc = sourceLoc;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsBackOrderDtlPK) {
            final OmsBackOrderDtlPK otherOmsBackOrderDtlPK = (OmsBackOrderDtlPK)other;
            final boolean areEqual =
                (otherOmsBackOrderDtlPK.item.equals(item) && otherOmsBackOrderDtlPK.lineNo.equals(lineNo) &&
                 otherOmsBackOrderDtlPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsBackOrderDtlPK.sourceLoc.equals(sourceLoc));
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

    public BigDecimal getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(BigDecimal sourceLoc) {
        this.sourceLoc = sourceLoc;
    }
}
