package oracle.retail.sim.client.swing.lov;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.filter.TableSortElement;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RArrowButton;

/******************************************************************************************
 * A table transfer panel where objects placed in a selectable list are transfered to a
 * selected list one at a time (or many at a time).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

class RListOfValuesTransferPanel extends RPanel implements REventListener {
    private static final long serialVersionUID = 8814579524992521132L;

    private RListOfValuesTable originalTable = new RListOfValuesTable();
    private RListOfValuesTablePane originalPane = new RListOfValuesTablePane(originalTable);

    private RListOfValuesTable selectedTable = new RListOfValuesTable();
    private RListOfValuesTablePane selectedPane = new RListOfValuesTablePane(selectedTable);

    private RListOfValuesPagePanel pagePanel = new RListOfValuesPagePanel();

    private RPanel buttonPanel = new RPanel();

    private RArrowButton selectPickButton = new RArrowButton(RArrowButton.EAST);
    private RArrowButton deselectPickButton = new RArrowButton(RArrowButton.WEST);

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private int layoutType = HORIZONTAL;

    private static final String SELECT_DOUBLE_CLICK_ACTION = "SelectAction";
    private static final String DESELECT_DOUBLE_CLICK_ACTION = "DeSelectAction";

    /******************************************************************************************
     * Creates a new RListTransferPanel.
     *****************************************************************************************/
    public RListOfValuesTransferPanel() {
        super(new GridBagLayout());
        originalTable.registerDoubleClickAction(this, SELECT_DOUBLE_CLICK_ACTION);
        selectedTable.registerDoubleClickAction(this, DESELECT_DOUBLE_CLICK_ACTION);
        initializeButtons();
        layoutPanel();
    }

    /******************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("RListTransferPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Lays out the panel horizontally or veritically depending on the values.
     *****************************************************************************************/
    private void layoutPanel() {
        removeAll();

        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridBagLayout());

        if (isHorizontal()) {
            buttonPanel.add(selectPickButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            buttonPanel.add(deselectPickButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

            add(originalPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            add(buttonPanel, GridTool.constraints(1, 0, 1, 1, 0, 1, 0, 2, 0, 7, 0, 7));
            add(selectedPane, GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            add(pagePanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        } else {
            buttonPanel.add(selectPickButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 5));
            buttonPanel.add(deselectPickButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));

            add(originalPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            add(pagePanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
            add(buttonPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 7, 0, 7, 0));
            add(selectedPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        }
    }

    /******************************************************************************************
     * Assigns the title to the values that are being selected.
     * <p>
     * @param title The title to assign.
     *****************************************************************************************/
    protected void setTitle(String title) {
        String listText = Translator.getText("List");
        String selectedText = Translator.getText("Selected");
        String nameText = Translator.getText(title);

        originalPane.setTitle(nameText + " " + listText);
        selectedPane.setTitle(selectedText + " " + nameText);
    }

    /******************************************************************************************
     * Sets the transfer panel as horizontally layed out.
     *****************************************************************************************/
    protected void setHorizontal() {
        if (layoutType != HORIZONTAL) {
            layoutType = HORIZONTAL;
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
    protected boolean isHorizontal() {
        return layoutType == HORIZONTAL;
    }

    /******************************************************************************************
     * Sets the transfer panel as vertically layed out.
     *****************************************************************************************/
    protected void setVertical() {
        if (layoutType != VERTICAL) {
            layoutType = VERTICAL;
            selectPickButton = new RArrowButton(RArrowButton.SOUTH);
            deselectPickButton = new RArrowButton(RArrowButton.NORTH);
            initializeButtons();
            layoutPanel();
        }
    }

    /******************************************************************************************
     * Retrieves the page panel displayed by the LOV transferPanel.
     *****************************************************************************************/
    protected RListOfValuesPagePanel getPagePanel() {
        return pagePanel;
    }

    /******************************************************************************************
     * Validates the button sizes and actions.
     *****************************************************************************************/
    private void initializeButtons() {
        Dimension minDimension = new Dimension(40, 20);

        selectPickButton.setMinimumSize(minDimension);
        selectPickButton.setPreferredSize(minDimension);
        deselectPickButton.setMinimumSize(minDimension);
        deselectPickButton.setPreferredSize(minDimension);

        selectPickButton.addActionListener(createSelectPickAction());
        deselectPickButton.addActionListener(createDeselectPickAction());
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
     * Adds a filter change listener to the selectable side of the transfer panel.
     * <p>
     * @param listener The listener to assign.
     *****************************************************************************************/
    protected void setSelectableChangeListener(PropertyChangeListener listener) {
        originalTable.setFilterChangeListener(listener);
        pagePanel.addPropertyChangeListener(listener);
    }

    /******************************************************************************************
     * Assigns the displayer that will display the table rows within the popup dialog.
     * <p>
     *@param displayer The TableRowDisplayer to install.
     *****************************************************************************************/
    protected void setTableRowDisplayer(TableRowDisplayer displayer) {
        originalTable.setTableRowDisplayer(displayer);
        selectedTable.setTableRowDisplayer(displayer);
    }

    /*****************************************************************************************
     * Retrieves the TableSortElement currently associated with the table.
     * <p>
     * @return The TableSortElement.
     *****************************************************************************************/
    protected TableSortElement getOriginalTableSortElement() {
        return originalTable.getTableSortElement();
    }

    /******************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param items A collection of selectable items.
     *****************************************************************************************/
    protected void setSelectableItems(Collection items) {
        setSelectableItems(items.toArray());
    }

    /******************************************************************************************
     * Assigns the items that are selectable (appear in the left or top table).
     * <p>
     * @param items An array of selectable items.
     *****************************************************************************************/
    protected void setSelectableItems(Object[] items) {
        if (items == null) {
            items = new Object[0];
        }
        originalTable.resetTable();
        originalTable.addRows(Arrays.asList(items));
        originalTable.resort();
    }

    /******************************************************************************************
     * Assigns the selected items of the transfer panel.
     * <p>
     * @param item An array of current selected items.
     *****************************************************************************************/
    protected void setSelectedItems(Object[] items) {
        selectedTable.resetTable();
        addToSelectedTable(Arrays.asList(items));
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Retrieves the selected items as a list of objects.
     * <p>
     * @return The selected items as a list of objects.
     *****************************************************************************************/
    protected List getSelectedItems() {
        return selectedTable.getAllData();
    }

    /******************************************************************************************
     * Assigns the page title information to the page panel.
     *****************************************************************************************/
    protected void setPageTitle(int startValue, int endValue, int totalValue) {
        pagePanel.setPageTitle(startValue, endValue, totalValue);
    }

    /******************************************************************************************
     * Assigns the page to be displayed.
     * <p>
     * @param page The current page.
     * @param totalPages The total number of pages.
     ******************************************************************************************/
    protected void setPage(int page, int totalPages) {
        pagePanel.setPage(page, totalPages);
    }

    /******************************************************************************************
     * Returns true if the selected values are empty, false if at least one value has been
     * selected.
     *****************************************************************************************/
    protected boolean isEmpty() {
        return !selectedTable.hasSelectedRows();
    }

    /******************************************************************************************
     * Handles the REventListener for the table (table double clicks)
     *****************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(SELECT_DOUBLE_CLICK_ACTION)) {
            doDoubleClickSelectAction();
        } else if (command.equals(DESELECT_DOUBLE_CLICK_ACTION)) {
            doDoubleClickDeselectAction();
        }
    }

    /******************************************************************************************
     * Transfers a double clicked selectable row.
     *****************************************************************************************/
    private void doDoubleClickSelectAction() {
        List allValues = originalTable.getAllSelectedData();
        if (allValues.size() == 1) {
            addToSelectedTable(allValues);
            fireTransferOccurred();
        }
    }

    /******************************************************************************************
     * Transfers a double clicked deselected row.
     *****************************************************************************************/
    private void doDoubleClickDeselectAction() {
        List allValues = selectedTable.getAllSelectedData();
        if (allValues.size() == 1) {
            selectedTable.removeSelectedRow();
            fireTransferOccurred();
        }
    }

    /******************************************************************************************
     * Transfers chosen selectable items to selected.
     *****************************************************************************************/
    private void doSelectPickAction() {
        addToSelectedTable(originalTable.getAllSelectedData());
        selectedTable.resort();
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Transfers chosen selected items to selectable.
     *****************************************************************************************/
    private void doDeselectPickAction() {
        selectedTable.removeSelectedRows();
        fireTransferOccurred();
    }

    /******************************************************************************************
     * Adds List Of Items To Selected Table. This method does not allow duplicates.
     *****************************************************************************************/
    private void addToSelectedTable(List items) {
        List addItemList = new ArrayList<>();
        List dataList = selectedTable.getAllData();
        Object object;
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            object = iterator.next();
            if (!dataList.contains(object)) {
                addItemList.add(object);
            }
        }
        selectedTable.addRows(addItemList);
        selectedTable.resort();
    }

    //	/******************************************************************************************
    //	 * Displays an exception fo rthe transfer panel.
    //	 *****************************************************************************************/
    //	private void displayException(Throwable exception) {
    //		Container container = getTopLevelAncestor();
    //
    //		if (container instanceof ApplicationFrame) {
    //			((ApplicationFrame) container).displayException(getClass(), exception);
    //		} else if (container instanceof RDialog) {
    //			((RDialog) container).displayException(getClass(), exception);
    //		}
    //	}

    /******************************************************************************************
     * Fire table transfer occurred property change.
     *****************************************************************************************/
    private void fireTransferOccurred() {
        firePropertyChange(UIPropertyName.TABLE_TRANSFER_OCCURRED, Integer.MIN_VALUE, selectedTable.getRowCount());
    }
}
