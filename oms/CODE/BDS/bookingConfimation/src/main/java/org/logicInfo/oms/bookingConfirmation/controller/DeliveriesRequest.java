package org.logicInfo.oms.bookingConfirmation.controller;

import java.util.ArrayList;

public class DeliveriesRequest {
	
	private int deliveryId;
	
	private ArrayList<BookingRequest> bookings;

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public ArrayList<BookingRequest> getBookings() {
		return bookings;
	}

	public void setBookings(ArrayList<BookingRequest> bookings) {
		this.bookings = bookings;
	}
}
