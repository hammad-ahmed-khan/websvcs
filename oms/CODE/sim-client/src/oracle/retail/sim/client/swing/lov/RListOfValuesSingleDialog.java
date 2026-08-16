package oracle.retail.sim.client.swing.lov;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Collection;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/******************************************************************************************
 * This is the popup dialog for the list of values editor when it is in single selection mode.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

class RListOfValuesSingleDialog extends RListOfValuesPopupDialog implements REventListener {
    private static final long serialVersionUID = 720732497871128071L;

    private RListOfValuesTable displayTable = new RListOfValuesTable();
    private RListOfValuesTablePane displayPane = new RListOfValuesTablePane(displayTable);
    private RListOfValuesPagePanel pagePanel = new RListOfValuesPagePanel();

    private static final String VALIDATE = "Validate";

    /******************************************************************************************
     * Creates new list of values dialog on a frame.
     ******************************************************************************************/
    public RListOfValuesSingleDialog(JFrame frame) {
        super(frame);
        initializeSingleDialog();
    }

    /******************************************************************************************
     * Creates new list of values dialog on a dialog.
     ******************************************************************************************/
    public RListOfValuesSingleDialog(JDialog dialog) {
        super(dialog);
        initializeSingleDialog();
    }

    /******************************************************************************************
     * Initializes the properties of the dialog.
     ******************************************************************************************/
    private void initializeSingleDialog() {
        setSize(300, 400);

        pagePanel.setOpaque(false);

        displayTable.registerDoubleClickAction(this, SELECT);
        displayTable.registerSingleClickAction(this, VALIDATE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(displayPane, BorderLayout.CENTER);
        container.add(pagePanel, BorderLayout.SOUTH);
    }

    /******************************************************************************************
     * Assigns the table row displayer.
     * <p>
     *@param param The TableRowDisplayer to install.
     *****************************************************************************************/
    protected void setRowDisplayer(TableRowDisplayer displayer) {
        displayTable.setTableRowDisplayer(displayer);
    }

    /******************************************************************************************
     * Implements the REventListener method to deal with the single click on the table.
     ******************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SELECT)) {
            doSelection();
        } else if (command.equals(VALIDATE)) {
            selectButton.setEnabled(displayTable.hasSelectedRows());
        }
    }

    /******************************************************************************************
     * Assigns the selected values. This method does nothing as the single selection mode
     * does not display selected values.
     * <p>
     * @param values The values to assign.
     ******************************************************************************************/
    protected void setSelectedValues(Object[] values) {
    }

    /******************************************************************************************
     * Refreshes the selectable values displayed in the selection panel.
     * <p>
     * @throws OldUIException Thrown if error occurs retrieving data from the model.
     ******************************************************************************************/
    protected void refreshSelectableValues() throws UIException {
        if (selectionPageModel != null) {
            selectableCriteria.setTableSortElement(displayTable.getTableSortElement());
            selectableResults = selectionPageModel.getSelectableValues(selectableCriteria);
            displaySelectableValues(selectableResults.getData());
            resetPageTitle();
            return;
        }
        displaySelectableValues(selectionModel.getSelectableValues());
    }

    /******************************************************************************************
     * Displays the selectable values in the table.
     ******************************************************************************************/
    private void displaySelectableValues(Collection values) {
        displayTable.resetTable();
        displayTable.addRows(values);
        displayTable.resort();
    }

    /******************************************************************************************
     * Resets the page title.
     ******************************************************************************************/
    protected void resetPageTitle() {
        int page = selectableCriteria.getPage();
        int pageSize = selectableCriteria.getPageSize();
        int pageStart = page * pageSize - pageSize + 1;
        int dataSize = selectableResults.getDataSize();
        pagePanel.setPageTitle(pageStart, pageStart + dataSize - 1, selectableResults.getTotalValues());
        pagePanel.setPage(page, selectableResults.getTotalPages());
    }

    /******************************************************************************************
     * Handles what occurs when a double click action is taken or select button is pressed.
     ******************************************************************************************/
    protected void doSelection() {
        Object[] dataArray = displayTable.getAllSelectedData().toArray();
        firePropertyChange(UIPropertyName.LIST_OF_VALUES_SELECTION, null, dataArray);
        dispose();
    }
}
