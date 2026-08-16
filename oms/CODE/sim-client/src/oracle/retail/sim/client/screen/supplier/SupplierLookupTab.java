package oracle.retail.sim.client.screen.supplier;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierVO;

/********************************************************************************************************
 * Supplier Lookup Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupTab extends SimTab implements REventListener {
    private static final long serialVersionUID = 898873202337713024L;

    private static final String ITEM_MODIFIED = "Item.modified";

    private SupplierLookupTabModel model = new SupplierLookupTabModel();

    private RTextFieldEditor supplierIdEditor = new RTextFieldEditor("Supplier ID");
    private RTextFieldEditor supplierNameEditor = new RTextFieldEditor("Supplier Name");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RCheckBoxEditor itemPacksEditor = new RCheckBoxEditor("Include Item Packs");

    private SimTable supplierTable = new SimTable(new SupplierTableDefinition());
    private SimTablePane supplierPane = new SimTablePane(supplierTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/
    public SupplierLookupTab() {
        setTitle("Lookup");
        setSize(800, 600);
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        supplierIdEditor.setIdentifier(SimName.SUPPLIER_ID);
        supplierNameEditor.setIdentifier(SimName.SUPPLIER_NAME);
        searchLimitEditor.setIdentifier(SimName.SUPPLIER_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);
        itemEditor.registerAction(this, ITEM_MODIFIED);
        itemEditor.setSearchListener(buildItemSearchListener());
        itemPacksEditor.setSizeType(EditorConstants.SMALL);
        itemPacksEditor.setEnabled(false);

        supplierTable.setTableEditable(false);
        supplierTable.setSingleRowSelectionMode();
    }

    private void layoutTab() {
        RPanel panel = new RPanel(new GridBagLayout());
        panel.add(supplierIdEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 5, 0, 0, 0));
        panel.add(supplierNameEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 5, 5, 0, 0));
        panel.add(searchLimitEditor, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 5, 5, 0, 0));
        panel.add(itemEditor, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 1, 5, 0, 0, 0));
        panel.add(itemPacksEditor, GridTool.constraints(2, 1, 1, 1, 1, 0, 0, 1, 5, 5, 0, 0));

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        setLayout(new GridBagLayout());
        add(panel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        add(supplierPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignEditorsInGridBag(panel);
    }

    /****************************************************************************************************
     * Get and Set Dialog Properties
     ***************************************************************************************************/

    protected void registerAction(REventListener listener, String actionCommand) {
        supplierTable.registerSingleClickAction(listener, actionCommand);
    }

    protected boolean isSupplierSelected() {
        return supplierTable.getSelectedRowCount() > 0;
    }

    /****************************************************************************************************
     * RESET
     ***************************************************************************************************/

    protected void doReset() {
        supplierTable.clearRows();
        supplierIdEditor.clear();
        supplierNameEditor.clear();
        searchLimitEditor.setInteger(99);
        supplierIdEditor.requestFocusInWindow();
        itemEditor.clear();
        itemPacksEditor.setSelected(false);
    }

    /****************************************************************************************************
     * SEARCH
     ***************************************************************************************************/

    protected void doSearch() throws Exception {
        SourceQueryFilter filter = BOFactory.createSourceQueryFilter();
        filter.setStoreId(model.getStoreId());
        filter.setSupplierId(supplierIdEditor.getTextOrNull());
        filter.setSupplierName(supplierNameEditor.getTextOrNull());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
            filter.setIncludeItemPacks(itemPacksEditor.isSelected());
        } else {
            filter.setItemId(null);
            filter.setIncludeItemPacks(false);
        }

        supplierTable.setRows(model.findSuppliers(filter));

        if (supplierTable.isEmpty()) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);

            if (supplierNameEditor.isEmpty()) {
                supplierIdEditor.requestFocusInWindow();
            } else {
                supplierNameEditor.requestFocusInWindow();
            }
        }
    }

    /****************************************************************************************************
     * Retrieve Supplier
     ***************************************************************************************************/

    public Supplier getSelectedSupplier() throws Exception {
        SupplierVO supplierVO = (SupplierVO) supplierTable.getSelectedRowData();
        if (supplierVO != null) {
            return model.getSupplier(supplierVO);
        }
        return null;
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
            if (command.equals(ITEM_MODIFIED)) {
                doItemModified();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doItemModified() {
        //Toggle item packs enabled state based on item state
        boolean itemPacksEnabled = itemPacksEditor.isEnabled();
        if (itemEditor.getData() != null ^ itemPacksEnabled) {
            itemPacksEditor.setEnabled(!itemPacksEnabled);
        }
    }

    /****************************************************************************************************
     * SUPPLIER TABLE DEFINITION
     ***************************************************************************************************/

    private class SupplierTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return SupplierVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> columns = new ArrayList<>();
            columns.add(new SimTableAttribute("Supplier ID", "id"));
            columns.add(new SimTableAttribute("Supplier Name", "name"));
            return columns;
        }
    }
}
