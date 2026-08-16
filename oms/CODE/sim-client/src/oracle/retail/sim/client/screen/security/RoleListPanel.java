package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedAttributeDisplayer;
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
 * Role List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7879748435229276409L;

    private static final String ROLE_FILTER_SELECTED = "Role.filterSelected";
    private static final String ROLE_SELECTED = "Role.selected";

    private RoleListModel model = new RoleListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable roleTable = new SimTable(new RoleTableDefinition());
    private SimTablePane rolePane = new SimTablePane(roleTable);

    private RoleFilterDialog filterDialog = new RoleFilterDialog();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public RoleListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, ROLE_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        roleTable.setTableEditable(false);
        roleTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        roleTable.registerDoubleClickAction(this, ROLE_SELECTED);
    }

    private void layoutPanel() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(rolePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return roleTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadAvailableRoleTypes();
        filterDialog.setAvailableRoleTypes(model.getAvailableRoleTypes(), model.isRestrictedRoleTypes());
        populateScreen();
    }

    /****************************************************************************************************
     * Helper method to populate the screen with roles.
     ***************************************************************************************************/

    public void populateScreen() throws Exception {
        filterEditor.setText(model.getFilterDescriptionMap());
        roleTable.setRows(model.findRoles());
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ROLE_SELECTED)) {
                doRoleSelected();
            } else if (command.equals(ROLE_FILTER_SELECTED)) {
                doRoleFilterSelected();
            } else if (command.equals(SimClientStateKey.ROLE_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doRoleSelected() throws Exception {
        model.storeRole((RoleWrapper) roleTable.getSelectedRowData());
        navigate(SimScreenName.ROLE_DETAIL_SCREEN);
    }

    private void doRoleFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    public void handleDelete() throws Exception {
        List<RoleWrapper> wrappers = roleTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        List<String> deleteRoleNames = new ArrayList<String>();
        for (RoleWrapper wrapper : wrappers) {
            if (!model.isAttachedToUser(wrapper)) {
                deleteRoleNames.add(wrapper.getName());
            }
        }
        if (wrappers.size() != deleteRoleNames.size()) {
            displayWarning(SecurityMessageText.ROLE_HAS_USER);
        }
        if (deleteRoleNames.isEmpty()) {
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", SecurityMessageText.ROLES_DELETE_CONFIRM)) {
            try {
                model.deleteRoles(deleteRoleNames);
            } catch (Throwable t) {
                displayException(t);
            } finally {
                populateScreen();
            }
        }
    }

    /****************************************************************************************************
     * Role Table Definition
     ***************************************************************************************************/

    private class RoleTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return RoleWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("name"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Role Name", "name"));
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("Role Type", "type", new TranslatedAttributeDisplayer("description")));
            return attributes;
        }
    }
}
