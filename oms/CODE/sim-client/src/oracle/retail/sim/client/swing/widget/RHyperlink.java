package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.Icon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/**************************************************************************************************
 * This class is a borderless, "transparent" java button with the look of a hyperlink. The
 * Hyperlink UI is installed regardless of the look and feel being used. Note that also the
 * hyperlink is transparent by default, if you assign setOpaque(true), the hyperlink WILL paint
 * its background (often looking weird if used in a normal panel).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

public class RHyperlink extends RButton {
    private static final long serialVersionUID = -1789693097247481743L;

    private Color borderColor = Color.BLACK;
    private Color disabledForeground = Color.BLACK;
    private Color selectedForeground = Color.BLACK;
    private boolean isUnderlineEnabled = true;

    /******************************************************************************************
     * Constructs new hyperlink widget.
     ******************************************************************************************/
    public RHyperlink() {
        initialize();
    }

    /******************************************************************************************
     * Constructs new hyperlink widget.
     * <p>
     * @param icon An icon to assign to the widget.
     ******************************************************************************************/
    public RHyperlink(Icon icon) {
        initialize();
        setIcon(icon);
    }

    /******************************************************************************************
     * Constructs new hyperlink widget.
     * <p>
     * @param text The text to assign to the widget.
     ******************************************************************************************/
    public RHyperlink(String text) {
        super(text);
        initialize();
    }

    /******************************************************************************************
     * Constructs new hyperlink widget.
     * <p>
     * @param text The text to assign to the widget.
     * @param isUnderlineEnabled True if the hyperlink should be underlined, false otherwise.
     ******************************************************************************************/
    public RHyperlink(String text, boolean isUnderlineEnabled) {
        super(text);
        setUnderlineEnabled(isUnderlineEnabled);
        initialize();
    }

    /******************************************************************************************
     * Retrieves the UIClassID.
     * <p>
     * @return The UI class ID.
     ******************************************************************************************/
    public String getUIClassID() {
        return "HyperlinkUI";
    }

    /******************************************************************************************
     * Initializes hyperlink.
     ******************************************************************************************/
    private void initialize() {
        setOpaque(false);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(null);

        setFont(UIManager.getFont(UIThemeName.HYPERLINK_FONT));
        setForeground(UIManager.getColor(UIThemeName.HYPERLINK_FOREGROUND));
        setDisabledForeground(UIManager.getColor(UIThemeName.HYPERLINK_DISABLED_FOREGROUND));
        setSelectedForeground(UIManager.getColor(UIThemeName.HYPERLINK_SELECTED_FOREGROUND));
        setBorderColor(UIManager.getColor(UIThemeName.HYPERLINK_BORDER_COLOR));
    }

    /******************************************************************************************
     * Assigns whether or not the hyperlink cursor is enabled. The hyperlink cursor is a little
     * hand.
     * <p>
     * @param enabled True if the hyperlink cursor should be enabled, false otherwise.
     ******************************************************************************************/
    public void setHyperCursorEnabled(boolean enabled) {
        if (enabled) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /******************************************************************************************
     * Assigns whether or not the hyperlink underline functionality is enabled.
     * <p>
     * @param enabled True if the hyperlink underline should be enabled, false otherwise.
     ******************************************************************************************/
    public void setUnderlineEnabled(boolean enabled) {
        isUnderlineEnabled = enabled;
    }

    /******************************************************************************************
     * Retrieves whether or not the hyperlink underline functionality is enabled.
     * <p>
     * @param True if the hyperlink underline is enabled, false otherwise.
     ******************************************************************************************/
    public boolean isUnderlineEnabled() {
        return isUnderlineEnabled;
    }

    /******************************************************************************************
     * Assigns a color to the border of a hyperlink. This only comes into play when the
     * hyperlink has focus.
     * <p>
     * @param color The color to assign.
     ******************************************************************************************/
    public void setBorderColor(Color color) {
        if (color != null) {
            borderColor = color;
        }
    }

    /******************************************************************************************
     * Retrieves the border color of a hyperlink when it has focus.
     * <p>
     * @return The border color.
     ******************************************************************************************/
    public Color getBorderColor() {
        return borderColor;
    }

    /******************************************************************************************
     * Assigns the disabled foreground color.
     * <p>
     * @param color The color to assign.
     ******************************************************************************************/
    public void setDisabledForeground(Color color) {
        if (color != null) {
            disabledForeground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the disabled foreground color.
     * <p>
     * @return The disabled foreground color.
     ******************************************************************************************/
    public Color getDisabledForeground() {
        return disabledForeground;
    }

    /******************************************************************************************
     * Assigns the selected foreground color.
     * <p>
     * @param color The color to assign.
     ******************************************************************************************/
    public void setSelectedForeground(Color color) {
        if (color != null) {
            selectedForeground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the selected foreground color.
     * <p>
     * @return The selected foreground color.
     ******************************************************************************************/
    public Color getSelectedForeground() {
        return selectedForeground;
    }
}
