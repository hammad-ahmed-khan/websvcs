package com.order.notification.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.order.notification.mail.bean.Recipients;
import com.order.notification.mail.bean.To;
import com.order.notification.sms.bean.MetaData;
import com.order.notification.sms.bean.SmsData;
import com.order.notification.sms.bean.SmsInfo;


public class SmsNotificationDAO {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	Connection sim14Conn;
	Connection omsConn;
	PreparedStatement getOrderNoPeSTMT = null;
	PreparedStatement getReminderOrderNoPeSTMT = null;
	PreparedStatement getStoreManagerDetailsPeSTMT = null;
	PreparedStatement getSystemParameterPeSTMT = null;
	PreparedStatement updateSmsFlagPeSTMT = null;
	PreparedStatement getOrderStatusPeSTMT = null;
	PreparedStatement updateEmailStatusFlagPreStmt = null;
	PreparedStatement orderCountryStmt = null;
	PreparedStatement getOrderTypePeSTMT = null;
	
	String getOrderNo = "select CUST_ORDER_NO,STORE_NO from CFS_SMS_EMAIL_STATUS_INFO where ORDER_PICK_STATUS = '0' and SMS_STATUS_CODE ='0' and SMS_PROCESS_IND='N' and STORE_NO != '19010' and trunc(CREATE_DATETIME)=trunc(sysdate)";
	String getReminderOrderNo = "select CUST_ORDER_NO,STORE_NO from CFS_SMS_EMAIL_STATUS_INFO where ORDER_PICK_STATUS != '1' and SMS_STATUS_CODE ='1' and SMS_PROCESS_IND='N' and STORE_NO != '19010' and trunc(CREATE_DATETIME)=trunc(sysdate)";
	String getOrderStatus = "select STATUS from sim14.FUL_ORD where CUST_ORDER_ID = ?";
	String updateSmsFlag = "update OMSDEV.CFS_SMS_EMAIL_STATUS_INFO set SMS_STATUS_CODE = ? where CUST_ORDER_NO =?";
	// Charan - QA
    // String getStoreManagerDetails = "select PHONE from RMS14.XXK_STORE_CONTACT_V2 where PHONE IS NOT NULL and STOREID = ? and PHONE NOT IN (select PHONENO from extradev.restricted_mail)";
	// Charan - PROD
	String getStoreManagerDetails = "select PHONE from extradev.xxk_store_contact_v where PHONE IS NOT NULL and STOREID = ? and PHONE NOT IN (select PHONENO from restricted_mail)";	
	String getFromPhoneNumber = "SELECT PARAMETER_VALUE FROM OMS_CFS_SYSTEM_PARAMETERS where PARAMETER_ID =?";
	String updateStatusFlag = "UPDATE OMSDEV.CFS_SMS_EMAIL_STATUS_INFO SET SMS_PROCESS_IND='Y' WHERE CUST_ORDER_NO =?";
	String orderType = "select DELIVERY_TYPE from OMS_CUST_ORD_HEAD where ORD_PAYMENT_STATUS ='S' AND STATUS='S' AND CUST_ORDER_NO =?";

	public SmsNotificationDAO(Connection sim14Conn, Connection omsConn) throws SQLException {
		this.sim14Conn = sim14Conn;
		this.getOrderNoPeSTMT = omsConn.prepareStatement(getOrderNo);
		this.getReminderOrderNoPeSTMT = omsConn.prepareStatement(getReminderOrderNo);
		this.getStoreManagerDetailsPeSTMT = omsConn.prepareStatement(getStoreManagerDetails);
		this.getSystemParameterPeSTMT = omsConn.prepareStatement(getFromPhoneNumber);
		this.updateSmsFlagPeSTMT = omsConn.prepareStatement(updateSmsFlag);
		this.getOrderStatusPeSTMT = sim14Conn.prepareStatement(getOrderStatus);
		this.updateEmailStatusFlagPreStmt=omsConn.prepareStatement(updateStatusFlag);
		this.getOrderTypePeSTMT = omsConn.prepareStatement(orderType);
	}

