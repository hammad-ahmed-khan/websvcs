package oracle.retail.sim.client.swing.frame;

import java.awt.Component;
import java.awt.Cursor;

/**********************************************************************************
 * This interface is implemented by classes that handle the resizing of the window
 * when the user grabs the edge or whatnot.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **********************************************************************************/

interface Resizer {

    /******************************************************************************
     * Retrieves the cursor to display while this resizing is happening.
     * <p>
     * @return Cursor
     ******************************************************************************/
    Cursor getCursor();

    /*******************************************************************************
     * Resizes the component with the specified change in X/Y.
     * <p>
     * @param component The component to resize.
     * @param deltaX The change in the x location.
     * @param deltaY The change in the y location.
     ******************************************************************************/
    void resize(Component component, int deltaX, int deltaY);
}
