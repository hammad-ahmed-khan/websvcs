/**
 * 
 */
package com.extra.notification.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.extra.notification.common.BaseException;
import com.extra.notification.dao.NotificationTemplateDAO;
import com.extra.notification.model.NotificationInfo;
import com.extra.notification.util.DBUtill;

/**
 * @author aibrahim
 *
 */
public class NotificationService {

	private static Logger LOGGER = Logger.getLogger(NotificationService.class);

	private NotificationTemplateDAO notificationDAO;

	public NotificationService() {
		notificationDAO = new NotificationTemplateDAO();
	}

	public void processNotification(String notificationType) throws BaseException {
		LOGGER.info("Getting database connection");
		Connection connection = null;
		try {
			connection = DBUtill.getConnection();
			List<NotificationInfo> notifications = notificationDAO.getNotificationMessages(connection, notificationType);
			if (notifications != null && !notifications.isEmpty()) {
				LOGGER.info("Number of orders received for notification " + notifications.size());
				int noOfThreads = Runtime.getRuntime().availableProcessors();
				int capacity = notifications.size() >= noOfThreads ? notifications.size() / noOfThreads : 1;
				int startIndex = 0;
				List<Thread> threads = new ArrayList<Thread>(noOfThreads);
				LOGGER.info("Number of threads availabe to process the notification " + noOfThreads);
				while (startIndex < notifications.size()) {
					NotificationThread thread = new NotificationThread(notifications.subList(startIndex, Math.min(startIndex += capacity, notifications.size())));
					thread.start();
					threads.add(thread);
				}
				LOGGER.info("Number of threads created to process the notification " + threads.size());
				LOGGER.info("Waiting for Threads to complete the notification process.");
				for(Thread t: threads) {
					t.join();
				}
				LOGGER.info("Threads completed the notification process.");
				notificationDAO.updateNotificationStatus(connection, notifications);
			} else {
				LOGGER.info("No data available for notification");
			}
			LOGGER.info("Closing database connection");
		} catch (Exception e) {
			throw new BaseException(e);
		} finally {
			DBUtill.closeConnection(connection);
		}

	}
}
