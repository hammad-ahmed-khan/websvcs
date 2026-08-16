package oracle.retail.sim.common.store;

import oracle.retail.sim.common.core.SimEnum;

public enum MultiSetOfBooksIndicator implements SimEnum<String> {
  ENABLED("Enabled"),
  SIM_ONLY("SIM Only"),
  DISABLED("Disabled");
  
  private final String code;
  
  MultiSetOfBooksIndicator(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public static MultiSetOfBooksIndicator toValue(String paramString) {
    if (paramString != null)
      for (MultiSetOfBooksIndicator multiSetOfBooksIndicator : values()) {
        if (multiSetOfBooksIndicator.code.equals(paramString))
          return multiSetOfBooksIndicator; 
      }  
    return null;
  }
  
  public static boolean isDisabled(String paramString) {
    return DISABLED.code.equals(paramString);
  }
  
  public static boolean isSIMOnly(String paramString) {
    return SIM_ONLY.code.equals(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\MultiSetOfBooksIndicator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */