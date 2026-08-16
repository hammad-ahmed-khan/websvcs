package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrderPickupPK implements Serializable {
    public BigDecimal custOrderPicVoSeq;
    public String customerOrderId;

    public OmsOrposCustOrderPickupPK() {
    }

    public OmsOrposCustOrderPickupPK(BigDecimal custOrderPicVoSeq, String customerOrderId) {
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.customerOrderId = customerOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrderPickupPK) {
            final OmsOrposCustOrderPickupPK otherOmsOrposCustOrderPickupPK = (OmsOrposCustOrderPickupPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrderPickupPK.custOrderPicVoSeq.equals(custOrderPicVoSeq) && otherOmsOrposCustOrderPickupPK.customerOrderId.equals(customerOrderId));
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

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }
}
