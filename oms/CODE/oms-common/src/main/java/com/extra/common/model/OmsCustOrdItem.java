package com.extra.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsCustOrdItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2779779268181592714L;

	private Timestamp backorderDlyDate;

	private String backorderInd;

	private String comments;

	private Timestamp createDatetime;

	private BigDecimal cumQtyDelivered;

	private String item;

	private Timestamp lastUpdateDatetime;

	private BigDecimal lineLinkNo;

	private BigDecimal lineNo;

	private Long itemDept;

	private String invInd;

	private BigDecimal omsCustOrdNo;

	private String origItem;

	private BigDecimal origUnitRetail;

	private BigDecimal qtyCancelled;

	private BigDecimal qtyOrderedSuom;

	private String retailCurr;

	private String shipClassification;

	private String standardUom;

	private String status;

	private String substituteAllowInd;

	private String transactionUom;

	private BigDecimal unitRetail;

	private BigDecimal unitVatAmount;

	private BigDecimal qtyReturned;

	private Timestamp expectedDeliveryDate;

	private String rsaItem;

	public Timestamp getBackorderDlyDate() {
		return backorderDlyDate;
	}

	public void setBackorderDlyDate(Timestamp backorderDlyDate) {
		this.backorderDlyDate = backorderDlyDate;
	}

	public String getBackorderInd() {
		return backorderInd;
	}

	public void setBackorderInd(String backorderInd) {
		this.backorderInd = backorderInd;
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

	public BigDecimal getCumQtyDelivered() {
		return cumQtyDelivered;
	}

	public void setCumQtyDelivered(BigDecimal cumQtyDelivered) {
		this.cumQtyDelivered = cumQtyDelivered;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Timestamp getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public BigDecimal getLineLinkNo() {
		return lineLinkNo;
	}

	public void setLineLinkNo(BigDecimal lineLinkNo) {
		this.lineLinkNo = lineLinkNo;
	}

	public BigDecimal getLineNo() {
		return lineNo;
	}

	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}

	public BigDecimal getOmsCustOrdNo() {
		return omsCustOrdNo;
	}

	public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		this.omsCustOrdNo = omsCustOrdNo;
	}

	public String getOrigItem() {
		return origItem;
	}

	public void setOrigItem(String origItem) {
		this.origItem = origItem;
	}

	public BigDecimal getOrigUnitRetail() {
		return origUnitRetail;
	}

	public void setOrigUnitRetail(BigDecimal origUnitRetail) {
		this.origUnitRetail = origUnitRetail;
	}

	public BigDecimal getQtyCancelled() {
		return qtyCancelled;
	}

	public void setQtyCancelled(BigDecimal qtyCancelled) {
		this.qtyCancelled = qtyCancelled;
	}

	public BigDecimal getQtyOrderedSuom() {
		return qtyOrderedSuom;
	}

	public void setQtyOrderedSuom(BigDecimal qtyOrderedSuom) {
		this.qtyOrderedSuom = qtyOrderedSuom;
	}

	public String getRetailCurr() {
		return retailCurr;
	}

	public void setRetailCurr(String retailCurr) {
		this.retailCurr = retailCurr;
	}

	public String getShipClassification() {
		return shipClassification;
	}

	public void setShipClassification(String shipClassification) {
		this.shipClassification = shipClassification;
	}

	public String getStandardUom() {
		return standardUom;
	}

	public void setStandardUom(String standardUom) {
		this.standardUom = standardUom;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubstituteAllowInd() {
		return substituteAllowInd;
	}

	public void setSubstituteAllowInd(String substituteAllowInd) {
		this.substituteAllowInd = substituteAllowInd;
	}

	public String getTransactionUom() {
		return transactionUom;
	}

	public void setTransactionUom(String transactionUom) {
		this.transactionUom = transactionUom;
	}

	public BigDecimal getUnitRetail() {
		return unitRetail;
	}

	public void setUnitRetail(BigDecimal unitRetail) {
		this.unitRetail = unitRetail;
	}

	public void setQtyReturned(BigDecimal qtyReturned) {
		this.qtyReturned = qtyReturned;
	}

	public BigDecimal getQtyReturned() {
		return qtyReturned;
	}

	public void setUnitVatAmount(BigDecimal unitVatAmount) {
		this.unitVatAmount = unitVatAmount;
	}

	public BigDecimal getUnitVatAmount() {
		return unitVatAmount;
	}

	public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public Timestamp getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setRsaItem(String rsaItem) {
		this.rsaItem = rsaItem;
	}

	public String getRsaItem() {
		return rsaItem;
	}

	public Long getItemDept() {
		return itemDept;
	}

	public void setItemDept(Long itemDept) {
		this.itemDept = itemDept;
	}

	public String getInvInd() {
		return invInd;
	}

	public void setInvInd(String invInd) {
		this.invInd = invInd;
	}
}
