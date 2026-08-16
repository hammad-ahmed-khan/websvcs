package com.extra.jood.bean;

public class Lines {

	private long lineNumber;
	private String item;
	private long qty;

	private long unitRetailPrice;
	private long unitTotalRetailDiscount;
	private long unitTotalJoodDiscount;
	private long unitSellingPrice;

	private String unitRewardsCB;
	private String unitRedeemedCB;
	private String unitAvailableCB;

	public long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public long getUnitRetailPrice() {
		return unitRetailPrice;
	}

	public void setUnitRetailPrice(long unitRetailPrice) {
		this.unitRetailPrice = unitRetailPrice;
	}

	public long getUnitTotalRetailDiscount() {
		return unitTotalRetailDiscount;
	}

	public void setUnitTotalRetailDiscount(long unitTotalRetailDiscount) {
		this.unitTotalRetailDiscount = unitTotalRetailDiscount;
	}

	public long getUnitTotalJoodDiscount() {
		return unitTotalJoodDiscount;
	}

	public void setUnitTotalJoodDiscount(long unitTotalJoodDiscount) {
		this.unitTotalJoodDiscount = unitTotalJoodDiscount;
	}

	public long getUnitSellingPrice() {
		return unitSellingPrice;
	}

	public void setUnitSellingPrice(long unitSellingPrice) {
		this.unitSellingPrice = unitSellingPrice;
	}

	public String getUnitRewardsCB() {
		return unitRewardsCB;
	}

	public void setUnitRewardsCB(String unitRewardsCB) {
		this.unitRewardsCB = unitRewardsCB;
	}

	public String getUnitRedeemedCB() {
		return unitRedeemedCB;
	}

	public void setUnitRedeemedCB(String unitRedeemedCB) {
		this.unitRedeemedCB = unitRedeemedCB;
	}

	public String getUnitAvailableCB() {
		return unitAvailableCB;
	}

	public void setUnitAvailableCB(String unitAvailableCB) {
		this.unitAvailableCB = unitAvailableCB;
	}

}
