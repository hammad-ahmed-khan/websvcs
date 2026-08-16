package com.extra.restservice.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.restservice.bean.Customer;
import com.extra.restservice.bean.Status;
import com.extra.restservice.dao.ExtraReturnStoreDao;
import com.extra.restservice.service.ExtraReturnStoreService;

@RestController
//@RequestMapping("/order")
public class ExtraRestService {

	@Autowired
	ExtraReturnStoreDao extraRetrunStoreDao;

	@Autowired
	ExtraReturnStoreService extraReturnStoreService;
	Status status = new Status();

	@GetMapping("/info")
	public ResponseEntity<String> healthCheck() {
		return new  ResponseEntity<String>("Noon Return up and running",HttpStatus.OK);
	}

	@PostMapping("/customer")
	public ResponseEntity<Status> customerInput(@RequestBody Customer customer) {
		try {

			System.out.println("Inside controller: " + customer);
			extraRetrunStoreDao.insertRequestData(customer);
	
			String s = extraRetrunStoreDao.checkCustomerData(customer);
			if (s.equalsIgnoreCase("success") && s.contains("success")) {
				System.out.println("Inside controller - If");
				String RMAID = extraRetrunStoreDao.insertCustomerData(customer);
				extraRetrunStoreDao.insertItemData(customer, RMAID);
				extraRetrunStoreDao.insertOMSDetails(customer, RMAID);
				String res = callingPostTransactionwebService(customer);
				status.setCode(200);
				status.setSuccess(res);
				status.setMessage(null);
			} else {
				System.out.println("Inside False status");
				status.setCode(500);
				status.setSuccess("False");
				status.setMessage(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Status>(status, HttpStatus.OK);
	}

	// Charan
	// @PostMapping(value="/callwebservice")
	public String callingPostTransactionwebService(Customer customer) {
		System.out.println("Before callingPostTransactionwebService");
		extraReturnStoreService.callWebservice(customer);
		System.out.println("After callingPostTransactionwebService");
		return "True";
	}
}
