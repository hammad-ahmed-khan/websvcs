package extra.retail.sim.client.core;

/********************************************************************************************************
 * This class contains some common SIM navigation commands. Each of the menu
 * items has a static variable declaration here for readability and consistency.
 * The value is the button identifier, which is also its label and its
 * identifier when declared in that navigation.xml configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraSimNavigation {

	public static final String DELIVERY = "Delivery";

	public static final String CREATE_PICK = "Create Pick";

	public static final String REQUEST_AWB = "Request AWB";

	public static final String PRINT_AWB = "Print AWB";

	public static final String CANCEL_AWB = "Cancel AWB";

	public static final String CANCEL_SHIPMENT = "Cancel Shipment";

	public static final String IMEI = "IMEI";

	public static final String HANDOVER_TO_COURIER = "Handed to Courier";

	public static final String BIN_LOOKUP = "Bin Lookup";

	private ExtraSimNavigation() {
	}
}
