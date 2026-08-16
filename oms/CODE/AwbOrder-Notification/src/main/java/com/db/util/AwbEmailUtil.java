package com.db.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AwbEmailUtil {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);
	
	    public static final Integer englanguagecode=1;// This for the English
	    public static final Integer arblanguagecode=13;// This for the arabic 
	    public static final String engMultipleShiplment="Y";//It indicates the multiple shipment in english if it is N Not multiple chipment
	    public static final String arbMultipleShiplment="Y";//It indicates the multiple shipment in Arabic if it is N Not multiple chipment
	   
     public String getOmsCurdNo(PreparedStatement getomsCurdNo,String omscurd) {

		LOGGER.info("Oms customer order number method is excuting");
		String omscurdno = null;
		try {
			getomsCurdNo.setString(1, omscurd);
			ResultSet result = getomsCurdNo.executeQuery();
			while (result.next()) {
				omscurdno = result.getString("OMS_CUST_ORD_NO");
			}
		} catch (SQLException e) {
			LOGGER.error("Exception in the omsCurd No" + e);
		}
		LOGGER.info("Oms customer order number is completed");
		return omscurdno;

	}

  public String getCustomerName(PreparedStatement getcustomername,String omsCurdNo) {
		String firstName = null;
		String lastName=null;
		String fullName=null;
		LOGGER.info("get customerName method is executing");
      try {
			getcustomername.setString(1, omsCurdNo);
			ResultSet resultset = getcustomername.executeQuery();

			while (resultset.next()) {
			firstName=resultset.getString("DELIVER_FIRST_NAME");
			lastName =resultset.getString("DELIVER_LAST_NAME");
			fullName=firstName+" " +lastName;
		   }
			if (fullName == null) {
				LOGGER.info("Customer Name is Null");
				fullName = "";
			}

		} catch (SQLException e) {
			LOGGER.error("Exception in the Customer Name" + e);
		}
		LOGGER.info("get customerName method is completed");
		return fullName;
	}
	
	
	
	
	public List<Items> getEnglishItems(PreparedStatement getItemList,PreparedStatement getItemName, Integer awb_id) {
		LOGGER.info("Preparing the English items list is excuting");
		List<Items> items = new ArrayList<Items>();
		try {
			getItemList.setInt(1, awb_id);
			ResultSet itemresultset = getItemList.executeQuery();
			while (itemresultset.next()) {
				Items item = new Items();

				getItemName.setString(1, itemresultset.getString("ITEM_ID"));
				ResultSet resultSet = getItemName.executeQuery();

				String itemName = null;
				while (resultSet.next()) {

					itemName = resultSet.getString(25);

				}
				item.setItemName(itemName);
				item.setPrice(itemresultset.getString("UNIT_RETAIL"));
				item.setQty(itemresultset.getString("QTY"));
				items.add(item);

			}
		} catch (SQLException e) {

			LOGGER.info("Exception getEnglishItems" + e);
		}
		LOGGER.info("Preparing the English item list is completed");
		return items;

	}

	
	// getting items for the arabic
	public List<Items> getItemsArabic(PreparedStatement getItemList,PreparedStatement getItemName,Integer awb_id) {
		LOGGER.info("Preparing the Arabic items list is executing");
		List<Items> items = new ArrayList<Items>();
		try {
			getItemList.setInt(1, awb_id);
			ResultSet itemresultset = getItemList.executeQuery();
			while (itemresultset.next()) {
				Items item = new Items();

				getItemName.setString(1, itemresultset.getString("ITEM_ID"));

				ResultSet resultSet = getItemName.executeQuery();

				String itemName = null;
				while (resultSet.next()) {
					LOGGER.info("Get the ItemName");
					itemName = resultSet.getString(24);

				}

				/*byte[] bytearray = itemName.getBytes();
				try {
					LOGGER.info("Arabic Conversion");
					itemName = new String(bytearray, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("arabicBodyMsg is null" + e);

				}*/

				item.setItemName(itemName);
				item.setPrice(itemresultset.getString("UNIT_RETAIL"));
				item.setQty(itemresultset.getString("QTY"));
				items.add(item);

			}
		} catch (SQLException e) {
			LOGGER.info("Exception getItemsArabic" + e);
			
		}
		LOGGER.info("Preparing the Arabic item list is completed");
		return items;

	}

	
}
