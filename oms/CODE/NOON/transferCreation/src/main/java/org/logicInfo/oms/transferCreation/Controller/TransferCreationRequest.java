package org.logicInfo.oms.transferCreation.Controller;

import java.util.ArrayList;

public class TransferCreationRequest {
	private int src_id;
	private int dest_id;
	private String ref_no;

	private ArrayList<TransferRequest> customerItems;

	public int getSrc_id() {
		return src_id;
	}

	public void setSrc_id(int src_id) {
		this.src_id = src_id;
	}

	public int getDest_id() {
		return dest_id;
	}

	public void setDest_id(int dest_id) {
		this.dest_id = dest_id;
	}

	public String getRef_no() {
		return ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}

	public ArrayList<TransferRequest> getCustomerItems() {
		return customerItems;
	}

	public void setCustomerItems(ArrayList<TransferRequest> customerItems) {
		this.customerItems = customerItems;
	}

}
