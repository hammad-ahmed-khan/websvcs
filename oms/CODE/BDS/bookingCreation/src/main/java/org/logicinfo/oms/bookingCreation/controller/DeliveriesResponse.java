package org.logicinfo.oms.bookingCreation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveriesResponse {

	int deliveryId;

	private List<BookingsResponse> bookings = new ArrayList<BookingsResponse>();

	@JsonIgnore
	private Status statuss;

	public DeliveriesResponse() {
		super();
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public int getDeliveryId() {
		return deliveryId;
	}

	public List<BookingsResponse> getBookings() {
		return bookings;
	}

	public void setBookings(BookingsResponse bookingsObj) {
		this.bookings.add(bookingsObj);
	}
	@JsonIgnore
	public Status getStatuss() {
		return statuss;
	}
	@JsonProperty
	public void setStatuss(Status statuss) {
		this.statuss = statuss;
	}

}
