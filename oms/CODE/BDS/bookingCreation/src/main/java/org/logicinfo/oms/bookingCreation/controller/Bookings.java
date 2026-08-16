package org.logicinfo.oms.bookingCreation.controller;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;

public class Bookings {
	private int groupId;
	private int reservationId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;
	
	private String windowCode;
	private int slots;
	private int regionID;
	private String regionName;
	
	private List<Items> items;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWindowCode() {
		return windowCode;
	}

	public void setWindowCode(String windowCode) {
		this.windowCode = windowCode;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}
	
	
}
