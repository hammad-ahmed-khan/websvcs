package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.event.RListTransferHandler;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * This class extends JList to add custom functionality for retail.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RList extends JList implements RetailComponent, ListDataListener, KeyListener {
    private static final long serialVersionUID = -7124463400830669031L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private DefaultListModel listModel = new DefaultListModel();
    private Comparator listComparator = new ListComparator();
    private KeyEvent storeKeyEvent;

    private Color enabledBackgroundColor;
    private Color disabledBackgroundColor;
    private boolean isControllingColor;

    private BasicDisplayer displayer = new DefaultDisplayer();
    private StringBuilder characterBuffer = new StringBuilder();

    private boolean autoSortEnabled = true;

    /****************************************************************************************************
     * Returns new RList object with default list model assigned.
     ***************************************************************************************************/
    public RList() {
        setCellRenderer(new RListCellRenderer(displayer));
        setModel(listModel);
        addKeyListener(this);
        setTransferHandler(new RListTransferHandler());

        listModel.addListDataListener(this);
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
     * Assigns single selection mode to the list.
     ***************************************************************************************************/
    public void setSingleSelectionMode() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /****************************************************************************************************
     * Assigns multi selection mode to the list.
     ***************************************************************************************************/
    public void setMultiSelectionMode() {
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /****************************************************************************************************
     * Allows multiple line display within the list. This means that if display text for the list
     * contains line returns, it will display over multiple lines.
     ***************************************************************************************************/
    public void setMultiLineMode() {
        setCellRenderer(new RListCellMultiLineRenderer());
        setItems(getItems());
    }

    /****************************************************************************************************
     * Allows only single line display within the list. This means that if display text for the list
     * contains line returns, it will be truncated.
     ***************************************************************************************************/
    public void setSingleLineMode() {
        setCellRenderer(new RListCellRenderer());
        setItems(getItems());
    }

    /****************************************************************************************************
     * Assigns the auto sort flag of the list. If true, the list will attempt to sort all contents
     * alphabetically. By default, auto sort is enabled.
     * <p>
     * @param enabled True if the list should sort, false if not.
     ***************************************************************************************************/
    public void setAutoSort(boolean enabled) {
        autoSortEnabled = enabled;
        sortItems();
    }

    /****************************************************************************************************
     * Retrieves the auto sort flag.
     ***************************************************************************************************/
    public boolean isAutoSort() {
        return autoSortEnabled;
    }

    /****************************************************************************************************
     * Assigns the object that displays rows within the list.
     * <p>
     * @param basicDisplayer The BasicDisplayer that will display the list objects.
     ***************************************************************************************************/
    public void setRowDisplayer(BasicDisplayer basicDisplayer) {
        if (basicDisplayer != null) {
            displayer = basicDisplayer;
            setCellRenderer(new RListCellRenderer(displayer));
        }
    }

    /****************************************************************************************************
     * Retrieves the basic displayer for the list. This property can never be null.
     * <p>
     * @return The BasicDisplayer assigned to this list.
     ***************************************************************************************************/
    public BasicDisplayer getRowDisplayer() {
        return displayer;
    }

    /****************************************************************************************************
     * Assigns a comparator to use to sort the objects in the combo box popup list. By default, the combo
     * box sorts alphabetically using the assigned BasicDisplayer object
     * <p>
     * @param comparator The comparator to assign.
     ***************************************************************************************************/
    public void setRowComparator(Comparator comparator) {
        if (comparator != null) {
            listComparator = comparator;
            sortItems();
        }
    }

    /****************************************************************************************************
     * Sets an array of objects in the list. This method first removes all previous objects before
     * assigning the new objects.
     * <p>
     * @param collection A collection of objects to display in the list.
     ***************************************************************************************************/
    public void setItems(Object[] array) {
        listModel.removeListDataListener(this);
        listModel.removeAllElements();

        for (Object object : array) {
            listModel.addElement(object);
        }
        sortItems();
        listModel.addListDataListener(this);
    }

    /****************************************************************************************************
     * Sets a collection of objects in the list. This method first removes all previous objects before
     * assigning the new objects.
     * <p>
     * @param collection A collection of objects to display in the list.
     ***************************************************************************************************/
    public void setItems(Collection collection) {
        listModel.removeListDataListener(this);
        listModel.removeAllElements();

        for (Object object : collection) {
            listModel.addElement(object);
        }
        sortItems();
        listModel.addListDataListener(this);
    }

    /****************************************************************************************************
     * Adds an array of objects to the list. Duplicate objects will not be added.
     * <p>
     * @param array The array of objects to add to the list.
     ***************************************************************************************************/
    public void addItems(Object[] array) {
        listModel.removeListDataListener(this);
        List allItems = getItemsAsList();

        for (Object object : array) {
            if (!allItems.contains(object)) {
                listModel.addElement(object);
            }
        }
        sortItems();
        listModel.addListDataListener(this);
    }

    /****************************************************************************************************
     * Adds a collection of objects to the list. Duplicate objects will not be added.
     * <p>
     * @param collection The collection of objects to add to the list.
     ***************************************************************************************************/
    public void addItems(Collection collection) {
        listModel.removeListDataListener(this);
        List allItems = getItemsAsList();

        for (Object object : collection) {
            if (!allItems.contains(object)) {
                listModel.addElement(object);
            }
        }
        sortItems();
        listModel.addListDataListener(this);
    }

    /****************************************************************************************************
     * Adds a single item to the list.
     * <p>
     * @param object The item to add to the list.
     ***************************************************************************************************/
    public void addItem(Object object) {
        listModel.removeListDataListener(this);
        if (!getItemsAsList().contains(object)) {
            listModel.addElement(object);
            sortItems();
        }
        listModel.addListDataListener(this);
    }

    /****************************************************************************************************
     * Updates an item in the list. This simply takes an object and replaces it with itself, refreshing
     * the object description.
     * <p>
     * @param object The item to update in the list.
     ***************************************************************************************************/
    public void updateItem(Object object) {
        int index = listModel.indexOf(object);
        if (index > -1) {
            listModel.setElementAt(object, index);
        }
    }

    /****************************************************************************************************
     * Retrieves all the items in the list box as a array.
     * <p>
     * @return An array of all items in the list.
     ***************************************************************************************************/
    public Object[] getItems() {
        return listModel.toArray();
    }

    /****************************************************************************************************
     * Retrieves all the items in the list box as a list.
     * <p>
     * @return A list of all items within the list.
     ***************************************************************************************************/
    public List getItemsAsList() {
        return Arrays.asList(listModel.toArray());
    }

    /****************************************************************************************************
     * Selects all items in the collection passed in. If an item is in the collection, but not this list,
     * it is ignored.
     * <p>
     * @param values A collection of values to be selected.
     ***************************************************************************************************/
    public void setSelectedValues(Collection values) {
        List allItemList = getItemsAsList();
        int[] tempArray = new int[values.size()];
        int index = 0;

        for (Object value : values) {
            if (allItemList.contains(value)) {
                tempArray[index++] = listModel.indexOf(value);
            }
        }

        int[] indexArray = new int[index];
        System.arraycopy(tempArray, 0, indexArray, 0, index);
        setSelectedIndices(indexArray);

        if (indexArray.length > 0) {
            ensureIndexIsVisible(indexArray[0]);
        }
    }

    /****************************************************************************************************
     * Removes all currently selected values from the list.
     ***************************************************************************************************/
    public void removeSelectedValues() {
        Object[] array = getSelectedValues();

        for (Object object : array) {
            listModel.removeElement(object);
        }
    }

    /****************************************************************************************************
     * Removes all items from the list.
     ***************************************************************************************************/
    public void removeItems() {
        listModel.removeAllElements();
    }

    /****************************************************************************************************
     * Removes an array of objects from the list.
     * <p>
     * @param array The array of objects to remove from the list.
     ***************************************************************************************************/
    public void removeItems(Object[] array) {
        List allItems = getItemsAsList();

        for (Object object : array) {
            if (allItems.contains(object)) {
                listModel.removeElement(object);
            }
        }
    }

    /****************************************************************************************************
     * Removes a collection of objects from the list.
     * <p>
     * @param collection The collection of objects to remove from the list.
     ***************************************************************************************************/
    public void removeItems(Collection collection) {
        List allItems = getItemsAsList();

        for (Object object : collection) {
            if (allItems.contains(object)) {
                listModel.removeElement(object);
            }
        }
    }

    /****************************************************************************************************
     * Removes the specified item from the list.
     * <p>
     * @param object The object to remove from the list.
     ***************************************************************************************************/
    public void removeItem(Object object) {
        if (getItemsAsList().contains(object)) {
            listModel.removeElement(object);
        }
    }

    /****************************************************************************************************
     * Sorts items by getDescription() if they implement Displayable or toString() if not.
     ***************************************************************************************************/
    protected void sortItems() {
        if (autoSortEnabled) {
            List list = getItemsAsList();

            Collections.sort(list, listComparator);

            listModel.removeAllElements();

            for (Object item : list) {
                listModel.addElement(item);
            }
        }
    }

    /****************************************************************************************************
     * Select the item for the current character buffer.
     ***************************************************************************************************/
    private void selectItemForCharacterBuffer() {
        String testString = StringUtility.toLowerCase(characterBuffer.toString());
        String fullString;
        Object[] items = getItems();

        for (Object item : items) {
            if (item instanceof Displayable) {
                fullString = StringUtility.toLowerCase(((Displayable) item).toDisplayString());
            } else {
                fullString = StringUtility.toLowerCase(item.toString());
            }
            if (StringUtility.startsWith(fullString, testString)) {
                setSelectedValue(item, true);
                break;
            }
        }
    }

    /****************************************************************************************************
     * Whenever a new item or items are added, validate that the list is sorted.
     ***************************************************************************************************/
    public void intervalAdded(ListDataEvent event) {
        sortItems();
    }

    /****************************************************************************************************
     * Empty implementation of the list data listener interface method.
     ***************************************************************************************************/
    public void intervalRemoved(ListDataEvent e) {
    }

    /****************************************************************************************************
     * Empty implementation of the list data listener interface method.
     ***************************************************************************************************/
    public void contentsChanged(ListDataEvent event) {
    }

    /****************************************************************************************************
     * Implements the key listener method to capture the keystroke and select a different item.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent event) {
        if (storeKeyEvent == null) {
            storeKeyEvent = event;
        }
        if (event.getWhen() - storeKeyEvent.getWhen() > 750L) {
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
     * Empty implementation of the key listener interface method.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation of the key listener interface method.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /****************************************************************************************************
     * @see setBackground() in JList
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (!isControllingColor) {
            enabledBackgroundColor = color;
        }
        super.setBackground(color);
    }

    /****************************************************************************************************
     * @see setBackground() in JList
     ***************************************************************************************************/
    public void setDisabledBackground(Color color) {
        if (!isControllingColor) {
            disabledBackgroundColor = color;
        }
        super.setBackground(color);
    }

    /****************************************************************************************************
     * Retrieves the disabled background color.
     * <p>
     * @param The disabled background color.
     ***************************************************************************************************/
    public Color getDisabledBackgroundColor() {
        return disabledBackgroundColor;
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
     * Updates the color state of the list.
     ***************************************************************************************************/
    private void updateColorState() {
        if (enabledBackgroundColor == null) {
            setBackground(UIManager.getColor(UIThemeName.LIST_BACKGROUND));
        }
        if (disabledBackgroundColor == null) {
            setDisabledBackground(UIManager.getColor(UIThemeName.LIST_DISABLED_BACKGROUND));
        }

        isControllingColor = true;
        if (isEnabled()) {
            setBackground(enabledBackgroundColor);
        } else {
            setBackground(disabledBackgroundColor);
        }
        isControllingColor = false;
    }

    /****************************************************************************************************
     * This comparator sorts two objects based on whether or not they implement Displayable and their
     * toString() method. This should be the displayable description of the data.
     ***************************************************************************************************/
    private class ListComparator implements Comparator {

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
