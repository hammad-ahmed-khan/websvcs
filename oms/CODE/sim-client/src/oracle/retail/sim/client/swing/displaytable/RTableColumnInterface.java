package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;

/******************************************************************************************
 * Interface must be implemented by any JTable subclass that wishes to use the
 * RTableCellRenderer as a cell renderer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface RTableColumnInterface {

    /******************************************************************************************
     * Retrieves the column background color at the specified row and column.
     *****************************************************************************************/
    Color getColumnBackground(int row, int column);

    /******************************************************************************************
     * Retrieves the column foreground color at the specified row and column.
     *****************************************************************************************/
    Color getColumnForeground(int row, int column);
}
