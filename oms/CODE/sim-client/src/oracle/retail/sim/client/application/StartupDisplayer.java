package oracle.retail.sim.client.application;

import javax.swing.JFrame;

/********************************************************************************************************
 * STARTUP DISPLAYER
 * <p>
 * Controls displaying the two startup windows as the application initializes itself.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StartupDisplayer {

    private SplashScreenDialog splashDialog = new SplashScreenDialog(new JFrame());

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public StartupDisplayer() {
    }

    /****************************************************************************************************
     * Assigns the status message
     ***************************************************************************************************/
    public void setStatus(String message) {
        splashDialog.setOnline(true);
        splashDialog.setStatus(message);
    }

    /****************************************************************************************************
     * Sets the progress target amount
     ***************************************************************************************************/
    public void setProgressTarget(int value) {
        splashDialog.setProgressTarget(value);
    }

    /****************************************************************************************************
     * Sets the current progress value
     ***************************************************************************************************/
    public void setProgress(int value) {
        splashDialog.setProgress(value);
    }

    /****************************************************************************************************
     * Moves the splash dialog to the back of the dialog stack.
     ***************************************************************************************************/
    public void toBack() {
        splashDialog.toBack();
    }

    /****************************************************************************************************
     * Shows the splash screen
     ***************************************************************************************************/
    public void show() {
        splashDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Hides the splash screen
     ***************************************************************************************************/
    public void hide() {
        splashDialog.setVisible(false);
    }
}
