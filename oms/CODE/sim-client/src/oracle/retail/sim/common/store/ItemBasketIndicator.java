package oracle.retail.sim.common.store;

import oracle.retail.sim.common.core.SimEnum;

public enum ItemBasketIndicator implements SimEnum<String> {
  AUTOMATIC("Automatic"),
  MANUAL("Manual");
  
  private final String code;
  
  ItemBasketIndicator(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public static ItemBasketIndicator toValue(String paramString) {
    if (paramString != null)
      for (ItemBasketIndicator itemBasketIndicator : values()) {
        if (itemBasketIndicator.code.equals(paramString))
          return itemBasketIndicator; 
      }  
    return null;
  }
  
  public static boolean isAutomatic(String paramString) {
    return AUTOMATIC.code.equals(paramString);
  }
  
  public static boolean isManual(String paramString) {
    return MANUAL.code.equals(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\ItemBasketIndicator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */