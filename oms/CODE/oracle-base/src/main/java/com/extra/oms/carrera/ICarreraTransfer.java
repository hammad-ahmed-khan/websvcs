package com.extra.oms.carrera;

import com.extra.oms.carrera.model.TransferCreationRequest;
import com.extra.oms.carrera.model.TransferCreationResponse;
import feign.Headers;
import feign.RequestLine;

public interface ICarreraTransfer {

	@RequestLine("POST /CarreraTransferCreation/NewTransferCreation")
	@Headers({ "Content-Type: application/json", "Accept: application/json" })
	TransferCreationResponse getOrderItemsResponse(TransferCreationRequest paramTransferCreationRequest);
}
