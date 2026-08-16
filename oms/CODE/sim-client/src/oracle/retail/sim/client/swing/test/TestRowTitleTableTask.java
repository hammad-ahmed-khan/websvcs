package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.rowtitletable.RowTitleTable;
import oracle.retail.sim.client.swing.rowtitletable.RowTitleTableCell;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Row Title Table Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestRowTitleTableTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -3996162767310828641L;

    private TestRowTitleTablePanel contentPanel = new TestRowTitleTablePanel("Display Table Test Panel");

    private static final String LOAD = "Load";
    private static final String DONE = "Done";

    private RButton loadButton = new RButton(LOAD);
    private RButton exitButton = new RButton(DONE);

    private static final String TEST_SELECTED_CELL = "Test Selected Cell";

    private RButton test01Button = new RButton(TEST_SELECTED_CELL);

    public TestRowTitleTableTask() {
        setTaskTitle("Test Row Title Table");
    }

    public TestRowTitleTableTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        test01Button.registerAction(this, TEST_SELECTED_CELL);

        loadButton.registerAction(this, LOAD);
        exitButton.registerAction(this, DONE);

        addButton(loadButton);
        // addButton(test01Button);
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
        } else if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TABLE PANEL
     ***************************************************************************************************/

    private class TestRowTitleTablePanel extends RContentPanel {
        private static final long serialVersionUID = -8471563681007972920L;

        private RowTitleTable titleTable = new RowTitleTable();

        private String[] rowHeaders = { "Row One", "Row Two", "Row Three" };
        private String[] columnHeaders = { "Column One", "Column Two", "Column Three" };

        public TestRowTitleTablePanel(String title) {
            super(title);

            RPanel mainPanel = new RPanel(new BorderLayout());
            mainPanel.add(titleTable, BorderLayout.CENTER);
            setContentPane(mainPanel);
        }

        public void loadTable() {
            titleTable.setRowHeaders(rowHeaders);
            titleTable.setColumnHeaders(columnHeaders);

            RowTitleTableCell[][] grid = new RowTitleTableCell[3][3];
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    String[] array = { String.valueOf(x), String.valueOf(y) };
                    grid[x][y] = new RowTitleTableCell(array, String.valueOf(x + y));
                }
            }
            titleTable.refreshTable(grid);
        }
    }
}
