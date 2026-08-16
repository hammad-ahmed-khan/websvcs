package oracle.retail.sim.service.activitylock;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.activitylock.ActivityLock;
import oracle.retail.sim.common.activitylock.ActivityLockQueryFilter;
import oracle.retail.sim.common.activitylock.ActivityLockType;

public abstract class ActivityLockServices {
  public abstract Long createActivityLock(ActivityLockType paramActivityLockType, String paramString, boolean paramBoolean) throws Exception;
  
  public abstract Long overrideActivityLock(ActivityLockType paramActivityLockType, String paramString) throws Exception;
  
  public abstract boolean confirmActivityLock(Long paramLong) throws Exception;
  
  public abstract boolean confirmActivityLock(ActivityLockType paramActivityLockType, String paramString) throws Exception;
  
  public abstract void releaseActivityLock(Long paramLong) throws Exception;
  
  public abstract void releaseSessionActivityLock(ActivityLockType paramActivityLockType, String paramString) throws Exception;
  
  public abstract void releaseAllActivityLocks(ActivityLockType paramActivityLockType, String paramString) throws Exception;
  
  public abstract void releaseAllActivityLocks(ActivityLockType paramActivityLockType, Collection<String> paramCollection) throws Exception;
  
  public abstract void releaseAllSessionActivityLocks() throws Exception;
  
  public abstract ActivityLock readActivityLock(Long paramLong) throws Exception;
  
  public abstract List<ActivityLock> findActivityLocks(ActivityLockQueryFilter paramActivityLockQueryFilter) throws Exception;
  
  public abstract Set<String> findActivityLockOwners(ActivityLockType paramActivityLockType, String paramString) throws Exception;
  
  public abstract Set<String> findActivityLockOwners(ActivityLockType paramActivityLockType, Collection<String> paramCollection) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\activitylock\ActivityLockServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */