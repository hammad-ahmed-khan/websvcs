package com.extra.oms.einvoicingxml.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.einvoicingxml.model.*;
import com.extra.oms.einvoicingxml.service.HybrisOmsXmlService;


@RestController
@RequestMapping(path = "/hybristranxml")
public class XmlController {

	private static final Logger LOG = Logger.getLogger(XmlController.class);

	@Autowired
	private HybrisOmsXmlService hybrisOmsXmlService;

	@PostMapping
	public Response saveXmlInfo(@RequestBody HybrisOmsXmlRequest hybomsxml) {
		Response response = new Response();
		LOG.info("Hybris Tranx API: Inside Save Hybris OMS XML method ");
		try {
			System.out.println("Inside SaveXMLInfo");
			hybrisOmsXmlService.saveXMLInfo(hybomsxml);
			response.setStatus("Success");
			response.setMessage("Transaction XML inserted Successfully");
			LOG.info("XML saved successfully");
		} catch (Exception e) {
			LOG.error("Error while saving the XML for order number " + hybomsxml.getOrderId(), e);
			response.setStatus("Failed");
			response.setMessage("Insertion of Transaction XML Failed");
		}
		return response;
	}
}

