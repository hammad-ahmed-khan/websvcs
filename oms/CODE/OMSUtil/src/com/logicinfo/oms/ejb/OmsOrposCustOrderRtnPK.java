package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrderRtnPK implements Serializable {
    public BigDecimal custOrderRtnSeqNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposCustOrderRtnPK() {
    }

    public OmsOrposCustOrderRtnPK(BigDecimal custOrderRtnSeqNo, BigDecimal omsOrposCustOrderId) {
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrderRtnPK) {
            final OmsOrposCustOrderRtnPK otherOmsOrposCustOrderRtnPK = (OmsOrposCustOrderRtnPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrderRtnPK.custOrderRtnSeqNo.equals(custOrderRtnSeqNo) && otherOmsOrposCustOrderRtnPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCustOrderRtnSeqNo() {
        return custOrderRtnSeqNo;
    }

    public void setCustOrderRtnSeqNo(BigDecimal custOrderRtnSeqNo) {
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
