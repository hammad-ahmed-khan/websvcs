package oracle.retail.sim.client.swing.test;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RDateField;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * Test Date Field Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestDateFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 3424245345086402747L;

    private TestCalendarFieldPanel tabContentPanel = new TestCalendarFieldPanel("Test Date Field");

    private static final String DONE = "Done";
    private static final String TEST = "Test";

    private RButton testButton = new RButton(TEST);
    private RButton exitButton = new RButton(DONE);

    public TestDateFieldTask() {
        setTaskTitle("Test Date Field");
    }

    public TestDateFieldTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        exitButton.registerAction(this, DONE);
        testButton.registerAction(this, TEST);
        addButton(testButton);
        addButton(exitButton);
        addContentPanel(tabContentPanel);
    }

    public void start() {
    }

    public void stop() {
    }

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
        } else if (command.equals(TEST)) {
            tabContentPanel.doTestStartDate();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestCalendarFieldPanel extends RContentPanel {
        private static final long serialVersionUID = 5915081500587410202L;

        private RDateFieldEditor calendarFieldEditor1 = new RDateFieldEditor("Test One");
        private RDateFieldEditor calendarFieldEditor2 = new RDateFieldEditor("Test Two", true);
        private RDateFieldEditor calendarFieldEditor3 = new RDateFieldEditor("Test Three");
        private RDateFieldEditor calendarFieldEditor4 = new RDateFieldEditor("Test Four", true);
        private RDateFieldEditor calendarFieldEditor5 = new RDateFieldEditor("Test Five");
        private RDateFieldEditor calendarFieldEditor6 = new RDateFieldEditor("Test Six", true);
        private RDateFieldEditor calendarFieldEditor7 = new RDateFieldEditor("Test Seven");
        private RDateFieldEditor calendarFieldEditor8 = new RDateFieldEditor("Test 8", true);
        private RDateField calendarField = new RDateField();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestCalendarFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            calendarField.setEnabled(true);
            try {
                calendarFieldEditor1.setText("07-13-2006", "3 PM");
                calendarFieldEditor2.setText("07-11-2006", "1 PM");
                calendarFieldEditor1.setIncludeTime(true);
            } catch (Throwable exception) {
                displayException(exception);
            }
            bottomLabel.setText("Just holding a space for visual reasons...");

            calendarFieldEditor1.setSizeType(EditorConstants.SMALL);
            calendarFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            calendarFieldEditor3.setSizeType(EditorConstants.LARGE);
            calendarFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            calendarFieldEditor5.setErrorState(true);
            calendarFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            calendarFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            calendarFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            calendarFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            calendarFieldEditor2.setValidStartDate(SimDateUtil.getCurrentDate());
            calendarFieldEditor4.setValidDateRange(SimDateUtil.getCurrentDate(), SimDateUtil.getCurrentDate());

            calendarFieldEditor1.setIdentifier("DateField");
            calendarFieldEditor2.setIdentifier("DateField2");
        }

        private void layoutContents() {
            topPanel.add(calendarFieldEditor1);
            topPanel.add(calendarFieldEditor2);
            topPanel.add(calendarFieldEditor3);
            topPanel.add(calendarFieldEditor4);
            topPanel.add(calendarFieldEditor7);
            topPanel.add(calendarFieldEditor8);
            topPanel.add(calendarFieldEditor5);
            topPanel.add(calendarFieldEditor6);

            botPanel.setLayout(new GridLayout(2, 1));
            botPanel.add(calendarField);
            botPanel.add(bottomLabel);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }

        public void doTestStartDate() {
            List<UIProblem> problems = new ArrayList<>();
            problems.add(new UIProblem(CommonMessageText.ACTION_INVALID));
            problems.add(new UIProblem("Hello", CommonMessageText.VALUE_NOT_VALID));
            displayException(new UIException(problems));
        }
    }
}
