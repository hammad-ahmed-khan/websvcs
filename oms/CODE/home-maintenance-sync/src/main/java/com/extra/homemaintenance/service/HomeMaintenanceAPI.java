package com.extra.homemaintenance.service;

import com.extra.homemaintenance.model.HomeMaintenanceSyncRequest;
import com.extra.homemaintenance.model.HomeMaintenanceSyncResponse;

import feign.Headers;
import feign.RequestLine;

@Headers({ "Content-Type: application/json" })
public interface HomeMaintenanceAPI {

	@RequestLine("POST /jood/api/homemaintenance/vip/subscription/sync")
	public HomeMaintenanceSyncResponse homemaintenanceVipmembership(HomeMaintenanceSyncRequest homemaintenanceSyncRequest);

}
