package oracle.retail.sim.client.core;

import oracle.retail.sim.client.swing.logging.UILogger;
import oracle.retail.sim.client.swing.logging.UILoggerFactory;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimLoggerFactory implements UILoggerFactory {

    public UILogger buildLogger(Class source) {
        return new SimLogger(source);
    }
}
