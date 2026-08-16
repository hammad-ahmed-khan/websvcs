package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserPasswordValidationCommand;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Change Password Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ChangePasswordDialogModel extends SimScreenModel {
    public void validatePassword(char[] password) throws Exception {
        UserPasswordValidationCommand command = ClientCommandFactory.createUserPasswordValidationCommand();
        command.setPassword(password);
        command.execute();
    }

    public void savePassword(char[] oldPassword, char[] newPassword) throws Exception {
        ClientServiceFactory.getSecurityServices().saveUserPassword(getUserName(), oldPassword, newPassword, getStoreId());
        User user = getUser();
        if (user.isChangePassword()) {
            user.setChangePassword(false);
        }
    }
}
