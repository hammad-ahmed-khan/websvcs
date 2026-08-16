/**
 * 
 */
package com.extra.oms.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.bean.AddressInfo;
import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.BookingDetail;
import com.extra.oms.core.bean.CustomerAddressInfo;
import com.extra.oms.core.bean.ItemCancellation;
import com.extra.oms.core.bean.ItemReturnDetail;
import com.extra.oms.core.bean.OrderInfo;
import com.extra.oms.core.service.OrderService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/order")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<OrderInfo> searchCustomerOrders(@RequestParam Map<String, Object> params) throws BaseException {
		return orderService.searchCustomerOrders(params);
	}

	@GetMapping(path = "/validate/{type}/{orderNo}")
	public String validateOrderNumber(@PathVariable("type") String type, @PathVariable("orderNo") String orderNo) throws BaseException {
		return orderService.validateOrderNumber(type, orderNo);
	}

	@GetMapping(path = "/detail/{omsCustomerNo}")
	public OrderInfo getOrderDetail(@PathVariable("omsCustomerNo") Long omsCustomerNo) throws BaseException {
		return orderService.getOrderDetail(omsCustomerNo);
	}

	@GetMapping(path = "/address/{omsCustomerNo}")
	public CustomerAddressInfo getAddressDetail(@PathVariable("omsCustomerNo") Long omsCustomerNo) throws BaseException {
		return orderService.getAddressDetail(omsCustomerNo);
	}

	@PostMapping(path = "/address")
	public String updateAddress(@RequestBody AddressInfo addressInfo) throws BaseException {
		return orderService.updateAddress(addressInfo);
	}

	@GetMapping(path = "/cancel/{customerNo}")
	public List<ItemCancellation> getCancellationDetail(@PathVariable("customerNo") String customerNo) throws BaseException {
		return orderService.getCancellationDetail(customerNo);
	}

	
	@GetMapping(path = "/return/{customerNo}")
	public List<ItemReturnDetail> getReturnDetail(@PathVariable("customerNo") String customerNo) throws BaseException {
		return orderService.getReturnDetail(customerNo);
	}

	@GetMapping(path = "/booking/{customerNo}")
	public List<BookingDetail> getBookingDetail(@PathVariable("customerNo") String customerNo) throws BaseException {
		return orderService.getBookingDetail(customerNo);
	}
}
