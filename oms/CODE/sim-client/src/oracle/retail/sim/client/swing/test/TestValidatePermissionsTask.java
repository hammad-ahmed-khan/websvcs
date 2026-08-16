package oracle.retail.sim.client.swing.test;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
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
import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Validate Permissions Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestValidatePermissionsTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -2614029083006328409L;

    private TestPermissionsPanel tabContentPanel = new TestPermissionsPanel("Retail Editor Permission Test");

    private static final String DONE = "Done";
    private RButton exitButton = new RButton(DONE);

    private static final String TEST = "Test";
    private RButton testButton = new RButton(TEST);

    public TestValidatePermissionsTask() {
        setTaskTitle("Validate Permission Test");
    }

    public TestValidatePermissionsTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        testButton.registerAction(this, TEST);
        exitButton.registerAction(this, DONE);
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
        if (event.getEventCommand().equals(DONE)) {
            doDone();
        } else if (event.getEventCommand().equals(TEST)) {
            tabContentPanel.performTest();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestPermissionsPanel extends RContentPanel {
        private static final long serialVersionUID = -6442344288214527646L;

        private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo Box Test", true);
        private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Replace Combo Box Test");
        private RListEditor listEditor = new RListEditor("List Test");
        private RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long Field Test");
        private RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password Field Test", true);
        private RTextAreaEditor textAreaEditor = new RTextAreaEditor("Text Area Test");
        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field Test");
        private RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Currency Field Test");
        private RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Calendar Field Test");
        private RListOfValuesEditor tableOfValuesEditor = new RListOfValuesEditor("Test Table Of Values");

        private REditorPanel panel1 = new REditorPanel(7);

        public TestPermissionsPanel(String title) {
            super(title);
            initialize();
        }

        private void initialize() {
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            comboBoxEditor.setIdentifier("Combo Box");
            checkBoxEditor.setIdentifier("Check Box");
            listEditor.setIdentifier("List");
            longFieldEditor.setIdentifier("Long Field");
            passwordFieldEditor.setIdentifier("Password Field");
            textAreaEditor.setIdentifier("Text Area");
            textFieldEditor.setIdentifier("Text Field");
            currencyFieldEditor.setIdentifier("Currency Field");
            calendarFieldEditor.setIdentifier("Calendar Field");
            tableOfValuesEditor.setIdentifier("LOV Field");
        }

        private void layoutContents() {
            panel1.add(comboBoxEditor);
            panel1.add(checkBoxEditor);
            panel1.add(listEditor);
            panel1.add(longFieldEditor);
            panel1.add(passwordFieldEditor);
            panel1.add(textAreaEditor);
            panel1.add(textFieldEditor);

            RPanel panel2 = new RPanel(new GridBagLayout());
            panel2.add(currencyFieldEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 3, 0));
            panel2.add(calendarFieldEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 3, 0));
            panel2.add(tableOfValuesEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 3, 0));
            panel2.add(new JLabel(), GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 3, 0));

            List editorList = new ArrayList<>();
            editorList.add(currencyFieldEditor);
            editorList.add(calendarFieldEditor);
            editorList.add(tableOfValuesEditor);

            LayoutUtility.alignEditors(editorList);

            RPanel mainPanel = new RPanel(new GridBagLayout());
            mainPanel.add(panel1, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            mainPanel.add(panel2, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

            setContentPane(mainPanel);
        }

        public void performTest() {
            try {
                validatePermissions();
            } catch (UIException exception) {
                displayException(exception);
            }
        }
    }
}
