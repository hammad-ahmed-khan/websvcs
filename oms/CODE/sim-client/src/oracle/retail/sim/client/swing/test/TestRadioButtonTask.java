package oracle.retail.sim.client.swing.test;

import java.awt.GridLayout;
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
import oracle.retail.sim.client.swing.widget.RRadioButton;

/********************************************************************************************************
 * Test Radion Button Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestRadioButtonTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 9104404170478353414L;

    private TestRadioButtonPanel tabContentPanel = new TestRadioButtonPanel("Test Radio Button");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestRadioButtonTask() {
        setTaskTitle("Test Radio Button");
    }

    public TestRadioButtonTask(String title) {
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

    private class TestRadioButtonPanel extends RContentPanel {
        private static final long serialVersionUID = -3601374844995053609L;

        private RRadioButton radioButton1 = new RRadioButton("Test One");
        private RRadioButton radioButton2 = new RRadioButton("Test Two");

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(2);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestRadioButtonPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            radioButton2.setEnabled(false);

            bottomLabel.setText("Just holding a space for visual reasons...");
        }

        private void layoutContents() {
            topPanel.add(radioButton1);
            topPanel.add(radioButton2);

            botPanel.setLayout(new GridLayout(1, 1));
            botPanel.add(bottomLabel);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
