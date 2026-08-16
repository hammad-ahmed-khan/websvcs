package oracle.retail.sim.client.screen.security;

import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
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
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * This dialog handles entering the filter information for a user search.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1414429635067422700L;

    public static final String STORE_SELECTED = "Store.selected";

    private UserFilterDialogModel model = new UserFilterDialogModel();

    private RTextFieldEditor userNameEditor = new RTextFieldEditor("Username");
    private RTextFieldEditor firstNameEditor = new RTextFieldEditor("First Name");
    private RTextFieldEditor lastNameEditor = new RTextFieldEditor("Last Name");
    private RComboBoxEditor roleEditor = new RComboBoxEditor("Role");
    private RComboBoxEditor typeEditor = new RComboBoxEditor("Type");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RDateFieldEditor createDateEditor = new RDateFieldEditor("Create Date");
    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Start Date");
    private RComboBoxEditor storeEditor = SimEditorFactory.createStoreComboEditor("Store");
    private RComboBoxEditor defaultStoreEditor = new RComboBoxEditor("Default Store");
    private RLongTextFieldEditor commentEditor = new RLongTextFieldEditor("Comments");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public UserFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("User Filter");
        setSize(400, 350);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        userNameEditor.setIdentifier(SimName.USER_USERNAME);
        firstNameEditor.setIdentifier(SimName.USER_FIRST_NAME);
        lastNameEditor.setIdentifier(SimName.USER_LAST_NAME);
        commentEditor.setIdentifier(SimName.USER_COMMENT);

        roleEditor.setDisplayer(new AttributeDisplayer("description"));
        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        defaultStoreEditor.setDisplayer(new BooleanDisplayer());

        storeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        roleEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        defaultStoreEditor.setEmptyType(RComboBoxEmptyType.ALL);

        createDateEditor.setSizeType(EditorConstants.LARGE);
        startDateEditor.setSizeType(EditorConstants.LARGE);

        storeEditor.registerAction(this, STORE_SELECTED);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(11);
        mainPanel.add(userNameEditor);
        mainPanel.add(firstNameEditor);
        mainPanel.add(lastNameEditor);
        mainPanel.add(roleEditor);
        mainPanel.add(typeEditor);
        mainPanel.add(statusEditor);
        mainPanel.add(createDateEditor);
        mainPanel.add(startDateEditor);
        mainPanel.add(storeEditor);
        mainPanel.add(defaultStoreEditor);
        mainPanel.add(commentEditor);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(UserQueryFilter filter) throws Exception {
        List<Role> roles = model.findAvailableRoles();
        roleEditor.setActionsEnabled(false);
        roleEditor.setItems(roles);
        if (filter.getRoleName() != null) {
            roleEditor.setSelectedItem(model.getRoleFromList(roles, filter.getRoleName()));
        } else {
            roleEditor.setEmptySelection();
        }
        roleEditor.setActionsEnabled(true);

        typeEditor.setActionsEnabled(false);
        typeEditor.setItems(model.findAvailableUserTypes());
        if (filter.getTypes().size() == 1) {
            typeEditor.setSelectedItem(filter.getTypes().get(0));
        } else {
            typeEditor.setEmptySelection();
        }
        typeEditor.setActionsEnabled(true);

        statusEditor.setActionsEnabled(false);
        statusEditor.setItems(model.findUserStatuses());
        if (filter.getStatus() != null) {
            statusEditor.setSelectedItem(filter.getStatus());
        } else {
            statusEditor.setEmptySelection();
        }
        statusEditor.setActionsEnabled(true);

        storeEditor.setActionsEnabled(false);
        List<Store> stores = model.getAllowedStores();
        storeEditor.setItems(stores);
        if (filter.getStoreId() != null) {
            storeEditor.setSelectedItem(model.getStoreFromList(stores, filter.getStoreId()));
        }
        storeEditor.setActionsEnabled(true);

        defaultStoreEditor.setActionsEnabled(false);
        defaultStoreEditor.setItems(model.findDefaultStoreValues());
        if (!storeEditor.isEmptySelection()) {
            defaultStoreEditor.setEnabled(true);
            if (filter.isDefaultStore() != null) {
                defaultStoreEditor.setSelectedItem(filter.isDefaultStore());
            } else {
                defaultStoreEditor.setEmptySelection();
            }
        } else {
            defaultStoreEditor.setEnabled(false);
            defaultStoreEditor.setEmptySelection();
        }
        defaultStoreEditor.setActionsEnabled(true);

        userNameEditor.setText(filter.getUserName());
        firstNameEditor.setText(filter.getFirstName());
        lastNameEditor.setText(filter.getLastName());
        createDateEditor.setDate(filter.getCreateDateMin());
        startDateEditor.setDate(filter.getStartDateMin());
        commentEditor.setText(filter.getComments());

        setDefaultButton(applyButton);
    }

    public void setAvailableUserTypes(List<UserType> availableUserTypes, boolean restrictedUserTypes) {
        model.setAvailableUserTypes(availableUserTypes, restrictedUserTypes);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(STORE_SELECTED)) {
                doStoreSelected();
            } else if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
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
     * Reset Action
     ***************************************************************************************************/
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        if (isAllContentEmpty()) {
            throw new BusinessException(CommonMessageText.NO_CRITERIA_ENTERED);
        }

        UserQueryFilter filter = BOFactory.createUserQueryFilter();
        filter.setUserName(userNameEditor.getTextOrNull());
        filter.setFirstName(firstNameEditor.getTextOrNull());
        filter.setLastName(lastNameEditor.getTextOrNull());
        Store store = (Store) storeEditor.getSelectedItem();
        filter.setStoreId(store != null ? store.getId() : null);
        filter.setStatus((UserStatus) statusEditor.getSelectedItem());
        filter.setDefaultStore((Boolean) defaultStoreEditor.getSelectedItem());
        filter.setCreateDateRange(createDateEditor.getDateAtStartOfDay(), createDateEditor.getDateAtEndOfDay());
        filter.setStartDateRange(startDateEditor.getDateAtStartOfDay(), startDateEditor.getDateAtEndOfDay());
        filter.setComments(commentEditor.getTextOrNull());
        Role role = (Role) roleEditor.getSelectedItem();
        filter.setRoleName(role != null ? role.getName() : null);
        model.setFilterUserTypes(filter, (UserType) typeEditor.getSelectedItem());

        RepositoryManager.addStateObject(SimClientStateKey.USER_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.USER_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
