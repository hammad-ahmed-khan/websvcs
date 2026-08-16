package oracle.retail.sim.client.swing.logging;

import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.common.business.MessageText;

/*****************************************************************************************
 * Interface defines all the ways in which a status can be displayed. Each application
 * must create its own class that implements this interface in order to handle exception
 * processing. During development, only UIStatusUtility should be used to display
 * exceptions, messages and errors. See that class for further documentation.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface UIStatusDisplayer {

    void displayException(Object object, Throwable exception);

    void displayException(Object object, Throwable exception, MessageText message);

    void displayException(Object object, RErrorEvent event);

    void displayWarning(Object object, MessageText message);
    
    void displayMessage(Object object, MessageText message);

    void displayMessage(Object object, MessageText message, String value);

    void displaySearchMessage(Object object, MessageText message);

    void displayException(RetailEditor editor);

    void clearException(RetailEditor editor);

    void clear();
}
