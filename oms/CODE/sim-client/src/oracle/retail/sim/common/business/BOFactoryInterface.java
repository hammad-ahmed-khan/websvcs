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

public interface BOFactoryInterface {
  ActivityHistoryVO createActivityHistoryVO(ActivityType paramActivityType, Long paramLong, String paramString, DeviceType paramDeviceType);
  
  AllocationVO createAllocationVO();
  
  BarcodeItem createBarcodeItem(StockItem paramStockItem);
  
  BarcodeInfo createBarcodeInfo(String paramString);
  
  BatchGroupVO createBatchGroupVO();
  
  ConfigBatchImpExp createBatchImportConfig();
  
  BatchImport createBatchImportExport();
  
  BillOfLading createBillOfLading();
  
  BillOfLadingMotive createBillOfLadingMotive(String paramString1, String paramString2);
  
  BuddyStore createBuddyStore(Long paramLong, String paramString);
  
  CodeInfo createCodeInfo(String paramString);
  
  ConfigurationOption createConfigurationOption(String paramString);
  
  ContactInfo createContactInfo();
  
  ContextType createContextType();
  
  CustomerAddress createCustomerAddress(CustomerAddressType paramCustomerAddressType);
  
  CustomTheme createCustomTheme();
  
  DailySchedule createDailySchedule();
  
  DailySchedule createDailySchedule(Integer paramInteger);
  
  DailyByWeekdaySchedule createDailyByWeekdaySchedule();
  
  Deal createDeal();
  
  DeliveryTimeSlot createDeliveryTimeSlot();
  
  Differentiator createDifferentiator();
  
  DifferentiatorType createDiffType();
  
  DirectDelivery createDirectDelivery();
  
  DirectDeliveryCarton createDirectDeliveryCarton();
  
  DirectDeliveryCartonVO createDirectDeliveryCartonVO();
  
  DirectDeliveryCompositeLineItem createDirectDeliveryCompositeLineItem(DirectDeliverySimpleLineItem[] paramArrayOfDirectDeliverySimpleLineItem);
  
  DirectDeliveryQueryFilter createDirectDeliveryQueryFilter();
  
  DirectDeliverySimpleLineItem createDirectDeliverySimpleLineItem(StockItem paramStockItem);
  
  DirectDeliveryVO createDirectDeliveryVO();
  
  Finisher createFinisher(String paramString1, String paramString2);
  
  FinisherContactInfo createFinisherContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo);
  
  FinisherVO createFinisherVO(String paramString1, String paramString2, FinisherStatus paramFinisherStatus);
  
  FulfillmentOrderMgmtQueryFilter createFulfillmentOrderMgmtQueryFilter();
  
  FulfillmentOrderMgmtListVO createFulfillmentOrderMgmtListVO();
  
  FulfillmentOrder createFulfillmentOrder();
  
  FulfillmentOrderBin createFulfillmentOrderBin();
  
  FulfillmentOrderContactInfo createFulfillmentOrderContactInfo();
  
  FulfillmentOrderCreateCustomerVO createFulfillmentOrderCreateCustomerVO();
  
  FulfillmentOrderCreateLineItemVO createFulfillmentOrderCreateLineItemVO();
  
  FulfillmentOrderCreateVO createFulfillmentOrderCreateVO();
  
  FulfillmentOrderDelivery createFulfillmentOrderDelivery();
  
  FulfillmentOrderDeliveryVO createFulfillmentOrderDeliveryVO();
  
  FulfillmentOrderDeliveryLineItem createFulfillmentOrderDeliveryLineItem();
  
  FulfillmentOrderDeliveryQueryFilter createFulfillmentOrderDeliveryQueryFilter();
  
  FulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem);
  
  FulfillmentOrderPick createFulfillmentOrderPick();
  
  FulfillmentOrderPickLineItem createFulfillmentOrderPickLineItem(StockItem paramStockItem);
  
  FulfillmentOrderPickQueryFilter createFulfillmentOrderPickQueryFilter();
  
  FulfillmentOrderPickVO createFulfillmentOrderPickVO();
  
  FulfillmentOrderPossiblePickVO createFulfillmentOrderPossiblePickVO();
  
  FulfillmentOrderReversePick createFulfillmentOrderReversePick();
  
  FulfillmentOrderReversePickLineItem createFulfillmentOrderReversePickLineItem();
  
  FulfillmentOrderReversePickVO createFulfillmentOrderReversePickVO();
  
  FulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter();
  
  FulfillmentOrderVO createFulfillmentOrderVO();
  
  FutureCountQueryFilter createFutureCountQueryFilter();
  
  FuturePriceVO createFuturePriceVO();
  
  InventoryAdjustment createInventoryAdjustment();
  
  InventoryAdjustmentLineItem createInventoryAdjustmentLineItem(StockItem paramStockItem);
  
  InventoryAdjustmentQueryFilter createInventoryAdjustmentQueryFilter();
  
  InventoryAdjustmentVO createInventoryAdjustmentVO();
  
  InventoryAdjustmentReason createInventoryAdjustmentReason();
  
  InventoryAdjustmentTemplate createInventoryAdjustmentTemplate();
  
  InventoryAdjustmentTemplateLineItem createInventoryAdjustmentTemplateLineItem(StockItem paramStockItem);
  
  InventoryAdjustmentTemplateQueryFilter createInventoryAdjustmentTemplateQueryFilter();
  
  InventoryAdjustmentTemplateVO createInventoryAdjustmentTemplateVO();
  
  Item createItem(String paramString);
  
  StoreItemStockVO createItemAvailableBuddyStockVO();
  
  ItemAvailableStockVO createItemAvailableStockVO();
  
  ItemBasket createItemBasket();
  
  ItemBasketLineItem createItemBasketLineItem();
  
  ItemBasketType createItemBasketType(Integer paramInteger, String paramString);
  
  ItemFulfillmentOrderVO createItemCustomerOrderVO();
  
  ItemDetailVO createItemDetailVO(StockItem paramStockItem);
  
  ItemDiffVO createItemDiffVO(String paramString);
  
  ItemInventoryStockVO createItemInventoryStockVO(StockItem paramStockItem);
  
  ItemVO createItemVO(String paramString);
  
  ItemVOByInventoryQueryFilter createItemVOByInventoryQueryFilter();
  
  ItemVOByItemQueryFilter createItemVOByItemQueryFilter();
  
  ItemVOBySupplierQueryFilter createItemVOBySupplierQueryFilter();
  
  ItemVOByWarehouseQueryFilter createItemVOByWarehouseQueryFilter();
  
  ItemVOByFinisherQueryFilter createItemVOByFinisherQueryFilter();
  
  ItemVOByUDAQueryFilter createItemVOByUDAQueryFilter();
  
  ItemVOByDiffQueryFilter createItemVOByDiffQueryFilter();
  
  ItemImageInfo createItemImageInfo();
  
  ItemRequest createItemRequest(Long paramLong);
  
  ItemRequestVO createItemRequestVO(Long paramLong);
  
  ItemRequestLineItem createItemRequestLineItem(OrderItem paramOrderItem);
  
  ItemRequestQueryFilter createItemRequestQueryFilter();
  
  ItemPrice createItemPrice(Long paramLong);
  
  ItemPriceVO createItemPriceVO(Long paramLong);
  
  ItemCurrentPriceVO createItemCurrentPriceVO(Long paramLong, String paramString);
  
  ItemPriceQueryFilter createItemPriceQueryFilter();
  
  ItemSale createItemSale();
  
  ItemTicket createItemTicket(RetailItem paramRetailItem);
  
  ItemTicketQueryFilter createItemTicketQueryFilter(Long paramLong);
  
  ItemUDA createItemUDA();
  
  ItemUDAVO createItemUDAVO(String paramString);
  
  ManifestOpenShipmentQueryFilter createManifestOpenShipmentQueryFilter();
  
  MdseHierarchyNode createMdseHierarchyNode();
  
  MonthlyByDaySchedule createMonthlyByDaySchedule();
  
  MonthlyByDaySchedule createMonthlyByDaySchedule(Integer paramInteger1, Integer paramInteger2);
  
  MonthlyByWeekSchedule createMonthlyByWeekSchedule();
  
  MonthlyByWeekSchedule createMonthlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  MpsStagedMessageQueryFilter createMpsStagedMessageQueryFilter();
  
  MpsStagedMessage createMpsStagedMessage();
  
  MpsStagedMessageVO createMpsStagedMessageVO();
  
  MpsWorkerType createMpsWorkerType();
  
  MpsWorkerTypeVO createMpsWorkerTypeVO();
  
  NavigationData createNavigationData();
  
  NonSellableQtyType createNonSellableQtyType(Long paramLong, String paramString);
  
  Note createNote();
  
  OrderItem createOrderItem(Item paramItem, Long paramLong);
  
  PackHeaderVO createPackHeaderVO();
  
  PasswordConfiguration createPasswordConfiguration();
  
  Permission createPermission();
  
  PermissionGroup createPermissionGroup();
  
  PermissionQueryFilter createPermissionQueryFilter();
  
  PermissionGroupQueryFilter createPermissionGroupQueryFilter();
  
  PermissionSet createPermissionSet();
  
  POSTransaction createPOSTransaction();
  
  PostalAddress createPostalAddress();
  
  PriceChange createPriceChange();
  
  PriceInfo createPriceInfo(SimMoney paramSimMoney, Date paramDate);
  
  ProductGroup createProductGroup(ProductGroupType paramProductGroupType);
  
  ProductGroupItem createProductGroupItem(String paramString);
  
  ProductGroupVO createProductGroupVO();
  
  ProductGroupHierarchy createProductGroupHierarchy(Long paramLong1, Long paramLong2, Long paramLong3);
  
  ProductGroupQueryFilter createProductGroupQueryFilter();
  
  ProductGroupSchedule createProductGroupSchedule();
  
  ProductGroupScheduleVO createProductGroupScheduleVO();
  
  ProductGroupScheduleQueryFilter createProductGroupScheduleQueryFilter();
  
  ProductGroupBatchVO createProductGroupBatchRecord(Long paramLong1, Long paramLong2);
  
  PromotionQueryFilter createPromotionQueryFilter();
  
  PromotionVO createPromotionVO(String paramString1, String paramString2);
  
  PurchaseOrder createPurchaseOrder();
  
  PurchaseOrderVO createPurchaseOrderVO();
  
  PurchaseOrderLineItem createPurchaseOrderLineItem(SupplierItem paramSupplierItem);
  
  PurchaseOrderQueryFilter createPurchaseOrderQueryFilter();
  
  RelatedItem createRelatedItem(String paramString);
  
  ReportRequest createReportRequest();
  
  ReportResponse createReportResponse();
  
  ReportTypeFormat createReportTypeFormat();
  
  RetailItem createRetailItem(String paramString, Long paramLong);
  
  StorePrinter createRetailStorePrinter();
  
  ReturnQueryFilter createReturnQueryFilter();
  
  Return createReturn(Long paramLong, SourceType paramSourceType);
  
  Return createReturn(Long paramLong, SourceType paramSourceType, boolean paramBoolean);
  
  ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType);
  
  ReturnLineItem createReturnLineItem(StockItem paramStockItem, SourceType paramSourceType, boolean paramBoolean);
  
  ReturnReason createReturnReason();
  
  ReturnVO createReturnVO();
  
  Role createRole();
  
  RoleQueryFilter createRoleQueryFilter();
  
  RoleType createRoleType();
  
  SaleItem createSaleItem(String paramString);
  
  SerialNumberValue createSerialNumberValue();
  
  SessionPrinter createSessionPrinter();
  
  ShelfReplenishment createShelfReplenishment();
  
  ShelfReplenishment createShelfReplenishment(Long paramLong);
  
  ShelfReplenishmentGroupVO createShelfReplenishmentGroupVO();
  
  ShelfReplenishmentVO createShelfReplenishmentVO(Long paramLong);
  
  ShelfReplenishmentLineItem createShelfReplenishmentLineItem(StockItem paramStockItem);
  
  ShelfReplenishmentLineItem createShelfReplenishmentLineItem(Long paramLong, StockItem paramStockItem);
  
  ShelfReplenishmentQueryFilter createShelfReplenishmentQueryFilter();
  
  ShipmentCarrier createShipmentCarrier();
  
  ShipmentCarrierService createShipmentCarrierService();
  
  ShipmentCartonType createShipmentCartonType();
  
  ShipmentWeightUom createShipmentWeightUom();
  
  SourceQueryFilter createSourceQueryFilter();
  
  StockCount createStockCount(Long paramLong);
  
  StockCountImport createStockCountImport();
  
  StockCountImportExtract createStockCountImportExtract();
  
  StockCountItem createStockCountItem(String paramString, Long paramLong);
  
  StockCountLineItemAreaBreakdownVO createStockCountLineItemAreaBreakdownVO();
  
  StockCountLineItemCompBreakdownVO createStockCountLineItemCompBreakdownVO();
  
  StockCountAuthorizeSerialNumber createStockCountLineItemAuthUINVO();
  
  StockCountGroupVO createStockCountGroupVO();
  
  StockCountQueryFilter createStockCountQueryFilter();
  
  StockCountLineItem createStockCountLineItem(Long paramLong1, Long paramLong2, StockCountItem paramStockCountItem);
  
  StockCountChild createStockCountChild(Long paramLong);
  
  StockCountRejectedLineItem createStockCountRejectedLineItem(Long paramLong);
  
  StockCountSerialNumber createStockCountSerialNumber(Long paramLong, String paramString);
  
  StockInfo createStockInfo();
  
  StockItem createStockItem(Item paramItem, Long paramLong);
  
  Store createStore(Long paramLong);
  
  StoreOrder createStoreOrder();
  
  StoreOrder createStoreOrder(String paramString);
  
  StoreOrderLineItem createStoreOrderLineItem();
  
  StoreOrderLineItem createStoreOrderLineItem(OrderItem paramOrderItem);
  
  StoreOrderLineItem createStoreOrderLineItem(String paramString, OrderItem paramOrderItem);
  
  StoreOrderQueryFilter createStoreOrderQueryFilter();
  
  StoreSequenceArea createStoreSequenceArea();
  
  StoreSequenceItem createStoreSequenceItem();
  
  StoreSequenceItemQueryFilter createStoreSequenceItemQueryFilter();
  
  Supplier createSupplier(String paramString1, String paramString2);
  
  SupplierContactInfo createSupplierContactInfo(AddressType paramAddressType, ContactInfo paramContactInfo);
  
  SupplierItem createSupplierItem(String paramString1, String paramString2);
  
  SupplierVO createSupplierVO(String paramString1, String paramString2, SupplierStatus paramSupplierStatus);
  
  TicketType createTicketType();
  
  TicketTypeFormat createTicketTypeFormat();
  
  ToleranceAdmin createToleranceAdmin();
  
  ToleranceVO createToleranceVO();
  
  TransactionHistoryQueryFilter createTransactionHistoryQueryFilter();
  
  TransactionHistoryVO createTransactionHistoryVO();
  
  Transfer createTransfer();
  
  TransferAllocationVO createTransferAllocationVO();
  
  TransferLineItem createTransferLineItem(StockItem paramStockItem);
  
  TransferQueryFilter createTransferQueryFilter();
  
  TransferSerialNumber createTransferSerialNumber();
  
  TransferVO createTransferVO();
  
  UDADetail createUDADetail();
  
  UDAValue createUDAValue();
  
  UINAttributeImport createUINAttributeImport();
  
  UINDetail createUINDetail(Long paramLong);
  
  UINDetailLookupQueryFilter createUINDetailLookupQueryFilter();
  
  UINDetailVO createUINDetailLookupVO();
  
  UINSelectVO createUINDetailSelectVO();
  
  UINHistoryVO createUINHistoryVO();
  
  UINProblemDetail createUINProblemDetail();
  
  UINStoreDept createUINStoreDept();
  
  UINStoreItem createUINStoreItem(Long paramLong);
  
  UINProblemDetailQueryFilter createUINResolutionQueryFilter();
  
  User createUser();
  
  UserLoginVO createUserLoginVO();
  
  UserPassword createUserPassword();
  
  UserPasswordGenerator createUserPasswordGenerator();
  
  UserQueryFilter createUserQueryFilter();
  
  UserRole createUserRole();
  
  UserRolesSaveVO createUserRolesSaveVO();
  
  UserSaveVO createUserSaveVO();
  
  UserStore createUserStore();
  
  UserStoresSaveVO createUserStoresSaveVO();
  
  UsernameGenerator createUsernameGenerator();
  
  Warehouse createWarehouse(String paramString1, String paramString2);
  
  WarehouseVO createWarehouseVO(String paramString1, String paramString2);
  
  WarehouseDetailVO createWarehouseDetailVO(String paramString1, String paramString2);
  
  WarehouseDelivery createWarehouseDelivery();
  
  WarehouseDeliveryVO createWarehouseDeliveryVO();
  
  WarehouseDeliveryCarton createWarehouseDeliveryCarton();
  
  WarehouseDeliveryCartonQueryFilter createWarehouseDeliveryCartonQueryFilter();
  
  WarehouseDeliveryCartonVO createWarehouseDeliveryCartonVO();
  
  WarehouseDeliveryQuickVO createWarehouseDeliveryQuickVO();
  
  WarehouseDeliveryCompositeLineItem createWarehouseDeliveryCompositeLineItem(WarehouseDeliverySimpleLineItem[] paramArrayOfWarehouseDeliverySimpleLineItem);
  
  WarehouseDeliveryQueryFilter createWarehouseDeliveryQueryFilter();
  
  WarehouseDeliverySimpleLineItem createWarehouseDeliverySimpleLineItem(StockItem paramStockItem);
  
  WeeklySchedule createWeeklySchedule();
  
  WeeklySchedule createWeeklySchedule(Integer paramInteger1, Integer paramInteger2);
  
  WeeklySchedule createWeeklySchedule(Integer paramInteger, Set<Integer> paramSet);
  
  YearlyByWeekSchedule createYearlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  YearlyByDaySchedule createYearlyByDaySchedule();
  
  YearlyByDaySchedule createYearlyByDaySchedule(Integer paramInteger1, Integer paramInteger2);
  
  YearlyByWeekSchedule createYearlyByWeekSchedule();
  
  DirectDeliveryReportRequest createDirectDeliveryReportRequest(Long paramLong);
  
  FulfillmentOrderReportRequest createFulfillmentOrderReportRequest(Long paramLong);
  
  FulfillmentOrderBinReportRequest createFulfillmentOrderBinReportRequest(String paramString, Long paramLong);
  
  FulfillmentOrderDeliveryReportRequest createFulfillmentOrderDeliveryReportRequest(Long paramLong);
  
  FulfillmentOrderPickReportRequest createFulfillmentOrderPickReportRequest(Long paramLong);
  
  FulfillmentOrderReversePickReportRequest createFulfillmentOrderReversePickReportRequest(Long paramLong);
  
  InventoryAdjustmentReportRequest createInventoryAdjustmentReportRequest(Long paramLong);
  
  ItemReportRequest createItemReportRequest(String paramString);
  
  ItemBasketReportRequest createItemBasketReportRequest(Long paramLong);
  
  ItemRequestReportRequest createItemRequestReportRequest(Long paramLong);
  
  ItemTicketReportRequest createItemTicketReportRequest();
  
  ReturnReportRequest createReturnReportRequest(Long paramLong);
  
  ShelfReplenishmentReportRequest createShelfReplenishmentReportRequest(Long paramLong);
  
  StockCountReportRequest createStockCountReportRequest();
  
  StockCountRejectedItemReportRequest createStockCountRejectedItemReportRequest(Long paramLong);
  
  StoreOrderReportRequest createStoreOrderReportRequest(String paramString);
  
  TransferReportRequest createTransferReportRequest(Long paramLong);
  
  UinReportRequest createUinReportRequest();
  
  WarehouseDeliveryReportRequest createWarehouseDeliveryReportRequest(Long paramLong);
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BOFactoryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */