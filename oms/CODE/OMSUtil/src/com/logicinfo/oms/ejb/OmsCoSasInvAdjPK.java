package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoSasInvAdjPK implements Serializable {
    public String custOrderNo;
    public String item;
    public BigDecimal lineItemNo;
    public BigDecimal locationId;
    public BigDecimal omsCustOrdNo;
    public String subCustOrderNo;

    public OmsCoSasInvAdjPK() {
    }

    public OmsCoSasInvAdjPK(String custOrderNo, String item, BigDecimal lineItemNo, BigDecimal locationId,
                            BigDecimal omsCustOrdNo, String subCustOrderNo) {
        this.custOrderNo = custOrderNo;
        this.item = item;
        this.lineItemNo = lineItemNo;
        this.locationId = locationId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.subCustOrderNo = subCustOrderNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCoSasInvAdjPK) {
            final OmsCoSasInvAdjPK otherOmsCoSasInvAdjPK = (OmsCoSasInvAdjPK)other;
            final boolean areEqual =
                (otherOmsCoSasInvAdjPK.custOrderNo.equals(custOrderNo) && otherOmsCoSasInvAdjPK.item.equals(item) &&
                 otherOmsCoSasInvAdjPK.lineItemNo.equals(lineItemNo) &&
                 otherOmsCoSasInvAdjPK.locationId.equals(locationId) &&
                 otherOmsCoSasInvAdjPK.omsCustOrdNo.equals(omsCustOrdNo) &&
                 otherOmsCoSasInvAdjPK.subCustOrderNo.equals(subCustOrderNo));
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(String subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
