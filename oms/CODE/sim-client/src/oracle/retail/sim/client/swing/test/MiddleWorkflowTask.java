package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Auto Navigation Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MiddleWorkflowTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -4443473914504504408L;

    private TestMiddlePanel tabContentPanel = new TestMiddlePanel("Middle Workflow Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public MiddleWorkflowTask() {
        setTaskTitle("Middle Workflow Test");
    }

    public MiddleWorkflowTask(String title) {
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

    private class TestMiddlePanel extends RContentPanel {
        private static final long serialVersionUID = -7244312193116863142L;

        private RComboBoxEditor comboBoxEditor1 = new RComboBoxEditor("Test One");
        private RComboBoxEditor comboBoxdEditor2 = new RComboBoxEditor("Test Two", true);
        private REditorPanel topPanel = new REditorPanel(2);

        public TestMiddlePanel(String title) {
            super(title);

            comboBoxEditor1.setSizeType(EditorConstants.SMALL);
            comboBoxEditor1.setErrorState(true);
            comboBoxdEditor2.setSizeType(EditorConstants.MEDIUM);

            topPanel.add(comboBoxEditor1);
            topPanel.add(comboBoxdEditor2);

            setContentPane(topPanel);
        }
    }
}
