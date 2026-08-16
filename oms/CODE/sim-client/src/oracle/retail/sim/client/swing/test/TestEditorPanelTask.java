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
import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Editor Panel Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestEditorPanelTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -5053011425533904933L;

    private TestEditorPanel tabContentPanel = new TestEditorPanel("Test Panel");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestEditorPanelTask() {
        setTaskTitle("Test Matrix Panel");
    }

    public TestEditorPanelTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        tabContentPanel.setStretchable(true);
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
     *
     ***************************************************************************************************/

    private class TestEditorPanel extends RContentPanel {
        private static final long serialVersionUID = -3373619572043547274L;

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

        private REditorPanel panel1 = new REditorPanel(5, 2);

        public TestEditorPanel(String title) {
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
        }

        private void layoutContents() {
            panel1.add(comboBoxEditor);
            panel1.add(checkBoxEditor);
            panel1.add(listEditor);
            panel1.add(longFieldEditor);
            panel1.add(passwordFieldEditor);
            panel1.add(textAreaEditor);
            panel1.add(textFieldEditor);
            panel1.add(currencyFieldEditor);
            panel1.add(calendarFieldEditor);
            panel1.add(tableOfValuesEditor);

            setContentPane(panel1);
        }

        // /**********************************************************************************************************
        // *
        // * LIST OF VALUES STUFF
        // *
        // **********************************************************************************************************/
        //
        // private class ListSelectionModel implements ListOfValuesPageModel {
        //
        // private String[] array1 = { "One", "Two", "Three", "Four", "Five" };
        // private String[] array2 = { "Three", "Four" };
        //
        // public SelectableResults getSelectableValues(SelectableCriteria criteria) throws UIException {
        // return new SelectableResults(Arrays.asList(array1));
        // }
        //
        // public Collection getSelectedValues(String[] values) {
        // return Arrays.asList(array2);
        // }
        //
        // public void setSelectedValues(Collection collection) {
        // }
        // }
        //
        // private class ListSelectionDisplayer implements ListOfValuesDisplayer {
        //
        // public String getEntryText(Object object) {
        // return object.toString() + " Name";
        // }
        //
        // public String getDescriptionText(Object object) {
        // return object.toString();
        // }
        // }
    }
}
