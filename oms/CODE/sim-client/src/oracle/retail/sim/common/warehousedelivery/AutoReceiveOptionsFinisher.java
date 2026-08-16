package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.core.SimEnum;

public enum AutoReceiveOptionsFinisher implements SimEnum<Integer> {
  NOT_ALLOWED(0, "Not Allowed"),
  EXTERNAL_MESSAGE(1, "External Message"),
  DATE_DRIVEN(2, "Date Driven");
  
  private final int code;
  
  private final String description;
  
  AutoReceiveOptionsFinisher(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static AutoReceiveOptionsFinisher toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (AutoReceiveOptionsFinisher autoReceiveOptionsFinisher : values()) {
        if (autoReceiveOptionsFinisher.code == paramInteger.intValue())
          return autoReceiveOptionsFinisher; 
      }  
    return null;
  }
  
  public static AutoReceiveOptionsFinisher toValue(String paramString) {
    if (paramString != null)
      for (AutoReceiveOptionsFinisher autoReceiveOptionsFinisher : values()) {
        if (autoReceiveOptionsFinisher.description.equals(paramString))
          return autoReceiveOptionsFinisher; 
      }  
    return null;
  }
  
  public static boolean isNotAllowed(String paramString) {
    return NOT_ALLOWED.description.equals(paramString);
  }
  
  public static boolean isExternalMessage(String paramString) {
    return EXTERNAL_MESSAGE.description.equals(paramString);
  }
  
  public static boolean isDateDriven(String paramString) {
    return DATE_DRIVEN.description.equals(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\AutoReceiveOptionsFinisher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */