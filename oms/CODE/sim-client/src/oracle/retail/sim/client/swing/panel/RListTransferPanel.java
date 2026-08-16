package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * Class represents a list transfer panel where objects placed in a selectable list or transfered to a
 * selected list one at a time (or many at a time).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RListTransferPanel extends RPanel implements REventListener {
    private static final long serialVersionUID = -4645963867051581825L;

    private RListEditor originalList = new RListEditor();
    private RListEditor selectedList = new RListEditor();
    private RPanel buttonPanel = new RPanel();

    private RButton selectAllButton = new RButton(">>");
    private RButton deselectAllButton = new RButton("<<");
    private RArrowButton selectPickButton = new RArrowButton(RArrowButton.EAST);
    private RArrowButton deselectPickButton = new RArrowButton(RArrowButton.WEST);

    private Object[] originalSelectableItems = new Object[0];

    private static final String LT_SELECT_TRANSFER = "LtSelectTransfer";
    private static final String LT_DESELECT_TRANSFER = "LtDeselectTransfer";

    private int opad;
    private int spad;

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    private boolean includeAllOptions;
    private int layoutType = -1;

    /****************************************************************************************************
     * Creates a new RListTransferPanel.
     ***************************************************************************************************/
    public RListTransferPanel() {
        super(new GridBagLayout());
        initialize();
        setHorizontal();
    }

    /****************************************************************************************************
     * Creates a new RListTransferPanel.
     * <p>
     * @param title The title of the values that are being selected.
     ***************************************************************************************************/
    public RListTransferPanel(String title) {
        super(new GridBagLayout());
        setTitle(title);
        initialize();
        setHorizontal();
    }

    /****************************************************************************************************
     * Creates a new RListTransferPanel.
     * <p>
     * @param title The title of the values that are being selected.
     ***************************************************************************************************/
    public RListTransferPanel(String selectableTitle, String selectedTitle) {
        super(new GridBagLayout());
        setTitle(selectableTitle, selectedTitle);
        initialize();
        setHorizontal();
    }

    /****************************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     ***************************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("RListTransferPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /****************************************************************************************************
     * Initializes the widgets in the list transfer panel.
     ***************************************************************************************************/
    private void initialize() {
        originalList.setErrorIndicatorAvailable(false);
        originalList.setMultipleSelectionMode();
        originalList.setAutoSort(true);
        originalList.setAllowsSwap(false);
        originalList.registerDoubleClickAction(this, LT_SELECT_TRANSFER);

        selectedList.setErrorIndicatorAvailable(false);
        selectedList.setMultipleSelectionMode();
        selectedList.setAutoSort(true);
        selectedList.setAllowsSwap(false);
        selectedList.registerDoubleClickAction(this, LT_DESELECT_TRANSFER);

        selectAllButton.addActionListener(createSelectAllAction());
        selectPickButton.addActionListener(createSelectPickAction());
        deselectAllButton.addActionListener(createDeselectAllAction());
        deselectPickButton.addActionListener(createDeselectPickAction());
    }

    /****************************************************************************************************
     * Creates the select all action.
     ***************************************************************************************************/
    private ActionListener createSelectAllAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doSelectAllAction();
            }
        };
    }

    /****************************************************************************************************
     * Creas the select only the chosen action.
     ***************************************************************************************************/
    private ActionListener createSelectPickAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doSelectPickAction();
            }
        };
    }

    /****************************************************************************************************
     * Creates the deselect all action.
     ***************************************************************************************************/
    private ActionListener createDeselectAllAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDeselectAllAction();
            }
        };
    }

    /****************************************************************************************************
     * Creates the deselect only the chosen action.
     ***************************************************************************************************/
    private ActionListener createDeselectPickAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDeselectPickAction();
            }
        };
    }

    /****************************************************************************************************
     * Lays out the panel horizontally or veritically depending on the values.
     ***************************************************************************************************/
    private void layoutPanel() {
        removeAll();
        super.setLayout(new GridBagLayout());

        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridBagLayout());

        if (isHorizontal()) {
            buttonPanel.add(selectAllButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(selectPickButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(deselectPickButton, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(deselectAllButton, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

            validateBasePads();

            super.add(originalList, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, opad, 0));
            super.add(buttonPanel, GridTool.constraints(1, 0, 1, 1, 0, 1, 0, 2, 0, 7, 0, 7));
            super.add(selectedList, GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, spad, 0));
        } else {
            buttonPanel.add(selectAllButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(selectPickButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(deselectPickButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(deselectAllButton, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));

            super.add(originalList, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            super.add(buttonPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 7, 0, 7, 0));
            super.add(selectedList, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        }
    }

    /****************************************************************************************************
     * Assigns the title to the values that are being selected.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        String listText = Translator.getText("List");
        String selectedText = Translator.getText("Selected");
        String nameText = Translator.getText(title);

        originalList.setTitle(nameText + " " + listText);
        selectedList.setTitle(selectedText + " " + nameText);
    }

    /****************************************************************************************************
     * Assigns the selectable and selected column titles.
     * <p>
     * @param selectableTitle The title to place on the selectable list.
     * @param selectedTitle The title to place on the selected list.
     ***************************************************************************************************/
    public void setTitle(String selectableTitle, String selectedTitle) {
        originalList.setTitle(selectableTitle);
        selectedList.setTitle(selectedTitle);
    }

    /****************************************************************************************************
     * Assigns a font to the title area of the lists.
     * <p>
     * @param font The font to assign.
     ***************************************************************************************************/
    public void setTitleFont(Font font) {
        originalList.getLabel().setFont(font);
        selectedList.getLabel().setFont(font);
    }

    /****************************************************************************************************
     * Sets the transfer panel as horizontally layed out.
     ***************************************************************************************************/
    public void setHorizontal() {
        if (layoutType != HORIZONTAL) {
            layoutType = HORIZONTAL;
            selectAllButton = new RButton(">>");
            deselectAllButton = new RButton("<<");
            selectPickButton = new RArrowButton(RArrowButton.EAST);
            deselectPickButton = new RArrowButton(RArrowButton.WEST);
            initializeButtons();
            layoutPanel();
        }
    }

    /****************************************************************************************************
     * Retrieves if the panel is horizontally oriented.
     * <p>
     * @return True if horizontally oriented, false if vertically oriented.
     ***************************************************************************************************/
    public boolean isHorizontal() {
        return layoutType == HORIZONTAL;
    }

    /****************************************************************************************************
     * Sets the transfer panel as vertically layed out.
     ***************************************************************************************************/
    public void setVertical() {
        if (layoutType != VERTICAL) {
            layoutType = VERTICAL;
            selectAllButton = new RButton("V V");
            deselectAllButton = new RButton("^^");
            selectPickButton = new RArrowButton(RArrowButton.SOUTH);
            deselectPickButton = new RArrowButton(RArrowButton.NORTH);
            initializeButtons();
            layoutPanel();
        }
    }

    /****************************************************************************************************
     * Validates the button sizes and actions.
     ***************************************************************************************************/
    private void initializeButtons() {
        RButton dimButton = new RButton(">>");
        Dimension minDimension = dimButton.getPreferredSize();

        selectAllButton.setMinimumSize(minDimension);
        selectAllButton.setPreferredSize(minDimension);
        deselectAllButton.setMinimumSize(minDimension);
        deselectAllButton.setPreferredSize(minDimension);
        selectPickButton.setMinimumSize(minDimension);
        selectPickButton.setPreferredSize(minDimension);
        deselectPickButton.setMinimumSize(minDimension);
        deselectPickButton.setPreferredSize(minDimension);

        selectAllButton.addActionListener(createSelectAllAction());
        selectPickButton.addActionListener(createSelectPickAction());
        deselectAllButton.addActionListener(createDeselectAllAction());
        deselectPickButton.addActionListener(createDeselectPickAction());

        setIncludeAllOptions(includeAllOptions);
    }

    /****************************************************************************************************
     * Adds PropertyChangeListener to the selectable list.
     * <p>
     * @param listener The PropertyChangeListener.
     ***************************************************************************************************/
    public void addSelectableChangeListener(PropertyChangeListener listener) {
        originalList.addPropertyChangeListener(listener);
    }

    /****************************************************************************************************
     * Adds PropertyChangeListener to the selected list.
     * <p>
     * @param listener The PropertyChangeListener.
     ***************************************************************************************************/
    public void addSelectedChangeListener(PropertyChangeListener listener) {
        selectedList.addPropertyChangeListener(listener);
    }

    /****************************************************************************************************
     * Assigns whether or not the "all" options should be included. If true, the "all" buttons will be
     * visible, otherwise false.
     * <p>
     * @param include True if the "all" options should be included.
     ***************************************************************************************************/
    public void setIncludeAllOptions(boolean include) {
        selectAllButton.setVisible(include);
        deselectAllButton.setVisible(include);
        includeAllOptions = include;
    }

    /****************************************************************************************************
     * Assigns the auto sort flag of the list. If true, the list will attempt to sort all contents
     * alphabetically. By default, auto sort is enabled.
     * <p>
     * @param enabled True if the list should sort, false if not.
     ***************************************************************************************************/
    public void setSelectableAutoSort(boolean autoSort) {
        originalList.setAutoSort(autoSort);
    }

    /****************************************************************************************************
     * Sets whether or not the selectable list should allows its items to be swapped up or down. If auto
     * sort is set to true, this method will be ignored.
     * <p>
     * @param allowsSwap True if the list should allow reorganization, false if not.
     ***************************************************************************************************/
    public void setSelectableAllowsSwap(boolean allowsSwap) {
        originalList.setAllowsSwap(allowsSwap);
        layoutPanel();
    }

    /****************************************************************************************************
     * Sets whether or not the selected list should allows its selected item to be swapped up or down. If
     * auto sort is set to true, this value will always be false.
     * <p>
     * @param allowsSwap True if the list should allow reorganization, false if not.
     ***************************************************************************************************/
    public void setSelectedAllowsSwap(boolean allowsSwap) {
        selectedList.setAllowsSwap(allowsSwap);
        layoutPanel();
    }

    /****************************************************************************************************
     * Sets the row displayer for the transfer panel. The default row displayer of lists will be used if
     * this is not set.
     * <p>
     * @param displayer The BasicDisplayer to assign.
     ***************************************************************************************************/
    public void setRowDisplayer(BasicDisplayer displayer) {
        originalList.setRowDisplayer(displayer);
        selectedList.setRowDisplayer(displayer);
    }

    /****************************************************************************************************
     * Sets the row comparator for the transfer panel. The default list comparator will be used if this
     * is not set.
     * <p>
     * @param comparator The Comparator to assign.
     ***************************************************************************************************/
    public void setRowComparator(Comparator comparator) {
        originalList.setRowComparator(comparator);
        selectedList.setRowComparator(comparator);
    }

    /****************************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param items An array of selectable items.
     ***************************************************************************************************/
    public void setSelectableItems(Object[] items) {
        if (items == null) {
            items = new Object[0];
        }
        originalSelectableItems = items;
        originalList.setItems(items);
        removeSelectedItemsFromSelectable();
        validatePanelSizes();
    }

    /****************************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param collection A collection of selectable items.
     ***************************************************************************************************/
    public void setSelectableItems(Collection collection) {
        setSelectableItems(collection.toArray());
    }

    /****************************************************************************************************
     * Retrieves the remaining selectable items of the transfer panel (this will NOT include any items
     * that are already selected).
     * <p>
     * @return The remaining selectable items.
     ***************************************************************************************************/
    public List getRemainingSelectableItems() {
        return originalList.getItemsAsList();
    }

    /****************************************************************************************************
     * Retrieves the all selectable items of the transfer panel (this will NOT include any items that are
     * already selected).
     * <p>
     * @return The remaining selectable items.
     ***************************************************************************************************/
    public List getAllSelectableItems() {
        return Arrays.asList(originalSelectableItems);
    }

    /****************************************************************************************************
     * Assigns the selected items of the transfer panel.
     * <p>
     * @param item An array of current selected items.
     ***************************************************************************************************/
    public void setSelectedItems(Object[] items) {
        clearSelectedItems();
        if (items == null) {
            return;
        }
        selectedList.setItems(items);
        removeSelectedItemsFromSelectable();
    }

    /****************************************************************************************************
     * Assigns the selected items of the transfer panel.
     * <p>
     * @param collection An array of current selected items.
     ***************************************************************************************************/
    public void setSelectedItems(Collection collection) {
        setSelectedItems(collection.toArray());
    }

    /****************************************************************************************************
     * Retrieves the selected items as an array of objects.
     * <p>
     * @return The array of selected objects.
     ***************************************************************************************************/
    public Object[] getSelectedItemsAsArray() {
        return selectedList.getItems();
    }

    /****************************************************************************************************
     * Retrieves the selected items as a list of objects.
     * <p>
     * @return The selected items as a list of objects.
     ***************************************************************************************************/
    public List getSelectedItems() {
        return selectedList.getItemsAsList();
    }

    /****************************************************************************************************
     * Retrieves the highlighted selected items as a list of objects. These are the currently
     * row-selected objects of the selected side of the transfer panel.
     * <p>
     * @return The highlighted selected items as a list of objects.
     ***************************************************************************************************/
    public List getHighlightedSelectedItems() {
        return Arrays.asList(selectedList.getSelectedValues());
    }

    /****************************************************************************************************
     * This removes all the selectable items, and thus logically, all the selected items. It has the
     * effect of clearing the entire transfer panel.
     ***************************************************************************************************/
    public void clearSelectableItems() {
        selectedList.removeItems();
        originalList.removeItems();
        originalSelectableItems = new Object[0];
    }

    /****************************************************************************************************
     * Clears the selected items from the transfer panel.
     ***************************************************************************************************/
    public void clearSelectedItems() {
        selectedList.removeItems();
        originalList.setItems(originalSelectableItems);
        validatePanelSizes();
    }

    /****************************************************************************************************
     * Validates the sizes of the panels trying to keep them the same size.
     ***************************************************************************************************/
    private void validatePanelSizes() {
        if (isHorizontal() && isEmpty()) {
            int availableWidth = getWidth() - buttonPanel.getWidth();
            if (availableWidth > 0) {
                int newWidth = availableWidth / 2;
                originalList.setPreferredSize(new Dimension(newWidth, 0));
                selectedList.setPreferredSize(new Dimension(newWidth, 0));
            }
            return;
        }
        setPreferredSize(null);
    }

    /****************************************************************************************************
     * Returns true if the selected values are empty, false if at least one value has been selected.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return selectedList.isEmpty();
    }

    /****************************************************************************************************
     * Overrides the superclass method to also enable or disable all of the internal editors.
     * <p>
     * @param enabled True if the list transfer panel should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        originalList.setEnabled(enabled);
        selectedList.setEnabled(enabled);
        selectAllButton.setEnabled(enabled);
        deselectAllButton.setEnabled(enabled);
        selectPickButton.setEnabled(enabled);
        deselectPickButton.setEnabled(enabled);
    }

    /****************************************************************************************************
     * For horizontal layouts, it
     ***************************************************************************************************/
    private void validateBasePads() {
        opad = 0;
        spad = 0;
        if (originalList.allowsSwap() && !selectedList.allowsSwap()) {
            spad = originalList.getSwapPad();
        } else if (!originalList.allowsSwap() && selectedList.allowsSwap()) {
            opad = selectedList.getSwapPad();
        }
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. The component is added at the next
     * available column (within the current row). It wraps to the next row when out of columns. This
     * method has been overridden and made unavailable.
     * <p>
     * @param component The component to add to the panel.
     ***************************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Adds the specified component at the given column and row. This method has been overridden and made
     * unavailable.
     * <p>
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     ***************************************************************************************************/
    public Component add(Component component, int row, int column) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the 1.1 method,
     * add(Component, Object), in place of this method. This method has been overridden and made
     * unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Adds the specified component to this container at the given index. This method has been overridden
     * and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ***************************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Adds the specified component to this container with the specified constraints at the specified
     * index. Also notifies the layout manager to add the component to the this container's layout using
     * the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component. -1 means
     *            insert at the end.
     ***************************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /****************************************************************************************************
     * Implements the event listener method to perform actions on double clicks.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(LT_SELECT_TRANSFER)) {
            doSelectPickAction();
        } else if (command.equals(LT_DESELECT_TRANSFER)) {
            doDeselectPickAction();
        }
    }

    /****************************************************************************************************
     * Transfers all the selectable items to selected.
     ***************************************************************************************************/
    private void doSelectAllAction() {
        Object[] allValues = originalList.getItems();
        selectedList.addItems(allValues);
        originalList.removeItems();
        fireTransferOccurred();
    }

    /****************************************************************************************************
     * Transfers chosen selectable items to selected.
     ***************************************************************************************************/
    private void doSelectPickAction() {
        if (!selectPickButton.isEnabled()) {
            return;
        }
        Object[] allValues = originalList.getSelectedValues();
        selectedList.addItems(allValues);
        originalList.removeSelectedValues();
        fireTransferOccurred();
    }

    /****************************************************************************************************
     * Transfers ALL selected items to selectable.
     ***************************************************************************************************/
    private void doDeselectAllAction() {
        Object[] allValues = selectedList.getItems();
        originalList.addItems(allValues);
        selectedList.removeItems();
        validatePanelSizes();
        fireTransferOccurred();
    }

    /****************************************************************************************************
     * Transfers chosen selected items to selectable.
     ***************************************************************************************************/
    private void doDeselectPickAction() {
        if (!deselectPickButton.isEnabled()) {
            return;
        }
        Object[] allValues = selectedList.getSelectedValues();
        originalList.addItems(allValues);
        selectedList.removeSelectedValues();
        validatePanelSizes();
        fireTransferOccurred();
    }

    /****************************************************************************************************
     * Removes all selected items from selectable.
     ***************************************************************************************************/
    private void removeSelectedItemsFromSelectable() {
        List selectedItems = selectedList.getItemsAsList();
        if (selectedItems.isEmpty()) {
            return;
        }

        List selectableItems = originalList.getItemsAsList();
        List trimmedItems = new ArrayList<>();
        Object object;
        for (Iterator iterator = selectableItems.iterator(); iterator.hasNext();) {
            object = iterator.next();
            if (selectedItems.contains(object)) {
                continue;
            }
            trimmedItems.add(object);
        }
        originalList.setItems(trimmedItems);
    }

    /****************************************************************************************************
     * Fire table transfer occurred property change.
     ***************************************************************************************************/
    private void fireTransferOccurred() {
        firePropertyChange(UIPropertyName.LIST_TRANSFER_OCCURRED, Integer.MIN_VALUE, selectedList.getItems().length);
    }
}
