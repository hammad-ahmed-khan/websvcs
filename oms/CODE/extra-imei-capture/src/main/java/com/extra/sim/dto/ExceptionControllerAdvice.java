package com.extra.sim.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Madhuchandra
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		System.out.println("inside general exception");
		error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());// HttpStatus.INTERNAL_SERVER_ERROR.value()
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}
}
