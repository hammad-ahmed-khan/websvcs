package oracle.retail.sim.client.swing.test;

import java.awt.Color;
import java.awt.GridBagLayout;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Test Button Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestButtonTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 2496060515870949014L;

    private TestButtonPanel tabContentPanel = new TestButtonPanel("Button Test Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestButtonTask() {
        setTaskTitle("Button Test");
    }

    public TestButtonTask(String title) {
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
     *
     ***************************************************************************************************/

    private class TestButtonPanel extends RContentPanel {
        private static final long serialVersionUID = -6619724821670211540L;

        private RButton button1 = new RButton("Test Button #1");
        private RButton button2 = new RButton("Test Button #2");
        private RButton button3 = new RButton("Test Button #3");
        private RButton button4 = new RButton("Test Button #4");
        private RButton button6 = new RButton("Test Button #6");
        private RButton button7 = new RButton("Test Button #7");
        private RArrowButton button5 = new RArrowButton(RArrowButton.EAST);

        private RLabel fiLabel = new RLabel();

        public TestButtonPanel() {
            initialize();
        }

        public TestButtonPanel(String title) {
            super(title);
            initialize();
        }

        private void initialize() {
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            button1.setBackground(Color.CYAN);
            button2.setChromeActivated(false);
            button3.setEnabled(false);
            button4.requestFocus();
        }

        private void layoutContents() {
            addButton(button6);
            addButton(button7);

            RPanel panel = getContentPane();

            panel.setLayout(new GridBagLayout());
            panel.add(button1, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            panel.add(button2, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            panel.add(button3, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            panel.add(button4, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            panel.add(button5, GridTool.constraints(0, 4, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            panel.add(fiLabel, GridTool.constraints(1, 5, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));
        }
    }
}
