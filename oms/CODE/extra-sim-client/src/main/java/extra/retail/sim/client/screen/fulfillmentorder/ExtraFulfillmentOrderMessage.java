package extra.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.common.business.MessageText;

/**
 * ExtraFulfillmentOrderMessage.java
 * aibrahim
 * 2023
 */
public enum ExtraFulfillmentOrderMessage implements MessageText {

	NO_PICK_FOUND("No pick found for this order.");

	ExtraFulfillmentOrderMessage(String message) {
		this.message = message;
	}

	private final String message;

	public String getCode() {
		return name();
	}

	public String getText() {
		return this.message;
	}
}
