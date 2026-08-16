package extra.retail.sim.client.screen.returns;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.screen.returns.ReturnFilterDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
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
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnProperty;
import oracle.retail.sim.common.stockreturn.ReturnVO;

import extra.retail.sim.client.core.ExtraSimScreenName;

/********************************************************************************************************
 * Return List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraReturnListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 5236222840437506012L;

    private ExtraReturnListModel model = new ExtraReturnListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable returnTable = new SimTable(new ReturnTableDefinition());
    private SimTablePane returnPane = new SimTablePane(returnTable);

    private ReturnFilterDialog filterDialog = new ReturnFilterDialog();

    private static final String RETURN_FILTER_SELECTED = "Return.filterSelected";
    private static final String RETURN_SELECTED = "Return.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ExtraReturnListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, RETURN_FILTER_SELECTED);
        filterDialog.addREventListener(this);

        returnTable.setTableEditable(false);
        returnTable.registerDoubleClickAction(this, RETURN_SELECTED);

        returnTable.setColumnSize(ReturnProperty.AUTHORIZATION_CODE, EditorConstants.COLUMN_LABEL_WIDTH);
        returnTable.setColumnSize(ReturnProperty.NOT_AFTER_DATE, EditorConstants.COLUMN_LABEL_WIDTH);
        returnTable.setColumnSize(ReturnProperty.LINE_ITEM_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(returnPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return returnTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isCancelReturnNotAvailable() {
        return model.isDeleteNotAvailable();
    }

    public boolean isDispatchNotAvailable() {
        return model.isDispatchNotAvailable();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (returnTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        model.printReturns(returnTable.getAllSelectedRowData());
    }

    /****************************************************************************************************
     * Delete
     ***************************************************************************************************/

    public void handleCancelReturn() throws Exception {
        if (returnTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        List<ReturnVO> returnVOs = returnTable.getAllSelectedRowData();
        if (RConfirmUtility.confirm("Delete Confirmation", ReturnMessageText.DELETE_CONFIRM)) {
            try {
                model.deleteReturns(returnVOs);
            } catch (Throwable exception) {
                displayException(exception);
            } finally {
                populateScreen();
            }
        }
    }

    /****************************************************************************************************
     * Dispatch
     ***************************************************************************************************/

    public void handleDispatch() throws Exception {
        if (returnTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<ReturnVO> returnVOs = returnTable.getAllSelectedRowData();
        for (ReturnVO returnVO : returnVOs) {
        	if (!model.validateShipTrailer(returnVO)) {
    			displayError(CommonMessageText.SHIP_TRAILER_REQUIRED);
    			return;
    		}
        }
        if (RConfirmUtility.confirm("Dispatch Confirmation", ReturnMessageText.DISPATCH_CONFIRM)) {
            try {
                model.dispatchReturns(returnVOs);
            } catch (Throwable exception) {
                displayException(exception);
            } finally {
                populateScreen();
            }
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(RETURN_SELECTED)) {
                doReturnSelected();
            } else if (command.equals(RETURN_FILTER_SELECTED)) {
                doReturnFilterSelected();
            } else if (command.equals(SimClientStateKey.RETURN_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doReturnSelected() throws Exception {
        model.storeReturn((ReturnVO) returnTable.getSelectedRowData());
        navigate(ExtraSimScreenName.RETURN_DETAIL_SCREEN);
    }

    private void doReturnFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        returnTable.setRows(model.findReturnVOs());
    }

    /****************************************************************************************************
     * Return Table Definition
     ***************************************************************************************************/

    public class ReturnTableDefinition extends SimTableDefinition {

        private TranslatedObjectDisplayer statusDisplayer = new TranslatedObjectDisplayer();

        public Class getDataClass() {
            return ReturnVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("Return", ReturnProperty.ID));
            attributes.add(new SimTableAttribute("External Id", ReturnProperty.EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Authorization", ReturnProperty.AUTHORIZATION_CODE));
            attributes.add(new SimTableAttribute("Destination", ReturnProperty.DESTINATION_NAME));
            attributes.add(new SimTableAttribute("Date", ReturnProperty.STATUS_DATE));
            attributes.add(new SimTableAttribute("Not After Date", ReturnProperty.NOT_AFTER_DATE));
            attributes.add(new SimTableAttribute("Status", ReturnProperty.STATUS, statusDisplayer));
            attributes.add(new SimTableAttribute("Total Lines", ReturnProperty.LINE_ITEM_COUNT));
            attributes.add(new SimTableAttribute("User", ReturnProperty.EMPLOYEE_ID));
            return attributes;
        }
    }
}
