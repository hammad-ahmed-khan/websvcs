package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposCustOrdItmRtnPK implements Serializable {
    public BigDecimal custOrderRtnSeqNo;
    public BigDecimal lineItemNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposCustOrdItmRtnPK() {
    }

    public OmsOrposCustOrdItmRtnPK(BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo,
                                   BigDecimal omsOrposCustOrderId) {
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.lineItemNo = lineItemNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCustOrdItmRtnPK) {
            final OmsOrposCustOrdItmRtnPK otherOmsOrposCustOrdItmRtnPK = (OmsOrposCustOrdItmRtnPK)other;
            final boolean areEqual =
                (otherOmsOrposCustOrdItmRtnPK.custOrderRtnSeqNo.equals(custOrderRtnSeqNo) && otherOmsOrposCustOrdItmRtnPK.lineItemNo.equals(lineItemNo) &&
                 otherOmsOrposCustOrdItmRtnPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
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

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
