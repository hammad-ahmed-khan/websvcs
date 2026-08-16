package com.extra.jood.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseInfo {

	private String joodTranId;

	private String joodTranSeqNo;
	
	private Character status;

	private String resCode;

	private String message;


	public Character getStatus() {
		return status;
	}

	public String getResCode() {
		return resCode;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getJoodTranId() {
		return joodTranId;
	}

	public void setJoodTranId(String joodTranId) {
		this.joodTranId = joodTranId;
	}

	public String getJoodTranSeqNo() {
		return joodTranSeqNo;
	}

	public void setJoodTranSeqNo(String joodTranSeqNo) {
		this.joodTranSeqNo = joodTranSeqNo;
	}

}
