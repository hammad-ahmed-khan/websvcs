/**
 * 
 */
package com.extra.notification.model;

/**
 * @author aibrahim
 *
 */
public class SMS {

	private String toNumber;

	private String body;

	private String fromNumber;

	public String getToNumber() {
		return toNumber;
	}

	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFromNumber() {
		return fromNumber;
	}

	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}
}
