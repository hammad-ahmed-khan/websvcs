package com.extra.finance.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.extra.finance.dao.OrderDAO;

/**
 * @author aibrahim
 *
 */
@Service
public class OrderService {

	private static final Logger LOG = Logger.getLogger(OrderService.class);

	@Autowired
	private FTPService ftpService;

	@Autowired
	private OrderDAO orderDAO;

	@Scheduled(fixedDelay = 1000)
	public void checkOrderStatus() {
		LOG.info("Started the FTP file name reading process.");
		List<String> ordNos = orderDAO.getContractOrders();
		if (ordNos != null && !ordNos.isEmpty()) {
			ftpService.validateContractForOrders(ordNos);
			if (!ordNos.isEmpty()) {
				orderDAO.updateOrderStatus(ordNos);
				LOG.info("Order status updated successfully");
			}
		}
		LOG.info("FTP file name reading process completed");
	}
}
