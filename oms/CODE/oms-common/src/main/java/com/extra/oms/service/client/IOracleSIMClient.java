package com.extra.oms.service.client;

import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.CreateFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.CreateFulfillmentOrderDeliveryResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.DispatchFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.DispatchFulfillmentOrderDeliveryResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.LookupFulfillmentOrderDeliveryHeaders;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.LookupFulfillmentOrderDeliveryHeadersResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.UpdateFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.UpdateFulfillmentOrderDeliveryResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ConfirmReversePick;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ConfirmReversePickResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.CreateReversePick;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.CreateReversePickResponse;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustmentResponse;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.ProcessPOSTransactions;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.ProcessPOSTransactionsResponse;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetailResponse;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetailResponse;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.LookupFulfillmentOrderHeaders;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.LookupFulfillmentOrderHeadersResponse;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.Ping;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.PingResponse;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ReadFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ReadFulfillmentOrderDetailResponse;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransferResponse;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.LookupTransferHeader;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.LookupTransferHeaderResponse;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReadTransferDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReadTransferDetailResponse;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequest;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequestResponse;

import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface IOracleSIMClient {

	@RequestLine("POST /StoreFulfillmentOrderBean/StoreFulfillmentOrderService")
	CreateFulfillmentOrderDetailResponse createFulfillmentOrderDetail(CreateFulfillmentOrderDetail fulfilOrdColDesc);

	@RequestLine("POST /InventoryAdjustmentBean/InventoryAdjustmentService")
	SaveAndConfirmInventoryAdjustmentResponse saveAndConfirmInventoryAdjustment(SaveAndConfirmInventoryAdjustment strAdjModVo);

	@RequestLine("POST /StoreToStoreTransferBean/StoreToStoreTransferService")
	SavePendingTransferRequestResponse savePendingTransferRequest(SavePendingTransferRequest stsTsfApvModVo);

	@RequestLine("POST /StoreToStoreTransferBean/StoreToStoreTransferService")
	ApproveTransferResponse approveTransfer(ApproveTransfer stsTsfRef);

	@RequestLine("POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService")
	DispatchFulfillmentOrderDeliveryResponse dispatchFulfillmentOrderDelivery(DispatchFulfillmentOrderDelivery dispatchFulfillmentOrderDelivery);

	@RequestLine("POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService")
	CreateFulfillmentOrderDeliveryResponse createFulfillmentOrderDelivery(CreateFulfillmentOrderDelivery fulfillmentOrderDelivery);

	@RequestLine("POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService")
	UpdateFulfillmentOrderDeliveryResponse updateFulfillmentOrderDelivery(UpdateFulfillmentOrderDelivery orderDelivery);

	@RequestLine("POST /FulfillmentOrderReversePickBean/FulfillmentOrderReversePickService")
	ConfirmReversePickResponse confirmReversePick(ConfirmReversePick confirmReversePick);

	@RequestLine("POST /StoreFulfillmentOrderBean/StoreFulfillmentOrderService")
	LookupFulfillmentOrderHeadersResponse lookupFulfillmentOrderHeaders(LookupFulfillmentOrderHeaders fulfillmentOrderHeaders);

	@RequestLine("POST /StoreFulfillmentOrderBean/StoreFulfillmentOrderService")
	ReadFulfillmentOrderDetailResponse readFulfillmentOrderDetail(ReadFulfillmentOrderDetail readFulfillmentOrderDetail);

	@RequestLine("POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService")
	LookupFulfillmentOrderDeliveryHeadersResponse lookupFulfillmentOrderDeliveryHeaders(LookupFulfillmentOrderDeliveryHeaders lookupFulfillmentOrderDeliveryHeaders);

	@RequestLine("POST /StoreToStoreTransferBean/StoreToStoreTransferService")
	LookupTransferHeaderResponse lookupTransferHeader(LookupTransferHeader lookupTransferHeader);

	@RequestLine("POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService")
	ReadTransferDetailResponse readTransferDetail(ReadTransferDetail readTransferDetail);

	@RequestLine("POST /StoreFulfillmentOrderBean/StoreFulfillmentOrderService")
	CancelFulfillmentOrderDetailResponse cancelFulfillmentOrderDetail(CancelFulfillmentOrderDetail cancelFulfillmentOrderDetail);

	@RequestLine("POST /FulfillmentOrderReversePickBean/FulfillmentOrderReversePickService")
	CreateReversePickResponse createReversePick(CreateReversePick createReversePick);

	@RequestLine("POST /StoreFulfillmentOrderBean/StoreFulfillmentOrderService")
	PingResponse ping(Ping ping);

	@RequestLine("POST /POSTransactionBean/POSTransactionService")
	ProcessPOSTransactionsResponse processPOSTransactions(ProcessPOSTransactions paramProcessPOSTransactions);

}
