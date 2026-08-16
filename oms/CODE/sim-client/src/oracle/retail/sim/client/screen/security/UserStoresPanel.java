package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.panel.RTableTransferPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * User Stores Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserStoresPanel extends ScreenPanel implements PropertyChangeListener, REventListener {
    private static final long serialVersionUID = -8082642820397545497L;

    private static final String DEFAULT_STORE_ACTION = "DefaultStore.action";

    private UserStoresModel model = new UserStoresModel();

    private RDisplayLabelEditor userNameEditor = new RDisplayLabelEditor("Username");
    private RDisplayLabelEditor userFullNameEditor = new RDisplayLabelEditor("Name");
    private RDisplayLabelEditor userTypeEditor = new RDisplayLabelEditor("Type");
    private RDisplayLabelEditor defaultStoreEditor = new RDisplayLabelEditor("Default Store");

    private RTableTransferPanel storeTransferPanel = new RTableTransferPanel("Stores");
    private RButton defaultStoreButton = new RButton("Set Default");

    private BasicDisplayer storeDisplayer = new IdNameDisplayer();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UserStoresPanel() {
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
        storeTransferPanel.setSelectableRowDisplayer(new StoreAvailableRowDisplayer());
        storeTransferPanel.setSelectedRowDisplayer(new StoreSelectedRowDisplayer());
        storeTransferPanel.setSelectableColumnSortOrder(new String[] { "Store" });
        storeTransferPanel.setSelectedColumnSortOrder(new String[] { "Store" });
        storeTransferPanel.addPropertyChangeListener(this);

        defaultStoreButton.registerAction(this, DEFAULT_STORE_ACTION);
    }

    private void layoutPanel() {
        REditorPanel userPanel = new REditorPanel(2, 2);
        userPanel.setTitleBorder("User Detail");
        userPanel.add(userNameEditor);
        userPanel.add(userFullNameEditor);
        userPanel.add(userTypeEditor);
        userPanel.add(defaultStoreEditor);

        RPanel assignmentsPanel = new RPanel(new GridBagLayout());
        assignmentsPanel.setTitleBorder("Store Assignments");
        assignmentsPanel.add(storeTransferPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        assignmentsPanel.add(defaultStoreButton, GridTool.constraints(0, 1, 1, 1, 1, 0, 2, 0, 5, 0, 5, 5));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(userPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(assignmentsPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

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
        UserDetailWrapper wrapper = model.getUserDetailWrapper();

        userNameEditor.setData(wrapper.getUserName());
        userFullNameEditor.setData(wrapper.getName());
        userTypeEditor.setData(wrapper.getType());
        defaultStoreEditor.setData(wrapper.getDefaultStore());

        storeTransferPanel.setSelectableItems(wrapper.getAvailableStores().keySet());
        if (wrapper.isSuperUser()) {
            storeTransferPanel.setSelectOptionsEnabled(false);
            storeTransferPanel.setDeselectOptionsEnabled(false);
            storeTransferPanel.setSelectedItems(storeTransferPanel.getAllSelectableItems());
        } else {
            if (wrapper.isDeleted()) {
                storeTransferPanel.setSelectOptionsEnabled(false);
                storeTransferPanel.setDeselectOptionsEnabled(false);
            }
            storeTransferPanel.setSelectedItems(wrapper.getAssignedStoreIds());
        }
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/

    public void handleDone() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            return;
        }
        if (!wrapper.isUserReadOnly()) {
            Store defaultStore = model.getDefaultStore();
            if (defaultStore == null) {
                throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NEEDED);
            }
            wrapper.setDefaultStore(defaultStore);
        }
        if (!wrapper.isSuperUser()) {
            wrapper.assignStores(storeTransferPanel.getSelectedItems());
        }
    }

    /****************************************************************************************************
     * Property Change Listener For Transfer Panel
     ***************************************************************************************************/

    public void propertyChange(PropertyChangeEvent event) {
        String command = event.getPropertyName();
        if (command.equals(UIPropertyName.TABLE_TRANSFER_OCCURRED)) {
            doTransferOccurred();
        }
    }

    private void doTransferOccurred() {
        //Prevent default store and cached assignments from being removed
        List<Long> selectableStoreIds = storeTransferPanel.getRemainingSelectableItems();
        List<Long> selectedStoreIds = storeTransferPanel.getSelectedItems();
        int originalSelectedItemsCount = selectedStoreIds.size();
        for (Long storeId : selectableStoreIds) {
            if (model.isCachedAssignment(storeId) || model.isDefaultStore(storeId)) {
                selectedStoreIds.add(storeId);
            }
        }
        if (selectedStoreIds.size() != originalSelectedItemsCount) {
            storeTransferPanel.setSelectedItems(selectedStoreIds);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DEFAULT_STORE_ACTION)) {
                doAssignDefaultStore();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doAssignDefaultStore() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            throw new BusinessException(SecurityMessageText.DELETED_USER_ACTION_DENIED);
        }
        if (wrapper.isCached()) {
            throw new BusinessException(SecurityMessageText.EXTERNAL_DEFAULT_STORE);
        }
        if (wrapper.isUserReadOnly()) {
            throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NOT_MODIFIABLE);
        }
        List<Long> storeIds = storeTransferPanel.getHighlightedSelectedItems();
        if (storeIds.isEmpty()) {
            throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NEEDED);
        }
        if (storeIds.size() != 1) {
            throw new BusinessException(SecurityMessageText.SINGLE_DEFAULT_STORE);
        }
        Long storeId = storeIds.get(0);
        if (model.isDefaultStore(storeId)) {
            return;
        }
        model.setDefaultStore(storeId);
        storeTransferPanel.setSelectedItems(storeTransferPanel.getSelectedItems());
    }

    /****************************************************************************************************
     * Store Available Row Displayer
     ***************************************************************************************************/

    private class StoreAvailableRowDisplayer implements TableRowDisplayer {
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
     * Store Selected Row Displayer
     ***************************************************************************************************/

    private class StoreSelectedRowDisplayer implements TableRowDisplayer {
        private String[] headers;
        private int[] types;
        private int[] sizes;
        private boolean securityModeInternal;

        private StoreSelectedRowDisplayer() {
            securityModeInternal = model.isSecurityModeInternal();
            if (securityModeInternal) {
                headers = new String[] { "Store", "Default" };
                types = new int[] { DataTypeConstants.TEXT, DataTypeConstants.TEXT };
                sizes = new int[] { -1, -1 };
            } else {
                headers = new String[] { "Store", "Default", "External" };
                types = new int[] { DataTypeConstants.TEXT, DataTypeConstants.TEXT, DataTypeConstants.TEXT };
                sizes = new int[] { -1, -1, -1 };
            }
        }

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
            Long storeId = (Long) object;
            Store store = model.getStore(storeId);
            String[] displayArray;
            if (securityModeInternal) {
                displayArray = new String[2];
                displayArray[0] = store != null ? storeDisplayer.getDisplayText(store) : StringConstants.EMPTY;
                displayArray[1] = model.isDefaultStore(storeId) ? Translator.getText("Yes") : StringConstants.EMPTY;
            } else {
                displayArray = new String[3];
                displayArray[0] = store != null ? storeDisplayer.getDisplayText(store) : StringConstants.EMPTY;
                displayArray[1] = model.isDefaultStore(storeId) ? Translator.getText("Yes") : StringConstants.EMPTY;
                displayArray[2] = model.isCachedAssignment(storeId) ? Translator.getText("Yes") : StringConstants.EMPTY;
            }
            return displayArray;
        }
    }
}
