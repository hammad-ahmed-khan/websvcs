package oracle.retail.sim.client.swing.frame;

import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.core.type.Displayable;

/******************************************************************************************
 * This is the superclass of all task panels used by the application. This abstract class
 * should be generic enough to use inside any area of a SWING application as a panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class RTaskPanel extends RPanel implements Displayable {

    /******************************************************************************************
     * This method is called on all task panels when they are first created.
     *****************************************************************************************/
    public abstract void init();

    /******************************************************************************************
     * This method is called on all task panels when they are placed in the workspace.
     *****************************************************************************************/
    public abstract void start();

    /******************************************************************************************
     * This method is called on all task panels when they are removed from the workspace.
     *****************************************************************************************/
    public abstract void stop();

    /******************************************************************************************
     * This method is called prior to placing the panel in the workspace. If it throws an
     * exception, the panel will NOT be placed in the workspace and the message will be
     * displayed. If the task is NOT startable, but no error occurred and no message needs
     * to be displayed, then the method should return false instead of throwing an exception.
     * <p>
     * @return True if the task is startable, false otherwise.
     * @throws OldUIException If a message should be displayed in the status bar indicating why
     * the task is not startable.
     *****************************************************************************************/
    public abstract boolean isStartable() throws UIException;

    /******************************************************************************************
     * This method is called prior to removing the panel from the workspace. If this throws
     * an exception, the panel will NOT be removed from the workspace and a message will be
     * displayed. If the task is NOT stoppable, but no error occurred and no message needs
     * to be displayed, then the method should return false instead of throwing an exception.
     * <p>
     * @return True if the task is stoppable, false otherwise.
     * @throws OldUIException If a message should be displayed in the status bar indicating why
     * the task is not stoppable.
     *****************************************************************************************/
    public abstract boolean isStoppable() throws UIException;

    /******************************************************************************************
     * Assigns the navigation task item data to the task panel.
     * <p>
     * @param data The navigation task item data to assign.
     *****************************************************************************************/
    protected abstract void setNavigationTaskItemData(NavigationTaskItemData data);

    /******************************************************************************************
     * Retrieves the navigation task item data of the task panel.
     * <p>
     * @return The navigation task item data.
     *****************************************************************************************/
    protected abstract NavigationTaskItemData getNavigationTaskItemData();

    /******************************************************************************************
     * Sets the task resize state.
     * <p>
     * @param isMaximized True if the task panel is maximized, false if it is not.
     *****************************************************************************************/
    protected abstract void setTaskResizeState(boolean isTaskMaximized);

    /******************************************************************************************
     * Protected method called by the application frame to determine if a task is truly
     * startable or not. It calls isStartable() on the subclass when it is done doing its
     * own validation of permissions.
     * <p>
     * @return True if the task is startable, false otherwise.
     *****************************************************************************************/
    protected abstract boolean validateTask() throws UIException;

    /******************************************************************************************
     * If the task is stoppable, this removes the task from the application frame.
     *****************************************************************************************/
    public void closeTask() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /******************************************************************************************
     * This removes the task from the application frame without any validation.
     *****************************************************************************************/
    public void killTask() {
        ApplicationInternal.getApplicationFrame().killTaskPanel();
    }

    /******************************************************************************************
     * This method is called when the application attempts to recover from a fatal error.
     * The default implementation kills the curren task with no cleanup. Override this method
     * to provided custom recovery for specific tasks.
     *****************************************************************************************/
    public abstract void recover();
}
