package com.extra.oms.crossChannel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrossChannelRequest {

	@JsonProperty("mobileNo")
	private String mobileNo;

	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("OrderNo")
	private String orderNo;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
