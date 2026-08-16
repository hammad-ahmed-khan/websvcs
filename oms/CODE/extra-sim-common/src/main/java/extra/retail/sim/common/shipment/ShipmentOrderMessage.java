package extra.retail.sim.common.shipment;

import oracle.retail.sim.common.business.MessageText;

/**
 * ShipmentOrderMessage.java
 * aibrahim
 * 2024
 */
public enum ShipmentOrderMessage implements MessageText {
	
	NO_LABEL_PATH_FOUND("Queue and Label file path not found");
	
	ShipmentOrderMessage(String message) {
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
