package com.extra.oms.spareParts.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferCreationRequest {

	@JsonProperty("tsf_cre_seq_id")
	protected String tsfCreSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	@JsonProperty("req_loc")
	protected BigDecimal reqLoc;

	@JsonProperty("req_tech_id")
	protected String reqTechId;

	@JsonProperty("item")
	protected String item;

	@JsonProperty("qty")
	protected BigDecimal qty;

	@JsonProperty("src_loc")
	protected BigDecimal srcLoc;

	@JsonProperty("dest_loc")
	protected BigDecimal destLoc;

	@JsonProperty("reason_code")
	protected Long reasonCode;
	
	public String getTsfCreSeqId() {
		return tsfCreSeqId;
	}

	public void setTsfCreSeqId(String tsfCreSeqId) {
		this.tsfCreSeqId = tsfCreSeqId;
	}

	public String getSrvReqId() {
		return srvReqId;
	}

	public void setSrvReqId(String srvReqId) {
		this.srvReqId = srvReqId;
	}

	public BigDecimal getReqLoc() {
		return reqLoc;
	}

	public void setReqLoc(BigDecimal reqLoc) {
		this.reqLoc = reqLoc;
	}

	public String getReqTechId() {
		return reqTechId;
	}

	public void setReqTechId(String reqTechId) {
		this.reqTechId = reqTechId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getSrcLoc() {
		return srcLoc;
	}

	public void setSrcLoc(BigDecimal srcLoc) {
		this.srcLoc = srcLoc;
	}

	public BigDecimal getDestLoc() {
		return destLoc;
	}

	public void setDestLoc(BigDecimal destLoc) {
		this.destLoc = destLoc;
	}

	public Long getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Long reasonCode) {
		this.reasonCode = reasonCode;
	}

}
