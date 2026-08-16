package oracle.retail.sim.client.swing.dialog;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.business.MessageText;

/******************************************************************************************
 * Superclass for the three classes that use the option pane to display information.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class ROptionPaneDialog {

    protected static final String YES = "Yes";
    protected static final String NO = "No";
    protected static final String OK = "OK";
    protected static final String CANCEL = "Cancel";

    private JFrame frame;
    private JDialog dialog;
    private String title;
    private String message;

    /******************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param frame The frame to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ******************************************************************************************/
    protected ROptionPaneDialog(JFrame frame, String title) {
        this.frame = frame;
        this.title = Translator.getText(title);
    }

    /******************************************************************************************
     * Constructs and returns a new RConfirmDialog widget.
     * <p>
     * @param dialog The dialog to place the confirm dialog on.
     * @param title The title to place in the dialog window.
     ******************************************************************************************/
    protected ROptionPaneDialog(JDialog dialog, String title) {
        this.dialog = dialog;
        this.title = Translator.getText(title);
    }

    /******************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     ******************************************************************************************/
    public void setMessage(MessageText message) {
        this.message = Translator.getMessage(message.getText());
    }

    /******************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     * @param value A replacement value to place in the translated message.
     ******************************************************************************************/
    public void setMessage(MessageText message, String value) {
        if (value == null) {
            this.message = Translator.getMessage(message.getText());
        } else {
            this.message = Translator.getMessage(message.getText(), value);
        }
    }

    /******************************************************************************************
     * Assigns a message to the confirmation dialog. This message is translated.
     * <p>
     * @param message The message code to translate.
     * @param values An array of replacement values to place in the message.
     ******************************************************************************************/
    public void setMessage(MessageText message, String[] values) {
        this.message = Translator.getMessage(message.getText(), values);
    }

    /******************************************************************************************
     * Retrieves the title of the dialog.
     ******************************************************************************************/
    protected String getTitle() {
        return title;
    }

    /******************************************************************************************
     * Helper method to build a message panel.
     ******************************************************************************************/
    protected JPanel getMessagePanel() {
        String[] messageArray = StringUtility.splitStringByLength(message, 100);
        JPanel panel = new JPanel(new GridLayout(messageArray.length, 1));
        JLabel label = null;
        Font font = UIManager.getFont(UIThemeName.OPTIONPANE_FONT);
        for (String element : messageArray) {
            label = new JLabel(element);
            label.setFont(font);
            label.setFocusable(false);
            panel.add(label);
        }
        return panel;
    }

    /******************************************************************************************
     * Retrieves the correct component to display the option pane on.
     * <p>
     * @return The component to display the option pane on.
     ******************************************************************************************/
    protected Component getComponent() {
        if (dialog != null) {
            return dialog;
        }
        return frame;
    }
}
