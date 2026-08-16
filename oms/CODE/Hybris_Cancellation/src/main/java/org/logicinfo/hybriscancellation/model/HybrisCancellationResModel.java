package org.logicinfo.hybriscancellation.model;

import java.util.List;

public class HybrisCancellationResModel {

	private String status;
	private String code;
	private String message;
	private List<HybrisCancellationResLineModel> lineRes;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<HybrisCancellationResLineModel> getLineRes() {
		return lineRes;
	}

	public void setLineRes(List<HybrisCancellationResLineModel> lineRes) {
		this.lineRes = lineRes;
	}

	public HybrisCancellationResModel() {

	}

}
