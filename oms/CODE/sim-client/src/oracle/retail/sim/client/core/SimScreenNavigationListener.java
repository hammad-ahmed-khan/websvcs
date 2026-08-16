package oracle.retail.sim.client.core;

import oracle.retail.sim.client.application.NavigationEvent;

/********************************************************************************************************
 * Interface for any object that wants to listen to button events in the navigation bar at the top
 * of screens.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimScreenNavigationListener {

    public abstract void processEvent(NavigationEvent event);
}
