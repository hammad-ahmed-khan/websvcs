package oracle.retail.sim.common.integration;

import oracle.retail.sim.common.core.SimEnum;

public enum SimMessageDirection implements SimEnum<String> {
  INBOUND("Inbound"),
  OUTBOUND("Outbound");
  
  private final String code;
  
  SimMessageDirection(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public boolean isInbound() {
    return (this == INBOUND);
  }
  
  public static SimMessageDirection toValue(String paramString) {
    if (paramString != null)
      for (SimMessageDirection simMessageDirection : values()) {
        if (simMessageDirection.code.equals(paramString))
          return simMessageDirection; 
      }  
    return null;
  }
  
  public static SimMessageDirection toValue(boolean paramBoolean) {
    return paramBoolean ? INBOUND : OUTBOUND;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\SimMessageDirection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */