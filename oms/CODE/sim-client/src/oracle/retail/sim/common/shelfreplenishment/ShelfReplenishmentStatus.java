package oracle.retail.sim.common.shelfreplenishment;

import oracle.retail.sim.common.core.SimEnum;

public enum ShelfReplenishmentStatus implements SimEnum<Integer> {
  NEW(0, "Pending"),
  IN_PROGRESS(2, "In Progress"),
  COMPLETE(3, "Completed"),
  CANCELED(4, "Canceled"),
  CLOSED(5, "Closed"),
  PENDING_ALTERED(6, "Pending Altered");
  
  private final int code;
  
  private final String description;
  
  ShelfReplenishmentStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ShelfReplenishmentStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ShelfReplenishmentStatus shelfReplenishmentStatus : values()) {
        if (shelfReplenishmentStatus.code == paramInteger.intValue())
          return shelfReplenishmentStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */