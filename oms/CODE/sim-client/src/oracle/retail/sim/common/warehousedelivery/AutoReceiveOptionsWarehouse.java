package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.core.SimEnum;

public enum AutoReceiveOptionsWarehouse implements SimEnum<Integer> {
  NOT_ALLOWED(0, "Not Allowed"),
  EXTERNAL_MESSAGE(1, "External Message"),
  DATE_DRIVEN(2, "Date Driven");
  
  private final int code;
  
  private final String description;
  
  AutoReceiveOptionsWarehouse(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static AutoReceiveOptionsWarehouse toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (AutoReceiveOptionsWarehouse autoReceiveOptionsWarehouse : values()) {
        if (autoReceiveOptionsWarehouse.code == paramInteger.intValue())
          return autoReceiveOptionsWarehouse; 
      }  
    return null;
  }
  
  public static boolean isNotAllowed(String paramString) {
    return NOT_ALLOWED.toString().equals(paramString);
  }
  
  public static boolean isExternalMessage(String paramString) {
    return EXTERNAL_MESSAGE.toString().equals(paramString);
  }
  
  public static boolean isDateDriven(String paramString) {
    return DATE_DRIVEN.toString().equals(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\AutoReceiveOptionsWarehouse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */