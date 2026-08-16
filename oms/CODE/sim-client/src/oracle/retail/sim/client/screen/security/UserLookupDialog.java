package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * User Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserLookupDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -4949074873542877630L;

    public static final String STORE_SELECTED = "Store.selected";

    private UserLookupDialogModel model = new UserLookupDialogModel();

    private RTextFieldEditor usernameEditor = new RTextFieldEditor("Username");
    private RComboBoxEditor roleEditor = new RComboBoxEditor("Role");
    private RTextFieldEditor firstNameEditor = new RTextFieldEditor("First Name");
    private RTextFieldEditor lastNameEditor = new RTextFieldEditor("Last Name");
    private RComboBoxEditor storeEditor = SimEditorFactory.createStoreComboEditor("Store");
    private RComboBoxEditor defaultStoreEditor = new RComboBoxEditor("Default Store");
    private RDateFieldEditor createDateEditor = new RDateFieldEditor("Create Date");
    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Start Date");
    private RComboBoxEditor typeEditor = new RComboBoxEditor("Type");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RLongTextFieldEditor commentEditor = new RLongTextFieldEditor("Comments");

    private SimTable userTable = new SimTable(new UserTableDefinition());
    private SimTablePane userPane = new SimTablePane(userTable);

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private SearchListener searchListener;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public UserLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("User Lookup");
        setSize(600, 400);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        usernameEditor.setIdentifier(SimName.USER_USERNAME);
        firstNameEditor.setIdentifier(SimName.USER_FIRST_NAME);
        lastNameEditor.setIdentifier(SimName.USER_LAST_NAME);
        commentEditor.setIdentifier(SimName.USER_COMMENT);

        roleEditor.setDisplayer(new AttributeDisplayer("description"));
        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        defaultStoreEditor.setDisplayer(new BooleanDisplayer());

        storeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        roleEditor.setEmptyType(RComboBoxEmptyType.ALL);
        defaultStoreEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);

        createDateEditor.setSizeType(EditorConstants.MEDIUM);
        startDateEditor.setSizeType(EditorConstants.MEDIUM);

        userTable.setTableEditable(false);
        userTable.setSingleRowSelectionMode();
        defaultStoreEditor.setEnabled(false);

        storeEditor.registerAction(this, STORE_SELECTED);
        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        setDefaultButton(searchButton);
    }

    private void layoutContent() {
        addButton(searchButton);
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel topPanel = new REditorPanel(6, 2);
        topPanel.add(usernameEditor);
        topPanel.add(firstNameEditor);
        topPanel.add(storeEditor);
        topPanel.add(createDateEditor);
        topPanel.add(typeEditor);
        topPanel.add(statusEditor);
        topPanel.add(roleEditor);
        topPanel.add(lastNameEditor);
        topPanel.add(defaultStoreEditor);
        topPanel.add(startDateEditor);
        topPanel.add(commentEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(userPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Basic Dialog Property Methods
     ***************************************************************************************************/

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void setMultipleSelectionMode() {
        userTable.setMultipleRowSelectionMode();
    }

    public void setAvailableRoles(List<Role> availableRoles) {
        model.setAvailableRoles(availableRoles);
    }

    public void setAvailableUserTypes(List<UserType> availableUserTypes, boolean restrictedUserTypes) {
        model.setAvailableUserTypes(availableUserTypes, restrictedUserTypes);
    }

    public void setExcludeUserStatus(UserStatus excludeUserStatus) {
        model.setExcludeUserStatus(excludeUserStatus);
    }

    public void loadDialog() {
        try {
            storeEditor.setItems(model.getAllowedStores());
            roleEditor.setItems(model.findAvailableRoles());
            typeEditor.setItems(model.findAvailableUserTypes());
            statusEditor.setItems(model.findUserStatuses());
            defaultStoreEditor.setItems(model.findDefaultStoreValues());
            doReset();
        } catch (Throwable t) {
            displayException(t);
        }
    }
    
    public void stopEditing() {
        userTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(STORE_SELECTED)) {
                doStoreSelected();
            } else if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_SEARCH)) {
                doSearch();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doStoreSelected() {
        if (storeEditor.isEmptySelection()) {
            defaultStoreEditor.setEnabled(false);
            defaultStoreEditor.setEmptySelection();
        } else {
            defaultStoreEditor.setEnabled(true);
        }
    }

    /****************************************************************************************************
     * Reset
     ***************************************************************************************************/
    private void doReset() {
        usernameEditor.clear();
        roleEditor.setEmptySelection();
        firstNameEditor.clear();
        lastNameEditor.clear();
        storeEditor.setEmptySelection();
        defaultStoreEditor.setEmptySelection();
        createDateEditor.clear();
        startDateEditor.clear();
        typeEditor.setEmptySelection();
        statusEditor.setEmptySelection();
        commentEditor.clear();
        userTable.clearRows();

        statusEditor.setSelectedItem(UserStatus.ACTIVE);
        storeEditor.setSelectedItem(model.getStore());
        defaultStoreEditor.setSelectedItem(Boolean.TRUE);
        defaultStoreEditor.setEnabled(true);
    }

    /****************************************************************************************************
     * Search
     ***************************************************************************************************/
    private void doSearch() throws Exception {
        userTable.clearRows();

        UserQueryFilter filter = BOFactory.createUserQueryFilter();
        filter.setUserName(usernameEditor.getTextOrNull());
        filter.setFirstName(firstNameEditor.getTextOrNull());
        filter.setLastName(lastNameEditor.getTextOrNull());
        Store store = (Store) storeEditor.getSelectedItem();
        filter.setStoreId(store != null ? store.getId() : null);
        filter.setStatus((UserStatus) statusEditor.getSelectedItem());
        filter.setCreateDateRange(createDateEditor.getDateAtStartOfDay(), createDateEditor.getDateAtEndOfDay());
        filter.setDefaultStore((Boolean) defaultStoreEditor.getSelectedItem());
        filter.setStartDateRange(startDateEditor.getDateAtStartOfDay(), startDateEditor.getDateAtEndOfDay());
        filter.setComments(commentEditor.getTextOrNull());
        Role role = (Role) roleEditor.getSelectedItem();
        filter.setRoleName(role != null ? role.getName() : null);
        model.setFilterUserTypes(filter, (UserType) typeEditor.getSelectedItem());
        filter.doSetSearchLimit(100);

        userTable.setRows(model.findUsers(filter));
    }

    /****************************************************************************************************
     * Apply
     ***************************************************************************************************/
    private void doApply() throws Exception {
        List<UserWrapper> wrappers = userTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(SecurityMessageText.NO_USERS_SELECTED);
        }
        if (userTable.isMultipleRowSelectionMode()) {
            searchListener.assign(model.getUsers(wrappers));
        } else {
            searchListener.assign(model.getSingleUser(wrappers));
        }
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
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
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute("Username", "userName"));
            attributes.add(new SimTableAttribute("Name", "name"));
            attributes.add(new SimTableAttribute("Type", "type", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
