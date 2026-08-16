package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.notes.NotesDialog;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItem;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickProperty;
import oracle.retail.sim.common.item.BarcodeItem;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickDetailPanel extends ScreenPanel implements ItemScannerListener {

    private static final long serialVersionUID = -2879183474441165834L;

    private FulfillmentOrderReversePickDetailModel model = new FulfillmentOrderReversePickDetailModel();

    private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
    private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
    private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
    private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");
    private RDisplayLabelEditor reservationTypeEditor = new RDisplayLabelEditor("Reservation Type");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Order Create Date");
    private RDisplayLabelEditor releaseDateEditor = new RDisplayLabelEditor("Order Release Date");
    private RDisplayLabelEditor deliveryDateEditor = new RDisplayLabelEditor("Order Delivery Date");
    private RDisplayLabelEditor reversePickIdEditor = new RDisplayLabelEditor("Reverse Pick ID");
    private RDisplayLabelEditor reversePickStatusEditor = new RDisplayLabelEditor("Reverse Pick Status");
    private RDisplayLabelEditor reversePickCreateDateEditor = new RDisplayLabelEditor("Reverse Pick Create Date");
    private RDisplayLabelEditor reversePickCreateUserEditor = new RDisplayLabelEditor("Reverse Pick Create User");
    private RDisplayLabelEditor deliveryTypeEditor = new RDisplayLabelEditor("Delivery Type");
    private RDisplayLabelEditor carrierEditor = new RDisplayLabelEditor("Carrier");
    private RDisplayLabelEditor serviceEditor = new RDisplayLabelEditor("Service");
    private RDisplayLabelEditor partialDeliveryEditor = new RDisplayLabelEditor("Allow Partial Delivery");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private StockItemScannerDialog scannerDialog;

    private SimTable lineItemTable = new SimTable(new CustomerOrderReversePickItemsDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    public FulfillmentOrderReversePickDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        reversePickIdEditor.setDisplayer(new IdNameDisplayer());
        simFulfilOrderIdEditor.setDisplayer(new IdNameDisplayer());
        customerOrderIdEditor.setDisplayer(new IdNameDisplayer());
        fulfillmentOrderIdEditor.setDisplayer(new IdNameDisplayer());
        reversePickStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        orderStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        reservationTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        commentsEditor.setEnabled(true, false);
        reversePickCreateDateEditor.setDisplayer(new DateTimeDisplayer());
        reversePickCreateUserEditor.setDisplayer(new TranslatedObjectDisplayer());
        createDateEditor.setDisplayer(new DateTimeDisplayer());
        releaseDateEditor.setDisplayer(new DateTimeDisplayer());
        deliveryDateEditor.setDisplayer(new DateTimeDisplayer());
        deliveryTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        carrierEditor.setDisplayer(new TranslatedObjectDisplayer());
        serviceEditor.setDisplayer(new TranslatedObjectDisplayer());
        partialDeliveryEditor.setDisplayer(new BooleanDisplayer());
        lineItemTable.setSingleRowSelectionMode();
        lineItemTable.setColumnSize(FulfillmentOrderProperty.REMAINING_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(FulfillmentOrderProperty.PICKED_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(FulfillmentOrderReversePickProperty.QUANTITY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        REditorPanel detailPanel = new REditorPanel(5, 4);
        detailPanel.add(customerOrderIdEditor);
        detailPanel.add(fulfillmentOrderIdEditor);
        detailPanel.add(simFulfilOrderIdEditor);
        detailPanel.add(orderStatusEditor);
        detailPanel.add(reservationTypeEditor);
        detailPanel.add(createDateEditor);
        detailPanel.add(releaseDateEditor);
        detailPanel.add(deliveryDateEditor);
        detailPanel.skip();
        detailPanel.skip();
        detailPanel.add(reversePickIdEditor);
        detailPanel.add(reversePickStatusEditor);
        detailPanel.add(reversePickCreateDateEditor);
        detailPanel.add(reversePickCreateUserEditor);
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
        model.loadFulfillmentOrder();
        model.loadReversePick();
        populateScreen();
        launchScanner();
    }

    private void populateScreen() throws Exception {
        FulfillmentOrder fulfillmentOrder = model.getFulfillmentOrder();
        FulfillmentOrderReversePick reversePick = model.getReversePick();
        reversePickIdEditor.setData(reversePick.getIdAsString());
        reversePickCreateDateEditor.setData(reversePick.getCreateDate());
        reversePickCreateUserEditor.setData(reversePick.getCreateUser());
        reversePickStatusEditor.setData(reversePick.getStatus());
        simFulfilOrderIdEditor.setData(fulfillmentOrder.getId());
        customerOrderIdEditor.setData(fulfillmentOrder.getCustomerOrderId());
        fulfillmentOrderIdEditor.setData(fulfillmentOrder.getExternalId());
        orderStatusEditor.setData(fulfillmentOrder.getStatus());
        reservationTypeEditor.setData(fulfillmentOrder.getOrderType());
        commentsEditor.setText(fulfillmentOrder.getComments());
        createDateEditor.setData(fulfillmentOrder.getCreateDate());
        releaseDateEditor.setData(fulfillmentOrder.getReleaseDate());
        deliveryDateEditor.setData(fulfillmentOrder.getDeliveryDate());
        deliveryTypeEditor.setData(fulfillmentOrder.getDeliveryType());
        carrierEditor.setData(fulfillmentOrder.getDeliveryCarrier());
        serviceEditor.setData(fulfillmentOrder.getDeliveryService());
        partialDeliveryEditor.setData(fulfillmentOrder.isAllowPartialDelivery());
        lineItemTable.setRows(model.getReversePickItems());
        lineItemTable.setEnabled(model.isReversePickEditAllowed());
    }

    public boolean isReversePickClosed() {
        return model.isReversePickClosed();
    }

    public boolean confirmActivityLock() throws Exception {
        return model.checkLock();
    }

    public boolean isReversePickEditAllowed() throws Exception {
        return model.isReversePickEditAllowed();
    }

    public boolean isConfirmFunctionAvailable() {
        return model.isConfirmFunctionAvailable();
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
            List<FulfillmentOrderReversePickLineItemWrapper> existingWrappers = lineItemTable.getAllRowData();
            List<FulfillmentOrderReversePickLineItemWrapper> foundWrappers = new ArrayList<>();
            for (FulfillmentOrderReversePickLineItemWrapper wrapper : existingWrappers) {
                if (barcodeItem.getId().equals(wrapper.getStockItem().getId())) {
                    foundWrappers.add(wrapper);
                }
            }
            if (foundWrappers.isEmpty()) {
                throw new UIException(CommonMessageText.LINE_ITEM_NOT_FOUND, RErrorSeverity.WARNING);
            }
            if (foundWrappers.size() == 1) {
                model.updateExistingLineItem(foundWrappers.get(0), barcodeItem);
            }
            if (foundWrappers.size() > 1) {
                FulfillmentOrderReversePickLineItemDialog dialog = new FulfillmentOrderReversePickLineItemDialog();
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
     * Handle Done
     ***************************************************************************************************/

    public boolean handleDone() {
        lineItemTable.stopEditing();
        boolean isQuantityMissing = false;
        for (FulfillmentOrderReversePickLineItem lineItem : model.getReversePick().getLineItems()) {
            if (lineItem.getQuantity() == null) {
                isQuantityMissing = true;
            }
        }
        if (isQuantityMissing) {
            if (!RConfirmUtility.confirm("Reverse Pick Confirm", CommonMessageText.DEFAULT_TO_ZERO_CONFIRM)) {
                return false;
            }
            model.setAllQuantitiesToZero();
        }
        try {
            model.saveReversePick();
            model.releaseLock();
            return true;
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Confirm
     ***************************************************************************************************/

    public boolean handleConfirm() throws Exception {
        lineItemTable.stopEditing();
        try {
            Long reversePickId = model.getReversePick().getId();
            boolean isQuantityMissing = false;
            boolean allQtysAreZero = true;
            for (FulfillmentOrderReversePickLineItem lineItem : model.getReversePick().getLineItems()) {
                if (lineItem.getQuantityOrZero().isPositive()) {
                    allQtysAreZero = false;
                }
                if (lineItem.getQuantity() == null) {
                    isQuantityMissing = true;
                }
            }
            if (allQtysAreZero) {
                if (!RConfirmUtility.confirm("Reverse Pick Confirm", FulfillmentOrderMessageText.ALL_QUANTITIES_ZERO_CANCEL_REVERSE_PICK)) {
                    return false;
                }
                if (reversePickId != null) {
                    try {
                        model.cancelReversePick(reversePickId);
                    } catch (Exception e) {
                        displayException(e);
                    } finally {
                        model.releaseLock(reversePickId.toString());
                    }
                }
                return true;
            }
            if (isQuantityMissing) {
                if (!RConfirmUtility.confirm("Reverse Pick Confirm", CommonMessageText.DEFAULT_TO_ZERO_CONFIRM)) {
                    return false;
                }
                model.setAllQuantitiesToZero();
            }
            if (RConfirmUtility.confirm("Reverse Pick Confirm", FulfillmentOrderMessageText.CONFIRM_REVERSE_PICK_CONFIRM)) {
                model.confirmReversePick();
                model.releaseLock();
                return true;
            }
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Default Quantities
     ***************************************************************************************************/

    public void handleDefaultQuantities() throws Exception {
        lineItemTable.stopEditing();
        List<FulfillmentOrderReversePickLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        model.setDefaultQuantities(wrappers);
        lineItemTable.refreshTable();
    }

    /****************************************************************************************************
     * Handle Notes
     ***************************************************************************************************/

    public void handleNotes() {
        try {
            NotesDialog dialog = new NotesDialog();
            dialog.setTitle("Customer Order Notes");
            dialog.loadNotes(FunctionalArea.CUSTOMER_ORDER, model.getFulfillmentOrder().getId(), model.isNotesEditable());
            dialog.setVisible(true);
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle SCANNER
     ***************************************************************************************************/

    public void handleScanner() throws Exception {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() throws Exception {
        model.releaseLock();
    }

    /****************************************************************************************************
     * Line Item Table Definition
     ***************************************************************************************************/

    private class CustomerOrderReversePickItemsDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return FulfillmentOrderReversePickLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(13);
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderProperty.ITEM_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Description", FulfillmentOrderProperty.ITEM_DESCRIPTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("UOM", FulfillmentOrderProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", FulfillmentOrderProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Remaining Qty", FulfillmentOrderProperty.REMAINING_QTY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Order Qty", FulfillmentOrderProperty.ORDERED_QTY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Picked Qty", FulfillmentOrderProperty.PICKED_QTY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Delivered Qty", FulfillmentOrderProperty.DELIVERED_QTY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Canceled Qty", FulfillmentOrderProperty.CANCELED_QTY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Sugg. Reverse Qty", FulfillmentOrderReversePickProperty.SUGGESTED_QTY, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Quantity", FulfillmentOrderReversePickProperty.QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Substitute", FulfillmentOrderProperty.SUBSTITUTE_ID));
            return attributes;
        }
    }
}
