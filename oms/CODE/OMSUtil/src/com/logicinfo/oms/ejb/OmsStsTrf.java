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
@NamedQueries( { @NamedQuery(name = "OmsStsTrf.findAll", query = "select o from OmsStsTrf o"),
                 @NamedQuery(name = "OmsStsTrf.findByTransReqId",
                             query = "select o.omsTrfReqId from OmsStsTrf o where o.applicationId=:appId and o.transferRequestId=:transID"),
                 @NamedQuery(name = "OmsStsTrf.findByOmsTrfReqId",
                             query = "select o from OmsStsTrf o where o.omsTrfReqId=:reqId ") })
@Table(name = "OMS_STS_TRF")
public class OmsStsTrf implements Serializable {
    @Column(name = "APPLICATION_ID", nullable = false, length = 30)
    private String applicationId;
    @Column(length = 240)
    private String comments;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "ENTITY_ID", nullable = false, length = 30)
    private String entityId;
    @Column(name = "FROM_LOC", nullable = false)
    private BigDecimal fromLoc;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "OMS_TRF_REQ_ID", nullable = false)
    @SequenceGenerator(name = "trfSeq", sequenceName = "OMS_TSF_REQ_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trfSeq")
    private BigDecimal omsTrfReqId;
    @Column(name = "REQUEST_DATETIMESTAMP", nullable = false)
    private Timestamp requestDatetimestamp;
    @Column(name = "RESPONSE_DATETIMESTAMP")
    private Timestamp responseDatetimestamp;
    @Column(name = "SIM_STS_TRF_ID")
    private BigDecimal simStsTrfId;
    @Column(length = 12)
    private String status;
    @Column(name = "TO_LOC", nullable = false)
    private BigDecimal toLoc;
    @Column(name = "TRANSFER_REQUEST_ID", nullable = false, length = 30)
    private String transferRequestId;

    public OmsStsTrf() {
    }

    public OmsStsTrf(String applicationId, String comments, Timestamp createDatetime, String entityId,
                     BigDecimal fromLoc, Timestamp lastUpdateDatetime, BigDecimal omsTrfReqId,
                     Timestamp requestDatetimestamp, Timestamp responseDatetimestamp, BigDecimal simStsTrfId,
                     String status, BigDecimal toLoc, String transferRequestId) {
        this.applicationId = applicationId;
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.entityId = entityId;
        this.fromLoc = fromLoc;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsTrfReqId = omsTrfReqId;
        this.requestDatetimestamp = requestDatetimestamp;
        this.responseDatetimestamp = responseDatetimestamp;
        this.simStsTrfId = simStsTrfId;
        this.status = status;
        this.toLoc = toLoc;
        this.transferRequestId = transferRequestId;
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

    public BigDecimal getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(BigDecimal fromLoc) {
        this.fromLoc = fromLoc;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getOmsTrfReqId() {
        return omsTrfReqId;
    }

    public void setOmsTrfReqId(BigDecimal omsTrfReqId) {
        this.omsTrfReqId = omsTrfReqId;
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

    public BigDecimal getSimStsTrfId() {
        return simStsTrfId;
    }

    public void setSimStsTrfId(BigDecimal simStsTrfId) {
        this.simStsTrfId = simStsTrfId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getToLoc() {
        return toLoc;
    }

    public void setToLoc(BigDecimal toLoc) {
        this.toLoc = toLoc;
    }

    public String getTransferRequestId() {
        return transferRequestId;
    }

    public void setTransferRequestId(String transferRequestId) {
        this.transferRequestId = transferRequestId;
    }
}
