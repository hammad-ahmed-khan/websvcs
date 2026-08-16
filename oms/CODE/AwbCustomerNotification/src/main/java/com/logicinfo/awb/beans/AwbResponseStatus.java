package com.logicinfo.awb.beans;

public class AwbResponseStatus {

	private  Boolean  result;
	private String statusMessage;

	public AwbResponseStatus() {

	}
	public AwbResponseStatus(Boolean result, String statusMessage) {
		this.result = result;
		this.statusMessage = statusMessage;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	

}
