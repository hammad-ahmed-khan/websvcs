package oracle.retail.sim.client.swing.test;

import java.awt.BorderLayout;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RDividerPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.swing.widget.RTextArea;

/********************************************************************************************************
 * Test Text Area Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestTextAreaTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 22317786673967121L;

    private TestTextAreaPanel tabContentPanel = new TestTextAreaPanel("Test Text Area");

    private static final String DONE = "Done";

    private RButton exitButton = new RButton(DONE);

    public TestTextAreaTask() {
        setTaskTitle("Test Text Area");
    }

    public TestTextAreaTask(String title) {
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

    private class TestTextAreaPanel extends RContentPanel {
        private static final long serialVersionUID = 8410693136932471030L;

        private RTextAreaEditor textAreaEditor1 = new RTextAreaEditor("Test One");
        private RTextAreaEditor textAreaEditor2 = new RTextAreaEditor("Test Two", true);
        private RTextAreaEditor textAreaEditor3 = new RTextAreaEditor("Test Three");
        private RTextAreaEditor textAreaEditor4 = new RTextAreaEditor("Test Four", true);

        private RTextArea textArea = new RTextArea();
        private RScrollPane textPane = new RScrollPane(textArea);

        private REditorPanel topPanel = new REditorPanel(8);
        private RPanel botPanel = new RPanel();
        private RDividerPanel dividerPanel = new RDividerPanel(2, 2);

        public TestTextAreaPanel(String title) {
            super(title);
            initializeWidgets();
            layoutContents();
        }

        private void initializeWidgets() {
            textAreaEditor1.setLength(Integer.MAX_VALUE);
            textAreaEditor2.setLength(Integer.MAX_VALUE);

            textAreaEditor1.setTitleAlignment(EditorConstants.TOP);
            textAreaEditor2.setTitleAlignment(EditorConstants.RIGHT);

            textArea.setLength(Integer.MAX_VALUE);

            textAreaEditor1.setSizeType(EditorConstants.SMALL);
            textAreaEditor2.setSizeType(EditorConstants.MEDIUM);
            textAreaEditor3.setSizeType(EditorConstants.LARGE);

            textAreaEditor1.setErrorState(true);
            textAreaEditor4.setErrorState(true);
        }

        private void layoutContents() {
            topPanel.add(textAreaEditor1);
            topPanel.add(textAreaEditor2);
            topPanel.add(textAreaEditor3);
            topPanel.add(textAreaEditor4);

            botPanel.setLayout(new BorderLayout());
            botPanel.add(textPane);

            dividerPanel.add(topPanel, 0, 0);
            dividerPanel.add(botPanel, 1, 1);

            setContentPane(dividerPanel);
        }
    }
}
