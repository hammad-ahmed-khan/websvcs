package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.notes.NotesDialog;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
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
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.shipment.BillOfLading;

/********************************************************************************************************
 * Fulfillment Order Delivery Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderDeliveryDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {

	private static final long serialVersionUID = -6817231662399678268L;

	private FulfillmentOrderDeliveryDetailModel model = new FulfillmentOrderDeliveryDetailModel();

	private RDisplayLabelEditor deliveryEditor = new RDisplayLabelEditor("Delivery ID");
	private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
	private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
	private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
	private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");
	private RDisplayLabelEditor resvTypeEditor = new RDisplayLabelEditor("Reservation Type");
	private RDisplayLabelEditor orderCreateDateEditor = new RDisplayLabelEditor("Order Create Date");
	private RDisplayLabelEditor orderReleaseDateEditor = new RDisplayLabelEditor("Order Release Date");
	private RDisplayLabelEditor orderDeliveryDateEditor = new RDisplayLabelEditor("Order Delivery Date");
	private RDisplayLabelEditor deliveryTypeEditor = new RDisplayLabelEditor("Delivery Type");
	private RDisplayLabelEditor carrierEditor = new RDisplayLabelEditor("Carrier");
	private RDisplayLabelEditor serviceEditor = new RDisplayLabelEditor("Service");
	private RDisplayLabelEditor partialDeliveryEditor = new RDisplayLabelEditor("Allow Partial Delivery");
	private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
	private RDisplayLabelEditor deliveryStatusEditor = new RDisplayLabelEditor("Delivery Status");
	private RDisplayLabelEditor deliveryCreateDateEditor = new RDisplayLabelEditor("Delivery Create Date");
	private RDisplayLabelEditor deliveryCreateUserEditor = new RDisplayLabelEditor("Delivery Create User");

	private StockItemScannerDialog scannerDialog = null;

	private SimTable lineItemTable = new SimTable(new CustomerOrderDeliveryItemsDefinition());
	private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

	/****************************************************************************************************
	 * Build Panel
	 ***************************************************************************************************/

	public FulfillmentOrderDeliveryDetailPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		deliveryEditor.setDisplayer(new IdNameDisplayer());
		simFulfilOrderIdEditor.setDisplayer(new IdNameDisplayer());
		customerOrderIdEditor.setDisplayer(new IdNameDisplayer());
		fulfillmentOrderIdEditor.setDisplayer(new IdNameDisplayer());
		orderStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
		deliveryStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
		resvTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
		deliveryCreateDateEditor.setDisplayer(new DateTimeDisplayer());
		orderCreateDateEditor.setDisplayer(new DateTimeDisplayer());
		orderReleaseDateEditor.setDisplayer(new DateTimeDisplayer());
		orderDeliveryDateEditor.setDisplayer(new DateTimeDisplayer());
		partialDeliveryEditor.setDisplayer(new BooleanDisplayer());

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

		if (model.isSerialNumberProcessingEnabled()) {
			lineItemTable.setColumnSize(FulfillmentOrderProperty.SERIAL_NUMBER_COUNT,
					EditorConstants.COLUMN_LABEL_WIDTH);
		}
	}

	private void layoutScreen() {
		REditorPanel detailPanel = new REditorPanel(5, 4);
		detailPanel.add(customerOrderIdEditor);
		detailPanel.add(fulfillmentOrderIdEditor);
		detailPanel.add(simFulfilOrderIdEditor);
		detailPanel.add(orderStatusEditor);
		detailPanel.add(resvTypeEditor);
		detailPanel.add(orderCreateDateEditor);
		detailPanel.add(orderReleaseDateEditor);
		detailPanel.add(orderDeliveryDateEditor);
		detailPanel.skip();
		detailPanel.skip();
		detailPanel.add(deliveryEditor);
		detailPanel.add(deliveryStatusEditor);
		detailPanel.add(deliveryCreateDateEditor);
		detailPanel.add(deliveryCreateUserEditor);
		detailPanel.skip();
		detailPanel.add(deliveryTypeEditor);
		detailPanel.add(carrierEditor);
		detailPanel.add(serviceEditor);
		detailPanel.add(partialDeliveryEditor);

		REditorPanel commentPanel = new REditorPanel(1, 1);
		commentPanel.add(commentsEditor);

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
		launchScanner();
	}

	private void populateScreen() throws Exception {
		FulfillmentOrderDelivery delivery = model.getDelivery();
		FulfillmentOrder order = model.getFulfillmentOrder();
		if (delivery.isNew()) {
			deliveryEditor.setData(Translator.getText("New"));
		} else {
			deliveryEditor.setData(delivery.getId());
		}
		simFulfilOrderIdEditor.setData(order.getId());
		customerOrderIdEditor.setData(order.getCustomerOrderId());
		fulfillmentOrderIdEditor.setData(order.getExternalId());
		orderStatusEditor.setData(order.getStatus());
		deliveryStatusEditor.setData(delivery.getStatus());
		commentsEditor.setText(order.getComments());
		deliveryCreateDateEditor.setData(delivery.getCreateDate());
		deliveryCreateUserEditor.setData(delivery.getCreateUser());
		orderCreateDateEditor.setData(order.getCreateDate());
		orderReleaseDateEditor.setData(order.getReleaseDate());
		orderDeliveryDateEditor.setData(order.getDeliveryDate());
		resvTypeEditor.setData(order.getOrderType().toString());
		deliveryTypeEditor.setData(order.getDeliveryType().toString());
		partialDeliveryEditor.setData(order.isAllowPartialDelivery());

		BillOfLading billOfLading = delivery.getBillOfLading();
		carrierEditor.setData(billOfLading.getCarrier());
		serviceEditor.setData(billOfLading.getCarrierService());

		lineItemTable.setRows(model.getDeliveryItems());
		lineItemTable.setTableEditable(model.isDeliveryEditAllowed());
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

	private void launchScanner() {
		if (model.isScannerAvailable()) {
			if (model.isScannerAutoDisplay()) {
				displayScanner();
			}
		}
	}

	private void displayScanner() {
		if (model.isScannerAvailable()) {
			if (scannerDialog == null) {
				scannerDialog = new StockItemScannerDialog();
				scannerDialog.setItemProcessor(this);
			}
			scannerDialog.setVisible(true);
		}
	}

	private void shutdownScanner() {
		if (scannerDialog != null) {
			scannerDialog.setVisible(false);
			scannerDialog = null;
		}
	}

	public void processBarcodeItem(BarcodeItem barcodeItem) {
		lineItemTable.stopEditing();
		try {
			List<FulfillmentOrderDeliveryLineItemWrapper> existingWrappers = lineItemTable.getAllRowData();
			List<FulfillmentOrderDeliveryLineItemWrapper> foundWrappers = new ArrayList<FulfillmentOrderDeliveryLineItemWrapper>();
			for (FulfillmentOrderDeliveryLineItemWrapper wrapper : existingWrappers) {
				if (barcodeItem.getId().equals(wrapper.getStockItem().getId())) {
					foundWrappers.add(wrapper);
				}
			}
			if (foundWrappers.size() == 0) {
				throw new UIException(CommonMessageText.LINE_ITEM_NOT_FOUND, RErrorSeverity.WARNING);
			}
			if (foundWrappers.size() == 1) {
				model.updateExistingLineItem(foundWrappers.get(0), barcodeItem);
			}
			if (foundWrappers.size() > 1) {
				FulfillmentOrderDeliveryLineItemDialog dialog = new FulfillmentOrderDeliveryLineItemDialog();
				dialog.setLineItems(foundWrappers);
				dialog.setVisible(true);

				if (dialog.getSelectedLineItem() != null) {
					model.updateExistingLineItem(dialog.getSelectedLineItem(), barcodeItem);
				}
			}
		} catch (Exception exception) {
			scannerDialog.displayException(exception);
		} finally {
			lineItemTable.refreshTable();
		}
	}

	/****************************************************************************************************
	 * Handle Default Quantities
	 ***************************************************************************************************/

	public void handleDefaultQuantities() throws Exception {
		lineItemTable.stopEditing();
		int totalRows = lineItemTable.getRowCount();
		for (int row = 0; row < totalRows; row++) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItemTable
					.getRowData(row);
			if (wrapper.isSerialNumberRequired()) {
				continue;
			}
			if (wrapper.getQuantity() == null) {
				lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
				UOMMode currentMode = wrapper.getUnitOfMeasureMode();
				Quantity defaultQuantity = model.getDefaultQuantity(wrapper);
				wrapper.setUnitOfMeasureMode(UOMMode.STANDARD);
				try {
					wrapper.setQuantityBasedOnUom(defaultQuantity);
				} catch (SimTableResetFocusException e) {
					// Ignore this for default qty setting
				} finally {
					wrapper.setUnitOfMeasureMode(currentMode);
				}
			}
		}
		lineItemTable.stopEditing();
		lineItemTable.refreshTable();
	}

	public boolean handleIMEI() throws UIException {
		lineItemTable.stopEditing();
		try {
			System.out.println("Panel : handle IMEI..");
			return model.checkBOLAssigned();
		} catch (Exception exception) {
			if (exception.getMessage() == "BOL_ASSIGNED") {
				throw new UIException(CommonMessageText.BOL_ASSIGNED);
			} else {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			}
		}
	}

	/****************************************************************************************************
	 * Handle Save
	 * 
	 * @throws UIException
	 ***************************************************************************************************/

	public boolean handleSave() throws Exception {
		lineItemTable.stopEditing();
		System.out.println("handle Save..");
		try {
			if (model.lookupIMEI()) {
				if (!validateLineItemWrappers() || !validateLineItemQuantities()) {
					return false;
				}
				model.saveDelivery();
				model.releaseLock();
				return true;
			} else {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			}
		} catch (UIException e) {
			if (e.getMessage() == "Something Went Wrong") {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			} else {
				displayException(e);
			}

		} catch (Exception exception) {
			if (exception.getMessage() == "EMPTY_IMEI") {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			} else if (exception.getMessage() == "IMEI Quantity Cannot be Greater than the Quantity Entered") {
				throw new UIException(CommonMessageText.IMEI_QUANTITY_GREATER_THAN_ENTERED_QUANTITY);
			} else {
				displayException(exception);
			}

		}
		return false;
	}

	/****************************************************************************************************
	 * Handle Cancel
	 ***************************************************************************************************/

	public void handleCancel() throws Exception {
		if (model.isDeliveryEditAllowed()) {
			model.releaseLock();
		}
	}

	public void handleIMEICancel() throws Exception {
		try {
			model.handleIMEICancel();
		} catch (Exception exception) {
			throw new UIException(CommonMessageText.IMEI_GENERAL);
		}
	}

	/****************************************************************************************************
	 * Handle Submit
	 ***************************************************************************************************/

	public boolean handleSubmit() throws Exception {
		lineItemTable.stopEditing();
		try {
			FulfillmentOrderDelivery delivery = model.getDelivery();
			if (!validateLineItemWrappers() || !validateLineItemQuantities()) {
				return false;
			}
			if (isDeliveryEmpty()) {
				if (!delivery.isNew()) {
					if (RConfirmUtility.confirm("Delete Delivery Confirmation",
							FulfillmentOrderMessageText.EMPTY_DELIVERY_CONFIRM)) {
						model.cancelDelivery();
						model.releaseLock();
						return true;
					}
					return false;
				}
				return true;
			}
			if (model.hasOpenReversePicks()) {
				if (RConfirmUtility.confirm("Submit Confirmation",
						FulfillmentOrderMessageText.OPEN_REVERSE_PICKS_SUBMIT_CONFIRM)) {
					if (!submitDispatchQuantityValidation()) {
						return false;
					}
					model.submitDelivery();
					model.releaseLock();
					return true;
				}
			} else {
				if (!submitDispatchQuantityValidation()) {
					return false;
				}
				model.submitDelivery();
				model.releaseLock();
				return true;
			}

		} catch (Exception exception) {
			displayException(exception);
		}
		return false;
	}

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
	 * 
	 * @throws UIException
	 ***************************************************************************************************/

	public boolean handleDispatch() throws UIException {
		lineItemTable.stopEditing();
		try {
			if (model.lookupIMEI()) {
				FulfillmentOrderDelivery delivery = model.getDelivery();
				if (!validateLineItemWrappers() || !validateLineItemQuantities()) {
					return false;
				}
				if (isDeliveryEmpty()) {
					if (!delivery.isNew()) {
						if (RConfirmUtility.confirm("Delete Delivery Confirmation",
								FulfillmentOrderMessageText.EMPTY_DELIVERY_CONFIRM)) {
							model.cancelDelivery();
							model.releaseLock();
							return true;
						}
						return false;
					}
					return true;
				}

				if (model.hasOpenReversePicks()) {
					if (RConfirmUtility.confirm("Dispatch Confirmation",
							FulfillmentOrderMessageText.OPEN_REVERSE_PICKS_CONFIRM)) {
						return dispatchDelivery();
					}
				} else if (RConfirmUtility.confirm("Dispatch Confirmation",
						FulfillmentOrderMessageText.DISPATCH_CONFIRM)) {
					return dispatchDelivery();
				}
			} else {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			}

		} catch (UIException e) {
			if (e.getMessage() == "Something Went Wrong") {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			} else {
				displayException(e);
			}
		} catch (Exception exception) {
			if (exception.getMessage() == "EMPTY_IMEI") {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			} else if (exception.getMessage() == "IMEI Quantity Cannot be Greater than the Quantity Entered") {
				throw new UIException(CommonMessageText.IMEI_QUANTITY_GREATER_THAN_ENTERED_QUANTITY);
			} else if (exception.getMessage() == "Something Went Wrong") {
				throw new UIException(CommonMessageText.IMEI_GENERAL);
			} else {
				displayException(exception);
			}

		}
		return false;
	}

	public boolean saveIndicatorIMEI() {
		try {
			return model.updateIndicatorIMEI("S");
		} catch (Exception exception) {
			displayException(exception);
		}
		return false;

	}

	public boolean dispatchIndicatorIMEI() {
		try {
			return model.updateIndicatorIMEI("D");
		} catch (Exception exception) {
			displayException(exception);
		}
		return false;
	}

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

	public void handleScanner() {
		lineItemTable.stopEditing();
		displayScanner();
	}

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

	private boolean validateLineItemWrappers() throws Exception {
		int totalRows = lineItemTable.getRowCount();
		boolean setQuantitiesToZero = false;
		for (int row = 0; row < totalRows; row++) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItemTable
					.getRowData(row);
			Quantity quantity = wrapper.getQuantity();

			if (quantity == null) {
				if (!setQuantitiesToZero) {
					if (!RConfirmUtility.confirm("Set Empty Quantities Confirmation",
							CommonMessageText.DEFAULT_TO_ZERO_CONFIRM)) {
						return false;
					}
					setQuantitiesToZero = true;
				}
				wrapper.setQuantityBasedOnUom(Quantity.ZERO);
			}
		}
		return true;
	}

	private boolean validateLineItemQuantities() {
		int totalRows = lineItemTable.getRowCount();

		for (int row = 0; row < totalRows; row++) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItemTable
					.getRowData(row);
			Quantity quantity = wrapper.getQuantity();
			if (model.isPickingRequired()) {
				if (quantity.subtract(wrapper.getPickedQty().subtract(wrapper.getDeliveredQty())).isPositive()) {
					UIStatusUtility.displayWarning(this, FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_PICKED);
					lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
					return false;
				}
			} else {
				if (wrapper.getPickedQty().subtract(wrapper.getOrderedQty()).isPositive()) {
					if (quantity.subtract(wrapper.getPickedQty().subtract(wrapper.getDeliveredQty())).isPositive()) {
						UIStatusUtility.displayWarning(this, FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_PICKED);
						lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
						return false;
					}
				} else {
					if (quantity.subtract(wrapper.getRemainingQty()).isPositive()) {
						UIStatusUtility.displayWarning(this,
								FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_REMAINING);
						lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean submitDispatchQuantityValidation() throws Exception {

		if (model.isAllowPartialDelivery()) {
			return true;
		}

		// Used to track if the user has already answered outstanding qty
		// confirm
		Boolean deliveryDispatchConfirm = false;

		int totalRows = lineItemTable.getRowCount();
		for (int row = 0; row < totalRows; row++) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItemTable
					.getRowData(row);
			Quantity quantity = wrapper.getQuantity();

			// If Picked > Ordered
			if (wrapper.getPickedQty().subtract(wrapper.getOrderedQty()).isPositive()) {
				if (!quantity.subtract(wrapper.getPickedQty().subtract(wrapper.getDeliveredQty())).isZero()) {
					if (model.hasDispatchIncompletePermission()) {
						if (!deliveryDispatchConfirm) {
							deliveryDispatchConfirm = true;
							if (!RConfirmUtility.confirm("Outstanding Quantities Confirmation",
									FulfillmentOrderMessageText.OUTSTANDING_QUANTITIES_CONFIRM)) {
								return false;
							}
						}
					} else {
						UIStatusUtility.displayWarning(this,
								FulfillmentOrderMessageText.NO_OUTSTANDING_QUANTITIES_ALLOWED);
						lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
						return false;
					}
				}
			} else {
				if (!quantity.subtract(wrapper.getRemainingQty()).isZero()) {
					if (model.hasDispatchIncompletePermission()) {
						if (!deliveryDispatchConfirm) {
							deliveryDispatchConfirm = true;
							if (!RConfirmUtility.confirm("Outstanding Quantities Confirmation",
									FulfillmentOrderMessageText.OUTSTANDING_QUANTITIES_CONFIRM)) {
								return false;
							}
						}
					} else {
						UIStatusUtility.displayWarning(this,
								FulfillmentOrderMessageText.NO_OUTSTANDING_QUANTITIES_ALLOWED);
						lineItemTable.editCellInRow(FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM, row);
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean dispatchDelivery() throws Exception {
		if (!submitDispatchQuantityValidation()) {
			return false;
		}
		model.dispatchDelivery();
		model.releaseLock();
		return true;
	}

	private boolean isDeliveryEmpty() {
		int totalRows = lineItemTable.getRowCount();
		for (int row = 0; row < totalRows; row++) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItemTable
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

		private SerialNumberTableEditor serialNumberTableEditor = new SerialNumberTableEditor(new UINPopupListener());

		public Class getDataClass() {
			return FulfillmentOrderDeliveryLineItemWrapper.class;
		}

		public List<SimTableSortAttribute> getSortAttributes() {
			return Collections.singletonList(new SimTableSortAttribute("itemId", false));
		}

		public List<String> getOverrideEditableAttributes() {
			return Collections.singletonList(FulfillmentOrderProperty.SERIAL_NUMBER_COUNT);
		}

		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(10);
			attributes.add(new SimTableAttribute("Item", FulfillmentOrderProperty.ITEM_ID, new GenericIdDisplayer()));
			attributes.add(new SimTableAttribute("Description", FulfillmentOrderProperty.ITEM_DESCRIPTION,
					new TranslatedObjectDisplayer()));
			attributes.add(new SimTableAttribute("UOM", FulfillmentOrderProperty.UOM_MODE, new UomModeDisplayer(),
					new UomModeTableEditor(true)));
			attributes.add(new SimTableAttribute("Pack Size", FulfillmentOrderProperty.CASE_SIZE,
					new QuantityDisplayer(), new LineItemQuantityTableEditor()));
			attributes.add(new SimTableAttribute("Remaining Qty", FulfillmentOrderProperty.REMAINING_QTY_BASED_ON_UOM,
					new QuantityDisplayer()));
			attributes.add(new SimTableAttribute("Order Qty", FulfillmentOrderProperty.ORDERED_QTY_BASED_ON_UOM,
					new QuantityDisplayer()));
			attributes.add(new SimTableAttribute("Picked Qty", FulfillmentOrderProperty.PICKED_QTY_BASED_ON_UOM,
					new QuantityDisplayer()));
			attributes.add(new SimTableAttribute("Delivered Qty", FulfillmentOrderProperty.DELIVERED_QTY_BASED_ON_UOM,
					new QuantityDisplayer()));
			attributes.add(new SimTableAttribute("Canceled Qty", FulfillmentOrderProperty.CANCELED_QTY_BASED_ON_UOM,
					new QuantityDisplayer()));
			attributes.add(new SimTableAttribute("Quantity", FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM,
					new QuantityDisplayer(), new LineItemQuantityTableEditor()));
			if (model.isSerialNumberProcessingEnabled()) {
				attributes.add(new SimTableAttribute("UIN Qty", FulfillmentOrderProperty.SERIAL_NUMBER_COUNT,
						new SerialNumberTableDisplayer(), serialNumberTableEditor));
			}
			attributes.add(new SimTableAttribute("Substitute", FulfillmentOrderProperty.SUBSTITUTE_ID));
			return attributes;
		}
	}

	/****************************************************************************************************
	 * UIN POPUP LISTENER
	 ***************************************************************************************************/

	private class UINPopupListener implements PopupTableEditorListener {
		public void popupDialog(Object lineItem) {
			FulfillmentOrderDeliveryLineItemWrapper wrapper = (FulfillmentOrderDeliveryLineItemWrapper) lineItem;
			if (wrapper.isSerialNumberRequired()) {
				FulfillmentOrderUinDialog dialog = new FulfillmentOrderUinDialog();
				dialog.setLineItemWrapper((FulfillmentOrderDeliveryLineItemWrapper) lineItem);
				dialog.setVisible(true);

				lineItemTable.refreshTable();
			}
		}
	}
}
