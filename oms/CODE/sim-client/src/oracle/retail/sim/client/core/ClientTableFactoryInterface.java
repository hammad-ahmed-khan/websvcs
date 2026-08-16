package oracle.retail.sim.client.core;

import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableDef;

/**
 * Interface that defines all table instantiations in SIM PC client.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public interface ClientTableFactoryInterface {

    SimTable createCustomUinTable(SimTableDef tableDef);

}
