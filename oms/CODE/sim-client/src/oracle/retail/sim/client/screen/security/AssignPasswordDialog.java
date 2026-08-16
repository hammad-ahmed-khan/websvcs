package oracle.retail.sim.client.screen.security;

import java.util.Arrays;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;

/********************************************************************************************************
 * This dialog handles assigning a password to a user.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignPasswordDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 7751044467778013575L;

    private AssignPasswordDialogModel model = new AssignPasswordDialogModel();

    private RPasswordFieldEditor passwordEditor = new RPasswordFieldEditor("Password");
    private RPasswordFieldEditor retypedEditor = new RPasswordFieldEditor("Retype Password");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton generateButton = new RButton(SimNavigation.DIALOG_AUTO_GENERATE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public AssignPasswordDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Assign Password");
        setSize(350, 180);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        passwordEditor.setIdentifier(SimName.USER_PASSWORD);
        retypedEditor.setIdentifier(SimName.USER_PASSWORD);

        passwordEditor.setRequired(true);
        retypedEditor.setRequired(true);

        Integer maxLength = SimConfigManager.getInteger(SimConfigManager.PASSWORD_MAXIMUM_LENGTH);
        if (maxLength != null && maxLength > 0) {
            passwordEditor.setLength(maxLength);
            retypedEditor.setLength(maxLength);
        }

        passwordEditor.getPasswordField().setEchoChar((char) 0);
        retypedEditor.getPasswordField().setEchoChar((char) 0);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        generateButton.registerAction(this, SimNavigation.DIALOG_AUTO_GENERATE);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        setDefaultButton(applyButton);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(generateButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(2);
        mainPanel.add(passwordEditor);
        mainPanel.add(retypedEditor);

        setContentPane(mainPanel);
    }
    
    /****************************************************************************************************
     * Basic Property Methods
     ***************************************************************************************************/

    public void setUserDetailWrapper(UserDetailWrapper wrapper) {
        model.setUserDetailWrapper(wrapper);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_AUTO_GENERATE)) {
                doAutoGenerate();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        validateRequiredContent();

        char[] currentPassword = passwordEditor.getPassword();
        char[] retypedPassword = retypedEditor.getPassword();

        if (!Arrays.equals(currentPassword, retypedPassword)) {
            passwordEditor.clear();
            retypedEditor.clear();
            throw new BusinessException(CommonMessageText.SECURITY_PASSWORD_MISMATCH);
        }
        model.validatePassword(currentPassword);

        model.getUserDetailWrapper().doSetPassword(currentPassword);

        closeWindow();
    }

    /****************************************************************************************************
     * Auto Generate Password Action
     ***************************************************************************************************/
    private void doAutoGenerate() {
        String password = model.generatePassword();
        passwordEditor.setText(password);
        retypedEditor.setText(password);
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
