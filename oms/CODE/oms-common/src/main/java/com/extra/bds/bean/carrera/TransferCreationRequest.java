package com.extra.bds.bean.carrera;

import java.util.List;

public class TransferCreationRequest {

	private int src_id;

	private int dest_id;

	private String ref_no;

	private int src_loc;

	private int ful_loc;

	private int ful_ord_no;

	private String cust_ord_no;

	private List<TransferRequest> customerItems;

	public int getSrc_id() {
		return this.src_id;
	}

	public void setSrc_id(int src_id) {
		this.src_id = src_id;
	}

	public int getDest_id() {
		return this.dest_id;
	}

	public void setDest_id(int dest_id) {
		this.dest_id = dest_id;
	}

	public String getRef_no() {
		return this.ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}

	public int getSrc_loc() {
		return this.src_loc;
	}

	public void setSrc_loc(int src_loc) {
		this.src_loc = src_loc;
	}

	public int getFul_loc() {
		return this.ful_loc;
	}

	public void setFul_loc(int ful_loc) {
		this.ful_loc = ful_loc;
	}

	public int getFul_ord_no() {
		return this.ful_ord_no;
	}

	public void setFul_ord_no(int ful_ord_no) {
		this.ful_ord_no = ful_ord_no;
	}

	public String getCust_ord_no() {
		return this.cust_ord_no;
	}

	public void setCust_ord_no(String cust_ord_no) {
		this.cust_ord_no = cust_ord_no;
	}

	public List<TransferRequest> getCustomerItems() {
		return this.customerItems;
	}

	public void setCustomerItems(List<TransferRequest> customerItems) {
		this.customerItems = customerItems;
	}
}
