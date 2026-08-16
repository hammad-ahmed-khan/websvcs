package oracle.retail.sim.common.lineitem;

import oracle.retail.sim.common.core.SimEnum;

public enum UOMMode implements SimEnum<Integer> {
  STANDARD(1),
  CASES(2),
  PREFERRED(3);
  
  private final int code;
  
  UOMMode(int paramInt1) {
    this.code = paramInt1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public static UOMMode toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (UOMMode uOMMode : values()) {
        if (uOMMode.code == paramInteger.intValue())
          return uOMMode; 
      }  
    return STANDARD;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\lineitem\UOMMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */