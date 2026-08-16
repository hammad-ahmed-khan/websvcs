package com.order.notification.mail.bean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Email {
	
	@SerializedName("from")
	@Expose
	private String from;
	@SerializedName("fromName")
	@Expose
	private String fromName;
	@SerializedName("subject")
	@Expose
	private String subject;
	@SerializedName("text")
	@Expose
	private String text;
	@SerializedName("recipients")
	@Expose
	private Recipients recipients;
	@SerializedName("metadata")
	@Expose
	private Metadata metadata;
	
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

	public Recipients getRecipients() {
	return recipients;
	}

	public void setRecipients(Recipients recipients) {
	this.recipients = recipients;
	}

	public Metadata getMetadata() {
	return metadata;
	}

	public void setMetadata(Metadata metadata) {
	this.metadata = metadata;
	}

	}

