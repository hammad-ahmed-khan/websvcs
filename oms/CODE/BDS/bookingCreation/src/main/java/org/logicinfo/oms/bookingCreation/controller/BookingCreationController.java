package org.logicinfo.oms.bookingCreation.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class BookingCreationController {

	@Autowired
	private BookingCreationDAO dao;

	@Autowired
	CustomerValidator customerValidator;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger log = Logger.getLogger(BookingCreationController.class);

	@PostMapping(value = "/booking", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<CreateBookingResponse>> getOrderItemsResponse(@RequestBody CreateBookingRequest sub)
			throws JsonProcessingException, SQLException {
		List<CreateBookingResponse> bCreationList = new ArrayList<CreateBookingResponse>();
		Long seqId = null;
		log.info("Inside Controller: Booking Reservation: Order Number is :" + sub.getOrderNo() + "Date:" +new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

		try {
			/*
			 * Save the Incoming Request
			 */
			try {
				log.info("Saving the Request Body into the table: Booking Reservation: Order Number is :" + "Date" +sub.getOrderNo() + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
				seqId = saveRequest(sub);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			ArrayList<Deliveries> deliveries = sub.getDeliveries();

			Map<Integer, List<BookingsResponse>> mapBookResponseObj = new ConcurrentHashMap<Integer, List<BookingsResponse>>();
			Map<Integer, List<Status>> mapstatusObj = new ConcurrentHashMap<Integer, List<Status>>();
			List<DeliveriesResponse> delvResponseList = null;
			List<BookingsResponse> bookingResponseList = null;
			List<Status> statusList = null;
			CreateBookingResponse CreateBookingResponseObj = null;
			DeliveriesResponse dlvrResponse = new DeliveriesResponse();
			String orderNo = null;
			int resvId = 0;
			// int delvId =0;
			int grpId = 0;
			String custNo = null;
			Date reqDate = null;
			String window = null;
			int slots = 0;
			int region = 0;
			int lineNo = 0;
			int orderQty = 0;
			String itemCode = null;
			String custCity = null;
			String custArea = null;
			String custName = null;
			String custAddr = null;
			String custMob = null;
			String custLat = null;
			String custLng = null;
			/*
			 * String bookingId = null; String status = null; String msg = null;
			 * String inputQuery="";
			 */
			String flag = "F";
			String inputMismatch = "";
			List<Items> inputItemsList;
//			System.out.println("Inside controller: Delivery Size:::" + deliveries.size());

			ErrorResponse valid = customerValidator.validation(sub);
//			System.out.println("Controller Valid:::" + valid);

			if (valid.getErrorCode() != null) {
				log.info("Validation Error");
			} else {
				orderNo = sub.getOrderNo();
				for (int dsize = 0; dsize < deliveries.size(); dsize++) {

					// delvId = sub.getDeliveries().get(dsize).getDeliveryId();
					custCity = sub.getDeliveries().get(dsize).getDeliveryAddress().getCity(); 
					custArea = sub.getDeliveries().get(dsize).getDeliveryAddress().getArea();
					custName = sub.getDeliveries().get(dsize).getDeliveryAddress().getFirstName();
					custAddr = sub.getDeliveries().get(dsize).getDeliveryAddress().getAddress1();
					custMob = sub.getDeliveries().get(dsize).getDeliveryAddress().getPhoneNo();
					custLat = sub.getDeliveries().get(dsize).getDeliveryAddress().getLat();
					custLng = sub.getDeliveries().get(dsize).getDeliveryAddress().getLng();

					int countk = 0;
					for (int bsize = 0; bsize < deliveries.get(dsize).getBookings().size(); bsize++) {



						grpId = sub.getDeliveries().get(dsize).getBookings().get(bsize).getGroupId();
						resvId = sub.getDeliveries().get(dsize).getBookings().get(bsize).getReservationId();
						custNo = sub.getCustomerDetails().getCustomerId();
						reqDate = (Date) sub.getDeliveries().get(dsize).getBookings().get(bsize).getDate();
						log.info("DATE:" + reqDate);
						window = sub.getDeliveries().get(dsize).getBookings().get(bsize).getWindowCode();
						slots = sub.getDeliveries().get(dsize).getBookings().get(bsize).getSlots();
						region = sub.getDeliveries().get(dsize).getBookings().get(bsize).getRegionID();
						inputItemsList = sub.getDeliveries().get(dsize).getBookings().get(bsize).getItems();

						inputMismatch = installMismatch(inputItemsList);
						if (inputMismatch.equalsIgnoreCase("Mismatch")) {
							flag = "T";
						} else {
							log.info("Before Package call DateTime: Booking Reservation: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
							dlvrResponse = dao.getResponse(resvId, orderNo, deliveries, dsize, grpId, bsize, lineNo,
									itemCode, orderQty, reqDate, window, slots, region, custCity, custArea, custName,
									custAddr, custMob, custLat, custLng);
							System.out.println("After Package call DateTime: Booking Reservation: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
							Integer delveryId = dlvrResponse.getDeliveryId();
							BookingsResponse bkresponse = dlvrResponse.getBookings().get(0);
							Status statusObj = dlvrResponse.getStatuss();

							bookingResponseList = new ArrayList<BookingsResponse>();
							statusList = new ArrayList<Status>();
							if (mapBookResponseObj.containsKey(delveryId) == Boolean.FALSE) {
								bookingResponseList.add(bkresponse);
								mapBookResponseObj.put(delveryId, bookingResponseList);
								if (null != statusObj) {
									statusList.add(statusObj);
									mapstatusObj.put(delveryId, statusList);
								}

							} else {
								List<BookingsResponse> previousList = mapBookResponseObj.get(delveryId);
								previousList.add(bkresponse);
								mapBookResponseObj.put(delveryId, previousList);
								if (null != statusObj) {
									List<Status> previousstatusList = mapstatusObj.get(delveryId);
									if (null != previousstatusList) {
										previousstatusList.add(statusObj);
										mapstatusObj.put(delveryId, previousstatusList);
									} else {
										statusList.add(statusObj);
										mapstatusObj.put(delveryId, statusList);
									}
								}
							}
						}
					}
				}
				CreateBookingResponseObj = new CreateBookingResponse();
				if (flag.equalsIgnoreCase("F")) {
					// delvResponseList
					// Get the delivery id and based on the build the Booking
					// BookingsResponseList

					int counter = 0;
					for (Integer key : mapBookResponseObj.keySet()) {
						List<BookingsResponse> bookingResponseList1 = mapBookResponseObj.get(key);
						delvResponseList = new ArrayList<DeliveriesResponse>();
						DeliveriesResponse deliveryResponse = new DeliveriesResponse();
						deliveryResponse.setDeliveryId(key);
						for (BookingsResponse bookingResponseObjecttemp : bookingResponseList1) {
							deliveryResponse.setBookings(bookingResponseObjecttemp);
						}
						delvResponseList.add(deliveryResponse);
						if (counter == 0) {
							CreateBookingResponseObj.setDeliveries(delvResponseList);
							counter++;
						} else {
							List<DeliveriesResponse> previousDeliveryList = CreateBookingResponseObj.getDeliveries();
							List<DeliveriesResponse> newDeliveryResponseList = new ArrayList<DeliveriesResponse>(
									previousDeliveryList);
							newDeliveryResponseList.addAll(delvResponseList);
							CreateBookingResponseObj.setDeliveries(newDeliveryResponseList);
						}
					}
				}
				Status statuses = null; 
				if (mapstatusObj.size() == 0 && flag != "T") {
					statuses = new Status();
					statuses.setCode("");
					statuses.setSuccess(Boolean.TRUE);
					statuses.setMessage(null);

				} else {
					statuses = new Status();
					statuses.setCode("");
					statuses.setSuccess(Boolean.FALSE);
					if (flag.equalsIgnoreCase("T")) {
						statuses.setMessage("ITEMS MISMATCH");
					} else {
						for (Integer key : mapstatusObj.keySet()) {
							statuses.setMessage(mapstatusObj.get(key).get(0).getMessage());
							break;
						}
					}
				}

				CreateBookingResponseObj.setStatus(statuses);
				bCreationList.add(CreateBookingResponseObj);


			}
			try {
				saveResponse(sub.getOrderNo(), bCreationList, seqId);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} catch (Exception exception) {
			log.error("Error :", exception);
			Status status = new Status();
			CreateBookingResponse response = new CreateBookingResponse();
			status.setCode("400");
			status.setSuccess(false);
			status.setMessage("Error During Booking Reserve. The error message is "+ exception);
			response.setStatus(status);
			bCreationList.add(response);
			exception.printStackTrace();
		}
		return new ResponseEntity<List<CreateBookingResponse>>(bCreationList, HttpStatus.OK);
	}

	public String installMismatch(List<Items> inputItemsList) throws SQLException {
		// String line="";
		String insQuery = "";
		String dept = "";
		String delvQuery = "";
		String installQuery = "";
		int count, acCount, installCount;
		count = acCount = installCount = 0;
		ResultSet rs = null;
		ResultSet rsDelv = null;
		ResultSet rsInsSku = null;
		Statement stmtInsSku = null;
		Statement stmt = null;
		Statement stmtDelv = null;
		Connection conn = null;
		int iSize = 0;
		int itemClass = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			stmtDelv = conn.createStatement();
			stmtInsSku = conn.createStatement();
			for (Items itm : inputItemsList) {
				insQuery = "select DEPT, CLASS from ITEM_MASTER WHERE ITEM ='" + itm.getItemCode() + "'";
				Map<String, Object> params = Collections.<String, Object>singletonMap("item", itm.getItemCode());
				Map<String, Object> row = jdbcTemplate.queryForMap(insQuery);

				dept = String.valueOf(row.get("DEPT"));
				itemClass = ((Number) row.get("CLASS")).intValue();

				delvQuery = "select item_code from XX_DLVRY_AC_ITEMS where item_code ='" + itm.getItemCode() + "'";
				installQuery = "select item from XX_DLVRY_AC_INS_ITEMS_V where item ='" + itm.getItemCode() + "'";
				rsDelv = stmtDelv.executeQuery(delvQuery);
				rsInsSku = stmtInsSku.executeQuery(installQuery);
				if (row != null) {
					if (rsDelv.next()) {
						if (rsInsSku.next()) { // ||dept.equals("411")
							iSize = itm.getOrderQty();
							installCount = iSize + installCount;
						} else {
							count++;
						}
					} else {
						if (dept.equals("402") && (itemClass == 2 || itemClass == 3)) {
							iSize = itm.getOrderQty();
							acCount = iSize + acCount;
						} else {
							count++;
						}
					}
				}
			}
			if (acCount > 0 && installCount > 0) {
				if (acCount == installCount) {
					return "Equal";
				} else if (acCount != installCount) {
					return "Mismatch";
				}
			} else {
				return "Equal";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
			if (rsDelv != null) {
				rsDelv.close();
			}
			if (rsInsSku != null) {
				rsInsSku.close();
			}
			if (stmtDelv != null) {
				stmtDelv.close();
			}
			if (stmtInsSku != null) {
				stmtInsSku.close();
			}
		}
		return "Equal";
	}

	private Long saveRequest(CreateBookingRequest sub) {
		try {
			return dao.saveRequest(sub);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;

	}

	private void saveResponse(String OrderNumber, List<CreateBookingResponse> sAResposne, Long seqId) {
		try {
			dao.saveResponse(OrderNumber, sAResposne, seqId);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
