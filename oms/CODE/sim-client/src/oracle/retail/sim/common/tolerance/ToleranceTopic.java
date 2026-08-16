package oracle.retail.sim.common.tolerance;

import oracle.retail.sim.common.core.SimEnum;

public enum ToleranceTopic implements SimEnum<Integer> {
  ADHOC_STOCK_COUNT(0, "Ad Hoc Stock Count"),
  FULFILLMENT_ORDER_PICKING(1, "Customer Order Picking");
  
  private final int code;
  
  private final String description;
  
  ToleranceTopic(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ToleranceTopic toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ToleranceTopic toleranceTopic : values()) {
        if (toleranceTopic.code == paramInteger.intValue())
          return toleranceTopic; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tolerance\ToleranceTopic.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */