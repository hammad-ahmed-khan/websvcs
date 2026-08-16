package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/******************************************************************************************
 * This class sublcasses the standard RRadioButton class in the Swing package to provide
 * custom functionality for Oracle Retail. Unlike many other widgets, this one does NOT have
 * an editor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RRadioButton extends JRadioButton implements ItemListener {
    private static final long serialVersionUID = 220421290732495034L;

    private REventListener eventListener;
    private String eventCommand;
    private boolean actionsEnabled = true;

    /******************************************************************************************
     * Returns new RRadioButton object.
     *****************************************************************************************/
    public RRadioButton() {
        initialize();
    }

    /******************************************************************************************
     * Return new RRadioButton object with an established display text.
     * <p>
     * @param text The label text to display near the check box.
     *****************************************************************************************/
    public RRadioButton(String text) {
        setText(text);
        initialize();
    }

    /******************************************************************************************
     * Initializes the default radio button state.
     *****************************************************************************************/
    private void initialize() {
        setOpaque(false);
        setAlignment(RIGHT, LEFT);
        setFont(UIManager.getFont(UIThemeName.RADIOBUTTON_FONT));
        setBackground(UIManager.getColor(UIThemeName.RADIOBUTTON_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.RADIOBUTTON_FOREGROUND));
        addItemListener(this);
    }

    /******************************************************************************************
     * Builds a radio button box with a specified alignement.
     * <p>
     *@param boxAlign The horizontal positioning of the radio button.
     *@param textAlign The horizontal positioning of the label text
     *****************************************************************************************/
    public void setAlignment(int boxAlign, int textAlign) {
        setHorizontalAlignment(boxAlign);
        setHorizontalTextPosition(textAlign);
    }

    /******************************************************************************************
     * Registers an action with the radio button. When the state of the button changes, the
     * command is sent to the listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     *****************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventListener = listener;
        eventCommand = command;
    }

    /******************************************************************************************
     * Sets whether or not the actions are enabled on the radio button. If they are not enabled,
     * then no event is sent when the state changes.
     * <p>
     *@param enabled True if actions should be enabled, false otherwise.
     *****************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        actionsEnabled = enabled;
    }

    /******************************************************************************************
     * Sets the text to display within the radio button. Automatic language translation occurs.
     * <p>
     *@param text The text to display within the radio button.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
    }

    /******************************************************************************************
     * Sets the font style for the checkbox.
     * <p>
     * @param style The style to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(int style) {
        setFont(getFont().deriveFont(style));
    }

    /******************************************************************************************
     * Sets the font style and size for the checkbox.
     * <p>
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(int style, float size) {
        setFont(getFont().deriveFont(style, size));
    }

    /******************************************************************************************
     * Sets the font color, style and size for the checkbox.
     * <p>
     * @param color The color to assign to the foreground of the checkbox.
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(Color color, int style, float size) {
        setFont(getFont().deriveFont(style, size));
        setForeground(color);
    }

    /******************************************************************************************
     * Implements the state changed method to notify an event listener with the command.
     /******************************************************************************************/
    public void itemStateChanged(ItemEvent event) {
        if (actionsEnabled && eventListener != null && eventCommand != null) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand, isSelected()));
        }
    }
}
