package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsCustOrdHeadPK implements Serializable {
    public String applicationId;
    public String custOrderNo;
    public String entityId;
    public String subCustOrderNo;

    public OmsCustOrdHeadPK() {
    }

    public OmsCustOrdHeadPK(String applicationId, String custOrderNo, String entityId, String subCustOrderNo) {
        this.applicationId = applicationId;
        this.custOrderNo = custOrderNo;
        this.entityId = entityId;
        this.subCustOrderNo = subCustOrderNo;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsCustOrdHeadPK) {
            final OmsCustOrdHeadPK otherOmsCustOrdHeadPK = (OmsCustOrdHeadPK)other;
            final boolean areEqual =
                (otherOmsCustOrdHeadPK.applicationId.equals(applicationId) && otherOmsCustOrdHeadPK.custOrderNo.equals(custOrderNo) &&
                 otherOmsCustOrdHeadPK.entityId.equals(entityId) &&
                 otherOmsCustOrdHeadPK.subCustOrderNo.equals(subCustOrderNo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(String subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
