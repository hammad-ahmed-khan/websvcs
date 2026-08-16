package oracle.retail.sim.client.screen.warehousedelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryMessageText;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/********************************************************************************************************
 * Warehouse Delivery Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 5932514392507267676L;

    private static final String CARTON_SELECTED = "Carton.selected";
    private static final String COMMENT_MODIFIED = "Comments.modified";

    private WarehouseDeliveryDetailModel model = new WarehouseDeliveryDetailModel();

    private RDisplayLabelEditor sourceEditor = new RDisplayLabelEditor("From");
    private RDisplayLabelEditor asnIdEditor = new RDisplayLabelEditor("ASN ID");
    private RDisplayLabelEditor etaEditor = new RDisplayLabelEditor("ETA");
    private RDisplayLabelEditor receiveDateEditor = new RDisplayLabelEditor("Receive Date");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
    private RDisplayLabelEditor contextTypeEditor = new RDisplayLabelEditor("Context Type");
    private RDisplayLabelEditor contextValueEditor = new RDisplayLabelEditor("Context Value");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private SimTable cartonTable = new SimTable(new WarehouseDeliveryCartonDefinition());

    public WarehouseDeliveryDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        sourceEditor.setDisplayer(new IdNameDisplayer());
        etaEditor.setDataType(DataTypeConstants.DATE_SHORT);
        receiveDateEditor.setDisplayer(new DateTimeDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        contextTypeEditor.setDisplayer(new AttributeDisplayer(WarehouseDeliveryProperty.NAME));
        commentsEditor.setIdentifier(SimName.WAREHOUSE_DELIVERY_COMMENT);
        commentsEditor.registerAction(this, COMMENT_MODIFIED);
        if (model.isSerialNumberProcessingEnabled()) {
            cartonTable.setColumnSize(WarehouseDeliveryProperty.SERIAL_NUMBER_REQUIRED, SimTable.LABEL_WIDTH);
        }
        cartonTable.setTableEditable(false);
        cartonTable.registerDoubleClickAction(this, CARTON_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(3, 3);
        headerPanel.add(sourceEditor);
        headerPanel.add(asnIdEditor);
        headerPanel.add(contextTypeEditor);
        headerPanel.add(etaEditor);
        headerPanel.add(receiveDateEditor);
        headerPanel.add(contextValueEditor);
        headerPanel.add(statusEditor);
        headerPanel.add(userEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);
        SimTablePane cartonPane = new SimTablePane(cartonTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel,    GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(commentsEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 2, 5, 0));
        mainPanel.add(divider,        GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(cartonPane,     GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return cartonTable;
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

    public boolean isDeliveryAdjustable() {
        return model.isDeliveryAdjustable();
    }

    public boolean isAllowAdjustment() {
        return model.isAllowAdjustment();
    }

    public boolean isFulfillmentOrderRelated() {
        return model.isFulfillmentOrderRelated();
    }

    /****************************************************************************************************
     * Start Methods
     ***************************************************************************************************/

    public void start() {
        model.loadState();
        try {
            model.loadFulfillmentOrders();
        } catch (Exception e) {
            displayException(new BusinessException(WarehouseDeliveryMessageText.FULFILLMENT_ORDER_UNAVAILABLE, e));
        }
        populateScreen();
    }

    public boolean resume() {
        if (model.isLockBroken()) {
            navigateLater(SimNavigation.BACK);
            return false;
        }
        if (model.isCanceled()) {
            model.revertDelivery();
        }
        if (!model.isViewOnlyMode() && !model.isDeliveryClosed() && !checkLock()) {
            return false;
        }
        populateScreen();
        return true;
    }

    public void stop() {
        model.clearState();
    }

    /****************************************************************************************************
     * Adjust Delivery
     ***************************************************************************************************/

    public boolean handleAdjustDelivery() throws Exception {
        if (!RConfirmUtility.confirm("Re-Open The Delivery", WarehouseDeliveryMessageText.CONFIRM_REOPEN) || !model.obtainLock()) {
            return false;
        }
        model.adjustDelivery();
        populateScreen();
        return true;
    }

    /****************************************************************************************************
     * Handle Fulfillment Order
     ***************************************************************************************************/

    public boolean handleFulfillmentOrders() {
        try {
            List<FulfillmentOrderVO> fulfillmentOrderVOs = model.getFulfillmentOrderVOs();
            if (fulfillmentOrderVOs.isEmpty()) {
                throw new BusinessException(WarehouseDeliveryMessageText.FULFILLMENT_ORDER_UNAVAILABLE);
            }
            List<WarehouseDeliveryCartonWrapper> cartons = cartonTable.getAllSelectedRowData();
            if (cartons.isEmpty()) {
                cartons = cartonTable.getAllRowData();
            }
            model.storeCartonWrappers(cartons);
            model.storeFulfillmentOrderVOs(fulfillmentOrderVOs);
            return true;
        } catch (Throwable t) {
            displayException(t);
            return false;
        }
    }

    /****************************************************************************************************
     * Handle Receive
     ***************************************************************************************************/

    public void handleReceive() throws Exception {
        if (!checkLock()) {
            return;
        }
        if (cartonTable.getSelectedRowCount() > 0) {
            List<WarehouseDeliveryCartonWrapper> cartons = cartonTable.getAllSelectedRowData();
            if (!validateUnreceivedUINItems(cartons)) {
                return;
            }
            model.receiveCartons(cartons);
            cartonTable.refreshTable();
        } else if (RConfirmUtility.confirm("Receive All Confirmation", WarehouseDeliveryMessageText.RECEIVE_ALL_CARTONS)) {
            if (!validateUnreceivedUINItems(cartonTable.getAllRowData())) {
                return;
            }
            model.receiveAll();
            cartonTable.refreshTable();
        }
    }

    /****************************************************************************************************
     * Handle Unreceive
     ***************************************************************************************************/

    public void handleUnreceive() throws Exception {
        if (!checkLock()) {
            return;
        }
        if (cartonTable.getSelectedRowCount() > 0) {
            model.unreceiveCartons(cartonTable.getAllSelectedRowData());
            cartonTable.refreshTable();
        } else if (RConfirmUtility.confirm("Un-Receive All Confirmation", WarehouseDeliveryMessageText.UNRECEIVE_ALL_CARTONS)) {
            model.unreceiveAll();
            cartonTable.refreshTable();
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() {
        try {
            if (!checkLock()) {
                return false;
            }
            model.updateDelivery();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Confirm
     ***************************************************************************************************/

    public boolean handleConfirm() {
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
            if (!RConfirmUtility.confirm("Confirmation", WarehouseDeliveryMessageText.RECEIVE_CONFIRM)) {
                return false;
            }
            if (!validateCartonsReceived()) {
                return false;
            }
            if (model.isSerialNumberProcessingEnabled()) {
                if (!validateUnreceivedUINItems(cartonTable.getAllRowData())) {
                    return false;
                }
            }
            if (model.isFulfillmentOrderRelated()) {
                displayMessage(WarehouseDeliveryMessageText.FULFILLMENT_ORDER_RELATED);
            }
            model.receiveDelivery();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    private boolean validateCartonsReceived() {
        List<WarehouseDeliveryCartonWrapper> cartons = cartonTable.getAllRowData();
        for (WarehouseDeliveryCartonWrapper carton : cartons) {
            if (carton.getStatus() == WarehouseDeliveryStatus.IN_PROGRESS) {
                return RConfirmUtility.confirm("Confirmation", WarehouseDeliveryMessageText.CARTONS_NOT_RECEIVED);
            }
        }
        return true;
    }

    private boolean validateUnreceivedUINItems(List<WarehouseDeliveryCartonWrapper> cartons) {
        boolean isFinisherDelivery = model.isFinisherDelivery();
        List<WarehouseDeliveryCartonWrapper> cartonsWithUnreceivedUins = new ArrayList<WarehouseDeliveryCartonWrapper>();
        for (WarehouseDeliveryCartonWrapper carton : cartons) {
            if (carton.getStatus() != WarehouseDeliveryStatus.IN_PROGRESS) {
                continue;
            }
            for (WarehouseDeliveryLineItem lineItem : carton.getCarton().getLineItems()) {
                if (lineItem.getQuantityReceived() != null) {
                    continue;
                }
                StockItem stockItem = lineItem.getStockItem();
                if (stockItem.isSerialNumberRequired() && (!stockItem.isAgsnEnabled() || isFinisherDelivery)) {
                    cartonsWithUnreceivedUins.add(carton);
                    break;
                }
            }
        }
        if (cartonsWithUnreceivedUins.isEmpty()) {
            return true;
        }
        StringBuilder cartonIdsWithUnreceivedUins = new StringBuilder();
        for (WarehouseDeliveryCartonWrapper carton : cartonsWithUnreceivedUins) {
            if (cartonIdsWithUnreceivedUins.length() > 0) {
                cartonIdsWithUnreceivedUins.append(", ");
            }
            cartonIdsWithUnreceivedUins.append(carton.getExternalId());
        }
        return RConfirmUtility.confirm("Confirmation", WarehouseDeliveryMessageText.UIN_RECEIVE_WITH_CONFIRMATION, cartonIdsWithUnreceivedUins.toString());
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() {
        if (!model.isViewOnlyMode() && !model.isDeliveryClosed()) {
            try {
                model.releaseLock(model.getDelivery().getId());
            } catch (Throwable t) {
                displayException(t);
            }
        }
        model.storeCanceled();
    }

    /****************************************************************************************************
     * Activity Lock Methods
     ***************************************************************************************************/

    private boolean checkLock() {
        boolean locked = false;
        try {
            locked = model.checkLock();
        } catch (Throwable t) {
            displayException(t);
        }
        if (!locked) {
            displayException(new BusinessException(CommonMessageText.LOCK_TAKEN_OVER));
            navigate(SimNavigation.BACK);
        }
        return locked;
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/
    private void populateScreen() {
        boolean notViewOnly = !model.isViewOnlyMode();
        boolean deliveryClosed = model.isDeliveryClosed();
        WarehouseDelivery delivery = model.getDelivery();
        sourceEditor.setData(delivery.getSource());
        asnIdEditor.setData(delivery.getAsnId());
        etaEditor.setData(delivery.getExpectedArrivalDate());
        Date completeDate = delivery.getCompleteDate();
        if (completeDate == null && notViewOnly && !deliveryClosed) {
            completeDate = SimDateUtil.getCurrentDate();
        }
        receiveDateEditor.setData(completeDate);
        statusEditor.setData(delivery.getStatus());
        userEditor.setData(delivery.getUserId());
        contextTypeEditor.setData(delivery.getContextType());
        contextValueEditor.setData(delivery.getContextValue());
        commentsEditor.setText(delivery.getComments());
        cartonTable.setRows(model.getCartonWrappers());

        commentsEditor.setEnabled(notViewOnly);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CARTON_SELECTED)) {
                handleCartonSelected();
            } else if (command.equals(COMMENT_MODIFIED)) {
                handleCommentsModified();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void handleCartonSelected() throws Exception {
        if (!model.isDeliveryClosed() && !model.isViewOnlyMode() && !checkLock()) {
            return;
        }
        WarehouseDeliveryCartonWrapper carton = (WarehouseDeliveryCartonWrapper) cartonTable.getSelectedRowData();
        if (cartonTable.getSelectedRowCount() != 1 || carton == null) {
            throw new BusinessException(CommonMessageText.NO_SINGLE_ROW_SELECTED);
        }
        model.storeCarton(carton.getCarton());
        navigate(SimScreenName.WAREHOUSE_DELIVERY_CARTON_DETAIL_SCREEN);
    }

    private void handleCommentsModified() {
        try {
            WarehouseDelivery delivery = model.getDelivery();
            String value = commentsEditor.getTextOrNull();
            if (!StringHelper.equalsTrim(delivery.getComments(), value)) {
                delivery.setComments(value);
            }
        } catch (BusinessException e) {
            displayException(e);
            assignFocusInScreen(commentsEditor);
        }
    }

    /****************************************************************************************************
     * Receive Carton Table
     ***************************************************************************************************/

    private class WarehouseDeliveryCartonDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return WarehouseDeliveryCartonWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(WarehouseDeliveryProperty.EXTERNAL_ID));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Container ID", WarehouseDeliveryProperty.EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Status", WarehouseDeliveryProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Expected Cases", WarehouseDeliveryProperty.NUMBER_OF_CASES_EXPECTED));
            attributes.add(new SimTableAttribute("Customer Order", WarehouseDeliveryProperty.FULFILLMENT_ORDER_INDICATOR, new TranslatedObjectDisplayer()));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Required", WarehouseDeliveryProperty.SERIAL_NUMBER_REQUIRED, new SimTableCheckBoxRenderer()));
            }
            return attributes;
        }
    }
}
