package oracle.retail.sim.client.application;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/******************************************************************************************************
 * The abstract screen class that all screens must inherit from.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************************/

public abstract class Screen extends JPanel implements NavigationListener {

    public abstract void start() throws Throwable;

    public abstract void resume() throws Throwable;

    public abstract void pause();

    public abstract void stop();

    public abstract boolean isStartable();

    public abstract boolean isStoppable();

    public abstract void setInitialized(boolean intitalized);

    public abstract boolean isInitialized();

    public abstract void setPaused(boolean paused);

    public abstract boolean isPaused();

    public abstract String getScreenName();

    public abstract boolean isHomeAllowed();

    public abstract boolean isNavigationValid();

    public abstract void assignFocusInScreen();

    protected Screen() {
        super(new BorderLayout());
    }
}
