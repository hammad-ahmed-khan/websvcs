package org.logicinfo.oms.slotBookingAvailability.controller;

import java.util.List;

public class Deliveries {
	private int deliveryId;
	private String deliveryType;
	private int pickLoc;
    
	private DeliveryAddress deliveryAddress;
    private List<Items> items;
    
    
    public int getDeliveryId(){
        return deliveryId;
    }
    public String getDeliveryType(){
        return deliveryType;
    }
    public int getPickLoc(){
        return pickLoc;
    }

    public void setDeliveryId(int deliveryId){
        this.deliveryId=deliveryId;
    }

    public void setDeliveryType(String deliveryType){
        this.deliveryType=deliveryType;
    }

    public void setPickLoc(int pickLoc){
        this.pickLoc=pickLoc;
    }
	
	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDelveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public List<Items> getItems() {
		return items;
	}
	public void setItems(List<Items> items) {
		this.items = items;
	}
	
}
