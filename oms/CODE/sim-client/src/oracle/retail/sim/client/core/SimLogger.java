package oracle.retail.sim.client.core;

import oracle.retail.sim.client.swing.logging.UILogger;
import oracle.retail.sim.common.logging.LogService;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimLogger implements UILogger {

    private Class source;

    public SimLogger(Class source) {
        this.source = source;
    }

    public boolean isDebugEnabled() {
        return LogService.isDebugEnabled(source);
    }

    public boolean isInfoEnabled() {
        return LogService.isInfoEnabled(source);
    }

    public void debug(String message) {
        LogService.debug(source, message);
    }

    public void info(String message) {
        LogService.info(source, message);
    }

    public void error(String message) {
        LogService.error(source, message);
    }

    public void fatal(String message) {
        LogService.fatal(source, message);
    }
}
