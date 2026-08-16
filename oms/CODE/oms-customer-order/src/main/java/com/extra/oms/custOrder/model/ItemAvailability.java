package com.extra.oms.custOrder.model;

import java.math.BigDecimal;

public class ItemAvailability {

	private String item;
	private BigDecimal orderQty;
	private BigDecimal availableQty;
	private String errorMessage;
	private BigDecimal lineNo;

	public String getItem() {
		return item;
	}

	public void setItem(String value) {
		this.item = value;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal value) {
		this.orderQty = value;
	}

	public BigDecimal getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigDecimal value) {
		this.availableQty = value;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}

	public BigDecimal getLineNo() {
		return lineNo;
	}
}
