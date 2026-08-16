package oracle.retail.sim.client.swing.lov;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/******************************************************************************************
 * This is the popup dialog for the list of values editor when in multiple selection mode.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

class RListOfValuesMultipleDialog extends RListOfValuesPopupDialog implements PropertyChangeListener {
    private static final long serialVersionUID = 4658011244197456527L;

    private RListOfValuesTransferPanel selectionPanel = new RListOfValuesTransferPanel();

    /******************************************************************************************
     * Creates new list of values dialog on a frame.
     ******************************************************************************************/
    public RListOfValuesMultipleDialog(JFrame frame) {
        super(frame);
        initializeMultipleDialog();
    }

    /******************************************************************************************
     * Creates new list of values dialog on a dialog.
     ******************************************************************************************/
    public RListOfValuesMultipleDialog(JDialog dialog) {
        super(dialog);
        initializeMultipleDialog();
    }

    /******************************************************************************************
     * Initializes the properties of the dialog.
     ******************************************************************************************/
    private void initializeMultipleDialog() {
        selectionPanel.setVertical();
        selectionPanel.addPropertyChangeListener(this);
        selectionPanel.setSelectableChangeListener(this);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(selectionPanel, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Assigns a layout alignment of the dialog.
     * <p>
     * @param isHorizontal True if the dialog should layout horizontally, false otherwise.
     ******************************************************************************************/
    protected void setLayoutAlignment(boolean isHorizontal) {
        if (isHorizontal) {
            selectionPanel.setHorizontal();
        } else {
            selectionPanel.setVertical();
        }
    }

    /******************************************************************************************
     * Assigns the row displayer that will display the table rows within the popup dialog.
     * <p>
     *@param param The TableRowDisplayer to install.
     *****************************************************************************************/
    protected void setRowDisplayer(TableRowDisplayer displayer) {
        selectionPanel.setTableRowDisplayer(displayer);
    }

    /******************************************************************************************
     * Assigns the selected values. These will be removed from the selectable values if they
     * already exist.
     * <p>
     * @param values The values to assign.
     ******************************************************************************************/
    protected void setSelectedValues(Object[] values) {
        selectionPanel.setSelectedItems(values);
    }

    /******************************************************************************************
     * Refreshes the selectable values displayed in the selection panel.
     * <p>
     * @throws OldUIException Thrown if error occurs retrieving data from the model.
     ******************************************************************************************/
    protected void refreshSelectableValues() throws UIException {
        if (selectionPageModel != null) {
            selectableCriteria.setTableSortElement(selectionPanel.getOriginalTableSortElement());
            selectableResults = selectionPageModel.getSelectableValues(selectableCriteria);
            selectionPanel.setSelectableItems(selectableResults.getData().toArray());
            resetPageTitle();
            return;
        }
        selectionPanel.setSelectableItems(selectionModel.getSelectableValues().toArray());
    }

    /******************************************************************************************
     * Resets the page title.
     ******************************************************************************************/
    protected void resetPageTitle() {
        int page = selectableCriteria.getPage();
        int pageSize = selectableCriteria.getPageSize();
        int pageStart = page * pageSize - pageSize + 1;
        int dataSize = selectableResults.getDataSize();
        selectionPanel.setPageTitle(pageStart, pageStart + dataSize - 1, selectableResults.getTotalValues());
        selectionPanel.setPage(page, selectableResults.getTotalPages());
    }

    /******************************************************************************************
     * Handles what occurs when a double click action is taken or select button is pressed.
     ******************************************************************************************/
    protected void doSelection() {
        Object[] dataArray = selectionPanel.getSelectedItems().toArray();
        firePropertyChange(UIPropertyName.LIST_OF_VALUES_SELECTION, null, dataArray);
        dispose();
    }
}
