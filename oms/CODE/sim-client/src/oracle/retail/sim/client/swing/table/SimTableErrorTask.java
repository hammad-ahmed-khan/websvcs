package oracle.retail.sim.client.swing.table;

/********************************************************************************************************
 * This is the superclass of all runnable error tasks that can executed when a table cell editor receives
 * an error during a tabel cell event. This runnable class will not have access to an SimSession
 * and thus should never be implemented to make service calls to the server.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimTableErrorTask implements Runnable {

    private SimTableException exception;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    protected SimTableErrorTask() {
    }

    /****************************************************************************************************
     * Assigns the exception that caused this error task to be executed.
     ***************************************************************************************************/
    public void setException(SimTableException exception) {
        this.exception = exception;
    }

    /****************************************************************************************************
     * Implements runnable so that the table can execute this using SwingUtilities. It calls back to the
     * abstract handle exception.
     ***************************************************************************************************/
    public void run() {
        handleException(exception);
    }

    /****************************************************************************************************
     * Abstract method to be defined by all subclasses to handle the exception that took place.
     ***************************************************************************************************/
    protected abstract void handleException(SimTableException exception);
}
