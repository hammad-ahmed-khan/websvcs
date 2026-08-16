package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsInvAdj.findAll", query = "select o from OmsInvAdj o"),
                 @NamedQuery(name = "OmsInvAdj.findOmsAdjReqId",
                             query = "select o.omsAdjReqId from OmsInvAdj o where o.adjRequestId=:reqId and o.applicationId=:appId"),
                 @NamedQuery(name = "OmsInvAdj.findByOmsAdjReqId",
                             query = "select o from OmsInvAdj o where o.omsAdjReqId=:reqId ") })
@Table(name = "OMS_INV_ADJ")
public class OmsInvAdj implements Serializable {
    @Column(name = "ADJ_REQUEST_ID", nullable = false, length = 30)
    private String adjRequestId;
    @Column(name = "APPLICATION_ID", nullable = false, length = 30)
    private String applicationId;
    @Column(length = 240)
    private String comments;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "ENTITY_ID", nullable = false, length = 30)
    private String entityId;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "OMS_ADJ_REQ_ID", nullable = false)
    @SequenceGenerator(name = "invadjSeq", sequenceName = "OMS_ADJ_REQ_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invadjSeq")
    private BigDecimal omsAdjReqId;
    @Column(name = "REASON_CODE", nullable = false)
    private BigDecimal reasonCode;
    @Column(name = "REQUEST_DATETIMESTAMP", nullable = false)
    private Timestamp requestDatetimestamp;
    @Column(name = "RESPONSE_DATETIMESTAMP")
    private Timestamp responseDatetimestamp;
    @Column(name = "SIM_INV_ADJ_ID")
    private BigDecimal simInvAdjId;
    @Column(length = 12)
    private String status;
    @Column(name = "STORE_ID", nullable = false)
    private BigDecimal storeId;

    public OmsInvAdj() {
    }

    public OmsInvAdj(String adjRequestId, String applicationId, String comments, Timestamp createDatetime,
                     String entityId, Timestamp lastUpdateDatetime, BigDecimal omsAdjReqId, BigDecimal reasonCode,
                     Timestamp requestDatetimestamp, Timestamp responseDatetimestamp, BigDecimal simInvAdjId,
                     String status, BigDecimal storeId) {
        this.adjRequestId = adjRequestId;
        this.applicationId = applicationId;
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.entityId = entityId;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsAdjReqId = omsAdjReqId;
        this.reasonCode = reasonCode;
        this.requestDatetimestamp = requestDatetimestamp;
        this.responseDatetimestamp = responseDatetimestamp;
        this.simInvAdjId = simInvAdjId;
        this.status = status;
        this.storeId = storeId;
    }

    public String getAdjRequestId() {
        return adjRequestId;
    }

    public void setAdjRequestId(String adjRequestId) {
        this.adjRequestId = adjRequestId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getOmsAdjReqId() {
        return omsAdjReqId;
    }

    public void setOmsAdjReqId(BigDecimal omsAdjReqId) {
        this.omsAdjReqId = omsAdjReqId;
    }

    public BigDecimal getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(BigDecimal reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Timestamp getRequestDatetimestamp() {
        return requestDatetimestamp;
    }

    public void setRequestDatetimestamp(Timestamp requestDatetimestamp) {
        this.requestDatetimestamp = requestDatetimestamp;
    }

    public Timestamp getResponseDatetimestamp() {
        return responseDatetimestamp;
    }

    public void setResponseDatetimestamp(Timestamp responseDatetimestamp) {
        this.responseDatetimestamp = responseDatetimestamp;
    }

    public BigDecimal getSimInvAdjId() {
        return simInvAdjId;
    }

    public void setSimInvAdjId(BigDecimal simInvAdjId) {
        this.simInvAdjId = simInvAdjId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getStoreId() {
        return storeId;
    }

    public void setStoreId(BigDecimal storeId) {
        this.storeId = storeId;
    }
}
