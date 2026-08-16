package oracle.retail.sim.client.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;

import extra.retail.sim.client.screen.imeifulfillmentorderdelivery.IMEIFulfillmentOrderDeliveryLineItemWrapper;
import extra.retail.sim.webservice.fulfillmentorderdelivery.model.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.webservice.fulfillmentorderdelivery.model.ExtraIMEIFulfillmentOrderDelivery;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.IMEIFulfillmentOrderDelivery;
import oracle.retail.sim.client.screen.carton.CartonLineItemWrapper;
import oracle.retail.sim.client.screen.directdelivery.DirectDeliveryLineItemWrapper;
import oracle.retail.sim.client.screen.directdelivery.PurchaseOrderLineItemWrapper;
import oracle.retail.sim.client.screen.fulfillmentorder.FulfillmentOrderLineItemWrapper;
import oracle.retail.sim.client.screen.fulfillmentorder.FulfillmentOrderMgmtListWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItemWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderdelivery.FulfillmentOrderDeliveryWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderpick.FulfillmentOrderBinWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderpick.FulfillmentOrderPickLineItemWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderpick.FulfillmentOrderPickWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderpick.ItemSubstitutionWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItemWrapper;
import oracle.retail.sim.client.screen.fulfillmentorderreversepick.FulfillmentOrderReversePickWrapper;
import oracle.retail.sim.client.screen.invadjustment.InventoryAdjustmentLineItemWrapper;
import oracle.retail.sim.client.screen.invadjustment.InventoryTemplateLineItemWrapper;
import oracle.retail.sim.client.screen.item.AllocationTransferWrapper;
import oracle.retail.sim.client.screen.item.NonSellableQuantityWrapper;
import oracle.retail.sim.client.screen.item.PackComponentWrapper;
import oracle.retail.sim.client.screen.item.RelatedItemWrapper;
import oracle.retail.sim.client.screen.itemrequest.ItemRequestLineItemWrapper;
import oracle.retail.sim.client.screen.itemticket.ItemTicketLineItemWrapper;
import oracle.retail.sim.client.screen.itemticket.ItemTicketWrapper;
import oracle.retail.sim.client.screen.printer.RetailStorePrinterWrapper;
import oracle.retail.sim.client.screen.printer.SessionPrinterWrapper;
import oracle.retail.sim.client.screen.productgroup.ProductGroupDetailLineItemWrapper;
import oracle.retail.sim.client.screen.productgroup.ProductGroupScheduleWrapper;
import oracle.retail.sim.client.screen.reportformat.RPrinterDialogWrapper;
import oracle.retail.sim.client.screen.reportformat.ReportFormatWrapper;
import oracle.retail.sim.client.screen.returns.ReturnLineItemWrapper;
import oracle.retail.sim.client.screen.returns.ReturnReasonWrapper;
import oracle.retail.sim.client.screen.security.DataPermissionWrapper;
import oracle.retail.sim.client.screen.security.RoleWrapper;
import oracle.retail.sim.client.screen.security.UserDetailWrapper;
import oracle.retail.sim.client.screen.security.UserRoleAssignmentAction;
import oracle.retail.sim.client.screen.security.UserRoleAssignmentWrapper;
import oracle.retail.sim.client.screen.security.UserRoleWrapper;
import oracle.retail.sim.client.screen.security.UserStoreAssignmentAction;
import oracle.retail.sim.client.screen.security.UserStoreAssignmentWrapper;
import oracle.retail.sim.client.screen.security.UserWrapper;
import oracle.retail.sim.client.screen.shelfreplenishment.ShelfReplenishmentLineItemWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountAuthorizeUinWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountChildWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountComponentDetailWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountLineItemAuthWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountLineItemWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountUinWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountWrapper;
import oracle.retail.sim.client.screen.storeorder.StoreOrderLineItemWrapper;
import oracle.retail.sim.client.screen.storeorder.StoreOrderWrapper;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceAreaWrapper;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceItemWrapper;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceWrapper;
import oracle.retail.sim.client.screen.theme.CustomThemeWrapper;
import oracle.retail.sim.client.screen.theme.FontWrapper;
import oracle.retail.sim.client.screen.theme.IconWrapper;
import oracle.retail.sim.client.screen.tranhistory.TransactionHistoryVOWrapper;
import oracle.retail.sim.client.screen.transfer.TransferLineItemWrapper;
import oracle.retail.sim.client.screen.translation.TranslationDetailWrapper;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryCartonWrapper;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryFulfillmentOrderWrapper;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryLineItemWrapper;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItem;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateLineItem;
import oracle.retail.sim.common.item.AllocationVO;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestLineItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentLineItem;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnLineItem;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.theme.CustomFont;
import oracle.retail.sim.common.theme.CustomIcon;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tranhistory.TransactionHistoryVO;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferAllocationVO;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;

