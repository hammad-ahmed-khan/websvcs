package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsSystemParameters.findAll", query = "select o from OmsSystemParameters o"),
                 @NamedQuery(name = "OmsSystemParameters.findIndValue",
                             query = "select o.parameterValue from OmsSystemParameters o where  o.parameterId=:paraId and  o.parameterName=:paraName"),
                 @NamedQuery(name = "OmsSystemParameters.findByParameterId",
                             query = "select o from OmsSystemParameters o where  o.parameterId=:paraId "),
                 @NamedQuery(name = "OmsSystemParameters.findByParameterValue",
                             query = "select o.parameterComment from OmsSystemParameters o where  o.parameterValue=:parameterValue and o.parameterId='RTLOG_TENDERS' ") })
@Table(name = "OMS_SYSTEM_PARAMETERS")
@IdClass(OmsSystemParametersPK.class)
public class OmsSystemParameters implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", length = 40)
    private String createdBy;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "LAST_UPDATED_BY", length = 40)
    private String lastUpdatedBy;
    @Column(name = "PARAMETER_COMMENT", nullable = false, length = 250)
    private String parameterComment;
    @Id
    @Column(name = "PARAMETER_ID", nullable = false, length = 40)
    private String parameterId;
    @Id
    @Column(name = "PARAMETER_NAME", nullable = false, length = 40)
    private String parameterName;
    @Column(name = "PARAMETER_ORDER", nullable = false)
    private BigDecimal parameterOrder;
    @Id
    @Column(name = "PARAMETER_VALUE", nullable = false, length = 40)
    private String parameterValue;
    @Column(name = "SYSTEM_REQUIRED_IND", length = 1)
    private String systemRequiredInd;

    public OmsSystemParameters() {
    }

    public OmsSystemParameters(Timestamp createDatetime, String createdBy, Timestamp lastUpdateDatetime,
                               String lastUpdatedBy, String parameterComment, String parameterId, String parameterName,
                               BigDecimal parameterOrder, String parameterValue, String systemRequiredInd) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdatedBy = lastUpdatedBy;
        this.parameterComment = parameterComment;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.parameterOrder = parameterOrder;
        this.parameterValue = parameterValue;
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

    public String getParameterComment() {
        return parameterComment;
    }

    public void setParameterComment(String parameterComment) {
        this.parameterComment = parameterComment;
    }

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public BigDecimal getParameterOrder() {
        return parameterOrder;
    }

    public void setParameterOrder(BigDecimal parameterOrder) {
        this.parameterOrder = parameterOrder;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getSystemRequiredInd() {
        return systemRequiredInd;
    }

    public void setSystemRequiredInd(String systemRequiredInd) {
        this.systemRequiredInd = systemRequiredInd;
    }
}
