package oracle.retail.sim.service.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.activityhistory.ActivityHistoryEJBServices;
import oracle.retail.sim.service.activityhistory.ActivityHistoryServices;
import oracle.retail.sim.service.activitylock.ActivityLockEJBServices;
import oracle.retail.sim.service.activitylock.ActivityLockServices;
import oracle.retail.sim.service.batch.BatchEJBServices;
import oracle.retail.sim.service.batch.BatchServices;
import oracle.retail.sim.service.config.ConfigEJBServices;
import oracle.retail.sim.service.config.ConfigServices;
import oracle.retail.sim.service.customuin.CustomUINEJBServices;
import oracle.retail.sim.service.customuin.CustomUINServices;
import oracle.retail.sim.service.deals.DealEJBServices;
import oracle.retail.sim.service.deals.DealServices;
import oracle.retail.sim.service.directdelivery.DirectDeliveryEJBServices;
import oracle.retail.sim.service.directdelivery.DirectDeliveryServices;
import oracle.retail.sim.service.fulfillmentorder.FulfillmentOrderEJBServices;
import oracle.retail.sim.service.fulfillmentorder.FulfillmentOrderServices;
import oracle.retail.sim.service.fulfillmentorderdelivery.FulfillmentOrderDeliveryEJBServices;
import oracle.retail.sim.service.fulfillmentorderdelivery.FulfillmentOrderDeliveryServices;
import oracle.retail.sim.service.fulfillmentorderpick.FulfillmentOrderPickEJBServices;
import oracle.retail.sim.service.fulfillmentorderpick.FulfillmentOrderPickServices;
import oracle.retail.sim.service.fulfillmentorderreversepick.FulfillmentOrderReversePickEJBServices;
import oracle.retail.sim.service.fulfillmentorderreversepick.FulfillmentOrderReversePickServices;
import oracle.retail.sim.service.invadjustment.InventoryAdjustmentEJBServices;
import oracle.retail.sim.service.invadjustment.InventoryAdjustmentServices;
import oracle.retail.sim.service.item.ItemEJBServices;
import oracle.retail.sim.service.item.ItemServices;
import oracle.retail.sim.service.itembasket.ItemBasketEJBServices;
import oracle.retail.sim.service.itembasket.ItemBasketServices;
import oracle.retail.sim.service.itemprice.ItemPriceEJBServices;
import oracle.retail.sim.service.itemprice.ItemPriceServices;
import oracle.retail.sim.service.itemrequest.ItemRequestEJBServices;
import oracle.retail.sim.service.itemrequest.ItemRequestServices;
import oracle.retail.sim.service.itemticket.ItemTicketEJBServices;
import oracle.retail.sim.service.itemticket.ItemTicketServices;
import oracle.retail.sim.service.mdsehierarchy.MdseHierarchyEJBServices;
import oracle.retail.sim.service.mdsehierarchy.MdseHierarchyServices;
import oracle.retail.sim.service.mps.MpsEJBServices;
import oracle.retail.sim.service.mps.MpsServices;
import oracle.retail.sim.service.notes.NoteEJBServices;
import oracle.retail.sim.service.notes.NoteServices;
import oracle.retail.sim.service.postransaction.POSTransactionEJBServices;
import oracle.retail.sim.service.postransaction.POSTransactionServices;
import oracle.retail.sim.service.productgroup.ProductGroupEJBServices;
import oracle.retail.sim.service.productgroup.ProductGroupServices;
import oracle.retail.sim.service.report.ReportingEJBServices;
import oracle.retail.sim.service.report.ReportingServices;
import oracle.retail.sim.service.reportformat.ReportFormatEJBServices;
import oracle.retail.sim.service.reportformat.ReportFormatServices;
import oracle.retail.sim.service.schedule.ProductGroupScheduleEJBServices;
import oracle.retail.sim.service.schedule.ProductGroupScheduleServices;
import oracle.retail.sim.service.security.SecurityEJBServices;
import oracle.retail.sim.service.security.SecurityServices;
import oracle.retail.sim.service.shelfreplenishment.ShelfReplenishmentEJBServices;
import oracle.retail.sim.service.shelfreplenishment.ShelfReplenishmentServices;
import oracle.retail.sim.service.shipment.ShipmentEJBServices;
import oracle.retail.sim.service.shipment.ShipmentServices;
import oracle.retail.sim.service.source.SourceEJBServices;
import oracle.retail.sim.service.source.SourceServices;
import oracle.retail.sim.service.stockcount.StockCountChildEJBServices;
import oracle.retail.sim.service.stockcount.StockCountChildServices;
import oracle.retail.sim.service.stockcount.StockCountEJBServices;
import oracle.retail.sim.service.stockcount.StockCountLineItemEJBServices;
import oracle.retail.sim.service.stockcount.StockCountLineItemServices;
import oracle.retail.sim.service.stockcount.StockCountServices;
import oracle.retail.sim.service.stockreturn.ReturnEJBServices;
import oracle.retail.sim.service.stockreturn.ReturnServices;
import oracle.retail.sim.service.store.StoreEJBServices;
import oracle.retail.sim.service.store.StoreServices;
import oracle.retail.sim.service.storeorder.StoreOrderEJBServices;
import oracle.retail.sim.service.storeorder.StoreOrderServices;
import oracle.retail.sim.service.storesequence.StoreSequenceEJBServices;
import oracle.retail.sim.service.storesequence.StoreSequenceServices;
import oracle.retail.sim.service.theme.CustomThemeEJBServices;
import oracle.retail.sim.service.theme.CustomThemeServices;
import oracle.retail.sim.service.tolerance.ToleranceAdminEJBServices;
import oracle.retail.sim.service.tolerance.ToleranceAdminServices;
import oracle.retail.sim.service.tranhistory.TransactionHistoryEJBServices;
import oracle.retail.sim.service.tranhistory.TransactionHistoryServices;
import oracle.retail.sim.service.transfer.TransferEJBServices;
import oracle.retail.sim.service.transfer.TransferServices;
import oracle.retail.sim.service.translation.TranslationEJBServices;
import oracle.retail.sim.service.translation.TranslationServices;
import oracle.retail.sim.service.uda.UDAEJBServices;
import oracle.retail.sim.service.uda.UDAServices;
import oracle.retail.sim.service.uin.UINEJBServices;
import oracle.retail.sim.service.uin.UINServices;
import oracle.retail.sim.service.uom.UOMEJBServices;
import oracle.retail.sim.service.uom.UOMServices;
import oracle.retail.sim.service.warehousedelivery.WarehouseDeliveryEJBServices;
import oracle.retail.sim.service.warehousedelivery.WarehouseDeliveryServices;

