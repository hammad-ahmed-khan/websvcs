package com.extra.oms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressInfo {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String orderNo;

	private String lat;

	@JsonProperty("long")
	private String longitude;

	private String shortaddress;

	private String country = "SA";

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String type;

	public String getOrderNo() {
		return this.orderNo;
	}

	public String getLat() {
		return this.lat;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public String getShortaddress() {
		return this.shortaddress;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setShortaddress(String shortaddress) {
		this.shortaddress = shortaddress;
	}

	public String getCountry() {
		return this.country;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
