/**
 * 
 */
package com.extra.bds.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.bds.bean.Booking;
import com.extra.bds.bean.Delivery;
import com.extra.bds.bean.Item;
import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;
import com.extra.bds.bean.Status;
import com.extra.bds.dao.BookingDAO;
import com.extra.bds.util.StatusException;

/**
 * @author aibrahim
 *
 */
@Service
public class BookingService {

	private static final Logger _LOG = Logger.getLogger(BookingService.class);

	@Autowired
	private BookingDAO bookingDAO;

	public List<Response> getItemsAvailablity(Request request) throws Exception {

		Status status = validateAvailablityRequest(request);
		if (status != null) {
			throw new StatusException(status);
		}
		_LOG.info("Slot available request received for order number " + request.getOrderNo() + ". Saving the request to database.");
		Long sequenceId = bookingDAO.saveRequest(request);

		boolean onlyDeliveryCharge = false;
		if ("POS_CO".equals(request.getOrderType())) {
			groupItems(request);
			onlyDeliveryCharge = bookingDAO.updateRequestLocation(request);
		}

		List<Response> responses = null;
		if (onlyDeliveryCharge) {
			Response resp = new Response();
			status = new Status();
			status.setSuccess(true);
			resp.setStatus(status);
			responses = Collections.singletonList(resp);
		} else {
			responses = getAvailableSlots(request);
		}

		if ("Y".equals(request.getDeliveryChargeInd())) {
			bookingDAO.getDeliveryCharges(request, responses.get(0));
		}
		_LOG.info("Slot available request processed for order number " + request.getOrderNo() + ". Saving the response to database.");
		bookingDAO.updateResponse(request.getOrderNo(), responses, sequenceId);
		return responses;
	}

	private void groupItems(Request request) {
		Map<String, Item> itemMap = new HashMap<String, Item>();
		for (Delivery delivery : request.getDeliveries()) {
			if (delivery.getItems() != null) {
				Iterator<Item> itemIterator = delivery.getItems().iterator();
				while(itemIterator.hasNext()) {
					Item newItem = itemIterator.next();
					Item item = itemMap.get(newItem.getItemCode());
					if (item == null) {
						itemMap.put(newItem.getItemCode(), newItem);
					} else {
						item.setOrderQty(newItem.getOrderQty() + item.getOrderQty());
						itemIterator.remove();
					}
				}
			}
			itemMap.clear();
			if (delivery.getBookings() != null) {
				for (Booking booking : delivery.getBookings()) {
					if (booking.getItems() != null) {
						Iterator<Item> itemIterator = booking.getItems().iterator();
						while(itemIterator.hasNext()) {
							Item newItem = itemIterator.next();
							Item item = itemMap.get(newItem.getItemCode());
							if (item == null) {
								itemMap.put(newItem.getItemCode(), newItem);
							} else {
								item.setOrderQty(newItem.getOrderQty() + item.getOrderQty());
								itemIterator.remove();
							}
						}
					}
				}
			}
		}
	}

	public List<Response> getAvailablityByOrder(Request request) throws Exception {
		
		Status status = validateOrderRequest(request);
		if (status != null) {
			throw new StatusException(status);
		}
		_LOG.info("Availablity check request received for order number " + request.getOrderNo() + ". Saving the request to database.");
		Long sequenceId = bookingDAO.saveRequest(request);
		bookingDAO.getOrderDetail(request);
		if ("SMALL".equals(request.getDeliveries().get(0).getItems().get(0).getShippingClassification())) {
			throw new StatusException("E-505", "Shipclassifcation is SMALL");
		}
		List<Response> responses = getAvailableSlots(request);
		_LOG.info("Availablity check request processed for order number " + request.getOrderNo() + ". Saving the response to database.");
		bookingDAO.updateResponse(request.getOrderNo(), responses, sequenceId);
		return responses;
	}

	private List<Response> getAvailableSlots(Request request) throws Exception {
		Response resp = new Response();
		try {

			Set<String> items = new HashSet<String>();
			for (Delivery delivery : request.getDeliveries()) {
				for (Item item : delivery.getItems()) {
					items.add(item.getItemCode());
				}
			}
			if (bookingDAO.hasACInstallMismatch(items)) {
				throw new StatusException("E-705", "Items in the cart are not matching");
			}
			Map<String, Character> insallFlagMap = bookingDAO.getItemInstallationFlag(items, Arrays.asList("2005"));
			Map<Integer, String> itemQueryMap = getItemQuery(request.getDeliveries(), insallFlagMap);
			String expressItemSKU = bookingDAO.getExpressItemSKU();
			validateCityArea(request.getDeliveries(), expressItemSKU);
			resp.setDeliveries(bookingDAO.getAvailablity(request.getDeliveries(), itemQueryMap));
			_LOG.info("Processed the avilablity request for the order number " + request.getOrderNo());
			Status status = new Status();
			status.setSuccess(true);
			resp.setStatus(status);
		} catch (Exception e) {
			_LOG.error("Error while checking the availablity for order number " + request.getOrderNo(), e);
			throw e;
		}
		return Collections.singletonList(resp);
	}

