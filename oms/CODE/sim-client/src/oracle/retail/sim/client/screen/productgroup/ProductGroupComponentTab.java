package oracle.retail.sim.client.screen.productgroup;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Product Group Component Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupComponentTab extends SimTab implements REventListener {
    private static final long serialVersionUID = -3871982505759582176L;

    private ProductGroupDetailModel model;

    private RDisplayLabelEditor groupTypeEditor = new RDisplayLabelEditor("Type");
    private RDisplayLabelEditor countMethodEditor = new RDisplayLabelEditor("Counting Method");
    private RDisplayLabelEditor recommendEditor = new RDisplayLabelEditor("Recommended # of Items");
    private RDisplayLabelEditor totalItemsEditor = new RDisplayLabelEditor("Total # of Items in Group");

    private static final String HIERARCHY = "Hierarchy";
    private static final String ITEM = "Item";
    private static final String SUPPLIER = "Supplier";
    private static final String PROMOTION = "Promotion ID";
    private static final String ALL_DEPARTMENTS = "All Departments";
    private static final String[] RADIO_BUTTONS = { HIERARCHY, ITEM, SUPPLIER, PROMOTION, ALL_DEPARTMENTS };
    private RRadioButtonEditor elementTypeEditor = new RRadioButtonEditor();
    private ProductGroupElementPanel elementTypePanel = new ProductGroupElementPanel();

    private SimTable groupDetailTable = new SimTable(new ProductDetailTableDefinition());
    private SimTablePane groupDetailPane = new SimTablePane(groupDetailTable);

    private static final String ADD_TO_GROUP = "Add To Group";
    private static final String DELETE_FROM_GROUP = "Delete From Group";

    private RButton addToGroupButton = new RButton(ADD_TO_GROUP);
    private RButton deleteButton = new RButton(DELETE_FROM_GROUP);

    private RLabel allItemsLabel = new RLabel("All Items");

    private TranslatedObjectDisplayer translatedDisplayer = new TranslatedObjectDisplayer();

    private RCardPanel bottomPanel = new RCardPanel();
    private static final String TABLE_CARD = "TableCard";
    private static final String ALL_DEPT_CARD = "AllDeptCard";

    private static final String ELEMENT_TYPE_SELECTED = "ElementType.selected";

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/
    public ProductGroupComponentTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        elementTypeEditor.setRadioButtons(RADIO_BUTTONS, 5, 1);
        elementTypeEditor.setRadioTextPosition(EditorConstants.RIGHT);
        elementTypeEditor.registerAction(this, ELEMENT_TYPE_SELECTED);

        elementTypePanel.setItemSearchListener(buildItemSearchListener());
        elementTypePanel.setSupplierSearchListener(buildSupplierSearchListener());

        groupDetailTable.setColumnSize("itemCount", SimTable.LABEL_WIDTH);
        groupDetailTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        addToGroupButton.registerAction(this, ADD_TO_GROUP);
        deleteButton.registerAction(this, DELETE_FROM_GROUP);

        Border border1 = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border border2 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        allItemsLabel.setBorder(BorderFactory.createCompoundBorder(border1, border2));
        allItemsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void layoutTab() {
        REditorPanel headerPanel = new REditorPanel(2, 2);
        headerPanel.add(groupTypeEditor);
        headerPanel.add(countMethodEditor);
        headerPanel.add(recommendEditor);
        headerPanel.add(totalItemsEditor);

        RButtonPanel buttonPanel = new RButtonPanel();
        buttonPanel.add(addToGroupButton);
        buttonPanel.add(deleteButton);

        RPanel elementPanel = new RPanel(new GridBagLayout());
        elementPanel.setTitleBorder("New Product Group Element");
        elementPanel.add(elementTypeEditor, GridTool.constraints(0, 0, 1, 2, 0, 0, 0, 3, 0, 10, 5, 0));
        elementPanel.add(elementTypePanel, GridTool.constraints(1, 0, 1, 2, 1, 0, 0, 3, 0, 10, 5, 0));
        elementPanel.add(buttonPanel, GridTool.constraints(2, 1, 1, 1, 0, 0, 4, 0, 0, 10, 5, 0));

        RPanel messagePanel = new RPanel(new GridBagLayout());
        messagePanel.add(allItemsLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 10, 5, 0));
        messagePanel.add(new JLabel(), GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 10, 5, 0));

        bottomPanel.addCard(TABLE_CARD, groupDetailPane);
        bottomPanel.addCard(ALL_DEPT_CARD, messagePanel);

        setLayout(new GridBagLayout());
        add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        add(elementPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        add(bottomPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    public void setModel(ProductGroupDetailModel model) {
        this.model = model;
    }

    public void loadTab() throws Exception {
        elementTypePanel.loadHierarchyPanel();

        ProductGroup productGroup = model.getProductGroup();
        groupTypeEditor.setData(translatedDisplayer.getDisplayText(productGroup.getType()));

        if (productGroup.getCountingMethod() != null) {
            countMethodEditor.setData(translatedDisplayer.getDisplayText(productGroup.getCountingMethod()));
        } else {
            countMethodEditor.setData(translatedDisplayer.getDisplayText("Auto"));
        }
        countMethodEditor.setVisible(model.isCountMethodDisplayable());

        if (productGroup.isAllItems()) {
            displayAllDepartmentPanel();
            return;
        }
        if (productGroup.getId() != null) {
            groupDetailTable.setRows(model.getDetailLineItems());
            boolean isEditModeAllowed = model.isDataEntryAllowed();
            addToGroupButton.setEnabled(isEditModeAllowed);
            deleteButton.setEnabled(isEditModeAllowed);
        } else {
            groupDetailTable.setRows(model.getDetailLineItems());
        }
        displayGroupItemTable();
    }

    /****************************************************************************************************
     * Enable And Disable Table Or Panel For All Departments
     ***************************************************************************************************/

    private void displayGroupItemTable() throws Exception {
        ProductGroupWrapper wrapper = model.getProductGroupWrapper();
        boolean isEditModeAllowed = model.isDataEntryAllowed();
        elementTypeEditor.setEnabled(HIERARCHY, isEditModeAllowed && wrapper.isHierarchyOptionEnabled());
        elementTypeEditor.setEnabled(ITEM, isEditModeAllowed && wrapper.isItemOptionEnabled());
        elementTypeEditor.setEnabled(SUPPLIER, isEditModeAllowed && wrapper.isSupplierOptionEnabled());
        elementTypeEditor.setEnabled(PROMOTION, isEditModeAllowed && wrapper.isPromotionIdOptionEnabled());
        elementTypeEditor.setEnabled(ALL_DEPARTMENTS, isEditModeAllowed && wrapper.isAllDepartmentOptionEnabled());
        elementTypeEditor.setSelected(HIERARCHY, true);
        addToGroupButton.setEnabled(isEditModeAllowed);
        deleteButton.setEnabled(isEditModeAllowed);
        bottomPanel.showCard(TABLE_CARD);
        refreshItemCount();
    }

    private void displayAllDepartmentPanel() throws Exception {
        allItemsLabel.setText(ProductGroupMessageText.ALL_ITEMS_MESSAGE.getText());
        elementTypeEditor.setEnabled(false);
        addToGroupButton.setEnabled(false);
        deleteButton.setEnabled(model.isDataEntryAllowed());
        bottomPanel.showCard(ALL_DEPT_CARD);
        groupDetailTable.clearRows();
        refreshItemCount();
    }

    /****************************************************************************************************
     * Handle Action Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ELEMENT_TYPE_SELECTED)) {
                doElementTypeModified();
            } else if (command.equals(ADD_TO_GROUP)) {
                doAddToGroup();
            } else if (command.equals(DELETE_FROM_GROUP)) {
                doDeleteFromGroup();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Element Type Modified Action
     ***************************************************************************************************/

    private void doElementTypeModified() {
        if (elementTypeEditor.isSelected(HIERARCHY)) {
            elementTypePanel.showHierarchyPanel();
        } else if (elementTypeEditor.isSelected(ITEM)) {
            elementTypePanel.showItemPanel();
        } else if (elementTypeEditor.isSelected(SUPPLIER)) {
            elementTypePanel.showSupplierPanel();
        } else if (elementTypeEditor.isSelected(PROMOTION)) {
            elementTypePanel.showPromoPanel();
        } else if (elementTypeEditor.isSelected(ALL_DEPARTMENTS)) {
            elementTypePanel.showEmptyPanel();
        }
    }

    /****************************************************************************************************
     * Add To Group Action
     ***************************************************************************************************/

    private void doAddToGroup() throws Exception {
        if (elementTypeEditor.isSelected(HIERARCHY)) {
            addHierarchyItems();
        } else if (elementTypeEditor.isSelected(ITEM)) {
            addSingleLocalItem();
        } else if (elementTypeEditor.isSelected(SUPPLIER)) {
            addSupplierItems();
        } else if (elementTypeEditor.isSelected(PROMOTION)) {
            addPromotionItems();
        } else if (elementTypeEditor.isSelected(ALL_DEPARTMENTS)) {
            addAllDepartments();
        }
    }

    /****************************************************************************************************
     * Delete From Group Action
     ***************************************************************************************************/

    private void doDeleteFromGroup() throws Exception {
        ProductGroup productGroup = getProductGroup();
        if (productGroup.isAllItems()) {
            productGroup.setAllItems(false);
            displayGroupItemTable();
            return;
        }
        if (groupDetailTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        groupDetailTable.stopEditing();

        List<ProductGroupDetailLineItemWrapper> wrappers = groupDetailTable.getAllSelectedRowData();
        for (ProductGroupDetailLineItemWrapper wrapper : wrappers) {
            model.removeLineItem(wrapper);
            groupDetailTable.removeRow(wrapper);
        }
        refreshItemCount();
    }

    /****************************************************************************************************
     * Add Hierarchy Items
     ***************************************************************************************************/

    private void addHierarchyItems() throws Exception {
        MdseHierarchyNode node = elementTypePanel.getHierarchy();
        if (node == null) {
            displayError(ProductGroupMessageText.MISSING_HIERARCHY);
            return;
        }
        Integer additionalCount = model.calculateHierarchyItemCount(node);
        ProductGroupHierarchy hierarchy = model.buildProductGroupHierarchy(node, additionalCount);
        ProductGroup productGroup = getProductGroup();
        productGroup.doSetAllItems(false);
        productGroup.addHierarchy(hierarchy);

        groupDetailTable.addRow(ClientWrapperFactory.createProductGroupDetailLineItemWrapper(hierarchy));
        elementTypePanel.clearHierarchyNode();
        refreshItemCount();
    }

    /****************************************************************************************************
     * Add Single Location Item
     ***************************************************************************************************/

    private void addSingleLocalItem() throws Exception {
        ProductGroupItem item = elementTypePanel.getProductGroupItem();
        if (item == null) {
            displayError(ProductGroupMessageText.MISSING_ITEM);
            return;
        }
        ProductGroup productGroup = getProductGroup();
        productGroup.doSetAllItems(false);
        productGroup.addSingleItem(item);
        groupDetailTable.addRow(ClientWrapperFactory.createProductGroupDetailLineItemWrapper(item));
        elementTypePanel.clearItem();
        refreshItemCount();
    }

    /****************************************************************************************************
     * Add Supplier Items To Group
     ***************************************************************************************************/

    private void addSupplierItems() throws Exception {
        Supplier supplier = elementTypePanel.getSupplier();
        if (supplier == null) {
            displayError(ProductGroupMessageText.MISSING_SUPPLIER);
            return;
        }
        List<ProductGroupItem> items = model.findSupplierItems(supplier);
        addItemsToProductGroup(items, true);
        elementTypePanel.clearSupplier();
        refreshItemCount();
    }

    /****************************************************************************************************
     * Add Promotion Items To Group
     ***************************************************************************************************/

    private void addPromotionItems() throws Exception {
        Long promotionId = elementTypePanel.getPromotionId();
        if (promotionId == null) {
            displayError(ProductGroupMessageText.MISSING_PROMOTION);
            return;
        }
        List<ProductGroupItem> items = model.findPromotionItems(promotionId);
        if (items.isEmpty()) {
            displayWarning(ItemRequestMessageText.PROMOTION_ITEM_ERROR);
            return;
        }
        addItemsToProductGroup(items, false);
        elementTypePanel.clearPromotionId();
        refreshItemCount();
    }

    /****************************************************************************************************
     * Add All Departments To Group
     ***************************************************************************************************/

    private void addAllDepartments() throws Exception {
        ProductGroup productGroup = getProductGroup();
        productGroup.setAllItems(true);
        displayAllDepartmentPanel();
    }

    /****************************************************************************************************
     * Helper method to add an array of items to the product group.
     ***************************************************************************************************/

    private void addItemsToProductGroup(List<ProductGroupItem> items, boolean isSupplierOption) {
        ProductGroup productGroup = model.getProductGroupWrapper().getProductGroup();
        if (productGroup.getType().isItemRequest()) {
            addItemsToItemRequestGroup(items, isSupplierOption);
        } else {
            addItemsToNormalGroup(items);
        }
    }

    private void addItemsToItemRequestGroup(List<ProductGroupItem> productGroupItems, boolean isSupplierOption) {
        ProductGroup productGroup = model.getProductGroupWrapper().getProductGroup();
        List<ProductGroupItem> failedItems = new ArrayList<ProductGroupItem>();
        boolean noValidItemsFound = true;
        for (ProductGroupItem productGroupItem : productGroupItems) {
            if (isQualifiedItem(productGroupItem)) {
                try {
                    if (productGroupItem.isStoreOrderReplenishmentType()) {
                        noValidItemsFound = false;
                        ProductGroupDetailLineItemWrapper wrapper = ClientWrapperFactory.createProductGroupDetailLineItemWrapper(productGroupItem);
                        productGroup.addSingleItem(wrapper.getItem());
                        groupDetailTable.addRow(wrapper);
                    }
                } catch (BusinessException ex) {
                    failedItems.add(productGroupItem);
                }
            }
        }
        if (noValidItemsFound) {
            if (isSupplierOption) {
                displayWarning(ItemRequestMessageText.SUPPLIER_ITEMS_ERROR);
            } else {
                displayWarning(ItemRequestMessageText.PROMOTION_ITEM_ERROR);
            }
            return;
        }
        processFailedItems(failedItems);
    }

    private void addItemsToNormalGroup(List<ProductGroupItem> items) {
        ProductGroup productGroup = model.getProductGroupWrapper().getProductGroup();
        List<ProductGroupItem> failedItems = new ArrayList<>();
        for (ProductGroupItem productGroupItem : items) {
            if (isQualifiedItem(productGroupItem)) {
                try {
                    ProductGroupDetailLineItemWrapper wrapper = ClientWrapperFactory.createProductGroupDetailLineItemWrapper(productGroupItem);
                    productGroup.addSingleItem(wrapper.getItem());
                    groupDetailTable.addRow(wrapper);
                } catch (BusinessException bre) {
                    failedItems.add(productGroupItem);
                }
            }
        }
        processFailedItems(failedItems);
    }

    private boolean isQualifiedItem(ProductGroupItem productGroupItem) {
        if (productGroupItem.getItemType().isNonInventoryItem()) {
            return false;
        }
        return productGroupItem.isSellable() || productGroupItem.getItemType().isPack();
    }

    private void processFailedItems(List<ProductGroupItem> failedItems) {
        if (failedItems.isEmpty()) {
            return;
        }
        List<String> itemIds = new ArrayList<String>();
        for (ProductGroupItem item : failedItems) {
            itemIds.add(item.getId());
        }
        displayException(new BusinessException(ProductGroupMessageText.ADD_ITEMS_ERROR, itemIds));
    }

    /****************************************************************************************************
     * Refresh Item Count
     ***************************************************************************************************/

    private void refreshItemCount() throws Exception {
        List<ProductGroupDetailLineItemWrapper> wrappers = groupDetailTable.getAllRowData();
        NumberFormat formatter = LocaleManager.getIntegerFormatter(true);
        recommendEditor.setData(formatter.format(model.getRecommendedItemCount()));
        totalItemsEditor.setData(formatter.format(model.calculateItemCount(wrappers)));
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private ProductGroup getProductGroup() {
        if (model.getProductGroupWrapper() != null) {
            return model.getProductGroupWrapper().getProductGroup();
        }
        return null;
    }

    /****************************************************************************************************
     * Search Listeners
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                elementTypePanel.setProductGroupItem(itemVO);
            }
        };
    }

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                elementTypePanel.setSupplier(supplier);
            }
        };
    }

    /****************************************************************************************************
     * Product Group Detail Table
     ***************************************************************************************************/

    private class ProductDetailTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ProductGroupDetailLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(5);
            attributes.add(new SimTableAttribute("Department", "department"));
            attributes.add(new SimTableAttribute("Class", "clazz"));
            attributes.add(new SimTableAttribute("Sub-Class", "subclass"));
            attributes.add(new SimTableAttribute("Item", "description"));
            attributes.add(new SimTableAttribute("Total # of Items", "itemCount"));
            return attributes;
        }
    }
}
