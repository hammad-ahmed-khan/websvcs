package oracle.retail.sim.client.swing.editor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.CheckBoxActionAdapter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RCheckBox;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class represents a check box with a title label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RCheckBoxEditor extends AbstractEditor {
    private static final long serialVersionUID = -3746462782471648508L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RCheckBox valueCheckBox = new RCheckBox();

    private CheckBoxActionAdapter actionAdapter;
    private REventListener eventListener;

    /****************************************************************************************************
     * Creates a new RCheckBoxEditor with no title.
     ***************************************************************************************************/
    public RCheckBoxEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RCheckBoxEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RCheckBoxEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RCheckBoxEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RCheckBoxEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initialize() {
        actionAdapter = new CheckBoxEditorActionAdapter();
        actionAdapter.setSource(this);

        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        valueCheckBox.addItemListener(actionAdapter);
        valueCheckBox.addFocusListener(createManagerFocusListener());

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
        return titleLabel;
    }

    /****************************************************************************************************
     * Retrieves the RCheckBox object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RCheckBox widget to get access to methods that
     * are not provided.
     * <p>
     * @return The RCheckBox widget.
     ***************************************************************************************************/
    public RCheckBox getCheckBox() {
        return valueCheckBox;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueCheckBox.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifer is altered in an editor. Subclasses should override this
     * for any desired custom functionality.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueCheckBox.setIdentifier(identifier);
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM.
     * <p>
     * @param alignment The alignment to assign.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title alignment (see
     * EditorConstants).
     * <p>
     * @return The title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /****************************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /****************************************************************************************************
     * Validates the layout of the editor.
     ***************************************************************************************************/
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
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 1, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(2, 1, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RTextFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the inner panel widget/error label.
     ***************************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(valueCheckBox, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueCheckBox, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueCheckBox, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueCheckBox, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
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
                setMinimumWidth(UIManager.getInt(UIThemeName.CHECKBOX_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.CHECKBOX_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.CHECKBOX_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Assigns a minimum width to the check box area of the editor.
     * <p>
     * @param width The width (in pixels) to assign.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        Dimension dimension = new Dimension(width, valueCheckBox.getPreferredSize().height);
        valueCheckBox.setMinimumSize(dimension);
        valueCheckBox.setPreferredSize(dimension);
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
     * Override the setEnabled() of JPanel to call through to the label and to the RTextField.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueCheckBox.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueCheckBox.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return valueCheckBox.isEnabled();
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Sets the selected state of the button. Note that this method does not trigger an an
     * <code>actionEvent</code>. Call <code>doClick</code> to perform a programatic action change.
     * <p>
     * @param selected True if the button is selected, otherwise false.
     ***************************************************************************************************/
    public void setSelected(boolean selected) {
        valueCheckBox.setSelected(selected);
    }

    /****************************************************************************************************
     * Returns the selected state of the button. True if the toggle button is selected, false if it's
     * not.
     * <p>
     * @param selected True if the button is selected, otherwise false.
     ***************************************************************************************************/
    public boolean isSelected() {
        return valueCheckBox.isSelected();
    }

    /****************************************************************************************************
     * Returns whether or not content is missing in the editor. For a check box, this is always false.
     * <p>
     * @return false
     ***************************************************************************************************/
    public boolean isEmpty() {
        return !valueCheckBox.isSelected();
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the state of the check box changes, the command will be
     * sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        actionAdapter.setCommand(command);
        eventListener = listener;
    }

    /****************************************************************************************************
     * Retrieves the top level exception container for a given editor.
     * <p>
     * @param The top level ancestor of the value check box.
     ***************************************************************************************************/
    protected Container getExceptionContainer() {
        return valueCheckBox.getTopLevelAncestor();
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return valueCheckBox.isFocusOwner();
    }

    /****************************************************************************************************
     * Requests the focus move the check box within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueCheckBox.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueCheckBox, exception);
    }

    /****************************************************************************************************
     *
     * INNER CLASS CHECK BOX EDITOR ACTION ADAPTER - Handles check box actions
     *
     ***************************************************************************************************/
    private class CheckBoxEditorActionAdapter extends CheckBoxActionAdapter {

        public void performCheckBoxAction(RActionEvent event) {
            if (isErrorState) {
                setErrorState(false);
            }
            if (isActionEnabled && eventListener != null) {
                eventListener.performActionEvent(event);
            }
        }
    }
}
