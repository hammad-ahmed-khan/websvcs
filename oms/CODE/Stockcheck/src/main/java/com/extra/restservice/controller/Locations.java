package com.extra.restservice.controller;

import java.math.BigDecimal;

public class Locations {

	private String code;

	private String type;

	private BigDecimal channelId;

	private String physicalStockInd;

	private String packItemInd;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getChannelId() {
		return channelId;
	}

	public void setChannelId(BigDecimal channelId) {
		this.channelId = channelId;
	}

	public String getPhysicalStockInd() {
		return physicalStockInd;
	}

	public void setPhysicalStockInd(String physicalStockInd) {
		this.physicalStockInd = physicalStockInd;
	}

	public String getPackItemInd() {
		return packItemInd;
	}

	public void setPackItemInd(String packItemInd) {
		this.packItemInd = packItemInd;
	}

}
