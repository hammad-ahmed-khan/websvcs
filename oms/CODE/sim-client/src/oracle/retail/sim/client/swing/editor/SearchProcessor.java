package oracle.retail.sim.client.swing.editor;

import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * This interface needs to be implemented by any class that wishes to handle the customizable
 * functionality of an RSearchFieldEditor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SearchProcessor {

    /**
     * Returns the displayer that will handle displaying the editable entry half of the search field.
     */
    BasicDisplayer getEntryDisplayer();

    /**
     * Returns the displayer that will handle displaying the un-editable value half of the search field.
     */
    BasicDisplayer getValueDisplayer();

    /**
     * The implementation of this method should return the full business object for the input ID.
     */
    Object searchById(String id) throws Exception;

    /**
     * This method should validate the data object and return the validated object. This method is called
     * whenever someone attempts to set a full data object on the search editor.
     */
    Object validateData(Object data) throws Exception;
}
