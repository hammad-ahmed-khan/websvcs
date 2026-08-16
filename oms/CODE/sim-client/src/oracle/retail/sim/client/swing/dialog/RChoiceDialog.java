package oracle.retail.sim.client.swing.dialog;

import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * This class is a popup window is a dialog window containing a message, list of selections, and
 * okay/cancel buttons. The user selections a choice and presses "OK" or cancels the window if he doesn't
 * wish to proceed.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RChoiceDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -8258874050283277342L;

    private static final String OKAY = "OK";
    private static final String CANCEL = "Cancel";
    private static final String SELECTION = "Selection";

    private RLabel messageLabel = new RLabel(StringConstants.EMPTY);
    private RComboBoxEditor choiceEditor = new RComboBoxEditor();

    private RButton okayButton = new RButton(OKAY);
    private RButton cancelButton = new RButton(CANCEL);

    private String eventCommand = StringConstants.EMPTY;

    /****************************************************************************************************
     * Creates a new dialog.
     * <p>
     * @param frame The parent frame that will own the dialog.
     ***************************************************************************************************/
    public RChoiceDialog(JFrame frame) {
        super(frame);
        setSize(350, 170);
        setStatusBarVisible(false);
        buildDialog();
        layoutDialog();
        centerOnOwner();
    }

    /****************************************************************************************************
     * Creates a new dialog.
     * <p>
     * @param dialog The parent dialog that will own the dialog.
     ***************************************************************************************************/
    public RChoiceDialog(JDialog dialog) {
        super(dialog);
        setSize(350, 170);
        setStatusBarVisible(false);
        buildDialog();
        layoutDialog();
        centerOnOwner();
    }

    /****************************************************************************************************
     * Sets the various properties of the widgets.
     ***************************************************************************************************/
    private void buildDialog() {
        choiceEditor.registerAction(this, SELECTION);
        messageLabel.setVisible(false);
        messageLabel.setHorizontalAlignment(EditorConstants.LEFT);

        okayButton.registerAction(this, OKAY);
        cancelButton.registerAction(this, CANCEL);

        okayButton.setEnabled(false);
    }

    /****************************************************************************************************
     * Lays out the widgets on the panel.
     ***************************************************************************************************/
    private void layoutDialog() {
        addButton(okayButton);
        addButton(cancelButton);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.setEmptyBorder(10, 0, 10, 0);
        mainPanel.add(messageLabel, GridTool.constraints(0, 0, 1, 1, 1, 1, 5, 3, 0, 10, 3, 10));
        mainPanel.add(choiceEditor, GridTool.constraints(0, 1, 1, 1, 1, 1, 5, 3, 5, 0, 5, 10));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assigns message to be displayed. The parameter is used to look up the translated message in a
     * bundle.
     * <p>
     * @param messageKey The key of the message to be displayed.
     ***************************************************************************************************/
    public void setMessage(MessageText message) {
        messageLabel.setText(Translator.getMessage(message.getText()));
        messageLabel.setVisible(true);
    }

    public void setMessage(MessageText message, String[] values) {
        messageLabel.setText(Translator.getMessage(message.getText(), values));
        messageLabel.setVisible(true);
    }

    /****************************************************************************************************
     * Assigns label to be displayed. This label is placed in front of the choice selection combo box.
     * <p>
     * @param label The label to be displayed.
     ***************************************************************************************************/
    public void setLabel(String label) {
        choiceEditor.setTitle(label);
    }

    /****************************************************************************************************
     * Assigns options to the drop down combo box.
     * <p>
     * @param options The options to assign to the drop down combo box.
     ***************************************************************************************************/
    public void setOptions(Object[] options) {
        choiceEditor.setActionsEnabled(false);
        choiceEditor.setItems(options);
        choiceEditor.setActionsEnabled(true);
    }

    /****************************************************************************************************
     * Assigns options to the drop down combo box.
     * <p>
     * @param options The options to assign to the drop down combo box.
     ***************************************************************************************************/
    public void setOptions(Collection options) {
        choiceEditor.setActionsEnabled(false);
        choiceEditor.setItems(options);
        choiceEditor.setActionsEnabled(true);
    }

    /****************************************************************************************************
     * Assigns options to the drop down combo box with comparator
     * <p>
     * @param options The options to assign to the drop down combo box.
     * @param comparator How to sort and compare the optiosn
     ***************************************************************************************************/
    public void setOptions(List options, Comparator comparator) {
        Collections.sort(options, comparator);
        choiceEditor.setActionsEnabled(false);
        choiceEditor.setItems(options);
        choiceEditor.setActionsEnabled(true);
    }

    /****************************************************************************************************
     * Returns the selection option (null if none).
     * <p>
     * return The selection option (null if none).
     ***************************************************************************************************/
    public void setSelectedOption(Object option) {
        choiceEditor.setSelectedItem(option);
    }

    /****************************************************************************************************
     * Returns the selection option (null if none).
     * <p>
     * return The selection option (null if none).
     ***************************************************************************************************/
    public Object getSelectedOption() {
        return choiceEditor.getSelectedItem();
    }

    /****************************************************************************************************
     * Assigns the command for the choice dialog to return to its owner.
     * <p>
     * @param command The command to return to the owner upon selection.
     ***************************************************************************************************/
    public void setCommand(String command) {
        eventCommand = command;
    }

    /****************************************************************************************************
     * Assigns a display to do the string display for the data.
     * <p>
     * @param displayer The display to convert object data to string descriptions.
     ***************************************************************************************************/
    public void setChoiceDisplayer(BasicDisplayer displayer) {
        choiceEditor.setDisplayer(displayer);
    }

    /****************************************************************************************************
     * Handle events.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(OKAY)) {
            notifyREventListeners(new RActionEvent(this, eventCommand, choiceEditor.getSelectedItem()));
            closeWindow();
        } else if (command.equals(CANCEL)) {
            doCancel();
        } else if (command.equals(SELECTION)) {
            okayButton.setEnabled(!choiceEditor.isEmptySelection());
        }
    }

    /****************************************************************************************************
     * Cancels the choice dialog.
     ***************************************************************************************************/
    private void doCancel() {
        choiceEditor.setEmptySelection();
        closeWindow();
    }
}
