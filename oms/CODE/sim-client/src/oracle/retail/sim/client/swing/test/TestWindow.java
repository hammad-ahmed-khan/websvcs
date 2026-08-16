package oracle.retail.sim.client.swing.test;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.editor.RLongFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Test Window Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestWindow extends RDialog implements REventListener {
    private static final long serialVersionUID = 7001881725718825192L;

    private RTextFieldEditor textFieldEditor = new RTextFieldEditor("TextField", true);
    private RCheckBoxEditor checkBoxEditor = new RCheckBoxEditor("CheckBox");
    private RLongFieldEditor longFieldEditor = new RLongFieldEditor("LongField", true);
    private RComboBoxEditor comboBoxEditor = new RComboBoxEditor("ComboBox");
    private RListEditor listEditor = new RListEditor("List");
    private RTextAreaEditor textAreaEditor = new RTextAreaEditor("TextArea");

    private static final String CLOSE = "Close";

    private RButton closeButton = new RButton(CLOSE);

    public TestWindow(JFrame frame) {
        super(frame);
        setTitle("Test Window");
        setSize(500, 400);
        buildDialog();
        layoutDialog();
        centerWindow();
    }

    private void buildDialog() {
        textFieldEditor.setIdentifier("A");
        longFieldEditor.setIdentifier("B");
        checkBoxEditor.setIdentifier("C");
        comboBoxEditor.setIdentifier("D");
        listEditor.setIdentifier("E");
        textAreaEditor.setIdentifier("F");

        textFieldEditor.setLength(99);
        textAreaEditor.setLength(99);
        longFieldEditor.setLength(99);

        String[] comboArray = { "ComboChoiceOne", "ComboChoiceTwo" };

        comboBoxEditor.setItems(comboArray);

        String[] listArray = { "ListItemOne", "ListItemTwo" };

        listEditor.setItems(listArray);
        listEditor.setTitleAlignment(EditorConstants.LEFT);

        textAreaEditor.registerAction(this, "Hello");

        closeButton.registerAction(this, CLOSE);
    }

    private void layoutDialog() {
        addButton(closeButton);

        REditorPanel panel = new REditorPanel(7, 1);

        panel.add(textFieldEditor);
        panel.add(checkBoxEditor);
        panel.add(longFieldEditor);
        panel.add(comboBoxEditor);
        panel.add(listEditor);
        panel.add(textAreaEditor);

        setContentPane(panel);
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        try {
            if ("Hello".equals(command)) {
                triggerExceptionTest();
            } else if (command.equals(CLOSE)) {
                closeWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void triggerExceptionTest() {
        List<UIProblem> problems = new ArrayList<>();
        problems.add(new UIProblem(CommonMessageText.ACTION_INVALID));
        problems.add(new UIProblem("A", CommonMessageText.VALUE_INVALID_NEGATIVE));
        problems.add(new UIProblem("B", CommonMessageText.VALUE_INVALID_ZERO));
        problems.add(new UIProblem("C", CommonMessageText.VALUE_NOT_IN_RANGE));
        problems.add(new UIProblem("D", CommonMessageText.VALUE_NOT_VALID));
        problems.add(new UIProblem("E", CommonMessageText.VALUE_NOT_VALID_ID));
        problems.add(new UIProblem("F", CommonMessageText.VALUE_NOT_WHOLE));

        displayException(new UIException(problems));
    }
}
