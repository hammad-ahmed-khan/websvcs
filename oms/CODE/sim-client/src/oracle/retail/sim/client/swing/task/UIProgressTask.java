package oracle.retail.sim.client.swing.task;

import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This class defines an asynchronous task that can execute in the RProgressFrame window.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class UIProgressTask implements UITask {

    private String title;
    private MessageText permanentMessage;
    private String progressMessage;
    private boolean isErrorState;
    private boolean isActive = true;

    /****************************************************************************************************
     * Retrieves the title of the progress task. This will be displayed in the title area of the progress
     * frame.
     ***************************************************************************************************/
    public String getTitle() {
        return title;
    }

    /****************************************************************************************************
     * Assigns the progress task title.
     ***************************************************************************************************/
    public void setTitle(String title) {
        this.title = title;
    }

    /****************************************************************************************************
     * Retrieves the permanent message of the progress task. This will be displayed in a static area of
     * the progress frame and will not change for the life cycle of the progress.
     ***************************************************************************************************/
    public MessageText getPermanentMessage() {
        return permanentMessage;
    }

    /****************************************************************************************************
     * Assigns the permanent message of the progress task.
     ***************************************************************************************************/
    public void setPermanentMessage(MessageText message) {
        permanentMessage = message;
    }

    /****************************************************************************************************
     * Retrieves the progress message. This will be queried by the frame once at the start of the dialog
     * and again when the task is completed.
     ***************************************************************************************************/
    public synchronized String getProgressMessage() {
        return progressMessage;
    }

    /****************************************************************************************************
     * Assigns the progress message.
     ***************************************************************************************************/
    public synchronized void setProgressMessage(String message) {
        progressMessage = message;
    }

    /****************************************************************************************************
     * Retrieves whether or not the task is in error state or not. If true is set, that means some error
     * took place during the execution of the task.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Assigns the error state of the task.
     ***************************************************************************************************/
    public void setErrorState(boolean isErrorState) {
        this.isErrorState = isErrorState;
    }

    /****************************************************************************************************
     * Default implementation of the UITask method will display finished in the progress frame when the
     * task is finished.
     ***************************************************************************************************/
    public void executeResponse() {
    }

    /****************************************************************************************************
     * Returns true if the task is active, false otherwise.
     ***************************************************************************************************/
    public boolean isActive() {
        return isActive;
    }

    /****************************************************************************************************
     * Deactivates the task.
     ***************************************************************************************************/
    public void deactivate() {
        isActive = false;
    }
}
