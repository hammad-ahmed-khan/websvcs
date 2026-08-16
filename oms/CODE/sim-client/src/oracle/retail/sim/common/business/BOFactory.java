package oracle.retail.sim.common.business;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.activityhistory.ActivityHistoryVO;
import oracle.retail.sim.common.activityhistory.ActivityType;
import oracle.retail.sim.common.batch.BatchImport;
import oracle.retail.sim.common.batch.ConfigBatchImpExp;
import oracle.retail.sim.common.config.CodeInfo;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.deals.Deal;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.diffs.Differentiator;
import oracle.retail.sim.common.diffs.DifferentiatorType;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCarton;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCartonVO;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCompositeLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliverySimpleLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.directdelivery.PurchaseOrderQueryFilter;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;
import oracle.retail.sim.common.fulfillmentorder.CustomerAddress;
import oracle.retail.sim.common.fulfillmentorder.CustomerAddressType;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderContactInfo;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderCreateCustomerVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderCreateLineItemVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderCreateVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryQueryFilter;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPossiblePickVO;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItem;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentVO;
import oracle.retail.sim.common.item.AllocationVO;
import oracle.retail.sim.common.item.BarcodeInfo;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.item.ItemAvailableStockVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemDiffVO;
import oracle.retail.sim.common.item.ItemImageInfo;
import oracle.retail.sim.common.item.ItemInventoryStockVO;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOByDiffQueryFilter;
import oracle.retail.sim.common.item.ItemVOByFinisherQueryFilter;
import oracle.retail.sim.common.item.ItemVOByInventoryQueryFilter;
import oracle.retail.sim.common.item.ItemVOByItemQueryFilter;
import oracle.retail.sim.common.item.ItemVOBySupplierQueryFilter;
import oracle.retail.sim.common.item.ItemVOByUDAQueryFilter;
import oracle.retail.sim.common.item.ItemVOByWarehouseQueryFilter;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.PackHeaderVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.SaleItem;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.item.StockInfo;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.StoreItemStockVO;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.itembasket.ItemBasket;
import oracle.retail.sim.common.itembasket.ItemBasketLineItem;
import oracle.retail.sim.common.itembasket.ItemBasketType;
import oracle.retail.sim.common.itemprice.FuturePriceVO;
import oracle.retail.sim.common.itemprice.ItemCurrentPriceVO;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemprice.ItemPriceQueryFilter;
import oracle.retail.sim.common.itemprice.ItemPriceVO;
import oracle.retail.sim.common.itemprice.PriceChange;
import oracle.retail.sim.common.itemprice.PriceInfo;
import oracle.retail.sim.common.itemprice.PromotionQueryFilter;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestLineItem;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestVO;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.manifest.ManifestOpenShipmentQueryFilter;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;
import oracle.retail.sim.common.mps.MpsStagedMessageVO;
import oracle.retail.sim.common.mps.MpsWorkerType;
import oracle.retail.sim.common.mps.MpsWorkerTypeVO;
import oracle.retail.sim.common.notes.Note;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.FinisherContactInfo;
import oracle.retail.sim.common.person.PostalAddress;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.postransaction.POSTransaction;
import oracle.retail.sim.common.productgroup.BatchGroupVO;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.productgroup.ShelfReplenishmentGroupVO;
import oracle.retail.sim.common.productgroup.StockCountGroupVO;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.common.reportrequest.DirectDeliveryReportRequest;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderBinReportRequest;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderDeliveryReportRequest;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderPickReportRequest;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderReportRequest;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderReversePickReportRequest;
import oracle.retail.sim.common.reportrequest.InventoryAdjustmentReportRequest;
import oracle.retail.sim.common.reportrequest.ItemBasketReportRequest;
import oracle.retail.sim.common.reportrequest.ItemReportRequest;
import oracle.retail.sim.common.reportrequest.ItemRequestReportRequest;
import oracle.retail.sim.common.reportrequest.ItemTicketReportRequest;
import oracle.retail.sim.common.reportrequest.ReturnReportRequest;
import oracle.retail.sim.common.reportrequest.ShelfReplenishmentReportRequest;
import oracle.retail.sim.common.reportrequest.StockCountRejectedItemReportRequest;
import oracle.retail.sim.common.reportrequest.StockCountReportRequest;
import oracle.retail.sim.common.reportrequest.StoreOrderReportRequest;
import oracle.retail.sim.common.reportrequest.TransferReportRequest;
import oracle.retail.sim.common.reportrequest.UinReportRequest;
import oracle.retail.sim.common.reportrequest.WarehouseDeliveryReportRequest;
import oracle.retail.sim.common.schedule.DailyByWeekdaySchedule;
import oracle.retail.sim.common.schedule.DailySchedule;
import oracle.retail.sim.common.schedule.MonthlyByDaySchedule;
import oracle.retail.sim.common.schedule.MonthlyByWeekSchedule;
import oracle.retail.sim.common.schedule.ProductGroupBatchVO;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;
import oracle.retail.sim.common.schedule.WeeklySchedule;
import oracle.retail.sim.common.schedule.YearlyByDaySchedule;
import oracle.retail.sim.common.schedule.YearlyByWeekSchedule;
import oracle.retail.sim.common.security.PasswordConfiguration;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.PermissionGroupQueryFilter;
import oracle.retail.sim.common.security.PermissionQueryFilter;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserLoginVO;
import oracle.retail.sim.common.security.UserPassword;
import oracle.retail.sim.common.security.UserPasswordGenerator;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserRolesSaveVO;
import oracle.retail.sim.common.security.UserSaveVO;
import oracle.retail.sim.common.security.UserStore;
import oracle.retail.sim.common.security.UserStoresSaveVO;
import oracle.retail.sim.common.security.UsernameGenerator;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentLineItem;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;
import oracle.retail.sim.common.shipment.BillOfLading;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.shipment.ShipmentWeightUom;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.FinisherStatus;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierStatus;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseDetailVO;
import oracle.retail.sim.common.source.WarehouseVO;
import oracle.retail.sim.common.stockcount.FutureCountQueryFilter;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountImport;
import oracle.retail.sim.common.stockcount.StockCountImportExtract;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnLineItem;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnVO;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.storeorder.ItemSale;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceVO;
import oracle.retail.sim.common.tranhistory.TransactionHistoryQueryFilter;
import oracle.retail.sim.common.tranhistory.TransactionHistoryVO;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferAllocationVO;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.transfer.TransferVO;
import oracle.retail.sim.common.uda.ItemUDA;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAValue;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINAttributeImport;
import oracle.retail.sim.common.uin.UINDetail;
import oracle.retail.sim.common.uin.UINDetailLookupQueryFilter;
import oracle.retail.sim.common.uin.UINDetailVO;
import oracle.retail.sim.common.uin.UINHistoryVO;
import oracle.retail.sim.common.uin.UINProblemDetail;
import oracle.retail.sim.common.uin.UINProblemDetailQueryFilter;
import oracle.retail.sim.common.uin.UINSelectVO;
import oracle.retail.sim.common.uin.UINStoreDept;
import oracle.retail.sim.common.uin.UINStoreItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCompositeLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQuickVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliverySimpleLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;