public class ClientServiceFactoryImpl implements ServiceFactoryInterface {
  private static Map<Class<?>, Object> cache = new ConcurrentHashMap<>();
  
  private static <T> T getService(Class<T> paramClass) {
    Object object = cache.get(paramClass);
    if (object == null)
      try {
        object = paramClass.newInstance();
        cache.put(paramClass, object);
      } catch (Throwable throwable) {
        LogService.error(ClientServiceFactoryImpl.class, "Could not create client service: " + paramClass, throwable);
        throw new RuntimeException("Could not create client service: " + paramClass);
      }  
    return (T)object;
  }
  
  public ActivityLockServices getActivityLockServices() {
    return (ActivityLockServices)getService(ActivityLockEJBServices.class);
  }
  
  public ActivityHistoryServices getActivityHistoryServices() {
    return (ActivityHistoryServices)getService(ActivityHistoryEJBServices.class);
  }
  
  public BatchServices getBatchServices() {
    return (BatchServices)getService(BatchEJBServices.class);
  }
  
  public ConfigServices getConfigServices() {
    return (ConfigServices)getService(ConfigEJBServices.class);
  }
  
  public CustomThemeServices getCustomThemeServices() {
    return (CustomThemeServices)getService(CustomThemeEJBServices.class);
  }
  
  public CustomUINServices getCustomUINServices() {
    return (CustomUINServices)getService(CustomUINEJBServices.class);
  }
  
  public DealServices getDealServices() {
    return (DealServices)getService(DealEJBServices.class);
  }
  
  public DirectDeliveryServices getDirectDeliveryServices() {
    return (DirectDeliveryServices)getService(DirectDeliveryEJBServices.class);
  }
  
  public FulfillmentOrderServices getFulfillmentOrderServices() {
    return (FulfillmentOrderServices)getService(FulfillmentOrderEJBServices.class);
  }
  
  public FulfillmentOrderDeliveryServices getFulfillmentOrderDeliveryServices() {
    return (FulfillmentOrderDeliveryServices)getService(FulfillmentOrderDeliveryEJBServices.class);
  }
  
  public FulfillmentOrderPickServices getFulfillmentOrderPickServices() {
    return (FulfillmentOrderPickServices)getService(FulfillmentOrderPickEJBServices.class);
  }
  
  public FulfillmentOrderReversePickServices getFulfillmentOrderReversePickServices() {
    return (FulfillmentOrderReversePickServices)getService(FulfillmentOrderReversePickEJBServices.class);
  }
  
