package com.logicinfo.awb.entites;

import java.io.Serializable;
import java.math.BigDecimal;

public class OmsAwbDetailInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal awbId;
	private BigDecimal id;
	private String itemId;
	private BigDecimal qty;
	private BigDecimal unitRetail;

	public OmsAwbDetailInfo() {
	}

	public BigDecimal getAwbId() {
		return this.awbId;
	}
	public void setAwbId(BigDecimal awbId) {
		this.awbId = awbId;
	}
	public String getItemId() {
		return this.itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUnitRetail() {
		return this.unitRetail;
	}

	public void setUnitRetail(BigDecimal unitRetail) {
		this.unitRetail = unitRetail;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}
}