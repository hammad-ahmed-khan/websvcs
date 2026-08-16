package oracle.retail.sim.client.bootstrap;

import oracle.retail.sim.client.application.ClientApplicationLockSupport;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.VersionManager;
import oracle.retail.sim.common.core.Initializer;
import oracle.retail.sim.common.logging.LogService;

/**
 * This initializer is run first in a SIM client startup process.
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimClientInitializer implements Initializer {
    public void executeInitialization() throws Exception {
        LogService.info(this, "Starting SIM client: " + VersionManager.getVersion(true, true));

        if (ClientApplicationLockSupport.checkClientApplicationAlreadyRunning()) {
            throw new BusinessException(CommonMessageText.CLIENT_ALREADY_RUNNING);
        }

        SimScreenName.applyConfigSettings();

        DisplayerUtility.installDisplayer(DataTypeConstants.CURRENCY, new SimMoneyDisplayer());
        DisplayerUtility.installDisplayer(DataTypeConstants.CURRENCY_LEFT, new SimMoneyDisplayer());
        DisplayerUtility.installDisplayer(DataTypeConstants.CURRENCY_RIGHT, new SimMoneyDisplayer());
    }
}
