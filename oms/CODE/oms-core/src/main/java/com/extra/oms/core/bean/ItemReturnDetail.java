package com.extra.oms.core.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ItemReturnDetail {

	private String type;

	private String item;

	private BigDecimal quantity;

	private Date returnTime;

	private String refundOption;

	private String refundStatus;

	private BigDecimal refundAmount;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public String getRefundOption() {
		return refundOption;
	}

	public void setRefundOption(String refundOption) {
		this.refundOption = refundOption;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
}
