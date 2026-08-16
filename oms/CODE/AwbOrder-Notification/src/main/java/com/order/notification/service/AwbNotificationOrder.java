package com.order.notification.service;



import java.sql.Connection;
import java.util.List;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.order.notification.DAO.EmailNotificationDAO;
import com.order.notification.DAO.EmailReminderNoftication;
import com.order.notification.DAO.SmsNotificationDAO;
import com.order.notification.DAO.SmsReminderNotification;

import com.order.notification.mail.EmailNotification;
import com.order.notification.mail.SmsNotification;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.sms.bean.SmsInfo;


public class AwbNotificationOrder {

	private static final Logger LOGGER = LogManager.getLogger(AwbNotificationOrder.class.getName());

	public static void main(String[] args) {
		//Charan
         String arg=args[0];
         //String arg="NORMAL";
        LOGGER.info("--------Start OrderEmailSmsNotification batch-----------");
		GetDBConnection getDBConnection = new GetDBConnection();
		EmailNotification emailNotification = new EmailNotification();
		SmsNotification smsNotification = new SmsNotification();
		Connection omsConn = null;
		try {

			// Loading OMS the DB properties
			getDBConnection.loadProperties(Constant.Database.OMS);

			// Calling for OMS DB Connection
			omsConn = getDBConnection.getConnection();

			EmailNotificationDAO emailNotificationDAO = new EmailNotificationDAO(omsConn);
			SmsNotificationDAO smsNotificationDAO = new SmsNotificationDAO(omsConn);
			
            List<EmailInfo> emailInfoList = null;
			List<SmsInfo> smsInfoList = null;
			
			if (PropertiesReader.getProperty(Constant.NORMAL).equalsIgnoreCase(arg)) {
                   LOGGER.info("Fetching data for NORMAL flow execution");
		        emailInfoList = emailNotificationDAO.getOrderEmailInfo();
				smsInfoList = smsNotificationDAO.getOrderSmsInfo();
			}else if (PropertiesReader.getProperty(Constant.REMINDER).equalsIgnoreCase(arg)) {
				LOGGER.info("Fetching data for REMINDER flow excution");
				emailInfoList = emailNotificationDAO.getReminderEmailInfo();
				smsInfoList=smsNotificationDAO.getReminderSmsInfo();
				
			}
			
			
			for (EmailInfo emailInfo : emailInfoList) {
				try {
					// Sending email to storeManager
					emailNotification.sendMail(emailInfo);
					emailNotificationDAO.updateEmailFlag(emailInfo.getOrderNumber(), "2");
				} catch (Exception e) {
					LOGGER.error("Email Api Exeception"+e);
					emailNotificationDAO.updateEmailFlag(emailInfo.getOrderNumber(), "1");
				}
			}
			
			
			
			for (SmsInfo smsInfo : smsInfoList) {

				try {
					smsNotification.sendsms(smsInfo);
					
//					/ * Status code 0 no sms send status code 1 called api to
//					 * send sms but failed. status code 2 called api to send sms
//					 * but success.
//					 /
					smsNotificationDAO.updateSmsFlag(smsInfo.getOrderNumber(), "2");
				} catch (Exception e) {
					LOGGER.error("Sms Api Exception"+e);
					smsNotificationDAO.updateSmsFlag(smsInfo.getOrderNumber(), "1");
				}
			}
		} catch (Exception e) {
			LOGGER.info(e);

		} finally {
			GetDBConnection.closeConnection(omsConn);
		}

		LOGGER.info("--------Successfully stoped OrderEmailSmsNotification batch -----------");
	
	}
}
