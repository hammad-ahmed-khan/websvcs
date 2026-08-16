package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrdFulPK implements Serializable {
    public BigDecimal custOrdFulSeqNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposCustOrdFulPK() {
    }

    public OmsOrposCustOrdFulPK(BigDecimal custOrdFulSeqNo, BigDecimal omsOrposCustOrderId) {
        this.custOrdFulSeqNo = custOrdFulSeqNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrdFulPK) {
            final OmsOrposCustOrdFulPK otherOmsOrposCustOrdFulPK = (OmsOrposCustOrdFulPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrdFulPK.custOrdFulSeqNo.equals(custOrdFulSeqNo) && otherOmsOrposCustOrdFulPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCustOrdFulSeqNo() {
        return custOrdFulSeqNo;
    }

    public void setCustOrdFulSeqNo(BigDecimal custOrdFulSeqNo) {
        this.custOrdFulSeqNo = custOrdFulSeqNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
