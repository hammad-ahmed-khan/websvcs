package com.extra.bds.bean;

import java.util.List;

public class Request {

	private String orderNo;

	private String subOrderNo;

	private String orderType;

	private String country;

	private String comments;

	private boolean reserveOnly;

	private String createDate;

	private String payInStore;

	private String deliveryChargeInd;

	/*
	 * 
	 */
	private Long reservationId;

	private Long requestorId;

	private CustomerDetail customerDetails;

	private List<Delivery> deliveries;

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

	public CustomerDetail getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetail customerDetails) {
		this.customerDetails = customerDetails;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public String getDeliveryChargeInd() {
		return deliveryChargeInd;
	}

	public void setDeliveryChargeInd(String deliveryChargeInd) {
		this.deliveryChargeInd = deliveryChargeInd;
	}

	public Long getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(Long requestorId) {
		this.requestorId = requestorId;
	}
}
