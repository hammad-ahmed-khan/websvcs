package com.extra.oms.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.common.exception.BaseException;
import com.extra.oms.service.client.IOracleSIMClient;
import com.extra.oms.sim.bean.Request;
import com.extra.oms.sim.dao.SIMDispatchDAO;
import com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.CreateFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.CreateFulfillmentOrderDeliveryResponse;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.DispatchFulfillmentOrderDelivery;

/**
 * @author aibrahim
 *
 */
@Service
public class DispatcherService {

	@Autowired
	private SIMDispatchDAO simDispatchDAO ;

	@Autowired
	private IOracleSIMClient oracleSIMClient;

	public void dispatchOrder(Request request) throws BaseException {
		FodCreModVo modVo = simDispatchDAO.getFulfilmentDetails(request);
		if (modVo == null) {
			throw new BaseException("Order No: " + request.getOrderNo() + " with external no " + request.getFulfilNo() + " is not exist");
		}
		CreateFulfillmentOrderDelivery orderDelivery = new CreateFulfillmentOrderDelivery();
		orderDelivery.setFodCreModVo(modVo);
		CreateFulfillmentOrderDeliveryResponse response = oracleSIMClient.createFulfillmentOrderDelivery(orderDelivery);
		Long deliveryId = response.getFodRef().getDeliveryId();

		DispatchFulfillmentOrderDelivery fulfillmentOrderDelivery = new DispatchFulfillmentOrderDelivery();
		FodRef fodRef = new FodRef();
		fulfillmentOrderDelivery.setFodRef(fodRef);
		fodRef.setDeliveryId(deliveryId);
		oracleSIMClient.dispatchFulfillmentOrderDelivery(fulfillmentOrderDelivery);
	}
}
