package oracle.retail.sim.client.core;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableDef;

/**
 * Utility class to create SIM table objects for the PC client. This class has static methods that delegate to an
 * implementation of ClientTableFactoryInterface. The implementation to be loaded is defined in
 * common.cfg. All creation of new business objects should be handled by this class.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ClientTableFactory {
    public static final String GUI_TABLE_FACTORY = "GUI.TABLE_FACTORY";

    private static ClientTableFactoryInterface factory = getDefaultFactory();

    private static ClientTableFactoryInterface getDefaultFactory() {
        return Application.getConfigManager().getObject(GUI_TABLE_FACTORY, ClientTableFactoryInterface.class);
    }

    private ClientTableFactory() {
    }

    public static SimTable createCustomUinTable(SimTableDef tableDef) {
        return factory.createCustomUinTable(tableDef);
    }
}
