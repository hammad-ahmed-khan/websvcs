package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import oracle.retail.sim.client.swing.displaytable.DefaultTableRowDisplayer;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.client.swing.displaytable.RDisplayTablePane;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.lov.DefaultListOfValuesDisplayer;
import oracle.retail.sim.client.swing.lov.DefaultListOfValuesPageModel;
import oracle.retail.sim.client.swing.lov.ListOfValuesDisplayer;
import oracle.retail.sim.client.swing.lov.ListOfValuesPageModel;
import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;
import oracle.retail.sim.client.swing.lov.SelectableCriteria;
import oracle.retail.sim.client.swing.lov.SelectableResults;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Panel Layout Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestTaskLayoutTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -7212872525229934436L;

    private TestContentPanelOne tabContentPanel1 = new TestContentPanelOne("Test Panel One");
    private TestContentPanelOne tabContentPanel2 = new TestContentPanelOne("Test Panel One");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestTaskLayoutTask() {
        setTaskTitle("Test Table Of Values");
    }

    public TestTaskLayoutTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        tabContentPanel1.setStretchable(false);
        tabContentPanel2.setStretchable(false);
        tabContentPanel1.addDisplayTable();
        tabContentPanel2.addDisplayTable();
        tabContentPanel1.setPanelHeight(275);
        tabContentPanel2.setPanelHeight(275);
        exitButton.registerAction(this, DONE);
        addButton(exitButton);
        addContentPanel(tabContentPanel1);
        addContentPanel(tabContentPanel2);
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isStartable() {
        return true;
    }

    public boolean isStoppable() {
        return true;
    }

    public void performActionEvent(RActionEvent e) {
        String command = e.getEventCommand();
        if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestContentPanelOne extends RContentPanel {
        private static final long serialVersionUID = -3938786491231199792L;

        private TestScreenModel screenModel = new TestScreenModel();

        private RListOfValuesEditor valuesEditor1 = new RListOfValuesEditor("Test One");
        private RListOfValuesEditor valuesEditor2 = new RListOfValuesEditor("Test Two", true);
        private RListOfValuesEditor valuesEditor3 = new RListOfValuesEditor("Test Three", true);
        private RListOfValuesEditor valuesEditor4 = new RListOfValuesEditor("Test Four");
        private RListOfValuesEditor valuesEditor5 = new RListOfValuesEditor("Test Five");
        private RListOfValuesEditor valuesEditor6 = new RListOfValuesEditor("Test Six", true);
        private RListOfValuesEditor valuesEditor7 = new RListOfValuesEditor("Test Seven", true);
        private RListOfValuesEditor valuesEditor8 = new RListOfValuesEditor("Test Eight", true);
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);

        public TestContentPanelOne(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        public void addDisplayTable() {
            RDisplayTable table = new RDisplayTable("Test Layout With Table");
            RDisplayTablePane tablePane = new RDisplayTablePane(table);
            String[] headers = { "One", "Two", "Three" };
            table.setColumnHeaders(headers);

            RPanel mainPanel = new RPanel(new BorderLayout());
            mainPanel.add(tablePane, BorderLayout.CENTER);
            mainPanel.add(topPanel, BorderLayout.SOUTH);
            setContentPane(mainPanel);
        }

        private void initializeWidgets() {
            String name = "Name";
            String dept = "Departments";
            String length = "Length";
            String[] attributeNames = { name, "TooLong", length };

            valuesEditor1.setSelectionPageModel(new DefaultListOfValuesPageModel(screenModel, dept, name));
            valuesEditor1.setSelectionDisplayer(new DefaultListOfValuesDisplayer(name, length));
            valuesEditor1.setTableRowDisplayer(new DefaultTableRowDisplayer(TestListOfValuesData.class, attributeNames));

            valuesEditor2.setSelectionPageModel(new TOVSelectionModel());
            valuesEditor2.setSelectionDisplayer(new TOVSelectionDisplayer());
            valuesEditor2.setTableRowDisplayer(new TOVRowDisplayer());
            // valuesEditor2.setMultiValueMode();
            valuesEditor2.setHorizontalAlignment();
            valuesEditor2.setPopupWidth(600);

            valuesEditor3.setSelectionPageModel(new TOVSelectionModel());
            valuesEditor3.setSelectionDisplayer(new TOVSelectionDisplayer());
            valuesEditor3.setTableRowDisplayer(new TOVRowDisplayer());
            valuesEditor3.setSingleValueMode();

            valuesEditor4.setSelectionPageModel(new TOVSelectionModel());
            valuesEditor4.setSelectionDisplayer(new TOVSelectionDisplayer());
            valuesEditor4.setTableRowDisplayer(new TOVRowDisplayer());

            bottomLabel.setText("Just holding a space for visual reasons...");

            valuesEditor1.setSizeType(EditorConstants.SMALL);
            // valuesEditor1.setErrorState(true);
            valuesEditor2.setSizeType(EditorConstants.MEDIUM);
            valuesEditor3.setSizeType(EditorConstants.LARGE);
            valuesEditor5.setTitleAlignment(EditorConstants.RIGHT);
            valuesEditor5.setErrorState(true);
            valuesEditor6.setTitleAlignment(EditorConstants.RIGHT);
            valuesEditor6.setSizeType(EditorConstants.MEDIUM);
            valuesEditor7.setTitleAlignment(EditorConstants.TOP);
            valuesEditor8.setTitleAlignment(EditorConstants.BOTTOM);
        }

        private void layoutContents() {
            topPanel.add(valuesEditor1);
            topPanel.add(valuesEditor2);
            topPanel.add(valuesEditor3);
            topPanel.add(valuesEditor4);
            topPanel.add(valuesEditor5);
            topPanel.add(valuesEditor6);
            topPanel.add(valuesEditor7);
            topPanel.add(valuesEditor8);

            setContentPane(topPanel);

            valuesEditor1.setMultiValueMode();
        }

        public void doTest() {
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TOVSelectionModel implements ListOfValuesPageModel {

        private String[] array1 = { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8", "Value9", "Value10" };

        public SelectableResults getSelectableValues(SelectableCriteria criteria) throws UIException {
            return new SelectableResults(Arrays.asList(array1));
        }

        public Collection getSelectedValues(String[] values) throws UIException {
            Object[] array2 = new Object[values.length];
            int index = 0;
            for (String value : values) {
                for (String element : array1) {
                    if (value.equalsIgnoreCase(element)) {
                        array2[index++] = value;
                    }
                }
            }
            for (Object element : array2) {
                if (element == null) {
                    throw new UIException(CommonMessageText.ACTION_INVALID);
                }
            }
            return Arrays.asList(array2);
        }

        public void setSelectedValues(Collection collection) {
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TOVSelectionDisplayer implements ListOfValuesDisplayer {

        public String getEntryText(Object object) {
            return object.toString() + " Name";
        }

        public String getDescriptionText(Object object) {
            return object.toString();
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TOVRowDisplayer implements TableRowDisplayer {

        private int rowCounter;
        private TestValuesModel dataModel = new TestValuesModel();

        public String[] getHeaders() {
            String[] headers = { "Header1", "Header2", "Headers3" };
            return headers;
        }

        public int[] getColumnTypes() {
            int[] types = { DataTypeConstants.TEXT, DataTypeConstants.BOOLEAN, DataTypeConstants.INTEGER };
            return types;
        }

        public int[] getColumnSizes() {
            int[] sizes = { -1, -1, -1 };
            return sizes;
        }

        public String[] buildRow(Object object) {
            return dataModel.buildDisplayableRow(object, rowCounter++);
        }
    }

    /****************************************************************************************************
     * INNER CLASS TEST MODEL
     ***************************************************************************************************/

    private class TestValuesModel {

        public String[] buildDisplayableRow(Object object, int rowCounter) {
            String[] row = new String[3];
            row[0] = object.toString();
            row[1] = String.valueOf(object.toString().length() > 3);
            row[2] = String.valueOf(rowCounter);
            return row;
        }
    }
}
