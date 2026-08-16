package org.logicInfo.oms.transferCreation.Controller;

public class TransferCreationResponse {
	private int code;
	private boolean success;
	private String tsf_No;
	private String message;
	
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
}
