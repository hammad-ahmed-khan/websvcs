package oracle.retail.sim.client.swing.test;

import java.util.Arrays;
import java.util.Collection;
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
import oracle.retail.sim.client.swing.lov.ListOfValuesDisplayer;
import oracle.retail.sim.client.swing.lov.ListOfValuesPageModel;
import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;
import oracle.retail.sim.client.swing.lov.SelectableCriteria;
import oracle.retail.sim.client.swing.lov.SelectableResults;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Divider Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestDividerTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -1544904280292451602L;

    private TestDividerPanel tabContentPanel = new TestDividerPanel("Test Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestDividerTask() {
        setTaskTitle("Test Divider Panel");
    }

    public TestDividerTask(String title) {
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
            doDone();
        }
    }

    private void doDone() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestDividerPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = 8983568825771105004L;

        private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo Box Test", true);
        private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Replace Combo Box Test");
        private RListEditor listEditor = new RListEditor("List Test");
        private RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long Field Test");
        private RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password Field Test", true);
        private RTextAreaEditor textAreaEditor = new RTextAreaEditor("Text Area Test");
        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field Test");
        private RListOfValuesEditor listValuesEditor = new RListOfValuesEditor("List Of Values Test", true);
        private RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Currency Field Test");
        private RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Calendar Field Test");
        private RDateFieldEditor replacementFieldEditor = new RDateFieldEditor("Replacement Field");

        private REditorPanel panel1 = new REditorPanel(3, 1);
        private REditorPanel panel2 = new REditorPanel(3, 1);
        private REditorPanel panel3 = new REditorPanel(2, 1);
        private REditorPanel panel4 = new REditorPanel(2, 1);
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        private static final String REPLACEMENT_ACTION = "ReplacementAction";

        public TestDividerPanel() {
            initialize();
        }

        public TestDividerPanel(String title) {
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

            listValuesEditor.setSelectionPageModel(new ListSelectionModel());
            listValuesEditor.setSelectionDisplayer(new ListSelectionDisplayer());
            listValuesEditor.setMultiValueMode();

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
            panel3.add(listValuesEditor);

            panel4.add(currencyFieldEditor);
            panel4.add(calendarFieldEditor);

            dividerPanel.add(panel1);
            dividerPanel.add(panel2);
            dividerPanel.add(panel3);
            dividerPanel.add(panel4);

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

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class ListSelectionModel implements ListOfValuesPageModel {

        private String[] array1 = { "One", "Two", "Three", "Four", "Five" };
        private String[] array2 = { "Three", "Four" };

        public SelectableResults getSelectableValues(SelectableCriteria criteria) throws UIException {
            return new SelectableResults(Arrays.asList(array1));
        }

        public Collection getSelectedValues(String[] values) {
            return Arrays.asList(array2);
        }

        public void setSelectedValues(Collection collection) {}
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class ListSelectionDisplayer implements ListOfValuesDisplayer {

        public String getEntryText(Object object) {
            return object.toString() + " Name";
        }

        public String getDescriptionText(Object object) {
            return object.toString();
        }
    }
}
