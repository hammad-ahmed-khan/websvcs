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
import oracle.retail.sim.common.report.ReportRequest;
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

public class BOFactoryImpl implements BOFactoryInterface {
  public ActivityHistoryVO createActivityHistoryVO(ActivityType paramActivityType, Long paramLong, String paramString, DeviceType paramDeviceType) {
    return new ActivityHistoryVO(paramActivityType, paramLong, paramString, paramDeviceType);
  }
  
  public AllocationVO createAllocationVO() {
    return new AllocationVO();
  }
  
  public BarcodeItem createBarcodeItem(StockItem paramStockItem) {
    return new BarcodeItem(paramStockItem);
  }
  
  public BarcodeInfo createBarcodeInfo(String paramString) {
    return new BarcodeInfo(paramString);
  }
  
  public BatchGroupVO createBatchGroupVO() {
    return new BatchGroupVO();
  }
  
  public ConfigBatchImpExp createBatchImportConfig() {
    return new ConfigBatchImpExp();
  }
  
  public BatchImport createBatchImportExport() {
    return new BatchImport();
  }
  
  public BillOfLading createBillOfLading() {
    return new BillOfLading();
  }
  
  public BillOfLadingMotive createBillOfLadingMotive(String paramString1, String paramString2) {
    return new BillOfLadingMotive(paramString1, paramString2);
  }
  
  public BuddyStore createBuddyStore(Long paramLong, String paramString) {
    return new BuddyStore(paramLong, paramString);
  }
  
  public CodeInfo createCodeInfo(String paramString) {
    return new CodeInfo(paramString);
  }
  
  public ConfigurationOption createConfigurationOption(String paramString) {
    return new ConfigurationOption(paramString);
  }
  
  public ContactInfo createContactInfo() {
    return new ContactInfo();
  }
  
  public ContextType createContextType() {
    return new ContextType();
  }
  
  public CustomerAddress createCustomerAddress(CustomerAddressType paramCustomerAddressType) {
    return new CustomerAddress(paramCustomerAddressType);
  }
  
  public CustomTheme createCustomTheme() {
    return new CustomTheme();
  }
  
  public DailySchedule createDailySchedule() {
    return new DailySchedule();
  }
  
  public DailySchedule createDailySchedule(Integer paramInteger) {
    return new DailySchedule(paramInteger);
  }
  
  public DailyByWeekdaySchedule createDailyByWeekdaySchedule() {
    return new DailyByWeekdaySchedule();
  }
  
  public Deal createDeal() {
    return new Deal();
  }
  
  public Differentiator createDifferentiator() {
    return new Differentiator();
  }
  
  public DifferentiatorType createDiffType() {
    return new DifferentiatorType();
  }
  
  public DirectDelivery createDirectDelivery() {
    return new DirectDelivery();
  }
  
  public DirectDeliveryCarton createDirectDeliveryCarton() {
    return new DirectDeliveryCarton();
  }
  
  public DirectDeliveryCartonVO createDirectDeliveryCartonVO() {
    return new DirectDeliveryCartonVO();
  }
  
  public DirectDeliveryCompositeLineItem createDirectDeliveryCompositeLineItem(DirectDeliverySimpleLineItem[] paramArrayOfDirectDeliverySimpleLineItem) {
    return new DirectDeliveryCompositeLineItem(paramArrayOfDirectDeliverySimpleLineItem);
  }
  
  public DirectDeliveryQueryFilter createDirectDeliveryQueryFilter() {
    return new DirectDeliveryQueryFilter();
  }
  
  public DirectDeliverySimpleLineItem createDirectDeliverySimpleLineItem(StockItem paramStockItem) {
    return new DirectDeliverySimpleLineItem(paramStockItem);
  }
  
  public DirectDeliveryVO createDirectDeliveryVO() {
    return new DirectDeliveryVO();
  }
  
  public Finisher createFinisher(String paramString1, String paramString2) {
    return new Finisher(paramString1, paramString2);
  }
  
