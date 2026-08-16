package com.extra.notification.model;

import java.util.List;
import java.util.concurrent.Future;
import com.sun.jersey.api.client.ClientResponse;

public class NotificationInfo {

	private Long seqNumber;

	private String messageTye;

	private String target;

	private String eventSource;

	private String orderNo;

	private String itemName;

	private String storeName;

	private String notificationType;

	private String body;

	private Integer emailStatus;

	private Integer smsStatus;
	
	private EmailInfo emailInfo;

	private SMSInfo smsInfo;

	private Integer pickStatus;

	private List<NotificationInfo> linkedNotifications;

	private Future<ClientResponse> emailResponse;

	private Future<ClientResponse> smsResponse;

	public Long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(Long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getMessageTye() {
		return messageTye;
	}

	public void setMessageTye(String messageTye) {
		this.messageTye = messageTye;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public EmailInfo getEmailInfo() {
		return emailInfo;
	}

	public void setEmailInfo(EmailInfo emailInfo) {
		this.emailInfo = emailInfo;
	}

	public SMSInfo getSmsInfo() {
		return smsInfo;
	}

	public void setSmsInfo(SMSInfo smsInfo) {
		this.smsInfo = smsInfo;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	public Integer getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(Integer smsStatus) {
		this.smsStatus = smsStatus;
	}

	public void setPickStatus(Integer pickStatus) {
		this.pickStatus = pickStatus;
	}

	public Integer getPickStatus() {
		return pickStatus;
	}

	public List<NotificationInfo> getLinkedNotifications() {
		return linkedNotifications;
	}

	public void setLinkedNotifications(List<NotificationInfo> linkedNotifications) {
		this.linkedNotifications = linkedNotifications;
	}

	public Future<ClientResponse> getEmailResponse() {
		return emailResponse;
	}

	public void setEmailResponse(Future<ClientResponse> emailResponse) {
		this.emailResponse = emailResponse;
	}

	public Future<ClientResponse> getSmsResponse() {
		return smsResponse;
	}

	public void setSmsResponse(Future<ClientResponse> smsResponse) {
		this.smsResponse = smsResponse;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
