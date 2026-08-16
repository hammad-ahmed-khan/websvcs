package com.logicinfo.oms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.ItemMaster;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.Wh;
import com.logicinfo.oms.model.ItemClassification;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStruct;

/**
 * DataAccessDAO.java aibrahim 2024
 */
public class DataAccessDAO {

	private final static Logger _LOG = Logger.getLogger(DataAccessDAO.class.getName());

	public Map<String, ItemMaster> getItemDeptInvInds(Set<String> itemIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, ItemMaster> itemMap = new HashMap<String, ItemMaster>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, DEPT, STATUS, INVENTORY_IND FROM ITEM_MASTER WHERE ITEM IN (");
			for (int i = 1; i <= itemIds.size(); i++) {
				query.append("?");
				if (i != itemIds.size()) {
					query.append(", ");
				}
			}
			query.append(")");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (String itemId : itemIds) {
				statement.setString(lpCnt++, itemId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				ItemMaster item = new ItemMaster();
				item.setItem(rs.getString(1));
				item.setDept(rs.getBigDecimal(2));
				item.setInventory_Ind(rs.getString(4));
				itemMap.put(item.getItem(), item);
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching item details ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return itemMap;
	}

	public Map<String, ItemClassification> getShipClassification(Long initiateLocId, List<String> sfsItemIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, ItemClassification> itemClassificationMap = new HashMap<String, ItemClassification>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			statement = connection.prepareStatement("SELECT GET_CLASSIFICATION_BO_TEST(?, ? ) FROM  DUAL");
			Object[][] objArr = new Object[sfsItemIds.size()][2];
			for (int i = 0; i < sfsItemIds.size(); i++) {
				_LOG.info("Item Classification inside method" + sfsItemIds.get(i) + "intitate Loc Id" + initiateLocId);
				objArr[i][0] = sfsItemIds.get(i);
				objArr[i][1] = null;
			}
			statement.setArray(1, ((OracleConnection) connection).createOracleArray("ITEM_CLASSIFICATION_TABLE", objArr));
			statement.setString(2, initiateLocId.toString());
			rs = statement.executeQuery();
			if (rs.next()) {
				Object[] obj = ((Object[]) ((OracleArray) rs.getArray(1)).getArray());
				for (Object o : obj) {
					OracleStruct struct = (OracleStruct) o;
					Object[] stageRS = struct.getAttributes();
					ItemClassification classification = new ItemClassification();
					classification.setItemId(stageRS[0].toString());
					String claz[] = stageRS[1].toString().split("-");
					_LOG.info("Item Classification inside method" + claz[0]);
					classification.setClassification(claz[0]);
					classification.setPreOrder("Y".equals(claz[1]));
					if ("Y".equals(claz[1])) {
						classification.setClassification("PRE ORDER " + claz[0]);
					}
					_LOG.info("Item Classification - " + classification.getItemId() + ":" + classification.getClassification());
					itemClassificationMap.put(classification.getItemId(), classification);
				}
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching ship classification details ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return itemClassificationMap;
	}

	public Map<String, Date> getPreOrderItemsDlvDates(Set<String> preOrdCities, Set<String> preOrdItems, Long initiateLocId, Set<String> delvTypes) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Long firstDigitOfLocId = Long.valueOf(String.valueOf(initiateLocId).substring(0, 1));
		StringBuilder query = new StringBuilder("SELECT DELV_DATE, ITEM, CITY_NAME, DELIVERY_TYPE FROM OMS_CUST_ORDER_DELV_DATE WHERE (CITY_NAME) IN (");
		for (int i = 1; i <= preOrdCities.size(); i++) {
			query.append("?");
			if (i != preOrdCities.size()) {
				query.append(", ");
			}
		}
		query.append(") AND UPPER(CLASSIFICATION) = ? AND REQUESTOR_ID = ? AND ITEM IN (");
		for (int i = 1; i <= preOrdItems.size(); i++) {
			query.append("?");
			if (i != preOrdItems.size()) {
				query.append(", ");
			}
		}
		query.append(") AND STATUS = 'A' AND DELIVERY_TYPE IN (");
		for (int i = 1; i <= delvTypes.size(); i++) {
			query.append("?");
			if (i != delvTypes.size()) {
				query.append(", ");
			}
		}
		query.append(") ");
		Map<String, Date> preOrdDLVDateMap = new HashMap<String, Date>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (String city : preOrdCities) {
				statement.setString(lpCnt++, city);
			}
			statement.setString(lpCnt++, "SMALL");
			statement.setBigDecimal(lpCnt++, new BigDecimal(firstDigitOfLocId));
			for (String item : preOrdItems) {
				statement.setString(lpCnt++, item);
			}
			for (String delvType : delvTypes) {
				statement.setString(lpCnt++, delvType);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				preOrdDLVDateMap.put(rs.getString(3) + "~" + rs.getString(2) + "~" + rs.getString(4), rs.getDate(1));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching preorder items delivery promise dates ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return preOrdDLVDateMap;
	}

	public Map<String, Date> getCfsDlvDates(long initiateLocId) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Date> itemDlvDateMap = new HashMap<String, Date>();

		String query = "SELECT CITY, MAX(DELV_LEAD_TIME) AS LEAD_TIME, DELIVERY_TYPE " + "FROM OMS_CUST_ORDER_DLT " + "WHERE STATUS = 'A' AND DELIVERY_TYPE = 'C' "
				+ "AND CLASSIFICATION = 'BIG' AND REQUESTOR_ID = ? " + "GROUP BY CITY, DELIVERY_TYPE";

		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			statement = connection.prepareStatement(query);
			statement.setLong(1, initiateLocId);
			rs = statement.executeQuery();

			while (rs.next()) {
				Calendar cutOffTime = Calendar.getInstance();
				cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
				cutOffTime.set(Calendar.MINUTE, 0);
				cutOffTime.set(Calendar.SECOND, 0);
				cutOffTime.set(Calendar.MILLISECOND, 0);

				int leadTime = rs.getInt("LEAD_TIME");

				itemDlvDateMap.put(rs.getString("CITY") + "~" + rs.getString("DELIVERY_TYPE"), calculateDlvTime(cutOffTime, leadTime));
			}

		} catch (Exception e) {
			_LOG.error("Error while fetching item delivery dates ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}

		return itemDlvDateMap;
	}

	public Map<String, Date> getItemsDlvDates(Set<String> normalOrdCities, long initiateLocId, Set<String> delvTypes) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder("SELECT CITY, MAX(DELV_LEAD_TIME), DELIVERY_TYPE FROM OMS_CUST_ORDER_DLT WHERE CITY IN (");
		for (int i = 1; i <= normalOrdCities.size(); i++) {
			query.append("?");
			if (i != normalOrdCities.size()) {
				query.append(", ");
			}
		}
		query.append(") AND CLASSIFICATION = ? AND REQUESTOR_ID = ? AND STATUS = 'A' AND DELIVERY_TYPE IN (");
		for (int i = 1; i <= delvTypes.size(); i++) {
			query.append("?");
			if (i != delvTypes.size()) {
				query.append(", ");
			}
		}
		query.append(") GROUP BY CITY, DELIVERY_TYPE");
		Map<String, Date> itemDlvDateMap = new HashMap<String, Date>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (String city : normalOrdCities) {
				statement.setString(lpCnt++, city);
			}
			statement.setString(lpCnt++, "SMALL");
			statement.setBigDecimal(lpCnt++, new BigDecimal(initiateLocId));
			for (String delvType : delvTypes) {
				statement.setString(lpCnt++, delvType);
			}
			rs = statement.executeQuery();
			Calendar cutOffTime = Calendar.getInstance();
			cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
			cutOffTime.set(Calendar.MINUTE, 0);
			cutOffTime.set(Calendar.SECOND, 0);
			cutOffTime.set(Calendar.MILLISECOND, 0);
			while (rs.next()) {
				int leadTime = rs.getInt(2);
				itemDlvDateMap.put(rs.getString(1) + "~" + rs.getString(3), calculateDlvTime(cutOffTime, leadTime));
			}

			for (String city : normalOrdCities) {
				for (String delvType : delvTypes) {
					String key = city + "~" + delvType;
					if (!itemDlvDateMap.containsKey(key)) {
						itemDlvDateMap.put(key, calculateDlvTime(cutOffTime, 5));
					}
				}
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching item delivery dates ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return itemDlvDateMap;
	}

	private Date calculateDlvTime(Calendar cutOffTime, int leadTime) {
		Calendar c = Calendar.getInstance();
		if (c.after(cutOffTime)) {
			++leadTime;
		}
		c.add(Calendar.DATE, leadTime);
		int fromDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int toDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (leadTime >= 7 || fromDayOfWeek == Calendar.FRIDAY || Calendar.FRIDAY == toDayOfWeek || (fromDayOfWeek < Calendar.FRIDAY && Calendar.FRIDAY < toDayOfWeek)
				|| (fromDayOfWeek < Calendar.FRIDAY && toDayOfWeek < fromDayOfWeek)) {
			c.add(Calendar.DATE, 1);
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			c.add(Calendar.DATE, 1);
		}
		return c.getTime();
	}

	public Map<String, List<OmsFulfillMatrixExtDetail>> getMatrixFulfilmentDetails(Set<BigDecimal> initLocations, Set<String> shipClzs, Set<String> shipCityIds, boolean hasCfs) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, List<OmsFulfillMatrixExtDetail>> matrixDetailMap = new HashMap<String, List<OmsFulfillMatrixExtDetail>>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT H.COMBINATION_ID, H.REQUESTOR_ID, H.SHIP_CLASSIFICATION, H.CUSTOMER_CITY, ")
					.append("H.MODE_OF_DELIVERY, D.PRIORITY, D.LOCATION_TYPE, D.LOCATION, D.DELIVERY_FROM_LOC_TYPE, D.DELIVERY_FROM_LOC ")
					.append("FROM OMS_FULFILL_MATRIX_EXT_HEAD H, OMS_FULFILL_MATRIX_EXT_DETAIL D WHERE H.COMBINATION_ID = D.COMBINATION_ID AND ").append("H.REQUESTOR_ID IN (");
			for (int i = 1; i <= initLocations.size(); i++) {
				query.append("?");
				if (i != initLocations.size()) {
					query.append(", ");
				}
			}
			query.append(") AND H.SHIP_CLASSIFICATION IN (");
			for (int i = 1; i <= shipClzs.size(); i++) {
				query.append("?");
				if (i != shipClzs.size()) {
					query.append(", ");
				}
			}
			_LOG.info("SHIP_CLASSIFICATION - " + shipClzs);
			query.append(") AND ((");
			if (hasCfs) {
				query.append("H.MODE_OF_DELIVERY = 'C' ");
				if (!shipCityIds.isEmpty()) {
					query.append("OR ");
				}
			}
			if (!shipCityIds.isEmpty()) {
				query.append("(H.MODE_OF_DELIVERY = 'S' AND (");
				for (int i = 1; i <= shipCityIds.size(); i++) {
					query.append("H.CUSTOMER_CITY = ? OR ");
				}
				query.append("H.CUSTOMER_CITY = 'ALL'))");
			}
			query.append(")) ORDER BY H.CUSTOMER_CITY, D.PRIORITY");

			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal loc : initLocations) {
				statement.setBigDecimal(lpCnt++, loc);
			}
			for (String clz : shipClzs) {
				statement.setString(lpCnt++, clz);
			}
			for (String cityId : shipCityIds) {
				statement.setString(lpCnt++, cityId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				String dlvType = rs.getString(5);
				String key = rs.getString(2) + "~" + rs.getString(3) + "~" + dlvType + ("S".equals(dlvType) ? ("~" + rs.getString(4)) : "");
				List<OmsFulfillMatrixExtDetail> matrixDetails = matrixDetailMap.get(key);
				if (matrixDetails == null) {
					matrixDetails = new ArrayList<OmsFulfillMatrixExtDetail>();
					matrixDetailMap.put(key, matrixDetails);
				}
				OmsFulfillMatrixExtDetail matrixDetail = new OmsFulfillMatrixExtDetail();
				matrixDetail.setPriority(rs.getBigDecimal(6));
				matrixDetail.setLocationType(rs.getString(7));
				matrixDetail.setLocation(rs.getBigDecimal(8));
				matrixDetail.setDeliveryFromLocType(rs.getString(9));
				matrixDetail.setDeliveryFromLoc(rs.getBigDecimal(10));
				matrixDetails.add(matrixDetail);
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching matrix details ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return matrixDetailMap;
	}

	public Map<String, Integer> getStoreStocks(Set<BigDecimal> storeLocs, Set<String> itemIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> stStockMap = new HashMap<String, Integer>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_SIM_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM_ID, STORE_ID, (AVAIL_TO_SELL - UNFULFILLED_BO) FROM XX_OMS_POS_INV_V WHERE STORE_ID IN (");
			for (int i = 1; i <= storeLocs.size(); i++) {
				query.append("?");
				if (i != storeLocs.size()) {
					query.append(", ");
				}
			}
			query.append(") AND ITEM_ID IN (");
			for (int i = 1; i <= itemIds.size(); i++) {
				query.append("?");
				if (i != itemIds.size()) {
					query.append(", ");
				}
			}
			query.append(") ");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : storeLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			for (String itemId : itemIds) {
				statement.setString(lpCnt++, itemId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				stStockMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching storestocks ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return stStockMap;
	}

	public Map<String, Integer> getWarehouseStocks(Set<BigDecimal> whLocs, Set<String> itemIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> whStockMap = new HashMap<String, Integer>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, LOC, AVAIL_QTY_AFTER_BACK_ORDER FROM XX_OMS_POS_INVAVAIL_V WHERE LOC IN (");
			for (int i = 1; i <= whLocs.size(); i++) {
				query.append("?");
				if (i != whLocs.size()) {
					query.append(", ");
				}
			}
			query.append(") AND ITEM IN (");
			for (int i = 1; i <= itemIds.size(); i++) {
				query.append("?");
				if (i != itemIds.size()) {
					query.append(", ");
				}
			}
			query.append(")");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : whLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			for (String itemId : itemIds) {
				statement.setString(lpCnt++, itemId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				whStockMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching warehouse stock details ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return whStockMap;
	}

	public Map<String, Integer> getPreOrdItemStocks(Set<BigDecimal> whLocs, Set<BigDecimal> storeLocs, Set<String> preOrdItems) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> preOrdItemStockMap = new HashMap<String, Integer>();
		try {
			_LOG.info("whLocs- " + whLocs + "storeLocs - " + storeLocs);
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, LOCATION, AVAIL_QTY FROM V_CUST_FUTURE_INV_ECOM_FEED_P WHERE LOCATION IN (");
			for (int i = 1; i <= whLocs.size(); i++) {
				query.append("?");
				if (i != whLocs.size() || !storeLocs.isEmpty()) {
					query.append(", ");
				}
			}
			for (int i = 1; i <= storeLocs.size(); i++) {
				query.append("?");
				if (i != storeLocs.size()) {
					query.append(", ");
				}
			}
			query.append(") AND ITEM IN (");
			for (int i = 1; i <= preOrdItems.size(); i++) {
				query.append("?");
				if (i != preOrdItems.size()) {
					query.append(", ");
				}
			}
			query.append(")");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : whLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			for (BigDecimal locId : storeLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			for (String itemId : preOrdItems) {
				statement.setString(lpCnt++, itemId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				preOrdItemStockMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching pre order item stock details ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return preOrdItemStockMap;
	}

	public Set<String> getLinkedClzSKUs(Set<String> items) {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Set<String> linkedSKUs = new HashSet<String>();
		StringBuilder query = new StringBuilder("SELECT ITEM FROM XX_WALL_BRACKET_SKU WHERE ITEM IN ( ");

		for (int i = 1; i <= items.size(); i++) {
			query.append("?");
			if (i != items.size()) {
				query.append(", ");
			}
		}
		query.append(")");
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (String itemId : items) {
				pstmt.setString(lpCnt++, itemId);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				linkedSKUs.add(rs.getString(1));
			}
		} catch (Exception e) {
			_LOG.error("Exception while fetching linked classification for sku: ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				_LOG.warn("Error while closing result set: ", e);
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				_LOG.warn("Error while closing statement: ", e);
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				_LOG.warn("Error while closing connection: ", e);
			}
		}
		return linkedSKUs;
	}

	public List<String> getBOItemStatus(Set<String> itemIds, List<BigDecimal> allLocs) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<String> boActiveItems = new ArrayList<String>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, LOC FROM ITEM_LOC_TRAITS WHERE BACK_ORDER_IND = 'Y' AND LOC IN (");
			for (int i = 1; i <= allLocs.size(); i++) {
				query.append("?");
				if (i != allLocs.size()) {
					query.append(", ");
				}
			}
			query.append(") AND ITEM IN (");
			for (int i = 1; i <= itemIds.size(); i++) {
				query.append("?");
				if (i != itemIds.size()) {
					query.append(", ");
				}
			}
			query.append(")");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : allLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			for (String itemId : itemIds) {
				statement.setString(lpCnt++, itemId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				boActiveItems.add(rs.getString(1) + "~" + rs.getString(2));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching storestocks ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return boActiveItems;
	}

	public List<Wh> getWhsWithChannelIds(Set<BigDecimal> whIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<Wh> whs = new ArrayList<Wh>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT PHYSICAL_WH, CHANNEL_ID FROM WH WHERE WH IN (");
			for (int i = 1; i <= whIds.size(); i++) {
				query.append("?");
				if (i != whIds.size()) {
					query.append(", ");
				}
			}
			query.append(")");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : whIds) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				Wh wh = new Wh();
				wh.setChannelId(rs.getBigDecimal("CHANNEL_ID"));
				wh.setPhysicalWH(rs.getBigDecimal("PHYSICAL_WH"));
				whs.add(wh);
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching storestocks ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return whs;
	}

	public Map<String, Integer> getWHPOStocks(String itemId, List<Wh> whChIds) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> whPOStockMap = new HashMap<String, Integer>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_DAS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, LOCATION, EXPECTED_QTY FROM V_CUST_FUTURE_INV_POSITION WHERE ITEM = ? AND ");

			query.append(" ( ");
			for (int i = 1; i <= whChIds.size(); i++) {
				query.append(" ( LOCATION = ? AND CHANNEL_ID = ? )");
				if (i != whChIds.size()) {
					query.append(" OR ");
				}
			}
			query.append(") ");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			statement.setString(lpCnt++, itemId);
			for (Wh loc : whChIds) {
				statement.setBigDecimal(lpCnt++, loc.getPhysicalWH());
				statement.setBigDecimal(lpCnt++, loc.getChannelId());
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				whPOStockMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching back order storestocks ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return whPOStockMap;
	}

	public Map<String, Integer> getStorePOStocks(String itemId, Set<BigDecimal> stores) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> stPOStockMap = new HashMap<String, Integer>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_DAS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, LOCATION, EXPECTED_QTY FROM V_CUST_FUTURE_INV_POSITION WHERE ITEM = ? AND ");

			query.append(" LOCATION IN (");
			for (int i = 1; i <= stores.size(); i++) {
				query.append("?");
				if (i != stores.size()) {
					query.append(", ");
				}
			}
			query.append(") ");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			statement.setString(lpCnt++, itemId);
			for (BigDecimal locId : stores) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				stPOStockMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching back order storestocks ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return stPOStockMap;
	}

	public Map<String, Integer> getBOAllocatedQty(String itemId, List<BigDecimal> allLocs) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Integer> boAllocQtyMap = new HashMap<String, Integer>();
		try {
			connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
			StringBuilder query = new StringBuilder("SELECT ITEM, SOURCE_LOC, SUM(SOURCE_QTY - FULFILL_QTY) FROM OMS_BACK_ORDER_DTL WHERE BACKORDER_STATUS <> 'S' AND SOURCE_LOC IN (");
			for (int i = 1; i <= allLocs.size(); i++) {
				query.append("?");
				if (i != allLocs.size()) {
					query.append(", ");
				}
			}
			query.append(") AND ITEM = ? ");
			statement = connection.prepareStatement(query.toString());
			int lpCnt = 1;
			for (BigDecimal locId : allLocs) {
				statement.setBigDecimal(lpCnt++, locId);
			}
			statement.setString(lpCnt++, itemId);
			rs = statement.executeQuery();
			while (rs.next()) {
				boAllocQtyMap.put(rs.getString(1) + "~" + rs.getString(2), rs.getInt(3));
			}
		} catch (Exception e) {
			_LOG.error("Error while fetching back order alloacted quantity ", e);
			throw new RuntimeException(e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return boAllocQtyMap;
	}

	public Set<String> getWallBracketItems(List<String> items) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Set<String> wallBracketSet = new HashSet<String>();

		if (items == null || items.isEmpty()) {
			return wallBracketSet;
		}

		StringBuilder query = new StringBuilder("SELECT ITEM FROM XX_WALL_BRACKET_SKU WHERE ITEM IN (");

		for (int i = 0; i < items.size(); i++) {
			query.append("?");
			if (i < items.size() - 1) {
				query.append(",");
			}
		}
		query.append(")");

		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query.toString());

			int index = 1;
			for (String item : items) {
				pstmt.setString(index++, item);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				wallBracketSet.add(rs.getString("ITEM"));
			}

			_LOG.info("Wall bracket items found: " + wallBracketSet);

		} catch (Exception e) {
			_LOG.error("Error fetching wall bracket items", e);
		} finally {
			OMSUtil.closeDBConnection(con, pstmt, rs);
		}

		return wallBracketSet;
	}
}
