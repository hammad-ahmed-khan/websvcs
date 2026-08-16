package com.extra.oms.spareParts.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockDeductionRequest {
	@JsonProperty("sr_seq_id")
	protected String srSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	@JsonProperty("req_loc")
	protected BigDecimal reqLoc;

	@JsonProperty("req_tech_id")
	protected String reqTechId;

	@JsonProperty("qty")
	protected BigDecimal qty;

	@JsonProperty("reason_code")
	protected long reasonCode;

	@JsonProperty("oms_serv_id")
	protected Long omsServId;
	
	@JsonProperty("items")
	protected List<Items> items;

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

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public long getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(long reasonCode) {
		this.reasonCode = reasonCode;
	}

	public Long getOmsServId() {
		return omsServId;
	}

	public void setOmsServId(Long omsServId) {
		this.omsServId = omsServId;
	}

}
