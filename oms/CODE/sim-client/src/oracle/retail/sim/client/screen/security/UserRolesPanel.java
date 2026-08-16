package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
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
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * User Roles Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserRolesPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 8956658105524018087L;

    private static final String ADD_BUTTON_ACTION = "AddButton.action";
    private static final String DELETE_BUTTON_ACTION = "DeleteButton.action";

    private UserRolesModel model = new UserRolesModel();

    private RDisplayLabelEditor userNameEditor = new RDisplayLabelEditor("Username");
    private RDisplayLabelEditor userFullNameEditor = new RDisplayLabelEditor("Name");
    private RDisplayLabelEditor userTypeEditor = new RDisplayLabelEditor("Type");
    private RDisplayLabelEditor defaultStoreEditor = new RDisplayLabelEditor("Default Store");

    private RTableTransferPanel storeTransferPanel = new RTableTransferPanel("Available Stores", "Selected Stores");
    private RTableTransferPanel roleTransferPanel = new RTableTransferPanel("Available Roles", "Selected Roles");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("Role End Date");
    private RButton addButton = new RButton("Add");

    private SimTable userRoleTable = new SimTable(new UserRoleTableDefinition());
    private SimTablePane userRoleTablePane = new SimTablePane(userRoleTable);
    private RButton deleteButton = new RButton("Delete");

    private BasicDisplayer storeDisplayer = new IdNameDisplayer();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UserRolesPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        userTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        defaultStoreEditor.setDisplayer(storeDisplayer);
        defaultStoreEditor.setSizeType(EditorConstants.MEDIUM);

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
        addButton.registerAction(this, ADD_BUTTON_ACTION);

        userRoleTable.setTableEditable(false);
        userRoleTablePane.setMinimumHeight(200);

        deleteButton.registerAction(this, DELETE_BUTTON_ACTION);
    }

    private void layoutPanel() {
        REditorPanel userPanel = new REditorPanel(2, 2);
        userPanel.setTitleBorder("User Detail");
        userPanel.add(userNameEditor);
        userPanel.add(userFullNameEditor);
        userPanel.add(userTypeEditor);
        userPanel.add(defaultStoreEditor);

        RPanel selectionActionPanel = new RPanel(new GridBagLayout());
        selectionActionPanel.add(endDateEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        selectionActionPanel.add(addButton, GridTool.constraints(1, 0, 1, 1, 1, 0, 2, 0, 0, 5, 0, 0));

        RPanel selectionPanel = new RPanel(new GridBagLayout());
        selectionPanel.setTitleBorder("New Role Assignment");
        selectionPanel.add(storeTransferPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 5, 5));
        selectionPanel.add(roleTransferPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 5, 5, 0));
        selectionPanel.add(selectionActionPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 5, 5));

        RPanel assignmentsPanel = new RPanel(new GridBagLayout());
        assignmentsPanel.setTitleBorder("Role Assignments");
        assignmentsPanel.add(userRoleTablePane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        assignmentsPanel.add(deleteButton, GridTool.constraints(0, 1, 1, 1, 1, 0, 2, 0, 5, 0, 5, 5));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(userPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(selectionPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(assignmentsPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return userRoleTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();

        userNameEditor.setData(wrapper.getUserName());
        userFullNameEditor.setData(wrapper.getName());
        userTypeEditor.setData(wrapper.getType());
        defaultStoreEditor.setData(wrapper.getDefaultStore());

        if (wrapper.isSuperUser()) {
            storeTransferPanel.setSelectableItems(wrapper.getAvailableStores().keySet());
        } else {
            storeTransferPanel.setSelectableItems(wrapper.getAssignedStoreIds());
        }
        roleTransferPanel.setSelectableItems(wrapper.getAvailableRoles().keySet());

        userRoleTable.setRows(model.getUserRoleWrappers());
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            return;
        }
        List<UserRoleWrapper> userRoleWrappers = userRoleTable.getAllRowData();
        List<UserRole> assignedUserRoles = new ArrayList<UserRole>(userRoleWrappers.size());
        for (UserRoleWrapper userRoleWrapper : userRoleWrappers) {
            assignedUserRoles.add(userRoleWrapper.getUserRole());
        }
        wrapper.assignRoles(assignedUserRoles);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ADD_BUTTON_ACTION)) {
                handleAddAssignment();
            } else if (command.equals(DELETE_BUTTON_ACTION)) {
                handleDeleteAssignment();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void handleAddAssignment() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            throw new BusinessException(SecurityMessageText.DELETED_USER_ACTION_DENIED);
        }
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
        List<UserRoleWrapper> newUserRoleWrappers = new ArrayList<UserRoleWrapper>(selectedStoreIds.size() * selectedRoleNames.size());
        Set<UserRoleWrapper> userRoleWrappers = new HashSet<UserRoleWrapper>(userRoleTable.getAllRowData());
        boolean assignmentsConflict = false;
        String userName = wrapper.getUserName();
        for (Long storeId : selectedStoreIds) {
            Store store = model.getStore(storeId);
            for (String roleName : selectedRoleNames) {
                UserRole userRole = BOFactory.createUserRole();
                userRole.setUserName(userName);
                userRole.setRoleName(roleName);
                userRole.setStoreId(storeId);
                Role role = model.getRole(roleName);
                if (endDate != null) {
                    userRole.setEndDate(endDate);
                } else if (role != null && role.isDateRequired()) {
                    throw new BusinessException(SecurityMessageText.ROLE_END_DATE_REQUIRED);
                }
                UserRoleWrapper userRoleWrapper = ClientWrapperFactory.createUserRoleWrapper(userRole, role, store);
                if (!userRoleWrappers.contains(userRoleWrapper)) {
                    newUserRoleWrappers.add(userRoleWrapper);
                } else if (!assignmentsConflict) {
                    assignmentsConflict = true;
                }
            }
        }
        if (!newUserRoleWrappers.isEmpty()) {
            userRoleTable.addRows(newUserRoleWrappers);
            userRoleTable.sort();
        }
        if (assignmentsConflict) {
            displayMessage(SecurityMessageText.ASSIGNMENT_CONFLICT);
        }
    }

    private void handleDeleteAssignment() throws Exception {
        if (model.getUserDetailWrapper().isDeleted()) {
            throw new BusinessException(SecurityMessageText.DELETED_USER_ACTION_DENIED);
        }
        List<UserRoleWrapper> selectedUserRoleWrappers = userRoleTable.getAllSelectedRowData();
        if (selectedUserRoleWrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_ASSIGNMENTS_SELECTED);
        }
        for (UserRoleWrapper userRoleWrapper : selectedUserRoleWrappers) {
            if (userRoleWrapper.isCached()) {
                throw new BusinessException(SecurityMessageText.EXTERNAL_ASSIGNMENT_NOT_MODIFIABLE);
            }
        }
        for (UserRoleWrapper userRoleWrapper : selectedUserRoleWrappers) {
            userRoleTable.removeRow(userRoleWrapper);
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
     * User Role Table Definition
     ***************************************************************************************************/

    private class UserRoleTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return UserRoleWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute("store"));
            attributes.add(new SimTableSortAttribute("roleDescription"));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Store", "store", new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Role", "roleDescription"));
            attributes.add(new SimTableAttribute("End Date", "endDate"));
            if (!model.isSecurityModeInternal()) {
                attributes.add(new SimTableAttribute("External", "cached"));
            }
            return attributes;
        }
    }
}
