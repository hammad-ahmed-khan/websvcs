package com.extra.oms.service.client;

import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustmentResponse;

import feign.RequestLine;

public interface IOracleInvAdjClient {

	@RequestLine("POST /InventoryAdjustmentBean/InventoryAdjustmentService")
	SaveAndConfirmInventoryAdjustmentResponse saveAndConfirmInventoryAdjustment(
			SaveAndConfirmInventoryAdjustment strAdjModVo);
}
