package oracle.retail.sim.client.application;

import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.common.configutil.JndiConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.ArrayUtility;
import oracle.retail.sim.common.util.JndiServiceManager;

/********************************************************************************************************
 * APPLICATION LOGIN REPOSITORY
 * <p>
 * Stores global login information
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public final class ApplicationArguments {
    public static final String DELIMITER = "=";

    private ApplicationArguments() {
    }

    public static void setApplicationArgs(String[] args) {
        if (ArrayUtility.isNullOrEmpty(args)) {
            return;
        }
        for (String arg : args) {
            if (!StringHelper.isNullOrEmpty(arg)) {
                int i = arg.indexOf(DELIMITER);
                if (i > 0 && i < arg.length() - 1) {
                    setApplicationProperty(StringHelper.trimToNull(arg.substring(0, i)), StringHelper.trimToNull(arg.substring(i + 1)));
                }
            }
        }
    }

    public static void setApplicationProperty(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        switch (key) {
            case JndiConfigManager.NAMING_SERVER_URL:
                JndiServiceManager.setNamingServerUrl(value);
                break;
            case SimLoginManager.SSO_CREDENTIAL:
                SimLoginManager.setSsoCredentials(value.toCharArray());
                break;
            case SimLoginManager.SSO_USER:
                SimLoginManager.setSsoUserName(value);
                break;
            default:
                LogService.info(ApplicationArguments.class, "Unknown application property: " + key);
                break;
        }
    }
}
