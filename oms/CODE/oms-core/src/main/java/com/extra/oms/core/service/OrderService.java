/**
 * 
 */
package com.extra.oms.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.bean.Address;
import com.extra.oms.bean.AddressInfo;
import com.extra.oms.bean.AddressResponse;
import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.BookingDetail;
import com.extra.oms.core.bean.CustomerAddressInfo;
import com.extra.oms.core.bean.ItemCancellation;
import com.extra.oms.core.bean.ItemReturnDetail;
import com.extra.oms.core.bean.OrderInfo;
import com.extra.oms.core.dao.OrderDAO;
import com.extra.oms.service.client.IMuleAddressClient;

/**
 * @author aibrahim
 *
 */
@Service
public class OrderService {

	private static final Logger _LOG = Logger.getLogger(OrderService.class);

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private IMuleAddressClient muleAddressClient;

	public List<OrderInfo> searchCustomerOrders(Map<String, Object> params) throws BaseException {
		return orderDAO.searchCustomerOrders(params);
	}

	public OrderInfo getOrderDetail(Long omsCustomerNo) throws BaseException {
		return orderDAO.getOrderDetail(omsCustomerNo);
	}

	public CustomerAddressInfo getAddressDetail(Long omsCustomerNo) {
		return orderDAO.getAddressDetail(omsCustomerNo);
	}

	public List<ItemCancellation> getCancellationDetail(String customerNo) {
		return orderDAO.getCancellationDetail(customerNo);
	}

	public List<ItemReturnDetail> getReturnDetail(String customerNo) {
		return orderDAO.getReturnDetail(customerNo);
	}

	public List<BookingDetail> getBookingDetail(String customerNo) {
		return orderDAO.getBookingDetail(customerNo);
	}

	public String validateOrderNumber(String type, String orderNo) {
		return orderDAO.validateOrderNumber(type, orderNo);
	}

	public String updateAddress(AddressInfo addressInfo) {
		AddressResponse response = null;
		String message = "In Valid Address";
		if (addressInfo.getShortaddress() != null && !addressInfo.getShortaddress().isEmpty()) {
			response = muleAddressClient.getAddressByShortAddress(addressInfo);
		} else {
			response = muleAddressClient.getAddressByGeocode(addressInfo);
		}
		if (response != null && response.getResponseHeader().getStatus().equals("S")) {
			Address address = response.getNationalAddress();
			if (address.getLatitude() == null) {
				address.setLatitude(new BigDecimal(addressInfo.getLat()));
				address.setLongitude(new BigDecimal(addressInfo.getLongitude()));
			}
			_LOG.info("Address validated successfully for order " + addressInfo.getOrderNo());
			String customerCity = orderDAO.getCustomerCity(addressInfo.getOrderNo());
			if ("invoice".equalsIgnoreCase(addressInfo.getType()) || (address.getCity() != null && customerCity.trim().toUpperCase().equals(address.getCity().trim().toUpperCase()))) {				
				orderDAO.updateCustomerAddress(addressInfo, address);
				message = "OK";
			} else {
				message = "The selected city does not match the customer's delivery city.";
			}
		} else if (response != null) {
			message = response.getResponseHeader().getResMSG();
			_LOG.info("Address validated failed for order " + addressInfo.getOrderNo() + " message received " + message);
		}
		return message;
	}
}
