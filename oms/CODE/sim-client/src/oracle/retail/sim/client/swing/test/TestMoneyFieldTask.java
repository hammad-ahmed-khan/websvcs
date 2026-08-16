package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.Locale;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RMoneyFieldEditor;
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
import oracle.retail.sim.client.swing.widget.RMoneyField;

/********************************************************************************************************
 * Test Money Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestMoneyFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 1025478885890776618L;

    private TestCurrencyFieldPanel tabContentPanel = new TestCurrencyFieldPanel("Test Currency Field");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestMoneyFieldTask() {
        setTaskTitle("Test Currency Field");
    }

    public TestMoneyFieldTask(String title) {
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

    private class TestCurrencyFieldPanel extends RContentPanel {
        private static final long serialVersionUID = -8912321918024584051L;

        private RMoneyFieldEditor currencyFieldEditor1 = new RMoneyFieldEditor("Test One");
        private RMoneyFieldEditor currencyFieldEditor2 = new RMoneyFieldEditor("Test Two", true);
        private RMoneyFieldEditor currencyFieldEditor3 = new RMoneyFieldEditor("Test Three");
        private RMoneyFieldEditor currencyFieldEditor4 = new RMoneyFieldEditor("Test Four", true);
        private RMoneyFieldEditor currencyFieldEditor5 = new RMoneyFieldEditor("Test Five");
        private RMoneyFieldEditor currencyFieldEditor6 = new RMoneyFieldEditor("Test Six", true);
        private RMoneyFieldEditor currencyFieldEditor7 = new RMoneyFieldEditor("Test Seven");
        private RMoneyFieldEditor currencyFieldEditor8 = new RMoneyFieldEditor("Test Eight", true);

        private RMoneyField currencyField = new RMoneyField();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestCurrencyFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            currencyFieldEditor1.setLength(20);
            currencyFieldEditor2.setLength(20);
            currencyField.setLength(20);

            bottomLabel.setText("Just holding a space for visual reasons...");

            currencyFieldEditor1.setSizeType(EditorConstants.SMALL);
            currencyFieldEditor1.setErrorState(true);
            currencyFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            currencyFieldEditor3.setSizeType(EditorConstants.LARGE);
            currencyFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            currencyFieldEditor5.setErrorState(true);
            currencyFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            currencyFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            currencyFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            currencyFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            currencyFieldEditor2.setIdentifier("TestnameX");
            currencyFieldEditor2.setLength(20);
            currencyFieldEditor2.setLocale(Locale.UK);
        }

        private void layoutContents() {
            topPanel.add(currencyFieldEditor1);
            topPanel.add(currencyFieldEditor2);
            topPanel.add(currencyFieldEditor3);
            topPanel.add(currencyFieldEditor4);
            topPanel.add(currencyFieldEditor7);
            topPanel.add(currencyFieldEditor8);
            topPanel.add(currencyFieldEditor5);
            topPanel.add(currencyFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(currencyField, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
