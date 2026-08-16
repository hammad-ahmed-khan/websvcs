package org.logicinfo.stockfeed.model;

import java.util.List;

public class StockFeed {

	private String productCode;

	private List<Stock> stock;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<Stock> getStock() {
		return stock;
	}

	public void setStock(List<Stock> stock) {
		this.stock = stock;
	}
}
