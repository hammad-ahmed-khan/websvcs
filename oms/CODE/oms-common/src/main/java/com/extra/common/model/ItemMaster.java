package com.extra.common.model;

import java.math.BigDecimal;

public class ItemMaster {

	private String item;
	private String standardUom;
	private BigDecimal dept;
	private String itemDesc;
	private String status;
	private String inventoryInd;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStandardUom() {
		return standardUom;
	}

	public void setStandardUom(String standardUom) {
		this.standardUom = standardUom;
	}

	public BigDecimal getDept() {
		return dept;
	}

	public void setDept(BigDecimal dept) {
		this.dept = dept;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInventoryInd() {
		return inventoryInd;
	}

	public void setInventoryInd(String inventoryInd) {
		this.inventoryInd = inventoryInd;
	}

}
