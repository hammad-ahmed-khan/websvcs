package oracle.retail.sim.client.screen.shelfreplenishment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.ShelfReplenishmentPickFromDisplayer;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentProperty;

/********************************************************************************************************
 * Shelf Replenishment Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentDetailPanel extends ScreenPanel implements ItemScannerListener {
    private static final long serialVersionUID = 5110914084010828296L;

    private ShelfReplenishmentDetailModel model = new ShelfReplenishmentDetailModel();

    private RDisplayLabelEditor shelfReplenishmentEditor = new RDisplayLabelEditor("ID");
    private RDisplayLabelEditor groupEditor = new RDisplayLabelEditor("Product Group");
    private RDisplayLabelEditor quantityEditor = new RDisplayLabelEditor("Quantity");
    private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
    private RDisplayLabelEditor dateEditor = new RDisplayLabelEditor("Create Date/Time");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");

    private StockItemScannerDialog scannerDialog = null;

    private SimTable lineItemTable = new SimTable(new ShelfReplenishmentItemDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    /****************************************************************************************************
     * Initialization & Layout
     ***************************************************************************************************/

    public ShelfReplenishmentDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        groupEditor.setDisplayer(new AttributeDisplayer("description"));
        dateEditor.setDisplayer(new DateTimeDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        quantityEditor.setDataType(DataTypeConstants.DECIMAL_LEFT);
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(3, 2);
        headerPanel.add(shelfReplenishmentEditor);
        headerPanel.add(groupEditor);
        headerPanel.add(quantityEditor);
        headerPanel.add(userEditor);
        headerPanel.add(dateEditor);
        headerPanel.add(statusEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

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
        model.loadShelfReplenishment();
        if (!model.obtainShelfReplenishmentLock()) {
        	model.setViewOnly(true);
        }
        populateScreen();
        launchScanner();
        checkForSequenceAltered();
    }

    private void populateScreen() throws Exception {
        ShelfReplenishment shelfReplenishment = model.getShelfReplenishment();

        shelfReplenishmentEditor.setData(shelfReplenishment.getId());
        groupEditor.setData(model.readProductGroup(shelfReplenishment.getProductGroupId()));
        quantityEditor.setData(shelfReplenishment.getQuantityToReplenish());
        userEditor.setData(shelfReplenishment.getEmployeeId());
        dateEditor.setData(shelfReplenishment.getCreateDate());
        statusEditor.setData(shelfReplenishment.getStatus());
        lineItemTable = new SimTable(new ShelfReplenishmentItemDefinition());
        lineItemPane.setTable(lineItemTable);
        lineItemTable.setRows(model.getLineItems());
        lineItemTable.setTableEditable(model.isLineItemsEditable());
    }

    private void checkForSequenceAltered() {
        if (model.isShelfReplenishmentSequenceAltered()) {
            RConfirmUtility.showMessage(Application.getFrame(), "Shelf Replenishment List", ShelfReplenishmentMessageText.SEQUENCE_ALTERED);
        }
    }

    public void stop() {
        try {
            model.clearShelfReplenishment();
        } catch (Throwable e) {
            displayException(e);
        }
        shutdownScanner();
    }
    
    /****************************************************************************************************
     * State Methods for Screen
     ***************************************************************************************************/
    
    public boolean isShelfReplenishmentAdjustable() {
        return model.isShelfReplenishmentAdjustable();
    }
    
    public boolean isAllowAdjustment() {
        return model.isAllowAdjustment();
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
            ShelfReplenishmentLineItemWrapper existingWrapper = findExistingWrapper(barcodeItem.getId());
            if (existingWrapper == null) {
                throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
            }
            if (!existingWrapper.isPropertyModifiable(ShelfReplenishmentProperty.ACTUAL_QTY_BASED_ON_UOM)) {
                throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
            }
            model.updateExistingLineItem(existingWrapper, barcodeItem);
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    private ShelfReplenishmentLineItemWrapper findExistingWrapper(String itemId) {
        List<ShelfReplenishmentLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (ShelfReplenishmentLineItemWrapper wrapper : wrappers) {
            StockItem stockItem = wrapper.getStockItem();
            if (stockItem != null && itemId.equals(stockItem.getId())) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        if (!model.isCoherent()) {
            return;
        }
        if (!model.checkShelfReplenishmentLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        if (model.isShelfReplenishmentComplete()) {
            model.updateShelfReplenishment();
            return;
        }
        if (model.isShelfReplenishmentInProgress()) {
            if (RConfirmUtility.confirm("Confirmation", ShelfReplenishmentMessageText.COMPLETE_CONFIRM)) {
                model.completeShelfReplenishment();
            }
        }
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (!model.checkShelfReplenishmentLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        try {
            if (model.isShelfReplenishmentNew() || model.isShelfReplenishmentInProgress()) {
                doPrintPendingShelfReplenishment();
            } else {
                doPrintOtherShelfReplenishment();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
        populateScreen();
    }

    private void doPrintPendingShelfReplenishment() throws Exception {
        if (RConfirmUtility.confirm("Confirmation", ShelfReplenishmentMessageText.PRINT_PENDED_CONFIRM)) {
            model.printPendingShelfReplenishment();
        }
    }

    private void doPrintOtherShelfReplenishment() throws Exception {
        if (RConfirmUtility.confirm("Confirmation", ShelfReplenishmentMessageText.PRINT_CONFIRM)) {
            model.printShelfReplenishment();
        }
    }
    
    /****************************************************************************************************
     * Adjust ShelfReplenishment
     ***************************************************************************************************/

    public boolean handleAdjustShelfReplenishment() throws Exception {
        lineItemTable.stopEditing();
        if (!RConfirmUtility.confirm("Re-Open The Shelf Replenishment", ShelfReplenishmentMessageText.REOPEN_SHELF_REPLENISHMENT)) {
            return false;
        }
        if (!model.obtainShelfReplenishmentLock()) {
            return false;
        }
        model.adjustShelfReplenishment();
        populateScreen();
        launchScanner();
        return true;
    }


    /****************************************************************************************************
     * HANDLE SCANNER
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Shelf Replenishment List Item Table
     ***************************************************************************************************/

    private class ShelfReplenishmentItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ShelfReplenishmentLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(8);
            attributes.add(new SimTableAttribute("Item", ShelfReplenishmentProperty.STOCK_ITEM, new AttributeDisplayer("id")));
            attributes.add(new SimTableAttribute("Description", ShelfReplenishmentProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("Replenish From", ShelfReplenishmentProperty.FROM_AREA, new ShelfReplenishmentPickFromDisplayer()));
            attributes.add(new SimTableAttribute("UOM", ShelfReplenishmentProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", ShelfReplenishmentProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Qty", ShelfReplenishmentProperty.REQUESTED_QTY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isActualQuantityAvailable()) {
                attributes.add(new SimTableAttribute("Actual Quantity", ShelfReplenishmentProperty.ACTUAL_QTY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            }
            return attributes;
        }
    }
}
