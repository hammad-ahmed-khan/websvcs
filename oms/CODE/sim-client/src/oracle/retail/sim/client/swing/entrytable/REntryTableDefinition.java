package oracle.retail.sim.client.swing.entrytable;

/********************************************************************************************************
 * REntryTableDefinition
 * <p>
 * This interface defines the various aspects of a table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface REntryTableDefinition {

    /****************************************************************************************************
     * The Class of the data that each row in the table represents. This will be passed into the table
     * model class.
     ***************************************************************************************************/
    Class getDataClass();

    /****************************************************************************************************
     * A list of REntryTableAttributes that represent the attribute of each column.
     ***************************************************************************************************/
    REntryAttribute[] getAttributes();
}
