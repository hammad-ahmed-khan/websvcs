package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
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

/********************************************************************************************************
 * This class sub-classes the standard JTextField class in the Swing package to provide custom
 * functionality for the client application.
 * <p>
 * This widgets has the ability to limit the number of character allowed in the field. RTextFields will
 * remain disabled until and entry length is specified.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RTextField extends JTextField implements RetailComponent, KeyListener, FocusListener {
    private static final long serialVersionUID = -6079463605261955606L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;

    private Color inactiveBackgroundColor;
    private Color inactiveForegroundColor;

    private Color focusBackgroundColor;
    private Color focusForegroundColor;

    private Color defaultCaretColor;
    private Color focusCaretColor;

    private boolean isControllingColor;

    private Mask formatMask;
    private boolean isFormatInvalid;
    private boolean isMaskLocked;

    private String lastTextValue = StringConstants.EMPTY;
    protected int allowedLength;
    protected boolean processKey;

    /****************************************************************************************************
     * Returns a new RTextField object.
     ***************************************************************************************************/
    public RTextField() {
        initializeColors();
        addFocusListener(this);
        addKeyListener(this);
        setEditable(false);
        setEnabled(false);
    }

    /****************************************************************************************************
     * Initializes the colors used in the text field.
     ***************************************************************************************************/
    private void initializeColors() {
        setBackground(UIManager.getColor(UIThemeName.TEXTFIELD_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.TEXTFIELD_FOREGROUND));

        setInactiveBackground(UIManager.getColor(UIThemeName.TEXTFIELD_INACTIVE_BACKGROUND));
        setInactiveForeground(UIManager.getColor(UIThemeName.TEXTFIELD_INACTIVE_FOREGROUND));

        focusBackgroundColor = UIManager.getColor(UIThemeName.TEXTFIELD_FOCUS_BACKGROUND);
        focusForegroundColor = UIManager.getColor(UIThemeName.TEXTFIELD_FOCUS_FOREGROUND);

        defaultCaretColor = UIManager.getColor(UIThemeName.TEXTFIELD_CARET_FOREGROUND);
        focusCaretColor = UIManager.getColor(UIThemeName.TEXTFIELD_CARET_FOCUS_FOREGROUND);
    }

    /****************************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() useable by
     * developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        setIdentifier(identifier, true);
    }

    /****************************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() useable by
     * developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     * @param validateLength True if the length of the field should be validated, false otherwise.
     ***************************************************************************************************/
    public void setIdentifier(String identifier, boolean validateLength) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        if (validateLength) {
            setLength(identifier);
        }
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the identifer to the component.
     * <p>
     * @return The identifier.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the text field.
     * <p>
     * @param width The width in pixels.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        Dimension dim = new Dimension(width, getPreferredSize().height);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width and height of the text field.
     * <p>
     * @param width The width in pixels.
     * @param height The height in pixels.
     ***************************************************************************************************/
    public void setMinimumSize(int width, int height) {
        Dimension dim = new Dimension(width, height);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width and height of the text field as well as the number of
     * characters allowed in the field.
     * <p>
     * @param width The width in pixels.
     * @param height The height in pixels.
     * @param length The length in characters that the text field should allow.
     ***************************************************************************************************/
    public void setDefaultSize(int width, int height, int length) {
        setMinimumSize(width, height);
        setLength(length);
    }

    /****************************************************************************************************
     * Sets the allowable text length based on the text field name. This will hook through the framework
     * into a properties file that defines the appropriate length of the name.
     * <p>
     * @param name The name to assign to the text field and look up the appropriate field size.
     ***************************************************************************************************/
    public void setLength(String name) {
        setLength(TextLengthTranslator.getLength(name));
    }

    /****************************************************************************************************
     * Sets the allowable text length. The text field will only allow users to enter up to this length in
     * characters before it stops processing keystrokes.
     * <p>
     * @param allowedLength The length in characters that the text field should allow.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Returns the allowable text length.
     * <p>
     * @return The length in characters that the text field should allow.
     ***************************************************************************************************/
    public int getLength() {
        return allowedLength;
    }

    /****************************************************************************************************
     * Returns whether this Component can be focused. This method is overridden to forcefully return
     * false if text field is not editable or not enabled. <p?
     * @return True if the text field is focusable, false if it is not.
     ***************************************************************************************************/
    public boolean isFocusable() {
        if (isEditable() && isEnabled()) {
            return super.isFocusable();
        }
        return false;
    }

    /****************************************************************************************************
     * Assigns a format mask to the text field. The specific mask is cast to the interface for generic
     * operability.
     * <p>
     * @param formatMask A format mask to assign to this text field.
     ***************************************************************************************************/
    public void setMask(Mask formatMask) {
        if (isMaskLocked) {
            throw new IllegalStateException("Mask is currently locked. Cannot assign new mask.");
        }
        this.formatMask = formatMask;
    }

    /****************************************************************************************************
     * Retrieves the format mask.
     * <p>
     * @return The format mask or null if none exists.
     ***************************************************************************************************/
    public Mask getMask() {
        return formatMask;
    }

    /****************************************************************************************************
     * Removes the mask assigned to this text field.
     ***************************************************************************************************/
    public void removeMask() {
        formatMask = null;
    }

    /****************************************************************************************************
     * Sets the mask currently assigned to the text field as locked. Setting a new mask will throw an
     * IllegalArgumentException on a locked mask.
     * <p>
     * @param locked True if the mask should be locked, false otherwise.
     ***************************************************************************************************/
    public void setMaskLocked(boolean locked) {
        isMaskLocked = locked;
    }

    /****************************************************************************************************
     * Retrieves whether or not the mask is locked.
     * <p>
     * @return True if the mask is locked, false otherwise.
     ***************************************************************************************************/
    public boolean isMaskLocked() {
        return isMaskLocked;
    }

    /****************************************************************************************************
     * Overrides paste() to make sure they do not paste more text than the field is allowed.
     ***************************************************************************************************/
    public void paste() {
        super.paste();
        refresh();
        lastTextValue = StringConstants.EMPTY;
        firePropertyChange(UIPropertyName.TEXT_COMPONENT_PASTE, false, true);
    }

    /****************************************************************************************************
     * Refreshes the text field (this will trigger the formatting and line length logic).
     ***************************************************************************************************/
    public void refresh() {
        setText(getText());
    }

    /****************************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text field.
     ***************************************************************************************************/
    public void setText(String text) {
        if (text == null) {
            text = StringConstants.EMPTY;
        }
        if (formatMask != null) {
            text = formatMask.format(text);
            super.setText(text);
        }
        if (!StringConstants.EMPTY.equals(text) && lastTextValue.equals(text)) {
            return;
        }
        text = validateAllowedLength(text);
        lastTextValue = text;
        super.setText(text);
    }

    /****************************************************************************************************
     * Helper method to shrink the text to the allowed length in the field. This should be overriden by
     * subclasses that would use a different method to calculated valid characters to count.
     ***************************************************************************************************/
    protected String validateAllowedLength(String text) {
        if (allowedLength > 0) {
            try {
                if (text.getBytes(StringConstants.DATABASE_ENCODING).length > allowedLength) {
                    return StringUtility.truncate(text, allowedLength);
                }
            } catch (UnsupportedEncodingException uex) {
                return StringConstants.EMPTY;
            }
        }
        return text;
    }

    /****************************************************************************************************
     * Assigns the text in the text field, formatted from a string array through a mask.
     * <p>
     * @param textArray An array of strings to format through a mask.
     ***************************************************************************************************/
    public void setText(String[] textArray) {
        setText(formatMask.format(textArray));
    }

    /****************************************************************************************************
     * Retrieves the text string from the text field and returns the unformatted and trimmed version of
     * the text.
     * <p>
     * @return The unformatted and trimmed text in the text field.
     ***************************************************************************************************/
    public String getText() {
        return getUnformattedText().trim();
    }

    /****************************************************************************************************
     * Retrieves the text string from the text field and returns the unformatted and trimmed version of
     * the text.
     * <p>
     * @return The unformatted and trimmed text in the text field. Null if the text field is empty.
     ***************************************************************************************************/
    public String getTextOrNull() {
        return StringUtility.trimToNull(getUnformattedText());
    }

    /****************************************************************************************************
     * Retrieves the text string from the text field and returns the unformatted version of the text.
     * <p>
     * @return The unformatted text in the text field.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Retrieves whether or not the text field is empty.
     * <p>
     * @return True if the text field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return getText().length() == 0;
    }

    /****************************************************************************************************
     * Clears the text field of input.
     ***************************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Sets whether or not the text field has invalidly formatted data in the field.
     * <p>
     * @param formatInvalid True if the text field currently has invalid data, false if not.
     ***************************************************************************************************/
    public void setFormatInvalid(boolean formatInvalid) {
        isFormatInvalid = formatInvalid;
    }

    /****************************************************************************************************
     * Retrieves whether or not the information in the text field fails the format validation.
     * <p>
     * @return True if the format is invalid, false otherwise.
     ***************************************************************************************************/
    public boolean isFormatInvalid() {
        return isFormatInvalid;
    }

    /****************************************************************************************************
     * Implements the key listener interface "key released" method.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /****************************************************************************************************
     * Implements the key listener interface "key pressed" method. The method tracks whether or not the
     * key pressed was a valid quick entry keystroke.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_LEFT:
                processKey = false;
                break;
            default:
                processKey = true;
        }
    }

    /****************************************************************************************************
     * Implements the key listener interface "key typed" method. It captures the key typed action, checks
     * to see if the allowable byte length is reached and if the key is not a backspace/delete/left arrow
     * key, then the key is ignored.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent event) {
        if (formatMask != null && !formatMask.validCharacter(event.getKeyChar())) {
            event.consume();
        }
        if (getSelectedText() != null) {
            return;
        }
        if (processKey && calculateLength() >= allowedLength) {
            event.consume();
        }
    }

    /****************************************************************************************************
     * Calcualtes the length of the text.
     ***************************************************************************************************/
    protected int calculateLength() {
        try {
            int length = getUnformattedText().getBytes(StringConstants.DATABASE_ENCODING).length;
            return length + countDoubleCharacters();
        } catch (Throwable ex) {
            return allowedLength;
        }
    }

    /****************************************************************************************************
     * Calculates the number of double-chars (basically tabs and new lines) found in the text. Oracle
     * stores these as two characters. Note: Does this have internationalization implications?
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Update color state on focus gained.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
        updateColorState();
        selectAll();
    }

    /****************************************************************************************************
     * Update color state on focus lost.
     ***************************************************************************************************/
    public void focusLost(FocusEvent event) {
        updateColorState();
        refresh();
    }

    /****************************************************************************************************
     * @see setBackground() in JTextField.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (!isControllingColor) {
            defaultBackgroundColor = color;
        }
        super.setBackground(color);
    }

    /****************************************************************************************************
     * @see setForeground() in JTextField.
     ***************************************************************************************************/
    public void setForeground(Color color) {
        if (!isControllingColor) {
            defaultForegroundColor = color;
        }
        super.setForeground(color);
    }

    /****************************************************************************************************
     * Sets the inactive background color of the text field.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setInactiveBackground(Color color) {
        if (color != null) {
            inactiveBackgroundColor = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the inactive background color of the text field.
     * <p>
     * @return The inactive background color.
     ***************************************************************************************************/
    public Color getInactiveBackground() {
        return inactiveBackgroundColor;
    }

    /****************************************************************************************************
     * Sets the inactive foreground color of the text field.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setInactiveForeground(Color color) {
        if (color != null) {
            inactiveForegroundColor = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the inactive foreground color of the text field.
     * <p>
     * @return The inactive foreground color.
     ***************************************************************************************************/
    public Color getInactiveForeground() {
        return inactiveForegroundColor;
    }

    /****************************************************************************************************
     * Overrides the superclass setVisible() to check permissions first. /
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (permission.equals(NavigationPermission.NONE)) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /****************************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first. /
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (!permission.equals(NavigationPermission.FULL)) {
            enabled = false;
        }
        super.setEnabled(enabled);
        setEditable(enabled);
        updateColorState();
    }

    /****************************************************************************************************
     * Validates the permission of the object based on its identifier. If no identifier exists, then the
     * permission is true. If an identifier exists and the permission returns as false, the component
     * will not be able to be enabled()
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission. /
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Updates the color state. This method ensures the color displayed by the widget is correct based on
     * the state of the widget. The controlling color flag is to keep the set() methods from changing the
     * default background color.
     ***************************************************************************************************/
    public void updateColorState() {
        isControllingColor = true;
        if (!isFocusable() && UIManager.getBoolean(UIThemeName.SYSTEM_DISABLED_VALUE_ACTIVE)) {
            setBackground(inactiveBackgroundColor);
            setForeground(inactiveForegroundColor);
            setDisabledTextColor(inactiveForegroundColor);
            setCaretColor(defaultCaretColor);
        } else if (!isFocusable()) {
            setBackground(inactiveBackgroundColor);
            setForeground(defaultForegroundColor);
            setDisabledTextColor(defaultForegroundColor);
            setCaretColor(defaultCaretColor);
        } else if (isFocusOwner()) {
            setBackground(focusBackgroundColor);
            setForeground(focusForegroundColor);
            setDisabledTextColor(defaultForegroundColor);
            setCaretColor(focusCaretColor);
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
