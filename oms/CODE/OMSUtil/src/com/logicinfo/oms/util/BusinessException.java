/**
 * 
 */
package com.logicinfo.oms.util;

/**
 * @author aibrahim
 *
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	public BusinessException(String code) {
		super();
		this.code = code;
	}

	public BusinessException(String code, Throwable t) {
		super(t);
		this.code = code;
	}

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(String code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
