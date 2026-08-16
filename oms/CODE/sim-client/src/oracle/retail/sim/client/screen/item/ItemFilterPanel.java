package oracle.retail.sim.client.screen.item;

import java.awt.Component;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.finisher.FinisherSearchListener;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateRangeEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.invadjustment.InventoryStatus;
import oracle.retail.sim.common.item.ItemVOByFinisherQueryFilter;
import oracle.retail.sim.common.item.ItemVOByInventoryQueryFilter;
import oracle.retail.sim.common.item.ItemVOByItemQueryFilter;
import oracle.retail.sim.common.item.ItemVOBySupplierQueryFilter;
import oracle.retail.sim.common.item.ItemVOByUDAQueryFilter;
import oracle.retail.sim.common.item.ItemVOByWarehouseQueryFilter;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAValue;

/********************************************************************************************************
 * Item Filter Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemFilterPanel extends RCardPanel implements REventListener {
    private static final long serialVersionUID = 4484140425652065765L;

    private ItemFilterModel model = new ItemFilterModel();

    private REditorPanel itemSearchPanel = new REditorPanel(2);
    private RTextFieldEditor itemEditor = new RTextFieldEditor("Item");
    private RTextFieldEditor uinEditor = new RTextFieldEditor("UIN");

    private REditorPanel supplierSearchPanel = new REditorPanel(2);
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
    private RCheckBoxEditor primarySupplierEditor = new RCheckBoxEditor("Primary Supplier");

    private REditorPanel warehouseSearchPanel = new REditorPanel(1);
    private RComboBoxEditor warehouseEditor = new RComboBoxEditor("Warehouse");

    private REditorPanel finisherSearchPanel = new REditorPanel(1);
    private RSearchFieldEditor finisherEditor = SimEditorFactory.createFinisherSearchFieldEditor();

    private REditorPanel inventorySearchPanel = new REditorPanel(2);
    private RComboBoxEditor inventoryStatusEditor = new RComboBoxEditor("Inventory Status");
    private RComboBoxEditor nonSellableTypeEditor = new RComboBoxEditor("Sub-bucket");

    private REditorPanel udaSearchPanel = new REditorPanel(6);
    private RComboBoxEditor udaTypeEditor1 = new RComboBoxEditor("UDA");
    private RComboBoxEditor udaTypeEditor2 = new RComboBoxEditor("UDA");
    private RComboBoxEditor udaTypeEditor3 = new RComboBoxEditor("UDA");
    private RTextFieldEditor udaValueEditor1 = new RTextFieldEditor("Text");
    private RComboBoxEditor udaValueEditor2 = new RComboBoxEditor("Value");
    private RDateRangeEditor udaValueEditor3 = new RDateRangeEditor("Date");

    private static final String UDA_TYPE1_SELECTED = "UDAType1.selected";
    private static final String UDA_TYPE2_SELECTED = "UDAType2.selected";
    private static final String UDA_TYPE3_SELECTED = "UDAType3.selected";
    private static final String INVENTORY_STATUS_MODIFIED = "InventoryStatus.modified";

    /********************************************************************************************************
     * Build And Layout Panel
     *******************************************************************************************************/

    public ItemFilterPanel() {
        initFilterPanel();
        layoutFilterPanel();
    }

    private void initFilterPanel() {
        itemEditor.setIdentifier(SimName.ITEM_ID);
        itemEditor.setSizeType(EditorConstants.MEDIUM);
        uinEditor.setIdentifier(SimName.SERIAL_NUMBER);
        uinEditor.setSizeType(EditorConstants.MEDIUM);

        supplierEditor.setSearchListener(buildSupplierSearchListener());
        supplierEditor.setSizeType(EditorConstants.LARGE);

        primarySupplierEditor.setSizeType(EditorConstants.SMALL);
        primarySupplierEditor.setSelected(true);

        warehouseEditor.setDisplayer(new IdNameDisplayer());
        warehouseEditor.setComparator(WarehouseComparator.getInstance());
        warehouseEditor.setSizeType(EditorConstants.LARGE);

        finisherEditor.setSearchListener(buildFinisherSearchListener());
        finisherEditor.setSizeType(EditorConstants.LARGE);

        udaTypeEditor1.setSizeType(EditorConstants.LARGE);
        udaTypeEditor2.setSizeType(EditorConstants.LARGE);
        udaTypeEditor3.setSizeType(EditorConstants.LARGE);

        udaTypeEditor1.setDisplayer(new TranslatedObjectDisplayer());
        udaTypeEditor2.setDisplayer(new TranslatedObjectDisplayer());
        udaTypeEditor3.setDisplayer(new TranslatedObjectDisplayer());
        udaValueEditor2.setDisplayer(new TranslatedObjectDisplayer());

        udaTypeEditor1.registerAction(this, UDA_TYPE1_SELECTED);
        udaTypeEditor2.registerAction(this, UDA_TYPE2_SELECTED);
        udaTypeEditor3.registerAction(this, UDA_TYPE3_SELECTED);

        udaValueEditor1.setIdentifier(SimName.UDA_TEXT);
        udaValueEditor1.setSizeType(EditorConstants.LARGE);
        udaValueEditor2.setSizeType(EditorConstants.LARGE);
        udaValueEditor1.setEnabled(false);
        udaValueEditor2.setEnabled(false);
        udaValueEditor3.setEnabled(false);

        inventoryStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        inventoryStatusEditor.setSizeType(EditorConstants.LARGE);
        inventoryStatusEditor.registerAction(this, INVENTORY_STATUS_MODIFIED);

        nonSellableTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        nonSellableTypeEditor.setSizeType(EditorConstants.LARGE);
        nonSellableTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        nonSellableTypeEditor.setEnabled(false);
        nonSellableTypeEditor.setVisible(model.isNonSellableTypesActive());
    }

    private void layoutFilterPanel() {
        itemSearchPanel.add(itemEditor);
        itemSearchPanel.add(uinEditor);

        supplierSearchPanel.add(supplierEditor);
        supplierSearchPanel.add(primarySupplierEditor);

        warehouseSearchPanel.add(warehouseEditor);

        finisherSearchPanel.add(finisherEditor);

        udaSearchPanel.add(udaTypeEditor1);
        udaSearchPanel.add(udaValueEditor1);
        udaSearchPanel.add(udaTypeEditor2);
        udaSearchPanel.add(udaValueEditor2);
        udaSearchPanel.add(udaTypeEditor3);
        udaSearchPanel.add(udaValueEditor3);

        inventorySearchPanel.add(inventoryStatusEditor);
        inventorySearchPanel.add(nonSellableTypeEditor);

        addCard("Item", itemSearchPanel);
        addCard("Supplier", supplierSearchPanel);
        addCard("Warehouse", warehouseSearchPanel);
        addCard("Finisher", finisherSearchPanel);
        if (model.hasPermission(PermissionKey.PC_ACCESS_ITEM_UDA)) {
            addCard("UDA", udaSearchPanel);
        }
        addCard("InventoryStatus", inventorySearchPanel);
    }

    /********************************************************************************************************
     * Methods To Show The Correct Search Mode
     *******************************************************************************************************/

    protected void showItemPanel() {
        showCard(itemSearchPanel);
    }

    protected void showSupplierPanel() {
        showCard(supplierSearchPanel);
    }

    protected void showWarehousePanel() throws Exception {
        validateWarehousesLoaded();
        showCard(warehouseSearchPanel);
    }

    protected void showFinisherPanel() {
        showCard(finisherSearchPanel);
    }

    protected void showUDAPanel() throws Exception {
        validateUDAsLoaded();
        showCard(udaSearchPanel);
    }

    protected void showInventoryPanel() throws Exception {
        validateInventoryStatusLoaded();
        showCard(inventorySearchPanel);
    }

    private void validateWarehousesLoaded() throws Exception {
        if (warehouseEditor.isEmpty()) {
            warehouseEditor.setItems(model.findAllWarehouses());
        }
    }

    private void validateUDAsLoaded() throws Exception {
        if (udaTypeEditor1.isEmpty() && udaTypeEditor2.isEmpty() && udaTypeEditor3.isEmpty()) {
            udaTypeEditor1.setActionsEnabled(false);
            udaTypeEditor2.setActionsEnabled(false);
            udaTypeEditor3.setActionsEnabled(false);

            udaTypeEditor1.setItems(model.findAllTextUdaTypes());
            udaTypeEditor2.setItems(model.findAllValueUdaTypes());
            udaTypeEditor3.setItems(model.findAllDateUdaTypes());

            udaTypeEditor1.setActionsEnabled(true);
            udaTypeEditor2.setActionsEnabled(true);
            udaTypeEditor3.setActionsEnabled(true);
        }
    }

    private void validateInventoryStatusLoaded() throws Exception {
        if (inventoryStatusEditor.isEmpty()) {
            inventoryStatusEditor.setItems(model.findAllInventoryStatus());
        }
        if (nonSellableTypeEditor.isEmpty()) {
            nonSellableTypeEditor.setItems(model.findAllNonSellableQtyTypes());
        }
    }

    /********************************************************************************************************
     * Methods To Populate And Clear Panel
     *******************************************************************************************************/

    protected void populatePanel() throws Exception {
        boolean isSerialNumbersEnabled = model.isSerialNumberProcessingEnabled();

        uinEditor.setVisible(isSerialNumbersEnabled);
        uinEditor.setEnabled(isSerialNumbersEnabled);

        Supplier supplier = model.getSupplier();
        if (supplier != null) {
            supplierEditor.setText(supplier.getId());
            showSupplierPanel();
            return;
        }

        Warehouse warehouse = model.getWarehouse();
        if (warehouse != null) {
            validateWarehousesLoaded();
            warehouseEditor.setSelectedItem(warehouse);
            showWarehousePanel();
            return;
        }

        Finisher finisher = model.getFinisher();
        if (finisher != null) {
            finisherEditor.setText(finisher.getId());
            showFinisherPanel();
        }
    }

    protected void clearPanel() {
        itemEditor.clear();
        supplierEditor.clear();
        primarySupplierEditor.setSelected(true);
        warehouseEditor.setEmptySelection();
        finisherEditor.clear();
        uinEditor.clear();
        udaTypeEditor1.setEmptySelection();
        udaTypeEditor2.setEmptySelection();
        udaTypeEditor3.setEmptySelection();
        udaValueEditor1.clear();
        udaValueEditor2.setEmptySelection();
        udaValueEditor3.setStartDate(null);
        udaValueEditor3.setEndDate(null);
    }

    protected void assignFocus() {
        Component card = getCard();
        if (card == itemSearchPanel) {
            itemEditor.requestFocusInWindow();
        } else if (card == supplierSearchPanel) {
            supplierEditor.requestFocusInWindow();
        } else if (card == warehouseSearchPanel) {
            warehouseEditor.requestFocusInWindow();
        } else if (card == finisherSearchPanel) {
            finisherEditor.requestFocusInWindow();
        } else if (card == udaSearchPanel) {
            udaTypeEditor1.requestFocusInWindow();
        }
    }

    /********************************************************************************************************
     * Build And Return Correct Search Query Filter
     *******************************************************************************************************/

    protected ItemVOQueryFilter getItemVOQueryFilter() throws Exception {
        Component card = getCard();
        if (card == supplierSearchPanel) {
            return getItemVOBySupplierQueryFilter();
        }
        if (card == warehouseSearchPanel) {
            return getItemVOByWarehouseQueryFilter();
        }
        if (card == finisherSearchPanel) {
            return getItemVOByFinisherQueryFilter();
        }
        if (card == udaSearchPanel) {
            return getItemVOByUDAQueryFilter();
        }
        if (card == inventorySearchPanel) {
            return getItemVOByInventoryQueryFilter();
        }
        return getItemVOByItemQueryFilter();
    }

    private ItemVOQueryFilter getItemVOBySupplierQueryFilter() throws BusinessException {
        ItemVOBySupplierQueryFilter filter = BOFactory.createItemVOBySupplierQueryFilter();
        Supplier supplier = (Supplier) supplierEditor.getData();
        if (supplier != null) {
            filter.setSupplierId(supplier.getId());
        }
        filter.setPrimarySupplier(primarySupplierEditor.isSelected());
        return filter;
    }

    private ItemVOQueryFilter getItemVOByWarehouseQueryFilter() throws BusinessException {
        ItemVOByWarehouseQueryFilter filter = BOFactory.createItemVOByWarehouseQueryFilter();
        Warehouse warehouse = (Warehouse) warehouseEditor.getSelectedItem();
        if (warehouse != null) {
            filter.setWarehouseId(warehouse.getId());
        }
        return filter;
    }

    private ItemVOQueryFilter getItemVOByFinisherQueryFilter() throws Exception {
        ItemVOByFinisherQueryFilter filter = BOFactory.createItemVOByFinisherQueryFilter();
        Finisher finisher = (Finisher) finisherEditor.getData();
        if (finisher != null) {
            filter.setFinisherId(finisher.getId());
        }
        return filter;
    }

    private ItemVOQueryFilter getItemVOByUDAQueryFilter() throws Exception {
        UDADetail udaDetail1 = (UDADetail) udaTypeEditor1.getSelectedItem();
        UDADetail udaDetail2 = (UDADetail) udaTypeEditor2.getSelectedItem();
        UDADetail udaDetail3 = (UDADetail) udaTypeEditor3.getSelectedItem();

        ItemVOByUDAQueryFilter filter = BOFactory.createItemVOByUDAQueryFilter();
        if (udaDetail1 != null) {
            filter.setUdaId1(udaDetail1.getId());
            filter.setUdaText(udaValueEditor1.getTextOrNull());
        }
        if (udaDetail2 != null) {
            filter.setUdaId2(udaDetail2.getId());

            UDAValue udaValue = (UDAValue) udaValueEditor2.getSelectedItem();
            if (udaValue != null) {
                filter.setUdaValue(udaValue.getValueId());
            }
        }
        if (udaDetail3 != null) {
            filter.setUdaId3(udaDetail3.getId());
            filter.setUdaStartDate(udaValueEditor3.getStartDate());
            filter.setUdaEndDate(udaValueEditor3.getEndDate());
        }
        return filter;
    }

    private ItemVOQueryFilter getItemVOByInventoryQueryFilter() throws Exception {
        ItemVOByInventoryQueryFilter filter = BOFactory.createItemVOByInventoryQueryFilter();
        filter.setInventoryStatus((InventoryStatus) inventoryStatusEditor.getSelectedItem());
        filter.setNonSellableQtyType((NonSellableQtyType) nonSellableTypeEditor.getSelectedItem());
        return filter;
    }

    private ItemVOQueryFilter getItemVOByItemQueryFilter() throws Exception {
        ItemVOByItemQueryFilter filter = BOFactory.createItemVOByItemQueryFilter();
        filter.setItemId(itemEditor.getTextOrNull());
        filter.setSerialNumber(uinEditor.getTextOrNull());
        return filter;
    }

    /********************************************************************************************************
     * Search Listeners For Supplier And Finisher
     *******************************************************************************************************/

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                if (supplier != null) {
                    supplierEditor.setData(supplier);
                }
            }
        };
    }

    private FinisherSearchListener buildFinisherSearchListener() {
        return new FinisherSearchListener() {
            public void assignFinisher(Finisher finisher) {
                if (finisher != null) {
                    finisherEditor.setData(finisher);
                }
            }
        };
    }

    /********************************************************************************************************
     * Action Listener
     *******************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(UDA_TYPE1_SELECTED)) {
                validateUDA1EnabledState();
            } else if (command.equals(UDA_TYPE2_SELECTED)) {
                validateUDA2EnabledState();
            } else if (command.equals(UDA_TYPE3_SELECTED)) {
                validateUDA3EnabledState();
            } else if (command.equals(INVENTORY_STATUS_MODIFIED)) {
                validateNonsellableEnabledState();
            }
        } catch (Throwable exception) {
            UIStatusUtility.displayException(this, exception);
        }
    }

    private void validateUDA1EnabledState() {
        udaValueEditor1.setEnabled(!udaTypeEditor1.isEmptySelection());
    }

    private void validateUDA2EnabledState() throws Exception {
        UDADetail udaDetail = (UDADetail) udaTypeEditor2.getSelectedItem();
        udaValueEditor2.setItems(model.findUDAValues(udaDetail));
        udaValueEditor2.setEnabled(udaDetail != null);
    }

    private void validateUDA3EnabledState() {
        udaValueEditor3.setEnabled(!udaTypeEditor3.isEmptySelection());
    }

    private void validateNonsellableEnabledState() {
        InventoryStatus status = (InventoryStatus) inventoryStatusEditor.getSelectedItem();
        if (status == InventoryStatus.UNAVAILABLE) {
            nonSellableTypeEditor.setEnabled(true);
        } else {
            nonSellableTypeEditor.setSelectedItem(null);
            nonSellableTypeEditor.setEnabled(false);
        }
    }
}
