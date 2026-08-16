package oracle.retail.sim.client.screen.invadjustment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.InventoryDispositionDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.LineItemInventoryDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.tableeditor.InventoryAdjustmentReasonTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentProperty;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * Inventory Adjustment Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = 324666090570383960L;

    private InventoryAdjustmentDetailModel model = new InventoryAdjustmentDetailModel();

    private RDisplayLabelEditor adjustmentEditor = new RDisplayLabelEditor("Adjustment Number");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor referenceEditor = new RDisplayLabelEditor("Reference Adj. ID");
    private RComboBoxEditor templateEditor = new RComboBoxEditor("Template");
    private RIntegerFieldEditor multiplierEditor = new RIntegerFieldEditor("Multiplier");
    private RButton templateButton = new RButton("Apply Template");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor approveDateEditor = new RDisplayLabelEditor("Approval Date");
    private RDisplayLabelEditor createUserEditor = new RDisplayLabelEditor("Create User");
    private RDisplayLabelEditor approveUserEditor = new RDisplayLabelEditor("Approval User");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
    private RComboBoxEditor defaultReasonEditor = new RComboBoxEditor("Reason");

    private StockItemTableEditor stockItemTableEditor = new StockItemTableEditor();
    private InventoryAdjustmentReasonTableEditor reasonTableEditor = new InventoryAdjustmentReasonTableEditor();

    private SimTable lineItemTable = new SimTable(new InventoryCreateTableDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private StockItemScannerDialog scannerDialog = null;

    private static final String APPLY_TEMPLATE = "Template.apply";
    private static final String COMMENTS_MODIFIED = "Comments.modified";
    private static final String REASON_MODIFIED = "Reason.modified";

    /****************************************************************************************************
     * Initialization & Layout
     ***************************************************************************************************/

    public InventoryAdjustmentDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        templateButton.registerAction(this, APPLY_TEMPLATE);

        multiplierEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_MULTIPLIER);
        multiplierEditor.setSizeType(EditorConstants.SMALL);
        multiplierEditor.setMinimumValue(1);
        multiplierEditor.setMaximumValue(999);
        multiplierEditor.setInteger(1);

        createDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        approveDateEditor.setDataType(DataTypeConstants.DATE_SHORT);

        commentsEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_COMMENT);
        commentsEditor.registerAction(this, COMMENTS_MODIFIED);

        defaultReasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        defaultReasonEditor.setSizeType(EditorConstants.LARGE);
        defaultReasonEditor.registerAction(this, REASON_MODIFIED);

        stockItemTableEditor.setSearchListener(buildItemSearchListener());

        lineItemTable.setColumnSize(InventoryAdjustmentProperty.CASE_SIZE, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(InventoryAdjustmentProperty.INVENTORY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(InventoryAdjustmentProperty.SERIAL_NUMBER_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
        }
    }

    private void layoutScreen() {
        REditorPanel datePanel = new REditorPanel(2, 1);
        datePanel.setTitleBorder("Date");
        datePanel.add(createDateEditor);
        datePanel.add(approveDateEditor);

        REditorPanel userPanel = new REditorPanel(2, 1);
        userPanel.setTitleBorder("User");
        userPanel.add(createUserEditor);
        userPanel.add(approveUserEditor);

        RPanel templatePanel = new RPanel(new GridBagLayout());
        templatePanel.setTitleBorder("Template");
        templatePanel.add(templateEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        templatePanel.add(multiplierEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        templatePanel.add(templateButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(adjustmentEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 5, 0, 3, 10));
        headerPanel.add(referenceEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 5, 0, 3, 10));
        headerPanel.add(statusEditor, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 3, 0, 0, 0, 10));
        headerPanel.add(templatePanel, GridTool.constraints(1, 0, 1, 3, 1, 0, 0, 3, 0, 0, 0, 00));
        headerPanel.add(datePanel, GridTool.constraints(2, 0, 1, 3, 0, 0, 0, 3, 0, 0, 0, 00));
        headerPanel.add(userPanel, GridTool.constraints(3, 0, 1, 3, 0, 0, 0, 3, 0, 0, 0, 00));

        REditorPanel bottomPanel = new REditorPanel(2);
        bottomPanel.add(commentsEditor);
        bottomPanel.add(defaultReasonEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(bottomPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(headerPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    /****************************************************************************************************
     * Navigation
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadInventoryAdjustment();
        loadAdjustmentReasons();
        populateScreen();
        validateScreenState();
        launchScanner();
    }

    public void stop() {
        shutdownScanner();
    }

    private void loadAdjustmentReasons() throws Exception {
        List<InventoryAdjustmentReason> reasons = model.getInventoryAdjustmentReasons();
        defaultReasonEditor.setItems(reasons);
        reasonTableEditor.setAdjustmentReasons(reasons);
    }

    private void populateScreen() throws Exception {
        InventoryAdjustment invAdjustment = model.getInventoryAdjustment();
        if (invAdjustment.isNew()) {
            adjustmentEditor.setData(Translator.getText("New"));
            templateEditor.setItems(model.getAllInventoryTemplates());
            multiplierEditor.setInteger(1);
        } else {
            adjustmentEditor.setData(invAdjustment.getId());
            templateEditor.setItems(model.getSingleInventoryTemplate());
            multiplierEditor.setInteger(invAdjustment.getTemplateModifier());
        }
        referenceEditor.setData(invAdjustment.getReferenceId());
        statusEditor.setData(invAdjustment.getStatus());
        templateEditor.setSelectedItem(model.findTemplate(invAdjustment.getTemplateId()));
        createDateEditor.setData(invAdjustment.getCreateDate());
        approveDateEditor.setData(invAdjustment.getApproveDate());
        createUserEditor.setData(invAdjustment.getCreateUser());
        approveUserEditor.setData(invAdjustment.getApproveUser());
        commentsEditor.setText(invAdjustment.getComments());

        lineItemTable.setRows(model.getLineItemWrappers());
    }

    private void validateScreenState() {
        boolean isEditable = model.isAdjustmentEditable();
        boolean isTemplateEditable = model.isTemplateEditable();
        commentsEditor.setEnabled(isEditable);
        templateEditor.setEnabled(isEditable && isTemplateEditable);
        multiplierEditor.setEnabled(isEditable && isTemplateEditable);
        templateButton.setEnabled(isEditable && isTemplateEditable);
        defaultReasonEditor.setEnabled(isEditable);
    }

    public boolean confirmInventoryAdjustmentLock() throws Exception {
        return model.confirmInventoryAdjustmentLock();
    }

    public boolean isAdjustmentEditable() {
        return model.isAdjustmentEditable();
    }

    public boolean isConfirmFunctionAvailable() {
        return model.isConfirmFunctionAvailable();
    }

    public boolean isCopyFunctionAvailable() {
        return model.isCopyFunctionAvailable();
    }

    public boolean isPrintFunctionAvailable() {
        return model.isPrintFunctionAvailable();
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
            InventoryAdjustmentLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
            if (lineItemWrapper != null) {
                model.updateExistingLineItem(lineItemWrapper, barcodeItem, null);
                return;
            }
            InventoryAdjustmentReason reason = (InventoryAdjustmentReason) defaultReasonEditor.getSelectedItem();
            if (reason == null) {
                throw new BusinessException(InventoryAdjustmentMessageText.MISSING_REASON);
            }
            lineItemWrapper = findEmptyWrapper();
            if (lineItemWrapper != null) {
                model.updateExistingLineItem(lineItemWrapper, barcodeItem, reason);
                return;
            }
            lineItemWrapper = model.createNewLineItemWrapper(reason);
            if (lineItemWrapper != null) {
                lineItemTable.addRow(lineItemWrapper);
                model.updateExistingLineItem(lineItemWrapper, barcodeItem, reason);
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    private InventoryAdjustmentLineItemWrapper findExistingWrapper(String itemId) {
        List<InventoryAdjustmentLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (InventoryAdjustmentLineItemWrapper wrapper : wrappers) {
            StockItem stockItem = wrapper.getStockItem();
            if (stockItem != null && itemId.equals(stockItem.getId())) {
                return wrapper;
            }
        }
        return null;
    }

    private InventoryAdjustmentLineItemWrapper findEmptyWrapper() {
        List<InventoryAdjustmentLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (InventoryAdjustmentLineItemWrapper wrapper : wrappers) {
            if (wrapper.getStockItem() == null) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Action Event Handler
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(APPLY_TEMPLATE)) {
                doApplyTemplate();
            } else if (command.equals(COMMENTS_MODIFIED)) {
                doCommentsModified();
            } else if (command.equals(REASON_MODIFIED)) {
                doReasonModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Private method to handle applying a template
     ***************************************************************************************************/

    private void doApplyTemplate() throws Exception {
        InventoryAdjustmentTemplateVO templateVO = (InventoryAdjustmentTemplateVO) templateEditor.getSelectedItem();
        if (templateVO == null) {
            return;
        }
        try {
            model.applyTemplate(templateVO, multiplierEditor.getIntegerValue());
            templateEditor.setEnabled(false);
            templateButton.setEnabled(false);
            multiplierEditor.setEnabled(false);
        } catch (Throwable exception) {
            displayException(exception);
        } finally {
            lineItemTable.setRows(model.getLineItemWrappers());
        }
    }

    /****************************************************************************************************
     * HANDLE COMMENTS MODIFIED
     ***************************************************************************************************/

    private void doCommentsModified() {
        try {
            model.getInventoryAdjustment().setComments(commentsEditor.getTextOrNull());
        } catch (BusinessException exception) {
            displayException(exception);
            assignFocusInScreen(commentsEditor);
        }
    }

    /****************************************************************************************************
     * HANDLE DEFAULT REASON MODIFIED
     ***************************************************************************************************/

    private void doReasonModified() {
        InventoryAdjustmentReason reason = (InventoryAdjustmentReason) defaultReasonEditor.getSelectedItem();
        if (reason != null) {
            List<InventoryAdjustmentLineItemWrapper> wrappers = lineItemTable.getAllRowData();
            for (InventoryAdjustmentLineItemWrapper wrapper : wrappers) {
                wrapper.setDefaultReason(reason);
            }
        }
    }

    /****************************************************************************************************
     * HANDLE ADD ITEM
     ***************************************************************************************************/

    protected void handleAddItem() {
        lineItemTable.stopEditing();

        if (validateLineItemWrappers()) {
            InventoryAdjustmentReason reason = (InventoryAdjustmentReason) defaultReasonEditor.getSelectedItem();
            lineItemTable.addRow(model.createNewLineItemWrapper(reason));
            lineItemTable.editCellInLastRow(InventoryAdjustmentProperty.STOCK_ITEM);
        }
    }

    /****************************************************************************************************
     * HANDLE DELETE ITEM
     ***************************************************************************************************/

    protected void handleDeleteItem() {
        lineItemTable.stopEditing();

        if (lineItemTable.getSelectedRowCount() < 1) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            try {
                List<InventoryAdjustmentLineItemWrapper> wrappers = lineItemTable.getAllSelectedRowData();
                for (InventoryAdjustmentLineItemWrapper wrapper : wrappers) {
                    model.removeLineItem(wrapper);

                    lineItemTable.removeRow(wrapper);
                }
            } catch (Throwable exception) {
                displayException(exception);
            }
        }
        // Validate Template Status
        boolean isTemplateEditable = model.isTemplateEditable();
        if (lineItemTable.getRowCount() < 1) {
            templateEditor.setEnabled(isTemplateEditable);
            multiplierEditor.setEnabled(isTemplateEditable);
            templateButton.setEnabled(isTemplateEditable);
        }
    }

    /****************************************************************************************************
     * HANDLE ADD CANCEL
     ***************************************************************************************************/

    protected void handleCancel() {
        if (model.isAdjustmentEditable()) {
            model.releaseInventoryAdjustmentLock();
        }
    }

    /****************************************************************************************************
     * HANDLE COPY
     ***************************************************************************************************/

    public boolean handleCopy() {
        lineItemTable.stopEditing();
        if (validateLineItemWrappers()) {
            try {
                model.copyInventoryAdjustment();
                model.releaseInventoryAdjustmentLock();
                model.clearState();
                return true;
            } catch (Exception exception) {
                displayException(exception);
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        lineItemTable.stopEditing();
        model.printAdjustmentReport();
    }

    /****************************************************************************************************
     * HANDLE SCANNER
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * HANDLE SAVE
     ***************************************************************************************************/

    public boolean handleSave() {
        lineItemTable.stopEditing();
        try {
            InventoryAdjustment inventoryAdjustment = model.getInventoryAdjustment();
            if (!inventoryAdjustment.isNew() && inventoryAdjustment.getLineItems().isEmpty()) {
                if (RConfirmUtility.confirmWithOkCancelType("Delete Adjustment Confirmation", InventoryAdjustmentMessageText.EMPTY_ADJUSTMENT_WARNING)) {
                    model.cancelInventoryAdjustment();
                    return true;
                }
                return false;
            }
            if (validateLineItemWrappers()) {
                model.saveInventoryAdjustment();
                return true;
            }
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * HANDLE CONFIRM
     ***************************************************************************************************/

    public boolean handleConfirm() {
        lineItemTable.stopEditing();
        if (validateLineItemWrappers() && validateLineItemQuantities()) {
            InventoryAdjustment inventoryAdjustment = model.getInventoryAdjustment();
            try {
                if (inventoryAdjustment.isCoherent()) {
                    boolean applyToStockCounts = false;
                    if (model.isStockCountValidationNeeded()) {
                        applyToStockCounts = RConfirmUtility.confirm("Confirmation", InventoryAdjustmentMessageText.OPEN_STOCK_COUNT_WARNING);
                    }
                    model.confirmInventoryAdjustment(applyToStockCounts);
                    return true;
                }
            } catch (Exception exception) {
                displayException(exception);
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Helper methods to perform validation
     ***************************************************************************************************/

    private boolean validateLineItemWrappers() {
        int totalRows = lineItemTable.getRowCount();
        for (int row = 0; row < totalRows; row++) {
            InventoryAdjustmentLineItemWrapper wrapper = (InventoryAdjustmentLineItemWrapper) lineItemTable.getRowData(row);
            StockItem stockItem = wrapper.getStockItem();
            InventoryAdjustmentReason reason = wrapper.getReason();
            Quantity quantity = wrapper.getQuantity();

            if (stockItem == null) {
                displayError(CommonMessageText.LINE_ITEM_NO_ITEM);
                lineItemTable.editCellInRow(InventoryAdjustmentProperty.STOCK_ITEM, row);
                return false;
            }
            if (reason == null) {
                displayError(InventoryAdjustmentMessageText.MISSING_REASON);
                lineItemTable.editCellInRow(InventoryAdjustmentProperty.REASON, row);
                return false;
            }
            if (stockItem.isSerialNumberRequired() && model.isSerialNumberProcessingEnabled()) {
                if (wrapper.getSerialNumbers().isEmpty()) {
                    if (stockItem.getUINType() == UINType.SERIAL) {
                        displayError(CommonMessageText.UIN_REQUIRED, stockItem.getId());
                        lineItemTable.setRowSelectionInterval(row, row);
                        return false;
                    }
                    if (stockItem.getUINType() == UINType.AGSN) {
                        if (wrapper.getDisposition() == InventoryDisposition.OUT_TO_AVAILABLE) {
                            if (quantity == null || !quantity.isPositive()) {
                                displayError(CommonMessageText.UIN_REQUIRED, stockItem.getId());
                                lineItemTable.editCellInRow(InventoryAdjustmentProperty.SERIAL_NUMBER_COUNT, row);
                                return false;
                            }
                        }
                    }
                }
            }
            if (quantity == null || !quantity.isPositive()) {
                displayError(InventoryAdjustmentMessageText.MISSING_QUANTITY);
                lineItemTable.editCellInRow(InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM, row);
                return false;
            }
        }
        return true;
    }

    private boolean validateLineItemQuantities() {
        int totalRows = lineItemTable.getRowCount();
        boolean isAvailableQtyConfirm = false;
        for (int row = 0; row < totalRows; row++) {
            InventoryAdjustmentLineItemWrapper wrapper = (InventoryAdjustmentLineItemWrapper) lineItemTable.getRowData(row);
            if(!wrapper.isValidatedQty() && !isAvailableQtyConfirm) {
                StockItem stockItem = wrapper.getStockItem();
                InventoryDisposition disposition = wrapper.getReason().getDisposition();
                Quantity quantity = wrapper.getQuantity();
                if (disposition == InventoryDisposition.AVAILABLE_TO_OUT || disposition == InventoryDisposition.AVAILABLE_SOH_TO_UNAVAILABLE_SOH) {
                    if (stockItem.isQtyGreaterThanAvailableStockOnHand(quantity)) {
                        if (!RConfirmUtility.confirm("Quantity Confirmation", InventoryAdjustmentMessageText.AVAILABLE_QTY_CONFIRM)) {
                            lineItemTable.editCellInRow(InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM, row);
                            return false;
                        }
                    }
                }
                if (disposition == InventoryDisposition.UNAVAILABLE_TO_OUT || disposition == InventoryDisposition.UNAVAILABLE_SOH_TO_AVAILABLE_SOH) {
                    if (stockItem.isQtyGreaterThanNonSellable(quantity)) {
                        UIStatusUtility.displayWarning(this, InventoryAdjustmentMessageText.UNAVAILABLE_QTY_ERROR);
                        lineItemTable.editCellInRow(InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM, row);
                        return false;
                    }
                }            	
                wrapper.setValidatedQty(true);
                isAvailableQtyConfirm = true;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                stockItemTableEditor.setData(stockItem);
            }
        };
    }

    /****************************************************************************************************
     * Inventory Create Table Definition
     ***************************************************************************************************/

    private class InventoryCreateTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return InventoryAdjustmentLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(InventoryAdjustmentProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(9);
            attributes.add(new SimTableAttribute("Item", InventoryAdjustmentProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemTableEditor));
            attributes.add(new SimTableAttribute("Item Description", InventoryAdjustmentProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("Reason", InventoryAdjustmentProperty.REASON, new TranslatedObjectDisplayer(), reasonTableEditor));
            attributes.add(new SimTableAttribute("Disposition", InventoryAdjustmentProperty.DISPOSITION, new InventoryDispositionDisplayer()));
            if (model.isNonSellableTypesActive()) {
                attributes.add(new SimTableAttribute("Sub-bucket", InventoryAdjustmentProperty.NON_SELLABLE_TYPE_DESC));
            }
            attributes.add(new SimTableAttribute("UOM", InventoryAdjustmentProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", InventoryAdjustmentProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Inventory", InventoryAdjustmentProperty.INVENTORY_BASED_ON_UOM, new LineItemInventoryDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes
                        .add(new SimTableAttribute("UIN Qty", InventoryAdjustmentProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {
        public void popupDialog(Object lineItem) {
            InventoryAdjustmentLineItemWrapper wrapper = (InventoryAdjustmentLineItemWrapper) lineItem;
            if (wrapper.isSerialNumberRequired()) {
                InventoryAdjustmentUinDialog dialog = new InventoryAdjustmentUinDialog();
                dialog.setLineItemWrapper((InventoryAdjustmentLineItemWrapper) lineItem);
                dialog.setVisible(true);

                lineItemTable.refreshTable();
            }
        }
    }
}
