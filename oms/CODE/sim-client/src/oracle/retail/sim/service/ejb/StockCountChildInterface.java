package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.stockcount.StockCountChild;

@Remote
public interface StockCountChildInterface {
  CompressedObject<?> applySales(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<StockCountChild>> findAllReadyToApproveStockCountChilds(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<StockCountChild>> findStockCountChilds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> hasOpenInventoryAdjustmentItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Boolean> isLocationLineItemsUncounted(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> markStockCountChildApproved(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> markStockCountChildCounted(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> markStockCountChildRecounted(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> markStockCountChildsReadyToApprove(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> markStockCountChildsReadyToApprove2(CompressedObject<Long> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> markStockCountChildsStarted(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> markStockCountChildsStarted2(CompressedObject<Long> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<StockCountChild> readStockCountChild(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateAuthorizationQuantities(CompressedObject<Long> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StockCountChildInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */