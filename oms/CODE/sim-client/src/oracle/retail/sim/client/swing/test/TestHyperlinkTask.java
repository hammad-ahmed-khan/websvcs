package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.awt.Color;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RHyperlink;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Test Hyperlink Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestHyperlinkTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 1585008167371192836L;

    private TestHyperlinkPanel tabContentPanel = new TestHyperlinkPanel("Test Hyperlink");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestHyperlinkTask() {
        setTaskTitle("Test Hyperlink");
    }

    public TestHyperlinkTask(String title) {
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

    private class TestHyperlinkPanel extends RContentPanel {
        private static final long serialVersionUID = 1456475719804644947L;

        private RHyperlink hyperlink1 = new RHyperlink("Hyperlink #1");
        private RHyperlink hyperlink2 = new RHyperlink("Hyperlink #2");
        private RHyperlink hyperlink3 = new RHyperlink("Hyperlink #3");
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(2);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestHyperlinkPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            hyperlink1.setForeground(Color.CYAN);
            hyperlink2.setForeground(Color.GREEN);
            hyperlink3.setForeground(Color.MAGENTA);

            hyperlink1.setDisabledForeground(Color.GRAY);
            hyperlink2.setDisabledForeground(Color.GRAY);
            hyperlink3.setDisabledForeground(Color.GRAY);

            hyperlink1.setSelectedForeground(Color.WHITE);
            hyperlink2.setSelectedForeground(Color.WHITE);
            hyperlink3.setSelectedForeground(Color.WHITE);

            hyperlink2.setEnabled(false);

            bottomLabel.setText("Just holding a space for visual reasons...");
        }

        private void layoutContents() {
            topPanel.add(hyperlink1);
            topPanel.add(hyperlink2);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(hyperlink3, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
