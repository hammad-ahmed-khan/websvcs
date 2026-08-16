package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRecieveResponse {

	@JsonProperty("code")
	protected long code;

	@JsonProperty("success")
	protected String success;

	@JsonProperty("message")
	protected String message;

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
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
