package oracle.retail.sim.client.swing.editor;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RPasswordField;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class represents a password field with a title label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RPasswordFieldEditor extends AbstractEditor implements PropertyChangeListener {
    private static final long serialVersionUID = -4910800305223431035L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RPasswordField valueField = new RPasswordField();

    private REventListener emptyActionListener;
    private KeyListener emptyKeyListener;

    /******************************************************************************************
     * Creates a new RPasswordFieldEditor with no title.
     ******************************************************************************************/
    public RPasswordFieldEditor() {
        initialize();
    }

    /******************************************************************************************
     * Creates a new RPasswordFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public RPasswordFieldEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /******************************************************************************************
     * Creates a new RPasswordFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ******************************************************************************************/
    public RPasswordFieldEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /******************************************************************************************
     * Initializes the editor.
     ******************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        titleLabel.setEnabled(false);

        valueField.addPropertyChangeListener(this);
        valueField.addKeyListener(createErrorKeyListener());
        valueField.addFocusListener(createManagerFocusListener());

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /******************************************************************************************
     * Retrieves the error state key listener.
     ******************************************************************************************/
    private KeyListener createErrorKeyListener() {
        return new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
            }

            public void keyTyped(KeyEvent event) {
                if (isErrorState) {
                    setErrorState(false);
                }
            }
        };
    }

    /******************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ******************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /******************************************************************************************
     * Retrieves the RPasswordField object of the editor. Many helpful methods have been added to
     * the editor, but the developer can always retrieve the actual RPasswordField widget to get
     * access to methods that are not provided.
     * <p>
     * @return The RTextField widget.
     ******************************************************************************************/
    public RPasswordField getPasswordField() {
        return valueField;
    }

    /******************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal
     * component rather than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ******************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueField.addKeyListener(listener);
    }

    /******************************************************************************************
     * This method is called when the identifer is altered in an editor.
     ******************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueField.setIdentifier(identifier);
    }

    /******************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ******************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /******************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /******************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM.
     * The label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     *****************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /******************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title
     * alignment (see EditorConstants).
     * <p>
     * @return The title alignment.
     *****************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /******************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     *****************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
    }

    /******************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     *****************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /******************************************************************************************
     * Validates the layout of the editor.
     ******************************************************************************************/
    private void validateInnerLayout() {
        removeAll();

        validateInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RPasswordFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /******************************************************************************************
     * Validates the inner panel widget/error label.
     ******************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(valueField, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
        }
    }

    /******************************************************************************************
     * Validates the visible of the title.
     ******************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /******************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include
     * EditorConstants.SMALL, EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     *@param sizeType The size type (SMALL, MEDIUM, or LARGE).
     *****************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.PASSWORDFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.PASSWORDFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.PASSWORDFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the text field.
     * <p>
     *@param width The width in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        valueField.setMinimumWidth(width);
    }

    /******************************************************************************************
     * Sets the allowable text length based on the password field name. This will hook through
     * the framework into a properties file that defines the appropriate length of the name.
     * <p>
     *@param name The name to assign to the text field and look up the appropriate field size.
     *****************************************************************************************/
    public void setLength(String name) {
        valueField.setLength(name);
    }

    /******************************************************************************************
     * Sets the allowable text length. The password field will only allow users to enter up to this
     * length in characters before it stops processing keystrokes.
     * <p>
     *@param allowedLength The length in characters that the text field should allow.
     *****************************************************************************************/
    public void setLength(int length) {
        valueField.setLength(length);
    }

    /******************************************************************************************
     * Retrieves the password of the password field.
     * <p>
     * @return The password.
     *****************************************************************************************/
    public char[] getPassword() {
        return valueField.getPassword();
    }

    /******************************************************************************************
     * Assign text to the password field. This should only be used 
     *****************************************************************************************/
    public void setText(String text) {
        valueField.setText(text);
    }

    /******************************************************************************************
     * Clears the password field of input.
     *****************************************************************************************/
    public void clear() {
        valueField.clear();
    }

    /******************************************************************************************
     * Retrieves whether or not the password field is empty.
     * <p>
     * @return True if the password field is empty, false if not.
     *****************************************************************************************/
    public boolean isEmpty() {
        return valueField.isEmpty();
    }

    /******************************************************************************************
     * Override the setVisible() of JPanel to call through to all widgets.
     *****************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /******************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RTextField.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueField.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueField.setEnabled(fieldEnabled);
    }

    /******************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     *****************************************************************************************/
    public boolean isEnabled() {
        return valueField.isEnabled();
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     *****************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     *****************************************************************************************/
    public void setErrorState(boolean errorState, String errorText) {
        isErrorState = errorState;

        if (isErrorState) {
            errorLabel.setIcon(errorIcon);
            errorLabel.setToolTipText(errorText);
        } else {
            errorLabel.setIcon(null);
            errorLabel.setToolTipText(StringConstants.EMPTY);
        }
    }

    /******************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Registers to receive actions from a widget when keystrokes occur in the widget. This
     * sends the commands Editor.TEXT_COMPONENT_EMPTY or Editor.TEXT_COMPONENT_NOT_EMPTY, when
     * the widget changes state.
     * <p>
     * @param listener The object that should receive the RActionEvent.
     ******************************************************************************************/
    public void registerEmptyStateAction(REventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("REventListener cannot be null!");
        }
        valueField.addKeyListener(getEmptyStateKeyListener());
        emptyActionListener = listener;
    }

    /******************************************************************************************
     * Removes an empty state from the editor. An action will no longer be triggered for this
     * widget when keys are pressed.
     ******************************************************************************************/
    public void unregisterEmptyStateAction() {
        valueField.removeKeyListener(getEmptyStateKeyListener());
        emptyActionListener = null;
    }

    /******************************************************************************************
     * Retrieves the empty state key listener.
     ******************************************************************************************/
    private KeyListener getEmptyStateKeyListener() {
        if (emptyKeyListener == null) {
            emptyKeyListener = new KeyListener() {
                public void keyReleased(KeyEvent event) {
                }

                public void keyPressed(KeyEvent event) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.VK_DELETE:
                        case KeyEvent.VK_BACK_SPACE:
                            doEmptyStateTextRemoved();
                            break;
                        default:
                            break;
                    }
                }

                public void keyTyped(KeyEvent event) {
                    switch (event.getKeyChar()) {
                        case KeyEvent.VK_BACK_SPACE:
                        case KeyEvent.VK_DELETE:
                        case KeyEvent.VK_SPACE:
                        case KeyEvent.VK_TAB:
                            break;
                        default:
                            doEmptyStateKeyTyped();
                            break;
                    }
                }
            };
        }
        return emptyKeyListener;
    }

    /******************************************************************************************
     * This method is called when it is possible that a keystroke removed a character from the
     * text field. If this causes the field to go from containing data to being empty, then
     * an event is sent to the listener.
     ******************************************************************************************/
    private void doEmptyStateTextRemoved() {
        if (emptyActionListener != null) {
            int length = valueField.getPassword().length;
            String selectedText = valueField.getSelectedText();

            if (!StringUtility.isNullOrEmpty(selectedText)) {
                length = length - selectedText.trim().length();
            }
            if (length <= 1) {
                RActionEvent event = new RActionEvent(this, UIPropertyName.TEXT_COMPONENT_EMPTY, getIdentifier());
                emptyActionListener.performActionEvent(event);
            }
        }
    }

    /******************************************************************************************
     * This method is called when it is possible that a keystroke added a character to the
     * text field. If the keystroke caused the field to go from being empty to containing data
     * an event is sent to the listener.
     ******************************************************************************************/
    private void doEmptyStateKeyTyped() {
        if (emptyActionListener != null) {
            if (valueField.isEmpty()) {
                RActionEvent event = new RActionEvent(this, UIPropertyName.TEXT_COMPONENT_NOT_EMPTY, getIdentifier());
                emptyActionListener.performActionEvent(event);
            }
        }
    }

    /******************************************************************************************
     * Registers an action with the editor. When the state of the password field changes, the
     * command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     *****************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        throw new UnsupportedOperationException("This operation is not supported for RPasswordFieldEditor.");
    }

    /******************************************************************************************
     * Implements the property change listener method to disable and enable the title along
     * with the actual text field.
     *****************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(UIPropertyName.TEXT_COMPONENT_ENABLED)) {
            titleLabel.setEnabled(((Boolean) event.getNewValue()).booleanValue());
        }
    }

    /******************************************************************************************
     * Retrieves the top level exception container for a given editor.
     * <p>
     * @param The top level ancestor of the password field.
     *****************************************************************************************/
    protected Container getExceptionContainer() {
        return valueField.getTopLevelAncestor();
    }

    /******************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     *****************************************************************************************/
    public boolean isFocusOwner() {
        return valueField.isFocusOwner();
    }

    /******************************************************************************************
     * Requests the focus move the password field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely
     * to succeed.
     *****************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /******************************************************************************************
     * Handles displaying an exception on the application.
     *****************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }
}
