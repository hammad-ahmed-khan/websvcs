package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
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
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RLongTextField;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Long Text Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestLongTextFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -5684719372279237537L;

    private TestLongTextFieldPanel tabContentPanel = new TestLongTextFieldPanel("Test Long Field");

    private static final String ERROR = "Error";
    private static final String DONE = "Done";

    private RButton errorButton = new RButton(ERROR);
    private RButton exitButton = new RButton(DONE);

    public TestLongTextFieldTask() {
        setTaskTitle("Test Long Field Field");
    }

    public TestLongTextFieldTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        errorButton.registerAction(this, ERROR);
        exitButton.registerAction(this, DONE);

        addButton(errorButton);
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
        } else if (command.equals(ERROR)) {
            doError();
        }
    }

    private void doError() {
        tabContentPanel.displayError();
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * INNER CLASS TEST PANEL
     ***************************************************************************************************/

    private class TestLongTextFieldPanel extends RContentPanel {
        private static final long serialVersionUID = -3730731788501640795L;

        private RLongTextFieldEditor longFieldEditor1 = new RLongTextFieldEditor("Test One");
        private RLongTextFieldEditor longFieldEditor2 = new RLongTextFieldEditor("Test Two", true);
        private RLongTextFieldEditor longFieldEditor3 = new RLongTextFieldEditor("Test Three");
        private RLongTextFieldEditor longFieldEditor4 = new RLongTextFieldEditor("Test Four", true);
        private RLongTextFieldEditor longFieldEditor5 = new RLongTextFieldEditor("Test Five");
        private RLongTextFieldEditor longFieldEditor6 = new RLongTextFieldEditor("Test Six", true);
        private RLongTextFieldEditor longFieldEditor7 = new RLongTextFieldEditor("Test Seven");
        private RLongTextFieldEditor longFieldEditor8 = new RLongTextFieldEditor("Test Eight", true);

        private RLongTextField longField = new RLongTextField();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestLongTextFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            longFieldEditor1.setIdentifier("HALO1");
            longFieldEditor1.setLength(Integer.MAX_VALUE);

            longFieldEditor2.setLength(Integer.MAX_VALUE);
            // longFieldEditor2.setEnabled(false);

            longField.setLength(Integer.MAX_VALUE);

            bottomLabel.setText("Just holding a space for visual reasons...");

            longFieldEditor1.setSizeType(EditorConstants.SMALL);
            longFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            longFieldEditor3.setSizeType(EditorConstants.LARGE);
            longFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            longFieldEditor5.setErrorState(true);
            longFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            longFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            longFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            longFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);
        }

        private void layoutContents() {
            topPanel.add(longFieldEditor1);
            topPanel.add(longFieldEditor2);
            topPanel.add(longFieldEditor3);
            topPanel.add(longFieldEditor4);
            topPanel.add(longFieldEditor7);
            topPanel.add(longFieldEditor8);
            topPanel.add(longFieldEditor5);
            topPanel.add(longFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(longField, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);

            longFieldEditor2.setText("Hello WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"
                    + "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
            longFieldEditor3.setText("Hello WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"
                    + "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        }

        private void displayError() {
            List<UIProblem> problems = new ArrayList<>();
            problems.add(new UIProblem("HALO1", CommonMessageText.ACTION_INVALID));
            displayException(new UIException(problems));
        }
    }
}
