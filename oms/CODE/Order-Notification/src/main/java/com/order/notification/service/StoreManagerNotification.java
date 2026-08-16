package com.order.notification.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.order.notification.DAO.EmailNotificationDAO;
import com.order.notification.DAO.SmsNotificationDAO;
import com.order.notification.mail.EmailNotification;
import com.order.notification.mail.SmsNotification;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.sms.bean.SmsInfo;

public class StoreManagerNotification {

	private static final Logger LOGGER = LogManager.getLogger(StoreManagerNotification.class.getName());

	public static void main(String[] args) {
		
		//args = new String[2];
		//args[0] = PropertiesReader.getProperty(Constant.NORMAL);
		//Charan_Local
		String arg = "NORMAL";
		//Charan_PROD
		// String arg = args[0];
		LOGGER.info("--------Start OrderEmailSmsNotification batch-----------");
		GetDBConnection getDBConnection = new GetDBConnection();
		EmailNotification emailNotification = new EmailNotification();
		SmsNotification smsNotification = new SmsNotification();
		Connection sim14Conn = null;
		Connection omsConn = null;
		try {
			// Loading sim14 the DB properties
			getDBConnection.loadProperties(Constant.Database.SIM14);

			// Calling for sim14 DB Connecting
			sim14Conn = getDBConnection.getConnection();

			// Loading sim14 the DB properties
			getDBConnection.loadProperties(Constant.Database.OMS);

			// Calling for OMSDEV DB Connecting
			omsConn = getDBConnection.getConnection();

			EmailNotificationDAO emailNotificationDAO = new EmailNotificationDAO(sim14Conn, omsConn);
			SmsNotificationDAO smsNotificationDAO = new SmsNotificationDAO(sim14Conn, omsConn);

			List<EmailInfo> emailInfoList = null;
			List<SmsInfo> smsInfoList = null;
			if (PropertiesReader.getProperty(Constant.NORMAL).equalsIgnoreCase(arg)) {
				// Get all email information for sending the email to respective
				// storeManager
				LOGGER.info("Fetching data for normal flow excution");
				emailInfoList = emailNotificationDAO.getOrderEmailInfo();
				smsInfoList = smsNotificationDAO.getOrderSmsInfo();
				
				LOGGER.info(" The Email size is  for NORMAL " +emailInfoList.size());
				LOGGER.info(" The Sms  Size is  for NORMAL  " +smsInfoList.size());
				
			} else if (PropertiesReader.getProperty(Constant.REMINDER).equalsIgnoreCase(arg)) {
				LOGGER.info("Fetching data for reminder flow excution");
				emailInfoList = emailNotificationDAO.getOrderReminderEmailInfo();
				smsInfoList = smsNotificationDAO.getOrderReminderSmsInfo();
				
				LOGGER.info(" The Email size is  for REMINDER " +emailInfoList.size());
				LOGGER.info(" The Sms  Size is  for REMINDER  " +smsInfoList.size());
				
			}
			for (SmsInfo smsInfo : smsInfoList) {
				try {
					// Sending SMS to storeManager
					smsNotification.sendsms(smsInfo);
					LOGGER.info("updateSmsFlag with order number "+smsInfo.getOrderNumber());
					smsNotificationDAO.updateSmsFlag(smsInfo.getOrderNumber(), "1");
					
					 if (PropertiesReader.getProperty(Constant.REMINDER).equalsIgnoreCase(arg)){
						 //Added by Bijay
						 smsNotificationDAO.updateSMSStatus(smsInfo.getOrderNumber());
						 LOGGER.info("SMS_PROCESS_IND changed to 'Y' for orderNumber: "+smsInfo.getOrderNumber());
						 
					 }
					 
				} catch (Exception e) {
					
					 LOGGER.error("Order STATUS Failed SMS ");
					
				}
				
			}
			
			for (EmailInfo emailInfo : emailInfoList) {
				try {
					// Sending email to storeManager
					
					emailNotification.sendMail(emailInfo);
					  LOGGER.info("updateEmailFlag with order no "+emailInfo.getOrderNumber());
					emailNotificationDAO.updateEmailFlag(emailInfo.getOrderNumber(), "1");
					
					 if (PropertiesReader.getProperty(Constant.REMINDER).equalsIgnoreCase(arg)){
						 //Added by Bijay
						 
						 emailNotificationDAO.updateEmailStatus(emailInfo.getOrderNumber());
						 LOGGER.info("EMAIL_PROCESS_IND changed to 'Y' for orderNumber: "+emailInfo.getOrderNumber());
					 }
				} catch (Exception e) {
					
					LOGGER.error("Order status failed For Email Notification");
				}
				
			}
			
			
		} catch (SQLException e) {
			LOGGER.error("Error :" + e);
			GetDBConnection.closeConnection(sim14Conn);
			GetDBConnection.closeConnection(omsConn);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Error :" + e);
			GetDBConnection.closeConnection(sim14Conn);
			GetDBConnection.closeConnection(omsConn);
			e.printStackTrace();
		}
		GetDBConnection.closeConnection(sim14Conn);
		GetDBConnection.closeConnection(omsConn);
		LOGGER.info("--------Successfully stoped OrderEmailSmsNotification batch -----------");
	}

}
