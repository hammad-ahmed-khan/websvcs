package oracle.retail.sim.client.swing.editor;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Comparator;
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
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * This class represents a combination list box with a label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RComboBoxEditor extends AbstractEditor {
    private static final long serialVersionUID = 6966883024310888160L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RComboBox valueCombo = new RComboBox();

    private REventListener eventListener;
    private String eventCommand;
    private Object lastSelectedValue;

    /****************************************************************************************************
     * Creates a new RTextFieldEditor with no title.
     ***************************************************************************************************/
    public RComboBoxEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RTextFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RComboBoxEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RComboBoxEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RComboBoxEditor(String title, boolean required) {
        titleLabel.setText(title);
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

        valueCombo.addActionListener(createInternalActionListener());
        valueCombo.addFocusListener(createManagerFocusListener());

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /****************************************************************************************************
     * Creates an internal action listener that calls the selection modified method when a selection is
     * actually modified.
     ***************************************************************************************************/
    private ActionListener createInternalActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Object selectedValue = ((RComboBox) event.getSource()).getSelectedItem();
                if (lastSelectedValue != selectedValue) {
                    doSelectionModified(selectedValue);
                }
                lastSelectedValue = selectedValue;
            }
        };
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
     * Retrieves the RComboBox object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RComboBox widget to get access to methods that
     * are not provided.
     * <p>
     * @return The RComboBox widget.
     ***************************************************************************************************/
    public RComboBox getComboBox() {
        return valueCombo;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueCombo.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifier is altered in an editor. Subclasses should override this
     * for any desired custom functionality.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueCombo.setIdentifier(identifier);
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
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM. The
     * label suffix feature is turned off for all alignments except for LEFT.
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
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public boolean setNullMode(boolean isAll) {
        return titleLabel.isRequired();
    }

    /****************************************************************************************************
     * Assigns the description to display for the NULL selection in the combo box.
     ***************************************************************************************************/
    public void setEmptyType(RComboBoxEmptyType emptyType) {
        valueCombo.setEmptyType(emptyType);
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
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RComboBoxEditor alignment!");
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
                    innerPanel.add(valueCombo, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueCombo, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueCombo, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueCombo, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
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
                setMinimumWidth(UIManager.getInt(UIThemeName.COMBOBOX_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.COMBOBOX_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.COMBOBOX_LARGE));
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
        valueCombo.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * Assigns the object that displays rows within the list.
     * <p>
     * @param basicDisplayer The BasicDisplayer that will display the combo list objects.
     ***************************************************************************************************/
    public void setDisplayer(BasicDisplayer basicDisplayer) {
        valueCombo.setDisplayer(basicDisplayer);
    }

    /****************************************************************************************************
     * Retrieves the basic displayer for the combo box. This property can never be null.
     * <p>
     * @return The BasicDisplayer assigned to this combo box.
     ***************************************************************************************************/
    public BasicDisplayer getDisplayer() {
        return valueCombo.getDisplayer();
    }

    /****************************************************************************************************
     * Assigns a comparator to use to sort the objects in the combo box popup list. By default, the combo
     * box sorts alphabetically using the assigned BasicDisplayer object.
     * <p>
     * @param comparator The comparator to assign.
     ***************************************************************************************************/
    public void setComparator(Comparator comparator) {
        valueCombo.setComparator(comparator);
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
     * By default, the combo box sorts its data based on its assigned comparator. If sorting is set to
     * false, the combo box will not sort its internal data. If you do not desire the combo box to sort,
     * make sure you assign this property prior to assigning any items to the combo box.
     * <p>
     * @param sortEnable True if sorting should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setSortEnabled(boolean sortEnabled) {
        valueCombo.setSortEnabled(sortEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the combo box should sort its items.
     * <p>
     * @return True if the combo box should sort its items, false otherwise.
     ***************************************************************************************************/
    public boolean isSortEnabled() {
        return valueCombo.isSortEnabled();
    }

    /****************************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RTextField.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueCombo.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueCombo.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return valueCombo.isEnabled();
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
     * Retrieves whether or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Sets whether or not a selection is required in the combo box.
     * <p>
     * @param isRequired True if a selection is required, false if not.
     ***************************************************************************************************/
    public void setSelectionRequired(boolean required) {
        valueCombo.setSelectionRequired(required);
    }

    /****************************************************************************************************
     * Retrieves whether or not a selection is required in the combo box.
     * <p>
     * @return True if a selection is required, false if not.
     ***************************************************************************************************/
    public boolean isSelectionRequired() {
        return valueCombo.isSelectionRequired();
    }

    /****************************************************************************************************
     * Retrieves the item at the specified index. If this item is EMPTY_SELECTION, then null is returned.
     * <p>
     * @param index The list position where the first item starts at zero.
     * @return The selected object, or null if none is selected.
     ***************************************************************************************************/
    public Object getItemAt(int index) {
        return valueCombo.getItemAt(index);
    }

    /****************************************************************************************************
     * Retrieves all the items in the combo box (at the moment, including the EMPTY_SELECTION.
     * <p>
     * @return An array of all the items in the combo box.
     ***************************************************************************************************/
    public Object[] getItems() {
        return valueCombo.getItems();
    }

    /****************************************************************************************************
     * Retrieves the currently selected item. If this item is EMPTY_SELECTION, then null is returned.
     * This method is preferred to getSelectedItem(), which will return the actual EMPTY_SELECTION object.
     * <p>
     * @return The selected object, or null if none is selected.
     ***************************************************************************************************/
    public Object getSelectedItem() {
        if (valueCombo.isEmptySelection()) {
            return null;
        }
        return valueCombo.getSelectedItem();
    }

    /****************************************************************************************************
     * Retrieves the description of the selected item.
     * <p>
     * @return The description of the selected object, or an empty string if none is selected.
     ***************************************************************************************************/
    public String getSelectedDescription() {
        return valueCombo.getSelectedDescription();
    }

    /****************************************************************************************************
     * Returns an array of selected objects. This method is implemented for compatibility with
     * ItemSelectable.
     * <p>
     * @return An object array of the selected objects.
     ***************************************************************************************************/
    public Object[] getSelectedObjects() {
        return valueCombo.getSelectedObjects();
    }

    /****************************************************************************************************
     * Assigns a series of objects in an array to the combo box. This removes all the current items from
     * the combo box and then assigns the new items in sequence. If more than one item is added (or
     * none), then a blank item is included as the first option.
     * <p>
     * NOTE: This method will NOT (!!!!) trigger the notification of the item that was selected. Do not
     * rely on the standard SWING notification from the combo box when you add items and the first item
     * in the list is auto magically selected.
     * <p>
     * @param array An array of objects.
     ***************************************************************************************************/
    public void setItems(Object[] array) {
        valueCombo.setItems(array);
    }

    /****************************************************************************************************
     * Assigns a series of objects in an array to the combo box. This removes all the current items from
     * the combo box and then assigns the new items in sequence. If more than one item is added (or
     * none), then a blank item is included as the first option.
     * <p>
     * NOTE: This method will NOT (!!!!) trigger the notification of the item that was selected. Do not
     * rely on the standard SWING notification from the combo box when you add items and the first item
     * in the list is auto magically selected.
     * <p>
     * @param collection A collection of objects.
     ***************************************************************************************************/
    public void setItems(Collection collection) {
        valueCombo.setItems(collection);
    }

    /****************************************************************************************************
     * Sets the selected item in the combo box. This will remove EMPTY_SELECTION if the combo box is a
     * required value and something other than EMPTY_SELECTION has been selected.
     * <p>
     * @param object The object to select in the combo box.
     ***************************************************************************************************/
    public void setSelectedItem(Object object) {
        valueCombo.setSelectedItem(object);
    }

    /****************************************************************************************************
     * Sets the selected index in the combo box. This will remove EMPTY_SELECTION if the combo box is a
     * required value and something other than EMPTY_SELECTION has been selected.
     * <p>
     * @param index The index to select in the combo box.
     ***************************************************************************************************/
    public void setSelectedIndex(int index) {
        valueCombo.setSelectedIndex(index);
    }

    /****************************************************************************************************
     * Adds an item to the combo box.
     * <p>
     * @param item The item to add to the combo box.
     ***************************************************************************************************/
    public void addItem(Object item) {
        valueCombo.addItem(item);
    }

    /****************************************************************************************************
     * Removes an item to the combo box.
     * <p>
     * @param item The item to remove from the combo box.
     ***************************************************************************************************/
    public void removeItem(Object item) {
        valueCombo.removeItem(item);
    }

    /****************************************************************************************************
     * Sets the current selection in the combo box to the empty selection.
     ***************************************************************************************************/
    public void setEmptySelection() {
        valueCombo.setEmptySelection();
    }

    /****************************************************************************************************
     * Retrieves whether or not there is a selection chosen in the combo box.
     * <p>
     * @return true if no selection exists and false if a selection exists.
     ***************************************************************************************************/
    public boolean isEmptySelection() {
        return valueCombo.isEmptySelection();
    }

    /****************************************************************************************************
     * Retrieves whether or not there are any items in the combo box.
     * <p>
     * @return True if no items exists, false otherwise.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    /****************************************************************************************************
     * Removes the empty selection from the combo box.
     ***************************************************************************************************/
    public void removeEmptySelection() {
        valueCombo.removeEmptySelection();
    }

    /****************************************************************************************************
     * Returns the number of items in the list.
     * <p>
     * @return An integer equal to the number of items in the list
     ***************************************************************************************************/
    public int getItemCount() {
        Object[] items = valueCombo.getItems();
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (Object item : items) {
            if (item != null) {
                count++;
            }
        }
        return count;
    }

    /****************************************************************************************************
     * Sorts items by toDisplayString() if they implement Displayable or toString() if not.
     ***************************************************************************************************/
    public void sort() {
        valueCombo.sort();
    }

    /****************************************************************************************************
     * Removes all items from the combo box.
     ***************************************************************************************************/
    public void clear() {
        valueCombo.clear();
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the state of the combo box changes, the command will be
     * sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the notification.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventCommand = command;
        eventListener = listener;
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return valueCombo.isFocusOwner();
    }

    /****************************************************************************************************
     * Requests the focus move the combo box within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueCombo.requestFocusInWindow();
    }

    /****************************************************************************************************
     * If the combo box was in error state, the error state is cleared. If actions are enabled and a
     * listener and command exists, the listener is sent the command along with the selection.
     * <p>
     * @param selection The modified object.
     ***************************************************************************************************/
    private void doSelectionModified(Object selection) {
        if (isErrorState) {
            setErrorState(false);
        }
        if (isActionEnabled && eventListener != null && eventCommand != null) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand, selection));
        }
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueCombo, exception);
    }
}
