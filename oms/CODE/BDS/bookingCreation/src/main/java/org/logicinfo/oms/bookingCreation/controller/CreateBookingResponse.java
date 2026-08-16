package org.logicinfo.oms.bookingCreation.controller;

import java.util.ArrayList;
import java.util.List;

public class CreateBookingResponse {
	private Status status;
	
	private List<DeliveriesResponse> deliveries;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<DeliveriesResponse> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<DeliveriesResponse> deliveries) {
		this.deliveries = deliveries;
	}
	
	
}
