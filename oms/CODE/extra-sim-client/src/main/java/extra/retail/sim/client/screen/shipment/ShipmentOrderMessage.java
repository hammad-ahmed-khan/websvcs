package extra.retail.sim.client.screen.shipment;

import oracle.retail.sim.common.business.MessageText;

/**
 * ShipmentOrderMessage.java
 * aibrahim
 * 2023
 */
public enum ShipmentOrderMessage implements MessageText {

	DELIVERY_PENDING("Please create delivery for the picked items"),

	AWB_REQUESTED("AWB already requested for the order "),
	
	AWB_ASSIGNED("AWB already assigned, can't cancel the shipment"), 
	
	AWB_NOT_ASSIGNED("AWB not assigned"), 

	IMEI_EMPTY("IMEI shouldn't be empty"),

	IMEI_NOT_SAVED("IMEI should be saved before AWB request."), 
	
	AWB_CANCEL_SUCCESS("AWB cancelled successfully"), 

	CANCEL_SHIPMENT_SUCCESS("Shipment cancelled successfully"), 

	IMEI_SAVE_SUCCESS("IMEI saved successfully"), 

	DELIVERY_CREATE_SUCCESS("Delivery created successfully"), 
	
	AWB_REQUEST_SUCCESS("AWB Requested successfully"), 
	
	IMEI_ALREADY_SAVED("IMEI saved already"), 

	AWB_PRINT_REQUESTED("AWB Print Requested"), 

	HO_TO_COURIER_PRINTED("AWB Printed");
	
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
