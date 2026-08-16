package com.extra.bds.bean;

import java.util.List;

public class Response {

	private Status status;

	private List<Delivery> deliveries;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
}