/**
 * Default implementation of the WrapperFactoryInterface that instantiates business objects.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ClientWrapperFactoryImpl implements ClientWrapperFactoryInterface {
    public AllocationTransferWrapper createAllocationTransferWrapper(AllocationVO allocation, String uom) {
        return new AllocationTransferWrapper(allocation, uom);
    }

    public AllocationTransferWrapper createAllocationTransferWrapper(TransferAllocationVO allocation, String uom) {
        return new AllocationTransferWrapper(allocation, uom);
    }

    public CartonLineItemWrapper createCartonLineItemWrapper(WarehouseDeliveryLineItem lineItem) {
        return new CartonLineItemWrapper(lineItem);
    }

    public FulfillmentOrderMgmtListWrapper createFulfillmentOrderMgmtListWrapper(FulfillmentOrderMgmtListVO fulfillmentOrderMgmtListVO) {
        return new FulfillmentOrderMgmtListWrapper(fulfillmentOrderMgmtListVO);
    }

    public DirectDeliveryLineItemWrapper createDirectDeliveryLineItemWrapper(DirectDelivery delivery) {
        return new DirectDeliveryLineItemWrapper(delivery);
    }

    public FulfillmentOrderBinWrapper createFulfillmentOrderBinWrapper(FulfillmentOrderBin bin, boolean isEditable) {
        return new FulfillmentOrderBinWrapper(bin, isEditable);
    }

    public FulfillmentOrderDeliveryWrapper createFulfillmentOrderDeliveryWrapper(FulfillmentOrderDeliveryVO customerOrderDeliveryVO) {
        return new FulfillmentOrderDeliveryWrapper(customerOrderDeliveryVO);
    }

    public FulfillmentOrderDeliveryLineItemWrapper createFulfillmentOrderDeliveryLineItemWrapper(FulfillmentOrderDelivery delivery, FulfillmentOrderDeliveryLineItem lineItem,
            FulfillmentOrder fulfillmentOrder, FulfillmentOrderLineItem orderLineItem, BigDecimal uomConversionFactor) {
        return new FulfillmentOrderDeliveryLineItemWrapper(delivery, lineItem, fulfillmentOrder, orderLineItem, uomConversionFactor);
    }
    public IMEIFulfillmentOrderDeliveryLineItemWrapper createIMEIFulfillmentOrderDeliveryLineItemWrapper(IMEIFulfillmentOrderDelivery orderDelivery) {
        return new IMEIFulfillmentOrderDeliveryLineItemWrapper(orderDelivery);
    }

    public FulfillmentOrderPickLineItemWrapper createFulfillmentOrderPickLineItemWrapper(FulfillmentOrderPick pick, FulfillmentOrderPickLineItem lineItem, FulfillmentOrder fulfillmentOrder,
            ToleranceAdmin toleranceAdmin, BigDecimal uomConversionFactor) {
        return new FulfillmentOrderPickLineItemWrapper(pick, lineItem, fulfillmentOrder, toleranceAdmin, uomConversionFactor);
    }

    public FulfillmentOrderPickWrapper createFulfillmentOrderPickWrapper(FulfillmentOrderPickVO pickVO) {
        return new FulfillmentOrderPickWrapper(pickVO);
    }

    public FulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(FulfillmentOrder order, FulfillmentOrderLineItem lineItem, BigDecimal uomConversionFactor) {
        return new FulfillmentOrderLineItemWrapper(order, lineItem, uomConversionFactor);
    }

    public FulfillmentOrderReversePickLineItemWrapper createFulfillmentOrderReversePickLineItemWrapper(FulfillmentOrderReversePick reversePick, FulfillmentOrderReversePickLineItem reversePickLineItem) {
        return new FulfillmentOrderReversePickLineItemWrapper(reversePick, reversePickLineItem);
    }

    public FulfillmentOrderReversePickWrapper createFulfillmentOrderReversePickWrapper(FulfillmentOrderReversePickVO reversePickVO) {
        return new FulfillmentOrderReversePickWrapper(reversePickVO);
    }

    public InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentLineItem lineItem) {
        return new InventoryAdjustmentLineItemWrapper(adjustment, lineItem);
    }

    public InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentReason defaultReason) {
        return new InventoryAdjustmentLineItemWrapper(adjustment, defaultReason);
    }

    public InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template) {
        return new InventoryTemplateLineItemWrapper(template);
    }

    public InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template, InventoryAdjustmentTemplateLineItem lineItem) {
        return new InventoryTemplateLineItemWrapper(template, lineItem);
    }

    public ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest) {
        return new ItemRequestLineItemWrapper(itemRequest);
    }

    public ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest, ItemRequestLineItem lineItem) {
        return new ItemRequestLineItemWrapper(itemRequest, lineItem);
    }

    public ItemSubstitutionWrapper createItemSubstitutionWrapper(FulfillmentOrderPickLineItem originalItem, RelatedItem relatedItem, BigDecimal uomConvFactor) {
        return new ItemSubstitutionWrapper(originalItem, relatedItem, uomConvFactor);
    }

    public NonSellableQuantityWrapper createNonSellableQuantityWrapper(NonSellableQtyType quantityType, Quantity quantity) {
        return new NonSellableQuantityWrapper(quantityType, quantity);
    }

    public PackComponentWrapper createPackComponentWrapper(StockItem componentItem) {
        return new PackComponentWrapper(componentItem);
    }

    public ItemTicketWrapper createItemTicketWrapper(ItemTicket itemTicket) {
        return new ItemTicketWrapper(itemTicket);
    }

    public ShelfReplenishmentLineItemWrapper createItemTicketWrapper(ShelfReplenishment shelfReplenishment, ShelfReplenishmentLineItem lineItem) {
        return new ShelfReplenishmentLineItemWrapper(shelfReplenishment, lineItem);
    }

    public RelatedItemWrapper createRelatedItemWrapper(RelatedItem relatedItem) {
        return new RelatedItemWrapper(relatedItem);
    }

    public RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer) {
        return new RetailStorePrinterWrapper(printer);
    }

    public SessionPrinterWrapper createSessionPrinterWrapper(SessionPrinter sessionPrinter, StorePrinter storePrinter) {
        return new SessionPrinterWrapper(sessionPrinter, storePrinter);
    }

    public RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer, boolean isNew) {
        return new RetailStorePrinterWrapper(printer, isNew);
    }

    public ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupHierarchy hierarchy) {
        return new ProductGroupDetailLineItemWrapper(hierarchy);
    }

    public ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupItem item) {
        return new ProductGroupDetailLineItemWrapper(item);
    }

    public ProductGroupScheduleWrapper createProductGroupScheduleWrapper(ProductGroupScheduleVO scheduleVO, TimeZone timeZone) {
        return new ProductGroupScheduleWrapper(scheduleVO, timeZone);
    }

    public PurchaseOrderLineItemWrapper createPurchaseOrderLineItemWrapper(PurchaseOrderLineItem purchaseOrderLineItem) {
        return new PurchaseOrderLineItemWrapper(purchaseOrderLineItem);
    }

    public ReportFormatWrapper createReportFormatWrapper(List<StorePrinter> printers) {
        return new ReportFormatWrapper(printers);
    }

    public ReportFormatWrapper createReportFormatWrapper(ReportTypeFormat format, List<StorePrinter> printers, boolean isNew) {
        return new ReportFormatWrapper(format, printers, isNew);
    }

    public RPrinterDialogWrapper createRPrinterDialogWrapper(ReportTypeFormat format, List<StorePrinter> printers) {
        return new RPrinterDialogWrapper(format, printers);
    }

    public ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn) {
        return new ReturnLineItemWrapper(stockReturn);
    }

    public ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnReason defaultReason) {
        return new ReturnLineItemWrapper(stockReturn, defaultReason);
    }

    public ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnLineItem lineItem) {
        return new ReturnLineItemWrapper(stockReturn, lineItem);
    }

    public ReturnReasonWrapper createReturnReasonWrapper(ReturnReason returnReason) {
        return new ReturnReasonWrapper(returnReason);
    }

    public DataPermissionWrapper createDataPermissionWrapper(String name, Object value) {
        return new DataPermissionWrapper(name, value);
    }

    public RoleWrapper createRoleWrapper(Role role) {
        return new RoleWrapper(role);
    }

    public UserDetailWrapper createUserDetailWrapper(User user) {
        return new UserDetailWrapper(user);
    }

    public UserRoleAssignmentWrapper createUserRoleAssignmentWrapper(UserRoleAssignmentAction action, Role role, Store store, Date endDate) {
        return new UserRoleAssignmentWrapper(action, role, store, endDate);
    }

    public UserRoleWrapper createUserDetailWrapper(UserRole userRole, Role role, Store store) {
        return new UserRoleWrapper(userRole, role, store);
    }

    public UserStoreAssignmentWrapper createUserStoreAssignmentWrapper(UserStoreAssignmentAction action, Store store) {
        return new UserStoreAssignmentWrapper(action, store);
    }

    public UserWrapper createUserWrapper(User user) {
        return new UserWrapper(user);
    }

    public StockCountAuthorizeUinWrapper createStockCountAuthorizeUinWrapper(StockCountAuthorizeSerialNumber serialNumber, UINType uinType, String uinLabel) {
        return new StockCountAuthorizeUinWrapper(serialNumber, uinType, uinLabel);
    }

    public StockCountChildWrapper createStockCountChildWrapper(StockCountChild stockCountChild) {
        return new StockCountChildWrapper(stockCountChild);
    }

    public StockCountComponentDetailWrapper createStockCountComponentDetailWrapper(StockCountLineItemCompBreakdownVO breakdownVO) {
        return new StockCountComponentDetailWrapper(breakdownVO);
    }

    public StockCountLineItemAuthWrapper createStockCountLineItemAuthWrapper(StockCountLineItemAreaBreakdownVO lineItemVO) {
        return new StockCountLineItemAuthWrapper(lineItemVO);
    }

    public StockCountLineItemWrapper createStockCountLineItemAuthWrapper(StockCountLineItem lineItem, boolean authorizePhase) {
        return new StockCountLineItemWrapper(lineItem, authorizePhase);
    }

    public StockCountUinWrapper createStockCountUinWrapper() {
        return new StockCountUinWrapper();
    }

    public StockCountWrapper createStockCountWrapper(StockCount stockCount) {
        return new StockCountWrapper(stockCount);
    }

    public StoreOrderWrapper createStoreOrderWrapper(StoreOrder storeOrder) {
        return new StoreOrderWrapper(storeOrder);
    }

    public StoreOrderLineItemWrapper createStoreOrderLineItemWrapper(StoreOrderLineItem lineItem, StoreOrder storeOrder) {
        return new StoreOrderLineItemWrapper(lineItem, storeOrder);
    }

    public StoreSequenceWrapper createStoreSequenceWrapper(Long storeId) {
        return new StoreSequenceWrapper(storeId);
    }

    public StoreSequenceItemWrapper createStoreSequenceItemWrapper(StoreSequenceItem sequenceItem) {
        return new StoreSequenceItemWrapper(sequenceItem);
    }

    public StoreSequenceAreaWrapper createStoreSequenceAreaWrapper(StoreSequenceArea sequenceArea, StoreSequenceWrapper storeSequence) {
        return new StoreSequenceAreaWrapper(sequenceArea, storeSequence);
    }

    public CustomThemeWrapper createCustomThemeWrapper(CustomTheme customTheme) {
        return new CustomThemeWrapper(customTheme);
    }

    public FontWrapper createFontWrapper(String fontKey, CustomFont customFont) {
        return new FontWrapper(fontKey, customFont);
    }

    public FontWrapper createFontWrapper(String fontKey, FontUIResource fontResource) {
        return new FontWrapper(fontKey, fontResource);
    }

    public IconWrapper createIconWrapper(String iconKey, CustomIcon customIcon) {
        return new IconWrapper(iconKey, customIcon);
    }

    public IconWrapper createIconWrapper(String iconKey, IconUIResource iconResource) {
        return new IconWrapper(iconKey, iconResource);
    }

    public TransactionHistoryVOWrapper createTransactionHistoryVOWrapper(TransactionHistoryVO historyVO) {
        return new TransactionHistoryVOWrapper(historyVO);
    }

    public TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer) {
        return new TransferLineItemWrapper(transfer);
    }

    public TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem) {
        return new TransferLineItemWrapper(transfer, lineItem);
    }

    public TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem, boolean isViewOnly) {
        return new TransferLineItemWrapper(transfer, lineItem, isViewOnly);
    }

    public TranslationDetailWrapper createTranslationDetailWrapper(String key, String english, String value, String comment) {
        return new TranslationDetailWrapper(key, english, value, comment);
    }

    public SerialNumberWrapper createSerialNumberWrapper(FunctionalArea functionalArea, String itemId, UINType uinType, String uinLabel) {
        return new SerialNumberWrapper(functionalArea, itemId, uinType, uinLabel);
    }

    public WarehouseDeliveryCartonWrapper createWarehouseDeliveryCartonWrapper(WarehouseDeliveryCarton carton) {
        return new WarehouseDeliveryCartonWrapper(carton);
    }

    public WarehouseDeliveryFulfillmentOrderWrapper createWarehouseDeliveryFulfillmentOrderWrapper() {
        return new WarehouseDeliveryFulfillmentOrderWrapper();
    }

    public WarehouseDeliveryLineItemWrapper createWarehouseDeliveryLineItemWrapper(WarehouseDeliveryCarton carton) {
        return new WarehouseDeliveryLineItemWrapper(carton);
    }

    public ItemTicketLineItemWrapper createItemTicketLineItemWrapper(ItemTicket itemTicket) {
        return new ItemTicketLineItemWrapper(itemTicket);
    }
}
