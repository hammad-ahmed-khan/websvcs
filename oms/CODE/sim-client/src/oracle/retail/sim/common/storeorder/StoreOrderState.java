package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.core.SimEnum;

public enum StoreOrderState implements SimEnum<Integer> {
  CREATE(0),
  EDIT(1);
  
  private final int code;
  
  StoreOrderState(int paramInt1) {
    this.code = paramInt1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public static StoreOrderState toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (StoreOrderState storeOrderState : values()) {
        if (storeOrderState.code == paramInteger.intValue())
          return storeOrderState; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */