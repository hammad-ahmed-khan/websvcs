package com.extra.oms.discount.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.discount.model.DiscountInfo;
import com.extra.oms.discount.model.Response;
import com.extra.oms.discount.service.DiscountService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/discount")
public class DiscountController {

	private static final Logger LOG = Logger.getLogger(DiscountController.class);

	@Autowired
	private DiscountService discountService;

	@PostMapping
	public Response saveDiscountInfo(@RequestBody DiscountInfo discount) {
		Response response = new Response();
		LOG.info("Request received to process discount ");
		try {
			discountService.saveDiscountInfo(discount);
			response.setStatus("Success");
			response.setMessage("Discount detail updated Successfully");
			LOG.info("Request saved successfully");
		} catch (Exception e) {
			LOG.info("Error while saving the request", e);
			response.setStatus("Failed");
			response.setMessage("Discount detail update Failed");
		}
		return response;
	}
}
