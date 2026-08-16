/**
 * 
 */
package com.extra.apple.pricing.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.extra.apple.pricing.model.Response;

/**
 * @author aibrahim
 *
 */
public abstract class BaseController {
	
	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler
	public Response handleException(HttpServletResponse httpResponse, Exception e) {
		LOG.error("Error while processing the request", e);;
		Response response = new Response();
		response.setStatus("N");
		response.setMessage(e.getMessage());
		return response;
	}
}
