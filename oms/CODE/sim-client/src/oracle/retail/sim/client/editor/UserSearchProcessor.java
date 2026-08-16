package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.editor.SearchProcessor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for users.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UserSearchProcessor implements SearchProcessor {
    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("userName");
    private DualAttributeDisplayer valueDisplayer = new DualAttributeDisplayer("firstName", "lastName", StringConstants.SPACE);

    public Object searchById(String userName) throws Exception {
        User user = ClientServiceFactory.getSecurityServices().readUser(userName);
        if (user == null) {
            throw new BusinessException(CommonMessageText.USER_NOT_FOUND, userName);
        }
        return user;
    }

    public BasicDisplayer getEntryDisplayer() {
        return entryDisplayer;
    }

    public BasicDisplayer getValueDisplayer() {
        return valueDisplayer;
    }

    public Object validateData(Object data) {
        return data;
    }
}
