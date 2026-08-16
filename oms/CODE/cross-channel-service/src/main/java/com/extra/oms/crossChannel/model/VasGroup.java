package com.extra.oms.crossChannel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VasGroup {
	
    @JsonProperty("years")
	private String years;
	
    @JsonProperty("brand")
	private String brand;
	
    @JsonProperty("serial_number")
	private String serialNumber;

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
    
    

}
