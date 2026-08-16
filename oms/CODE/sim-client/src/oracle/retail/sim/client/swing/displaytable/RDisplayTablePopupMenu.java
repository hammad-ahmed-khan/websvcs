package oracle.retail.sim.client.swing.displaytable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigConstants;
import oracle.retail.sim.client.swing.widget.RMenu;
import oracle.retail.sim.client.swing.widget.RMenuItem;
import oracle.retail.sim.client.swing.widget.RPopupMenu;

/********************************************************************************************************
 * This class is the popup menu shown in an RDisplayTable when the table is right-clicked on.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDisplayTablePopupMenu extends RPopupMenu implements ActionListener {
    private static final long serialVersionUID = 8374315616642040035L;

    private static final String HIDE_COLUMN = "Hide Column";
    private static final String REMOVE_SORT = "Remove Sort";
    private static final String PRIMARY_ASC = "Make Primary Sort, Ascending";
    private static final String PRIMARY_DES = "Make Primary Sort, Descending";
    private static final String NEXT_ASC = "Make Next Sort, Ascending";
    private static final String NEXT_DES = "Make Next Sort, Descending";

    private static final String SIZE_CONTENT = "Size Content";
    private static final String SHOW_GRIDLINES = "Show Gridlines";
    private static final String TABLE_CONFIG = "Table Configuration";

    private static final String SMALLEST = "Smallest";
    private static final String SMALLER = "Smaller";
    private static final String STANDARD = "Standard";
    private static final String LARGE = "Large";
    private static final String LARGER = "Larger";
    private static final String LARGEST = "Largest";

    private static final String ALL_LINES = "Row & Column";
    private static final String NONE_LINES = "None";
    private static final String COLUMN_LINES = "Column Only";
    private static final String ROW_LINES = "Row Only";

    // Column Items
    private RMenuItem hideColumnItem = new RMenuItem();
    private RMenuItem removeSortItem = new RMenuItem();
    private RMenuItem sortPrimaryAscItem = new RMenuItem();
    private RMenuItem sortPrimaryDesItem = new RMenuItem();
    private RMenuItem sortNextAscItem = new RMenuItem();
    private RMenuItem sortNextDesItem = new RMenuItem();

    private RMenuItem smallestSizeItem = new RMenuItem(SMALLEST);
    private RMenuItem smallerSizeItem = new RMenuItem(SMALLER);
    private RMenuItem standardSizeItem = new RMenuItem(STANDARD);
    private RMenuItem largeSizeItem = new RMenuItem(LARGE);
    private RMenuItem largerSizeItem = new RMenuItem(LARGER);
    private RMenuItem largestSizeItem = new RMenuItem(LARGEST);

    private RMenuItem allLinesItem = new RMenuItem(ALL_LINES);
    private RMenuItem noLinesItem = new RMenuItem(NONE_LINES);
    private RMenuItem columnLinesItem = new RMenuItem(COLUMN_LINES);
    private RMenuItem rowLinesItem = new RMenuItem(ROW_LINES);

    // Table Menus
    private RMenu sizeContentMenu = new RMenu(SIZE_CONTENT);
    private RMenu gridlinesMenu = new RMenu(SHOW_GRIDLINES);

    // Table Items
    private RMenuItem configurationItem = new RMenuItem(TABLE_CONFIG);

    private RDisplayTable displayTable;

    /****************************************************************************************************
     * This class sub-classes JPopupMenu in order to supply additional functionality.
     ***************************************************************************************************/
    public RDisplayTablePopupMenu(RDisplayTable table) {
        displayTable = table;
        initializeComponents();
        layoutComponents();
    }

    /****************************************************************************************************
     * Initializes Componenets
     ***************************************************************************************************/
    private void initializeComponents() {
        configurationItem.registerAction(this, TABLE_CONFIG);
        smallestSizeItem.registerAction(this, SMALLEST);
        smallerSizeItem.registerAction(this, SMALLER);
        standardSizeItem.registerAction(this, STANDARD);
        largerSizeItem.registerAction(this, LARGE);
        largerSizeItem.registerAction(this, LARGER);
        largestSizeItem.registerAction(this, LARGEST);

        allLinesItem.registerAction(this, ALL_LINES);
        noLinesItem.registerAction(this, NONE_LINES);
        columnLinesItem.registerAction(this, COLUMN_LINES);
        rowLinesItem.registerAction(this, ROW_LINES);
    }

    /****************************************************************************************************
     * Lays out the components within the table.
     ***************************************************************************************************/
    private void layoutComponents() {
        sizeContentMenu.add(smallestSizeItem);
        sizeContentMenu.add(smallerSizeItem);
        sizeContentMenu.add(standardSizeItem);
        sizeContentMenu.add(largeSizeItem);
        sizeContentMenu.add(largerSizeItem);
        sizeContentMenu.add(largestSizeItem);

        gridlinesMenu.add(allLinesItem);
        gridlinesMenu.add(noLinesItem);
        gridlinesMenu.add(columnLinesItem);
        gridlinesMenu.add(rowLinesItem);
    }

    /****************************************************************************************************
     * Initializes all the menu items based on the column passed in.
     ***************************************************************************************************/
    public void initialize(int column) {
        removeAll();

        boolean showColumnOptions = column > -1;

        if (showColumnOptions) {
            String name = displayTable.getColumnName(column);

            hideColumnItem.setText(HIDE_COLUMN, "(" + name + ")");
            removeSortItem.setText(REMOVE_SORT, "(" + name + ")");

            sortPrimaryAscItem.setText(PRIMARY_ASC);
            sortPrimaryDesItem.setText(PRIMARY_DES);
            sortNextAscItem.setText(NEXT_ASC);
            sortNextDesItem.setText(NEXT_DES);
        }
        layoutPopupMenu(showColumnOptions);
    }

    /****************************************************************************************************
     * Lays out menus and menu items on popup dialog.
     ***************************************************************************************************/
    private void layoutPopupMenu(boolean showColumnOptions) {
        removeAll();

        add(sizeContentMenu);
        add(gridlinesMenu);
        addSeparator();
        add(configurationItem);
    }

    /****************************************************************************************************
     * Implements the action listener method to perform the correct action.
     ***************************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals(TABLE_CONFIG)) {
            displayTable.displayConfigurationDialog();
        } else if (command.equals(SMALLEST)) {
            displayTable.validateFontSize(RTableConfigConstants.SMALLEST);
        } else if (command.equals(SMALLER)) {
            displayTable.validateFontSize(RTableConfigConstants.SMALLER);
        } else if (command.equals(STANDARD)) {
            displayTable.validateFontSize(RTableConfigConstants.STANDARD);
        } else if (command.equals(LARGE)) {
            displayTable.validateFontSize(RTableConfigConstants.LARGE);
        } else if (command.equals(LARGER)) {
            displayTable.validateFontSize(RTableConfigConstants.LARGER);
        } else if (command.equals(LARGEST)) {
            displayTable.validateFontSize(RTableConfigConstants.LARGEST);
        } else if (command.equals(ALL_LINES)) {
            displayTable.validateGridLines(RTableConfigConstants.ALL_GRIDLINES);
        } else if (command.equals(NONE_LINES)) {
            displayTable.validateGridLines(RTableConfigConstants.NO_GRIDLINES);
        } else if (command.equals(COLUMN_LINES)) {
            displayTable.validateGridLines(RTableConfigConstants.COL_GRIDLINES);
        } else if (command.equals(ROW_LINES)) {
            displayTable.validateGridLines(RTableConfigConstants.ROW_GRIDLINES);
        }
    }
}
