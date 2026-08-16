package com.extra.oms.core.bean;

import java.util.Date;

public class BookingDetail {

	private Long bookingId;

	private String item;

	private Date date;

	private String deliveryTime;

	private String status;

	private String window;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}
}
