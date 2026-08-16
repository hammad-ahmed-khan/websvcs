package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.editor.RLongFieldEditor;
import oracle.retail.sim.client.swing.editor.RMoneyFieldEditor;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RExpandablePanel;
import oracle.retail.sim.client.swing.widget.RButton;

/*********************************************************************************************
 * Test Expandable Panel Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class TestExpandPanelTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 4601790433096594092L;

    private TestExpandPanelPanel tabContentPanel = new TestExpandPanelPanel("Test Panel");

    private static final String ADD_CONTENT = "Add Content Panels";
    private static final String DONE = "Done";

    private RButton expandButton = new RButton(ADD_CONTENT);
    private RButton exitButton = new RButton(DONE);

    public TestExpandPanelTask() {
        setTaskTitle("Test Divider Panel");
    }

    public TestExpandPanelTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        expandButton.registerAction(this, ADD_CONTENT);
        exitButton.registerAction(this, DONE);

        addButton(expandButton);
        addButton(exitButton);

        tabContentPanel.setStretchable(true);
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

    //     private ActionListener createExpandAction() {
    //        return new ActionListener() {
    //            public void actionPerformed(ActionEvent event) {
    //                TestDividerPanel tabContentPanel2 = new TestDividerPanel("Test Panel 2");
    //                TestDividerPanel tabContentPanel3 = new TestDividerPanel("Test Panel 3");
    //
    //                tabContentPanel2.setStretchable(false);
    //                tabContentPanel3.setStretchable(false);
    //
    //                addContentPanel(tabContentPanel2);
    //                addContentPanel(tabContentPanel3);
    //
    //                expandButton.setEnabled(false);
    //            }
    //        };
    //    }

    public void performActionEvent(RActionEvent event) {
        if (event.getEventCommand().equals(DONE)) {
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /*********************************************************************************************
     *
     *********************************************************************************************/

    private class TestExpandPanelPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = -1953833516282845960L;

        private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo Box Test", true);
        private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Replace Combo Box Test");
        private RListEditor listEditor = new RListEditor("List Test");
        private RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long Field Test");
        private RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password Field Test", true);
        private RTextAreaEditor textAreaEditor = new RTextAreaEditor("Text Area Test");
        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field Test");
        private RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Currency Field Test");
        private RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Calendar Field Test");
        private RDateFieldEditor replacementFieldEditor = new RDateFieldEditor("Replacement Field");

        private RTextFieldEditor textFieldEditorE1 = new RTextFieldEditor("Text Field E1");
        private RTextFieldEditor textFieldEditorE2 = new RTextFieldEditor("Text Field E2");
        private RTextFieldEditor textFieldEditorE3 = new RTextFieldEditor("Text Field E2");

        private REditorPanel panel1 = new REditorPanel(3, 1);
        private REditorPanel panel2 = new REditorPanel(3, 1);
        private REditorPanel panel3 = new REditorPanel(2, 1);
        private REditorPanel panel4 = new REditorPanel(5);
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);
        private RExpandablePanel expandPanel = new RExpandablePanel("Expandable Panel");

        private static final String REPLACEMENT_ACTION = "ReplacementAction";

        public TestExpandPanelPanel() {
            initialize();
        }

        public TestExpandPanelPanel(String title) {
            super(title);
            initialize();
        }

        private void initialize() {
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            longFieldEditor.setLength(Integer.MAX_VALUE);
            textAreaEditor.setLength(Integer.MAX_VALUE);
            passwordFieldEditor.setLength(20);
            textFieldEditor.setLength(35);
            currencyFieldEditor.setLength(20);
            checkBoxEditor.registerAction(this, REPLACEMENT_ACTION);
        }

        private void layoutContents() {
            panel1.add(comboBoxEditor);
            panel1.add(checkBoxEditor);
            panel1.add(listEditor);

            panel2.add(longFieldEditor);
            panel2.add(passwordFieldEditor);
            panel2.add(textAreaEditor);

            panel3.add(textFieldEditor);

            panel4.add(currencyFieldEditor);
            panel4.add(calendarFieldEditor);
            panel4.add(textFieldEditorE1);
            panel4.add(textFieldEditorE2);
            panel4.add(textFieldEditorE3);

            expandPanel.setContentPane(panel4);

            dividerPanel.add(panel1);
            dividerPanel.add(panel2);
            dividerPanel.add(panel3);
            dividerPanel.add(expandPanel);

            setContentPane(dividerPanel);
        }

        public void performActionEvent(RActionEvent event) {
            if (checkBoxEditor.isSelected()) {
                panel1.replace(comboBoxEditor, replacementFieldEditor);
            } else {
                panel1.replace(replacementFieldEditor, comboBoxEditor);
            }
        }
    }
}
