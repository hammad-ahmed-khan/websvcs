package oracle.retail.sim.client.util;

import oracle.retail.sim.client.application.Application;

/********************************************************************************************************
 * A class to help with toolbar related things.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ToolbarHelper {

    /****************************************************************************************************
     * Invokes a button action if one was supposed to happen. Use this carefully as it may produce
     * undesirable results in some situations. You should usually use this after showing a confirm
     * (yes/no) style dialog, and you want a button press to happen if it was supposed to.
     ***************************************************************************************************/
    public static void reclickArmedButton() {
        Application.getApplicationFrame().getNavigationToolbar().clickArmedButton();
    }
}
