package com.extra.restservice.bean;

public class Customer {
	
	private String customerOrderNo;
	private String returnReqId;
	private String returnLocId;
	private String refundAmount;
	private String marketPlaceInd;
// 	Charan_PROD
//	private List<Items> items=new ArrayList<Items>();
//Charan_QA	
	private String itemCode;
	private int lineNo;
	private int orderQty;

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

	@Override
	public String toString() {
		return "Customer [customerOrderNo=" + customerOrderNo + ", returnReqId=" + returnReqId + ", returnLocId="
				+ returnLocId + ", refundAmount=" + refundAmount +", item ="+itemCode+ "]";
	}

	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getReturnReqId() {
		return returnReqId;
	}

	public void setReturnReqId(String returnReqId) {
		this.returnReqId = returnReqId;
	}

	public String getReturnLocId() {
		return returnLocId;
	}

	public void setReturnLocId(String returnLocId) {
		this.returnLocId = returnLocId;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getMarketPlaceInd() {
		return marketPlaceInd;
	}

	public void setMarketPlaceInd(String marketPlaceInd) {
		this.marketPlaceInd = marketPlaceInd;
	}

/*	Charan_PROD
 * public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}
	*/
	
	
	
	
	
	

}
