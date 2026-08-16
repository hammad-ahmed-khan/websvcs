package oracle.retail.sim.common.activitylock;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class ActivityLock implements Serializable {
  private static final long serialVersionUID = 9038998302872005640L;
  
  private Long id;
  
  private final String sessionId;
  
  private final ActivityLockType activityType;
  
  private final String activityId;
  
  private final DeviceType deviceType;
  
  private final String userName;
  
  private final Date lockDate;
  
  public ActivityLock(ActivityLockType paramActivityLockType, String paramString1, String paramString2, DeviceType paramDeviceType, String paramString3, Date paramDate) {
    this.activityType = paramActivityLockType;
    this.activityId = StringHelper.trimToNull(paramString1);
    this.sessionId = StringHelper.trimToNull(paramString2);
    this.deviceType = paramDeviceType;
    this.userName = StringHelper.trimToNull(paramString3);
    this.lockDate = paramDate;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getSessionId() {
    return this.sessionId;
  }
  
  public ActivityLockType getActivityType() {
    return this.activityType;
  }
  
  public String getActivityId() {
    return this.activityId;
  }
  
  public DeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public Date getLockDate() {
    return this.lockDate;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ActivityLock activityLock = (ActivityLock)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, activityLock.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("ActivityLock[");
    stringBuilder.append("id=").append(this.id).append(",");
    stringBuilder.append("activityType=").append(this.activityType.getCode()).append(",");
    stringBuilder.append("activityId=").append(this.activityId).append(",");
    stringBuilder.append("sessionId=").append(this.sessionId).append(",");
    stringBuilder.append("deviceType=").append(this.deviceType).append(",");
    stringBuilder.append("userName=").append(this.userName).append(",");
    stringBuilder.append("lockDate=").append(this.lockDate);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\activitylock\ActivityLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */