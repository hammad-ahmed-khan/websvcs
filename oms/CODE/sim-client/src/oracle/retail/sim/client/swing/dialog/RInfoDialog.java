package oracle.retail.sim.client.swing.dialog;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * Information Dialog class that displays an informational message with an OK button.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RInfoDialog extends ROptionPaneDialog {

    private Object[] options = new Object[1];

    /****************************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param frame The frame to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ***************************************************************************************************/
    public RInfoDialog(JFrame frame, String title) {
        super(frame, title);
        options[0] = Translator.getText(OK);
    }

    /****************************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param dialog The dialog to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ***************************************************************************************************/
    public RInfoDialog(JDialog dialog, String title) {
        super(dialog, title);
        options[0] = Translator.getText(OK);
    }

    /****************************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     ***************************************************************************************************/
    public void displayMessage(MessageText message) {
        setMessage(message);
        showInfo(JOptionPane.INFORMATION_MESSAGE);
    }

    /****************************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     * @param value A replacement value to place in the translated message.
     ***************************************************************************************************/
    public void displayMessage(MessageText message, String value) {
        setMessage(message, value);
        showInfo(JOptionPane.INFORMATION_MESSAGE);
    }

    /****************************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     * @param values An array of replacement values to place in the message.
     ***************************************************************************************************/
    public void displayMessage(MessageText message, String[] values) {
        setMessage(message, values);
        showInfo(JOptionPane.INFORMATION_MESSAGE);
    }

    /****************************************************************************************************
     * Assigns a warning to the confirmation dialog. This warning is translated.
     * <p>
     * @param warning The warning code to translate.
     ***************************************************************************************************/
    public void displayWarning(MessageText warning) {
        setMessage(warning);
        showInfo(JOptionPane.WARNING_MESSAGE);
    }

    /****************************************************************************************************
     * Assigns a warning to the confirmation dialog. This warning is translated.
     * <p>
     * @param warning The warning code to translate.
     * @param value A replacement value to place in the translated warning.
     ***************************************************************************************************/
    public void displayWarning(MessageText warning, String value) {
        setMessage(warning, value);
        showInfo(JOptionPane.WARNING_MESSAGE);
    }

    /****************************************************************************************************
     * Assigns a warning to the confirmation dialog. This warning is translated.
     * <p>
     * @param warning The warning code to translate.
     * @param values An array of replacement values to place in the warning.
     ***************************************************************************************************/
    public void displayWarning(MessageText warning, String[] values) {
        setMessage(warning, values);
        showInfo(JOptionPane.WARNING_MESSAGE);
    }

    /****************************************************************************************************
     * Displays the dialog.
     ***************************************************************************************************/
    private void showInfo(int messageType) {
        Component parent = getComponent();
        JPanel message = getMessagePanel();
        String title = getTitle();
        int dialogType = JOptionPane.CANCEL_OPTION;
        validateParent(parent);
        JOptionPane.showOptionDialog(parent, message, title, dialogType, messageType, null, options, options[0]);
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
