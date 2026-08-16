package oracle.retail.sim.client.swing.navigation;

import java.io.File;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationData;

/******************************************************************************************
 * This interface must be implemented by any class that wants to be responsible for
 * retrieving the NavigationData object used to initialize Navigation.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface NavigationDataBuilder {

    NavigationData getNavigationData() throws UIException;

    NavigationData getNavigationData(File file) throws UIException;
}
