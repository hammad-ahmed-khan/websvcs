package com.logicinfo.transfer.receive.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TsfReceiveResponse {
	private int code;
	private String success;
	@JsonInclude(value = Include.NON_NULL)
	private String tsf_No;
	@JsonInclude(value = Include.NON_NULL)
	private String message;
	@JsonInclude(value = Include.NON_NULL)
	private String error;
	@JsonInclude(value = Include.NON_NULL)
	private List<Items> items;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTsf_No() {
		return tsf_No;
	}

	public void setTsf_No(String tsf_No) {
		this.tsf_No = tsf_No;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
		this.message = message;
	}

}
