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
@NamedQueries({ @NamedQuery(name = "OmsSparePartFulfill.findAll", query = "select o from OmsSparePartFulfill o"),
		@NamedQuery(name = "OmsSparePartFulfill.findByRequestAndItem", query = "select o from OmsSparePartFulfill o where o.omsServiceReqSeqId=:omsServiceReqSeqId and o.itemId=:itemId"),
		@NamedQuery(name = "OmsSparePartFulfill.findByOmsServiceReqId", query = "select o from OmsSparePartFulfill o where o.omsServiceReqSeqId=:omsServiceReqSeqId ") })

@Table(name = "OMS_SPARE_PART_FULFILL")
@IdClass(OmsSparePartFulfillPK.class)
public class OmsSparePartFulfill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1037987017510105955L;

	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CREATED_BY", nullable = false, length = 40)
	private String createdBy;
	@Column(name = "ITEM_ID", nullable = false, length = 25)
	private String itemId;
	@Column(name = "LAST_UPDATED_DATETIME")
	private Timestamp lastUpdatedDatetime;
	@Id
	@Column(name = "OMS_SERVICE_REQ_SEQ_ID", nullable = false, length = 20)
	private String omsServiceReqSeqId;
	@Column(name = "QUANTITY_CANCELLED")
	private BigDecimal quantityCancelled;
	@Column(name = "QUANTITY_DEDUCTED")
	private BigDecimal quantityDeducted;
	@Column(name = "QUANTITY_RECEIVED")
	private BigDecimal quantityReceived;
	@Column(name = "QUANTITY_RESERVED")
	private BigDecimal quantityReserved;
	@Column(name = "QUANTITY_SHIPPED")
	private BigDecimal quantityShipped;
	@Column(name = "QUANTITY_TSF_RESERVED")
	private BigDecimal quantityTsfReserved;
	@Column(name = "QUANTITY_UNRESERVED")
	private BigDecimal quantityUnreserved;
	@Id
	@Column(name = "SOURCE_LOCATION", nullable = false)
	private BigDecimal sourceLocation;
	@Id
	@Column(name = "TRAN_ID", nullable = false)
	private BigDecimal tranId;
	@Column(name = "TRAN_TYPE", nullable = false, length = 3)
	private String tranType;
	@Column(name = "UPDATED_BY", length = 40)
	private String updatedBy;

	@Column(name = "SUB_BUCKET", length = 30)
	private String subBucket;

	@Column(name = "SUB_BKT_QTY")
	private BigDecimal subBucketQty;

	public OmsSparePartFulfill() {
	}

	public OmsSparePartFulfill(Timestamp createDatetime, String createdBy, String itemId, Timestamp lastUpdatedDatetime, String omsServiceReqSeqId, BigDecimal quantityCancelled,
			BigDecimal quantityDeducted, BigDecimal quantityReceived, BigDecimal quantityReserved, BigDecimal quantityShipped, BigDecimal quantityTsfReserved, BigDecimal quantityUnreserved,
			BigDecimal sourceLocation, BigDecimal tranId, String tranType, String updatedBy) {
		this.createDatetime = createDatetime;
		this.createdBy = createdBy;
		this.itemId = itemId;
		this.lastUpdatedDatetime = lastUpdatedDatetime;
		this.omsServiceReqSeqId = omsServiceReqSeqId;
		this.quantityCancelled = quantityCancelled;
		this.quantityDeducted = quantityDeducted;
		this.quantityReceived = quantityReceived;
		this.quantityReserved = quantityReserved;
		this.quantityShipped = quantityShipped;
		this.quantityTsfReserved = quantityTsfReserved;
		this.quantityUnreserved = quantityUnreserved;
		this.sourceLocation = sourceLocation;
		this.tranId = tranId;
		this.tranType = tranType;
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Timestamp getLastUpdatedDatetime() {
		return lastUpdatedDatetime;
	}

	public void setLastUpdatedDatetime(Timestamp lastUpdatedDatetime) {
		this.lastUpdatedDatetime = lastUpdatedDatetime;
	}

	public String getOmsServiceReqSeqId() {
		return omsServiceReqSeqId;
	}

	public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
		this.omsServiceReqSeqId = omsServiceReqSeqId;
	}

	public BigDecimal getQuantityCancelled() {
		return quantityCancelled;
	}

	public void setQuantityCancelled(BigDecimal quantityCancelled) {
		this.quantityCancelled = quantityCancelled;
	}

	public BigDecimal getQuantityDeducted() {
		return quantityDeducted;
	}

	public void setQuantityDeducted(BigDecimal quantityDeducted) {
		this.quantityDeducted = quantityDeducted;
	}

	public BigDecimal getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(BigDecimal quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public BigDecimal getQuantityReserved() {
		return quantityReserved;
	}

	public void setQuantityReserved(BigDecimal quantityReserved) {
		this.quantityReserved = quantityReserved;
	}

	public BigDecimal getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(BigDecimal quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public BigDecimal getQuantityTsfReserved() {
		return quantityTsfReserved;
	}

	public void setQuantityTsfReserved(BigDecimal quantityTsfReserved) {
		this.quantityTsfReserved = quantityTsfReserved;
	}

	public BigDecimal getQuantityUnreserved() {
		return quantityUnreserved;
	}

	public void setQuantityUnreserved(BigDecimal quantityUnreserved) {
		this.quantityUnreserved = quantityUnreserved;
	}

	public BigDecimal getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(BigDecimal sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public BigDecimal getTranId() {
		return tranId;
	}

	public void setTranId(BigDecimal tranId) {
		this.tranId = tranId;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getSubBucket() {
		return subBucket;
	}

	public void setSubBucket(String subBucket) {
		this.subBucket = subBucket;
	}

	public BigDecimal getSubBucketQty() {
		return subBucketQty;
	}

	public void setSubBucketQty(BigDecimal subBucketQty) {
		this.subBucketQty = subBucketQty;
	}
}
