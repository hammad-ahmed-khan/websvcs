package oracle.retail.sim.service.core;

import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.service.activityhistory.ActivityHistoryServices;
import oracle.retail.sim.service.activitylock.ActivityLockServices;
import oracle.retail.sim.service.batch.BatchServices;
import oracle.retail.sim.service.config.ConfigServices;
import oracle.retail.sim.service.customuin.CustomUINServices;
import oracle.retail.sim.service.deals.DealServices;
import oracle.retail.sim.service.directdelivery.DirectDeliveryServices;
import oracle.retail.sim.service.fulfillmentorder.FulfillmentOrderServices;
import oracle.retail.sim.service.fulfillmentorderdelivery.FulfillmentOrderDeliveryServices;
import oracle.retail.sim.service.fulfillmentorderpick.FulfillmentOrderPickServices;
import oracle.retail.sim.service.fulfillmentorderreversepick.FulfillmentOrderReversePickServices;
import oracle.retail.sim.service.invadjustment.InventoryAdjustmentServices;
import oracle.retail.sim.service.item.ItemServices;
import oracle.retail.sim.service.itembasket.ItemBasketServices;
import oracle.retail.sim.service.itemprice.ItemPriceServices;
import oracle.retail.sim.service.itemrequest.ItemRequestServices;
import oracle.retail.sim.service.itemticket.ItemTicketServices;
import oracle.retail.sim.service.mdsehierarchy.MdseHierarchyServices;
import oracle.retail.sim.service.mps.MpsServices;
import oracle.retail.sim.service.notes.NoteServices;
import oracle.retail.sim.service.postransaction.POSTransactionServices;
import oracle.retail.sim.service.productgroup.ProductGroupServices;
import oracle.retail.sim.service.report.ReportingServices;
import oracle.retail.sim.service.reportformat.ReportFormatServices;
import oracle.retail.sim.service.schedule.ProductGroupScheduleServices;
import oracle.retail.sim.service.security.SecurityServices;
import oracle.retail.sim.service.shelfreplenishment.ShelfReplenishmentServices;
import oracle.retail.sim.service.shipment.ShipmentServices;
import oracle.retail.sim.service.source.SourceServices;
import oracle.retail.sim.service.stockcount.StockCountChildServices;
import oracle.retail.sim.service.stockcount.StockCountLineItemServices;
import oracle.retail.sim.service.stockcount.StockCountServices;
import oracle.retail.sim.service.stockreturn.ReturnServices;
import oracle.retail.sim.service.store.StoreServices;
import oracle.retail.sim.service.storeorder.StoreOrderServices;
import oracle.retail.sim.service.storesequence.StoreSequenceServices;
import oracle.retail.sim.service.theme.CustomThemeServices;
import oracle.retail.sim.service.tolerance.ToleranceAdminServices;
import oracle.retail.sim.service.tranhistory.TransactionHistoryServices;
import oracle.retail.sim.service.transfer.TransferServices;
import oracle.retail.sim.service.translation.TranslationServices;
import oracle.retail.sim.service.uda.UDAServices;
import oracle.retail.sim.service.uin.UINServices;
import oracle.retail.sim.service.uom.UOMServices;
import oracle.retail.sim.service.warehousedelivery.WarehouseDeliveryServices;

public class NativeServiceFactory {
  private static ServiceFactoryInterface factory = getDefaultFactory();
  
  public static ServiceFactoryInterface getFactory() {
    return factory;
  }
  
  public static void setFactory(ServiceFactoryInterface paramServiceFactoryInterface) {
    if (paramServiceFactoryInterface == null)
      throw new IllegalArgumentException("ServiceFactory cannot be null!"); 
    factory = paramServiceFactoryInterface;
  }
  
  private static ServiceFactoryInterface getDefaultFactory() {
    return JvmLocation.isServer() ? CommonConfigManager.getServerServiceFactoryImpl() : CommonConfigManager.getClientServiceFactoryImpl();
  }
  
  public static ActivityLockServices getActivityLockServices() {
    return factory.getActivityLockServices();
  }
  
  public static ActivityHistoryServices getActivityHistoryServices() {
    return factory.getActivityHistoryServices();
  }
  
  public static BatchServices getBatchServices() {
    return factory.getBatchServices();
  }
  
  public static ConfigServices getConfigServices() {
    return factory.getConfigServices();
  }
  
  public static CustomThemeServices getCustomThemeServices() {
    return factory.getCustomThemeServices();
  }
  
