package extra.retail.sim.client.screen.item;

import oracle.retail.sim.common.business.MessageText;

/**
 * ShipmentOrderMessage.java
 * aibrahim
 * 2023
 */
public enum ItemLookupMessage implements MessageText {

	NO_ROW("Please select row(s)");
	
	ItemLookupMessage(String message) {
		this.message = message;
	}

	private final String message;

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getText() {
		return this.message;
	}
}
