package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RButtonTextFieldEditor;
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
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * Test Button Text Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestButtonTextFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -5684719372279237537L;

    private TestButtonTextFieldPanel tabContentPanel = new TestButtonTextFieldPanel("Test Button Text Field");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestButtonTextFieldTask() {
        setTaskTitle("Test Button Text Field");
    }

    public TestButtonTextFieldTask(String title) {
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
        String command = event.getEventCommand();
        if (command.equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestButtonTextFieldPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = -3730731788501640795L;

        private RButtonTextFieldEditor editor1 = new RButtonTextFieldEditor("Test One");
        private RButtonTextFieldEditor editor2 = new RButtonTextFieldEditor("Test Two", true);
        private RButtonTextFieldEditor editor3 = new RButtonTextFieldEditor("Test Three");
        private RButtonTextFieldEditor editor4 = new RButtonTextFieldEditor("Test Four", true);
        private RButtonTextFieldEditor editor5 = new RButtonTextFieldEditor("Test Five");
        private RButtonTextFieldEditor editor6 = new RButtonTextFieldEditor("Test Six", true);
        private RButtonTextFieldEditor editor7 = new RButtonTextFieldEditor("Test Seven");
        private RButtonTextFieldEditor editor8 = new RButtonTextFieldEditor("Test Eight", true);

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestButtonTextFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            editor1.setIdentifier("HALO1");
            editor1.setLength(Integer.MAX_VALUE);

            editor2.setLength(Integer.MAX_VALUE);

            bottomLabel.setText("Just holding a space for visual reasons...");

            editor1.setSizeType(EditorConstants.SMALL);
            editor2.setSizeType(EditorConstants.MEDIUM);
            editor3.setSizeType(EditorConstants.LARGE);
            editor5.setTitleAlignment(EditorConstants.RIGHT);
            editor5.setErrorState(true);
            editor6.setTitleAlignment(EditorConstants.RIGHT);
            editor6.setSizeType(EditorConstants.MEDIUM);
            editor7.setTitleAlignment(EditorConstants.TOP);
            editor8.setTitleAlignment(EditorConstants.BOTTOM);

            editor1.registerAction(this, "Something");
        }

        private void layoutContents() {
            topPanel.add(editor1);
            topPanel.add(editor2);
            topPanel.add(editor3);
            topPanel.add(editor4);
            topPanel.add(editor7);
            topPanel.add(editor8);
            topPanel.add(editor5);
            topPanel.add(editor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);

            editor2.setText("Hello WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" + "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
            editor3.setText("Hello WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" + "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        }

        public void performActionEvent(RActionEvent event) {
            LogService.info(this, editor1.getTitle() + " was pressed!");
        }
    }
}
