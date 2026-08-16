package oracle.retail.sim.client.swing.task;

/*********************************************************************************************
 * Task is a little helper interface for running asynchronous tasks. This is a short fix to
 * be used until a better long-term solution is reached.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public interface UITask {

    /*********************************************************************************************
     * This is executed first by the task. All server related communication should occur in this
     * method. No SWING related activities should be coded here as it will not be put on the
     * Swing event queue and conflicts could arise. This method should return true if it is
     * successful, false if it is not.
     *********************************************************************************************/
    boolean executeRequest();

    /*********************************************************************************************
     * If the executeRequest() is successful, then executeResponse() is executed. All swing related
     * code should be here. This method will be invoked from inside SwingUtilities in order to
     * not cause painting conflicts.
     *********************************************************************************************/
    void executeResponse();
}
