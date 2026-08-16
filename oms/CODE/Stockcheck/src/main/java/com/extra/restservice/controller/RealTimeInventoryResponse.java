package com.extra.restservice.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RealTimeInventoryResponse {

	private Status status;

	@JsonInclude(value = Include.NON_NULL)
	private List<ItemAvailability> itemAvailability;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<ItemAvailability> getItemAvailability() {
		return itemAvailability;
	}

	public void setItemAvailablity(List<ItemAvailability> itemAvailability) {
		this.itemAvailability = itemAvailability;
	}
}
