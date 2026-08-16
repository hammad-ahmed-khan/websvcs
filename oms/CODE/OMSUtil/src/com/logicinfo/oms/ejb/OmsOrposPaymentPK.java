package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposPaymentPK implements Serializable {
    public BigDecimal omsOrposCustOrderId;
    public BigDecimal paymentSeqNo;

    public OmsOrposPaymentPK() {
    }

    public OmsOrposPaymentPK(BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposPaymentPK) {
            final OmsOrposPaymentPK otherOmsOrposPaymentPK = (OmsOrposPaymentPK)other;
            final boolean areEqual =
                (otherOmsOrposPaymentPK.omsOrposCustOrderId.equals(omsOrposCustOrderId) && otherOmsOrposPaymentPK.paymentSeqNo.equals(paymentSeqNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }
}
