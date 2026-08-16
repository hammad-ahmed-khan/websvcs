package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.Locale;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDecimalFieldEditor;
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
 * Test Decimal Field Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestDecimalFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -719023312302356716L;

    private TestDecimalFieldPanel tabContentPanel = new TestDecimalFieldPanel("Test Decimal Field");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestDecimalFieldTask() {
        setTaskTitle("Test Decimal Field");
    }

    public TestDecimalFieldTask(String title) {
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
            closeTask();
        }
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestDecimalFieldPanel extends RContentPanel {
        private static final long serialVersionUID = 1928576804378360850L;

        private RDecimalFieldEditor decimalFieldEditor1 = new RDecimalFieldEditor("Test One");
        private RDecimalFieldEditor decimalFieldEditor2 = new RDecimalFieldEditor("Test Two", true);
        private RDecimalFieldEditor decimalFieldEditor3 = new RDecimalFieldEditor("Test Three");
        private RDecimalFieldEditor decimalFieldEditor4 = new RDecimalFieldEditor("Test Four", true);
        private RDecimalFieldEditor decimalFieldEditor5 = new RDecimalFieldEditor("Test Five");
        private RDecimalFieldEditor decimalFieldEditor6 = new RDecimalFieldEditor("Test Six", true);
        private RDecimalFieldEditor decimalFieldEditor7 = new RDecimalFieldEditor("Test Seven");
        private RDecimalFieldEditor decimalFieldEditor8 = new RDecimalFieldEditor("Test Eight", true);

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestDecimalFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            decimalFieldEditor1.setLength(20);
            decimalFieldEditor2.setLength(20);

            bottomLabel.setText("Just holding a space for visual reasons...");

            decimalFieldEditor1.setSizeType(EditorConstants.SMALL);
            decimalFieldEditor1.setErrorState(true);
            decimalFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            decimalFieldEditor3.setSizeType(EditorConstants.LARGE);
            decimalFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            decimalFieldEditor5.setErrorState(true);
            decimalFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            decimalFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            decimalFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            decimalFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            decimalFieldEditor2.setIdentifier("TestnameX");
            decimalFieldEditor2.setLength(20);
            decimalFieldEditor2.setLocale(Locale.UK);
        }

        private void layoutContents() {
            topPanel.add(decimalFieldEditor1);
            topPanel.add(decimalFieldEditor2);
            topPanel.add(decimalFieldEditor3);
            topPanel.add(decimalFieldEditor4);
            topPanel.add(decimalFieldEditor7);
            topPanel.add(decimalFieldEditor8);
            topPanel.add(decimalFieldEditor5);
            topPanel.add(decimalFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
