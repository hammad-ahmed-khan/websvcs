package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrdItmPickupPK implements Serializable {
    public BigDecimal custOrderPicVoSeq;
    public BigDecimal lineItemNo;

    public OmsOrposCustOrdItmPickupPK() {
    }

    public OmsOrposCustOrdItmPickupPK(BigDecimal custOrderPicVoSeq, BigDecimal lineItemNo) {
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.lineItemNo = lineItemNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrdItmPickupPK) {
            final OmsOrposCustOrdItmPickupPK otherOmsOrposCustOrdItmPickupPK = (OmsOrposCustOrdItmPickupPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrdItmPickupPK.custOrderPicVoSeq.equals(custOrderPicVoSeq) && otherOmsOrposCustOrdItmPickupPK.lineItemNo.equals(lineItemNo));
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
}
