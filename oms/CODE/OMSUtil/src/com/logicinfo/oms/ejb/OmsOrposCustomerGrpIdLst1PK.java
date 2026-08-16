package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustomerGrpIdLst1PK implements Serializable {
    public BigDecimal customerId;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposCustomerGrpIdLst1PK() {
    }

    public OmsOrposCustomerGrpIdLst1PK(BigDecimal customerId, BigDecimal omsOrposCustOrderId) {
        this.customerId = customerId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustomerGrpIdLst1PK) {
            final OmsOrposCustomerGrpIdLst1PK otherOmsOrposCustomerGrpIdLst1PK = (OmsOrposCustomerGrpIdLst1PK)other;
            final boolean areEqual =
                (otherOmsOrposCustomerGrpIdLst1PK.customerId.equals(customerId) && otherOmsOrposCustomerGrpIdLst1PK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
