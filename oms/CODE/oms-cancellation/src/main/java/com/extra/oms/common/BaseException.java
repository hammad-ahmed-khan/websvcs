/**
 * 
 */
package com.extra.oms.common;

/**
 * @author aibrahim
 *
 */
public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3998379068988230788L;

	private String code = "TECHNICAL_ERROR";

	public BaseException() {
		super();
	}

	public BaseException(String code) {
		super();
		this.code = code;
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String code, String message) {
		super(message);
		this.code = code;
	}

	public BaseException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
