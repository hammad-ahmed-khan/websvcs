package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.stockcount.FutureCountQueryFilter;
import oracle.retail.sim.common.stockcount.FutureCountVO;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;

@Remote
public interface StockCountInterface {
  CompressedObject<StockCount> createAdhocStockCount(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> delete(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FutureCountVO>> findFutureStockCounts(CompressedObject<FutureCountQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StockCount>> findOpenAdhocStockCounts(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StockCount>> findOpenStockCounts(CompressedObject<Long> paramCompressedObject, CompressedObject<StockCountPhase> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StockCount>> findStockCounts(CompressedObject<StockCountQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StockCount> generateFutureStockCount(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Date> paramCompressedObject, CompressedObject<ProductGroupType> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<Long> generateStockCounts(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Date> paramCompressedObject, CompressedObject<ProductGroupType> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<Boolean> hasOpenStockCountItems(CompressedObject<Long> paramCompressedObject, CompressedObject<Set<String>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> processThirdPartyNonRangedItems(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StockCount> readStockCount(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> update(CompressedObject<StockCount> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StockCountInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */