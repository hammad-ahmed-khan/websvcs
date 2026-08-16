package oracle.retail.sim.client.swing.test;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import oracle.retail.sim.client.swing.editor.EditorConstants;
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
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Layout Utility Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestLayoutUtilityTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 3260305476505538814L;

    private TestMatrixUtilityPanel topContentPanel = new TestMatrixUtilityPanel("Test Panel");
    private TestGridUtilityPanel lowContentPanel = new TestGridUtilityPanel("Test Panel 2");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestLayoutUtilityTask() {
        setTaskTitle("Test Matrix Utility Panel");
    }

    public TestLayoutUtilityTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        exitButton.registerAction(this, DONE);
        addButton(exitButton);

        addContentPanel(topContentPanel);
        addContentPanel(lowContentPanel);
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
     * MATRIX UTILITY PANEL
     ***************************************************************************************************/

    private class TestMatrixUtilityPanel extends RContentPanel {
        private static final long serialVersionUID = -1000399788453991843L;

        private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("Combo", true);
        private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("Check");
        private RListEditor listEditor = new RListEditor("List");
        private RLongFieldEditor longFieldEditor = new RLongFieldEditor("Long");
        private RPasswordFieldEditor passwordFieldEditor = new RPasswordFieldEditor("Password", true);
        private RTextAreaEditor textAreaEditor = new RTextAreaEditor("Long Area Name Dude");
        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Long Field Name Dude");
        private RMoneyFieldEditor currencyFieldEditor = new RMoneyFieldEditor("Long Currency Field Test");
        private RDateFieldEditor calendarFieldEditor = new RDateFieldEditor("Long Calendar Field Test");

        private REditorPanel panel1 = new REditorPanel(5, 1);
        private REditorPanel panel2 = new REditorPanel(4, 1);

        public TestMatrixUtilityPanel(String title) {
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

            panel2.add(textAreaEditor);
            panel2.add(textFieldEditor);
            panel2.add(currencyFieldEditor);
            panel2.add(calendarFieldEditor);

            RPanel mainPanel = new RPanel(new GridLayout(2, 1));
            mainPanel.add(panel1);
            mainPanel.add(panel2);

            setContentPane(mainPanel);

            LayoutUtility.alignPanels(panel1, panel2);
        }
    }

    /****************************************************************************************************
     * GRID UTILITY PANEL
     ***************************************************************************************************/

    private class TestGridUtilityPanel extends RContentPanel {
        private static final long serialVersionUID = 7309210051485883948L;

        private RTextFieldEditor locationIDEditor = new RTextFieldEditor("Location ID");
        private RTextFieldEditor locationNameEditor = new RTextFieldEditor("Location Name");
        private RTextFieldEditor addressOneEditor = new RTextFieldEditor("Address 1");
        private RTextFieldEditor addressTwoEditor = new RTextFieldEditor("Address 2");
        private RTextFieldEditor cityEditor = new RTextFieldEditor("City");
        private RTextFieldEditor stateEditor = new RTextFieldEditor("State");
        private RTextFieldEditor countyEditor = new RTextFieldEditor("County");
        private RTextFieldEditor zipCodeEditor = new RTextFieldEditor("Postal Code");
        private RComboBoxEditor countryEditor = new RComboBoxEditor("Country");
        private RTextFieldEditor phoneNumberEditor = new RTextFieldEditor("Phone");

        public TestGridUtilityPanel(String title) {
            super(title);
            buildContentPanel();
            layoutContentPanel();
        }

        private void buildContentPanel() {
            locationIDEditor.setSizeType(EditorConstants.MEDIUM);
            phoneNumberEditor.setSizeType(EditorConstants.MEDIUM);

            stateEditor.setMinimumWidth(70);
            zipCodeEditor.setMinimumWidth(70);
        }

        private void layoutContentPanel() {
            RPanel mainPanel = new RPanel(new GridBagLayout());

            mainPanel.add(locationIDEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(locationNameEditor, GridTool.constraints(0, 1, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(addressOneEditor, GridTool.constraints(0, 2, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(addressTwoEditor, GridTool.constraints(0, 3, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(cityEditor, GridTool.constraints(0, 4, 1, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(stateEditor, GridTool.constraints(1, 4, 1, 1, 0, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(countyEditor, GridTool.constraints(0, 5, 1, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(zipCodeEditor, GridTool.constraints(1, 5, 1, 1, 0, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(countryEditor, GridTool.constraints(0, 6, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));
            mainPanel.add(phoneNumberEditor, GridTool.constraints(0, 7, 2, 1, 1, 0, 5, 1, 0, 0, 2, 0));

            setContentPane(mainPanel);

            LayoutUtility.alignEditorsInGridBag(mainPanel);
        }
    }
}
