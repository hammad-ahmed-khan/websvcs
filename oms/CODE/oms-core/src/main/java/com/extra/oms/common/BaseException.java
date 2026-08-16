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

	private String code = "";

	public BaseException() {
		super();
		code = "TECHINAL_ERROR";
	}

	public BaseException(Throwable t) {
		super(t);
		code = "TECHINAL_ERROR";
	}

	public BaseException(String code) {
		super();
		this.code = code;
	}

	public BaseException(String code, Throwable t) {
		super(t);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
