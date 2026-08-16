package oracle.retail.sim.client.swing.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import oracle.retail.sim.client.locale.Translator;

/******************************************************************************************
 * Confirmation Dialog class that displays a confirmation message and returns true or
 * false depending on the selection.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class ROptionDialog extends ROptionPaneDialog {

    private int type = JOptionPane.DEFAULT_OPTION;
    private String[] options = new String[2];

    /******************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param frame The frame to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ******************************************************************************************/
    public ROptionDialog(JFrame frame, String title) {
        super(frame, title);
    }

    /******************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param dialog The dialog to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ******************************************************************************************/
    public ROptionDialog(JDialog dialog, String title) {
        super(dialog, title);
    }

    /******************************************************************************************
     * Assigns the two button labels to display on the dialog.
     * <p>
     * @param button1 The first button label.
     * @param button2 The second button label.
     ******************************************************************************************/
    public void setButtons(String button1, String button2) {
        options[0] = Translator.getText(button1);
        options[1] = Translator.getText(button2);
    }

    /******************************************************************************************
     * Returns whether or not the user pressed the first of the two buttons.
     * <p>
     * @return True if the user pressed the first button, false if not.
     ******************************************************************************************/
    public boolean isFirstSelection() {
        int option = JOptionPane.showOptionDialog(getComponent(), getMessagePanel(), getTitle(), type, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return option == 0;
    }
}
