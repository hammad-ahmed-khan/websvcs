package oracle.retail.sim.client.swing.entrytable;

/********************************************************************************************************
 * REntryTableEditorCreator
 * <p>
 * Interface that table columns use to access the editors within the column.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface REntryTableEditorCreator {

    /****************************************************************************************************
     * Method is called when the table column needs to generate a new editor to display in a cell.
     ***************************************************************************************************/
    REntryTableEditor createEditor();
}
