package oracle.retail.sim.client.core;

import java.util.Properties;
import oracle.retail.sim.client.application.SimplifiedApplicationFrame;
import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.frame.ApplicationExit;
import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.ClientSecurityAuditUtility;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * SIM Application Frame
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimApplicationFrame extends SimplifiedApplicationFrame {
    private static final long serialVersionUID = -204008226353699112L;

    private final SimSecurityManager securityManager = new SimSecurityManager();

    /****************************************************************************************************
     * Constructor - This is called by the framework using reflection. See the SimConfigFiles.CLIENT_CONFIG file to
     * see where this is specified.
     ***************************************************************************************************/
    public SimApplicationFrame() {
        setSize(1024, 768);
    }

    /****************************************************************************************************
     * Retrieves the security manager associated with the Sim PC Application Frame.
     ***************************************************************************************************/
    public SecurityInterface getSecurityManager() {
        return securityManager;
    }

    /****************************************************************************************************
     * Apply previous settings from found ini file and add window listener that will save properties when
     * window is closed..
     ***************************************************************************************************/
    public void resetWindow() {
        Properties props = SimApplicationConfig.getWindowPropertiesFromCache();
        resetWindowProperties(props == null ? getWindowProperties() : props);
    }

    /****************************************************************************************************
     * Returns whether or not the RFrame is closeable. This method should be overwritten by subclasses if
     * logic is required.
     * <p>
     * @return True if the RFrame is closeable, false otherwise.
     ***************************************************************************************************/
    public boolean isCloseable() {
        if (SimLoginManager.isAutoLogout() || RConfirmUtility.confirm("Exit Application", CommonMessageText.UI_EXIT_CONFIRM)) {
            return true;
        }
        return false;
    }

    public void closeWindow() {
        if (isCloseable()) {
            try {
                clearSession();
            } finally {
                ApplicationExit.exit(this);
            }
        }
    }

    /****************************************************************************************************
     * Clears the session information for SIM. This method cannot be allowed to fail, so simply log any
     * exceptions.
     ***************************************************************************************************/
    public void clearSession() {
        try {
            if (currentScreen != null) {
                currentScreen.stop();
            }
        } catch (Throwable exception) {
            LogService.info(this, exception.getLocalizedMessage());
        }
        try {
            ClientSecurityAuditUtility.auditLogout(SimRepository.getStoreId());
        } catch (Throwable exception) {
            LogService.info(this, exception.getLocalizedMessage());
        }
        try {
            ClientServiceFactory.getActivityLockServices().releaseAllSessionActivityLocks();
        } catch (Throwable exception) {
            LogService.info(this, exception.getLocalizedMessage());
        }
        if (SimRepository.getUser() != null) {
            SimApplicationConfig.cacheWindowProperties(getWindowProperties());
            SimApplicationConfig.cacheTableConfiguration();
        }
    }
}
