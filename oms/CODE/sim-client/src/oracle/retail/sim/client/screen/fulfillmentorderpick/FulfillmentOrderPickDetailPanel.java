package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
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
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.item.BarcodeItem;

/********************************************************************************************************
 * Fulfillment Order Pick Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickDetailPanel extends ScreenPanel implements ItemScannerListener {

    private static final long serialVersionUID = -6665833554362995427L;

    private FulfillmentOrderPickDetailModel model = new FulfillmentOrderPickDetailModel();

    private RDisplayLabelEditor pickEditor = new RDisplayLabelEditor("Pick ID");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor completeDateEditor = new RDisplayLabelEditor("Complete Date");
    private RDisplayLabelEditor createUserEditor = new RDisplayLabelEditor("Create User");
    private RDisplayLabelEditor completeUserEditor = new RDisplayLabelEditor("Complete User");

    private StockItemScannerDialog scannerDialog = null;

    private SimTable lineItemTable = new SimTable(new CustomerOrderPickItemsDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public FulfillmentOrderPickDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        pickEditor.setDisplayer(new IdNameDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        createDateEditor.setDisplayer(new DateTimeDisplayer());
        completeDateEditor.setDisplayer(new DateTimeDisplayer());

        lineItemTable.setSingleRowSelectionMode();
        lineItemTable.setColumnSize(FulfillmentOrderPickProperty.DISCREPANCY_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(FulfillmentOrderPickProperty.PICK_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(FulfillmentOrderPickProperty.QUANTITY, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        REditorPanel detailPanel = new REditorPanel(2, 3);
        detailPanel.add(pickEditor);
        detailPanel.add(statusEditor);
        detailPanel.add(createDateEditor);
        detailPanel.add(completeDateEditor);
        detailPanel.add(createUserEditor);
        detailPanel.add(completeUserEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
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

    public boolean isPickClosed() {
        return model.isPickClosed();
    }

    public boolean isPickBinType() {
        return model.isPickBinType();
    }

    public boolean confirmActivityLock() throws Exception {
        return model.checkLock();
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadPick();
        populateScreen();
        launchScanner();
        validateBinsCaptured();
    }

    private void populateScreen() throws Exception {
        FulfillmentOrderPick pick = model.getPick();

        pickEditor.setData(pick.getId());
        statusEditor.setData(pick.getStatus());
        createDateEditor.setData(pick.getCreateDate());
        completeDateEditor.setData(pick.getCompleteDate());
        createUserEditor.setData(pick.getCreateUser());
        completeUserEditor.setData(pick.getCompleteUser());

        lineItemTable.setRows(model.getPickItems());
        lineItemTable.setTableEditable(model.isPickEditAllowed());
    }

    private void validateBinsCaptured() throws Exception {
        if (!model.isBinsCaptured()) {
            handleBins();
        }
    }

    public void stop() {
        shutdownScanner();
    }

    /****************************************************************************************************
     * Handle Dispatch
     ***************************************************************************************************/

    public boolean handleConfirm() {
        lineItemTable.stopEditing();
        try {
            FulfillmentOrderPick pick = model.getPick();

            if (model.isPickEmpty()) {
                if (!pick.isNew()) {
                    if (RConfirmUtility.confirm("Delete Pick Confirmation", FulfillmentOrderMessageText.EMPTY_PICK_WARNING)) {
                        model.cancelPick();
                        model.releaseLock();
                        return true;
                    }
                    return false;
                }
                return true;
            }

            if (RConfirmUtility.confirm("Pick Confirm Confirmation", FulfillmentOrderMessageText.CONFIRM_PICK_CONFIRM)) {
                model.confirmPick();
                model.releaseLock();
                return true;
            }

        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Bins
     ***************************************************************************************************/

    public void handleBins() throws Exception {
        BinDetailDialog dialog = new BinDetailDialog();
        dialog.setPick(model.getPick());
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Item Substitution
     ***************************************************************************************************/

    public void handleSubstitution() throws Exception {
        FulfillmentOrderPickLineItemWrapper wrapper = (FulfillmentOrderPickLineItemWrapper) lineItemTable.getSelectedRowData();
        if (wrapper == null) {
            throw new BusinessException(FulfillmentOrderMessageText.MISSING_ROW);
        }
        if (!wrapper.isSubstituteAllowed()) {
            throw new BusinessException(FulfillmentOrderMessageText.ITEM_SUBSTITUTION_NOT_ALLOWED);
        }
        ItemSubstitutionDialog dialog = new ItemSubstitutionDialog();
        dialog.setWrapper(wrapper);
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        model.printPick();
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    public void handleScanner() throws Exception {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() throws Exception {
        if (model.isPickEditAllowed()) {
            model.releaseLock();
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
            List<FulfillmentOrderPickLineItemWrapper> existingWrappers = lineItemTable.getAllRowData();
            List<FulfillmentOrderPickLineItemWrapper> foundWrappers = new ArrayList<FulfillmentOrderPickLineItemWrapper>();
            for (FulfillmentOrderPickLineItemWrapper wrapper : existingWrappers) {
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
                FulfillmentOrderPickLineItemDialog dialog = new FulfillmentOrderPickLineItemDialog();
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
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() {
        lineItemTable.stopEditing();
        try {
            model.savePick();
            model.releaseLock();
            return true;
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Line Item Table Definition
     ***************************************************************************************************/

    private class CustomerOrderPickItemsDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderPickLineItemWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.PRIMARY_LOCATION, true));
            attributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.ITEM_ID, true));
            attributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.BIN_ID, true));
            attributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.SIM_CUSTOMER_ORDER_ID, true));
            attributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.PICK_QTY, true));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(12);
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderPickProperty.ITEM_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Description", FulfillmentOrderPickProperty.ITEM_DESCRIPTION, new TranslatedObjectDisplayer()));
            if (model.isSequenceFieldEnabled()) {
                attributes.add(new SimTableAttribute("Primary Location", FulfillmentOrderPickProperty.PRIMARY_LOCATION, new TranslatedObjectDisplayer()));
            }
            attributes.add(new SimTableAttribute("SIM Customer Order ID", FulfillmentOrderPickProperty.SIM_CUSTOMER_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Bin ID", FulfillmentOrderPickProperty.BIN_ID));
            attributes.add(new SimTableAttribute("Fulfillment ID", FulfillmentOrderPickProperty.FULFILLMENT_ID));
            attributes.add(new SimTableAttribute("UOM", FulfillmentOrderPickProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", FulfillmentOrderPickProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Substitute", FulfillmentOrderPickProperty.SUBSTITUTE, new BooleanDisplayer()));
            attributes.add(new SimTableAttribute("Adjusted Pick Qty", FulfillmentOrderPickProperty.DISCREPANCY_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Pick Qty", FulfillmentOrderPickProperty.PICK_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", FulfillmentOrderPickProperty.QUANTITY, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            return attributes;
        }
    }

}
