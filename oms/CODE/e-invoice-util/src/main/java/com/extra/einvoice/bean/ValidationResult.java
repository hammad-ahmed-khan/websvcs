/**
 * 
 */
package com.extra.einvoice.bean;

import java.util.List;

/**
 * @author abubakkarSiddique
 *
 */
public class ValidationResult {

	private List<Message> infoMessages;

	private List<Message> warningMessages;

	private List<Message> errorMessages;

	private String status;

	public List<Message> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<Message> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public List<Message> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<Message> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<Message> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<Message> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
