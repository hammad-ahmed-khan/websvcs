package oracle.retail.sim.client.swing.table;

import javax.swing.table.TableColumn;
import oracle.retail.sim.common.core.type.Displayer;

/********************************************************************************************************
 * Represents a column in a table model. This column is intended to be used with the SimTableModel class
 * or a suitable subclass of this. It contains the attribute, title, editable and sortable properties.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableColumn extends TableColumn {
    private static final long serialVersionUID = 4259306474402947121L;

    private boolean sortable = true;
    private boolean editable = true;
    private String title;
    private String attribute;
    private Displayer displayer;

    /****************************************************************************************************
     * Creates a column with the given title and attribute This constructor will create an editable
     * column if the table allows it, and if the model object supplies an appropriate write method.
     * @param title The column title.
     * @param attribute The column attribute.
     ***************************************************************************************************/
    public SimTableColumn(String title, String attribute) {
        this(title, attribute, true, true);
    }

    /****************************************************************************************************
     * Creates a column with the given title, attribute, editable and sortable properties.
     * <p>
     * @param title The title to display in the column (in English - translation occurs automatically).
     * @param attribute The attribute that this column represents. This property (following the Java
     *            Beans spec.) determines what is/get method to use to "read" data, as well as what set
     *            method to use to "write" data (if one exists.) Currently, indexed attributes are not
     *            supported, but there is no reason this could not be added.
     * @param editable Whether this column should be editable or not. Even if this is set to true, the
     *            column may still not be editable if either the table is declared to be uneditable, or
     *            if there is no appropriate "write" method on the model object.
     * @param sortable True if the column should be sortable, false otherwise.
     ***************************************************************************************************/
    public SimTableColumn(String title, String attribute, boolean editable, boolean sortable) {
        this.title = title;
        this.attribute = attribute;
        this.editable = editable;
        this.sortable = sortable;
    }

    /****************************************************************************************************
     * Retrieves the untranslated title of this column.
     ***************************************************************************************************/
    public String getTitle() {
        return title;
    }

    /****************************************************************************************************
     * Gets the attribute for this column. This attribute should correspond to an is/get method as well
     * as an optional set method on the model object. Each column in this table model represents an
     * attribute on a bean object. This attribute determines what method is called (combination of
     * get/set methods on objects) on the business object for this columns values.
     ***************************************************************************************************/
    public String getAttribute() {
        return attribute;
    }

    /****************************************************************************************************
     * Determines if this column is flagged as editable in the column definition.
     ***************************************************************************************************/
    public boolean isEditable() {
        return editable;
    }

    /****************************************************************************************************
     * Set the editable property for this column.
     ***************************************************************************************************/
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /****************************************************************************************************
     * Determines if this column allows sorting.
     ***************************************************************************************************/
    public boolean isSortable() {
        return sortable;
    }

    /****************************************************************************************************
     * Sets the sortable property for this column.
     ***************************************************************************************************/
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /****************************************************************************************************
     * Returns the displayer assigns to this column
     ***************************************************************************************************/
    public Displayer getDisplayer() {
        return displayer;
    }

    /****************************************************************************************************
     * Sets the displayer assigned to this column.
     ***************************************************************************************************/
    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }
}
