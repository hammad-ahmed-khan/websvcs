package oracle.retail.sim.client.screen.warehousedelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryMessageText;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;

/********************************************************************************************************
 * Warehouse Delivery List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 8464548211232472665L;

    private static final String DELIVERY_SELECTED = "Delivery.selected";
    private static final String WAREHOUSE_DELIVERY_FILTER_SELECTED = "WarehouseDelivery.filterSelected";

    private WarehouseDeliveryListModel model = new WarehouseDeliveryListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable deliveryTable = new SimTable(new DeliveryListDefinition());
    private SimTablePane deliveryPane = new SimTablePane(deliveryTable);

    private WarehouseDeliveryFilterDialog filterDialog = new WarehouseDeliveryFilterDialog();

    public WarehouseDeliveryListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, WAREHOUSE_DELIVERY_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        deliveryTable.setTableEditable(false);
        deliveryTable.registerDoubleClickAction(this, DELIVERY_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1);
        topPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(deliveryPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return deliveryTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    public void resume() throws Exception {
        if (model.isCanceled()) {
            return;
        }
        populateScreen();
    }

    public void stop() {
        model.clearState();
    }

    /****************************************************************************************************
     * Handle Refresh
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        clearScreen();
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        List<WarehouseDeliveryVO> deliveryVOs = deliveryTable.getAllSelectedRowData();
        if (deliveryVOs.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printDeliveries(deliveryVOs);
    }

    /****************************************************************************************************
     * Handle Fulfillment Order
     ***************************************************************************************************/

    public boolean handleFulfillmentOrders() throws Exception {
        try {
            model.clearSelectedState();
            WarehouseDeliveryVO deliveryVO = (WarehouseDeliveryVO) deliveryTable.getSelectedRowData();
            if (deliveryTable.getSelectedRowCount() != 1 || deliveryVO == null) {
                throw new BusinessException(CommonMessageText.NO_SINGLE_ROW_SELECTED);
            }
            if (!deliveryVO.isFulfillmentOrderRelated()) {
                throw new BusinessException(WarehouseDeliveryMessageText.FULFILLMENT_ORDER_UNAVAILABLE);
            }
            WarehouseDelivery delivery = model.getDelivery(deliveryVO.getId());
            if (delivery == null) {
                throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
            }
            List<FulfillmentOrderVO> fulfillmentOrderVOs = model.getFulfillmentOrderVOs(delivery.getId());
            if (fulfillmentOrderVOs.isEmpty()) {
                throw new BusinessException(WarehouseDeliveryMessageText.FULFILLMENT_ORDER_UNAVAILABLE);
            }

            List<WarehouseDeliveryCartonWrapper> cartons = model.getCartonWrappers(delivery);
            model.storeDelivery(delivery);
            model.storeCartonWrappers(cartons);
            model.storeFulfillmentOrderVOs(fulfillmentOrderVOs);

            return true;
        } catch (Throwable t) {
            displayException(t);
            return false;
        }
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void clearScreen() {
        deliveryTable.clearRows();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DELIVERY_SELECTED)) {
                doDeliverySelected();
            } else if (command.equals(WAREHOUSE_DELIVERY_FILTER_SELECTED)) {
                doDeliveryFilterSelected();
            } else if (command.equals(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doDeliverySelected() throws Exception {
        model.clearSelectedState();
        WarehouseDeliveryVO deliveryVO = (WarehouseDeliveryVO) deliveryTable.getSelectedRowData();
        if (deliveryTable.getSelectedRowCount() != 1 || deliveryVO == null) {
            throw new BusinessException(CommonMessageText.NO_SINGLE_ROW_SELECTED);
        }
        WarehouseDelivery delivery = model.getDelivery(deliveryVO.getId());
        if (!model.isDeliveryEditAllowed() || !model.isDeliveryClosed(delivery) && !model.obtainLock(delivery)) {
            model.storeViewOnly();
        } else {
            model.markNewInProgress(delivery);
        }
        model.storeDelivery(delivery);
        navigate(SimScreenName.WAREHOUSE_DELIVERY_DETAIL_SCREEN);
    }

    private void doDeliveryFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        deliveryTable.setRows(model.getDeliveries());
    }

    /****************************************************************************************************
     * Delivery List Table Definition
     ***************************************************************************************************/

    private class DeliveryListDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return WarehouseDeliveryVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(WarehouseDeliveryProperty.ASN_ID));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("ID", WarehouseDeliveryProperty.ID));
            attributes.add(new SimTableAttribute("ASN", WarehouseDeliveryProperty.ASN_ID));
            attributes.add(new SimTableAttribute("Status", WarehouseDeliveryProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("ETA", WarehouseDeliveryProperty.EXPECTED_ARRIVAL_DATE));
            attributes.add(new SimTableAttribute("Receive Date", WarehouseDeliveryProperty.COMPLETE_DATE));
            attributes.add(new SimTableAttribute("From", WarehouseDeliveryProperty.SOURCE, new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Containers", WarehouseDeliveryProperty.NUMBER_OF_CARTONS));
            attributes.add(new SimTableAttribute("Missing", WarehouseDeliveryProperty.MISSING_CARTONS));
            attributes.add(new SimTableAttribute("Customer Order", WarehouseDeliveryProperty.FULFILLMENT_ORDER_RELATED, new BooleanDisplayer()));
            return attributes;
        }
    }
}
