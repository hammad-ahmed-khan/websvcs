package org.logicinfo.hybriscancellation.Exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.logicinfo.hybriscancellation.model.HybrisCancellationResModel;
import org.logicinfo.hybriscancellation.utill.UtillConstantsCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class hybriscancellationServerException {
	private static final Logger _LOGGER = LogManager.getLogger(hybriscancellationServerException.class.getName());
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HybrisCancellationResModel> exceptionHandler(Exception ex) {
		_LOGGER.info("Inside Server Exception Handler");
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		res.setCode(UtillConstantsCode.invaildInput);
		res.setStatus(UtillConstantsCode.failure);
		res.setMessage(UtillConstantsCode.request_error);
		_LOGGER.info(" Hybris Cancellation Web service is not for working" + ex);
		return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
	}
}
