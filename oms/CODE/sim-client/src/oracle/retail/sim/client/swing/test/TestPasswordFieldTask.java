package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RPasswordField;

/********************************************************************************************************
 * Test Password Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestPasswordFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -4158360071183433915L;

    private TestPasswordFieldPanel tabContentPanel = new TestPasswordFieldPanel("Test Password Field");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestPasswordFieldTask() {
        setTaskTitle("Test Password Field");
    }

    public TestPasswordFieldTask(String title) {
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

    private class TestPasswordFieldPanel extends RContentPanel {
        private static final long serialVersionUID = -1258120047463665028L;

        private RPasswordFieldEditor passFieldEditor1 = new RPasswordFieldEditor("Test One");
        private RPasswordFieldEditor passFieldEditor2 = new RPasswordFieldEditor("Test Two", true);
        private RPasswordFieldEditor passFieldEditor3 = new RPasswordFieldEditor("Test Three");
        private RPasswordFieldEditor passFieldEditor4 = new RPasswordFieldEditor("Test Four", true);
        private RPasswordFieldEditor passFieldEditor5 = new RPasswordFieldEditor("Test Five");
        private RPasswordFieldEditor passFieldEditor6 = new RPasswordFieldEditor("Test Six", true);
        private RPasswordFieldEditor passFieldEditor7 = new RPasswordFieldEditor("Test Seven");
        private RPasswordFieldEditor passFieldEditor8 = new RPasswordFieldEditor("Test Eigh", true);

        private RPasswordField passwordField = new RPasswordField();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestPasswordFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            passFieldEditor1.setLength(10);
            passFieldEditor2.setLength(10);
            passwordField.setLength(10);

            bottomLabel.setText("Just holding a space for visual reasons...");

            passFieldEditor1.setSizeType(EditorConstants.SMALL);
            passFieldEditor1.setErrorState(true);
            passFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            passFieldEditor3.setSizeType(EditorConstants.LARGE);
            passFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            passFieldEditor5.setErrorState(true);
            passFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            passFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            passFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            passFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);
        }

        private void layoutContents() {
            topPanel.add(passFieldEditor1);
            topPanel.add(passFieldEditor2);
            topPanel.add(passFieldEditor3);
            topPanel.add(passFieldEditor4);
            topPanel.add(passFieldEditor7);
            topPanel.add(passFieldEditor8);
            topPanel.add(passFieldEditor5);
            topPanel.add(passFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(passwordField, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
