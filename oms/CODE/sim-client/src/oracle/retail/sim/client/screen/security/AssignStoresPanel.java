package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
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
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Assign Stores Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignStoresPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4726565395655162225L;

    private static final String ADD_USER_ACTION = "AddUser.action";
    private static final String DELETE_USER_ACTION = "DeleteUser.action";
    private static final String ADD_STORE_ACTION = "AddStore.action";
    private static final String DELETE_STORE_ACTION = "DeleteStore.action";
    private static final String DEFAULT_STORE_ACTION = "DefaultStore.action";
    private static final String DELETE_ASSIGNMENT_ACTION = "DeleteAssignment.action";

    private AssignStoresModel model = new AssignStoresModel();

    private SimTable userTable = new SimTable(new UserTableDefinition());
    private SimTablePane userTablePane = new SimTablePane(userTable);
    private RButton addUserButton = new RButton("Select");
    private RButton deleteUserButton = new RButton("Remove");

    private RTableTransferPanel storeTransferPanel = new RTableTransferPanel("Available Stores", "Selected Stores");
    private RButton defaultStoreButton = new RButton("Set Default");
    private RButton addStoreButton = new RButton("Add");
    private RButton deleteStoreButton = new RButton("Delete");

    private SimTable assignmentTable = new SimTable(new AssignmentTableDefinition());
    private SimTablePane assignmentTablePane = new SimTablePane(assignmentTable);
    private RButton deleteAssignmentButton = new RButton("Delete");

    private BasicDisplayer storeDisplayer = new IdNameDisplayer();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public AssignStoresPanel() {
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

        defaultStoreButton.registerAction(this, DEFAULT_STORE_ACTION);
        addStoreButton.registerAction(this, ADD_STORE_ACTION);
        deleteStoreButton.registerAction(this, DELETE_STORE_ACTION);

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
        selectionButtonPanel.addButton(defaultStoreButton);
        selectionButtonPanel.addButton(addStoreButton);
        selectionButtonPanel.addButton(deleteStoreButton);

        RPanel selectionPanel = new RPanel(new GridBagLayout());
        selectionPanel.setTitleBorder("Change Store Assignment");
        selectionPanel.add(storeTransferPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        selectionPanel.add(selectionButtonPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        RPanel assignmentsPanel = new RPanel(new GridBagLayout());
        assignmentsPanel.setTitleBorder("Store Assignment Changes");
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
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        userTable.setRows(Collections.emptyList());
        assignmentTable.setRows(Collections.emptyList());
        storeTransferPanel.setSelectableItems(model.findAvailableStores().keySet());
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
     * Handle Default Store
     ***************************************************************************************************/

    private void handleDefaultStore() throws Exception {
        if (!model.hasPermission(PermissionKey.PC_EDIT_USER)) {
            throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NOT_MODIFIABLE);
        }
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        if (selectedStoreIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NEEDED);
        }
        if (selectedStoreIds.size() != 1) {
            throw new BusinessException(SecurityMessageText.SINGLE_DEFAULT_STORE);
        }
        Store store = model.getStore(selectedStoreIds.get(0));
        if (store == null) {
            return;
        }
        List<UserStoreAssignmentWrapper> assignmentWrappers = assignmentTable.getAllRowData();
        for (UserStoreAssignmentWrapper assignmentWrapper : assignmentWrappers) {
            if (assignmentWrapper.getAction() == UserStoreAssignmentAction.DEFAULT) {
                if (store.equals(assignmentWrapper.getStore())) {
                    return;
                }
                assignmentTable.updateRow(ClientWrapperFactory.createUserStoreAssignmentWrapper(UserStoreAssignmentAction.ADD, assignmentWrapper.getStore()));
                break;
            }
        }
        UserStoreAssignmentWrapper assignmentWrapper = ClientWrapperFactory.createUserStoreAssignmentWrapper(UserStoreAssignmentAction.DEFAULT, store);
        if (!assignmentTable.updateRow(assignmentWrapper)) {
            assignmentTable.addRow(assignmentWrapper);
        }
        assignmentTable.sort();
    }

    /****************************************************************************************************
     * Handle Add Store
     ***************************************************************************************************/

    private void handleAddStores() throws Exception {
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        if (selectedStoreIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_STORES_SELECTED);
        }
        List<UserStoreAssignmentWrapper> newAssignmentWrappers = new ArrayList<UserStoreAssignmentWrapper>(selectedStoreIds.size());
        for (Long storeId : selectedStoreIds) {
            Store store = model.getStore(storeId);
            if (store != null) {
                newAssignmentWrappers.add(ClientWrapperFactory.createUserStoreAssignmentWrapper(UserStoreAssignmentAction.ADD, store));
            }
        }
        if (newAssignmentWrappers.isEmpty()) {
            return;
        }
        Set<UserStoreAssignmentWrapper> assignmentWrappers = new HashSet<UserStoreAssignmentWrapper>(assignmentTable.getAllRowData());
        assignmentWrappers.removeAll(newAssignmentWrappers);
        assignmentWrappers.addAll(newAssignmentWrappers);
        assignmentTable.setRows(assignmentWrappers);
    }

    /****************************************************************************************************
     * Handle Remove Store
     ***************************************************************************************************/

    private void handleDeleteStores() throws Exception {
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        if (selectedStoreIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_STORES_SELECTED);
        }
        List<UserStoreAssignmentWrapper> newAssignmentWrappers = new ArrayList<UserStoreAssignmentWrapper>(selectedStoreIds.size());
        for (Long storeId : selectedStoreIds) {
            Store store = model.getStore(storeId);
            if (store != null) {
                newAssignmentWrappers.add(ClientWrapperFactory.createUserStoreAssignmentWrapper(UserStoreAssignmentAction.DELETE, store));
            }
        }
        if (newAssignmentWrappers.isEmpty()) {
            return;
        }
        Set<UserStoreAssignmentWrapper> assignmentWrappers = new HashSet<UserStoreAssignmentWrapper>(assignmentTable.getAllRowData());
        assignmentWrappers.removeAll(newAssignmentWrappers);
        assignmentWrappers.addAll(newAssignmentWrappers);
        assignmentTable.setRows(assignmentWrappers);
    }

    /****************************************************************************************************
     * Handle Delete Assignment
     ***************************************************************************************************/

    private void handleDeleteAssignment() throws Exception {
        List<UserStoreAssignmentWrapper> selectedAssignmentWrappers = assignmentTable.getAllSelectedRowData();
        if (selectedAssignmentWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ASSIGNMENTS_SELECTED);
        }
        for (UserStoreAssignmentWrapper assignmentWrapper : selectedAssignmentWrappers) {
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
        List<UserStoreAssignmentWrapper> assignmentWrappers = assignmentTable.getAllRowData();
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
            } else if (command.equals(ADD_STORE_ACTION)) {
                handleAddStores();
            } else if (command.equals(DELETE_STORE_ACTION)) {
                handleDeleteStores();
            } else if (command.equals(DEFAULT_STORE_ACTION)) {
                handleDefaultStore();
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
     * Assignment Table Definition
     ***************************************************************************************************/

    private class AssignmentTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return UserStoreAssignmentWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute("action"));
            attributes.add(new SimTableSortAttribute("store"));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Action", "action", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Store", "store", new IdNameDisplayer()));
            return attributes;
        }
    }
}
