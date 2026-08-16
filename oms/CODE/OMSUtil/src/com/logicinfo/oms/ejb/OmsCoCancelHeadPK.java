package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoCancelHeadPK implements Serializable {
    public BigDecimal cancelReqId;
    public String custOrdNo;
    public String subCustOrdNo;

    public OmsCoCancelHeadPK() {
    }

    public OmsCoCancelHeadPK(BigDecimal cancelReqId, String custOrdNo, String subCustOrdNo) {
        this.cancelReqId = cancelReqId;
        this.custOrdNo = custOrdNo;
        this.subCustOrdNo = subCustOrdNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoCancelHeadPK) {
            final OmsCoCancelHeadPK otherOmsCoCancelHeadPK = (OmsCoCancelHeadPK)other;
            final boolean areEqual =
                (otherOmsCoCancelHeadPK.cancelReqId.equals(cancelReqId) && otherOmsCoCancelHeadPK.custOrdNo.equals(custOrdNo) &&
                 otherOmsCoCancelHeadPK.subCustOrdNo.equals(subCustOrdNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCancelReqId() {
        return cancelReqId;
    }

    public void setCancelReqId(BigDecimal cancelReqId) {
        this.cancelReqId = cancelReqId;
    }

    public String getCustOrdNo() {
        return custOrdNo;
    }

    public void setCustOrdNo(String custOrdNo) {
        this.custOrdNo = custOrdNo;
    }

    public String getSubCustOrdNo() {
        return subCustOrdNo;
    }

    public void setSubCustOrdNo(String subCustOrdNo) {
        this.subCustOrdNo = subCustOrdNo;
    }
}
