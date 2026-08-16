package oracle.retail.sim.service.core;

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

public interface ServiceFactoryInterface {
  ActivityLockServices getActivityLockServices();
  
  ActivityHistoryServices getActivityHistoryServices();
  
  BatchServices getBatchServices();
  
  ConfigServices getConfigServices();
  
  CustomThemeServices getCustomThemeServices();
  
  CustomUINServices getCustomUINServices();
  
  DealServices getDealServices();
  
  DirectDeliveryServices getDirectDeliveryServices();
  
  FulfillmentOrderServices getFulfillmentOrderServices();
  
  FulfillmentOrderDeliveryServices getFulfillmentOrderDeliveryServices();
  
  FulfillmentOrderPickServices getFulfillmentOrderPickServices();
  
  FulfillmentOrderReversePickServices getFulfillmentOrderReversePickServices();
  
  InventoryAdjustmentServices getInventoryAdjustmentServices();
  
  ItemServices getItemServices();
  
  ItemBasketServices getItemBasketServices();
  
  ItemPriceServices getItemPriceServices();
  
  ItemRequestServices getItemRequestServices();
  
  ItemTicketServices getItemTicketServices();
  
  MdseHierarchyServices getMdseHierarchyServices();
  
  MpsServices getMpsServices();
  
  NoteServices getNoteServices();
  
  ShelfReplenishmentServices getShelfReplenishmentServices();
  
  POSTransactionServices getPOSTransactionServices();
  
  ProductGroupServices getProductGroupServices();
  
  ProductGroupScheduleServices getProductGroupScheduleServices();
  
  ReportFormatServices getReportFormatServices();
  
  ReportingServices getReportingServices();
  
  ReturnServices getReturnServices();
  
  SecurityServices getSecurityServices();
  
  ShipmentServices getShipmentServices();
  
  SourceServices getSourceServices();
  
  StockCountServices getStockCountServices();
  
  StockCountLineItemServices getStockCountLineItemServices();
  
  StockCountChildServices getStockCountChildServices();
  
  StoreSequenceServices getStoreSequenceServices();
  
  StoreServices getStoreServices();
  
  StoreOrderServices getStoreOrderServices();
  
  ToleranceAdminServices getToleranceAdminServices();
  
  TransactionHistoryServices getTransactionHistoryServices();
  
  TransferServices getTransferServices();
  
  TranslationServices getTranslationServices();
  
  UDAServices getUDAServices();
  
  UINServices getUINServices();
  
  UOMServices getUOMServices();
  
  WarehouseDeliveryServices getWarehouseDeliveryServices();
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\core\ServiceFactoryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */