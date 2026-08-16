package com.extra.jood.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.jood.dao.JoodDetailsDAO;
import com.extra.jood.model.JoodSyncRequest;
import com.extra.jood.model.JoodSyncResponse;

/**
 * JoodSyncService.java aibrahim 2024
 */
@Service
public class JoodSyncService {

	private static final Logger log = LogManager.getLogger(JoodSyncService.class);

	@Autowired
	private JoodDetailsDAO joodDetailsDAO;

	@Autowired
	private JoodAPI joodApi;

	public void scheduleJoodMembershipUpdate() {
		try {
			updateJoodMembership();
		} catch (Exception e) {
			log.error("Error occurred during scheduled Jood membership update", e);
		}
	}

	public void updateJoodMembership() throws Exception {
		log.info("Getting Data from Jood membership table...");
		List<JoodSyncRequest> joodSyncRequestList = joodDetailsDAO.getJoodMemberShipDetails();
		for (JoodSyncRequest joodSyncRequest : joodSyncRequestList) {
			log.info("Calling api for memberId... " + joodSyncRequest.getMembershipId());
			JoodSyncResponse response = null;
			try {
				response = joodApi.joodVipmembership(joodSyncRequest);
				if ("S".equals(response.getStatus())) {
					joodDetailsDAO.updatePublishInd(joodSyncRequest.getMembershipId());
				}
				log.info("Success Calling api..." + response.toString() + "...Member Id - " + joodSyncRequest.getMembershipId());
			} catch (Exception ex) {
				log.error("Failed Calling API for memberId: " + joodSyncRequest.getMembershipId(), ex);
			}
		}
	}
}
