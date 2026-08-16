package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;

@Remote
public interface ShelfReplenishmentInterface {
  CompressedObject<?> cancelShelfReplenishment(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ShelfReplenishment> createShelfReplenishment(CompressedObject<ShelfReplenishment> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<String>> findEmployeesIds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ShelfReplenishmentVO>> findShelfReplenishmentVOs(CompressedObject<ShelfReplenishmentQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ShelfReplenishment>> findShelfReplenishments(CompressedObject<ShelfReplenishmentQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ShelfReplenishment> readShelfReplenishment(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ShelfReplenishment> updateShelfReplenishment(CompressedObject<ShelfReplenishment> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ShelfReplenishmentInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */