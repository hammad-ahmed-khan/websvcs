package oracle.retail.sim.client.core;

import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableDef;

/**
 * Default implementation of the ClientTableFactoryInterface that instantiates tables.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ClientTableFactoryImpl implements ClientTableFactoryInterface {

    public SimTable createCustomUinTable(SimTableDef tableDef) {
        return new SimTable(tableDef);
    }
}
