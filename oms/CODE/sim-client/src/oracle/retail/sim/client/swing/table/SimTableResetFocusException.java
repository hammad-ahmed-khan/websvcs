package oracle.retail.sim.client.swing.table;

import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * Exception designed specifically to be thrown by any SimTableData Reflection calls into a data object.
 * When setABC() is called and fails for some reason that does NOT require displaying an actually
 * exception, this exception should be thrown instead.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableResetFocusException extends UIException {
    private static final long serialVersionUID = 3763938735299289615L;

    public SimTableResetFocusException() {
        super(UIMessageText.TABLE_RESET_FOCUS);
    }
}
