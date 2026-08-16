package oracle.retail.sim.client.screen.fulfillmentorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;

/********************************************************************************************************
 * Fulfillment Order List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4294622538633506232L;

    private static final String FULFILLMENT_ORDER_FILTER_SELECTED = "FulfillmentOrder.filterSelected";
    private static final String FULFILLMENT_ORDER_SELECTED = "FulfillmentOrder.selected";

    private FulfillmentOrderListModel model = new FulfillmentOrderListModel();

    private FulfillmentOrderFilterDialog filterDialog = new FulfillmentOrderFilterDialog();

    private SimTable fulfillmentOrderTable = new SimTable(new FulfillmentOrderListDefinition());
    private SimTablePane fulfillmentOrderPane = new SimTablePane(fulfillmentOrderTable);
    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public FulfillmentOrderListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, FULFILLMENT_ORDER_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        fulfillmentOrderTable.setTableEditable(false);
        fulfillmentOrderTable.setSingleRowSelectionMode();
        fulfillmentOrderTable.registerDoubleClickAction(this, FULFILLMENT_ORDER_SELECTED);
        fulfillmentOrderTable.setColumnSize(FulfillmentOrderProperty.LINE_ITEMS_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);

    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(fulfillmentOrderPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return fulfillmentOrderTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() {
        try {
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        fulfillmentOrderTable.setRows(model.findFulfillmentOrderVOs());
    }

    /****************************************************************************************************
     * Handle Delivery
     ***************************************************************************************************/
    public void handleDelivery() throws Exception {
        if (fulfillmentOrderTable.getSelectedRowCount() == 0) {
            displayWarning(FulfillmentOrderMessageText.NO_DELIVERY_SELECTED);
            return;
        }
        FulfillmentOrderVO orderVO = (FulfillmentOrderVO) fulfillmentOrderTable.getSelectedRowData();
        model.storeFulfillmentOrder(orderVO);
        navigate(SimScreenName.FULFILLMENT_ORDER_DELIVERY_LIST_SCREEN);
    }

    /****************************************************************************************************
     * Handle Reverse Pick
     ***************************************************************************************************/
    public void handleReversePick() throws Exception {
        if (fulfillmentOrderTable.getSelectedRowCount() == 0) {
            displayWarning(FulfillmentOrderMessageText.NO_DELIVERY_SELECTED_REVERSE_PICKING);
            return;
        }
        FulfillmentOrderVO orderVO = (FulfillmentOrderVO) fulfillmentOrderTable.getSelectedRowData();
        if (!model.isWebOrder(orderVO)) {
            displayWarning(FulfillmentOrderMessageText.INVALID_DELIVERY_TYPE_REVERSE_PICK);
            return;
        }
        model.storeFulfillmentOrder(orderVO);
        navigate(SimScreenName.FULFILLMENT_ORDER_REVERSE_PICK_LIST_SCREEN);
    }

    public void handlePrint() throws Exception {
        List<FulfillmentOrderVO> orderVOs = fulfillmentOrderTable.getAllSelectedRowData();
        if (orderVOs.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printFulfillmentOrders(orderVOs);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FULFILLMENT_ORDER_SELECTED)) {
                doFulfillmentOrderSelected();
            } else if (command.equals(FULFILLMENT_ORDER_FILTER_SELECTED)) {
                doFulfillmentOrderFilterSelected();
            } else if (command.equals(SimClientStateKey.FULFILLMENT_ORDER_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doFulfillmentOrderSelected() throws Exception {
        FulfillmentOrderVO orderVO = (FulfillmentOrderVO) fulfillmentOrderTable.getSelectedRowData();
        if (orderVO != null) {
            model.storeFulfillmentOrder(orderVO);
            navigate(SimScreenName.FULFILLMENT_ORDER_DETAIL_SCREEN);
        }
    }

    private void doFulfillmentOrderFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Fulfillment Order List Table Definition
     ***************************************************************************************************/

    private class FulfillmentOrderListDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return FulfillmentOrderVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderProperty.RELEASE_DATE, true));
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderProperty.CUSTOMER_ORDER_ID, true));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("SIM Customer Order", FulfillmentOrderProperty.ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Customer Order", FulfillmentOrderProperty.CUSTOMER_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Fulfillment Order", FulfillmentOrderProperty.EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Type", FulfillmentOrderProperty.ORDER_TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Status", FulfillmentOrderProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", FulfillmentOrderProperty.LINE_ITEMS_COUNT));
            attributes.add(new SimTableAttribute("Create Date", FulfillmentOrderProperty.CREATE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Release Date", FulfillmentOrderProperty.RELEASE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Comments", FulfillmentOrderProperty.COMMENTS));
            return attributes;
        }
    }
}
