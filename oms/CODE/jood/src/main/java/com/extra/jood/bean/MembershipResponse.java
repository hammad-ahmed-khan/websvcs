package com.extra.jood.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MembershipResponse.java
 * aibrahim
 * 2024
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipResponse {

	private ResponseInfo responseHeader;

	private List<ResponseDetail> responseDetails;

	public ResponseInfo getResponseHeader() {
		return responseHeader;
	}

	public List<ResponseDetail> getResponseDetails() {
		return responseDetails;
	}

	public void setResponseHeader(ResponseInfo responseHeader) {
		this.responseHeader = responseHeader;
	}

	public void setResponseDetails(List<ResponseDetail> responseDetails) {
		this.responseDetails = responseDetails;
	}
}
