package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import extra.retail.sim.client.swing.displayer.IMEIPickedQuantityDisplayer;
import extra.retail.sim.client.swing.displayer.StringDisplayer;
import extra.retail.sim.client.swing.tableeditor.IMEITableEditor;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.notes.NotesDialog;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;

public class IMEIFulfillmentOrderDeliveryDetailPanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = -6817231662399678268L;

	private IMEIFulfillmentOrderDeliveryDetailModel model = new IMEIFulfillmentOrderDeliveryDetailModel();

	private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
	private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
	private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
	private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");

	private StockItemScannerDialog scannerDialog = null;

	private SimTable lineItemTable = new SimTable(new CustomerOrderDeliveryItemsDefinition());
	private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

	/****************************************************************************************************
	 * Build Panel
	 ***************************************************************************************************/

	public IMEIFulfillmentOrderDeliveryDetailPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		simFulfilOrderIdEditor.setDisplayer(new IdNameDisplayer());
		customerOrderIdEditor.setDisplayer(new IdNameDisplayer());
		fulfillmentOrderIdEditor.setDisplayer(new IdNameDisplayer());
		orderStatusEditor.setDisplayer(new TranslatedObjectDisplayer());

		lineItemTable.setColumnSize(FulfillmentOrderProperty.REMAINING_QTY_BASED_ON_UOM,
				EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(FulfillmentOrderProperty.ORDERED_QTY_BASED_ON_UOM,
				EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(FulfillmentOrderProperty.PICKED_QTY_BASED_ON_UOM,
				EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(FulfillmentOrderProperty.DELIVERED_QTY_BASED_ON_UOM,
				EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(FulfillmentOrderProperty.CANCELED_QTY_BASED_ON_UOM,
				EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);

		lineItemTable.setColumnSize(FulfillmentOrderProperty.SERIAL_NUMBER_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);

	}

	private void layoutScreen() {
		REditorPanel detailPanel = new REditorPanel(5, 4);
		detailPanel.add(customerOrderIdEditor);
		detailPanel.add(fulfillmentOrderIdEditor);
		detailPanel.add(simFulfilOrderIdEditor);
		detailPanel.add(orderStatusEditor);
		REditorPanel commentPanel = new REditorPanel(1, 1);

		RDivider divider = new RDivider(RDivider.HORIZONTAL);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(commentPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(lineItemPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

		LayoutUtility.alignPanels(detailPanel, commentPanel);

		setContentPane(mainPanel);
	}

	public SimScreenModel getScreenModel() {
		return model;
	}

	public SimTable getScreenTable() {
		return lineItemTable;
	}

	/****************************************************************************************************
	 * State Methods for Screen
	 ***************************************************************************************************/

	public boolean isViewOnlyMode() {
		return model.isViewOnlyMode();
	}

	public void clearViewOnly() {
		model.clearViewOnly();
	}

	public boolean isDeliveryClosed() {
		return model.isDeliveryClosed();
	}

	public boolean isDeliverySubmitted() {
		return model.isDeliverySubmitted();
	}

	public boolean isCancelSubmitAllowed() {
		return model.isCancelSubmitAllowed();
	}

	public boolean isDispatchAllowed() {
		return model.isDispatchAllowed();
	}

	public boolean isSubmitAllowed() {
		return model.isSubmitAllowed();
	}

	public boolean confirmActivityLock() throws Exception {
		return model.checkLock();
	}

	public boolean isWebOrder() {
		return model.isWebOrder();
	}

	public boolean isShipmentType() {
		return model.isShipmentType();
	}

	/****************************************************************************************************
	 * Initialize Panel
	 ***************************************************************************************************/

	public void start() throws Exception {
		model.loadDelivery();
		populateScreen();
	}

	private void populateScreen() throws Exception {
		FulfillmentOrder order = model.getFulfillmentOrder();

		simFulfilOrderIdEditor.setData(order.getId());
		customerOrderIdEditor.setData(order.getCustomerOrderId());
		fulfillmentOrderIdEditor.setData(order.getExternalId());
		orderStatusEditor.setData(order.getStatus());

		lineItemTable.setRows(model.getDeliveryItems());
		lineItemTable.setTableEditable(true);
	}

	public void stop() {
		shutdownScanner();
	}

	/****************************************************************************************************
	 * Scanner Methods
	 ***************************************************************************************************/

	public boolean isScannerAvailable() {
		return model.isScannerAvailable();
	}

	private void shutdownScanner() {
		if (scannerDialog != null) {
			scannerDialog.setVisible(false);
			scannerDialog = null;
		}
	}

	/****************************************************************************************************
	 * Handle Save
	 ***************************************************************************************************/

	public boolean handleSave() throws Exception {
		lineItemTable.stopEditing();
		try {
			return model.saveDelivery();

		} catch (UIException exception) {
			if (exception.getMessage().contains("unique constraint")) {
				throw new UIException(CommonMessageText.IMEI_EXISTS);
			} else if (exception.getMessage().contains("IMEI Qty cannot be more than Picked Quantity")) {
				throw new UIException(CommonMessageText.IMEI_QUANTITY_GREATER_THAN_PICKED_QUANTITY);
			} else if (exception.getMessage().contains("Please Enter The IMEI Number")) {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			} else if (exception.getMessage().contains("Cannot enter the IMEI with special characters.")) {
				throw new UIException(CommonMessageText.SPECIAL_CHARACTERS);
			} else if (exception.getMessage().contains("Cannot enter the IMEI with first/last character as space.")) {
				throw new UIException(CommonMessageText.SPACE_EXISTS);
			} else {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			}

		} catch (Exception exception) {
			if (exception.getMessage().contains("unique constraint")) {
				throw new UIException(CommonMessageText.IMEI_EXISTS);
			} else if (exception.getMessage().contains("IMEI Qty cannot be more than Picked Quantity")) {
				throw new UIException(CommonMessageText.IMEI_QUANTITY_GREATER_THAN_PICKED_QUANTITY);
			} else if (exception.getMessage().contains("Invalid IMEI Number")) {
				throw new UIException(CommonMessageText.IMEI_INVALID_NUMBER);
			} else {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			}
		}
	}

	/****************************************************************************************************
	 * Handle Cancel
	 ***************************************************************************************************/

	public void handleCancel() throws Exception {
		try {
			String handleCancelIMEI = model.handleCancelIMEI();
			System.out.println("Handle Cancel : " + handleCancelIMEI);
		} catch (Exception exception) {
			throw new UIException(CommonMessageText.IMEI_GENERAL);
		}
	}

	/****************************************************************************************************
	 * Handle Submit
	 ***************************************************************************************************/

	public boolean handleCancelSubmit() throws Exception {
		if (RConfirmUtility.confirm("Cancel Submit Delivery Confirmation",
				FulfillmentOrderMessageText.CANCEL_SUBMIT_CONFIRM)) {
			model.cancelSubmitDelivery();
			model.releaseLock();
			return true;
		}
		return false;
	}

	/****************************************************************************************************
	 * Handle Dispatch
	 ***************************************************************************************************/

	/****************************************************************************************************
	 * Handle Refresh
	 ***************************************************************************************************/

	public void handleRefresh() {
		try {
			model.refreshDeliveryData();
			populateScreen();
		} catch (Exception exception) {
			displayException(exception);
		}
	}

	/****************************************************************************************************
	 * Handle Scanner
	 ***************************************************************************************************/

	/****************************************************************************************************
	 * Handle Notes
	 ***************************************************************************************************/

	public void handleNotes() {
		try {
			NotesDialog dialog = new NotesDialog();
			dialog.setTitle("Customer Order Notes");
			dialog.loadNotes(FunctionalArea.CUSTOMER_ORDER, model.getFulfillmentOrder().getId(),
					model.isNotesEditable());
			dialog.setVisible(true);
		} catch (Exception exception) {
			displayException(exception);
		}
	}

	/****************************************************************************************************
	 * Handle Bill Of Lading
	 ***************************************************************************************************/

	public void storeDeliveryForBillOfLading() {
		model.storeDeliveryForBillOfLading();
	}

	/****************************************************************************************************
	 * Screen Actions
	 ***************************************************************************************************/

	public void performActionEvent(RActionEvent event) {
	}

	/****************************************************************************************************
	 * Helper methods to perform validation
	 ***************************************************************************************************/

	private boolean isDeliveryEmpty() {
		int totalRows = lineItemTable.getRowCount();
		for (int row = 0; row < totalRows; row++) {
			IMEIFulfillmentOrderDeliveryLineItemWrapper wrapper = (IMEIFulfillmentOrderDeliveryLineItemWrapper) lineItemTable
					.getRowData(row);
			if (wrapper.getQuantity() != null && !Quantity.ZERO.equals(wrapper.getQuantity())) {
				return false;
			}
		}
		return true;
	}

	/****************************************************************************************************
	 * DELIVERY TABLE DEFINITION
	 ***************************************************************************************************/

	private class CustomerOrderDeliveryItemsDefinition extends SimTableDefinition {

		// private SerialNumberTableEditor serialNumberTableEditor = new
		// SerialNumberTableEditor(new UINPopupListener());

		public Class getDataClass() {
			return IMEIFulfillmentOrderDeliveryLineItemWrapper.class;
		}

		public List<SimTableSortAttribute> getSortAttributes() {
			return Collections.singletonList(new SimTableSortAttribute("itemId", false));
		}

		public List<String> getOverrideEditableAttributes() {
			return Collections.singletonList(FulfillmentOrderProperty.SERIAL_NUMBER_COUNT);
		}

		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(10);
			attributes.add(new SimTableAttribute("Item", "itemId", new GenericIdDisplayer()));
			attributes.add(new SimTableAttribute("Description", "itemDescription", new TranslatedObjectDisplayer()));
			attributes.add(new SimTableAttribute("Picked Qty", "pickedQuantity", new IMEIPickedQuantityDisplayer()));
			attributes.add(
					new SimTableAttribute("IMEI Number", "imeiNumber", new StringDisplayer(), new IMEITableEditor()));

			return attributes;
		}
	}

	/****************************************************************************************************
	 * UIN POPUP LISTENER
	 ***************************************************************************************************/

}
