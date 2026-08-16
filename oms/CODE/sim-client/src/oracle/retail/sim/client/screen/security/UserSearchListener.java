package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.security.User;

/********************************************************************************************************
 * Generic User Lookup Listener
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class UserSearchListener implements SearchListener {
    public void search() {
        UserLookupDialog dialog = new UserLookupDialog();
        dialog.setSearchListener(this);
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignUser((User) value);
    }

    public abstract void assignUser(User user);
}
