package oracle.retail.sim.service.shelfreplenishment;

import java.util.List;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;

public abstract class ShelfReplenishmentServices {
  public abstract ShelfReplenishment createShelfReplenishment(ShelfReplenishment paramShelfReplenishment) throws Exception;
  
  public abstract ShelfReplenishment updateShelfReplenishment(ShelfReplenishment paramShelfReplenishment) throws Exception;
  
  public abstract ShelfReplenishment readShelfReplenishment(Long paramLong) throws Exception;
  
  public abstract void cancelShelfReplenishment(Long paramLong) throws Exception;
  
  public abstract List<ShelfReplenishment> findShelfReplenishments(ShelfReplenishmentQueryFilter paramShelfReplenishmentQueryFilter) throws Exception;
  
  public abstract List<ShelfReplenishmentVO> findShelfReplenishmentVOs(ShelfReplenishmentQueryFilter paramShelfReplenishmentQueryFilter) throws Exception;
  
  public abstract List<String> findEmployeesIds(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\shelfreplenishment\ShelfReplenishmentServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */