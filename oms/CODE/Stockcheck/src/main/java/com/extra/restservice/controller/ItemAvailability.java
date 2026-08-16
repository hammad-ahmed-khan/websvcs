package com.extra.restservice.controller;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ItemAvailability {

	private String item;
	private String location;
	private String locationType;
	@JsonInclude(value = Include.NON_NULL)
	private String channel_id;
	private BigDecimal availableQuantity;
	private String unitOfMeasure;
	private String packCalculateIndicator;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getPackCalculateIndicator() {
		return packCalculateIndicator;
	}

	public void setPackCalculateIndicator(String packCalculateIndicator) {
		this.packCalculateIndicator = packCalculateIndicator;
	}

	public BigDecimal getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(BigDecimal availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
}
