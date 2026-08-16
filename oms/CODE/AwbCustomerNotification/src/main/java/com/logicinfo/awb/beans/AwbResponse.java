package com.logicinfo.awb.beans;

public class AwbResponse {
	
	private String status;	
	
	private String statusMessage;

	public AwbResponse() {

	}

	public AwbResponse(String status, String statusMessage) {
		super();
		this.status = status;
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public AwbResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
