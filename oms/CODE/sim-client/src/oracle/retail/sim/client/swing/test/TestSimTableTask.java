package oracle.retail.sim.client.swing.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.Quantity;

/********************************************************************************************************
 * Test Sim Table Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestSimTableTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 8664032666159812883L;

    private TestSimTablePanel panel = new TestSimTablePanel();

    private static final String LOAD = "Load";
    private static final String DONE = "Done";

    private RButton loadButton = new RButton(LOAD);
    private RButton exitButton = new RButton(DONE);

    public TestSimTableTask() {
        setTaskTitle("Test SIM Table");
    }

    public void init() {
        loadButton.registerAction(this, LOAD);
        exitButton.registerAction(this, DONE);

        addButton(loadButton);
        addButton(exitButton);

        addContentPanel(panel);
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isStartable() throws UIException {
        return true;
    }

    public boolean isStoppable() throws UIException {
        return true;
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(LOAD)) {
            panel.load();
        } else if (command.equals(DONE)) {
            closeTask();
        }
    }

    /****************************************************************************************************
     * Test Sim Table Content Panel
     ***************************************************************************************************/

    private class TestSimTablePanel extends RContentPanel {
        private static final long serialVersionUID = -4796542643883502719L;

        private SimTable simTable = new SimTable(new TestSimTableDefinition());
        private SimTablePane simTablePane = new SimTablePane(simTable);

        public TestSimTablePanel() {
            setContentPane(simTablePane);
        }

        public void load() {
            GregorianCalendar calendar = new GregorianCalendar();
            Date date1 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date date2 = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            Date date3 = calendar.getTime();
            calendar.add(Calendar.YEAR, 1);
            Date date4 = calendar.getTime();

            simTable.addRow(new TestSimTableData("One", "A Value", date2, new Quantity("1.4"), true));
            simTable.addRow(new TestSimTableData("Two", "Z Value", date1, new Quantity("11.44"), false));
            simTable.addRow(new TestSimTableData("Three", "M Value", date3, new Quantity("8.7"), true));
            simTable.addRow(new TestSimTableData("Four", "M value", date4, new Quantity("7.8"), false));
            simTable.sort();
        }
    }

    /****************************************************************************************************
     * Test Sim Table Definition
     ***************************************************************************************************/

    private class TestSimTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TestSimTableData.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("value2", false));
        }

        public List getAttributes() {
            List attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Column One", "value1"));
            attributes.add(new SimTableAttribute("Column Two", "value2"));
            attributes.add(new SimTableAttribute("Column Three", "value3"));
            attributes.add(new SimTableAttribute("Column Four", "value4"));
            attributes.add(new SimTableAttribute("Column Five", "value5", new SimTableCheckBoxRenderer()));
            return attributes;
        }
    }
}
