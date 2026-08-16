package com.extra.oms.sim.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author aibrahim
 *
 */
@JsonInclude(content = Include.NON_NULL)
public class Request {

	private String orderNo;

	private String fulfilNo;

	private String item;

	private int qty;

	public String getOrderNo() {
		return orderNo;
	}

	public String getFulfilNo() {
		return fulfilNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setFulfilNo(String fulfilNo) {
		this.fulfilNo = fulfilNo;
	}

	public String getItem() {
		return item;
	}

	public int getQty() {
		return qty;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
}
