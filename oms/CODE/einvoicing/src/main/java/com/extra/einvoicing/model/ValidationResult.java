package com.extra.einvoicing.model;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aibrahim
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

	public List<Message> getWarningMessages() {
		return warningMessages;
	}

	public List<Message> getErrorMessages() {
		return errorMessages;
	}

	public String getStatus() {
		return status;
	}

	public void setInfoMessages(List<Message> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public void setWarningMessages(List<Message> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public void setErrorMessages(List<Message> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// EAT Exception
			return "";
		}
	}
}
