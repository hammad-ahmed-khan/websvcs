package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Fulfillment Order Pick List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickListPanel extends ScreenPanel implements REventListener {

    private static final long serialVersionUID = 2657086824690493967L;

    private FulfillmentOrderPickListModel model = new FulfillmentOrderPickListModel();

    private static final String CUST_ORDER_PICK_FILTER_SELECTED = "CustomerOrderPick.filterSelected";
    private static final String CUST_ORDER_PICK_SELECTED = "CustomerOrderPick.selected";

    private FulfillmentOrderPickFilterDialog filterDialog = new FulfillmentOrderPickFilterDialog();

    private SimTable pickTable = new SimTable(new CustomerOrderPickListDefinition());
    private SimTablePane pickPane = new SimTablePane(pickTable);
    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/
    public FulfillmentOrderPickListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, CUST_ORDER_PICK_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        pickTable.setTableEditable(false);
        pickTable.setMultipleRowSelectionMode();
        pickTable.registerDoubleClickAction(this, CUST_ORDER_PICK_SELECTED);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(pickPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return pickTable;
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
        pickTable.setRows(model.findFulfillmentOrderPickVOs());
        pickTable.setTableEditable(false);
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CUST_ORDER_PICK_SELECTED)) {
                doPickSelected(event);
            } else if (command.equals(CUST_ORDER_PICK_FILTER_SELECTED)) {
                doPickFilterSelected();
            } else if (command.equals(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doPickSelected(RActionEvent event) throws Exception {
        FulfillmentOrderPickWrapper wrapper = (FulfillmentOrderPickWrapper) pickTable.getSelectedRowData();
        if (wrapper != null) {
            model.storePick(wrapper);
            navigate(SimScreenName.FULFILLMENT_ORDER_PICK_DETAIL_SCREEN);
        }

    }

    private void doPickFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    public boolean isCreateFunctionAvailable() {
        return model.isCreateFunctionAvailable();
    }

    public boolean isCancelFunctionAvailable() {
        return model.isDeleteFunctionAvailable();
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/
    public void handleCreatePick() {

    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/
    public void handleCancelPick() {
        pickTable.stopEditing();

        if (pickTable.getSelectedRowCount() < 1) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }

        if (RConfirmUtility.confirm("Pick Delete Confirmation", FulfillmentOrderMessageText.PICK_DELETE_CONFIRM)) {
            try {
                List<FulfillmentOrderPickWrapper> wrappers = pickTable.getAllSelectedRowData();
                for (FulfillmentOrderPickWrapper wrapper : wrappers) {
                    model.cancelPick(wrapper);
                }
                populateScreen();
            } catch (Throwable exception) {
                displayException(exception);
            }
        }
    }

    /****************************************************************************************************
     * Handle Refresh
     ***************************************************************************************************/
    public void handleRefresh() {
        try {
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/
    public void handlePrint() throws Exception {
        List<FulfillmentOrderPickWrapper> wrappers = pickTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printPicks(wrappers);
    }

    /****************************************************************************************************
     * Customer Order List Table Definition
     ***************************************************************************************************/

    private class CustomerOrderPickListDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderPickWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.CREATE_DATE, false));
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderPickProperty.PICK_ID, true));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("ID", FulfillmentOrderPickProperty.PICK_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Type", FulfillmentOrderPickProperty.TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", FulfillmentOrderPickProperty.CREATE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Status", FulfillmentOrderPickProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Pick Qty", FulfillmentOrderPickProperty.TOTAL_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Actual Qty", FulfillmentOrderPickProperty.ACTUAL_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("User", FulfillmentOrderPickProperty.USER));
            return attributes;
        }
    }

}