	public List<Response> slotBooking(Request request) throws Exception {
		Response resp = new Response();
		Status status = validateBookingRequest(request);
		if (status != null) {
			throw new StatusException(status);
		}
		_LOG.info("Booking request received for order number " + request.getOrderNo() + ". Saving the request to database.");
		Long sequenceId = bookingDAO.saveRequest(request);
		if ("POS_CO".equals(request.getOrderType())) {
			groupItems(request);
		}
		try {
			Set<String> items = new HashSet<String>();
			for (Delivery delivery : request.getDeliveries()) {
				for (Booking booking : delivery.getBookings()) {
					for (Item item : booking.getItems()) {
						items.add(item.getItemCode());
					}
				}
			}
			if (bookingDAO.hasACInstallMismatch(items)) {
				throw new StatusException("E-705", "Items in the cart are not matching");
			}
			Map<String, Character> itemInstallMap = bookingDAO.getItemInstallationFlag(items, Arrays.asList("402", "411"));
			if ("POS_CO".equals(request.getOrderType())) {
				resp.setDeliveries(bookingDAO.booking(request, itemInstallMap));
			} else {
				resp.setDeliveries(bookingDAO.reservation(request, itemInstallMap));
			}
			_LOG.info("Processed the reservation request for the order number " + request.getOrderNo());
			for (Delivery delivery : resp.getDeliveries()) {
				if (delivery.getStatus() != null) {
					status = delivery.getStatus();
					break;
				}
			}
			if (status == null) {
				status = new Status();
				status.setSuccess(true);
			}
			resp.setStatus(status);
		} catch (Exception e) {
			_LOG.error("Error while booking the slot for the order number " + request.getOrderNo(), e);
			throw e;
		}
		_LOG.info("Slot reserved for order number " + request.getOrderNo() + ". Saving the response to database.");
		bookingDAO.updateResponse(request.getOrderNo(), Collections.singletonList(resp), sequenceId);
		return Collections.singletonList(resp);
	}

	public List<Response> confirmBooking(Request request) throws Exception {
		Response resp = new Response();
		Status status = validateConfirmRequest(request);
		if (status != null) {
			throw new StatusException(status);
		}
		_LOG.info("Booking confirm request received for order number " + request.getOrderNo() + ". Saving the request to database.");
		Long sequenceId = bookingDAO.saveRequest(request);
		try {
			List<Delivery> deliveries = null;
			if ("POS_CO".equals(request.getOrderType())) {
				bookingDAO.getOrderDetail(request);
				Set<String> items = new HashSet<String>();
				for (Delivery delivery : request.getDeliveries()) {
					for (Item item : delivery.getItems()) {
						items.add(item.getItemCode());
					}
					for (Booking booking : delivery.getBookings()) {
						booking.setRegionID(1);
						booking.setSlots(1);
					}
				}
				Map<String, Character> itemInstallMap = bookingDAO.getItemInstallationFlag(items, Arrays.asList("402", "411"));
				deliveries = bookingDAO.booking(request, itemInstallMap);
			} else {
				deliveries = bookingDAO.confirmBooking(request);
			}
			resp.setDeliveries(deliveries);
			_LOG.info("Processed the confirmation request for the order number " + request.getOrderNo());
			for (Delivery delivery : deliveries) {
				if (delivery.getStatus() != null) {
					status = delivery.getStatus();
					break;
				}
			}
			if (status == null) {
				status = new Status();
				status.setSuccess(true);
			}
			resp.setStatus(status);
		} catch (Exception e) {
			_LOG.error("Error while confirming the slot for the order number " + request.getOrderNo(), e);
			throw e;
		}
		_LOG.info("Slot confirmed for order number " + request.getOrderNo() + ". Saving the response to database.");
		bookingDAO.updateResponse(request.getOrderNo(), Collections.singletonList(resp), sequenceId);
		return Collections.singletonList(resp);
	}

