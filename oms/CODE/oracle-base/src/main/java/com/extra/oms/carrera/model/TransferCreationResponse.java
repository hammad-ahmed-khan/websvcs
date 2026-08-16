package com.extra.oms.carrera.model;

public class TransferCreationResponse {

	private int code;

	private boolean success;

	private String tsf_No;

	private String message;

	public String getTsf_No() {
		return this.tsf_No;
	}

	public void setTsf_No(String tsf_No) {
		this.tsf_No = tsf_No;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

/*
 * Location:
 * C:\Users\aibrahim\AppData\Local\Temp\Rar$DRa13780.3666\WEB-INF\lib\oracle-
 * base-0.0.1.jar!\com\extra\oms\carrera\model\TransferCreationResponse.class
 * Java compiler version: 6 (50.0) JD-Core Version: 1.1.3
 */