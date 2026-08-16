package oracle.retail.sim.client.swing.editor;

/******************************************************************************************
 * Receives the results of a search. This must be implemented by any class interested
 * in receiving the results of a search field editor search.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface SearchReceiver {

    void setData(Object value);
}
