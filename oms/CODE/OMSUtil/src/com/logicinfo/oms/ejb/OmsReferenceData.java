package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsReferenceData.findAll", query = "select o from OmsReferenceData o"),
                 @NamedQuery(name = "OmsReferenceData.findRefValue", query = "select o.refValue from OmsReferenceData o where o.refKey1=:refKey1 and o.refKey2=:refKey2 and o.refKey3=:refKey3"),
                             @NamedQuery(name = "OmsReferenceData.findByISOCode", query = "select o.refValue from OmsReferenceData o where o.refKey1=:refKey1  and o.refKey3=:refKey3")})
@Table(name = "OMS_REFERENCE_DATA")
public class OmsReferenceData implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", length = 40)
    private String createdBy;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "LAST_UPDATED_BY", length = 40)
    private String lastUpdatedBy;
    @Column(name = "REF_COMMENT", length = 250)
    private String refComment;
    @Id
    @Column(name = "REF_ID", nullable = false, length = 40)
    private String refId;
    @Column(name = "REF_KEY_1", length = 40)
    private String refKey1;
    @Column(name = "REF_KEY_2", length = 40)
    private String refKey2;
    @Column(name = "REF_KEY_3", length = 40)
    private String refKey3;
    @Column(name = "REF_VALUE", length = 40)
    private String refValue;
    @Column(name = "SYSTEM_REQUIRED_IND", length = 1)
    private String systemRequiredInd;

    public OmsReferenceData() {
    }

    public OmsReferenceData(Timestamp createDatetime, String createdBy, Timestamp lastUpdateDatetime,
                            String lastUpdatedBy, String refComment, String refId, String refKey1, String refKey2,
                            String refKey3, String refValue, String systemRequiredInd) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdatedBy = lastUpdatedBy;
        this.refComment = refComment;
        this.refId = refId;
        this.refKey1 = refKey1;
        this.refKey2 = refKey2;
        this.refKey3 = refKey3;
        this.refValue = refValue;
        this.systemRequiredInd = systemRequiredInd;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getRefComment() {
        return refComment;
    }

    public void setRefComment(String refComment) {
        this.refComment = refComment;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefKey1() {
        return refKey1;
    }

    public void setRefKey1(String refKey1) {
        this.refKey1 = refKey1;
    }

    public String getRefKey2() {
        return refKey2;
    }

    public void setRefKey2(String refKey2) {
        this.refKey2 = refKey2;
    }

    public String getRefKey3() {
        return refKey3;
    }

    public void setRefKey3(String refKey3) {
        this.refKey3 = refKey3;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public String getSystemRequiredInd() {
        return systemRequiredInd;
    }

    public void setSystemRequiredInd(String systemRequiredInd) {
        this.systemRequiredInd = systemRequiredInd;
    }
}
