package oracle.retail.sim.client.swing.frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.logging.LogService;

/******************************************************************************************
 * This class times the length of time a message has been displayed on the status bar and
 * clears it after a certain time has passed.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RStatusTimer {

    private RStatusBar statusBar;
    private boolean isActivated;
    private int activeTimerId;
    private int timeToWait = 10000;

    /*****************************************************************************************
     * Creatures a returns a new status timer for the status bar.
     *****************************************************************************************/
    public RStatusTimer(RStatusBar statusBar) {
        this.statusBar = statusBar;
        initializeTimeToWait();
    }

    /*****************************************************************************************
     * Initializes the time to wait.
     *****************************************************************************************/
    private void initializeTimeToWait() {
        String defaultTimeToWait = UIManager.getString(UIThemeName.STATUSBAR_CLEAR_TIME);

        if (defaultTimeToWait != null) {
            try {
                timeToWait = Integer.parseInt(defaultTimeToWait);
            } catch (Throwable exception) {
                // No that this is not internationalized because it is in US locale only properties file.
                LogService.debug(this, "ignoring Excepton");
            }
        }
    }

    /*****************************************************************************************
     * Deactivates the timer.
     *****************************************************************************************/
    public void deactivate() {
        isActivated = false;
    }

    /*****************************************************************************************
     * Activates the timer.
     *****************************************************************************************/
    public void activate() {
        isActivated = true;

        if (activeTimerId == Integer.MAX_VALUE) {
            activeTimerId = 0;
        }

        activeTimerId++;

        new InnerTimer(activeTimerId, timeToWait).start();
    }

    /*****************************************************************************************
     * Clears the status bar.
     * <p>
     * @param timerId The id of the timer that called back.
     *****************************************************************************************/
    private void clear(int timerId) {
        if (isActivated) {
            if (activeTimerId == timerId) {
                SwingUtilities.invokeLater(new ClearStatusBar(statusBar));
                deactivate();
            }
        }
    }

    /*****************************************************************************************
     * INNER CLASS - This class waits a period of time and then calls back to the clear
     * method of the timer.
     *****************************************************************************************/

    private class InnerTimer extends Thread {

        private int timerId = -1;
        private int timePeriod;

        public InnerTimer(int id, int period) {
            timerId = id;
            timePeriod = period;
        }

        public void run() {
            try {
                sleep(timePeriod);
                clear(timerId);
            } catch (Exception exception) {
                deactivate();
            }
        }
    }

    /*****************************************************************************************
     * INNER CLASS - This class clears the status bar in a form that can be used by SwingUtilities.
     *****************************************************************************************/
    private class ClearStatusBar implements Runnable {

        private RStatusBar localStatusBar;

        public ClearStatusBar(RStatusBar statusBar) {
            localStatusBar = statusBar;
        }

        public void run() {
            localStatusBar.clearFromTimer();
        }
    }
}
