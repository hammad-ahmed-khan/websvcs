package com.extra.oms.spareParts.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Items {

	@JsonProperty("item")
	protected String item;

	@JsonProperty("qty")
	protected BigDecimal qty;

	@JsonProperty("line_id")
	protected BigDecimal lineId;

	protected BigDecimal techAvailQty;

	protected Long omsServId;

	protected Long origReasonCode;

	protected Long availToTechReasonCode;

	protected BigDecimal reqLoc;

	protected String techId;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getLineId() {
		return lineId;
	}

	public void setLineId(BigDecimal lineId) {
		this.lineId = lineId;
	}

	public BigDecimal getTechAvailQty() {
		return techAvailQty;
	}

	public void setTechAvailQty(BigDecimal techAvailQty) {
		this.techAvailQty = techAvailQty;
	}

	public Long getOmsServId() {
		return omsServId;
	}

	public void setOmsServId(Long omsServId) {
		this.omsServId = omsServId;
	}

	public Long getOrigReasonCode() {
		return origReasonCode;
	}

	public void setOrigReasonCode(Long origReasonCode) {
		this.origReasonCode = origReasonCode;
	}

	public BigDecimal getReqLoc() {
		return reqLoc;
	}

	public void setReqLoc(BigDecimal reqLoc) {
		this.reqLoc = reqLoc;
	}

	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public Long getAvailToTechReasonCode() {
		return availToTechReasonCode;
	}

	public void setAvailToTechReasonCode(Long availToTechReasonCode) {
		this.availToTechReasonCode = availToTechReasonCode;
	}

}
