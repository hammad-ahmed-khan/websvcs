package oracle.retail.sim.client.screen.transfer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.TransferVOStatusDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
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
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferVO;

/********************************************************************************************************
 * Transfer List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 5062765455142508476L;

    private TransferListModel model = new TransferListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable transferTable = new SimTable(new TransferListDefinition());
    private SimTablePane transferPane = new SimTablePane(transferTable);

    private TransferFilterDialog filterDialog = new TransferFilterDialog();

    private static final String TRANSFER_FILTER_SELECTED = "Transfer.filterSelected";
    private static final String TRANSFER_SELECTED = "Transfer.selected";

    /********************************************************************************************************
     * Build Panel
     *******************************************************************************************************/

    public TransferListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, TRANSFER_FILTER_SELECTED);
        filterDialog.addREventListener(this);

        transferTable.setColumnVisible("Request Approver", false);
        transferTable.setColumnVisible("Receiving User", false);
        transferTable.setTableEditable(false);
        transferTable.registerDoubleClickAction(this, TRANSFER_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1, 1);
        topPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(transferPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return transferTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (transferTable.getSelectedRowCount() == 0) {
            displayError(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return;
        }
        List<ReportRequest> requests = model.getPrintRequests(transferTable.getAllSelectedRowData());
        List<ReportFormat> formats = new ArrayList<>();
        formats.add(ReportFormat.TRANSFER);
        formats.add(ReportFormat.TRANSFER_BOL);
        List<RetailStoreFormatPrinter> printers = SimClientPrintUtility.selectFormatPrinter(model.getStoreId(), formats);
        if (printers == null || printers.isEmpty()) {
            return;
        }
        SimClientPrintUtility.printReportRequests(requests, printers, TransferMessageText.REPORT_PRINTED);
    }

    /****************************************************************************************************
     * Handle Dispatch
     ***************************************************************************************************/

    public void handleDispatch() throws Exception {
        // No Transfers Selected
        if (transferTable.getSelectedRowCount() == 0) {
            displayError(TransferMessageText.NO_ROWS_SELECTED_DISPATCH);
            return;
        }

        // Sort Out Transfers
        List<TransferVO> allTransferVOs = transferTable.getAllSelectedRowData();
        List<TransferVO> validTransferVOs = new ArrayList<>();
        List<TransferVO> invalidTransferVOS = new ArrayList<>();
        for (TransferVO transferVO : allTransferVOs) {
            if (model.isValidForDispatch(transferVO.getStatus(), transferVO.getSendingStoreId())
            		   && transferVO.getSendingStoreId().equals(model.getStoreId()) ) {
                validTransferVOs.add(transferVO);
            } else {
                invalidTransferVOS.add(transferVO);
            }
        }

        // Invalid Transfers Found
        if (invalidTransferVOS.size() > 0) {
            displayWarning(model.getDispatchWarning(invalidTransferVOS.get(0).getSendingStoreId()));
        }

        // No Valid Transfers Found
        if (validTransferVOs.isEmpty()) {
            return;
        }

        // Dispatch Transfers And Display Appropriate Response
        if (RConfirmUtility.confirm("Dispatch Confirmation", TransferMessageText.DISPATCH_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.dispatchTransfers(validTransferVOs);
                displayMessage(TransferMessageText.DISPATCH_COMPLETED);
            } catch (Throwable exception) {
                displayException(exception);
            } finally {
                showScreenBusy(false);
                populateScreen();
            }
        }
    }

    /****************************************************************************************************
     * Handle Cancel Transfer
     ***************************************************************************************************/

    public void handleCancelTransfer() throws Exception {
        if (transferTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", TransferMessageText.DELETE_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.cancelTransfers(transferTable.getAllSelectedRowData());
            } catch (BusinessException exception) {
                displayException(exception);
            } finally {
                showScreenBusy(false);
            }
            populateScreen();
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TRANSFER_SELECTED)) {
                doTransferSelected();
            } else if (command.equals(TRANSFER_FILTER_SELECTED)) {
                doTransferFilterSelected();
            } else if (command.equals(SimClientStateKey.TRANSFER_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doTransferSelected() throws Exception {
        TransferVO transferVO = (TransferVO) transferTable.getSelectedRowData();
        String screenName = model.storeTransfer(transferVO);
        if (screenName != null) {
            navigate(screenName);
        }
    }

    private void doTransferFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        transferTable.setRows(model.findTransfers());
    }

    /****************************************************************************************************
     * TRANSFER LIST TABLE DEFINITION
     ***************************************************************************************************/

    public class TransferListDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TransferVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("statusDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(12);
            attributes.add(new SimTableAttribute("ID", "id"));
            attributes.add(new SimTableAttribute("External Id", "externalId"));
            attributes.add(new SimTableAttribute("From", "sendingStore"));
            attributes.add(new SimTableAttribute("To", "receivingStore"));
            attributes.add(new SimTableAttribute("Date", "statusDate"));
            attributes.add(new SimTableAttribute("Type", "phase", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TransferVOStatusDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", "numberOfLineItems"));
            attributes.add(new SimTableAttribute("Create User", "createUserId"));
            attributes.add(new SimTableAttribute("Request Approver", "approveUserId"));
            attributes.add(new SimTableAttribute("Receiving User", "receiveUserId"));
            attributes.add(new SimTableAttribute("Customer Order", TransferProperty.FULFILLMENT_ORDER_RELATED, new BooleanDisplayer()));

            return attributes;
        }
    }
}