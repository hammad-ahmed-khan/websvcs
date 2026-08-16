package oracle.retail.sim.client.swing.widget;

import java.awt.event.FocusListener;
import oracle.retail.sim.client.swing.util.UIException;

/******************************************************************************************
 * Interface that should be implemented by all Retial component extensions (ie. widgets).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface RetailComponent {
    void setIdentifier(String identifier);

    String getIdentifier();

    void validatePermission(String ownerPrefix) throws UIException;

    void addFocusListener(FocusListener listener);
}
