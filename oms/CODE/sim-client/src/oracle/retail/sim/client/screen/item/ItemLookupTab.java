package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
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
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.security.PermissionKey;

/********************************************************************************************************
 * Item Lookup Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemLookupTab extends SimTab implements REventListener {
    private static final long serialVersionUID = 898873202337713024L;

    private ItemLookupTabModel model = new ItemLookupTabModel();

    private static final String RADIO_ITEM = "Item";
    private static final String RADIO_SUPPLIER = "Supplier";
    private static final String RADIO_WAREHOUSE = "Warehouse";
    private static final String RADIO_FINISHER = "Finisher";
    private static final String RADIO_UDA = "UDA";
    private static final String RADIO_INVENTORY = "Inventory";
    private static final String[] RADIO_HEADERS = { RADIO_ITEM, RADIO_SUPPLIER, RADIO_WAREHOUSE, RADIO_FINISHER, RADIO_UDA, RADIO_INVENTORY };

    private RRadioButtonEditor searchTypeEditor = new RRadioButtonEditor();
    private ItemFilterPanel filterLeftPanel = new ItemFilterPanel();

    private RTextFieldEditor itemDescriptionEditor = new RTextFieldEditor("Item Description");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");
    private RCheckBoxEditor nonRangedEditor = new RCheckBoxEditor("Include Non-Ranged");

    private SimTable itemTable = new SimTable(new ItemTableDefinition());
    private SimTablePane itemPane = new SimTablePane(itemTable);

    private SearchListener searchListener;
    private REventListener parentListener;

    private static final String SEARCH_TYPE_MODIFIED = "SearchType.modified";
    private static final String ITEM_SELECTED = "Item.selected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemLookupTab() {
        setTitle("Lookup");
        setSize(800, 600);
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        itemDescriptionEditor.setIdentifier(SimName.ITEM_DESCRIPTION);

        searchLimitEditor.setIdentifier(SimName.ITEM_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setRequired(true);

        nonRangedEditor.setSizeType(EditorConstants.SMALL);

        searchTypeEditor.setRadioButtons(RADIO_HEADERS, 6, 1);
        searchTypeEditor.setRadioTextPosition(EditorConstants.RIGHT);
        searchTypeEditor.setSelected(RADIO_ITEM, true);
        searchTypeEditor.registerAction(this, SEARCH_TYPE_MODIFIED);

        itemTable.setTableEditable(false);
        itemTable.setSingleRowSelectionMode();
        itemTable.registerDoubleClickAction(this, ITEM_SELECTED);
    }

    private void layoutTab() {
        REditorPanel searchPanel = new REditorPanel(1);
        searchPanel.setTitleBorder("Search Type");
        searchPanel.add(searchTypeEditor);

        REditorPanel itemDescPanel = new REditorPanel(1);
        itemDescPanel.add(itemDescriptionEditor);

        REditorPanel itemLimitPanel = new REditorPanel(1, 2);
        itemLimitPanel.add(searchLimitEditor);
        itemLimitPanel.add(nonRangedEditor);

        RPanel filterRightPanel = new RPanel(new GridBagLayout());
        filterRightPanel.add(itemDescPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(hierarchyPanel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(itemLimitPanel, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        filterRightPanel.add(new JLabel(), GridTool.constraints(0, 3, 1, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.add(searchPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
        filterPanel.add(filterLeftPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 0));
        filterPanel.add(filterRightPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));

        setLayout(new GridBagLayout());
        add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        add(itemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        List<REditorPanel> panels = new ArrayList<>();
        panels.add(itemDescPanel);
        panels.add(hierarchyPanel);
        panels.add(itemLimitPanel);

        LayoutUtility.alignPanels(panels);
    }

    /****************************************************************************************************
     * Get and Set Dialog Properties
     ***************************************************************************************************/

    protected void loadDialog() throws Exception {
        hierarchyPanel.loadDepartments();

        filterLeftPanel.populatePanel();

        searchLimitEditor.setInteger(model.getDefaultSearchLimit());

        if (!model.hasPermission(PermissionKey.PC_ACCESS_ITEM_UDA)) {
            searchTypeEditor.removeRadioButton(RADIO_UDA);
        }
        if (!model.hasPermission(PermissionKey.PC_ACCESS_SUPPLIER_LOOKUP)) {
        	searchTypeEditor.removeRadioButton(RADIO_SUPPLIER);
        }
        if (!model.isFinishersEnabled()) {
            searchTypeEditor.removeRadioButton(RADIO_FINISHER);
        }

        if (model.getSupplier() != null) {
            searchTypeEditor.setSelected(RADIO_SUPPLIER, true);
        } else if (model.getWarehouse() != null) {
            searchTypeEditor.setSelected(RADIO_WAREHOUSE, true);
        } else if (model.getFinisher() != null) {
            searchTypeEditor.setSelected(RADIO_FINISHER, true);
        }
    }

    protected void setItemLookupType(ItemLookupType type) {
        model.setItemLookupType(type);
    }

    protected void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    protected void setParentListener(REventListener listener) {
        parentListener = listener;
    }

    protected void registerAction(REventListener listener, String actionCommand) {
        itemTable.registerSingleClickAction(listener, actionCommand);
        setParentListener(listener);
    }

    protected boolean isItemSelected() {
        return itemTable.getSelectedRowCount() > 0;
    }

    protected ItemDetailVO getSelectedDetailItem() throws Exception {
        return model.getDetailItem((ItemVO) itemTable.getSelectedRowData());
    }

    /****************************************************************************************************
     * RESET
     ***************************************************************************************************/

    protected void doReset() {
        itemTable.clearRows();
        itemDescriptionEditor.clear();
        nonRangedEditor.setSelected(false);
        hierarchyPanel.clearSelection();
        filterLeftPanel.clearPanel();
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
    }

    /****************************************************************************************************
     * SEARCH
     ***************************************************************************************************/

    protected void doSearch() throws Exception {
        ItemVOQueryFilter searchFilter = filterLeftPanel.getItemVOQueryFilter();

        if (SimConfigManager.isItemShortDescription()) {
            searchFilter.setItemShortDescription(itemDescriptionEditor.getText());
        } else {
            searchFilter.setItemLongDescription(itemDescriptionEditor.getText());
        }
        searchFilter.setMdseHierarchy(hierarchyPanel.getHierarchyNode());
        searchFilter.setIncludeNonRanged(nonRangedEditor.isSelected());
        searchFilter.setSearchLimit(searchLimitEditor.getIntegerValue());

        itemTable.setRows(model.findItemVOs(searchFilter));

        if (itemTable.getRowCount() < 1) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);
            filterLeftPanel.assignFocus();
        }
    }

    /****************************************************************************************************
     * Retrieve Supplier
     ***************************************************************************************************/

    protected void doApply() throws Exception {
        ItemVO itemVO = (ItemVO) itemTable.getSelectedRowData();
        if (itemVO == null) {
            displayWarning(ItemMessageText.NO_ITEMS_SELECTED);
            return;
        }
        Object object = model.processItemVO(itemVO);
        if (object == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        searchListener.assign(object);
        parentListener.performActionEvent(new RActionEvent(this, SimNavigation.CANCEL));
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SEARCH_TYPE_MODIFIED)) {
                doSearchTypeModified();
            } else if (command.equals(ITEM_SELECTED)) {
                doApply();
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

    /****************************************************************************************************
     * ITEM TABLE DEFINITION
     ***************************************************************************************************/

    private static class ItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            if (SimConfigManager.isItemShortDescription()) {
                return Collections.singletonList(new SimTableSortAttribute("shortDescription"));
            }
            return Collections.singletonList(new SimTableSortAttribute("longDescription"));
        }

        public List getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
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
