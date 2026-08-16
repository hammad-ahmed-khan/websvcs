package oracle.retail.sim.client.application;

/********************************************************************************************************
 * NAVIGATION EVENT
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NavigationEvent {

    private String command;
    private String screen;
    private boolean isConsumed;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public NavigationEvent() {
    }

    /****************************************************************************************************
     * Sets the command associated with the event
     ***************************************************************************************************/
    public void setCommand(String command) {
        this.command = command;
    }

    /****************************************************************************************************
     * Returns the commandassociated with this event.
     ***************************************************************************************************/
    public String getCommand() {
        return command;
    }

    /****************************************************************************************************
     * Assigns the target screen associated with this event.
     ***************************************************************************************************/
    public void setScreen(String screen) {
        this.screen = screen;
    }

    /****************************************************************************************************
     * Returns the target screen associated with this event.
     ***************************************************************************************************/
    public String getScreen() {
        return screen;
    }

    /****************************************************************************************************
     * Determines whether the event has already been consumed.
     * @return <code>true</code> if the event has been consumed, <code>false</code> otherwise
     ***************************************************************************************************/
    public boolean isConsumed() {
        return isConsumed;
    }

    /****************************************************************************************************
     * Flags the event as having been consumed.
     ***************************************************************************************************/
    public void consume() {
        setConsumed(true);
    }

    /****************************************************************************************************
     * Set the event's consumed flag
     * @param isConsumed <code>true</code> if the event has been consumed, <code>false</code>
     *            otherwise
     ***************************************************************************************************/
    private void setConsumed(boolean isConsumed) {
        this.isConsumed = isConsumed;
    }
}
