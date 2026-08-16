package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.client.swing.widget.RScrollPane;

/********************************************************************************************************
 * Test List Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestListTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = -4756073038225559258L;

    private TestListFieldPanel tabContentPanel = new TestListFieldPanel("Test List");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestListTask() {
        setTaskTitle("Test List");
    }

    public TestListTask(String title) {
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

    private class TestListFieldPanel extends RContentPanel {
        private static final long serialVersionUID = 902573084073746508L;

        private RListEditor listEditor1 = new RListEditor("Test One");
        private RListEditor listEditor2 = new RListEditor("Test Two");
        private RListEditor listEditor3 = new RListEditor("Test Three");
        private RListEditor listEditor4 = new RListEditor("Test Four");

        private RList listField = new RList();
        private RScrollPane listPane = new RScrollPane(listField);

        private RLabel bottomLabel = new RLabel();

        private REditorPanel topPanel = new REditorPanel(4);
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestListFieldPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            String[] array = { "This is the\nfirst string.", "This is the\nsecond string.", "This is the\nthird string." };

            listEditor1.setItems(array);
            listEditor2.setItems(array);
            listEditor2.setMultiLineMode();

            bottomLabel.setText("Just holding a space for visual reasons...");

            listEditor1.setSizeType(EditorConstants.SMALL);
            listEditor1.setErrorState(true);
            listEditor1.setTitleAlignment(EditorConstants.LEFT);
            listEditor2.setSizeType(EditorConstants.MEDIUM);
            listEditor2.setTitleAlignment(EditorConstants.RIGHT);
            listEditor3.setSizeType(EditorConstants.LARGE);
            listEditor3.setTitleAlignment(EditorConstants.TOP);
            listEditor4.setErrorState(true);
            listEditor4.setTitleAlignment(EditorConstants.BOTTOM);
        }

        private void layoutContents() {
            topPanel.add(listEditor1);
            topPanel.add(listEditor2);
            topPanel.add(listEditor3);
            topPanel.add(listEditor4);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(listPane, 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
