package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.TextLengthTranslator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.Mask;

/******************************************************************************************
 * This class sub-classes the standard JTextField class in the Swing package to provide
 * custom functionality for the Rcom client application.
 * <p>
 * This widgets has the ability to limit the number of character allowed in the field.
 * RTextFields will remain disabled until and entry length is specified.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTextArea extends JTextArea implements RetailComponent, KeyListener {
    private static final long serialVersionUID = -5861664666891637703L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private Color defaultCaretColor;

    private Color inactiveBackgroundColor;
    private Color inactiveForegroundColor;

    private boolean isControllingColor;
    private boolean isDisabledActiveFlag;

    private Mask formatMask;
    private boolean isFormatInvalid;

    private String lastSetTextValue = StringConstants.EMPTY;
    protected int allowedLength;

    /******************************************************************************************
     * Returns a new object.
     *****************************************************************************************/
    public RTextArea() {
        setBorder(BorderFactory.createLoweredBevelBorder());
        initializeColors();
        addKeyListener(this);
        setWrapStyleWord(true);
        setLineWrap(true);
        setBorder(null);
        setEditable(false);
        setEnabled(false);
    }

    /******************************************************************************************
     * Initializes the colors used in the text area.
     *****************************************************************************************/
    private void initializeColors() {
        isDisabledActiveFlag = UIManager.getBoolean(UIThemeName.SYSTEM_DISABLED_VALUE_ACTIVE);

        setBackground(UIManager.getColor(UIThemeName.TEXTAREA_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.TEXTAREA_FOREGROUND));

        setInactiveBackground(UIManager.getColor(UIThemeName.TEXTAREA_INACTIVE_BACKGROUND));
        setInactiveForeground(UIManager.getColor(UIThemeName.TEXTAREA_INACTIVE_FOREGROUND));

        defaultCaretColor = UIManager.getColor(UIThemeName.TEXTAREA_CARET_FOREGROUND);
    }

    /******************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() useable
     * by developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     ******************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        setLength(identifier);
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifer to the component.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the text area.
     * <p>
     *@param width The width in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        Dimension dim = new Dimension(width, getPreferredSize().height);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the text area.
     * <p>
     *@param width The width in pixels.
     *@param height The height in pixels.
     *****************************************************************************************/
    public void setMinimumSize(int width, int height) {
        Dimension dim = new Dimension(width, height);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the text area as well as the number
     * of characters allowed in the area.
     * <p>
     *@param width The width in pixels.
     *@param height The height in pixels.
     *@param length The length in characters that the text area should allow.
     *****************************************************************************************/
    public void setDefaultSize(int width, int height, int length) {
        setMinimumSize(width, height);
        setLength(length);
    }

    /******************************************************************************************
     * Sets the allowable text length based on the text area name. This will hook through
     * the framework into a properties file that defines the appropriate length of the name.
     * <p>
     *@param name The name to assign to the text area and look up the appropriate area size.
     *****************************************************************************************/
    public void setLength(String name) {
        setLength(TextLengthTranslator.getLength(name));
    }

    /******************************************************************************************
     * Sets the allowable text length. The text area will only allow users to enter up to this
     * length in characters before it stops processing keystrokes.
     * <p>
     *@param allowedLength The length in characters that the text area should allow.
     *****************************************************************************************/
    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be a negative value.");
        }
        allowedLength = length;

        if (allowedLength > 0) {
            setEditable(true);
            setEnabled(true);
            firePropertyChange(UIPropertyName.TEXT_COMPONENT_ENABLED, false, true);
        } else {
            setEditable(false);
            setEnabled(false);
            firePropertyChange(UIPropertyName.TEXT_COMPONENT_ENABLED, true, false);
        }
    }

    /******************************************************************************************
     * Returns the allowable text length.
     * <p>
     *@return The length in characters that the text area should allow.
     *****************************************************************************************/
    public int getLength() {
        return allowedLength;
    }

    /******************************************************************************************
     * @see JTextField setFocusable()
     *****************************************************************************************/
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        updateColorState();
    }

    /******************************************************************************************
     * Returns whether this Component can be focused. This method is overridden to forcefully
     * return false if text area is not editable or not enabled.
     * <p?
     * @return True if the text area is focusable, false if it is not.
     *****************************************************************************************/
    public boolean isFocusable() {
        if (isEditable() && isEnabled()) {
            return super.isFocusable();
        }
        return false;
    }

    /******************************************************************************************
     * Assigns a format mask to the text area. The specific mask is cast to the interface for
     * generic operability.
     * <p>
     *@param formatMask A format mask to assign to this text area.
     *****************************************************************************************/
    public void setMask(Mask formatMask) {
        this.formatMask = formatMask;
    }

    /******************************************************************************************
     * Removes the mask assigned to this text area.
     *****************************************************************************************/
    public void removeMask() {
        formatMask = null;
    }

    /******************************************************************************************
     * Overrides paste() to make sure they do not paste more text than the area is allowed.
     *****************************************************************************************/
    public void paste() {
        super.paste();
        refresh();
        firePropertyChange(UIPropertyName.TEXT_COMPONENT_PASTE, false, true);
    }

    /******************************************************************************************
     * Refreshes the text area (this will trigger the formatting and line length logic).
     *****************************************************************************************/
    public void refresh() {
        setText(getText());
    }

    /******************************************************************************************
     * Assigns the text in the area. The text will be formatted if a mask exists. Text longer
     * than the allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text area.
     *****************************************************************************************/
    public void setText(String text) {
        if (text == null) {
            text = StringConstants.EMPTY;
        }
        if (formatMask != null) {
            text = formatMask.format(text);
            super.setText(text);
        }
        if (!StringConstants.EMPTY.equals(text) && lastSetTextValue.equals(text)) {
            return;
        }
        try {
            if (allowedLength > 0 && calculateLength() > allowedLength) {
                text = StringUtility.truncate(text, allowedLength - countDoubleCharacters());
            }
        } catch (Throwable exception) {
            text = StringConstants.EMPTY;
        }
        lastSetTextValue = text;
        super.setText(text);
    }

    /******************************************************************************************
     * Assigns the text in the text area, formatted from a string array through a mask.
     * <p>
     * @param textArray An array of strings to format through a mask.
     *****************************************************************************************/
    public void setText(String[] textArray) {
        setText(formatMask.format(textArray));
    }

    /******************************************************************************************
     * Appends text to the text area.
     *****************************************************************************************/
    public void append(String text) {
        if (text != null) {
            setText(getUnformattedText() + text);
        }
    }

    /******************************************************************************************
     * Retrieves the text string from the text area and returns the unformatted and trimmed
     * version of the text.
     * <p>
     * @return The unformatted and trimmed text in the text area.
     *****************************************************************************************/
    public String getText() {
        return getUnformattedText().trim();
    }

    /******************************************************************************************
     * Retrieves the text string from the text area and returns the unformatted version of the
     * text.
     * <p>
     * @return The unformatted text in the text area.
     *****************************************************************************************/
    protected String getUnformattedText() {
        String text = super.getText();
        if (text == null) {
            return StringConstants.EMPTY;
        }
        if (formatMask != null) {
            text = formatMask.unformat(text);
        }
        return text;
    }

    /******************************************************************************************
     * Retrieves whether or not the text area is empty.
     * <p>
     * @return True if the text area is empty, false if not.
     *****************************************************************************************/
    public boolean isEmpty() {
        return getText().length() == 0;
    }

    /******************************************************************************************
     * Clears the text area of input.
     *****************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Sets whether or not the text area has invalidly formatted data in the area.
     *<p>
     * @param formatInvalid True if the text area currently has invalid data, false if not.
     *****************************************************************************************/
    public void setFormatInvalid(boolean formatInvalid) {
        isFormatInvalid = formatInvalid;
    }

    /******************************************************************************************
     * Retrieves whether or not the information in the text area fails the format validation.
     * <p>
     * @return True if the format is invalid, false otherwise.
     *****************************************************************************************/
    public boolean isFormatInvalid() {
        return isFormatInvalid;
    }

    /******************************************************************************************
     * Implements the key listener interface "key released" method. If quick entries has been
     * activated for the text area, then the current built word must be reinitialized if a
     * key was released and the SHIFT and ALT is not pressed.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /******************************************************************************************
     * Implements the key listener interface "key pressed" method. The method tracks whether
     * or not the key pressed was a valid quick entry keystroke.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyPressed(KeyEvent event) {
    }

    /******************************************************************************************
     * Implements the key listener interface "key typed" method. It captures the key typed
     * action, checks to see if the allowable length is reached and if the key is not a
     * backspace/delete/left arrow key, then the key is ignored.
     * <p>
     * If the text in the area is selected, a new keystroke would replace the selected text
     * and by default that means we cannot allow the event to continue.
     * <p>
     * If quick entry has been activated, the system must look for SHIFT/ALT and then find
     * if the sequence of characters in the built up word exists in the quickmap. If it does
     * the information should replace the text in the text area.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyTyped(KeyEvent event) {
        if (getSelectedText() != null) {
            return;
        }
        if (calculateLength() >= allowedLength) {
            event.consume();
        }
    }

    /******************************************************************************************
     * Calcualtes the length of the text.
     *****************************************************************************************/
    protected int calculateLength() {
        try {
            int length = getUnformattedText().getBytes(StringConstants.DATABASE_ENCODING).length;
            return length + countDoubleCharacters();
        } catch (Throwable ex) {
            return allowedLength;
        }
    }

    /******************************************************************************************
     * Calculates the number of double-chars (basically tabs and new lines) found in the text.
     * Oracle stores these as two characters. Note: Does this have internationalization
     * implications?
     *****************************************************************************************/
    private int countDoubleCharacters() {
        String text = getUnformattedText();
        int loopLength = text.length();
        int count = 0;
        for (int i = 0; i < loopLength; i++) {
            switch (text.charAt(i)) {
                case '\n':
                case '\t':
                    count++;
                    break;
                default:
                    break;
            }
        }
        return count;
    }

    /******************************************************************************************
     *@see setBackground() in JTextArea.
     *****************************************************************************************/
    public void setBackground(Color color) {
        if (!isControllingColor) {
            defaultBackgroundColor = color;
        }
        super.setBackground(color);
    }

    /******************************************************************************************
     *@see setForeground() in JTextArea.
     *****************************************************************************************/
    public void setForeground(Color color) {
        if (!isControllingColor) {
            defaultForegroundColor = color;
        }
        super.setForeground(color);
    }

    /******************************************************************************************
     * Allows the developer to set whether or not the disable color scheme is active
     * irregardless of the system setting.
     * <p>
     * @param active True if the disable color scheme is active, false otherwise.
     *****************************************************************************************/
    public void setDisabledColorSchemeActive(boolean active) {
        isDisabledActiveFlag = active;
        updateColorState();
    }

    /******************************************************************************************
     * Sets the inactive background color of the text area.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setInactiveBackground(Color color) {
        if (color != null) {
            inactiveBackgroundColor = color;
        }
        updateColorState();
    }

    /******************************************************************************************
     * Retrieves the inactive background color of the text area.
     * <p>
     * @return The inactive background color.
     *****************************************************************************************/
    public Color getInactiveBackground() {
        return inactiveBackgroundColor;
    }

    /******************************************************************************************
     * Sets the inactive foreground color of the text area.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setInactiveForeground(Color color) {
        if (color != null) {
            inactiveForegroundColor = color;
        }
        updateColorState();
    }

    /******************************************************************************************
     * Retrieves the inactive foreground color of the text area.
     * <p>
     * @return The inactive foreground color.
     *****************************************************************************************/
    public Color getInactiveForeground() {
        return inactiveForegroundColor;
    }

    /******************************************************************************************
     * Overrides the superclass setVisible() to check permissions first.
     /******************************************************************************************/
    public void setVisible(boolean visible) {
        if (permission.equals(NavigationPermission.NONE)) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /******************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first.
     /******************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (!permission.equals(NavigationPermission.FULL)) {
            enabled = false;
        }
        super.setEnabled(enabled);
        setEditable(enabled);
        updateColorState();
    }

    /******************************************************************************************
     * Validates the permission of the object based on its identifier. If no identifier
     * exists, then the permission is true. If an identifier exists and the permission returns
     * as false, the component will not be able to be enabled()
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
    /******************************************************************************************/
    public void validatePermission(String ownerPrefix) throws UIException {
        if (permissionManager == null) {
            permissionManager = new UIPermissionManager();
        }
        permission = permissionManager.getComponentPermission(identifier, ownerPrefix);
        if (permission.equals(NavigationPermission.FULL)) {
            return;
        }
        if (permission.equals(NavigationPermission.NONE)) {
            setVisible(false);
        }
        setEnabled(false);
    }

    /******************************************************************************************
     * Updates the color state. This method ensures the color displayed by the widget is
     * correct based on the state of the widget. The controlling color flag is to keep the
     * set() methods from changing the default background color.
     *****************************************************************************************/
    private void updateColorState() {
        isControllingColor = true;
        if (!isFocusable() && isDisabledActiveFlag) {
            setBackground(inactiveBackgroundColor);
            setForeground(inactiveForegroundColor);
            setDisabledTextColor(inactiveForegroundColor);
            setCaretColor(defaultCaretColor);
        } else if (!isFocusable()) {
            setBackground(inactiveBackgroundColor);
            setForeground(defaultForegroundColor);
            setDisabledTextColor(defaultForegroundColor);
            setCaretColor(defaultCaretColor);
        } else {
            setBackground(defaultBackgroundColor);
            setForeground(defaultForegroundColor);
            setDisabledTextColor(defaultForegroundColor);
            setCaretColor(defaultCaretColor);
        }
        isControllingColor = false;
        repaint();
    }
}
