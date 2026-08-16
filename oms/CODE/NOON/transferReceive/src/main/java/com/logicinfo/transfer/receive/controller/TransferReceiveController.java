package com.logicinfo.transfer.receive.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.logicinfo.transfer.receive.model.Items;
import com.logicinfo.transfer.receive.model.Response;
import com.logicinfo.transfer.receive.model.TsfReceiveRequest;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;
import com.logicinfo.transfer.receive.service.TransferReceiveService;

@RestController
public class TransferReceiveController {
	@Autowired
	TransferReceiveService service;

	private final static Logger log = Logger.getLogger(TransferReceiveController.class.getName());

	@PostMapping(value = "/receive/cust", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<TsfReceiveResponse> saveDOA(@RequestBody TsfReceiveRequest request) throws Exception {
		try {
			String receiptNumber = "";
			int destId = 0;
			final String poNumber = request.getPoNumber();
			receiptNumber = request.getReceiptNumber();
			destId = request.getDestId();
			log.info("Inside TransferReceiveService Po Numb= " + poNumber + ":ReceiptNumber:" + receiptNumber + ":destId:" + destId);
			if (poNumber.isEmpty() || poNumber.equalsIgnoreCase("") || poNumber == null || receiptNumber.isEmpty() || receiptNumber.equalsIgnoreCase("") || receiptNumber == null || destId == 0) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("False");
				response.setMessage("PoNumber or ReceiptNumber is empty");
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}

			// check whether the transfer No exist or not
			String PoExist = service.checktransferNoExist(poNumber);
			log.info("Check whether the Transfer exist or not");
			if (PoExist.equalsIgnoreCase("Transfer not exist")) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setTsf_No(poNumber);
				response.setSuccess("false");
				response.setError("PoNumber or ReceiptNumber is doesnot exist");
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}

			log.info("check whether requested transfer contains invalid items");
			ArrayList<Items> invalidItems = service.checkInvalidItems(request);
			log.info("invalidItems.size() " + invalidItems.size());
			if (invalidItems != null && invalidItems.size() > 0) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("false");
				response.setTsf_No(poNumber);
				response.setError("invalid_items");
				response.setItems(invalidItems);
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}
			log.info("check whether requested receive contains valid quantity");
			ArrayList<Items> invalidQty = service.checkInvalidQty(request);
			log.info("invalidQty.size() " + invalidQty.size());
			if (invalidQty != null && invalidQty.size() > 0) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("false");
				response.setTsf_No(poNumber);
				response.setError("NOT_SHIPPED");
				response.setItems(invalidQty);
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}
			log.info("check whether requested quantity already received");
			ArrayList<Items> validateRecivedQty = service.checkReceivedQty(request);
			log.info("invalidQty.size() " + validateRecivedQty.size());
			if (validateRecivedQty != null && validateRecivedQty.size() > 0) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("false");
				response.setTsf_No(poNumber);
				response.setError("expected_qty_received_already");
				response.setItems(validateRecivedQty);
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}

			try {
				log.info("creating lock for the po -> " + request.getPoNumber());
				service.lockReceiveRequest(request);
			} catch (Exception e) {
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("false");
				response.setTsf_No(poNumber);
				response.setError("Receipt number '" + poNumber + "' is already is in progress. Please try after some time");
				response.setItems(validateRecivedQty);
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}

			log.info("Inside else:::");
			TsfReceiveRequest wrp = new TsfReceiveRequest();
			wrp.setPoNumber(poNumber);
			wrp.setReceiptNumber(receiptNumber);
			wrp.setDestId(destId);
			wrp.setItems(request.getItems());
			Response resp = service.tsfReceive(wrp);

			if (resp.getRes().equals("success")) {
				log.info("Inside Success");
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(200);
				response.setSuccess("Success");
				response.setMessage("");
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			} else {
				log.info("Inside PO/ITEM/QTY Value failure");
				TsfReceiveResponse response = new TsfReceiveResponse();
				response.setCode(500);
				response.setSuccess("false");
				response.setTsf_No(poNumber);
				response.setError(resp.getRes());
				if (resp.getItem() != null) {
					List<Items> items = new ArrayList<Items>(1);
					items.add(resp.getItem());
					response.setItems(items);
				}
				service.saveRequestandResponse(request, response);
				return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			TsfReceiveResponse response = new TsfReceiveResponse();
			response.setCode(500);
			response.setSuccess("false");
			response.setError(e.getMessage());
			log.info("Exception in transferReceive controller " + e.getMessage());
			service.saveRequestandResponse(request, response);
			return new ResponseEntity<TsfReceiveResponse>(response, HttpStatus.OK);
		} finally {
			service.removeReceiveRequestLock(request);
		}
	}

}
