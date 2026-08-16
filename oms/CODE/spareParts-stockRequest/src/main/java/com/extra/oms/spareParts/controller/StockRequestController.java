package com.extra.oms.spareParts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.spareParts.model.CancelRequest;
import com.extra.oms.spareParts.model.CancelResponse;
import com.extra.oms.spareParts.model.StockDeductionRequest;
import com.extra.oms.spareParts.model.StockDeductionResponse;
import com.extra.oms.spareParts.model.StockRequest;
import com.extra.oms.spareParts.model.StockResponse;
import com.extra.oms.spareParts.model.TransferRecieveRequest;
import com.extra.oms.spareParts.model.TransferRecieveResponse;
import com.extra.oms.spareParts.service.SparePartsService;

@RestController
public class StockRequestController {

	@Autowired
	private SparePartsService sparePartsService;

//	1.Stock request or return request based on type.
	@PostMapping(path = "/stock-return-request")
	public StockResponse stockRequest(@RequestBody StockRequest request) throws Exception {
		return sparePartsService.processRequest(request);
	}

//	2.Receiving stock.
	@PostMapping(path = "/stock-receiving")
	public TransferRecieveResponse transferReceiving(@RequestBody TransferRecieveRequest request) throws Exception {
		return sparePartsService.processReceiving(request);
	}

//	3.Stock deducting.
	@PostMapping(path = "/stock-deducting")
	public StockDeductionResponse stockDeducting(@RequestBody StockDeductionRequest request) throws Exception {
		return sparePartsService.stockDeducting(request);
	}

//	4.Stock Cancellation.
	@PostMapping(path = "/stock-cancellation")
	public CancelResponse stockCancellation(@RequestBody CancelRequest request) throws Exception {
		return sparePartsService.stockCancellation(request);
	}
}
