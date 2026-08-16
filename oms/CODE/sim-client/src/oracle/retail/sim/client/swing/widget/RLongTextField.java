package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class sublcasses an RPanel and supplies a long field with a popup window for expanded text.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RLongTextField extends RPanel implements RetailComponent, PropertyChangeListener, REventListener {
    private static final long serialVersionUID = -3673546370456230229L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private RTextField valueField = new RTextField(); // For single line display
    private RIconButton popupButton = new RIconButton();

    private String dialogTitle = null;
    private String enteredText = StringConstants.EMPTY;
    private boolean isValueEditable = false;

    private static final String POPUP = "LongFieldPopup";

    /****************************************************************************************************
     * Return new RLongField object.
     ***************************************************************************************************/
    public RLongTextField() {
        initializeWidget();
        layoutWidget();
    }

    /****************************************************************************************************
     * Initializes the default settings.
     ***************************************************************************************************/
    private void initializeWidget() {
        valueField.addPropertyChangeListener(this);
        popupButton.setIcon(UIManager.getIcon(UIThemeName.LONGFIELD_ICON));
        popupButton.registerAction(this, POPUP);
        setEnabled(false);
    }

    /****************************************************************************************************
     * Lays out the text field and popup button within the widget.
     ***************************************************************************************************/
    private void layoutWidget() {
        setLayout(new GridBagLayout());
        add(valueField, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        add(popupButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() useable by
     * developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        this.identifier = identifier;
        valueField.setIdentifier(identifier);
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
     * Retrieves the text field used by the long field.
     * <p>
     * @param The text field used by the long field.
     ***************************************************************************************************/
    public RTextField getTextField() {
        return valueField;
    }

    /****************************************************************************************************
     * Assigns a title to the popup text window. A default title is supplied if none is set.
     * <p>
     * @param title The title of the popup dialog.
     ***************************************************************************************************/
    public void setPopupTitle(String title) {
        dialogTitle = Translator.getText(title);
    }

    /****************************************************************************************************
     * Retrieve the title of the popup dialog.
     * <p>
     * @return The title of the popup dialog.
     ***************************************************************************************************/
    public String getPopupTitle() {
        if (dialogTitle == null) {
            dialogTitle = Translator.getText("Edit Text");
        }
        return dialogTitle;
    }

    /****************************************************************************************************
     * Sets the minimum width of the component.
     * <p>
     * @param width The minimum width (number of pixels).
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        valueField.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * Sets the allowable text length based on the text field name. This will hook through the framework
     * into a properties file that defines the appropriate length of the name.
     * <p>
     * @param name The name to assign to the text field and look up the appropriate field size.
     ***************************************************************************************************/
    public void setLength(String name) {
        valueField.setLength(name);
    }

    /****************************************************************************************************
     * Sets the allowable text length. The text field will only allow users to enter up to this length in
     * characters before it stops processing keystrokes.
     * <p>
     * @param length The length in characters that the text field should allow.
     ***************************************************************************************************/
    public void setLength(int length) {
        valueField.setLength(length);
    }

    /****************************************************************************************************
     * Retrieves the date as a string. This retrieves the string that is within the dialog value area.
     * <p>
     * @return The date as a string.
     ***************************************************************************************************/
    public String getText() {
        if (valueField.isEnabled()) {
            return valueField.getText();
        }
        return enteredText;
    }

    /****************************************************************************************************
     * Retrieves the selected text within the value field.
     * <p>
     * @return The selected text.
     ***************************************************************************************************/
    public String getSelectedText() {
        return valueField.getSelectedText();
    }

    /****************************************************************************************************
     * Sets the text on the long field.
     * <p>
     * @param text The text.
     ***************************************************************************************************/
    public void setText(String text) {
        if (text == null) {
            text = StringConstants.EMPTY;
        }
        String oldText = enteredText;
        enteredText = text;
        valueField.setText(text);
        if (isValueEditable) {
            validateValueFieldState();
        }
        if (!oldText.equals(enteredText)) {
            firePropertyChange(UIPropertyName.TEXT_COMPONENT_DATA_ALTERED, oldText, enteredText);
        }
    }

    /****************************************************************************************************
     * Validates the state of the value field (not the editor as a whole)
     * <p>
     * @param text The text.
     ***************************************************************************************************/
    private void validateValueFieldState() {
        char[] characters = enteredText.toCharArray();
        for (char character : characters) {
            if (character == '\n') {
                valueField.setEditable(false);
                valueField.setEnabled(false);
                return;
            }
        }
        valueField.setEditable(true);
        valueField.setEnabled(true);
    }

    /****************************************************************************************************
     * Retrieves whether or not the long field is empty.
     * <p>
     * @return True if the long field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        if (valueField.isEditable() && valueField.isEnabled()) {
            return valueField.isEmpty();
        }
        return StringUtility.isNullOrEmpty(enteredText);
    }

    /****************************************************************************************************
     * Clears the long field of all information.
     ***************************************************************************************************/
    public void clear() {
        enteredText = StringConstants.EMPTY;
        valueField.clear();
    }

    /****************************************************************************************************
     * Selects all the text in the long field.
     ***************************************************************************************************/
    public void selectAll() {
        valueField.selectAll();
    }

    /****************************************************************************************************
     * Refreshes the displayed text in the long field (this will reformat the text).
     ***************************************************************************************************/
    public void refresh() {
        valueField.refresh();
    }

    /****************************************************************************************************
     * Transfers focus of the cursor to the long field.
     ***************************************************************************************************/
    public void requestFocus() {
        valueField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Transfers focus of the cursor to the long field.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Sets whether or not the long field is editable. This disables/enables the long field.
     * <p>
     * @param editable True if the long field should be editable, false if not.
     ***************************************************************************************************/
    public void setEditable(boolean editable) {
        throw new UnsupportedOperationException("This method cannot be accessed for this editor.");
    }

    /****************************************************************************************************
     * @see setBackground() in JTextField.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (valueField != null) {
            valueField.setBackground(color);
        }
    }

    /****************************************************************************************************
     * @see setForeground() in JTextField.
     ***************************************************************************************************/
    public void setForeground(Color color) {
        if (valueField != null) {
            valueField.setForeground(color);
        }
    }

    /****************************************************************************************************
     * @see setCaretPosition() in JTextField.
     ***************************************************************************************************/
    public void setCaretPosition(int position) {
        if (valueField != null) {
            valueField.setCaretPosition(position);
        }
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
        if (enabled) {
            isValueEditable = true;
            validateValueFieldState();
        } else {
            isValueEditable = false;
            valueField.setEnabled(enabled);
        }
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
        valueField.updateColorState();
    }

    /****************************************************************************************************
     * Listens for property change on the value field and responds.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(UIPropertyName.TEXT_COMPONENT_ENABLED)) {
            firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
        }
        if (name.equals(UIPropertyName.TEXT_COMPONENT_DATA_ALTERED)) {
            firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
        }
    }

    /****************************************************************************************************
     * Implement the action listener method to respond when the popup button is clicked.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        Container container = valueField.getTopLevelAncestor();

        RLongTextDialog dialog = null;
        if (container instanceof JFrame) {
            dialog = new RLongTextDialog((JFrame) container, getPopupTitle());
        } else if (container instanceof JDialog) {
            dialog = new RLongTextDialog((JDialog) container, getPopupTitle());
        }
        if (dialog != null) {
            WindowPlacer.alignToComponent(ApplicationInternal.getFrame(), this, dialog, true, false);
            dialog.addTextListener(createTextListener());
            dialog.setIdentifier(getIdentifier());
            dialog.setText(getText(), isValueEditable);
            dialog.setVisible(true);
        }
    }

    public RLongTextListener createTextListener() {
        return new RLongTextListener() {
            public void updateText(String text) {
                setText(text);
            }
        };
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return valueField.isFocusOwner() || popupButton.isFocusOwner();
    }
}
