package org.logicinfo.oms.slotBookingAvailability.controller;

import java.util.List;

public class SlotsAvailabilityResponse {

	public SlotsAvailabilityResponse() {
		super();
	}

	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	private List<DeliveriesResponse> deliveries;

	public List<DeliveriesResponse> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<DeliveriesResponse> deliveries) {
		this.deliveries = deliveries;
	}

}
