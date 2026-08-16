package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Next Workflow Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NextWorkflowTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -7492833541260611791L;

    private TestNextPanel tabContentPanel = new TestNextPanel("Next Workflow Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public NextWorkflowTask() {
        setTaskTitle("Next Workflow Test");
    }

    public NextWorkflowTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        exitButton.registerAction(this, DONE);
        addButton(exitButton);

        addContentPanel(tabContentPanel);
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
        if (event.getEventCommand().equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestNextPanel extends RContentPanel {
        private static final long serialVersionUID = -8288395336443804536L;

        private RDateFieldEditor calendarFieldEditor1 = new RDateFieldEditor("Test One");
        private RDateFieldEditor calendarFieldEditor2 = new RDateFieldEditor("Test Two", true);
        private REditorPanel topPanel = new REditorPanel(2);

        public TestNextPanel(String title) {
            super(title);

            calendarFieldEditor1.setSizeType(EditorConstants.SMALL);
            calendarFieldEditor1.setErrorState(true);
            calendarFieldEditor2.setSizeType(EditorConstants.MEDIUM);

            topPanel.add(calendarFieldEditor1);
            topPanel.add(calendarFieldEditor2);

            setContentPane(topPanel);
        }
    }
}
