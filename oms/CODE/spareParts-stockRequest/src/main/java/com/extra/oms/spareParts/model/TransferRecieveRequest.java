package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRecieveRequest {

	@JsonProperty("tsf_rec_seq_id")
	protected String tsfRecSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	public String getTsfRecSeqId() {
		return tsfRecSeqId;
	}

	public void setTsfRecSeqId(String tsfRecSeqId) {
		this.tsfRecSeqId = tsfRecSeqId;
	}

	public String getSrvReqId() {
		return srvReqId;
	}

	public void setSrvReqId(String srvReqId) {
		this.srvReqId = srvReqId;
	}

}
