package oracle.retail.sim.client.swing.table;

import java.awt.Color;
import java.awt.Font;

/********************************************************************************************************
 * Interface must be implemented by any JTable subclass that wishes to use the RTableHeader as a header
 * renderer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SimTableHeaderInterface {

    /****************************************************************************************************
     * Retrieves the table header background color.
     ***************************************************************************************************/
    Color getTableHeaderBackground();

    /****************************************************************************************************
     * Retrieves the table header foreground color.
     ***************************************************************************************************/
    Color getTableHeaderForeground();

    /****************************************************************************************************
     * Retrieves the table header font.
     ***************************************************************************************************/
    Font getTableHeaderFont();
}
