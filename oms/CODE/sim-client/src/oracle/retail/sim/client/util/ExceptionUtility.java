package oracle.retail.sim.client.util;

/******************************************************************************************
 * Utility for dealing with exceptions.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class ExceptionUtility {
    private ExceptionUtility() {
    }

    /******************************************************************************************
     * Retrieves the original cause exception of a particular exception. If no cause is find,
     * the parameter exception is returned.
     * <p>
     * @param exception The exception.
     * @return The original exception.
     ******************************************************************************************/
    public static Throwable getOriginalCause(Throwable exception) {
        Throwable lastCause = exception.getCause();
        Throwable cause = lastCause;
        while (cause != null) {
            cause = lastCause.getCause();
            if (cause != null) {
                lastCause = cause;
            }
        }
        if (lastCause != null) {
            return lastCause;
        }
        return exception;
    }
}
