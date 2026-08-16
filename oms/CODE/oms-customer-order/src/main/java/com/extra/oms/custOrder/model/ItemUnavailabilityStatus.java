package com.extra.oms.custOrder.model;

import java.util.ArrayList;
import java.util.List;

public class ItemUnavailabilityStatus {

	String status;

	List<ItemAvailability> unavailableInvtemsList = new ArrayList<ItemAvailability>();

	public ItemUnavailabilityStatus() {
		super();
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setUnavailableInvtemsList(List<ItemAvailability> unavailableInvtemsList) {
		this.unavailableInvtemsList = unavailableInvtemsList;
	}

	public List<ItemAvailability> getUnavailableInvtemsList() {
		return unavailableInvtemsList;
	}
}
