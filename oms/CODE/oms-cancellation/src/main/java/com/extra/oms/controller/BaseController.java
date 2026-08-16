/**
 * 
 */
package com.extra.oms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.extra.oms.model.POSOrderCancelResponse;

/**
 * @author aibrahim
 *
 */
public abstract class BaseController {

	private static final Logger LOG = Logger.getLogger(OrderCancelController.class);

	@ExceptionHandler(Exception.class)
	public POSOrderCancelResponse handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		LOG.error("Error while process the request. " + request.getServletPath(), e);
		POSOrderCancelResponse res = new POSOrderCancelResponse();
		res.setResponseMessage("TECHNICAL_ERROR");
		res.setMessageStatus("E");
		return res;
	}
}
