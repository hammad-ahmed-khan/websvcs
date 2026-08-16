package oracle.retail.sim.client.swing.widget;

/******************************************************************************************
 * Interface that must be implemented by long text field that wants to receive changes 
 * from the dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface RLongTextListener {

    void updateText(String text);

}
