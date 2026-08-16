package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.TextLengthTranslator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class sub-classes JPasswordField in order to supply additional functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RPasswordField extends JPasswordField implements RetailComponent, FocusListener, KeyListener {
    private static final long serialVersionUID = 1878192104258232886L;

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

    private int allowedLength;
    private boolean processKey;

    /******************************************************************************************
     * Returns a new RPasswordField object.
     *****************************************************************************************/
    public RPasswordField() {
        initializeColors();
        addFocusListener(this);
        addKeyListener(this);
        setEditable(false);
        setEnabled(false);
    }

    /******************************************************************************************
     * Initializes the colors used in the text field.
     *****************************************************************************************/
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
     * Retrieves the identifier to the component.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the password field.
     * <p>
     *@param width The width in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        Dimension dim = new Dimension(width, getPreferredSize().height);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the password field.
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
     * Sets the minimum and preferred width and height of the password field as well as the number
     * of characters allowed in the field.
     * <p>
     *@param width The width in pixels.
     *@param height The height in pixels.
     *@param length The length in characters that the text field should allow.
     *****************************************************************************************/
    public void setDefaultSize(int width, int height, int length) {
        setMinimumSize(width, height);
        setLength(length);
    }

    /******************************************************************************************
     * Sets the allowable text length based on the field name. This will hook through
     * the framework into a properties file that defines the appropriate length of the name.
     * <p>
     *@param name The name to assign to the password field and look up the appropriate field size.
     *****************************************************************************************/
    public void setLength(String name) {
        setLength(TextLengthTranslator.getLength(name));
    }

    /******************************************************************************************
     * Sets the allowable text length. The text field will only allow users to enter up to this
     * length in characters before it stops processing keystrokes.
     * <p>
     *@param allowedLength The length in characters that the text field should allow.
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
     *@return The length in characters that the password field should allow.
     *****************************************************************************************/
    public int getLength() {
        return allowedLength;
    }

    /******************************************************************************************
     * Returns whether this Component can be focused. This method is overridden to forcefully
     * return false if text field is not editable or not enabled.
     * <p>
     * @return True if the text field is focusable, false if it is not.
     *****************************************************************************************/
    public boolean isFocusable() {
        return isEditable() && isEnabled() && super.isFocusable();
    }

    /******************************************************************************************
     * Overridden to return an empty string. This method is not allowed.
     *****************************************************************************************/
    public String getText() {
        return StringConstants.EMPTY;
    }

    /******************************************************************************************
     * Assigns the text in the field. Text longer than the allowed length is truncated.
     * <p>
     * @param text The text to place in the text field.
     *****************************************************************************************/
    public void setText(String text) {
        if (text != null) {
            try {
                if (allowedLength > 0 && text.getBytes(StringConstants.DATABASE_ENCODING).length > allowedLength) {
                    text = StringUtility.truncate(text, allowedLength);
                }
            } catch (UnsupportedEncodingException e) {
                text = null;
            }
        }
        if (text == null) {
            text = StringConstants.EMPTY;
        }
        super.setText(text);
    }

    /******************************************************************************************
     * Retrieves whether or not the text field is empty.
     * <p>
     * @return True if the text field is empty, false if not.
     *****************************************************************************************/
    public boolean isEmpty() {
        return getPassword().length == 0;
    }

    /******************************************************************************************
     * Clears the text field of input.
     *****************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Implements the key listener interface "key released" method.
     *****************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /******************************************************************************************
     * Implements the key listener interface "key pressed" method. The method tracks whether
     * or not the key pressed was a processable keystroke.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
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

    /******************************************************************************************
     * Implements the key listener interface "key typed" method. It captures the key typed
     * action, checks to see if the allowable length is reached and if the key is not a
     * backspace/delete/left arrow key, then the key is ignored.
     * <p>
     * If the text in the field is selected, a new keystroke would replace the selected text
     * and by default that means we cannot allow the event to continue.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyTyped(KeyEvent event) {
        if (getSelectedText() != null) {
            return;
        }
        try {
            if (processKey) {
                String tempPassword = new String(getPassword());
                if (tempPassword.getBytes(StringConstants.DATABASE_ENCODING).length >= allowedLength) {
                    event.consume();
                }
            }
        } catch (UnsupportedEncodingException exception) {
            event.consume();
        }
    }

    /******************************************************************************************
     * Update color state on focus gained.
     *****************************************************************************************/
    public void focusGained(FocusEvent event) {
        updateColorState();
        selectAll();
    }

    /******************************************************************************************
     * Update color state on focus lost.
     *****************************************************************************************/
    public void focusLost(FocusEvent event) {
        updateColorState();
    }

    /******************************************************************************************
     *@see setBackground() in JTextField.
     *****************************************************************************************/
    public void setBackground(Color color) {
        if (!isControllingColor) {
            defaultBackgroundColor = color;
        }
        super.setBackground(color);
    }

    /******************************************************************************************
     *@see setForeground() in JTextField.
     *****************************************************************************************/
    public void setForeground(Color color) {
        if (!isControllingColor) {
            defaultForegroundColor = color;
        }
        super.setForeground(color);
    }

    /******************************************************************************************
     * Sets the inactive background color of the text field.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setInactiveBackground(Color color) {
        if (color != null) {
            inactiveBackgroundColor = color;
        }
    }

    /******************************************************************************************
     * Retrieves the inactive background color of the text field.
     * <p>
     * @return The inactive background color.
     *****************************************************************************************/
    public Color getInactiveBackground() {
        return inactiveBackgroundColor;
    }

    /******************************************************************************************
     * Sets the inactive foreground color of the text field.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setInactiveForeground(Color color) {
        if (color != null) {
            inactiveForegroundColor = color;
        }
    }

    /******************************************************************************************
     * Retrieves the inactive foreground color of the text field.
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
        if (permission == NavigationPermission.NONE) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /******************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first.
     /******************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (permission != NavigationPermission.FULL) {
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
        if (permission == NavigationPermission.FULL) {
            return;
        }
        if (permission == NavigationPermission.NONE) {
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
