package com.extra.jood;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.extra.jood.config.JoodSyncConfiguration;
import com.extra.jood.service.JoodSyncService;

public class JoodSyncApplication {

	private static final Logger LOG = LogManager.getLogger(JoodSyncApplication.class);

	public static void main(String[] args) {
	
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(JoodSyncConfiguration.class);
		try {
			applicationContext.getBean(JoodSyncService.class).updateJoodMembership();
		} catch (Exception e) {
			LOG.error("Error while updating the jood memberships", e);
		}
		applicationContext.close();
		LOG.info("Completed...");
	}
}
