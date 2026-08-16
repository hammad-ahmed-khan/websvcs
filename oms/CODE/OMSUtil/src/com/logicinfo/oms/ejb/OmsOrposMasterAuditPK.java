package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposMasterAuditPK implements Serializable {
    public BigDecimal lineItemNo;
    public BigDecimal omsCustOrderNo;
    public String orderId;
    public String orposTransactionNumber;

    public OmsOrposMasterAuditPK() {
    }

    public OmsOrposMasterAuditPK(BigDecimal lineItemNo, BigDecimal omsCustOrderNo, String orderId,
                                 String orposTransactionNumber) {
        this.lineItemNo = lineItemNo;
        this.omsCustOrderNo = omsCustOrderNo;
        this.orderId = orderId;
        this.orposTransactionNumber = orposTransactionNumber;
    }

   

    public boolean equals(Object other) {
        if (other instanceof OmsOrposMasterAuditPK) {
            final OmsOrposMasterAuditPK otherOmsOrposMasterAuditPK = (OmsOrposMasterAuditPK)other;
            final boolean areEqual =
                (otherOmsOrposMasterAuditPK.lineItemNo.equals(lineItemNo) && otherOmsOrposMasterAuditPK.omsCustOrderNo.equals(omsCustOrderNo) &&
                 otherOmsOrposMasterAuditPK.orderId.equals(orderId) &&
                 otherOmsOrposMasterAuditPK.orposTransactionNumber.equals(orposTransactionNumber));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getOmsCustOrderNo() {
        return omsCustOrderNo;
    }

    public void setOmsCustOrderNo(BigDecimal omsCustOrderNo) {
        this.omsCustOrderNo = omsCustOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrposTransactionNumber() {
        return orposTransactionNumber;
    }

    public void setOrposTransactionNumber(String orposTransactionNumber) {
        this.orposTransactionNumber = orposTransactionNumber;
    }
}
