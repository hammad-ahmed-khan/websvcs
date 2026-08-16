package com.extra.oms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsCoFoCancel {

	private Timestamp createDatetime;

	private BigDecimal foCancelledOty;

	private BigDecimal fulfillOrderNo;

	private String item;

	private BigDecimal lineNo;

	private Long omsCancelId;

	private String wsResponse;

	private BigDecimal fulfillLoc;

    private String fulfillLocType;

    private BigDecimal sourceLoc;

    private String sourceLocType;

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public BigDecimal getFoCancelledOty() {
		return foCancelledOty;
	}

	public void setFoCancelledOty(BigDecimal foCancelledOty) {
		this.foCancelledOty = foCancelledOty;
	}

	public BigDecimal getFulfillOrderNo() {
		return fulfillOrderNo;
	}

	public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
		this.fulfillOrderNo = fulfillOrderNo;
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

	public Long getOmsCancelId() {
		return omsCancelId;
	}

	public void setOmsCancelId(Long omsCancelId) {
		this.omsCancelId = omsCancelId;
	}

	public String getWsResponse() {
		return wsResponse;
	}

	public void setWsResponse(String wsResponse) {
		this.wsResponse = wsResponse;
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
}
