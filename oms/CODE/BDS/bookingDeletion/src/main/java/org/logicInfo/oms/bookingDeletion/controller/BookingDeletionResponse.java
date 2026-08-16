package org.logicInfo.oms.bookingDeletion.controller;

import java.util.ArrayList;

public class BookingDeletionResponse {
	private ArrayList<StatusResponse> status;

	public ArrayList<StatusResponse> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<StatusResponse> status) {
		this.status = status;
	}
}
