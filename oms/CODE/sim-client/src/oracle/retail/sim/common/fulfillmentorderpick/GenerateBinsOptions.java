package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.core.SimEnum;

public enum GenerateBinsOptions implements SimEnum<Integer> {
  SYSTEM(0, "System"),
  MANUAL(1, "Manual");
  
  private final int code;
  
  private final String description;
  
  GenerateBinsOptions(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static GenerateBinsOptions toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (GenerateBinsOptions generateBinsOptions : values()) {
        if (generateBinsOptions.code == paramInteger.intValue())
          return generateBinsOptions; 
      }  
    return null;
  }
  
  public static GenerateBinsOptions toValue(String paramString) {
    if (paramString != null)
      for (GenerateBinsOptions generateBinsOptions : values()) {
        if (generateBinsOptions.description.equals(paramString))
          return generateBinsOptions; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\GenerateBinsOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */