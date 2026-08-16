package oracle.retail.sim.client.screen.login;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RLoginDialog;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.util.ArrayUtility;

/********************************************************************************************************
 * SIM Login Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimLoginDialog extends RLoginDialog {
    private static final long serialVersionUID = 8094004301317152809L;

    private String userName;
    private char[] password;
    private boolean cancelled;

    public SimLoginDialog(JFrame frame) {
        super(frame);
        setTitle("SIM Login");
        setWelcomeLabel("Welcome to SIM!");
        setStatusBarVisible(false);
        resetUserNameFocus();
    }

    public void windowOpened(WindowEvent event) {
        resetUserNameFocus();
    }

    protected void doAttemptLogin(String userName, char[] password) {
        try {
            if (StringHelper.isNullOrEmpty(userName)) {
                throw new BusinessException(CommonMessageText.LOGIN_USERNAME_REQUIRED);
            }
            if (ArrayUtility.isNullOrEmpty(password)) {
                throw new BusinessException(CommonMessageText.LOGIN_PASSWORD_ERROR);
            }
            this.userName = userName;
            this.password = password;
            setVisible(false);
        } catch (Exception e) {
            clearState();
            displayException(e);
            resetUserNameFocus();
        }
    }

    public void doAttemptCancel() {
        cancelled = true;
        setVisible(false);
    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void clearState() {
        userName = null;
        password = null;
        cancelled = false;
    }
}
