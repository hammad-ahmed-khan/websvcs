package oracle.retail.sim.client.swing.logging;

/*****************************************************************************************
 * This interface must be implement by all logger objects that wish to receive log requests
 * from the user interface.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface UILogger {

    void debug(String message);

    void info(String message);

    void error(String message);

    void fatal(String message);

    boolean isDebugEnabled();

    boolean isInfoEnabled();
}
