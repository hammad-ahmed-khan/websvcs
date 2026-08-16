package com.extra.bds.bean;

public class Status {

	private boolean success;

	private String code;

	private String message;

	private Integer deliveryCharge;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(Integer deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
}
