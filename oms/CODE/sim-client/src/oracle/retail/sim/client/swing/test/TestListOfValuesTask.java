package oracle.retail.sim.client.swing.test;

import java.util.Arrays;
import java.util.Collection;
import oracle.retail.sim.client.swing.displaytable.DefaultTableRowDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.lov.DefaultListOfValuesDisplayer;
import oracle.retail.sim.client.swing.lov.DefaultListOfValuesModel;
import oracle.retail.sim.client.swing.lov.DefaultListOfValuesPageModel;
import oracle.retail.sim.client.swing.lov.ListOfValuesDisplayer;
import oracle.retail.sim.client.swing.lov.ListOfValuesPageModel;
import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;
import oracle.retail.sim.client.swing.lov.SelectableCriteria;
import oracle.retail.sim.client.swing.lov.SelectableResults;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test List of Values Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestListOfValuesTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -2330963757511623267L;

    private TestTableOfValuesPanel tabContentPanel = new TestTableOfValuesPanel("Test Table Of Values");

    private static final String DONE = "Done";
    private static final String TEST = "Test";

    private RButton testButton = new RButton(TEST);
    private RButton exitButton = new RButton(DONE);

    public TestListOfValuesTask() {
        setTaskTitle("Test Table Of Values");
    }

    public TestListOfValuesTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        testButton.registerAction(this, TEST);
        exitButton.registerAction(this, DONE);
        // addButton(testButton);
        addButton(exitButton);
        addContentPanel(tabContentPanel);
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

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(TEST)) {
            tabContentPanel.doTest();
        } else if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestTableOfValuesPanel extends RContentPanel {
        private static final long serialVersionUID = 5383739151092561806L;

        private TestScreenModel screenModel = new TestScreenModel();

        private RListOfValuesEditor valuesEditor1 = new RListOfValuesEditor("Test One");
        private RListOfValuesEditor valuesEditor2 = new RListOfValuesEditor("Test Two", true);
        private RListOfValuesEditor valuesEditor3 = new RListOfValuesEditor("Test Three", true);
        private RListOfValuesEditor valuesEditor4 = new RListOfValuesEditor("Test Four", true);
        private RListOfValuesEditor valuesEditor5 = new RListOfValuesEditor("Test Five");
        private RListOfValuesEditor valuesEditor6 = new RListOfValuesEditor("Test Six");
        private RListOfValuesEditor valuesEditor7 = new RListOfValuesEditor("Test Seven", true);
        private RListOfValuesEditor valuesEditor8 = new RListOfValuesEditor("Test Eight", true);
        private RListOfValuesEditor valuesEditor9 = new RListOfValuesEditor("Test Nine");
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(9);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestTableOfValuesPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            String name = "Name";
            String dept = "Departments";
            String length = "Length";
            String[] attributeNames = { name, "TooLong", length };

            valuesEditor1.setSelectionPageModel(new TestTOVModel());
            valuesEditor1.setSelectionDisplayer(new TestTOVDisplayer());
            valuesEditor1.setTableRowDisplayer(new TestTableRowDisplayer());
            valuesEditor1.setSingleValueMode();
            valuesEditor1.setHorizontalAlignment();
            valuesEditor1.setPopupWidth(600);

            valuesEditor2.setSelectionPageModel(new DefaultListOfValuesPageModel(screenModel, dept, name));
            valuesEditor2.setSelectionDisplayer(new DefaultListOfValuesDisplayer(name, length));
            valuesEditor2.setTableRowDisplayer(new DefaultTableRowDisplayer(TestListOfValuesData.class, attributeNames));

            valuesEditor3.setSelectionModel(new DefaultListOfValuesModel(screenModel, dept, name));
            valuesEditor3.setSelectionDisplayer(new DefaultListOfValuesDisplayer(name, length));
            valuesEditor3.setTableRowDisplayer(new DefaultTableRowDisplayer(TestListOfValuesData.class, attributeNames));

            valuesEditor4.setSelectionPageModel(new TestTOVModel());
            valuesEditor4.setSelectionDisplayer(new TestTOVDisplayer());
            valuesEditor4.setTableRowDisplayer(new TestTableRowDisplayer());
            valuesEditor4.setSingleValueMode();

            valuesEditor5.setSelectionPageModel(new TestTOVModel());
            valuesEditor5.setSelectionDisplayer(new TestTOVDisplayer());
            valuesEditor5.setTableRowDisplayer(new TestTableRowDisplayer());

            bottomLabel.setText("Just holding a space for visual reasons...");

            valuesEditor1.setSizeType(EditorConstants.SMALL);
            // valuesEditor1.setErrorState(true);
            valuesEditor2.setSizeType(EditorConstants.MEDIUM);
            valuesEditor4.setSizeType(EditorConstants.LARGE);
            valuesEditor6.setTitleAlignment(EditorConstants.RIGHT);
            valuesEditor6.setErrorState(true);
            valuesEditor7.setTitleAlignment(EditorConstants.RIGHT);
            valuesEditor7.setSizeType(EditorConstants.MEDIUM);
            valuesEditor8.setTitleAlignment(EditorConstants.TOP);
            valuesEditor9.setTitleAlignment(EditorConstants.BOTTOM);

            valuesEditor1.setIdentifier("ListOfValuesField");
            valuesEditor2.setIdentifier("ListOfValuesField");
            valuesEditor3.setIdentifier("ListOfValuesField");
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
            topPanel.add(valuesEditor9);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(bottomLabel, 1, 1);

            setContentPane(dividerPanel);
        }

        public void doTest() {
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestTOVModel implements ListOfValuesPageModel {

        private String[] array1 = { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8", "Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15" };

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

    private class TestTOVDisplayer implements ListOfValuesDisplayer {

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

    private class TestTableRowDisplayer implements TableRowDisplayer {

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
