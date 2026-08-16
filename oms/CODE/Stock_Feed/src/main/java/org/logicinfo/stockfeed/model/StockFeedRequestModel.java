package org.logicinfo.stockfeed.model;

public class StockFeedRequestModel {

	private String id;

	private String physicalStockInd;

	private String packItemInd;

	private String locType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhysicalStockInd() {
		return physicalStockInd;
	}

	public void setPhysicalStockInd(String physicalStockInd) {
		this.physicalStockInd = physicalStockInd;
	}

	public String getLocType() {
		return locType;
	}

	public void setLocType(String locType) {
		this.locType = locType;
	}

	public String getPackItemInd() {
		return packItemInd;
	}

	public void setPackItemInd(String packItemInd) {
		this.packItemInd = packItemInd;
	}
}
