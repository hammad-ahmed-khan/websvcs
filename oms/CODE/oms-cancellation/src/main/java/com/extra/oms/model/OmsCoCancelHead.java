package com.extra.oms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsCoCancelHead {

	private String applicationId;

	private Timestamp canReqDatetime;

	private BigDecimal cancelReqId;

	private BigDecimal cancelReqstLocId;

	private String comments;

	private Timestamp createDatetime;

	private String custOrdNo;

	private String entityId;

	private Timestamp lastUpdateDatetime;

	private BigDecimal omsCancelId;

	private BigDecimal refundAmount;

	private String refundCompltInd;

	private String refundOption;

	private String status;

	private String subCustOrdNo;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Timestamp getCanReqDatetime() {
		return canReqDatetime;
	}

	public void setCanReqDatetime(Timestamp canReqDatetime) {
		this.canReqDatetime = canReqDatetime;
	}

	public BigDecimal getCancelReqId() {
		return cancelReqId;
	}

	public void setCancelReqId(BigDecimal cancelReqId) {
		this.cancelReqId = cancelReqId;
	}

	public BigDecimal getCancelReqstLocId() {
		return cancelReqstLocId;
	}

	public void setCancelReqstLocId(BigDecimal cancelReqstLocId) {
		this.cancelReqstLocId = cancelReqstLocId;
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

	public String getCustOrdNo() {
		return custOrdNo;
	}

	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
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

	public BigDecimal getOmsCancelId() {
		return omsCancelId;
	}

	public void setOmsCancelId(BigDecimal omsCancelId) {
		this.omsCancelId = omsCancelId;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefindAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundCompltInd() {
		return refundCompltInd;
	}

	public void setRefundCompltInd(String refundCompltInd) {
		this.refundCompltInd = refundCompltInd;
	}

	public String getRefundOption() {
		return refundOption;
	}

	public void setRefundOption(String refundOption) {
		this.refundOption = refundOption;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubCustOrdNo() {
		return subCustOrdNo;
	}

	public void setSubCustOrdNo(String subCustOrdNo) {
		this.subCustOrdNo = subCustOrdNo;
	}
}
