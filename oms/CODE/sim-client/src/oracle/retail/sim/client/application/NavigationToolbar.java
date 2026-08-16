package oracle.retail.sim.client.application;

import javax.swing.JButton;
import javax.swing.JToolBar;

/********************************************************************************************************
 * A toolbar that takes a set of buttons. This is used for the main navigation of the application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class NavigationToolbar extends JToolBar {

    /****************************************************************************************************
     * Display this defined set of buttons.
     ***************************************************************************************************/
    public abstract void setButtons(JButton[] buttons);

    /****************************************************************************************************
     * Return the defined set of buttons for the toolbar.
     ***************************************************************************************************/
    public abstract JButton[] getButtons();

    /****************************************************************************************************
     * Handle special key events delivered to the application toolbar.
     ***************************************************************************************************/
    public abstract void doHotKeyPressed(int keyCode);

    /****************************************************************************************************
     * Gets the armed button, if any, and performs a doClick() on it.
     ***************************************************************************************************/
    public abstract void clickArmedButton();
}
