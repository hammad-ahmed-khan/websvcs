package com.extra.ecom.reconcilliation.bean;

import java.math.BigDecimal;

public class ProductDetail {

	private String productCode;

	private BigDecimal basePrice;

	private BigDecimal sellingPrice;

	public String getProductCode() {
		return productCode;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
}
