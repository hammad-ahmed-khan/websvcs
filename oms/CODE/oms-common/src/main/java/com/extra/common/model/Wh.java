package com.extra.common.model;

import java.math.BigDecimal;

/**
 * @author aibrahim
 *
 */
public class Wh {

	private BigDecimal id;

	private BigDecimal physicalWh;

	private Integer channelId;

	public BigDecimal getId() {
		return id;
	}

	public BigDecimal getPhysicalWh() {
		return physicalWh;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public void setPhysicalWh(BigDecimal physicalWh) {
		this.physicalWh = physicalWh;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
