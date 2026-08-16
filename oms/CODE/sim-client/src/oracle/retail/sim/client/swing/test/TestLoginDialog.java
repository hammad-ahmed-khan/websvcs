package oracle.retail.sim.client.swing.test;

import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RLoginDialog;

/*********************************************************************************************
 * Test Login Dialog Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class TestLoginDialog extends RLoginDialog {
    private static final long serialVersionUID = -2187608557293577672L;

    public TestLoginDialog(JFrame frame) {
        super(frame);
        setTitle("Test Login Dialog");
    }

    protected void doAttemptLogin(String userId, char[] password) {}

    protected void doAttemptCancel() {}
}
