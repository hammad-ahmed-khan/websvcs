package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.core.SimEnum;

public enum StoreOrderType implements SimEnum<Integer> {
  PURCHASE_ORDER(0),
  TRANSFER(1);
  
  private final int code;
  
  StoreOrderType(int paramInt1) {
    this.code = paramInt1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public static StoreOrderType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (StoreOrderType storeOrderType : values()) {
        if (storeOrderType.code == paramInteger.intValue())
          return storeOrderType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */