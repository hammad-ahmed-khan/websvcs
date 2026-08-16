package oracle.retail.sim.client.swing.task;

import javax.swing.SwingUtilities;

/*********************************************************************************************
 * This is a thread that wraps a task and executes the request. If the request is successful,
 * it creates a new action and puts it on the SwingUtilities queue.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

class UITaskAction extends Thread {

    private UITask task;

    /*********************************************************************************************
     * Creates a new task action.
     * <p>
     * @param task The task to execute.
     *********************************************************************************************/
    public UITaskAction(UITask task) {
        this.task = task;
    }

    /*********************************************************************************************
     * Calls executeRequest() and if that is successful, it creates a new swing action and puts
     * it on the Swing queue.
     *********************************************************************************************/
    public void run() {
        if (task.executeRequest()) {
            SwingUtilities.invokeLater(new SwingAction(task));
        }
    }
}
