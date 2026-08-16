package oracle.retail.sim.client.application;

/********************************************************************************************************
 * NAVIGATION LISTENER
 * <p>
 * Implemented by any class that wishes to receive a navigation event from the primary navigation area.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface NavigationListener {
    void navigationEvent(NavigationEvent event);
}
