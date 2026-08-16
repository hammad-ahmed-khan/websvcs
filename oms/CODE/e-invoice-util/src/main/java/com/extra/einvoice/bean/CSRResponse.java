/**
 * 
 */
package com.extra.einvoice.bean;

/**
 * 
 */
public class CSRResponse {

	private String requestID;

	private String dispositionMessage;

	private String binarySecurityToken;

	private String secret;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getDispositionMessage() {
		return dispositionMessage;
	}

	public void setDispositionMessage(String dispositionMessage) {
		this.dispositionMessage = dispositionMessage;
	}

	public String getBinarySecurityToken() {
		return binarySecurityToken;
	}

	public void setBinarySecurityToken(String binarySecurityToken) {
		this.binarySecurityToken = binarySecurityToken;
	}
}
