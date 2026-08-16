package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

public class OmsOrdItemTenderSplitPK implements Serializable {
    public String item;
    public BigDecimal omsCustOrdNo;
    public BigDecimal tenderSeqNo;
    public BigDecimal lineNo;

    public OmsOrdItemTenderSplitPK() {
    }

    public OmsOrdItemTenderSplitPK(String item, BigDecimal omsCustOrdNo, BigDecimal tenderSeqNo,BigDecimal lineNo) {
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tenderSeqNo = tenderSeqNo;
        this.lineNo=lineNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrdItemTenderSplitPK) {
            final OmsOrdItemTenderSplitPK otherOmsOrdItemTenderSplitPK = (OmsOrdItemTenderSplitPK)other;
            final boolean areEqual =
                (otherOmsOrdItemTenderSplitPK.item.equals(item) && otherOmsOrdItemTenderSplitPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsOrdItemTenderSplitPK.tenderSeqNo.equals(tenderSeqNo));
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTenderSeqNo() {
        return tenderSeqNo;
    }

    public void setTenderSeqNo(BigDecimal tenderSeqNo) {
        this.tenderSeqNo = tenderSeqNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }
}
