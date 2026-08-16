package oracle.retail.sim.client.swing.rowtitletable;

import javax.swing.AbstractListModel;

/****************************************************************************************************
 * This is the list model used by the JList that is the row titles of the table.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ****************************************************************************************************/

public class RowTitleTableModel extends AbstractListModel {
    private static final long serialVersionUID = 377422412132707257L;

    private String[] rowTitles;

    /****************************************************************************************************
     * Constructs a new RowTitleTableModel.
     ****************************************************************************************************/
    public RowTitleTableModel() {
        setRowTitles(new String[1]);
    }

    /****************************************************************************************************
     * Constructs a new RowTitleTableModel.
     * <p>
     * @param titles The row titles to display.
     ****************************************************************************************************/
    public RowTitleTableModel(String[] titles) {
        setRowTitles(titles);
    }

    /****************************************************************************************************
     * Assigns the row titles to be displayed in the table.
     * <p>
     * @param titles An array of titles to display as the row titles.
     ****************************************************************************************************/
    public void setRowTitles(String[] titles) {
        rowTitles = titles;
    }

    /****************************************************************************************************
     * Retrieves the number of row titles in the model.
     * <p>
     * @return The number of row titles in the model.
     ****************************************************************************************************/
    public int getSize() {
        return rowTitles.length;
    }

    /****************************************************************************************************
     * Retrieves the element located at the specified index.
     * <p>
     * @param index The index into the row titles.
     * @return The row title at the requested index.
     ****************************************************************************************************/
    public Object getElementAt(int index) {
        return rowTitles[index];
    }
}
