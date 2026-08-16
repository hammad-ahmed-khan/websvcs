package oracle.retail.sim.client.swing.entrytable;

import java.awt.Color;
import java.awt.Font;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * REntryColumn
 * <p>
 * This class represents a single column within the REntry table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class REntryColumn {

    private REntryTableEditorCreator editorCreator;
    private String title;
    private String attribute;
    private String identifier;
    private boolean isSort;
    private boolean isEditable;
    private boolean isRequired;
    private boolean isVisible = true;
    private int sequence = -1;
    private int width = EditorConstants.COLUMN_STRETCHABLE;
    private Color foregroundColor;
    private Color backgroundColor;
    private Font font;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public REntryColumn() {
    }

    /****************************************************************************************************
     * Retrieves the editor creator assigned to the column.
     * <p>
     * @return The editor creator.
     ***************************************************************************************************/
    public REntryTableEditorCreator getEditorCreator() {
        return editorCreator;
    }

    /****************************************************************************************************
     * Assigns the editor creator to the column. The editor creator is responsible for generating the
     * appropriate editor to display within each cell of the column as rows are added to the table.
     * <p>
     * @param creator The REntryTableEditorCreator to assign.
     ***************************************************************************************************/
    public void setEditorCreator(REntryTableEditorCreator creator) {
        editorCreator = creator;
    }

    /****************************************************************************************************
     * Assign the identifier to the column. Identifiers are assigned to the editors created by the editor
     * creator. These identifiers are used throughout the framework to identify the editor.
     * <p>
     * @param identifier The identifier to assign.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the identifier of the column.
     * <p>
     * @return The identifier.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Assigns foreground color to the column. This should override the row foreground.
     * <p>
     * @param foreground The foreground color.
     ***************************************************************************************************/
    public void setForeground(Color foreground) {
        foregroundColor = foreground;
    }

    /****************************************************************************************************
     * Retrieves the foreground color of the column.
     * <p>
     * @return The foreground color of the column.
     ***************************************************************************************************/
    public Color getForeground() {
        return foregroundColor;
    }

    /****************************************************************************************************
     * Assigns background color to the column.
     * <p>
     * @param background The background color.
     ***************************************************************************************************/
    public void setBackground(Color background) {
        backgroundColor = background;
    }

    /****************************************************************************************************
     * Retrieves the background color of the column.
     * <p>
     * @return The background color property of the column.
     ***************************************************************************************************/
    public Color getBackground() {
        return backgroundColor;
    }

    /****************************************************************************************************
     * Assigns font to the column.
     * <p>
     * @param font The font to assign to the column.
     ***************************************************************************************************/
    public void setFont(Font font) {
        this.font = font;
    }

    /****************************************************************************************************
     * Retrieves the font of the column.
     * <p>
     * @return The font of the column.
     ***************************************************************************************************/
    public Font getFont() {
        return font;
    }

    /****************************************************************************************************
     * Retrieves the attribute of the column.
     * <p>
     * @return The attribute.
     ***************************************************************************************************/
    protected String getAttribute() {
        return attribute;
    }

    /****************************************************************************************************
     * Assigns the attribute of the column. This is the attribute that this column represents on the data
     * object that will be assigned to the row.
     * <p>
     * param The attribute.
     ***************************************************************************************************/
    protected void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /****************************************************************************************************
     * Returns true if the column is editable, false otherwise.
     ***************************************************************************************************/
    protected boolean isEditable() {
        return isEditable;
    }

    /****************************************************************************************************
     * Assigns whether or not the column should be editable.
     * <p>
     * @param isEditable True if the column is editable, false otherwise.
     ***************************************************************************************************/
    protected void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /****************************************************************************************************
     * Return true if the column is required for display on the table, false otherwise.
     ***************************************************************************************************/
    protected boolean isRequired() {
        return isRequired;
    }

    /****************************************************************************************************
     * Assigns whether or not the column is required.
     * <p>
     * @param isRequired True if the column is required for display on the table, false otherwise.
     ***************************************************************************************************/
    protected void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    /****************************************************************************************************
     * Returns the sequence this column appears in the row.
     ***************************************************************************************************/
    protected int getSequence() {
        return sequence;
    }

    /****************************************************************************************************
     * Assigns the sequence this column appears in the row.
     * <p>
     * @param sequence The sequence.
     ***************************************************************************************************/
    protected void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /****************************************************************************************************
     * Retrieves the title of the column.
     ***************************************************************************************************/
    protected String getTitle() {
        if (title == null) {
            return StringConstants.EMPTY;
        }
        return title;
    }

    /****************************************************************************************************
     * Assigns the title of the column. The title may simply be english text. This title is used as a key
     * to translation at the time of display, but is always stored in its original form.
     * <p>
     * @param title The title.
     ***************************************************************************************************/
    protected void setTitle(String title) {
        this.title = title;
    }

    /****************************************************************************************************
     * Assigns whether or not the column is visible. A column marked as not visible will not appear
     * within the row.
     * <p>
     * @param visible True if the column should be visible, false otherwise.
     ***************************************************************************************************/
    protected void setVisible(boolean visible) {
        isVisible = visible;
    }

    /****************************************************************************************************
     * Retrieves whether or not the column is visible.
     ***************************************************************************************************/
    protected boolean isVisible() {
        return isVisible;
    }

    /****************************************************************************************************
     * Returns true if the column is to be sorted, false otherwise.
     ***************************************************************************************************/
    protected boolean isPrimarySort() {
        return isSort;
    }

    /****************************************************************************************************
     * Assigns whether or not the column is to be sorted. At this time, there is no secondary sort.
     * <p>
     * @param sort True if the column should be sorted, false otherwise.
     ***************************************************************************************************/
    protected void setPrimarySort(boolean sort) {
        isSort = sort;
    }

    /****************************************************************************************************
     * Retrieves the width of the column (measured in pixels).
     ***************************************************************************************************/
    protected int getWidth() {
        return width;
    }

    /****************************************************************************************************
     * Assigns the width of the column (measured in pixels).
     * <p>
     * @param width The width.
     ***************************************************************************************************/
    protected void setWidth(int width) {
        this.width = width;
    }

    /****************************************************************************************************
     * Returns the translated title of the column.
     ***************************************************************************************************/
    public String toDisplayString() {
        return Translator.getText(getTitle());
    }

    /****************************************************************************************************
     * Returns string describing this column properties.
     ***************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("ColumnProperties [");
        buffer.append("Title = ").append(getTitle());
        buffer.append("; Attribute = ").append(getAttribute());
        buffer.append("; Identifier = ").append(getIdentifier());
        buffer.append("; Width = ").append(getWidth());
        buffer.append("; Foreground Color = ").append(getForeground());
        buffer.append("; Background Color = ").append(getBackground());
        buffer.append("; Font = ").append(getFont());
        buffer.append("]");
        return buffer.toString();
    }
}
