package oracle.retail.sim.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.store.StoreAddressVO;

@Remote
public interface StoreInterface {
  CompressedObject<List<Store>> findAllStores(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<BuddyStore>> findAutoReceiveStores(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<BuddyStore>> findBuddyStores(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Store>> findStoreIdInTransferZone(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Store>> findStoreNamesInTransferZone(CompressedObject<String> paramCompressedObject1, CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<Store>> findStores(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Store>> findStoresInTransferZone(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isValidStoreId(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Store> readStore(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Store>> readStores(CompressedObject<Collection<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<TimeZone> readTimeZone(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StoreAddressVO> selectStoreAddress(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateAutoReceiveStores(CompressedObject<Long> paramCompressedObject, CompressedObject<Collection<BuddyStore>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateBuddyStores(CompressedObject<Long> paramCompressedObject, CompressedObject<Collection<BuddyStore>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateSimStoreFlags(CompressedObject<Collection<Store>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StoreInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */