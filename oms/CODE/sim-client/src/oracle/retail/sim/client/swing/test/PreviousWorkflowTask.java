package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Previous Workflow Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PreviousWorkflowTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -7933557822105613833L;

    private TestPreviousPanel tabContentPanel = new TestPreviousPanel("Previous Test Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public PreviousWorkflowTask() {
        setTaskTitle("Previous Workflow Test");
    }

    public PreviousWorkflowTask(String title) {
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

    private class TestPreviousPanel extends RContentPanel {
        private static final long serialVersionUID = -8918590358417674480L;

        private RTextFieldEditor textFieldEditor1 = new RTextFieldEditor("Test One");
        private RTextFieldEditor textFieldEditor2 = new RTextFieldEditor("Test Two", true);
        private REditorPanel topPanel = new REditorPanel(2);

        public TestPreviousPanel(String title) {
            super(title);

            textFieldEditor1.setLength(100);
            textFieldEditor2.setLength(100);

            textFieldEditor1.setSizeType(EditorConstants.SMALL);
            textFieldEditor1.setErrorState(true);
            textFieldEditor2.setSizeType(EditorConstants.MEDIUM);

            topPanel.add(textFieldEditor1);
            topPanel.add(textFieldEditor2);

            setContentPane(topPanel);
        }
    }
}
