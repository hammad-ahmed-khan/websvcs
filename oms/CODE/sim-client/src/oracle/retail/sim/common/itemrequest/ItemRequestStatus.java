package oracle.retail.sim.common.itemrequest;

import oracle.retail.sim.common.core.SimEnum;

public enum ItemRequestStatus implements SimEnum<Integer> {
  PENDING(1, "Pending"),
  COMPLETED(2, "Completed"),
  CANCELED(4, "Canceled");
  
  private final int code;
  
  private final String description;
  
  ItemRequestStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ItemRequestStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ItemRequestStatus itemRequestStatus : values()) {
        if (itemRequestStatus.code == paramInteger.intValue())
          return itemRequestStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequestStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */