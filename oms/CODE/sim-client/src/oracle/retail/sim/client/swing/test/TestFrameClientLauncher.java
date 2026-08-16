package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.core.ClientLauncher;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;

/********************************************************************************************************
 * This class launches the Test Frame client application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestFrameClientLauncher extends ClientLauncher {

    private static final String BASE_RESOURCE_PATH = "";

    /****************************************************************************************************
     * Returns new TestFrameClientLauncher object
     * <p>
     * @param configurationFilename The name of the file used to configure the application.
     ***************************************************************************************************/
    public TestFrameClientLauncher(String configurationFilename) {
        super(configurationFilename);
    }

    /****************************************************************************************************
     * The main method is called upon application execution. It creates a new TestFrameClientLauncher
     * objects then launch the main screen of the application.
     * <p>
     * @param argv[0] The system configuration filename.
     * @param argv Array of arguments passed in from the command line execution call.
     ***************************************************************************************************/
    public static void main(String[] argv) {
        if (argv.length < 1) {
            System.out.println("Usage: TestFrameClientLauncher configuration-file");
            System.exit(0);
        }
        new TestFrameClientLauncher(BASE_RESOURCE_PATH + argv[0]).launch(argv.length > 1);
    }

    /****************************************************************************************************
     * Launches the main background screen of the application. This creates the main GUI window frame,
     * initializes its properties and then displays the screen on the desktop.
     ***************************************************************************************************/
    public void launch(boolean loginRequired) {
        try {
            TestFrameApplicationFrame mainFrame = new TestFrameApplicationFrame();

            ApplicationInternal.setApplicationFrame(mainFrame);
            ApplicationInternal.setFrame(mainFrame);

            mainFrame.installNavigationSecurity(new TestSecurityManager());
            mainFrame.initialize();
            mainFrame.login(loginRequired);
            mainFrame.setSize(1025, 768);
            mainFrame.setVisible(true);
        } catch (Throwable exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
