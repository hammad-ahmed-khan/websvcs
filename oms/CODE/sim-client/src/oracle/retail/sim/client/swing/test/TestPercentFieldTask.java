package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.Locale;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RPercentFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Test Percent Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestPercentFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 355872072178918900L;

    private TestPercentFieldPanel tabContentPanel = new TestPercentFieldPanel("Test Percent Field Task");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestPercentFieldTask() {
        setTaskTitle("Test Percent Field");
    }

    public TestPercentFieldTask(String title) {
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
            closeTask();
        }
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestPercentFieldPanel extends RContentPanel {
        private static final long serialVersionUID = -1489812373937675701L;

        private RPercentFieldEditor percentFieldEditor1 = new RPercentFieldEditor("Test One");
        private RPercentFieldEditor percentFieldEditor2 = new RPercentFieldEditor("Test Two", true);
        private RPercentFieldEditor percentFieldEditor3 = new RPercentFieldEditor("Test Three");
        private RPercentFieldEditor percentFieldEditor4 = new RPercentFieldEditor("Test Four", true);
        private RPercentFieldEditor percentFieldEditor5 = new RPercentFieldEditor("Test Five");
        private RPercentFieldEditor percentFieldEditor6 = new RPercentFieldEditor("Test Six", true);
        private RPercentFieldEditor percentFieldEditor7 = new RPercentFieldEditor("Test Seven");
        private RPercentFieldEditor percentFieldEditor8 = new RPercentFieldEditor("Test Eight", true);

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestPercentFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            percentFieldEditor1.setLength(20);
            percentFieldEditor2.setLength(20);

            bottomLabel.setText("Just holding a space for visual reasons...");

            percentFieldEditor1.setSizeType(EditorConstants.SMALL);
            percentFieldEditor1.setErrorState(true);
            percentFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            percentFieldEditor3.setSizeType(EditorConstants.LARGE);
            percentFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            percentFieldEditor5.setErrorState(true);
            percentFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            percentFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            percentFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            percentFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            percentFieldEditor2.setIdentifier("TestnameX");
            percentFieldEditor2.setLength(20);
            percentFieldEditor2.setLocale(Locale.UK);
        }

        private void layoutContents() {
            topPanel.add(percentFieldEditor1);
            topPanel.add(percentFieldEditor2);
            topPanel.add(percentFieldEditor3);
            topPanel.add(percentFieldEditor4);
            topPanel.add(percentFieldEditor7);
            topPanel.add(percentFieldEditor8);
            topPanel.add(percentFieldEditor5);
            topPanel.add(percentFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
