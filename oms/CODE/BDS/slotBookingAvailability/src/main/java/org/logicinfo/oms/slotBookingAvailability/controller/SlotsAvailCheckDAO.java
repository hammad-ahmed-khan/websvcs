package org.logicinfo.oms.slotBookingAvailability.controller;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oracle.jdbc.OracleTypes;

@Transactional
@Repository("SlotsAvailDAO")
public class SlotsAvailCheckDAO implements SlotsAvailDAO {

	private static final Logger log = Logger.getLogger(SlotsAvailCheckDAO.class);

	SlotsAvailCheckDAO() {
		log.info(" Default Constructor SlotsAvailCheckDAO is Executed");
	}

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<SlotsAvailabilityResponse> getResponse(final String itemQuery, List<Deliveries> deliveries, int i, int size, String expressItemSKU) throws SQLException {
		List<SlotsAvailabilityResponse> slotsRespList = new ArrayList<SlotsAvailabilityResponse>();
		log.info("-----------Inside DAO------------------" + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		List<SlotsAvailabilityResponse> slotsRespListt = new ArrayList<SlotsAvailabilityResponse>();
		boolean isExpressDlvry = false;
		log.info("Before Calling Package XXHDB_CORE_PKG.get_schedule " + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		final String packageCallStmt = " { call XXHDB_CORE_PKG.get_schedule(?, ?, ?, ?, ?) }";
		try {

			StatusResponse statusResponse = new StatusResponse();
			Status status = new Status();
			status.setSuccess(true);
			status.setCode("");
			status.setMessage("");
			SlotsAvailabilityResponse slotsResp = null;
			List<DeliveriesResponse> delv1 = null;
			Groups grp = null;
			List<Groups> groups = null;

			List<ItemsResponse> items = null;
			ItemsResponse itemsResponse = null;
			Windows windows = null;
			WindowsResponse windowsResponse = null;
			List<WindowsResponse> windowsResponseList = null;
			List<Windows> windowsList = null;
			Dates dates = null;
			List<Dates> datesList = null;
			List<Items> itemCode = null;
			int fTime = 0;
			int tTime = 0;
			Map<BigDecimal, Groups> groupMap = new ConcurrentHashMap<BigDecimal, Groups>();
			Map<BigDecimal, List<ItemsResponse>> itemMap = new ConcurrentHashMap<BigDecimal, List<ItemsResponse>>();
			Map<BigDecimal, List<Dates>> dateMap = new ConcurrentHashMap<BigDecimal, List<Dates>>();
			Map<GroupTemp, List<WindowsResponse>> windowsMap = new ConcurrentHashMap<GroupTemp, List<WindowsResponse>>();

			// for (int i = 0; i < deliveries.size(); i++) {

			items = new ArrayList<ItemsResponse>();
			itemsResponse = new ItemsResponse();
			windows = new Windows();
			windowsResponse = new WindowsResponse();
			windowsList = new ArrayList<Windows>();
			dates = new Dates();
			datesList = new ArrayList<Dates>();
			windowsResponseList = new ArrayList<WindowsResponse>();
			itemCode = deliveries.get(i).getItems();
			for (Items item : itemCode) {
				if (item.getItemCode().equals(expressItemSKU)) {
					isExpressDlvry = true;
					break;
				}
			}
			BigDecimal bd = new BigDecimal(999);
			slotsResp = new SlotsAvailabilityResponse();
			delv1 = new ArrayList<DeliveriesResponse>();
			DeliveriesResponse delv = new DeliveriesResponse();
			groups = new ArrayList<Groups>();
			grp = new Groups();
			String selectQuery = "";
			final String deliveryCity = deliveries.get(i).getDeliveryAddress().getCity().toUpperCase().replace("'", "");
			String deliveryArea = deliveries.get(i).getDeliveryAddress().getArea();
			if (!isExpressDlvry && deliveryArea != null) {
				deliveryArea = deliveryArea.toUpperCase();
			}
			/*
			 * selectQuery =
			 * "select b.city_id, c.area_id from xx_dlvry_cities b, xx_dlvry_areas c where c.city_id = b.city_id and upper(b.city_name) ='"
			 * + deliveryCity + "'and upper(c.area_name) = '" + deliveryArea + "'";
			 */
			selectQuery = "select count(CITY_ID) from XX_DLVRY_REGIONS_V where upper(city_name) ='" + deliveryCity + "'";
			if (!isExpressDlvry) {
				selectQuery += " and upper(area_name) = '" + deliveryArea + "'";
			}
			int ctyCnt = jdbcTemplate.queryForObject(selectQuery, (SqlParameterSource) null, Integer.class);
			if (ctyCnt < 1) {

				status.setSuccess(false);
				status.setCode("400");
				status.setMessage("Invalid City/Area");
				slotsResp.setStatus(status);
				slotsRespList.add(slotsResp);

			} else {
				List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
				declaredParameters.add(new SqlParameter(Types.VARCHAR));
				declaredParameters.add(new SqlParameter(Types.VARCHAR));
				declaredParameters.add(new SqlOutParameter("groups", OracleTypes.CURSOR));
				declaredParameters.add(new SqlOutParameter("items", OracleTypes.CURSOR));
				declaredParameters.add(new SqlOutParameter("dates", OracleTypes.CURSOR));
				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement stmt = con.prepareCall(packageCallStmt);
						stmt.setString(1, itemQuery);
						stmt.setString(2, deliveryCity);
						stmt.registerOutParameter(3, OracleTypes.CURSOR);
						stmt.registerOutParameter(4, OracleTypes.CURSOR);
						stmt.registerOutParameter(5, OracleTypes.CURSOR);
						return stmt;
					}
				}, declaredParameters);

				List<Map<String, Object>> groupList = (List<Map<String, Object>>) result.get("groups");
				delv.setDeliveryId(deliveries.get(i).getDeliveryId());
				log.info("After Calling Package XXHDB_CORE_PKG.get_schedule " + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

				boolean listEmpty = true;

				// Group List of function
				for (Map<String, Object> gMap : groupList) {
					listEmpty = false;
					grp = new Groups();
					// Charan - Commenting for BDS Merging Calendar
					BigDecimal gid = bd;
					// BigDecimal gid =
					// (BigDecimal)resultsetGroupList.getObject(1);
					// System.out.println("***GID***"+gid);
					String gstatus = (String) gMap.get("STATUS");
					if (gid.signum() < 0 || gstatus.equals("F")) {
						// continue;
						return null;
					} else {
						// Charan - Commenting for BDS Merging Calendar
						// grp.setGroupId((BigDecimal)
						// resultsetGroupList.getObject(1));
						grp.setGroupId(bd);
						grp.setGroupName((String) gMap.get("GROUP_NAME"));
						grp.setSlots((BigDecimal) gMap.get("SLOTS"));
						// grp.setStatus((String)
						// resultsetGroupList.getObject(5));
						statusResponse.setCode("");// (String)
													// resultsetGroupList.getObject(5)
						statusResponse.setMessage("");
						grp.setRegionID((BigDecimal) gMap.get("REGION_ID"));
						grp.setRegionName((String) gMap.get("REGION_NAME"));
						grp.setStatus(statusResponse);
						groups.add(grp);
						// Charan - Commenting for BDS Merging Calendar
						// groupMap.put((BigDecimal)
						// resultsetGroupList.getObject(1), grp);
						groupMap.put(bd, grp);
					}
				}
				if (listEmpty) {
					log.info("Group list is empty " + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}

				List<Map<String, Object>> itemListList = (List<Map<String, Object>>) result.get("items");

				List<ItemsResponse> itemresponseList = null;

				int count = 0;
				boolean itemEmpty = true;
				for (Map<String, Object> iMap : itemListList) {
					itemEmpty = false;
					// Charan - Commenting for BDS Merging Calendar
					BigDecimal gid = bd;
					// BigDecimal gid =
					// (BigDecimal)resultsetItemList.getObject(3);
					log.info("**************************************" + gid + "****" + (++count) + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					if (gid.signum() < 0) {
						continue;
					} else {
						itemsResponse = new ItemsResponse();
						itemsResponse.setLineNo((BigDecimal) iMap.get("LINE_NO"));
						itemsResponse.setItemCode((String) iMap.get("ITEM"));
						// grp.setGroupId((BigDecimal)
						// resultsetItemList.getObject(3));
						itemsResponse.setOrderQty((BigDecimal) iMap.get("QTY"));

						items.add(itemsResponse);

						itemresponseList = new ArrayList<ItemsResponse>();
						itemresponseList.add(itemsResponse);

					}
					// System.out.println("***********Item set
					// size:*********"+itemresponseList.size());
					// Check the groupId is present in the Itemmap List , If
					// exist append to it
					/// other wise we need create new group Id
					// itemresponseList.add(itemsResponse);

					if (itemMap.containsKey(gid) == Boolean.FALSE) {
						itemMap.put(gid, itemresponseList);

					} else {
						List<ItemsResponse> perviousItemList = itemMap.get(gid);
						perviousItemList.add(itemsResponse);
						itemMap.put(gid, perviousItemList);
					}

				}
				if (itemEmpty) {
					log.info("Item List is empty " + "SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}

				for (BigDecimal mee : groupMap.keySet()) {
					log.info("***KKEEEEEEEYY***" + mee + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					log.info(groupMap.get(mee).getGroupId() + ":" + groupMap.get(mee).getGroupName() + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

				}
				//
				boolean empty = true;

				List<Map<String, Object>> dateList = (List<Map<String, Object>>) result.get("dates");
				log.info(" Fetching Delivery Date Details_After getting results from package -:" + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				for (Map<String, Object> dMap : dateList) {
					// BigDecimal gid =
					// (BigDecimal)resultsetDeliveryDateList.getObject(2);
					// Charan - Commenting for BDS Merging Calendar
					BigDecimal gid = bd;
					// BigDecimal gid = new
					// BigDecimal((String)resultsetDeliveryDateList.getObject(2));
					empty = false;
					if (gid.signum() < 0) {
						continue;
					} else {

						windowsResponse = new WindowsResponse();

						dates = new Dates();
						// System.out.println("Dates
						// output:"+resultsetDeliveryDateList.getObject(3));
						// grp.setGroupId((BigDecimal)
						// resultsetDeliveryDateList.getObject(2));
						Timestamp ts = (Timestamp) dMap.get("DELIVERY_DATE");
						dates.setDate(new Date(ts.getTime()));
						windowsResponse.setDates(new Date(ts.getTime()));

						windowsResponse.setCode((String) dMap.get("WINDOW"));
						String frTime = dMap.get("TIME_EN").toString();

						fTime = Integer.parseInt(frTime.substring(0, 2));
						tTime = Integer.parseInt(frTime.substring(11, 13));

						if ((frTime.substring(6, 8)).equals("PM") && fTime < 12) {
							fTime = fTime + 12;
						}
						if ((frTime.substring(17)).equals("PM") && tTime < 12) {
							tTime = tTime + 12;
						}
						if (windowsResponse.getCode().equals("W5")) {
							tTime = 00;
						}
						if (windowsResponse.getCode().equals("W6")) {
							fTime = 00;
							tTime = 03;
						}
						windowsResponse.setFromTime(new DecimalFormat("00").format(fTime));
						windowsResponse.setToTime(new DecimalFormat("00").format(tTime));

						Character available = dMap.get("AVAILABLE").toString().charAt(0);

						if (available.equals('Y')) {
							windowsResponse.setAvailable((true));
						} else {
							windowsResponse.setAvailable((false));
						}
						// windowsList.add(windows);
						// dates.setWindows(windowsList);

						datesList.add(dates);
						windowsResponseList.add(windowsResponse);

						List<WindowsResponse> windowsResponsetemp = new ArrayList<WindowsResponse>();

						windowsResponsetemp.add(windowsResponse);

						// If new group Id comes it into dateMap other wise and
						// it
						// exists already append need to be done
						List<Dates> deliveryDateList = new ArrayList<Dates>();

						GroupTemp gtemp = new GroupTemp();
						gtemp.setGroupId(gid);
						gtemp.setWindowsDate(toDate(dates.getDate()));

						if (windowsMap.containsKey(gtemp) == Boolean.FALSE) {
							windowsMap.put(gtemp, windowsResponsetemp);
						} else {
							List<WindowsResponse> previousWindowsList = windowsMap.get(gtemp);
							previousWindowsList.add(windowsResponse);
							windowsMap.put(gtemp, previousWindowsList);
						}

						if (dateMap.containsKey(gid) == Boolean.FALSE) {
							deliveryDateList.add(dates);
							dateMap.put(gid, deliveryDateList);
						} else {
							List<Dates> previousDateList = dateMap.get(gid);
							previousDateList.add(dates);
							dateMap.put(gid, previousDateList);
						}
					}
				}
				if (empty) {
					log.info("Delivery Date details is empty" + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}


				for (GroupTemp gtemp : windowsMap.keySet()) {
				}

				// Get the group If from Group map for each group Id key find
				// the Item List and Delivery Date List

				groups = new ArrayList<Groups>();

				windowsList = new ArrayList<Windows>();
				int k = 0;

				for (BigDecimal me : groupMap.keySet()) {


					Groups groupsList = groupMap.get(me);

					List<ItemsResponse> itemResponseList = itemMap.get(me);

					List<Dates> deliverydateteList = dateMap.get(bd); // (me)
					// System.out.println("deliveryDateList:::" +
					// deliverydateteList);

					List<WindowsResponse> windowsRespsList = null;
					/*
					 * for(Date d:windowsMap.keySet()){ windowsRespsList = windowsMap.get(d);
					 * System.out.println("Windows size " +windowsRespsList.size()); }
					 */
					grp = new Groups();
					statusResponse = new StatusResponse();
					// Charan - Commenting for BDS Merging calendar Changes
					// grp.setGroupId(groupsList.getGroupId());
					grp.setGroupId(bd);
					grp.setGroupName(groupsList.getGroupName());
					grp.setSlots(groupsList.getSlots());
					grp.setRegionID(groupsList.getRegionID());
					grp.setRegionName(groupsList.getRegionName());
					statusResponse.setCode(groupsList.getStatus().getCode());
					statusResponse.setMessage("");
					grp.setStatus(statusResponse);
					// groups.add(grp);

					items = new ArrayList<ItemsResponse>();

					for (ItemsResponse itr : itemResponseList) {
						itemsResponse = new ItemsResponse();
						itemsResponse.setLineNo(itr.getLineNo());
						itemsResponse.setItemCode(itr.getItemCode());
						// Charan - Commenting for BDS Merging calendar Changes
						// grp.setGroupId(groupsList.getGroupId());
						grp.setGroupId(bd);
						itemsResponse.setOrderQty(itr.getOrderQty());
						items.add(itemsResponse);

					}
					// For a single group Id , addding set of items
					grp.setItems(items);
					// groups.add(grp);


					datesList = new ArrayList<Dates>();
					List<GroupTemp> grouptempList = new ArrayList<GroupTemp>();
					for (Dates deliveryDate : deliverydateteList) {

						dates = new Dates();
						windows = new Windows();
						// Charan - Commenting for BDS Merging calendar Changes
						// grp.setGroupId(groupsList.getGroupId());
						grp.setGroupId(bd);
						dates.setDate(toDate(deliveryDate.getDate()));

						GroupTemp gtemp = new GroupTemp();
						// Charan
						// gtemp.setGroupId(grp.getGroupId());
						gtemp.setGroupId(bd);
						gtemp.setWindowsDate(toDate(deliveryDate.getDate()));

						if (grouptempList.size() == 0) {

						} else {

							if (grouptempList.contains(gtemp) == Boolean.TRUE)
								continue;
						}
						grouptempList.add(gtemp);

						windowsRespsList = windowsMap.get(gtemp);

						windowsList = new ArrayList<Windows>();

						for (WindowsResponse wr : windowsRespsList) {
							windows = new Windows();
							windows.setCode(wr.getCode());
							windows.setFromTime(wr.getFromTime());
							windows.setToTime(wr.getToTime());
							if (wr.isAvailable()) {
								windows.setAvailable(wr.isAvailable());
							} else {
								windows.setAvailable(wr.isAvailable());
							}

							windowsList.add(windows);
						}
						dates.setWindows(windowsList);
						datesList.add(dates);

					}

					// set of delivery dates of a particular group Id

					grp.setDates(datesList);

					// Adding each group

					groups.add(grp);

				} // end of groupid key set

				delv.setGroups(groups);

				delv1.add(delv);

				// }

				slotsResp.setStatus(status);
				slotsResp.setDeliveries(delv1);
				slotsRespList.add(slotsResp);
				// conn.close();

			}
		} catch (Exception e) {
			log.error("Exception" + e);
			SlotsAvailabilityResponse slotsResp = new SlotsAvailabilityResponse();
			Status status = new Status();
			status.setSuccess(false);
			status.setCode("500");
			status.setMessage("Error -: " + e);
			slotsResp.setStatus(status);
			slotsResp.setDeliveries(null);
			slotsRespList.add(slotsResp);

		}
		slotsRespListt.addAll(slotsRespList);
		if (i == (size - 1)) {
			slotsRespList = new ArrayList<SlotsAvailabilityResponse>();
			return slotsRespListt;
		}

		return slotsRespList;

	}

	@Override
	public Long saveRequest(SlotsAvailCheckRequest sub) throws Exception {

		String city = "";
		String area = "";
		Long seq = null;
		try {
			city = sub.getDeliveries().get(0).getDeliveryAddress().getCity().toUpperCase().replace("'", "");
			area = sub.getDeliveries().get(0).getDeliveryAddress().getArea().toUpperCase().replace("'", "");
			log.info("inside dao, Inserting the Request body for the Order Number" + sub.getOrderNo() + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			KeyHolder seqHolder = new GeneratedKeyHolder();

			String keys[] = { "SEQUENCE_NO" };
			Gson gson = new Gson();
			String gsonString = gson.toJson(sub);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("orderNo", sub.getOrderNo());
			params.addValue("request", gsonString);
			params.addValue("status", "N");
			params.addValue("ordInd", 0);
			params.addValue("source", "SAC");
			params.addValue("city", city);
			params.addValue("area", area);

			jdbcTemplate.update(
					"INSERT INTO OMS_ORDER_BOOKING_HEAD_INFO (SEQUENCE_NO, ORDER_NO,REQUEST,STATUS,ORD_IND,SOURCE,CREATE_DATETIME,CITY,AREA) VALUES(XX_BOOK_SEQ.nextval, :orderNo, :request, :status, :ordInd, :source, SYSTIMESTAMP, :city, :area)",
					params, seqHolder, keys);
			seq = seqHolder.getKey().longValue();
		} catch (Exception exception) {
			log.error("Failed to insert Request Body while Inserting the Request for Order :"+ sub.getOrderNo() + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			exception.printStackTrace();
		}
		return seq;
	}

	@Override
	public void saveResponse(String OrderNumber, List<SlotsAvailabilityResponse> sAResposne, Long seqId) {
		log.info("inside dao, Inserting the Response body " + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		try {
			Gson gson = new Gson();
			String gsonString = gson.toJson(sAResposne);
			jdbcTemplate.getJdbcOperations().update("UPDATE OMS_ORDER_BOOKING_HEAD_INFO set response= ?,status='S',ord_ind=1, UPDATE_DATETIME = SYSTIMESTAMP where SEQUENCE_NO = ? ", gsonString,
					seqId);
		} catch (Exception exception) {
			log.error("Failed to insert Response.. " + " SAC :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			exception.printStackTrace();
		}
	}

	@Override
	public String getExpressItemSKU() {
		try {
			return jdbcTemplate.queryForObject("SELECT PARAMETER_VALUE FROM OMS_SYSTEM_PARAMETERS WHERE PARAMETER_NAME = 'SHIPPING_CHARGE' AND PARAMETER_COMMENT = 'Express Delivery'",
					Collections.<String, Object>emptyMap(), String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Date toDate(String yyyyMMdd) {
	    if (yyyyMMdd == null || yyyyMMdd.trim().isEmpty()) {
	        return null;
	    }
	 
	    String[] parts = yyyyMMdd.split("-");
	    int year  = Integer.parseInt(parts[0]);
	    int month = Integer.parseInt(parts[1]) - 1; // Calendar month is 0-based
	    int day   = Integer.parseInt(parts[2]);
	 
	    Calendar cal = Calendar.getInstance();
	    cal.clear();                // IMPORTANT
	    cal.set(year, month, day);  // date only
	 
	    return cal.getTime();
	}
}
