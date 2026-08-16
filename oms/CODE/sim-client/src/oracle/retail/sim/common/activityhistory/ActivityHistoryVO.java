package oracle.retail.sim.common.activityhistory;

import java.io.Serializable;
import oracle.retail.sim.common.core.DeviceType;

public class ActivityHistoryVO implements Serializable {
  private static final long serialVersionUID = -7236750816009865861L;
  
  private ActivityType type;
  
  private String userName;
  
  private Long storeId;
  
  private DeviceType deviceType;
  
  private String[] attributes;
  
  public ActivityHistoryVO(ActivityType paramActivityType, Long paramLong, String paramString, DeviceType paramDeviceType) {
    doSetActivityType(paramActivityType);
    doSetStoreId(paramLong);
    doSetUserName(paramString);
    doSetDeviceType(paramDeviceType);
  }
  
  public ActivityType getType() {
    return this.type;
  }
  
  public void doSetActivityType(ActivityType paramActivityType) {
    this.type = paramActivityType;
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public void doSetUserName(String paramString) {
    this.userName = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public DeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public void doSetDeviceType(DeviceType paramDeviceType) {
    this.deviceType = paramDeviceType;
  }
  
  public String[] getAttributes() {
    return this.attributes;
  }
  
  public void doSetAttributes(String... paramVarArgs) {
    this.attributes = paramVarArgs;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\activityhistory\ActivityHistoryVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */