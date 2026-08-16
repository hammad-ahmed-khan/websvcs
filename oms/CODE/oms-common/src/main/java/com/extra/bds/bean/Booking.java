package com.extra.bds.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Booking {

	private int groupId;

	private int reservationId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;

	private String windowCode;

	private Integer slots;

	private Integer regionID;

	private String regionName;

	private List<Item> items;

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

	public Integer getSlots() {
		return slots;
	}

	public void setSlots(Integer slots) {
		this.slots = slots;
	}

	public Integer getRegionID() {
		return regionID;
	}

	public void setRegionID(Integer regionID) {
		this.regionID = regionID;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
