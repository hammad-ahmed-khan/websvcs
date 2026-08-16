package oracle.retail.sim.client.swing.lov;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.filter.TableFilterElement;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;

/******************************************************************************************
 * This is the abstract popup dialog for the list of values editor. The single selection
 * mode and multiple selection mode inherit from this class.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

abstract class RListOfValuesPopupDialog extends RDialog implements PropertyChangeListener {

    protected static final String CANCEL = "Cancel";
    protected static final String SELECT = "Select";

    protected RButton cancelButton = new RButton(CANCEL);
    protected RButton selectButton = new RButton(SELECT);

    protected ListOfValuesModel selectionModel;
    protected ListOfValuesPageModel selectionPageModel;
    protected SelectableCriteria selectableCriteria = new SelectableCriteria();
    protected SelectableResults selectableResults;

    /******************************************************************************************
     * Creates new list of values dialog on a frame.
     ******************************************************************************************/
    protected RListOfValuesPopupDialog(JFrame frame) {
        super(frame, true);
        initialize();
    }

    /******************************************************************************************
     * Creates new list of values dialog on a dialog.
     ******************************************************************************************/
    protected RListOfValuesPopupDialog(JDialog dialog) {
        super(dialog, true);
        initialize();
    }

    /******************************************************************************************
     * Initializes the properties of the dialog.
     ******************************************************************************************/
    private void initialize() {
        setSize(300, 400);

        selectButton.addActionListener(createSelectActionListener());
        cancelButton.addActionListener(createCancelActionListener());

        selectButton.setEnabled(false);

        addButton(selectButton);
        addButton(cancelButton);
    }

    /******************************************************************************************
     * Creates an action listener for the select button.
     ******************************************************************************************/
    private ActionListener createSelectActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doSelection();
            }
        };
    }

    /******************************************************************************************
     * Creates an action listener for the cancel button.
     ******************************************************************************************/
    private ActionListener createCancelActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
    }

    /******************************************************************************************
     * Assigns a width to the dialog.
     * <p>
     * @param width The width to assign.
     ******************************************************************************************/
    protected void setPopupWidth(int width) {
        setSize(width, 400);
    }

    /******************************************************************************************
     * Assigns the page information to the dialog.
     * <p>
     * @param pageThreshold The page threshold.
     * @param pageSize The page size.
     ******************************************************************************************/
    protected void setPageInformation(int pageThreshold, int pageSize) {
        selectableCriteria.setPageThreshold(pageThreshold);
        selectableCriteria.setPageSize(pageSize);
    }

    /******************************************************************************************
     * Assigns the ListOfValuesModel to the dialog for retrieving selectable values.
     * <p>
     * @param selectionModel The ListOfValuesModel to assign.
     * <p>
     * @throws OldUIException Thrown if an error occurs refreshing the selectable values.
     ******************************************************************************************/
    protected void setSelectionModel(ListOfValuesModel selectionModel) {
        this.selectionModel = selectionModel;
    }

    /******************************************************************************************
     * Assigns the ListOfValuesPageModel to the dialog for retrieving selectable values.
     * <p>
     * @param selectionModel The ListOfValuesModel to assign.
     * <p>
     * @throws OldUIException Thrown if an error occurs refreshing the selectable values.
     ******************************************************************************************/
    protected void setSelectionPageModel(ListOfValuesPageModel selectionModel) {
        selectionPageModel = selectionModel;
    }

    /******************************************************************************************
     * Listener that monitors the transfer table for row selection changes and activates button.
     * It also monitors for changes in the filter applied to the table.
     ******************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();

        try {
            if (propertyName.equals(UIPropertyName.TABLE_TRANSFER_OCCURRED)) {
                Integer rowCount = (Integer) event.getNewValue();
                selectButton.setEnabled(rowCount > 0);
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_FILTER)) {
                applyFilter((TableFilterElement) event.getNewValue());
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_NEXT_PAGE)) {
                doNextPage();
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_PREV_PAGE)) {
                doPrevPage();
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_FIRST_PAGE)) {
                doFirstPage();
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_LAST_PAGE)) {
                doLastPage();
            } else if (propertyName.equals(UIPropertyName.LIST_OF_VALUES_PAGE)) {
                doPage((Integer) event.getNewValue());
            }
        } catch (UIException exception) {
            displayException(exception);
        }
    }

    /******************************************************************************************
     * Applies a filter to the search criteria and refreshes the selectable values.
     ******************************************************************************************/
    private void applyFilter(TableFilterElement element) throws UIException {
        selectableCriteria.setTableFilterElement(element);
        refreshSelectableValues();
    }

    /******************************************************************************************
     * Moves the selectable values to the next page.
     ******************************************************************************************/
    private void doNextPage() throws UIException {
        int page = selectableCriteria.getPage() + 1;
        if (page > selectableResults.getTotalPages()) {
            page = selectableResults.getTotalPages();
        }
        selectableCriteria.setPage(page);
        refreshSelectableValues();
    }

    /******************************************************************************************
     * Moves the selectable values to the previous page.
     ******************************************************************************************/
    private void doPrevPage() throws UIException {
        int page = selectableCriteria.getPage() - 1;
        if (page < 1) {
            page = 1;
        }
        selectableCriteria.setPage(page);
        refreshSelectableValues();
    }

    /******************************************************************************************
     * Moves the selectable values to the first page.
     ******************************************************************************************/
    private void doFirstPage() throws UIException {
        if (selectableCriteria.getPage() != 1) {
            selectableCriteria.setPage(1);
            refreshSelectableValues();
        }
    }

    /******************************************************************************************
     * Moves the selectable values to the last page.
     ******************************************************************************************/
    private void doLastPage() throws UIException {
        int totalPages = selectableResults.getTotalPages();
        if (selectableCriteria.getPage() != totalPages) {
            selectableCriteria.setPage(totalPages);
            refreshSelectableValues();
        }
    }

    /*****************************************************************************************
     * Moves the selectable values to the specified page.
     ******************************************************************************************/
    private void doPage(Integer page) throws UIException {
        selectableCriteria.setPage(page);
        refreshSelectableValues();
    }

    /******************************************************************************************
     * Assigns the row displayer that will display the table rows within the popup dialog.
     * <p>
     *@param param The TableRowDisplayer to install.
     *****************************************************************************************/
    protected abstract void setRowDisplayer(TableRowDisplayer displayer);

    /******************************************************************************************
     * Assigns the selected values. These will be removed from the selectable values if they
     * already exist.
     * <p>
     * @param values The values to assign.
     ******************************************************************************************/
    protected abstract void setSelectedValues(Object[] values);

    /******************************************************************************************
     * Refreshes the selectable values displayed in the selection panel.
     * <p>
     * @throws OldUIException Thrown if error occurs retrieving data from the model.
     ******************************************************************************************/
    protected abstract void refreshSelectableValues() throws UIException;

    /******************************************************************************************
     * Resets the page title.
     ******************************************************************************************/
    protected abstract void resetPageTitle();

    /******************************************************************************************
     * Implements what occurs when a selection takes place
     ******************************************************************************************/
    protected abstract void doSelection();
}
