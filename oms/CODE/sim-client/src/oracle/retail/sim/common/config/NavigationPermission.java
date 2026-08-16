package oracle.retail.sim.common.config;

import oracle.retail.sim.common.core.SimEnum;

public enum NavigationPermission implements SimEnum<Integer> {
  NONE(1, "None"),
  VIEW(2, "View"),
  FULL(3, "Full");
  
  private final int code;
  
  private final String description;
  
  NavigationPermission(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static NavigationPermission toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (NavigationPermission navigationPermission : values()) {
        if (navigationPermission.code == paramInteger.intValue())
          return navigationPermission; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationPermission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */