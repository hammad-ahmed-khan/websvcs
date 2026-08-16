package com.extra.apple.pricing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.apple.pricing.model.Response;
import com.extra.apple.pricing.service.PricingService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/pricing")
public class PricingController extends BaseController {
	
	private static final Logger LOG = Logger.getLogger(PricingController.class);

	@Autowired
	private PricingService pricingService;

	@GetMapping()
	public Response updatePricingDetail() throws Exception {
		LOG.info("Request received to update active mpns");
		pricingService.updatePricingDetail();
		Response response = new Response();
		response.setStatus("Y");
		LOG.info("Request processed successfully to update active mpns");
		return response;
	}

	@PostMapping()
	public Response publishPricingDetail() throws Exception {
		pricingService.publishPricingDetail();
		Response response = new Response();
		response.setStatus("Y");
		return response;
	}
}
