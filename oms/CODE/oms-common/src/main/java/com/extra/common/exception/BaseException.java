package com.extra.common.exception;

/**
 * @author aibrahim
 *
 */
public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8512760370162265294L;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
