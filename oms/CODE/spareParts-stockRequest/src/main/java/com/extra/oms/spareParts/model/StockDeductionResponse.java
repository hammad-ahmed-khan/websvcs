package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockDeductionResponse {

	@JsonProperty("sr_seq_id")
	protected String srSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	@JsonProperty("oms_serv_id")
	protected Long omsServId;

	@JsonProperty("success")
	protected String success;

	@JsonProperty("code")
	protected long code;

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

	public Long getOmsServId() {
		return omsServId;
	}

	public void setOmsServId(Long omsServId) {
		this.omsServId = omsServId;
	}

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
