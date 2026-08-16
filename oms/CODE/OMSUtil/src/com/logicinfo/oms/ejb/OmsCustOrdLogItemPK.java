package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCustOrdLogItemPK implements Serializable {
    public String item;
    public BigDecimal logSeqNo;

    public OmsCustOrdLogItemPK() {
    }

    public OmsCustOrdLogItemPK(String item, BigDecimal logSeqNo) {
        this.item = item;
        this.logSeqNo = logSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdLogItemPK) {
            final OmsCustOrdLogItemPK otherOmsCustOrdLogItemPK = (OmsCustOrdLogItemPK)other;
            final boolean areEqual =
                (otherOmsCustOrdLogItemPK.item.equals(item) && otherOmsCustOrdLogItemPK.logSeqNo.equals(logSeqNo));
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

    public BigDecimal getLogSeqNo() {
        return logSeqNo;
    }

    public void setLogSeqNo(BigDecimal logSeqNo) {
        this.logSeqNo = logSeqNo;
    }
}
