
package com.extra.oms.custOrder.model;

import java.math.BigDecimal;
import java.util.List;

public class CustomerOrderResponseItems {

   
    protected String item;

    protected BigDecimal orderQtySuom;
    protected BigDecimal fulfillQtySuom;
    protected String status;
    protected String statusMessage;
    protected List<CustomerOrderResponseItemFulfillment> customerOrderResponseItemFulfillment;
    protected BigDecimal availableQty;
    protected long lineNo;
    
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public BigDecimal getOrderQtySuom() {
		return orderQtySuom;
	}
	public void setOrderQtySuom(BigDecimal orderQtySuom) {
		this.orderQtySuom = orderQtySuom;
	}
	public BigDecimal getFulfillQtySuom() {
		return fulfillQtySuom;
	}
	public void setFulfillQtySuom(BigDecimal fulfillQtySuom) {
		this.fulfillQtySuom = fulfillQtySuom;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public List<CustomerOrderResponseItemFulfillment> getCustomerOrderResponseItemFulfillment() {
		return customerOrderResponseItemFulfillment;
	}
	public void setCustomerOrderResponseItemFulfillment(List<CustomerOrderResponseItemFulfillment> customerOrderResponseItemFulfillment) {
		this.customerOrderResponseItemFulfillment = customerOrderResponseItemFulfillment;
	}
	public BigDecimal getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(BigDecimal availableQty) {
		this.availableQty = availableQty;
	}
	public long getLineNo() {
		return lineNo;
	}
	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}

  
}
