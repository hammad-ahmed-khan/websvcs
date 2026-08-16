/**
 * 
 */
package com.extra.oms.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.oms.model.POSOrderCancelResponse;
import com.extra.oms.service.OrderCancellationService;

/**
 * @author aibrahim
 *
 */
@RestController
public class OrderCancelController extends BaseController {

	private static final Logger LOG = Logger.getLogger(OrderCancelController.class);

	@Autowired
	private OrderCancellationService cancellationService;

	@PostMapping(path = "/omscancellation")
	public POSOrderCancelResponse cancelPOSOrder(@RequestBody POSOrderCancelRequest cancelRequest) throws BaseException {
		LOG.info("Cancellation requet received for the customer order number " + cancelRequest.getCustOrderNo());
		try {
			return cancellationService.cancelOrder(cancelRequest);
		} catch (BaseException e) {
			LOG.error("Error while processing the cancellation request -> " + cancelRequest.getCustOrderNo(), e);
			POSOrderCancelResponse response = new POSOrderCancelResponse();
			response.setCancellationId(cancelRequest.getCancellationId());
			response.setMessageStatus("E");
			response.setMessageCode(e.getCode());
			response.setResponseDatetimestamp(new Date());
			return response;
		}
	}
}