	public List<Response> cancelBooking(Request request) throws Exception {
		Response resp = new Response();
		Status status = validateCancelRequest(request);
		if (status != null) {
			throw new StatusException(status);
		}
		_LOG.info("Booking cancel request received for order number " + request.getOrderNo() + ". Saving the request to database.");
		Long sequenceId = bookingDAO.saveRequest(request);
		try {
			List<Delivery> deliveries = bookingDAO.cancelBooking(request);
			_LOG.info("Processed the cancellation request for the order number " + request.getOrderNo());
			for (Delivery delivery : deliveries) {
				if (delivery.getStatus() != null) {
					status = delivery.getStatus();
					break;
				}
			}
			if (status == null) {
				status = new Status();
				status.setSuccess(true);
			}
			resp.setStatus(status);
		} catch (Exception e) {
			_LOG.error("Error while canceling the slot for the order number " + request.getOrderNo(), e);
			throw e;
		}
		_LOG.info("Slot cancelled for order number " + request.getOrderNo() + ". Saving the response to database.");
		bookingDAO.updateResponse(request.getOrderNo(), Collections.singletonList(resp), sequenceId);
		return Collections.singletonList(resp);
	}

	public void updateResponse(List<Response> response) {
		bookingDAO.updateResponse(null, response, null);
	}

	private void validateCityArea(List<Delivery> deliveries, String expressItemSKU) throws Exception {
		StringBuilder cityAreaQuery = new StringBuilder();
		Map<String, List<String>> cityAreaMap = new HashMap<String, List<String>>();
		for (Delivery delivery : deliveries) {
			boolean isExpressDlvry = false;
			for (Item item : delivery.getItems()) {
				if (item.getItemCode().equals(expressItemSKU)) {
					isExpressDlvry = true;
					break;
				}
			}
			String city = delivery.getDeliveryAddress().getCity().toUpperCase();
			String area = null;
			List<String> areas = cityAreaMap.get(city);
			if (isExpressDlvry) {
				if (areas != null) {
					areas.clear();
					continue;
				} else {
					cityAreaMap.put(city, Collections.<String>emptyList());
				}
			} else {
				area = delivery.getDeliveryAddress().getArea().toUpperCase();
				if (areas == null) {
					areas = new ArrayList<String>();
					cityAreaMap.put(city, areas);
				} else if (areas.contains(area)) {
					continue;
				}
				areas.add(area);
			}
			if (cityAreaQuery.length() > 0) {
				cityAreaQuery.append(" OR ");
			} else {
				cityAreaQuery.append(" WHERE ");
			}
			cityAreaQuery.append(" (UPPER(CITY_NAME) = '").append(city).append("'");
			if (isExpressDlvry) {
				cityAreaQuery.append(") ");
			} else {
				cityAreaQuery.append(" AND UPPER(AREA_NAME) = '").append(area).append("') ");
			}
			delivery.getDeliveryAddress().getCity();
		}
		cityAreaQuery.insert(0, "SELECT UPPER(CITY_NAME), COUNT(CITY_NAME) FROM XX_DLVRY_REGIONS_V").append(" GROUP BY CITY_NAME ");
		Map<String, Integer> cityCountMap = bookingDAO.getCityAreaCount(cityAreaQuery.toString());
		for (Entry<String, List<String>> cityEntry : cityAreaMap.entrySet()) {
			Integer areaCnt = cityCountMap.get(cityEntry.getKey());
			if (areaCnt == null || (cityEntry.getValue().size() > 0 && cityEntry.getValue().size() != areaCnt)) {
				throw new StatusException("400", "Invalid City/Area");
			}
		}
	}

	private Map<Integer, String> getItemQuery(List<Delivery> deliveries, Map<String, Character> insallFlagMap) {
		Map<Integer, String> itemQueryMap = new HashMap<Integer, String>();
		for (Delivery delivery : deliveries) {
			StringBuilder queryBuilder = new StringBuilder();
			for (Item item : delivery.getItems()) {
				if (queryBuilder.length() > 0) {
					queryBuilder.append(" UNION ALL ");
				}
				queryBuilder.append("SELECT ").append(item.getLineNo()).append(" AS LINE_NO, '").append(item.getItemCode()).append("' AS ITEM, ").append(item.getOrderQty()).append(" AS QTY,")
						.append(item.getSourceLocation()).append(" AS SOURCE_LOC,").append(item.getFulfillmentLocation()).append(" AS FULFILL_LOC, '").append(insallFlagMap.get(item.getItemCode()))
						.append("' AS INSTALL_SRV_FLAG FROM DUAL");
			}
			itemQueryMap.put(delivery.getDeliveryId(), queryBuilder.toString());
		}
		return itemQueryMap;
	}

