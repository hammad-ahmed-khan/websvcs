package com.extra.oms.spareParts.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockRequest {

	@JsonProperty("tsf_req_seq_id")
	protected String tsfReqSeqId;

	@JsonProperty("srv_req_id")
	protected String srvReqId;

	@JsonProperty("srv_aging")
	protected String srvAging;

	@JsonProperty("srv_line")
	protected String srvLine;

	@JsonProperty("req_loc")
	protected BigDecimal reqLoc;

	@JsonProperty("req_tech_id")
	protected String reqTechId;

	@JsonProperty("type")
	protected String type;

	@JsonProperty("items")
	protected List<Items> items;

	public String getTsfReqSeqId() {
		return tsfReqSeqId;
	}

	public void setTsfReqSeqId(String tsfReqSeqId) {
		this.tsfReqSeqId = tsfReqSeqId;
	}

	public String getSrvReqId() {
		return srvReqId;
	}

	public void setSrvReqId(String srvReqId) {
		this.srvReqId = srvReqId;
	}

	public String getSrvAging() {
		return srvAging;
	}

	public void setSrvAging(String srvAging) {
		this.srvAging = srvAging;
	}

	public String getSrvLine() {
		return srvLine;
	}

	public void setSrvLine(String srvLine) {
		this.srvLine = srvLine;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

}
