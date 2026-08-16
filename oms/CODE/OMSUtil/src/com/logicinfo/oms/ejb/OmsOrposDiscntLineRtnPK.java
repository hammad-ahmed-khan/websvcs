package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposDiscntLineRtnPK implements Serializable {
    public BigDecimal custOrderRtnSeqNo;
    public BigDecimal lineItemNo;
    public BigDecimal lineNo;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposDiscntLineRtnPK() {
    }

    public OmsOrposDiscntLineRtnPK(BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo, BigDecimal lineNo,
                                   BigDecimal omsOrposCustOrderId) {
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.lineItemNo = lineItemNo;
        this.lineNo = lineNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposDiscntLineRtnPK) {
            final OmsOrposDiscntLineRtnPK otherOmsOrposDiscntLineRtnPK = (OmsOrposDiscntLineRtnPK)other;
            final boolean areEqual =
                (otherOmsOrposDiscntLineRtnPK.custOrderRtnSeqNo.equals(custOrderRtnSeqNo) && otherOmsOrposDiscntLineRtnPK.lineItemNo.equals(lineItemNo) &&
                 otherOmsOrposDiscntLineRtnPK.lineNo.equals(lineNo) &&
                 otherOmsOrposDiscntLineRtnPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
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

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
