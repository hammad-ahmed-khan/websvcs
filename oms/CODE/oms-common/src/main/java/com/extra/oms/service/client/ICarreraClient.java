package com.extra.oms.service.client;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.bds.bean.carrera.TransferCreationResponse;

import feign.Headers;
import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface ICarreraClient {

	@RequestLine("POST /CarreraTransferCreation/NewTransferCreation")
	@Headers({ "Content-Type: application/json", "Accept: application/json" })
	TransferCreationResponse getOrderItemsResponse(TransferCreationRequest paramTransferCreationRequest);
}
