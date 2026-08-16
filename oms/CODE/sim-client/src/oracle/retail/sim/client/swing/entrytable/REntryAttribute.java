package oracle.retail.sim.client.swing.entrytable;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * REntryAttribute
 * <p>
 * Defines a basic attribute properties of the entry table. Each attribute is assigned to one column.
 * This column uses its attribute to access the data object for the row and determine what data it is
 * responsible for displaying.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class REntryAttribute implements Displayable {

    private String title;
    private String attribute;
    private String identifier;
    private boolean isEditable;
    private boolean isRequired;
    private REntryTableEditorCreator editorCreator;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the column.
     ***************************************************************************************************/
    public REntryAttribute(String title) {
        this(title, StringConstants.EMPTY, null, null, false, false);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute represented by the column.
     ***************************************************************************************************/
    public REntryAttribute(String title, String attribute) {
        this(title, attribute, null, null, false, false);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute represented by the column.
     * @param isRequired True if the column must always be visible, false otherwise.
     ***************************************************************************************************/
    public REntryAttribute(String title, String attribute, boolean isRequired) {
        this(title, attribute, null, null, false, isRequired);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute represented by the column.
     * @param identifier The identifier to assign to any editors that are displayed within the column.
     * @param isRequired True if the column must always be visible, false otherwise.
     ***************************************************************************************************/
    public REntryAttribute(String title, String attribute, String identifier, REntryTableEditorCreator editor, boolean isEditable) {
        this(title, attribute, identifier, editor, isEditable, false);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the column.
     * @param attribute The attribute represented by the column.
     * @param identifier The editor identifier.
     * @param editorCreator The REntryTableEditorCreator responsible for creating the column editor.
     * @param isEditable True if the column should allow editing, false otherwise
     * @param isRequired True if the column must always be visible, false otherwise.
     ***************************************************************************************************/
    public REntryAttribute(String title, String attribute, String identifier, REntryTableEditorCreator editorCreator, boolean isEditable, boolean isRequired) {
        this.title = title;
        this.attribute = attribute;
        this.identifier = identifier;
        this.editorCreator = editorCreator;
        this.isEditable = isEditable;
        this.isRequired = isRequired;
    }

    /****************************************************************************************************
     * Retrieves the title of the column.
     ***************************************************************************************************/
    public String getTitle() {
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
    public void setTitle(String title) {
        this.title = title;
    }

    /****************************************************************************************************
     * Retrieves the attribute.
     ***************************************************************************************************/
    public String getAttribute() {
        return attribute;
    }

    /****************************************************************************************************
     * Assigns the attribute that this entry attribute actually uses to access the data object.
     * <p>
     * @param attribute The attribute.
     ***************************************************************************************************/
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /****************************************************************************************************
     * Retrieves the identifier associated with the editor of the column.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Assigns the identifier to associate with the editor of the column. When an editor is created for
     * the column, this identifier will be assigned to it.
     * <p>
     * @param identifier The identifier.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the editor creator the will generate editors for the column.
     ***************************************************************************************************/
    public REntryTableEditorCreator getEditorCreator() {
        return editorCreator;
    }

    /****************************************************************************************************
     * Assigns the editor creator that will be used by the column when it needs to display its value.
     * <p>
     * @param editorCreatore The REntryTableEditorCreator to use.
     ***************************************************************************************************/
    public void setEditorCreator(REntryTableEditorCreator editorCreator) {
        this.editorCreator = editorCreator;
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
     * Returns the translated title of the column.
     ***************************************************************************************************/
    public String toDisplayString() {
        return Translator.getText(getTitle());
    }
}
