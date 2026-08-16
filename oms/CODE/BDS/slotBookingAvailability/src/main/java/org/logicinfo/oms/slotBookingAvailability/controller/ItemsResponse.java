package org.logicinfo.oms.slotBookingAvailability.controller;

import java.math.BigDecimal;

public class ItemsResponse {
	
	private String itemCode;
	private BigDecimal lineNo;
	private BigDecimal orderQty;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(BigDecimal lineNo) {
		System.out.println("LineNo:"+lineNo);
		this.lineNo = lineNo;
	}
	public BigDecimal getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
		
}
