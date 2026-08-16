package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelRequest {

	@JsonProperty("sr_seq_id")
	protected String srSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

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

}
