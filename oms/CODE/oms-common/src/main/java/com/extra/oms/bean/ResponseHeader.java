package com.extra.oms.bean;

public class ResponseHeader {

	private String status;

	private String resCode;

	private String resMSG;

	public String getStatus() {
		return this.status;
	}

	public String getResCode() {
		return this.resCode;
	}

	public String getResMSG() {
		return this.resMSG;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public void setResMSG(String resMSG) {
		this.resMSG = resMSG;
	}
}
