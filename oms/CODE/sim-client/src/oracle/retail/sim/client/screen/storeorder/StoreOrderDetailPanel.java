package oracle.retail.sim.client.screen.storeorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.item.OrderItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.OrderItemScannerDialog;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
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
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.client.tableeditor.SimMoneyTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.OrderItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderProperty;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;
import oracle.retail.sim.common.storeorder.StoreOrderType;

/********************************************************************************************************
 * Store Order Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = 1115286626048243610L;

    private StoreOrderDetailModel model = new StoreOrderDetailModel();

    private RDisplayLabelEditor orderIdEditor = new RDisplayLabelEditor("Order ID");
    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor dateEditor = new RDisplayLabelEditor("Date");
    private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private RRadioButton warehouseButton = new RRadioButton("Warehouse");
    private RRadioButton supplierButton = new RRadioButton("Supplier");
    private RComboBoxEditor warehouseEditor = new RComboBoxEditor();
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
    private RDateFieldEditor notBeforeEditor = new RDateFieldEditor("Not Before Date");
    private RDateFieldEditor notAfterEditor = new RDateFieldEditor("Not After Date");
    private RDisplayLabelEditor totalEditor = new RDisplayLabelEditor("Total");

    private OrderItemTableEditor orderItemEditor = new OrderItemTableEditor();
    private SimTable lineItemTable = new SimTable(new StoreOrderItemDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private ButtonGroup sourceGroup = new ButtonGroup();

    private OrderItemScannerDialog scannerDialog = null;

    private static final String SUPPLIER_SELECTED = "Supplier.selected";
    private static final String SUPPLIER_MODIFIED = "Supplier.modified";
    private static final String WAREHOUSE_SELECTED = "Warehouse.selected";
    private static final String WAREHOUSE_MODIFIED = "Warehouse.modified";

    /****************************************************************************************************
     * Build Screen
     ***************************************************************************************************/

    public StoreOrderDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        commentsEditor.setIdentifier(SimName.ITEM_REQUEST_COMMENT);
        commentsEditor.setTitleAlignment(EditorConstants.TOP);
        notBeforeEditor.setSizeType(EditorConstants.LARGE);

        dateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        totalEditor.setDataType(DataTypeConstants.INTEGER_LEFT);

        warehouseEditor.setDisplayer(new IdNameDisplayer());
        warehouseEditor.setComparator(WarehouseComparator.getInstance());

        supplierButton.registerAction(this, SUPPLIER_SELECTED);
        warehouseButton.registerAction(this, WAREHOUSE_SELECTED);
        supplierEditor.registerAction(this, SUPPLIER_MODIFIED);
        warehouseEditor.registerAction(this, WAREHOUSE_MODIFIED);

        sourceGroup.add(supplierButton);
        sourceGroup.add(warehouseButton);

        supplierEditor.setSearchListener(buildSupplierSearchListener());
        orderItemEditor.setStoreOrder(true);
        orderItemEditor.setSearchListener(buildItemSearchListener());
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(3, 2);
        topPanel.add(orderIdEditor);
        topPanel.add(storeEditor);
        topPanel.add(statusEditor);
        topPanel.add(dateEditor);
        topPanel.add(userEditor);

        REditorPanel datePanel = new REditorPanel(2);
        datePanel.add(notBeforeEditor);
        datePanel.add(notAfterEditor);

        RPanel sourcePanel = new RPanel(new GridBagLayout());
        sourcePanel.setTitleBorder("Source");
        sourcePanel.add(warehouseButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 5, 5));
        sourcePanel.add(warehouseEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 5));
        sourcePanel.add(supplierButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 5));
        sourcePanel.add(supplierEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 5));

        RPanel middlePanel = new RPanel(new GridBagLayout());
        middlePanel.add(sourcePanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        middlePanel.add(datePanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 10, 0, 0, 0));

        RDivider divider1 = new RDivider(RDivider.HORIZONTAL);
        RDivider divider2 = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(commentsEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider1, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(middlePanel, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider2, GridTool.constraints(0, 3, 2, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(totalEditor, GridTool.constraints(0, 4, 2, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 5, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));

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

    public void start() throws Exception {
        if (model.hasSelectedStoreOrder()) {
            model.loadExistingStoreOrder();
        } else {
            model.loadNewStoreOrder();
        }
        warehouseEditor.setItems(model.findAllWarehouses());
        if (isNewStoreOrder()) {
            populateNewOrder();
        } else {
            populateExistingOrder();
        }
        lineItemTable.setRowSelectionAllowed(isNewStoreOrder());
        launchScanner();
    }

    public void stop() {
        shutdownScanner();
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_CREATE_NEW);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_REQUEST_ITEMS);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
    }

    public void assignFocusInScreen() {
        if (warehouseButton.isSelected()) {
            assignFocusInScreen(warehouseButton);
        } else {
            assignFocusInScreen(supplierButton);
        }
    }

    /****************************************************************************************************
     * State Queries
     ***************************************************************************************************/

    public boolean hasStoreOrder() {
        return model.hasStoreOrder();
    }

    public boolean isWarehousedOrder() {
        return warehouseButton.isSelected();
    }

    public boolean isSupplierOrder() {
        return supplierButton.isSelected();
    }

    public boolean isTSFStoreOrder() {
        return model.isTSFStoreOrder();
    }

    public boolean isStoreOrderClosed() {
        return model.isStoreOrderClosed();
    }

    public boolean isStoreOrderApproved() {
        return model.isStoreOrderApproved();
    }

    public boolean isNewStoreOrder() {
        return model.isNewStoreOrder();
    }

    public boolean isStoreOrderUnmodifiable() {
        return model.isStoreOrderUnmodifiable();
    }

    public boolean isAddItemUnavailable() {
        return model.isAddItemUnavailable();
    }

    public boolean isApproveUnavailable() {
        return model.isApproveUnavailable();
    }

    public void setNavigationState(boolean enabled) {
        StoreOrder storeOrder = model.getStoreOrder();
        lineItemTable.setTableEditable(enabled);
        if (model.isStoreOrderApproved()) {
            lineItemTable.setEnabled(true);
        } else {
            lineItemTable.setEnabled(enabled);
        }
        commentsEditor.setEnabled(enabled);
        notBeforeEditor.setEnabled(enabled);
        if (storeOrder.getType() == StoreOrderType.TRANSFER) {
            notAfterEditor.setEnabled(false);
        } else {
            notAfterEditor.setEnabled(enabled);
        }
        checkUnmodifiables();
    }

    /****************************************************************************************************
     * Load Store Order Details
     ***************************************************************************************************/

    private void populateNewOrder() {
        StoreOrder storeOrder = model.getStoreOrder();
        storeEditor.setData(model.getStoreId());
        orderIdEditor.setData(Translator.getText("New"));
        statusEditor.setData(Translator.getText("Pending"));
        dateEditor.setData(SimDateUtil.getCurrentDate());
        userEditor.setData(model.getUserName());
        commentsEditor.setText(storeOrder.getComments());
        commentsEditor.setEnabled(!commentsEditor.isEmpty());
        notBeforeEditor.setDate(storeOrder.getNotBeforeDate());
        notAfterEditor.setDate(storeOrder.getNotAfterDate());
        totalEditor.setData(0);
        warehouseEditor.setEnabled(false);
        supplierEditor.setEnabled(false);
        totalEditor.setVisible(false);

        validateSourceEditors();
    }

    private void populateExistingOrder() throws Exception {
        StoreOrder storeOrder = model.getStoreOrder();

        storeEditor.setData(model.getToLocationInfo());
        orderIdEditor.setData(storeOrder.getStoreOrderNumber());
        statusEditor.setData(storeOrder.getStatusDescription());
        commentsEditor.setText(storeOrder.getComments());
        commentsEditor.setEnabled(!commentsEditor.isEmpty());
        dateEditor.setData(storeOrder.getCreationDate());
        userEditor.setData(storeOrder.getCreationUser());
        notBeforeEditor.setDate(storeOrder.getNotBeforeDate());
        notAfterEditor.setDate(storeOrder.getNotAfterDate());
        totalEditor.setData(storeOrder.getLineItems().size());
        validateSourceEditors();
        lineItemTable.setRows(model.findLineItems());
        validateNonRangedItems();
    }

    private void validateNonRangedItems() {
        List<StoreOrderLineItem> nonRangedLineItems = model.getNonRangedItems();
        if (nonRangedLineItems.isEmpty()) {
            return;
        }
        StringBuilder nonRangedItemIds = new StringBuilder();
        for (Iterator iterator = nonRangedLineItems.iterator(); iterator.hasNext();) {
            OrderItem orderItem = ((StoreOrderLineItem) iterator.next()).getOrderItem();
            if (orderItem != null) {
                nonRangedItemIds.append(orderItem.getId());
            }
            if (iterator.hasNext()) {
                nonRangedItemIds.append(", ");
            }
        }
        displayError(StoreOrderMessageText.STORE_ORDER_RANGE_ERROR, nonRangedItemIds.toString());
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
                scannerDialog = new OrderItemScannerDialog();
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
            StoreOrderLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
            if (lineItemWrapper != null) {
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                return;
            }
            lineItemWrapper = findEmptyWrapper();
            if (lineItemWrapper != null) {
                lineItemWrapper.setOrderItem(scannerDialog.getOrderItem());
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                return;
            }
            lineItemWrapper = model.createNewLineItemWrapper();
            if (lineItemWrapper != null) {
                lineItemWrapper.setOrderItem(scannerDialog.getOrderItem());
                lineItemTable.addRow(lineItemWrapper);
                totalEditor.setData(model.getStoreOrder().getLineItems().size());
                validateSourceEditors();
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    private StoreOrderLineItemWrapper findExistingWrapper(String itemId) {
        List<StoreOrderLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (StoreOrderLineItemWrapper wrapper : wrappers) {
            OrderItem orderItem = wrapper.getOrderItem();
            if (orderItem != null && itemId.equals(orderItem.getId())) {
                return wrapper;
            }
        }
        return null;
    }

    private StoreOrderLineItemWrapper findEmptyWrapper() {
        List<StoreOrderLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (StoreOrderLineItemWrapper wrapper : wrappers) {
            if (wrapper.getOrderItem() == null) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Delete Item
     ***************************************************************************************************/

    // Returns true if cancel entire store order, false if nothing or only single item
    public boolean handleCancelItem() throws Exception {
        if (lineItemTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return false;
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return false;
        }
        lineItemTable.stopEditing();

        List<StoreOrderLineItemWrapper> wrappers = lineItemTable.getAllSelectedRowData();
        for (StoreOrderLineItemWrapper wrapper : wrappers) {
            wrapper.cancelLineItem();
            model.removeLineItem(wrapper);
            lineItemTable.removeRow(wrapper);
        }

        totalEditor.setData(model.getStoreOrder().getLineItems().size());

        if (isNewStoreOrder() && lineItemTable.isEmpty()) {
            validateSourceEditors();
        }
        return cancelEmptyOrder();
    }

    // Helper method to cancel an empty order

    private boolean cancelEmptyOrder() throws Exception {
        if (isStoreOrderEmpty()) {
            if (RConfirmUtility.confirm("Cancel Item Confirmation", CommonMessageText.NO_ROWS_REMAINING)) {
                model.cancelStoreOrder();
                clearScreen();
                clearState();
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() throws BusinessException {
        if (hasInvalidLineItem()) {
            displayError(CommonMessageText.LINE_ITEM_NO_ITEM);
            return;
        }
        StoreOrder storeOrder = model.getStoreOrder();
        if (storeOrder.getFromLocation() == null) {
            if (storeOrder.getType() == StoreOrderType.PURCHASE_ORDER) {
                displayError(StoreOrderMessageText.STORE_ORDER_NO_SUPPLIER);
            } else if (storeOrder.getType() == StoreOrderType.TRANSFER) {
                displayError(StoreOrderMessageText.STORE_ORDER_NO_WAREHOUSE);
            } else {
                displayError(StoreOrderMessageText.MISSING_SOURCE);
            }
            return;
        }

        lineItemTable.addRow(model.buildNewStoreOrderLineItem());
        totalEditor.setData(model.getStoreOrder().getLineItems().size());
        validateSourceEditors();
        lineItemTable.editCellInLastRow(StoreOrderProperty.ORDER_ITEM);
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        if (cancelEmptyOrder()) {
            return;
        }
        StoreOrder storeOrder = model.getStoreOrder();
        if (storeOrder.getStatus() == StoreOrderStatus.CLOSED || storeOrder.getStatus() == StoreOrderStatus.APPROVED) {
            return;
        }
        if (isWarehousedOrder()) {
            if (!RConfirmUtility.confirm("Approval Confirmation", StoreOrderMessageText.WAREHOUSE_SAVE_APPROVE_CONFIRM)) {
                return;
            }
            updateOrder();
            storeOrder.setStatus(StoreOrderStatus.APPROVED);
        } else {
            updateOrder();
        }
        saveOrder();
    }

    public void handleApprove() throws Exception {
        updateOrder();
        model.getStoreOrder().setStatus(StoreOrderStatus.APPROVED);
        saveOrder();
    }

    private void updateOrder() throws Exception {
        StoreOrder storeOrder = model.getStoreOrder();
        validateNotBeforeDate();
        validateNotAfterDate();
        storeOrder.setCreationDate(SimDateUtil.getCurrentDate());
        storeOrder.setComments(commentsEditor.getText());
        if (!userEditor.isEmpty()) {
            storeOrder.setCreationUser(userEditor.getText());
        }
    }

    private void saveOrder() throws Exception {
        StoreOrder storeOrder = model.getStoreOrder();
        if (storeOrder == null || !model.isCoherent(storeOrder) || hasInvalidLineItem()) {
            return;
        }
        if (isNewStoreOrder()) {
            model.createStoreOrder();
        } else if (!model.isStoreOrderClosed()) {
            updateStoreOrderDetails();
            model.updateStoreOrder();
        }
        clearScreen();
        clearState();
    }

    private void updateStoreOrderDetails() throws Exception {
        StoreOrder storeOrder = model.getStoreOrder();

        storeOrder.setCreationUser(model.getUserName());
        storeOrder.setToLocation(BOFactory.createStore(model.getStoreId()));
        storeOrder.setComments(commentsEditor.getText());

        if (model.isPOStoreOrder() && !notAfterEditor.isEmpty()) {
            storeOrder.setNotAfterDate(notAfterEditor.getDate());
        }
        if (!notBeforeEditor.isEmpty()) {
            storeOrder.setNotBeforeDate(notBeforeEditor.getDate());
        }
    }

    /****************************************************************************************************
     * Handle Item Orders/Sales
     ***************************************************************************************************/

    public boolean handleItemOrders() {
        if (isStoreOrderValidForDetails()) {
            StoreOrderLineItemWrapper lineItem = (StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData();
            if (isValidLineItem(lineItem)) {
                model.storeStoreOrderForItemOrders(lineItem);
                storeSelectedStoreOrder();
                return true;
            } else {
                displayError(ItemMessageText.ITEM_MISSING);
                return false;
            }
        }
        return false;
    }

    public boolean handleItemSales() {
        if (isStoreOrderValidForDetails()) {
            StoreOrderLineItemWrapper lineItem = (StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData();
            if (isValidLineItem(lineItem)) {
                storeSelectedStoreOrder();
                return true;
            } else {
                displayError(ItemMessageText.ITEM_MISSING);
                return false;
            }
        }
        return false;
    }

    private boolean isStoreOrderValidForDetails() {
        if (isStoreOrderEmpty()) {
            displayError(StoreOrderMessageText.ORDER_EMPTY);
            return false;
        }
        if (lineItemTable.getSelectedRowCount() == 0) {
            displayError(StoreOrderMessageText.MISSING_LINE_ITEM);
            return false;
        }
        StoreOrderLineItemWrapper wrapper = (StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData();

        if (wrapper.getCaseSize() == null) {
            displayError(ItemMessageText.ITEM_VERIFY_CASE_SIZE);
            return false;
        }
        return true;
    }

    public boolean isStoreOrderEmpty() {
        return lineItemTable.getRowCount() == 0;
    }

    private void storeSelectedStoreOrder() {
        model.storeStoreOrderForItemOrders((StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData());
    }

    /****************************************************************************************************
     * Handle Deals Query
     ***************************************************************************************************/
    public boolean handleDealsQuery() throws Exception {
        if (!isStoreOrderValidForDealsQuery()) {
            return false;
        }
        StoreOrder storeOrder = model.getStoreOrder();

        Supplier supplier = (Supplier) storeOrder.getFromLocation();

        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_SUPPLIER, supplier);
        RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_DETAIL, storeOrder);

        // Adds not before date state object so deals query can access it
        if (storeOrder.getNotBeforeDate() != null) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_NOT_BEFORE_DATE, storeOrder.getNotBeforeDate());
        }

        StoreOrderLineItemWrapper wrapper = (StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, wrapper.getLineItem());
        return true;
    }

    /**
     * Validates store order for DealsQuery screen; will either return true or throw a UIException.
     */
    private boolean isStoreOrderValidForDealsQuery() throws Exception {
        StoreOrder storeOrder = model.getStoreOrder();
        if (storeOrder.getType() != StoreOrderType.PURCHASE_ORDER) {
            throw new BusinessException(StoreOrderMessageText.STORE_ORDER_NO_SUPPLIER);
        } else if (storeOrder.getFromLocation() == null) {
            throw new BusinessException(StoreOrderMessageText.STORE_ORDER_NO_SUPPLIER);
        } else if (lineItemTable.getSelectedRowCount() == 0) {
            throw new BusinessException(StoreOrderMessageText.DEAL_REQUIRES_ITEM);
        }
        StoreOrderLineItemWrapper wrapper = (StoreOrderLineItemWrapper) lineItemTable.getSelectedRowData();
        if (wrapper.getCaseSize() == null) {
            throw new BusinessException(ItemMessageText.ITEM_VERIFY_CASE_SIZE);
        }
        validateNotBeforeDate();
        validateNotAfterDate();
        return true;
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/
    public void handleCancel() {
        clearScreen();
        clearState();
    }

    private void clearScreen() {
        model.clearStoreOrder();

        orderIdEditor.clear();
        storeEditor.clear();
        statusEditor.clear();
        dateEditor.clear();
        userEditor.clear();
        commentsEditor.clear();
        warehouseButton.setEnabled(true);
        warehouseButton.setSelected(true);
        warehouseEditor.setEmptySelection();
        warehouseEditor.setEnabled(true);
        supplierEditor.setEnabled(true);
        supplierEditor.clear();
        supplierEditor.setEnabled(false);
        notBeforeEditor.clear();
        notAfterEditor.clear();
        totalEditor.clear();

        lineItemTable.clearRows();
    }

    private void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_DETAIL);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_REQUEST_ITEMS);
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_TEMP);
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        model.printStoreOrder();
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    public void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Date Validation
     ***************************************************************************************************/

    // Transfer/Warehouse has not after date same as not before date
    private void validateNotAfterDate() throws Exception {
        if (model.isPOStoreOrder()) {
            if (notAfterEditor.isEmpty()) {
                throw new BusinessException(StoreOrderMessageText.MISSING_NOT_AFTER_DATE);
            }
            model.getStoreOrder().setNotAfterDate(notAfterEditor.getDate());
        } else {
            if (notBeforeEditor.isEmpty()) {
                throw new BusinessException(StoreOrderMessageText.MISSING_NOT_BEFORE_DATE);
            }
            model.getStoreOrder().setNotBeforeDate(notBeforeEditor.getDate());
            model.getStoreOrder().setNotAfterDate(notBeforeEditor.getDate());
        }
    }

    private void validateNotBeforeDate() throws Exception {
        if (notBeforeEditor.isEmpty()) {
            throw new BusinessException(StoreOrderMessageText.MISSING_NOT_BEFORE_DATE);
        }
        model.getStoreOrder().setNotBeforeDate(notBeforeEditor.getDate());
    }

    /****************************************************************************************************
     * Validations
     ***************************************************************************************************/
    private boolean isValidLineItem(StoreOrderLineItemWrapper lineItem) {
        if (lineItem.getOrderItem() == null) {
            return false;
        }
        return true;
    }

    private boolean hasInvalidLineItem() {
        List<StoreOrderLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (StoreOrderLineItemWrapper wrapper : wrappers) {
            if (wrapper.getOrderItem() == null || wrapper.getQuantityBasedOnUom() == null) {
                return true;
            }
            if (wrapper.getQuantity().doubleValue() <= 0.0d) {
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Helper Method to Validate Source Editors
     ***************************************************************************************************/

    // If the store order is a PO type, meaning a supplier was selected, then populate the supplier
    // panel. If the store order is a Transfer type, meaning a warehouse was selected, then populate the
    // warehouse panel.
    private void validateSourceEditors() {
        boolean sourceEnabled = isNewStoreOrder() && lineItemTable.isEmpty();
        warehouseButton.setEnabled(sourceEnabled);
        supplierButton.setEnabled(sourceEnabled);

        StoreOrder storeOrder = model.getStoreOrder();

        boolean dateEnabled = storeOrder.getStatus() != StoreOrderStatus.CLOSED && storeOrder.getStatus() != StoreOrderStatus.APPROVED;

        if (storeOrder.getType() == StoreOrderType.PURCHASE_ORDER) {
            supplierEditor.setData(storeOrder.getFromLocation());
            warehouseEditor.setEmptySelection();
            supplierButton.setSelected(true);
            supplierEditor.setEnabled(sourceEnabled);
            warehouseEditor.setEnabled(false);
            notBeforeEditor.setEnabled(dateEnabled);
            notAfterEditor.setEnabled(dateEnabled);
        } else if (storeOrder.getType() == StoreOrderType.TRANSFER) {
            warehouseEditor.setSelectedItem(storeOrder.getFromLocation());
            supplierEditor.clear();
            warehouseButton.setSelected(true);
            supplierEditor.setEnabled(false);
            warehouseEditor.setEnabled(sourceEnabled);
            notBeforeEditor.setEnabled(dateEnabled);
            notAfterEditor.setEnabled(false);
        }
        checkUnmodifiables(); //must do this at the end
    }

    private void checkUnmodifiables() {
        if (isStoreOrderUnmodifiable()) {
            warehouseEditor.setEnabled(false);
            supplierEditor.setEnabled(false);
            warehouseButton.setEnabled(false);
            supplierButton.setEnabled(false);
            notBeforeEditor.setEnabled(false);
            notAfterEditor.setEnabled(false);
            commentsEditor.setEnabled(false);
            orderItemEditor.setEnabled(false);
            lineItemTable.setEnabled(false);
        }
    }

    /****************************************************************************************************
     * Handle Panel Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (model.hasStoreOrder()) {
            if (command.equals(SUPPLIER_SELECTED)) {
                doSupplierSelected();
            } else if (command.equals(SUPPLIER_MODIFIED)) {
                doSupplierModified();
            } else if (command.equals(WAREHOUSE_SELECTED)) {
                doWarehouseSelected();
            } else if (command.equals(WAREHOUSE_MODIFIED)) {
                doWarehouseModified();
            }
        }
    }

    /****************************************************************************************************
     * Supplier Panel Events
     ***************************************************************************************************/

    private void doSupplierSelected() {
        model.getStoreOrder().setType(StoreOrderType.PURCHASE_ORDER);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STORE_ORDER_TYPE_MODIFIED));
        warehouseEditor.setEmptySelection();
        warehouseEditor.setEnabled(false);
        supplierEditor.setEnabled(true);
        notAfterEditor.setEnabled(true);
        totalEditor.setVisible(true);
        refreshTable();
    }

    private void doSupplierModified() {
        if (supplierEditor.getData() == null) {
            return;
        }
        try {
            model.getStoreOrder().setFromLocation((Supplier) supplierEditor.getData());
        } catch (BusinessException exception) {
            supplierEditor.clear();
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Warehouse Selected
     ***************************************************************************************************/

    private void doWarehouseSelected() {
        model.getStoreOrder().setType(StoreOrderType.TRANSFER);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STORE_ORDER_TYPE_MODIFIED));
        supplierEditor.clear();
        supplierEditor.setEnabled(false);
        warehouseEditor.setEnabled(true);
        notAfterEditor.setEnabled(false);
        totalEditor.setVisible(false);
        refreshTable();
    }

    private void doWarehouseModified() {
        if (warehouseEditor.getSelectedItem() == null) {
            return;
        }
        try {
            model.getStoreOrder().setFromLocation((Warehouse) warehouseEditor.getSelectedItem());
        } catch (BusinessException exception) {
            warehouseEditor.clear();
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Refresh The Table
     ***************************************************************************************************/

    private void refreshTable() {
        try {
            lineItemTable = new SimTable(new StoreOrderItemDefinition());
            lineItemPane.setTable(lineItemTable);
            lineItemTable.setRows(model.findLineItems());
            validateNonRangedItems();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Search Listeners
     ***************************************************************************************************/

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                if (supplier != null) {
                    supplierEditor.setData(supplier);
                    validateSourceEditors();
                }
            }
        };
    }

    private OrderItemSearchListener buildItemSearchListener() {
        return new OrderItemSearchListener() {
            public void search() {
                model.storeFromLocation();
                super.search();
            }

            public void assignOrderItem(OrderItem orderItem) {
                try {
                    orderItemEditor.setData(orderItem);
                    lineItemTable.editCellInSelectedRow(StoreOrderProperty.CASE_SIZE);
                } catch (Throwable exception) {
                    displayException(exception);
                    lineItemTable.editCellInSelectedRow(StoreOrderProperty.ORDER_ITEM);
                }
                validateSourceEditors();
            }
        };
    }

    /****************************************************************************************************
     * Store Order Item Table
     ***************************************************************************************************/

    private class StoreOrderItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreOrderLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(6);
            attributes.add(new SimTableAttribute("Item", StoreOrderProperty.ORDER_ITEM, new AttributeDisplayer("id"), orderItemEditor));
            attributes.add(new SimTableAttribute("Item Description", "description"));
            attributes.add(new SimTableAttribute("UOM", StoreOrderProperty.UOM, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", StoreOrderProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor(), model.isPackSizeEnabled()));
            attributes.add(new SimTableAttribute("Qty", StoreOrderProperty.QTY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isUnitCostEnterable()) {
                attributes.add(new SimTableAttribute("Unit Cost", "unitCost", new SimMoneyDisplayer(), new SimMoneyTableEditor(false)));
            }
            return attributes;
        }
    }
}
