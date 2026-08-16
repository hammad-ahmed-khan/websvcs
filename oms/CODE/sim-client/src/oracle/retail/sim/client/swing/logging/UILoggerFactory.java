package oracle.retail.sim.client.swing.logging;

/*****************************************************************************************
 * This class creates loggers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface UILoggerFactory {

    /*****************************************************************************************
     * Builds a logger for the source class.
     *****************************************************************************************/
    UILogger buildLogger(Class source);
}
