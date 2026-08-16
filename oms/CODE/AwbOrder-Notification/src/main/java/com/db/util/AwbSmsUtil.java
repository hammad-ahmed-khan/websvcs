package com.db.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


import org.apache.log4j.Logger;

public class AwbSmsUtil {

	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	public Integer getcustomerOrderLanguageCode(PreparedStatement getLanguageCodeCustOrderNo, String custorderNo) {
		LOGGER.info("getcustomerOrderLanguageCode method is Executing");
		ResultSet orderLanguageCodeResultSet = null;
		Integer laguagecode=1;
		try {
			getLanguageCodeCustOrderNo.setString(1, custorderNo);
			orderLanguageCodeResultSet = getLanguageCodeCustOrderNo.executeQuery();
			while (orderLanguageCodeResultSet.next()) {
				laguagecode = orderLanguageCodeResultSet.getInt("CUSTOMER_LANG");

			}
		} catch (SQLException e) {
			LOGGER.error("Error while getcustomerOrderlanguage code" + e);

		}
		LOGGER.info("getcustomerOrderLanguageCode method is completed");
		return laguagecode;
	}

	
	
	
	
public String getMultipleShipmentCheck(CallableStatement getMultipleshipment, String orderNumber) {
		LOGGER.info("getMultipleShipmentCheck method is executing with customer order number" + orderNumber);
		String indicator=null;
		try {
			getMultipleshipment.setString(2, orderNumber);
			getMultipleshipment.registerOutParameter(1, Types.VARCHAR);
			getMultipleshipment.execute();
			indicator=getMultipleshipment.getString(1);
			LOGGER.info(" end of getMultipleShipmentCheck method");
		} catch (SQLException e) {
			LOGGER.error("Exception in the multipleshipment "+e);
		}

		LOGGER.info("getMultipleShipmentCheck method is completed with customer order number" + orderNumber);
		return indicator;
	}
	
	
	
   public String getCourierUrl(PreparedStatement getCourierName,String courieName, String trackingNumber) {
		LOGGER.info("get CourierUrl method is executing");
		String CourierUrl = null;
		LOGGER.info("get CourierUrl method is executing1");
		try {
			getCourierName.setString(1, courieName.toUpperCase());
			ResultSet result = getCourierName.executeQuery();
			
			while (result.next()) {
				LOGGER.info("getting the courier urlfrom table");
				CourierUrl = result.getString("COURIER_URL");

				if (CourierUrl.contains("$$")) {

					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf + 3);

					int IndexOfAnd = CourierUrl.indexOf("$$");
					CourierUrl = CourierUrl.substring(0, IndexOfAnd) + "&" + CourierUrl.substring(IndexOfAnd + 2);

				} else {

					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf + 3);

				}
			}

		} catch (SQLException e) {
			LOGGER.error("Exception in the Courier URl" + e);
			// if the courier url is null
			return "";
		}
		LOGGER.info("get CourierUrl method is completed");

		return CourierUrl;

	}
	
	
	
	
	
