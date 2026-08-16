package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Auto Navigation Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestAutoNavigationTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -1529141647680066671L;

    private RContentPanel contentPanel = new RContentPanel("Auto Navigation Panel");

    private static final String TEST = "Test";
    private static final String DONE = "Done";

    private RButton testButton = new RButton(TEST);
    private RButton exitButton = new RButton(DONE);

    public TestAutoNavigationTask() {
        setTaskTitle("Test Auto Navigationt");
    }

    public TestAutoNavigationTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        testButton.registerAction(this, TEST);
        exitButton.registerAction(this, DONE);

        addButton(testButton);
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
        if (command.equals(DONE)) {
            doDone();
        } else if (command.equals(TEST)) {
            doTest();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    private void doTest() {
        ApplicationInternal.navigate(TestMoneyFieldTask.class.getName());
    }
}
