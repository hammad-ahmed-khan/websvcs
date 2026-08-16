package com.extra.oms.spareParts.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class StockRecDetails {

	private String tsfRecSeqId;
	private String srvReqId;
	private String srvAging;
	private String srvLine;
	private BigDecimal reqLoc;
	private String reqTechId;
	private String type;
	private Timestamp creationTime;
	private Timestamp lastUpdatedTime;
	private String status;
	private String comments;
	private List<Items> items;

	public String getTsfRecSeqId() {
		return tsfRecSeqId;
	}

	public void setTsfReqSeqId(String tsfRecSeqId) {
		this.tsfRecSeqId = tsfRecSeqId;
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

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public Timestamp getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

}