	private Status validateAvailablityRequest(Request request) {

		Status status = null;

		status = validateRequest(request);
		if (status != null) {
			return status;
		}

		status = new Status();
		if (request.getCreateDate() == null || request.getCreateDate().trim().isEmpty()) {
			status.setCode("E-0003");
			status.setMessage("CreateDate doesn't Exists");
			return status;
		} else {
			for (Delivery delivery : request.getDeliveries()) {

				if (delivery.getItems() == null || delivery.getItems().isEmpty()) {
					status.setCode("E-0009");
					status.setMessage("Item Details doesn't Exists");
					return status;
				}

				for (Item item : delivery.getItems()) {
					if ((item.getItemCode() == null) || item.getItemCode().trim().isEmpty()) {
						status.setCode("E-0007");
						status.setMessage("Item Code doesn't Exists");
						return status;
					} else if (item.getLineNo() < 0) {
						status.setCode("E-0008");
						status.setMessage("Line no doesn't Exists for Item:" + item.getItemCode());
						return status;
					} else if ((!"POS_CO".equals(request.getOrderType())) && (item.getSourceLocation() == null || item.getSourceLocation().trim().isEmpty() || item.getFulfillmentLocation() == null
							|| item.getFulfillmentLocation().trim().isEmpty())) {
						status.setCode("E-615");
						status.setMessage("Source or Fulfillment Location is null");
						return status;
					}
				}
			}
		}
		return null;
	}

	private Status validateBookingRequest(Request request) {

		Status status = null;

		status = validateRequest(request);
		if (status != null) {
			return status;
		}

		status = new Status();

		for (Delivery delivery : request.getDeliveries()) {

			if (delivery.getBookings() == null || delivery.getBookings().isEmpty()) {
				status.setCode("E-0005");
				status.setMessage("Bookings doesn't Exists for this delivery");
				return status;
			} else {
				for (Booking booking : delivery.getBookings()) {
					if (booking.getReservationId() <= 0) {
						status.setCode("E-0001");
						status.setMessage("Reservation Id doesn't Exists");
						return status;
					} else if (booking.getGroupId() <= 0) {
						status.setCode("E-0005");
						status.setMessage("Group Id doesn't Exists");
						return status;
					}
					if (booking.getWindowCode() == null) {
						status.setCode("E-0005");
						status.setMessage("Window Code doesn't Exists");
						return status;
					}
					if (booking.getDate() == null) {
						status.setCode("E-0005");
						status.setMessage("Date doesn't Exists for this booking");
						return status;
					}
					if (booking.getItems() == null || booking.getItems().isEmpty()) {
						status.setCode("E-0005");
						status.setMessage("Item details doesn't Exists");
						return status;
					} else {
						for (Item item : booking.getItems()) {
							if (item.getItemCode() == null || item.getItemCode().isEmpty()) {
								status.setCode("E-0005");
								status.setMessage("Item Code doesn't Exists");
								return status;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private Status validateConfirmRequest(Request request) {

		Status status = new Status();
		if (request.getOrderNo() == null || request.getOrderNo().trim().isEmpty()) {
			status.setCode("E-0001");
			status.setMessage("OrderNo doesn't Exists");
			return status;
		} else if (request.getDeliveries() == null || request.getDeliveries().isEmpty()) {
			status.setCode("E-0002");
			status.setMessage("Delivery Details doesn't Exists");
			return status;
		}

		for (Delivery delivery : request.getDeliveries()) {
			if (delivery.getBookings() == null || delivery.getBookings().isEmpty()) {
				status.setCode("E-0003");
				status.setMessage("Bookings doesn't Exists for this delivery");
				return status;
			} else {
				for (Booking booking : delivery.getBookings()) {
					if (booking.getReservationId() <= 0) {
						status.setCode("E-0004");
						status.setMessage("Reservation Id doesn't Exists");
						return status;
					} else if (booking.getGroupId() <= 0) {
						status.setCode("E-0005");
						status.setMessage("Group Id doesn't Exists");
						return status;
					}
				}
			}
		}
		return null;
	}

	private Status validateCancelRequest(Request request) {
		return validateConfirmRequest(request);
	}

	private Status validateRequest(Request request) {

		Status status = new Status();
		if (request.getOrderNo() == null || request.getOrderNo().trim().isEmpty()) {
			status.setCode("E-0001");
			status.setMessage("OrderNo doesn't Exists");
			return status;
		}

		if (request.getCustomerDetails() == null) {
			status.setCode("E-0004");
			status.setMessage("Customer Details doesn't Exists");
			return status;
		}

		if (request.getDeliveries() == null || request.getDeliveries().isEmpty()) {
			status.setCode("E-0005");
			status.setMessage("Delivery Details doesn't Exists");
			return status;
		} else {
			for (Delivery delivery : request.getDeliveries()) {
				if (delivery.getDeliveryId() <= 0) {
					status.setCode("E-0006");
					status.setMessage("Delivery Id doesn't Exists");
					return status;
				}
			}
		}
		return null;
	}

	private Status validateOrderRequest(Request request) {

		Status status = new Status();
		if (request.getOrderNo() == null || request.getOrderNo().trim().isEmpty()) {
			status.setCode("E-0001");
			status.setMessage("OrderNo doesn't Exists");
			return status;
		}

		if (request.getReservationId() == null ) {
			status.setCode("E-0010");
			status.setMessage("Reservation Id doesn't Exists");
			return status;
		}
		return null;
	}
}
