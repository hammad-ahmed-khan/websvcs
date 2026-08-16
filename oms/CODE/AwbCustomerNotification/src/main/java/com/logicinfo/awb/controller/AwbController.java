package com.logicinfo.awb.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.logicinfo.awb.beans.AwbRequest;
import com.logicinfo.awb.beans.AwbResponse;
import com.logicinfo.awb.beans.AwbResponseStatus;
import com.logicinfo.awb.service.AwbTrackingService;

@Controller
@RequestMapping("/awb")
public class AwbController {

	
	private static final Logger _LOGGER = LogManager.getLogger(AwbController.class.getName());
	
	
	@Autowired
	private AwbTrackingService service;

	public AwbController() {
		System.out.println("*************AWBController ***************************");
	}

	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String getMessage() {
		_LOGGER.info(" Awb Get Method is called to check the service is up or not");
		return "Service Is Up";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AwbResponse>  createAwb(@RequestBody AwbRequest awbRequestObj) {
		_LOGGER.info("The create Awb method is called to save the infromation");
		
		_LOGGER.info(" The input Request is "+awbRequestObj.getTrackingId()+"source is"+
					   awbRequestObj.getSource()+"courier name is"+awbRequestObj.getCourier());
		
		AwbResponseStatus awbResponseStatusObj=	service.addAwb(awbRequestObj);
		
		_LOGGER.info("End of the Request is "+awbRequestObj.getTrackingId()+"source is"+
				   awbRequestObj.getSource()+"courier name is"+awbRequestObj.getCourier());
		_LOGGER.info("End of the functinal  call ");
		if (awbResponseStatusObj.getResult() == true) {
			return new  ResponseEntity<AwbResponse>(new AwbResponse("S","Success"), HttpStatus.OK) ;
		} else {
			return new  ResponseEntity<AwbResponse>(new AwbResponse("F",awbResponseStatusObj.getStatusMessage()), HttpStatus.NOT_FOUND);

		}
		
	}

}