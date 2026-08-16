package oracle.retail.sim.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.item.BarcodeInfo;
import oracle.retail.sim.common.item.ItemAvailableStockVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemDiffVO;
import oracle.retail.sim.common.item.ItemImage;
import oracle.retail.sim.common.item.ItemImageInfo;
import oracle.retail.sim.common.item.ItemInventoryStockVO;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.ItemSuppCtryMfrVO;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOByDiffQueryFilter;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.PackHeaderVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.SaleItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.StoreItemStockVO;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.uom.UOMInventoryType;

@Remote
public interface ItemInterface {
  CompressedObject<BarcodeInfo> findBarcodeInfoForItemScan(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<BarcodeInfo> findBarcodeInfoForUinScan(CompressedObject<String> paramCompressedObject1, CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<ItemSuppCtryMfrVO>> findCountriesOfManufacture(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<ItemSuppCtryMfrVO> findDefaultCountryOfManufacture(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemAvailableStockVO>> findItemAvailableStockVOs(CompressedObject<List<String>> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<String>> findItemImageURLs(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemImage>> findItemImages(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemInventoryStockVO>> findItemInventoryStockVOs(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Set<Long>> paramCompressedObject1, CompressedObject<UOMInventoryType> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<ItemVO>> findItemVOs(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemVO>> findItemVOs2(CompressedObject<Collection<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemVO>> findItemVOs3(CompressedObject<ItemVOQueryFilter> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<OrderItem>> findOrderItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<PackHeaderVO>> findPackHeaderVOsContainingItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemVO>> findPackItemVOsContainingItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, String>> findPrimaryProductCodes(CompressedObject<Set<String>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ProductGroupItem>> findProductGroupItems(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ProductGroupItem>> findProductGroupItemsByPromotion(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ProductGroupItem>> findProductGroupItemsBySupplier(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<RelatedItem>> findRelatedItems(CompressedObject<ItemVOByDiffQueryFilter> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<RelatedItem>> findRelatedItems2(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<RetailItem>> findRetailItems(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StockItem>> findStockItems(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StockItem>> findStockItems2(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Long> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<StoreItemStockVO>> findStoreItemStockVOs(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<SupplierItem>> findSupplierItems(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<SupplierItem>> findSupplierItems2(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<String, Double>> findWastePercentage(CompressedObject<Set<String>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isItemAssociatedWithFinisher(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Boolean> isItemShippedByWarehouse(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Boolean> isItemSuppliedBySupplier(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Boolean> isMultipleDeliveryAllowed(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Boolean> isRanged(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<ItemDetailVO> readItemDetailVO(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<ItemDiffVO> readItemDiffVO(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ItemStatus> readItemStatus(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<OrderItem> readOrderItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<ProductGroupItem> readProductGroupItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<ProductGroupItem> readProductGroupItemOrCreate(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ProductGroupItem>> readProductGroupItems(CompressedObject<Collection<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<RetailItem> readRetailItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<RetailItem> readRetailItemOrCreate(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, RetailItem>> readRetailItems(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<SaleItem> readSaleItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, SaleItem>> readSaleItems(CompressedObject<Collection<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<StockItem> readStockItem(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<StockItem> readStockItemOrCreate(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, StockItem>> readStockItems(CompressedObject<Collection<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, StockItem>> readStockItemsOrCreate(CompressedObject<Collection<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<SupplierItem> readSupplierItem(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> saveItemImages(CompressedObject<List<ItemImageInfo>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ItemInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */