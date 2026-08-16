package oracle.retail.sim.client.swing.test;

import java.awt.Component;
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
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Displaying Exception Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestDisplayExceptionTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -1782127333866934072L;

    private TestExceptionPanel tabContentPanel = new TestExceptionPanel("Retail Editor Permission Test");

    private static final String DONE = "Done";
    private RButton exitButton = new RButton(DONE);

    private static final String TEST = "Test";
    private RButton testButton = new RButton(TEST);

    public TestDisplayExceptionTask() {
        setTaskTitle("Validate Permission Test");
    }

    public TestDisplayExceptionTask(String title) {
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

    private class TestExceptionPanel extends RContentPanel {
        private static final long serialVersionUID = -6720506016151330641L;

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

        private String value1 = "Value One";
        private String value2 = "Value Two";
        private String value3 = "Value Three";

        private REditorPanel panel1 = new REditorPanel(7);

        public TestExceptionPanel(String title) {
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
            List<String> valueList = new ArrayList<>();
            valueList.add(value1);
            valueList.add(value2);
            valueList.add(value3);
            comboBoxEditor.setItems(valueList);
            listEditor.setItems(valueList);
            longFieldEditor.setLength(2000);
            passwordFieldEditor.setLength(10);
            textAreaEditor.setLength(2000);
            textFieldEditor.setLength(2000);
            currencyFieldEditor.setLength(4);
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

            List<Component> editorList = new ArrayList<>();
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
            List<UIProblem> errors = new ArrayList<>();
            errors.add(new UIProblem(CommonMessageText.ACTION_INVALID));
            errors.add(new UIProblem("Combo Box", CommonMessageText.VALUE_NOT_VALID));
            errors.add(new UIProblem("List", CommonMessageText.VALUE_NOT_WHOLE));
            errors.add(new UIProblem("Long Field", CommonMessageText.VALUE_NOT_VALID));
            errors.add(new UIProblem("Password Field", CommonMessageText.VALUE_NOT_WHOLE));
            errors.add(new UIProblem("Text Area", CommonMessageText.VALUE_NOT_VALID));
            errors.add(new UIProblem("Text Field", CommonMessageText.VALUE_NOT_WHOLE));
            errors.add(new UIProblem("Currency Field", CommonMessageText.VALUE_NOT_VALID));
            errors.add(new UIProblem("Calendar Field", CommonMessageText.VALUE_NOT_WHOLE));
            errors.add(new UIProblem("LOV Field", CommonMessageText.VALUE_NOT_VALID));

            displayException(new UIException(errors));
        }
    }
}
