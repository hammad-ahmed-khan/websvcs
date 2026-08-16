package oracle.retail.sim.client.swing.dialog;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.RFrame;

/********************************************************************************************************
 * Confirmation Dialog class that displays a confirmation message and returns true or false depending on
 * the selection.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RConfirmDialog extends ROptionPaneDialog {

    private int dialogType = JOptionPane.OK_CANCEL_OPTION;
    private int messageType = JOptionPane.QUESTION_MESSAGE;

    private Object[] options;

    /****************************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param frame The frame to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ***************************************************************************************************/
    public RConfirmDialog(JFrame frame, String title) {
        super(frame, title);
    }

    /****************************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param dialog The dialog to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ***************************************************************************************************/
    public RConfirmDialog(JDialog dialog, String title) {
        super(dialog, title);
    }

    /****************************************************************************************************
     * Assigns the ok/cancel type to the dialog.
     ***************************************************************************************************/
    public void setOkCancelType() {
        dialogType = JOptionPane.OK_CANCEL_OPTION;
        options = new Object[2];
        options[0] = Translator.getText(OK);
        options[1] = Translator.getText(CANCEL);
    }

    /****************************************************************************************************
     * Assigns the yes/no type to the dialog.
     ***************************************************************************************************/
    public void setYesNoType() {
        dialogType = JOptionPane.YES_NO_OPTION;
        options = new Object[2];
        options[0] = Translator.getText(YES);
        options[1] = Translator.getText(NO);
    }

    /****************************************************************************************************
     * Assigns the Yes/No/Cancel type to the dialog.
     ***************************************************************************************************/
    public void setYesNoCancelType() {
        dialogType = JOptionPane.YES_NO_CANCEL_OPTION;
        options = new Object[3];
        options[0] = Translator.getText(YES);
        options[1] = Translator.getText(NO);
        options[1] = Translator.getText(CANCEL);
    }

    /****************************************************************************************************
     * Returns whether or not the user confirmed (hit the "OK" button).
     * <p>
     * @return True if the user confirmed the action, false if not.
     ***************************************************************************************************/
    public boolean getConfirmation() {
        int option = getSelection();
        if (dialogType == JOptionPane.OK_CANCEL_OPTION && option == JOptionPane.OK_OPTION) {
            return true;
        }
        if (dialogType == JOptionPane.YES_NO_OPTION && option == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Returns which button was selected from the dialog. This should ONLY be used in the case of
     * Yes/No/Cancel option.
     * <p>
     * @return The JOptionPane static value that indicates which button was selected.
     ***************************************************************************************************/
    public int getSelection() {
        Component parent = getComponent();
        JPanel message = getMessagePanel();
        String title = getTitle();

        validateParent(parent);

        return JOptionPane.showOptionDialog(parent, message, title, dialogType, messageType, null, options, options[0]);
    }

    /****************************************************************************************************
     * Validates that the parent container is visible before displaying the popup dialog.
     ***************************************************************************************************/
    private void validateParent(Component parent) {
        while (parent != null) {
            if (parent instanceof RFrame) {
                RFrame frame = (RFrame) parent;
                if (frame.isIconified()) {
                    frame.setRestored();
                }
                return;
            }
            parent = parent.getParent();
        }
    }
}
