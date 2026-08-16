package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.entrytable.REntryAttribute;
import oracle.retail.sim.client.swing.entrytable.REntryTable;
import oracle.retail.sim.client.swing.entrytable.REntryTableDefinition;
import oracle.retail.sim.client.swing.entrytable.editor.DefaultEntryTableComboBoxCreator;
import oracle.retail.sim.client.swing.entrytable.editor.DefaultEntryTableDateCreator;
import oracle.retail.sim.client.swing.entrytable.editor.DefaultEntryTableDecimalCreator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * Test Entry Table Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestEntryTableTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -1671984816517831425L;

    private TestEntryTablePanel panel = new TestEntryTablePanel();

    private static final String ADD_ITEM = "Add Item";
    private static final String DONE = "Done";

    private RButton itemButton = new RButton(ADD_ITEM);
    private RButton exitButton = new RButton(DONE);

    public TestEntryTableTask() {
        setTaskTitle("Test Entry Table");
    }

    public TestEntryTableTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        itemButton.registerAction(this, ADD_ITEM);
        exitButton.registerAction(this, DONE);

        addButton(itemButton);
        addButton(exitButton);

        addContentPanel(panel);
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

        if (command.equals(ADD_ITEM)) {
            doAddItem();
        } else if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doAddItem() {
        panel.addItem();
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private static class TestEntryTablePanel extends RContentPanel {
        private static final long serialVersionUID = 7589422802756941711L;

        private REntryTable entryTable = new REntryTable();

        public TestEntryTablePanel() {
            super("Test Entry Table");
            buildTable();
            layoutPanel();
        }

        private void buildTable() {
            entryTable.setTableDefinition(new TestDefinition());
            entryTable.setMnemonic("Test Four", 'T');
        }

        private void layoutPanel() {
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(entryTable, BorderLayout.CENTER);
        }

        public void addItem() {
            entryTable.addRow(new TestEntryTableData(SimDateUtil.getCurrentDate(), BigDecimal.valueOf(5.55), "Dude"));
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private static class TestDefinition implements REntryTableDefinition {

        public Class getDataClass() {
            return TestEntryTableData.class;
        }

        public REntryAttribute[] getAttributes() {
            List items = new ArrayList<>();
            items.add("Not Dude");
            items.add("Dude");
            items.add("Bigger Dude");

            REntryAttribute[] attributes = new REntryAttribute[4];
            attributes[0] = new REntryAttribute("Test Four", "testFour", null, null, false);
            attributes[1] = new REntryAttribute("Test One", "testOne", "TextFieldA", new DefaultEntryTableDateCreator(), false);
            attributes[2] = new REntryAttribute("Test Two", "testTwo", "TextFieldA", new DefaultEntryTableDecimalCreator(), false);
            attributes[3] = new REntryAttribute("Test Three", "testThree", null, new DefaultEntryTableComboBoxCreator(items), false);
            return attributes;
        }
    }
}
