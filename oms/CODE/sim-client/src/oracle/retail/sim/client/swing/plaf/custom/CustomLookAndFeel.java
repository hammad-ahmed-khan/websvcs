package oracle.retail.sim.client.swing.plaf.custom;

import java.util.Map;

/***********************************************************************************************************************
 * This interface must be defined by any class that needs to accept the default settings from a custom theme.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **********************************************************************************************************************/

public interface CustomLookAndFeel {

    String getDescription();

    void setCustomDefaults(Map defaultMap);
}
