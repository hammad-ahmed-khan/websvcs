package com.extra.bds.bean;

import java.math.BigDecimal;
import java.util.List;

public class Group {

	private BigDecimal groupId;

	private String groupName;

	private BigDecimal slots;

	private BigDecimal regionID;

	private String regionName;

	private StatusResponse status;

	private List<Dates> dates;

	private List<Item> items;

	public List<Dates> getDates() {
		return dates;
	}

	public void setDates(List<Dates> dates) {
		this.dates = dates;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public BigDecimal getSlots() {
		return slots;
	}

	public void setSlots(BigDecimal slots) {
		this.slots = slots;
	}

	public BigDecimal getRegionID() {
		return regionID;
	}

	public void setRegionID(BigDecimal regionID) {
		this.regionID = regionID;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

}
