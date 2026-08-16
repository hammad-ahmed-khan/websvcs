package oracle.retail.sim.client.swing.displaytable;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public interface RTableDefinition {

    Class getTypeClass();

    String[] getHeaders();

    String[] getAttributes();

    int[] getSizes();
}
