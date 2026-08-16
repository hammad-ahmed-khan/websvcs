package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Test Combo Box Funcationality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestComboBoxTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 3319596643646858044L;

    private TestComboBoxPanel tabContentPanel = new TestComboBoxPanel("Test Combo Box");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestComboBoxTask() {
        setTaskTitle("Test Combo Box");
    }

    public TestComboBoxTask(String title) {
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

    private class TestComboBoxPanel extends RContentPanel implements REventListener {
        private static final long serialVersionUID = -6248989953820563868L;

        private RComboBoxEditor comboBoxEditor1 = new RComboBoxEditor("Test One");
        private RComboBoxEditor comboBoxEditor2 = new RComboBoxEditor("Test Two", true);
        private RComboBoxEditor comboBoxEditor3 = new RComboBoxEditor("Test Three");
        private RComboBoxEditor comboBoxEditor4 = new RComboBoxEditor("Test Four", true);
        private RComboBoxEditor comboBoxEditor5 = new RComboBoxEditor("Test Five");
        private RComboBoxEditor comboBoxEditor6 = new RComboBoxEditor("Test Six", true);
        private RComboBoxEditor comboBoxEditor7 = new RComboBoxEditor("Test Seven");
        private RComboBoxEditor comboBoxEditor8 = new RComboBoxEditor("Test Eight", true);

        private RComboBox comboBox = new RComboBox();
        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        private static final String TEST = "Test";
        private RButton testButton = new RButton(TEST);

        String[] unsortedStrings = new String[] { "\u00e4pple", "banan", "p\u00e4ron", "orange" };

        public TestComboBoxPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            bottomLabel.setText("Just holding a space for visual reasons...");

            comboBoxEditor1.setSizeType(EditorConstants.SMALL);
            comboBoxEditor1.setErrorState(true);
            comboBoxEditor2.setSizeType(EditorConstants.MEDIUM);
            comboBoxEditor3.setSizeType(EditorConstants.LARGE);
            comboBoxEditor5.setTitleAlignment(EditorConstants.RIGHT);
            comboBoxEditor5.setErrorState(true);
            comboBoxEditor6.setTitleAlignment(EditorConstants.RIGHT);
            comboBoxEditor6.setSizeType(EditorConstants.MEDIUM);
            comboBoxEditor7.setTitleAlignment(EditorConstants.TOP);
            comboBoxEditor8.setTitleAlignment(EditorConstants.BOTTOM);

            List dataList = new ArrayList<>();
            dataList.add(new TestComboObject("One"));
            dataList.add(new TestComboObject("Two"));
            dataList.add(new TestComboObject("Three"));

            comboBoxEditor2.setDisplayer(new ComboTestRowDisplayer());
            comboBoxEditor2.setItems(dataList);

            comboBoxEditor3.setItems(new ArrayList<>());
            System.out.println(comboBoxEditor3.isEmpty());

            comboBoxEditor6.setItems(unsortedStrings);

            testButton.registerAction(this, TEST);
        }

        private void layoutContents() {
            addButton(testButton);

            topPanel.add(comboBoxEditor1);
            topPanel.add(comboBoxEditor2);
            topPanel.add(comboBoxEditor3);
            topPanel.add(comboBoxEditor4);
            topPanel.add(comboBoxEditor7);
            topPanel.add(comboBoxEditor8);
            topPanel.add(comboBoxEditor5);
            topPanel.add(comboBoxEditor6);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(comboBox, BorderLayout.NORTH);
            botPanel.add(bottomLabel, BorderLayout.CENTER);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 0);
            dividerPanel.add(new RPanel(), 1, 1);

            setContentPane(dividerPanel);
        }

        public void performActionEvent(RActionEvent arg0) {
            comboBoxEditor2.setDisplayer(new ComboTestRowDisplayer());
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class ComboTestRowDisplayer extends AbstractDisplayer {
        public String getDisplayText(Object object) {
            return "++" + ((TestComboObject) object).getName() + "++";
        }
    }

    /****************************************************************************************************
     *
     ***************************************************************************************************/

    private class TestComboObject {

        private String testName = "";

        public TestComboObject(String name) {
            testName = name;
        }

        public String getName() {
            return testName;
        }
    }
}
