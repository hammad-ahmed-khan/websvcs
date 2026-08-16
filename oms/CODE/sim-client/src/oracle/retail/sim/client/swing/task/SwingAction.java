package oracle.retail.sim.client.swing.task;

/*********************************************************************************************
 * This class wraps a task and implements runnable so that it can be placed on the Swing queue.
 * When it is activated, it calls executeResponse() on the task.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

class SwingAction implements Runnable {

    private UITask task;

    /*********************************************************************************************
     * Creates a new swing action with a task.
     * <p>
     * @param task The task to execute.
     *********************************************************************************************/
    public SwingAction(UITask task) {
        this.task = task;
    }

    /*********************************************************************************************
     * Calls executeResponse() on the task.
     *********************************************************************************************/
    public void run() {
        task.executeResponse();
    }
}
