package com.extra.notification.service;

import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.extra.notification.common.Constant;
import com.extra.notification.model.NotificationInfo;
import com.extra.notification.util.PropertyHolder;
import com.sun.jersey.api.client.ClientResponse;

/**
 * @author aibrahim
 *
 */
public class NotificationThread extends Thread {

	private static Logger LOGGER = Logger.getLogger(NotificationThread.class);

	private List<NotificationInfo> notifications;

	private APIService apiService;

	public NotificationThread(List<NotificationInfo> notifications) {
		this.notifications = notifications;
		apiService = new APIService();
	}

	@Override
	public void run() {
		try {
			for(NotificationInfo notification: notifications) {
				if (notification.getEmailInfo() != null) {
					LOGGER.info("Sending email for the order number '" + notification.getOrderNo() + "' to " + StringUtils.join(notification.getEmailInfo().getEmail().getRecipients().getTo(), ","));
					notification.setEmailResponse(emailNotification(notification));
				}
				if (notification.getSmsInfo() != null) {
					LOGGER.info("Sending SMS for the order number '" + notification.getOrderNo() + "' to " + notification.getSmsInfo().getSmsData().getToNumber());
					notification.setSmsResponse(smsNotification(notification));
				}
			}
			for(NotificationInfo notification: notifications) {
				if (notification.getEmailInfo() != null) {
					try {
						notification.getEmailResponse().get();
					} catch (Exception e) {
					}
				}
				if (notification.getSmsInfo() != null) {
					try {
						notification.getSmsResponse().get();
					} catch (Exception e) {
					}
				}
			}
			for(NotificationInfo notification: notifications) {
				if (notification.getEmailInfo() != null) {
					ClientResponse response = null;
					try {
						response = notification.getEmailResponse().get();
						if (response.getStatus() == Status.OK.getStatusCode()) {
							LOGGER.info("Email sent successfully for sequence ID -> " + notification.getSeqNumber());
							if (notification.getNotificationType().equals("NORMAL")) {
								Integer status = notification.getTarget().equals("Internal") ? 1 : 2;
								notification.setEmailStatus(status);
								if (notification.getLinkedNotifications() != null) {
									for (NotificationInfo n: notification.getLinkedNotifications()) {
										n.setEmailStatus(status);
									}
								}
							}
						} else {
							LOGGER.warn("Email Notification failed for sequence ID -> " + notification.getSeqNumber() + "; Reason : " + response.getStatus() + " - " + response.getStatusInfo().getReasonPhrase());
						}
					} catch (Exception e) {
						LOGGER.warn("Email Notification failed for sequence ID -> " + notification.getSeqNumber(), e);
					}
				}
				if (notification.getSmsInfo() != null) {
					ClientResponse response = null;
					try {
						response = notification.getSmsResponse().get();
						if (response.getStatus() == Status.OK.getStatusCode()) {
							LOGGER.info("SMS sent successfully for sequence ID -> " + notification.getSeqNumber());
							if (notification.getNotificationType().equals("NORMAL")) {
								Integer status = notification.getTarget().equals("Internal") ? 1 : 2;
								notification.setSmsStatus(status);
								if (notification.getLinkedNotifications() != null) {
									for (NotificationInfo n: notification.getLinkedNotifications()) {
										n.setSmsStatus(status);
									}
								}
							}
						} else {
							LOGGER.warn("SMS Notification failed for sequence ID -> " + notification.getSeqNumber() + "; Reason : " + response.getStatus() + " - " + response.getStatusInfo().getReasonPhrase());
						}
					} catch (Exception e) {
						LOGGER.error("SMS Notification failed for sequence ID -> " + notification.getSeqNumber(), e);
					}
				}
			}
		} finally {
			
		}
	}

	private Future<ClientResponse> emailNotification(NotificationInfo notification) {
		return apiService.invokeAPI(PropertyHolder.getValue(Constant.NOTIFICATION_GATEWAY_EMAIL_URL), notification.getEmailInfo());
	}

	private Future<ClientResponse> smsNotification(NotificationInfo notification) {
		return apiService.invokeAPI(PropertyHolder.getValue(Constant.NOTIFICATION_GATEWAY_SMS_URL), notification.getSmsInfo());
	}
}
