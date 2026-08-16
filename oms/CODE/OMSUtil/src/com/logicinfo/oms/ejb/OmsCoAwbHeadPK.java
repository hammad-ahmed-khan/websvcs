package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoAwbHeadPK implements Serializable {
    public String custOrderNo;
    public BigDecimal subCustOrderNo;

    public OmsCoAwbHeadPK() {
    }

    public OmsCoAwbHeadPK(String custOrderNo, BigDecimal subCustOrderNo) {
        this.custOrderNo = custOrderNo;
        this.subCustOrderNo = subCustOrderNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoAwbHeadPK) {
            final OmsCoAwbHeadPK otherOmsCoAwbHeadPK = (OmsCoAwbHeadPK)other;
            final boolean areEqual =
                (otherOmsCoAwbHeadPK.custOrderNo.equals(custOrderNo) && otherOmsCoAwbHeadPK.subCustOrderNo.equals(subCustOrderNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public BigDecimal getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(BigDecimal subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
