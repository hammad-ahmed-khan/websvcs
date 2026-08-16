package com.extra.imei.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.imei.bean.IMEIValidateReq;
import com.extra.imei.bean.IMEIValidationRes;
import com.extra.imei.service.IMEIService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class IMEIController {

	private static final Logger LOG = Logger.getLogger(IMEIController.class);

	@Autowired
	private IMEIService imeiService;

	@PostMapping(path = "/validate")
	public IMEIValidationRes validateIMEI(@RequestBody() IMEIValidateReq validateReq) {
		LOG.info("Validating Item IMEI number");
		return imeiService.validateIMEI(validateReq);
	}

	@ExceptionHandler
	public IMEIValidationRes handleException(HttpServletResponse response, Exception e) {
		LOG.error("Error while validating the imei ", e);
		IMEIValidationRes res = new IMEIValidationRes();
		res.setMessage("Technical error: " + e.getMessage());
		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		return res;
	}
}
