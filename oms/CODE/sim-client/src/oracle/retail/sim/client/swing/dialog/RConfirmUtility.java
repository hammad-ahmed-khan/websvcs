package oracle.retail.sim.client.swing.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * Utility for building often repeated popup confirmation boxes.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RConfirmUtility {

    private static final String SAVE_CHANGES = "Save Changes?";
    private static final String CLEAR_CONFIRMATION = "Clear Confirmation";

    /****************************************************************************************************
     * Returns new static RConfirmUtility object.
     ***************************************************************************************************/
    private RConfirmUtility() {
    }

    /****************************************************************************************************
     * Creates and displays a basic message dialog.
     * <p>
     * @param frame The frame that will own the dialog.
     * @param title The title to display in the dialog.
     * @param message The message to display in the dialog.
     ***************************************************************************************************/
    public static void showMessage(RFrame frame, String title, MessageText message) {
        showMessage(frame, title, message, null);
    }

    /****************************************************************************************************
     * Creates and displays a basic message dialog.
     * <p>
     * @param frame The frame that will own the dialog.
     * @param title The title to display in the dialog.
     * @param message The message to display in the dialog.
     ***************************************************************************************************/
    public static void showMessage(RFrame frame, String title, MessageText message, String[] messageValues) {
        if (frame.isIconified()) {
            frame.setRestored();
        }
        String dialogTitle = Translator.getText(title);
        String dialogText = StringConstants.EMPTY;
        if (messageValues == null) {
            dialogText = Translator.getMessage(message.getText());
        } else {
            dialogText = Translator.getMessage(message.getText(), messageValues);
        }
        String[] messageArray = StringUtility.splitStringByLength(dialogText, 80);
        JOptionPane.showMessageDialog(frame, messageArray, dialogTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    /****************************************************************************************************
     * Creates a save changes dialog box with default message. It returns true if the user wishes to save
     * changes, false if not.
     * <p>
     * @param frame The RFrame parent to display it above.
     ***************************************************************************************************/
    public static boolean saveChanges(RFrame frame) {
        RConfirmDialog confirmDialog = new RConfirmDialog(frame, SAVE_CHANGES);
        confirmDialog.setMessage(UIMessageText.SAVE_MODIFIED_DATA);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Creates a save changes dialog box with default message. It returns true if the user wishes to save
     * changes, false if not.
     * <p>
     * @param dialog The RDialog parent to display it on.
     * @return True if "Yes" was pressed, false otherwise.
     ***************************************************************************************************/
    public static boolean saveChanges(RDialog dialog) {
        RConfirmDialog confirmDialog = new RConfirmDialog(dialog, SAVE_CHANGES);
        confirmDialog.setMessage(UIMessageText.SAVE_MODIFIED_DATA);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Creates a save changes dialog box with a customer message. It returns true if the user wishes to
     * save changes, false if not. The error code is translated through the Messages.properties files.
     * <p>
     * @param frame The RFrame parent to display it above.
     * @param message The error code of the message to be displayed.
     * @return True if "Yes" was pressed, false otherwise.
     ***************************************************************************************************/
    public static boolean saveChanges(RFrame frame, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(frame, SAVE_CHANGES);
        confirmDialog.setMessage(message);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Creates a save changes dialog box with a customer message. It returns true if the user wishes to
     * save changes, false if not. The error code is translated through the Messages.properties files.
     * <p>
     * @param dialog The RDialog parent to display it on.
     * @param message The error code of the message to be displayed.
     * @return True if "Yes" was pressed, false otherwise.
     ***************************************************************************************************/
    public static boolean saveChanges(RDialog dialog, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(dialog, SAVE_CHANGES);
        confirmDialog.setMessage(message);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Creates and displays a generic confirm clear OK/CANCEL dialog and returns the selected response.
     * <p>
     * @param frame The frame that will own the dialog.
     * @param message The error code of the message to be displayed.
     * @return True if OK was pressed, false if not.
     ***************************************************************************************************/
    public static boolean confirmClear(JFrame frame, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(frame, CLEAR_CONFIRMATION);
        confirmDialog.setMessage(message);
        confirmDialog.setOkCancelType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Creates and displays a generic confirm clear OK/CANCEL dialog and returns the selected response.
     * <p>
     * @param dialog The RDialog parent to display it on.
     * @param message The error code of the message to be displayed.
     * @return True if OK was pressed, false if not.
     ***************************************************************************************************/
    public static boolean confirmClear(JDialog dialog, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(dialog, CLEAR_CONFIRMATION);
        confirmDialog.setMessage(message);
        confirmDialog.setOkCancelType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Displays a YES/NO confirm dialog with title and message.
     ***************************************************************************************************/
    public static boolean confirm(String title, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(ApplicationInternal.getFrame(), title);
        confirmDialog.setMessage(message);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Displays a YES/NO confirm dialog with title and message with value filled in and formatted.
     ***************************************************************************************************/
    public static boolean confirm(String title, MessageText message, String value) {
        RConfirmDialog confirmDialog = new RConfirmDialog(ApplicationInternal.getFrame(), title);
        confirmDialog.setMessage(message, value);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Displays a YES/NO confirm dialog with title and message with values filled in and formatted.
     ***************************************************************************************************/
    public static boolean confirm(String title, MessageText message, String[] values) {
        RConfirmDialog confirmDialog = new RConfirmDialog(ApplicationInternal.getFrame(), title);
        confirmDialog.setMessage(message, values);
        confirmDialog.setYesNoType();
        return confirmDialog.getConfirmation();
    }

    /****************************************************************************************************
     * Displays a two button confirm dialog with title and message and two buttons.
     * <p>
     * @return True if button1 was selected, false if button2 was selected.
     ***************************************************************************************************/
    public static boolean confirm(String title, MessageText message, String button1, String button2) {
        ROptionDialog optionDialog = new ROptionDialog(ApplicationInternal.getFrame(), title);
        optionDialog.setMessage(message);
        optionDialog.setButtons(button1, button2);
        return optionDialog.isFirstSelection();
    }

    /****************************************************************************************************
     * Displays a Cancel/OK confirm dialog with title and message.
     ***************************************************************************************************/
    public static boolean confirmWithOkCancelType(String title, MessageText message) {
        RConfirmDialog confirmDialog = new RConfirmDialog(ApplicationInternal.getFrame(), title);
        confirmDialog.setMessage(message);
        confirmDialog.setOkCancelType();
        return confirmDialog.getConfirmation();
    }
}
