package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsSparePartHeader.findAll", query = "select o from OmsSparePartHeader o") 
               , @NamedQuery(name = "OmsSparePartHeader.findSparePartHeaderByExtKeys", query = "select o from OmsSparePartHeader o where o.serviceRequestId= :serviceRequestId and o.sequenceId = :sequenceId")
               , @NamedQuery(name = "OmsSparePartHeader.findSparePartHeaderByServiceIdItem", query = "select o from OmsSparePartHeader o where o.serviceRequestId= :serviceRequestId and o.itemId = :itemId")
               , @NamedQuery(name = "OmsSparePartHeader.findSparePartHeaderByIntKey", query = "select o from OmsSparePartHeader o where o.omsServiceReqSeqId= :omsServiceReqSeqId "),
                @NamedQuery(name = "OmsSparePartHeader.findSparePartHeaderByKeyComb", query = "select o from OmsSparePartHeader o where o.omsServiceReqSeqId= :omsServiceReqSeqId and o.serviceRequestId=:serviceRequestId")
            })
@Table(name = "OMS_SPARE_PART_HEADER")
@IdClass(OmsSparePartHeaderPK.class)
public class OmsSparePartHeader implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Column(name = "CUM_CANCELLED_QTY")
    private BigDecimal cumCancelledQty;
    @Column(name = "CUM_QUANTITY_DEDUCTED")
    private BigDecimal cumQuantityDeducted;
    @Column(name = "CUM_QUANTITY_RECEIVED")
    private BigDecimal cumQuantityReceived;
    @Column(name = "CUM_QUANTITY_RESERVED")
    private BigDecimal cumQuantityReserved;
    @Column(name = "CUM_QUANTITY_SHIPPED")
    private BigDecimal cumQuantityShipped;
    @Column(name = "CUM_QUANTITY_TSF_RESERVED")
    private BigDecimal cumQuantityTsfReserved;
    @Column(name = "CUM_QUANTITY_UNRESERVED")
    private BigDecimal cumQuantityUnreserved;
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "OMS_SERVICE_REQ_SEQ_ID", nullable = false, length = 20)
    @SequenceGenerator(name = "sparePartHeaderSeqId", sequenceName = "OMS_SPARE_PART_HEADER_SEQ", allocationSize = 1,initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sparePartHeaderSeqId")        
    private String omsServiceReqSeqId;
    @Column(name = "PENDING_QTY", nullable = false)
    private BigDecimal pendingQty;
    @Column(name = "QUANTITY_REQUESTED", nullable = false)
    private BigDecimal quantityRequested;
    @Id
    @Column(name = "SEQUENCE_ID", nullable = false, length = 10)
    private String sequenceId;
    @Id
    @Column(name = "SERVICE_REQUEST_ID", nullable = false, length = 20)
    private String serviceRequestId;
    @Column(nullable = false, length = 2)
    private String status;
    @Column(name = "STORE_ID", nullable = false)
    private BigDecimal storeId;
    @Column(name = "UPDATED_BY", length = 40)
    private String updatedBy;
    @Column(name = "READY_TO_NOTIFY_FLAG", length = 1)
    private String readyToNotifyFlag;
    @Column(name = "NOTIFIED_FLAG", length = 1)
    private String notifiedFlag;
    public OmsSparePartHeader() {
    }

    public OmsSparePartHeader(Timestamp createDatetime, String createdBy, BigDecimal cumCancelledQty,
                              BigDecimal cumQuantityDeducted, BigDecimal cumQuantityReceived,
                              BigDecimal cumQuantityReserved, BigDecimal cumQuantityShipped,
                              BigDecimal cumQuantityTsfReserved, BigDecimal cumQuantityUnreserved, String itemId,
                              Timestamp lastUpdateDatetime, String omsServiceReqSeqId, BigDecimal pendingQty,
                              BigDecimal quantityRequested, String sequenceId, String serviceRequestId, String status,
                              BigDecimal storeId, String updatedBy) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.cumCancelledQty = cumCancelledQty;
        this.cumQuantityDeducted = cumQuantityDeducted;
        this.cumQuantityReceived = cumQuantityReceived;
        this.cumQuantityReserved = cumQuantityReserved;
        this.cumQuantityShipped = cumQuantityShipped;
        this.cumQuantityTsfReserved = cumQuantityTsfReserved;
        this.cumQuantityUnreserved = cumQuantityUnreserved;
        this.itemId = itemId;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsServiceReqSeqId = omsServiceReqSeqId;
        this.pendingQty = pendingQty;
        this.quantityRequested = quantityRequested;
        this.sequenceId = sequenceId;
        this.serviceRequestId = serviceRequestId;
        this.status = status;
        this.storeId = storeId;
        this.updatedBy = updatedBy;
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

    public BigDecimal getCumCancelledQty() {
        return cumCancelledQty;
    }

    public void setCumCancelledQty(BigDecimal cumCancelledQty) {
        this.cumCancelledQty = cumCancelledQty;
    }

    public BigDecimal getCumQuantityDeducted() {
        return cumQuantityDeducted;
    }

    public void setCumQuantityDeducted(BigDecimal cumQuantityDeducted) {
        this.cumQuantityDeducted = cumQuantityDeducted;
    }

    public BigDecimal getCumQuantityReceived() {
        return cumQuantityReceived;
    }

    public void setCumQuantityReceived(BigDecimal cumQuantityReceived) {
        this.cumQuantityReceived = cumQuantityReceived;
    }

    public BigDecimal getCumQuantityReserved() {
        return cumQuantityReserved;
    }

    public void setCumQuantityReserved(BigDecimal cumQuantityReserved) {
        this.cumQuantityReserved = cumQuantityReserved;
    }

    public BigDecimal getCumQuantityShipped() {
        return cumQuantityShipped;
    }

    public void setCumQuantityShipped(BigDecimal cumQuantityShipped) {
        this.cumQuantityShipped = cumQuantityShipped;
    }

    public BigDecimal getCumQuantityTsfReserved() {
        return cumQuantityTsfReserved;
    }

    public void setCumQuantityTsfReserved(BigDecimal cumQuantityTsfReserved) {
        this.cumQuantityTsfReserved = cumQuantityTsfReserved;
    }

    public BigDecimal getCumQuantityUnreserved() {
        return cumQuantityUnreserved;
    }

    public void setCumQuantityUnreserved(BigDecimal cumQuantityUnreserved) {
        this.cumQuantityUnreserved = cumQuantityUnreserved;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getOmsServiceReqSeqId() {
        return omsServiceReqSeqId;
    }

    public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
    }

    public BigDecimal getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(BigDecimal pendingQty) {
        this.pendingQty = pendingQty;
    }

    public BigDecimal getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(BigDecimal quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setReadyToNotifyFlag(String readyToNotifyFlag) {
        this.readyToNotifyFlag = readyToNotifyFlag;
    }

    public String getReadyToNotifyFlag() {
        return readyToNotifyFlag;
    }

    public void setNotifiedFlag(String notifiedFlag) {
        this.notifiedFlag = notifiedFlag;
    }

    public String getNotifiedFlag() {
        return notifiedFlag;
    }
}
