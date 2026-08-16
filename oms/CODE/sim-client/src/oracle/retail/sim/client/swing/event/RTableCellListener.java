package oracle.retail.sim.client.swing.event;

/******************************************************************************************
 * This interface must be implemented by objects that wish to receive generic oracle
 * retail table cell events.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface RTableCellListener {

    void processTableCellEvent(RTableCellEvent event);
}
