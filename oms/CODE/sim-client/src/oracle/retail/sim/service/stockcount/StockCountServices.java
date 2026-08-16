package oracle.retail.sim.service.stockcount;

import java.util.Date;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.stockcount.FutureCountQueryFilter;
import oracle.retail.sim.common.stockcount.FutureCountVO;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;

public abstract class StockCountServices {
  public abstract StockCount readStockCount(Long paramLong) throws Exception;
  
  public abstract List<StockCount> findStockCounts(StockCountQueryFilter paramStockCountQueryFilter) throws Exception;
  
  public abstract List<StockCount> findOpenStockCounts(Long paramLong, StockCountPhase paramStockCountPhase) throws Exception;
  
  public abstract List<StockCount> findOpenAdhocStockCounts(Long paramLong) throws Exception;
  
  public abstract boolean hasOpenStockCountItems(Long paramLong, Set<String> paramSet) throws Exception;
  
  public abstract List<FutureCountVO> findFutureStockCounts(FutureCountQueryFilter paramFutureCountQueryFilter) throws Exception;
  
  public abstract StockCount createAdhocStockCount(Long paramLong) throws Exception;
  
  public abstract void update(StockCount paramStockCount) throws Exception;
  
  public abstract void delete(Long paramLong) throws Exception;
  
  public abstract void processThirdPartyNonRangedItems(Long paramLong) throws Exception;
  
  public abstract long generateStockCounts(Long paramLong1, Long paramLong2, Date paramDate, ProductGroupType paramProductGroupType) throws Exception;
  
  public abstract StockCount generateFutureStockCount(Long paramLong1, Long paramLong2, Date paramDate, ProductGroupType paramProductGroupType) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\stockcount\StockCountServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */