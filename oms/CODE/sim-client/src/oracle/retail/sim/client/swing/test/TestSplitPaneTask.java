package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JSplitPane;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.panel.RSplitPane;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Test Split Pane Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestSplitPaneTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 504166732694182717L;

    private TestSplitPanePanel tabContentPanel = new TestSplitPanePanel("Test Split Pane");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestSplitPaneTask() {
        setTaskTitle("Test Split Pane");
    }

    public TestSplitPaneTask(String title) {
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

    private class TestSplitPanePanel extends RContentPanel {
        private static final long serialVersionUID = -1571430064564846808L;

        private RLabel helloLabel = new RLabel("Hello");
        private RLabel goodbyeLabel = new RLabel("Goodbye");
        private RSplitPane splitPane = new RSplitPane(JSplitPane.VERTICAL_SPLIT);

        public TestSplitPanePanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            helloLabel.setBackground(Color.CYAN);
            helloLabel.setOpaque(true);
            goodbyeLabel.setBackground(Color.GREEN);
            goodbyeLabel.setOpaque(true);

            splitPane.setDividerSize(6);
            splitPane.setLeftComponent(helloLabel);
            splitPane.setRightComponent(goodbyeLabel);
        }

        private void layoutContents() {
            RPanel mainPanel = new RPanel(new BorderLayout());
            mainPanel.add(splitPane, BorderLayout.CENTER);
            setContentPane(mainPanel);
        }
    }
}
