package oracle.retail.sim.common.shelfreplenishment;

import oracle.retail.sim.common.core.SimEnum;

public enum ShelfReplenishmentType implements SimEnum<Integer> {
  END_OF_DAY(0, "End of Day"),
  WITHIN_DAY(1, "Within Day");
  
  private final int code;
  
  private final String description;
  
  ShelfReplenishmentType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ShelfReplenishmentType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ShelfReplenishmentType shelfReplenishmentType : values()) {
        if (shelfReplenishmentType.code == paramInteger.intValue())
          return shelfReplenishmentType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */