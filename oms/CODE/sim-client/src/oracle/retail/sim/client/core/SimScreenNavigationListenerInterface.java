package oracle.retail.sim.client.core;

/********************************************************************************************************
 * Interface that defines the method to instantiate a custom client-side listener that handles navigation
 * events prior to SIM receiving them.
 * @see SimScreenNavigationListenerFactory
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public interface SimScreenNavigationListenerInterface {

    SimScreenNavigationListener getNavigationListener(SimScreen screen);
}
