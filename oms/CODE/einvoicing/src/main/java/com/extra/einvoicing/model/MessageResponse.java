package com.extra.einvoicing.model;

/**
 * @author aibrahim
 *
 */
public class MessageResponse {

	private int statusCode;

	private String statusMessage;

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
