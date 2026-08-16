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
@NamedQueries( { @NamedQuery(name = "OmsSparePartAudit.findAll", query = "select o from OmsSparePartAudit o") })
@Table(name = "OMS_SPARE_PART_AUDIT")
public class OmsSparePartAudit implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Column(name = "EVENT_TYPE", nullable = false, length = 6)
    private String eventType;
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "OMS_AUDIT_SEQ_ID", nullable = false)
    @SequenceGenerator(name = "sparePartAuditSeqId", sequenceName = "OMS_SPAREPART_AUDIT_SEQ", allocationSize = 1,
                       initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sparePartAuditSeqId")        
    private BigDecimal omsAuditSeqId;
    @Column(name = "OMS_SERVICE_REQ_SEQ_ID", nullable = false, length = 20)
    private String omsServiceReqSeqId;
    private BigDecimal quantity;
    @Column(name = "SOURCE_LOCATION", nullable = false)
    private BigDecimal sourceLocation;

    public OmsSparePartAudit() {
    }

    public OmsSparePartAudit(Timestamp createDatetime, String createdBy, String eventType, String itemId,
                             BigDecimal omsAuditSeqId, String omsServiceReqSeqId, BigDecimal quantity,
                             BigDecimal sourceLocation) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.eventType = eventType;
        this.itemId = itemId;
        this.omsAuditSeqId = omsAuditSeqId;
        this.omsServiceReqSeqId = omsServiceReqSeqId;
        this.quantity = quantity;
        this.sourceLocation = sourceLocation;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getOmsAuditSeqId() {
        return omsAuditSeqId;
    }

    public void setOmsAuditSeqId(BigDecimal omsAuditSeqId) {
        this.omsAuditSeqId = omsAuditSeqId;
    }

    public String getOmsServiceReqSeqId() {
        return omsServiceReqSeqId;
    }

    public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(BigDecimal sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
}
