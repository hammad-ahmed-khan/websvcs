package com.extra.notification.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.extra.notification.common.BaseException;
import com.extra.notification.service.NotificationService;

/**
 * @author aibrahim
 *
 */
public class NotificationUtil {

	private static Logger LOGGER = Logger.getLogger(NotificationUtil.class);

	public static void main(String args[]) {
		
		LOGGER.info("Starting notification batch process. ");
		LOGGER.info("Notification process started at " + new Date());
		Long startMS = System.currentTimeMillis();
		try {
			if (args.length == 0) {
				throw new BaseException("Notification type argument missing. Please provide type as either NORMAL or REMAINDER", new IllegalArgumentException());
			}
			String notificationType = args[0];
			if (!(notificationType.equals("NORMAL") || notificationType.equals("REMAINDER"))) {
				throw new BaseException("Invalid notification type. Please provide type as either NORMAL or REMAINDER", new IllegalArgumentException());
			}
			NotificationService notificationService = new NotificationService(); 
			notificationService.processNotification(notificationType);
			LOGGER.info("Notification batch process completed successfully. ");
		} catch (BaseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch(Exception e) {
			LOGGER.error("Error while processing email and sms process.", e);
		}
		Long totalTime = (System.currentTimeMillis() - startMS) / (1000 * 60);
		LOGGER.info("Notification process ended at " + new Date() + ". Total time to compelete the process is " + totalTime + " min(s)");
		LOGGER.info("Notification batch process terminated. ");
	}
}
