package oracle.retail.sim.client.screen.reportformat;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.DisplayerTableCellEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanCheckBoxTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.tableeditor.RetailPrinterTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.report.ReportingServices;

/********************************************************************************************************
 * This class is a popup window is a dialog window containing a message, list of selections, and
 * okay/cancel buttons. The user selections a choice and presses "OK" or cancels the window if he doesn't
 * wish to proceed.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPrinterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -8258874050283277342L;

    private RetailPrinterTableEditor printerEditor = new RetailPrinterTableEditor(false);

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";

    private RButton cancelButton = new RButton(CANCEL);
    private RButton okButton = new RButton(OK);

    private RDisplayLabelEditor labelEditor = new RDisplayLabelEditor("Please select a report");

    private SimTable formatTable = new SimTable(new ReportFormatDefinition());
    private SimTablePane formatPane = new SimTablePane(formatTable);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public RPrinterDialog(RFrame frame) {
        super(frame);
        setTitle("Report Selection");
        setSize(700, 300);
        setStatusBarVisible(false);
        buildDialog();
        layoutDialog();
        centerOnOwner();
    }

    private void buildDialog() {
        okButton.registerAction(this, OK);
        cancelButton.registerAction(this, CANCEL);
        formatTable.setTableEditable(true);
        formatTable.setColumnRenderer("selected", new SimTableCheckBoxRenderer(true));
        formatTable.setColumnSize("selected", EditorConstants.COLUMN_LABEL_WIDTH);
        formatTable.setColumnEditor("selected", new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
    }

    private void layoutDialog() {
        addButton(okButton);
        addButton(cancelButton);
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(labelEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 570));
        mainPanel.add(formatPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 5, 3, 0, 5, 0, 5));
        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Init Dialog
     ***************************************************************************************************/

    public void setPrinters(List<StorePrinter> printers) {
        printerEditor.setItems(printers);
    }

    public void setFormatOptions(List<ReportTypeFormat> formats) throws Exception {
        List<RPrinterDialogWrapper> wrappers = new ArrayList<>();
        ReportingServices reportingServices = ClientServiceFactory.getReportingServices();
        for (ReportTypeFormat format : formats) {
            List<StorePrinter> printers = reportingServices.findPrinters(SimRepository.getStoreId());
            wrappers.add(ClientWrapperFactory.createRPrinterDialogWrapper(format, printers));
        }
        formatTable.setRows(wrappers);
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CANCEL)) {
                handleCancel();
            } else if (command.equals(OK)) {
                handleOk();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/
    private void handleCancel() {
        formatTable.stopEditing();
        formatTable.clearRows();
        super.closeWindow();
    }
    
    /****************************************************************************************************
     * Handle Close
     ***************************************************************************************************/
    public void closeWindow() {
    	handleCancel();
    }

    /****************************************************************************************************
     * Handle OK
     ***************************************************************************************************/
    private void handleOk() throws Exception {
        List<RPrinterDialogWrapper> wrappers = formatTable.getAllRowData();
        boolean isRowSelected = false;
        for (RPrinterDialogWrapper wrapper : wrappers) {
            if (wrapper.isSelected()) {
                isRowSelected = true;
                if (wrapper.getDefaultPrinter() == null) {
                    throw new BusinessException(ReportMessageText.PRINT_SELECT_FOR_FORMAT, wrapper.getFormat());
                }
            }
        }
        if (!isRowSelected) {
            throw new BusinessException(ReportMessageText.REPORT_FORMAT_NOT_SELECTED);
        }
        super.closeWindow();
    }

    /****************************************************************************************************
     * Return Selected Reports
     ***************************************************************************************************/

    public List<RetailStoreFormatPrinter> getSelectedOption() {
        List<RetailStoreFormatPrinter> formatPrinters = new ArrayList<>();
        List<RPrinterDialogWrapper> wrappers = formatTable.getAllRowData();
        for (RPrinterDialogWrapper wrapper : wrappers) {
            if (wrapper.isSelected()) {
                RetailStoreFormatPrinter formatPrinter = new RetailStoreFormatPrinter();
                formatPrinter.setFormat(wrapper.getFormat());
                formatPrinter.setReportURL(wrapper.getPrintURL());
                formatPrinter.setPrinter(wrapper.getDefaultPrinter());
                formatPrinter.setReportType(wrapper.getReportType());
                formatPrinters.add(formatPrinter);
            }
        }
        return formatPrinters;
    }

    /****************************************************************************************************
     * Label, Ticket and Reports Format Table Definition
     ***************************************************************************************************/
    private class ReportFormatDefinition extends SimTableDefinition {
        private AttributeDisplayer printerDisplayer = new AttributeDisplayer("description", "-Select-");

        public Class getDataClass() {
            return RPrinterDialogWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("selected", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute("-", "selected"));
            attributes.add(new SimTableAttribute("Format", "format"));
            attributes.add(new SimTableAttribute("Printer", "defaultPrinter", printerDisplayer, printerEditor));
            return attributes;
        }
    }
}
