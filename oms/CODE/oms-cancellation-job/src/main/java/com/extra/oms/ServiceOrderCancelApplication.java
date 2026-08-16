package com.extra.oms;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.extra.oms.config.OrderCancelConfiguration;
import com.extra.oms.service.LockService;
import com.extra.oms.service.OrderCancelService;

public class ServiceOrderCancelApplication {

	private static final Logger LOG = LogManager.getLogger(ServiceOrderCancelApplication.class);

	public static void main(String[] args) {
	
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(OrderCancelConfiguration.class);
		LockService lockService = null;
		try {
			lockService = applicationContext.getBean(LockService.class);
			lockService.lockApplication();
			applicationContext.getBean(OrderCancelService.class).cancelServiceOrders();
		} catch (Exception e) {
			LOG.error("Error while cancelling the service orders ", e);
		} finally {
			lockService.unlockApplication();
		}
		applicationContext.close();
		LOG.info("Completed...");
	}
}
