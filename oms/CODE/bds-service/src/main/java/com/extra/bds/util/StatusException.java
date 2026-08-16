/**
 * 
 */
package com.extra.bds.util;

import com.extra.bds.bean.Status;

/**
 * @author aibrahim
 *
 */
public class StatusException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6638397306690714544L;

	private Status errorStatus;

	public StatusException(Status status, Throwable t) {
		super(t);
		this.errorStatus = status;
	}

	public StatusException(Status status) {
		super();
		this.errorStatus = status;
	}

	public StatusException(String code, String message) {
		super();
		this.errorStatus = new Status();
		errorStatus.setCode(code);
		errorStatus.setMessage(message);
	}

	public Status getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(Status errorStatus) {
		this.errorStatus = errorStatus;
	}
}
