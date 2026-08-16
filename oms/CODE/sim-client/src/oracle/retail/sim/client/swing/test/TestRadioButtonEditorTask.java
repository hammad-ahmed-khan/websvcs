package oracle.retail.sim.client.swing.test;

import java.awt.GridLayout;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
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

/********************************************************************************************************
 * Test Radion Button Editor Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestRadioButtonEditorTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 7268829808806248642L;

    private TestRadioButtonPanel tabContentPanel = new TestRadioButtonPanel("Test Radio Button");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestRadioButtonEditorTask() {
        setTaskTitle("Test Radio Button");
    }

    public TestRadioButtonEditorTask(String title) {
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

    private class TestRadioButtonPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = 2201263740420678789L;

        private RRadioButtonEditor radioButtonEditor1 = new RRadioButtonEditor("Test One");
        private RRadioButtonEditor radioButtonEditor2 = new RRadioButtonEditor("Test Two");

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(2);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);
        private String[] titleArray = { "Radio One", "Radio Two", "Radio Three" };

        public TestRadioButtonPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            radioButtonEditor1.addRadioButton("Radio One");
            radioButtonEditor1.addRadioButton("Radio Two");
            radioButtonEditor1.registerAction(this, "Hello");
            radioButtonEditor2.setRadioButtons(titleArray);
            radioButtonEditor2.setEnabled(false);

            bottomLabel.setText("Just holding a space for visual reasons...");
        }

        private void layoutContents() {
            topPanel.add(radioButtonEditor1);
            topPanel.add(radioButtonEditor2);

            botPanel.setLayout(new GridLayout(1, 1));
            botPanel.add(bottomLabel);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }

        public void performActionEvent(RActionEvent event) {
            System.out.println(event.getEventCommand());
        }
    }
}
