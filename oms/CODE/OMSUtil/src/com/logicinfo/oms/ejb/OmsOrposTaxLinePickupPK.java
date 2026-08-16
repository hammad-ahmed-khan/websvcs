package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposTaxLinePickupPK implements Serializable {
    public BigDecimal custOrderPicVoSeq;
    public BigDecimal lineItemNo;
    public BigDecimal lineNo;

    public OmsOrposTaxLinePickupPK() {
    }

    public OmsOrposTaxLinePickupPK(BigDecimal custOrderPicVoSeq, BigDecimal lineItemNo, BigDecimal lineNo) {
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.lineItemNo = lineItemNo;
        this.lineNo = lineNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposTaxLinePickupPK) {
            final OmsOrposTaxLinePickupPK otherOmsOrposTaxLinePickupPK = (OmsOrposTaxLinePickupPK)other;
            final boolean areEqual =
                (otherOmsOrposTaxLinePickupPK.custOrderPicVoSeq.equals(custOrderPicVoSeq) && otherOmsOrposTaxLinePickupPK.lineItemNo.equals(lineItemNo) &&
                 otherOmsOrposTaxLinePickupPK.lineNo.equals(lineNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCustOrderPicVoSeq() {
        return custOrderPicVoSeq;
    }

    public void setCustOrderPicVoSeq(BigDecimal custOrderPicVoSeq) {
        this.custOrderPicVoSeq = custOrderPicVoSeq;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }
}

