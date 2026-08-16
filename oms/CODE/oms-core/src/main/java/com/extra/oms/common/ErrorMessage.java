package com.extra.oms.common;

/**
 * @author aibrahim
 *
 */
public enum ErrorMessage {

	INVALID_QUANTITY_ON_CANCEL("Items already delivered / cancelled"), 
		CANCEL_QUANTITY_MAX_REACHED("Requested quantity is more than the pending quantity"),
			STOCK_NOT_AVAILABLE("Stock not available in the requested location");

	private String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
