package extra.retail.sim.client.screen.returns;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.BooleanAvailableDisplayer;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.displayer.TranslatedIdNameDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.finisher.FinisherSearchListener;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.itemprice.PromotionSearchListener;
import oracle.retail.sim.client.screen.returns.ReturnLineItemWrapper;
import oracle.retail.sim.client.screen.returns.ReturnUinDialog;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.LineItemInventoryDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.ReturnReasonTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.ContextTypeComparator;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnProperty;
import oracle.retail.sim.common.stockreturn.ReturnReason;

/********************************************************************************************************
 * Return Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraReturnDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
	private static final long serialVersionUID = -2091496001317582501L;

	private ExtraReturnDetailModel model = new ExtraReturnDetailModel();

	private RDisplayLabelEditor returnEditor = new RDisplayLabelEditor("Return Number");
	private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
	private RDisplayLabelEditor dateEditor = new RDisplayLabelEditor("Date");
	private RDisplayLabelEditor notDateEditor = new RDisplayLabelEditor("Not After Date");
	private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
	private RComboBoxEditor returnTypeEditor = new RComboBoxEditor("Return Type");
	private RComboBoxEditor warehouseEditor = new RComboBoxEditor("Warehouse");
	private RComboBoxEditor inventoryStatusEditor = new RComboBoxEditor("Inventory Status");
	private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
	private RSearchFieldEditor finisherEditor = SimEditorFactory.createFinisherSearchFieldEditor();
	private RComboBoxEditor finisherContextTypeEditor = new RComboBoxEditor("Context Type");
	private RSearchFieldEditor finisherContextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor();
	private RComboBoxEditor warehouseContextTypeEditor = new RComboBoxEditor("Context Type");
	private RSearchFieldEditor warehouseContextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor();
	private RTextFieldEditor authorizationEditor = new RTextFieldEditor("Authorization Number");
	private RComboBoxEditor defaultReasonEditor = new RComboBoxEditor("Reason");
	private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
	private RDisplayLabelEditor externalIdEditor = new RDisplayLabelEditor("External Id");
	private RCardPanel sourcePanel = new RCardPanel();

	private RDisplayLabelEditor reqStatusEditor = new RDisplayLabelEditor("Request Status");

	private StockItemScannerDialog scannerDialog = null;

	private StockItemTableEditor stockItemTableEditor = new StockItemTableEditor(FunctionalArea.CREATE_RETURN);
	private ReturnReasonTableEditor returnReasonTableEditor = new ReturnReasonTableEditor();

	private SimTable lineItemTable = new SimTable(new ReturnItemDefinition());
	private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

	private static final String TYPE_SELECTED = "ReturnType.selected";
	private static final String INVENTORY_STATUS_MODIFIED = "InventoryStatus.modified";
	private static final String WAREHOUSE_MODIFIED = "Warehouse.modified";
	private static final String SUPPLIER_MODIFIED = "Supplier.modified";
	private static final String FINISHER_MODIFIED = "Finisher.modified";
	private static final String FIN_CONTEXT_TYPE_MODIFIED = "FinisherContextType.modified";
	private static final String WAR_CONTEXT_TYPE_MODIFIED = "WarehouseContextType.modified";
	private static final String CONTEXT_VALUE_MODIFIED = "ContextValue.modified";
	private static final String REASON_MODIFIED = "ReturnReason.modified";
	private static final String AUTH_MODIFIED = "Authorization.modified";
	private static final String COMMENTS_MODIFIED = "Comments.modified";
	private static final String LINE_SELECT_COMMAND = "LineItem.selected";

	/****************************************************************************************************
	 * Initialize Screen
	 ***************************************************************************************************/

	public ExtraReturnDetailPanel() {
		initializePanel();
		layoutPanel();
	}

	private void initializePanel() {
		commentsEditor.setIdentifier(SimName.RETURN_COMMENT);
		authorizationEditor.setIdentifier(SimName.RETURN_AUTHORIZATION);

		returnEditor.setDisplayer(new GenericIdDisplayer());
		externalIdEditor.setDisplayer(new GenericIdDisplayer());
		storeEditor.setDisplayer(new StoreDisplayer());
		dateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		notDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		returnTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
		warehouseEditor.setDisplayer(new IdNameDisplayer());
		inventoryStatusEditor.setDisplayer(new BooleanAvailableDisplayer());
		finisherContextTypeEditor.setDisplayer(new TranslatedIdNameDisplayer());
		warehouseContextTypeEditor.setDisplayer(new TranslatedIdNameDisplayer());
		defaultReasonEditor.setDisplayer(new TranslatedObjectDisplayer());

		returnTypeEditor.setSizeType(EditorConstants.MEDIUM);
		warehouseEditor.setSizeType(EditorConstants.LARGE);
		supplierEditor.setSizeType(EditorConstants.LARGE);
		finisherEditor.setSizeType(EditorConstants.LARGE);
		finisherContextTypeEditor.setSizeType(EditorConstants.LARGE);
		finisherContextValueEditor.setSizeType(EditorConstants.LARGE);
		warehouseContextTypeEditor.setSizeType(EditorConstants.LARGE);
		warehouseContextValueEditor.setSizeType(EditorConstants.LARGE);
		defaultReasonEditor.setSizeType(EditorConstants.MEDIUM);
		authorizationEditor.setSizeType(EditorConstants.MEDIUM);

		lineItemTable.setColumnSize(ReturnProperty.USE_UNAVAILABLE_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(ReturnProperty.CASE_SIZE, EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(ReturnProperty.INVENTORY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(ReturnProperty.QUANTITY_REQUESTED_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
		lineItemTable.setColumnSize(ReturnProperty.QUANTITY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);

		inventoryStatusEditor.addItem(Boolean.TRUE);
		inventoryStatusEditor.addItem(Boolean.FALSE);

		returnTypeEditor.registerAction(this, TYPE_SELECTED);
		warehouseEditor.registerAction(this, WAREHOUSE_MODIFIED);
		supplierEditor.registerAction(this, SUPPLIER_MODIFIED);
		finisherEditor.registerAction(this, FINISHER_MODIFIED);
		finisherContextTypeEditor.registerAction(this, FIN_CONTEXT_TYPE_MODIFIED);
		finisherContextValueEditor.registerAction(this, CONTEXT_VALUE_MODIFIED);
		warehouseContextTypeEditor.registerAction(this, WAR_CONTEXT_TYPE_MODIFIED);
		warehouseContextValueEditor.registerAction(this, CONTEXT_VALUE_MODIFIED);
		authorizationEditor.registerAction(this, AUTH_MODIFIED);
		defaultReasonEditor.registerAction(this, REASON_MODIFIED);
		commentsEditor.registerAction(this, COMMENTS_MODIFIED);
		inventoryStatusEditor.registerAction(this, INVENTORY_STATUS_MODIFIED);

		stockItemTableEditor.addTableEditorListener(buildStockItemListener());
		stockItemTableEditor.setSearchListener(buildItemSearchListener());

		supplierEditor.setSearchListener(buildSupplierSearchListener());
		finisherEditor.setSearchListener(buildFinisherSearchListener());
		finisherContextValueEditor.setSearchListener(buildPromotionSearchListenerForFinisher());
		warehouseContextValueEditor.setSearchListener(buildPromotionSearchListenerForWarehouse());
	}

	private void layoutPanel() {
		REditorPanel headerPanel = new REditorPanel(4, 2);
		headerPanel.add(returnEditor);
		headerPanel.add(storeEditor);
		headerPanel.add(externalIdEditor);
		headerPanel.add(commentsEditor);
		headerPanel.add(dateEditor);
		headerPanel.add(notDateEditor);
		headerPanel.add(userEditor);
		headerPanel.add(inventoryStatusEditor);

		REditorPanel typePanel = new REditorPanel(2);
		typePanel.setTitleBorder("Return Type");
		typePanel.add(returnTypeEditor);
		typePanel.add(reqStatusEditor, 1, 0);

		REditorPanel warehousePanel = new REditorPanel(2, 2);
		warehousePanel.add(warehouseEditor);
		warehousePanel.skip();
		warehousePanel.add(warehouseContextTypeEditor);
		warehousePanel.add(warehouseContextValueEditor);

		REditorPanel supplierPanel = new REditorPanel(1);
		supplierPanel.add(supplierEditor);

		REditorPanel finisherPanel = new REditorPanel(2, 2);
		finisherPanel.add(finisherEditor);
		if (model.isFinishersEnabled()) {
			finisherPanel.skip();
			finisherPanel.add(finisherContextTypeEditor);
			finisherPanel.add(finisherContextValueEditor);
		}
		sourcePanel.addCard(SourceType.WAREHOUSE.toString(), warehousePanel);
		sourcePanel.addCard(SourceType.SUPPLIER.toString(), supplierPanel);
		if (model.isFinishersEnabled()) {
			sourcePanel.addCard(SourceType.FINISHER.toString(), finisherPanel);
		}
		sourcePanel.showCard(SourceType.WAREHOUSE.toString());

		REditorPanel reasonPanel = new REditorPanel(2);
		reasonPanel.add(authorizationEditor);
		reasonPanel.add(defaultReasonEditor);

		RPanel middlePanel = new RPanel(new GridBagLayout());
		middlePanel.add(sourcePanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 00, 0));
		middlePanel.add(reasonPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 15, 0));

		RDivider divider1 = new RDivider(RDivider.HORIZONTAL);
		RDivider divider2 = new RDivider(RDivider.HORIZONTAL);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(typePanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(divider1, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(middlePanel, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 3, 0, 0, 5, 5));
		mainPanel.add(divider2, GridTool.constraints(0, 3, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(lineItemPane, GridTool.constraints(0, 4, 2, 1, 1, 1, 0, 3, 0, 0, 5, 0));

		setContentPane(mainPanel);
	}

	public SimScreenModel getScreenModel() {
		return model;
	}

	public SimTable getScreenTable() {
		return lineItemTable;
	}

	/****************************************************************************************************
	 * Start
	 ***************************************************************************************************/

	public boolean isStartable() {
		if (model.hasSourcePermissionForCreate()) {
			returnTypeEditor.setActionsEnabled(false);
			returnTypeEditor.setItems(model.loadReturnTypes());
			returnTypeEditor.removeEmptySelection();
			returnTypeEditor.setActionsEnabled(true);
			return true;
		}
		displayError(ReturnMessageText.NO_CREATE_PERMISSION);
		return false;
	}

	public void start() throws Exception {
		model.loadStockReturn();
		if (model.getStockReturn() == null) {
			createStockReturn();
		}
		populateScreen();
		launchScanner();
	}

	public void resume() throws Exception {
		launchScanner();
	}

	private void createStockReturn() {
		returnTypeEditor.setActionsEnabled(false);
		List<SourceType> sourceTypes = model.loadReturnTypes();
		if (sourceTypes.contains(SourceType.WAREHOUSE)) {
			returnTypeEditor.setSelectedItem(SourceType.WAREHOUSE);
		} else if (sourceTypes.contains(SourceType.SUPPLIER)) {
			returnTypeEditor.setSelectedItem(SourceType.SUPPLIER);
		} else if (sourceTypes.contains(SourceType.FINISHER)) {
			returnTypeEditor.setSelectedItem(SourceType.FINISHER);
		}
		returnTypeEditor.setActionsEnabled(true);
		doReturnTypeSelected();
	}

	private void populateScreen() throws Exception {
		Return stockReturn = model.getStockReturn();

		returnEditor.setData(stockReturn.getId());
		externalIdEditor.setData(stockReturn.getExternalId());
		storeEditor.setData(model.findStore(stockReturn.getStoreId()));
		dateEditor.setData(stockReturn.getStatusDate());
		userEditor.setData(stockReturn.getCreateUser());
		notDateEditor.setData(stockReturn.getNotAfterDate());
		authorizationEditor.setText(stockReturn.getAuthorizationCode());
		commentsEditor.setText(stockReturn.getComments());

		returnTypeEditor.setActionsEnabled(false);
		returnTypeEditor.setSelectedItem(stockReturn.getType());
		returnTypeEditor.setActionsEnabled(true);

		if (stockReturn.getLineItems().size() > 0) {
			inventoryStatusEditor.setActionsEnabled(false);
			if (stockReturn.isVendorReturn()) {
				inventoryStatusEditor.setEmptySelection();
			} else {
				inventoryStatusEditor.setSelectedItem(stockReturn.isUseAvailable());
			}
			inventoryStatusEditor.setActionsEnabled(true);
		}
		lineItemTable.registerSingleClickAction(this, LINE_SELECT_COMMAND);

		if (model.isWarehouseReturn()) {
			populateWarehouseReturn(stockReturn);
		} else if (model.isSupplierReturn()) {
			populateSupplierReturn(stockReturn);
		} else {
			populateFinisherReturn(stockReturn);
		}

		if (isReturnUnmodifiable() || isReturnRequestUnmodifiable()) {
			setDetailScreenViewOnly();
		}
		if (model.isSerialNumberProcessingEnabled()) {
			lineItemTable.setColumnSize(ReturnProperty.SERIAL_NUMBER_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
		}

		lineItemTable.setRows(model.getReturnLineItems());
		if (model.isReturnRequireApproval()) {
			reqStatusEditor.setData(model.getRequestStatus());
			reqStatusEditor.setVisible(true);
		} else {
			reqStatusEditor.setVisible(false);
		}
		validateEnabledState();
	}

	private void populateWarehouseReturn(Return stockReturn) throws Exception {
		sourcePanel.showCard(SourceType.WAREHOUSE.toString());
		warehouseEditor.setActionsEnabled(false);
		warehouseEditor.setComparator(WarehouseComparator.getInstance());
		warehouseEditor.setItems(model.getAllWarehouses());
		warehouseEditor.setSelectedItem(stockReturn.getDestination());
		warehouseEditor.setActionsEnabled(true);
		warehouseContextTypeEditor.setActionsEnabled(false);
		warehouseContextTypeEditor.setComparator(ContextTypeComparator.getInstance());
		warehouseContextTypeEditor.setItems(model.getAllContextTypes());
		warehouseContextTypeEditor.setSelectedItem(stockReturn.getContextType());
		warehouseContextTypeEditor.setActionsEnabled(true);
		warehouseContextValueEditor.setActionsEnabled(false);
		warehouseContextValueEditor.setText(stockReturn.getContextValue());
		warehouseContextValueEditor.setActionsEnabled(true);
		List<ReturnReason> returnReasons = model.findWarehouseReturnReasons(stockReturn.isUseAvailable());
		defaultReasonEditor.setItems(returnReasons);
		returnReasonTableEditor.setReturnReasons(returnReasons);
	}

	private void populateSupplierReturn(Return stockReturn) throws Exception {
		sourcePanel.showCard(SourceType.SUPPLIER.toString());
		supplierEditor.setActionsEnabled(false);
		supplierEditor.setData(stockReturn.getDestination());
		supplierEditor.setActionsEnabled(true);
		List<ReturnReason> returnReasons = model.findSupplierReturnReasons();
		defaultReasonEditor.setItems(returnReasons);
		returnReasonTableEditor.setReturnReasons(returnReasons);
	}

	private void populateFinisherReturn(Return stockReturn) throws Exception {
		sourcePanel.showCard(SourceType.FINISHER.toString());
		finisherEditor.setActionsEnabled(false);
		finisherEditor.setData(stockReturn.getDestination());
		finisherEditor.setActionsEnabled(true);
		finisherContextTypeEditor.setActionsEnabled(false);
		finisherContextTypeEditor.setComparator(ContextTypeComparator.getInstance());
		finisherContextTypeEditor.setItems(model.getAllContextTypes());
		finisherContextTypeEditor.setSelectedItem(stockReturn.getContextType());
		finisherContextTypeEditor.setActionsEnabled(true);
		finisherContextValueEditor.setActionsEnabled(false);
		finisherContextValueEditor.setText(stockReturn.getContextValue());
		finisherContextValueEditor.setActionsEnabled(true);
		List<ReturnReason> returnReasons = model.findFinisherReturnReasons(stockReturn.isUseAvailable());
		defaultReasonEditor.setItems(returnReasons);
		returnReasonTableEditor.setReturnReasons(returnReasons);
	}

	/****************************************************************************************************
	 * State Methods
	 ***************************************************************************************************/

	protected void setDetailScreenViewOnly() {
		returnTypeEditor.setEnabled(false);
		warehouseEditor.setEnabled(false);
		supplierEditor.setEnabled(false);
		finisherEditor.setEnabled(false);
		finisherContextTypeEditor.setEnabled(false);
		finisherContextValueEditor.setEnabled(false);
		warehouseContextTypeEditor.setEnabled(false);
		warehouseContextValueEditor.setEnabled(false);
		authorizationEditor.setEnabled(false);
		commentsEditor.setEnabled(false);
		inventoryStatusEditor.setEnabled(false);
		lineItemTable.setTableEditable(false);
		model.setViewOnly(true);
	}

	protected boolean isReturnRequestUnmodifiable() {
		return model.isReturnRequest() && model.hasNotAfterDatePast();
	}

	/****************************************************************************************************
	 * State Methods
	 ***************************************************************************************************/

	protected boolean isAddItemNotAvailable() {
		return model.isAddItemNotAvailable();
	}

	protected boolean isRemoveItemNotAvailable() {
		return model.isRemoveItemNotAvailable();
	}

	protected boolean isSubmitNotAvailable() {
		return model.isSubmitNotAvailable();
	}

	protected boolean isCancelSubmitNotAvailable() {
		return model.isCancelSubmitNotAvailable();
	}

	protected boolean isDispatchNotAvailable() {
		return model.isDispatchNotAvailable();
	}

	protected boolean isReturnUnmodifiable() {
		return model.isReturnUnmodifiable();
	}

	protected boolean isReturnSubmitted() {
		return model.isStatusSubmitted();
	}

	protected boolean confirmReturnLock() throws Exception {
		return model.confirmReturnLock();
	}

	protected boolean isObtainReturnLock() throws Exception {
		return model.obtainReturnLock();
	}

	protected boolean isViewOnly() throws Exception {
		return model.isViewOnly();
	}

	/****************************************************************************************************
	 * SCANNER METHODS
	 ***************************************************************************************************/

	protected boolean isScannerAvailable() {
		return model.isScannerAvailable();
	}

	protected void launchScanner() {
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
			Source destination = model.getStockReturn().getDestination();
			if (destination == null) {
				throw new BusinessException(ReturnMessageText.MISSING_DESTINATION);
			}
			ReturnReason returnReason = (ReturnReason) defaultReasonEditor.getSelectedItem();
			if (returnReason == null) {
				throw new BusinessException(ReturnMessageText.MISSING_REASON);
			}
			ReturnLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
			if (lineItemWrapper != null) {
				model.updateExistingLineItem(lineItemWrapper, barcodeItem);
				return;
			}
			lineItemWrapper = findEmptyWrapper();
			if (lineItemWrapper != null) {
				lineItemWrapper.setStockItem(barcodeItem.getStockItem());
				if (lineItemWrapper.getReason() == null) {
					lineItemWrapper.setReason(returnReason);
				}
				model.updateExistingLineItem(lineItemWrapper, barcodeItem);
				return;
			}
			lineItemWrapper = model.buildNewLineItemWrapper(returnReason);
			if (lineItemWrapper != null) {
				lineItemWrapper.setStockItem(barcodeItem.getStockItem());
				lineItemTable.addRow(lineItemWrapper);
				lineItemWrapper.setReason(returnReason);
				model.updateExistingLineItem(lineItemWrapper, barcodeItem);
			}
		} catch (Exception exception) {
			scannerDialog.displayException(exception);
		} finally {
			lineItemTable.refreshTable();
		}
	}

	private ReturnLineItemWrapper findExistingWrapper(String itemId) {
		for (ReturnLineItemWrapper wrapper : getAllWrappers()) {
			StockItem stockItem = wrapper.getStockItem();
			if (stockItem != null && itemId.equals(stockItem.getId())) {
				return wrapper;
			}
		}
		return null;
	}

	private ReturnLineItemWrapper findEmptyWrapper() {
		for (ReturnLineItemWrapper wrapper : getAllWrappers()) {
			if (wrapper.getStockItem() == null) {
				return wrapper;
			}
		}
		return null;
	}

	/****************************************************************************************************
	 * HANDLE CANCEL
	 ***************************************************************************************************/

	protected void pauseScreen() {
		shutdownScanner();
	}

	protected void stopScreen() {
		lineItemTable.clearRows();
		try {
			model.clearStockReturn();
		} catch (Throwable e) {
			displayException(e);
		}
		shutdownScanner();
	}

	/****************************************************************************************************
	 * ADD ITEM
	 ***************************************************************************************************/

	protected void handleAddItem() throws Exception {
		lineItemTable.stopEditing();

		if (model.isAddItemAllowed(getAllWrappers())) {
			Boolean useUnavailable = (Boolean) inventoryStatusEditor.getSelectedItem();
			if (!model.isSupplierReturn() && useUnavailable == null) {
				return;
			}
			ReturnReason reason = (ReturnReason) defaultReasonEditor.getSelectedItem();
			lineItemTable.addRow(model.buildNewLineItemWrapper(reason));
			lineItemTable.editCellInLastRow(ReturnProperty.STOCK_ITEM);
			inventoryStatusEditor.setEnabled(false);
			validateEnabledState();
		}
	}

	/****************************************************************************************************
	 * DELETE
	 ***************************************************************************************************/

	protected void handleRemoveItem() throws Exception {
		if (lineItemTable.getSelectedRowCount() < 1) {
			displayMessage(CommonMessageText.NO_ROWS_SELECTED);
			return;
		}

		List<ReturnLineItemWrapper> returnLineItems = lineItemTable.getAllSelectedRowData();
		if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
			return;
		}
		if (model.includesExternallyCreatedItems(returnLineItems)) {
			if (!RConfirmUtility.confirm("Item Delete Confirmation", ReturnMessageText.DELETE_EXTERNAL_ITEM_ERROR)) {
				return;
			}
		}

		lineItemTable.stopEditing();

		List<ReturnLineItemWrapper> removedLineItems = new ArrayList<>();
		for (ReturnLineItemWrapper returnItem : returnLineItems) {
			if (returnItem.getLineItem() == null || returnItem.getLineItem().isSimCreated()) {
				model.deleteReturnLineItem(returnItem);
				removedLineItems.add(returnItem);
			} else {
				// externally created, quantities will be zeroed out and not removed from return
				returnItem.getLineItem().removeQuantities();
			}
		}
		for (ReturnLineItemWrapper removedLineItem : removedLineItems) {
			lineItemTable.removeRow(removedLineItem);
		}
		validateEnabledState();
	}

	protected boolean isReturnEmpty() {
		return lineItemTable.isEmpty();
	}

	protected void cancelEmptyReturn() throws Exception {
		model.cancelReturn();
	}

	/****************************************************************************************************
	 * DISPATCH
	 ***************************************************************************************************/

	public boolean handleDispatch() throws Exception {
		if (!model.isValidForDispatch()) {
			return false;
		}

		Date notAfterDate = model.getStockReturn().getNotAfterDate();
		if (notAfterDate != null) {
			if (notAfterDate.compareTo(SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone())) < 0) {
				displayError(ReturnMessageText.BEYOND_END_DATE_ERROR);
				return false;
			}
		}
		if (!RConfirmUtility.confirm("Dispatch Confirmation", ReturnMessageText.DISPATCH_CONFIRM)) {
			return false;
		}

		if (!model.isQtyPopulatedForAllItems(getAllWrappers())) {
			displayError(ReturnMessageText.EMPTY_RETURN_WARNING);
			return false;
		}

		if (!model.isValidDestinationStatus()) {
			return false;
		}
		model.dispatchStockReturn();
		return true;
	}

	/****************************************************************************************************
	 * SUBMIT
	 ***************************************************************************************************/

	public boolean handleSubmit() throws Exception {
		if (!model.isValidForSubmit()) {
			return false;
		}

		Date notAfterDate = model.getStockReturn().getNotAfterDate();
		if (notAfterDate != null) {
			if (notAfterDate.compareTo(SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone())) < 0) {
				displayError(ReturnMessageText.BEYOND_END_DATE_ERROR);
				return false;
			}
		}
		if (!RConfirmUtility.confirm("Submit Confirmation", ReturnMessageText.SUBMIT_CONFIRM)) {
			return false;
		}

		if (!model.isQtyPopulatedForAllItems(getAllWrappers())) {
			displayError(ReturnMessageText.EMPTY_RETURN_WARNING);
			return false;
		}

		if (!model.isValidDestinationStatus()) {
			return false;
		}
		model.submitStockReturn();
		return true;
	}

	/****************************************************************************************************
	 * CANCEL SUBMITTED RETURN
	 ***************************************************************************************************/

	public boolean handleCancelSubmission() throws Exception {
		if (!model.isValidForCancelSubmitReturn()) {
			return false;
		}

		if (!RConfirmUtility.confirm("Cancel Submit Confirmation", ReturnMessageText.CANCEL_SUBMIT_CONFIRM)) {
			return false;
		}
		model.cancelSubmittedStockReturn();
		return true;
	}

	/****************************************************************************************************
	 * SCANNER
	 ***************************************************************************************************/

	protected void handleScanner() {
		lineItemTable.stopEditing();
		displayScanner();
	}

	/****************************************************************************************************
	 * DONE
	 ***************************************************************************************************/

	protected boolean hasNoQuantity() throws Exception {
		if (model.getStockReturn().isUINRequired()) {
			if (!model.getStockReturn().isRequest()) {
				checkMissingRequiredUinItems();
			}
		}
		return model.stockReturnHasNoQuantity();
	}

	private void checkMissingRequiredUinItems() throws Exception {
		List<ReturnLineItemWrapper> wrappers = getAllWrappers();
		List<ReturnLineItemWrapper> wrappersMissingUins = model.getLineItemWrappersMissingRequiredUins(wrappers);
		if (wrappersMissingUins.isEmpty()) {
			return;
		}
		int firstRow = wrappers.indexOf(wrappersMissingUins.get(0));
		if (firstRow >= 0 && firstRow < wrappers.size()) {
			lineItemTable.setRowSelectionInterval(firstRow, firstRow);
		}
		boolean isShortDescription = SimConfigManager.isItemShortDescription();
		List<String> missingUinItems = new ArrayList<>(wrappersMissingUins.size());
		for (ReturnLineItemWrapper wrapper : wrappersMissingUins) {
			StockItem stockItem = wrapper.getStockItem();
			missingUinItems.add(stockItem.getId() + " - " + (isShortDescription ? stockItem.getShortDescription() : stockItem.getLongDescription()));
		}
		BusinessException exception;
		if (missingUinItems.size() > 1) {
			exception = new BusinessException(CommonMessageText.UIN_REQUIRED_MULTI, missingUinItems);
		} else {
			exception = new BusinessException(CommonMessageText.UIN_REQUIRED, missingUinItems.get(0));
		}
		throw exception;
	}

	protected boolean cancelReturn() throws Exception {
		if (RConfirmUtility.confirmWithOkCancelType("Delete Return Confirmation", ReturnMessageText.EMPTY_RETURN_WARNING)) {
			model.cancelReturn();
			return true;
		}
		return false;
	}

	protected boolean isValidForDone() throws BusinessException {
		return model.isValidForDone();
	}

	protected void updateStockReturn() throws Exception {
		model.updateReturn();
	}

	/****************************************************************************************************
	 * BILL OF LADING
	 ***************************************************************************************************/

	public void storeReturnForBillOfLading() {
		model.storeReturnForBillOfLading();
	}

	public boolean isValidForBillOfLading() throws BusinessException {
		return model.isValidForBillOfLading();
	}

	/****************************************************************************************************
	 * Helper Methods
	 ***************************************************************************************************/

	public List<ReturnLineItemWrapper> getAllWrappers() {
		return (List<ReturnLineItemWrapper>) lineItemTable.getAllRowData();
	}

	private void validateEnabledState() {
		if (returnTypeEditor.getSelectedItem() != null) {
			returnTypeEditor.setEnabled(model.isDestinationModifiable());
			warehouseEditor.setEnabled(model.isDestinationModifiable());
			authorizationEditor.setEnabled(model.isAuthorizationModifiable());
			supplierEditor.setEnabled(model.isDestinationModifiable());
			finisherEditor.setEnabled(model.isDestinationModifiable());
			commentsEditor.setEnabled(model.isCommentsModifiable());
			finisherContextTypeEditor.setEnabled(model.isContextTypeModifiable() && model.getStockReturn().isSimCreated());
			warehouseContextTypeEditor.setEnabled(model.isContextTypeModifiable());
			finisherContextValueEditor.setEnabled(model.isContextTypeModifiable() && model.getStockReturn().isSimCreated() && model.isPromotion());
			warehouseContextValueEditor.setEnabled(model.isContextTypeModifiable() && model.isPromotion());
			inventoryStatusEditor.setEnabled(model.isDestinationModifiable());

			if (hasLineItems()) {
				inventoryStatusEditor.setEnabled(false);
				warehouseEditor.setEnabled(false);
				finisherEditor.setEnabled(false);
			}
			if (model.isSupplierReturn()) {
				inventoryStatusEditor.setEnabled(false);
			}
		} else {
			returnTypeEditor.setEnabled(false);
			warehouseEditor.setEnabled(false);
			supplierEditor.setEnabled(false);
			finisherEditor.setEnabled(false);
			commentsEditor.setEnabled(false);
			authorizationEditor.setEnabled(false);
			finisherContextTypeEditor.setEnabled(false);
			finisherContextValueEditor.setEnabled(false);
			warehouseContextTypeEditor.setEnabled(false);
			warehouseContextValueEditor.setEnabled(false);
			inventoryStatusEditor.setEnabled(false);
			if (scannerDialog != null) {
				scannerDialog.setVisible(false);
			}
		}
		if (!model.isContextFieldEditable()) {
			finisherContextTypeEditor.setEnabled(false);
			warehouseContextTypeEditor.setEnabled(false);
			finisherContextValueEditor.setEnabled(false);
			warehouseContextValueEditor.setEnabled(false);
		}
	}

	private boolean hasLineItems() {
		for (ReturnLineItemWrapper wrapper : model.getReturnLineItems()) {
			if (wrapper.getStockItem() != null) {
				return true;
			}
		}
		return false;
	}

	/****************************************************************************************************
	 * Screen Events
	 ***************************************************************************************************/

	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		if (command.equals(TYPE_SELECTED)) {
			doReturnTypeSelected();
		} else if (command.equals(WAREHOUSE_MODIFIED)) {
			doWarehouseModified();
		} else if (command.equals(SUPPLIER_MODIFIED)) {
			doSupplierModified();
		} else if (command.equals(FINISHER_MODIFIED)) {
			doFinisherModified();
		} else if (command.equals(FIN_CONTEXT_TYPE_MODIFIED)) {
			doFinisherContextTypeModified();
		} else if (command.equals(WAR_CONTEXT_TYPE_MODIFIED)) {
			doWarehouseContextTypeModified();
		} else if (command.equals(CONTEXT_VALUE_MODIFIED)) {
			doContextValueModified((RSearchFieldEditor) event.getSource());
		} else if (command.equals(COMMENTS_MODIFIED)) {
			doCommentsModified();
		} else if (command.equals(AUTH_MODIFIED)) {
			doAuthorizationModified();
		} else if (command.equals(INVENTORY_STATUS_MODIFIED)) {
			doInventoryStatusModified();
		} else if (command.equals(REASON_MODIFIED)) {
			doDefaultReasonModified();
		}
	}

	/****************************************************************************************************
	 * Return Type Modified
	 ***************************************************************************************************/

	private void doReturnTypeSelected() {
		SourceType sourceType = (SourceType) returnTypeEditor.getSelectedItem();
		try {
			inventoryStatusEditor.setActionsEnabled(false);
			inventoryStatusEditor.setEmptySelection();
			inventoryStatusEditor.setEnabled(sourceType != SourceType.SUPPLIER);
			inventoryStatusEditor.setActionsEnabled(true);

			model.createReturn(sourceType);

			lineItemTable.clearRows();

			if (sourceType == SourceType.WAREHOUSE) {
				sourcePanel.showCard(SourceType.WAREHOUSE.toString());
				warehouseEditor.requestFocusInWindow();
			} else if (sourceType == SourceType.FINISHER) {
				sourcePanel.showCard(SourceType.FINISHER.toString());
				finisherEditor.requestFocusInWindow();
			} else {
				sourcePanel.showCard(SourceType.SUPPLIER.toString());
				supplierEditor.requestFocusInWindow();
			}
			populateScreen();
		} catch (Throwable exception) {
			displayException(exception);
		}
	}

	/****************************************************************************************************
	 * Source Modified
	 ***************************************************************************************************/

	private void doWarehouseModified() {
		Source source = (Source) warehouseEditor.getSelectedItem();
		Boolean useUnavailable = (Boolean) inventoryStatusEditor.getSelectedItem();
		try {
			model.setDestination(source);
			if (useUnavailable != null) {
				resetTableToSingleRow();
			}
			warehouseEditor.removeEmptySelection();
		} catch (Throwable exception) {
			warehouseEditor.setEmptySelection();
			displayException(exception);
		}
	}

	private void doSupplierModified() {
		Source source = (Source) supplierEditor.getData();
		if (source == null) {
			supplierEditor.setText(model.getStockReturn().getDestination().getId());
			return;
		}
		try {
			model.setDestination(source);
			resetTableToSingleRow();
		} catch (Throwable exception) {
			supplierEditor.clear();
			displayException(exception);
		}
	}

	private void doFinisherModified() {
		Source source = null;
		if (returnTypeEditor.getSelectedItem().equals(SourceType.FINISHER)) {
			source = (Source) finisherEditor.getData();
		} else {
			source = (Source) warehouseEditor.getSelectedItem();
		}
		Boolean useUnavailable = (Boolean) inventoryStatusEditor.getSelectedItem();
		try {
			model.setDestination(source);
			if (useUnavailable != null) {
				resetTableToSingleRow();
			}
		} catch (Throwable exception) {
			finisherEditor.clear();
			displayException(exception);
		}
	}

	private void resetTableToSingleRow() {
		lineItemTable.clearRows();
		validateEnabledState();
		Source source = model.getStockReturn().getDestination();
		if (source != null) {
			ReturnReason reason = (ReturnReason) defaultReasonEditor.getSelectedItem();
			lineItemTable.addRow(model.buildNewLineItemWrapper(reason));
			lineItemTable.editCellInLastRow(ReturnProperty.STOCK_ITEM);
		}
	}

	/****************************************************************************************************
	 * Inventory Status Modified
	 ***************************************************************************************************/

	private void doInventoryStatusModified() {
		try {
			Boolean useAvailable = (Boolean) inventoryStatusEditor.getSelectedItem();
			if (model.isWarehouseReturn()) {
				if (useAvailable != null) {
					List<ReturnReason> returnReasons = model.findWarehouseReturnReasons(useAvailable);
					defaultReasonEditor.setItems(returnReasons);
					returnReasonTableEditor.setReturnReasons(returnReasons);
				}
			}
			if (model.isFinisherReturn()) {
				if (useAvailable != null) {
					List<ReturnReason> returnReasons = model.findWarehouseReturnReasons(useAvailable);
					defaultReasonEditor.setItems(returnReasons);
					returnReasonTableEditor.setReturnReasons(returnReasons);
				}
			}
			resetTableToSingleRow();
		} catch (Throwable exception) {
			inventoryStatusEditor.requestFocus();
			displayException(exception);
		}
	}

	/****************************************************************************************************
	 * Default Reason Modified
	 ***************************************************************************************************/
	private void doDefaultReasonModified() {
		ReturnReason defaultReason = (ReturnReason) defaultReasonEditor.getSelectedItem();
		for (ReturnLineItemWrapper wrapper : getAllWrappers()) {
			wrapper.setDefaultReason(defaultReason);
		}
		validateEnabledState();
	}

	/****************************************************************************************************
	 * Context Information Modified
	 ***************************************************************************************************/

	private void doFinisherContextTypeModified() {
		ContextType contextType = (ContextType) finisherContextTypeEditor.getSelectedItem();
		try {
			if (contextType == null) {
				finisherContextValueEditor.clear();
				finisherContextValueEditor.setEnabled(false);
				model.setContextType(null);
				return;
			}
			if (contextType.isPromotion()) {
				finisherContextValueEditor.setEnabled(true);
			} else {
				finisherContextValueEditor.clear();
				finisherContextValueEditor.setEnabled(false);
				model.setContextValue(null);
			}
			model.setContextType(contextType);
		} catch (Throwable exception) {
			finisherContextTypeEditor.clear();
			displayException(exception);
		}
	}

	private void doWarehouseContextTypeModified() {
		ContextType contextType = (ContextType) warehouseContextTypeEditor.getSelectedItem();
		try {
			if (contextType == null) {
				warehouseContextValueEditor.clear();
				warehouseContextValueEditor.setEnabled(false);
				model.setContextType(null);
				return;
			}
			model.setContextType(contextType);
			if (contextType.isPromotion()) {
				warehouseContextValueEditor.setEnabled(true);
			} else {
				warehouseContextValueEditor.clear();
				warehouseContextValueEditor.setEnabled(false);
				model.setContextValue(null);
			}
		} catch (Throwable exception) {
			warehouseContextTypeEditor.clear();
			displayException(exception);
		}
	}

	private void doContextValueModified(RSearchFieldEditor editor) {
		try {
			if (editor.isEnabled()) {
				model.setContextValue(editor.getText());
			} else {
				model.setContextValue(null);
			}
		} catch (Throwable exception) {
			editor.clear();
			displayException(exception);
		}
	}

	private void doCommentsModified() {
		try {
			model.setComments(commentsEditor.getText());
		} catch (BusinessException exception) {
			displayException(exception);
			assignFocusInScreen(commentsEditor);
		} finally {
			validateEnabledState();
		}
	}

	private void doAuthorizationModified() {
		try {
			if (StringHelper.isNullOrEmpty(authorizationEditor.getText())) {
				if (model.getStockReturn().getDestination() instanceof Supplier) {
					Supplier supplier = (Supplier) model.getStockReturn().getDestination();
					if (supplier.isAuthorizationRequired()) {
						throw new BusinessException(ReturnMessageText.MISSING_AUTH_CODE);
					} else {
						model.getStockReturn().doSetAuthorizationCode(authorizationEditor.getText());
					}
				} else {
					model.getStockReturn().doSetAuthorizationCode(authorizationEditor.getText());
				}
			} else {
				model.setAuthorization(authorizationEditor.getText());
			}
		} catch (BusinessException exception) {
			displayException(exception);
			assignFocusInScreen(authorizationEditor);
			if (model.getStockReturn().getAuthorizationCode() != null) {
				authorizationEditor.setText(model.getStockReturn().getAuthorizationCode());
			}

		} finally {
			validateEnabledState();
		}
	}

	/****************************************************************************************************
	 * Stockable Listener
	 ***************************************************************************************************/

	private SimTableEditorListener buildStockItemListener() {
		return new SimTableEditorListener() {
			public void performTableEditorEvent(SimTableEditorEvent event) {
				validateEnabledState();
			}
		};
	}

	/****************************************************************************************************
	 * Search Listeners
	 ***************************************************************************************************/

	private SupplierSearchListener buildSupplierSearchListener() {
		return new SupplierSearchListener() {
			public void assignSupplier(Supplier supplier) {
				if (supplier != null) {
					supplierEditor.setData(supplier);
				}
			}
		};
	}

	private StockItemSearchListener buildItemSearchListener() {
		return new StockItemSearchListener() {
			public void search() {
				model.storeDestinationInRepository();
				super.search();
			}

			public void assignStockItem(StockItem stockItem) {
				stockItemTableEditor.setData(stockItem);
			}
		};
	}

	private FinisherSearchListener buildFinisherSearchListener() {
		return new FinisherSearchListener() {
			public void assignFinisher(Finisher finisher) {
				if (finisher != null) {
					finisherEditor.setData(finisher);
				}
			}
		};
	}

	private PromotionSearchListener buildPromotionSearchListenerForFinisher() {
		return new PromotionSearchListener() {
			public void assignPromotion(PromotionVO promotion) {
				if (promotion != null) {
					finisherContextValueEditor.setData(promotion);
				}
			}
		};
	}

	private PromotionSearchListener buildPromotionSearchListenerForWarehouse() {
		return new PromotionSearchListener() {
			public void assignPromotion(PromotionVO promotion) {
				if (promotion != null) {
					warehouseContextValueEditor.setData(promotion);
				}
			}
		};
	}

	/****************************************************************************************************
	 * Return Item Definition
	 ***************************************************************************************************/

	private class ReturnItemDefinition extends SimTableDefinition {

		public Class getDataClass() {
			return ReturnLineItemWrapper.class;
		}

		public List<String> getOverrideEditableAttributes() {
			return Collections.singletonList(ReturnProperty.SERIAL_NUMBER_COUNT);
		}

		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(10);
			attributes.add(new SimTableAttribute("Item", ReturnProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemTableEditor));
			attributes.add(new SimTableAttribute("Item Description", ReturnProperty.DESCRIPTION));
			attributes.add(new SimTableAttribute("Reason", ReturnProperty.REASON, new TranslatedObjectDisplayer(), returnReasonTableEditor));
			if (model.isNonSellableTypesActive()) {
				attributes.add(new SimTableAttribute("Sub-bucket", ReturnProperty.NON_SELLABLE_TYPE_DESC));
			}
			attributes.add(new SimTableAttribute("Unavailable", ReturnProperty.USE_UNAVAILABLE_QTY, new BooleanDisplayer()));
			attributes.add(new SimTableAttribute("UOM", ReturnProperty.UNIT_OF_MEASURE_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
			attributes.add(new SimTableAttribute("Pack Size", ReturnProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
			attributes.add(new SimTableAttribute("Inventory", ReturnProperty.INVENTORY_BASED_ON_UOM, new LineItemInventoryDisplayer()));
			attributes.add(new SimTableAttribute("Request Qty", ReturnProperty.QUANTITY_REQUESTED_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
			attributes.add(new SimTableAttribute("Quantity", ReturnProperty.QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
			if (model.isSerialNumberProcessingEnabled()) {
				attributes.add(new SimTableAttribute("UIN Qty", ReturnProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
			}
			return attributes;
		}
	}

	/****************************************************************************************************
	 * UIN POPUP LISTENER
	 ***************************************************************************************************/

	private class UINPopupListener implements PopupTableEditorListener {
		public void popupDialog(Object lineItem) {
			ReturnUinDialog dialog = new ReturnUinDialog();
			dialog.setReturnLineItemWrapper((ReturnLineItemWrapper) lineItem);
			dialog.setVisible(true);
		}
	}

	public void handleShipTrailer() {
		model.storeReturnForShipTrailer();
	}

	public boolean isReturnTypeWH() {
		if (!model.getStockReturn().getType().equals(SourceType.WAREHOUSE)) {
			displayError(CommonMessageText.SHIP_TRAILER_INVALID_RETURN_TYPE);
			return false;
		}
		return true;
	}

	public boolean validateShipTrailer() {
		if (!model.validateShipTrailer()) {
			displayError(CommonMessageText.SHIP_TRAILER_REQUIRED);
			return false;
		}
		return true;
	}

	public boolean isShipTrailerEnabled() {
		return model.isShipTrailerEnabled();
	}
}
