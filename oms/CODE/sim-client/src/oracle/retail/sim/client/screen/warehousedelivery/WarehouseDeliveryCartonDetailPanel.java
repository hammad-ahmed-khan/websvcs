package oracle.retail.sim.client.screen.warehousedelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;

/********************************************************************************************************
 * Warehouse Delivery Carton Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryCartonDetailPanel extends ScreenPanel implements ItemScannerListener {
    private static final long serialVersionUID = -3869155631289891805L;

    private WarehouseDeliveryCartonDetailModel model = new WarehouseDeliveryCartonDetailModel();

    private RDisplayLabelEditor sourceEditor = new RDisplayLabelEditor("From");
    private RDisplayLabelEditor asnIdEditor = new RDisplayLabelEditor("ASN ID");
    private RDisplayLabelEditor cartonIdEditor = new RDisplayLabelEditor("Container ID");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor etaEditor = new RDisplayLabelEditor("ETA");
    private RDisplayLabelEditor receiveDateEditor = new RDisplayLabelEditor("Receive Date");
    private RDisplayLabelEditor expectedCasesEditor = new RDisplayLabelEditor("Expected Cases");
    private RDisplayLabelEditor receivedCasesEditor = new RDisplayLabelEditor("Received Cases");
    private RDisplayLabelEditor damagedLinesEditor = new RDisplayLabelEditor("Damaged Lines");

    private StockItemScannerDialog scannerDialog;

    private StockItemTableEditor stockItemEditor = new StockItemTableEditor();
    private SimTable lineItemTable = new SimTable(new WarehouseDeliveryItemDefinition());

    public WarehouseDeliveryCartonDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        sourceEditor.setDisplayer(new IdNameDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        etaEditor.setDataType(DataTypeConstants.DATE_SHORT);
        receiveDateEditor.setDisplayer(new DateTimeDisplayer());
        expectedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        receivedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        damagedLinesEditor.setDataType(DataTypeConstants.INTEGER);
        stockItemEditor.setSearchListener(buildItemSearchListener());
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
        lineItemTable.getModel().addTableModelListener(buildTableQuantityListener());
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(3, 3);
        headerPanel.add(cartonIdEditor);
        headerPanel.add(asnIdEditor);
        headerPanel.add(sourceEditor);
        headerPanel.add(statusEditor);
        headerPanel.add(etaEditor);
        headerPanel.add(receiveDateEditor);
        headerPanel.add(expectedCasesEditor);
        headerPanel.add(receivedCasesEditor);
        headerPanel.add(damagedLinesEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);
        SimTablePane lineItemPane = new SimTablePane(lineItemTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

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

    public boolean isDeliveryClosed() {
        return model.isDeliveryClosed();
    }

    public boolean isAddItemAllowed() {
        return model.isAddItemAllowed();
    }

    public boolean isDeleteItemAllowed() {
        return model.isDeleteItemAllowed();
    }

    /****************************************************************************************************
     * Start Methods
     ***************************************************************************************************/

    public void start() {
        try {
            model.loadState();
            populateScreen();
            launchScanner();
        } catch (Throwable t) {
            displayException(t);
        }
    }

    public void stop() {
        model.clearState();
        shutdownScanner();
    }

    /****************************************************************************************************
     * SCANNER METHODS
     ***************************************************************************************************/

    protected boolean isScannerAvailable() {
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
                scannerDialog.activateReceiving();
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
            WarehouseDeliveryLineItemWrapper lineItem = findExistingLineItem(barcodeItem.getId());
            if (lineItem != null) {
                model.updateExistingLineItem(lineItem, barcodeItem);
                return;
            }
            if (!model.isAddItemAllowed()) {
                throw new UIException(CommonMessageText.LINE_ITEM_NOT_FOUND, RErrorSeverity.WARNING);
            }
            lineItem = findNewLineItem();
            if (lineItem == null) {
                lineItem = model.createNewLineItemWrapper();
                lineItemTable.addRow(lineItem);
            }
            lineItem.setStockItem(barcodeItem.getStockItem());
            model.updateExistingLineItem(lineItem, barcodeItem);
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
            refreshHeader();
        }
    }

    /****************************************************************************************************
     * HANDLE SCANNER
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() {
        model.storeCanceled();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() {
        lineItemTable.stopEditing();
        try {
            if (model.isViewOnlyMode()) {
                return true;
            }
            if (model.isDeliveryClosed()) {
                return true;
            }
            if (!checkLock()) {
                return false;
            }
            removeNewLineItem();
            if (model.isSerialNumberProcessingEnabled()) {
                if (!validateUnreceivedUINItems()) {
                    return false;
                }
                validateReceivedItemsMissingUINs();
            }
            model.receiveCarton();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    private boolean validateUnreceivedUINItems() throws Exception {
        boolean userConfirmed = false;
        List<WarehouseDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (WarehouseDeliveryLineItemWrapper lineItem : lineItems) {
            if (!lineItem.isSerialNumberRequired() || lineItem.getQuantityReceivedBasedOnUom() != null) {
                continue;
            }
            if (!userConfirmed) {
                if (!RConfirmUtility.confirm("Warehouse Delivery", CommonMessageText.UIN_RECEIVE_ZERO_QTY_CONFIRM)) {
                    lineItemTable.setRowSelection(lineItem);
                    return false;
                }
                userConfirmed = true;
            }
            lineItem.setQuantityReceivedBasedOnUom(Quantity.ZERO);
        }
        return true;
    }

    private void validateReceivedItemsMissingUINs() throws Exception {
        List<String> itemsMissingUins = new ArrayList<String>();
        boolean isItemShortDescription = SimConfigManager.isItemShortDescription();
        WarehouseDeliveryLineItemWrapper firstLineItemMissingUins = null;
        List<WarehouseDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (WarehouseDeliveryLineItemWrapper lineItem : lineItems) {
            if (lineItem.isMissingRequiredUins()) {
                if (firstLineItemMissingUins == null) {
                    firstLineItemMissingUins = lineItem;
                }
                itemsMissingUins.add(lineItem.getItemIdDescription(isItemShortDescription));
            }
        }
        if (itemsMissingUins.isEmpty()) {
            return;
        }
        if (firstLineItemMissingUins != null) {
            lineItemTable.setRowSelection(firstLineItemMissingUins);
        }
        if (itemsMissingUins.size() == 1) {
            throw new BusinessException(CommonMessageText.UIN_REQUIRED, itemsMissingUins.get(0));
        }
        throw new BusinessException(CommonMessageText.UIN_REQUIRED_MULTI, itemsMissingUins);
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() throws Exception {
        lineItemTable.stopEditing();
        if (!model.isAddItemAllowed()) {
            throw new BusinessException(CommonMessageText.FUNCTION_NOT_AVAILABLE);
        }
        if (findNewLineItem() != null) {
            return;
        }
        if (checkLock()) {
            addNewLineItem();
        }
    }

    private void addNewLineItem() {
        lineItemTable.addRow(model.createNewLineItemWrapper());
        lineItemTable.editCellInLastRow(WarehouseDeliveryProperty.STOCK_ITEM);
    }

    private void removeNewLineItem() {
        WarehouseDeliveryLineItemWrapper lineItem = findNewLineItem();
        if (lineItem != null) {
            lineItemTable.removeRow(lineItem);
        }
    }

    private WarehouseDeliveryLineItemWrapper findNewLineItem() {
        if (lineItemTable.isEmpty()) {
            return null;
        }
        WarehouseDeliveryLineItemWrapper lineItem = (WarehouseDeliveryLineItemWrapper) lineItemTable.getRowData(lineItemTable.getLastRowIndex());
        if (lineItem != null && lineItem.isNew()) {
            return lineItem;
        }
        return null;
    }

    private WarehouseDeliveryLineItemWrapper findExistingLineItem(String itemId) {
        List<WarehouseDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (WarehouseDeliveryLineItemWrapper lineItem : lineItems) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem != null && itemId.equals(stockItem.getId())) {
                return lineItem;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleRemoveItem() throws Exception {
        lineItemTable.stopEditing();
        if (!model.isDeleteItemAllowed()) {
            throw new BusinessException(CommonMessageText.FUNCTION_NOT_AVAILABLE);
        }
        if (lineItemTable.getSelectedRowCount() <= 0) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM) || !checkLock()) {
            return;
        }
        List<WarehouseDeliveryLineItemWrapper> lineItems = lineItemTable.getAllSelectedRowData();
        for (WarehouseDeliveryLineItemWrapper lineItem : lineItems) {
            if (!lineItem.isDeleteAllowed()) {
                throw new BusinessException(ItemMessageText.ITEM_REMOVE_ERROR);
            }
            lineItem.removeLineItem();
            lineItemTable.removeRow(lineItem);
        }
        refreshHeader();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void populateScreen() throws Exception {
        boolean notViewOnly = !model.isViewOnlyMode();
        boolean deliveryClosed = model.isDeliveryClosed();
        WarehouseDeliveryCarton carton = model.getCarton();
        WarehouseDelivery delivery = carton.getDelivery();
        sourceEditor.setData(delivery.getSource());
        asnIdEditor.setData(delivery.getAsnId());
        cartonIdEditor.setData(carton.getExternalId());
        statusEditor.setData(carton.getStatus());
        etaEditor.setData(delivery.getExpectedArrivalDate());
        Date completeDate = delivery.getCompleteDate();
        if (completeDate == null && notViewOnly && !deliveryClosed) {
            completeDate = SimDateUtil.getCurrentDate();
        }
        receiveDateEditor.setData(completeDate);
        expectedCasesEditor.setData(carton.getNumberOfCasesExpected().intValue());
        refreshHeader();

        lineItemTable.setRows(model.createLineItemWrappers());
    }

    private void refreshHeader() {
        WarehouseDeliveryCarton carton = model.getCarton();
        receivedCasesEditor.setData(carton.getNumberOfCasesReceived().intValue());
        damagedLinesEditor.setData(carton.getNumberOfLineItemsDamaged());
    }

    private boolean checkLock() {
        boolean locked = false;
        try {
            locked = model.checkLock();
        } catch (Throwable t) {
            displayException(t);
        }
        if (!locked) {
            model.storeLockBroken();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            navigate(SimNavigation.BACK);
        }
        return locked;
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                stockItemEditor.setData(stockItem);
            }
        };
    }

    /****************************************************************************************************
     * Table Editor Listeners
     ***************************************************************************************************/

    private TableModelListener buildTableQuantityListener() {
        return new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                refreshHeader();
            }
        };
    }

    /****************************************************************************************************
     * Warehouse Delivery Item Table Definition
     ***************************************************************************************************/

    private class WarehouseDeliveryItemDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return WarehouseDeliveryLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Item", WarehouseDeliveryProperty.STOCK_ITEM, new AttributeDisplayer(WarehouseDeliveryProperty.ID), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", WarehouseDeliveryProperty.STOCK_ITEM_DESCRIPTION, false));
            attributes.add(new SimTableAttribute("UOM", WarehouseDeliveryProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", WarehouseDeliveryProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Expected", WarehouseDeliveryProperty.QUANTITY_EXPECTED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Received", WarehouseDeliveryProperty.QUANTITY_RECEIVED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Damaged", WarehouseDeliveryProperty.QUANTITY_DAMAGED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {
        public void popupDialog(Object data) {
            WarehouseDeliveryUinDialog dialog = new WarehouseDeliveryUinDialog(FunctionalArea.WAREHOUSE_DELIVERY_RECEIPT);
            dialog.setSerialNumberLineItemWrapper((SerialNumberLineItemWrapper) data);
            dialog.setVisible(true);
            lineItemTable.updateRow(data);
        }
    }
}
