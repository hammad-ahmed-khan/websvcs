package com.extra.bds.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;

@RestController
public class AvailablityController extends BaseController {

	@PostMapping(path = "/available")
	public List<Response> getItemsAvailablity(@RequestBody Request request) throws Exception {
		return bookingService.getItemsAvailablity(request);
	}

	@PostMapping(path = "/available/order")
	public List<Response> getAvailablityByOrder(@RequestBody Request request) throws Exception {
		return bookingService.getAvailablityByOrder(request);
	}
}
