package com.order.notification.DAO;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


public class SmsNotificationDAO {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);
	Connection omsConn;
	PreparedStatement getHeaderInfo = null;
	PreparedStatement getSystemParameterPeSTMT = null;
	PreparedStatement updateSmsFlagPeSTMT = null;
	PreparedStatement getOrderStatusPeSTMT = null;
	PreparedStatement getLanguageCodeCustOrderNo = null;
	PreparedStatement getCourierName = null;
	CallableStatement getMultipleshipment = null;
	PreparedStatement getItemName = null;
	PreparedStatement getHeaderReminderInfo = null;
	AwbSmsUtil awbSmsUtil=null;
	
	public SmsNotificationDAO(Connection omsConn) throws SQLException {

		this.getMultipleshipment = omsConn.prepareCall(AwbInfoUtil.getMultipleShipmentcheck);
		this.getHeaderInfo = omsConn.prepareStatement(AwbInfoUtil.hearder_info_sms);
		this.getSystemParameterPeSTMT = omsConn.prepareStatement(AwbInfoUtil.getFromPhoneNumber_sms);
		this.updateSmsFlagPeSTMT = omsConn.prepareStatement(AwbInfoUtil.updateSmsFlag_sms);
		this.getLanguageCodeCustOrderNo = omsConn.prepareStatement(AwbInfoUtil.getcustOrderNoLanguageCode);
		this.getCourierName = omsConn.prepareStatement(AwbInfoUtil.getCourierName);
		this.getHeaderReminderInfo=omsConn.prepareStatement(AwbInfoUtil.getReminderHeader_info_sms);
        this.awbSmsUtil=new AwbSmsUtil();
	}

	public List<SmsInfo> getOrderSmsInfo() throws SQLException {
		List<SmsInfo> smsinfolist = new ArrayList<SmsInfo>();
		String msgBody = null;
		
		LOGGER.info(
				"**************************getOrderSmsInfo method is executing*****************************************");
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

			ResultSet headerinfo = getHeaderInfo.executeQuery();

			while (headerinfo.next()) {
				LOGGER.info("************header info table is executing*************");
				String orderNumber = headerinfo.getString("CUST_ORDER_NO");

				Integer AWB_ID = headerinfo.getInt("AWB_ID");

				String courier_name = headerinfo.getString("COURIER_NAME");
				String trackingId = headerinfo.getString("TRACKING_ID");
				SmsInfo smsInfo = new SmsInfo();
				SmsData sms = new SmsData();
				sms.setFromNumber(fromPhoneNumber);

				// This for checking either english or Arabic
				Integer languagecode =awbSmsUtil.getcustomerOrderLanguageCode(getLanguageCodeCustOrderNo,orderNumber);
				/* checking the multipleshipment from packages */
				
				String checkMultipleShipmentIndicator=awbSmsUtil.getMultipleShipmentCheck(getMultipleshipment, orderNumber);
			    msgBody=awbSmsUtil.getSmsMessageBody(getCourierName, languagecode, checkMultipleShipmentIndicator, orderNumber, courier_name, trackingId);
				
			    sms.setBody(msgBody);
				sms.setToNumber(headerinfo.getString("DELIVER_PHONE_NO"));
				smsInfo.setOrderNumber(AWB_ID);
				smsInfo.setSmsData(sms);
				smsinfolist.add(smsInfo);
				LOGGER.info("************header info table is completed*************");

			}
			headerinfo.close();

		} catch (SQLException e) {
			LOGGER.error("Error while fetching  smsinfoList" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}

		LOGGER.info(
				"***************************getOrderSmsInfo() is completed*****************************************");
		return smsinfolist;
	}

	
	
	
	
	


	
public List<SmsInfo> getReminderSmsInfo() throws SQLException {

		List<SmsInfo> smsinfolist = new ArrayList<SmsInfo>();
		String msgBody = null;
		
		LOGGER.info(
				"**************************getReminderSmsInfo method is executing*****************************************");
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

			ResultSet headerinfo = getHeaderReminderInfo.executeQuery();

			while (headerinfo.next()) {
				LOGGER.info("************header info table is executing*************");
				String orderNumber = headerinfo.getString("CUST_ORDER_NO");

				Integer AWB_ID = headerinfo.getInt("AWB_ID");

				String courier_name = headerinfo.getString("COURIER_NAME");
				String trackingId = headerinfo.getString("TRACKING_ID");
				SmsInfo smsInfo = new SmsInfo();
				SmsData sms = new SmsData();
				sms.setFromNumber(fromPhoneNumber);

				/** This for checking either english or Arabic   */
				Integer languagecode =awbSmsUtil.getcustomerOrderLanguageCode(getLanguageCodeCustOrderNo,orderNumber);
				
				/* checking the multipleshipment from packages */
				String checkMultipleShipmentIndicator=awbSmsUtil.getMultipleShipmentCheck(getMultipleshipment, orderNumber);
			   
				msgBody=awbSmsUtil.getSmsMessageBody(getCourierName, languagecode, checkMultipleShipmentIndicator, orderNumber, courier_name, trackingId);
				sms.setBody(msgBody);
				sms.setToNumber(headerinfo.getString("DELIVER_PHONE_NO"));
				smsInfo.setOrderNumber(AWB_ID);
				smsInfo.setSmsData(sms);
				smsinfolist.add(smsInfo);
				LOGGER.info("************header info table is completed*************");

			}
			headerinfo.close();

		} catch (SQLException e) {
			LOGGER.error("Error while fetching  smsinfoList" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}

		LOGGER.info(
				"***************************getReminderSmsInfo() is completed*****************************************");
		return smsinfolist;
	}

	
	
	
public void updateSmsFlag(Integer awb, String status) {
		LOGGER.info("updateSmsFlag is executing");
		try {

			updateSmsFlagPeSTMT.setString(1, status);
			updateSmsFlagPeSTMT.setInt(2, awb);
			updateSmsFlagPeSTMT.executeUpdate();
			

		} catch (SQLException e) {
			LOGGER.error("Error while updating the sms flag" + e);
		}
		LOGGER.info("updateSmsFlag is completed");

	}

	
private String formatMessage(String message, String[] messageValues) {
		String valueToken;
		if (message.contains("%s")) {
			int c = 0;
			for (int i = 0; i < messageValues.length; i++) {
				valueToken = "%s" + (++c);
				message = message.replaceFirst(valueToken, messageValues[i]);
			}
		}

		return message;
	}

}
