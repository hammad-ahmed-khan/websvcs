package com.extra.restservice.controller;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockcheckController {

	private static final Logger logger = LogManager.getLogger(StockcheckController.class.getName());

	@Autowired
	private LookUpInventoryServiceImpl lookupInventoryService;

	@PostMapping("/inventory")
	public ResponseEntity<RealTimeInventoryResponse> lookUpAvailableInventory(@RequestBody RealTimeInventoryRequest request) throws Exception {
		Date date1 = new Date();
		logger.info("lookupInventory realtime stockcheck Start Time " + date1);
		RealTimeInventoryResponse response = new RealTimeInventoryResponse();
		if (request != null && request.getItems().size() > 0 && request.getLocations().size() > 0) {
			logger.info("request is valid for lookupInventory ");
			response = lookupInventoryService.lookupInventory(request);
		} else {
			logger.info("request is invalid for lookupInventory ");
			Status status = new Status();
			status.setCode("F");
			status.setMessage("Request is invalid");
			response.setStatus(status);
		}
		Date date2 = new Date();
		logger.info("lookupInventory realtime stockcheck end Time " + date2);
		logger.info("Difference in start and end time for lookupInventory realtime stockcheck is " + (date2.getTime() - date1.getTime()) + " milliseconds");
		return new ResponseEntity<RealTimeInventoryResponse>(response, HttpStatus.OK);
	}

}
