package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.panel.RTableTransferPanel;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Transfer Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestTransferTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 5722202670154994314L;

    private TestTransferPanel contentPanel = new TestTransferPanel("Test Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestTransferTask() {
        setTaskTitle("Test Divider Panel");
    }

    public TestTransferTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        exitButton.registerAction(this, DONE);
        addButton(exitButton);

        contentPanel.setStretchable(true);
        addContentPanel(contentPanel);
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
        if (event.getEventCommand().equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestTransferPanel extends RContentPanel {
        private static final long serialVersionUID = -7847314394208026202L;

        private RListTransferPanel panel1 = new RListTransferPanel("Values Name");
        private RTableTransferPanel panel2 = new RTableTransferPanel("Table Values Name");
        private RDividerPanel dividerPanel = new RDividerPanel(2, 1);

        public TestTransferPanel() {
            initialize();
        }

        public TestTransferPanel(String title) {
            super(title);
            initialize();
        }

        private void initialize() {
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            String[] array1 = { "One", "Two", "Three", "Four", "Five" };
            String[] array2 = { "Three", "Four" };

            panel1.setHorizontal();
            panel1.setSelectableItems(array1);
            // panel1.setSelectedItems(array2);

            panel2.setRowDisplayer(new TestRowDisplayer());
            panel2.setSelectableItems(array1);
            panel2.setSelectedItems(array2);
        }

        private void layoutContents() {
            dividerPanel.add(panel1);
            dividerPanel.add(panel2);

            setContentPane(dividerPanel);
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestRowDisplayer implements TableRowDisplayer {

        private TestTransferModel dataModel = new TestTransferModel();
        private int rowCounter;

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
     *
     ***************************************************************************************************/

    private class TestTransferModel {

        public String[] buildDisplayableRow(Object object, int rowCounter) {
            String[] row = new String[3];
            row[0] = object.toString();
            row[1] = String.valueOf(object.toString().length() > 3);
            row[2] = String.valueOf(rowCounter);
            return row;
        }
    }
}
