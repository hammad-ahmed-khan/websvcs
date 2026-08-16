/**
 * 
 */
package com.extra.bds.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.extra.bds.bean.Response;
import com.extra.bds.bean.Status;
import com.extra.bds.service.BookingService;
import com.extra.bds.util.StatusException;

/**
 * @author aibrahim
 *
 */
public abstract class BaseController {

	private static final Logger LOG = Logger.getLogger(BaseController.class);

	@Autowired
	protected BookingService bookingService;

	@ExceptionHandler
	public List<Response> handleException(HttpServletRequest httpRequest, Exception e) {
		LOG.error("Error while processing the availablity request", e);
		Response response = new Response();
		if (e instanceof StatusException) {
			response.setStatus(((StatusException) e).getErrorStatus());
		} else {
			Status eStatus= new Status();
			eStatus.setCode("E-500");
			eStatus.setMessage("Internal Server Error");
			response.setStatus(eStatus);
		}
		List<Response> restResp = Collections.singletonList(response);
		try {
			bookingService.updateResponse(restResp);
		} catch (Exception se) {
			// EAT Exception
		}
		return restResp;
	}
}
