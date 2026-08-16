package oracle.retail.sim.client.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;

import extra.retail.sim.client.screen.imeifulfillmentorderdelivery.IMEIFulfillmentOrderDeliveryLineItemWrapper;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.IMEIFulfillmentOrderDelivery;
import oracle.retail.sim.client.application.Application;
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
 * Utility class to create business objects. This class has static methods that delegate to an
 * implementation of WrapperFactoryInterface. The implementation to be loaded is defined in
 * common.cfg. All creation of new business objects should be handled by this class.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ClientWrapperFactory {
    public static final String GUI_WRAPPER_FACTORY = "GUI.WRAPPER_FACTORY";

    private static ClientWrapperFactoryInterface factory = getDefaultFactory();

    private static ClientWrapperFactoryInterface getDefaultFactory() {
        return Application.getConfigManager().getObject(GUI_WRAPPER_FACTORY, ClientWrapperFactoryInterface.class);
    }

    private ClientWrapperFactory() {
    }

    public static CartonLineItemWrapper createCartonLineItemWrapper(WarehouseDeliveryLineItem lineItem) {
        return factory.createCartonLineItemWrapper(lineItem);
    }

    public static FulfillmentOrderMgmtListWrapper createFulfillmentOrderMgmtListWrapper(FulfillmentOrderMgmtListVO fulfillmentOrderMgmtListVO) {
        return factory.createFulfillmentOrderMgmtListWrapper(fulfillmentOrderMgmtListVO);
    }

    public static DirectDeliveryLineItemWrapper createDirectDeliveryLineItemWrapper(DirectDelivery delivery) {
        return factory.createDirectDeliveryLineItemWrapper(delivery);
    }

    public static FulfillmentOrderBinWrapper createFulfillmentOrderBinWrapper(FulfillmentOrderBin bin, boolean isEditable) {
        return factory.createFulfillmentOrderBinWrapper(bin, isEditable);
    }

    public static FulfillmentOrderDeliveryWrapper createFulfillmentOrderDeliveryWrapper(FulfillmentOrderDeliveryVO customerOrderDeliveryVO) {
        return factory.createFulfillmentOrderDeliveryWrapper(customerOrderDeliveryVO);
    }

    public static FulfillmentOrderDeliveryLineItemWrapper createFulfillmentOrderDeliveryLineItemWrapper(FulfillmentOrderDelivery delivery, FulfillmentOrderDeliveryLineItem lineItem,
            FulfillmentOrder fulfillmentOrder, FulfillmentOrderLineItem orderLineItem, BigDecimal uomConversionFactor) {
        return factory.createFulfillmentOrderDeliveryLineItemWrapper(delivery, lineItem, fulfillmentOrder, orderLineItem, uomConversionFactor);
    }
    public static IMEIFulfillmentOrderDeliveryLineItemWrapper createIMEIFulfillmentOrderDeliveryLineItemWrapper(IMEIFulfillmentOrderDelivery orderDelivery) {
        return factory.createIMEIFulfillmentOrderDeliveryLineItemWrapper(orderDelivery);
    }

    public static FulfillmentOrderPickLineItemWrapper createFulfillmentOrderPickLineItemWrapper(FulfillmentOrderPick pick, FulfillmentOrderPickLineItem lineItem, FulfillmentOrder fulfillmentOrder,
            ToleranceAdmin toleranceAdmin, BigDecimal uomConversionFactor) {
        return factory.createFulfillmentOrderPickLineItemWrapper(pick, lineItem, fulfillmentOrder, toleranceAdmin, uomConversionFactor);
    }

    public static FulfillmentOrderPickWrapper createFulfillmentOrderPickWrapper(FulfillmentOrderPickVO pickVO) {
        return factory.createFulfillmentOrderPickWrapper(pickVO);
    }

    public static FulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(FulfillmentOrder order, FulfillmentOrderLineItem lineItem, BigDecimal uomConversionFactor) {
        return factory.createFulfillmentOrderLineItemWrapper(order, lineItem, uomConversionFactor);
    }

    public static FulfillmentOrderReversePickWrapper createFulfillmentOrderReversePickWrapper(FulfillmentOrderReversePickVO reversePickVO) {
        return factory.createFulfillmentOrderReversePickWrapper(reversePickVO);
    }

    public static FulfillmentOrderReversePickLineItemWrapper createFulfillmentOrderReversePickLineItemWrapper(FulfillmentOrderReversePick reversePick,
            FulfillmentOrderReversePickLineItem reversePickLineItem) {
        return factory.createFulfillmentOrderReversePickLineItemWrapper(reversePick, reversePickLineItem);
    }

    public static InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentLineItem lineItem) {
        return factory.createInventoryAdjustmentLineItemWrapper(adjustment, lineItem);
    }

    public static InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentReason defaultReason) {
        return factory.createInventoryAdjustmentLineItemWrapper(adjustment, defaultReason);
    }

    public static InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template) {
        return factory.createInventoryTemplateLineItemWrapper(template);
    }

    public static InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template, InventoryAdjustmentTemplateLineItem lineItem) {
        return factory.createInventoryTemplateLineItemWrapper(template, lineItem);
    }

    public static AllocationTransferWrapper createAllocationTransferWrapper(AllocationVO allocation, String uom) {
        return factory.createAllocationTransferWrapper(allocation, uom);
    }

    public static AllocationTransferWrapper createAllocationTransferWrapper(TransferAllocationVO allocation, String uom) {
        return factory.createAllocationTransferWrapper(allocation, uom);
    }

    public static NonSellableQuantityWrapper createNonSellableQuantityWrapper(NonSellableQtyType quantityType, Quantity quantity) {
        return factory.createNonSellableQuantityWrapper(quantityType, quantity);
    }

    public static PackComponentWrapper createPackComponentWrapper(StockItem componentItem) {
        return factory.createPackComponentWrapper(componentItem);
    }

    public static ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest) {
        return factory.createItemRequestLineItemWrapper(itemRequest);
    }

    public static ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest, ItemRequestLineItem lineItem) {
        return factory.createItemRequestLineItemWrapper(itemRequest, lineItem);
    }

    public static ItemSubstitutionWrapper createItemSubstitutionWrapper(FulfillmentOrderPickLineItem originalItem, RelatedItem relatedItem, BigDecimal uomConvFactor) {
        return factory.createItemSubstitutionWrapper(originalItem, relatedItem, uomConvFactor);
    }

    public static ItemTicketWrapper createItemTicketWrapper(ItemTicket ticket) {
        return factory.createItemTicketWrapper(ticket);
    }

    public static RelatedItemWrapper createRelatedItemWrapper(RelatedItem relatedItem) {
        return factory.createRelatedItemWrapper(relatedItem);
    }

    public static RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer) {
        return factory.createRetailStorePrinterWrapper(printer);
    }

    public static RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer, boolean isNew) {
        return factory.createRetailStorePrinterWrapper(printer, isNew);
    }

    public static SessionPrinterWrapper createSessionPrinterWrapper(SessionPrinter sessionPrinter, StorePrinter storePrinter) {
        return factory.createSessionPrinterWrapper(sessionPrinter, storePrinter);
    }

    public static ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupHierarchy hierarchy) {
        return factory.createProductGroupDetailLineItemWrapper(hierarchy);
    }

    public static ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupItem item) {
        return factory.createProductGroupDetailLineItemWrapper(item);
    }

    public static ProductGroupScheduleWrapper createProductGroupScheduleWrapper(ProductGroupScheduleVO scheduleVO, TimeZone timeZone) {
        return factory.createProductGroupScheduleWrapper(scheduleVO, timeZone);
    }

    public static PurchaseOrderLineItemWrapper createPurchaseOrderLineItemWrapper(PurchaseOrderLineItem purchaseOrderLineItem) {
        return factory.createPurchaseOrderLineItemWrapper(purchaseOrderLineItem);
    }

    public static ReportFormatWrapper createReportFormatWrapper(List<StorePrinter> printers) {
        return factory.createReportFormatWrapper(printers);
    }

    public static ReportFormatWrapper createReportFormatWrapper(ReportTypeFormat format, List<StorePrinter> printers, boolean isNew) {
        return factory.createReportFormatWrapper(format, printers, isNew);
    }

    public static RPrinterDialogWrapper createRPrinterDialogWrapper(ReportTypeFormat format, List<StorePrinter> printers) {
        return factory.createRPrinterDialogWrapper(format, printers);
    }

    public static ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn) {
        return factory.createReturnLineItemWrapper(stockReturn);
    }

    public static ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnReason defaultReason) {
        return factory.createReturnLineItemWrapper(stockReturn, defaultReason);
    }

    public static ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnLineItem returnLineItem) {
        return factory.createReturnLineItemWrapper(stockReturn, returnLineItem);
    }

    public static ReturnReasonWrapper createReturnReasonWrapper(ReturnReason returnReason) {
        return factory.createReturnReasonWrapper(returnReason);
    }

    public static DataPermissionWrapper createDataPermissionWrapper(String name, Object value) {
        return factory.createDataPermissionWrapper(name, value);
    }

    public static RoleWrapper createRoleWrapper(Role role) {
        return factory.createRoleWrapper(role);
    }

    public static UserDetailWrapper createUserDetailWrapper(User user) {
        return factory.createUserDetailWrapper(user);
    }

    public static UserRoleAssignmentWrapper createUserRoleAssignmentWrapper(UserRoleAssignmentAction action, Role role, Store store, Date endDate) {
        return factory.createUserRoleAssignmentWrapper(action, role, store, endDate);
    }

    public static UserRoleWrapper createUserRoleWrapper(UserRole userRole, Role role, Store store) {
        return factory.createUserDetailWrapper(userRole, role, store);
    }

    public static UserStoreAssignmentWrapper createUserStoreAssignmentWrapper(UserStoreAssignmentAction action, Store store) {
        return factory.createUserStoreAssignmentWrapper(action, store);
    }

    public static UserWrapper createUserWrapper(User user) {
        return factory.createUserWrapper(user);
    }

    public static ShelfReplenishmentLineItemWrapper createShelfReplenishmentLineItemWrapper(ShelfReplenishment shelfReplenishment, ShelfReplenishmentLineItem lineItem) {
        return factory.createItemTicketWrapper(shelfReplenishment, lineItem);
    }

    public static StockCountAuthorizeUinWrapper createStockCountAuthorizeUinWrapper(StockCountAuthorizeSerialNumber vo, UINType uinType, String uinLabel) {
        return factory.createStockCountAuthorizeUinWrapper(vo, uinType, uinLabel);
    }

    public static StockCountChildWrapper createStockCountChildWrapper(StockCountChild stockCountChild) {
        return factory.createStockCountChildWrapper(stockCountChild);
    }

    public static StockCountComponentDetailWrapper createStockCountComponentDetailWrapper(StockCountLineItemCompBreakdownVO breakdownVO) {
        return factory.createStockCountComponentDetailWrapper(breakdownVO);
    }

    public static StockCountLineItemAuthWrapper createStockCountLineItemAuthWrapper(StockCountLineItemAreaBreakdownVO lineItemVO) {
        return factory.createStockCountLineItemAuthWrapper(lineItemVO);
    }

    public static StockCountLineItemWrapper createStockCountLineItemWrapper(StockCountLineItem lineItem, boolean authorizePhase) {
        return factory.createStockCountLineItemAuthWrapper(lineItem, authorizePhase);
    }

    public static StockCountUinWrapper createStockCountUinWrapper() {
        return factory.createStockCountUinWrapper();
    }

    public static StockCountWrapper createStockCountWrapper(StockCount stockCount) {
        return factory.createStockCountWrapper(stockCount);
    }

    public static StoreOrderWrapper createStoreOrderWrapper(StoreOrder storeOrder) {
        return factory.createStoreOrderWrapper(storeOrder);
    }

    public static StoreOrderLineItemWrapper createStoreOrderLineItemWrapper(StoreOrderLineItem lineItem, StoreOrder storeOrder) {
        return factory.createStoreOrderLineItemWrapper(lineItem, storeOrder);
    }

    public static StoreSequenceWrapper createStoreSequenceWrapper(Long storeId) {
        return factory.createStoreSequenceWrapper(storeId);
    }

    public static StoreSequenceItemWrapper createStoreSequenceItemWrapper(StoreSequenceItem sequenceItem) {
        return factory.createStoreSequenceItemWrapper(sequenceItem);
    }

    public static StoreSequenceAreaWrapper createStoreSequenceAreaWrapper(StoreSequenceArea sequenceArea, StoreSequenceWrapper storeSequence) {
        return factory.createStoreSequenceAreaWrapper(sequenceArea, storeSequence);
    }

    public static CustomThemeWrapper createCustomThemeWrapper(CustomTheme customTheme) {
        return factory.createCustomThemeWrapper(customTheme);
    }

    public static FontWrapper createFontWrapper(String fontKey, CustomFont customFont) {
        return factory.createFontWrapper(fontKey, customFont);
    }

    public static FontWrapper createFontWrapper(String fontKey, FontUIResource fontResource) {
        return factory.createFontWrapper(fontKey, fontResource);
    }

    public static IconWrapper createIconWrapper(String iconKey, CustomIcon customIcon) {
        return factory.createIconWrapper(iconKey, customIcon);
    }

    public static IconWrapper createIconWrapper(String iconKey, IconUIResource iconResource) {
        return factory.createIconWrapper(iconKey, iconResource);
    }

    public static TransactionHistoryVOWrapper createTransactionHistoryVOWrapper(TransactionHistoryVO historyVO) {
        return factory.createTransactionHistoryVOWrapper(historyVO);
    }

    public static TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer) {
        return factory.createTransferLineItemWrapper(transfer);
    }

    public static TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem) {
        return factory.createTransferLineItemWrapper(transfer, lineItem);
    }

    public static TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem, boolean isViewOnly) {
        return factory.createTransferLineItemWrapper(transfer, lineItem, isViewOnly);
    }

    public static TranslationDetailWrapper createTranslationDetailWrapper(String key, String english, String value, String comment) {
        return factory.createTranslationDetailWrapper(key, english, value, comment);
    }

    public static SerialNumberWrapper createSerialNumberWrapper(FunctionalArea functionalArea, String itemId, UINType uinType, String uinLabel) {
        return factory.createSerialNumberWrapper(functionalArea, itemId, uinType, uinLabel);
    }

    public static WarehouseDeliveryCartonWrapper createWarehouseDeliveryCartonWrapper(WarehouseDeliveryCarton carton) {
        return factory.createWarehouseDeliveryCartonWrapper(carton);
    }

    public static WarehouseDeliveryFulfillmentOrderWrapper createWarehouseDeliveryFulfillmentOrderWrapper() {
        return factory.createWarehouseDeliveryFulfillmentOrderWrapper();
    }

    public static WarehouseDeliveryLineItemWrapper createWarehouseDeliveryLineItemWrapper(WarehouseDeliveryCarton carton) {
        return factory.createWarehouseDeliveryLineItemWrapper(carton);
    }

    public static ItemTicketLineItemWrapper createItemTicketLineItemWrapper(ItemTicket itemTicket) {
        return factory.createItemTicketLineItemWrapper(itemTicket);
    }
}
