package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.security.SecurityMessageText;

/********************************************************************************************************
 * User List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 3965544129414099094L;

    private static final String USER_FILTER_SELECTED = "User.filterSelected";
    private static final String USER_SELECTED = "User.selected";

    private UserListModel model = new UserListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable userTable = new SimTable(new UserTableDefinition());
    private SimTablePane userPane = new SimTablePane(userTable);

    private UserFilterDialog filterDialog = new UserFilterDialog();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UserListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, USER_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        userTable.setTableEditable(false);
        userTable.registerDoubleClickAction(this, USER_SELECTED);
    }

    private void layoutPanel() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(userPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return userTable;
    }

    /****************************************************************************************************
     * Start - No search is triggered on user list screen upon entry.
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadAvailableUserTypes();
        filterDialog.setAvailableUserTypes(model.getAvailableUserTypes(), model.isRestrictedUserTypes());
        populateScreen();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(USER_SELECTED)) {
                doUserSelected();
            } else if (command.equals(USER_FILTER_SELECTED)) {
                doUserFilterSelected();
            } else if (command.equals(SimClientStateKey.USER_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doUserSelected() throws Exception {
        model.storeUser((UserWrapper) userTable.getSelectedRowData());
        navigate(SimScreenName.USER_DETAIL_SCREEN);
    }

    private void doUserFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    public void populateScreen() throws Exception {
        filterEditor.setText(model.getFilterDescriptionMap());
        userTable.setRows(model.findUsers());
    }

    public void handleDelete() throws Exception {
        List<UserWrapper> wrappers = userTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        boolean externalUsers = false;
        boolean readOnlyUsers = false;
        List<String> deleteUserNames = new ArrayList<String>();
        for (UserWrapper wrapper : wrappers) {
            if (wrapper.isCached()) {
                externalUsers = true;
            } else if (wrapper.isUserReadOnly()) {
                readOnlyUsers = true;
            } else {
                deleteUserNames.add(wrapper.getUserName());
            }
        }
        if (readOnlyUsers) {
            displayWarning(SecurityMessageText.USER_DELETE_DENIED);
        } else if (externalUsers) {
            displayWarning(SecurityMessageText.EXTERNAL_USER_NOT_DELETED);
        }
        if (deleteUserNames.isEmpty()) {
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", SecurityMessageText.USERS_DELETE_CONFIRM)) {
            try {
                model.deleteUsers(deleteUserNames);
            } catch (Throwable t) {
                displayException(t);
            } finally {
                populateScreen();
            }
        }
    }

    /****************************************************************************************************
     * User Table Definition
     ***************************************************************************************************/

    private class UserTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return UserWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("userName"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Username", "userName"));
            attributes.add(new SimTableAttribute("Name", "name"));
            attributes.add(new SimTableAttribute("Create Date", "createDate"));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
