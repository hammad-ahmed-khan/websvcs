package oracle.retail.sim.client.swing.tableconfig;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTab;

/********************************************************************************************************
 * This class is the tab that handles the configuration of the columns in the table. Settings that can be
 * altered are which columns are visible, the ordering of the columns and which columns should be sorted.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RTableConfigColumnTab extends RTab implements REventListener {
    private static final long serialVersionUID = -2253743536346346498L;

    private String identifier;
    private SimTable columnTable = new SimTable(new ColumnConfigTableDefinition());
    private SimTablePane columnPane = new SimTablePane(columnTable);

    private static final String HIDE = "Hide";
    private static final String SORT = "Sort";
    private static final String NO_SORT = "No Sort";
    private static final String VISIBLE = "Visible";
    private static final String MOVEUP = "Move Up";
    private static final String MOVEDOWN = "Move Down";
    private static final String ROW_SELECTED = "Row.selected";

    private RButton hideButton = new RButton(HIDE);
    private RButton sortButton = new RButton(SORT);
    private RButton moveUpButton = new RButton(MOVEUP);
    private RButton moveDownButton = new RButton(MOVEDOWN);

    /****************************************************************************************************
     * Constructs a new column configuration tab.
     * <p>
     * @param title The title to assign to the tab.
     ***************************************************************************************************/
    public RTableConfigColumnTab(String title) {
        setTitle(title);
        initializeTab();
        layoutTab();
    }

    /****************************************************************************************************
     * Initializes the components of the tab.
     ***************************************************************************************************/
    private void initializeTab() {
        hideButton.setEnabled(false);
        sortButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);

        hideButton.registerAction(this, HIDE);
        sortButton.registerAction(this, SORT);
        moveUpButton.registerAction(this, MOVEUP);
        moveDownButton.registerAction(this, MOVEDOWN);

        columnTable.setTableEditable(false);
        columnTable.setSingleRowSelectionMode();
        columnTable.setTableConfigurationEnabled(false);
        columnTable.setSortingEnabled(false);
        columnTable.setDefaultRenderer(Boolean.class, new SimTableCheckBoxRenderer());
        columnTable.setDefaultRenderer(Boolean.TYPE, new SimTableCheckBoxRenderer());
        columnTable.registerSingleClickAction(this, ROW_SELECTED);
    }

    /****************************************************************************************************
     * Lays out the components of the tab.
     ***************************************************************************************************/
    private void layoutTab() {
        setLayout(new BorderLayout());
        add(columnPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Retrieves the buttons the tab is responsible for.
     ***************************************************************************************************/
    protected RButton[] getButtons() {
        RButton[] array = new RButton[4];
        array[0] = hideButton;
        array[1] = sortButton;
        array[2] = moveUpButton;
        array[3] = moveDownButton;
        return array;
    }

    /****************************************************************************************************
     * Loads the configuration settings for the column. If no configuration data is found, default
     * configuration data is generated.
     * @param identifier The table configuration identifer.
     * @param columnTitles A list of column titles of the table that is being configured.
     ***************************************************************************************************/
    protected void loadConfigSettings(String identifier, List<String> columnTitles) {
        this.identifier = identifier;

        RTableConfigData data = getConfigData();
        if (data.getColumnConfigData().isEmpty()) {
            List<RTableConfigColumnData> configDataList = new ArrayList<>();
            for (String columnTitle : columnTitles) {
                configDataList.add(new RTableConfigColumnData(columnTitle));
            }
            data.setColumnConfigData(configDataList);
        }
        columnTable.setRows(data.getColumnConfigData());
    }

    /****************************************************************************************************
     * Implements the action listener for this tab to delete to the appropriate methods.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(ROW_SELECTED)) {
            updateButtonState();
        } else if (command.equals(HIDE)) {
            doHide();
        } else if (command.equals(VISIBLE)) {
            doVisible();
        } else if (command.equals(SORT)) {
            doSort();
        } else if (command.equals(MOVEUP)) {
            doMoveUp();
        } else if (command.equals(MOVEDOWN)) {
            doMoveDown();
        }
    }

    /****************************************************************************************************
     * Update the selected column data to indicate that the column should be hidden. Update the button
     * state of the other buttons. The last column visible cannot be hidden.
     ***************************************************************************************************/
    private void doHide() {
        if (isMultipleColumnsVisible()) {
            RTableConfigColumnData columnData = (RTableConfigColumnData) columnTable.getSelectedRowData();
            columnData.setVisible(false);
            columnData.setSort(false);
            columnData.setSortOrder(-1);
            getConfigData().resetSortOrder();
            columnTable.updateRow(columnData);
            updateButtonState();
        }
    }

    /****************************************************************************************************
     * Update the selected column data to indicate that the column should be visible. Update the button
     * state of the other buttons.
     ***************************************************************************************************/
    private void doVisible() {
        RTableConfigColumnData columnData = (RTableConfigColumnData) columnTable.getSelectedRowData();
        columnData.setVisible(true);
        columnTable.updateRow(columnData);
        updateButtonState();
    }

    /****************************************************************************************************
     * Alter the sort indicator of the selected column. Mark it sort if not currently indicated, or
     * unmark it if it is currently indicated.
     ***************************************************************************************************/
    private void doSort() {
        RTableConfigColumnData data = (RTableConfigColumnData) columnTable.getSelectedRowData();
        if (data.isSort()) {
            data.setSort(false);
            data.setSortOrder(-1);
            getConfigData().resetSortOrder();
            sortButton.setText(SORT);
        } else {
            data.setSortOrder(getConfigData().getNextSortOrder());
            data.setSort(true);
            sortButton.setText(NO_SORT);
        }
        columnTable.updateRow(data);
    }

    /****************************************************************************************************
     * Move the currently selected row one up making it earlier in the order of displayed columns.
     ***************************************************************************************************/
    private void doMoveUp() {
        int index = columnTable.getSelectedRow();
        if (index == 0) {
            return;
        }
        Object value = columnTable.getRowData(index);
        columnTable.removeRow(value);
        columnTable.insertRow(index - 1, value);
        columnTable.setRowSelectionInterval(index - 1, index - 1);
    }

    /****************************************************************************************************
     * Move the currently selected row one down making it later in the order of displayed columns.
     ***************************************************************************************************/
    private void doMoveDown() {
        int index = columnTable.getSelectedRow();
        if (index == columnTable.getRowCount() - 1) {
            return;
        }
        Object value = columnTable.getRowData(index);
        columnTable.removeRow(value);
        columnTable.insertRow(index + 1, value);
        columnTable.setRowSelectionInterval(index + 1, index + 1);
    }

    /****************************************************************************************************
     * Update the state of the buttons for the column tab.
     ***************************************************************************************************/
    private void updateButtonState() {
        RTableConfigColumnData data = (RTableConfigColumnData) columnTable.getSelectedRowData();
        if (data == null) {
            hideButton.setEnabled(false);
            sortButton.setEnabled(false);
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            return;
        }
        if (data.isVisible()) {
            sortButton.setEnabled(true);
            hideButton.setText(HIDE);
            hideButton.registerAction(this, HIDE);
            hideButton.setEnabled(isMultipleColumnsVisible());
        } else {
            sortButton.setEnabled(false);
            hideButton.setText(VISIBLE);
            hideButton.registerAction(this, VISIBLE);
            hideButton.setEnabled(true);
        }
        if (data.isSort()) {
            sortButton.setText(NO_SORT);
        } else {
            sortButton.setText(SORT);
        }
        moveUpButton.setEnabled(true);
        moveDownButton.setEnabled(true);
    }

    /****************************************************************************************************
     * Helper method to count visible columns.
     ***************************************************************************************************/
    private boolean isMultipleColumnsVisible() {
        int visibleCount = 0;
        for (Iterator iter = columnTable.getAllRowData().iterator(); iter.hasNext();) {
            RTableConfigColumnData testData = (RTableConfigColumnData) iter.next();
            if (testData.isVisible()) {
                visibleCount++;
            }
        }
        return visibleCount > 1;
    }

    /****************************************************************************************************
     * Save the configuration settings.
     ***************************************************************************************************/
    protected void saveConfigSettings() {
        getConfigData().setColumnConfigData(columnTable.getAllRowData());
    }

    /****************************************************************************************************
     * Helper method to retrieve config data.
     ***************************************************************************************************/
    private RTableConfigData getConfigData() {
        return RTableConfigRepository.getTableConfigurationData(identifier);
    }

    /****************************************************************************************************
     * Inner Class - The table definition of the column table.
     ***************************************************************************************************/
    private class ColumnConfigTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return RTableConfigColumnData.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute("Column", "title", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Visible", "visible"));
            attributes.add(new SimTableAttribute("Sort", "sort"));
            return attributes;
        }
    }
}