	public List<SmsInfo> getOrderSmsInfo() throws SQLException {
		
		LOGGER.info("***getOrderSmsInfo start executing***");
		String fromPhoneNumber = null;
		
		String body = null;
		List<SmsInfo> storManagerInfoList = new ArrayList<SmsInfo>();
		try {
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_PHONE_NUMBER));
			ResultSet fromSmsReST = getSystemParameterPeSTMT.executeQuery();
			if (fromSmsReST.next()) {
				fromPhoneNumber = fromSmsReST.getString("PARAMETER_VALUE");
			} else {
				throw new Exception("Phone number is not available in DB");
			}
			fromSmsReST.close();
			/*
			 * getSystemParameterPeSTMT.setString(1,
			 * PropertiesReader.getProperty(Constant.ORDER_SMS_TEMPLATE)); ResultSet
			 * orderTemplateReST = getSystemParameterPeSTMT.executeQuery(); if
			 * (orderTemplateReST.next()) { body =
			 * orderTemplateReST.getString("PARAMETER_VALUE"); } else { throw new
			 * Exception("Template is not available in DB"); } orderTemplateReST.close();
			 */
			
			
			
			ResultSet orderNoReST = getOrderNoPeSTMT.executeQuery();
			while (orderNoReST.next()) {
				String orderNumber = orderNoReST.getString("CUST_ORDER_NO");
				System.out.println("inside order #:"+orderNumber);
				SmsInfo smsInfo;// = new SmsInfo();
				MetaData meta;// = new MetaData();
				SmsData sms = new SmsData();
				String country = "";
				char c;
				
				getOrderTypePeSTMT.setString(1, orderNumber);
				ResultSet orderTypeReST = getOrderTypePeSTMT.executeQuery();
				if(orderTypeReST.next()) {
					if(orderTypeReST.getString("DELIVERY_TYPE").equals("S")) {
						System.out.println("SMS SFS:"+orderNumber);
						getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_SMS_TEMPLATES));
					}else {
						System.out.println("SMS CFS:"+orderNumber);
						getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_SMS_TEMPLATE));
					}
				}
				orderTypeReST.close();
				
				ResultSet orderTemplateReST = getSystemParameterPeSTMT.executeQuery();
				if (orderTemplateReST.next()) {
					body = orderTemplateReST.getString("PARAMETER_VALUE");
				} else {
					throw new Exception("Template is not available in DB");
				}
				orderTemplateReST.close();
				System.out.println("SMS body #:"+body);
				getStoreManagerDetailsPeSTMT.setInt(1, orderNoReST.getInt("STORE_NO"));
				ResultSet rs = getStoreManagerDetailsPeSTMT.executeQuery();				
				int i=0;
				String toNumber = null;
				while (rs.next()) {
					smsInfo = new SmsInfo();
					sms = new SmsData();
					meta = new MetaData();
					i++;
					sms.setFromNumber(fromPhoneNumber);
					sms.setBody(formatMessage(body, new String[] { orderNumber }));
					LOGGER.info("*** Phone Number before setting :"+rs.getString("PHONE")+" ::: "+i+" ::: "+ toNumber);
				//  Charan	
					if(null!=toNumber){
						toNumber = /* toNumber +","+ */rs.getString("PHONE");
					}else{
						toNumber = rs.getString("PHONE");
					}
					sms.setToNumber(toNumber);
					LOGGER.info("*** Phone Number after setting :"+sms.getToNumber());
					
					//New changes in SMS request body
					country = String.valueOf( orderNoReST.getInt("STORE_NO") );
					c = country.charAt(0);
					if(c=='1') {
						meta.setCountry("SA");
					}else if(c=='2') {
						meta.setCountry("BH");
					}else if(c=='3') {
						meta.setCountry("OM");
					}
					meta.setCampaignType("TRANSACTIONAL");
					
					smsInfo.setOrderNumber(orderNumber);		
					smsInfo.setSmsData(sms);
					smsInfo.setMetadata(meta);
					storManagerInfoList.add(smsInfo);
				}		
				
			}
			orderNoReST.close();

		} catch (SQLException e) {
			LOGGER.error("Error while fetching the StoreManager Details" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		return storManagerInfoList;
	}

	public List<SmsInfo> getOrderReminderSmsInfo() throws SQLException {
		LOGGER.info("***getOrderReminderSmsInfo start getting executed***");
		String fromPhoneNumber = null;
		String body = null;
		List<SmsInfo> storManagerInfoList = new ArrayList<SmsInfo>();
		try {
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_PHONE_NUMBER));
			ResultSet fromSmsReST = getSystemParameterPeSTMT.executeQuery();
			if (fromSmsReST.next()) {
				fromPhoneNumber = fromSmsReST.getString("PARAMETER_VALUE");
			} else {
				throw new Exception("Phone number is not available in DB");
			}
			fromSmsReST.close();
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_SMS_TEMPLATE));
			ResultSet orderTemplateReST = getSystemParameterPeSTMT.executeQuery();
			if (orderTemplateReST.next()) {
				body = orderTemplateReST.getString("PARAMETER_VALUE");
			} else {
				throw new Exception("Template is not available in DB");
			}
			orderTemplateReST.close();
			ResultSet reminderOrderNoReST = getReminderOrderNoPeSTMT.executeQuery();
			while (reminderOrderNoReST.next()) {
				String orderNumber = reminderOrderNoReST.getString("CUST_ORDER_NO");
				getOrderStatusPeSTMT.setString(1, orderNumber);
				ResultSet orderStatusReST = getOrderStatusPeSTMT.executeQuery();

				if (orderStatusReST.next()) {
					if ("0".equals(orderStatusReST.getString("STATUS"))) {
						SmsInfo smsInfo = new SmsInfo();
						SmsData sms = new SmsData();
						sms.setFromNumber(fromPhoneNumber);
						sms.setBody(formatMessage(body, new String[] { orderNumber }));
						getStoreManagerDetailsPeSTMT.setInt(1, reminderOrderNoReST.getInt("STORE_NO"));
						ResultSet rs = getStoreManagerDetailsPeSTMT.executeQuery();
						while (rs.next()) {
							sms.setToNumber(rs.getString("PHONE"));
						}

						smsInfo.setOrderNumber(orderNumber);
						smsInfo.setSmsData(sms);
						storManagerInfoList.add(smsInfo);
					}
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Error while fetching the StoreManager Details" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		return storManagerInfoList;
	}

	private String formatMessage(String message, String[] messageValues) {
		String valueToken;
		if(message.contains("%%")){
			int index =message.indexOf("%%");
			String str= message.substring(0, index)+"\n"+message.substring(index+2);
			message=str;
			LOGGER.info("Message is "+message);
		}
		if (message.contains("%s")) {
			int c = 0;
			for (int i = 0; i < messageValues.length; i++) {
				valueToken = "%s" + (++c);
				message = message.replaceFirst(valueToken, messageValues[i]);
			}
		}

		return message;
	}

	public void updateSmsFlag(String orderNumber, String status) {
		try {
			updateSmsFlagPeSTMT.setString(1, status);
			updateSmsFlagPeSTMT.setString(2, orderNumber);
			updateSmsFlagPeSTMT.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error while updating the sms flag" + e);
			e.printStackTrace();
		}

	}
	
	// Added by Bijay

		public void updateSMSStatus(String orderNumber) {

			try {
				updateEmailStatusFlagPreStmt.setString(1, orderNumber);
				updateEmailStatusFlagPreStmt.executeUpdate();
			} catch (SQLException e) {
				LOGGER.error("Error while updating the SMS_PROCESS_IND" + e);
				e.printStackTrace();
				
			}
		}
		
		

}
