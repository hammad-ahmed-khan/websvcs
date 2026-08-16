package com.extra.einvoicing.util;

/**
 * @author aibrahim
 * Just for validation purpose.
 */
public class SoftException extends Exception {

	private static final long serialVersionUID = -8629609670121210027L;

	public SoftException(String message) {
		super(message);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		/*
		 * Override to avoid filling the stack.
		 */
		return this;
	}
}