public class BOFactory {
  private static BOFactoryInterface factory = getDefaultFactory();
  
  private static BOFactoryInterface getDefaultFactory() {
    return CommonConfigManager.getBOFactoryImpl();
  }
  
  public static ActivityHistoryVO createActivityHistoryVO(ActivityType paramActivityType, Long paramLong, String paramString, DeviceType paramDeviceType) {
    return factory.createActivityHistoryVO(paramActivityType, paramLong, paramString, paramDeviceType);
  }
  
  public static AllocationVO createAllocationVO() {
    return factory.createAllocationVO();
  }
  
  public static BarcodeInfo createBarcodeInfo(String paramString) {
    return factory.createBarcodeInfo(paramString);
  }
  
  public static BarcodeItem createBarcodeItem(StockItem paramStockItem) {
    return factory.createBarcodeItem(paramStockItem);
  }
  
  public static BatchGroupVO createBatchGroupVO() {
    return factory.createBatchGroupVO();
  }
  
  public static ConfigBatchImpExp createBatchImportConfig() {
    return factory.createBatchImportConfig();
  }
  
  public static BatchImport createBatchImportExport() {
    return factory.createBatchImportExport();
  }
  
  public static BillOfLading createBillOfLading() {
    return factory.createBillOfLading();
  }
  
  public static BillOfLadingMotive createBillOfLadingMotive(String paramString1, String paramString2) {
    return factory.createBillOfLadingMotive(paramString1, paramString2);
  }
  
  public static BuddyStore createBuddyStore(Long paramLong, String paramString) {
    return factory.createBuddyStore(paramLong, paramString);
  }
  
  public static BuddyStore createBuddyStore(Store paramStore) {
    return factory.createBuddyStore(paramStore.getId(), paramStore.getName());
  }
  
  public static CodeInfo createCodeInfo(String paramString) {
    return factory.createCodeInfo(paramString);
  }
  
  public static ConfigurationOption createConfigurationOption(String paramString) {
    return factory.createConfigurationOption(paramString);
  }
  
  public static ContactInfo createContactInfo() {
    return factory.createContactInfo();
  }
  
  public static ContextType createContextType() {
    return factory.createContextType();
  }
  
  public static CustomerAddress createCustomerAddress(CustomerAddressType paramCustomerAddressType) {
    return factory.createCustomerAddress(paramCustomerAddressType);
  }
  
  public static CustomTheme createCustomTheme() {
    return factory.createCustomTheme();
  }
  
  public static DailySchedule createDailySchedule() {
    return factory.createDailySchedule();
  }
  
  public static DailySchedule createDailySchedule(Integer paramInteger) {
    return factory.createDailySchedule(paramInteger);
  }
  
  public static DailyByWeekdaySchedule createDailyByWeekdaySchedule() {
    return factory.createDailyByWeekdaySchedule();
  }
  
  public static Deal createDeal() {
    return factory.createDeal();
  }
  
  public static Differentiator createDifferentiator() {
    return factory.createDifferentiator();
  }
  
  public static DifferentiatorType createDiffType() {
    return factory.createDiffType();
  }
  
  public static DirectDelivery createDirectDelivery() {
    return factory.createDirectDelivery();
  }
  
  public static DirectDeliveryCarton createDirectDeliveryCarton() {
    return factory.createDirectDeliveryCarton();
  }
  
  public static DirectDeliveryCartonVO createDirectDeliveryCartonVO() {
    return factory.createDirectDeliveryCartonVO();
  }
  
  public static DirectDeliveryCompositeLineItem createDirectDeliveryCompositeLineItem(DirectDeliverySimpleLineItem[] paramArrayOfDirectDeliverySimpleLineItem) {
    return factory.createDirectDeliveryCompositeLineItem(paramArrayOfDirectDeliverySimpleLineItem);
  }
  
  public static DirectDeliveryQueryFilter createDirectDeliveryQueryFilter() {
    return factory.createDirectDeliveryQueryFilter();
  }
  
  public static DirectDeliverySimpleLineItem createDirectDeliverySimpleLineItem(StockItem paramStockItem) {
    return factory.createDirectDeliverySimpleLineItem(paramStockItem);
  }
  
