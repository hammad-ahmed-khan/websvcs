package oracle.retail.sim.common.shelfreplenishment;

import oracle.retail.sim.common.core.SimEnum;

public enum ShelfReplenishmentFromArea implements SimEnum<Integer> {
  BACKROOM(0, "Backroom"),
  DELIVERY_BAY(1, "Delivery Bay");
  
  private final int code;
  
  private final String description;
  
  ShelfReplenishmentFromArea(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ShelfReplenishmentFromArea toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ShelfReplenishmentFromArea shelfReplenishmentFromArea : values()) {
        if (shelfReplenishmentFromArea.code == paramInteger.intValue())
          return shelfReplenishmentFromArea; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentFromArea.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */