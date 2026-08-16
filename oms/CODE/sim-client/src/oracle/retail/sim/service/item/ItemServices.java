package oracle.retail.sim.service.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public abstract class ItemServices {
  public abstract StockItem readStockItem(String paramString, Long paramLong) throws Exception;
  
  public abstract StockItem readStockItemOrCreate(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, StockItem> readStockItems(Collection<String> paramCollection, Long paramLong) throws Exception;
  
  public abstract Map<String, StockItem> readStockItemsOrCreate(Collection<String> paramCollection, Long paramLong) throws Exception;
  
  public abstract List<StockItem> findStockItems(Long paramLong1, Long paramLong2, Long paramLong3) throws Exception;
  
  public abstract List<StockItem> findStockItems(String paramString, Long paramLong) throws Exception;
  
  public abstract BarcodeInfo findBarcodeInfoForItemScan(String paramString, Long paramLong) throws Exception;
  
  public abstract BarcodeInfo findBarcodeInfoForUinScan(String paramString1, Long paramLong, String paramString2) throws Exception;
  
  public abstract List<OrderItem> findOrderItem(String paramString, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract OrderItem readOrderItem(String paramString, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract List<RetailItem> findRetailItems(String paramString, Long paramLong) throws Exception;
  
  public abstract RetailItem readRetailItem(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, RetailItem> readRetailItems(List<String> paramList, Long paramLong) throws Exception;
  
  public abstract RetailItem readRetailItemOrCreate(String paramString, Long paramLong) throws Exception;
  
  public abstract SaleItem readSaleItem(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, SaleItem> readSaleItems(Collection<String> paramCollection, Long paramLong) throws Exception;
  
  public abstract ProductGroupItem readProductGroupItem(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ProductGroupItem> readProductGroupItems(Collection<String> paramCollection, Long paramLong) throws Exception;
  
  public abstract ProductGroupItem readProductGroupItemOrCreate(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ProductGroupItem> findProductGroupItems(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ProductGroupItem> findProductGroupItemsByPromotion(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract List<ProductGroupItem> findProductGroupItemsBySupplier(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ItemVO> findItemVOs(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ItemVO> findItemVOs(ItemVOQueryFilter paramItemVOQueryFilter, Long paramLong) throws Exception;
  
  public abstract List<ItemVO> findItemVOs(Collection<String> paramCollection, Long paramLong) throws Exception;
  
  public abstract List<ItemVO> findPackItemVOsContainingItem(String paramString, Long paramLong) throws Exception;
  
  public abstract List<SupplierItem> findSupplierItems(String paramString) throws Exception;
  
  public abstract List<SupplierItem> findSupplierItems(String paramString1, String paramString2) throws Exception;
  
  public abstract SupplierItem readSupplierItem(String paramString1, String paramString2, String paramString3) throws Exception;
  
  public abstract boolean isItemSuppliedBySupplier(String paramString1, String paramString2) throws Exception;
  
  public abstract boolean isItemAssociatedWithFinisher(String paramString1, String paramString2) throws Exception;
  
  public abstract boolean isItemShippedByWarehouse(String paramString1, String paramString2) throws Exception;
  
  public abstract boolean isRanged(String paramString, Long paramLong) throws Exception;
  
  public abstract boolean isMultipleDeliveryAllowed(String paramString, Long paramLong) throws Exception;
  
  public abstract ItemDetailVO readItemDetailVO(String paramString, Long paramLong) throws Exception;
  
  public abstract ItemDiffVO readItemDiffVO(String paramString) throws Exception;
  
  public abstract ItemStatus readItemStatus(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, String> findPrimaryProductCodes(Set<String> paramSet) throws Exception;
  
  public abstract List<PackHeaderVO> findPackHeaderVOsContainingItem(String paramString, Long paramLong) throws Exception;
  
  public abstract List<RelatedItem> findRelatedItems(String paramString1, String paramString2, Long paramLong) throws Exception;
  
  public abstract List<RelatedItem> findRelatedItems(ItemVOByDiffQueryFilter paramItemVOByDiffQueryFilter, Long paramLong) throws Exception;
  
  public abstract List<StoreItemStockVO> findStoreItemStockVOs(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ItemAvailableStockVO> findItemAvailableStockVOs(List<String> paramList, List<Long> paramList1) throws Exception;
  
  public abstract List<ItemInventoryStockVO> findItemInventoryStockVOs(List<String> paramList, Set<Long> paramSet, UOMInventoryType paramUOMInventoryType) throws Exception;
  
  public abstract List<ItemSuppCtryMfrVO> findCountriesOfManufacture(String paramString, Long paramLong) throws Exception;
  
  public abstract ItemSuppCtryMfrVO findDefaultCountryOfManufacture(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, Double> findWastePercentage(Set<String> paramSet) throws Exception;
  
  public abstract List<ItemImage> findItemImages(String paramString) throws Exception;
  
  public abstract List<String> findItemImageURLs(String paramString) throws Exception;
  
  public abstract void saveItemImages(List<ItemImageInfo> paramList, Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\item\ItemServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */