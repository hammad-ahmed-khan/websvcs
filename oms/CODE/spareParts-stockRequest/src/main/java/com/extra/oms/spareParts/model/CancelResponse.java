package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelResponse {

	@JsonProperty("sr_seq_id")
	protected String srSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	@JsonProperty("success")
	protected String success;

	@JsonProperty("code")
	protected Long code;

	@JsonProperty("message")
	protected String message;

	public String getSrSeqId() {
		return srSeqId;
	}

	public void setSrSeqId(String srSeqId) {
		this.srSeqId = srSeqId;
	}

	public String getSrvReqId() {
		return srvReqId;
	}

	public void setSrvReqId(String srvReqId) {
		this.srvReqId = srvReqId;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
        if (message != null && message.length() > 30) {
            this.message = message.substring(0, 30); 
        } else {
            this.message = message;	
        }
    }

}
