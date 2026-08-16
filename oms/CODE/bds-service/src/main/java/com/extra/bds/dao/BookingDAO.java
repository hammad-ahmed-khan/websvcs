/**
 * 
 */
package com.extra.bds.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.extra.bds.bean.Booking;
import com.extra.bds.bean.CustomerDetail;
import com.extra.bds.bean.Dates;
import com.extra.bds.bean.Delivery;
import com.extra.bds.bean.DeliveryAddress;
import com.extra.bds.bean.Group;
import com.extra.bds.bean.Item;
import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;
import com.extra.bds.bean.Status;
import com.extra.bds.bean.StatusResponse;
import com.extra.bds.bean.Window;
import com.extra.bds.util.StatusException;
import com.extra.common.model.OmsCoFulfillDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

import oracle.jdbc.OracleTypes;

/**
 * @author aibrahim
 *
 */
@Repository
public class BookingDAO {

	private static final Logger _LOG = Logger.getLogger(BookingDAO.class);

	private static final ThreadLocal<Long> THREAD_LOCAL_ORD = new ThreadLocal<Long>();

	private static final String ORDER_DETAIL_QUERY = "SELECT H.CUST_ORDER_NO, H.CUST_ORDER_TYPE, A.DELIVER_COUNTRY, H.CREATE_DATETIME, H.CUST_ID, H.CUST_PHONE_NO, "
			+ "H.CUSTOMER_LANG, A.BILL_ADD_2, A.DELIVER_FIRST_NAME, A.DELIVER_LAST_NAME, A.DELIVER_ADD_2, A.DELIVER_CITY, A.DELIVER_ADD_1, A.DELIVER_ADD_3, I.ITEM, "
			+ "I.LINE_NO, I.SHIP_CLASSIFICATION, I.BACKORDER_IND,  I.QTY_ORDERED_SUOM, I.UNIT_RETAIL, I.RETAIL_CURR, M.DEPT, M.CLASS, M.SUBCLASS, F.SOURCE_LOC, F.FULFILL_LOC "
			+ " FROM OMS_CUST_ORD_HEAD H, OMS_CUST_ORD_ITEM I, OMS_CUST_ORD_ADDRESS A, OMS_CO_FULFILL_DETAIL F, ITEM_MASTER M WHERE H.OMS_CUST_ORD_NO = I.OMS_CUST_ORD_NO "
			+ "AND H.OMS_CUST_ORD_NO = A.OMS_CUST_ORD_NO AND I.OMS_CUST_ORD_NO   = A.OMS_CUST_ORD_NO AND F.OMS_CUST_ORD_NO   = A.OMS_CUST_ORD_NO AND F.ITEM = I.ITEM AND "
			+ "F.LINE_NO = I.LINE_NO AND M.ITEM = I.ITEM AND H.STATUS = 'S' AND H.CUST_ORDER_NO = :ordNo";

	@Autowired
	private ObjectMapper jsonMapper;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public Long saveRequest(Request request) throws Exception {

		MapSqlParameterSource params = new MapSqlParameterSource();
		Long sequenceId = null;
		try {
			_LOG.info("Inserting the request body for the order number " + request.getOrderNo());
			GeneratedKeyHolder seqHolder = new GeneratedKeyHolder();
			params.addValue("ordNo", request.getOrderNo());
			params.addValue("req", jsonMapper.writeValueAsString(request), Types.CLOB);
			params.addValue("status", "N");
			params.addValue("ordInd", 0);
			params.addValue("source", "SAC");
			params.addValue("city", request.getDeliveries() != null && request.getDeliveries().get(0).getDeliveryAddress() != null ? request.getDeliveries().get(0).getDeliveryAddress().getCity() : null);
			params.addValue("area", request.getDeliveries() != null && request.getDeliveries().get(0).getDeliveryAddress() != null ? request.getDeliveries().get(0).getDeliveryAddress().getArea() : null);
			jdbcTemplate.update(
					"INSERT INTO OMS_ORDER_BOOKING_HEAD_INFO (SEQUENCE_NO, ORDER_NO, REQUEST, STATUS, ORD_IND, SOURCE, CREATE_DATETIME, CITY, AREA) VALUES(XX_BOOK_SEQ.NEXTVAL, :ordNo, :req, :status, :ordInd, :source, SYSTIMESTAMP, :city, :area)",
					params, seqHolder, new String[] { "SEQUENCE_NO" });
			sequenceId = seqHolder.getKey().longValue();
			THREAD_LOCAL_ORD.set(sequenceId);
		} catch (Exception exception) {
			_LOG.error("Failed to save request body for the order " + request.getOrderNo(), exception);
			throw exception;
		}
		return sequenceId;
	}