  public static CustomUINServices getCustomUINServices() {
    return factory.getCustomUINServices();
  }
  
  public static DealServices getDealServices() {
    return factory.getDealServices();
  }
  
  public static DirectDeliveryServices getDirectDeliveryServices() {
    return factory.getDirectDeliveryServices();
  }
  
  public static FulfillmentOrderServices getFulfillmentOrderServices() {
    return factory.getFulfillmentOrderServices();
  }
  
  public static FulfillmentOrderDeliveryServices getFulfillmentOrderDeliveryServices() {
    return factory.getFulfillmentOrderDeliveryServices();
  }
  
  public static FulfillmentOrderPickServices getFulfillmentOrderPickServices() {
    return factory.getFulfillmentOrderPickServices();
  }
  
  public static FulfillmentOrderReversePickServices getFulfillmentOrderReversePickServices() {
    return factory.getFulfillmentOrderReversePickServices();
  }
  
  public static InventoryAdjustmentServices getInventoryAdjustmentServices() {
    return factory.getInventoryAdjustmentServices();
  }
  
  public static ItemServices getItemServices() {
    return factory.getItemServices();
  }
  
  public static ItemBasketServices getItemBasketServices() {
    return factory.getItemBasketServices();
  }
  
  public static ItemPriceServices getItemPriceServices() {
    return factory.getItemPriceServices();
  }
  
  public static ItemRequestServices getItemRequestServices() {
    return factory.getItemRequestServices();
  }
  
  public static ItemTicketServices getItemTicketServices() {
    return factory.getItemTicketServices();
  }
  
  public static MdseHierarchyServices getMdseHierarchyServices() {
    return factory.getMdseHierarchyServices();
  }
  
  public static MpsServices getMpsServices() {
    return factory.getMpsServices();
  }
  
  public static NoteServices getNoteServices() {
    return factory.getNoteServices();
  }
  
  public static ShelfReplenishmentServices getShelfReplenishmentServices() {
    return factory.getShelfReplenishmentServices();
  }
  
  public static POSTransactionServices getPOSTransactionServices() {
    return factory.getPOSTransactionServices();
  }
  
  public static ProductGroupServices getProductGroupServices() {
    return factory.getProductGroupServices();
  }
  
  public static ProductGroupScheduleServices getProductGroupScheduleServices() {
    return factory.getProductGroupScheduleServices();
  }
  
  public static ReportFormatServices getReportFormatServices() {
    return factory.getReportFormatServices();
  }
  
  public static ReportingServices getReportingServices() {
    return factory.getReportingServices();
  }
  
  public static ReturnServices getReturnServices() {
    return factory.getReturnServices();
  }
  
  public static SecurityServices getSecurityServices() {
    return factory.getSecurityServices();
  }
  
  public static ShipmentServices getShipmentServices() {
    return factory.getShipmentServices();
  }
  
  public static SourceServices getSourceServices() {
    return factory.getSourceServices();
  }
  
  public static StockCountServices getStockCountServices() {
    return factory.getStockCountServices();
  }
  
  public static StockCountLineItemServices getStockCountLineItemServices() {
    return factory.getStockCountLineItemServices();
  }
  
  public static StockCountChildServices getStockCountChildServices() {
    return factory.getStockCountChildServices();
  }
  
  public static StoreSequenceServices getStoreSequenceServices() {
    return factory.getStoreSequenceServices();
  }
  
  public static StoreServices getStoreServices() {
    return factory.getStoreServices();
  }
  
  public static StoreOrderServices getStoreOrderServices() {
    return factory.getStoreOrderServices();
  }
  
  public static ToleranceAdminServices getToleranceAdminServices() {
    return factory.getToleranceAdminServices();
  }
  
  public static TransactionHistoryServices getTransactionHistoryServices() {
    return factory.getTransactionHistoryServices();
  }
  
  public static TransferServices getTransferServices() {
    return factory.getTransferServices();
  }
  
  public static TranslationServices getTranslationServices() {
    return factory.getTranslationServices();
  }
  
  public static UDAServices getUDAServices() {
    return factory.getUDAServices();
  }
  
  public static UINServices getUINServices() {
    return factory.getUINServices();
  }
  
  public static UOMServices getUOMServices() {
    return factory.getUOMServices();
  }
  
  public static WarehouseDeliveryServices getWarehouseDeliveryServices() {
    return factory.getWarehouseDeliveryServices();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\core\NativeServiceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */