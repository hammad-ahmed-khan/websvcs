/**
 * 
 */
package com.extra.oms.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.oms.bean.Address;
import com.extra.oms.bean.AddressInfo;
import com.extra.oms.common.BaseException;
import com.extra.oms.common.util.NumberUtil;
import com.extra.oms.core.bean.BookingDetail;
import com.extra.oms.core.bean.CustomerAddressInfo;
import com.extra.oms.core.bean.ItemCancellation;
import com.extra.oms.core.bean.ItemReturnDetail;
import com.extra.oms.core.bean.OrderInfo;
import com.extra.oms.core.bean.OrderItemDetail;

/**
 * @author aibrahim
 *
 */
@Repository
public class OrderDAO extends BaseDAO {

	private static final Logger _LOG = Logger.getLogger(OrderDAO.class);

	private final static String ORDER_SEARCH_QUERY = "SELECT A.CUST_ORDER_NO, A.OMS_CUST_ORD_NO, A.ORDER_REQUESTOR_ID,  A.CREATE_DATETIME, CASE WHEN A.ORDER_REQUESTOR_ID LIKE '1%'"
			+ " AND A.ORDER_REQUESTOR_ID <> 19008 THEN 'KSA' WHEN ORDER_REQUESTOR_ID = 19008 THEN 'NOON' WHEN ORDER_REQUESTOR_ID LIKE '2%' THEN 'BH' "
			+ " WHEN ORDER_REQUESTOR_ID LIKE '3%' THEN 'OM' END AS \"COUNTRY\", DECODE (A.DELIVERY_TYPE, 'C', 'CFS', 'SFS') DELIVERY_TYPE, A.PICK_LOC, "
			+ "(SELECT STORE_NAME3 FROM STORE X WHERE X.STORE = A.PICK_LOC) PICKLOC_NAME, DECODE (A.ORDER_CREATE_RESERVE_IND, 'R', 'Reservation', 'C', 'Confirmed') PAYMENT_TYPE,"
			+ " A.ORDER_CREATE_RESERVE_IND, DECODE (A.ORD_PAYMENT_STATUS, 'S', 'Success', 'P', 'Pending', 'R', 'Rejected') PAYMENT_STATUS,  A.ORD_PAYMENT_STATUS, "
			+ "(SELECT SUM (NVL(TENDER_AMT,0)) TND_AMT FROM OMS_CUST_ORD_TENDER Z WHERE Z.OMS_CUST_ORD_NO = A.OMS_CUST_ORD_NO) TENDER_AMT, "
			+ "(SELECT CASE WHEN SUM(NVL(CUM_QTY_DELIVERED,0)) + SUM(NVL(QTY_CANCELLED,0)) = 0 THEN 'New' "
			+ "WHEN SUM(NVL(QTY_ORDERED_SUOM,0)) = (SUM(NVL(CUM_QTY_DELIVERED,0)) + SUM(NVL(QTY_CANCELLED,0))) THEN 'Completed' "
			+ "WHEN SUM(NVL(QTY_ORDERED_SUOM,0)) > (SUM(NVL(CUM_QTY_DELIVERED,0)) + SUM(NVL(QTY_CANCELLED,0))) THEN 'In-Progress' END AS \"STATUS\" "
			+ "FROM  OMS_CUST_ORD_ITEM X WHERE X.OMS_CUST_ORD_NO = A.OMS_CUST_ORD_NO ) AS \"ORDER_STATUS\", (SELECT LISTAGG(TENDER_TYPE_GROUP|| '-' ||TENDER_TYPE_ID, ', ') "
			+ "WITHIN GROUP (ORDER BY TENDER_TYPE_GROUP|| '-' ||TENDER_TYPE_ID DESC) \"TENDER_TYPE\" FROM OMS_CUST_ORD_TENDER X WHERE X.OMS_CUST_ORD_NO = A.OMS_CUST_ORD_NO)"
			+ " TENDER_TYPE, A.CUST_FIRST_NAME || ' ' || A.CUST_LAST_NAME CUSTOMER_NAME, A.CUST_PHONE_NO CUSTOMER_PHONE_NO, a.CLOSE_DATETIME FROM OMS_CUST_ORD_HEAD A WHERE A.STATUS = 'S'";

	private final static String GET_ORDER_DETAIL = "SELECT I.ITEM, I.LINE_NO, I.SHIP_CLASSIFICATION, I.QTY_ORDERED_SUOM, I.CUM_QTY_DELIVERED, I.QTY_CANCELLED, I.QTY_RETURNED, F.SOURCE_LOC,F.SOURCE_LOC_TYPE, CASE WHEN F.SOURCE_LOC_TYPE = 'ST' THEN (SELECT STORE_NAME3 FROM STORE S WHERE S.STORE = F.SOURCE_LOC) WHEN F.SOURCE_LOC_TYPE = 'WH' THEN (SELECT WH_NAME FROM WH W WHERE W.WH = F.SOURCE_LOC) WHEN F.SOURCE_LOC_TYPE = 'SU' THEN (SELECT SUP_NAME FROM SUPS SP WHERE SP.SUPPLIER = F.SOURCE_LOC) END AS SOURCE_LOC_NAME, F.FULFILL_LOC, CASE WHEN F.FULFILL_LOC_TYPE = 'S' THEN (SELECT STORE_NAME3 FROM STORE S WHERE S.STORE = F.FULFILL_LOC) WHEN F.FULFILL_LOC_TYPE = 'V' THEN 'Virtual' END AS FULFILL_LOC_NAME, F.FULFILL_LOC_TYPE, F.FULFILL_ORDER_NO, SC.CARRIER_CODE, SC.CARRIER_SHIPMENT_NBR FROM OMS_CUST_ORD_ITEM I INNER JOIN OMS_CUST_ORD_HEAD H ON H.OMS_CUST_ORD_NO = I.OMS_CUST_ORD_NO LEFT OUTER JOIN OMS_CO_FULFILL_DETAIL F ON F.ITEM = I.ITEM AND F.LINE_NO = I.LINE_NO AND F.OMS_CUST_ORD_NO = I.OMS_CUST_ORD_NO LEFT OUTER JOIN XX_CARRIER_TRACKING_V SC ON SC.ITEM_ID = I.ITEM AND SC.CUST_ORDER_NBR = H.CUST_ORDER_NO WHERE I.OMS_CUST_ORD_NO = :omsOrderNo";

	private static final String GET_ADDRESS_DETAIL = "SELECT DELIVER_FIRST_NAME, DELIVER_LAST_NAME, DELIVER_ADD_1, DELIVER_ADD_2, DELIVER_CITY, (SELECT DESCRIPTION FROM STATE WHERE STATE = DELIVER_STATE AND ROWNUM = 1) DELIVER_STATE, (SELECT COUNTRY_DESC FROM COUNTRY WHERE COUNTRY_ID = DELIVER_COUNTRY) DELIVER_COUNTRY, DELIVER_POST, DELIVER_PHONE_NO FROM OMS_CUST_ORD_ADDRESS WHERE OMS_CUST_ORD_NO = :omsOrderNo";

	private static final String GET_CANCELLATION_DETAIL = "SELECT H.CUST_ORD_NO, I.OMS_CANCEL_ID, H.CANCEL_REQST_LOC_ID, H.REFUND_OPTION, H.REFUND_COMPLT_IND, H.REFUND_AMOUNT, H.CAN_REQ_DATETIME, I.LINE_NO, I.ITEM, I.CANCEL_CONF_QTY, U.USER_ID, U.USER_NAME FROM OMS_CO_CANCEL_HEAD H, OMS_CO_CANCEL_ITEM I, OMS_CANCEL_REQ_AUDIT A, USER_ATTRIB U WHERE A.CANCELLATION_ID (+) = H.CANCEL_REQ_ID AND H.OMS_CANCEL_ID = I.OMS_CANCEL_ID AND U.USER_ID (+) = A.CREATED_ID AND H.CUST_ORD_NO = :customerNo ";

