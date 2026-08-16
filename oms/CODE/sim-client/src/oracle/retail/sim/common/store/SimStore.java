package oracle.retail.sim.common.store;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.NativeServiceFactory;
import oracle.retail.sim.service.store.StoreServices;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SimStore extends BusinessObject implements Serializable {
  private static final long serialVersionUID = -1876452519082800049L;
  
  private final Store store;
  
  private SortedSet<BuddyStore> buddyStores = new TreeSet<>();
  
  private SortedSet<BuddyStore> autoReceiveStores = new TreeSet<>();
  
  private boolean storesNotLoaded = true;
  
  public SimStore(Store paramStore) {
    this.store = paramStore;
  }
  
  public Long getId() {
    return this.store.getId();
  }
  
  public String getName() {
    return this.store.getName();
  }
  
  public String getTransferZone() {
    return this.store.getTransferZone();
  }
  
  public Set<BuddyStore> getBuddyStores() {
    doLoadStoreLists();
    return Collections.unmodifiableSortedSet(this.buddyStores);
  }
  
  public void replaceBuddyStores(List<BuddyStore> paramList) throws BusinessException {
    checkForNullParameter("Buddy Stores", paramList);
    executeRule("replaceBuddyStores", new Object[] { paramList });
    doReplaceBuddyStores(paramList);
  }
  
  public void doReplaceBuddyStores(List<BuddyStore> paramList) {
    this.buddyStores.clear();
    this.buddyStores.addAll(paramList);
  }
  
  public void replaceAutoReceiveStores(List<BuddyStore> paramList) throws BusinessException {
    checkForNullParameter("Auto Received Stores", paramList);
    executeRule("replaceAutoReceiveStores", new Object[] { paramList });
    doReplaceAutoReceiveStores(paramList);
  }
  
  public void doReplaceAutoReceiveStores(List<BuddyStore> paramList) {
    this.autoReceiveStores.clear();
    this.autoReceiveStores.addAll(paramList);
  }
  
  public boolean isBuddy(Store paramStore) {
    doLoadStoreLists();
    return this.buddyStores.contains(BOFactory.createBuddyStore(paramStore));
  }
  
  public Set<BuddyStore> getAutoReceiveStores() {
    doLoadStoreLists();
    return Collections.unmodifiableSortedSet(this.autoReceiveStores);
  }
  
  public boolean isAutoReceive(Store paramStore) {
    doLoadStoreLists();
    return this.autoReceiveStores.contains(BOFactory.createBuddyStore(paramStore));
  }
  
  public void doAddBuddyStore(BuddyStore paramBuddyStore) {
    doLoadStoreLists();
    this.buddyStores.add(paramBuddyStore);
  }
  
  public void doAddAutoReceiveStore(BuddyStore paramBuddyStore) {
    doLoadStoreLists();
    this.autoReceiveStores.add(paramBuddyStore);
  }
  
  public void doRemoveBuddyStore(BuddyStore paramBuddyStore) {
    doLoadStoreLists();
    this.buddyStores.remove(paramBuddyStore);
  }
  
  public void doRemoveAutoReceiveStore(BuddyStore paramBuddyStore) {
    doLoadStoreLists();
    this.autoReceiveStores.remove(paramBuddyStore);
  }
  
  private void doLoadStoreLists() {
    if (this.storesNotLoaded)
      try {
        StoreServices storeServices = NativeServiceFactory.getStoreServices();
        this.buddyStores = new TreeSet<>(storeServices.findBuddyStores(this.store.getId()));
        this.autoReceiveStores = new TreeSet<>(storeServices.findAutoReceiveStores(this.store.getId()));
        this.storesNotLoaded = false;
      } catch (Exception exception) {
        LogService.error(this, "SimStore could not load store lists", exception);
      }  
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    SimStore simStore = (SimStore)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(getId(), simStore.getId());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(getId());
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" SimStore[");
    stringBuilder.append("Id: ").append(getId());
    stringBuilder.append(";  Name: ").append(getName());
    stringBuilder.append(";  Transfer Zone: ").append(getTransferZone());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\SimStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */