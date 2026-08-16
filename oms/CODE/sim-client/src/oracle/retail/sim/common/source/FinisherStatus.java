package oracle.retail.sim.common.source;

import oracle.retail.sim.common.core.SimEnum;

public enum FinisherStatus implements SimEnum<String> {
  ACTIVE("A", "Active"),
  INACTIVE("I", "Inactive");
  
  private final String code;
  
  private final String description;
  
  FinisherStatus(String paramString1, String paramString2) {
    this.code = paramString1;
    this.description = paramString2;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FinisherStatus toValue(String paramString) {
    if (paramString != null)
      for (FinisherStatus finisherStatus : values()) {
        if (finisherStatus.code.equals(paramString))
          return finisherStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\FinisherStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */