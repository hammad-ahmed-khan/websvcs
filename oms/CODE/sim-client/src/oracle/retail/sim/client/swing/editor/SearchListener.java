package oracle.retail.sim.client.swing.editor;

/********************************************************************************************************
 * This interface needs to be implemented by any class that wishes to listen to the "Search" button in an
 * RSearchEditor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SearchListener {

    void search();

    void assign(Object value);
}
