package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockResponse {

	@JsonProperty("success")
	protected String success;

	@JsonProperty("code")
	protected long code;

	@JsonProperty("message")
	protected String message;

	@JsonProperty("tsf_req_seq_id")
	protected String tsfReqSeqId;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getTsfReqSeqId() {
		return tsfReqSeqId;
	}

	public void setTsfReqSeqId(String tsfReqSeqId) {
		this.tsfReqSeqId = tsfReqSeqId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
