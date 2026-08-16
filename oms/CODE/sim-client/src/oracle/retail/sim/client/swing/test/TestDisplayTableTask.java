package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.client.swing.displaytable.RDisplayTableCell;
import oracle.retail.sim.client.swing.displaytable.RDisplayTablePane;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.event.RTableCellEvent;
import oracle.retail.sim.client.swing.event.RTableCellListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * Test Display Table Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestDisplayTableTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -3957264076858864425L;

    private TestDisplayTablePanelTwo contentPanel = new TestDisplayTablePanelTwo("Display Table Test Panel");

    private static final String HEADER_ONE = "Header One";
    private static final String HEADER_TWO = "Header Two";
    private static final String HEADER_THREE = "Header Three";
    private static final String HEADER_FOUR = "Header Four";

    private static final String LOAD = "Load";
    private static final String DONE = "Done";

    private RButton loadButton = new RButton(LOAD);
    private RButton exitButton = new RButton(DONE);

    private static final String TEST_SELECTED_ROW = "Test Selected Row";
    private static final String TEST_UPDATED_ROW = "Test Updated Row";
    private static final String TEST_COLUMN_TYPE = "Test Column Type";
    private static final String TEST_COLUMN_BACKGROUND = "Test Column Background";
    private static final String TEST_COLUMN_FOREGROUND = "Test Column Foreground";
    private static final String TEST_GET_CELL_VALUE = "Test Get Cell Value";
    private static final String TEST_GET_ROW_NUMBER = "Test Get Row Number";
    private static final String TEST_SELECTED_COL_DATA = "Test Selected Col Data";
    private static final String TEST_GET_DATA = "Test Get Data";
    private static final String TEST_GET_ALL_DATA = "Test Get All Data";
    private static final String TEST_GET_ALL_DATA_COLUMN = "Test Get All Data By Column";
    private static final String TEST_GET_SELECTED_DATA = "Test Get Selected Data";
    private static final String TEST_GET_ALL_SELECTED_DATA = "Test Get All Selected Data";
    private static final String TEST_GET_DISPLAY_DATA = "Test Get Selected Display Data";
    private static final String TEST_GET_DISPLAY_DATA_BY_ROW = "Test Get Display Data By Row";
    private static final String TEST_SET_CELL_VALUE = "Test Set Cell Value";
    private static final String TEST_SET_DATA = "Test Set Data";
    private static final String TEST_CLEAR_COLUMN = "Test Clear Column";
    private static final String TEST_SET_ROW_SELECTION = "Test Row Selection";
    private static final String TEST_SET_ROW_SELECTION_BY_DATA = "Test Row Selection By Data";
    private static final String TEST_ADD_ROWS = "Test Add Rows";
    private static final String TEST_ADD_SINGLE_ROW = "Test Add Single Row";
    private static final String TEST_ADD_ROW_BY_ARRAY = "Test Add Row By Array";
    private static final String TEST_ADD_ROW_BY_TABLE_CELL = "Test Add Row By TableCell";
    private static final String TEST_ADD_ROW_ARRAY_OBJECT = "Test Add Row With Array and Object";
    // private static final String TEST_ADD_ROW_ARRAY_COLUMN = "Test Add Row Array, Column and Object";
    private static final String TEST_INSERT_ROW = "Test Insert Row";
    private static final String TEST_INSERT_ROW_TABLE_CELL = "Test Insert Row By TableCell";
    private static final String TEST_FULL_INSERT_ROW = "Test Full Insert Row";
    private static final String TEST_UPDATE_ROW = "Test Update Row";
    private static final String TEST_UPDATE_ROW_ARRAY_OBJECT = "Test Update Row With Array and Object";
    private static final String TEST_UPDATE_ROW_TABLE_CELL = "Test Update Row By TableCell";
    private static final String TEST_UPDATE_ROW_ARRAY_NUMBER = "Test Update Row With Array and Row";
    private static final String TEST_REMOVE_ROW_BY_VALUE = "Test Remove Row By Value";
    private static final String TEST_REMOVE_ROW_BY_DATA = "Test Remove Row By Data";
    private static final String TEST_REMOVE_ROW_BY_COL_DATA = "Test Remove Row Column Data";
    private static final String TEST_REMOVE_ROW_BY_COLLECTION = "Test Remove Row Collection";

    private RButton test01Button = new RButton(TEST_SELECTED_ROW);
    private RButton test02Button = new RButton(TEST_UPDATED_ROW);
    private RButton test03Button = new RButton(TEST_COLUMN_TYPE);
    private RButton test04Button = new RButton(TEST_COLUMN_BACKGROUND);
    private RButton test05Button = new RButton(TEST_COLUMN_FOREGROUND);
    private RButton test06Button = new RButton(TEST_GET_CELL_VALUE);
    private RButton test07Button = new RButton(TEST_GET_ROW_NUMBER);
    private RButton test08Button = new RButton(TEST_SELECTED_COL_DATA);
    private RButton test09Button = new RButton(TEST_GET_DATA);
    private RButton test10Button = new RButton(TEST_GET_ALL_DATA);
    private RButton test11Button = new RButton(TEST_GET_ALL_DATA_COLUMN);
    private RButton test12Button = new RButton(TEST_GET_SELECTED_DATA);
    private RButton test13Button = new RButton(TEST_GET_ALL_SELECTED_DATA);
    // CHARLES - HERE
    private RButton test14Button = new RButton(TEST_GET_DISPLAY_DATA);
    private RButton test15Button = new RButton(TEST_GET_DISPLAY_DATA_BY_ROW);
    private RButton test16Button = new RButton(TEST_SET_CELL_VALUE);
    private RButton test17Button = new RButton(TEST_SET_DATA);
    private RButton test18Button = new RButton(TEST_CLEAR_COLUMN);
    private RButton test19Button = new RButton(TEST_SET_ROW_SELECTION);
    private RButton test20Button = new RButton(TEST_SET_ROW_SELECTION_BY_DATA);
    private RButton test21Button = new RButton(TEST_ADD_ROWS);
    private RButton test22Button = new RButton(TEST_ADD_SINGLE_ROW);
    private RButton test23Button = new RButton(TEST_ADD_ROW_BY_ARRAY);
    private RButton test24Button = new RButton(TEST_ADD_ROW_BY_TABLE_CELL);
    private RButton test25Button = new RButton(TEST_ADD_ROW_ARRAY_OBJECT);
    private RButton test26Button = new RButton(TEST_INSERT_ROW);
    private RButton test27Button = new RButton(TEST_INSERT_ROW_TABLE_CELL);
    // CHARLES - HERE
    private RButton test28Button = new RButton(TEST_FULL_INSERT_ROW);
    private RButton test29Button = new RButton(TEST_UPDATE_ROW);
    private RButton test30Button = new RButton(TEST_UPDATE_ROW_ARRAY_OBJECT);
    private RButton test31Button = new RButton(TEST_UPDATE_ROW_TABLE_CELL);
    private RButton test32Button = new RButton(TEST_UPDATE_ROW_ARRAY_NUMBER);
    private RButton test33Button = new RButton(TEST_REMOVE_ROW_BY_VALUE);
    private RButton test34Button = new RButton(TEST_REMOVE_ROW_BY_DATA);
    private RButton test35Button = new RButton(TEST_REMOVE_ROW_BY_COL_DATA);
    private RButton test36Button = new RButton(TEST_REMOVE_ROW_BY_COLLECTION);

    private DataObject objectOne = new DataObject("Row One Object");
    private DataObject objectTwo = new DataObject("Row Two Object");
    private DataObject objectThree = new DataObject("Row Three Object");
    private DataObject objectFour = new DataObject("Row Four Object");
    private DataObject objectFive = new DataObject("Row Five Object");
    private DataObject objectSix = new DataObject("Row Six Object");
    private DataObject objectSeven = new DataObject("Row Seven Object");
    private DataObject objectEight = new DataObject("Row Eight Object");

    public TestDisplayTableTask() {
        setTaskTitle("Test Display Table");
    }

    public TestDisplayTableTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        test01Button.registerAction(this, TEST_SELECTED_ROW);
        test02Button.registerAction(this, TEST_UPDATED_ROW);
        test03Button.registerAction(this, TEST_COLUMN_TYPE);
        test04Button.registerAction(this, TEST_COLUMN_BACKGROUND);
        test05Button.registerAction(this, TEST_COLUMN_FOREGROUND);
        test06Button.registerAction(this, TEST_GET_CELL_VALUE);
        test07Button.registerAction(this, TEST_GET_ROW_NUMBER);
        test08Button.registerAction(this, TEST_SELECTED_COL_DATA);
        test09Button.registerAction(this, TEST_GET_DATA);
        test10Button.registerAction(this, TEST_GET_ALL_DATA);
        test11Button.registerAction(this, TEST_GET_ALL_DATA_COLUMN);
        test12Button.registerAction(this, TEST_GET_SELECTED_DATA);
        test13Button.registerAction(this, TEST_GET_ALL_SELECTED_DATA);
        test14Button.registerAction(this, TEST_GET_DISPLAY_DATA);
        test15Button.registerAction(this, TEST_GET_DISPLAY_DATA_BY_ROW);
        test16Button.registerAction(this, TEST_SET_CELL_VALUE);
        test17Button.registerAction(this, TEST_SET_DATA);
        test18Button.registerAction(this, TEST_CLEAR_COLUMN);
        test19Button.registerAction(this, TEST_SET_ROW_SELECTION);
        test20Button.registerAction(this, TEST_SET_ROW_SELECTION_BY_DATA);
        test21Button.registerAction(this, TEST_ADD_ROWS);
        test22Button.registerAction(this, TEST_ADD_SINGLE_ROW);
        test23Button.registerAction(this, TEST_ADD_ROW_BY_ARRAY);
        test24Button.registerAction(this, TEST_ADD_ROW_BY_TABLE_CELL);
        test25Button.registerAction(this, TEST_ADD_ROW_ARRAY_OBJECT);
        test26Button.registerAction(this, TEST_INSERT_ROW);
        test27Button.registerAction(this, TEST_INSERT_ROW_TABLE_CELL);
        test28Button.registerAction(this, TEST_FULL_INSERT_ROW);
        test29Button.registerAction(this, TEST_UPDATE_ROW);
        test30Button.registerAction(this, TEST_UPDATE_ROW_ARRAY_OBJECT);
        test31Button.registerAction(this, TEST_UPDATE_ROW_TABLE_CELL);
        test32Button.registerAction(this, TEST_UPDATE_ROW_ARRAY_NUMBER);
        test33Button.registerAction(this, TEST_REMOVE_ROW_BY_VALUE);
        test34Button.registerAction(this, TEST_REMOVE_ROW_BY_DATA);
        test35Button.registerAction(this, TEST_REMOVE_ROW_BY_COL_DATA);
        test36Button.registerAction(this, TEST_REMOVE_ROW_BY_COLLECTION);

        loadButton.registerAction(this, LOAD);
        exitButton.registerAction(this, DONE);

        addButton(loadButton);
        addButton(test15Button);
        addButton(exitButton);

        addContentPanel(contentPanel);
    }

    public void start() {}

    public void stop() {}

    public boolean isStartable() {
        return true;
    }

    public boolean isStoppable() {
        return true;
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(LOAD)) {
            contentPanel.loadTable();
        } else if (command.equals(TEST_SELECTED_ROW)) {
            contentPanel.testSelectedRow();
        } else if (command.equals(TEST_UPDATED_ROW)) {
            contentPanel.testUpdatedRow();
        } else if (command.equals(TEST_COLUMN_TYPE)) {
            contentPanel.testColumnType();
        } else if (command.equals(TEST_COLUMN_BACKGROUND)) {
            contentPanel.testColumnBackground();
        } else if (command.equals(TEST_COLUMN_FOREGROUND)) {
            contentPanel.testColumnForeground();
        } else if (command.equals(TEST_GET_CELL_VALUE)) {
            contentPanel.testGetCellValue();
        } else if (command.equals(TEST_GET_ROW_NUMBER)) {
            contentPanel.testGetRowNumber();
        } else if (command.equals(TEST_SELECTED_COL_DATA)) {
            contentPanel.testSelectedColumnData();
        } else if (command.equals(TEST_GET_DATA)) {
            contentPanel.testGetData();
        } else if (command.equals(TEST_GET_ALL_DATA)) {
            contentPanel.testGetAllData();
        } else if (command.equals(TEST_GET_ALL_DATA_COLUMN)) {
            contentPanel.testGetAllDataByColumn();
        } else if (command.equals(TEST_GET_SELECTED_DATA)) {
            contentPanel.testGetSelectedData();
        } else if (command.equals(TEST_GET_ALL_SELECTED_DATA)) {
            contentPanel.testGetAllSelectedData();
        } else if (command.equals(TEST_GET_DISPLAY_DATA)) {
            contentPanel.testGetSelectedDisplayData();
        } else if (command.equals(TEST_GET_DISPLAY_DATA_BY_ROW)) {
            contentPanel.testGetSelectedDisplayDataByRow();
        } else if (command.equals(TEST_SET_CELL_VALUE)) {
            contentPanel.testSetCellValue();
        } else if (command.equals(TEST_SET_DATA)) {
            contentPanel.testSetData();
        } else if (command.equals(TEST_CLEAR_COLUMN)) {
            contentPanel.testClearColumn();
        } else if (command.equals(TEST_SET_ROW_SELECTION)) {
            contentPanel.testSetRowSelection();
        } else if (command.equals(TEST_SET_ROW_SELECTION_BY_DATA)) {
            contentPanel.testSetRowSelectionByData();
        } else if (command.equals(TEST_ADD_ROWS)) {
            contentPanel.testAddRows();
        } else if (command.equals(TEST_ADD_SINGLE_ROW)) {
            contentPanel.testAddSingleRow();
        } else if (command.equals(TEST_ADD_ROW_BY_ARRAY)) {
            contentPanel.testAddRowByArray();
        } else if (command.equals(TEST_ADD_ROW_BY_TABLE_CELL)) {
            contentPanel.testAddRowByTableCell();
        } else if (command.equals(TEST_ADD_ROW_ARRAY_OBJECT)) {
            contentPanel.testAddRowArrayObject();
        } else if (command.equals(TEST_INSERT_ROW)) {
            contentPanel.testInsertRow();
        } else if (command.equals(TEST_INSERT_ROW_TABLE_CELL)) {
            contentPanel.testInsertRowTableCell();
        } else if (command.equals(TEST_FULL_INSERT_ROW)) {
            contentPanel.testFullInsertRow();
        } else if (command.equals(TEST_UPDATE_ROW)) {
            contentPanel.testUpdateRow();
        } else if (command.equals(TEST_UPDATE_ROW_ARRAY_OBJECT)) {
            contentPanel.testUpdateRowArrayObject();
        } else if (command.equals(TEST_UPDATE_ROW_TABLE_CELL)) {
            contentPanel.testUpdateRowTableCell();
        } else if (command.equals(TEST_UPDATE_ROW_ARRAY_NUMBER)) {
            contentPanel.testUpdateRowArrayNumber();
        } else if (command.equals(TEST_REMOVE_ROW_BY_VALUE)) {
            contentPanel.testRemoveRowByValue();
        } else if (command.equals(TEST_REMOVE_ROW_BY_DATA)) {
            contentPanel.testRemoveRowByData();
        } else if (command.equals(TEST_REMOVE_ROW_BY_COL_DATA)) {
            contentPanel.testRemoveRowByColData();
        } else if (command.equals(TEST_REMOVE_ROW_BY_COLLECTION)) {
            contentPanel.testRemoveRowByCollection();
        } else if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     * INNER CLASS TABLE PANEL
     *
     ***************************************************************************************************/

    private class TestDisplayTablePanelTwo extends RContentPanel implements RTableCellListener {
        private static final long serialVersionUID = 2034358501367828396L;

        private String[] headers = { HEADER_ONE, HEADER_TWO, HEADER_THREE, HEADER_FOUR };
        private String[] sortHeaders = { HEADER_ONE, HEADER_TWO, HEADER_THREE };

        private RDisplayTable displayTable = new RDisplayTable("Test Display Table");
        private RDisplayTablePane displayTablePane = new RDisplayTablePane(displayTable, "Table Title");

        private RTextAreaEditor outputEditor = new RTextAreaEditor("Test Results");

        public TestDisplayTablePanelTwo(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            // displayTable.setTableRowDisplayer(new DefaultSelectableTableRowDisplayer(String.class,
            // headers, headers));
            displayTable.setTableRowDisplayer(new TestTableRowDisplayer(headers));
            displayTable.setColumnSortOrder(sortHeaders);
            displayTable.setColumnSortDirection(HEADER_TWO, false);
            displayTable.setColumnIcon(HEADER_FOUR, UIManager.getIcon(UIThemeName.CALENDARFIELD_ICON));
            displayTable.setSelectionMode(RDisplayTable.MULTIPLE_ROWS);
            displayTable.registerTableCellAction(this, "Hello");

            outputEditor.setMinimumHeight(100);
            outputEditor.setTitleAlignment(EditorConstants.TOP);
        }

        private void layoutContents() {
            RPanel panel = getContentPane();
            panel.setLayout(new BorderLayout());
            panel.add(displayTablePane, BorderLayout.CENTER);
            panel.add(outputEditor, BorderLayout.SOUTH);
        }

        public void processTableCellEvent(RTableCellEvent event) {
            printLine(event);
        }

        protected void loadTable() {
            displayTable.setRows(getDataList());
            displayTable.resort();
        }

        private List getDataList() {
            List dataList = new ArrayList<>();
            dataList.add(objectOne);
            dataList.add(objectTwo);
            dataList.add(objectThree);
            dataList.add(objectFour);
            dataList.add(objectFive);
            dataList.add(objectSix);
            return dataList;
        }

        protected void testSelectedRow() {
            Object object = displayTable.getSelectedData();
            if (object != null) {
                printLine("Selected data is " + object.toString());
            }
        }

        protected void testUpdatedRow() {
            Object object = displayTable.getSelectedData();
            if (object != null) {
                DataObject dataObject = (DataObject) object;
                dataObject.setDescription(dataObject.getDescription() + " Altered");
                displayTable.updateRow(dataObject);
            }
        }

        protected void testColumnBackground() {
            displayTable.setColumnBackgroundColor(HEADER_TWO, Color.CYAN);
        }

        protected void testColumnForeground() {
            displayTable.setColumnForegroundColor(HEADER_TWO, Color.BLUE);
        }

        protected void testColumnType() {
            displayTable.setColumnType(HEADER_FOUR, DataTypeConstants.TEXT);
        }

        protected void testGetCellValue() {
            printLine(displayTable.getCellValueAt(0, HEADER_FOUR));
        }

        protected void testGetRowNumber() {
            printLine(displayTable.getRowNumber(HEADER_ONE, "Row One Object"));
            Object object = displayTable.getSelectedData();
            if (object != null) {
                printLine(displayTable.getRowNumber(HEADER_ONE, object));
            }
        }

        protected void testSelectedColumnData() {
            String[] data = displayTable.getSelectedColumnData(HEADER_THREE);
            if (data.length > 0) {
                printLine(data[0]);
            }
        }

        protected void testGetData() {
            printLine("Row 0 Object: " + displayTable.getData(0));
            printLine("Row 0 Object From Header: " + displayTable.getData(0, HEADER_ONE));
            List dataList = displayTable.getData(HEADER_TWO, "14");
            for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                printLine("Filter: " + iterator.next());
            }
            dataList = displayTable.getData(HEADER_ONE, HEADER_TWO, "14");
            for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                printLine("Filter From Header: " + iterator.next());
            }
        }

        protected void testGetAllData() {
            List dataList = displayTable.getAllData();
            int counter = 1;
            for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                printLine("Data: " + String.valueOf(counter++) + " " + iterator.next());
            }
        }

        protected void testGetAllDataByColumn() {
            List dataList = displayTable.getAllData(HEADER_ONE);
            int counter = 1;
            for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                printLine("Data: " + String.valueOf(counter++) + " " + iterator.next());
            }
        }

        protected void testGetSelectedData() {
            printLine(displayTable.getSelectedData(HEADER_ONE));
        }

        protected void testGetAllSelectedData() {
            List dataList = displayTable.getAllSelectedData(HEADER_ONE);
            int counter = 1;
            for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                printLine("Data: " + String.valueOf(counter++) + " " + iterator.next());
            }
        }

        public void testGetSelectedDisplayData() {
            printLine(displayTable.getSelectedRowDisplayData());
        }

        public void testGetSelectedDisplayDataByRow() {
            printLine(displayTable.getRowDisplayData(0));
        }

        protected void testSetCellValue() {
            displayTable.setCellValue(0, HEADER_FOUR, "DUDE");
        }

        protected void testSetData() {
            displayTable.setData(0, HEADER_ONE, new DataObject("Row Seven"));
            printLine(displayTable.getData(0, HEADER_ONE));
        }

        protected void testClearColumn() {
            displayTable.clearColumn(HEADER_TWO);
            displayTable.clearColumn(HEADER_FOUR);
        }

        protected void testSetRowSelection() {
            displayTable.setRowSelection(1);
        }

        protected void testSetRowSelectionByData() {
            displayTable.setRowSelection(objectThree);
            // displayTable.setRowSelection(HEADER_ONE, objectSix); // Test Both Ways
        }

        protected void testAddRows() {
            List rowList = new ArrayList<>();
            rowList.add(objectSeven);
            rowList.add(objectEight);
            displayTable.addRows(rowList);
        }

        protected void testAddSingleRow() {
            displayTable.addRow(objectSeven);
        }

        protected void testAddRowByArray() {
            String[] array = { "a", "b", "c", "d" };
            displayTable.addRow(array);
        }

        protected void testAddRowByTableCell() {
            RDisplayTableCell[] array = new RDisplayTableCell[4];
            array[0] = getCell(DataTypeConstants.INTEGER_RIGHT, "45");
            array[1] = getCell(DataTypeConstants.TEXT, "Hello");
            array[2] = getCell(DataTypeConstants.TEXT, "Goodbye");
            array[3] = getCell(DataTypeConstants.TEXT, "Whatever");
            displayTable.addRow(array);
        }

        private RDisplayTableCell getCell(int type, String value) {
            RDisplayTableCell cell = new RDisplayTableCell();
            cell.setType(type);
            cell.setValue(value);
            return cell;
        }

        protected void testAddRowArrayObject() {
            String[] array = { "a", "b", "c", "d" };
            String x = "x";
            displayTable.addRow(array, HEADER_ONE, x);
            displayTable.setRowSelection(x);
            printLine(displayTable.getSelectedData());
        }

        protected void testInsertRow() {
            displayTable.insertRow(objectEight, 3);
        }

        protected void testInsertRowTableCell() {
            printLine("testing");
            RDisplayTableCell[] array = new RDisplayTableCell[4];
            array[0] = getCell(DataTypeConstants.INTEGER_RIGHT, "45");
            array[1] = getCell(DataTypeConstants.TEXT, "Hello");
            array[2] = getCell(DataTypeConstants.TEXT, "Goodbye");
            array[3] = getCell(DataTypeConstants.TEXT, "Whatever");
            displayTable.insertRow(array, 2);
        }

        protected void testFullInsertRow() {}

        protected void testUpdateRow() {}

        protected void testUpdateRowArrayObject() {}

        protected void testUpdateRowTableCell() {}

        protected void testUpdateRowArrayNumber() {}

        protected void testRemoveRowByValue() {}

        protected void testRemoveRowByData() {}

        protected void testRemoveRowByColData() {}

        protected void testRemoveRowByCollection() {}

        private void printLine(String[] array) {
            StringBuilder buffer = new StringBuilder();
            for (String element : array) {
                buffer.append(element);
                buffer.append(" } ");
            }
            outputEditor.setText(buffer.toString());
        }

        private void printLine(int number) {
            outputEditor.setText(String.valueOf(number));
        }

        private void printLine(Object object) {
            if (object == null) {
                outputEditor.setText("Null");
            } else {
                outputEditor.setText(object.toString());
            }
        }
    }

    /****************************************************************************************************
     *
     * INNER CLASS TABLE PANEL
     *
     ***************************************************************************************************/

    private class TestTableRowDisplayer implements TableRowDisplayer {

        private String[] rowHeaders = new String[0];
        private int[] types = { DataTypeConstants.TEXT, DataTypeConstants.INTEGER_RIGHT, DataTypeConstants.DATE, DataTypeConstants.ICON };
        private int[] sizes = { -1, 0, 0, -1 };

        public TestTableRowDisplayer(String[] headerArray) {
            rowHeaders = headerArray;
        }

        public String[] getHeaders() {
            return rowHeaders;
        }

        public int[] getColumnTypes() {
            return types;
        }

        public int[] getColumnSizes() {
            return sizes;
        }

        public String[] buildRow(Object object) {
            String[] row = new String[rowHeaders.length];
            row[0] = object.toString();
            row[1] = String.valueOf(object.toString().length());
            row[2] = SimDateUtil.getCurrentDate().toString();
            row[3] = Boolean.FALSE.toString();
            return row;
        }
    }

    /****************************************************************************************************
     *
     * INNER CLASS DATA OBJECT
     *
     ***************************************************************************************************/

    private class DataObject {

        private String description = "";

        public DataObject(String text) {
            setDescription(text);
        }

        public void setDescription(String text) {
            description = text;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }
}
