package com.extra.oms.bean.ws;

import java.math.BigDecimal;

/**
 * @author aibrahim
 *
 */
public class WhAdjModVo {

	private String item;

	private Integer status;

	private BigDecimal location;

	private String locationType;

	private BigDecimal qty;

	private Integer reasonCode;

	private String userId;

	public String getItem() {
		return item;
	}

	public Integer getStatus() {
		return status;
	}

	public BigDecimal getLocation() {
		return location;
	}

	public String getLocationType() {
		return locationType;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public Integer getReasonCode() {
		return reasonCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setLocation(BigDecimal location) {
		this.location = location;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
