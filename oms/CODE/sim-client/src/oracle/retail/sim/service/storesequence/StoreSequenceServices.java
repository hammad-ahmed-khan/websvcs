package oracle.retail.sim.service.storesequence;

import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;

public abstract class StoreSequenceServices {
  public abstract List<StoreSequenceArea> findStoreSequenceAreas(Long paramLong) throws Exception;
  
  public abstract void updateStoreSequenceAreas(List<StoreSequenceArea> paramList1, List<StoreSequenceArea> paramList2, Long paramLong) throws Exception;
  
  public abstract List<StoreSequenceItem> findStoreSequenceItems(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void updateStoreSequenceItems(List<StoreSequenceItem> paramList1, List<StoreSequenceItem> paramList2, Long paramLong) throws Exception;
  
  public abstract void overwriteStoreSequenceItems(Long paramLong1, List<StoreSequenceItem> paramList, Long paramLong2) throws Exception;
  
  public abstract List<StoreSequenceItem> findNoAreaStoreSequenceItems(StoreSequenceItemQueryFilter paramStoreSequenceItemQueryFilter, Long paramLong) throws Exception;
  
  public abstract List<StoreSequenceItem> findStoreSequenceItems(String paramString, Long paramLong) throws Exception;
  
  public abstract Map<String, String> findPrimaryStoreSequenceAreaDescriptions(List<String> paramList, Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\storesequence\StoreSequenceServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */