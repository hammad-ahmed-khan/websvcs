package oracle.retail.sim.common.business;

import oracle.retail.sim.common.core.SimEnum;

public enum LocationType implements SimEnum<String> {
  STORE("ST"),
  SUPPLIER("SUPP"),
  WAREHOUSE("WH"),
  FINISHER("FI"),
  FULFILLMENT_ORDER("FO");
  
  private final String code;
  
  LocationType(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public static LocationType toValue(String paramString) {
    if (paramString != null)
      for (LocationType locationType : values()) {
        if (locationType.code.equals(paramString))
          return locationType; 
      }  
    throw new IllegalArgumentException("invalid code: " + paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\LocationType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */