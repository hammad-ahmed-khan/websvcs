package com.order.notification.DAO;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.util.AwbInfoUtil;
import com.db.util.AwbSmsUtil;
import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.order.notification.sms.bean.SmsData;
import com.order.notification.sms.bean.SmsInfo;

public class SmsReminderNotification {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);
	
	Connection omsConn;
	PreparedStatement getHeaderInfo = null;
	PreparedStatement getSystemParameterPeSTMT = null;
	PreparedStatement updateSmsFlagPeSTMT = null;
	PreparedStatement getOrderStatusPeSTMT = null;
	PreparedStatement getLanguageCodeCustOrderNo = null;
	PreparedStatement getCourierName = null;
	CallableStatement getMultipleshipment = null;
	PreparedStatement getReminderSms=null;
	AwbSmsUtil awbInfoUtilReMinder=null;
	

	public SmsReminderNotification(Connection omsConn) throws SQLException {
		this.getMultipleshipment = omsConn.prepareCall(AwbInfoUtil.getMultipleShipmentcheck);
	    this.getSystemParameterPeSTMT = omsConn.prepareStatement(AwbInfoUtil.getFromPhoneNumber_sms);
		this.updateSmsFlagPeSTMT = omsConn.prepareStatement(AwbInfoUtil.updateSmsFlag_sms);
		this.getLanguageCodeCustOrderNo = omsConn.prepareStatement(AwbInfoUtil.getcustOrderNoLanguageCode);
		this.getCourierName = omsConn.prepareStatement(AwbInfoUtil.getCourierName);
		this.getReminderSms=omsConn.prepareStatement(AwbInfoUtil.getReminderHeader_info_sms);
	}

	public List<SmsInfo> getReminderSms() throws SQLException {
		
		LOGGER.info("getRemindersms method is executing");
		List<SmsInfo> smsInfoList = new ArrayList<SmsInfo>();
		
	try {
			String fromPhoneNumber = null;
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_PHONE_NUMBER));
			ResultSet fromSmsReST = getSystemParameterPeSTMT.executeQuery();
          if (fromSmsReST.next()) {
				fromPhoneNumber = fromSmsReST.getString("PARAMETER_VALUE");

			} else {
				throw new Exception("Phone number is not available in DB");
			}
			fromSmsReST.close();

			/**
			 * Getting the order no from oms_awb_header_info table.
			 */

			ResultSet orderNoReST=getReminderSms.executeQuery();
			while (orderNoReST.next()) {
				String orderNumber = orderNoReST.getString("CUST_ORDER_NO");

				Integer AWB_ID = orderNoReST.getInt("AWB_ID");

				String courier_name = orderNoReST.getString("COURIER_NAME");
				String trackingId = orderNoReST.getString("TRACKING_ID");
				SmsInfo smsInfo = new SmsInfo();
				SmsData sms = new SmsData();
				sms.setFromNumber(fromPhoneNumber);
				
				// This for checking either english or Arabic
				Integer languagecode = getcustomerOrderLanguageCode(orderNumber);
				/*Integer languagecode =awbInfoUtilReMinder.getcustomerOrderLanguageCode(getLanguageCodeCustOrderNo,orderNumber);*/
				
				String msgBody = null;
             
				/* checking the multipleshipment from packages     */
				String checkMultipleShipmentIndicator = getMultipleShipmentCheck(orderNumber);
				/*String checkMultipleShipmentIndicator=awbInfoUtilReMinder.getMultipleShipmentCheck(getMultipleshipment, orderNumber)*/

				msgBody = getSmsMessageBody(languagecode, checkMultipleShipmentIndicator, orderNumber, courier_name,
						trackingId);
				  /*  msgBody=awbInfoUtilReMinder.getSmsMessageBody(getCourierName, languagecode, checkMultipleShipmentIndicator, orderNumber, courier_name, trackingId);*/
			
				sms.setBody(msgBody);
				sms.setToNumber(orderNoReST.getString("DELIVER_PHONE_NO"));
				smsInfo.setOrderNumber(AWB_ID);
				smsInfo.setSmsData(sms);
				smsInfoList.add(smsInfo);

			}
			orderNoReST.close();

		} catch (SQLException e) {
			LOGGER.error("Reminder Error while fetching the headerinfotable" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		return smsInfoList;
	}

	public void updateSmsFlag(Integer awb, String status) {
		try {

			updateSmsFlagPeSTMT.setString(1, status);
			updateSmsFlagPeSTMT.setInt(2, awb);
			updateSmsFlagPeSTMT.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Exception Updating the sms flag" + e);
		
		}

	}

	private Integer getcustomerOrderLanguageCode(String custorderNo) {
		LOGGER.info("getcustomerOrderLanguageCode with customer number"+custorderNo);
		ResultSet orderLanguageCodeResultSet = null;
		Integer laguagecode =1;
		try {
			getLanguageCodeCustOrderNo.setString(1, custorderNo);
			orderLanguageCodeResultSet = getLanguageCodeCustOrderNo.executeQuery();
			while (orderLanguageCodeResultSet.next()) {
				laguagecode = orderLanguageCodeResultSet.getInt("CUSTOMER_LANG");

			}
		} catch (SQLException e) {
			LOGGER.error("laguagecode" +e);

		}

		return laguagecode;
	}

	private String getEnglishMessageBodyForSingleShipment(String customerOrder, String courier_name,
			String trackingId) {
		LOGGER.info("English message body for the single shipment");
		if(courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			return "Your Order items" + " " + customerOrder + " " + "has now been shipped with" + " " + courier_name 
					+ " and Shipment # is" + " " + trackingId+"." +" "+"You will receive separate shipment notifications if Order is split into multiple shipments";
		}else{
			String courier_url = getCourierUrl(courier_name, trackingId);
	
			return "Your Order items" + " " + customerOrder + " " + "has now been shipped with" + " " + courier_name 
					+ " and Shipment # is" + " " + trackingId+"." +" "+ "You will be able to track your shipment from here" +" "+ courier_url+" "+"You will receive separate shipment notifications if Order is split into multiple shipments";
		}
}

	private String getArabicMessageBodyForSingleShipment(String customerOrder, String courier_name, String trackingId) {
		String arabicBodyMsg = "";
		if(courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع"  +" "+ " " + courier_name + "."
					+ "ر رقم الشحنة" + " "; 
		}
		else{
			String courier_url = getCourierUrl(courier_name, trackingId);
			LOGGER.info("Arabic message body for the single shipment");
	        arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع"  +" "+ " " + courier_name + "."
					+ "ر رقم الشحنة" + " " + trackingId + " "+ "وبامكانك الان تتبع مسار شحنتك عن طريق هذا الراب"         +" " + courier_url;
		}
		byte[] bytearray = arabicBodyMsg.getBytes();
		try {
			return new String(bytearray, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			return null;

		}
	}

	private String getCourierUrl(String courieName, String trackingNumber) {
		
		String CourierUrl = null;

		try {
			getCourierName.setString(1, courieName.toUpperCase());
			getCourierName.setString(2, courieName.toUpperCase());
			ResultSet result = getCourierName.executeQuery();
			while (result.next()) {
				CourierUrl = result.getString("COURIER_URL");
			
				if (CourierUrl.contains("$$")) {

					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf +3);

					int IndexOfAnd = CourierUrl.indexOf("$$");
					CourierUrl = CourierUrl.substring(0, IndexOfAnd) + "&" + CourierUrl.substring(IndexOfAnd + 2);
					System.out.println("$$url" + CourierUrl);
				} else {
					
					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf + 3);
				
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
		
			LOGGER.error("Reminder Exeception error in the Courier_URL"+e);
		}

		return CourierUrl;

	}


	private String getMultipleShipmentCheck(String orderNumber) {
		LOGGER.info("getMultipleShipmentCheck is Exceuted with customer Number"+orderNumber);
		String indicator = null;
		try {
			this.getMultipleshipment.setString(2, orderNumber);
			this.getMultipleshipment.registerOutParameter(1, Types.VARCHAR);
			this.getMultipleshipment.execute();
			indicator = this.getMultipleshipment.getString(1);
		} catch (SQLException e) {
			LOGGER.error("gerMultipleshipmentCheck exception"+e);
			
		}
		return indicator;
	}

	private String getSmsMessageBody(Integer languagecode, String checkMultipleShipmentIndicator, String orderNumber,
			String courier_name, String trackingId) {

		String msgBody = null;
		if (null != languagecode && new Integer(1).equals(languagecode)) {
			// English is the language is confirmed need to check single
			// shipment or
			// multiple shipment and Y means multiple shipment

			if ("Y".equals(checkMultipleShipmentIndicator)) {
				LOGGER.info(" Multiple Shipment Order for english ");
				msgBody = getEnglishMessageBodyMultipleShipment(orderNumber, courier_name, trackingId);
			} else {
				LOGGER.info("Single Shipment Order for english");
			msgBody = getEnglishMessageBodyForSingleShipment(orderNumber, courier_name, trackingId);

			}

		} else {
			// Language is Arabic and need to check for single or multiple
			// shipment and Y means the multiple shipments
			if ("Y".equals(checkMultipleShipmentIndicator)) {
				LOGGER.info("Multiple Shipment Order for Arabic");
				msgBody = getArabicMessageBodyMultipleShipment(orderNumber, courier_name, trackingId);
			} else {
				LOGGER.info("Single Shipment Order for Arabic");
				msgBody = getArabicMessageBodyForSingleShipment(orderNumber, courier_name, trackingId);

			}

		}

		return msgBody;

	}

	private String getEnglishMessageBodyMultipleShipment(String customerOrder, String courier_name, String trackingId) {
		String courier_url = getCourierUrl(courier_name, trackingId);
		LOGGER.info("getEnglishMessageBodyMultipleShipment is executed");
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
					+ " Shipment No" + " " + trackingId + " Note, your order is splitted in multiple shipments";
		}
		else{
			return "Your shipment for Order" + " " + customerOrder + " " + "will be sent with" + " " + courier_name + "."
					+ " Shipment No" + " " + trackingId + "  you can track your shipment from" + courier_url
					+ "Note, your order is splitted in multiple shipments";
		}
	}

	private String getArabicMessageBodyMultipleShipment(String customerOrder, String courier_name, String trackingId) {
		LOGGER.info("getArabicMessageBodyMultipleShipment is executed");
		String courier_url = getCourierUrl(courier_name, trackingId);
		
		String arabicBodyMsg = "";
		if (courier_name.equalsIgnoreCase("extra") || courier_name.equalsIgnoreCase("xtra")){
			arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع" + " " + courier_name + "."
					+ "ر رقم الشحنة" + trackingId +" "+ "ملاحظة تم شحن طلبكم في شحنات متعددة";
		}
		else{
		
			arabicBodyMsg = "سوف يتم ارسال شحنتك لرقم الطلب" + customerOrder + " " + "مع" + " " + courier_name + "."
				+ "ر رقم الشحنة" + trackingId + " وبامكانك الان تتبع مسار شحنتك عن طريق هذا الراب"         + courier_url
				+" "+ "ملاحظة تم شحن طلبكم في شحنات متعددة";
		}
      byte[] bytearray = arabicBodyMsg.getBytes();

		try {
			return new String(bytearray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
          LOGGER.error("Multiple shipment exception"+e);
			return null;

		}
	}

	
	

}
