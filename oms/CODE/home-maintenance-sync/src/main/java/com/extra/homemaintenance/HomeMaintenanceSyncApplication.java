package com.extra.homemaintenance;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.extra.homemaintenance.config.HomeMaintenanceSyncConfiguration;
import com.extra.homemaintenance.service.HomeMaintenanceSyncService;

public class HomeMaintenanceSyncApplication {

	private static final Logger LOG = LogManager.getLogger(HomeMaintenanceSyncApplication.class);

	public static void main(String[] args) {
	
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(HomeMaintenanceSyncConfiguration.class);
		try {
			applicationContext.getBean(HomeMaintenanceSyncService.class).updateHomeMaintenance();
		} catch (Exception e) {
			LOG.error("Error while updating the Home Maintenance subscriptions", e);
		}
		applicationContext.close();
		LOG.info("Completed...");
	}
}
