package com.extra.oms.sim;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.sim.bean.Request;
import com.extra.oms.sim.bean.Response;
import com.extra.oms.sim.service.DispatcherService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/dispatch")
public class DispatchController {

	private static final Logger LOG = Logger.getLogger(DispatchController.class);

	@Autowired
	private DispatcherService dispatcherService;

	@PostMapping()
	public Response dispatchItem(@RequestBody Request request) {
		Response response = new Response();
		try {
			LOG.info("Dispatch order in sim " + request.getOrderNo());
			dispatcherService.dispatchOrder(request);
			response.setCode("SUCCESS");
			LOG.info("Dispatching order in sim " + request.getOrderNo() + " is completed ");
		} catch (Exception e) {
			LOG.error("Error while dispatching the order ." + request.getOrderNo(), e);
			response.setCode("ERROR");
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
