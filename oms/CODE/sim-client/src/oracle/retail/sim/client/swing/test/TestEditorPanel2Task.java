package oracle.retail.sim.client.swing.test;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Test Editor Panel Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestEditorPanel2Task extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 8638419425462687773L;

    private TestEditorPanel tabContentPanel = new TestEditorPanel("Test Panel");
    private TestEditorPanel2 tabContentPanel2 = new TestEditorPanel2("Test Panel");

    private static final String DONE = "Done";
    private RButton doneButton = new RButton(DONE);

    private static final String TEST = "Test";
    private RButton testButton = new RButton(TEST);

    public TestEditorPanel2Task() {
        setTaskTitle("Test Matrix Panel");
    }

    public TestEditorPanel2Task(String title) {
        setTaskTitle(title);
    }

    public void init() {
        testButton.registerAction(this, TEST);
        doneButton.registerAction(this, DONE);
        addButton(testButton);
        addButton(doneButton);
        addContentPanel(tabContentPanel);
        addContentPanel(tabContentPanel2);
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
        if (event.getEventCommand().equals(TEST)) {
            tabContentPanel.doTest();
        } else if (event.getEventCommand().equals(DONE)) {
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
        private static final long serialVersionUID = -267542812295495084L;

        private RTextFieldEditor textFieldEditor = new RTextFieldEditor("Text Field Test");
        private RComboBoxEditor comboBox1Editor = new RComboBoxEditor("Combo Box Test", true);
        private RComboBoxEditor comboBox2Editor = new RComboBoxEditor("Combo Box2 Test");

        private REditorPanel panel1 = new REditorPanel(1, 3);

        public TestEditorPanel(String title) {
            super(title);

            comboBox1Editor.setSizeType(EditorConstants.MEDIUM);
            comboBox2Editor.setSizeType(EditorConstants.MEDIUM);

            panel1.add(textFieldEditor);
            panel1.add(comboBox1Editor);
            panel1.add(comboBox2Editor);

            setContentPane(panel1);
        }

        protected void doTest() {
            List item1List = new ArrayList<>();
            item1List.add("This is really long 1");
            item1List.add("This is really long adfadfadfa adfadfadfadf");
            item1List.add("This is really long dfadfadf  adfdddddddddd");

            List item2List = new ArrayList<>();
            item2List.add("This is really long eeeeeeee");
            item2List.add("This is really long ffffffffffffff1");
            item2List.add("This is really long 1");

            comboBox1Editor.setItems(item1List);
            comboBox2Editor.setItems(item2List);
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestEditorPanel2 extends RContentPanel {
        private static final long serialVersionUID = 1905621019586053289L;

        private RComboBoxEditor comboBox1Editor = new RComboBoxEditor("Combo Box Test", true);
        private RComboBoxEditor comboBox2Editor = new RComboBoxEditor("Combo Box2 Test");

        private REditorPanel panel1 = new REditorPanel(2);

        public TestEditorPanel2(String title) {
            super(title);

            List item1List = new ArrayList<>();
            item1List.add("This is really long 1");
            item1List.add("This is really long adfadfadfa adfadfadfadf");
            item1List.add("This is really long dfadfadf  adfdddddddddd");

            List item2List = new ArrayList<>();
            item2List.add("This is really long eeeeeeee");
            item2List.add("This is really long ffffffffffffff1");
            item2List.add("This is really long 1");

            comboBox1Editor.setItems(item1List);
            comboBox2Editor.setItems(item2List);
            comboBox1Editor.setSizeType(EditorConstants.MEDIUM);
            comboBox2Editor.setSizeType(EditorConstants.MEDIUM);

            panel1.add(comboBox1Editor);
            panel1.add(comboBox2Editor);

            setContentPane(panel1);
        }
    }
}
