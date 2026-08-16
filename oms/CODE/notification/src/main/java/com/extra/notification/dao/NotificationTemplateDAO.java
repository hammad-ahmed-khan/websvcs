package com.extra.notification.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.log4j.Logger;

import com.extra.notification.common.BaseException;
import com.extra.notification.common.Constant;
import com.extra.notification.model.Email;
import com.extra.notification.model.EmailInfo;
import com.extra.notification.model.MetaInfo;
import com.extra.notification.model.NotificationInfo;
import com.extra.notification.model.Recipient;
import com.extra.notification.model.SMS;
import com.extra.notification.model.SMSInfo;
import com.extra.notification.model.To;
import com.extra.notification.util.DBUtill;
import com.extra.notification.util.PropertyHolder;

@SuppressWarnings("deprecation")
public class NotificationTemplateDAO {

	private static Logger LOGGER = Logger.getLogger(NotificationTemplateDAO.class);

	private static final String GET_NOTIFICATION_MESSAGE_QUERY = "SELECT D.SEQ_NO, C.MESSAGE_TYPE, C.TARGET, C.EVENT_SOURCE, EN_SUBJECT, AR_SUBJECT, C.EN_MSG, C.AR_MSG, CASE WHEN C.TARGET = 'Internal' THEN SS.MOBILE_NO ELSE D.MOBILE_NO END MOBILE_NO, CASE WHEN C.TARGET = 'Internal' THEN SS.EMAILID ELSE D.EMAIL_ADDRESS END EMAILID, D.EMAIL_STATUS_CODE, D.SMS_STATUS_CODE, D.PICK_STATUS, D.CUST_ORDER_NO, D.ITEM, D.ITEM_DESC, D.EVENT_LOCATION, D.STORE_NAME3, C.EVENT_ID, D.CUSTOMER_NAME, D.CUSTOMER_LN, (SELECT AB.TRACKING_URL FROM ESU_AWB AB WHERE AB.EVENT_ID = C.EXT_EVENT_ID AND AB.COURIER_NAME = UPPER(D.COURIER_NAME)) TRACKING_URL, D.AWB_NO, D.COURIER_NAME, D.PROMISED_DATE FROM ESU_DETAIL D , ESU_CONFIG C, (SELECT STOREID, CASE WHEN PHONE IN (SELECT PHONENO FROM RESTRICTED_MAIL) THEN NULL ELSE PHONE END MOBILE_NO, CASE WHEN EMAILD in (SELECT EMAILID FROM RESTRICTED_MAIL) THEN NULL ELSE EMAILD END EMAILID from XXK_STORE_CONTACT_V SS) SS WHERE (D.EMAIL_STATUS_CODE = ? OR D.SMS_STATUS_CODE = ?) AND D.NOTIFCIATION_ID = C.NOTFICIATION_ID AND SS.STOREID (+) = D.EVENT_LOCATION";

	private static final String UPDATE_NOTIFICATION_QUERY = "UPDATE ESU_DETAIL SET LAST_UPDATE_DATETIME = SYSDATE";

	private static final String GET_ORDER_STATUS_QUERY = "SELECT CUST_ORDER_ID, STATUS FROM FUL_ORD WHERE CUST_ORDER_ID IN (";

