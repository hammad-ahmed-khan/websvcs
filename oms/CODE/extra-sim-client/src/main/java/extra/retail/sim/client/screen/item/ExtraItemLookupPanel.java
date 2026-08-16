package extra.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.security.PermissionKey;

import extra.retail.sim.client.core.ExtraSimScreenName;

/********************************************************************************************************
 * Item Lookup Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraItemLookupPanel extends ScreenPanel implements REventListener {

    private static final long serialVersionUID = -3031044901716067689L;

    private ExtraItemLookupModel model = new ExtraItemLookupModel();

    private static final String RADIO_ITEM = "Item";
    private static final String RADIO_SUPPLIER = "Supplier";
    private static final String RADIO_WAREHOUSE = "Warehouse";
    private static final String RADIO_FINISHER = "Finisher";
    private static final String RADIO_UDA = "UDA";
    private static final String RADIO_INVENTORY = "Inventory";
    private static final String[] RADIO_HEADERS = { RADIO_ITEM, RADIO_SUPPLIER, RADIO_WAREHOUSE, RADIO_FINISHER, RADIO_UDA, RADIO_INVENTORY };

    private RRadioButtonEditor searchTypeEditor = new RRadioButtonEditor();
    private ExtraItemFilterPanel filterLeftPanel = new ExtraItemFilterPanel();

    private RTextFieldEditor itemDescriptionEditor = new RTextFieldEditor("Item Description");
    private RTextFieldEditor brandEditor = new RTextFieldEditor("Brand");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");
    private RCheckBoxEditor nonRangedEditor = new RCheckBoxEditor("Include Non-Ranged");

    private SimTable itemTable = new SimTable(new ItemTableDefinition());
    private SimTablePane itemPane = new SimTablePane(itemTable);

    private static final String SEARCH_TYPE_MODIFIED = "SearchType.modified";
    private static final String ITEM_SELECTED = "Item.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ExtraItemLookupPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        itemDescriptionEditor.setIdentifier(SimName.ITEM_DESCRIPTION);
        brandEditor.setIdentifier(SimName.ITEM_BRAND);

        searchLimitEditor.setIdentifier(SimName.ITEM_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setRequired(true);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());

        nonRangedEditor.setSizeType(EditorConstants.SMALL);

        searchTypeEditor.setRadioButtons(RADIO_HEADERS, 6, 1);
        searchTypeEditor.setRadioTextPosition(EditorConstants.RIGHT);
        searchTypeEditor.setSelected(RADIO_ITEM, true);
        searchTypeEditor.registerAction(this, SEARCH_TYPE_MODIFIED);

        itemTable.setTableEditable(false);
        itemTable.setSingleRowSelectionMode();
        itemTable.registerDoubleClickAction(this, ITEM_SELECTED);
    }

    private void layoutPanel() {
        REditorPanel searchPanel = new REditorPanel(1);
        searchPanel.setTitleBorder("Search Type");
        searchPanel.add(searchTypeEditor);

        REditorPanel itemDescPanel = new REditorPanel(1);
        itemDescPanel.add(itemDescriptionEditor);

        REditorPanel brandPanel = new REditorPanel(1);
        brandPanel.add(brandEditor);

        REditorPanel itemLimitPanel = new REditorPanel(1, 2);
        itemLimitPanel.add(searchLimitEditor);
        itemLimitPanel.add(nonRangedEditor);

        RPanel filterRightPanel = new RPanel(new GridBagLayout());
        filterRightPanel.add(itemDescPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(brandPanel, GridTool.constraints(0, 1, 1, 1, 0, 1, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(hierarchyPanel, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(itemLimitPanel, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.add(searchPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
        filterPanel.add(filterLeftPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 0));
        filterPanel.add(filterRightPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(itemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        List<REditorPanel> panels = new ArrayList<>();
        panels.add(itemDescPanel);
        panels.add(brandPanel);
        panels.add(hierarchyPanel);
        panels.add(itemLimitPanel);

        LayoutUtility.alignPanels(panels);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() {
        try {
            hierarchyPanel.loadDepartments();
            filterLeftPanel.populatePanel();
            searchLimitEditor.setInteger(model.getDefaultSearchLimit());

            validateEnabledState();
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    private void validateEnabledState() {
        if (!model.hasPermission(PermissionKey.PC_ACCESS_ITEM_UDA)) {
            searchTypeEditor.removeRadioButton(RADIO_UDA);
        }
        if (!model.isFinishersEnabled()) {
            searchTypeEditor.removeRadioButton(RADIO_FINISHER);
        }
        if (!model.isSupplierLookUpAvailable()) {
        	searchTypeEditor.removeRadioButton(RADIO_SUPPLIER);
        }
    }

    /****************************************************************************************************
     * Search - If only one item found, take the user to item details screen
     ***************************************************************************************************/

    public void handleSearch() throws Exception {
        // Skip logic if search limit is not pure numeric. Needed for default button processing.
        if (searchLimitEditor.isPositiveInteger()) {
            doItemSearch();
        }
    }

    private void doItemSearch() throws Exception {
        showScreenBusy(true);

        ItemVOQueryFilter searchFilter = filterLeftPanel.getItemVOQueryFilter();

        if (SimConfigManager.isItemShortDescription()) {
            searchFilter.setItemShortDescription(itemDescriptionEditor.getText());
        } else {
            searchFilter.setItemLongDescription(itemDescriptionEditor.getText());
        }
        searchFilter.setBrand(brandEditor.getTextOrNull());
        searchFilter.setMdseHierarchy(hierarchyPanel.getHierarchyNode());
        searchFilter.setIncludeNonRanged(nonRangedEditor.isSelected());
        searchFilter.setSearchLimit(searchLimitEditor.getIntegerValue());

        itemTable.setRows(model.findItemVOs(searchFilter));

        showScreenBusy(false);

        if (itemTable.getRowCount() < 1) {
            displayError(CommonMessageText.NO_RECORDS_FOUND);
            filterLeftPanel.assignFocus();
            return;
        }
        if (itemTable.getRowCount() == 1) {
            doNavigateToDetailScreen((ItemVO) itemTable.getRowData(0));
        }
    }

    /****************************************************************************************************
     * Reset
     ***************************************************************************************************/

    public void handleReset() {
        itemTable.clearRows();
        itemDescriptionEditor.clear();
        brandEditor.clear();
        nonRangedEditor.setSelected(false);
        hierarchyPanel.clearSelection();
        filterLeftPanel.clearPanel();
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        filterLeftPanel.assignFocus();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ITEM_SELECTED)) {
                doItemDetailItemSelected();
            } else if (command.equals(SEARCH_TYPE_MODIFIED)) {
                doSearchTypeModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSearchTypeModified() throws Exception {
        if (searchTypeEditor.isSelected(RADIO_ITEM)) {
            filterLeftPanel.showItemPanel();
        } else if (searchTypeEditor.isSelected(RADIO_SUPPLIER)) {
            filterLeftPanel.showSupplierPanel();
        } else if (searchTypeEditor.isSelected(RADIO_WAREHOUSE)) {
            filterLeftPanel.showWarehousePanel();
        } else if (searchTypeEditor.isSelected(RADIO_FINISHER)) {
            filterLeftPanel.showFinisherPanel();
        } else if (searchTypeEditor.isSelected(RADIO_UDA)) {
            filterLeftPanel.showUDAPanel();
        } else if (searchTypeEditor.isSelected(RADIO_INVENTORY)) {
            filterLeftPanel.showInventoryPanel();
        }
    }

    private void doItemDetailItemSelected() {
        doNavigateToDetailScreen((ItemVO) itemTable.getSelectedRowData());
    }

    private void doNavigateToDetailScreen(ItemVO itemVO) {
        if (itemVO != null) {
            showScreenBusy(true);
            model.storeItemForDetail(itemVO);
            navigate(ExtraSimScreenName.ITEM_DETAIL_SCREEN);
        }
    }
    
    /****************************************************************************************************
     * ITEM TABLE DEFINITION
     ***************************************************************************************************/

    private static class ItemTableDefinition extends SimTableDefinition {

        public Class<ItemVO> getDataClass() {
            return ItemVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            if (SimConfigManager.isItemShortDescription()) {
                return Collections.singletonList(new SimTableSortAttribute("shortDescription"));
            }
            return Collections.singletonList(new SimTableSortAttribute("longDescription"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Item", "id"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Item Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Item Description", "longDescription"));
            }
            attributes.add(new SimTableAttribute("Primary Supplier", "supplierVO.id"));
            attributes.add(new SimTableAttribute("Primary Supplier Name", "supplierVO.name"));
            attributes.add(new SimTableAttribute("Dept.", "departmentName"));
            attributes.add(new SimTableAttribute("Class", "className"));
            attributes.add(new SimTableAttribute("Sub-Class", "subclassName"));
            return attributes;
        }
    }
}