  public static DirectDeliveryVO createDirectDeliveryVO() {
    return factory.createDirectDeliveryVO();
  }
  
  public static FutureCountQueryFilter createFutureCountQueryFilter() {
    return factory.createFutureCountQueryFilter();
  }
  
  public static Finisher createFinisher(String paramString1, String paramString2) {
    return factory.createFinisher(paramString1, paramString2);
  }
  
  public static FinisherContactInfo createFinisherContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo) {
    return factory.createFinisherContactInfo(paramAddressType, paramContactInfo);
  }
  
  public static FinisherVO createFinisherVO(String paramString1, String paramString2, FinisherStatus paramFinisherStatus) {
    return factory.createFinisherVO(paramString1, paramString2, paramFinisherStatus);
  }
  
  public static FulfillmentOrderMgmtQueryFilter createFulfillmentOrderMgmtQueryFilter() {
    return factory.createFulfillmentOrderMgmtQueryFilter();
  }
  
  public static FulfillmentOrder createFulfillmentOrder() {
    return factory.createFulfillmentOrder();
  }
  
  public static FulfillmentOrderBin createFulfillmentOrderBin() {
    return factory.createFulfillmentOrderBin();
  }
  
  public static FulfillmentOrderContactInfo createFulfillmentOrderContactInfo() {
    return factory.createFulfillmentOrderContactInfo();
  }
  
  public static FulfillmentOrderCreateCustomerVO createFulfillmentOrderCreateCustomerVO() {
    return factory.createFulfillmentOrderCreateCustomerVO();
  }
  
  public static FulfillmentOrderCreateLineItemVO createFulfillmentOrderCreateLineItemVO() {
    return factory.createFulfillmentOrderCreateLineItemVO();
  }
  
  public static FulfillmentOrderCreateVO createFulfillmentOrderCreateVO() {
    return factory.createFulfillmentOrderCreateVO();
  }
  
  public static FulfillmentOrderDelivery createFulfillmentOrderDelivery() {
    return factory.createFulfillmentOrderDelivery();
  }
  
  public static FulfillmentOrderDeliveryVO createFulfillmentOrderDeliveryVO() {
    return factory.createFulfillmentOrderDeliveryVO();
  }
  
  public static FulfillmentOrderDeliveryLineItem createFulfillmentOrderDeliveryLineItem() {
    return factory.createFulfillmentOrderDeliveryLineItem();
  }
  
  public static FulfillmentOrderDeliveryQueryFilter createFulfillmentOrderDeliveryQueryFilter() {
    return factory.createFulfillmentOrderDeliveryQueryFilter();
  }
  
  public static FulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem) {
    return factory.createFulfillmentOrderLineItem(paramStockItem);
  }
  
  public static FulfillmentOrderMgmtListVO createFulfillmentOrderMgmtListVO() {
    return factory.createFulfillmentOrderMgmtListVO();
  }
  
  public static FulfillmentOrderPick createFulfillmentOrderPick() {
    return factory.createFulfillmentOrderPick();
  }
  
  public static FulfillmentOrderPickLineItem createFulfillmentOrderPickLineItem(StockItem paramStockItem) {
    return factory.createFulfillmentOrderPickLineItem(paramStockItem);
  }
  
  public static FulfillmentOrderPickQueryFilter createFulfillmentOrderPickQueryFilter() {
    return factory.createFulfillmentOrderPickQueryFilter();
  }
  
  public static FulfillmentOrderPickVO createFulfillmentOrderPickVO() {
    return factory.createFulfillmentOrderPickVO();
  }
  
  public static FulfillmentOrderPossiblePickVO createFulfillmentOrderPossiblePickVO() {
    return factory.createFulfillmentOrderPossiblePickVO();
  }
  
  public static FulfillmentOrderReversePick createFulfillmentOrderReversePick() {
    return factory.createFulfillmentOrderReversePick();
  }
  
  public static FulfillmentOrderReversePickLineItem createFulfillmentOrderReversePickLineItem() {
    return factory.createFulfillmentOrderReversePickLineItem();
  }
  
  public static FulfillmentOrderReversePickVO createFulfillmentOrderReversePickVO() {
    return factory.createFulfillmentOrderReversePickVO();
  }
  
  public static FulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter() {
    return factory.createFulfillmentOrderQueryFilter();
  }
  
  public static FulfillmentOrderVO createFulfillmentOrderVO() {
    return factory.createFulfillmentOrderVO();
  }
  
  public static FuturePriceVO createFuturePriceVO() {
    return factory.createFuturePriceVO();
  }
  
  public static InventoryAdjustment createInventoryAdjustment() {
    return factory.createInventoryAdjustment();
  }
  
  public static InventoryAdjustmentLineItem createInventoryAdjustmentLineItem(StockItem paramStockItem) {
    return factory.createInventoryAdjustmentLineItem(paramStockItem);
  }
  
  public static InventoryAdjustmentQueryFilter createInventoryAdjustmentQueryFilter() {
    return factory.createInventoryAdjustmentQueryFilter();
  }
  
  public static InventoryAdjustmentVO createInventoryAdjustmentVO() {
    return factory.createInventoryAdjustmentVO();
  }
  
  public static InventoryAdjustmentReason createInventoryAdjustmentReason() {
    return factory.createInventoryAdjustmentReason();
  }
  
  public static InventoryAdjustmentTemplate createInventoryAdjustmentTemplate() {
    return factory.createInventoryAdjustmentTemplate();
  }
  
  public static InventoryAdjustmentTemplateLineItem createInventoryAdjustmentTemplateLineItem(StockItem paramStockItem) {
    return factory.createInventoryAdjustmentTemplateLineItem(paramStockItem);
  }
  
  public static InventoryAdjustmentTemplateQueryFilter createInventoryAdjustmentTemplateQueryFilter() {
    return factory.createInventoryAdjustmentTemplateQueryFilter();
  }
  
  public static InventoryAdjustmentTemplateVO createInventoryAdjustmentTemplateVO() {
    return factory.createInventoryAdjustmentTemplateVO();
  }
  
  public static Item createItem(String paramString) {
    return factory.createItem(paramString);
  }
  
  public static StoreItemStockVO createItemAvailableBuddyStockVO() {
    return factory.createItemAvailableBuddyStockVO();
  }
  
  public static ItemAvailableStockVO createItemAvailableStockVO() {
    return factory.createItemAvailableStockVO();
  }
  
  public static ItemBasket createItemBasket() {
    return factory.createItemBasket();
  }
  
  public static ItemBasketLineItem createItemBasketLineItem() {
    return factory.createItemBasketLineItem();
  }
  
  public static ItemBasketType createItemBasketType(Integer paramInteger, String paramString) {
    return factory.createItemBasketType(paramInteger, paramString);
  }
  
  public static ItemFulfillmentOrderVO createItemCustomerOrderVO() {
    return factory.createItemCustomerOrderVO();
  }
  
  public static ItemDetailVO createItemDetailVO(StockItem paramStockItem) {
    return factory.createItemDetailVO(paramStockItem);
  }
  
  public static ItemDiffVO createItemDiffVO(String paramString) {
    return factory.createItemDiffVO(paramString);
  }
  
  public static ItemInventoryStockVO createItemInventoryStockVO(StockItem paramStockItem) {
    return factory.createItemInventoryStockVO(paramStockItem);
  }
  
  public static ItemVOByInventoryQueryFilter createItemVOByInventoryQueryFilter() {
    return factory.createItemVOByInventoryQueryFilter();
  }
  
  public static ItemVOByItemQueryFilter createItemVOByItemQueryFilter() {
    return factory.createItemVOByItemQueryFilter();
  }
  
  public static ItemVOBySupplierQueryFilter createItemVOBySupplierQueryFilter() {
    return factory.createItemVOBySupplierQueryFilter();
  }
  
  public static ItemVOByWarehouseQueryFilter createItemVOByWarehouseQueryFilter() {
    return factory.createItemVOByWarehouseQueryFilter();
  }
  
  public static ItemVOByFinisherQueryFilter createItemVOByFinisherQueryFilter() {
    return factory.createItemVOByFinisherQueryFilter();
  }
  
  public static ItemVOByUDAQueryFilter createItemVOByUDAQueryFilter() {
    return factory.createItemVOByUDAQueryFilter();
  }
  
  public static ItemVOByDiffQueryFilter createItemVOByDiffQueryFilter() {
    return factory.createItemVOByDiffQueryFilter();
  }
  
  public static ItemUDA createItemUDA() {
    return factory.createItemUDA();
  }
  
  public static ItemVO createItemVO(String paramString) {
    return factory.createItemVO(paramString);
  }
  
  public static ItemImageInfo createItemImageInfo() {
    return factory.createItemImageInfo();
  }
  
  public static ItemRequest createItemRequest(Long paramLong) {
    return factory.createItemRequest(paramLong);
  }
  
  public static DeliveryTimeSlot createDeliveryTimeSlot() {
    return factory.createDeliveryTimeSlot();
  }
  
  public static ItemRequestLineItem createItemRequestLineItem(OrderItem paramOrderItem) {
    return factory.createItemRequestLineItem(paramOrderItem);
  }
  
  public static ItemRequestQueryFilter createItemRequestQueryFilter() {
    return factory.createItemRequestQueryFilter();
  }
  
  public static ItemRequestVO createItemRequestVO(Long paramLong) {
    return factory.createItemRequestVO(paramLong);
  }
  
  public static ItemSale createItemSale() {
    return factory.createItemSale();
  }
  
  public static ItemTicket createItemTicket(RetailItem paramRetailItem) {
    return factory.createItemTicket(paramRetailItem);
  }
  
  public static ItemTicketQueryFilter createItemTicketQueryFilter(Long paramLong) {
    return factory.createItemTicketQueryFilter(paramLong);
  }
  
  public static ItemUDAVO createItemUDAVO(String paramString) {
    return factory.createItemUDAVO(paramString);
  }
  
  public static ManifestOpenShipmentQueryFilter createManifestOpenShipmentQueryFilter() {
    return factory.createManifestOpenShipmentQueryFilter();
  }
  
  public static MdseHierarchyNode createMdseHierarchyNode() {
    return factory.createMdseHierarchyNode();
  }
  
  public static MonthlyByDaySchedule createMonthlyByDaySchedule() {
    return factory.createMonthlyByDaySchedule();
  }
  
  public static MonthlyByDaySchedule createMonthlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    return factory.createMonthlyByDaySchedule(paramInteger1, paramInteger2);
  }
  
  public static MonthlyByWeekSchedule createMonthlyByWeekSchedule() {
    return factory.createMonthlyByWeekSchedule();
  }
  
  public static MonthlyByWeekSchedule createMonthlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return factory.createMonthlyByWeekSchedule(paramInteger1, paramInteger2, paramInteger3);
  }
  
  public static MpsStagedMessageQueryFilter createMpsStagedMessageQueryFilter() {
    return factory.createMpsStagedMessageQueryFilter();
  }
  
  public static MpsStagedMessage createMpsStagedMessage() {
    return factory.createMpsStagedMessage();
  }
  
  public static MpsStagedMessageVO createMpsStagedMessageVO() {
    return factory.createMpsStagedMessageVO();
  }
  
  public static MpsWorkerType createMpsWorkerType() {
    return factory.createMpsWorkerType();
  }
  
  public static MpsWorkerTypeVO createMpsWorkerTypeVO() {
    return factory.createMpsWorkerTypeVO();
  }
  
  public static NavigationData createNavigationData() {
    return factory.createNavigationData();
  }
  
  public static NonSellableQtyType createNonSellableQtyType(Long paramLong, String paramString) {
    return factory.createNonSellableQtyType(paramLong, paramString);
  }
  
  public static Note createNote() {
    return factory.createNote();
  }
  
  public static OrderItem createOrderItem(Item paramItem, Long paramLong) {
    return factory.createOrderItem(paramItem, paramLong);
  }
  
  public static PackHeaderVO createPackHeaderVO() {
    return factory.createPackHeaderVO();
  }
  
  public static PasswordConfiguration createPasswordConfiguration() {
    return factory.createPasswordConfiguration();
  }
  
  public static Permission createPermission() {
    return factory.createPermission();
  }
  
  public static PermissionGroup createPermissionGroup() {
    return factory.createPermissionGroup();
  }
  
  public static PermissionQueryFilter createPermissionQueryFilter() {
    return factory.createPermissionQueryFilter();
  }
  
  public static PermissionGroupQueryFilter createPermissionGroupQueryFilter() {
    return factory.createPermissionGroupQueryFilter();
  }
  
  public static PermissionSet createPermissionSet() {
    return factory.createPermissionSet();
  }
  
  public static ShelfReplenishment createShelfReplenishment() {
    return factory.createShelfReplenishment();
  }
  
  public static ShelfReplenishment createShelfReplenishment(Long paramLong) {
    return factory.createShelfReplenishment(paramLong);
  }
  
  public static ShelfReplenishmentVO createShelfReplenishmentVO(Long paramLong) {
    return factory.createShelfReplenishmentVO(paramLong);
  }
  
  public static ShelfReplenishmentLineItem createShelfReplenishmentLineItem(Long paramLong, StockItem paramStockItem) {
    return factory.createShelfReplenishmentLineItem(paramLong, paramStockItem);
  }
  
  public static ShelfReplenishmentQueryFilter createShelfReplenishmentQueryFilter() {
    return factory.createShelfReplenishmentQueryFilter();
  }
  
  public static POSTransaction createPOSTransaction() {
    return factory.createPOSTransaction();
  }
  
  public static PostalAddress createPostalAddress() {
    return factory.createPostalAddress();
  }
  
  public static ItemPrice createItemPrice(Long paramLong) {
    return factory.createItemPrice(paramLong);
  }
  
  public static ItemPriceVO createItemPriceVO(Long paramLong) {
    return factory.createItemPriceVO(paramLong);
  }
  
  public static ItemCurrentPriceVO createItemCurrentPriceVO(Long paramLong, String paramString) {
    return factory.createItemCurrentPriceVO(paramLong, paramString);
  }
  
  public static ItemPriceQueryFilter createItemPriceQueryFilter() {
    return factory.createItemPriceQueryFilter();
  }
  
  public static PriceChange createPriceChange() {
    return factory.createPriceChange();
  }
  
  public static PriceInfo createPriceInfo(SimMoney paramSimMoney, Date paramDate) {
    return factory.createPriceInfo(paramSimMoney, paramDate);
  }
  
  public static ProductGroup createProductGroup(ProductGroupType paramProductGroupType) {
    return factory.createProductGroup(paramProductGroupType);
  }
  
  public static ProductGroupItem createProductGroupItem(String paramString) {
    return factory.createProductGroupItem(paramString);
  }
  
  public static ProductGroupVO createProductGroupVO() {
    return factory.createProductGroupVO();
  }
  
  public static ProductGroupHierarchy createProductGroupHierarchy(Long paramLong1, Long paramLong2, Long paramLong3) {
    return factory.createProductGroupHierarchy(paramLong1, paramLong2, paramLong3);
  }
  
  public static ProductGroupBatchVO createProductGroupBatchRecord(Long paramLong1, Long paramLong2) {
    return factory.createProductGroupBatchRecord(paramLong1, paramLong2);
  }
  
  public static ProductGroupQueryFilter createProductGroupQueryFilter() {
    return factory.createProductGroupQueryFilter();
  }
  
  public static ProductGroupScheduleQueryFilter createProductGroupScheduleQueryFilter() {
    return factory.createProductGroupScheduleQueryFilter();
  }
  
  public static ProductGroupSchedule createProductGroupSchedule() {
    return factory.createProductGroupSchedule();
  }
  
  public static ProductGroupScheduleVO createProductGroupScheduleVO() {
    return factory.createProductGroupScheduleVO();
  }
  
  public static PromotionQueryFilter createPromotionQueryFilter() {
    return factory.createPromotionQueryFilter();
  }
  
  public static PromotionVO createPromotionVO(String paramString1, String paramString2) {
    return factory.createPromotionVO(paramString1, paramString2);
  }
  
  public static PurchaseOrder createPurchaseOrder() {
    return factory.createPurchaseOrder();
  }
  
  public static PurchaseOrderVO createPurchaseOrderVO() {
    return factory.createPurchaseOrderVO();
  }
  
  public static PurchaseOrderLineItem createPurchaseOrderLineItem(SupplierItem paramSupplierItem) {
    return factory.createPurchaseOrderLineItem(paramSupplierItem);
  }
  
  public static PurchaseOrderQueryFilter createPurchaseOrderQueryFilter() {
    return factory.createPurchaseOrderQueryFilter();
  }
  
  public static RelatedItem createRelatedItem(String paramString) {
    return factory.createRelatedItem(paramString);
  }
  
  public static ReportResponse createReportResponse() {
    return factory.createReportResponse();
  }
  
  public static ReportTypeFormat createReportTypeFormat() {
    return factory.createReportTypeFormat();
  }
  
  public static RetailItem createRetailItem(String paramString, Long paramLong) {
    return factory.createRetailItem(paramString, paramLong);
  }
  
  public static StorePrinter createRetailStorePrinter() {
    return factory.createRetailStorePrinter();
  }
  
  public static SessionPrinter createSessionPrinter() {
    return factory.createSessionPrinter();
  }
  
  public static Return createReturn(Long paramLong, SourceType paramSourceType) {
    return factory.createReturn(paramLong, paramSourceType);
  }
  
  public static Return createReturn(Long paramLong, SourceType paramSourceType, boolean paramBoolean) {
    return factory.createReturn(paramLong, paramSourceType, paramBoolean);
  }
  
  public static ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType) {
    return factory.createReturnLineItem(paramStockItem, paramSourceType);
  }
  
  public static ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType, boolean paramBoolean) {
    return factory.createReturnLineItem(paramStockItem, paramSourceType, paramBoolean);
  }
  
  public static ReturnReason createReturnReason() {
    return factory.createReturnReason();
  }
  
  public static ReturnVO createReturnVO() {
    return factory.createReturnVO();
  }
  
  public static Role createRole() {
    return factory.createRole();
  }
  
  public static RoleQueryFilter createRoleQueryFilter() {
    return factory.createRoleQueryFilter();
  }
  
  public static RoleType createRoleType() {
    return factory.createRoleType();
  }
  
  public static SaleItem createSaleItem(String paramString) {
    return factory.createSaleItem(paramString);
  }
  
  public static SerialNumberValue createSerialNumberValue() {
    return factory.createSerialNumberValue();
  }
  
  public static ShelfReplenishmentGroupVO createShelfReplenishmentGroupVO() {
    return factory.createShelfReplenishmentGroupVO();
  }
  
  public static ShipmentCarrier createShipmentCarrier() {
    return factory.createShipmentCarrier();
  }
  
  public static ShipmentCarrierService createShipmentCarrierService() {
    return factory.createShipmentCarrierService();
  }
  
  public static ShipmentCartonType createShipmentCartonType() {
    return factory.createShipmentCartonType();
  }
  
  public static ShipmentWeightUom createShipmentWeightUom() {
    return factory.createShipmentWeightUom();
  }
  
  public static SourceQueryFilter createSourceQueryFilter() {
    return factory.createSourceQueryFilter();
  }
  
  public static StockCount createStockCount(Long paramLong) {
    return factory.createStockCount(paramLong);
  }
  
  public static StockCountImport createStockCountImport() {
    return factory.createStockCountImport();
  }
  
  public static StockCountImportExtract createStockCountImportExtract() {
    return factory.createStockCountImportExtract();
  }
  
  public static StockCountGroupVO createStockCountGroupVO() {
    return factory.createStockCountGroupVO();
  }
  
  public static StockCountItem createStockCountItem(String paramString, Long paramLong) {
    return factory.createStockCountItem(paramString, paramLong);
  }
  
  public static StockCountLineItem createStockCountLineItem(Long paramLong1, Long paramLong2, StockCountItem paramStockCountItem) {
    return factory.createStockCountLineItem(paramLong1, paramLong2, paramStockCountItem);
  }
  
  public static StockCountLineItemAreaBreakdownVO createStockCountLineItemAreaBreakdownVO() {
    return factory.createStockCountLineItemAreaBreakdownVO();
  }
  
  public static StockCountLineItemCompBreakdownVO createStockCountLineItemCompBreakdownVO() {
    return factory.createStockCountLineItemCompBreakdownVO();
  }
  
  public static StockCountAuthorizeSerialNumber createStockCountLineItemAuthUINVO() {
    return factory.createStockCountLineItemAuthUINVO();
  }
  
  public static StockCountQueryFilter createStockCountQueryFilter() {
    return factory.createStockCountQueryFilter();
  }
  
  public static StockCountChild createStockCountChild(Long paramLong) {
    return factory.createStockCountChild(paramLong);
  }
  
  public static StockCountRejectedLineItem createStockCountRejectedLineItem(Long paramLong) {
    return factory.createStockCountRejectedLineItem(paramLong);
  }
  
  public static StockCountSerialNumber createStockCountSerialNumber(Long paramLong, String paramString) {
    return factory.createStockCountSerialNumber(paramLong, paramString);
  }
  
  public static StockInfo createStockInfo() {
    return factory.createStockInfo();
  }
  
  public static StockItem createStockItem(Item paramItem, Long paramLong) {
    return factory.createStockItem(paramItem, paramLong);
  }
  
  public static ReturnQueryFilter createStockReturnQueryFilter() {
    return factory.createReturnQueryFilter();
  }
  
  public static Store createStore(Long paramLong) {
    return factory.createStore(paramLong);
  }
  
  public static StoreOrder createStoreOrder() {
    return factory.createStoreOrder();
  }
  
  public static StoreOrder createStoreOrder(String paramString) {
    return factory.createStoreOrder(paramString);
  }
  
  public static StoreOrderLineItem createStoreOrderLineItem() {
    return factory.createStoreOrderLineItem();
  }
  
  public static StoreOrderLineItem createStoreOrderLineItem(OrderItem paramOrderItem) {
    return factory.createStoreOrderLineItem(paramOrderItem);
  }
  
  public static StoreOrderLineItem createStoreOrderLineItem(String paramString, OrderItem paramOrderItem) {
    return factory.createStoreOrderLineItem(paramString, paramOrderItem);
  }
  
  public static StoreOrderQueryFilter createStoreOrderQueryFilter() {
    return factory.createStoreOrderQueryFilter();
  }
  
  public static StoreSequenceArea createStoreSequenceArea() {
    return factory.createStoreSequenceArea();
  }
  
  public static StoreSequenceItem createStoreSequenceItem() {
    return factory.createStoreSequenceItem();
  }
  
  public static StoreSequenceItemQueryFilter createStoreSequenceItemQueryFilter() {
    return factory.createStoreSequenceItemQueryFilter();
  }
  
  public static Supplier createSupplier(String paramString1, String paramString2) {
    return factory.createSupplier(paramString1, paramString2);
  }
  
  public static SupplierContactInfo createSupplierContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo) {
    return factory.createSupplierContactInfo(paramAddressType, paramContactInfo);
  }
  
  public static SupplierItem createSupplierItem(String paramString1, String paramString2) {
    return factory.createSupplierItem(paramString1, paramString2);
  }
  
  public static SupplierVO createSupplierVO(String paramString1, String paramString2, SupplierStatus paramSupplierStatus) {
    return factory.createSupplierVO(paramString1, paramString2, paramSupplierStatus);
  }
  
  public static TicketType createTicketType() {
    return factory.createTicketType();
  }
  
  public static TicketTypeFormat createTicketTypeFormat() {
    return factory.createTicketTypeFormat();
  }
  
  public static ToleranceAdmin createToleranceAdmin() {
    return factory.createToleranceAdmin();
  }
  
  public static ToleranceVO createToleranceVO() {
    return factory.createToleranceVO();
  }
  
  public static TransactionHistoryQueryFilter createTransactionHistoryQueryFilter() {
    return factory.createTransactionHistoryQueryFilter();
  }
  
  public static TransactionHistoryVO createTransactionHistoryVO() {
    return factory.createTransactionHistoryVO();
  }
  
  public static Transfer createTransfer() {
    return factory.createTransfer();
  }
  
  public static TransferAllocationVO createTransferAllocationVO() {
    return factory.createTransferAllocationVO();
  }
  
  public static TransferLineItem createTransferLineItem(StockItem paramStockItem) {
    return factory.createTransferLineItem(paramStockItem);
  }
  
  public static TransferQueryFilter createTransferQueryFilter() {
    return factory.createTransferQueryFilter();
  }
  
  public static TransferSerialNumber createTransferSerialNumber() {
    return factory.createTransferSerialNumber();
  }
  
  public static TransferVO createTransferVO() {
    return factory.createTransferVO();
  }
  
  public static UDADetail createUDADetail() {
    return factory.createUDADetail();
  }
  
  public static UDAValue createUDAValue() {
    return factory.createUDAValue();
  }
  
  public static UINDetail createUINDetail(Long paramLong) {
    return factory.createUINDetail(paramLong);
  }
  
  public static UINDetailVO createUINDetailLookupVO() {
    return factory.createUINDetailLookupVO();
  }
  
  public static UINDetailLookupQueryFilter createUINDetailLookupQueryFilter() {
    return factory.createUINDetailLookupQueryFilter();
  }
  
  public static UINSelectVO createUINDetailSelectVO() {
    return factory.createUINDetailSelectVO();
  }
  
  public static UINHistoryVO createUINHistoryVO() {
    return factory.createUINHistoryVO();
  }
  
  public static UINProblemDetail createUINProblemDetail() {
    return factory.createUINProblemDetail();
  }
  
  public static UINProblemDetailQueryFilter createUINResolutionQueryFilter() {
    return factory.createUINResolutionQueryFilter();
  }
  
  public static UINStoreDept createUINStoreDept() {
    return factory.createUINStoreDept();
  }
  
  public static UINStoreItem createUINStoreItem(Long paramLong) {
    return factory.createUINStoreItem(paramLong);
  }
  
  public static User createUser() {
    return factory.createUser();
  }
  
  public static UserLoginVO createUserLoginVO() {
    return factory.createUserLoginVO();
  }
  
  public static UserPassword createUserPassword() {
    return factory.createUserPassword();
  }
  
  public static UserPasswordGenerator createUserPasswordGenerator() {
    return factory.createUserPasswordGenerator();
  }
  
  public static UserQueryFilter createUserQueryFilter() {
    return factory.createUserQueryFilter();
  }
  
  public static UserRole createUserRole() {
    return factory.createUserRole();
  }
  
  public static UserRolesSaveVO createUserRolesSaveVO() {
    return factory.createUserRolesSaveVO();
  }
  
  public static UserSaveVO createUserSaveVO() {
    return factory.createUserSaveVO();
  }
  
  public static UserStore createUserStore() {
    return factory.createUserStore();
  }
  
  public static UserStoresSaveVO createUserStoresSaveVO() {
    return factory.createUserStoresSaveVO();
  }
  
  public static UsernameGenerator createUsernameGenerator() {
    return factory.createUsernameGenerator();
  }
  
  public static Warehouse createWarehouse(String paramString1, String paramString2) {
    return factory.createWarehouse(paramString1, paramString2);
  }
  
  public static WarehouseVO createWarehouseVO(String paramString1, String paramString2) {
    return factory.createWarehouseVO(paramString1, paramString2);
  }
  
  public static WarehouseDetailVO createWarehouseDetailVO(String paramString1, String paramString2) {
    return factory.createWarehouseDetailVO(paramString1, paramString2);
  }
  
  public static WarehouseDelivery createWarehouseDelivery() {
    return factory.createWarehouseDelivery();
  }
  
  public static WarehouseDeliveryVO createWarehouseDeliveryVO() {
    return factory.createWarehouseDeliveryVO();
  }
  
  public static WarehouseDeliveryCarton createWarehouseDeliveryCarton() {
    return factory.createWarehouseDeliveryCarton();
  }
  
  public static WarehouseDeliveryCartonQueryFilter createWarehouseDeliveryCartonQueryFilter() {
    return factory.createWarehouseDeliveryCartonQueryFilter();
  }
  
  public static WarehouseDeliveryCartonVO createWarehouseDeliveryCartonVO() {
    return factory.createWarehouseDeliveryCartonVO();
  }
  
  public static WarehouseDeliveryQuickVO createWarehouseDeliveryQuickVO() {
    return factory.createWarehouseDeliveryQuickVO();
  }
  
  public static WarehouseDeliveryCompositeLineItem createWarehouseDeliveryCompositeLineItem(WarehouseDeliverySimpleLineItem[] paramArrayOfWarehouseDeliverySimpleLineItem) {
    return factory.createWarehouseDeliveryCompositeLineItem(paramArrayOfWarehouseDeliverySimpleLineItem);
  }
  
  public static WarehouseDeliveryQueryFilter createWarehouseDeliveryQueryFilter() {
    return factory.createWarehouseDeliveryQueryFilter();
  }
  
  public static WarehouseDeliverySimpleLineItem createWarehouseDeliverySimpleLineItem(StockItem paramStockItem) {
    return factory.createWarehouseDeliverySimpleLineItem(paramStockItem);
  }
  
  public static WeeklySchedule createWeeklySchedule() {
    return factory.createWeeklySchedule();
  }
  
  public static WeeklySchedule createWeeklySchedule(Integer paramInteger1, Integer paramInteger2) {
    return factory.createWeeklySchedule(paramInteger1, paramInteger2);
  }
  
  public static WeeklySchedule createWeeklySchedule(Integer paramInteger, Set<Integer> paramSet) {
    return factory.createWeeklySchedule(paramInteger, paramSet);
  }
  
  public static YearlyByDaySchedule createYearlyByDaySchedule() {
    return factory.createYearlyByDaySchedule();
  }
  
  public static YearlyByDaySchedule createYearlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    return factory.createYearlyByDaySchedule(paramInteger1, paramInteger2);
  }
  
  public static YearlyByWeekSchedule createYearlyByWeekSchedule() {
    return factory.createYearlyByWeekSchedule();
  }
  
  public static YearlyByWeekSchedule createYearlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return factory.createYearlyByWeekSchedule(paramInteger1, paramInteger2, paramInteger3);
  }
  
  public static DirectDeliveryReportRequest createDirectDeliveryReportRequest(Long paramLong) {
    return factory.createDirectDeliveryReportRequest(paramLong);
  }
  
  public static FulfillmentOrderReportRequest createFulfillmentOrderReportRequest(Long paramLong) {
    return factory.createFulfillmentOrderReportRequest(paramLong);
  }
  
  public static FulfillmentOrderBinReportRequest createFulfillmentOrderBinReportRequest(String paramString, Long paramLong) {
    return factory.createFulfillmentOrderBinReportRequest(paramString, paramLong);
  }
  
  public static FulfillmentOrderDeliveryReportRequest createFulfillmentOrderDeliveryReportRequest(Long paramLong) {
    return factory.createFulfillmentOrderDeliveryReportRequest(paramLong);
  }
  
  public static FulfillmentOrderPickReportRequest createFulfillmentOrderPickReportRequest(Long paramLong) {
    return factory.createFulfillmentOrderPickReportRequest(paramLong);
  }
  
  public static FulfillmentOrderReversePickReportRequest createFulfillmentOrderReversePickReportRequest(Long paramLong) {
    return factory.createFulfillmentOrderReversePickReportRequest(paramLong);
  }
  
  public static InventoryAdjustmentReportRequest createInventoryAdjustmentReportRequest(Long paramLong) {
    return factory.createInventoryAdjustmentReportRequest(paramLong);
  }
  
  public static ItemReportRequest createItemReportRequest(String paramString) {
    return factory.createItemReportRequest(paramString);
  }
  
  public static ItemBasketReportRequest createItemBasketReportRequest(Long paramLong) {
    return factory.createItemBasketReportRequest(paramLong);
  }
  
  public static ItemRequestReportRequest createItemRequestReportRequest(Long paramLong) {
    return factory.createItemRequestReportRequest(paramLong);
  }
  
  public static ItemTicketReportRequest createItemTicketReportRequest() {
    return factory.createItemTicketReportRequest();
  }
  
  public static ReturnReportRequest createReturnReportRequest(Long paramLong) {
    return factory.createReturnReportRequest(paramLong);
  }
  
  public static ShelfReplenishmentReportRequest createShelfReplenishmentReportRequest(Long paramLong) {
    return factory.createShelfReplenishmentReportRequest(paramLong);
  }
  
  public static StockCountReportRequest createStockCountReportRequest() {
    return factory.createStockCountReportRequest();
  }
  
  public static StockCountRejectedItemReportRequest createStockCountRejectedItemReportRequest(Long paramLong) {
    return factory.createStockCountRejectedItemReportRequest(paramLong);
  }
  
  public static StoreOrderReportRequest createStoreOrderReportRequest(String paramString) {
    return factory.createStoreOrderReportRequest(paramString);
  }
  
  public static TransferReportRequest createTransferReportRequest(Long paramLong) {
    return factory.createTransferReportRequest(paramLong);
  }
  
  public static UinReportRequest createUinReportRequest() {
    return factory.createUinReportRequest();
  }
  
  public static WarehouseDeliveryReportRequest createWarehouseDeliveryReportRequest(Long paramLong) {
    return factory.createWarehouseDeliveryReportRequest(paramLong);
  }
  
  public static UINAttributeImport createUINAttributeImport() {
    return factory.createUINAttributeImport();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BOFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */