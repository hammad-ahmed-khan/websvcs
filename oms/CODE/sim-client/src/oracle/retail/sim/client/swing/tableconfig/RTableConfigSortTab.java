package oracle.retail.sim.client.swing.tableconfig;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
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
 * This class is the tab that handles the configuration of the sorted columns in the table. Settings that
 * can be altered include the sequence of the sorting and whether the columns sorts ascending or
 * descending.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RTableConfigSortTab extends RTab implements REventListener {
    private static final long serialVersionUID = -2253743536346346498L;

    private String identifier;
    private SimTable sortColumnTable = new SimTable(new SortConfigTableDefinition());
    private SimTablePane sortColumnPane = new SimTablePane(sortColumnTable);

    private static final String ASCENDING = "Ascending";
    private static final String DESCENDING = "Descending";
    private static final String DIRECTION = ASCENDING;
    private static final String MOVEUP = "Move Up";
    private static final String MOVEDOWN = "Move Down";
    private static final String ROW_SELECTED = "Row.selected";

    private RButton directionButton = new RButton(DIRECTION);
    private RButton moveUpButton = new RButton(MOVEUP);
    private RButton moveDownButton = new RButton(MOVEDOWN);

    /****************************************************************************************************
     * Constructs a new configuration sort tab.
     * <p>
     * @param title The title to assign to the tab.
     ***************************************************************************************************/
    public RTableConfigSortTab(String title) {
        setTitle(title);
        initializeTab();
        layoutTab();
    }

    /****************************************************************************************************
     * Initializes the components of the tab.
     ***************************************************************************************************/
    private void initializeTab() {
        directionButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);

        directionButton.registerAction(this, ASCENDING);
        moveUpButton.registerAction(this, MOVEUP);
        moveDownButton.registerAction(this, MOVEDOWN);

        sortColumnTable.setTableEditable(false);
        sortColumnTable.setTableConfigurationEnabled(false);
        sortColumnTable.setSortingEnabled(false);
        sortColumnTable.setDefaultRenderer(Boolean.class, new SimTableCheckBoxRenderer());
        sortColumnTable.setDefaultRenderer(Boolean.TYPE, new SimTableCheckBoxRenderer());
        sortColumnTable.registerSingleClickAction(this, ROW_SELECTED);
    }

    /****************************************************************************************************
     * Lays out the components of the tab.
     ***************************************************************************************************/
    private void layoutTab() {
        setLayout(new BorderLayout());
        add(sortColumnPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Retrieves the buttons the tab is responsible for.
     ***************************************************************************************************/
    protected RButton[] getButtons() {
        RButton[] array = new RButton[3];
        array[0] = directionButton;
        array[1] = moveUpButton;
        array[2] = moveDownButton;
        return array;
    }

    /****************************************************************************************************
     * Loads the configuration settings for the table.
     ***************************************************************************************************/
    protected void loadConfigSettings(String identifier) {
        this.identifier = identifier;
        reloadSortInformation();
    }

    /****************************************************************************************************
     * Reload the sort column table with the correct information.
     ***************************************************************************************************/
    protected void reloadSortInformation() {
        sortColumnTable.setRows(getConfigData().getSortColumnList());
    }

    /****************************************************************************************************
     * Implements the action listener for this tab.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(ROW_SELECTED)) {
            updateButtonState();
        } else if (command.equals(ASCENDING)) {
            doDirection();
        } else if (command.equals(MOVEUP)) {
            doMoveUp();
        } else if (command.equals(MOVEDOWN)) {
            doMoveDown();
        }
    }

    /****************************************************************************************************
     * Updates the selected column and assigns the correct ascending/descending setting to the
     * configuration information.
     ***************************************************************************************************/
    private void doDirection() {
        RTableConfigColumnData columnData = (RTableConfigColumnData) sortColumnTable.getSelectedRowData();
        if (columnData == null) {
            return;
        }
        if (columnData.isAscending()) {
            columnData.setAscending(false);
            directionButton.setText(ASCENDING);
        } else {
            columnData.setAscending(true);
            directionButton.setText(DESCENDING);
        }
        sortColumnTable.updateRow(columnData);
    }

    /****************************************************************************************************
     * Move the currently selected row one up making it earlier in the order of sorted columns.
     ***************************************************************************************************/
    private void doMoveUp() {
        int index = sortColumnTable.getSelectedRow();
        if (index == 0) {
            return;
        }
        Object value = sortColumnTable.getRowData(index);
        sortColumnTable.removeRow(value);
        sortColumnTable.insertRow(index - 1, value);
        sortColumnTable.setRowSelectionInterval(index - 1, index - 1);
        resetSortOrder();
    }

    /****************************************************************************************************
     * Move the currently selected row one down making it later in the order of sorted columns.
     ***************************************************************************************************/
    private void doMoveDown() {
        int index = sortColumnTable.getSelectedRow();
        if (index == sortColumnTable.getRowCount() - 1) {
            return;
        }
        Object value = sortColumnTable.getRowData(index);
        sortColumnTable.removeRow(value);
        sortColumnTable.insertRow(index + 1, value);
        sortColumnTable.setRowSelectionInterval(index + 1, index + 1);
        resetSortOrder();
    }

    /****************************************************************************************************
     * Update the state of the buttons for the tab.
     ***************************************************************************************************/
    private void resetSortOrder() {
        List<RTableConfigColumnData> sortedColumnList = sortColumnTable.getAllRowData();
        int i = 1;
        for (RTableConfigColumnData data : sortedColumnList) {
            data.setSortOrder(i++);
        }
    }

    /****************************************************************************************************
     * Update the state of the buttons for the tab.
     ***************************************************************************************************/
    private void updateButtonState() {
        RTableConfigColumnData data = (RTableConfigColumnData) sortColumnTable.getSelectedRowData();
        if (data == null) {
            directionButton.setEnabled(false);
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            return;
        }
        if (data.isAscending()) {
            directionButton.setText(DESCENDING);
        } else {
            directionButton.setText(ASCENDING);
        }
        directionButton.setEnabled(true);
        moveUpButton.setEnabled(true);
        moveDownButton.setEnabled(true);
    }

    /****************************************************************************************************
     * Helper method to retrieve the table configuration data.
     ***************************************************************************************************/
    private RTableConfigData getConfigData() {
        return RTableConfigRepository.getTableConfigurationData(identifier);
    }

    /****************************************************************************************************
     * Inner Class  - The table definition of the column sort table.
     ***************************************************************************************************/
    private class SortConfigTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return RTableConfigColumnData.class;
        }

        public List getAttributes() {
            List attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Column", "title"));
            attributes.add(new SimTableAttribute("Ascending", "ascending"));
            return attributes;
        }
    }
}
