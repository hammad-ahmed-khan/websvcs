package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.client.swing.displaytable.RDisplayTablePane;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RButton;

/******************************************************************************************
 * RTable Transfer Panel - A table transfer panel where objects placed in a selectable list
 * are transfered to a selected list one at a time (or many at a time).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTableTransferPanel extends RPanel implements REventListener {
    private static final long serialVersionUID = 3945950899465042136L;

    private RDisplayTable originalTable = new RDisplayTable("TransferPanel.selectableTable");
    private RDisplayTablePane originalPane = new RDisplayTablePane(originalTable);

    private RDisplayTable selectedTable = new RDisplayTable("TransferPanel.selectedTable");
    private RDisplayTablePane selectedPane = new RDisplayTablePane(selectedTable);

    private RPanel buttonPanel = new RPanel();

    private RButton selectAllButton = new RButton(">>");
    private RButton deselectAllButton = new RButton("<<");
    private RArrowButton selectPickButton = new RArrowButton(RArrowButton.EAST);
    private RArrowButton deselectPickButton = new RArrowButton(RArrowButton.WEST);

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    private int layoutType = HORIZONTAL;
    private List originalSelectableItems = new ArrayList<>();

    private static final String SINGLE_CLICK_ACTION = "SingleClickAction";
    private static final String SELECT_DOUBLE_CLICK_ACTION = "SelectAction";
    private static final String DESELECT_DOUBLE_CLICK_ACTION = "DeSelectAction";

    /******************************************************************************************
     * Creates a new RTableTransferPanel.
     *****************************************************************************************/
    public RTableTransferPanel() {
        super(new GridBagLayout());
        initialize();
        layoutPanel();
    }

    /******************************************************************************************
     * Creates a new RTableTransferPanel.
     * <p>
     * @param title The title of the values that are being selected.
     *****************************************************************************************/
    public RTableTransferPanel(String title) {
        super(new GridBagLayout());
        setTitle(title);
        initialize();
        layoutPanel();
    }

    /******************************************************************************************
     * Creates a new RTableTransferPanel.
     * <p>
     * @param title The title of the values that are being selected.
     *****************************************************************************************/
    public RTableTransferPanel(String selectableTitle, String selectedTitle) {
        super(new GridBagLayout());
        setTitle(selectableTitle, selectedTitle);
        initialize();
        layoutPanel();
    }

    /******************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("RTableTransferPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Initializes the widgets in the table transfer panel.
     *****************************************************************************************/
    private void initialize() {
        originalTable.setSelectionMode(RDisplayTable.MULTIPLE_ROWS);
        originalTable.registerDoubleClickAction(this, SELECT_DOUBLE_CLICK_ACTION);
        selectedTable.setSelectionMode(RDisplayTable.MULTIPLE_ROWS);
        selectedTable.registerSingleClickAction(this, SINGLE_CLICK_ACTION);
        selectedTable.registerDoubleClickAction(this, DESELECT_DOUBLE_CLICK_ACTION);

        selectAllButton.setVisible(false);
        deselectAllButton.setVisible(false);

        initializeButtons();
    }

    /******************************************************************************************
     * Creates the select all action.
     *****************************************************************************************/
    private ActionListener createSelectAllAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doSelectAllAction();
            }
        };
    }

    /******************************************************************************************
     * Creates the select only the chosen action.
     *****************************************************************************************/
    private ActionListener createSelectPickAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doSelectPickAction();
            }
        };
    }

    /******************************************************************************************
     * Creates the deselect all action.
     *****************************************************************************************/
    private ActionListener createDeselectAllAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDeselectAllAction();
            }
        };
    }

    /******************************************************************************************
     * Creates the deselect only the chosen action.
     *****************************************************************************************/
    private ActionListener createDeselectPickAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDeselectPickAction();
            }
        };
    }

    /******************************************************************************************
     * Lays out the panel horizontally or veritically depending on the values.
     *****************************************************************************************/
    private void layoutPanel() {
        removeAll();

        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridBagLayout());

        if (isHorizontal()) {
            buttonPanel.add(selectAllButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(selectPickButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 10, 0));
            buttonPanel.add(deselectPickButton, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(deselectAllButton, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

            super.add(originalPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            super.add(buttonPanel, GridTool.constraints(1, 0, 1, 1, 0, 1, 0, 2, 0, 7, 0, 7));
            super.add(selectedPane, GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        } else {
            buttonPanel.add(selectAllButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(selectPickButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 10));
            buttonPanel.add(deselectPickButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(deselectAllButton, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));

            super.add(originalPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            super.add(buttonPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 7, 0, 7, 0));
            super.add(selectedPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        }
    }

    /******************************************************************************************
     * Assigns the title to the values that are being selected.
     * <p>
     * @param title The title to assign.
     *****************************************************************************************/
    public void setTitle(String title) {
        String listText = Translator.getText("Available");
        String selectedText = Translator.getText("Assigned");
        String nameText = Translator.getText(title);

        originalPane.setTitle(listText + " " + nameText);
        selectedPane.setTitle(selectedText + " " + nameText);
    }

    /****************************************************************************************************
     * Assigns the selectable and selected column titles.
     * <p>
     * @param selectableTitle The title to place on the selectable list.
     * @param selectedTitle The title to place on the selected list.
     ***************************************************************************************************/
    public void setTitle(String selectableTitle, String selectedTitle) {
        originalPane.setTitle(!StringUtility.isNullOrEmpty(selectableTitle) ? Translator.getText(selectableTitle) : selectableTitle);
        selectedPane.setTitle(!StringUtility.isNullOrEmpty(selectedTitle) ? Translator.getText(selectedTitle) : selectedTitle);
    }

    /******************************************************************************************
     * Sets the transfer panel as horizontally layed out.
     *****************************************************************************************/
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

    /******************************************************************************************
     * Retrieves if the panel is horizontally oriented.
     * <p>
     * @return True if horizontally oriented, false if vertically oriented.
     *****************************************************************************************/
    public boolean isHorizontal() {
        return layoutType == HORIZONTAL;
    }

    /******************************************************************************************
     * Sets the transfer panel as vertically layed out.
     *****************************************************************************************/
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

    /******************************************************************************************
     * Validates the button sizes and actions.
     *****************************************************************************************/
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
    }

    /******************************************************************************************
     * Assigns whether or not configuration options are available for the transfer panel.
     * <p>
     * @param include True if the configuration option should be active.
     *****************************************************************************************/
    public void setConfigurationEnabled(boolean isEnabled) {
        originalPane.setConfigurationEnabled(isEnabled);
        selectedPane.setConfigurationEnabled(isEnabled);
    }

    /******************************************************************************************
     * Assigns whether or not the "all" options should be included. If true, the "all" buttons
     * will be visible, otherwise false.
     * <p>
     * @param include True if the "all" options should be included.
     *****************************************************************************************/
    public void setIncludeAllOptions(boolean include) {
        selectAllButton.setVisible(include);
        deselectAllButton.setVisible(include);
    }

    /******************************************************************************************
     * Assigns whether or not the "selection" options should be enabled.
     * <p>
     * @param enabled True if the "select" options should be enabled, false others.
     *****************************************************************************************/
    public void setSelectOptionsEnabled(boolean enabled) {
        selectAllButton.setEnabled(enabled);
        selectPickButton.setEnabled(enabled);
    }

    /******************************************************************************************
     * Assigns whether or not the "deselection" options should be enabled.
     * <p>
     * @param enabled True if the "deselect" options should be enabled, false others.
     *****************************************************************************************/
    public void setDeselectOptionsEnabled(boolean enabled) {
        deselectAllButton.setEnabled(enabled);
        deselectPickButton.setEnabled(enabled);
    }

    /******************************************************************************************
     * Assigns both the selectable and selected row displayer that displays the table rows
     * within the transfer panel.
     * <p>
     *@param param The TableRowDisplayer to install for both selectable and selected tables.
     *****************************************************************************************/
    public void setRowDisplayer(TableRowDisplayer displayer) {
        setSelectableRowDisplayer(displayer);
        setSelectedRowDisplayer(displayer);
    }

    /******************************************************************************************
     * Assigns only the selectable row displayer that displays the table rows.
     * <p>
     *@param param The TableRowDisplayer to install for the selectable table.
     *****************************************************************************************/
    public void setSelectableRowDisplayer(TableRowDisplayer displayer) {
        originalTable.setTableRowDisplayer(displayer);
    }

    /******************************************************************************************
     * Assigns only the selected row displayer that displays the table rows.
     * <p>
     *@param param The TableRowDisplayer to install for the selected table.
     *****************************************************************************************/
    public void setSelectedRowDisplayer(TableRowDisplayer displayer) {
        selectedTable.setTableRowDisplayer(displayer);
    }

    /******************************************************************************************
     * Assigns column sort order.
     *****************************************************************************************/
    public void setSelectableColumnSortOrder(String[] headers) {
        if (headers == null) {
            headers = new String[0];
        }
        originalTable.setColumnSortOrder(headers);
    }

    /******************************************************************************************
     * Assigns column sort order.
     *****************************************************************************************/
    public void setSelectedColumnSortOrder(String[] headers) {
        if (headers == null) {
            headers = new String[0];
        }
        selectedTable.setColumnSortOrder(headers);
    }

    /******************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param items A collection of selectable items.
     *****************************************************************************************/
    public void setSelectableItems(Collection items) {
        originalTable.setRows(items);
        originalTable.resort();
        selectedTable.resetTable();
        originalSelectableItems = new ArrayList<>(items);
    }

    /******************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param items An array of selectable items.
     *****************************************************************************************/
    public void setSelectableItems(Object[] items) {
        if (items == null) {
            items = new Object[0];
        }
        setSelectableItems(Arrays.asList(items));
    }

    /******************************************************************************************
     * Retrieves the remaining selectable items of the transfer panel (this will NOT include
     * any items that are already selected).
     * <p>
     * @return The remaining selectable items.
     *****************************************************************************************/
    public List getRemainingSelectableItems() {
        return originalTable.getAllData();
    }

    /******************************************************************************************
     * Retrieves the all selectable items of the transfer panel.
     * <p>
     * @return The remaining selectable items.
     *****************************************************************************************/
    public List getAllSelectableItems() {
        return originalSelectableItems;
    }

    /******************************************************************************************
     * Assigns the selected items of the transfer panel.
     * <p>
     * @param item A collection of current selected items.
     *****************************************************************************************/
    public void setSelectedItems(Collection items) {
        originalTable.setRows(originalSelectableItems);
        selectedTable.setRows(items);
        selectedTable.resort();
        removeSelectedItemsFromSelectable();
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Assigns the selected items of the transfer panel.
     * <p>
     * @param item An array of current selected items.
     *****************************************************************************************/
    public void setSelectedItems(Object[] items) {
        setSelectedItems(Arrays.asList(items));
    }

    /******************************************************************************************
     * Retrieves the selected items as a list of objects.
     * <p>
     * @return The selected items as a list of objects.
     *****************************************************************************************/
    public List getSelectedItems() {
        return selectedTable.getAllData();
    }

    /******************************************************************************************
     * Retrieves the highlighted selected items as a list of objects. These are the currently
     * row-selected objects of the selected side of the transfer panel.
     * <p>
     * @return The highlighted selected items as a list of objects.
     *****************************************************************************************/
    public List getHighlightedSelectedItems() {
        return selectedTable.getAllSelectedData();
    }

    /******************************************************************************************
     * Deselects all the highlighted items.
     *****************************************************************************************/
    public void clearHighlightedSelectedItems() {
        selectedTable.clearSelection();
    }

    /******************************************************************************************
     * This removes all the selectable items, and thus logically, all the selected items. It
     * has the effect of clearing the entire transfer panel.
     *****************************************************************************************/
    public void clearSelectableItems() {
        originalTable.resetTable();
        selectedTable.resetTable();
        originalSelectableItems = new ArrayList<>();
    }

    /******************************************************************************************
     * Clears the selected items from the transfer panel.
     *****************************************************************************************/
    public void clearSelectedItems() {
        selectedTable.resetTable();
        originalTable.setRows(originalSelectableItems);
        originalTable.resort();
    }

    /******************************************************************************************
     * Returns true if the selected values half of the panel is empty, false otherwise.
     *****************************************************************************************/
    public boolean isEmpty() {
        return selectedTable.isEmpty();
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added at the
     * next available column (within the current row). It wraps to the next row when out of
     * columns. This method has been overridden and made unavailable.
     * <p>
     * @param component The component to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /******************************************************************************************
     * Adds the specified component at the given column and row. This method has been
     * overridden and made unavailable.
     * <p>
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    public Component add(Component component, int row, int column) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the
     * 1.1 method, add(Component, Object), in place of this method. This method has been
     * overridden and made unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container at the given index. This method has
     * been overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to the end of this container. This method has been
     * overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container with the specified constraints at
     * the specified index.  Also notifies the layout manager to add the component to the
     * this container's layout using the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component.
     * 		-1 means insert at the end.
     ************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /******************************************************************************************
     * Handles the REventListener for the table (table double clicks)
     *****************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(SINGLE_CLICK_ACTION)) {
            firePropertyChange(UIPropertyName.TABLE_TRANSFER_ROW_SELECT, Integer.MIN_VALUE, event.getEventNumber());
        } else if (command.equals(SELECT_DOUBLE_CLICK_ACTION)) {
            doSelectSingleRowAction();
        } else if (command.equals(DESELECT_DOUBLE_CLICK_ACTION)) {
            doDeselectSingleRowAction();
        }
    }

    /******************************************************************************************
     * Transfers a double clicked selectable row
     *****************************************************************************************/
    private void doSelectSingleRowAction() {
        if (!selectPickButton.isEnabled()) {
            return;
        }
        List allValues = originalTable.getAllSelectedData();
        if (allValues.size() == 1) {
            selectedTable.addRows(allValues);
            selectedTable.resort();
            originalTable.removeSelectedRow();
            fireTransferOccurred();
        }
    }

    /******************************************************************************************
     * Transfers a double clicked deselected row
     *****************************************************************************************/
    private void doDeselectSingleRowAction() {
        if (!deselectPickButton.isEnabled()) {
            return;
        }
        List allValues = selectedTable.getAllSelectedData();
        if (allValues.size() == 1) {
            originalTable.addRows(allValues);
            originalTable.resort();
            selectedTable.removeSelectedRow();
            processDeselectedValues(allValues);
            fireTransferOccurred();
        }
    }

    /******************************************************************************************
     * Transfers all selectable items to selected.
     *****************************************************************************************/
    private void doSelectAllAction() {
        selectedTable.setRows(originalSelectableItems);
        selectedTable.resort();
        originalTable.resetTable();
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Transfers chosen selectable items to selected.
     *****************************************************************************************/
    private void doSelectPickAction() {
        List allValues = originalTable.getAllSelectedData();
        selectedTable.addRows(allValues);
        selectedTable.resort();
        originalTable.removeSelectedRows();
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Transfers ALL selected items to selectable.
     *****************************************************************************************/
    private void doDeselectAllAction() {
        List allValues = selectedTable.getAllData();
        clearSelectedItems();
        processDeselectedValues(allValues);
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Transfers chosen selected items to selectable.
     *****************************************************************************************/
    private void doDeselectPickAction() {
        List allValues = selectedTable.getAllSelectedData();
        originalTable.addRows(allValues);
        originalTable.resort();
        selectedTable.removeSelectedRows();
        processDeselectedValues(allValues);
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Removes all selected items from selectable items.
     *****************************************************************************************/
    private void removeSelectedItemsFromSelectable() {
        List selectedItems = selectedTable.getAllData();
        if (selectedItems.isEmpty()) {
            originalTable.resort();
            return;
        }
        List selectableItems = originalTable.getAllData();
        List trimmedItems = new ArrayList<>();
        for (Object selectableItem : selectableItems) {
            if (selectedItems.contains(selectableItem)) {
                continue;
            }
            trimmedItems.add(selectableItem);
        }
        originalTable.setRows(trimmedItems);
        originalTable.resort();
    }

    /******************************************************************************************
     * This method is called when values are deselected to allow subclasses to perform special
     * processing on the values.
     * <p>
     * @param values The values that were deselected.
     *****************************************************************************************/
    protected void processDeselectedValues(List values) {
    }

    /******************************************************************************************
     * Fire table transfer occurred property change.
     *****************************************************************************************/
    private void fireTransferOccurred() {
        firePropertyChange(UIPropertyName.TABLE_TRANSFER_OCCURRED, Integer.MIN_VALUE, selectedTable.getRowCount());
    }
}
