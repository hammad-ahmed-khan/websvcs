package oracle.retail.sim.client.screen.itemrequest;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
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
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.itemrequest.ItemRequestVO;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Item Request List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -5241652540350059796L;

    private ItemRequestListModel model = new ItemRequestListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable requestTable = new SimTable(new ItemRequestDefinition());
    private SimTablePane requestPane = new SimTablePane(requestTable);

    private ItemRequestFilterDialog filterDialog = new ItemRequestFilterDialog();

    private static final String ITEM_REQUEST_FILTER_SELECTED = "ItemRequest.filterSelected";

    public ItemRequestListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, ITEM_REQUEST_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        requestTable.setTableEditable(false);
        requestTable.registerDoubleClickAction(this, SimClientStateKey.ITEM_REQUEST_DETAIL);
    }

    private void layoutPanel() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(requestPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/

    public void stop() {
        if (RepositoryManager.getStateObject(SimClientStateKey.ITEM_REQUEST_FILTER) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.ITEM_REQUEST_FILTER);
        }
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (requestTable.getSelectedRowCount() == 0) {
            displayError(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return;
        }
        model.printItemRequests(requestTable.getAllSelectedRowData());
    }

    /****************************************************************************************************
     * Handle Cancel Request
     ***************************************************************************************************/

    public void handleCancelRequest() throws Exception {
        if (requestTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", ItemRequestMessageText.DELETE_CONFIRM)) {
            try {
                model.cancelRows(requestTable.getAllSelectedRowData());
            } catch (BusinessException exception) {
                displayException(exception);
            }
            populateScreen();
        }
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.ITEM_REQUEST_DETAIL)) {
                doItemRequestSelected();
            } else if (command.equals(ITEM_REQUEST_FILTER_SELECTED)) {
                doItemRequestFilterSelected();
            } else if (command.equals(SimClientStateKey.ITEM_REQUEST_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doItemRequestSelected() throws Exception {
        ItemRequestVO itemRequest = (ItemRequestVO) requestTable.getSelectedRowData();
        model.storeItemRequest(itemRequest.getId());
        navigate(SimScreenName.ITEM_REQUEST_DETAIL_SCREEN);
    }

    private void doItemRequestFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        requestTable.setRows(model.findItemRequestVOs());
    }

    /****************************************************************************************************
     * Item Request Table Definition
     ***************************************************************************************************/

    private class ItemRequestDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemRequestVO.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Request ID", "id"));
            attributes.add(new SimTableAttribute("Request Delivery Date", "requiredDeliveryDate"));
            attributes.add(new SimTableAttribute("Dept", "departmentId"));
            attributes.add(new SimTableAttribute("Request Description", "scheduleDescription"));
            attributes.add(new SimTableAttribute("Exp Date", "expirationDate"));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Lines", "numberOfLineItems"));
            attributes.add(new SimTableAttribute("User", "employeeId"));
            return attributes;
        }
    }
}
