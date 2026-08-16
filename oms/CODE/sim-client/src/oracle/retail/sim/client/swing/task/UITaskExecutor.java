package oracle.retail.sim.client.swing.task;

import java.awt.Component;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.business.MessageText;

/*********************************************************************************************
 * Executes GUI tasks asynchronously. See Task for further details.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class UITaskExecutor {

    /*********************************************************************************************
     * Static constructor.
     *********************************************************************************************/
    private UITaskExecutor() {
    }

    /*********************************************************************************************
     * Executes the task asynchronously (in it's own thread).
     * <p>
     * @param component The component that owns the task.
     * @param task The Task to execute.
     *********************************************************************************************/
    public static void execute(Component component, UITask task) {
        UIStatusUtility.displaySearchMessage(component, UIMessageText.EMPTY_SEARCH_MESSAGE);
        UITaskAction action = new UITaskAction(task);
        action.start();
    }

    /*********************************************************************************************
     * Executes the task asynchronously (in it's own thread).
     * <p>
     * @param component The component that owns the task.
     * @param task The Task to execute.
     * @param message The message to display.
     *********************************************************************************************/
    public static void execute(Component component, UITask task, MessageText message) {
        UIStatusUtility.displaySearchMessage(component, message);
        UITaskAction action = new UITaskAction(task);
        action.start();
    }
}
