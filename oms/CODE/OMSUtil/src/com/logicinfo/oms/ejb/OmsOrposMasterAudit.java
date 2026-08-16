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
@NamedQueries( { @NamedQuery(name = "OmsOrposMasterAudit.findAll", query = "select o from OmsOrposMasterAudit o"),
                @NamedQuery(name = "OmsOrposMasterAudit.findByOrderIdandOrposTransactionNumber", query = "select o from OmsOrposMasterAudit o where o.orderId = :orderId and o.orposTransactionNumber = :orposTransactionNumber")})
@Table(name = "OMS_ORPOS_MASTER_AUDIT")
@IdClass(OmsOrposMasterAuditPK.class)
public class OmsOrposMasterAudit implements Serializable {
    @Column(name = "CANCEL_QUANTITY")
    private BigDecimal cancelQuantity;
    @Column(name = "CANCEL_REQ_ID")
    private BigDecimal cancelReqId;
    @Column(name = "CREATE_TIMESTAMP")
    private Timestamp createTimestamp;
    @Column(name = "EVENT_ID")
    private String eventId;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "OMS_CUST_ORDER_NO", nullable = false)
    private BigDecimal omsCustOrderNo;
    @Id
    @Column(name = "ORDER_ID", nullable = false)
    private String orderId;
    @Id
    @Column(name = "ORPOS_TRANSACTION_NUMBER", nullable = false, length = 20)
    private String orposTransactionNumber;
    @Column(name = "PICKUP_QUANTITY")
    private BigDecimal pickupQuantity;
    @Column(name = "RETURN_QUANTITY")
    private BigDecimal returnQuantity;
    @Column(name = "RMA_REQ_ID")
    private BigDecimal rmaReqId;
    @Column(length = 1)
    private String status;
    @Column(name = "UPDATE_TIMESTAMP")
    private Timestamp updateTimestamp;
    @Column(name = "POS_INVOICE_NUMBER")
    private String orposInvoiceNumber;

    public OmsOrposMasterAudit() {
    }

    public OmsOrposMasterAudit(BigDecimal cancelQuantity, BigDecimal cancelReqId, Timestamp createTimestamp,
                               String eventId, BigDecimal lineItemNo, BigDecimal omsCustOrderNo,
                               String orderId, String orposTransactionNumber, BigDecimal pickupQuantity,
                               BigDecimal returnQuantity, BigDecimal rmaReqId, String status,
                               Timestamp updateTimestamp) {
        this.cancelQuantity = cancelQuantity;
        this.cancelReqId = cancelReqId;
        this.createTimestamp = createTimestamp;
        this.eventId = eventId;
        this.lineItemNo = lineItemNo;
        this.omsCustOrderNo = omsCustOrderNo;
        this.orderId = orderId;
        this.orposTransactionNumber = orposTransactionNumber;
        this.pickupQuantity = pickupQuantity;
        this.returnQuantity = returnQuantity;
        this.rmaReqId = rmaReqId;
        this.status = status;
        this.updateTimestamp = updateTimestamp;
    }

    public BigDecimal getCancelQuantity() {
        return cancelQuantity;
    }

    public void setCancelQuantity(BigDecimal cancelQuantity) {
        this.cancelQuantity = cancelQuantity;
    }

    public BigDecimal getCancelReqId() {
        return cancelReqId;
    }

    public void setCancelReqId(BigDecimal cancelReqId) {
        this.cancelReqId = cancelReqId;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getOmsCustOrderNo() {
        return omsCustOrderNo;
    }

    public void setOmsCustOrderNo(BigDecimal omsCustOrderNo) {
        this.omsCustOrderNo = omsCustOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrposTransactionNumber() {
        return orposTransactionNumber;
    }

    public void setOrposTransactionNumber(String orposTransactionNumber) {
        this.orposTransactionNumber = orposTransactionNumber;
    }

    public BigDecimal getPickupQuantity() {
        return pickupQuantity;
    }

    public void setPickupQuantity(BigDecimal pickupQuantity) {
        this.pickupQuantity = pickupQuantity;
    }

    public BigDecimal getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public BigDecimal getRmaReqId() {
        return rmaReqId;
    }

    public void setRmaReqId(BigDecimal rmaReqId) {
        this.rmaReqId = rmaReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getOrposInvoiceNumber() {
		return orposInvoiceNumber;
	}

	public void setOrposInvoiceNumber(String orposInvoiceNumber) {
		this.orposInvoiceNumber = orposInvoiceNumber;
	}
    
    
}
