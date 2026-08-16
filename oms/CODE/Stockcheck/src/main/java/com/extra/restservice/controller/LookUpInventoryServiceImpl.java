package com.extra.restservice.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LookUpInventoryServiceImpl implements LookUpInventoryService {

	private static final Logger logger = LogManager.getLogger(LookUpInventoryServiceImpl.class.getName());

	@Autowired
	private NamedParameterJdbcTemplate omsJdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate dasJdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate simJdbcTemplate;

	public RealTimeInventoryResponse lookupInventory(RealTimeInventoryRequest request) {
		RealTimeInventoryResponse response = new RealTimeInventoryResponse();
		Status status = new Status();

		List<ItemAvailability> itemAvailablityList = new ArrayList<ItemAvailability>();

		List<Long> strInvLocations = new ArrayList<Long>();
		List<Long> whInvLocations = new ArrayList<Long>();
		Set<String> stockItems = new HashSet<String>();
		Set<String> futureItems = new HashSet<String>();
		Set<String> futureLocations = new HashSet<String>();
		Set<String> packItems = new HashSet<String>();
		Map<String, Locations> packLocations = new HashMap<String, Locations>();
		Map<String, String> packItmMap = new HashMap<String, String>();
		Map<Long, Locations> whLocMap = new HashMap<Long, Locations>();

		for (Locations loc : request.getLocations()) {
			if ("Y".equalsIgnoreCase(loc.getPhysicalStockInd())) {
				if ("Y".equalsIgnoreCase(loc.getPackItemInd())) {
					packLocations.put(loc.getCode() + (loc.getChannelId() != null ? loc.getChannelId().toPlainString() : ""), loc);
				} else {
					if ("S".equalsIgnoreCase(loc.getType())) {
						strInvLocations.add(Long.parseLong(loc.getCode()));
					} else {
						Long whLoc = Long.parseLong(loc.getCode() + loc.getChannelId().intValue());
						whInvLocations.add(whLoc);
						whLocMap.put(whLoc, loc);
					}
				}
			} else {
				futureLocations.add(loc.getCode() + (loc.getChannelId() != null ? loc.getChannelId().toPlainString() : ""));
			}
		}
		for (Items item : request.getItems()) {
			if ("Y".equalsIgnoreCase(item.getPhysicalStockInd())) {
				if ("Y".equalsIgnoreCase(item.getPackItemInd())) {
					packItems.add(item.getCode());
				} else {
					stockItems.add(item.getCode());
				}
			} else {
				futureItems.add(item.getCode());
			}
			packItmMap.put(item.getCode(), item.getPackItemInd());
		}

		if (!(strInvLocations.isEmpty() || stockItems.isEmpty())) {
			itemAvailablityList = checkStoreInventoryQuantity(strInvLocations, stockItems, itemAvailablityList, packItmMap);
		}
		if (!(whInvLocations.isEmpty() || stockItems.isEmpty())) {
			itemAvailablityList = checkWHInventoryQuantity(whInvLocations, stockItems, itemAvailablityList, whLocMap);
		}
		if (!(futureLocations.isEmpty() || futureItems.isEmpty())) {
			itemAvailablityList = checkFutureInventoryQuantity(futureLocations, futureItems, itemAvailablityList);
		}
		if (!(packLocations.isEmpty() || packItems.isEmpty())) {
			itemAvailablityList = getPackItemInventoryQuantity(packLocations, packItems, itemAvailablityList);
		}

		if (itemAvailablityList != null && itemAvailablityList.size() > 0) {
			status.setCode("S");
			status.setMessage("SUCCESS");
			response.setStatus(status);
			response.setItemAvailablity(itemAvailablityList);
		} else {
			status.setCode("F");
			status.setMessage("Failure");
			response.setStatus(status);
		}
		return response;

	}

	public List<ItemAvailability> checkStoreInventoryQuantity(List<Long> strInvLocations, Set<String> stockItems, List<ItemAvailability> itemAvailablityList, final Map<String, String> packItmMap) {

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("items", stockItems);
			paramMap.put("locations", strInvLocations);
			itemAvailablityList.addAll(
					simJdbcTemplate.query("SELECT ITEM_ID, STORE_ID, SIM_AVAIL_TO_SELL AVAIL_QTY FROM XX_OMS_INV WHERE ITEM_ID IN (:items) AND STORE_ID IN (:locations) AND SOURCE = 'E-COMMERCE'",
							paramMap, new RowMapper<ItemAvailability>() {

								public ItemAvailability mapRow(ResultSet rs, int rowNum) throws SQLException {
									ItemAvailability itemAvailability = new ItemAvailability();
									String item = rs.getString("ITEM_ID");
									logger.info("Item from response for ST is  " + item);
									itemAvailability.setItem(item);
									String location = rs.getString("STORE_ID");
									logger.info("Location from response for ST is  " + location);
									itemAvailability.setLocation(location);
									String locType = "S";
									logger.info("Loc_type from response  for ST is " + locType);
									itemAvailability.setLocationType(locType);
									BigDecimal available_qty = rs.getBigDecimal("AVAIL_QTY");
									if (available_qty != null && available_qty.intValue() > 0) {
										logger.info("available_qty from response  for ST is " + available_qty);
										available_qty = getUnFulFilledQtyFromOmsBackOrderDTLTable(item, location, "ST", available_qty);
										itemAvailability.setAvailableQuantity(available_qty);
									} else {
										itemAvailability.setAvailableQuantity(BigDecimal.ZERO);
									}
									logger.info("setting the available_qty after calculating from backorder table for ST is " + available_qty);
									itemAvailability.setUnitOfMeasure("EA");
									itemAvailability.setPackCalculateIndicator(packItmMap.get(item));
									return itemAvailability;
								}
							}));
		} catch (Exception e) {
			logger.error("Exception occured from LookupInventory  for ST is " , e);
		}
		return itemAvailablityList;
	}

	public List<ItemAvailability> checkWHInventoryQuantity(List<Long> locations, Set<String> items, List<ItemAvailability> itemAvailablityList, final Map<Long, Locations> whLocMap) {

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("items", items);
			paramMap.put("locations", locations);
			itemAvailablityList.addAll(omsJdbcTemplate.query(" SELECT ITEM, LOC, LOC_TYPE, AVAIL_QTY_AFTER_BACK_ORDER FROM XX_OMS_INVAVAIL_V_BACK WHERE ITEM IN (:items) AND LOC IN (:locations) ",
				paramMap, new RowMapper<ItemAvailability>() {

					public ItemAvailability mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {

						ItemAvailability itemAvailability = new ItemAvailability();
						itemAvailability.setItem(rs.getString(1));
						Locations loc = whLocMap.get(rs.getLong(2));
						itemAvailability.setLocation(loc.getCode());
						itemAvailability.setLocationType(loc.getType());
						itemAvailability.setChannel_id(loc.getChannelId().toString());
						itemAvailability.setAvailableQuantity(rs.getBigDecimal(4));
						itemAvailability.setUnitOfMeasure("EA");
						itemAvailability.setPackCalculateIndicator("N");
						return itemAvailability;
					}
				}));
		} catch (Exception e) {
			logger.error("Exception occured from LookupInventory  for WH is ", e);
		}
		return itemAvailablityList;
	}

	public List<ItemAvailability> checkFutureInventoryQuantity(Collection<String> futureLocationset, Collection<String> futureItemList, List<ItemAvailability> itemAvailabilityList) {

		itemAvailabilityList = callFutureInventoryQuery(futureItemList, futureLocationset, itemAvailabilityList);

		return itemAvailabilityList;
	}

	public List<ItemAvailability> getPackItemInventoryQuantity(final Map<String, Locations> packLocations, Collection<String> packItems, List<ItemAvailability> itemAvailabilityList) {

		// String futureInventoryQuery = " select B.item, B.loc LOCATION,
		// A.EXPECTED_QTY, nvl (AVAIL_QTY, 0) AVAIL_QTY, BACK_ORDER_IND from
		// V_CUST_FUTURE_INV_ECOM_FEED_B A, item_loc B where B.ITEM IN (:itm) and B.loc
		// IN (:location) and A.item (+)= B.item and A.location (+)= B.loc ";
		String packItemQuery = " SELECT * FROM GET_PACK_QTY_V WHERE PACK_NO IN (:itm) AND LOC IN (:location) ";


		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("itm", packItems);
		paramMap.put("location", packLocations.keySet());
		try {

			itemAvailabilityList.addAll(omsJdbcTemplate.query(packItemQuery, paramMap, new RowMapper<ItemAvailability>() {

				public ItemAvailability mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
					ItemAvailability itemAvail = new ItemAvailability();
					itemAvail.setItem(rs.getString(1));
					String loc = rs.getString(2);
					Locations location = packLocations.get(loc);
					if (location.getType().equalsIgnoreCase("W")) {
						itemAvail.setLocationType("W");
						itemAvail.setLocation(loc.substring(0, loc.length() - 1));
						itemAvail.setChannel_id(Character.toString(loc.charAt(loc.length() - 1)));
					} else {
						itemAvail.setLocationType("S");
						itemAvail.setLocation(loc);
					}

					if (rs.getBigDecimal(3) != null && rs.getBigDecimal(3).intValue() > 0) {
						itemAvail.setAvailableQuantity(rs.getBigDecimal("AVAIL_QTY"));
					} else {
						itemAvail.setAvailableQuantity(BigDecimal.ZERO);
					}
					itemAvail.setUnitOfMeasure("EA");
					itemAvail.setPackCalculateIndicator("N");
					return itemAvail;
				}
			}));
		} catch (Exception e) {
			logger.error("Exception while retriving the record for futureInventoryQuery" + packItemQuery, e);
		}
		return itemAvailabilityList;
	}

	private List<ItemAvailability> callFutureInventoryQuery(Collection<String> items, Collection<String> locations, List<ItemAvailability> itemList) {

		// String futureInventoryQuery = " select B.item, B.loc LOCATION,
		// A.EXPECTED_QTY, nvl (AVAIL_QTY, 0) AVAIL_QTY, BACK_ORDER_IND from
		// V_CUST_FUTURE_INV_ECOM_FEED_B A, item_loc B where B.ITEM IN (:itm) and B.loc
		// IN (:location) and A.item (+)= B.item and A.location (+)= B.loc ";
		String futureInventoryQuery = " SELECT ITEM, LOCATION, LOC_TYPE, BACK_ORDER_IND, AVAIL_QTY FROM V_CUST_FUTURE_INV_ECOM_FEED_T WHERE ITEM IN (:itm) AND LOCATION IN (:location) ";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itm", items);
		paramMap.put("location", locations);
		try {

			itemList.addAll(omsJdbcTemplate.query(futureInventoryQuery, paramMap, new RowMapper<ItemAvailability>() {

				public ItemAvailability mapRow(ResultSet rs, int rowNum) throws SQLException {
					ItemAvailability itemAvail = new ItemAvailability();
					itemAvail.setItem(rs.getString("ITEM"));
					if (rs.getString("LOC_TYPE").equalsIgnoreCase("W")) {
						itemAvail.setLocationType("W");
						itemAvail.setLocation(rs.getString("LOCATION").substring(0, rs.getString("LOCATION").length() - 1));
						itemAvail.setChannel_id(Character.toString(rs.getString("LOCATION").charAt(rs.getString("LOCATION").length() - 1)));
					} else {
						itemAvail.setLocationType("S");
						itemAvail.setLocation(rs.getString("LOCATION"));
					}

					if (rs.getBigDecimal("AVAIL_QTY") != null && rs.getBigDecimal("AVAIL_QTY").intValue() > 0 && rs.getString("BACK_ORDER_IND") != null
							&& rs.getString("BACK_ORDER_IND").equalsIgnoreCase("Y")) {
						itemAvail.setAvailableQuantity(rs.getBigDecimal("AVAIL_QTY"));
					} else {
						itemAvail.setAvailableQuantity(BigDecimal.ZERO);
					}
					itemAvail.setUnitOfMeasure("EA");
					itemAvail.setPackCalculateIndicator("N");
					return itemAvail;
				}
			}));

		} catch (Exception e) {
			logger.error("Exception while retriving the record for futureInventoryQuery" + futureInventoryQuery, e);
		}
		return itemList;
	}

	public BigDecimal getUnFulFilledQtyFromOmsBackOrderDTLTable(String item, String location, String locType, BigDecimal availableQty) {
		BigDecimal availQty = availableQty;

		String sqlQuery = null;
		BigDecimal unFullFilledQty = BigDecimal.ZERO;
		BigDecimal loc = BigDecimal.ZERO;

		loc = new BigDecimal(location);

		sqlQuery = "SELECT SUM(SOURCE_QTY - FULFILL_QTY) unFullFilledQty FROM OMS_BACK_ORDER_DTL WHERE BACKORDER_STATUS ='N' AND SOURCE_QTY > FULFILL_QTY AND ITEM = '" + item + "' AND SOURCE_LOC = "
				+ loc;

		logger.info("BackOrder query for lookupInventory " + sqlQuery);

		try {

			unFullFilledQty = omsJdbcTemplate.queryForObject(sqlQuery, Collections.<String, Object>emptyMap(), BigDecimal.class);
			if (unFullFilledQty != null && unFullFilledQty.intValue() > 0) {
				availQty = availableQty.subtract(unFullFilledQty);
				logger.info("availQty - unFullFilledQty for an item " + item + " and location " + loc + " is " + availQty);

				if (availQty.intValue() <= 0) {
					availQty = BigDecimal.ZERO;
				}
			}

		} catch (Exception e) {
			logger.error("Exception occured while retriving the data from omsBackOrderDtl table ", e);

		}
		return availQty;
	}
}
