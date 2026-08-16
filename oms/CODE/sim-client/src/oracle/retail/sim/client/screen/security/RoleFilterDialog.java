package oracle.retail.sim.client.screen.security;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedAttributeDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;

/********************************************************************************************************
 * This dialog handles entering the filter information for roles.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 6502406917963990374L;

    private static final String PERMISSION_GROUP_SELECTED = "PermissionGroup.selected";

    private RoleFilterDialogModel model = new RoleFilterDialogModel();

    private RTextFieldEditor roleNameEditor = new RTextFieldEditor("Role Name");
    private RTextFieldEditor descriptionEditor = new RTextFieldEditor("Description");
    private RComboBoxEditor roleTypeEditor = new RComboBoxEditor("Role Type");
    private RComboBoxEditor permissionGroupEditor = new RComboBoxEditor("Topic");
    private RComboBoxEditor permissionEditor = new RComboBoxEditor("Permission");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public RoleFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Role Filter");
        setSize(400, 225);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        roleNameEditor.setIdentifier(SimName.ROLE_NAME);
        descriptionEditor.setIdentifier(SimName.ROLE_DESCRIPTION);
        permissionEditor.setEnabled(false);

        roleTypeEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));
        permissionGroupEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));
        permissionEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));

        roleTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        permissionGroupEditor.setEmptyType(RComboBoxEmptyType.ALL);
        permissionEditor.setEmptyType(RComboBoxEmptyType.ALL);

        permissionGroupEditor.registerAction(this, PERMISSION_GROUP_SELECTED);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(5);
        mainPanel.add(roleNameEditor);
        mainPanel.add(descriptionEditor);
        mainPanel.add(roleTypeEditor);
        mainPanel.add(permissionGroupEditor);
        mainPanel.add(permissionEditor);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(RoleQueryFilter filter) throws Exception {
        roleNameEditor.setText(filter.getName());
        descriptionEditor.setText(filter.getDescription());

        List<RoleType> roleTypes = model.findAvailableRoleTypes();
        roleTypeEditor.setActionsEnabled(false);
        roleTypeEditor.setItems(roleTypes);
        if (filter.getTypeNames().size() == 1) {
            roleTypeEditor.setSelectedItem(model.getRoleTypeFromList(roleTypes, filter.getTypeNames().get(0)));
        } else {
            roleTypeEditor.setEmptySelection();
        }
        roleTypeEditor.setActionsEnabled(true);

        List<PermissionGroup> permissionGroups = model.findPermissionGroups();
        permissionGroupEditor.setActionsEnabled(false);
        permissionGroupEditor.setItems(permissionGroups);
        if (filter.getPermissionGroupName() != null) {
            permissionGroupEditor.setSelectedItem(model.getPermissionGroupFromList(permissionGroups, filter.getPermissionGroupName()));
        } else {
            permissionGroupEditor.setEmptySelection();
        }
        permissionGroupEditor.setActionsEnabled(true);

        List<Permission> permissions = doPermissionGroupSelected();

        if (filter.getPermissionName() != null && !permissions.isEmpty()) {
            permissionEditor.setSelectedItem(model.getPermissionFromList(permissions, filter.getPermissionName()));
        } else {
            permissionEditor.setEmptySelection();
        }
        setDefaultButton(applyButton);
    }

    public void setAvailableRoleTypes(List<RoleType> availableRoleTypes, boolean restrictedRoleTypes) {
        model.setAvailableRoleTypes(availableRoleTypes, restrictedRoleTypes);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PERMISSION_GROUP_SELECTED)) {
                doPermissionGroupSelected();
            } else if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Permission Group Selected
     ***************************************************************************************************/
    private List<Permission> doPermissionGroupSelected() throws Exception {
        PermissionGroup permissionGroup = (PermissionGroup) permissionGroupEditor.getSelectedItem();
        if (permissionGroup == null) {
            permissionEditor.clear();
            permissionEditor.setEnabled(false);
            return Collections.emptyList();
        }
        List<Permission> permissions = model.findPermissions(permissionGroup);
        if (permissions == null || permissions.isEmpty()) {
            permissionEditor.clear();
            permissionEditor.setEnabled(false);
            return Collections.emptyList();
        }
        permissionEditor.setItems(permissions);
        permissionEditor.setEnabled(true);
        return permissions;
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
        RoleQueryFilter filter = BOFactory.createRoleQueryFilter();
        filter.setName(roleNameEditor.getTextOrNull());
        filter.setDescription(descriptionEditor.getTextOrNull());
        model.setFilterRoleTypeNames(filter, (RoleType) roleTypeEditor.getSelectedItem());
        PermissionGroup permissionGroup = (PermissionGroup) permissionGroupEditor.getSelectedItem();
        filter.setPermissionGroupName(permissionGroup != null ? permissionGroup.getName() : null);
        Permission permission = (Permission) permissionEditor.getSelectedItem();
        filter.setPermissionName(permission != null ? permission.getName() : null);

        RepositoryManager.addStateObject(SimClientStateKey.ROLE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.ROLE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
