package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoCancelItemPK implements Serializable {
    public String item;
    public BigDecimal lineNo;
    public BigDecimal omsCancelId;

    public OmsCoCancelItemPK() {
    }

    public OmsCoCancelItemPK(String item, BigDecimal lineNo, BigDecimal omsCancelId) {
        this.item = item;
        this.lineNo = lineNo;
        this.omsCancelId = omsCancelId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoCancelItemPK) {
            final OmsCoCancelItemPK otherOmsCoCancelItemPK = (OmsCoCancelItemPK)other;
            final boolean areEqual =
                (otherOmsCoCancelItemPK.item.equals(item) && otherOmsCoCancelItemPK.lineNo.equals(lineNo) &&
                 otherOmsCoCancelItemPK.omsCancelId.equals(omsCancelId));
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

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }
}
