package com.logicinfo.oms.util;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class StoreInventory {
	private final static Logger log = Logger.getLogger(StoreInventory.class.getName());

	public StoreInventory() {
		super();
	}

	public int getStockForAnItemAndLocationFromSIM(String item, BigDecimal nextLoc, String applicationId) {
		String query = "";
		if ("E-COMMERCE".equals(applicationId)) {
			 query = "select  GREATEST ((AVAIL_TO_SELL - UNFULFILLED_BO), 0) AVAIL_QTY, SOURCE from XX_OMS_INV_BACK where item_id=? and store_id=?";

		} else {
			 query = "select  GREATEST ((AVAIL_TO_SELL - UNFULFILLED_BO), 0) AVAIL_QTY  from XX_OMS_POS_INV_V  where item_id=? and store_id=? ";
		}
		log.info(" the selext statement is " + query);
		Map<String, BigDecimal> storeMap = new HashMap<String, BigDecimal>();
		BigDecimal avail_qty = BigDecimal.ZERO;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String source = null;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, (item));
			preparedStatement.setInt(2, nextLoc.intValue());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BigDecimal value = rs.getBigDecimal(1);
				if ("E-COMMERCE".equals(applicationId)) {
				 source = rs.getString(2); 
				}else {
//				if (null == source) 
//					source = "OMS";
					source = "ORPOS";
				}
				storeMap.put(source, value);

			}
		} catch (Exception e) {
			log.error("----Error occured while Executing the query-----", e);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		// If available qty is negative we are setting the value to zero..
		log.info(" size of storeMap = " + storeMap.size() + "and applicationId is =" + applicationId);

		log.info("-----------------------------------------------------------");
		if (storeMap.containsKey(applicationId)) {
			log.info("-----------------Application checking location ranging is fine--------------");
			avail_qty = storeMap.get(applicationId);
//		} else if (storeMap.containsKey("OMS")) {
//			log.info("---------------Source is null or not defined--------------------");
//			avail_qty = storeMap.get("OMS");
		} else {
			// If location is not ranged in for the item then we are getting null pointer
			// Exception
			log.info("----------------------Location is not Ranged the inventory should be zero------------------");
			avail_qty = BigDecimal.ZERO;
		}

		if (avail_qty.longValue() < 0) {
			avail_qty = BigDecimal.ZERO;
		}
		log.info("retrun value is ***** = " + avail_qty);
		return avail_qty.intValue();
	}
}
