package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.Locale;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
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
 * Test Integer Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestIntegerFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -6590714659022225371L;

    private TestIntegerFieldPanel tabContentPanel = new TestIntegerFieldPanel("Test Integer Field");
    private static final String DONE = "Done";
    private RButton exitButton = new RButton(DONE);

    public TestIntegerFieldTask() {
        setTaskTitle("Test Integer Field");
    }

    public TestIntegerFieldTask(String title) {
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

    private class TestIntegerFieldPanel extends RContentPanel {
        private static final long serialVersionUID = -2489783396334103957L;

        private RIntegerFieldEditor integerFieldEditor1 = new RIntegerFieldEditor("Test One");
        private RIntegerFieldEditor integerFieldEditor2 = new RIntegerFieldEditor("Test Two", true);
        private RIntegerFieldEditor integerFieldEditor3 = new RIntegerFieldEditor("Test Three");
        private RIntegerFieldEditor integerFieldEditor4 = new RIntegerFieldEditor("Test Four", true);
        private RIntegerFieldEditor integerFieldEditor5 = new RIntegerFieldEditor("Test Five");
        private RIntegerFieldEditor integerFieldEditor6 = new RIntegerFieldEditor("Test Six", true);
        private RIntegerFieldEditor integerFieldEditor7 = new RIntegerFieldEditor("Test Seven");
        private RIntegerFieldEditor integerFieldEditor8 = new RIntegerFieldEditor("Test Eight", true);

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestIntegerFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            integerFieldEditor1.setLength(20);
            integerFieldEditor2.setLength(20);

            bottomLabel.setText("Just holding a space for visual reasons...");

            integerFieldEditor1.setSizeType(EditorConstants.SMALL);
            integerFieldEditor1.setErrorState(true);
            integerFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            integerFieldEditor3.setSizeType(EditorConstants.LARGE);
            integerFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            integerFieldEditor5.setErrorState(true);
            integerFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            integerFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            integerFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            integerFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            integerFieldEditor2.setIdentifier("TestnameX");
            integerFieldEditor2.setLength(20);
            integerFieldEditor2.setLocale(Locale.UK);
        }

        private void layoutContents() {
            topPanel.add(integerFieldEditor1);
            topPanel.add(integerFieldEditor2);
            topPanel.add(integerFieldEditor3);
            topPanel.add(integerFieldEditor4);
            topPanel.add(integerFieldEditor7);
            topPanel.add(integerFieldEditor8);
            topPanel.add(integerFieldEditor5);
            topPanel.add(integerFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
