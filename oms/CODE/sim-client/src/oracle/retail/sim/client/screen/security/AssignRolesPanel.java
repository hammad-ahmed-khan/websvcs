package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.panel.RTableTransferPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Assign Roles Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignRolesPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 6147739997067681037L;

    private static final String ADD_USER_ACTION = "AddUser.action";
    private static final String DELETE_USER_ACTION = "DeleteUser.action";
    private static final String ADD_ROLE_ACTION = "AddRole.action";
    private static final String DELETE_ROLE_ACTION = "DeleteRole.action";
    private static final String DELETE_ASSIGNMENT_ACTION = "DeleteAssignment.action";

    private AssignRolesModel model = new AssignRolesModel();

    private SimTable userTable = new SimTable(new UserTableDefinition());
    private SimTablePane userTablePane = new SimTablePane(userTable);
    private RButton addUserButton = new RButton("Select");
    private RButton deleteUserButton = new RButton("Remove");

    private RTableTransferPanel storeTransferPanel = new RTableTransferPanel("Available Stores", "Selected Stores");
    private RTableTransferPanel roleTransferPanel = new RTableTransferPanel("Available Roles", "Selected Roles");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("Role End Date");
    private RButton addRoleButton = new RButton("Add");
    private RButton deleteRoleButton = new RButton("Delete");

    private SimTable assignmentTable = new SimTable(new AssignmentTableDefinition());
    private SimTablePane assignmentTablePane = new SimTablePane(assignmentTable);
    private RButton deleteAssignmentButton = new RButton("Delete");

    private BasicDisplayer storeDisplayer = new IdNameDisplayer();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public AssignRolesPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        userTable.setTableEditable(false);
        userTablePane.setMinimumHeight(100);

        addUserButton.registerAction(this, ADD_USER_ACTION);
        deleteUserButton.registerAction(this, DELETE_USER_ACTION);

        storeTransferPanel.setHorizontal();
        storeTransferPanel.setIncludeAllOptions(true);
        storeTransferPanel.setConfigurationEnabled(false);
        storeTransferPanel.setRowDisplayer(new StoreRowDisplayer());
        storeTransferPanel.setSelectableColumnSortOrder(new String[] { "Store" });
        storeTransferPanel.setSelectedColumnSortOrder(new String[] { "Store" });

        roleTransferPanel.setHorizontal();
        roleTransferPanel.setIncludeAllOptions(true);
        roleTransferPanel.setConfigurationEnabled(false);
        roleTransferPanel.setRowDisplayer(new RoleRowDisplayer());
        roleTransferPanel.setSelectableColumnSortOrder(new String[] { "Role" });
        roleTransferPanel.setSelectedColumnSortOrder(new String[] { "Role" });

        endDateEditor.setSizeType(EditorConstants.MEDIUM);
        addRoleButton.registerAction(this, ADD_ROLE_ACTION);
        deleteRoleButton.registerAction(this, DELETE_ROLE_ACTION);

        assignmentTable.setTableEditable(false);
        assignmentTablePane.setMinimumHeight(180);

        deleteAssignmentButton.registerAction(this, DELETE_ASSIGNMENT_ACTION);
    }

    private void layoutPanel() {
        RButtonPanel userButtonPanel = new RButtonPanel();
        userButtonPanel.addButton(addUserButton);
        userButtonPanel.addButton(deleteUserButton);

        RPanel userPanel = new RPanel(new GridBagLayout());
        userPanel.setTitleBorder("Selected Users");
        userPanel.add(userTablePane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        userPanel.add(userButtonPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        RButtonPanel selectionButtonPanel = new RButtonPanel();
        selectionButtonPanel.addButton(addRoleButton);
        selectionButtonPanel.addButton(deleteRoleButton);

        RPanel selectionActionPanel = new RPanel(new GridBagLayout());
        selectionActionPanel.add(endDateEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        selectionActionPanel.add(selectionButtonPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        RPanel selectionPanel = new RPanel(new GridBagLayout());
        selectionPanel.setTitleBorder("Change Role Assignment");
        selectionPanel.add(storeTransferPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 5));
        selectionPanel.add(roleTransferPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 5, 0, 0));
        selectionPanel.add(selectionActionPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        RPanel assignmentsPanel = new RPanel(new GridBagLayout());
        assignmentsPanel.setTitleBorder("Role Assignment Changes");
        assignmentsPanel.add(assignmentTablePane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        assignmentsPanel.add(deleteAssignmentButton, GridTool.constraints(0, 1, 1, 1, 1, 0, 2, 0, 5, 0, 5, 5));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(userPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(selectionPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(assignmentsPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return assignmentTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        userTable.setRows(Collections.emptyList());
        assignmentTable.setRows(Collections.emptyList());
        storeTransferPanel.setSelectableItems(model.findAvailableStores().keySet());
        roleTransferPanel.setSelectableItems(model.findAvailableRoles().keySet());
    }

    /****************************************************************************************************
     * Handle Add Users
     ***************************************************************************************************/

    private void handleAddUsers() {
        UserLookupDialog dialog = new UserLookupDialog();
        dialog.setSearchListener(buildUserSearchListener());
        dialog.setMultipleSelectionMode();
        List<UserType> availableUserTypes = model.findAvailableUserTypes();
        dialog.setAvailableUserTypes(availableUserTypes, availableUserTypes.size() != SimEnumUtility.findUserTypes().size());
        dialog.setExcludeUserStatus(UserStatus.DELETE);
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    private SearchListener buildUserSearchListener() {
        return new SearchListener() {
            public void search() {
            }

            public void assign(Object value) {
                List<User> users = (List<User>) value;
                if (users == null || users.isEmpty()) {
                    return;
                }
                doAddUsers(users);
            }
        };
    }

    private void doAddUsers(List<User> users) {
        try {
            List<UserWrapper> newUserWrappers = new ArrayList<UserWrapper>(users.size());
            Set<UserWrapper> userWrappers = new HashSet<UserWrapper>(userTable.getAllRowData());
            Map<Long, Store> availableStores = model.findAvailableStores();
            for (User user : users) {
                UserWrapper userWrapper = ClientWrapperFactory.createUserWrapper(user);
                if (!userWrappers.contains(userWrapper)) {
                    userWrapper.setAvailableStores(availableStores);
                    userWrapper.loadDefaultStore();
                    newUserWrappers.add(userWrapper);
                }
            }
            if (!newUserWrappers.isEmpty()) {
                userTable.addRows(newUserWrappers);
                userTable.sort();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Handle Delete Users
     ***************************************************************************************************/

    private void handleDeleteUsers() throws Exception {
        List<UserWrapper> selectedUserWrappers = userTable.getAllSelectedRowData();
        if (selectedUserWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_USERS_SELECTED);
        }
        for (UserWrapper userWrapper : selectedUserWrappers) {
            userTable.removeRow(userWrapper);
        }
    }

    /****************************************************************************************************
     * Handle Add Role
     ***************************************************************************************************/

    private void handleAddRoles() throws Exception {
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        if (selectedStoreIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_STORES_SELECTED);
        }
        List<String> selectedRoleNames = roleTransferPanel.getSelectedItems();
        if (selectedRoleNames.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ROLES_SELECTED);
        }
        Date endDate = endDateEditor.getDateAtEndOfDay();
        if (endDate != null && SimDateUtil.getCurrentDate().compareTo(endDate) >= 0) {
            throw new BusinessException(SecurityMessageText.ROLE_END_DATE_INVALID);
        }
        List<UserRoleAssignmentWrapper> newAssignmentWrappers = new ArrayList<UserRoleAssignmentWrapper>(selectedStoreIds.size() * selectedRoleNames.size());
        for (Long storeId : selectedStoreIds) {
            Store store = model.getStore(storeId);
            for (String roleName : selectedRoleNames) {
                Role role = model.getRole(roleName);
                if (role == null) {
                    continue;
                }
                if (endDate == null && role.isDateRequired()) {
                    throw new BusinessException(SecurityMessageText.ROLE_END_DATE_REQUIRED);
                }
                newAssignmentWrappers.add(ClientWrapperFactory.createUserRoleAssignmentWrapper(UserRoleAssignmentAction.ADD, role, store, endDate));
            }
        }
        if (newAssignmentWrappers.isEmpty()) {
            return;
        }
        Set<UserRoleAssignmentWrapper> assignmentWrappers = new HashSet<UserRoleAssignmentWrapper>(assignmentTable.getAllRowData());
        assignmentWrappers.removeAll(newAssignmentWrappers);
        assignmentWrappers.addAll(newAssignmentWrappers);
        assignmentTable.setRows(assignmentWrappers);
    }

    /****************************************************************************************************
     * Handle Remove Role
     ***************************************************************************************************/

    private void handleDeleteRoles() throws Exception {
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        if (selectedStoreIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_STORES_SELECTED);
        }
        List<String> selectedRoleNames = roleTransferPanel.getSelectedItems();
        if (selectedRoleNames.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ROLES_SELECTED);
        }
        List<UserRoleAssignmentWrapper> newAssignmentWrappers = new ArrayList<UserRoleAssignmentWrapper>(selectedStoreIds.size() * selectedRoleNames.size());
        for (Long storeId : selectedStoreIds) {
            Store store = model.getStore(storeId);
            for (String roleName : selectedRoleNames) {
                newAssignmentWrappers.add(ClientWrapperFactory.createUserRoleAssignmentWrapper(UserRoleAssignmentAction.DELETE, model.getRole(roleName), store, null));
            }
        }
        if (newAssignmentWrappers.isEmpty()) {
            return;
        }
        Set<UserRoleAssignmentWrapper> assignmentWrappers = new HashSet<UserRoleAssignmentWrapper>(assignmentTable.getAllRowData());
        assignmentWrappers.removeAll(newAssignmentWrappers);
        assignmentWrappers.addAll(newAssignmentWrappers);
        assignmentTable.setRows(assignmentWrappers);
    }

    /****************************************************************************************************
     * Handle Delete Assignment
     ***************************************************************************************************/

    private void handleDeleteAssignment() throws Exception {
        List<UserRoleAssignmentWrapper> selectedAssignmentWrappers = assignmentTable.getAllSelectedRowData();
        if (selectedAssignmentWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ASSIGNMENTS_SELECTED);
        }
        for (UserRoleAssignmentWrapper assignmentWrapper : selectedAssignmentWrappers) {
            assignmentTable.removeRow(assignmentWrapper);
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        List<UserWrapper> userWrappers = userTable.getAllRowData();
        if (userWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_USERS_SELECTED);
        }
        List<UserRoleAssignmentWrapper> assignmentWrappers = assignmentTable.getAllRowData();
        if (assignmentWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ASSIGNMENTS_SELECTED);
        }
        model.saveAssignments(userWrappers, assignmentWrappers);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ADD_USER_ACTION)) {
                handleAddUsers();
            } else if (command.equals(DELETE_USER_ACTION)) {
                handleDeleteUsers();
            } else if (command.equals(ADD_ROLE_ACTION)) {
                handleAddRoles();
            } else if (command.equals(DELETE_ROLE_ACTION)) {
                handleDeleteRoles();
            } else if (command.equals(DELETE_ASSIGNMENT_ACTION)) {
                handleDeleteAssignment();
            }
        } catch (Throwable t) {
            displayException(t);
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
            attributes.add(new SimTableAttribute("Type", "type", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Default Store", "defaultStore", new IdNameDisplayer()));
            return attributes;
        }
    }

    /****************************************************************************************************
     * Store Row Displayer
     ***************************************************************************************************/

    private class StoreRowDisplayer implements TableRowDisplayer {
        private String[] headers = { "Store" };
        private int[] types = { DataTypeConstants.TEXT };
        private int[] sizes = { -1 };

        public String[] getHeaders() {
            return headers;
        }

        public int[] getColumnTypes() {
            return types;
        }

        public int[] getColumnSizes() {
            return sizes;
        }

        public String[] buildRow(Object object) {
            Store store = model.getStore((Long) object);
            return new String[] { store != null ? storeDisplayer.getDisplayText(store) : StringConstants.EMPTY };
        }
    }

    /****************************************************************************************************
     * Role Row Displayer
     ***************************************************************************************************/

    private class RoleRowDisplayer implements TableRowDisplayer {
        private String[] headers = { "Role", "Type" };
        private int[] types = { DataTypeConstants.TEXT, DataTypeConstants.TEXT };
        private int[] sizes = { -1, -1 };

        public String[] getHeaders() {
            return headers;
        }

        public int[] getColumnTypes() {
            return types;
        }

        public int[] getColumnSizes() {
            return sizes;
        }

        public String[] buildRow(Object object) {
            Role role = model.getRole((String) object);
            String[] displayArray = new String[2];
            if (role != null) {
                displayArray[0] = role.isDateRequired() ? "*" + role.getDescription() : role.getDescription();
                displayArray[1] = Translator.getText(role.getType().getDescription());
            } else {
                displayArray[0] = StringConstants.EMPTY;
                displayArray[1] = StringConstants.EMPTY;
            }
            return displayArray;
        }
    }

    /****************************************************************************************************
     * Assignment Table Definition
     ***************************************************************************************************/

    private class AssignmentTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return UserRoleAssignmentWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute("action"));
            attributes.add(new SimTableSortAttribute("store"));
            attributes.add(new SimTableSortAttribute("roleDescription"));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Action", "action", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Store", "store", new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Role", "roleDescription"));
            attributes.add(new SimTableAttribute("End Date", "endDate"));
            return attributes;
        }
    }
}
