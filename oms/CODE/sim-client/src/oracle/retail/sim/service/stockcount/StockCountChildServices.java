package oracle.retail.sim.service.stockcount;

import java.util.List;
import oracle.retail.sim.common.stockcount.StockCountChild;

public abstract class StockCountChildServices {
  public abstract StockCountChild readStockCountChild(Long paramLong) throws Exception;
  
  public abstract List<StockCountChild> findStockCountChilds(Long paramLong) throws Exception;
  
  public abstract List<StockCountChild> findAllReadyToApproveStockCountChilds() throws Exception;
  
  public abstract void markStockCountChildsStarted(Long paramLong) throws Exception;
  
  public abstract void markStockCountChildsStarted(Long paramLong, List<Long> paramList) throws Exception;
  
  public abstract void markStockCountChildCounted(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void markStockCountChildRecounted(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void markStockCountChildsReadyToApprove(Long paramLong) throws Exception;
  
  public abstract void markStockCountChildsReadyToApprove(Long paramLong, List<Long> paramList) throws Exception;
  
  public abstract void markStockCountChildApproved(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void updateAuthorizationQuantities(Long paramLong, List<Long> paramList) throws Exception;
  
  public abstract void applySales(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract boolean isLocationLineItemsUncounted(Long paramLong1, Long paramLong2, List<Long> paramList) throws Exception;
  
  public abstract boolean hasOpenInventoryAdjustmentItems(Long paramLong1, Long paramLong2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\stockcount\StockCountChildServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */