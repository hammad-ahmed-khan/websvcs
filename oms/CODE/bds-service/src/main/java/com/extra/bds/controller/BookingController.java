/**
 * 
 */
package com.extra.bds.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/booking")
public class BookingController extends BaseController {

	@PostMapping
	public List<Response> slotBooking(@RequestBody Request request) throws Exception {
		return bookingService.slotBooking(request);
	}

	@PutMapping
	public List<Response> confirmBooking(@RequestBody Request request) throws Exception {
		return bookingService.confirmBooking(request);
	}

	@DeleteMapping
	public List<Response> cancelBooking(@RequestBody Request request) throws Exception {
		return bookingService.cancelBooking(request);
	}
}
