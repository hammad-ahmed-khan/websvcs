package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.security.UserPasswordValidationCommand;

/********************************************************************************************************
 * Assign Password Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignPasswordDialogModel extends SimScreenModel {
    private UserDetailWrapper wrapper;

    public UserDetailWrapper getUserDetailWrapper() {
        return wrapper;
    }

    public void setUserDetailWrapper(UserDetailWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void validatePassword(char[] password) throws Exception {
        UserPasswordValidationCommand command = ClientCommandFactory.createUserPasswordValidationCommand();
        command.setPassword(password);
        command.execute();
    }

    public String generatePassword() {
        return BOFactory.createUserPasswordGenerator().generatePassword();
    }
}
