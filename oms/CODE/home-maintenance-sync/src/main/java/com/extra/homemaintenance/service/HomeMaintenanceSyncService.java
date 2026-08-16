package com.extra.homemaintenance.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.homemaintenance.dao.HomeMaintenanceDetailsDAO;
import com.extra.homemaintenance.model.HomeMaintenanceSyncRequest;
import com.extra.homemaintenance.model.HomeMaintenanceSyncResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HomeMaintenanceSyncService.java aibrahim 2024
 */
@Service
public class HomeMaintenanceSyncService {

	private static final Logger log = LogManager.getLogger(HomeMaintenanceSyncService.class);

	@Autowired
	private HomeMaintenanceDetailsDAO homemaintenanceDetailsDAO;

	@Autowired
	private HomeMaintenanceAPI homemaintenanceApi;

	public void scheduleHomeMaintenanceUpdate() {
		try {
			updateHomeMaintenance();
		} catch (Exception e) {
			log.error("Error occurred during scheduled Home Maintenance subscription update", e);
		}
	}

	public void updateHomeMaintenance() throws Exception {
		log.info("Getting Data from Home Maintenance subscription table...");
		List<HomeMaintenanceSyncRequest> homemaintenanceSyncRequestList = homemaintenanceDetailsDAO
				.getHomeMaintenanceMemberShipDetails();
		for (HomeMaintenanceSyncRequest homemaintenanceSyncRequest : homemaintenanceSyncRequestList) {
			log.info("Calling api for memberId... " + homemaintenanceSyncRequest.getSubscriptionId());
			HomeMaintenanceSyncResponse response = null;
			try {
				ObjectMapper om = new ObjectMapper();

				log.info("Request: " + om.writeValueAsString(homemaintenanceSyncRequest));
				response = homemaintenanceApi.homemaintenanceVipmembership(homemaintenanceSyncRequest);
				log.info("Response: " + om.writeValueAsString(response));

				if ("S".equals(response.getResponseHeader().getStatus())) {
					homemaintenanceDetailsDAO.updatePublishInd(homemaintenanceSyncRequest.getSubscriptionId());
				}else {
					homemaintenanceDetailsDAO.updateFailure(homemaintenanceSyncRequest.getSubscriptionId());
				}
				log.info("Success Calling api..." + response.toString() + "...Member Id - "
						+ homemaintenanceSyncRequest.getSubscriptionId());
			} catch (Exception ex) {
				homemaintenanceDetailsDAO.updateFailure(homemaintenanceSyncRequest.getSubscriptionId());
				log.error("Failed Calling API for memberId: " + homemaintenanceSyncRequest.getSubscriptionId(), ex);
			}
		}
	}
}
