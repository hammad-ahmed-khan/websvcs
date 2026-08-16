package org.logicinfo.oms.bookingCreation.controller;

import java.util.ArrayList;


public class CreateBookingRequest {
	private String orderNo;
	private String subOrderNo;
	private String orderType;
	private String country;
	private String comments;
	private boolean reserveOnly;
	
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
