package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;

@Remote
public interface StoreSequenceInterface {
  CompressedObject<List<StoreSequenceItem>> findNoAreaStoreSequenceItems(CompressedObject<StoreSequenceItemQueryFilter> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Map<String, String>> findPrimaryStoreSequenceAreaDescriptions(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StoreSequenceArea>> findStoreSequenceAreas(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StoreSequenceItem>> findStoreSequenceItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<StoreSequenceItem>> findStoreSequenceItems2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> overwriteStoreSequenceItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<List<StoreSequenceItem>> paramCompressedObject, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> updateStoreSequenceAreas(CompressedObject<List<StoreSequenceArea>> paramCompressedObject1, CompressedObject<List<StoreSequenceArea>> paramCompressedObject2, CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> updateStoreSequenceItems(CompressedObject<List<StoreSequenceItem>> paramCompressedObject1, CompressedObject<List<StoreSequenceItem>> paramCompressedObject2, CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StoreSequenceInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */