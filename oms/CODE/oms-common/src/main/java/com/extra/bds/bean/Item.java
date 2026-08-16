package com.extra.bds.bean;

public class Item {

	private String itemCode;

	private int lineNo;

	private String linkLineNo;

	private String shippingClassification;

	private Boolean isBackOrder;

	private int orderQty;

	private String standardUom;

	private String unitRetailPrice;

	private String originalUnitRetailPrice;

	private String unitVATAmount;

	private String retailCurrency;

	private String transactionUom;

	private String isSubstitution;

	private String itemComments;

	private String group;

	private String dept;

	private String classs;

	private String subClass;

	private Boolean InventoryItemFlag;

	private String sourceLocation;

	private String fulfillmentLocation;

	private Integer rmsGroupNumber;

	private Integer rmsClassName;

	private Integer rmsSubClassName; 

	public String getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public String getFulfillmentLocation() {
		return fulfillmentLocation;
	}

	public void setFulfillmentLocation(String fulfillmentLocation) {
		this.fulfillmentLocation = fulfillmentLocation;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLinkLineNo(String linkLineNo) {
		this.linkLineNo = linkLineNo;
	}

	public String getLinkLineNo() {
		return linkLineNo;
	}

	public void setShippingClassification(String shippingClassification) {
		this.shippingClassification = shippingClassification;
	}

	public String getShippingClassification() {
		return shippingClassification;
	}

	public void setIsBackOrder(Boolean IsBackOrder) {
		this.isBackOrder = IsBackOrder;
	}

	public Boolean getIsBackOrder() {
		return isBackOrder;
	}

	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	public int getOrderQty() {
		return orderQty;
	}

	public void setStandardUom(String standardUom) {
		this.standardUom = standardUom;
	}

	public String getStandardUom() {
		return standardUom;
	}

	public void setUnitRetailPrice(String unitRetailPrice) {
		this.unitRetailPrice = unitRetailPrice;
	}

	public String getUnitRetailPrice() {
		return unitRetailPrice;
	}

	public void setOriginalUnitRetailPrice(String originalUnitRetailPrice) {
		this.originalUnitRetailPrice = originalUnitRetailPrice;
	}

	public String getOriginalUnitRetailPrice() {
		return originalUnitRetailPrice;
	}

	public void setUnitVATAmount(String unitVATAmount) {
		this.unitVATAmount = unitVATAmount;
	}

	public String getUnitVATAmount() {
		return unitVATAmount;
	}

	public void setRetailCurrency(String retailCurrency) {
		this.retailCurrency = retailCurrency;
	}

	public String getRetailCurrency() {
		return retailCurrency;
	}

	public void setTransactionUom(String transactionUom) {
		this.transactionUom = transactionUom;
	}

	public String getTransactionUom() {
		return transactionUom;
	}

	public void setIsSubstitution(String isSubstitution) {
		this.isSubstitution = isSubstitution;
	}

	public String getIsSubstitution() {
		return isSubstitution;
	}

	public void setItemComments(String itemComments) {
		this.itemComments = itemComments;
	}

	public String getItemComments() {
		return itemComments;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDept() {
		return dept;
	}

	public void setClasss(String classs) {
		this.classs = classs;
	}

	public String getClasss() {
		return classs;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}

	public String getSubClass() {
		return subClass;
	}

	public void setInventoryItemFlag(Boolean InventoryItemFlag) {
		this.InventoryItemFlag = InventoryItemFlag;
	}

	public Boolean getInventoryItemFlag() {
		return InventoryItemFlag;
	}

	public Integer getRmsGroupNumber() {
		return rmsGroupNumber;
	}

	public Integer getRmsClassName() {
		return rmsClassName;
	}

	public Integer getRmsSubClassName() {
		return rmsSubClassName;
	}

	public void setRmsGroupNumber(Integer rmsGroupNumber) {
		this.rmsGroupNumber = rmsGroupNumber;
	}

	public void setRmsClassName(Integer rmsClassName) {
		this.rmsClassName = rmsClassName;
	}

	public void setRmsSubClassName(Integer rmsSubClassName) {
		this.rmsSubClassName = rmsSubClassName;
	}

}
