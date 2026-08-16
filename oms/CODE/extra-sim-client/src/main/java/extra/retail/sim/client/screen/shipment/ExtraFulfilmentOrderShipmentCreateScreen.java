package extra.retail.sim.client.screen.shipment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

import extra.retail.sim.client.core.ExtraSimNavigation;

/**
 * ExtraFulfilmentOrderShipmentCreatePanel.java 
 * aibrahim
 * 2023
 */
public class ExtraFulfilmentOrderShipmentCreateScreen extends SimScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -929187066826308387L;

	private ExtraFulfilmentOrderShipmentCreatePanel panel = new ExtraFulfilmentOrderShipmentCreatePanel();

	public ExtraFulfilmentOrderShipmentCreateScreen() {
		add(panel);
	}

	@Override
	public ScreenPanel getScreenPanel() {
		return panel;
	}

	@Override
	public String getScreenName() {
		return "Customer Delivery";
	}

	public void start() throws Throwable {
		showMenu();
		panel.start();
	}

	public void resume() throws Throwable {
		showMenu();
		panel.start();
	}

	public void stop() {
		
	}

	/****************************************************************************************************
	 * Navigation Methods
	 ***************************************************************************************************/
	public void performNavigationEvent(NavigationEvent event) {
		String command = event.getCommand();
		try {
			if (ExtraSimNavigation.DELIVERY.equals(command)) {
				panel.handleSave();
			} else if (ExtraSimNavigation.REQUEST_AWB.equals(command)) {
				panel.handleRequestAWB();
			} else if (ExtraSimNavigation.PRINT_AWB.equals(command)) {
				panel.handlePrintAWB();
			} else if (ExtraSimNavigation.CANCEL_AWB.equals(command)) {
				panel.handleCancelAWB();
			} else if (ExtraSimNavigation.CANCEL_SHIPMENT.equals(command)) {
				panel.handleCancelShipment();
			} else if (ExtraSimNavigation.IMEI.equals(command)) {
				panel.saveIMEI();
			} else if (ExtraSimNavigation.HANDOVER_TO_COURIER.equals(command)) {
				panel.handleHOToCourier();
			}
		} catch (Throwable exception) {
			displayException(panel, event, exception);
		}
	}
}
