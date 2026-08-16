package com.extra.oms.custOrder.model;

import java.math.BigDecimal;

public class ItemSOH {

	private BigDecimal location;
	private BigDecimal soh;
	private int counter;

	public void setLocation(BigDecimal location) {
		this.location = location;
	}

	public BigDecimal getLocation() {
		return location;
	}

	public void setSoh(BigDecimal soh) {
		this.soh = soh;
	}

	public BigDecimal getSoh() {
		return soh;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}
}
