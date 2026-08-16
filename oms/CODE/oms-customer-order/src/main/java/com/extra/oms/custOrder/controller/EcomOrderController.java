package com.extra.oms.custOrder.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.extra.oms.custOrder.model.CustomerOrder;
import com.extra.oms.custOrder.model.CustomerOrderResponse;
import com.extra.oms.custOrder.service.EComOrderServiceImpl;

@Controller
public class EcomOrderController {

	private static final Logger log = LogManager.getLogger(EcomOrderController.class);

	@PostMapping(value = "/createNewOrder")
	public CustomerOrderResponse processNewOrder(@RequestBody CustomerOrder request) throws Exception {

		EComOrderServiceImpl customerOrderServiceImp = new EComOrderServiceImpl();
		log.info("***INPUT***" + request.toString());
//		1.Validating Input data
		customerOrderServiceImp.validateInput(request);
//

		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setMessageCode("500");
		response.setMessageStatus("success");

		return response;
	}
}
