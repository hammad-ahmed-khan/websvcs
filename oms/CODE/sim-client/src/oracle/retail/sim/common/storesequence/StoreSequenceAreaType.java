package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.core.SimEnum;

public enum StoreSequenceAreaType implements SimEnum<Integer> {
  NO_LOCATION(0, "No Location"),
  SHOPFLOOR(1, "Shopfloor"),
  BACKROOM(2, "Backroom");
  
  private final int code;
  
  private final String description;
  
  StoreSequenceAreaType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static StoreSequenceAreaType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (StoreSequenceAreaType storeSequenceAreaType : values()) {
        if (storeSequenceAreaType.code == paramInteger.intValue())
          return storeSequenceAreaType; 
      }  
    return NO_LOCATION;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceAreaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */