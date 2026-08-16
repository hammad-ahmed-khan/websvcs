package oracle.retail.sim.client.swing.table;

import java.util.Objects;
import javax.swing.table.TableCellRenderer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;

/********************************************************************************************************
 * Defines one attribute/column of a SIM table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableAttribute {

    private String title = StringConstants.EMPTY;
    private String attribute = StringConstants.EMPTY;
    private TableCellRenderer renderer;
    private Displayer displayer;
    private SimTableEditor editor;
    private boolean editable = true;
    private int minWidth = -1;
    private int prefWidth = -1;
    private int maxWidth = -1;

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute) {
        this(title, attribute, (Displayer) null, null, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param editable True if the column should be editable, false otherwise
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, boolean editable) {
        this(title, attribute, (Displayer) null, null, editable);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param displayer The displayer of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, Displayer displayer) {
        this(title, attribute, displayer, null, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param renderer The renderer of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, TableCellRenderer renderer) {
        this(title, attribute, renderer, null, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param editor The editor of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, SimTableEditor editor) {
        this(title, attribute, (Displayer) null, editor, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param displayer The displayer of the column.
     * @param editor The editor of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, Displayer displayer, SimTableEditor editor) {
        this(title, attribute, displayer, editor, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param renderer The renderer of the column.
     * @param editor The editor of the column.
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, TableCellRenderer renderer, SimTableEditor editor) {
        this(title, attribute, renderer, editor, true);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param displayer The displayer of the column.
     * @param editor The editor of the column.
     * @param editable True if the column should be editable, false otherwise
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, Displayer displayer, SimTableEditor editor, boolean editable) {
        setTitle(title);
        setAttribute(attribute);
        setDisplayer(displayer);
        setEditor(editor);
        setEditable(editable);
    }

    /****************************************************************************************************
     * Constructor.
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute of the column.
     * @param renderer The displayer of the column.
     * @param editor The editor of the column.
     * @param editable True if the column should be editable, false otherwise
     ***************************************************************************************************/
    public SimTableAttribute(String title, String attribute, TableCellRenderer renderer, SimTableEditor editor, boolean editable) {
        setTitle(title);
        setAttribute(attribute);
        setRenderer(renderer);
        setEditor(editor);
        setEditable(editable);
    }

    /****************************************************************************************************
     * Retrieves the title for the column.
     ***************************************************************************************************/
    public String getTitle() {
        return title;
    }

    /****************************************************************************************************
     * Retrieves the attribute represented by the column.
     ***************************************************************************************************/
    public String getAttribute() {
        return attribute;
    }

    /****************************************************************************************************
     * Retrieve the renderer for this attribute. If a renderer is set on the attribute, it will override
     * the displayer value.
     ***************************************************************************************************/
    public TableCellRenderer getRenderer() {
        return renderer;
    }

    /****************************************************************************************************
     * Retrieve the displayer for this attribute.
     ***************************************************************************************************/
    public Displayer getDisplayer() {
        return displayer;
    }

    /****************************************************************************************************
     * Retrieves the table editor for this attribute.
     ***************************************************************************************************/
    public SimTableEditor getEditor() {
        return editor;
    }

    /****************************************************************************************************
     * Retrieves whether or not the table attribute is editable
     ***************************************************************************************************/
    public boolean isEditable() {
        return editable;
    }

    /****************************************************************************************************
     * Assigns the title to the column represented by this attribute. Translation will occur
     * automatically within the framework.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (StringUtility.isNullOrEmpty(title)) {
            this.title = StringConstants.EMPTY;
        } else {
            this.title = title;
        }
    }

    /****************************************************************************************************
     * Assigns the attribute this column represents. The attribute should be the method name on the Class
     * object without the "get", "set", or "is".
     ***************************************************************************************************/
    public void setAttribute(String attribute) {
        if (StringUtility.isNullOrEmpty(attribute)) {
            this.attribute = StringConstants.EMPTY;
        } else {
            this.attribute = attribute;
        }
    }

    /****************************************************************************************************
     * Assigns a renderer responsible for drawing the attribute in display mode. If set, this value will
     * override the displayer.
     ***************************************************************************************************/
    public void setRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    /****************************************************************************************************
     * Assigns a displayer responsible for displaying the text of this attribute.
     ***************************************************************************************************/
    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }

    /****************************************************************************************************
     * Assigns an editor responsible for editing the content of this attribute on the Class.
     ***************************************************************************************************/
    public void setEditor(SimTableEditor editor) {
        this.editor = editor;
    }

    /****************************************************************************************************
     * Assigns whether or not the table attribute is editable.
     ***************************************************************************************************/
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /****************************************************************************************************
     * Assigns the minimum, preferred and maximum width in pixels in one stroke. Note that
     * SimTable.LABEL_WIDTH and SimTable.STRETCHABLE are valid values.
     ***************************************************************************************************/
    public void setWidth(int minWidth, int preferredWidth, int maxWidth) {
        setMinWidth(minWidth);
        setPreferredWidth(preferredWidth);
        setMaxWidth(maxWidth);
    }

    /****************************************************************************************************
     * Assigns the minimum width in pixels in one stroke. Note that SimTable.LABEL_WIDTH and
     * SimTable.STRETCHABLE are valid values.
     ***************************************************************************************************/
    public void setMinWidth(int minWidth) {
        if (minWidth < -1) {
            minWidth = -1;
        }
        this.minWidth = minWidth;
    }

    /****************************************************************************************************
     * Assigns the preferred width in pixels in one stroke. Note that SimTable.LABEL_WIDTH and
     * SimTable.STRETCHABLE are valid values.
     ***************************************************************************************************/
    public void setPreferredWidth(int prefWidth) {
        if (prefWidth < -1) {
            prefWidth = -1;
        }
        this.prefWidth = prefWidth;
    }

    /****************************************************************************************************
     * Assigns the maximum width in pixels in one stroke. Note that SimTable.LABEL_WIDTH and
     * SimTable.STRETCHABLE are valid values.
     ***************************************************************************************************/
    public void setMaxWidth(int maxWidth) {
        if (maxWidth < -1) {
            maxWidth = -1;
        }
        this.maxWidth = maxWidth;
    }

    /****************************************************************************************************
     * Retrieves the minimum width.
     ***************************************************************************************************/
    public int getMinWidth() {
        return minWidth;
    }

    /****************************************************************************************************
     * Retrieves the preferred width.
     ***************************************************************************************************/
    public int getPreferredWidth() {
        return prefWidth;
    }

    /****************************************************************************************************
     * Retrieves the maximum width.
     ***************************************************************************************************/
    public int getMaxWidth() {
        return maxWidth;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        SimTableAttribute that = (SimTableAttribute) object;
        return Objects.equals(attribute, that.attribute);
    }

    public int hashCode() {
        return Objects.hash(attribute);
    }
}
