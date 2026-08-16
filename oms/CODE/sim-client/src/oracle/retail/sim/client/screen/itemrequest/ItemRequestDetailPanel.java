package oracle.retail.sim.client.screen.itemrequest;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.item.OrderItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.OrderItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.DeliveryTimeSlotTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.OrderItemEstimatedQuantityDisplayer;
import oracle.retail.sim.client.uom.OrderItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.itemrequest.ItemRequestProperty;

/********************************************************************************************************
 * Item Request Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = 4382336737801921435L;

    private ItemRequestDetailModel model = new ItemRequestDetailModel();

    private RDisplayLabelEditor requestEditor = new RDisplayLabelEditor("Request ID");
    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor expirationDateEditor = new RDisplayLabelEditor("Expiration Date");
    private RDateFieldEditor deliveryDateEditor = new RDateFieldEditor("Request Delivery Date");
    private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
    private RComboBoxEditor defaultTimeslotEditor = new RComboBoxEditor("Request Delivery Timeslot");
    private DeliveryTimeSlotTableEditor deliveryTimeslotTableEditor = new DeliveryTimeSlotTableEditor();

    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private OrderItemTableEditor orderItemEditor = new OrderItemTableEditor();
    private SimTable lineItemTable = new SimTable(new RequestDetailDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private OrderItemScannerDialog scannerDialog = null;

    private static final String COMMENTS_MODIFIED = "COMMENTS_MODIFIED";
    private static final String DELIVERY_DATE_MODIFIED = "DELIVERY_DATE_MODIFIED";
    private static final String DELIVERY_TIMESLOT_MODIFIED = "DELIVERY_TIMESLOT_MODIFIED";

    public ItemRequestDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        storeEditor.setDisplayer(new StoreDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        createDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        expirationDateEditor.setDataType(DataTypeConstants.DATE_SHORT);

        commentsEditor.registerAction(this, COMMENTS_MODIFIED);
        commentsEditor.setIdentifier(SimName.ITEM_REQUEST_COMMENT);

        deliveryDateEditor.registerAction(this, DELIVERY_DATE_MODIFIED);

        orderItemEditor.setSearchListener(buildItemSearchListener());
        orderItemEditor.addTableEditorListener(buildOrderItemListener());

        defaultTimeslotEditor.setDisplayer(new AttributeDisplayer("description"));
        defaultTimeslotEditor.setSizeType(EditorConstants.LARGE);
        defaultTimeslotEditor.setSortEnabled(false);
        defaultTimeslotEditor.setEnabled(false);
        defaultTimeslotEditor.registerAction(this, DELIVERY_TIMESLOT_MODIFIED);

        deliveryTimeslotTableEditor.setEnabled(false);
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(3, 3);
        headerPanel.add(requestEditor);
        headerPanel.add(storeEditor);
        headerPanel.add(statusEditor);
        headerPanel.add(createDateEditor);
        headerPanel.add(expirationDateEditor);
        headerPanel.add(deliveryDateEditor);
        headerPanel.add(userEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(commentsEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 2, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        if (model.isMultiDeliveryAllowed()) {
            mainPanel.add(defaultTimeslotEditor, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
        }
        mainPanel.add(lineItemPane, GridTool.constraints(0, 4, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

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

    public void loadItemRequest() throws Exception {
        model.loadItemRequest();
    }

    public void start() throws Exception {
        populateScreen();
        launchScanner();
    }

    private void populateScreen() throws Exception {
        ItemRequest itemRequest = model.getItemRequest();

        if (model.isMultiDeliveryAllowed()) {
            deliveryTimeslotTableEditor.setDeliveryTimeslot(model.getDeliveryTimeSlots());
            deliveryTimeslotTableEditor.setEnabled(true);
            defaultTimeslotEditor.setEnabled(true);
        }

        setActionsEnabled(false);

        try {
            storeEditor.setData(model.getStore(itemRequest.getStoreId()));
        } catch (Throwable exception) {
            displayException(exception);
        }

        userEditor.setData(itemRequest.getUsername());
        statusEditor.setData(itemRequest.getStatus());
        createDateEditor.setData(itemRequest.getCreateDate());
        expirationDateEditor.setData(itemRequest.getExpirationDate());
        deliveryDateEditor.setDate(itemRequest.getReqDeliveryDate());
        commentsEditor.setText(itemRequest.getComments());

        if (model.isItemRequestCompleteOrCancelled()) {
            deliveryDateEditor.setEnabled(false);
            commentsEditor.setEnabled(false);
            lineItemTable.setTableEditable(false);
        } else {
            deliveryDateEditor.setEnabled(true);
            commentsEditor.setEnabled(true);
            lineItemTable.setTableEditable(true);
            defaultTimeslotEditor.setItems(model.getDeliveryTimeSlots());
        }

        if (itemRequest.isNew()) {
            requestEditor.setData(Translator.getText("New"));
            lineItemTable.addRow(model.buildRequestLineItem());
            lineItemTable.editCellInLastRow("stockItem");
        } else {
            requestEditor.setData(itemRequest.getId());
            defaultTimeslotEditor.setSelectedItem(itemRequest.getDeliveryTimeSlot());
            lineItemTable.setRows(model.getItemRequestLineItems());
        }

        setActionsEnabled(true);

        if (isItemRequestUnmodifiable()) {
            lineItemTable.setTableEditable(false);
            storeEditor.setEnabled(false);
            statusEditor.setEnabled(false);
            requestEditor.setEnabled(false);
            userEditor.setEnabled(false);
            createDateEditor.setEnabled(false);
            expirationDateEditor.setEnabled(false);
            deliveryDateEditor.setEnabled(false);
            commentsEditor.setEnabled(false);
            defaultTimeslotEditor.setEnabled(false);
            deliveryTimeslotTableEditor.setEnabled(false);
        }
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
            ItemRequestLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
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

            model.validateItemCount(lineItemTable.getRowCount());

            lineItemWrapper = model.buildRequestLineItem();
            if (lineItemWrapper != null) {
                lineItemWrapper.setOrderItem(scannerDialog.getOrderItem());
                if (lineItemWrapper.getOrderItem() != null) {
                    lineItemTable.addRow(lineItemWrapper);
                }
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    private ItemRequestLineItemWrapper findExistingWrapper(String itemId) {
        List<ItemRequestLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (ItemRequestLineItemWrapper wrapper : wrappers) {
            OrderItem orderItem = wrapper.getOrderItem();
            if (orderItem != null && itemId.equals(orderItem.getId())) {
                return wrapper;
            }
        }
        return null;
    }

    private ItemRequestLineItemWrapper findEmptyWrapper() {
        List<ItemRequestLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (ItemRequestLineItemWrapper wrapper : wrappers) {
            if (wrapper.getOrderItem() == null) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * State Methods 
     ***************************************************************************************************/

    public boolean isItemRequestUnmodifiable() throws Exception {
        return model.isItemRequestUnmodifiable();
    }

    public boolean isAddItemAvailable() {
        return model.isAddItemAvailable();
    }

    public boolean validateIsNotPending() {
        boolean isPending = model.isItemRequestPending();
        deliveryDateEditor.setEnabled(isPending);
        lineItemTable.setTableEditable(isPending);
        return !isPending;
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void stop() {
        try {
            model.releaseRequestLock();
        } catch (Throwable e) {
            displayException(e);
        }
        clearScreen();
        shutdownScanner();
    }

    /****************************************************************************************************
     * Handle Remove Item
     ***************************************************************************************************/

    public void handleRemoveItem() throws Exception {
        if (!model.isLineItemDeletable()) {
            return;
        }
        if (lineItemTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (!RConfirmUtility.confirm("Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return;
        }

        lineItemTable.stopEditing();

        boolean cannotDeleteSomeLineItems = false;
        List<ItemRequestLineItemWrapper> lineItems = lineItemTable.getAllSelectedRowData();
        for (ItemRequestLineItemWrapper wrapper : lineItems) {
            if (wrapper.getDeliveryTimeslot() != null && !model.getDeliveryTimeSlots().contains(wrapper.getDeliveryTimeslot())) {
                cannotDeleteSomeLineItems = true;
            } else {
                model.deleteLineItem(wrapper);
                lineItemTable.removeRow(wrapper);
            }
        }
        if (cannotDeleteSomeLineItems) {
            displayError(ItemRequestMessageText.LINE_ITEM_DEL_PRIV_ERROR);
        }
    }

    public boolean isRequestEmpty() {
        return lineItemTable.isEmpty();
    }

    public void cancelEmptyRequest() throws Exception {
        model.cancelRequest();
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() throws Exception {
        List<ItemRequestLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        ItemRequestLineItemWrapper wrapper = null;
        for (int i = lineItems.size() - 1; i >= 0; i--) {
            wrapper = lineItems.get(i);

            if (wrapper.getLineItem() == null) {
                return;
            }
        }
        model.validateItemCount(lineItemTable.getRowCount());

        lineItemTable.addRow(model.buildRequestLineItem());
        lineItemTable.editCellInLastRow("stockItem");
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        lineItemTable.stopEditing();
        if (model.isItemRequestPending()) {
            if (invalidActivityLock()) {
                return false;
            }
            Date deliveryDate = deliveryDateEditor.getDate();
            if (deliveryDate == null) {
                throw new BusinessException(ItemRequestMessageText.MISSING_DELIVERY_DATE);
            }
            model.updateItemRequest(deliveryDate);
        }
        if (model.isItemRequestCancelled()) {
            model.saveItemRequest();
        }
        clearScreen();
        return true;
    }

    /****************************************************************************************************
     * Handle Request
     ***************************************************************************************************/

    public boolean handleRequest() throws Exception {
        if (invalidActivityLock()) {
            return false;
        }
        Date deliveryDate = deliveryDateEditor.getDate();
        if (deliveryDate == null) {
            displayError(ItemRequestMessageText.MISSING_DELIVERY_DATE);
            return false;
        }
        Date currentDate = SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone());
        if (!SimDateUtil.isValidDateRange(currentDate, deliveryDate)) {
            throw new BusinessException(ItemRequestMessageText.INVALID_REQUEST_DATE);
        }
        if (!RConfirmUtility.confirm("Request Confirmation", ItemRequestMessageText.SUBMIT_CONFIRM)) {
            return false;
        }
        if (model.hasLineItemWithNoQuantity()) {
            if (!RConfirmUtility.confirm("Request Confirmation", CommonMessageText.DEFAULT_TO_ZERO_CONFIRM)) {
                return false;
            }
        }
        model.processItemRequest();
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_REQUEST_DETAIL_MODIFIED, Boolean.TRUE);
        clearScreen();
        return true;
    }

    /****************************************************************************************************
     * Handle Print Request
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        model.printItemRequest();
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    public void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    public void clearScreen() {
        setActionsEnabled(false);
        requestEditor.clear();
        storeEditor.clear();
        statusEditor.clear();
        createDateEditor.clear();
        expirationDateEditor.clear();
        deliveryDateEditor.clear();
        userEditor.clear();
        commentsEditor.clear();
        lineItemTable.clearRows();
        setActionsEnabled(true);
    }

    private boolean invalidActivityLock() throws Exception {
        if (model.checkRequestLock()) {
            return false;
        }
        displayError(CommonMessageText.LOCK_TAKEN_OVER);
        clearScreen();
        navigate(SimNavigation.BACK);
        return true;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(COMMENTS_MODIFIED)) {
            doCommentsModified();
        } else if (command.equals(DELIVERY_DATE_MODIFIED)) {
            doDeliveryDateModified();
        } else if (command.equals(DELIVERY_TIMESLOT_MODIFIED)) {
            doDeliveryTimeSlotModified();
        }
    }

    private void doDeliveryTimeSlotModified() {
        ItemRequest itemRequest = model.getItemRequest();
        try {
            itemRequest.setDeliveryTimeSlot((DeliveryTimeSlot) defaultTimeslotEditor.getSelectedItem());
        } catch (BusinessException exception) {
            displayException(exception);
            assignFocusInScreen(defaultTimeslotEditor);
        }
    }

    private void doCommentsModified() {
        try {
            model.getItemRequest().setComments(commentsEditor.getText());
        } catch (BusinessException exception) {
            displayException(exception);
            assignFocusInScreen(commentsEditor);
        }
    }

    private void doDeliveryDateModified() {
        try {
            model.getItemRequest().setReqDeliveryDate(deliveryDateEditor.getDate(), model.getTimeZone());
        } catch (Exception exception) {
            displayException(exception);
            deliveryDateEditor.setDate(model.getItemRequest().getReqDeliveryDate());
            assignFocusInScreen(deliveryDateEditor);
        }
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private OrderItemSearchListener buildItemSearchListener() {
        return new OrderItemSearchListener() {
            public void assignOrderItem(OrderItem orderItem) {
                orderItemEditor.setData(orderItem);
            }
        };
    }

    private SimTableEditorListener buildOrderItemListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                applyDefaultDeliveryTimeSlot();
            }
        };
    }

    private void applyDefaultDeliveryTimeSlot() {
        try {
            ItemRequestLineItemWrapper wrapper = (ItemRequestLineItemWrapper) lineItemTable.getSelectedRowData();
            if (wrapper != null) {
                model.applyDefaultDeliveryTimeSlot(wrapper);
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Item Request Detail Table Definition
     ***************************************************************************************************/

    private class RequestDetailDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemRequestLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(8);
            attributes.add(new SimTableAttribute("Item", "orderItem", new AttributeDisplayer("id"), orderItemEditor));
            attributes.add(new SimTableAttribute("Item Description", "description"));
            attributes.add(new SimTableAttribute("Available SOH", "availableSOH", new OrderItemEstimatedQuantityDisplayer()));
            attributes.add(new SimTableAttribute("In Transit", "inTransitQuantity"));
            attributes.add(new SimTableAttribute("UOM", ItemRequestProperty.UOM, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", ItemRequestProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Qty", ItemRequestProperty.QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isMultiDeliveryAllowed()) {
                attributes.add(new SimTableAttribute("Request Timeslot", "deliveryTimeslot", new AttributeDisplayer("description"), deliveryTimeslotTableEditor));
            }
            return attributes;
        }
    }
}
