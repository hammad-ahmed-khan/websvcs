package oracle.retail.sim.client.swing.lov;

/******************************************************************************************
 * Interface must be implemented by any subclass that wishes to be assigned to a list-of-values
 * editor as its displayer. The implementation of this may make server calls, read files
 * or any number of possibilities. However, this method may NOT throw errors and SHOULD NOT
 * return a null value, but rather an empty one. Errors must be dealt with in the local
 * implementation of the displayer. This interface is used to display selectable objects
 * in the List Of Values editors. There are two ready available implementations:
 * DefaultListOfValuesDisplayer and SimpleListOfValuesDisplayer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface ListOfValuesDisplayer {

    String getEntryText(Object object);

    String getDescriptionText(Object object);
}
