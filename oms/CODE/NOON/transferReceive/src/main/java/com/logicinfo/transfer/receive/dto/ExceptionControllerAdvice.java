package com.logicinfo.transfer.receive.dto;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logicinfo.transfer.receive.dao.TransferReceiveDaoImpl;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;

/**
 * @author Madhuchandra
 */
@ControllerAdvice
public class ExceptionControllerAdvice
{
  
  private static final Logger log = Logger.getLogger(ExceptionControllerAdvice.class.getName());

  @ExceptionHandler(Exception.class)
  public ResponseEntity<TsfReceiveResponse> exceptionHandler(Exception ex)
  {
    TsfReceiveResponse response = new TsfReceiveResponse();
    log.info("inside general exception");
    response.setCode(500);
    response.setMessage("Failure");
    response.setMessage(ex.getMessage());
    return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
  }
}
