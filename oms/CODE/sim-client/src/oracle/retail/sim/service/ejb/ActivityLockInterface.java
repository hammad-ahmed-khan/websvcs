package oracle.retail.sim.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import oracle.retail.sim.common.activitylock.ActivityLock;
import oracle.retail.sim.common.activitylock.ActivityLockQueryFilter;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

@Remote
public interface ActivityLockInterface {
  CompressedObject<Boolean> confirmActivityLock(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> confirmActivityLock2(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> createActivityLock(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Set<String>> findActivityLockOwners(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Set<String>> findActivityLockOwners2(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<Collection<String>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ActivityLock>> findActivityLocks(CompressedObject<ActivityLockQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> overrideActivityLock(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<ActivityLock> readActivityLock(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> releaseActivityLock(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> releaseAllActivityLocks(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> releaseAllActivityLocks2(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<Collection<String>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> releaseAllSessionActivityLocks(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> releaseSessionActivityLock(CompressedObject<ActivityLockType> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ActivityLockInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */