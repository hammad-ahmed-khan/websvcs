package org.logicinfo.oms.slotBookingAvailability.controller;

import java.util.ArrayList;

public class SlotsAvailCheckRequest {

	private String orderNo;

	private String subOrderNo;

	private String orderType;

	private String country;

	private String comments;

	private boolean reserveOnly;

	private String createDate;

	private String payInStore;

	private CustomerDetails customerDetails;

	private ArrayList<Deliveries> deliveries;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isReserveOnly() {
		return reserveOnly;
	}

	public void setReserveOnly(boolean reserveOnly) {
		this.reserveOnly = reserveOnly;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPayInStore() {
		return payInStore;
	}

	public void setPayInStore(String payInStore) {
		this.payInStore = payInStore;
	}

	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public ArrayList<Deliveries> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ArrayList<Deliveries> deliveries) {
		this.deliveries = deliveries;
	}
}
