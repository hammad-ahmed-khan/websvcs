package com.extra.oms.service;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.dao.ServiceOrderDetailsDAO;
import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.oms.model.POSOrderCancelResponse;

/**
 * OrderCancelService.java
 * aibrahim
 * 2024
 */
@Service
public class OrderCancelService {

	private static final Logger log = LogManager.getLogger(OrderCancelService.class);

	@Autowired
	private ServiceOrderDetailsDAO serviceOrderDetailsDAO;

	@Autowired
	private OMSCancellationAPI cancellationAPI;

	public void cancelServiceOrders() throws Exception {
		log.info("Getting Data from Service Order cancellation table...");
		Collection<POSOrderCancelRequest> cancellationRequestList = serviceOrderDetailsDAO.getServiceOrderDetails();
		for (POSOrderCancelRequest serviceOrder : cancellationRequestList) {
			log.info("Calling oms order no " + serviceOrder.getCustOrderNo());
			POSOrderCancelResponse response = null;
			try {
				response = cancellationAPI.cancelOMSOrder(serviceOrder);
				if ("S".equals(response.getMessageStatus())) {
					serviceOrderDetailsDAO.updateCancelRequestStatus(serviceOrder.getCustOrderNo());
				}
				log.info("Success cancellation api..." + response.toString() + "...Order Id - " + serviceOrder.getCustOrderNo());
			} catch (Exception ex) {
				log.error("Failed Calling API for Order no: " + serviceOrder.getCustOrderNo(), ex);
			}
		}
	}
}
