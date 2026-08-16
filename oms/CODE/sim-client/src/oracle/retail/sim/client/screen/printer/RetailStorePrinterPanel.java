package oracle.retail.sim.client.screen.printer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.RetailStorePrinterTypeDisplayer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.RetailStorePrinterTypeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Printers Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterPanel extends ScreenPanel implements REventListener, SimTableEditorListener {
    private static final long serialVersionUID = 1036041336736306982L;

    private RetailStorePrinterModel model = new RetailStorePrinterModel();

    private SimTable printerTable = new SimTable(new RetailStorePrinterTableDefinition());
    private SimTablePane printerPane = new SimTablePane(printerTable);

    public RetailStorePrinterPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        printerTable.setTableEditable(true);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(printerPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return printerTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/
    public void start() throws Exception {
        printerTable.setRows(model.findPrinters());
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/
    public void handleCreate() throws Exception {
        printerTable.stopEditing();
        if (tableContainsBlankRow()) {
            return;
        }
        printerTable.addRow(model.buildNewRetailStorePrinterWrapper());
        printerTable.editCellInLastRow("uri");
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/
    public void handleDelete() throws Exception {
        if (printerTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }

        RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Delete Confirmation");
        dialog.setMessage(ReportMessageText.PRINTER_DELETE_CONFIRM);
        dialog.setYesNoType();

        if (dialog.getConfirmation()) {
            List<RetailStorePrinterWrapper> wrappers = printerTable.getAllSelectedRowData();
            for (RetailStorePrinterWrapper wrapper : wrappers) {
                if (model.deletePrinter(wrapper)) {
                    printerTable.removeRow(wrapper);
                } else {
                    displayException(new BusinessException(ReportMessageText.PRINTER_DEFAULT_CANNOT_DELETE, wrapper.getRetailStorePrinter().getDescription()));
                }
            }
        }
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/
    public void handleSave() throws Exception {
        validatePrinters();
        model.updatePrinters(printerTable.getAllRowData());
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {

    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean tableContainsBlankRow() {
        List<RetailStorePrinterWrapper> printers = printerTable.getAllRowData();
        for (RetailStorePrinterWrapper printer : printers) {
            if (StringUtility.isNullOrEmpty(printer.getDescription())) {
                return true;
            }
        }
        return false;
    }

    private void validatePrinters() throws BusinessException {
        List<RetailStorePrinterWrapper> printers = printerTable.getAllRowData();
        for (RetailStorePrinterWrapper printer : printers) {
            if (StringUtility.isNullOrEmpty(printer.getDescription())) {
                throw new BusinessException(ReportMessageText.PRINTER_DESCRIPTION_REQUIRED);
            }
            if (StringUtility.isNullOrEmpty(printer.getUri())) {
                throw new BusinessException(ReportMessageText.PRINTER_NETWORK_ADDRESS_REQUIRED);
            }
            if (printer.getRetailStorePrinter().getType() == 0) {
                throw new BusinessException(ReportMessageText.PRINTER_TYPE_REQUIRED);
            }
        }
    }

    /****************************************************************************************************
     * Item Location Table Definition
     ***************************************************************************************************/

    public void performTableEditorEvent(SimTableEditorEvent event) {
    }

    /****************************************************************************************************
     * Label, Ticket and Reports Format Table Definition
     ***************************************************************************************************/
    private class RetailStorePrinterTableDefinition extends SimTableDefinition {
        private StringTableEditor descEditor = new StringTableEditor(SimName.PRINTER_DESCRIPTION);
        private StringTableEditor uriEditor = new StringTableEditor(SimName.PRINTER_NETWORK_ADDRESS);
        private RetailStorePrinterTypeTableEditor printerTypeRowEditor = new RetailStorePrinterTypeTableEditor();

        public Class<?> getDataClass() {
            return RetailStorePrinterWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("description"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Printer Description", "description", descEditor));
            attributes.add(new SimTableAttribute("Type", "type", new RetailStorePrinterTypeDisplayer(), printerTypeRowEditor));
            attributes.add(new SimTableAttribute("Network Address", "uri", uriEditor));
            return attributes;
        }
    }
}
