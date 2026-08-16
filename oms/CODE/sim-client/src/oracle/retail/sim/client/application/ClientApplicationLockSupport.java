package oracle.retail.sim.client.application;

import java.io.IOException;
import java.net.ServerSocket;
import oracle.retail.sim.common.logging.LogService;

/**
 * Helper class for determining whether a client application is already running on this machine or not.
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ClientApplicationLockSupport {
    public static final String CLIENT_LOCK_PORT_KEY = "CLIENT_LOCK_PORT";

    private ClientApplicationLockSupport() {
    }

    /**
     * Checks to see whether a client application is already running on this machine. If one is running,
     * this method returns true. If no application is running, then the client application is
     * "registered" so that subsequent calls to this method will return true.
     */
    public static boolean checkClientApplicationAlreadyRunning() {
        //TODO: implement singleton using javax.jnlp.SingleInstanceService
        // get the port, and do the check:
        return checkPortInUse(Application.getConfigManager().getInteger(CLIENT_LOCK_PORT_KEY, 0));
    }

    /**
     * Checks whether the port is in use.
     */
    public static boolean isPortInUse(int port) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            return false;
        } catch (IOException ioe) {
            return true;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LogService.debug(ClientApplicationLockSupport.class, "ignoring exception");
                }
            }
        }
    }

    /**
     * Checks to see whether the given port is in use or not. If not, the port is then used, so
     * subsequent checks will return true.
     */
    private static boolean checkPortInUse(final int port) {
        if (isPortInUse(port)) {
            return true;
        }

        Runnable r = new Runnable() {
            public void run() {
                ServerSocket socket = null;
                try {
                    socket = new ServerSocket(port);
                    while (true) {
                        try {
                            Thread.currentThread().join();
                        } catch (InterruptedException ie) {
                        }
                    }
                } catch (IOException ioe) {
                    LogService.debug(ClientApplicationLockSupport.class, "ignoring exception");
                    /*
                     * This is strange, since it didn't happen above. This should be extremely rare -
                     * but this could result in multiple app.'s being brought up, which is what we
                     * are trying to prevent here.
                     */
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            LogService.debug(ClientApplicationLockSupport.class, "ignoring exception");
                        }
                    }
                }
            }
        };

        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        return false;
    }
}
