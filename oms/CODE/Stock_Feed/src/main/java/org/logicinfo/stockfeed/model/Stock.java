package org.logicinfo.stockfeed.model;

import java.math.BigDecimal;

public class Stock {

	private String location;

	private BigDecimal quantity;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}
