package com.extra.einvoicing.model;

/**
 * @author aibrahim
 *
 */
public class Message {

	private MessageType type;

	private String code;

	private String category;

	private String message;

	private String status;

	public MessageType getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getCategory() {
		return category;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
