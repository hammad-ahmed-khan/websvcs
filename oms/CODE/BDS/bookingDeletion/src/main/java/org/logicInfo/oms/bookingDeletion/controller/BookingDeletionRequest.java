package org.logicInfo.oms.bookingDeletion.controller;

import java.util.ArrayList;

public class BookingDeletionRequest {
	private String orderNo;
	
	
	private ArrayList<DeliveriesRequest> deliveries;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
	public ArrayList<DeliveriesRequest> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ArrayList<DeliveriesRequest> deliveries) {
		this.deliveries = deliveries;
	}
	
	
}
