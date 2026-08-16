package oracle.retail.sim.client.swing.editor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class represents a radio button grouping with a label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RRadioButtonEditor extends AbstractEditor implements ItemListener {
    private static final long serialVersionUID = 233905069524942291L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RPanel radioPanel = new RPanel();

    private Map radioMap = new HashMap();
    private List radioList = new ArrayList();
    private ButtonGroup radioGroup = new ButtonGroup();
    private RRadioButton emptyButton = new RRadioButton();

    private REventListener eventListener;
    private String eventCommand;
    private int titleAlignment = EditorConstants.LEFT;
    private int textPosition = EditorConstants.LEFT;

    /******************************************************************************************
     * Creates a new RRadioButtonEditor with no title.
     ******************************************************************************************/
    public RRadioButtonEditor() {
        initialize();
    }

    /******************************************************************************************
     * Creates a new RRadioButtonEditor with a title.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public RRadioButtonEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /******************************************************************************************
     * Creates a new RRadioButtonEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ******************************************************************************************/
    public RRadioButtonEditor(String title, boolean required) {
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

        radioGroup.add(emptyButton);

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
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
     * Retrieves the internal radio button panel that contains all the radio buttons.
     * <p>
     * @return The radio button panel.
     ******************************************************************************************/
    public RPanel getRadioPanel() {
        return radioPanel;
    }

    /******************************************************************************************
     * Retrieves a RRadioButton from the editor based on its title.
     * <p>
     * @param title The title to search for.
     * @return The RRadioButton with the title.
     ******************************************************************************************/
    public RRadioButton getRadioButton(String title) {
        return (RRadioButton) radioMap.get(title);
    }

    /******************************************************************************************
     * Assigns all the radio buttons of the editor. It will remove all radio buttons prior
     * to assigning the new ones.
     * <p>
     * @param titleArray An array of titles to assign to the buttons.
     ******************************************************************************************/
    public void setRadioButtons(String[] titleArray) {
        setRadioButtons(titleArray, 1, titleArray.length);
    }

    /******************************************************************************************
     * Assigns all the radio buttons of the editor. It will remove all radio buttons prior
     * to assigning the new ones.
     * <p>
     * @param titleArray An array of titles to assign to the buttons.
     ******************************************************************************************/
    public void setRadioButtons(String[] titleArray, int rows, int columns) {
        for (Iterator iterator = radioMap.keySet().iterator(); iterator.hasNext();) {
            removeRadioButton((String) iterator.next());
        }

        radioPanel.removeAll();
        radioPanel.setLayout(new GridLayout(rows, columns));

        for (String element : titleArray) {
            addRadioButton(element);
        }
    }

    /******************************************************************************************
     * Adds a new radio button to the editor. The title will be automatically translated for
     * display, although the untranslated title is used as future identifiers of the button
     * for purposes of method calls.
     * <p>
     * @param title The title of the radio button to add.
     ******************************************************************************************/
    public void addRadioButton(String title) {
        if (!StringUtility.isNullOrEmpty(title)) {
            RRadioButton button = new RRadioButton(title);
            button.addItemListener(this);
            applyTextPosition(button);
            radioGroup.add(button);
            radioList.add(button);
            radioMap.put(title, button);
            radioPanel.add(button);
        }
    }

    /******************************************************************************************
     * Removes a radio button from the editor.
     * <p>
     * @param title The title of the radio button to remove.
     ******************************************************************************************/
    public void removeRadioButton(String title) {
        if (!StringUtility.isNullOrEmpty(title)) {
            RRadioButton button = (RRadioButton) radioMap.remove(title);
            if (button != null) {
                radioList.remove(button);
                radioGroup.remove(button);
                radioPanel.remove(button);
            }
        }
    }

    /******************************************************************************************
     * Assigns the position that the radio text should display at. Valid values are
     * EditorConstants.TOP, EditorConstants.BOTTOM, EditorConstants.LEFT and EditorConstants.RIGHT.
     * <p>
     * @param position The position of the text.
     ******************************************************************************************/
    public void setRadioTextPosition(int position) {
        if (position < EditorConstants.TOP || position > EditorConstants.RIGHT) {
            throw new IllegalArgumentException("Position not valid!");
        }

        textPosition = position;

        for (Iterator iterator = radioList.iterator(); iterator.hasNext();) {
            applyTextPosition((RRadioButton) iterator.next());
        }
    }

    /******************************************************************************************
     * Helper method to apply the current text position to a button
     ******************************************************************************************/
    private void applyTextPosition(RRadioButton button) {
        switch (textPosition) {
            case EditorConstants.RIGHT:
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setHorizontalTextPosition(SwingConstants.RIGHT);
                break;
            case EditorConstants.TOP:
                button.setHorizontalAlignment(SwingConstants.CENTER);
                button.setHorizontalTextPosition(SwingConstants.TOP);
                break;
            case EditorConstants.BOTTOM:
                button.setHorizontalAlignment(SwingConstants.CENTER);
                button.setHorizontalTextPosition(SwingConstants.BOTTOM);
                break;
            default:
                button.setHorizontalAlignment(SwingConstants.RIGHT);
                button.setHorizontalTextPosition(SwingConstants.LEFT);
        }
    }

    /******************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal
     * component rather than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ******************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        for (Iterator iterator = radioList.iterator(); iterator.hasNext();) {
            ((RRadioButton) iterator.next()).addKeyListener(listener);
        }
    }

    /******************************************************************************************
     * Empty implementation
     ******************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
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
     * @param suffixEnabled True if the editor should use the label suffix feature, otherwise false.
     *****************************************************************************************/
    public void setTitleAlignment(int alignment) {
        if (alignment < EditorConstants.TOP || alignment > EditorConstants.BOTTOM) {
            throw new IllegalArgumentException("Invalid alignment for editor!");
        }
        titleAlignment = alignment;
        validateInnerLayout();
    }

    /******************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title
     * alignment (see EditorConstants).
     * <p>
     * @return The title alignment.
     *****************************************************************************************/
    public int getTitleAlignment() {
        return titleAlignment;
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

        switch (titleAlignment) {
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

    /******************************************************************************************
     * Validates the inner panel widget/error label.
     ******************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleAlignment) {
                case EditorConstants.RIGHT:
                    innerPanel.add(radioPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(radioPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleAlignment) {
            case EditorConstants.RIGHT:
                innerPanel.add(radioPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(radioPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
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
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /******************************************************************************************
     * Assigns a minimum width to the radio button panel.
     * <p>
     *@param width The width (in pixels) to assign.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        Dimension dimension = new Dimension(width, radioPanel.getPreferredSize().height);
        radioPanel.setMinimumSize(dimension);
        radioPanel.setPreferredSize(dimension);
    }

    /******************************************************************************************
     * Override the setVisible() to validate against permissions first.
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
        for (Iterator iterator = radioMap.values().iterator(); iterator.hasNext();) {
            ((RRadioButton) iterator.next()).setEnabled(enabled);
        }
        titleLabel.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        for (Iterator iterator = radioMap.values().iterator(); iterator.hasNext();) {
            ((RRadioButton) iterator.next()).setEnabled(fieldEnabled);
        }
        titleLabel.setEnabled(labelEnabled);
    }

    /******************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RTextField.
     *****************************************************************************************/
    public void setEnabled(String name, boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        RRadioButton button = (RRadioButton) radioMap.get(name);
        if (button != null) {
            button.setEnabled(enabled);
        }
    }

    /******************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     *****************************************************************************************/
    public boolean isEnabled() {
        if (radioList.isEmpty()) {
            return false;
        }
        return ((RRadioButton) radioList.get(0)).isEnabled();
    }

    /******************************************************************************************
     * Retrieves whether or not the radio button associated with title is selected.
     * <p>
     * @param title The title of the radio button.
     * @return True if the radio button is selected, false otherwise
     *****************************************************************************************/
    public boolean isSelected(String title) {
        if (!StringUtility.isNullOrEmpty(title)) {
            RRadioButton button = (RRadioButton) radioMap.get(title);
            if (button != null) {
                return button.isSelected();
            }
        }
        return false;
    }

    /******************************************************************************************
     * Return true if at least one radio button is selected, false otherwise.
     *****************************************************************************************/
    public boolean hasSelection() {
        return !emptyButton.isSelected();
    }

    /******************************************************************************************
     * Clears all selections within the editor.
     *****************************************************************************************/
    public void clearSelection() {
        emptyButton.setSelected(true);
    }

    /******************************************************************************************
     * Sets a radio button selected.
     * <p>
     * @param title The title of the radio button.
     * @param selected True if the radio button should be selected, false otherwise
     *****************************************************************************************/
    public void setSelected(String title, boolean selected) {
        if (!StringUtility.isNullOrEmpty(title)) {
            RRadioButton button = (RRadioButton) radioMap.get(title);
            if (button != null) {
                button.setSelected(selected);
            }
        }
    }

    /******************************************************************************************
     * Assigns whether or not a specific radio button is visible within the editor.
     * <p>
     * @param title The title of the radio button.
     * @param visible True if the radio button should be visible, false otherwise
     *****************************************************************************************/
    public void setVisible(String title, boolean visible) {
        if (!StringUtility.isNullOrEmpty(title)) {
            RRadioButton button = (RRadioButton) radioMap.get(title);
            if (button != null) {
                button.setVisible(visible);
            }
        }
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
     * Retrieves whether or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Returns whether or not content is missing in the editor. For a check box, this is
     * always false.
     *<p>
     * @return false
     *****************************************************************************************/
    public boolean isEmpty() {
        return false;
    }

    /******************************************************************************************
     * Registers an action with the editor. When the state of the check box changes, the
     * command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the notification.
     *****************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventCommand = command;
        eventListener = listener;
    }

    /******************************************************************************************
     * Item Listener for radio buttons
     *****************************************************************************************/
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            if (isActionEnabled && eventListener != null && eventCommand != null) {
                eventListener.performActionEvent(new RActionEvent(this, eventCommand));
            }
        }
    }

    /******************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     *****************************************************************************************/
    public boolean isFocusOwner() {
        for (Iterator iterator = radioList.iterator(); iterator.hasNext();) {
            if (((RRadioButton) iterator.next()).isFocusOwner()) {
                return true;
            }
        }
        return false;
    }

    /******************************************************************************************
     * Requests the focus move the check box within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely
     * to succeed.
     *****************************************************************************************/
    public boolean requestFocusInWindow() {
        if (radioList.isEmpty()) {
            return false;
        }
        return ((RRadioButton) radioList.get(0)).requestFocusInWindow();
    }

    /******************************************************************************************
     * Handles displaying an exception on the application.
     *****************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(radioPanel, exception);
    }
}
