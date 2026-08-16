package com.extra.bds.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Delivery {

	private int deliveryId;

	private String deliveryType;

	private Integer pickLoc;

	private DeliveryAddress deliveryAddress;

	private List<Item> items;

	private List<Booking> bookings;

	private List<Group> groups;

	@JsonIgnore
	private Status status;

	public int getDeliveryId() {
		return deliveryId;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public Integer getPickLoc() {
		return pickLoc;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setPickLoc(Integer pickLoc) {
		this.pickLoc = pickLoc;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
