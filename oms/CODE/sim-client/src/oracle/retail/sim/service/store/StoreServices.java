package oracle.retail.sim.service.store;

import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.store.StoreAddressVO;

public abstract class StoreServices {
  public abstract Store readStore(Long paramLong) throws Exception;
  
  public abstract TimeZone readTimeZone(Long paramLong) throws Exception;
  
  public abstract List<Store> readStores(Collection<Long> paramCollection) throws Exception;
  
  public abstract List<Store> findStores(String paramString) throws Exception;
  
  public abstract List<Store> findAllStores() throws Exception;
  
  public abstract List<Store> findStoresInTransferZone(Long paramLong) throws Exception;
  
  public abstract List<Store> findStoreIdInTransferZone(Long paramLong, String paramString) throws Exception;
  
  public abstract List<Store> findStoreNamesInTransferZone(String paramString1, Long paramLong, String paramString2) throws Exception;
  
  public abstract List<BuddyStore> findBuddyStores(Long paramLong) throws Exception;
  
  public abstract List<BuddyStore> findAutoReceiveStores(Long paramLong) throws Exception;
  
  public abstract void updateBuddyStores(Long paramLong, Collection<BuddyStore> paramCollection) throws Exception;
  
  public abstract void updateAutoReceiveStores(Long paramLong, Collection<BuddyStore> paramCollection) throws Exception;
  
  public abstract void updateSimStoreFlags(Collection<Store> paramCollection) throws Exception;
  
  public abstract StoreAddressVO selectStoreAddress(Long paramLong) throws Exception;
  
  public abstract boolean isValidStoreId(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\store\StoreServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */