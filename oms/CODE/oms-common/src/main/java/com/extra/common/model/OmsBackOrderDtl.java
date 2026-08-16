package com.extra.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsBackOrderDtl {

	private String backorderStatus;

	private Timestamp confTs;

	private Timestamp createDatetime;

	private String createdBy;

	private Timestamp fulInvAvlDate;

	private BigDecimal fulfillLoc;

	private String fulfillLocType;

	private BigDecimal fulfillOrderNo;

	private BigDecimal fulfillQty;

	private Timestamp initiateTs;

	private String item;

	private BigDecimal lineNo;

	private BigDecimal omsCustOrdNo;

	private BigDecimal sourceLoc;

	private String sourceLocType;

	private BigDecimal sourceQty;

	private BigDecimal combinationId;

	private BigDecimal cancelledQty;

	private OmsCustOrdHead omsCustOrdHead;

	private OmsCustOrdItem omsCustOrdItem;

	private Wh wh;

	public String getBackorderStatus() {
		return backorderStatus;
	}

	public void setBackorderStatus(String backorderStatus) {
		this.backorderStatus = backorderStatus;
	}

	public Timestamp getConfTs() {
		return confTs;
	}

	public void setConfTs(Timestamp confTs) {
		this.confTs = confTs;
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

	public Timestamp getFulInvAvlDate() {
		return fulInvAvlDate;
	}

	public void setFulInvAvlDate(Timestamp fulInvAvlDate) {
		this.fulInvAvlDate = fulInvAvlDate;
	}

	public BigDecimal getFulfillLoc() {
		return fulfillLoc;
	}

	public void setFulfillLoc(BigDecimal fulfillLoc) {
		this.fulfillLoc = fulfillLoc;
	}

	public String getFulfillLocType() {
		return fulfillLocType;
	}

	public void setFulfillLocType(String fulfillLocType) {
		this.fulfillLocType = fulfillLocType;
	}

	public BigDecimal getFulfillOrderNo() {
		return fulfillOrderNo;
	}

	public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
		this.fulfillOrderNo = fulfillOrderNo;
	}

	public BigDecimal getFulfillQty() {
		return fulfillQty;
	}

	public void setFulfillQty(BigDecimal fulfillQty) {
		this.fulfillQty = fulfillQty;
	}

	public Timestamp getInitiateTs() {
		return initiateTs;
	}

	public void setInitiateTs(Timestamp initiateTs) {
		this.initiateTs = initiateTs;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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

	public BigDecimal getSourceLoc() {
		return sourceLoc;
	}

	public void setSourceLoc(BigDecimal sourceLoc) {
		this.sourceLoc = sourceLoc;
	}

	public String getSourceLocType() {
		return sourceLocType;
	}

	public void setSourceLocType(String sourceLocType) {
		this.sourceLocType = sourceLocType;
	}

	public BigDecimal getSourceQty() {
		return sourceQty;
	}

	public void setSourceQty(BigDecimal sourceQty) {
		this.sourceQty = sourceQty;
	}

	public void setCombinationId(BigDecimal combinationId) {
		this.combinationId = combinationId;
	}

	public BigDecimal getCombinationId() {
		return combinationId;
	}

	public BigDecimal getCancelledQty() {
		return cancelledQty;
	}

	public void setCancelledQty(BigDecimal cancelledQty) {
		this.cancelledQty = cancelledQty;
	}

	public OmsCustOrdHead getOmsCustOrdHead() {
		return omsCustOrdHead;
	}

	public OmsCustOrdItem getOmsCustOrdItem() {
		return omsCustOrdItem;
	}

	public void setOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead) {
		this.omsCustOrdHead = omsCustOrdHead;
	}

	public void setOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem) {
		this.omsCustOrdItem = omsCustOrdItem;
	}

	public Wh getWh() {
		return wh;
	}

	public void setWh(Wh wh) {
		this.wh = wh;
	}
}
