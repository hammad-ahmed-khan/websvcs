package com.extra.oms.crossChannel.controller;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.crossChannel.model.CrossChannelRequest;
import com.extra.oms.crossChannel.model.CrossChannelResponse;
import com.extra.oms.crossChannel.model.ErrorResponse;
import com.extra.oms.crossChannel.model.ServiceOfferedRequest;
import com.extra.oms.crossChannel.model.ServiceOfferedResponse;
import com.extra.oms.crossChannel.service.CrossChannelService;

@RestController
public class CorssChannelController {

	@Autowired
	private CrossChannelService crossChannelService;

	private static final Logger LOG = LogManager.getLogger(CorssChannelController.class);

//	1.Cross Channel Services Sales
	@PostMapping(path = "/offlineorders")
	public ResponseEntity<?> crossChannel(@RequestBody CrossChannelRequest request) {
		ErrorResponse errorResponse = null;
		try {
			List<CrossChannelResponse> response = crossChannelService.processCrossChannel(request);
			if (response == null || response.isEmpty()) {
				return ResponseEntity.ok(new ErrorResponse("404", "No Record Found"));
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorResponse = new ErrorResponse("500", "Failed to process the request - " + e.getMessage());
			return ResponseEntity.ok(errorResponse);
		}
	}
	
//	1.Cross Channel Services Sales
	@PostMapping(path = "/servicesoffered")
	public ResponseEntity<?> crossChannelServicesoffered(@RequestBody ServiceOfferedRequest request) {
		ErrorResponse errorResponse = null;
		try {
			List<ServiceOfferedResponse> response = crossChannelService.processCrossChannelServicesoffered(request);
			if (response == null || response.isEmpty()) {
				return ResponseEntity.ok(new ErrorResponse("404", "No Record Found"));
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOG.error("Failed to process the request - " + request.getMobileNo(), e);
			errorResponse = new ErrorResponse("500", "Failed to process the request - " + e.getMessage());
			return ResponseEntity.ok(errorResponse);
		}
	}
}
