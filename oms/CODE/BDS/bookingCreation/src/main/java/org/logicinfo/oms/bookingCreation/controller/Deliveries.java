package org.logicinfo.oms.bookingCreation.controller;

import java.util.ArrayList;
import java.util.List;

public class Deliveries {
	private int deliveryId;
	private String deliveryType;
	private int pickLoc;
    
	private DeliveryAddress deliveryAddress;
	private ArrayList<Bookings> bookings;
    
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
	public ArrayList<Bookings> getBookings() {
		return bookings;
	}
	public void setBookings(ArrayList<Bookings> bookings) {
		this.bookings = bookings;
	}
	
}
