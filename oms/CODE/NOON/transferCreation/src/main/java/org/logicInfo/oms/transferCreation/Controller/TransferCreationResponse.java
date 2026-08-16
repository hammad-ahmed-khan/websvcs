package org.logicInfo.oms.transferCreation.Controller;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TransferCreationResponse {
	private int code;
	private boolean success;
	private String tsf_No;
	private String message;
	@JsonInclude(value = Include.NON_NULL)
	private String error;
	@JsonInclude(value = Include.NON_NULL)
	private ArrayList<Items> items;

	public String getTsf_No() {
		return tsf_No;
	}

	public void setTsf_No(String tsf_No) {
		this.tsf_No = tsf_No;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ArrayList<Items> getItems() {
		return items;
	}

	public void setItems(ArrayList<Items> items) {
		this.items = items;
	}

}