	private static final String GET_RMA_DETAIL_QUERY = "SELECT * FROM (SELECT 'RMA' AS TYPE, R.CUST_ORDER_NO, R.RMA_ID, RI.ITEM, RD.RECEIVED_QTY, R.RETURN_LOC_ID, R.REFUND_OPTION, R.REFUND_AMOUNT, R.REFUND_COMPLT_IND, R.COMMENTS, R.RETURN_STATUS FROM OMS_RMA_REQ R, OMS_RMA_REQ_ITEM RI, OMS_RMA_RCV_DTL RD WHERE R.RMA_ID = RI.RMA_ID AND R.RMA_ID = RD.RMA_ID AND R.STATUS = 'S' UNION SELECT 'POS' AS TYPE, ORDER_ID CUST_ORDER_NO, NULL RMA_ID, B.ITEM, RETURN_QUANTITY RECEIVED_QTY, TO_NUMBER(SUBSTRB (ORPOS_TRANSACTION_NUMBER, 1, 5)) RETURN_LOC_ID ,'ORPOS' REFUND_OPTION, NULL REFUND_AMT, 'Y', 'ORPOS RETURN', 'Y' from OMS_ORPOS_MASTER_AUDIT A, OMS_CUST_ORD_ITEM B WHERE A.LINE_ITEM_NO = B.LINE_NO AND a.OMS_CUST_ORDER_NO = B.OMS_CUST_ORD_NO) WHERE CUST_ORDER_NO = :customerNo";

	private static final String GET_BOOKING_DETAIL_QUERY = "SELECT D.BOOK_ID BOOKING_ID, V.BOOK_LINE_ID, V.STATUS, V.LINE_NO, V.ITEM, D.REQUEST_DATE BOOKING_DATE, D.WINDOW_CODE BOOKING_WINDOW, V.DLVRY_TIME, D.AREA_NAME AREA, V.GROUP_ID, V.GROUP_NAME, D.SRC SOURCE FROM XX_DLVRY_BOOKING D, XX_DLVRY_BOOKING_V V WHERE D.BOOK_ID = V.BOOK_ID AND V.ORDER_NUMBER = :customerNo ";