	public List<NotificationInfo> getNotificationMessages(Connection connection, String notificationType) throws BaseException {

		List<NotificationInfo> notifications = null;
		ResultSet rs = null;
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Queue<Object> params = new LinkedList<Object>();
			if ("NORMAL".equals(notificationType)) {
				params.add(0);
				params.add(0);
			} else {
				params.add(1);
				params.add(1);
			}
			PreparedStatement statement = connection.prepareStatement(GET_NOTIFICATION_MESSAGE_QUERY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
			int paramIndex = 1;
			while(!params.isEmpty()) {
				statement.setObject(paramIndex++, params.poll());
			}
			statement.setFetchSize(100);
			final String fromAddress = PropertyHolder.getValue(Constant.EMAIL_FROM_ADDRESS);
			final String fromName = PropertyHolder.getValue(Constant.EMAIL_FROM_NAME);
			final String senderName = PropertyHolder.getValue(Constant.SMS_SENDER_NAME);
			LOGGER.info("Fetching data from database");
			rs = statement.executeQuery();
			LOGGER.info("Data fetched successfully from database");
			notifications = new ArrayList<NotificationInfo>();
			Map<String, String> formatParams = new HashMap<String, String>();
			Map<String, NotificationInfo> notificationMap = new HashMap<String, NotificationInfo>();
			HashSet<Long> sequenceSet = new HashSet<Long>();
			Map<String, Integer> orderStatusMap = null;
			int increment = 0;
			if ("REMAINDER".equals(notificationType)) {
				StringBuilder ordStatusQryBuilder = new StringBuilder();
				while(rs.next()) {
					if (increment == 1000) {
						increment = 0;
						ordStatusQryBuilder.append(") OR CUST_ORDER_ID IN (");
					} else if (ordStatusQryBuilder.length() > 0) {
						ordStatusQryBuilder.append(",");
					}
					ordStatusQryBuilder.append("'").append(rs.getString(14)).append("'");
					increment++;
				}
				if (ordStatusQryBuilder.length() > 0) {
					ordStatusQryBuilder.insert(0, GET_ORDER_STATUS_QUERY).append(")");
					orderStatusMap = getOrderStatus(ordStatusQryBuilder.toString());
					rs.beforeFirst();
				}
			}
			if (orderStatusMap == null) {
				orderStatusMap = Collections.emptyMap();
			}
			String[] replacemenText = StringUtils.split(StringUtils.defaultString(PropertyHolder.getValue(Constant.NOTIFICATION_REPLACEMENT_TEXT)), "~");
			while(rs.next()) {
				NotificationInfo notification = null;
				String orderNumber = rs.getString(14);
				String eventType = rs.getString(19);
				notification = notificationMap.get(orderNumber + "~" + eventType);
				if (notification == null) {
					notification = new NotificationInfo();
					notification.setSeqNumber(rs.getLong(1));
					notification.setMessageTye(rs.getString(2));
					notification.setTarget(rs.getString(3));
					notification.setNotificationType(notificationType);
					notification.setOrderNo(orderNumber);
				}
				String loc = rs.getString(18);
				String locId = rs.getString(17);
				formatParams.put("orderNumber", orderNumber);
				formatParams.put("itemName", rs.getString(16));
				formatParams.put("storeName", loc);
				formatParams.put("customerName", rs.getString(20));
				
				Date psDate = rs.getTimestamp(25);
				if (psDate != null) {
					formatParams.put("promiseDate", format.format(psDate));
				}

				String awbURL = rs.getString(22);
				String awbNo = rs.getString(23);
				formatParams.put("CourierName", rs.getString(24));
				formatParams.put("AWB_Number", awbNo);
				if (StringUtils.isNotBlank(awbURL) && StringUtils.isNotBlank(awbNo)) {
					formatParams.put("TRACKING_URL", awbURL.replace("{@@}", awbNo));
				} else {
					formatParams.put("TRACKING_URL", "");
				}

				String langCode = rs.getString(21);
				String body = rs.getString(8);
				if (langCode == null || "en".equalsIgnoreCase(langCode) || StringUtils.isBlank(body)) {
					body = rs.getString(7);
				}
				body = StrSubstitutor.replace(body, formatParams);
				if (StringUtils.isBlank(awbURL)) {
					for(String text: replacemenText) {
						body = body.replace(text, "");
					}
				}
				if (!notificationMap.containsKey(orderNumber + "~" + eventType)) {
					LOGGER.info("New notifcation sequence id -> " + notification.getSeqNumber());
					notification.setBody(body);
					int emailStatus = rs.getInt(11);
					Integer deliveryStatus = orderStatusMap.get(orderNumber);
					if (deliveryStatus != null && deliveryStatus > 0) {
						LOGGER.info("Order " + orderNumber + " is picked for delivery.");
						notification.setEmailStatus(2);
						notification.setSmsStatus(2);
						notification.setPickStatus(1);
					} else {
						if ((notification.getMessageTye().equals("BOTH") || notification.getMessageTye().equals("EMAIL")) && (("NORMAL".equals(notificationType) && emailStatus == 0) || ("REMAINDER".equals(notificationType) && emailStatus == 1))) {
							String toEmail = rs.getString(10);
							if (toEmail == null || "".equals(toEmail)) {
								LOGGER.warn("Email ID is missing for the Sequence No. " + notification.getSeqNumber());
							} else {
								EmailInfo emailInfo = new EmailInfo();
								Email email = new Email();
								email.setFrom(fromAddress);
								email.setFromName(fromName);
								String subject = rs.getString(6);
								if (langCode == null || "en".equalsIgnoreCase(langCode) || StringUtils.isBlank(subject)) {
									subject = rs.getString(5);
								}
								email.setSubject(StrSubstitutor.replace(subject, formatParams));
								email.setText(body);
								String[] receipents = toEmail.trim().split(",");
								Recipient recipient = new Recipient();
								recipient.setTo(new ArrayList<To>(receipents.length));
								for (String receipnt: receipents) {
									recipient.getTo().add(new To(receipnt));
								}
								email.setRecipients(recipient);
								emailInfo.setEmail(email);
								notification.setEmailInfo(emailInfo);
								LOGGER.info("Email ID for the Sequence No. " +  notification.getSeqNumber() + " and order number " + orderNumber + " is " + toEmail);
							}
						}
						int smsStatus = rs.getInt(12);
						if ((notification.getMessageTye().equals("BOTH") || notification.getMessageTye().equals("SMS")) && (("NORMAL".equals(notificationType) && smsStatus == 0) || ("REMAINDER".equals(notificationType) && smsStatus == 1))) {
							String toMobile = rs.getString(9);
							if (toMobile == null || "".equals(toMobile)) {
								LOGGER.warn("Mobile number is missing for the Sequence No. " + notification.getSeqNumber());
							} else {
								SMSInfo smsInfo = new SMSInfo();
								SMS sms = new SMS();
								sms.setToNumber(toMobile);
								sms.setBody(body.replaceAll("\\<[^>]*>", ""));
								sms.setFromNumber(senderName);
								MetaInfo info = new MetaInfo();
								info.setCampaignType("TRANSACTIONAL");
								if (locId.startsWith("1")) {
									info.setCountry("SA");
								} else if (locId.startsWith("2")) {
									info.setCountry("BH");
								} else if (locId.startsWith("3")) {
									info.setCountry("OM");
								}
								smsInfo.setSmsData(sms);
								smsInfo.setMetadata(info);
								notification.setSmsInfo(smsInfo);
								LOGGER.info("SMS Mobile number for the Sequence No. " +  notification.getSeqNumber() + " and order number " + orderNumber + " is " + toMobile);
							}
						}
					}
					if ((deliveryStatus == null || deliveryStatus.intValue() == 0) && notification.getEmailInfo() == null && notification.getSmsInfo() == null) {
						LOGGER.warn("Mobile detail and SMS detail are not available for the Sequence No. " + notification.getSeqNumber());
					} else {
						notifications.add(notification);
						sequenceSet.add(notification.getSeqNumber());
						notificationMap.put(orderNumber + "~" + eventType, notification);
					}
				} else if (!notification.getBody().contains(body)) {
					NotificationInfo exInfo = new NotificationInfo();
					exInfo.setSeqNumber(rs.getLong(1));
					LOGGER.info("Notification exist. Adding the sequence id -> " + exInfo.getSeqNumber() + " to existing sequence number " + notification.getSeqNumber());
					if (notification.getLinkedNotifications() == null) {
						notification.setLinkedNotifications(new ArrayList<NotificationInfo>());
					}
					sequenceSet.add(exInfo.getSeqNumber());
					notification.getLinkedNotifications().add(exInfo);
					if (notification.getEmailInfo() != null) {
						notification.getEmailInfo().getEmail().setText(notification.getEmailInfo().getEmail().getText() + "<br/><br/>" + body);
					}
					if (notification.getSmsInfo() != null) {
						notification.getSmsInfo().getSmsData().setBody(notification.getSmsInfo().getSmsData().getBody() + " \\n " + body.replaceAll("\\<[^>]*>", ""));
					}
				} else {
					LOGGER.info("Notification for new contact. Adding the sequence id -> " + rs.getLong(1) + " to existing sequence number " + notification.getSeqNumber());
					Integer deliveryStatus = orderStatusMap.get(orderNumber);
					if (deliveryStatus != null && deliveryStatus > 0) {
						LOGGER.info("Order " + orderNumber + " is picked for delivery.");
						NotificationInfo exInfo = new NotificationInfo();
						exInfo.setSeqNumber(rs.getLong(1));
						exInfo.setEmailStatus(2);
						exInfo.setSmsStatus(2);
						exInfo.setPickStatus(1);
						LOGGER.info("Updating delivery status adding the sequence id -> " + exInfo.getSeqNumber() + " to existing sequence number " + notification.getSeqNumber());
						if (notification.getLinkedNotifications() == null) {
							notification.setLinkedNotifications(new ArrayList<NotificationInfo>());
						}
						notification.getLinkedNotifications().add(exInfo);
					} else {
						String messageType = StringUtils.defaultString(rs.getString(2));
						int emailStatus = rs.getInt(11);
						if ((messageType.equals("BOTH") || messageType.equals("EMAIL")) && (("NORMAL".equals(notificationType) && emailStatus == 0) || ("REMAINDER".equals(notificationType) && emailStatus == 1))) {
							String toEmail = rs.getString(10);
							if (toEmail == null || "".equals(toEmail)) {
								LOGGER.warn("Email ID is missing for the Sequence No. " + notification.getSeqNumber());
							} else {
								EmailInfo emailInfo = notification.getEmailInfo();
								if (emailInfo != null) {
									String[] receipents = toEmail.trim().split(",");
									List<To> toList = emailInfo.getEmail().getRecipients().getTo();
									for (String receipnt: receipents) {
										To to = new To(receipnt);
										if (!emailInfo.getEmail().getRecipients().getTo().contains(to)) {
											toList.add(to);
										}
									}
								} else {
									emailInfo = new EmailInfo();
									Email email = new Email();
									email.setFrom(fromAddress);
									email.setFromName(fromName);
									String subject = rs.getString(6);
									if (langCode == null || "en".equalsIgnoreCase(langCode) || StringUtils.isBlank(subject)) {
										subject = rs.getString(5);
									}
									email.setSubject(StrSubstitutor.replace(subject, formatParams));
									email.setText(body);
									String[] receipents = toEmail.trim().split(",");
									Recipient recipient = new Recipient();
									recipient.setTo(new ArrayList<To>(receipents.length));
									for (String receipnt: receipents) {
										recipient.getTo().add(new To(receipnt));
									}
									email.setRecipients(recipient);
									emailInfo.setEmail(email);
									notification.setEmailInfo(emailInfo);
								}
								NotificationInfo exInfo = new NotificationInfo();
								exInfo.setSeqNumber(rs.getLong(1));
								if (!sequenceSet.contains(exInfo.getSeqNumber())) {
									LOGGER.info("Adding the sequence id -> " + exInfo.getSeqNumber() + " to existing sequence number " + notification.getSeqNumber());
									if (notification.getLinkedNotifications() == null) {
										notification.setLinkedNotifications(new ArrayList<NotificationInfo>());
									}
									sequenceSet.add(exInfo.getSeqNumber());
									notification.getLinkedNotifications().add(exInfo);
								}
							}
						}

						int smsStatus = rs.getInt(12);
						if ((messageType.equals("BOTH") || messageType.equals("SMS")) && (("NORMAL".equals(notificationType) && smsStatus == 0) || ("REMAINDER".equals(notificationType) && smsStatus == 1))) {
							String toMobile = rs.getString(9);
							if (toMobile == null || "".equals(toMobile)) {
								LOGGER.warn("Mobile number is missing for the Sequence No. " + rs.getLong(1));
							} else {
								notification = new NotificationInfo();
								notification.setSeqNumber(rs.getLong(1));
								notification.setMessageTye(messageType);
								notification.setTarget(rs.getString(3));
								notification.setNotificationType(notificationType);
								notification.setOrderNo(orderNumber);
								notification.setBody(body);

								SMSInfo smsInfo = new SMSInfo();
								SMS sms = new SMS();
								sms.setToNumber(toMobile);
								sms.setBody(body.replaceAll("\\<[^>]*>", ""));
								sms.setFromNumber(senderName);
								MetaInfo info = new MetaInfo();
								info.setCampaignType("TRANSACTIONAL");
								if (locId.startsWith("1")) {
									info.setCountry("SA");
								} else if (locId.startsWith("2")) {
									info.setCountry("BH");
								} else if (locId.startsWith("3")) {
									info.setCountry("OM");
								}
								smsInfo.setSmsData(sms);
								smsInfo.setMetadata(info);
								notification.setSmsInfo(smsInfo);
								notifications.add(notification);
								if (!sequenceSet.contains(notification.getSeqNumber())) {
									sequenceSet.add(notification.getSeqNumber());
								}
							}
						}
					}
				}
				formatParams.clear();
			}
		} catch (Exception e) {
			LOGGER.error("Error while fetching the notification data", e);
			throw new BaseException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
		return notifications;
	}

	private Map<String, Integer> getOrderStatus(String query) {
		String url = PropertyHolder.getValue(Constant.SIM_DATABASE_URL);
		String userName = PropertyHolder.getValue(Constant.SIM_DATABASE_USERNAME);
		String password = PropertyHolder.getValue(Constant.SIM_DATABASE_PASSWORD);
		Connection connection = null;
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Creating database connection to get the order status from SIM database");
			LOGGER.info("SIM Database URL -> " + url);
			LOGGER.info("SIM Database username -> " + userName);
			LOGGER.info("SIM Database password -> " + password);
		}
		Map<String, Integer> statusMap = new HashMap<String, Integer>();
		try {
			connection = DriverManager.getConnection(url, userName, password);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("SIM Database connection created successfully.");
			}
			ResultSet statusRS = connection.prepareStatement(query).executeQuery();
			while(statusRS.next()) {
				statusMap.put(statusRS.getString(1), statusRS.getInt(2));
			}
			statusRS.close();
		} catch (SQLException e) {
			LOGGER.error("Error while getting the connection", e);
			throw new BaseException(e);
		} finally {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Closing the SIM database connection....");
			}
			DBUtill.closeConnection(connection);
		}
		return statusMap;
	}

	public void updateNotificationStatus(Connection connection, List<NotificationInfo> notifications) {

		try {
			Statement statement = connection.createStatement();
			for(NotificationInfo notification: notifications) {
				if (notification.getEmailStatus() != null || notification.getSmsStatus() != null) {
					statement.addBatch(addUpdateQuery(notification));
					if (notification.getLinkedNotifications() != null) {
						for(NotificationInfo n: notification.getLinkedNotifications()) {
							if (n.getEmailStatus() != null || n.getSmsStatus() != null) {
								statement.addBatch(addUpdateQuery(n));
							}
						}
					}
				}
			}
			statement.executeBatch();
		} catch (Exception e) {
			LOGGER.error("Error while upating the status", e);
		}
	}

	private String addUpdateQuery(NotificationInfo notification) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(UPDATE_NOTIFICATION_QUERY);
		if (notification.getEmailStatus() != null) {
			queryBuilder.append(", EMAIL_STATUS_CODE = ").append(notification.getEmailStatus());
		}
		if (notification.getSmsStatus() != null) {
			queryBuilder.append(", SMS_STATUS_CODE = ").append(notification.getSmsStatus());
		}
		if (notification.getPickStatus() != null) {
			queryBuilder.append(", PICK_STATUS = ").append(notification.getPickStatus());
		}
		queryBuilder.append(" WHERE SEQ_NO = " + notification.getSeqNumber());
		return queryBuilder.toString();
	}
}
