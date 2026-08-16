package oracle.retail.sim.client.swing.test;

import java.awt.GridBagLayout;
import java.math.BigDecimal;
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
 * Test Validate Required Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestValidateRequiredTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 6873832392468217723L;

    private TestValidatePanel tabContentPanel = new TestValidatePanel("Retail Editor Validate Required Test");

    private static final String TEST = "Test";
    private static final String DONE = "Done";

    private RButton testButton = new RButton(TEST);
    private RButton exitButton = new RButton(DONE);

    public TestValidateRequiredTask() {
        setTaskTitle("Validate Required Test");
    }

    public TestValidateRequiredTask(String title) {
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
        String command = event.getEventCommand();
        if (command.equals(DONE)) {
            doDone();
        } else if (command.equals(TEST)) {
            tabContentPanel.performTest();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestValidatePanel extends RContentPanel {
        private static final long serialVersionUID = 9032618497697030021L;

        private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo Box", false);
        private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Check Box", false);
        private RListEditor listEditor = new RListEditor("List", false);
        private RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long Field", false);
        private RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password Field", false);
        private RTextAreaEditor textAreaEditor = new RTextAreaEditor("Text Area", false);
        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field", false);
        private RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Currency Field", false);
        private RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Calendar Field", false);
        private RListOfValuesEditor tableOfValuesEditor = new RListOfValuesEditor("Table Of Values", false);
        private RTextFieldEditor textFieldEditor2 = new RTextFieldEditor("Invisible", true);

        private String value1 = "Value One";
        private String value2 = "Value Two";
        private String value3 = "Value Three";

        private REditorPanel panel1 = new REditorPanel(7);
        private REditorPanel panel3 = new REditorPanel(1);

        public TestValidatePanel(String title) {
            super(title);
            initialize();
        }

        private void initialize() {
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            List valueList = new ArrayList<>();
            valueList.add(value1);
            valueList.add(value2);
            valueList.add(value3);
            comboBoxEditor.setItems(valueList);
            listEditor.setItems(valueList);
            longFieldEditor.setLength(2000);
            passwordFieldEditor.setLength(10);
            textAreaEditor.setLength(2000);
            textFieldEditor.setLength(2000);
            currencyFieldEditor.setMaximumValue(BigDecimal.valueOf(1000));
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

            panel3.add(textFieldEditor2);
            panel3.setVisible(false);

            List editorList = new ArrayList<>();
            editorList.add(currencyFieldEditor);
            editorList.add(calendarFieldEditor);
            editorList.add(tableOfValuesEditor);

            LayoutUtility.alignEditors(editorList);

            RPanel mainPanel = new RPanel(new GridBagLayout());
            mainPanel.add(panel1, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            mainPanel.add(panel2, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            mainPanel.add(panel3, GridTool.constraints(0, 1, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));

            setContentPane(mainPanel);
        }

        public void performTest() {
            try {
                validateRequiredContent();
            } catch (UIException exception) {
                displayException(exception);
            }
        }
    }
}
