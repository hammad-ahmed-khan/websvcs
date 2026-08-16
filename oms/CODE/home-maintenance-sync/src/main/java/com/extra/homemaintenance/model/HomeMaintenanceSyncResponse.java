package com.extra.homemaintenance.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeMaintenanceSyncResponse {

	@JsonProperty("responseHeader")
	private ResponseHeader responseHeader;

	@JsonProperty("subscriptionSyncDetails")
	private SubscriptionSyncDetails subscriptionSyncDetails;

	// Getters & Setters

	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public SubscriptionSyncDetails getSubscriptionSyncDetails() {
		return subscriptionSyncDetails;
	}

	public void setSubscriptionSyncDetails(SubscriptionSyncDetails subscriptionSyncDetails) {
		this.subscriptionSyncDetails = subscriptionSyncDetails;
	}

	// ================= INNER CLASSES =================

	public static class ResponseHeader {

		@JsonProperty("status")
		private String status;

		@JsonProperty("resCode")
		private String resCode;

		@JsonProperty("resMSG")
		private String resMSG;

		@JsonProperty("errorMSG")
		private String errorMSG;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getResCode() {
			return resCode;
		}

		public void setResCode(String resCode) {
			this.resCode = resCode;
		}

		public String getResMSG() {
			return resMSG;
		}

		public void setResMSG(String resMSG) {
			this.resMSG = resMSG;
		}

		public String getErrorMSG() {
			return errorMSG;
		}

		public void setErrorMSG(String errorMSG) {
			this.errorMSG = errorMSG;
		}
	}

	public static class SubscriptionSyncDetails {

		@JsonProperty("status")
		private String status;

		@JsonProperty("statusCode")
		private String statusCode;

		@JsonProperty("statusMessage")
		private String statusMessage;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getStatusMessage() {
			return statusMessage;
		}

		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
	}

}
