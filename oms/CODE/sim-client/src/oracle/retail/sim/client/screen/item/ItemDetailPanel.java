package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.ItemIdDescriptionDisplayer;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.EstimatedQuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RButtonTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
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
import oracle.retail.sim.client.swing.task.UITask;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.AllocationVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.PackHeaderVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.common.transfer.TransferAllocationVO;
import oracle.retail.sim.common.uda.ItemUDAVO;


/********************************************************************************************************
 * Item Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 6220066890945751812L;

    private ItemDetailModel model = new ItemDetailModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");
    private RDisplayLabelEditor upcEditor = new RDisplayLabelEditor("Primary UPC");
    private RDisplayLabelEditor vpnEditor = new RDisplayLabelEditor("VPN");
    private RDisplayLabelEditor brandEditor = new RDisplayLabelEditor("Brand");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Item Status");
    private RButtonTextFieldEditor supplierNameEditor = new RButtonTextFieldEditor("Primary Supplier Name");
    private RDisplayLabelEditor supplierIdEditor = new RDisplayLabelEditor("Primary Supplier Number");
    private RDisplayLabelEditor primaryStoreAreaEditor = new RDisplayLabelEditor("Primary Location");
    private RButton imageButton = new RButton("Image");
    private RDisplayLabelEditor totalStockEditor = new RDisplayLabelEditor("Total Stock On Hand");
    private RDisplayLabelEditor caseSizeEditor = new RDisplayLabelEditor("Pack Size");
    private RDisplayLabelEditor availableEditor = new RDisplayLabelEditor("Available SOH");
    private RDisplayLabelEditor shopFloorEditor = new RDisplayLabelEditor("Shop Floor");
    private RDisplayLabelEditor backRoomEditor = new RDisplayLabelEditor("Back Room");
    private RDisplayLabelEditor transferReserved = new RDisplayLabelEditor("Transfer Reserved");
    private RDisplayLabelEditor customerResvEditor = new RDisplayLabelEditor("Customer Order");
    private RDisplayLabelEditor nonSellableEditor = new RDisplayLabelEditor("Nonsellable");
    private RDisplayLabelEditor rtvReserved = new RDisplayLabelEditor("RTV Reserved");
    private RDisplayLabelEditor unavailableEditor = new RDisplayLabelEditor("Unavailable");
    private RDisplayLabelEditor orderedQtyEditor = new RDisplayLabelEditor("Ordered Qty");
    private RDisplayLabelEditor deliveryBayEditor = new RDisplayLabelEditor("Delivery Bay");
    private RDisplayLabelEditor inTransitEditor = new RDisplayLabelEditor("In Transit");
    private RDisplayLabelEditor receivedTodayEditor = new RDisplayLabelEditor("Received Today");
    private RDisplayLabelEditor currentRetailEditor = new RDisplayLabelEditor("Current Retail");
    private RDisplayLabelEditor sellingUOMEditor = new RDisplayLabelEditor("Selling UOM");
    private RDisplayLabelEditor priceTypeEditor = new RDisplayLabelEditor("Pricing Type");
    private RDisplayLabelEditor multiUnitPriceEditor = new RDisplayLabelEditor("Multi Unit Price");
    private RDisplayLabelEditor multiUnitQtyEditor = new RDisplayLabelEditor("Multi Unit Quantity");
    private RDisplayLabelEditor multiUnitUOM = new RDisplayLabelEditor("Multi Unit UOM");
    private RDisplayLabelEditor uomQtyEditor = new RDisplayLabelEditor("Deliv. Date Warehouse UOM Qty");
    private RDisplayLabelEditor ticketTypeEditor = new RDisplayLabelEditor("Ticket Type");
    private RDisplayLabelEditor departmentEditor = new RDisplayLabelEditor("Dept");
    private RDisplayLabelEditor classEditor = new RDisplayLabelEditor("Class");
    private RDisplayLabelEditor subclassEditor = new RDisplayLabelEditor("Sub-Class");
    private RDisplayLabelEditor diff1Editor = new RDisplayLabelEditor("Diff1");
    private RDisplayLabelEditor diff2Editor = new RDisplayLabelEditor("Diff2");
    private RDisplayLabelEditor diff3Editor = new RDisplayLabelEditor("Diff3");
    private RDisplayLabelEditor diff4Editor = new RDisplayLabelEditor("Diff4");
    private RDisplayLabelEditor replenishmentEditor = new RDisplayLabelEditor("Repl. Method");
    private RDisplayLabelEditor rejectOrderEditor = new RDisplayLabelEditor("Reject Store Order");
    private RDisplayLabelEditor nextDateEditor = new RDisplayLabelEditor("Next Delivery Date");
    private RDisplayLabelEditor uomEditor = new RDisplayLabelEditor("UOM");
    private RCheckBoxEditor orderableEditor = new RCheckBoxEditor("Orderable");
    private RCheckBoxEditor sellableEditor = new RCheckBoxEditor("Sellable");
    private RCheckBoxEditor packItemEditor = new RCheckBoxEditor("Pack Item");
    private RCheckBoxEditor packInventoryEditor = new RCheckBoxEditor("Store Pack Inventory");
    private RCheckBoxEditor consignmentEditor = new RCheckBoxEditor("Consignment");
    private RCheckBoxEditor concessionEditor = new RCheckBoxEditor("Concession");
    private RCheckBoxEditor nonInventoryEditor = new RCheckBoxEditor("Non-Inventory");

    private REditorPanel stockQtyPanel = new REditorPanel(13);

    private UomModeDisplayer uomDisplayer = new UomModeDisplayer();
    private TranslatedObjectDisplayer translationDisplayer = new TranslatedObjectDisplayer();

    private SimTable allocationTable = new SimTable(new AllocationTableDefinition());
    private SimTablePane allocationPane = new SimTablePane(allocationTable);

    private static final String DISPLAY_IMAGE = "display.image";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ItemDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
    	if(!model.isSupplierLookUpAvailable()) {
    		supplierNameEditor.setVisible(false);
    		supplierIdEditor.setVisible(false);
    	}  else {
            supplierNameEditor.setToolTipText("Go to Supplier Detail");
            supplierNameEditor.registerAction(this, SimNavigation.SUPPLIER_DETAIL);
            supplierNameEditor.setSizeType(EditorConstants.LARGE);
    	}
        caseSizeEditor.setDataType(DataTypeConstants.QUANTITY);
        nextDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        rejectOrderEditor.setDisplayer(new BooleanDisplayer());
        currentRetailEditor.setDisplayer(new SimMoneyDisplayer());
        multiUnitPriceEditor.setDisplayer(new SimMoneyDisplayer());
        multiUnitQtyEditor.setDataType(DataTypeConstants.QUANTITY);
        itemDescEditor.setEnabled(true, false);
        brandEditor.setEnabled(true);
        rangedEditor.setEnabled(true, false);        
        allocationPane.setTitleBorder("Planned Warehouse Deliveries and Store Transfers");
        orderableEditor.setEnabled(true, false);
        packItemEditor.setEnabled(true, false);
        sellableEditor.setEnabled(true, false);
        packInventoryEditor.setEnabled(true, false);
        concessionEditor.setEnabled(true, false);
        consignmentEditor.setEnabled(true, false);
        nonInventoryEditor.setEnabled(true, false);
        orderedQtyEditor.setDataType(DataTypeConstants.QUANTITY);
        inTransitEditor.setDataType(DataTypeConstants.QUANTITY);
        transferReserved.setDataType(DataTypeConstants.QUANTITY);
        rtvReserved.setDataType(DataTypeConstants.QUANTITY);
        customerResvEditor.setDataType(DataTypeConstants.QUANTITY);
        nonSellableEditor.setDataType(DataTypeConstants.QUANTITY);
        imageButton.registerAction(this, DISPLAY_IMAGE);
    }

    private void layoutPanel() {
        RPanel supplierPanel = new RPanel(new GridBagLayout());
        supplierPanel.setLineBorder(1);
        supplierPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(upcEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(vpnEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(itemDescEditor, GridTool.constraints(1, 0, 3, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(supplierNameEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(supplierIdEditor, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(brandEditor, GridTool.constraints(1, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        supplierPanel.add(new RLabel(), GridTool.constraints(3, 1, 1, 1, 2, 0, 0, 1, 0, 0, 5, 0));

        if (SimConfigManager.getBoolean(SimConfigManager.DISPLAY_ITEM_IMAGE_BUTTON)) {
            supplierPanel.add(imageButton, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
            supplierPanel.add(rangedEditor, GridTool.constraints(4, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            supplierPanel.add(statusEditor, GridTool.constraints(4, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            supplierPanel.add(primaryStoreAreaEditor, GridTool.constraints(4, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        } else {
            supplierPanel.add(rangedEditor, GridTool.constraints(4, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            supplierPanel.add(statusEditor, GridTool.constraints(4, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            supplierPanel.add(primaryStoreAreaEditor, GridTool.constraints(4, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        }
        supplierPanel.pack();

        stockQtyPanel.setTitleBorder("Stock On Hand Units", 0, 0, 0, 10);
        stockQtyPanel.add(totalStockEditor);
        stockQtyPanel.add(availableEditor);
        stockQtyPanel.add(shopFloorEditor);
        stockQtyPanel.add(backRoomEditor);
        stockQtyPanel.add(unavailableEditor);
        stockQtyPanel.add(transferReserved);
        stockQtyPanel.add(rtvReserved);
        stockQtyPanel.add(customerResvEditor);
        stockQtyPanel.add(nonSellableEditor);
        stockQtyPanel.add(orderedQtyEditor);
        stockQtyPanel.add(deliveryBayEditor);
        stockQtyPanel.add(inTransitEditor);
        stockQtyPanel.add(receivedTodayEditor);

        REditorPanel pricePanel = new REditorPanel(6);
        pricePanel.setTitleBorder("Pricing");
        pricePanel.add(currentRetailEditor);
        pricePanel.add(sellingUOMEditor);
        pricePanel.add(priceTypeEditor);
        pricePanel.add(multiUnitPriceEditor);
        pricePanel.add(multiUnitQtyEditor);
        pricePanel.add(multiUnitUOM);

        REditorPanel orderPanel = new REditorPanel(4);
        orderPanel.setTitleBorder("Ordering Attributes");
        orderPanel.add(replenishmentEditor);
        orderPanel.add(rejectOrderEditor);
        orderPanel.add(nextDateEditor);

        REditorPanel hierarchyPanel = new REditorPanel(7);
        hierarchyPanel.setTitleBorder("Merchandise Hierarchy");
        hierarchyPanel.add(departmentEditor);
        hierarchyPanel.add(classEditor);
        hierarchyPanel.add(subclassEditor);
        hierarchyPanel.add(diff1Editor);
        hierarchyPanel.add(diff2Editor);
        hierarchyPanel.add(diff3Editor);
        hierarchyPanel.add(diff4Editor);

        REditorPanel attributePanel = new REditorPanel(10);
        attributePanel.setTitleBorder("Item Attributes");
        attributePanel.add(uomEditor);
        attributePanel.add(caseSizeEditor);
        attributePanel.add(ticketTypeEditor);
        attributePanel.add(orderableEditor);
        attributePanel.add(sellableEditor);
        attributePanel.add(packItemEditor);
        attributePanel.add(consignmentEditor);
        attributePanel.add(concessionEditor);
        attributePanel.add(nonInventoryEditor);
        attributePanel.add(packInventoryEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(supplierPanel, GridTool.constraints(0, 0, 4, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(stockQtyPanel, GridTool.constraints(0, 1, 1, 2, 0, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(attributePanel, GridTool.constraints(1, 1, 1, 2, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(pricePanel, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(orderPanel, GridTool.constraints(2, 2, 2, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(hierarchyPanel, GridTool.constraints(3, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(allocationPane, GridTool.constraints(0, 4, 4, 1, 0, 1, 0, 3, 0, 0, 5, 0));

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

    public void loadItem() throws Exception {
        model.loadItem();
    }

    public void start() {
        clearScreen();

        ItemDetailVO item = model.getItem();
        if (item == null) {
            return;
        }

        String uomText = uomDisplayer.getDisplayText(UOMMode.STANDARD, item);
        String borderTitle = Translator.getMessage("Stock On Hand {0}", uomText);
        stockQtyPanel.setTitleBorder(borderTitle, 0, 0, 0, 10);

        allocationTable = new SimTable(new AllocationTableDefinition());
        allocationPane.setTable(allocationTable);

        displayBasicInformation(item);
        displayStockInformation(item);
        displayRetailInformation(item);
        displayStoreOrderInformation(item);
        displayAllocations(item);
    }

    public void clearScreen() {
        itemEditor.clear();
        itemDescEditor.clear();
        brandEditor.clear();
        rangedEditor.setSelected(false);
        upcEditor.clear();
        vpnEditor.clear();
        statusEditor.clear();
        supplierNameEditor.clear();
        supplierIdEditor.clear();
        totalStockEditor.clear();
        caseSizeEditor.clear();
        availableEditor.clear();
        shopFloorEditor.clear();
        backRoomEditor.clear();
        unavailableEditor.clear();
        transferReserved.clear();
        rtvReserved.clear();
        customerResvEditor.clear();
        nonSellableEditor.clear();
        orderedQtyEditor.clear();
        deliveryBayEditor.clear();
        inTransitEditor.clear();
        receivedTodayEditor.clear();
        currentRetailEditor.clear();
        sellingUOMEditor.clear();
        priceTypeEditor.clear();
        multiUnitPriceEditor.clear();
        multiUnitQtyEditor.clear();
        multiUnitUOM.clear();
        uomQtyEditor.clear();
        ticketTypeEditor.clear();
        departmentEditor.clear();
        classEditor.clear();
        subclassEditor.clear();
        diff1Editor.clear();
        diff2Editor.clear();
        diff3Editor.clear();
        diff4Editor.clear();
        replenishmentEditor.clear();
        rejectOrderEditor.clear();
        nextDateEditor.clear();
        boolean displaySequence = model.isDisplaySequenceActive();
        primaryStoreAreaEditor.setVisible(displaySequence);
        shopFloorEditor.setVisible(displaySequence);
        backRoomEditor.setVisible(displaySequence);
        deliveryBayEditor.setVisible(model.isDisplayDeliveryBayActive());
        uomEditor.clear();
        orderableEditor.setSelected(false);
        packItemEditor.setSelected(false);
        sellableEditor.setSelected(false);
        concessionEditor.setSelected(false);
        consignmentEditor.setSelected(false);
        nonInventoryEditor.setSelected(false);
        packInventoryEditor.setSelected(false);
    }

    private void displayBasicInformation(ItemDetailVO item) {
        itemEditor.setData(item.getId());

        if (SimConfigManager.isItemShortDescription()) {
            itemDescEditor.setText(item.getShortDescription());
        } else {
            itemDescEditor.setText(item.getLongDescription());
        }

        SupplierVO supplier = item.getSupplierVO();
        if (supplier != null) {
            supplierNameEditor.setText(supplier.getName());
            supplierIdEditor.setData(supplier.getId());
        }
        brandEditor.setData(item.getBrand());
        departmentEditor.setData(item.getDepartmentIdName());
        classEditor.setData(item.getClassIdName());
        subclassEditor.setData(item.getSubclassIdName());

        diff1Editor.setTitle(item.getDifferentiatorType1());
        diff2Editor.setTitle(item.getDifferentiatorType2());
        diff3Editor.setTitle(item.getDifferentiatorType3());
        diff4Editor.setTitle(item.getDifferentiatorType4());

        diff1Editor.setData(item.getDifferentiator1());
        diff2Editor.setData(item.getDifferentiator2());
        diff3Editor.setData(item.getDifferentiator3());
        diff4Editor.setData(item.getDifferentiator4());

        statusEditor.setData(translationDisplayer.getDisplayText(item.getStatus()));
        primaryStoreAreaEditor.setData(translationDisplayer.getDisplayText(item.getPrimarySequenceDescription()));

        upcEditor.setData(item.getUPC());
        vpnEditor.setData(item.getVPN());
        ticketTypeEditor.setData(item.getSuggestedTicketType());

        caseSizeEditor.setData(item.getCaseSize());
        uomEditor.setData(uomDisplayer.getDisplayText(UOMMode.STANDARD, item));
        orderableEditor.setSelected(item.isOrderable());
        sellableEditor.setSelected(item.isSellable());
        packItemEditor.setSelected(item.isPack());
        packInventoryEditor.setSelected(item.isInventoryAtPackLevel());
        
        ItemType itemType = item.getItemType();
        if (itemType == ItemType.CONSIGNMENT) {
            consignmentEditor.setSelected(true);
            nonInventoryEditor.setSelected(true);
        } else if (itemType == ItemType.CONCESSION) {
            concessionEditor.setSelected(true);
            nonInventoryEditor.setSelected(true);
        } else if (itemType == ItemType.NON_INVENTORY) {
            nonInventoryEditor.setSelected(true);
        }
    }

    private void displayStockInformation(ItemDetailVO itemDetailVO) {
        StockItem stockItem = itemDetailVO.getStockItem();

        rangedEditor.setSelected(stockItem.isRanged());

        if (itemDetailVO.isNonInventoryItem() || !itemDetailVO.isRanged()) {
            clearInventoryQuantities();
            return;
        }

        boolean inventoryDisplayable = stockItem.isInventoryDisplayable();
        boolean displayAsEstimate = stockItem.isInventoryEstimated();

        totalStockEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        availableEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        unavailableEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        nonSellableEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        receivedTodayEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        shopFloorEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        backRoomEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));
        deliveryBayEditor.setDisplayer(new EstimatedQuantityDisplayer(displayAsEstimate));

        totalStockEditor.setData(inventoryDisplayable ? stockItem.getStockOnHand() : null);
        availableEditor.setData(inventoryDisplayable ? stockItem.getAvailableStockOnHand() : null);
        unavailableEditor.setData(inventoryDisplayable ? stockItem.getUnavailableQty() : null);
        nonSellableEditor.setData(inventoryDisplayable ? stockItem.getNonSellableQty() : null);
        shopFloorEditor.setData(inventoryDisplayable ? stockItem.getStockOnShopFloor() : null);
        backRoomEditor.setData(inventoryDisplayable ? stockItem.getStockInBackRoom() : null);
        deliveryBayEditor.setData(inventoryDisplayable ? stockItem.getStockInDeliveryBay() : null);
        inTransitEditor.setData(stockItem.getInTransitQty());
        transferReserved.setData(stockItem.getTransferReservedQty());
        rtvReserved.setData(stockItem.getVendorReturnQty());
        customerResvEditor.setData(stockItem.getCustomerReservedQty());
        orderedQtyEditor.setData(itemDetailVO.getOrderedQty());

        if (!inventoryDisplayable) {
            receivedTodayEditor.setData(null);
        } else if (SimDateUtil.isSameDay(model.getTimeZone(), SimDateUtil.getCurrentDate(), stockItem.getLastReceivedDate())) {
            receivedTodayEditor.setData(stockItem.getReceivedTodayQty());
        } else {
            receivedTodayEditor.setData(Quantity.ZERO);
        }
    }

    private void clearInventoryQuantities() {
        totalStockEditor.setData(null);
        availableEditor.setData(null);
        unavailableEditor.setData(null);
        nonSellableEditor.setData(null);
        receivedTodayEditor.setData(null);
        shopFloorEditor.setData(null);
        backRoomEditor.setData(null);
        deliveryBayEditor.setData(null);
        inTransitEditor.setData(null);
        transferReserved.setData(null);
        rtvReserved.setData(null);
        customerResvEditor.setData(null);
        orderedQtyEditor.setData(null);
    }

    private void displayRetailInformation(ItemDetailVO item) {
        priceTypeEditor.setData(translationDisplayer.getDisplayText(item.getPriceType()));
        currentRetailEditor.setData(item.getRetailPrice());
        sellingUOMEditor.setData(item.getSellingUOM());
        multiUnitPriceEditor.setData(item.getMultiUnitRetail());
        multiUnitQtyEditor.setData(item.getMultiUnits());
        multiUnitUOM.setData(item.getMultiSellingUOM());
    }

    private void displayStoreOrderInformation(ItemDetailVO item) {
        replenishmentEditor.setData(item.getReplenishmentMethodDescription());
        if (item.isStoreOrderReplenishmentType()) {
            rejectOrderEditor.setData(item.isStoreOrderAllowed());
        } else {
            rejectOrderEditor.setData(null);
        }
        nextDateEditor.setData(item.getNextDeliveryDate());
    }

    private void displayAllocations(ItemDetailVO item) {
        String uom = item.getUnitOfMeasure();
        List<AllocationTransferWrapper> wrappers = new ArrayList<>();
        for (AllocationVO allocation : item.getAllocations()) {
            wrappers.add(ClientWrapperFactory.createAllocationTransferWrapper(allocation, uom));
        }
        for (TransferAllocationVO transferAllocationVO : item.getTransfers()) {
            wrappers.add(ClientWrapperFactory.createAllocationTransferWrapper(transferAllocationVO, uom));
        }
        allocationTable.setRows(wrappers);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void handleStockLocator() throws Exception {
        model.storeItem();
        StockLocatorDialog dialog = new StockLocatorDialog();
        dialog.setItem(model.getItem());
        dialog.setVisible(true);
    }

    public void handlePriceInformation() throws Exception {
        model.storeItem();
        PriceInformationDialog dialog = new PriceInformationDialog();
        dialog.setItem(model.getItem());
        dialog.setVisible(true);
    }

    public void handleUINDetail() {
        model.storeItem();
    }

    public boolean handleUDADetail() throws Exception {
        List<ItemUDAVO> itemUDAVOs = model.getUDADetail();
        if (itemUDAVOs.size() > 0) {
            ItemUdaDialog dialog = new ItemUdaDialog();
            dialog.setItem(model.getItem());
            dialog.setUDADetail(itemUDAVOs);
            dialog.setVisible(true);
            return true;
        }
        displayMessage(ItemMessageText.NO_UDA_DETAIL);
        return false;
    }

    public boolean handleCustomerOrder() throws Exception {
        if (model.hasCustomerOrders()) {
            model.storeItem();
            return true;
        }
        displayMessage(ItemMessageText.NO_CUSTOMER_ORDERS_FOUND);
        return false;
    }

    public boolean handleAdditionalSupplier() throws Exception {
        if (model.hasAdditionalSuppliers()) {
            return true;
        }
        displayMessage(ItemMessageText.NO_ADDITIONAL_SUPPLIERS);
        return false;
    }

    public boolean displayComponentsForPack() throws Exception {
        if (model.getItem().isPack()) {
            model.storeItem();
            PackComponentDialog dialog = new PackComponentDialog();
            dialog.setItem(model.getItem());
            dialog.setVisible(true);
            return true;
        }
        displayMessage(ItemMessageText.NO_COMPONENT_INFORMATION);
        return false;
    }

    public boolean displayPacksForComponent() throws Exception {
        List<PackHeaderVO> packItems = model.getPackItems();
        if (packItems.size() > 0) {
            PackHeaderDialog dialog = new PackHeaderDialog();
            dialog.setItem(model.getItem());
            dialog.setPackHeaders(packItems);
            dialog.addREventListener(this);
            dialog.setVisible(true);
            return true;
        }
        displayMessage(ItemMessageText.NO_PACK_INFORMATION);
        return false;
    }

    public boolean handleRelatedItem() {
        model.storeItem();
        return true;
    }

    /****************************************************************************************************
     * Handle Print Request
     * @throws Exception
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        model.printItemDetail();
    }

    /****************************************************************************************************
     * Handle Non-Sellable
     ***************************************************************************************************/

    public void handleNonSellable() throws Exception {
        NonSellableQuantityDialog dialog = new NonSellableQuantityDialog();
        dialog.setItem(model.getItem());
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * HANDLE SCREEN EVENTS
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.SUPPLIER_DETAIL)) {
                doDisplaySupplier();
            } else if (command.equals(DISPLAY_IMAGE)) {
                doDisplayImage();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    private void doDisplayImage() throws Exception {
        execute(new LaunchItemTask());
    }

    public void doDisplaySupplier() throws Exception {
        Supplier supplier = model.getSupplier();
        if (supplier == null) {
            String value = model.getItem().getId() + " " + model.getItem().getShortDescription();
            throw new BusinessException(CommonMessageText.SUPPLIER_NOT_FOUND, value);
        }

        List<Supplier> additionalSuppliers = model.findAdditionalSuppliers();

        RepositoryManager.addStateObject(SimClientStateKey.ITEM_DETAIL_ORIGIN, StringConstants.EMPTY);

        SupplierDetailDialog dialog = new SupplierDetailDialog();
        dialog.setSupplier(supplier, additionalSuppliers);
        dialog.setVisible(true);
    }

    public boolean showUINDetailButton() throws Exception {
        return model.isUINDetailAvailable();
    }

    public boolean showStockLocatorButton() throws Exception {
        return model.isStockLocatorAvailable();
    }

    public boolean showNonSellableDetailButton() {
        return model.isNonSellableTypesActive();
    }

    public boolean showPackInfo() throws Exception {
        return model.isPackInfoAvailable();
    }

    public boolean showComponentInfo() throws Exception {
        return model.isComponentInfoAvailable();
    }

    /****************************************************************************************************
     * ALLOCATION DEFINITION
     ***************************************************************************************************/

    private class AllocationTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return AllocationTransferWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("deliveryDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Delivery Date", "deliveryDate"));
            attributes.add(new SimTableAttribute("Location", "name"));
            attributes.add(new SimTableAttribute("Location Type", "location"));
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasure"));
            attributes.add(new SimTableAttribute("Quantity", "quantity", new QuantityDisplayer()));
            if (model.isMultipleDeliveryAllowed()) {
                attributes.add(new SimTableAttribute("Delivery Timeslot", "deliverySlotDescription"));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * Launch Item Task - Thread task to display image popup dialog
     ***************************************************************************************************/

    private class LaunchItemTask implements UITask {

        private List<ImageIcon> itemImages;
        private String itemDescription;

        public boolean executeRequest() {
            ItemIdDescriptionDisplayer displayer = new ItemIdDescriptionDisplayer();
            try {
                itemDescription = displayer.getDisplayText(model.getItem().getStockItem());
                itemImages = model.findItemImages();
            } catch (Throwable exception) {
                LogService.error(this, exception.getMessage());
                itemImages = Collections.emptyList();
            }
            return true;
        }

        public void executeResponse() {
            ItemImageDialog dialog = new ItemImageDialog();
            dialog.setTitle(Translator.getText("Item Image") + " - " + itemDescription);
            dialog.setImages(itemImages);
            dialog.setVisible(true);
        }
    }
}
