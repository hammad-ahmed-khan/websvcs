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
@NamedQueries( { @NamedQuery(name = "OmsSparePartCancelHdr.findAll",
                             query = "select o from OmsSparePartCancelHdr o") })
@Table(name = "OMS_SPARE_PART_CANCEL_HDR")
@IdClass(OmsSparePartCancelHdrPK.class)
public class OmsSparePartCancelHdr implements Serializable {
    @Column(name = "CANCEL_QTY")
    private BigDecimal cancelQty;
    @Id
    @Column(name = "CANCELLATION_ID", nullable = false, length = 20)
    private String cancellationId;
    @Column(name = "CREATE_DATETIME")
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", length = 40)
    private String createdBy;
    @Column(name = "OMS_SERVICE_REQ_SEQ_ID", length = 20)
    private String omsServiceReqSeqId;
    @Id
    @Column(name = "SEQUENCE_ID", nullable = false)
    private String sequenceId;
    @Id
    @Column(name = "SERVICE_REQ_SEQ_ID", nullable = false, length = 20)
    private String serviceReqSeqId;
    @Column(length = 2)
    private String status;

    public OmsSparePartCancelHdr() {
    }

    public OmsSparePartCancelHdr(BigDecimal cancelQty, String cancellationId, Timestamp createDatetime,
                                 String createdBy, String omsServiceReqSeqId, String sequenceId,
                                 String serviceReqSeqId, String status) {
        this.cancelQty = cancelQty;
        this.cancellationId = cancellationId;
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.omsServiceReqSeqId = omsServiceReqSeqId;
        this.sequenceId = sequenceId;
        this.serviceReqSeqId = serviceReqSeqId;
        this.status = status;
    }

    public BigDecimal getCancelQty() {
        return cancelQty;
    }

    public void setCancelQty(BigDecimal cancelQty) {
        this.cancelQty = cancelQty;
    }

    public String getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(String cancellationId) {
        this.cancellationId = cancellationId;
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

    public String getOmsServiceReqSeqId() {
        return omsServiceReqSeqId;
    }

    public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getServiceReqSeqId() {
        return serviceReqSeqId;
    }

    public void setServiceReqSeqId(String serviceReqSeqId) {
        this.serviceReqSeqId = serviceReqSeqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