public String getSmsMessageBody(PreparedStatement courierUrl,Integer languagecode, String checkMultipleShipmentIndicator, String orderNumber,
			String courier_name, String trackingId) {
		LOGGER.info("getSmsMessageBody is executing with tracking Id" + trackingId);
		String msgBody = null;

		if (null != languagecode && new Integer(1).equals(languagecode)) {
			// English is the language is confirmed need to check single
			// shipment or
			// multiple shipment and Y means multiple shipment

			if ("Y".equals(checkMultipleShipmentIndicator)) {
				LOGGER.info("Get the multiple english shipment");
				msgBody = getEnglishMessageBodyMultipleShipment(courierUrl,orderNumber, courier_name, trackingId);
			} else {
				LOGGER.info("Get the single english shipment");
				msgBody = getEnglishMessageBodyForSingleShipment(courierUrl,orderNumber, courier_name, trackingId);

			}

		} else {
			// Language is Arabic and need to check for single or multiple
			// shipment and Y means the multiple shipments
			if ("Y".equals(checkMultipleShipmentIndicator)) {
				LOGGER.info("Get the multiple arabic shipment");
				msgBody = getArabicMessageBodyMultipleShipment(courierUrl,orderNumber, courier_name, trackingId);

			} else {
				LOGGER.info("Get the single arabic shipment");
				msgBody = getArabicMessageBodyForSingleShipment(courierUrl,orderNumber, courier_name, trackingId);
				}

		}
		LOGGER.info("getSmsMessageBody is completed with tracking Id" + trackingId);

		return msgBody;

	}

	
	
	
	private String getEnglishMessageBodyMultipleShipment(PreparedStatement CourierUrl,String customerOrder, String courier_name, String trackingId) {
		String courier_url = getCourierUrl(CourierUrl,courier_name, trackingId);
		LOGGER.info("getEnglishMessageBodyMultipleShipment is executing");
		
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
					+""+ "Shipment No." + " " + trackingId +"."+" " + "Note, your order is split into multiple shipments.";			
		}
		else{
		return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
				+""+ "Shipment No." + " " + trackingId +"."+" " +"you can track your shipment from " +" "+ courier_url+" "
				+ "Note, your order is split into multiple shipments.";
		}
	}

	
	private String getArabicMessageBodyMultipleShipment(PreparedStatement CourierUrl,String customerOrder, String courier_name, String trackingId) {
		LOGGER.info("getArabicMessageBodyMultipleShipment is executing");
		String arabicBodyMsg = "";
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + "   " + "مع" + " " + courier_name + "."+"رقم الشحنة" + trackingId+".";
		}else{
		
		String courier_url = getCourierUrl(CourierUrl,courier_name, trackingId);
		
		arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + "   " + "مع" + " " + courier_name + "."+"رقم الشحنة" + trackingId+"."+" " +"وبامكانك الان تتبع مسار شحنتك عن طريق هذا الرابط"+ " "+ courier_url
				+ " " + "ملاحظة تم شحن طلبكم في شحنات متعددة"+".";
		}
		  byte[] bytes = arabicBodyMsg.getBytes(StandardCharsets.UTF_8);
			arabicBodyMsg= new String(bytes, StandardCharsets.UTF_8);
			
		
	  LOGGER.info("getArabicMessageBodyMultipleShipment is completed" +arabicBodyMsg);
		return arabicBodyMsg;
		/*
		 * byte[] bytearray = arabicBodyMsg.getBytes();
		 * 
		 * try { return new String(bytearray, "UTF-8"); } catch
		 * (UnsupportedEncodingException e) {
		 * LOGGER.error("Arabic Conversion error"+e); return null;
		 * 
		 * }
		 */
	
	
		/*try {
		arabicBodyMsg = new String(arabicBodyMsg.getBytes("UTF-8"));
		
	} catch (UnsupportedEncodingException e) {
		
		LOGGER.info("getArabicMessageBodyMultipleShipment exceptionn"+e);
	}*/
	
	
	}

	
private String getEnglishMessageBodyForSingleShipment(PreparedStatement CourierUrl,String customerOrder, String courier_name,
			String trackingId) {
		LOGGER.info("get English Message Body For SingleShipment is executing with tracking id" + trackingId);
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
					+ " Shipment No." + " " + trackingId+".";

		}
		else{
			String courier_url = getCourierUrl(CourierUrl,courier_name, trackingId);
			LOGGER.info("getEnglishMessageBodyForSingleShipment is completed with tracking id" + trackingId);
			return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
			+ " Shipment No." + " " + trackingId+"." +" "+ "you can track your shipment from  " +" "+ courier_url;
		}	
	}

	
	
	private String getArabicMessageBodyForSingleShipment(PreparedStatement CourierUrl,String customerOrder, String courier_name, String trackingId) {

		LOGGER.info("get Arabic Message Body For SingleShipment is executing with trackingId" + trackingId);
		String arabicBodyMsg = "";
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع" + " " + " " + courier_name
					+ "." +" "+ "رقم الشحنة:"  + " " + trackingId +".";
		}else{
		String courier_url = getCourierUrl(CourierUrl,courier_name, trackingId);

		arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع" + " " + " " + courier_name
				+ "." +" "+ "رقم الشحنة:"  + " " + trackingId +"."+" " + "وبامكانك الان تتبع مسار شحنتك عن طريق هذا الرابط"
				+ "  " + courier_url;
		}
		
	   byte[] bytes = arabicBodyMsg.getBytes(StandardCharsets.UTF_8);
		arabicBodyMsg= new String(bytes, StandardCharsets.UTF_8);
		
		
	/*	byte[] bytearray = arabicBodyMsg.getBytes();
		try {
			LOGGER.info("getArabicMessageBodyForSingleShipment is completed with trackingId" + trackingId);
			arabicBodyMsg=new String(bytearray, "UTF-8");
			LOGGER.info("Arabic Message Body for single"+arabicBodyMsg);

		} catch (UnsupportedEncodingException e) {
			LOGGER.error("arabicBodyMsg is null" + e);
			return "";

		}*/
		return arabicBodyMsg;
	}

	
	
	
	
	
	
	
	
	
	
	

}
