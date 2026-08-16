package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * This dialog handles entering the filter information for product groups.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -595656421584994313L;

    private ProductGroupFilterDialogModel model = new ProductGroupFilterDialogModel();

    private RComboBoxEditor typeEditor = new RComboBoxEditor("Type");
    private RLongTextFieldEditor descriptionEditor = new RLongTextFieldEditor("Description");
    private RComboBoxEditor storeEditor = SimEditorFactory.createStoreComboEditor("Store");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String TYPE_SELECTED = "Type.selected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ProductGroupFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Product Group Filter");
        setSize(450, 320);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        descriptionEditor.setIdentifier(SimName.PRODUCT_GROUP_DESCRIPTION);
        storeEditor.setEmptyType(RComboBoxEmptyType.ALL_STORES);

        hierarchyPanel.setTitleBorder("Hierarchy Filters");
        hierarchyPanel.setEmptyDescriptionToAll();

        itemEditor.setSearchListener(buildItemSearchListener());

        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeEditor.registerAction(this, TYPE_SELECTED);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel basicPanel = new REditorPanel(4);
        basicPanel.setTitleBorder("Additional Filters");
        basicPanel.add(typeEditor);
        basicPanel.add(descriptionEditor);
        basicPanel.add(storeEditor);
        basicPanel.add(itemEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(hierarchyPanel);
        mainPanel.add(basicPanel);

        LayoutUtility.alignPanels(basicPanel, hierarchyPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(ProductGroupQueryFilter filter) throws Exception {
        model.setFilter(filter);

        model.clearAllowedStores();
        typeEditor.clear();
        descriptionEditor.clear();
        storeEditor.clear();
        hierarchyPanel.clearSelection();

        typeEditor.setItems(model.getProductGroupTypes());
        typeEditor.setSelectedItem(filter.getProductGroupType());
        descriptionEditor.setText(filter.getDescription());

        storeEditor.setItems(model.getAllStores());
        storeEditor.setSelectedItem(model.getStore(filter.getStoreId()));

        itemEditor.setText(filter.getItemId());

        hierarchyPanel.loadDepartments();
        hierarchyPanel.setHierarchyNode(filter.getDepartmentId(), filter.getClassId(), filter.getSubclassId());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TYPE_SELECTED)) {
                doTypeSelected();
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
     * Type Selected Action
     ***************************************************************************************************/

    private void doTypeSelected() {
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
        ProductGroupQueryFilter filter = model.getFilter();

        filter.setDescription(descriptionEditor.getTextOrNull());
        filter.setProductGroupType((ProductGroupType) typeEditor.getSelectedItem());
        filter.setGroupId(null);

        Store store = (Store) storeEditor.getSelectedItem();
        if (store != null) {
            filter.setStoreId(store.getId());
        } else {
            filter.setStoreId(null);
        }

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        MdseHierarchyNode node = hierarchyPanel.getHierarchyNode();
        if (node == null) {
            filter.setDepartmentId(null);
            filter.setClassId(null);
            filter.setSubclassId(null);
        } else {
            filter.setDepartmentId(node.getDepartmentId());
            filter.setClassId(node.getClassId());
            filter.setSubclassId(node.getSubclassId());
        }

        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.PRODUCT_GROUP_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
