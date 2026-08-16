package extra.retail.sim.client.screen.shipment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.NumberDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.client.screen.fulfillmentorder.ExtraFulfillmentOrderMessage;
import extra.retail.sim.client.swing.tableeditor.IMEITableEditor;
import extra.retail.sim.common.fulfillmentorder.FulfilmentShipmentProperty;

/**
 * aibrahim 2023
 */
public class ExtraFulfilmentOrderShipmentCreatePanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = -7459733967259143864L;

	private static final String FULFILMENT_ORDER_PICK_ITEM_SELECTED = "FulfilmentOrderPick.itemSelected";

	private ExtraFulfilmentOrderShipmentCreateModel model = new ExtraFulfilmentOrderShipmentCreateModel();

	private CartonComboBoxEditor cartonColumnEditor = new CartonComboBoxEditor();

	private RComboBoxEditor pickEditor = new RComboBoxEditor("Pick ID", true);
	private RDisplayLabelEditor deliveryEditor = new RDisplayLabelEditor("Delivery");

	private SimTable lineItemTable = new SimTable(new CustomerOrderShipmentItemsDefinition());

	public ExtraFulfilmentOrderShipmentCreatePanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		pickEditor.setDisplayer(new NumberDisplayer());

		pickEditor.registerAction(this, FULFILMENT_ORDER_PICK_ITEM_SELECTED);
	}

	private void layoutScreen() {
		REditorPanel detailPanel = new REditorPanel(1, 2);
		detailPanel.add(pickEditor);
		detailPanel.add(deliveryEditor);

		RDivider divider = new RDivider(RDivider.HORIZONTAL);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new SimTablePane(lineItemTable), GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

		setContentPane(mainPanel);
	}

	@Override
	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		try {
			if (FULFILMENT_ORDER_PICK_ITEM_SELECTED.equals(command)) {
				loadPickDetail((Long) pickEditor.getSelectedItem());
			}
		} catch (Exception e) {
			LogService.error(this, "Failed to process the command: " + command, e);
		}
	}

	@Override
	public SimScreenModel getScreenModel() {
		return model;
	}

	@Override
	public SimTable getScreenTable() {
		return lineItemTable;
	}

	@Override
	public void start() throws Throwable {
		populateScreen();
	}

	private void populateScreen() throws Exception {

		List<Long> pickIds = model.getFulfilmentOrderPickIds();
		pickEditor.setItems(pickIds);
		if (pickIds != null && !pickIds.isEmpty()) {
			Long selectedPickId = pickIds.get(0);
			pickEditor.setSelectedItem(selectedPickId);
		} else {
			displayWarning(ExtraFulfillmentOrderMessage.NO_PICK_FOUND);
			navigateLater("BACK");
		}
		pickEditor.removeEmptySelection();
	}

	private void loadPickDetail(Long selectedPickId) throws Exception {
		if (selectedPickId != null) {
			Collection<FulfillmentOrderShipmentItemWrapper> wrappers = model.getPickLineItemWrappers(selectedPickId);
			lineItemTable.setRows(wrappers);
			int exLength = cartonColumnEditor.getItems().length;
			if (wrappers.size() > exLength) {
				for (int i = exLength + 1; i <= wrappers.size(); i++) {
					cartonColumnEditor.addItem(i);
				}
			} else {
				for (int i = exLength; i > wrappers.size(); i--) {
					cartonColumnEditor.removeItem(i);
				}
			}
		}
	}

	public void handleSave() throws Exception {
		if (model.isDeliveryPending()) {
			model.savePendingDelivery();
			UIStatusUtility.displayMessage(this, ShipmentOrderMessage.DELIVERY_CREATE_SUCCESS);
		}
	}

	public void handleRequestAWB() throws Exception {
		if (model.isDeliveryPending()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.DELIVERY_PENDING);
			return;
		} else if (!model.isIMEISaved() || !model.isImeiPersisted()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.IMEI_NOT_SAVED);
			return;
		} else if (model.isAWBAssigned()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.AWB_REQUESTED);
			return;
		}
		model.requestAWB();
		lineItemTable.setTableEditable(false);
		UIStatusUtility.displayMessage(this, ShipmentOrderMessage.AWB_REQUEST_SUCCESS);
	}

	public void handlePrintAWB() throws Exception {
		if (!model.isAWBAssigned()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.AWB_NOT_ASSIGNED);
			return;
		} else if (!model.isImeiPersisted()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.IMEI_NOT_SAVED);
			return;
		}
		model.printAWB();
		UIStatusUtility.displayMessage(this, ShipmentOrderMessage.AWB_PRINT_REQUESTED);
	}

	public void handleCancelAWB() throws Exception {
		if (!model.isAWBAssigned()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.AWB_NOT_ASSIGNED);
			return;
		}
		model.cancelAWB();
		lineItemTable.setTableEditable(true);
		UIStatusUtility.displayMessage(this, ShipmentOrderMessage.AWB_CANCEL_SUCCESS);
	}

	public void handleCancelShipment() throws Exception {
		if (model.isAWBAssigned()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.AWB_ASSIGNED);
			return;
		} else if (model.isDeliveryPending()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.DELIVERY_PENDING);
			return;
		}
		model.cancelDelivery();
		UIStatusUtility.displayMessage(this, ShipmentOrderMessage.CANCEL_SHIPMENT_SUCCESS);
	}

	public void saveIMEI() throws Exception {
		if (model.isDeliveryPending()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.DELIVERY_PENDING);
			return;
		} else if (model.isImeiPersisted()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.IMEI_ALREADY_SAVED);
			return;
		}
		model.saveIMEI();
		UIStatusUtility.displayMessage(this, ShipmentOrderMessage.IMEI_SAVE_SUCCESS);
	}

	public void handleHOToCourier() throws Exception {
		if (!model.isAWBAssigned()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.AWB_NOT_ASSIGNED);
			return;
		} else if (!model.isImeiPersisted()) {
			UIStatusUtility.displayException(this, ShipmentOrderMessage.IMEI_NOT_SAVED);
			return;
		}
		model.handOOverToCourier();
	}

	private class CustomerOrderShipmentItemsDefinition extends SimTableDefinition {

		@Override
		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(7);
			attributes.add(new SimTableAttribute("SIM Customer Order ID", FulfillmentOrderProperty.FULFILL_ORDER_ID));
			attributes.add(new SimTableAttribute("Pick ID", FulfilmentShipmentProperty.FULFILL_PICK_ID));
			attributes.add(new SimTableAttribute("Item ID", FulfilmentShipmentProperty.ITEM_ID));
			attributes.add(new SimTableAttribute("Order Line Item ID", FulfilmentShipmentProperty.FULFILL_LINE_ITEM_ID));
			attributes.add(new SimTableAttribute("Quantity Picked", FulfilmentShipmentProperty.QTY_PICKED));
			attributes.add(new SimTableAttribute("Carton", FulfilmentShipmentProperty.CARTON_NO, cartonColumnEditor));
			attributes.add(new SimTableAttribute("IMEI", FulfilmentShipmentProperty.ITEM_IMEI, new IMEITableEditor()));
			attributes.add(new SimTableAttribute("AWB No", FulfilmentShipmentProperty.REQ_AWB_NO));
			return attributes;
		}

		@Override
		public Class<FulfillmentOrderShipmentItemWrapper> getDataClass() {
			return FulfillmentOrderShipmentItemWrapper.class;
		}
	}

	private class CartonComboBoxEditor extends RComboBoxTableEditor {

		private static final long serialVersionUID = 3111070411335714721L;

		private CartonComboBoxEditor() {
			super();
			setValueClass(Integer.class);
			removeEmptySelection();
		}
	}
}
