package com.extra.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author aibrahim
 *
 */
public class OmsUnapprovedTransfer {

	private String item;

	private BigDecimal location;

	private BigDecimal omsCustOrdNo;

	private BigDecimal tsfNo;

	private BigDecimal unapprovedQty;

	private Timestamp createDatetime;

	public String getItem() {
		return item;
	}

	public BigDecimal getLocation() {
		return location;
	}

	public BigDecimal getOmsCustOrdNo() {
		return omsCustOrdNo;
	}

	public BigDecimal getTsfNo() {
		return tsfNo;
	}

	public BigDecimal getUnapprovedQty() {
		return unapprovedQty;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setLocation(BigDecimal location) {
		this.location = location;
	}

	public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		this.omsCustOrdNo = omsCustOrdNo;
	}

	public void setTsfNo(BigDecimal tsfNo) {
		this.tsfNo = tsfNo;
	}

	public void setUnapprovedQty(BigDecimal unapprovedQty) {
		this.unapprovedQty = unapprovedQty;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}
}
