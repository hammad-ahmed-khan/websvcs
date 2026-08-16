package oracle.retail.sim.client.swing.displaytable;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTab;

/*************************************************************************************************
 * This class is the filter tab portion of table configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

class RSortTab extends RTab implements REventListener {
    private static final long serialVersionUID = -5631235429404856297L;

    // Headers
    private static final String NO = "No.";
    private static final String COLUMN = "Column";
    private static final String DIRECTION = "Sort Direction";

    // Labels
    private static final String MOVE_UP = "Move Up";
    private static final String MOVE_DOWN = "Move Down";
    private static final String SORT_ASC = "Sort Ascending";
    private static final String SORT_DESC = "Sort Descending";
    private static final String LOCK_COL = "Lock Column";
    private static final String UNLOCK_COL = "Unlock Column";

    // Commands
    private static final String SORTED_MOVE_UP = "SortedUpCommand";
    private static final String SORTED_MOVE_DOWN = "SortedDownCommand";
    private static final String SORTED_SORT_ASC = "SortedAscCommand";
    private static final String SORTED_SORT_DESC = "SortedDesCommand";
    private static final String LOCKED_MOVE_UP = "LockedUpCommand";
    private static final String LOCKED_MOVE_DOWN = "LockedDownCommand";
    private static final String LOCKED_SORT_ASC = "LockedAscCommand";
    private static final String LOCKED_SORT_DESC = "LockedDesCommand";
    private static final String SELECT = "SelectColumn";
    private static final String DESELECT = "DeselectColumn";
    private static final String SORTED_ROW_CLICKED = "SortedRowClicked";
    private static final String LOCKED_ROW_CLICKED = "LockedRowClicked";

    private RListEditor columnEditor = new RListEditor("All Columns");

    private RPanel transferPanel = new RPanel();
    private RPanel sortedButtonPanel = new RPanel();
    private RPanel lockedButtonPanel = new RPanel();

    private RDisplayTable sortedTable = new RDisplayTable("ConfigurationDialog.sortedTable");
    private RDisplayTablePane sortedPane = new RDisplayTablePane(sortedTable, "Sorted Columns");

    private RDisplayTable lockedTable = new RDisplayTable("ConfigurationDialog.lockedTable");
    private RDisplayTablePane lockedPane = new RDisplayTablePane(lockedTable, "Locked Columns");

    private String[] headers = { NO, COLUMN, DIRECTION };

    private RLabel fillerOne = new RLabel();
    private RLabel fillerTwo = new RLabel();

    private RArrowButton selectPickButton = new RArrowButton(RArrowButton.EAST);
    private RArrowButton deselectPickButton = new RArrowButton(RArrowButton.WEST);

    private RButton sortedUpButton = new RButton(MOVE_UP);
    private RButton sortedDownButton = new RButton(MOVE_DOWN);
    private RButton sortedAscButton = new RButton(SORT_ASC);
    private RButton sortedDesButton = new RButton(SORT_DESC);
    private RButton sortedLockButton = new RButton(LOCK_COL);

    private RButton lockedUpButton = new RButton(MOVE_UP);
    private RButton lockedDownButton = new RButton(MOVE_DOWN);
    private RButton lockedAscButton = new RButton(SORT_ASC);
    private RButton lockedDesButton = new RButton(SORT_DESC);
    private RButton lockedLockButton = new RButton(UNLOCK_COL);

    //	private String[] columnHeaders = new String[0];
    private String[] translatedHeaders = new String[0];

    private String ascendingText = "Ascending";
    private String descendingText = "Descending";

    //	private boolean isDataModified = false;

    /*************************************************************************************************
     * Constructs a new filter tab.
     * <p>
     * @param title The title to assign to the tab.
     *************************************************************************************************/
    public RSortTab(String title) {
        setTitle(title);
        initializeTab();
        initializeButtons();
        layoutTab();
    }

    /*************************************************************************************************
     * Initializes the components of the tab.
     *************************************************************************************************/
    private void initializeTab() {
        columnEditor.getLabel().setFont(UIManager.getFont(UIThemeName.THEME_BOLD_FONT));

        ascendingText = Translator.getText(ascendingText);
        descendingText = Translator.getText(descendingText);

        sortedPane.setConfigurationEnabled(false);
        lockedPane.setConfigurationEnabled(false);

        int lockedWidth = EditorConstants.COLUMN_LABEL_WIDTH;

        sortedTable.setColumnHeaders(headers);
        sortedTable.buildColumn(NO, DataTypeConstants.INTEGER_LEFT, lockedWidth, lockedWidth, lockedWidth);
        sortedTable.setColumnSortingEnabled(false);
        sortedTable.registerSingleClickAction(this, SORTED_ROW_CLICKED);

        lockedTable.setColumnHeaders(headers);
        lockedTable.buildColumn(NO, DataTypeConstants.INTEGER_LEFT, lockedWidth, lockedWidth, lockedWidth);
        lockedTable.setColumnSortingEnabled(false);
        lockedTable.registerSingleClickAction(this, LOCKED_ROW_CLICKED);
    }

    private void initializeButtons() {
        Dimension minDimension = new Dimension(40, 25);

        selectPickButton.setMinimumSize(minDimension);
        selectPickButton.setPreferredSize(minDimension);
        deselectPickButton.setMinimumSize(minDimension);
        deselectPickButton.setPreferredSize(minDimension);

        selectPickButton.registerAction(this, SELECT);
        deselectPickButton.registerAction(this, DESELECT);

        sortedUpButton.registerAction(this, SORTED_MOVE_UP);
        sortedDownButton.registerAction(this, SORTED_MOVE_DOWN);
        sortedAscButton.registerAction(this, SORTED_SORT_ASC);
        sortedDesButton.registerAction(this, SORTED_SORT_DESC);
        sortedLockButton.registerAction(this, LOCK_COL);

        lockedUpButton.registerAction(this, LOCKED_MOVE_UP);
        lockedDownButton.registerAction(this, LOCKED_MOVE_DOWN);
        lockedAscButton.registerAction(this, LOCKED_SORT_ASC);
        lockedDesButton.registerAction(this, LOCKED_SORT_DESC);
        lockedLockButton.registerAction(this, UNLOCK_COL);

        setSortedButtonsEnabled(false);
        setLockedButtonsEnabled(false);
    }

    /*************************************************************************************************
     * Lays out the components of the tab.
     *************************************************************************************************/
    private void layoutTab() {
        transferPanel.setLayout(new GridBagLayout());
        transferPanel.add(selectPickButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        transferPanel.add(deselectPickButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

        sortedButtonPanel.setLayout(new GridBagLayout());
        sortedButtonPanel.add(fillerOne, GridTool.constraints(0, 0, 1, 1, 0, 1, 0, 2, 0, 0, 2, 0));
        sortedButtonPanel.add(sortedUpButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        sortedButtonPanel.add(sortedDownButton, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        sortedButtonPanel.add(sortedAscButton, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        sortedButtonPanel.add(sortedDesButton, GridTool.constraints(0, 4, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        sortedButtonPanel.add(sortedLockButton, GridTool.constraints(0, 5, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));

        lockedButtonPanel.setLayout(new GridBagLayout());
        lockedButtonPanel.add(fillerTwo, GridTool.constraints(0, 0, 1, 1, 0, 1, 0, 2, 0, 0, 2, 0));
        lockedButtonPanel.add(lockedUpButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        lockedButtonPanel.add(lockedDownButton, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        lockedButtonPanel.add(lockedAscButton, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        lockedButtonPanel.add(lockedDesButton, GridTool.constraints(0, 4, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));
        lockedButtonPanel.add(lockedLockButton, GridTool.constraints(0, 5, 1, 1, 0, 0, 0, 1, 0, 0, 2, 0));

        setLayout(new GridBagLayout());
        add(columnEditor, GridTool.constraints(0, 0, 1, 2, 1, 1, 0, 3, 0, 0, 0, 0));
        add(transferPanel, GridTool.constraints(1, 0, 1, 1, 0, 1, 0, 2, 0, 0, 0, 15));
        add(sortedPane, GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));
        add(sortedButtonPanel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 3, 0, 5, 5, 0));
        add(lockedPane, GridTool.constraints(2, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(lockedButtonPanel, GridTool.constraints(3, 1, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
    }

    /*************************************************************************************************
     * Assigns the headers to the sort tab.
     * <p>
     * @param cHeaders Original column headers.
     * @param tHeaders Language translated headers.
     *************************************************************************************************/
    public void setHeaders(String[] cHeaders, String[] tHeaders) {
        //		columnHeaders = cHeaders;
        translatedHeaders = tHeaders;
        columnEditor.setItems(translatedHeaders);
    }

    /*************************************************************************************************
     * Initializes the tab to the configuration data.
     *************************************************************************************************/
    protected void initialize(TableConfigurationData configurationData) {
        //TOP PRIORITY - SORT TAB - INITIALIZE TAB WITH CONFIGURATION DATA
        //TOP PRIORITY - FILTER TAB - IMPLEMENT
        //		isDataModified = false;
    }

    /*************************************************************************************************
     * Implements the event listener method to delegate to the correct method.
     *************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(SELECT)) {
            selectColumns();
        } else if (command.equals(DESELECT)) {
            deselectColumns();
        } else if (command.equals(LOCK_COL)) {
            doLockColumn();
        } else if (command.equals(UNLOCK_COL)) {
            doUnlockColumn();
        } else if (command.equals(SORTED_ROW_CLICKED)) {
            setSortedButtonsEnabled(sortedTable.hasSelectedRows());
        } else if (command.equals(LOCKED_ROW_CLICKED)) {
            setLockedButtonsEnabled(lockedTable.hasSelectedRows());
        } else if (command.equals(SORTED_MOVE_UP)) {
            doMoveSortRowUp();
        } else if (command.equals(SORTED_MOVE_DOWN)) {
            doMoveSortRowDown();
        } else if (command.equals(SORTED_SORT_ASC)) {
            doSortColumnAscending();
        } else if (command.equals(SORTED_SORT_DESC)) {
            doSortColumnDescending();
        } else if (command.equals(LOCKED_MOVE_UP)) {
            doMoveLockedRowUp();
        } else if (command.equals(LOCKED_MOVE_DOWN)) {
            doMoveLockedRowDown();
        } else if (command.equals(LOCKED_SORT_ASC)) {
            doLockedColumnAscending();
        } else if (command.equals(LOCKED_SORT_DESC)) {
            doLockedColumnDescending();
        }
    }

    /*************************************************************************************************
     * Adds selected rows to the sorted column table.
     *************************************************************************************************/
    private void selectColumns() {
        Object[] values = columnEditor.getSelectedValues();
        if (values.length > 0) {
            addToSortedTable(values);
            columnEditor.removeItems(values);
            //			isDataModified = true;
        }
    }

    /*************************************************************************************************
     * Removes selected from to the sorted column table.
     *************************************************************************************************/
    private void deselectColumns() {
        List values = sortedTable.getAllSelectedData();
        if (!values.isEmpty()) {
            columnEditor.addItems(values);
            sortedTable.removeSelectedRow();
            //			isDataModified = true;
        }
    }

    /*************************************************************************************************
     * Adds the values to the sorted table.
     *************************************************************************************************/
    private void addToSortedTable(Object[] values) {
        int lastRow = sortedTable.getLastRowNumber();
        int counter = 1;

        if (lastRow > -1) {
            try {
                String countText = sortedTable.getCellValueAt(sortedTable.getLastRowNumber(), NO);
                counter = Integer.parseInt(countText) + 1;
            } catch (NumberFormatException exception) {
                throw new IllegalStateException("Column Counter unexpectedly not an integer!");
            }
        }
        for (Object value : values) {
            String[] row = new String[3];

            row[0] = String.valueOf(counter++);
            row[1] = (String) value;
            row[2] = ascendingText;

            sortedTable.addRow(row, value);
        }
    }

    /*************************************************************************************************
     * Moves a column to the locked state.
     *************************************************************************************************/
    private void doLockColumn() {
        lockedTable.addRow(sortedTable.getSelectedRowDisplayData(), sortedTable.getSelectedData());
        resetLockedTable();
        sortedTable.removeSelectedRow();
        resetSortedTable();
    }

    /*************************************************************************************************
     * Removes a column to the locked state.
     *************************************************************************************************/
    private void doUnlockColumn() {
        sortedTable.addRow(lockedTable.getSelectedRowDisplayData(), lockedTable.getSelectedData());
        resetSortedTable();
        lockedTable.removeSelectedRow();
        resetLockedTable();
    }

    /*************************************************************************************************
     * Resets the row number on the sorted table.
     *************************************************************************************************/
    private void resetSortedTable() {
        String[] row = null;
        int rows = sortedTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            row = sortedTable.getRowDisplayData(i);
            row[0] = String.valueOf(i + 1);
            sortedTable.updateRow(row, i);
        }
    }

    /*************************************************************************************************
     * Resets the row number on the locked table.
     *************************************************************************************************/
    private void resetLockedTable() {
        String[] row = null;
        int rows = lockedTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            row = lockedTable.getRowDisplayData(i);
            row[0] = String.valueOf(i + 1);
            lockedTable.updateRow(row, i);
        }
    }

    /*************************************************************************************************
     * Enables or disabled the sorted buttons for the sorted column table.
     *************************************************************************************************/
    private void setSortedButtonsEnabled(boolean enabled) {
        sortedUpButton.setEnabled(enabled);
        sortedDownButton.setEnabled(enabled);
        sortedAscButton.setEnabled(enabled);
        sortedDesButton.setEnabled(enabled);
        sortedLockButton.setEnabled(enabled);
    }

    /*************************************************************************************************
     * Enables or disabled the locked buttons for the locked column table.
     *************************************************************************************************/
    private void setLockedButtonsEnabled(boolean enabled) {
        lockedUpButton.setEnabled(enabled);
        lockedDownButton.setEnabled(enabled);
        lockedAscButton.setEnabled(enabled);
        lockedDesButton.setEnabled(enabled);
        lockedLockButton.setEnabled(enabled);
    }

    /*************************************************************************************************
     * Alters value of sort column to ascending.
     *************************************************************************************************/
    private void doSortColumnAscending() {
        String[] row = sortedTable.getSelectedRowDisplayData();
        row[2] = ascendingText;
        sortedTable.updateRow(row, sortedTable.getSelectedRow());
    }

    /*************************************************************************************************
     * Alters value of sort column to descending.
     *************************************************************************************************/
    private void doSortColumnDescending() {
        String[] row = sortedTable.getSelectedRowDisplayData();
        row[2] = descendingText;
        sortedTable.updateRow(row, sortedTable.getSelectedRow());
    }

    /*************************************************************************************************
     * Alters value of locked column to ascending.
     *************************************************************************************************/
    private void doLockedColumnAscending() {
        String[] row = lockedTable.getSelectedRowDisplayData();
        row[2] = ascendingText;
        lockedTable.updateRow(row, lockedTable.getSelectedRow());
    }

    /*************************************************************************************************
     * Alters value of locked column to descending.
     *************************************************************************************************/
    private void doLockedColumnDescending() {
        String[] row = lockedTable.getSelectedRowDisplayData();
        row[2] = descendingText;
        lockedTable.updateRow(row, lockedTable.getSelectedRow());
    }

    /*************************************************************************************************
     * Moves a sort row up in the sequence.
     *************************************************************************************************/
    private void doMoveSortRowUp() {
        shiftRow(sortedTable, sortedTable.getSelectedRow() - 1);
        resetSortedTable();
    }

    /*************************************************************************************************
     * Moves a sort row down in the sequence.
     *************************************************************************************************/
    private void doMoveSortRowDown() {
        shiftRow(sortedTable, sortedTable.getSelectedRow() + 1);
        resetSortedTable();
    }

    /*************************************************************************************************
     * Shifts a row in a table.
     *************************************************************************************************/
    private void shiftRow(RDisplayTable table, int rowNumber) {
        if (rowNumber < 0 || rowNumber > table.getLastRowNumber()) {
            return;
        }
        Object object = table.getSelectedData();
        String[] row = table.getSelectedRowDisplayData();
        table.removeSelectedRow();
        table.insertRow(row, NO, object, rowNumber);
        table.setRowSelection(rowNumber);
    }

    /*************************************************************************************************
     * Moves a locked row up in the sequence.
     *************************************************************************************************/
    private void doMoveLockedRowUp() {
        shiftRow(lockedTable, lockedTable.getSelectedRow() - 1);
        resetLockedTable();
    }

    /*************************************************************************************************
     * Moves a locked row up in the sequence.
     *************************************************************************************************/
    private void doMoveLockedRowDown() {
        shiftRow(lockedTable, lockedTable.getSelectedRow() + 1);
        resetLockedTable();
    }

    /*************************************************************************************************
     * Saves the information to the configuration data.
     *************************************************************************************************/
    protected void save(TableConfigurationData configurationData) {
        //TOP PRIORITY - SORT TAB - SAVE INFORMATION TO CONFIGURATION DATA
    }
}
