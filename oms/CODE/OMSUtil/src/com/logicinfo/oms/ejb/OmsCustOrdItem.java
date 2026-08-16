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
@NamedQueries({ @NamedQuery(name = "OmsCustOrdItem.findAll", query = "select o from OmsCustOrdItem o"),
		@NamedQuery(name = "OmsCustOrdItem.findByItem ", query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo  and o.item=:item and o.lineNo=:lineNo "),
		@NamedQuery(name = "OmsCustOrdItem.findOmsCustOrdNoByBOInd ", query = "select o.omsCustOrdNo from OmsCustOrdItem o where o.backorderInd=:backorderInd "),
		@NamedQuery(name = "OmsCustOrdItem.findBackOrders ", query = "select o.omsCustOrdNo from OmsCustOrdItem o where o.backorderInd=:backorderInd and o.status<>'S' and o.backorderDlyDate<=:backorderDlyDate "),
		@NamedQuery(name = "OmsCustOrdItem.findByOmsCustOrdNo", query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo "),
		@NamedQuery(name = "OmsCustOrdItem.findByBOIndAndOmsCustNo", query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.backorderInd=:backorderInd "),
		@NamedQuery(name = "OmsCustOrdItem.findDeilverdQuantity", query = "select o.cumQtyDelivered from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and o.lineNo=:lineNo"),
		@NamedQuery(name = "OmsCustOrdItem.getOmsCustOrdItemFindByLinkLineNo", query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.lineLinkNo=:lineLinkNo"),
		@NamedQuery(name = "OmsCustOrdItem.findByItemAndLastUpdatedDateTime", query = "select o.omsCustOrdNo from OmsCustOrdItem o where o.item=:item and o.lastUpdateDatetime >= :startTime and o.lastUpdateDatetime <= :endTime and o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdItem.findSumOfUnitRetailPrice", query = "select sum(o.unitRetail) from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdItem.findByOmsCustOrdNoAndLineNo", query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo"),
		@NamedQuery(name = "OmsCustOrdItem.calculateSumOfQtysForCloseDateTime", query = "select sum(o.qtyOrderedSuom-(o.cumQtyDelivered+o.qtyCancelled)) from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo")

})

@Table(name = "OMS_CUST_ORD_ITEM")
@IdClass(OmsCustOrdItemPK.class)
public class OmsCustOrdItem implements Serializable {

	private static final long serialVersionUID = -4379514065487596574L;

	@Column(name = "BACKORDER_DLY_DATE")
	private Timestamp backorderDlyDate;
	@Column(name = "BACKORDER_IND", nullable = false, length = 1)
	private String backorderInd;
	@Column(length = 200)
	private String comments;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CUM_QTY_DELIVERED")
	private BigDecimal cumQtyDelivered;
	@Id
	@Column(nullable = false, length = 25)
	private String item;
	@Column(name = "LAST_UPDATE_DATETIME")
	private Timestamp lastUpdateDatetime;
	@Column(name = "LINE_LINK_NO")
	private BigDecimal lineLinkNo;
	@Id
	@Column(name = "LINE_NO", nullable = false)
	private BigDecimal lineNo;
	@Id
	@Column(name = "OMS_CUST_ORD_NO", nullable = false)
	private BigDecimal omsCustOrdNo;
	@Column(name = "ORIG_ITEM", length = 25)
	private String origItem;
	@Column(name = "ORIG_UNIT_RETAIL")
	private BigDecimal origUnitRetail;
	@Column(name = "QTY_CANCELLED")
	private BigDecimal qtyCancelled;
	@Column(name = "QTY_ORDERED_SUOM", nullable = false)
	private BigDecimal qtyOrderedSuom;
	@Column(name = "RETAIL_CURR", nullable = false, length = 3)
	private String retailCurr;
	@Column(name = "SHIP_CLASSIFICATION", nullable = false, length = 6)
	private String shipClassification;
	@Column(name = "STANDARD_UOM", nullable = false, length = 4)
	private String standardUom;
	@Column(length = 15)
	private String status;
	@Column(name = "SUBSTITUTE_ALLOW_IND", nullable = false, length = 1)
	private String substituteAllowInd;
	@Column(name = "TRANSACTION_UOM", nullable = false, length = 4)
	private String transactionUom;
	@Column(name = "UNIT_RETAIL", nullable = false)
	private BigDecimal unitRetail;
	@Column(name = "UNIT_VAT_AMOUNT")
	private BigDecimal unitVatAmount;
	@Column(name = "QTY_RETURNED")
	private BigDecimal qtyReturned;
	@Column(name = "EXPECTED_DELIVERY_DATE")
	private Timestamp expectedDeliveryDate;
	@Column(name = "JOOD_ITEM")
	private String joodItem;
	@Column(name ="MARKETPLACE_IND")
	private String marketPlaceInd;
	@Column(name ="APPLY_SERVICE_IND")
	private String applyServiceInd;
	@Column(name ="ORIGINAL_TRAN_ORDER_NO")
	protected String originalTranOrderNo;
	@Column(name ="ORIGINAL_TRAN_LINE_NO")
	protected long originalTranLineNo;	
	@Column(name ="SERVICE_TYPE")
	protected String serviceType;
	@Column(name ="ORD_SR_ID")
	protected String ordSrId;
	@Column(name = "DELV_EFFORT")
	private BigDecimal delvEffort;
	@Column(name = "RSAITEM", length = 5)
	private String rsaItem;
	@Column(name = "DELIVERY_DATETIME")
	private Timestamp deliveryDateTime;
	
	public OmsCustOrdItem() {
	}

	public OmsCustOrdItem(Timestamp backorderDlyDate, String backorderInd, String comments, Timestamp createDatetime, BigDecimal cumQtyDelivered, String item, Timestamp lastUpdateDatetime,
			BigDecimal lineLinkNo, BigDecimal lineNo, BigDecimal omsCustOrdNo, String origItem, BigDecimal origUnitRetail, BigDecimal qtyCancelled, BigDecimal qtyOrderedSuom, String retailCurr,
			String shipClassification, String standardUom, String status, String substituteAllowInd, String transactionUom, BigDecimal unitRetail, BigDecimal unitVatAmount,
			Timestamp expectedDeliveryDate, String rsaItem) {
		this.backorderDlyDate = backorderDlyDate;
		this.backorderInd = backorderInd;
		this.comments = comments;
		this.createDatetime = createDatetime;
		this.cumQtyDelivered = cumQtyDelivered;
		this.item = item;
		this.lastUpdateDatetime = lastUpdateDatetime;
		this.lineLinkNo = lineLinkNo;
		this.lineNo = lineNo;
		this.omsCustOrdNo = omsCustOrdNo;
		this.origItem = origItem;
		this.origUnitRetail = origUnitRetail;
		this.qtyCancelled = qtyCancelled;
		this.qtyOrderedSuom = qtyOrderedSuom;
		this.retailCurr = retailCurr;
		this.shipClassification = shipClassification;
		this.standardUom = standardUom;
		this.status = status;
		this.substituteAllowInd = substituteAllowInd;
		this.transactionUom = transactionUom;
		this.unitRetail = unitRetail;
		this.unitVatAmount = unitVatAmount;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.rsaItem = rsaItem;
	}

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

	public String getJoodItem() {
		return joodItem;
	}

	public void setJoodItem(String joodItem) {
		this.joodItem = joodItem;
	}

	public Timestamp getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(Timestamp deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}

	public String getMarketPlaceInd() {
		return marketPlaceInd;
	}

	public void setMarketPlaceInd(String marketPlaceInd) {
		this.marketPlaceInd = marketPlaceInd;
	}

	public String getOriginalTranOrderNo() {
		return originalTranOrderNo;
	}

	public void setOriginalTranOrderNo(String originalTranOrderNo) {
		this.originalTranOrderNo = originalTranOrderNo;
	}

	public long getOriginalTranLineNo() {
		return originalTranLineNo;
	}

	public void setOriginalTranLineNo(long originalTranLineNo) {
		this.originalTranLineNo = originalTranLineNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getApplyServiceInd() {
		return applyServiceInd;
	}

	public void setApplyServiceInd(String applyServiceInd) {
		this.applyServiceInd = applyServiceInd;
	}

	public String getOrdSrId() {
		return ordSrId;
	}

	public void setOrdSrId(String ordSrId) {
		this.ordSrId = ordSrId;
	}

	public BigDecimal getDelvEffort() {
		return delvEffort;
	}

	public void setDelvEffort(BigDecimal delvEffort) {
		this.delvEffort = delvEffort;
	}
}
