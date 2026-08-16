package oracle.retail.sim.client.swing.core;

import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.common.core.UniversalContext;

/********************************************************************************************************
 * This class is the superclass of all client application launchers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ClientLauncher {
    /****************************************************************************************************
     * Returns new ClientLauncher object. It sets the client-side flag and instantiations a configuration
     * manager and configures the system.
     * <p>
     * @param filename The name of the base configuration filename.
     ***************************************************************************************************/
    public ClientLauncher(String filename) {
        JvmLocation.setClient();
        UniversalContext.setSingleSession(true);
        UniversalContext.startSession("CLIENT_LAUNCHER", DeviceType.PC);
        new ConfigurationManager().configure(filename);
    }

    /**
     * Retrieves the amount of used memory.
     * <p>
     * @param totalMemory The total memory available to the application.
     * @return The amount of memory used by the application.
     */
    protected long getUsedMemory(long totalMemory) {
        return (totalMemory - Runtime.getRuntime().freeMemory()) / 1000000L;
    }

    /****************************************************************************************************
     * Retrieves the current used percentage of memory on the machine.
     * <p>
     * @param totalMemory The total memory available to the application.
     * @return The amount of memory used by the application.
     ***************************************************************************************************/
    protected long getUsedPercent(long totalMemory) {
        try {
            return 100L - 100L / (totalMemory / Runtime.getRuntime().freeMemory());
        } catch (ArithmeticException e) {
            return 100L;
        }
    }
}
