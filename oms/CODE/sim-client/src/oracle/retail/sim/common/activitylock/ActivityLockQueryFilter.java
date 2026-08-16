package oracle.retail.sim.common.activitylock;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class ActivityLockQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -6684716612803969724L;
  
  private ActivityLockType activityType;
  
  private String activityId;
  
  private String sessionId;
  
  private DeviceType deviceType;
  
  private String userName;
  
  private Date fromDate;
  
  private Date toDate;
  
  public ActivityLockType getActivityType() {
    return this.activityType;
  }
  
  public void doSetActivityType(ActivityLockType paramActivityLockType) {
    this.activityType = paramActivityLockType;
  }
  
  public void setActivityType(ActivityLockType paramActivityLockType) throws BusinessException {
    executeRule("setActivityType", new Object[] { paramActivityLockType });
    doSetActivityType(paramActivityLockType);
  }
  
  public String getActivityId() {
    return this.activityId;
  }
  
  public void doSetActivityId(String paramString) {
    this.activityId = StringHelper.trimToNull(paramString);
  }
  
  public void setActivityId(String paramString) throws BusinessException {
    executeRule("setActivityId", new Object[] { paramString });
    doSetActivityId(paramString);
  }
  
  public String getSessionId() {
    return this.sessionId;
  }
  
  public void doSetSessionId(String paramString) {
    this.sessionId = StringHelper.trimToNull(paramString);
  }
  
  public void setSessionId(String paramString) throws BusinessException {
    executeRule("setSessionId", new Object[] { paramString });
    doSetSessionId(paramString);
  }
  
  public DeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public void doSetDeviceType(DeviceType paramDeviceType) {
    this.deviceType = paramDeviceType;
  }
  
  public void setDeviceType(DeviceType paramDeviceType) throws BusinessException {
    executeRule("setDeviceType", new Object[] { paramDeviceType });
    doSetDeviceType(paramDeviceType);
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public void doSetUserName(String paramString) {
    this.userName = StringHelper.trimToNull(paramString);
  }
  
  public void setUserName(String paramString) throws BusinessException {
    executeRule("setUserName", new Object[] { paramString });
    doSetUserName(paramString);
  }
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    DateRangeValidRule.execute(paramDate1, paramDate2);
    executeRule("setDateRange", new Object[] { paramDate1, paramDate2 });
    doSetDateRange(paramDate1, paramDate2);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\activitylock\ActivityLockQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */