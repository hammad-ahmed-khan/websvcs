package com.extra.restservice.controller;

import java.util.List;

public class RealTimeInventoryRequest {

	private List<Items> items;

	private List<Locations> locations;

	private boolean storePickup;

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public List<Locations> getLocations() {
		return locations;
	}

	public void setLocations(List<Locations> locations) {
		this.locations = locations;
	}

	public boolean isStorePickup() {
		return storePickup;
	}

	public void setStorePickup(boolean storePickup) {
		this.storePickup = storePickup;
	}
}
