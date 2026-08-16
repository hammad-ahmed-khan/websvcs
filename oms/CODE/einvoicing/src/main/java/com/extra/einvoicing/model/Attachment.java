package com.extra.einvoicing.model;

/**
 * @author aibrahim
 *
 */
public class Attachment {

	private String name;

	private String contentBody;

	private String contentType;

	public String getName() {
		return name;
	}

	public String getContentBody() {
		return contentBody;
	}

	public String getContentType() {
		return contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContentBody(String contentBody) {
		this.contentBody = contentBody;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
