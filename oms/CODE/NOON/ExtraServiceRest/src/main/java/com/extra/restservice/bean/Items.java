package com.extra.restservice.bean;

 public class Items {
	
	private String itemCode;
	private int lineNo;
	private int orderQty;
	
	
	@Override
	public String toString() {
		return "Items [itemCode=" + itemCode + ", lineNo=" + lineNo + ", orderQty=" + orderQty + "]";
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	
	

}