  public InventoryAdjustmentServices getInventoryAdjustmentServices() {
    return (InventoryAdjustmentServices)getService(InventoryAdjustmentEJBServices.class);
  }
  
  public ItemServices getItemServices() {
    return (ItemServices)getService(ItemEJBServices.class);
  }
  
  public ItemBasketServices getItemBasketServices() {
    return (ItemBasketServices)getService(ItemBasketEJBServices.class);
  }
  
  public ItemPriceServices getItemPriceServices() {
    return (ItemPriceServices)getService(ItemPriceEJBServices.class);
  }
  
  public ItemRequestServices getItemRequestServices() {
    return (ItemRequestServices)getService(ItemRequestEJBServices.class);
  }
  
  public ItemTicketServices getItemTicketServices() {
    return (ItemTicketServices)getService(ItemTicketEJBServices.class);
  }
  
  public MdseHierarchyServices getMdseHierarchyServices() {
    return (MdseHierarchyServices)getService(MdseHierarchyEJBServices.class);
  }
  
  public MpsServices getMpsServices() {
    return (MpsServices)getService(MpsEJBServices.class);
  }
  
  public NoteServices getNoteServices() {
    return (NoteServices)getService(NoteEJBServices.class);
  }
  
  public ShelfReplenishmentServices getShelfReplenishmentServices() {
    return (ShelfReplenishmentServices)getService(ShelfReplenishmentEJBServices.class);
  }
  
  public POSTransactionServices getPOSTransactionServices() {
    return (POSTransactionServices)getService(POSTransactionEJBServices.class);
  }
  
  public ProductGroupServices getProductGroupServices() {
    return (ProductGroupServices)getService(ProductGroupEJBServices.class);
  }
  
  public ProductGroupScheduleServices getProductGroupScheduleServices() {
    return (ProductGroupScheduleServices)getService(ProductGroupScheduleEJBServices.class);
  }
  
  public ReportFormatServices getReportFormatServices() {
    return (ReportFormatServices)getService(ReportFormatEJBServices.class);
  }
  
  public ReportingServices getReportingServices() {
    return (ReportingServices)getService(ReportingEJBServices.class);
  }
  
  public ReturnServices getReturnServices() {
    return (ReturnServices)getService(ReturnEJBServices.class);
  }
  
  public SecurityServices getSecurityServices() {
    return (SecurityServices)getService(SecurityEJBServices.class);
  }
  
  public ShipmentServices getShipmentServices() {
    return (ShipmentServices)getService(ShipmentEJBServices.class);
  }
  
  public SourceServices getSourceServices() {
    return (SourceServices)getService(SourceEJBServices.class);
  }
  
  public StockCountServices getStockCountServices() {
    return (StockCountServices)getService(StockCountEJBServices.class);
  }
  
  public StockCountLineItemServices getStockCountLineItemServices() {
    return (StockCountLineItemServices)getService(StockCountLineItemEJBServices.class);
  }
  
  public StockCountChildServices getStockCountChildServices() {
    return (StockCountChildServices)getService(StockCountChildEJBServices.class);
  }
  
  public StoreSequenceServices getStoreSequenceServices() {
    return (StoreSequenceServices)getService(StoreSequenceEJBServices.class);
  }
  
  public StoreServices getStoreServices() {
    return (StoreServices)getService(StoreEJBServices.class);
  }
  
  public StoreOrderServices getStoreOrderServices() {
    return (StoreOrderServices)getService(StoreOrderEJBServices.class);
  }
  
  public ToleranceAdminServices getToleranceAdminServices() {
    return (ToleranceAdminServices)getService(ToleranceAdminEJBServices.class);
  }
  
  public TransactionHistoryServices getTransactionHistoryServices() {
    return (TransactionHistoryServices)getService(TransactionHistoryEJBServices.class);
  }
  
  public TransferServices getTransferServices() {
    return (TransferServices)getService(TransferEJBServices.class);
  }
  
  public TranslationServices getTranslationServices() {
    return (TranslationServices)getService(TranslationEJBServices.class);
  }
  
  public UDAServices getUDAServices() {
    return (UDAServices)getService(UDAEJBServices.class);
  }
  
  public UINServices getUINServices() {
    return (UINServices)getService(UINEJBServices.class);
  }
  
  public UOMServices getUOMServices() {
    return (UOMServices)getService(UOMEJBServices.class);
  }
  
  public WarehouseDeliveryServices getWarehouseDeliveryServices() {
    return (WarehouseDeliveryServices)getService(WarehouseDeliveryEJBServices.class);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\core\ClientServiceFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */