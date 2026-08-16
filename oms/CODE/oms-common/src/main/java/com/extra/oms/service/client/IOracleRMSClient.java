package com.extra.oms.service.client;

import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRefResponse;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDescResponse;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDesc;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDescResponse;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.Ping;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.PingResponse;

import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface IOracleRMSClient {

	@RequestLine("POST /FulfillOrderBean/FulfillOrderService")
	CreateFulfilOrdColDescResponse createFulfilOrdColDesc(CreateFulfilOrdColDesc fulfilOrdColDesc);

	@RequestLine("POST /InventoryBackOrderBean/InventoryBackOrderService")
	CreateInvBackOrdColDescResponse createInvBackOrdColDesc(CreateInvBackOrdColDesc invBackOrdColDesc);

	@RequestLine("POST /FulfillOrderBean/FulfillOrderService")
	CancelFulfilOrdColRefResponse cancelFulfilOrdColRef(CancelFulfilOrdColRef fulfilOrdColRef);

	@RequestLine("POST /FulfillOrderBean/FulfillOrderService")
	PingResponse ping(Ping ping);
}
