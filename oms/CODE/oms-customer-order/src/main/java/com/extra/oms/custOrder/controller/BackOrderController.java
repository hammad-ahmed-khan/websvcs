package com.extra.oms.custOrder.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.common.exception.BaseException;
import com.extra.oms.custOrder.service.BackOrderService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/back-order")
public class BackOrderController {

	private static final Logger LOG = Logger.getLogger(BackOrderController.class);

	@Autowired
	private BackOrderService backOrderService;

	@Autowired
	private ThreadPoolExecutor backOrderExecutor;

	@GetMapping
	public Map<String, Object> getProcessStatus() {
		Map<String, Object> pInfo = new HashMap<>();
		pInfo.put("activeOrders", backOrderExecutor.getActiveCount());
		pInfo.put("coreThreads", backOrderExecutor.getCorePoolSize());
		pInfo.put("maxThreads", backOrderExecutor.getMaximumPoolSize());
		pInfo.put("currentThreads", backOrderExecutor.getPoolSize());
		pInfo.put("taskCount", backOrderExecutor.getTaskCount());
		pInfo.put("pendingTask", backOrderExecutor.getQueue().size());
		return pInfo;
	}

	@PostMapping()
	public String processBackOrders() throws BaseException {
		String response = "SUCCESS";
		try {
			LOG.info("Back Order process started");
			backOrderService.processBackOrders();
			LOG.info("Back Order process completed successfully.");
		} catch (Exception e) {
			response = "FAILED" + (e instanceof BaseException ? " - " + e.getMessage() : "");
			LOG.error("Back order process failed", e);
		}
		return response;
	}
}