	public void updateResponse(String orderNumber, List<Response> response, Long sequenceId) {

		if (sequenceId == null) {
			sequenceId = THREAD_LOCAL_ORD.get();
		}
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("response", jsonMapper.writeValueAsString(response), Types.CLOB);
			params.addValue("ordNo", orderNumber);
			params.addValue("sequenceId", sequenceId);
			jdbcTemplate.update(
					"UPDATE OMS_ORDER_BOOKING_HEAD_INFO SET RESPONSE = :response, STATUS = 'S', ORD_IND = 1, UPDATE_DATETIME = SYSTIMESTAMP WHERE SEQUENCE_NO = :sequenceId",
					params);
		} catch (Exception exception) {
			_LOG.warn("Failed to update response body for the order " + orderNumber, exception);
		}
		THREAD_LOCAL_ORD.remove();
	}

	public Map<String, Character> getItemInstallationFlag(Collection<String> itemCodes, final List<String> deptIds) throws Exception {
		Map<String, Character> flagMap = jdbcTemplate.query("SELECT ITEM, DEPT FROM ITEM_MASTER WHERE ITEM IN (:itemCodes)", Collections.singletonMap("itemCodes", itemCodes),
				new ResultSetExtractor<Map<String, Character>>() {

					@Override
					public Map<String, Character> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, Character> itemInsallFlagMap = new HashMap<String, Character>();
						while (rs.next()) {
							itemInsallFlagMap.put(rs.getString(1), deptIds.contains(rs.getString(2)) ? 'Y' : 'N');
						}
						return itemInsallFlagMap;
					}
				});
		if (itemCodes.size() != flagMap.size()) {
			throw new StatusException("E-505", "Invalid Item Code");
		}
		return flagMap;
	}

	public String getExpressItemSKU() {
		try {
			return jdbcTemplate.getJdbcOperations()
					.queryForObject("SELECT PARAMETER_VALUE FROM OMS_SYSTEM_PARAMETERS WHERE PARAMETER_NAME = 'SHIPPING_CHARGE' AND PARAMETER_COMMENT = 'Express Delivery'", String.class);
		} catch (Exception e) {
			_LOG.warn("Error while fetching the express item sku", e);
		}
		return "";
	}

	public Map<String, Integer> getCityAreaCount(String query) {
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, Integer>>() {

			@Override
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, Integer> cityMap = new HashMap<String, Integer>();
				while (rs.next()) {
					cityMap.put(rs.getString(1), rs.getInt(2));
				}
				return cityMap;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Delivery> getAvailablity(List<Delivery> deliveries, final Map<Integer, String> itemQueryMap) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		List<Delivery> respDeliveries = new ArrayList<Delivery>(deliveries.size());
		StatusResponse status = new StatusResponse();
		status.setCode("");
		status.setMessage("");
		int fTime = 0;
		int tTime = 0;
		DecimalFormat dFormat = new DecimalFormat("00");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("groups", OracleTypes.CURSOR));
		declaredParameters.add(new SqlOutParameter("items", OracleTypes.CURSOR));
		declaredParameters.add(new SqlOutParameter("windows", OracleTypes.CURSOR));
		for (final Delivery delivery : deliveries) {

			Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL XXHDB_CORE_PKG.get_schedule(?, ?, ?, ?, ?) }");
					stmt.setString(1, itemQueryMap.get(delivery.getDeliveryId()));
					stmt.setString(2, delivery.getDeliveryAddress().getCity().toUpperCase());
					stmt.registerOutParameter(3, OracleTypes.CURSOR);
					stmt.registerOutParameter(4, OracleTypes.CURSOR);
					stmt.registerOutParameter(5, OracleTypes.CURSOR);
					return stmt;
				}
			}, declaredParameters);

			List<Map<String, Object>> groupSet = (List<Map<String, Object>>) result.get("groups");
			Delivery deliveryResp = new Delivery();
			deliveryResp.setDeliveryId(delivery.getDeliveryId());
			deliveryResp.setGroups(new ArrayList<Group>());
			for (Map<String, Object> groupMap : groupSet) {
				Group group = new Group();
				String gStatus = groupMap.get("STATUS").toString();
				if (gStatus.equals("F")) {
					throw new StatusException("E-905", "Delivery slots not available for the requested item");
				}
				group.setGroupId(new BigDecimal(groupMap.get("GROUP_ID").toString()));
				group.setGroupName(groupMap.get("GROUP_NAME").toString());
				group.setSlots(new BigDecimal(groupMap.get("SLOTS").toString()));
				group.setRegionID(new BigDecimal(groupMap.get("REGION_ID").toString()));
				group.setRegionName(groupMap.get("REGION_NAME").toString());
				group.setStatus(status);

				group.setItems(new ArrayList<Item>());
				List<Map<String, Object>> itemSet = (List<Map<String, Object>>) result.get("items");
				for (Map<String, Object> itemMap : itemSet) {
					Item item = new Item();
					item.setLineNo(Integer.parseInt(itemMap.get("LINE_NO").toString()));
					item.setItemCode(itemMap.get("ITEM").toString());
					item.setOrderQty(Integer.parseInt(itemMap.get("QTY").toString()));
					group.getItems().add(item);
				}

				List<Map<String, Object>> windowSet = (List<Map<String, Object>>) result.get("windows");
				group.setDates(new ArrayList<Dates>());
				Map<Date, Dates> dateMap = new HashMap<Date, Dates>();
				for (Map<String, Object> windowMap : windowSet) {
					Date date = dateFormatter.parse(windowMap.get("DELIVERY_DATE").toString());
					Dates dates = dateMap.get(date);
					if (dates == null) {
						dates = new Dates();
						dates.setDate(date);
						dates.setWindows(new ArrayList<Window>());
						group.getDates().add(dates);
						dateMap.put(date, dates);
					}
					Window window = new Window();
					window.setCode(windowMap.get("WINDOW").toString());
					window.setAvailable(windowMap.get("AVAILABLE").toString().equals("Y"));

					String time = windowMap.get("TIME_EN").toString();
					fTime = Integer.parseInt(time.substring(0, 2));
					tTime = Integer.parseInt(time.substring(11, 13));
					if ((time.substring(6, 8)).equals("PM") && fTime < 12) {
						fTime = fTime + 12;
					}
					if ((time.substring(17)).equals("PM") && tTime < 12) {
						tTime = tTime + 12;
					}
					if (window.getCode().equals("W5")) {
						tTime = 00;
					}
					if (window.getCode().equals("W6")) {
						fTime = 00;
						tTime = 03;
					}
					window.setFromTime(dFormat.format(fTime));
					window.setToTime(dFormat.format(tTime));

					dates.getWindows().add(window);
				}
				deliveryResp.getGroups().add(group);
				/*
				 * 14-05-2023 change for Megasale
				 * Added break to take only one group similar to old api
				 * need to remove this break later.
				 */
				break;
			}
			respDeliveries.add(deliveryResp);
		}
		return respDeliveries;
	}

	public List<Delivery> reservation(final Request request, Map<String, Character> itemInstallMap) {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		List<Delivery> respDeliveries = new ArrayList<Delivery>(request.getDeliveries().size());
		StatusResponse status = new StatusResponse();
		status.setCode("");
		status.setMessage("");

		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.ARRAY));
		declaredParameters.add(new SqlParameter(Types.DATE));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));

		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));

		StringBuilder cityAreaQuery = new StringBuilder();
		final Map<String, Integer> cityMap = new HashMap<String, Integer>();
		final Map<String, Integer> areaMap = new HashMap<String, Integer>();
		Set<String> cityNames = new HashSet<String>();
		for (Delivery delivery : request.getDeliveries()) {
			String city = delivery.getDeliveryAddress().getCity().toUpperCase();
			String area = delivery.getDeliveryAddress().getArea();
			if (area != null) {
				if (cityAreaQuery.length() > 0) {
					cityAreaQuery.append(" OR ");
				} else {
					cityAreaQuery.append(" WHERE ");
				}
				cityAreaQuery.append(" (UPPER(CITY_NAME) = '").append(city).append("'");
				cityAreaQuery.append(" AND UPPER(AREA_NAME) = '").append(area.toUpperCase()).append("') ");
			} else {
				cityNames.add(city);
			}
			delivery.getDeliveryAddress().getCity();
		}
		if (cityAreaQuery.length() > 0) {
			cityAreaQuery.insert(0, "SELECT UPPER(CITY_NAME), UPPER(AREA_NAME), CITY_ID, AREA_ID FROM XX_DLVRY_REGIONS_V");
			jdbcTemplate.query(cityAreaQuery.toString(), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					String city = rs.getString(1);
					cityMap.put(city, rs.getInt(3));
					areaMap.put(city + "~" + rs.getString(2), rs.getInt(4));
				}
			});
		}
		if (!cityNames.isEmpty()) {
			jdbcTemplate.query("SELECT DISTINCT CITY_NAME, CITY_ID FROM XX_DLVRY_REGIONS_V WHERE UPPER(CITY_NAME) IN (:cityNames)", new RowCallbackHandler() {

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					cityMap.put(rs.getString(1), rs.getInt(2));
				}
			});
		}

		for (final Delivery delivery : request.getDeliveries()) {
			Delivery deliveryResp = new Delivery();
			deliveryResp.setBookings(new ArrayList<Booking>());
			for (final Booking booking : delivery.getBookings()) {
				Booking bookResp = new Booking();
				final Object[][] objArr = new Object[booking.getItems().size()][4];
				int inc = 0;
				for (Item item : booking.getItems()) {
					objArr[inc][0] = item.getLineNo();
					objArr[inc][1] = item.getItemCode();
					objArr[inc][2] = item.getOrderQty();
					objArr[inc++][3] = itemInstallMap.get(item.getItemCode());
				}
				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						String city = delivery.getDeliveryAddress().getCity().toUpperCase();
						String area = delivery.getDeliveryAddress().getArea();
						CallableStatement stmt = con.prepareCall("{ CALL XXHDB_CORE_PKG.RESERVE_BOOKING(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
						stmt.setInt(1, booking.getReservationId());
						stmt.setString(2, request.getOrderNo());
						stmt.setInt(3, booking.getGroupId());
						stmt.setArray(4, ((oracle.jdbc.driver.OracleConnection) con).createOracleArray("XXHDB_TBL_TYPE", objArr));
						stmt.setDate(5, new java.sql.Date(booking.getDate().getTime()));
						stmt.setString(6, booking.getWindowCode());
						stmt.setInt(7, booking.getSlots());
						stmt.setInt(8, booking.getRegionID());
						stmt.setInt(9, cityMap.get(city));
						stmt.setInt(10, area == null ? 0 : areaMap.get(city + "~" + area.toUpperCase()));

						stmt.setString(11, delivery.getDeliveryAddress().getFirstName());
						stmt.setString(12, delivery.getDeliveryAddress().getAddress1());
						stmt.setString(13, delivery.getDeliveryAddress().getPhoneNo());
						stmt.setString(14, delivery.getDeliveryAddress().getLat());
						stmt.setString(15, delivery.getDeliveryAddress().getLng());
						stmt.registerOutParameter(16, Types.VARCHAR);
						stmt.registerOutParameter(17, Types.VARCHAR);
						return stmt;
					}
				}, declaredParameters);
				bookResp.setGroupId(booking.getGroupId());
				bookResp.setReservationId(booking.getReservationId());

				String bStatus = result.get("status").toString();
				if (bStatus.equals("F")) {
					Status eStatus = new Status();
					eStatus.setMessage(result.get("message").toString());
					deliveryResp.setStatus(eStatus);
				}
				deliveryResp.getBookings().add(bookResp);
			}
			deliveryResp.setDeliveryId(delivery.getDeliveryId());
			respDeliveries.add(deliveryResp);
		}
		return respDeliveries;
	}

	public List<Delivery> confirmBooking(final Request request) {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		List<Delivery> respDeliveries = new ArrayList<Delivery>(request.getDeliveries().size());
		StatusResponse status = new StatusResponse();
		status.setCode("");
		status.setMessage("");

		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("resultBookingId", Types.INTEGER));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));

		for (final Delivery delivery : request.getDeliveries()) {
			Delivery deliveryResp = new Delivery();
			deliveryResp.setBookings(new ArrayList<Booking>());
			for (final Booking booking : delivery.getBookings()) {
				Booking bookResp = new Booking();

				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement stmt = con.prepareCall("{ CALL XXHDB_CORE_PKG.CONFIRM_BOOKING(?, ?, ?, ?, ?, ?) }");
						stmt.setInt(1, booking.getReservationId());
						stmt.setString(2, request.getOrderNo());
						stmt.setString(3, request.getOrderNo());
						stmt.registerOutParameter(4, Types.INTEGER);
						stmt.registerOutParameter(5, Types.VARCHAR);
						stmt.registerOutParameter(6, Types.VARCHAR);
						return stmt;
					}
				}, declaredParameters);
				bookResp.setGroupId(booking.getGroupId());
				bookResp.setReservationId(booking.getReservationId());

				String bStatus = result.get("status").toString();
				if (bStatus.equals("F")) {
					Status eStatus = new Status();
					eStatus.setMessage(result.get("message").toString());
					deliveryResp.setStatus(eStatus);
				}
				deliveryResp.getBookings().add(bookResp);
			}
			deliveryResp.setDeliveryId(delivery.getDeliveryId());
			respDeliveries.add(deliveryResp);
		}
		return respDeliveries;
	}

	public List<Delivery> cancelBooking(final Request request) {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		List<Delivery> respDeliveries = new ArrayList<Delivery>(request.getDeliveries().size());
		StatusResponse status = new StatusResponse();
		status.setCode("");
		status.setMessage("");

		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));

		for (final Delivery delivery : request.getDeliveries()) {
			Delivery deliveryResp = new Delivery();
			deliveryResp.setBookings(new ArrayList<Booking>());
			for (final Booking booking : delivery.getBookings()) {
				Booking bookResp = new Booking();

				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement stmt = con.prepareCall("{ CALL XXHDB_CORE_PKG.CANCEL_BOOKING(?, ?, ?, ?) }");
						stmt.setInt(1, booking.getReservationId());
						stmt.setString(2, request.getOrderNo());
						stmt.registerOutParameter(3, Types.VARCHAR);
						stmt.registerOutParameter(4, Types.VARCHAR);
						return stmt;
					}
				}, declaredParameters);
				bookResp.setGroupId(booking.getGroupId());
				bookResp.setReservationId(booking.getReservationId());

				String bStatus = result.get("status").toString();
				if (bStatus.equals("F")) {
					Status eStatus = new Status();
					eStatus.setMessage(result.get("message").toString());
					deliveryResp.setStatus(eStatus);
				}
				deliveryResp.getBookings().add(bookResp);
			}
			deliveryResp.setDeliveryId(delivery.getDeliveryId());
			respDeliveries.add(deliveryResp);
		}
		return respDeliveries;
	}

	public List<Delivery> booking(final Request request, Map<String, Character> itemInstallMap) {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		List<Delivery> respDeliveries = new ArrayList<Delivery>(request.getDeliveries().size());
		StatusResponse status = new StatusResponse();
		status.setCode("");
		status.setMessage("");

		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.ARRAY));
		declaredParameters.add(new SqlParameter(Types.DATE));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.INTEGER));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));

		declaredParameters.add(new SqlOutParameter("bookingId", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));

		StringBuilder cityAreaQuery = new StringBuilder();
		final Map<String, Integer> cityMap = new HashMap<String, Integer>();
		final Map<String, Integer> areaMap = new HashMap<String, Integer>();
		final Map<String, Integer> regionMap = new HashMap<String, Integer>();
		Set<String> cityNames = new HashSet<String>();
		for (Delivery delivery : request.getDeliveries()) {
			String city = delivery.getDeliveryAddress().getCity().toUpperCase();
			String area = delivery.getDeliveryAddress().getArea();
			if (area != null) {
				if (cityAreaQuery.length() > 0) {
					cityAreaQuery.append(" OR ");
				} else {
					cityAreaQuery.append(" WHERE ");
				}
				cityAreaQuery.append(" (UPPER(CITY_NAME) = '").append(city).append("'");
				cityAreaQuery.append(" AND UPPER(AREA_NAME) = '").append(area.toUpperCase()).append("') ");
			} else {
				cityNames.add(city);
			}
			delivery.getDeliveryAddress().getCity();
		}
		if (cityAreaQuery.length() > 0) {
			cityAreaQuery.insert(0, "SELECT UPPER(CITY_NAME), UPPER(AREA_NAME), CITY_ID, AREA_ID, REGION_ID FROM XX_DLVRY_REGIONS_V");
			jdbcTemplate.query(cityAreaQuery.toString(), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					String city = rs.getString(1);
					cityMap.put(city, rs.getInt(3));
					areaMap.put(city + "~" + rs.getString(2), rs.getInt(4));
					regionMap.put(city + "~" + rs.getString(2), rs.getInt(5));
				}
			});
		}
		if (!cityNames.isEmpty()) {
			jdbcTemplate.query("SELECT DISTINCT CITY_NAME, CITY_ID, REGION_ID FROM XX_DLVRY_REGIONS_V WHERE UPPER(CITY_NAME) IN (:cityNames)", new RowCallbackHandler() {

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					cityMap.put(rs.getString(1), rs.getInt(2));
					regionMap.put(rs.getString(1), rs.getInt(3));
				}
			});
		}

		for (final Delivery delivery : request.getDeliveries()) {
			Delivery deliveryResp = new Delivery();
			deliveryResp.setBookings(new ArrayList<Booking>());
			for (final Booking booking : delivery.getBookings()) {
				Booking bookResp = new Booking();
				final Object[][] objArr = new Object[booking.getItems().size()][4];
				int inc = 0;
				for (Item item : booking.getItems()) {
					objArr[inc][0] = item.getLineNo();
					objArr[inc][1] = item.getItemCode();
					objArr[inc][2] = item.getOrderQty();
					objArr[inc++][3] = itemInstallMap.get(item.getItemCode());
				}
				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						String city = delivery.getDeliveryAddress().getCity().toUpperCase();
						String area = delivery.getDeliveryAddress().getArea();
						CallableStatement stmt = con.prepareCall("{ CALL XXHDB_CORE_PKG.BOOK_ORDER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
						stmt.setInt(1, booking.getReservationId());
						stmt.setString(2, request.getOrderNo());
						stmt.setInt(3, booking.getGroupId());
						stmt.setArray(4, ((oracle.jdbc.driver.OracleConnection) con).createOracleArray("XXHDB_TBL_TYPE", objArr));
						stmt.setDate(5, new java.sql.Date(booking.getDate().getTime()));
						stmt.setString(6, booking.getWindowCode());
						stmt.setInt(7, booking.getSlots());
						stmt.setInt(8,  area == null ? regionMap.get(city) : regionMap.get(city + "~" + area.toUpperCase()));
						stmt.setInt(9, cityMap.get(city));
						stmt.setInt(10, area == null ? 0 : areaMap.get(city + "~" + area.toUpperCase()));

						stmt.setString(11, delivery.getDeliveryAddress().getFirstName());
						stmt.setString(12, delivery.getDeliveryAddress().getAddress1());
						stmt.setString(13, delivery.getDeliveryAddress().getPhoneNo());
						stmt.setString(14, delivery.getDeliveryAddress().getLat());
						stmt.setString(15, delivery.getDeliveryAddress().getLng());
						stmt.registerOutParameter(16, Types.NUMERIC);
						stmt.registerOutParameter(17, Types.VARCHAR);
						stmt.registerOutParameter(18, Types.VARCHAR);
						return stmt;
					}
				}, declaredParameters);
				bookResp.setGroupId(booking.getGroupId());
				bookResp.setReservationId(booking.getReservationId());

				String bStatus = result.get("status").toString();
				if (bStatus.equals("F")) {
					Status eStatus = new Status();
					eStatus.setMessage(result.get("message").toString());
					deliveryResp.setStatus(eStatus);
				}
				deliveryResp.getBookings().add(bookResp);
			}
			deliveryResp.setDeliveryId(delivery.getDeliveryId());
			respDeliveries.add(deliveryResp);
		}
		return respDeliveries;
	}

	public boolean hasACInstallMismatch(Collection<String> items) {

		return jdbcTemplate.query(
				"SELECT COUNT(*), 'AC' AS TYPE FROM ITEM_MASTER WHERE DEPT = 402 AND ITEM IN (:itemCodes) AND NOT EXISTS (SELECT 1 FROM XX_DLVRY_AC_ITEMS WHERE ITEM_CODE = ITEM) " + " UNION "
						+ " SELECT COUNT(*), 'INSTALL' AS TYPE FROM XX_DLVRY_AC_INS_ITEMS_V WHERE ITEM IN (:itemCodes)",
				Collections.singletonMap("itemCodes", items), new ResultSetExtractor<Boolean>() {

					@Override
					public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
						int acCnt = 0, installCnt = 0;
						while (rs.next()) {
							if (rs.getString(2).equals("AC")) {
								acCnt = rs.getInt(1);
							} else {
								installCnt = rs.getInt(1);
							}
						}
						return installCnt != 0 && acCnt != 0 && acCnt != installCnt;
					}
				});
	}

	public void getOrderDetail(final Request request) {
		jdbcTemplate.query(ORDER_DETAIL_QUERY, Collections.singletonMap("ordNo", request.getOrderNo()), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(rs.isFirst()) {
					request.setCountry(rs.getString("DELIVER_COUNTRY"));
					request.setCreateDate(dateFormat.format(rs.getTimestamp("CREATE_DATETIME")));
					CustomerDetail customer = new CustomerDetail();
					customer.setFirstName(rs.getString("DELIVER_FIRST_NAME"));
					customer.setLastName(rs.getString("DELIVER_LAST_NAME"));
					customer.setCustomerId(rs.getString("CUST_ID"));
					customer.setPhoneNo(rs.getString("CUST_PHONE_NO"));
					customer.setLanguage(rs.getInt("CUSTOMER_LANG"));
					Delivery delivery = null;
					if (request.getDeliveries() != null && !request.getDeliveries().isEmpty()) {
						delivery = request.getDeliveries().get(0);
					} else {						
						delivery = new Delivery();
					}
					delivery.setDeliveryId(1);
					delivery.setDeliveryType("S");
					DeliveryAddress address = new DeliveryAddress();
					address.setFirstName(customer.getFirstName());
					address.setLastName(customer.getLastName());
					address.setPhoneNo(customer.getPhoneNo());
					address.setCountry(request.getCountry());
					address.setProvince(rs.getString("DELIVER_CITY"));
					address.setCity(rs.getString("DELIVER_CITY"));
					address.setStreetName(rs.getString("DELIVER_ADD_1"));
					address.setArea(rs.getString("DELIVER_ADD_2"));
					delivery.setDeliveryAddress(address);
					request.setDeliveries(Collections.singletonList(delivery));
					if (request.getDeliveries().get(0).getBookings() != null && !request.getDeliveries().get(0).getBookings().isEmpty()) {
						request.getDeliveries().get(0).getBookings().get(0).setItems(new ArrayList<Item>());
					}
					delivery.setItems(new ArrayList<Item>());
				}
				Item item = new Item();
				item.setItemCode(rs.getString("ITEM"));
				item.setLineNo(rs.getInt("LINE_NO"));
				item.setShippingClassification(rs.getString("SHIP_CLASSIFICATION"));
				item.setIsBackOrder("Y".equals(rs.getString("BACKORDER_IND")));
				item.setOrderQty(rs.getInt("QTY_ORDERED_SUOM"));
				item.setUnitRetailPrice(rs.getString("UNIT_RETAIL"));
				item.setRetailCurrency(rs.getString("RETAIL_CURR"));
				item.setDept(rs.getString("DEPT"));
				item.setGroup("");
				item.setClasss(rs.getString("CLASS"));
				item.setSubClass(rs.getString("SUBCLASS"));
				item.setSourceLocation(rs.getString("SOURCE_LOC"));
				item.setFulfillmentLocation(rs.getString("FULFILL_LOC"));
				if (request.getDeliveries().get(0).getBookings() != null && !request.getDeliveries().get(0).getBookings().isEmpty()) {
					request.getDeliveries().get(0).getBookings().get(0).setItems(new ArrayList<Item>());
					request.getDeliveries().get(0).getBookings().get(0).getItems().add(item);
				}
				request.getDeliveries().get(0).getItems().add(item);
			}
		});
	}

	public boolean updateRequestLocation(Request request) throws Exception {
		boolean onlyDeliveryCharge = false;
		String classification = "SMALL";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("item", request.getDeliveries().get(0).getItems().get(0).getItemCode());
		if (request.getRequestorId().toString().startsWith("1")) {
			params.put("vLoc", "10000");
		} else if (request.getRequestorId().toString().startsWith("2")) {
			params.put("vLoc", "20000");
		} else if (request.getRequestorId().toString().startsWith("3")) {
			params.put("vLoc", "30000");
		} else if (request.getRequestorId().toString().startsWith("4")) {
			params.put("vLoc", "10000");
		}
		try {			
			classification = jdbcTemplate.queryForObject("SELECT UPPER(NVL(VARCHAR2_7,'N')) FROM ITEM_LOC_CFA_EXT XC WHERE XC.ITEM = :item AND GROUP_ID = 22 AND LOC = :vLoc", params, String.class);
			if (classification.equals("Y")) {
				classification = "BIG";
			} else {
				classification = "SMALL";
			}
		} catch (EmptyResultDataAccessException e) {
			_LOG.warn("Ship Classification not found", e);
			classification = "SMALL";
		}
		params.clear();
		params.put("requestorId", request.getRequestorId());
		params.put("classification", classification);
		List<String> cities = new ArrayList<String>();
		List<String> areas = new ArrayList<String>();
		List<String> itemIds = new ArrayList<String>();
		for (Delivery delivery : request.getDeliveries()) {
			cities.add(delivery.getDeliveryAddress().getCity().toUpperCase());
			String area = delivery.getDeliveryAddress().getArea();
			if (area != null) {				
				areas.add(area.toUpperCase());
			}
			for (Item item :  delivery.getItems()) {
				itemIds.add(item.getItemCode());
			}
		}
		params.put("cities", cities);
		params.put("areas", areas);
		
		if ("SMALL".equals(classification)) {
			onlyDeliveryCharge = true;
		}
		cities.add("ALL");
		
		final List<Integer> storeIds = new ArrayList<Integer>();
		final List<Integer> whIds = new ArrayList<Integer>();
		Map<String, List<OmsCoFulfillDetail>> matrixMap = jdbcTemplate.query("SELECT H.CUSTOMER_CITY, D.PRIORITY, D.LOCATION_TYPE, D.LOCATION, D.DELIVERY_FROM_LOC FROM OMS_FULFILL_MATRIX_EXT_HEAD H, OMS_FULFILL_MATRIX_EXT_DETAIL D WHERE H.COMBINATION_ID = D.COMBINATION_ID AND H.REQUESTOR_ID = :requestorId and H.CUSTOMER_CITY IN (:cities) and H.SHIP_CLASSIFICATION = :classification AND H.MODE_OF_DELIVERY = 'S' ORDER BY PRIORITY", params, new ResultSetExtractor<Map<String, List<OmsCoFulfillDetail>>>() {

			@Override
			public Map<String, List<OmsCoFulfillDetail>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, List<OmsCoFulfillDetail>> matrixMap = new HashMap<String, List<OmsCoFulfillDetail>>();
				while(rs.next()) {
					String city = rs.getString(1);
					List<OmsCoFulfillDetail> cityEntries = matrixMap.get(city);
					if (cityEntries == null) {
						cityEntries = new ArrayList<OmsCoFulfillDetail>();
						matrixMap.put(city, cityEntries);
					}
					String locType = rs.getString(3);
					int loc = rs.getInt(4);
					if ("WH".equals(locType)) {
						whIds.add(loc);
					} else {
						storeIds.add(loc);
					}
					OmsCoFulfillDetail fulfil = new OmsCoFulfillDetail();
					fulfil.setSourceLoc(BigDecimal.valueOf(loc));
					fulfil.setSourceLocType(locType);
					fulfil.setFulfillLoc(rs.getBigDecimal(5));
					cityEntries.add(fulfil);
				}
				return matrixMap;
			}
		});
		params.clear();
		params.put("itemIds", itemIds);
		Map<String, Integer> whAvailMap = null;
		if (!whIds.isEmpty()) {			
			params.put("locIds", whIds);
			whAvailMap = jdbcTemplate.query("SELECT V.ITEM, SUM(V.AVAIL_QTY), W.WH FROM XX_OMS_INVAVAIL_V V, WH W WHERE W.WH = V.LOC AND W.WH IN (:locIds) AND V.ITEM IN (:itemIds) GROUP BY V.ITEM, W.WH", params, new ResultSetExtractor<Map<String, Integer>>() {

				@Override
				public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Integer> whAvailMap = new HashMap<String, Integer>();
					while(rs.next()) {
						whAvailMap.put(rs.getString(1) + "~" + rs.getInt(3), rs.getInt(2));
					}
					return whAvailMap;
				}
			});
		}

		Map<String, Integer> stAvailMap = null;
		if (!storeIds.isEmpty()) {			
			params.put("locIds", storeIds);
			stAvailMap = jdbcTemplate.query("SELECT ITEM_ID, STORE_ID, SUM(AVAIL_TO_SELL) FROM XX_OMS_INV@SIMDB WHERE SOURCE = 'ORPOS' AND ITEM_ID IN (:itemIds) AND STORE_ID IN (:locIds) GROUP BY ITEM_ID, STORE_ID", params, new ResultSetExtractor<Map<String, Integer>>() {

				@Override
				public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Integer> stAvailMap = new HashMap<String, Integer>();
					while(rs.next()) {
						stAvailMap.put(rs.getString(1) + "~" + rs.getInt(2), rs.getInt(3));
					}
					return stAvailMap;
				}
			});
		}

		List<String> invIndMap = null;
		if (!itemIds.isEmpty()) {
			invIndMap = jdbcTemplate.query("SELECT ITEM FROM ITEM_MASTER WHERE ITEM IN (:itemIds) AND INVENTORY_IND = 'N'", params, new ResultSetExtractor<List<String>>() {

				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> invIndList = new ArrayList<String>();
					while(rs.next()) {
						invIndList.add(rs.getString(1));
					}
					return invIndList;
				}
			});
		}

		for (Delivery delivery : request.getDeliveries()) {
			String city = delivery.getDeliveryAddress().getCity().toUpperCase();
			List<OmsCoFulfillDetail> fulfilments = matrixMap.containsKey(city) ? matrixMap.get(city) : matrixMap.get("ALL");
			List<Item> nonInvItems = new ArrayList<Item>();
			for (Item item : delivery.getItems()) {
				if (invIndMap != null && invIndMap.contains(item.getItemCode())) {
					nonInvItems.add(item);
				} else {
					int pendingQty = item.getOrderQty();
					for (OmsCoFulfillDetail fulfil : fulfilments) {
						if (fulfil.getSourceLocType().equals("WH")) {
							Integer qty = whAvailMap.get(item.getItemCode() + "~" + fulfil.getSourceLoc());
							pendingQty -= Math.max(0, (qty != null ? qty : 0));
						} else {
							Integer qty = stAvailMap.get(item.getItemCode() + "~" + fulfil.getSourceLoc());
							pendingQty -= Math.max(0, (qty != null ? qty : 0));
						}
						if (pendingQty <= 0) {
							item.setSourceLocation(fulfil.getSourceLoc().toPlainString());
							item.setFulfillmentLocation(fulfil.getFulfillLoc().toPlainString());
							break;
						}
					}
				}
			}
			if (!nonInvItems.isEmpty() && fulfilments != null) {
				for (Item item : nonInvItems) {
					item.setSourceLocation(fulfilments.get(0).getSourceLoc().toPlainString());
					item.setFulfillmentLocation(fulfilments.get(0).getFulfillLoc().toPlainString());
				}
			}
		}
		return onlyDeliveryCharge;
	}

	public void getDeliveryCharges(final Request request, Response response) {
		List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
		try {
			String expressItemSKU = getExpressItemSKU();
			Delivery delivery = request.getDeliveries().get(0);
			for(Item item : delivery.getItems()) {
				if (!item.getItemCode().equals(expressItemSKU)) {
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("custOrdNo", request.getOrderNo());
					param.addValue("requestorId", request.getRequestorId());
					param.addValue("countrty", request.getCountry());
					param.addValue("countrtyName", getCountryName(request.getCountry()));
					param.addValue("region", null);
					param.addValue("regionName", delivery.getDeliveryAddress().getProvince());
					param.addValue("city", null);
					param.addValue("cityName", delivery.getDeliveryAddress().getCity());
					param.addValue("area", null);
					param.addValue("areaName", delivery.getDeliveryAddress().getArea());
					param.addValue("floor", null);
					param.addValue("item", item.getItemCode());
					param.addValue("shipLoc", item.getFulfillmentLocation());
					param.addValue("qty", item.getOrderQty());
					params.add(param);
				}
			}
			
			jdbcTemplate.batchUpdate("INSERT INTO XX_SC_CHARGE_INT(SC_INT_ID, ORDER_NUMBER, REQ_ID, COUNTRY, COUNTRY_NAME, REGION, REGION_NAME, CITY, CITY_NAME, AREA, AREA_NAME, FLOOR, ITEM, QTY, SHIPPING_LOC) VALUES(OMS_CANCEL_ID_SEQ.NEXTVAL, :custOrdNo, :requestorId, :countrty, :countrtyName, :region, :regionName, :city, :cityName, :area, :areaName, :floor, :item, :qty, :shipLoc)", params.toArray(new SqlParameterSource[params.size()]));
			int deliveryCharge = jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
				
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{? = call XX_SHIPPING_CHARGE.CALCULATESHIPPINGCHARGES(?) }" );
					stmt.registerOutParameter(1, Types.NUMERIC);
					stmt.setString(2, request.getOrderNo());
			        return stmt;
				}
			}, new CallableStatementCallback<Integer>() {
	
				@Override
				public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					cs.execute();
					return cs.getInt(1);
				}
			} );
			response.getStatus().setDeliveryCharge(deliveryCharge);
		} catch (Exception e) {
			_LOG.error("Error while loading the delivery charge", e);
		}
	}

	private static String getCountryName(String country) {
		String cn = country.toUpperCase();
		if ("EG".equals(cn)) {
			return "Egypt";
		} else if ("SA".equals(cn)) {
			return "Saudi Arabia";
		} else if ("OM".equals(cn)) {
			return "Oman";
		} else if ("BH".equals(cn)) {
			return "Bahrain";
		}
		return "";
	}
}