	public List<OrderInfo> searchCustomerOrders(Map<String, Object> params) throws BaseException {
		
		StringBuilder queryBuilder = new StringBuilder(ORDER_SEARCH_QUERY);
		if (params.get("fromDate") != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(params.get("fromDate").toString()));
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			params.put("fromDate", cal.getTime());
			appendWhere(queryBuilder);
			queryBuilder.append(" A.CREATE_DATETIME >= :fromDate ");
		}
		if (params.get("toDate") != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(params.get("toDate").toString()));
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			params.put("toDate", cal.getTime());
			appendWhere(queryBuilder);
			queryBuilder.append(" A.CREATE_DATETIME < :toDate ");
		}
		if (params.get("orderNumber") != null) {
			appendWhere(queryBuilder);
			queryBuilder.append(" A.CUST_ORDER_NO = :orderNumber ");
		}
		if (params.get("deliveryType") != null) {
			appendWhere(queryBuilder);
			queryBuilder.append(" A.DELIVERY_TYPE = :deliveryType ");
		}
		if (params.get("paymentType") != null) {
			appendWhere(queryBuilder);
			queryBuilder.append(" A.ORD_PAYMENT_STATUS = :paymentType ");
		}
		Object country = null;
		if ((country = params.get("country")) != null) {
			appendWhere(queryBuilder);
			if ("KSA".equals(country)) {
				queryBuilder.append(" A.ORDER_REQUESTOR_ID <> 19008 AND A.ORDER_REQUESTOR_ID < 20000 ");
			} else if ("BH".equals(country)) {
				queryBuilder.append(" A.ORDER_REQUESTOR_ID >= 20000 AND A.ORDER_REQUESTOR_ID < 30000 ");
			} else if ("OM".equals(country)) {
				queryBuilder.append(" A.ORDER_REQUESTOR_ID >= 30000 ");
			}
		}
		Object orderType = null;
		if ((orderType = params.get("orderType")) != null) {
			appendWhere(queryBuilder);
			if ("NOON".equals(orderType)) {
				queryBuilder.append(" A.ORDER_REQUESTOR_ID = 19008 ");
			} else {
				queryBuilder.append(" A.APPLICATION_ID = :orderType ");
			}
		}
		if (params.get("status") != null) {
			appendWhere(queryBuilder);
			if ("O".equals(params.get("status"))) {
				queryBuilder.append(" A.CLOSE_DATETIME IS NULL ");
			} else {
				queryBuilder.append(" A.CLOSE_DATETIME IS NOT NULL ");
			}
		}
		return jdbcTemplate.query(queryBuilder.toString(), params, new RowMapper<OrderInfo>() {

			@Override
			public OrderInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrderInfo order = new OrderInfo();
				order.setOrderNumber(rs.getString(1));
				order.setOmsOrderNumber(rs.getString(2));
				order.setCreatedDate(rs.getTimestamp(4));
				order.setCountryName(rs.getString(5));
				order.setDeliveryType(rs.getString(6));
				order.setPicLocation(rs.getLong(7));
				order.setPicLocationName(rs.getString(8));
				order.setPaymentStatus(rs.getString(11));
				order.setPaymentType(rs.getString(9));
				order.setPaymentStatusCode(rs.getString(12));
				order.setTenderAmount(rs.getBigDecimal(13));
				order.setClosedDate(rs.getTimestamp(18));
				order.setCustomerName(rs.getString(16));
				order.setCustomerMobile(rs.getString(17));
				order.setStatus(rs.getString(14));
				return order;
			}
		});
	}

	public OrderInfo getOrderDetail(Long omsCustomerNo) {
		return jdbcTemplate.query(GET_ORDER_DETAIL, Collections.singletonMap("omsOrderNo", omsCustomerNo), new ResultSetExtractor<OrderInfo>() {

			@Override
			public OrderInfo extractData(ResultSet rs) throws SQLException {
				OrderInfo order = null;
				while(rs.next()) {
					if (rs.isFirst()) {
						order = new OrderInfo();
						order.setItems(new ArrayList<OrderItemDetail>());
					}
					OrderItemDetail itemDetail = new OrderItemDetail();
					itemDetail.setItem(rs.getString(1));
					itemDetail.setLineNo(rs.getLong(2));
					itemDetail.setClassification(rs.getString(3));
					itemDetail.setOrderedQty(rs.getBigDecimal(4));
					itemDetail.setDeliveredQty(rs.getBigDecimal(5));
					itemDetail.setCancelledQty(rs.getBigDecimal(6));
					itemDetail.setReturnedQty(rs.getBigDecimal(7));
					itemDetail.setSrcLocation(rs.getLong(8));
					itemDetail.setSrcLocationType(rs.getString(9));
					itemDetail.setSrcLocationName(rs.getString(10));
					itemDetail.setFulfilLocation(rs.getLong(11));
					itemDetail.setFulfilLocationName(rs.getString(12));
					itemDetail.setFulfilLocationType(rs.getString(13));
					itemDetail.setFulfilOrdNo(rs.getLong(14));
					itemDetail.setCourierName(rs.getString(15));
					itemDetail.setAwbNo(rs.getString(16));
					if (NumberUtil.zeroIfNull(itemDetail.getCancelledQty()).intValue() > 0) {
						order.setHasCancellation(true);
					}
					if (NumberUtil.zeroIfNull(itemDetail.getReturnedQty()).intValue() > 0) {
						order.setHasReturn(true);
					}
					order.getItems().add(itemDetail);
				}
				return order;
			}
		});
	}

	public CustomerAddressInfo getAddressDetail(Long omsCustomerNo) {
		return jdbcTemplate.queryForObject(GET_ADDRESS_DETAIL, Collections.singletonMap("omsOrderNo", omsCustomerNo), new RowMapper<CustomerAddressInfo>() {

			@Override
			public CustomerAddressInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CustomerAddressInfo addressInfo = null;
				addressInfo = new CustomerAddressInfo();
				addressInfo.setDeliverFirstName(rs.getString(1));
				addressInfo.setDeliverLastName(rs.getString(2));
				addressInfo.setDeliverAdd1(rs.getString(3));
				addressInfo.setDeliverAdd2(rs.getString(4));
				addressInfo.setDeliverCity(rs.getString(5));
				addressInfo.setDeliverState(rs.getString(6));
				addressInfo.setDeliverCountry(rs.getString(7));
				addressInfo.setDeliverPost(rs.getString(8));
				addressInfo.setDeliverPhone(rs.getString(9));
				return addressInfo;
			}
		});
	}

	public List<ItemCancellation> getCancellationDetail(String customerNo) {
		return jdbcTemplate.query(GET_CANCELLATION_DETAIL, Collections.singletonMap("customerNo", customerNo), new RowMapper<ItemCancellation>() {

			@Override
			public ItemCancellation mapRow(ResultSet rs, int rowNum) throws SQLException {
				ItemCancellation cancelDetail = new ItemCancellation();
				cancelDetail.setCancelReqId(rs.getLong(3));
				cancelDetail.setItem(rs.getString(9));
				cancelDetail.setQuantity(rs.getInt(10));
				cancelDetail.setCreatedTime(rs.getTimestamp(7));
				cancelDetail.setRefundType(rs.getString(4));
				cancelDetail.setRefundStatus(rs.getString(5));
				cancelDetail.setRefundAmount(rs.getBigDecimal(6));
				cancelDetail.setUserId(rs.getLong(11));
				cancelDetail.setUserName(rs.getString(12));
				return cancelDetail;
			}
		});
	}

	public List<ItemReturnDetail> getReturnDetail(String customerNo) {
		return jdbcTemplate.query(GET_RMA_DETAIL_QUERY, Collections.singletonMap("customerNo", customerNo), new RowMapper<ItemReturnDetail>() {

			@Override
			public ItemReturnDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
				ItemReturnDetail returnDetail = new ItemReturnDetail();
				returnDetail.setType(rs.getString(1));
				returnDetail.setItem(rs.getString(4));
				returnDetail.setQuantity(rs.getBigDecimal(5));
				returnDetail.setRefundOption(rs.getString(7));
				returnDetail.setRefundStatus(rs.getString(9));
				returnDetail.setRefundAmount(rs.getBigDecimal(8));
				return returnDetail;
			}
		});
	}

	public List<BookingDetail> getBookingDetail(String customerNo) {
		return jdbcTemplate.query(GET_BOOKING_DETAIL_QUERY, Collections.singletonMap("customerNo", customerNo), new RowMapper<BookingDetail>() {

			@Override
			public BookingDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
				BookingDetail bookingDetail = new BookingDetail();
				bookingDetail.setBookingId(rs.getLong(1));
				bookingDetail.setItem(rs.getString(4));
				bookingDetail.setDate(rs.getDate(6));
				bookingDetail.setDeliveryTime(rs.getString(8));
				bookingDetail.setStatus(rs.getString(3));
				bookingDetail.setWindow(rs.getString(7));
				return bookingDetail;
			}
		});
	}

	public String validateOrderNumber(String type, String orderNo) {
		String message = "OK";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("status", "S");
		if (type.equalsIgnoreCase("order")) {
			try {
				String status = jdbcTemplate.queryForObject("SELECT ADDR_VERIFIED_IND FROM OMS_CUST_ORD_HEAD WHERE CUST_ORDER_NO = :orderNo AND STATUS = :status ", params, String.class);
				if ("B".equalsIgnoreCase(status)) {
					message = "ALREADY_VERIFIED";
				}
			} catch (Exception e) {
				_LOG.warn("Error while check the order status", e);
				message = "INVALID";
			}
		} else {
			try {
				String status = jdbcTemplate.queryForObject("SELECT ADDR_VERIFIED_IND FROM XX_POS_ADDR_VER_PU WHERE INVOICE_NO = :orderNo ", params, String.class);
				if ("Y".equalsIgnoreCase(status)) {
					message = "ALREADY_VERIFIED";
				}
			} catch (Exception e) {
				// EAT
			}
		}
		return message;
	}

	@Transactional
	public void updateCustomerAddress(AddressInfo addressInfo, Address address) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", addressInfo.getOrderNo());
		params.put("ind", "Y");
		params.put("status", "S");
		params.put("addr1", address.getAddress1());
		params.put("addr2", address.getAddress2());
		params.put("addr3", address.getCity());
		params.put("lat", address.getLatitude());
		params.put("long", address.getLongitude());
		params.put("srtAddr", address.getShortAddress());
		params.put("city", address.getCity());
		if (addressInfo.getType().equalsIgnoreCase("order")) {
			jdbcTemplate.update("UPDATE OMS_CUST_ORD_HEAD SET ADDR_VERIFIED_IND = :ind WHERE STATUS = :status AND CUST_ORDER_NO = :orderNo", params);
			jdbcTemplate.update("UPDATE ORDCUST SET DELIVER_ADD1 = :addr1, DELIVER_ADD2 = :addr2, DELIVER_ADD3 = :addr3 WHERE STATUS <> 'X' AND CUSTOMER_ORDER_NO = :orderNo ", params);
			jdbcTemplate.update("UPDATE OMS_CUST_ORD_ADDRESS SET DELIVER_ADD_1 = :addr1, DELIVER_ADD_2 = :addr2, DELIVER_ADD_3 = :addr3, LATITUDE = :lat, LONGITUDE = :long, DELV_SHORT_ADDR = :srtAddr WHERE OMS_CUST_ORD_NO IN (SELECT OMS_CUST_ORD_NO FROM OMS_CUST_ORD_HEAD WHERE STATUS = :status AND CUST_ORDER_NO = :orderNo )", params);
			jdbcTemplate.update("UPDATE XX_DLVRY_BOOKING SET LONGITUDE = :long, LATITUDE = :lat, CUSTOMER_ADDRESS = :addr1 WHERE BOOK_ID IN (SELECT DISTINCT BOOK_ID FROM XX_DLVRY_BOOKING_LINES WHERE HYBRIS_CUST_NBR = :orderNo)", params);
		} else {
			jdbcTemplate.update("INSERT INTO XX_POS_ADDR_VER_PU(INVOICE_NO, DELIVER_ADD_1, DELIVER_ADD_2, DELIVER_ADD_3, DELIVER_CITY, DELV_SHORT_ADDR, LAT, LON, CREATE_DATETIME, ADDR_VERIFIED_IND) VALUES(:orderNo, :addr1, :addr2, :addr3, :city, :srtAddr, :lat, :long, SYSDATE, :ind)", params);
		}
	}

	public String getCustomerCity(String orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("status", "S");
		try {			
			return jdbcTemplate.queryForObject("SELECT DELIVER_CITY FROM OMS_CUST_ORD_ADDRESS WHERE OMS_CUST_ORD_NO = ( SELECT OMS_CUST_ORD_NO FROM OMS_CUST_ORD_HEAD WHERE CUST_ORDER_NO = :orderNo AND STATUS = :status) ", params, String.class);

		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
}
