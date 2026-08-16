package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.panel.RTableTransferPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.SecurityMessageText;

/********************************************************************************************************
 * Role Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -9122695444832013219L;

    private static final String PERM_FILTER_MODIFIED = "PermissionFilter.modified";

    private RoleDetailModel model = new RoleDetailModel();

    private RTextFieldEditor roleNameEditor = new RTextFieldEditor("Role Name");
    private RTextFieldEditor roleDescEditor = new RTextFieldEditor("Role Description");
    private RComboBoxEditor roleTypeEditor = new RComboBoxEditor("Role Type");
    private RCheckBoxEditor dateRequiredEditor = new RCheckBoxEditor("End Date Required");
    private RCheckBoxEditor dataPermissionsEditor = new RCheckBoxEditor("Contains Data Permissions");
    private RComboBoxEditor permissionGroupEditor = new RComboBoxEditor("Topic");
    private RComboBoxEditor deviceTypeEditor = new RComboBoxEditor("Device");

    private RTableTransferPanel transferPanel = new RTableTransferPanel("Permissions");

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public RoleDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        roleNameEditor.setIdentifier(SimName.ROLE_NAME);
        roleDescEditor.setIdentifier(SimName.ROLE_DESCRIPTION);

        roleTypeEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));
        permissionGroupEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));
        deviceTypeEditor.setDisplayer(new TranslatedObjectDisplayer());

        permissionGroupEditor.setEmptyType(RComboBoxEmptyType.ALL);
        deviceTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);

        roleNameEditor.setSizeType(EditorConstants.MEDIUM);
        roleDescEditor.setSizeType(EditorConstants.LARGE);
        permissionGroupEditor.setSizeType(EditorConstants.MEDIUM);
        deviceTypeEditor.setSizeType(EditorConstants.MEDIUM);
        roleTypeEditor.setSizeType(EditorConstants.MEDIUM);

        roleNameEditor.setRequired(true);
        roleDescEditor.setRequired(true);
        roleTypeEditor.setRequired(true);

        roleTypeEditor.setSelectionRequired(true);

        permissionGroupEditor.registerAction(this, PERM_FILTER_MODIFIED);
        deviceTypeEditor.registerAction(this, PERM_FILTER_MODIFIED);

        dataPermissionsEditor.setEnabled(false);

        transferPanel.setHorizontal();
        transferPanel.setIncludeAllOptions(true);
        transferPanel.setConfigurationEnabled(false);
        transferPanel.setRowDisplayer(new PermissionRowDisplayer());

        String[] sortHeaders = new String[] { "Permission" };
        transferPanel.setSelectableColumnSortOrder(sortHeaders);
        transferPanel.setSelectedColumnSortOrder(sortHeaders);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(roleNameEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        headerPanel.add(roleDescEditor, GridTool.constraints(0, 1, 1, 1, 2, 0, 0, 1, 0, 0, 0, 0));
        headerPanel.add(roleTypeEditor, GridTool.constraints(1, 0, 2, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        headerPanel.add(dateRequiredEditor, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        headerPanel.add(dataPermissionsEditor, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

        REditorPanel filterPanel = new REditorPanel(2);
        filterPanel.add(permissionGroupEditor);
        filterPanel.add(deviceTypeEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(filterPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(transferPanel, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        LayoutUtility.alignEditorsInGridBag(headerPanel);

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
        RoleWrapper roleWrapper = model.getRoleWrapper();
        model.loadRoleWrapperPermissionSet();

        setActionsEnabled(false);
        roleTypeEditor.setItems(model.findRoleTypes());
        permissionGroupEditor.setItems(model.findPermissionGroups());
        deviceTypeEditor.setItems(model.findDeviceTypes());
        setActionsEnabled(true);

        roleNameEditor.setText(roleWrapper.getName());
        roleNameEditor.setEnabled(roleWrapper.isNew());
        roleDescEditor.setText(roleWrapper.getDescription());
        roleTypeEditor.setSelectedItem(roleWrapper.getType());
        dateRequiredEditor.setSelected(roleWrapper.isDateRequired());
        dataPermissionsEditor.setSelected(roleWrapper.hasDataPermissions());

        List<Permission> availablePermissions = model.findAvailablePermissions(null, null);
        transferPanel.setSelectableItems(availablePermissions);
        transferPanel.setSelectedItems(model.findSelectedPermissions(availablePermissions));
    }

    public void resume() {
        dataPermissionsEditor.setSelected(model.getRoleWrapper().hasDataPermissions());
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        updateRoleInformation();

        RoleWrapper roleWrapper = model.getRoleWrapper();
        roleWrapper.removePermissions(transferPanel.getRemainingSelectableItems());
        roleWrapper.storePermissions(transferPanel.getSelectedItems());

        if (roleWrapper.isNew() && model.roleNameExists()) {
            throw new BusinessException(SecurityMessageText.ROLE_NAME_EXISTS);
        }

        model.saveRole();
    }

    public void updateRoleInformation() throws Exception {
        validateRequiredContent();

        RoleWrapper roleWrapper = model.getRoleWrapper();

        roleWrapper.setName(roleNameEditor.getTextOrNull());
        roleWrapper.setDescription(roleDescEditor.getTextOrNull());
        roleWrapper.setDateRequired(dateRequiredEditor.isSelected());
        roleWrapper.setType((RoleType) roleTypeEditor.getSelectedItem());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PERM_FILTER_MODIFIED)) {
                doPermissionFilterModified();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Handle Permission Filter Modified
     *
     * When the filter changes, we need to preserve the dataset. We start by removing everything on the
     * left side of the transfer panel from the role's permission set and then adding everything that is
     * on the right side.
     ***************************************************************************************************/
    private void doPermissionFilterModified() throws Exception {
        RoleWrapper roleWrapper = model.getRoleWrapper();

        roleWrapper.removePermissions(transferPanel.getRemainingSelectableItems());
        roleWrapper.storePermissions(transferPanel.getSelectedItems());

        transferPanel.clearSelectableItems();
        transferPanel.clearSelectedItems();

        PermissionGroup permissionGroup = (PermissionGroup) permissionGroupEditor.getSelectedItem();
        DeviceType deviceType = (DeviceType) deviceTypeEditor.getSelectedItem();

        List<Permission> availablePermissions = model.findAvailablePermissions(permissionGroup, deviceType);
        transferPanel.setSelectableItems(availablePermissions);
        transferPanel.setSelectedItems(model.findSelectedPermissions(availablePermissions));
    }

    /****************************************************************************************************
     * Permission Row Displayer
     ***************************************************************************************************/

    private class PermissionRowDisplayer implements TableRowDisplayer {
        private String[] headers = { "Permission", "Topic", "Device" };
        private int[] types = { DataTypeConstants.TEXT, DataTypeConstants.TEXT, DataTypeConstants.TEXT };
        private int[] sizes = { -1, -1, -1 };

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
            Permission permission = (Permission) object;
            String[] displayArray = new String[3];
            displayArray[0] = Translator.getText(permission.getDescription());
            PermissionGroup permissionGroup = permission.getGroup();
            if (permissionGroup != null) {
                displayArray[1] = Translator.getText(permissionGroup.getDescription());
            }
            DeviceType deviceType = permission.getDeviceType();
            if (deviceType != null) {
                displayArray[2] = Translator.getText(deviceType.toString());
            }
            return displayArray;
        }
    }
}
