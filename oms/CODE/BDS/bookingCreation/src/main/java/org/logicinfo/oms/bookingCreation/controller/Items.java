package org.logicinfo.oms.bookingCreation.controller;

public class Items {
	private int lineNo;
	private String itemCode;
	private String shippingClassification;
	private Boolean isBackOrder;
	private int orderQty;
	private int unitRetailPrice;
	private String retailCurrency;
	private int rmsGroupNumber;
	private int rmsClassName;
	private int rmsSubClassName; 
	
	
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }
	public int getOrderQty() {
        return orderQty;
    }
	public String getShippingClassification() {
		return shippingClassification;
	}
	public void setShippingClassification(String shippingClassification) {
		this.shippingClassification = shippingClassification;
	}
	public Boolean getIsBackOrder() {
		return isBackOrder;
	}
	public void setIsBackOrder(Boolean isBackOrder) {
		this.isBackOrder = isBackOrder;
	}
	public int getUnitRetailPrice() {
		return unitRetailPrice;
	}
	public void setUnitRetailPrice(int unitRetailPrice) {
		this.unitRetailPrice = unitRetailPrice;
	}
	public String getRetailCurrency() {
		return retailCurrency;
	}
	public void setRetailCurrency(String retailCurrency) {
		this.retailCurrency = retailCurrency;
	}
	public int getRmsGroupNumber() {
		return rmsGroupNumber;
	}
	public void setRmsGroupNumber(int rmsGroupNumber) {
		this.rmsGroupNumber = rmsGroupNumber;
	}
	public int getRmsClassName() {
		return rmsClassName;
	}
	public void setRmsClassName(int rmsClassName) {
		this.rmsClassName = rmsClassName;
	}
	public int getRmsSubClassName() {
		return rmsSubClassName;
	}
	public void setRmsSubClassName(int rmsSubClassName) {
		this.rmsSubClassName = rmsSubClassName;
	}
}
