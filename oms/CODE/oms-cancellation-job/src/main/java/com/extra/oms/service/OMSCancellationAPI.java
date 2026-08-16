package com.extra.oms.service;

import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.oms.model.POSOrderCancelResponse;

import feign.Headers;
import feign.RequestLine;

@Headers({ "Content-Type: application/json" })
public interface OMSCancellationAPI {

	@RequestLine("POST /omscancellation")
	public POSOrderCancelResponse cancelOMSOrder(POSOrderCancelRequest cancellationRequest);

}
