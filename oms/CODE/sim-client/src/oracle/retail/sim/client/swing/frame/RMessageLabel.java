package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/******************************************************************************************
 * This class is the message display area of the status bar.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RMessageLabel extends JLabel {
    private static final long serialVersionUID = 1198904002086225210L;

    public static final String EMPTY_LABEL = " ";

    private Color baseBackground;
    private Color baseForeground;
    private Color messageBackground;
    private Color messageForeground;
    private Color warningBackground;
    private Color warningForeground;
    private Color errorBackground;
    private Color errorForeground;

    private boolean errorDisplayed;
    private boolean warningDisplayed;

    /******************************************************************************************
     * Returns new RMessageLabel object.
     *****************************************************************************************/
    public RMessageLabel() {
        initializeFont();
        initializeColors();
        setBorder(new CompoundBorder(BorderFactory.createLoweredBevelBorder(), new EmptyBorder(2, 2, 2, 2)));
        setFocusable(false);
        setText(EMPTY_LABEL);
        setOpaque(true);
    }

    /******************************************************************************************
     * Initialize the default font.
     *****************************************************************************************/
    private void initializeFont() {
        setFont(UIManager.getFont(UIThemeName.STATUSBAR_FONT));
    }

    /******************************************************************************************
     * Initialize all the colors.
     *****************************************************************************************/
    private void initializeColors() {
        baseBackground = UIManager.getColor(UIThemeName.STATUSBAR_BACKGROUND);
        baseForeground = UIManager.getColor(UIThemeName.STATUSBAR_FOREGROUND);
        messageBackground = UIManager.getColor(UIThemeName.STATUSBAR_MESSAGE_BACKGROUND);
        messageForeground = UIManager.getColor(UIThemeName.STATUSBAR_MESSAGE_FOREGROUND);
        warningBackground = UIManager.getColor(UIThemeName.STATUSBAR_WARNING_BACKGROUND);
        warningForeground = UIManager.getColor(UIThemeName.STATUSBAR_WARNING_FOREGROUND);
        errorBackground = UIManager.getColor(UIThemeName.STATUSBAR_ERROR_BACKGROUND);
        errorForeground = UIManager.getColor(UIThemeName.STATUSBAR_ERROR_FOREGROUND);
        setBaseFormat();
    }

    /******************************************************************************************
     * Sets the message format to informational message.
     *****************************************************************************************/
    public void setBaseFormat() {
        setBackground(baseBackground);
        setForeground(baseForeground);
        errorDisplayed = false;
        warningDisplayed = false;
    }

    /******************************************************************************************
     * Sets the message format to informational message.
     *****************************************************************************************/
    public void setMessageFormat() {
        setBackground(messageBackground);
        setForeground(messageForeground);
        errorDisplayed = false;
        warningDisplayed = false;
    }

    /******************************************************************************************
     * Sets the message format to warning message.
     *****************************************************************************************/
    public void setWarningFormat() {
        setBackground(warningBackground);
        setForeground(warningForeground);
        errorDisplayed = false;
        warningDisplayed = true;
    }

    /******************************************************************************************
     * Sets the message format to error message.
     *****************************************************************************************/
    public void setErrorFormat() {
        setBackground(errorBackground);
        setForeground(errorForeground);
        errorDisplayed = true;
        warningDisplayed = false;
    }

    /*****************************************************************************************
     * Retrieves whether or not the label is currently displaying a warning.
     * <p>
     * @return True if the message label is currently displaying an warning, false if not.
     *****************************************************************************************/
    public boolean isWarningDisplayed() {
        return warningDisplayed;
    }

    /*****************************************************************************************
     * Retrieves whether or not the label is currently displaying an error.
     * <p>
     * @return True if the message label is currently displaying an error, false if not.
     *****************************************************************************************/
    public boolean isErrorDisplayed() {
        return errorDisplayed;
    }

    /*****************************************************************************************
     * Clears the message label.
     *****************************************************************************************/
    public void clear() {
        setBaseFormat();
        setText(EMPTY_LABEL);
        setToolTipText(EMPTY_LABEL);
    }
}
