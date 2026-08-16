package com.extra.sim.model;

public class ExtraFulfillmentOrderLineItem {

	private String itemId;
	private String imeiNumber;
	private int quantity;
	private String itemDescription;
	private Long fulOrdLineItemId;
	private Long fulFillmentOrderId;
	private Long fulOrdDlvId;
	private Long storeId;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Long getFulOrdLineItemId() {
		return fulOrdLineItemId;
	}

	public void setFulOrdLineItemId(Long fulOrdLineItemId) {
		this.fulOrdLineItemId = fulOrdLineItemId;
	}

	public Long getFulFillmentOrderId() {
		return fulFillmentOrderId;
	}

	public void setFulFillmentOrderId(Long fulFillmentOrderId) {
		this.fulFillmentOrderId = fulFillmentOrderId;
	}

	public Long getFulOrdDlvId() {
		return fulOrdDlvId;
	}

	public void setFulOrdDlvId(Long fulOrdDlvId) {
		this.fulOrdDlvId = fulOrdDlvId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

}
