package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.core.SimEnum;

public enum StoreOrderStatus implements SimEnum<Integer> {
  PENDING(1, "Pending"),
  CLOSED(2, "Closed"),
  APPROVED(3, "Approved");
  
  private final int code;
  
  private final String description;
  
  StoreOrderStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static StoreOrderStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (StoreOrderStatus storeOrderStatus : values()) {
        if (storeOrderStatus.code == paramInteger.intValue())
          return storeOrderStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */