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
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * Test Panel Content Modified Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestContentModifiedTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -2776509709271349696L;

    private final TestContentModifiedPanel tabContentPanel = new TestContentModifiedPanel("Retail Editor Content Modified Test");

    private static final String RESET = "Reset";
    private static final String TEST = "Test";
    private static final String DONE = "Done";

    private final RButton resetButton = new RButton(RESET);
    private final RButton testButton = new RButton(TEST);
    private final RButton exitButton = new RButton(DONE);

    public TestContentModifiedTask() {
        setTaskTitle("Content Modified Test");
    }

    public TestContentModifiedTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        resetButton.registerAction(this, RESET);
        testButton.registerAction(this, TEST);
        exitButton.registerAction(this, DONE);
        addButton(resetButton);
        addButton(testButton);
        addButton(exitButton);
        addContentPanel(tabContentPanel);
    }

    public void start() {
        tabContentPanel.performReset();
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
        } else if (command.equals(RESET)) {
            tabContentPanel.performReset();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestContentModifiedPanel extends RContentPanel {
        private static final long serialVersionUID = -5452375984656319968L;

        private final RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo Box", true);
        private final RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Check Box");
        private final RListEditor listEditor = new RListEditor("List");
        private final RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long Field");
        private final RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password Field", true);
        private final RTextAreaEditor textAreaEditor = new RTextAreaEditor("Text Area");
        private final RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field");
        private final RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Currency Field");
        private final RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Calendar Field");
        private final RListOfValuesEditor tableOfValuesEditor = new RListOfValuesEditor("Table Of Values");

        private final String value1 = "Value One";
        private final String value2 = "Value Two";
        private final String value3 = "Value Three";

        private final REditorPanel panel1 = new REditorPanel(7);

        public TestContentModifiedPanel(String title) {
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
            // tableOfValuesEditor.setIdentifier("LOV Field");
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

        public void performReset() {
            setContentModified(false);
        }

        public void performTest() {
            if (isContentModified()) {
                displayMessage(TestContentMessageText.CONTENT_MODIFIED);
            } else {
                displayMessage(TestContentMessageText.CONTENT_NOT_MODIFIED);
            }
        }
    }

    private enum TestContentMessageText implements MessageText {
        CONTENT_MODIFIED("Web Service Error"),
        CONTENT_NOT_MODIFIED("Web Service Error");

        private final String message;

        TestContentMessageText(String message) {
            this.message = message;
        }

        public String getCode() {
            return name();
        }

        public String getText() {
            return message;
        }
    }
}
