package oracle.retail.sim.client.screen.uda;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
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
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAType;

/********************************************************************************************************
 * UDA Print Setup Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UDAPrintSetupPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1905778946359900249L;

    private UDAPrintSetupModel model = new UDAPrintSetupModel();

    private RComboBoxEditor udaTypeEditor = new RComboBoxEditor("Type");

    private SimTable udaTable = new SimTable(new ReportFormatDefinition());
    private SimTablePane udaTablePane = new SimTablePane(udaTable);

    private static String UDA_TYPE_SELECTED = "UDAType.selected";

    public UDAPrintSetupPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        udaTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        udaTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        udaTypeEditor.setSizeType(EditorConstants.LARGE);
        udaTypeEditor.registerAction(this, UDA_TYPE_SELECTED);

        udaTable.setColumnSize("id", 100);

        udaTable.setColumnRenderer("printTicket", new SimTableCheckBoxRenderer(true));
        udaTable.setColumnEditor("printTicket", new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
        udaTable.setColumnSize("printTicket", EditorConstants.COLUMN_LABEL_WIDTH);

        udaTable.setColumnRenderer("printLabel", new SimTableCheckBoxRenderer(true));
        udaTable.setColumnEditor("printLabel", new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
        udaTable.setColumnSize("printLabel", EditorConstants.COLUMN_LABEL_WIDTH);

        udaTable.setTableEditable(true);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(udaTypeEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 10, 0));
        mainPanel.add(udaTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return udaTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/
    public void start() throws Exception {
        udaTypeEditor.setItems(UDAType.values());
        refreshTable();
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/
    public void handleSave() throws Exception {
        model.updateUDADetails(udaTable.getAllRowData());
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() {
        ClientDataCacheUtility.clearCache();
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(UDA_TYPE_SELECTED)) {
                doUDATypeSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doUDATypeSelected() throws Exception {
        udaTable.stopEditing();
        refreshTable();
    }

    private void refreshTable() throws Exception {
        UDAType udaType = (UDAType) udaTypeEditor.getSelectedItem();
        udaTable.setRows(model.findUDADetails(udaType));
    }

    /****************************************************************************************************
     * UDA PRINT SETUP TABLE DEFINITION
     ***************************************************************************************************/
    private class ReportFormatDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return UDADetail.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("description"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("ID", "id", false));
            attributes.add(new SimTableAttribute("UDA", "description", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Type", "type", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Ticket", "printTicket"));
            attributes.add(new SimTableAttribute("Label", "printLabel"));
            return attributes;
        }
    }
}
