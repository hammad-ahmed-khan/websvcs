package oracle.retail.sim.client.swing.widget;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * This class sublcasses the standard JComboBox class in the Swing package to provide custom
 * functionality. The popup window is made visible by hitting the SPACE key.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RComboBox extends JComboBox implements RetailComponent, KeyListener {
    private static final long serialVersionUID = 1788351513693815485L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private BasicDisplayer displayer = new DefaultDisplayer();
    private RComboBoxCellRenderer renderer = new RComboBoxCellRenderer(displayer);
    private Comparator comboBoxComparator = new ComboBoxComparator();
    private KeyEvent storeKeyEvent;

    private StringBuilder characterBuffer = new StringBuilder();
    private Object lastSelectedItem;

    private boolean isSelectionRequired;
    private boolean hasEmptySelection = true;
    private boolean isSortEnabled = true;

    /****************************************************************************************************
     * Returns new RComboBox object.
     ***************************************************************************************************/
    public RComboBox() {
        setRenderer(renderer);
        setKeySelectionManager(null);
        setMaximumRowCount(7);
        setKeySelectionManager(new ComboSelectionManager());
        addKeyListener(this);
        addItem(renderer.getEmptySelection());
    }

    /****************************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() usable by
     * developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the identifier to the component.
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
     * Sets whether or not a selection is required in the combo box.
     * <p>
     * @param isRequired True if a selection is required, false if not.
     ***************************************************************************************************/
    public void setSelectionRequired(boolean required) {
        isSelectionRequired = required;
    }

    /****************************************************************************************************
     * Retrieves whether or not a selection is required in the combo box.
     * <p>
     * @return True if a selection is required, false if not.
     ***************************************************************************************************/
    public boolean isSelectionRequired() {
        return isSelectionRequired;
    }

    /****************************************************************************************************
     * Assigns the description to display for the NULL selection in the combo box.
     ***************************************************************************************************/
    public void setEmptyType(RComboBoxEmptyType emptyType) {
        renderer.setEmptyType(emptyType);
    }

    /****************************************************************************************************
     * Assigns the object that displays rows within the list.
     * <p>
     * @param basicDisplayer The BasicDisplayer that will display the cell objects.
     ***************************************************************************************************/
    public void setDisplayer(BasicDisplayer basicDisplayer) {
        if (basicDisplayer != null) {
            displayer = basicDisplayer;
            renderer = new RComboBoxCellRenderer(displayer);
            setRenderer(renderer);
        }
    }

    /****************************************************************************************************
     * Retrieves the basic displayer for the combo box. This property can never be null.
     * <p>
     * @return The BasicDisplayer assigned to this combo box.
     ***************************************************************************************************/
    public BasicDisplayer getDisplayer() {
        return displayer;
    }

    /****************************************************************************************************
     * Assigns a comparator to use to sort the objects in the combo box popup list. By default, the combo
     * box sorts alphabetically using the assigned BasicDisplayer object
     * <p>
     * @param comparator The comparator to assign.
     ***************************************************************************************************/
    public void setComparator(Comparator comparator) {
        if (comparator != null) {
            comboBoxComparator = comparator;
            sort();
        }
    }

    /****************************************************************************************************
     * Retrieves the item at the specified index. If this item is EMPTY_SELECTION, then null is returned.
     * <p>
     * @param index The list position where the first item starts at zero.
     * @return The selected object, or null if none is selected.
     ***************************************************************************************************/
    public Object getItemAt(int index) {
        Object object = super.getItemAt(index);
        if (object != renderer.getEmptySelection()) {
            return object;
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves all the items in the combo box (at the moment, including the EMPTY_SELECTION.
     * <p>
     * @return An array of all the items in the combo box.
     ***************************************************************************************************/
    public Object[] getItems() {
        Object[] array = new Object[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            array[i] = getItemAt(i);
        }
        return array;
    }

    /****************************************************************************************************
     * Retrieves the description of the selected item.
     * <p>
     * @return The description of the selected object, or an empty string if none is selected.
     ***************************************************************************************************/
    public String getSelectedDescription() {
        Object object = getSelectedItem();
        if (object == null) {
            return StringConstants.EMPTY;
        } else if (object instanceof Displayable) {
            return ((Displayable) object).toDisplayString();
        } else if (displayer != null) {
            return displayer.getDisplayText(object);
        }
        return object.toString();
    }

    /****************************************************************************************************
     * Returns an array of selected objects. This method is implemented for compatibility with
     * ItemSelectable.
     * <p>
     * @return An object array of the selected objects.
     ***************************************************************************************************/
    public Object[] getSelectedObjects() {
        Object[] array = super.getSelectedObjects();
        List list = new ArrayList<>();

        for (Object element : array) {
            if (element != renderer.getEmptySelection()) {
                list.add(element);
            }
        }
        return list.toArray();
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
        if (array != null) {
            setItems(Arrays.asList(array));
        }
    }

    /****************************************************************************************************
     * Assigns a series of objects in an array to the combo box. This removes all the current items from
     * the combo box and then assigns the new items in sequence. If more than one item is added (or
     * none), then a blank item is included as the first option.
     * <p>
     * This will sort the items in alphabetically order.
     * <p>
     * NOTE: This method will NOT (!!!!) trigger the notification of the item that was selected. Do not
     * rely on the standard SWING notification from the combo box when you add items and the first item
     * in the list is auto magically selected.
     * <p>
     * @param collection A collection of objects.
     ***************************************************************************************************/
    public void setItems(Collection collection) {
        if (collection != null) {
            List list = new ArrayList<>(collection);
            if (isSortEnabled) {
                Collections.sort(list, comboBoxComparator);
            }
            setInnerItems(list);
            resetModified();
        } else {
            clear();
        }
    }

    /****************************************************************************************************
     * Internal method for assigning values to the combo box.
     ***************************************************************************************************/
    private void setInnerItems(Collection collection) {
        removeAllItems();
        if (!(isSelectionRequired && collection.size() == 1)) {
            addItem(renderer.getEmptySelection());
            hasEmptySelection = true;
        }
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            addItem(iterator.next());
        }
    }

    /****************************************************************************************************
     * Sets the selected item in the combo box. This will remove EMPTY_SELECTION if the combo box is a
     * required value and something other than EMPTY_SELECTION has been selected.
     * <p>
     * @param object The object to select in the combo box.
     ***************************************************************************************************/
    public void setSelectedItem(Object object) {
        if (object == null && hasEmptySelection) {
            object = renderer.getEmptySelection();
        }
        super.setSelectedItem(object);

        if (isSelectionRequired && hasEmptySelection && object != renderer.getEmptySelection()) {
            removeEmptySelection();
        }
    }

    /****************************************************************************************************
     * Sets the selected index in the combo box. This will remove EMPTY_SELECTION if the combo box is a
     * required value and something other than EMPTY_SELECTION has been selected.
     * <p>
     * @param index The index to select in the combo box.
     ***************************************************************************************************/
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        if (isSelectionRequired && hasEmptySelection && index > 0) {
            removeEmptySelection();
        }
    }

    /****************************************************************************************************
     * Sets the current selection in the combo box to the empty selection. This will not set the empty
     * selection if the empty selection does not exist in the list.
     ***************************************************************************************************/
    public void setEmptySelection() {
        Object[] itemArray = getItems();
        for (Object element : itemArray) {
            if (element == null) {
                setSelectedItem(renderer.getEmptySelection());
                return;
            }
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not there is a selection.
     * <p>
     * @return true if no selection exists and false if a selection exists.
     ***************************************************************************************************/
    public boolean isEmptySelection() {
        Object object = getSelectedItem();
        if (object == null) {
            return true;
        }
        return object == renderer.getEmptySelection();
    }

    /****************************************************************************************************
     * Removes the empty selection from the combo box.
     ***************************************************************************************************/
    public void removeEmptySelection() {
        removeItem(renderer.getEmptySelection());
        hasEmptySelection = false;
    }

    /****************************************************************************************************
     * Returns true if the combo box contains no items, false otherwise.
     ***************************************************************************************************/
    public boolean isEmpty() {
        Object[] tmpItems = getItems();
        for (Object tmpItem : tmpItems) {
            if (tmpItem != null) {
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Removes all items from the combo box.
     ***************************************************************************************************/
    public void clear() {
        removeAllItems();
    }

    /****************************************************************************************************
     * Empty implementation of the KeyListener method.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent event) {
        if (storeKeyEvent == null) {
            storeKeyEvent = event;
        }
        if (event.getWhen() - storeKeyEvent.getWhen() > 750) {
            characterBuffer = new StringBuilder();
        }
        char character = event.getKeyChar();
        if (Character.isLetterOrDigit(character)) {
            characterBuffer.append(character);
            selectItemForCharacterBuffer();
            event.consume();
        }
        storeKeyEvent = event;
    }

    /****************************************************************************************************
     * Displays the popup selection window after the SPACE key has been pressed and released. It must
     * wait until released or the release event closes the window.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation of the KeyListener method.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent event) {
    }

    /****************************************************************************************************
     * Select the item for the current character buffer.
     ***************************************************************************************************/
    private void selectItemForCharacterBuffer() {
        String testString = StringUtility.toLowerCase(characterBuffer.toString());
        String fullString = null;

        Object[] array = getItems();
        Object object;

        for (Object element : array) {
            object = element;

            if (object != null) {
                if (object instanceof Displayable) {
                    fullString = StringUtility.toLowerCase(((Displayable) object).toDisplayString());
                } else if (displayer != null) {
                    fullString = StringUtility.toLowerCase(displayer.getDisplayText(object));
                } else {
                    fullString = StringUtility.toLowerCase(object.toString());
                }

                if (StringUtility.startsWith(fullString, testString)) {
                    super.setSelectedItem(object);
                    return;
                }
            }
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not the combo box has been modified since its previous state.
     * <p>
     * @return True if the combo box has been modified since its previous state, false if not.
     ***************************************************************************************************/
    public boolean isModified() {
        return lastSelectedItem != getSelectedItem();
    }

    /****************************************************************************************************
     * Resets the modified state of the combo box to not modified.
     ***************************************************************************************************/
    public void resetModified() {
        lastSelectedItem = getSelectedItem();
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
     * By default, the combo box sorts its data based on its assigned comparator. If sorting is set to
     * false, the combo box will not sort its internal data. If you do not desire the combo box to sort,
     * make sure you assign this property prior to assigning any items to the combo box.
     * <p>
     * @param sortEnable True if sorting should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setSortEnabled(boolean sortEnabled) {
        isSortEnabled = sortEnabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not the combo box should sort its items.
     * <p>
     * @return True if the combo box should sort its items, false otherwise.
     ***************************************************************************************************/
    public boolean isSortEnabled() {
        return isSortEnabled;
    }

    /****************************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (!permission.equals(NavigationPermission.FULL)) {
            enabled = false;
        }
        super.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Sorts items by toDisplayString() if they implement Displayable or toString() if not.
     ***************************************************************************************************/
    public void sort() {
        if (isSortEnabled) {
            List list = Arrays.asList(getItems());
            Collections.sort(list, comboBoxComparator);
            Object value = getSelectedItem();
            setInnerItems(list);

            if (value != null) {
                setSelectedItem(value);
            }
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
     * We are monitoring key strokes on are own to allow for multiple characters to determine the
     * selection. Therefore, we must override the default selection manager with one that doesn't select
     * anything.
     ***************************************************************************************************/
    private class ComboSelectionManager implements KeySelectionManager {

        public int selectionForKey(char character, ComboBoxModel comboBoxModel) {
            return -1;
        }
    }

    /****************************************************************************************************
     * This comparator sorts two objects based on whether or not they implement Displayable and their
     * toString() method. This should be the displayable description of the data.
     ***************************************************************************************************/
    private class ComboBoxComparator implements Comparator {

        public int compare(Object objectOne, Object objectTwo) {
            if (objectOne == null) {
                return -1;
            }

            if (objectTwo == null) {
                return 1;
            }

            if (objectOne instanceof Displayable && objectTwo instanceof Displayable) {
                String textOne = ((Displayable) objectOne).toDisplayString();
                String textTwo = ((Displayable) objectTwo).toDisplayString();

                if (textOne == null) {
                    return -1;
                }
                if (textTwo == null) {
                    return 1;
                }

                return StringUtility.compareToIgnoreCase(textOne, textTwo);
            }

            if (displayer != null) {
                String textOne = displayer.getDisplayText(objectOne);
                String textTwo = displayer.getDisplayText(objectTwo);

                if (textOne == null) {
                    return -1;
                }
                if (textTwo == null) {
                    return 1;
                }
                return StringUtility.compareToIgnoreCase(textOne, textTwo);
            }

            return StringUtility.compareToIgnoreCase(objectOne.toString(), objectTwo.toString());
        }
    }
}