  public FinisherContactInfo createFinisherContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo) {
    return new FinisherContactInfo(paramAddressType, paramContactInfo);
  }
  
  public FinisherVO createFinisherVO(String paramString1, String paramString2, FinisherStatus paramFinisherStatus) {
    return new FinisherVO(paramString1, paramString2, paramFinisherStatus);
  }
  
  public FulfillmentOrder createFulfillmentOrder() {
    return new FulfillmentOrder();
  }
  
  public FulfillmentOrderBin createFulfillmentOrderBin() {
    return new FulfillmentOrderBin();
  }
  
  public FulfillmentOrderContactInfo createFulfillmentOrderContactInfo() {
    return new FulfillmentOrderContactInfo();
  }
  
  public FulfillmentOrderCreateCustomerVO createFulfillmentOrderCreateCustomerVO() {
    return new FulfillmentOrderCreateCustomerVO();
  }
  
  public FulfillmentOrderCreateLineItemVO createFulfillmentOrderCreateLineItemVO() {
    return new FulfillmentOrderCreateLineItemVO();
  }
  
  public FulfillmentOrderCreateVO createFulfillmentOrderCreateVO() {
    return new FulfillmentOrderCreateVO();
  }
  
  public FulfillmentOrderDelivery createFulfillmentOrderDelivery() {
    return new FulfillmentOrderDelivery();
  }
  
  public FulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem) {
    return new FulfillmentOrderLineItem(paramStockItem);
  }
  
  public FulfillmentOrderDeliveryVO createFulfillmentOrderDeliveryVO() {
    return new FulfillmentOrderDeliveryVO();
  }
  
  public FulfillmentOrderDeliveryLineItem createFulfillmentOrderDeliveryLineItem() {
    return new FulfillmentOrderDeliveryLineItem();
  }
  
  public FulfillmentOrderDeliveryQueryFilter createFulfillmentOrderDeliveryQueryFilter() {
    return new FulfillmentOrderDeliveryQueryFilter();
  }
  
  public FulfillmentOrderPick createFulfillmentOrderPick() {
    return new FulfillmentOrderPick();
  }
  
  public FulfillmentOrderPickLineItem createFulfillmentOrderPickLineItem(StockItem paramStockItem) {
    return new FulfillmentOrderPickLineItem(paramStockItem);
  }
  
  public FulfillmentOrderPickQueryFilter createFulfillmentOrderPickQueryFilter() {
    return new FulfillmentOrderPickQueryFilter();
  }
  
  public FulfillmentOrderPickVO createFulfillmentOrderPickVO() {
    return new FulfillmentOrderPickVO();
  }
  
  public FulfillmentOrderPossiblePickVO createFulfillmentOrderPossiblePickVO() {
    return new FulfillmentOrderPossiblePickVO();
  }
  
  public FulfillmentOrderReversePickVO createFulfillmentOrderReversePickVO() {
    return new FulfillmentOrderReversePickVO();
  }
  
  public FulfillmentOrderReversePick createFulfillmentOrderReversePick() {
    return new FulfillmentOrderReversePick();
  }
  
  public FulfillmentOrderReversePickLineItem createFulfillmentOrderReversePickLineItem() {
    return new FulfillmentOrderReversePickLineItem();
  }
  
  public FulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter() {
    return new FulfillmentOrderQueryFilter();
  }
  
  public FulfillmentOrderVO createFulfillmentOrderVO() {
    return new FulfillmentOrderVO();
  }
  
  public FulfillmentOrderMgmtListVO createFulfillmentOrderMgmtListVO() {
    return new FulfillmentOrderMgmtListVO();
  }
  
  public FulfillmentOrderMgmtQueryFilter createFulfillmentOrderMgmtQueryFilter() {
    return new FulfillmentOrderMgmtQueryFilter();
  }
  
  public FutureCountQueryFilter createFutureCountQueryFilter() {
    return new FutureCountQueryFilter();
  }
  
  public FuturePriceVO createFuturePriceVO() {
    return new FuturePriceVO();
  }
  
  public InventoryAdjustment createInventoryAdjustment() {
    return new InventoryAdjustment();
  }
  
  public InventoryAdjustmentLineItem createInventoryAdjustmentLineItem(StockItem paramStockItem) {
    return new InventoryAdjustmentLineItem(paramStockItem);
  }
  
  public InventoryAdjustmentQueryFilter createInventoryAdjustmentQueryFilter() {
    return new InventoryAdjustmentQueryFilter();
  }
  
  public InventoryAdjustmentVO createInventoryAdjustmentVO() {
    return new InventoryAdjustmentVO();
  }
  
  public InventoryAdjustmentReason createInventoryAdjustmentReason() {
    return new InventoryAdjustmentReason();
  }
  
  public InventoryAdjustmentTemplate createInventoryAdjustmentTemplate() {
    return new InventoryAdjustmentTemplate();
  }
  
  public InventoryAdjustmentTemplateLineItem createInventoryAdjustmentTemplateLineItem(StockItem paramStockItem) {
    return new InventoryAdjustmentTemplateLineItem(paramStockItem);
  }
  
  public InventoryAdjustmentTemplateQueryFilter createInventoryAdjustmentTemplateQueryFilter() {
    return new InventoryAdjustmentTemplateQueryFilter();
  }
  
  public InventoryAdjustmentTemplateVO createInventoryAdjustmentTemplateVO() {
    return new InventoryAdjustmentTemplateVO();
  }
  
  public Item createItem(String paramString) {
    return new Item(paramString);
  }
  
  public StoreItemStockVO createItemAvailableBuddyStockVO() {
    return new StoreItemStockVO();
  }
  
  public ItemAvailableStockVO createItemAvailableStockVO() {
    return new ItemAvailableStockVO();
  }
  
  public ItemBasket createItemBasket() {
    return new ItemBasket();
  }
  
  public ItemBasketLineItem createItemBasketLineItem() {
    return new ItemBasketLineItem();
  }
  
  public ItemBasketType createItemBasketType(Integer paramInteger, String paramString) {
    return new ItemBasketType(paramInteger, paramString);
  }
  
  public ItemFulfillmentOrderVO createItemCustomerOrderVO() {
    return new ItemFulfillmentOrderVO();
  }
  
  public ItemDetailVO createItemDetailVO(StockItem paramStockItem) {
    return new ItemDetailVO(paramStockItem);
  }
  
  public ItemDiffVO createItemDiffVO(String paramString) {
    return new ItemDiffVO(paramString);
  }
  
  public ItemInventoryStockVO createItemInventoryStockVO(StockItem paramStockItem) {
    return new ItemInventoryStockVO(paramStockItem);
  }
  
  public ItemVO createItemVO(String paramString) {
    return new ItemVO(paramString);
  }
  
  public ItemVOByInventoryQueryFilter createItemVOByInventoryQueryFilter() {
    return new ItemVOByInventoryQueryFilter();
  }
  
  public ItemVOByItemQueryFilter createItemVOByItemQueryFilter() {
    return new ItemVOByItemQueryFilter();
  }
  
  public ItemVOBySupplierQueryFilter createItemVOBySupplierQueryFilter() {
    return new ItemVOBySupplierQueryFilter();
  }
  
  public ItemVOByFinisherQueryFilter createItemVOByFinisherQueryFilter() {
    return new ItemVOByFinisherQueryFilter();
  }
  
  public ItemVOByUDAQueryFilter createItemVOByUDAQueryFilter() {
    return new ItemVOByUDAQueryFilter();
  }
  
  public ItemVOByDiffQueryFilter createItemVOByDiffQueryFilter() {
    return new ItemVOByDiffQueryFilter();
  }
  
  public ItemVOByWarehouseQueryFilter createItemVOByWarehouseQueryFilter() {
    return new ItemVOByWarehouseQueryFilter();
  }
  
  public ItemRequest createItemRequest(Long paramLong) {
    return new ItemRequest(paramLong);
  }
  
  public ItemUDA createItemUDA() {
    return new ItemUDA();
  }
  
  public ItemUDAVO createItemUDAVO(String paramString) {
    return new ItemUDAVO(paramString);
  }
  
  public ItemImageInfo createItemImageInfo() {
    return new ItemImageInfo();
  }
  
  public DeliveryTimeSlot createDeliveryTimeSlot() {
    return new DeliveryTimeSlot();
  }
  
  public ItemRequestVO createItemRequestVO(Long paramLong) {
    return new ItemRequestVO(paramLong);
  }
  
  public ItemRequestLineItem createItemRequestLineItem(OrderItem paramOrderItem) {
    return new ItemRequestLineItem(paramOrderItem);
  }
  
  public ItemRequestQueryFilter createItemRequestQueryFilter() {
    return new ItemRequestQueryFilter();
  }
  
  public ItemSale createItemSale() {
    return new ItemSale();
  }
  
  public ItemTicket createItemTicket(RetailItem paramRetailItem) {
    return new ItemTicket(paramRetailItem);
  }
  
  public ItemTicketQueryFilter createItemTicketQueryFilter(Long paramLong) {
    return new ItemTicketQueryFilter(paramLong);
  }
  
  public ManifestOpenShipmentQueryFilter createManifestOpenShipmentQueryFilter() {
    return new ManifestOpenShipmentQueryFilter();
  }
  
  public MdseHierarchyNode createMdseHierarchyNode() {
    return new MdseHierarchyNode();
  }
  
  public MonthlyByDaySchedule createMonthlyByDaySchedule() {
    return new MonthlyByDaySchedule();
  }
  
  public MonthlyByDaySchedule createMonthlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    return new MonthlyByDaySchedule(paramInteger1, paramInteger2);
  }
  
  public MonthlyByWeekSchedule createMonthlyByWeekSchedule() {
    return new MonthlyByWeekSchedule();
  }
  
  public MonthlyByWeekSchedule createMonthlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return new MonthlyByWeekSchedule(paramInteger1, paramInteger2, paramInteger3);
  }
  
  public MpsStagedMessageQueryFilter createMpsStagedMessageQueryFilter() {
    return new MpsStagedMessageQueryFilter();
  }
  
  public MpsStagedMessage createMpsStagedMessage() {
    return new MpsStagedMessage();
  }
  
  public MpsStagedMessageVO createMpsStagedMessageVO() {
    return new MpsStagedMessageVO();
  }
  
  public MpsWorkerType createMpsWorkerType() {
    return new MpsWorkerType();
  }
  
  public MpsWorkerTypeVO createMpsWorkerTypeVO() {
    return new MpsWorkerTypeVO();
  }
  
  public NavigationData createNavigationData() {
    return new NavigationData();
  }
  
  public NonSellableQtyType createNonSellableQtyType(Long paramLong, String paramString) {
    return new NonSellableQtyType(paramLong, paramString);
  }
  
  public Note createNote() {
    return new Note();
  }
  
  public OrderItem createOrderItem(Item paramItem, Long paramLong) {
    return new OrderItem(paramItem, paramLong);
  }
  
  public PackHeaderVO createPackHeaderVO() {
    return new PackHeaderVO();
  }
  
  public PasswordConfiguration createPasswordConfiguration() {
    return new PasswordConfiguration();
  }
  
  public Permission createPermission() {
    return new Permission();
  }
  
  public PermissionGroup createPermissionGroup() {
    return new PermissionGroup();
  }
  
  public PermissionQueryFilter createPermissionQueryFilter() {
    return new PermissionQueryFilter();
  }
  
  public PermissionGroupQueryFilter createPermissionGroupQueryFilter() {
    return new PermissionGroupQueryFilter();
  }
  
  public PermissionSet createPermissionSet() {
    return new PermissionSet();
  }
  
  public POSTransaction createPOSTransaction() {
    return new POSTransaction();
  }
  
  public PostalAddress createPostalAddress() {
    return new PostalAddress();
  }
  
  public ItemPrice createItemPrice(Long paramLong) {
    return new ItemPrice(paramLong);
  }
  
  public ItemPriceVO createItemPriceVO(Long paramLong) {
    return new ItemPriceVO(paramLong);
  }
  
  public ItemCurrentPriceVO createItemCurrentPriceVO(Long paramLong, String paramString) {
    return new ItemCurrentPriceVO(paramLong, paramString);
  }
  
  public ItemPriceQueryFilter createItemPriceQueryFilter() {
    return new ItemPriceQueryFilter();
  }
  
  public PriceChange createPriceChange() {
    return new PriceChange();
  }
  
  public PriceInfo createPriceInfo(SimMoney paramSimMoney, Date paramDate) {
    return new PriceInfo(paramSimMoney, paramDate);
  }
  
  public ProductGroup createProductGroup(ProductGroupType paramProductGroupType) {
    return new ProductGroup(paramProductGroupType);
  }
  
  public ProductGroupBatchVO createProductGroupBatchRecord(Long paramLong1, Long paramLong2) {
    return new ProductGroupBatchVO(paramLong1, paramLong2);
  }
  
  public ProductGroupItem createProductGroupItem(String paramString) {
    return new ProductGroupItem(paramString);
  }
  
  public ProductGroupVO createProductGroupVO() {
    return new ProductGroupVO();
  }
  
  public ProductGroupHierarchy createProductGroupHierarchy(Long paramLong1, Long paramLong2, Long paramLong3) {
    return new ProductGroupHierarchy(paramLong1, paramLong2, paramLong3);
  }
  
  public ProductGroupQueryFilter createProductGroupQueryFilter() {
    return new ProductGroupQueryFilter();
  }
  
  public ProductGroupSchedule createProductGroupSchedule() {
    return new ProductGroupSchedule();
  }
  
  public ProductGroupScheduleVO createProductGroupScheduleVO() {
    return new ProductGroupScheduleVO();
  }
  
  public ProductGroupScheduleQueryFilter createProductGroupScheduleQueryFilter() {
    return new ProductGroupScheduleQueryFilter();
  }
  
  public PromotionQueryFilter createPromotionQueryFilter() {
    return new PromotionQueryFilter();
  }
  
  public PromotionVO createPromotionVO(String paramString1, String paramString2) {
    return new PromotionVO(paramString1, paramString2);
  }
  
  public PurchaseOrder createPurchaseOrder() {
    return new PurchaseOrder();
  }
  
  public PurchaseOrderVO createPurchaseOrderVO() {
    return new PurchaseOrderVO();
  }
  
  public PurchaseOrderLineItem createPurchaseOrderLineItem(SupplierItem paramSupplierItem) {
    return new PurchaseOrderLineItem(paramSupplierItem);
  }
  
  public PurchaseOrderQueryFilter createPurchaseOrderQueryFilter() {
    return new PurchaseOrderQueryFilter();
  }
  
  public RelatedItem createRelatedItem(String paramString) {
    return new RelatedItem(paramString);
  }
  
  public ReportRequest createReportRequest() {
    return new ReportRequest();
  }
  
  public ReportResponse createReportResponse() {
    return new ReportResponse();
  }
  
  public ReportTypeFormat createReportTypeFormat() {
    return new ReportTypeFormat();
  }
  
  public RetailItem createRetailItem(String paramString, Long paramLong) {
    return new RetailItem(paramString, paramLong);
  }
  
  public StorePrinter createRetailStorePrinter() {
    return new StorePrinter();
  }
  
  public SessionPrinter createSessionPrinter() {
    return new SessionPrinter();
  }
  
  public ReturnQueryFilter createReturnQueryFilter() {
    return new ReturnQueryFilter();
  }
  
  public Return createReturn(Long paramLong, SourceType paramSourceType) {
    return new Return(paramLong, paramSourceType);
  }
  
  public Return createReturn(Long paramLong, SourceType paramSourceType, boolean paramBoolean) {
    return new Return(paramLong, paramSourceType, paramBoolean);
  }
  
  public ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType) {
    return new ReturnLineItem(paramStockItem, paramSourceType);
  }
  
  public ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType, boolean paramBoolean) {
    return new ReturnLineItem(paramStockItem, paramSourceType, paramBoolean);
  }
  
  public ReturnReason createReturnReason() {
    return new ReturnReason();
  }
  
  public ReturnVO createReturnVO() {
    return new ReturnVO();
  }
  
  public Role createRole() {
    return new Role();
  }
  
  public RoleQueryFilter createRoleQueryFilter() {
    return new RoleQueryFilter();
  }
  
  public RoleType createRoleType() {
    return new RoleType();
  }
  
  public SaleItem createSaleItem(String paramString) {
    return new SaleItem(paramString);
  }
  
  public SerialNumberValue createSerialNumberValue() {
    return new SerialNumberValue();
  }
  
  public ShelfReplenishment createShelfReplenishment() {
    return new ShelfReplenishment();
  }
  
  public ShelfReplenishment createShelfReplenishment(Long paramLong) {
    return new ShelfReplenishment(paramLong);
  }
  
  public ShelfReplenishmentGroupVO createShelfReplenishmentGroupVO() {
    return new ShelfReplenishmentGroupVO();
  }
  
  public ShelfReplenishmentVO createShelfReplenishmentVO(Long paramLong) {
    return new ShelfReplenishmentVO(paramLong);
  }
  
  public ShelfReplenishmentLineItem createShelfReplenishmentLineItem(StockItem paramStockItem) {
    return new ShelfReplenishmentLineItem(null, paramStockItem);
  }
  
  public ShelfReplenishmentLineItem createShelfReplenishmentLineItem(Long paramLong, StockItem paramStockItem) {
    return new ShelfReplenishmentLineItem(paramLong, paramStockItem);
  }
  
  public ShelfReplenishmentQueryFilter createShelfReplenishmentQueryFilter() {
    return new ShelfReplenishmentQueryFilter();
  }
  
  public ShipmentCarrier createShipmentCarrier() {
    return new ShipmentCarrier();
  }
  
  public ShipmentCarrierService createShipmentCarrierService() {
    return new ShipmentCarrierService();
  }
  
  public ShipmentCartonType createShipmentCartonType() {
    return new ShipmentCartonType();
  }
  
  public ShipmentWeightUom createShipmentWeightUom() {
    return new ShipmentWeightUom();
  }
  
  public SourceQueryFilter createSourceQueryFilter() {
    return new SourceQueryFilter();
  }
  
  public StockCount createStockCount(Long paramLong) {
    return new StockCount(paramLong);
  }
  
  public StockCountImport createStockCountImport() {
    return new StockCountImport();
  }
  
  public StockCountImportExtract createStockCountImportExtract() {
    return new StockCountImportExtract();
  }
  
  public StockCountItem createStockCountItem(String paramString, Long paramLong) {
    return new StockCountItem(paramString, paramLong);
  }
  
  public StockCountGroupVO createStockCountGroupVO() {
    return new StockCountGroupVO();
  }
  
  public StockCountQueryFilter createStockCountQueryFilter() {
    return new StockCountQueryFilter();
  }
  
  public StockCountLineItem createStockCountLineItem(Long paramLong1, Long paramLong2, StockCountItem paramStockCountItem) {
    return new StockCountLineItem(paramLong1, paramLong2, paramStockCountItem);
  }
  
  public StockCountLineItemAreaBreakdownVO createStockCountLineItemAreaBreakdownVO() {
    return new StockCountLineItemAreaBreakdownVO();
  }
  
  public StockCountLineItemCompBreakdownVO createStockCountLineItemCompBreakdownVO() {
    return new StockCountLineItemCompBreakdownVO();
  }
  
  public StockCountAuthorizeSerialNumber createStockCountLineItemAuthUINVO() {
    return new StockCountAuthorizeSerialNumber();
  }
  
  public StockCountChild createStockCountChild(Long paramLong) {
    return new StockCountChild(paramLong);
  }
  
  public StockCountRejectedLineItem createStockCountRejectedLineItem(Long paramLong) {
    return new StockCountRejectedLineItem(paramLong);
  }
  
  public StockCountSerialNumber createStockCountSerialNumber(Long paramLong, String paramString) {
    return new StockCountSerialNumber(paramLong, paramString);
  }
  
  public StockInfo createStockInfo() {
    return new StockInfo();
  }
  
  public StockItem createStockItem(Item paramItem, Long paramLong) {
    return new StockItem(paramItem, paramLong);
  }
  
  public Store createStore(Long paramLong) {
    return new Store(paramLong);
  }
  
  public StoreOrder createStoreOrder() {
    return new StoreOrder();
  }
  
  public StoreOrder createStoreOrder(String paramString) {
    return new StoreOrder(paramString);
  }
  
  public StoreOrderLineItem createStoreOrderLineItem() {
    return new StoreOrderLineItem();
  }
  
  public StoreOrderLineItem createStoreOrderLineItem(OrderItem paramOrderItem) {
    return new StoreOrderLineItem(paramOrderItem);
  }
  
  public StoreOrderLineItem createStoreOrderLineItem(String paramString, OrderItem paramOrderItem) {
    return new StoreOrderLineItem(paramString, paramOrderItem);
  }
  
  public StoreOrderQueryFilter createStoreOrderQueryFilter() {
    return new StoreOrderQueryFilter();
  }
  
  public StoreSequenceArea createStoreSequenceArea() {
    return new StoreSequenceArea();
  }
  
  public StoreSequenceItem createStoreSequenceItem() {
    return new StoreSequenceItem();
  }
  
  public StoreSequenceItemQueryFilter createStoreSequenceItemQueryFilter() {
    return new StoreSequenceItemQueryFilter();
  }
  
  public Supplier createSupplier(String paramString1, String paramString2) {
    return new Supplier(paramString1, paramString2);
  }
  
  public SupplierContactInfo createSupplierContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo) {
    return new SupplierContactInfo(paramAddressType, paramContactInfo);
  }
  
  public SupplierItem createSupplierItem(String paramString1, String paramString2) {
    return new SupplierItem(paramString1, paramString2);
  }
  
  public SupplierVO createSupplierVO(String paramString1, String paramString2, SupplierStatus paramSupplierStatus) {
    return new SupplierVO(paramString1, paramString2, paramSupplierStatus);
  }
  
  public TicketType createTicketType() {
    return new TicketType();
  }
  
  public TicketTypeFormat createTicketTypeFormat() {
    return new TicketTypeFormat();
  }
  
  public ToleranceAdmin createToleranceAdmin() {
    return new ToleranceAdmin();
  }
  
  public ToleranceVO createToleranceVO() {
    return new ToleranceVO();
  }
  
  public TransactionHistoryQueryFilter createTransactionHistoryQueryFilter() {
    return new TransactionHistoryQueryFilter();
  }
  
  public TransactionHistoryVO createTransactionHistoryVO() {
    return new TransactionHistoryVO();
  }
  
  public Transfer createTransfer() {
    return new Transfer();
  }
  
  public TransferAllocationVO createTransferAllocationVO() {
    return new TransferAllocationVO();
  }
  
  public TransferLineItem createTransferLineItem(StockItem paramStockItem) {
    return new TransferLineItem(paramStockItem);
  }
  
  public TransferQueryFilter createTransferQueryFilter() {
    return new TransferQueryFilter();
  }
  
  public TransferSerialNumber createTransferSerialNumber() {
    return new TransferSerialNumber();
  }
  
  public TransferVO createTransferVO() {
    return new TransferVO();
  }
  
  public UDADetail createUDADetail() {
    return new UDADetail();
  }
  
  public UDAValue createUDAValue() {
    return new UDAValue();
  }
  
  public UINAttributeImport createUINAttributeImport() {
    return new UINAttributeImport();
  }
  
  public UINDetail createUINDetail(Long paramLong) {
    return new UINDetail(paramLong);
  }
  
  public UINDetailLookupQueryFilter createUINDetailLookupQueryFilter() {
    return new UINDetailLookupQueryFilter();
  }
  
  public UINDetailVO createUINDetailLookupVO() {
    return new UINDetailVO();
  }
  
  public UINSelectVO createUINDetailSelectVO() {
    return new UINSelectVO();
  }
  
  public UINProblemDetail createUINProblemDetail() {
    return new UINProblemDetail();
  }
  
  public UINStoreDept createUINStoreDept() {
    return new UINStoreDept();
  }
  
  public UINStoreItem createUINStoreItem(Long paramLong) {
    return new UINStoreItem(paramLong);
  }
  
  public UINProblemDetailQueryFilter createUINResolutionQueryFilter() {
    return new UINProblemDetailQueryFilter();
  }
  
  public UINHistoryVO createUINHistoryVO() {
    return new UINHistoryVO();
  }
  
  public User createUser() {
    return new User();
  }
  
  public UserLoginVO createUserLoginVO() {
    return new UserLoginVO();
  }
  
  public UserPassword createUserPassword() {
    return new UserPassword();
  }
  
  public UserPasswordGenerator createUserPasswordGenerator() {
    return new UserPasswordGenerator();
  }
  
  public UserQueryFilter createUserQueryFilter() {
    return new UserQueryFilter();
  }
  
  public UserRole createUserRole() {
    return new UserRole();
  }
  
  public UserRolesSaveVO createUserRolesSaveVO() {
    return new UserRolesSaveVO();
  }
  
  public UserSaveVO createUserSaveVO() {
    return new UserSaveVO();
  }
  
  public UserStore createUserStore() {
    return new UserStore();
  }
  
  public UserStoresSaveVO createUserStoresSaveVO() {
    return new UserStoresSaveVO();
  }
  
  public UsernameGenerator createUsernameGenerator() {
    return new UsernameGenerator();
  }
  
  public Warehouse createWarehouse(String paramString1, String paramString2) {
    return new Warehouse(paramString1, paramString2);
  }
  
  public WarehouseVO createWarehouseVO(String paramString1, String paramString2) {
    return new WarehouseVO(paramString1, paramString2);
  }
  
  public WarehouseDetailVO createWarehouseDetailVO(String paramString1, String paramString2) {
    return new WarehouseDetailVO(paramString1, paramString2);
  }
  
  public WarehouseDelivery createWarehouseDelivery() {
    return new WarehouseDelivery();
  }
  
  public WarehouseDeliveryVO createWarehouseDeliveryVO() {
    return new WarehouseDeliveryVO();
  }
  
  public WarehouseDeliveryCarton createWarehouseDeliveryCarton() {
    return new WarehouseDeliveryCarton();
  }
  
  public WarehouseDeliveryCartonQueryFilter createWarehouseDeliveryCartonQueryFilter() {
    return new WarehouseDeliveryCartonQueryFilter();
  }
  
  public WarehouseDeliveryCartonVO createWarehouseDeliveryCartonVO() {
    return new WarehouseDeliveryCartonVO();
  }
  
  public WarehouseDeliveryQuickVO createWarehouseDeliveryQuickVO() {
    return new WarehouseDeliveryQuickVO();
  }
  
  public WarehouseDeliveryCompositeLineItem createWarehouseDeliveryCompositeLineItem(WarehouseDeliverySimpleLineItem[] paramArrayOfWarehouseDeliverySimpleLineItem) {
    return new WarehouseDeliveryCompositeLineItem(paramArrayOfWarehouseDeliverySimpleLineItem);
  }
  
  public WarehouseDeliveryQueryFilter createWarehouseDeliveryQueryFilter() {
    return new WarehouseDeliveryQueryFilter();
  }
  
  public WarehouseDeliverySimpleLineItem createWarehouseDeliverySimpleLineItem(StockItem paramStockItem) {
    return new WarehouseDeliverySimpleLineItem(paramStockItem);
  }
  
  public WeeklySchedule createWeeklySchedule() {
    return new WeeklySchedule();
  }
  
  public WeeklySchedule createWeeklySchedule(Integer paramInteger1, Integer paramInteger2) {
    return new WeeklySchedule(paramInteger1, paramInteger2);
  }
  
  public WeeklySchedule createWeeklySchedule(Integer paramInteger, Set<Integer> paramSet) {
    return new WeeklySchedule(paramInteger, paramSet);
  }
  
  public YearlyByDaySchedule createYearlyByDaySchedule() {
    return new YearlyByDaySchedule();
  }
  
  public YearlyByDaySchedule createYearlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    return new YearlyByDaySchedule(paramInteger1, paramInteger2);
  }
  
  public YearlyByWeekSchedule createYearlyByWeekSchedule() {
    return new YearlyByWeekSchedule();
  }
  
  public YearlyByWeekSchedule createYearlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return new YearlyByWeekSchedule(paramInteger1, paramInteger2, paramInteger3);
  }
  
  public DirectDeliveryReportRequest createDirectDeliveryReportRequest(Long paramLong) {
    return new DirectDeliveryReportRequest(paramLong);
  }
  
  public FulfillmentOrderReportRequest createFulfillmentOrderReportRequest(Long paramLong) {
    return new FulfillmentOrderReportRequest(paramLong);
  }
  
  public FulfillmentOrderBinReportRequest createFulfillmentOrderBinReportRequest(String paramString, Long paramLong) {
    return new FulfillmentOrderBinReportRequest(paramString, paramLong);
  }
  
  public FulfillmentOrderDeliveryReportRequest createFulfillmentOrderDeliveryReportRequest(Long paramLong) {
    return new FulfillmentOrderDeliveryReportRequest(paramLong);
  }
  
  public FulfillmentOrderPickReportRequest createFulfillmentOrderPickReportRequest(Long paramLong) {
    return new FulfillmentOrderPickReportRequest(paramLong);
  }
  
  public FulfillmentOrderReversePickReportRequest createFulfillmentOrderReversePickReportRequest(Long paramLong) {
    return new FulfillmentOrderReversePickReportRequest(paramLong);
  }
  
  public InventoryAdjustmentReportRequest createInventoryAdjustmentReportRequest(Long paramLong) {
    return new InventoryAdjustmentReportRequest(paramLong);
  }
  
  public ItemReportRequest createItemReportRequest(String paramString) {
    return new ItemReportRequest(paramString);
  }
  
  public ItemBasketReportRequest createItemBasketReportRequest(Long paramLong) {
    return new ItemBasketReportRequest(paramLong);
  }
  
  public ItemRequestReportRequest createItemRequestReportRequest(Long paramLong) {
    return new ItemRequestReportRequest(paramLong);
  }
  
  public ItemTicketReportRequest createItemTicketReportRequest() {
    return new ItemTicketReportRequest();
  }
  
  public ReturnReportRequest createReturnReportRequest(Long paramLong) {
    return new ReturnReportRequest(paramLong);
  }
  
  public ShelfReplenishmentReportRequest createShelfReplenishmentReportRequest(Long paramLong) {
    return new ShelfReplenishmentReportRequest(paramLong);
  }
  
  public StockCountReportRequest createStockCountReportRequest() {
    return new StockCountReportRequest();
  }
  
  public StockCountRejectedItemReportRequest createStockCountRejectedItemReportRequest(Long paramLong) {
    return new StockCountRejectedItemReportRequest(paramLong);
  }
  
  public StoreOrderReportRequest createStoreOrderReportRequest(String paramString) {
    return new StoreOrderReportRequest(paramString);
  }
  
  public TransferReportRequest createTransferReportRequest(Long paramLong) {
    return new TransferReportRequest(paramLong);
  }
  
  public UinReportRequest createUinReportRequest() {
    return new UinReportRequest();
  }
  
  public WarehouseDeliveryReportRequest createWarehouseDeliveryReportRequest(Long paramLong) {
    return new WarehouseDeliveryReportRequest(paramLong);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BOFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */