package com.logicinfo.oms.model;

/**
 * ItemClassification.java
 * aibrahim
 * 2024
 */
public class ItemClassification {

	private String itemId;

	private String classification;

	private boolean isPreOrder;

	public String getItemId() {
		return itemId;
	}

	public String getClassification() {
		return classification;
	}

	public boolean isPreOrder() {
		return isPreOrder;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void setPreOrder(boolean isPreOrder) {
		this.isPreOrder = isPreOrder;
	}
}
