package org.logicinfo.label.creation.controller;

import org.logicinfo.label.creation.serviceImpl.LableCreationServImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class LabelCreationController {
	@Autowired
	private LableCreationServImpl lableservice;

	@GetMapping("/ping")
	public ResponseEntity<String> getServiceResponse() {
		String res = "Lable Pakage creation service is up and running";

		return new ResponseEntity<String>(res, HttpStatus.OK);

	}

	@PostMapping(value = "/createLablePackage")
	public ResponseEntity<String> getLabelCreationResponse(@RequestBody String xml) {
		String res = "Error While retriving data";
		try {
			res = lableservice.getlablePackageResponse(xml);
			return new ResponseEntity<String>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(res, HttpStatus.OK);
		}

	}

	// , headers = "Accept=application/json", consumes = "application/json",
	// produces = "application/json"

}
