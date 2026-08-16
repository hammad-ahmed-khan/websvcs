package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
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
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.business.BusinessException;

/********************************************************************************************************
 * Data Permissions Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DataPermissionDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1436104473904351320L;

    private static final String DATA_PERM_FILTER_MODIFIED = "DataPermissionFilter.modified";

    private DataPermissionDetailModel model = new DataPermissionDetailModel();

    private RDisplayLabelEditor roleNameEditor = new RDisplayLabelEditor("Role Name");
    private RDisplayLabelEditor roleDescEditor = new RDisplayLabelEditor("Role Description");
    private RDisplayLabelEditor roleTypeEditor = new RDisplayLabelEditor("Role Type");
    private RComboBoxEditor dataPermissionTypeEditor = new RComboBoxEditor("Secured Data Value Types");
    private RTableTransferPanel transferPanel = new RTableTransferPanel("Data Values");

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public DataPermissionDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        roleTypeEditor.setDisplayer(new TranslatedAttributeDisplayer("description"));

        dataPermissionTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        dataPermissionTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        dataPermissionTypeEditor.setSizeType(EditorConstants.LARGE);
        dataPermissionTypeEditor.registerAction(this, DATA_PERM_FILTER_MODIFIED);

        transferPanel.setHorizontal();
        transferPanel.setIncludeAllOptions(true);
        transferPanel.setConfigurationEnabled(false);
        transferPanel.setRowDisplayer(new DataPermissionRowDisplayer());
    }

    private void layoutPanel() {
        REditorPanel headerPanel = new REditorPanel(1, 3);
        headerPanel.add(roleNameEditor);
        headerPanel.add(roleDescEditor);
        headerPanel.add(roleTypeEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        REditorPanel filterPanel = new REditorPanel(1);
        filterPanel.add(dataPermissionTypeEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(filterPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(transferPanel, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

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

        roleNameEditor.setData(roleWrapper.getName());
        roleDescEditor.setData(roleWrapper.getDescription());
        roleTypeEditor.setData(roleWrapper.getType());

        dataPermissionTypeEditor.setActionsEnabled(false);
        dataPermissionTypeEditor.setItems(model.findDataPermissionNames());
        dataPermissionTypeEditor.setActionsEnabled(true);

        List<DataPermissionWrapper> availableDataPermissions = model.findAvailableDataPermissions(null);
        transferPanel.setSelectableItems(availableDataPermissions);
        transferPanel.setSelectedItems(model.findSelectedDataPermissions(availableDataPermissions));
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws BusinessException {
        RoleWrapper roleWrapper = model.getRoleWrapper();
        roleWrapper.removeDataPermissions(transferPanel.getRemainingSelectableItems());
        roleWrapper.storeDataPermissions(transferPanel.getSelectedItems());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DATA_PERM_FILTER_MODIFIED)) {
                doDataPermissionFilterModified();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Handle Permission Filter Modified
     *
     * When the filter changes, we need to preserve the data set. We start by removing everything on the
     * left side of the transfer panel from the role's permission set and then adding everything that is
     * on the right side.
     ***************************************************************************************************/
    private void doDataPermissionFilterModified() throws Exception {
        RoleWrapper roleWrapper = model.getRoleWrapper();
        roleWrapper.removeDataPermissions(transferPanel.getRemainingSelectableItems());
        roleWrapper.storeDataPermissions(transferPanel.getSelectedItems());

        transferPanel.clearSelectableItems();
        transferPanel.clearSelectedItems();

        String dataPermissionName = (String) dataPermissionTypeEditor.getSelectedItem();

        List<DataPermissionWrapper> availableDataPermissions = model.findAvailableDataPermissions(dataPermissionName);
        transferPanel.setSelectableItems(availableDataPermissions);
        transferPanel.setSelectedItems(model.findSelectedDataPermissions(availableDataPermissions));
    }

    /****************************************************************************************************
     * Permission Row Displayer For The Data Permission Transfer Panel
     ***************************************************************************************************/

    private class DataPermissionRowDisplayer implements TableRowDisplayer {
        private String[] headers = { "Type", "Value" };
        private int[] types = { DataTypeConstants.TEXT, DataTypeConstants.TEXT, };
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
            DataPermissionWrapper wrapper = (DataPermissionWrapper) object;
            String[] displayArray = new String[2];
            displayArray[0] = Translator.getText(wrapper.getName());
            displayArray[1] = Translator.getText(wrapper.getValueDescription());
            return displayArray;
        }
    }
}
