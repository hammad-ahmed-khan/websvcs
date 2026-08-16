package extra.retail.sim.client.screen.reportformat;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.reportformat.ReportFormatWrapper;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.DisplayerTableCellEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanCheckBoxTableEditor;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.tableeditor.ReportTypeTableEditor;
import oracle.retail.sim.client.tableeditor.RetailPrinterTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.reportformat.ReportTypeProperty;

import extra.retail.sim.common.report.ExtraReportFormat;

/********************************************************************************************************
 * Report Format Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraReportFormatPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 1036041336736306982L;

    private static final String REPORT_TYPE_SELECTED = "ReportType.selected";

    private ExtraReportFormatModel model = new ExtraReportFormatModel();

    private RetailPrinterTableEditor printerEditor = new RetailPrinterTableEditor(true);
    private ReportTypeTableEditor reportTypeRowEditor = new ReportTypeTableEditor();
    private RComboBoxEditor filterEditor = new RComboBoxEditor("Formats to Display");

    private SimTable formatTable = new SimTable(new ReportFormatDefinition());
    private SimTablePane formatPane = new SimTablePane(formatTable);

    public ExtraReportFormatPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.setDisplayer(new TranslatedObjectDisplayer());
        filterEditor.setEmptyType(RComboBoxEmptyType.ALL);
        filterEditor.setSizeType(EditorConstants.LARGE);
        filterEditor.registerAction(this, REPORT_TYPE_SELECTED);

        formatTable.setColumnRenderer(ReportTypeProperty.DEFAULT_FORMAT, new SimTableCheckBoxRenderer(true));
        formatTable.setColumnSize(ReportTypeProperty.DEFAULT_FORMAT, EditorConstants.COLUMN_LABEL_WIDTH);
        formatTable.setColumnEditor(ReportTypeProperty.DEFAULT_FORMAT, new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
        formatTable.setTableEditable(true);

        reportTypeRowEditor.setDisplayer(new TranslatedObjectDisplayer());
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 10, 0));
        mainPanel.add(formatPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return formatTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/
    public void start() throws Exception {
        filterEditor.setItems(ExtraReportFormat.values());
        reportTypeRowEditor.setItems(ExtraReportFormat.values());
        printerEditor.setItems(model.findPrinters());
        formatTable.setRows(model.findAllReportFormats());
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/
    public void handleCreate() {
        formatTable.stopEditing();
        if (tableContainsBlankRow()) {
            return;
        }
        formatTable.addRow(model.buildNewFormatWrapper());
        formatTable.editCellInLastRow(ReportTypeProperty.FORMAT);
    }

    private void validateNoDuplicateFormat() throws Exception {
        List<ReportFormatWrapper> wrappers = model.getAllWrappers();
        for (ReportFormatWrapper wrapper : wrappers) {
            for (ReportFormatWrapper compareWrapper : wrappers) {
                if (wrapper != compareWrapper && wrapper.getReportType().equals(compareWrapper.getReportType())) {
                    if (StringHelper.equalsTrim(wrapper.getFormat(), compareWrapper.getFormat())) {
                        throw new BusinessException(ReportMessageText.REPORT_DUPLICATE_FORMAT_ERROR);
                    }
                    if (wrapper.isDefaultFormat() && compareWrapper.isDefaultFormat()) {
                        throw new BusinessException(ReportMessageText.REPORT_FORMAT_SINGLE_SELECT, compareWrapper.getReportType());
                    }
                }
            }
        }
    }

    private void validateAllFormatsAvailable() throws Exception {
        List<ReportFormatWrapper> wrappers = model.getAllWrappers();
        Set<String> reportTypes = new HashSet<>();
        Set<String> reportTypesUsed = new HashSet<>();
        for (ReportFormatWrapper wrapper : wrappers) {
            reportTypes.add(wrapper.getReportType());
            if (wrapper.isDefaultFormat() || wrapper.isReportTypeManifest() || wrapper.isReportTypePreShipment()) {
                reportTypesUsed.add(wrapper.getReportType());
            }
        }
        reportTypes.removeAll(reportTypesUsed);
        if (!reportTypes.isEmpty()) {
            throw new BusinessException(ReportMessageText.REPORT_FORMAT_NO_DEFAULT, reportTypes.iterator().next());
        }
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/
    public void handleDelete() {
        if (formatTable.getSelectedRowCount() == 0) {
            displayException(new BusinessException(CommonMessageText.NO_ROWS_SELECTED));
            return;
        }
        RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Delete Confirmation");
        dialog.setMessage(ReportMessageText.REPORT_DELETE_FORMAT_CONFIRM);
        dialog.setYesNoType();
        if (!dialog.getConfirmation()) {
            return;
        }
        List<ReportFormatWrapper> wrappers = formatTable.getAllSelectedRowData();
        for (ReportFormatWrapper wrapper : wrappers) {
            model.deleteReportType(wrapper);
            formatTable.removeRow(wrapper);
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/
    public void handleSave() throws Exception {
        validateFormatAndTemplate();
        validateNoDuplicateFormat();
        validateAllFormatsAvailable();
        model.updateReportTypeFormats();
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(REPORT_TYPE_SELECTED)) {
                doFormatTypeSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doFormatTypeSelected() {
        formatTable.stopEditing();
        ExtraReportFormat reportFormat = (ExtraReportFormat) filterEditor.getSelectedItem();
        formatTable.setRows(model.filterReportFormats(reportFormat));
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean tableContainsBlankRow() {
        for (ReportFormatWrapper wrapper : model.getAllWrappers()) {
            if (StringUtility.isNullOrEmpty(wrapper.getFormat())) {
                return true;
            }
        }
        return false;
    }

    private void validateFormatAndTemplate() throws BusinessException {
        for (ReportFormatWrapper wrapper : model.getAllWrappers()) {
            if (StringUtility.isNullOrEmpty(wrapper.getFormat())) {
                throw new BusinessException(ReportMessageText.REPORT_FORMAT_REQUIRED);
            }
        }
    }

    /****************************************************************************************************
     * Label, Ticket and Reports Format Table Definition
     ***************************************************************************************************/
    private class ReportFormatDefinition extends SimTableDefinition {
        public Class<ReportFormatWrapper> getDataClass() {
            return ReportFormatWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(ReportTypeProperty.REPORT_TYPE));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Format Name", ReportTypeProperty.FORMAT, new StringTableEditor(SimName.REPORT_FORMAT)));
            attributes.add(new SimTableAttribute("Type", ReportTypeProperty.REPORT_TYPE, new TranslatedObjectDisplayer(), reportTypeRowEditor));
            attributes.add(new SimTableAttribute("Default", ReportTypeProperty.DEFAULT_FORMAT));
            attributes.add(new SimTableAttribute("Default Printer", ReportTypeProperty.DEFAULT_PRINTER, new AttributeDisplayer("description"), printerEditor));
            attributes.add(new SimTableAttribute("URL Location", ReportTypeProperty.TEMPLATE_URL, new StringTableEditor(SimName.REPORT_FORMAT_TEMPLATE_URL)));
            return attributes;
        }
    }
}
