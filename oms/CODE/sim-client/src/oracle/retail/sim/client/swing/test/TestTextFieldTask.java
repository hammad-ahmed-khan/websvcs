package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
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
import oracle.retail.sim.client.swing.widget.RTextField;

/********************************************************************************************************
 * Test Text Field Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestTextFieldTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -1661672831554003929L;

    private TestTextFieldPanel tabContentPanel = new TestTextFieldPanel("Test Text Field");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestTextFieldTask() {
        setTaskTitle("Test Text Field");
    }

    public TestTextFieldTask(String title) {
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

    private class TestTextFieldPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = 5052077635025472631L;

        private RTextFieldEditor textFieldEditor1 = new RTextFieldEditor("Test One");
        private RTextFieldEditor textFieldEditor2 = new RTextFieldEditor("Test Two");
        private RTextFieldEditor textFieldEditor3 = new RTextFieldEditor("Test Three");
        private RTextFieldEditor textFieldEditor4 = new RTextFieldEditor("Test Four");
        private RTextFieldEditor textFieldEditor5 = new RTextFieldEditor("Test Five");
        private RTextFieldEditor textFieldEditor6 = new RTextFieldEditor("Test Six");
        private RTextFieldEditor textFieldEditor7 = new RTextFieldEditor("Test Seven");
        private RTextFieldEditor textFieldEditor8 = new RTextFieldEditor("Test Eight");

        private RTextField textField = new RTextField();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestTextFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            textFieldEditor1.setLength(100);
            textFieldEditor2.setLength(100);
            textFieldEditor6.setLength(10);
            textField.setLength(100);

            bottomLabel.setText("Just holding a space for visual reasons...");

            textFieldEditor1.setSizeType(EditorConstants.SMALL);
            textFieldEditor1.setErrorState(true);
            textFieldEditor2.setSizeType(EditorConstants.MEDIUM);
            textFieldEditor3.setSizeType(EditorConstants.LARGE);
            textFieldEditor5.setTitleAlignment(EditorConstants.RIGHT);
            textFieldEditor5.setErrorState(true);
            textFieldEditor6.setTitleAlignment(EditorConstants.RIGHT);
            textFieldEditor6.setSizeType(EditorConstants.MEDIUM);
            textFieldEditor6.registerEmptyStateAction(this);
            textFieldEditor7.setTitleAlignment(EditorConstants.TOP);
            textFieldEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            textFieldEditor1.setIdentifier("TextFieldA");
            textFieldEditor1.registerAction(this, "Hello", KeyEvent.VK_ENTER);
            textFieldEditor6.setIdentifier("Editor 6");

            textFieldEditor1.setText("TextFieldA - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor2.setText("TextFieldB - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor3.setText("TextFieldC - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor4.setText("TextFieldD - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor5.setText("TextFieldE - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor6.setText("TextFieldF - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor7.setText("TextFieldG - adsfffffffffffffffffffffffffffffffff");
            textFieldEditor8.setText("TextFieldH - adsfffffffffffffffffffffffffffffffff");

            textFieldEditor3.setEnabled(false);
            textFieldEditor4.setEnabled(false);
        }

        private void layoutContents() {
            topPanel.add(textFieldEditor1);
            topPanel.add(textFieldEditor2);
            topPanel.add(textFieldEditor3);
            topPanel.add(textFieldEditor4);
            topPanel.add(textFieldEditor7);
            topPanel.add(textFieldEditor8);
            topPanel.add(textFieldEditor5);
            topPanel.add(textFieldEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(textField, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }

        public void performActionEvent(RActionEvent event) {
            System.out.println(event);
        }
    }
}
