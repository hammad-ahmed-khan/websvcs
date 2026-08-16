/**
 * 
 */
package com.extra.oms.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.extra.oms.common.BaseException;

/**
 * @author aibrahim
 *
 */
public abstract class BaseController {

	private static final Logger _LOG = Logger.getLogger(BaseController.class);

	@ExceptionHandler
	public void handleException(HttpServletResponse response, Exception e) {
		_LOG.error("Error while processing the request", e);
		if (e instanceof BaseException) {
			try {
				response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((BaseException) e).getCode());
			} catch (IOException e1) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		} else {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
