package com.extra.sim.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.sim.model.EnteredSerialNumbers;
import com.extra.sim.model.ExtraFulfillmentOrderLineItem;
import com.extra.sim.model.ExtraIMEIFulfillmentOrderDelivery;
import com.extra.sim.model.IMEICancelDelivery;
import com.extra.sim.model.LookupUIN;
import com.extra.sim.model.SuccessResponse;
import com.extra.sim.model.UniqueSerialNumber;
import com.extra.sim.service.UniqueSerialNumberService;

/**
 * @author Madhuchandra
 */
@RestController
public class UniqueSerialNumberController {

	@Autowired
	private UniqueSerialNumberService service;

	private final static Logger log = Logger.getLogger(UniqueSerialNumberController.class.getName());
	
	@PostMapping(value ="/ping", consumes = "text/html", produces = "application/json")
	public ResponseEntity<SuccessResponse> ping(@RequestParam String name) throws Exception {
		
		SuccessResponse response=new SuccessResponse();
		response.setCode(200);
		response.setSuccess(true);
		response.setMessage("Successfully Pinged the webservice with the message :"+name);
		service.ping();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
	}

	@PostMapping(value ="/sim/imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> saveIMEI(@RequestBody List<UniqueSerialNumber> request,
			HttpServletRequest http) throws Exception {
		log.info("Inserting the IMEI Details..");
		SuccessResponse saveIMEI = service.saveIMEI(request);
		return new ResponseEntity<SuccessResponse>(saveIMEI, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/cancel-imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> cancelIMEI(@RequestBody List<UniqueSerialNumber> request,
			HttpServletRequest http) throws Exception {
		log.info("Cancelling the IMEI");
		SuccessResponse saveIMEI1 = service.cancelIMEI(request);
		return new ResponseEntity<SuccessResponse>(saveIMEI1, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/lookup-imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> lookupIMEI(@RequestBody ExtraFulfillmentOrderLineItem request) throws Exception {	
		log.info("Look up if IMEI entered or not..");
		SuccessResponse imeiLookup = service.lookupIMEI(request);
		return new ResponseEntity<SuccessResponse>(imeiLookup, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/update-imei-indicator", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> updateIndicatorIMEI(@RequestBody ExtraIMEIFulfillmentOrderDelivery request) throws Exception {	
		log.info("Updating IMEI indicator..");
		SuccessResponse imeiLookup = service.updateIndicatorIMEI(request);
		return new ResponseEntity<SuccessResponse>(imeiLookup, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/lookup-uin-enabled", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> lookupUINenabled(@RequestBody LookupUIN request) throws Exception {	
		log.info("Look up if UIN is enabled or not..");
		SuccessResponse imeiLookup = service.lookupUINenabled(request);
		return new ResponseEntity<SuccessResponse>(imeiLookup, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/lookup-imei-qty", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> lookupIMEIQty(@RequestBody UniqueSerialNumber request) throws Exception {	
		log.info("Look up IMEI Quantity..");
		SuccessResponse imeiQty = service.lookupIMEIQty(request);
		return new ResponseEntity<SuccessResponse>(imeiQty, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/delete-imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> cancelDelivery(@RequestBody IMEICancelDelivery request) throws Exception {	
		log.info("delete IMEI from FulfillmentOrderDeliveryList Screen..");
		SuccessResponse imeiQty = service.cancelDelivery(request);
		return new ResponseEntity<SuccessResponse>(imeiQty, HttpStatus.OK);
	}
	
	@PostMapping(value ="/sim/delete-qty-reverted-imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> deleteQuantityReverted(@RequestBody ExtraFulfillmentOrderLineItem request) throws Exception {	
		log.info("delete IMEI if Quantity Entered reverted to zero..");
		SuccessResponse imeiQty = service.deleteQuantityReverted(request);
		return new ResponseEntity<SuccessResponse>(imeiQty, HttpStatus.OK);
	}	
	@PostMapping(value ="/sim/retreive-imei", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<EnteredSerialNumbers> retrieveIMEIs(@RequestBody UniqueSerialNumber request) throws Exception {	
		log.info("Look up IMEI Quantity..");
		EnteredSerialNumbers imeiQty = service.retrieveIMEI(request);
		return new ResponseEntity<EnteredSerialNumbers>(imeiQty, HttpStatus.OK);
	}
	@PostMapping(value ="/sim/check-bol", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SuccessResponse> checkIfBOLexists(@RequestBody List<UniqueSerialNumber> request,
			HttpServletRequest http) throws Exception {
		log.info("Checking if BOL is assigned");
		SuccessResponse saveIMEI1 = service.checkIfBOLexists(request);
		return new ResponseEntity<SuccessResponse>(saveIMEI1, HttpStatus.OK);
	}
	

}
