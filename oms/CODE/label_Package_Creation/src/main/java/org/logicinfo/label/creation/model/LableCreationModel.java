package org.logicinfo.label.creation.model;

import java.util.ArrayList;


public class LableCreationModel {

	

	public String getstoreId() {
		return storeId;
	}

	public void setstoreId(String StoreId) {
		this.storeId = StoreId;
	}

	private String storeId;
	
	private String CustOrdNbr;

	private ArrayList<OrderDataModel> ItemPiecesResponse;

	
	

	public String getCustOrdNbr() {
		return CustOrdNbr;
	}

	public void setCustOrdNbr(String custOrdNbr) {
		CustOrdNbr = custOrdNbr;
	}

	public ArrayList<OrderDataModel> getItemPiecesResponse() {
		return ItemPiecesResponse;
	}

	public void setItemPiecesResponse(ArrayList<OrderDataModel> itemPiecesResponse) {
		ItemPiecesResponse = itemPiecesResponse;
	}

	LableCreationModel() {
		System.out.println("Default con");
	}

	

	

}