package oracle.retail.sim.common.store;

import java.io.Serializable;
import oracle.retail.sim.common.business.BusinessObject;

public class BuddyStore extends BusinessObject implements Serializable, Comparable {
  static final long serialVersionUID = -616639984734691297L;
  
  private Long id = null;
  
  private String name = null;
  
  public BuddyStore(Long paramLong, String paramString) {
    this.id = paramLong;
    this.name = paramString;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    BuddyStore buddyStore = (BuddyStore)paramObject;
    return this.id.equals(buddyStore.id);
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
  
  public int compareTo(Object paramObject) {
    BuddyStore buddyStore = (BuddyStore)paramObject;
    return this.id.compareTo(buddyStore.id);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\BuddyStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */