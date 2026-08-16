package oracle.retail.sim.common.core;

public enum DeviceType implements SimEnum<Integer> {
  PC(0, "PC"),
  HH(1, "Handheld"),
  SERVER(2, "Server"),
  INT_SERVICE(3, "Integration Service");
  
  private final int code;
  
  private final String description;
  
  DeviceType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static DeviceType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (DeviceType deviceType : values()) {
        if (deviceType.code == paramInteger.intValue())
          return deviceType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\DeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */