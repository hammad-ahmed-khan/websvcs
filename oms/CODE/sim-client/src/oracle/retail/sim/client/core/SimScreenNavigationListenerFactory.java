package oracle.retail.sim.client.core;

import oracle.retail.sim.client.application.Application;

/********************************************************************************************************
 * This factory is responsible for instantiating any listeners to process a navigation event from the
 * button menu prior to SIM receiving the event. If the event is consumed, then SIM will ignore it.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimScreenNavigationListenerFactory {
    public static final String GUI_NAV_LISTENER_FACTORY = "GUI.NAV_LISTENER_FACTORY";

    private static SimScreenNavigationListenerInterface factory = getDefaultFactory();

    private static SimScreenNavigationListenerInterface getDefaultFactory() {
        return Application.getConfigManager().getObject(GUI_NAV_LISTENER_FACTORY, SimScreenNavigationListenerInterface.class);
    }

    private SimScreenNavigationListenerFactory() {
    }

    public static SimScreenNavigationListener getNavigationListener(SimScreen screen) {
        return factory.getNavigationListener(screen);
    }
}
