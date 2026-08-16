package oracle.retail.sim.client.swing.test;

import java.awt.GridLayout;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RCheckBox;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Test Check Box Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestCheckBoxTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -3282639286973814040L;

    private TestCheckBoxPanel tabContentPanel = new TestCheckBoxPanel("Test Check Box");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestCheckBoxTask() {
        setTaskTitle("Test CheckBox Field");
    }

    public TestCheckBoxTask(String title) {
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

    private class TestCheckBoxPanel extends RContentPanel {
        private static final long serialVersionUID = -5325029860644626232L;

        private RCheckBoxEditor checkBoxEditor1 = new RCheckBoxEditor("Test One");
        private RCheckBoxEditor checkBoxEditor2 = new RCheckBoxEditor("Test Two", true);
        private RCheckBoxEditor checkBoxEditor3 = new RCheckBoxEditor("Test Three");
        private RCheckBoxEditor checkBoxEditor4 = new RCheckBoxEditor("Test Four", true);
        private RCheckBoxEditor checkBoxEditor5 = new RCheckBoxEditor("Test Five");
        private RCheckBoxEditor checkBoxEditor6 = new RCheckBoxEditor("Test Six", true);
        private RCheckBoxEditor checkBoxEditor7 = new RCheckBoxEditor("Test Seven");
        private RCheckBoxEditor checkBoxEditor8 = new RCheckBoxEditor("Test Eight", true);

        private RCheckBox checkBox = new RCheckBox("This is a solo test.");
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestCheckBoxPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            checkBoxEditor1.setSizeType(EditorConstants.SMALL);
            checkBoxEditor1.setErrorState(true);
            checkBoxEditor2.setSizeType(EditorConstants.MEDIUM);
            checkBoxEditor3.setSizeType(EditorConstants.LARGE);
            checkBoxEditor5.setTitleAlignment(EditorConstants.RIGHT);
            checkBoxEditor5.setErrorState(true);
            checkBoxEditor6.setTitleAlignment(EditorConstants.RIGHT);
            checkBoxEditor6.setSizeType(EditorConstants.MEDIUM);
            checkBoxEditor7.setTitleAlignment(EditorConstants.TOP);
            checkBoxEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            bottomLabel.setText("Just holding a space for visual reasons...");
        }

        private void layoutContents() {
            topPanel.add(checkBoxEditor1);
            topPanel.add(checkBoxEditor2);
            topPanel.add(checkBoxEditor3);
            topPanel.add(checkBoxEditor4);
            topPanel.add(checkBoxEditor5);
            topPanel.add(checkBoxEditor6);
            topPanel.add(checkBoxEditor7);
            topPanel.add(checkBoxEditor8);

            botPanel.setLayout(new GridLayout(2, 1));
            botPanel.add(checkBox);
            botPanel.add(bottomLabel);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
