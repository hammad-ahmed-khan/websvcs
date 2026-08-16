package oracle.retail.sim.client.widget;

import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This class subclasses RTab to provide custom SIM functionality for tabs.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTab extends RTab {
    private static final long serialVersionUID = 1725220132759742325L;

    /****************************************************************************************************
     * Returns new SimTab object.
     ***************************************************************************************************/
    public SimTab() {
    }

    /****************************************************************************************************
     * Returns new SimTab object with title assigned. RTab will attempt to access this when an RTab is
     * added to the pane.
     * <p>
     * @param title The title to assign to the RTab.
     ***************************************************************************************************/
    public SimTab(String title) {
        super(title);
    }

    /****************************************************************************************************
     * Displays a warning message in the status bar.
     * <p>
     * @param warning A warning message to translate and display.
     ***************************************************************************************************/
    public void displayWarning(MessageText message) {
        UIStatusUtility.displayWarning(this, message);
    }

    /****************************************************************************************************
     * Displays a warning message in the status bar.
     * <p>
     * @param warning A warning message to translate and display.
     ***************************************************************************************************/
    public void displayWarning(UIException exception) {
        if (exception.isWarning()) {
            UIStatusUtility.displayWarning(this, exception.getPrimaryMessageText());
        }
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Displays an error message in the status bar.
     * <p>
     * @param error An error message to translate and display.
     ***************************************************************************************************/
    public void displayError(MessageText message) {
        UIStatusUtility.displayException(this, message);
    }
}
