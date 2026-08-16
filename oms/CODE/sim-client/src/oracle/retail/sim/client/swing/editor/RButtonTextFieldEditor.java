package oracle.retail.sim.client.swing.editor;

import java.awt.GridBagLayout;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class represents a text field with a button instead of a title.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RButtonTextFieldEditor extends AbstractEditor {
    private static final long serialVersionUID = -8827434503271828795L;

    private JPanel innerPanel = new JPanel();
    private RButton titleLabel = new RButton();
    private RTextField valueField = new RTextField();

    private String buttonTitle = StringConstants.EMPTY;
    private int titleAlignment = EditorConstants.LEFT;
    private boolean cursorToFront = true;

    /****************************************************************************************************
     * Creates a new RButtonTextFieldEditor with no title.
     ***************************************************************************************************/
    public RButtonTextFieldEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RButtonTextFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RButtonTextFieldEditor(String title) {
        setTitle(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RButtonTextFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RButtonTextFieldEditor(String title, boolean required) {
        setTitle(title);
        setRequired(required);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /****************************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ***************************************************************************************************/
    public REditorLabel getLabel() {
        return new RPlainEditorLabel(titleLabel.getText());
    }

    /****************************************************************************************************
     * Retrieves the RTextField object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RTextfield widget to get access to methods that
     * are not provided.
     * <p>
     * @return The RTextField widget.
     ***************************************************************************************************/
    public RTextField getTextField() {
        return valueField;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueField.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifer is altered in an editor. Subclasses should override this
     * for any desired custom functionality.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueField.setIdentifier(identifier);
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return buttonTitle;
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            title = StringConstants.EMPTY;
        }
        buttonTitle = title;
        titleLabel.setText(title);
        validateTitle();
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM. The
     * label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleAlignment = alignment;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title alignment (see
     * EditorConstants).
     * <p>
     * @return The title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return titleAlignment;
    }

    /****************************************************************************************************
     * This method is ignored. Required is always false as this is not an entry field.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
    }

    /****************************************************************************************************
     * Always returns false as this is not an entry field.
     ***************************************************************************************************/
    public boolean isRequired() {
        return false;
    }

    /****************************************************************************************************
     * Validates the layout of the editor.
     ***************************************************************************************************/
    private void validateInnerLayout() {
        removeAll();

        validateInnerPanelLayout();

        switch (getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 5, 3, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 5, 3, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RButtonTextFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the inner panel widget/error label.
     ***************************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (getTitleAlignment()) {
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
        switch (getTitleAlignment()) {
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

    /****************************************************************************************************
     * Validates the visible of the title.
     ***************************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /****************************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include EditorConstants.SMALL,
     * EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     * @param sizeType The size type (SMALL, MEDIUM, or LARGE).
     ***************************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.LONGFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.LONGFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.LONGFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the text field.
     * <p>
     * @param width The width in pixels.
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
     * characters before it stops processing keystrokes. Note that characters are counted by the size of
     * bytes they will require in the database.
     * <p>
     * @param allowedLength The length in characters that the text field should allow.
     ***************************************************************************************************/
    public void setLength(int length) {
        valueField.setLength(length);
    }

    /****************************************************************************************************
     * Returns true if the default cursor text position is at the beginning of the text (after setText()
     * is called.
     * <p>
     * @return true if default position is beginning, false if end.
     ***************************************************************************************************/
    public void setDefaultCursorPositionToFront(boolean cursorToFront) {
        this.cursorToFront = cursorToFront;
        setText(getText());
    }

    /****************************************************************************************************
     * Returns true if the default cursor text position is at the beginning of the text (after setText()
     * is called.
     * <p>
     * @return true if default position is beginning, false if end.
     ***************************************************************************************************/
    public boolean isDefaultCursorPositionToFront() {
        return cursorToFront;
    }

    /****************************************************************************************************
     * Retrieves the text string from the text field and returns the unformatted and trimmed version of
     * the text.
     * <p>
     * @return The unformatted and trimmed text in the text field.
     ***************************************************************************************************/
    public String getText() {
        return valueField.getText();
    }

    /****************************************************************************************************
     * Retrieves the text string from the text field and returns the unformatted and trimmed version of
     * the text. An empty string will return null.
     * <p>
     * @return The unformatted and trimmed text in the text field.
     ***************************************************************************************************/
    public String getTextOrNull() {
        String text = valueField.getText();
        if (StringUtility.isNullOrEmpty(text)) {
            return null;
        }
        return text;
    }

    /****************************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text field.
     ***************************************************************************************************/
    public void setText(String text) {
        valueField.setText(text);
        if (isErrorState) {
            setErrorState(false);
        }
        if (cursorToFront) {
            valueField.setCaretPosition(0);
        }
    }

    /****************************************************************************************************
     * Assigns the tooltip text to the editor. This text will be assigned as the tip for both the field
     * and the button.
     * <p>
     * @param text The text to assign as the tooltip text.
     ***************************************************************************************************/
    public void setToolTipText(String text) {
        titleLabel.setToolTipText(text);
        valueField.setToolTipText(text);
    }

    /****************************************************************************************************
     * Retrieves the tooltip text to the editor.
     * <p>
     * return text The tool tip text.
     ***************************************************************************************************/
    public String getToolTipText() {
        return valueField.getToolTipText();
    }

    /****************************************************************************************************
     * Clears the text field of input.
     ***************************************************************************************************/
    public void clear() {
        valueField.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the text field is empty.
     * <p>
     * @return True if the text field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return valueField.isEmpty();
    }

    /****************************************************************************************************
     * Override the setVisible() to validate against permissions first.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label and to the RTextField.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        titleLabel.setEnabled(enabled);
        valueField.setEnabled(false);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        titleLabel.setEnabled(labelEnabled);
        valueField.setEnabled(false);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return false;
    }

    /****************************************************************************************************
     * This method does nothing. This class cannot have an error state.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState, String message) {
    }

    /****************************************************************************************************
     * This method does nothing. This class cannot have an error state.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the focus is lost and the state of the text field has
     * changed, the command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        titleLabel.registerAction(listener, command);
    }

    /****************************************************************************************************
     * Requests the focus move the text field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }
}
