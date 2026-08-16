package oracle.retail.sim.client.screen.login;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;

/********************************************************************************************************
 * This dialog allows a user to change his/her current password.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ChangePasswordDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 7677825620974477051L;

    private ChangePasswordDialogModel model = new ChangePasswordDialogModel();

    private RPasswordFieldEditor currentPasswordEditor = new RPasswordFieldEditor("Current Password");
    private RPasswordFieldEditor newPasswordEditor = new RPasswordFieldEditor("New Password");
    private RPasswordFieldEditor confirmPasswordEditor = new RPasswordFieldEditor("Confirm Password");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ChangePasswordDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Change Password");
        setSize(350, 150);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        currentPasswordEditor.setIdentifier(SimName.USER_PASSWORD);
        newPasswordEditor.setIdentifier(SimName.USER_PASSWORD);
        confirmPasswordEditor.setIdentifier(SimName.USER_PASSWORD);

        currentPasswordEditor.setRequired(true);
        newPasswordEditor.setRequired(true);
        confirmPasswordEditor.setRequired(true);

        Integer maxLength = SimConfigManager.getInteger(SimConfigManager.PASSWORD_MAXIMUM_LENGTH);
        if (maxLength != null && maxLength > 0) {
            currentPasswordEditor.setLength(maxLength);
            newPasswordEditor.setLength(maxLength);
            confirmPasswordEditor.setLength(maxLength);
        }

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        setDefaultButton(applyButton);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(3);
        mainPanel.add(currentPasswordEditor);
        mainPanel.add(newPasswordEditor);
        mainPanel.add(confirmPasswordEditor);

        setContentPane(mainPanel);
    }

    public void windowOpened(WindowEvent event) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                resetCurrentPasswordFocus();
            }
        });
    }

    private void resetCurrentPasswordFocus() {
        currentPasswordEditor.getPasswordField().requestFocusInWindow();
    }

    public void deactivateCancelOption() {
        cancelButton.setEnabled(false);
        cancelButton.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable e) {
            displayException(e);
            resetCurrentPasswordFocus();
        }
    }

    /****************************************************************************************************
     * Okay Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        validateRequiredContent();

        char[] oldPassword = currentPasswordEditor.getPassword();
        char[] newPassword = newPasswordEditor.getPassword();
        char[] confirmPassword = confirmPasswordEditor.getPassword();
        try {
            if (!Arrays.equals(newPassword, confirmPassword)) {
                throw new UIException(CommonMessageText.SECURITY_PASSWORD_MISMATCH);
            }
            model.validatePassword(newPassword);
        } finally {
            newPasswordEditor.clear();
            confirmPasswordEditor.clear();
        }
        model.savePassword(oldPassword, newPassword);
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
