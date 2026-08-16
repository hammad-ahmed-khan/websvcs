package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.task.RProgressFrame;
import oracle.retail.sim.client.swing.task.UIProgressTask;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Progress Frame Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestProgressFrameTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 2334558644722933051L;

    private static final String TEST = "Test";
    private static final String DONE = "Done";

    private RButton testButton = new RButton(TEST);
    private RButton exitButton = new RButton(DONE);

    public TestProgressFrameTask() {
        setTaskTitle("Test Progress Frame");
    }

    public TestProgressFrameTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        testButton.registerAction(this, TEST);
        exitButton.registerAction(this, DONE);
        addButton(testButton);
        addButton(exitButton);
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

        if (command.equals(TEST)) {
            doTestAction();
        } else if (command.equals(DONE)) {
            doDoneAction();
        }
    }

    private void doTestAction() {
        RProgressFrame frame = new RProgressFrame();
        frame.start(new TestProgressTask());
    }

    private void doDoneAction() {
        closeTask();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestProgressTask extends UIProgressTask {

        public TestProgressTask() {
            setTitle("Test Progress");
            setPermanentMessage(CommonMessageText.ACTION_INVALID);
        }

        public boolean executeRequest() {
            return true;
        }
    }
}
