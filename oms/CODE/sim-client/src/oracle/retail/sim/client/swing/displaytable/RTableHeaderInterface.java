package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;
import java.awt.Font;

/******************************************************************************************
 * Interface must be implemented by any JTable subclass that wishes to use the RTableHeader
 * as a header renderer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface RTableHeaderInterface {

    /******************************************************************************************
     * Retrieves the table header background color.
     *****************************************************************************************/
    Color getTableHeaderBackground();

    /******************************************************************************************
     * Retrieves the table header foreground color.
     *****************************************************************************************/
    Color getTableHeaderForeground();

    /******************************************************************************************
     * Retrieves the table header font.
     *****************************************************************************************/
    Font getTableHeaderFont();

    /******************************************************************************************
     * Retrieves the numerical order of sort for the given column.
     *****************************************************************************************/
    int getSortNumber(int column);

    /******************************************************************************************
     * Retrieves where or not the column is sorted ascending.
     *****************************************************************************************/
    boolean getSortAscending(int column);
}
