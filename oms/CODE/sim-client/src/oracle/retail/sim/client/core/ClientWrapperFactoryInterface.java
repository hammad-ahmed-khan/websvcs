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
 * Interface that defines all the different ways to instantiate client-side wrappers objects in the system.
 * @see ClientWrapperFactory
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public interface ClientWrapperFactoryInterface {
    CartonLineItemWrapper createCartonLineItemWrapper(WarehouseDeliveryLineItem lineItem);

    DirectDeliveryLineItemWrapper createDirectDeliveryLineItemWrapper(DirectDelivery delivery);

    FulfillmentOrderMgmtListWrapper createFulfillmentOrderMgmtListWrapper(FulfillmentOrderMgmtListVO fulfillmentOrderMgmtListVO);

    FulfillmentOrderBinWrapper createFulfillmentOrderBinWrapper(FulfillmentOrderBin bin, boolean isEditable);

    FulfillmentOrderDeliveryWrapper createFulfillmentOrderDeliveryWrapper(FulfillmentOrderDeliveryVO fulfillmentOrderDeliveryVO);

    FulfillmentOrderDeliveryLineItemWrapper createFulfillmentOrderDeliveryLineItemWrapper(FulfillmentOrderDelivery delivery, FulfillmentOrderDeliveryLineItem lineItem,
            FulfillmentOrder fulfillmentOrder, FulfillmentOrderLineItem orderLineItem, BigDecimal uomConversionFactor);
    IMEIFulfillmentOrderDeliveryLineItemWrapper createIMEIFulfillmentOrderDeliveryLineItemWrapper(IMEIFulfillmentOrderDelivery orderDelivery);
    FulfillmentOrderPickLineItemWrapper createFulfillmentOrderPickLineItemWrapper(FulfillmentOrderPick pick, FulfillmentOrderPickLineItem lineItem, FulfillmentOrder fulfillmentOrder,
            ToleranceAdmin toleranceAdmin, BigDecimal uomConversionFactor);

    FulfillmentOrderPickWrapper createFulfillmentOrderPickWrapper(FulfillmentOrderPickVO pickVO);

    FulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(FulfillmentOrder fulfillmentOrder, FulfillmentOrderLineItem lineItem, BigDecimal uomConversionFactor);

    FulfillmentOrderReversePickLineItemWrapper createFulfillmentOrderReversePickLineItemWrapper(FulfillmentOrderReversePick reversePick, FulfillmentOrderReversePickLineItem reversePickLineItem);

    FulfillmentOrderReversePickWrapper createFulfillmentOrderReversePickWrapper(FulfillmentOrderReversePickVO reversePickVO);

    InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentLineItem lineItem);

    InventoryAdjustmentLineItemWrapper createInventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentReason defaultReason);

    InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template);

    InventoryTemplateLineItemWrapper createInventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template, InventoryAdjustmentTemplateLineItem lineItem);

    AllocationTransferWrapper createAllocationTransferWrapper(AllocationVO allocation, String uom);

    AllocationTransferWrapper createAllocationTransferWrapper(TransferAllocationVO allocation, String uom);

    NonSellableQuantityWrapper createNonSellableQuantityWrapper(NonSellableQtyType quantityType, Quantity quantity);

    PackComponentWrapper createPackComponentWrapper(StockItem componentItem);

    ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest);

    ItemRequestLineItemWrapper createItemRequestLineItemWrapper(ItemRequest itemRequest, ItemRequestLineItem lineItem);

    ItemSubstitutionWrapper createItemSubstitutionWrapper(FulfillmentOrderPickLineItem originalItem, RelatedItem relatedItem, BigDecimal uomConvFactor);

    ItemTicketWrapper createItemTicketWrapper(ItemTicket ticket);

    ShelfReplenishmentLineItemWrapper createItemTicketWrapper(ShelfReplenishment shelfReplenishment, ShelfReplenishmentLineItem lineItem);

    RelatedItemWrapper createRelatedItemWrapper(RelatedItem relatedItem);

    RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer);

    RetailStorePrinterWrapper createRetailStorePrinterWrapper(StorePrinter printer, boolean isNew);

    SessionPrinterWrapper createSessionPrinterWrapper(SessionPrinter sessionPrinter, StorePrinter storePrinter);

    ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupHierarchy hierarchy);

    ProductGroupDetailLineItemWrapper createProductGroupDetailLineItemWrapper(ProductGroupItem item);

    ProductGroupScheduleWrapper createProductGroupScheduleWrapper(ProductGroupScheduleVO scheduleVO, TimeZone timeZone);

    PurchaseOrderLineItemWrapper createPurchaseOrderLineItemWrapper(PurchaseOrderLineItem purchaseOrderLineItem);

    ReportFormatWrapper createReportFormatWrapper(List<StorePrinter> printers);

    ReportFormatWrapper createReportFormatWrapper(ReportTypeFormat format, List<StorePrinter> printers, boolean isNew);

    RPrinterDialogWrapper createRPrinterDialogWrapper(ReportTypeFormat format, List<StorePrinter> printers);

    ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn);

    ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnReason defaultReason);

    ReturnLineItemWrapper createReturnLineItemWrapper(Return stockReturn, ReturnLineItem returnLineItem);

    ReturnReasonWrapper createReturnReasonWrapper(ReturnReason returnReason);

    DataPermissionWrapper createDataPermissionWrapper(String name, Object value);

    RoleWrapper createRoleWrapper(Role role);

    UserDetailWrapper createUserDetailWrapper(User user);

    UserRoleAssignmentWrapper createUserRoleAssignmentWrapper(UserRoleAssignmentAction action, Role role, Store store, Date endDate);

    UserRoleWrapper createUserDetailWrapper(UserRole userRole, Role role, Store store);

    UserStoreAssignmentWrapper createUserStoreAssignmentWrapper(UserStoreAssignmentAction action, Store store);

    UserWrapper createUserWrapper(User user);

    StockCountAuthorizeUinWrapper createStockCountAuthorizeUinWrapper(StockCountAuthorizeSerialNumber vo, UINType uinType, String uinLabel);

    StockCountChildWrapper createStockCountChildWrapper(StockCountChild stockCountChild);

    StockCountComponentDetailWrapper createStockCountComponentDetailWrapper(StockCountLineItemCompBreakdownVO breakdownVO);

    StockCountLineItemAuthWrapper createStockCountLineItemAuthWrapper(StockCountLineItemAreaBreakdownVO lineItemVO);

    StockCountLineItemWrapper createStockCountLineItemAuthWrapper(StockCountLineItem lineItem, boolean authorizePhase);

    StockCountUinWrapper createStockCountUinWrapper();

    StockCountWrapper createStockCountWrapper(StockCount stockCount);

    StoreOrderWrapper createStoreOrderWrapper(StoreOrder storeOrder);

    StoreOrderLineItemWrapper createStoreOrderLineItemWrapper(StoreOrderLineItem lineItem, StoreOrder storeOrder);

    StoreSequenceWrapper createStoreSequenceWrapper(Long storeId);

    StoreSequenceItemWrapper createStoreSequenceItemWrapper(StoreSequenceItem sequenceItem);

    StoreSequenceAreaWrapper createStoreSequenceAreaWrapper(StoreSequenceArea sequenceArea, StoreSequenceWrapper storeSequence);

    CustomThemeWrapper createCustomThemeWrapper(CustomTheme customTheme);

    FontWrapper createFontWrapper(String fontKey, CustomFont customFont);

    FontWrapper createFontWrapper(String fontKey, FontUIResource fontResource);

    IconWrapper createIconWrapper(String iconKey, CustomIcon customIcon);

    IconWrapper createIconWrapper(String iconKey, IconUIResource iconResource);

    TransactionHistoryVOWrapper createTransactionHistoryVOWrapper(TransactionHistoryVO historyVO);

    TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer);

    TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem);

    TransferLineItemWrapper createTransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem, boolean isViewOnly);

    TranslationDetailWrapper createTranslationDetailWrapper(String key, String english, String value, String comment);

    SerialNumberWrapper createSerialNumberWrapper(FunctionalArea functionalArea, String itemId, UINType uinType, String uinLabel);

    WarehouseDeliveryCartonWrapper createWarehouseDeliveryCartonWrapper(WarehouseDeliveryCarton carton);

    WarehouseDeliveryFulfillmentOrderWrapper createWarehouseDeliveryFulfillmentOrderWrapper();

    WarehouseDeliveryLineItemWrapper createWarehouseDeliveryLineItemWrapper(WarehouseDeliveryCarton carton);

    ItemTicketLineItemWrapper createItemTicketLineItemWrapper(ItemTicket itemTicket);

}
