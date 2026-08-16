package com.extra.notification.model;

import java.util.Collections;
import java.util.List;

public class Email {

	private String from;

	private String fromName;

	private String subject;
	
	private String text;

	private String html = "";

	private List<Object> attachments = Collections.emptyList();

	private List<String> replyTo = Collections.emptyList();

	private Recipient recipients;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Recipient getRecipients() {
		return recipients;
	}

	public void setRecipients(Recipient recipients) {
		this.recipients = recipients;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public List<Object> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Object> attachments) {
		this.attachments = attachments;
	}

	public List<String> getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(List<String> replyTo) {
		this.replyTo = replyTo;
	}
}
