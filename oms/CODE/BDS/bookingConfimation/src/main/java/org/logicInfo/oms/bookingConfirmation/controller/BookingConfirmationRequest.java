package org.logicInfo.oms.bookingConfirmation.controller;

import java.util.ArrayList;

public class BookingConfirmationRequest {
	private String orderNo;
	
	private String omsOrderNo;
	
	private ArrayList<DeliveriesRequest> deliveries;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOmsOrderNo() {
		return omsOrderNo;
	}

	public void setOmsOrderNo(String omsOrderNo) {
		this.omsOrderNo = omsOrderNo;
	}

	public ArrayList<DeliveriesRequest> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ArrayList<DeliveriesRequest> deliveries) {
		this.deliveries = deliveries;
	}
	
	
}
